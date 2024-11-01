package com.omarchdev.smartqsale.smartqsaleventas.Controlador;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.omarchdev.smartqsale.smartqsaleventas.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by OMAR CHH on 19/02/2018.
 */

public class RadioGroupedButton extends androidx.appcompat.widget.AppCompatRadioButton {
    private static Map<String, WeakReference<RadioGroupedButton>> buttonMap;
    private OnClickListener externalListener;
    private String groupName;

    static {
        buttonMap = new HashMap<String, WeakReference<RadioGroupedButton>>();
    }

    public RadioGroupedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributes(context, attrs);
        setOnClickListener(internalListener, true);
    }

    private void processAttributes(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.GroupedRadioButton);
        int attributeCount = attributes.getIndexCount();
        for (int i = 0; i < attributeCount; ++i) {
            int attr = attributes.getIndex(i);
            switch (attr) {
                case R.styleable.GroupedRadioButton_group:
                    this.groupName = attributes.getString(attr);
                    break;
            }
        }
        attributes.recycle();
    }

    private OnClickListener internalListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            processButtonClick(view);
        }
    };

    private void setOnClickListener(OnClickListener listener, boolean internal) {
        if (internal)
            super.setOnClickListener(internalListener);
        else
            this.externalListener = listener;
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        setOnClickListener(listener, false);
    }

    private void processButtonClick(View view) {
        if (!(view instanceof RadioGroupedButton))
            return;

        RadioGroupedButton clickedButton = (RadioGroupedButton) view;
        String groupName = clickedButton.groupName;
        WeakReference<RadioGroupedButton> selectedButtonReference = buttonMap.get(groupName);
        RadioGroupedButton selectedButton = selectedButtonReference == null ? null
                : selectedButtonReference.get();
        if (selectedButton != clickedButton) {
            if (selectedButton != null)
                selectedButton.setChecked(false);
            clickedButton.setChecked(true);
            buttonMap.put(groupName, new WeakReference<RadioGroupedButton>(clickedButton));
        }
        if (externalListener != null)
            externalListener.onClick(view);
    }
    public void setGroup(String groupName){
        this.groupName=groupName;
    }

}