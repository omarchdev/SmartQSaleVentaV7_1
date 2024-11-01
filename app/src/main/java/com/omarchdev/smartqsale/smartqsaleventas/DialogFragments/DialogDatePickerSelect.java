package com.omarchdev.smartqsale.smartqsaleventas.DialogFragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by OMAR CHH on 18/12/2017.
 */

public class DialogDatePickerSelect extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    interfaceFecha fechaListener;
    byte origen;
    int fechaInicio;
    int fechaFinal;
    int year, month, day;
    int diaActual, mesActual, anioActual;

    private void setOrigen(byte origen) {
        this.origen = origen;
    }

    private void setYear(int year) {
        this.year = year;
    }

    private void setMonth(int month) {
        this.month = month;
    }

    private void setDay(int day) {
        this.day = day;
    }


    public DialogDatePickerSelect(){

    }


    public DialogDatePickerSelect newInstance(byte origen, int year, int month, int day){
        DialogDatePickerSelect d=new DialogDatePickerSelect();
        d.setDay(day);
        d.setMonth(month);
        d.setOrigen(origen);
        d.setYear(year);

        return d;

    }

    public void setFechaListener(interfaceFecha fechaListener) {
        this.fechaListener = fechaListener;
    }


    public void setOrigen (byte origen,int year,int month,int day){

        this.origen = origen;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month - 1, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        if(fechaListener!=null)
        fechaListener.getFechaSelecionada(day, month + 1, year, origen);
    }

    public interface interfaceFecha {

        public void getFechaSelecionada(int day, int month, int year, byte origen);

    }

}