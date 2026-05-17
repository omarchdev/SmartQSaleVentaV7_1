package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;

import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by OMAR CHH on 16/02/2018.
 */

public class DialogScannerCam extends DialogFragment implements  ZXingScannerView.ResultHandler
{
    private ZXingScannerView scanner;
    ScannerResult scannerResult;

    public void setScannerResult(ScannerResult scannerResult){

        this.scannerResult=scannerResult;

    }


    public interface ScannerResult{

        public void ResultadoScanner(String resultText);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog;
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        List<BarcodeFormat> barcodeFormatList=new ArrayList<>();
        barcodeFormatList.add(BarcodeFormat.EAN_13);
        barcodeFormatList.add(BarcodeFormat.QR_CODE);
        barcodeFormatList.add(BarcodeFormat.CODABAR);
        scanner=new ZXingScannerView(getActivity());
        scanner.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //scanner.setFormats(new ArrayList<BarcodeFormat>());
        scanner.setActivated(true);
        scanner.setResultHandler(this);
        scanner.startCamera();

        scanner.setAutoFocus(true);
        scanner.setAspectTolerance(0.5f);

        builder.setView(scanner);
        dialog=builder.create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        scanner.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        scannerResult.ResultadoScanner(result.getText().toString());
        getDialog().dismiss();
    }
}
