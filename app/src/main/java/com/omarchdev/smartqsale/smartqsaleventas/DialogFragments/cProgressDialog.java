package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by OMAR CHH on 06/01/2018.
 */

public class cProgressDialog {

    ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog(Context context, String mensaje) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(mensaje);
        progressDialog.setCanceledOnTouchOutside(false);

        return progressDialog;
    }

}
