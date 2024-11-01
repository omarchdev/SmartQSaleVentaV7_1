package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.omarchdev.smartqsale.smartqsaleventas.DialogFragments.DialogAddEditCustomer;
import com.omarchdev.smartqsale.smartqsaleventas.R;
import com.omarchdev.smartqsale.smartqsaleventas.RvAdapter.RvAdapterCustomer;

/**
 * A simple {@link Fragment} subclass.
 */
public class clienteFragment extends Fragment implements View.OnClickListener {

    EditText  edtSearchClient;
    ImageButton btnAddContact,btnViewContact,btnDeleteText;
    RecyclerView rv;
    RvAdapterCustomer rvAdapterCustomer;
    LinearLayoutManager llm;
    DialogAddEditCustomer dialogAddEditCustomer;

    public clienteFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_cliente, container, false);
        dialogAddEditCustomer=new DialogAddEditCustomer().newInstance();

        rv=(RecyclerView)rootView.findViewById(R.id.rvListClients);
        rvAdapterCustomer=new RvAdapterCustomer();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(rvAdapterCustomer);
        edtSearchClient=(EditText)rootView.findViewById(R.id.edtSearchCliente);
        btnViewContact=(ImageButton) rootView.findViewById(R.id.btnViewContact);
        btnAddContact=(ImageButton)rootView.findViewById(R.id.btnAddContact);
        btnDeleteText=(ImageButton)rootView.findViewById(R.id.btnDeleteTextSearchCliente);

        btnDeleteText.setOnClickListener(this);
        btnViewContact.setOnClickListener(this);
        btnAddContact.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnDeleteTextSearchCliente){

        }
        else if(v.getId()==R.id.btnAddContact){

            DialogAddEditCustomer();
        }
    }

    public void borrarTexto(){
    }

    public void DialogAddEditCustomer(){


        DialogFragment dialogFragment=dialogAddEditCustomer;
        dialogFragment.show(((Activity)getContext()).getFragmentManager(),"");

    }


}
