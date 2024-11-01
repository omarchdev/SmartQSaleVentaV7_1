package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.os.Bundle;

import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CamScannerActivity extends ActivityParent  implements  ZXingScannerView.ResultHandler{

    private ZXingScannerView scanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_scanner);

        scanner=new ZXingScannerView(this);
        scanner.setResultHandler(this);
        scanner.startCamera();
        scanner.setAutoFocus(true);
        setContentView( scanner);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void handleResult(Result result) {

    }
}
