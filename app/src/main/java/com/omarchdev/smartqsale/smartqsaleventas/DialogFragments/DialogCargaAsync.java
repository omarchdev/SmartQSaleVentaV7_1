package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by OMAR CHH on 21/01/2018.
 */

public class DialogCargaAsync {

    ProgressDialog progressDialog;
    Context context;

    public DialogCargaAsync(Context context) {

        this.context = context;
    }

    public ProgressDialog getDialogCarga(String mensaje) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(mensaje);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

    public void hide(){
        progressDialog.hide();
    }
}
