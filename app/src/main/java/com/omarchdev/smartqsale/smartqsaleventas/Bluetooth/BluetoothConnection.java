package com.omarchdev.smartqsale.smartqsaleventas.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by OMAR CHH on 28/12/2017.
 */

public class BluetoothConnection {

    private final static int REQUEST_ENABLE_BT = 1;
    public static BluetoothConnection bluetoothConnection;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    BluetoothAdapter mBluetoothAdapter;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    Context context;

    public BluetoothConnection(Context context) {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    public static BluetoothConnection getSinglentonInstance(Context context) {

        if (bluetoothConnection == null) {

            bluetoothConnection = new BluetoothConnection(context);
        }
        return bluetoothConnection;
    }

    @Override
    protected BluetoothConnection clone() {

        try {
            throw new CloneNotSupportedException();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean VerifiedBt1() {

        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        return true;
    }

    public List<BluetoothDevice> verifiedDriverSync() {
        List<BluetoothDevice> list = new ArrayList<>();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice device : pairedDevices) {

                list.add(device);

            }

        } else if (pairedDevices.size() == 0) {

            list.clear();
        }
        return list;
    }

    public void setDeviceSelect(BluetoothDevice device) {
        this.mmDevice = device;
    }

    public BluetoothDevice getDevice() {
        return mmDevice;
    }

    public void selectDevice(String Address) {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {

            for (BluetoothDevice device : pairedDevices) {

                if (device.getAddress().equals(Address)) {

                    mmDevice = device;

                }

            }

        }

    }


    public void   openBT() {

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

            }
            mmSocket.connect();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                mmOutputStream = mmSocket.getOutputStream();
            }
            mmInputStream = mmSocket.getInputStream();
            beginListenForData();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isConnect(){

        return mmSocket.isConnected();

    }

    public OutputStream getOutputStream() {

        return mmOutputStream;
    }

    public InputStream getMmInputStream() {
        return mmInputStream;
    }

    private void beginListenForData() {
        try {
            final Handler handler = new Handler();
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                // myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
