package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.database.Cursor;
import android.hardware.usb.UsbDevice;
import android.os.Build;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.Bluetooth.BluetoothConnection;
import com.omarchdev.smartqsale.smartqsaleventas.ConexionBd.DbHelper;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterBluetoothDevice;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvUsbDevice;
import com.omarchdev.smartqsale.smartqsaleventas.UsbConnect.UsbController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 28/12/2017.
 */

public class DialogSelectPrinter extends DialogFragment implements View.OnClickListener, RvAdapterBluetoothDevice.DeviceListener {

    Dialog dialog;
    RadioButton rbNinguno, rbBluetooth, rbPdfPrinter, rbPdfRed, rbUsb;
    RvAdapterBluetoothDevice rvAdapterBluetoothDevice;
    UsbController usbController;
    RecyclerView rv;
    TextView txt;
    EditText edtIP, edtPuerto;
    BluetoothConnection btConnection;
    DbHelper dbHelper;
    String opcionImpresion;
    String comparacion;
    TextView txtSeleccionImpresora;
    String ip;
    String puerto;
    RvUsbDevice rvUsbDevice;

    @Override
    public void dismiss() {
        super.dismiss();
        if (usbController != null)
            usbController.unSubscribeBroadcast();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_seleccionar_impresora, null);
        rbBluetooth = v.findViewById(R.id.rbBluetooth);
        rbNinguno = v.findViewById(R.id.rbNinguno);
        rbPdfPrinter = v.findViewById(R.id.rbPdfPrinter);
        rbPdfRed = v.findViewById(R.id.rbPdfRed);
        rbUsb = v.findViewById(R.id.rbUsb);
        rv = v.findViewById(R.id.rvDriversBT);
        txt = v.findViewById(R.id.txtTextoInformacionBluetooth);
        txtSeleccionImpresora = v.findViewById(R.id.txtSeleccionImpresora);
        btConnection = new BluetoothConnection(getActivity());
        dbHelper = new DbHelper(getActivity());
        edtIP = v.findViewById(R.id.edtIP);
        edtPuerto = v.findViewById(R.id.edtPuerto);
        rvAdapterBluetoothDevice = new RvAdapterBluetoothDevice();
        rv.setAdapter(rvAdapterBluetoothDevice);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAdapterBluetoothDevice.setDeviceListener(this);
        rv.setVisibility(View.GONE);
        rvAdapterBluetoothDevice.setActivity((getActivity()));
        txt.setVisibility(View.GONE);
        edtIP.setVisibility(View.GONE);
        edtPuerto.setVisibility(View.GONE);
        txtSeleccionImpresora.setVisibility(View.GONE);
        rbPdfPrinter.setOnClickListener(this);
        rbNinguno.setOnClickListener(this);
        rbBluetooth.setOnClickListener(this);
        rbUsb.setOnClickListener(this);
        rbPdfRed.setOnClickListener(this);
        rvUsbDevice = new RvUsbDevice();
        rvUsbDevice.setClickPosition(usbDevice -> {
            // dbHelper.DeletePrint();
            try {
                dbHelper.InsertOptionPrint(rbUsb.getText().toString());
                dbHelper.DeleteImpresoraRed();
                dbHelper.InsertImpresoraRed(Integer.toString(usbDevice.getVendorId()),
                        Integer.parseInt(Integer.toString(usbDevice.getProductId())));
                Cursor c = dbHelper.SelectImpresoraRed();
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {

                        Toast.makeText(getActivity(), String.valueOf(c.getString(0)), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), String.valueOf(c.getInt(1)), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No existe ", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("blue",e.toString());
            }

        });
        comparacion = "";
        Cursor c = dbHelper.SelectOptionPrint();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            usbController = new UsbController(getContext());
            usbController.subscribeBroadcast();
            usbController.setUsbDevicesInterface(listDevices -> {
                try {
                    txt.setVisibility(View.GONE);
                    List<UsbDevice> list = new ArrayList<>();
                    for (int i = 0; i < listDevices.size(); i++) {
                        list.add(listDevices.get(i));
                    }
                    if (list.size() > 0) {
                    } else {
                    }
                    rvUsbDevice.agregarItems(list);
                } catch (Exception e) {

                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    Log.d("blueA",e.toString());
                }
            });
        }
        builder.setView(v).setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.DeletePrint();
                if (rbNinguno.isChecked()) {
                    dbHelper.InsertOptionPrint(rbNinguno.getText().toString());
                } else if (rbPdfPrinter.isChecked()) {
                    dbHelper.InsertOptionPrint(rbPdfPrinter.getText().toString());
                } else if (rbBluetooth.isChecked()) {

                    //dbHelper.InsertOptionPrint(rbBluetooth.getText().toString());
                    Toast.makeText(getActivity(), "No selecciono una impresora", Toast.LENGTH_SHORT).show();
                } else if (rbPdfRed.isChecked()) {
                    try {
                        dbHelper.InsertOptionPrint(rbPdfRed.getText().toString());

                        dbHelper.DeleteImpresoraRed();
                        dbHelper.InsertImpresoraRed(edtIP.getText().toString().trim(),
                                Integer.parseInt(edtPuerto.getText().toString().trim()));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        Log.d("blueB",e.toString());
                    }
                }
            }
        }).setTitle("Seleccione una impresora");
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                comparacion = c.getString(0);

                if (comparacion.equals(rbBluetooth.getText().toString())) {
                    rbBluetooth.setChecked(true);
                    rv.setVisibility(View.VISIBLE);
                    rv.setAdapter(rvAdapterBluetoothDevice);
                    txtSeleccionImpresora.setVisibility(View.VISIBLE);
                    VerificarBluetooth();
                    edtIP.setVisibility(View.GONE);
                    edtPuerto.setVisibility(View.GONE);


                } else if (comparacion.equals(rbPdfPrinter.getText().toString())) {
                    rbPdfPrinter.setChecked(true);
                    edtIP.setVisibility(View.GONE);
                    edtPuerto.setVisibility(View.GONE);
                } else if (comparacion.equals(rbNinguno.getText().toString())) {
                    rbNinguno.setChecked(true);
                    edtIP.setVisibility(View.GONE);
                    edtPuerto.setVisibility(View.GONE);
                } else if (comparacion.equals(rbPdfRed.getText().toString())) {
                    rbPdfRed.setChecked(true);
                    edtIP.setVisibility(View.VISIBLE);
                    edtPuerto.setVisibility(View.VISIBLE);
                    VerificarImpresoraRed();
                } else if (comparacion.equals(rbUsb.getText().toString())) {
                    rbUsb.setChecked(true);

                    edtPuerto.setVisibility(View.GONE);
                    edtIP.setVisibility(View.GONE);
                    rv.setAdapter(rvUsbDevice);
                    rv.setVisibility(View.VISIBLE);
                    usbController.printDeviceList();
                }

            }
        } else if (c.getCount() == 0) {
            opcionImpresion = rbNinguno.getText().toString();
            rbNinguno.setChecked(true);
        }

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.rbBluetooth:
                rv.setAdapter(rvAdapterBluetoothDevice);
                VerificarBluetooth();
                txtSeleccionImpresora.setVisibility(View.VISIBLE);
                opcionImpresion = rbBluetooth.getText().toString();
                edtPuerto.setVisibility(View.GONE);
                edtIP.setVisibility(View.GONE);
                break;
            case R.id.rbNinguno:
                txt.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                txtSeleccionImpresora.setVisibility(View.GONE);
                opcionImpresion = rbNinguno.getText().toString();
                edtPuerto.setVisibility(View.GONE);
                edtIP.setVisibility(View.GONE);
                break;
            case R.id.rbPdfPrinter:
                txt.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                txtSeleccionImpresora.setVisibility(View.GONE);
                opcionImpresion = rbPdfPrinter.getText().toString();
                edtPuerto.setVisibility(View.GONE);
                edtIP.setVisibility(View.GONE);
                break;
            case R.id.rbPdfRed:
                txt.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                txtSeleccionImpresora.setVisibility(View.GONE);
                edtPuerto.setVisibility(View.VISIBLE);
                edtIP.setVisibility(View.VISIBLE);
                VerificarImpresoraRed();
                break;
            case R.id.rbUsb:

                try {
                    edtPuerto.setVisibility(View.GONE);
                    edtIP.setVisibility(View.GONE);
                    rv.setAdapter(rvUsbDevice);
                    rv.setVisibility(View.VISIBLE);
                    usbController.printDeviceList();
                    //  rvAdapterBluetoothDevice.Add(new ArrayList<BluetoothDevice>());

                    //txtSeleccionImpresora.setVisibility(View.VISIBLE);
                    // opcionImpresion = rbUsb.getText().toString();


                } catch (Exception e) {
                    Toast.makeText(getActivity(), "select -> " + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("blueC",e.toString());
                }

                break;
        }

    }

    @Override
    public void getDeviceBluetooth(BluetoothDevice device) {
        try{
            dbHelper.DeletePrint();
            dbHelper.DeleteBluetooth();

            dbHelper.InsertDevice(device.getAddress());
            dbHelper.InsertOptionPrint(opcionImpresion);


            Toast.makeText(getActivity(), "Impresora seleccionada: " + device.getName(), Toast.LENGTH_SHORT).show();

            //  Toast.makeText(getActivity(), "Impresora seleccionada: " + device.getName(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }catch (Exception ex){

            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();

        }



    }


    private void VerificarImpresoraRed(){
        try {
            Cursor c = dbHelper.SelectImpresoraRed();
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    ip="";
                    puerto="";
                    ip = c.getString(0);
                    puerto = String.valueOf(c.getInt(1));
                    ip.length();
                    puerto.length();
                    edtIP.setText(ip);
                    edtPuerto.setText(puerto);
                }
            } else {
                Toast.makeText(getActivity(), "No existe ", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
            Log.d("blueF",e.toString());
        }
    }


    private void VerificarBluetooth() {
        try{
            opcionImpresion = rbBluetooth.getText().toString();
            if (btConnection.verifiedDriverSync().size() == 0 || btConnection.VerifiedBt1() == false) {
                txt.setVisibility(View.VISIBLE);

            } else if (btConnection.verifiedDriverSync().size() > 0 && btConnection.VerifiedBt1() == true) {
                txt.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                rvAdapterBluetoothDevice.Add(btConnection.verifiedDriverSync());
            }
        }catch (Exception ex){
            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_LONG).show();
            Log.d("blueG",ex.toString());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()!=null){
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
