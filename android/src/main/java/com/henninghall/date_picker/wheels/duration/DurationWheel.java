package com.henninghall.date_picker.wheels.duration;

import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.henninghall.date_picker.Label;
import com.henninghall.date_picker.State;
import com.henninghall.date_picker.models.Mode;
import com.henninghall.date_picker.pickers.Picker;
import com.henninghall.date_picker.wheels.Wheel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Need an extra wheel for duration as a day/hour wheel doesn't support 0 value and also has some other limitations.
 * For the duration, potentially we want to support a full range <0,+∞)
 */
public abstract class DurationWheel extends Wheel {

    abstract int getMaxValue();
    abstract int getMinValue();
    abstract public void setDuration(int durationS, boolean forceAnimation);

    @NonNull public Label label;

    private boolean initialized = false;

    public DurationWheel(@NonNull Picker picker,@NonNull Label label, State state) {
        super(picker, state);
        this.label = label;
        setDividerVisibility(false);
    }

    @Override
    protected void init() {
        if (initialized) return; // avoid multiple initializations
        super.init();
        setValue(getMinValue(),false);
        initialized = true;
    }

    @Override
    public ArrayList<String> getValues() {
        int max = getMaxValue();
        return IntStream.rangeClosed(0, max)
                .mapToObj(Integer::toString)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void updateVisibility(){
        int visibility = visible() ? View.VISIBLE: View.GONE;
        picker.setVisibility(visibility);

        int labelVisibility = labelVisible() ? View.VISIBLE : View.GONE;
        label.setVisibility(labelVisibility);
    }

    public void setValue(int value, boolean forceAnimation) {
        int index = values.indexOf(value + "");

        if(index < 0) {
            return;
        }
        // Set value directly during initializing (value==0). After init, always smooth scroll to value
        if(picker.getValue() == 0 && !forceAnimation) {
            picker.setValue(index);
        }
        else {
            picker.smoothScrollToValue(index);
        }
    }

    @Override
    public boolean visible() {
        return state.getMode() == Mode.duration;
    }

    @Override
    public boolean labelVisible() {
        return true;
    }

    @Override
    public boolean wrapSelectorWheel() {
        return false;
    }

    @Override
    public String getFormatPattern() {
        return "";
    }

    @Override
    public Paint.Align getTextAlign() {
        return Paint.Align.RIGHT;
    }
}
