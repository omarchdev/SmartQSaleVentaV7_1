package com.omarchdev.smartqsale.smartqsaleventas.Activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;

import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class ChartActivity extends ActivityParent implements OnChartValueSelectedListener, SeekBar.OnSeekBarChangeListener {

    private BarChart mChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mChart = (BarChart) findViewById(R.id.barChart);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(false);
        mChart.setDrawValueAboveBar(true);
        setData(2);


    }

    private void setData(int count) {

        ArrayList<BarEntry> yVals = new ArrayList<>();

        String[] lista = {"valor 1", "valor 2"};
        ArrayList<String> list = new ArrayList<>();
        list.add("Valor 1");
        list.add("Valor 2");
        for (int i = 0; i < count; i++) {

            float value = (float) (Math.random() * 100);
            yVals.add(new BarEntry(i, (int) value));


        }


        BarDataSet set = new BarDataSet(yVals, null);
        set.setColor(Color.parseColor("#279DAB"));
        set.setStackLabels(lista);

        BarData data = new BarData(set);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setData(data);
        mChart.invalidate();
        mChart.animateY(1500);

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
