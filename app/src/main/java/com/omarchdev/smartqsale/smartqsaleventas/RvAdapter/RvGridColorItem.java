package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 18/02/2018.
 */

public class RvGridColorItem extends BaseAdapter {

    List<String> listColor;
    ImageView img;
    ImageView item;
    public RvGridColorItem(){
        listColor=new ArrayList<>();
        listColor.add("#FF0000");
        listColor.add("#FF7A09");
        listColor.add("#3E0CE8");
        listColor.add("#10FF0F");
        listColor.add("#E85DC0");
        listColor.add("#E8DF1B");
        listColor.add("#999797");
        listColor.add("#999797");
    }

    @Override
    public int getCount() {
        return listColor.size();
    }

    @Override
    public String getItem(int position) {
        return listColor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_color, parent, false);
        }


        return convertView;
    }
}
