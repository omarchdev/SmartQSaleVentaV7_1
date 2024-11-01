package com.omarchdev.smartqsale.smartqsaleventas.Controlador;

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogCargaAsync;
import com.wang.avi.AVLoadingIndicatorView;

public class ControladorProcesoCargar {



    AVLoadingIndicatorView avi;
    RecyclerView rv;
    TextView txt;
    Context context;
    RelativeLayout rl;
    Dialog dialog;
    DialogCargaAsync dialogCargaAsync;

    public ControladorProcesoCargar(Context context){

        this.context=context;
        if(context!=null) {
            dialogCargaAsync = new DialogCargaAsync(context);
        }
    }


    public ControladorProcesoCargar(Context context,AVLoadingIndicatorView avi,TextView txt,RelativeLayout rl,RecyclerView rv){
        this.avi=avi;
        this.txt=txt;
        this.context=context;
        this.rl=rl;
        this.rv=rv;

        if(context!=null){
            dialogCargaAsync=new DialogCargaAsync(context);
        }

    }

    public void setContext(){

        this.context=context;
    }

    public void CargaInicio(){
        if(context!=null){
            if(avi!=null){
                avi.show();
            }
            if(txt!=null){
                txt.setVisibility(View.VISIBLE);
            }
            if(rl!=null){
                rl.setVisibility(View.INVISIBLE);
            }
            if(rv!=null){
                rv.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void CargaFinal(){
        if(context!=null){
            if(avi!=null){
                avi.hide();
            }
            if(txt!=null){
                txt.setVisibility(View.INVISIBLE);
            }
            if(rl!=null){
                rl.setVisibility(View.VISIBLE);
            }
            if(rv!=null){
                rv.setVisibility(View.VISIBLE);
            }
        }

    }

    public void IniciarDialogCarga(String mensaje){
        if(context!=null) {
            dialog = dialogCargaAsync.getDialogCarga(mensaje);
            dialog.show();
        }

    }
    public void FinalizarDialogCarga(){

        if(context!=null) {
            dialog.dismiss();
        }

    }

}
