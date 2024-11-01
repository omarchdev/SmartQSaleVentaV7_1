package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/**
 * Created by OMAR CHH on 25/02/2018.
 */

public class SnackBarsPer {
    Snackbar sb;
    Context context;
    View sbView;

    public SnackBarsPer(View v,Context context){
        sb=Snackbar.make(v,"",Snackbar.LENGTH_SHORT);
        sbView=sb.getView();


    }
    public void MostrarSnackBar(String text){

        sb.setText(text);
        sb.show();
    }
    public void setDuration(int duration){
        sb.setDuration(duration);
    }

    public void setColor(int color){
        sbView.setBackgroundColor(color);
    }


}
