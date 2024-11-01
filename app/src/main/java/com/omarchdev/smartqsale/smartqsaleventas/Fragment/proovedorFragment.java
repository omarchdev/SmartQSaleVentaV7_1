package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAddEditProvider;
import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class proovedorFragment extends Fragment implements View.OnClickListener ,TextWatcher{

    EditText edtSearchProvider;
    ImageButton imgAddContactProvider,imgAddProvider;
    DialogAddEditProvider dialogAddEditProvider;

    public proovedorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_proovedor, container, false);
        dialogAddEditProvider=new DialogAddEditProvider();
        edtSearchProvider=(EditText)v.findViewById(R.id.edtSearchProveedor);
        imgAddContactProvider=(ImageButton)v.findViewById(R.id.btnViewProveedor);
        imgAddProvider=(ImageButton)v.findViewById(R.id.btnAddProveedor);




        AsignarListener();


        return v;
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnAddProveedor){

            ShowDialogAddEditProvider();

        }

    }
    public void AsignarListener(){

        imgAddProvider.setOnClickListener(this);
        imgAddContactProvider.setOnClickListener(this);
        edtSearchProvider.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void ShowDialogAddEditProvider(){

        DialogFragment dialog=dialogAddEditProvider;
        dialog.show(((Activity)getContext()).getFragmentManager(),null);

    }
}
