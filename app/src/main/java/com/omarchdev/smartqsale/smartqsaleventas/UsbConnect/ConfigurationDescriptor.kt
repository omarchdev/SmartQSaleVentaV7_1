package com.omarchdev.smartqsale.smartqsaleventas.UsbConnect
import android.hardware.usb.UsbDeviceConnection

import java.text.ParseException
import java.util.Locale


data class ConfigurationDescriptor(val interfaceCount: Int, val attributes: Int, val maxPower: Int) {

    val busPowered = (attributes and UsbHelper.ATTR_BUSPOWER) == UsbHelper.ATTR_BUSPOWER
    val selfPowered = (attributes and UsbHelper.ATTR_SELFPOWER) == UsbHelper.ATTR_SELFPOWER
    val remoteWakeup = (attributes and UsbHelper.ATTR_REMOTEWAKE) == UsbHelper.ATTR_REMOTEWAKE

    var interfaces = mutableListOf<InterfaceInfo>()

    /**
     * Structured USB interface representation
     */
    class InterfaceInfo(
        val id: Int,
        val endpointCount: Int,
        val interfaceClass: Int,
        val interfaceSubclass: Int,
        val interfaceProtocol: Int
    ) {
        val endpoints = mutableListOf<EndpointInfo>()

        override fun toString() = String.format(Locale.US, "-- Interface %d "
                + "Class: %s -> Subclass: 0x%02x -> Protocol: 0x%02x, %d Endpoints\n",
            id,
            UsbHelper.nameForClass(interfaceClass),
            interfaceSubclass,
            interfaceProtocol,
            endpointCount)
    }

    /**
     * Structured USB endpoint representation
     */
    class EndpointInfo(
        address: Int,
        attributes: Int,
        val maxPacketSize: Int,
        val interval: Int
    ) {
        // Number is lower 4 bits
        val id: Int = address and 0x0F
        // Direction is high bit
        val direction: Int = address and 0x80
        // Type is the lower two bits
        val type: Int = attributes and 0x3

        override fun toString() = String.format(Locale.US,
            "   -- Endpoint %d, %s %s, %d byte packets\n",
            id,
            UsbHelper.nameForEndpointType(type),
            UsbHelper.nameForDirection(direction),
            maxPacketSize)
    }

    /**
     * Return a printable description of the connected device.
     */
    override fun toString(): String {
        return buildString {
            append("Configuration Descriptor:\n")
                .append(interfaceCount)
                .append(" Interfaces\n")

            append("Attributes:")
            if (busPowered) append(" BusPowered")
            if (selfPowered) append(" SelfPowered")
            if (remoteWakeup) append(" RemoteWakeup")
            append("\n")
            append("Max Power: ")
                .append(maxPower)
                .append("mA\n")

            for (interfaceInfo in interfaces) {
                append(interfaceInfo)
                for (endpointInfo in interfaceInfo.endpoints) {
                    append(endpointInfo)
                }
            }
        }
    }
}

/**
 * Request the active configuration descriptor through the USB device connection.
 */
fun UsbDeviceConnection.readConfigurationDescriptor(): ConfigurationDescriptor {
    // Create a sufficiently large buffer for incoming data
    var buffer = ByteArray(LENGTH)

    controlTransfer(REQUEST_TYPE, REQUEST, REQ_VALUE, REQ_INDEX,
        buffer, LENGTH, TIMEOUT)

    // Do a short read to determine descriptor length
    val totalLength = headerLengthCheck(buffer)
    // Obtain the full descriptor
    buffer = ByteArray(totalLength)
    controlTransfer(REQUEST_TYPE, REQUEST, REQ_VALUE, REQ_INDEX,
        buffer, totalLength, TIMEOUT)

    return parseResponse(buffer)
}

// Type: Indicates whether this is a read or write
//  Matches USB_ENDPOINT_DIR_MASK for either IN or OUT
private const val REQUEST_TYPE = 0x80
// Request: GET_DESCRIPTOR = 0x06
private const val REQUEST = 0x06
// Value: Descriptor Type (High) and Index (Low)
//  Configuration Descriptor = 0x2
//  Descriptor Index = 0x0 (First configuration)
private const val REQ_VALUE = 0x200
private const val REQ_INDEX = 0x00
private const val LENGTH = 9
private const val TIMEOUT = 2000

/**
 * Verify the buffers represents a proper descriptor, and return the length
 */
private fun headerLengthCheck(buffer: ByteArray): Int {
    require(buffer[0] == UsbHelper.DESC_SIZE_CONFIG
            && buffer[1] == UsbHelper.DESC_TYPE_CONFIG) {
        "Invalid configuration descriptor"
    }

    // Parse configuration descriptor header
    var totalLength = buffer[3].toPositiveInt() shl 8
    totalLength += buffer[2].toPositiveInt()

    return totalLength
}

/**
 * Parse the USB configuration descriptor response per the USB Specification.
 *
 * @param buffer Raw configuration descriptor from the device.
 */
private fun parseResponse(buffer: ByteArray): ConfigurationDescriptor {
    val totalLength = headerLengthCheck(buffer)

    // Interface count
    val interfaceCount = buffer[4].toPositiveInt()
    // Configuration attributes
    val attributes = buffer[7].toPositiveInt()
    // Power is given in 2mA increments
    val maxPower = buffer[8].toPositiveInt() * 2

    val descriptor = ConfigurationDescriptor(interfaceCount, attributes, maxPower)

    // The rest of the descriptor is interfaces and endpoints
    var index: Int = UsbHelper.DESC_SIZE_CONFIG.toInt()
    while (index < totalLength) {
        if (index >= buffer.size) {
            throw ParseException("Prematurely reached descriptor end.", index)
        }

        // Read length and type
        val len = buffer[index].toPositiveInt()
        val type = buffer[index + 1]
        when (type) {
            UsbHelper.DESC_TYPE_INTERFACE -> {
                if (len < UsbHelper.DESC_SIZE_INTERFACE) {
                    throw ParseException("Invalid interface descriptor length.", index)
                }

                val intfId = buffer[index + 2].toPositiveInt()
                val intfEndpointCount = buffer[index + 4].toPositiveInt()
                val intfClass = buffer[index + 5].toPositiveInt()
                val intfSubclass = buffer[index + 6].toPositiveInt()
                val intfProtocol = buffer[index + 7].toPositiveInt()

                val nextInterface = ConfigurationDescriptor.InterfaceInfo(intfId, intfEndpointCount,
                    intfClass, intfSubclass, intfProtocol)
                descriptor.interfaces.add(nextInterface)
            }
            UsbHelper.DESC_TYPE_ENDPOINT -> {
                if (len < UsbHelper.DESC_SIZE_ENDPOINT) {
                    throw ParseException("Invalid endpoint descriptor length.", index)
                }

                val endpointAddr = buffer[index + 2].toPositiveInt()
                val endpointAttrs = buffer[index + 3].toPositiveInt()
                var maxPacketSize = buffer[index + 5].toPositiveInt() shl 8
                maxPacketSize += buffer[index + 4].toPositiveInt()

                val epInterval = buffer[index + 6].toPositiveInt()

                val currentInterface = descriptor.interfaces.last()
                currentInterface.endpoints.add(
                    ConfigurationDescriptor.EndpointInfo(endpointAddr, endpointAttrs, maxPacketSize, epInterval))
            }
        }
        // Advance to next descriptor
        index += len
    }

    return descriptor
}