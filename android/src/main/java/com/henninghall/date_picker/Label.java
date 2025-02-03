package com.henninghall.date_picker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class Label extends AppCompatTextView {

    public Label(@NonNull Context context) {
        super(context);
    }

    public Label(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Label(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextColor(String color) {
        setTextColor(Color.parseColor(color));
    }
}

