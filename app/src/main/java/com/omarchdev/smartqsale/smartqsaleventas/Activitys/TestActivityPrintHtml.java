package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.PrinterCommands;
import com.omarchdev.smartqsale.smartqsaleventas.PrintOptions.Utils;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TestActivityPrintHtml extends AppCompatActivity {


    private final static int REQUEST_ENABLE_BT = 1;
    List<String> mArrayAdapter = new ArrayList<>();
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String htmlDocument;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_print_html);


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth

            Toast.makeText(this, "no tiene bluetooth", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "tiene bluetooth", Toast.LENGTH_SHORT).show();

        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView

                if (device.getName().equals("InnerPrinter")) {
                    mmDevice = device;
                    Toast.makeText(this, "Impresora encontrada", Toast.LENGTH_SHORT).show();
                    break;
                }
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mArrayAdapter.size();
            }
        }


        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
// Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy


        final WebView webView;
        String etqIHtml = "<html><body>";
        String eqtNombreTienda = "<h1 align='center'>Nombre Tienda</h1>";
        String etqCliente = "<p>Cliente : Nombre del Cliente</p>";
        String etqFecha = "<p align='center'>25/12/2017</p>";
        String etqProducto = "<p>1 Paneton x1 S/30.00</p>";
        String etqFHtml = "</body></html>";

        String etqITable = "<table align='center'>";
        String etqFTable = "</table>";
        String etqArticulo = "<tr><td style='text-align:right'>1</td><td>x</td><td>Nombre Producto</td><td style='text-align:right'>100.00</td></tr>";
        String etqArticulo1 = "<tr><td style='text-align:right'>11</td><td>x</td><td>Nombre Producto 2</td><td style='text-align:right'>1.00</td></tr>";

        htmlDocument = etqIHtml + eqtNombreTienda + etqFecha + etqCliente + etqITable + etqArticulo + etqArticulo1 + etqArticulo + etqArticulo1 + etqArticulo + etqArticulo1 + etqArticulo + etqArticulo1 + etqArticulo + etqArticulo1 + etqArticulo + etqFTable + etqFHtml;
        webView = (WebView) findViewById(R.id.webView);
        webView.loadDataWithBaseURL(null, htmlDocument, "text/HTML", "UTF-8", null);
        webView.setDrawingCacheEnabled(true);
        webView.buildDrawingCache();

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                int h = 0;
                int w = 0;
                h = view.getHeight();
                w = view.getWidth();
                Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                openBT();

            }
        });
        mWebView = webView;
        int h = 0;
        int w = 0;
        h = webView.getHeight();
        w = webView.getWidth();

        //createWebPrintJob(mWebView);


        openBT();


        printPhoto(R.drawable.ic_account_plus_black_48dp);
        printCustom("Fair Group BD", 2, 1);
        printCustom("Pepperoni Foods Ltd.", 0, 1);
        printCustom("H-123, R-123, Dhanmondi, Dhaka-1212", 0, 1);
        printCustom("Hot Line: +88000 000000", 0, 1);
        printCustom("Vat Reg : 0000000000,Mushak : 11", 0, 1);
        String dateTime[] = getDateTime();
        printText(leftRightAlign(dateTime[0], dateTime[1]));
        printNewLine();
        printText(leftRightAlign("Qty: Name", "Price "));
        printCustom(new String(new char[32]).replace("\0", "."), 0, 1);
        printText(leftRightAlign("Total", "2,0000/="));
        printNewLine();
        printCustom("Thank you for coming & we look", 0, 1);
        printCustom("forward to serve you again", 0, 1);
        printNewLine();
        printNewLine();


        printNewLine();
        printNewLine();
        printNewLine();

    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    void openBT() {

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

    private void beginListenForData() {

        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
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

    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if (bmp != null) {
                byte[] command = Utils.decodeBitmap(bmp);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode

    public void printUnicode() {
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

    private String leftRightAlign(String str1, String str2) {
        String ans = str1 + str2;
        if (ans.length() < 31) {
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printText(String msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printCustom(String msg, int size, int align) {


        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
