package com.omarchdev.smartqsale.smartqsaleventas.RvAdapter;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OMAR CHH on 28/12/2017.
 */

public class RvAdapterBluetoothDevice extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<BluetoothDevice> list;
    DeviceListener deviceListener;
    Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public RvAdapterBluetoothDevice() {
        list = new ArrayList<>();
    }

    public void setDeviceListener(DeviceListener deviceListener) {
        this.deviceListener = deviceListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
        return new DeviceSyncViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {

            DeviceSyncViewHolder deviceSyncViewHolder = (DeviceSyncViewHolder) holder;

            int check = ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT);
            int check2 =PackageManager.PERMISSION_GRANTED;


            if (check == check2) {
                deviceSyncViewHolder.txtDevice.setText(list.get(position).getName() + "||" + list.get(position).getAddress());

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }

        }catch (Exception ex){
            Toast.makeText(activity, ex.toString(), Toast.LENGTH_SHORT).show();
            Log.d("blueH",ex.toString());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void Add(List<BluetoothDevice> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface DeviceListener {

        public void getDeviceBluetooth(BluetoothDevice device);
    }

    public class DeviceSyncViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtDevice;
        MaterialCardView ll;

        public DeviceSyncViewHolder(View itemView) {
            super(itemView);
            txtDevice = itemView.findViewById(R.id.txtDeviceBt);
            ll = itemView.findViewById(R.id.llItemDevice);

            ll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.llItemDevice) {

                deviceListener.getDeviceBluetooth(list.get(getAdapterPosition()));
            }

        }
    }
}
