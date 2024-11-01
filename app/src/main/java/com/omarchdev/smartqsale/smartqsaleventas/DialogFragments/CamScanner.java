package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by OMAR CHH on 03/12/2017.
 */

public class CamScanner extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        IntentIntegrator c=new IntentIntegrator(getActivity());


        return super.onCreateDialog(savedInstanceState);
    }
}
