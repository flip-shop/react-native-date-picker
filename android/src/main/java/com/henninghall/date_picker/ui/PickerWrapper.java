package com.henninghall.date_picker.ui;

import android.view.View;
import android.widget.LinearLayout;

import com.henninghall.date_picker.Label;
import com.henninghall.date_picker.R;

class PickerWrapper {
    private final LinearLayout view;

    PickerWrapper(View rootView) {
        view = (LinearLayout) rootView.findViewById(R.id.pickerWrapper);
        view.setWillNotDraw(false);
    }

    void addPicker(View wheel) { view.addView(wheel); }

    void addLabel(Label label) {
        view.addView(label);
    }

    void addPicker(View wheel, int index) { view.addView(wheel,index); }

    void removeAll() {
        view.removeAllViews();
    }
}
