package com.omarchdev.smartqsale.smartqsaleventas.Model;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by OMAR CHH on 05/02/2018.
 */

public class xValueFormatter implements IAxisValueFormatter {

    private String[] mDataA;
    private String[] mDataB;
    private BarChart mChart;

    public xValueFormatter(BarChart mChart, List<String> valoresA, List<String> valoresB) {

        mDataA = new String[valoresA.size()];
        mDataB = new String[valoresB.size()];

        this.mChart = mChart;
        for (int i = 0; i < valoresA.size(); i++) {
            try {
                mDataA[i] = valoresA.get(i);
                mDataB[i] = valoresB.get(i);
            } catch (Exception e) {

                e.toString();

            }
        }

    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int valor = Math.round(value);
        String ValorInicial = mDataA[valor];
        String ValorFinal = mDataB[valor];
        return ValorInicial + "-" + ValorFinal + "h";
    }

}
