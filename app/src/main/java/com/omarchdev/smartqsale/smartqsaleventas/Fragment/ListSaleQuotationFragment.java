package com.omarchdev.smartqsale.smartqsaleventas.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omarchdev.smartqsale.smartqsaleventas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListSaleQuotationFragment extends Fragment {


    public ListSaleQuotationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_sale_quotation, container, false);
    }

}
