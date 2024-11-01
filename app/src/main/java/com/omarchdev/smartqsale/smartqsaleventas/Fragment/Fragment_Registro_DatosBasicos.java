package com.omarchdev.smartqsale.smartqsaleventas.Fragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * Created by OMAR CHH on 15/02/2018.
 */

public class Fragment_Registro_DatosBasicos extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_inventario,container,false);
        return rootView;
    }
}
