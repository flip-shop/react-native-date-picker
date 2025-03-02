package com.henninghall.date_picker.wheels;

import android.graphics.Paint;
import android.view.View;

import com.henninghall.date_picker.State;
import com.henninghall.date_picker.pickers.Picker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public abstract class Wheel {

    protected final State state;
    private Calendar userSetValue;

    public abstract boolean visible();
    public abstract boolean labelVisible();
    public abstract boolean wrapSelectorWheel();
    public abstract Paint.Align getTextAlign();
    public abstract String getFormatPattern();
    public abstract ArrayList<String> getValues();

    public String toDisplayValue(String value) {
        return value;
    }

    protected ArrayList<String> values = new ArrayList<>();
    public Picker picker;
    public SimpleDateFormat format;

    public Wheel(Picker picker, State state, int wheelItemCount) {
        this.state = state;
        this.picker = picker;
        this.format = new SimpleDateFormat(getFormatPattern(), state.getLocale());
        picker.setTextAlign(getTextAlign());
        picker.setWrapSelectorWheel(wrapSelectorWheel());
        updateSelectorWheelItemCount(wheelItemCount);
    }

    public Wheel(Picker picker, State state) {
        this(picker, state, 3);
    }

    private int getIndexOfDate(Calendar date){
        format.setTimeZone(state.getTimeZone());
        return values.indexOf(format.format(date.getTime()));
    }

    public void animateToDate(Calendar date) {
        picker.smoothScrollToValue(getIndexOfDate(date));
    }

    public String getValue() {
        if(!visible()) return format.format(userSetValue.getTime());
        return getValueAtIndex(getIndex());
    }

    public String getPastValue(int subtractIndex) {
        if(!visible()) return format.format(userSetValue.getTime());
        int size = values.size();
        int pastValueIndex = (getIndex() + size - subtractIndex) % size;
        return getValueAtIndex(pastValueIndex);
    }


    private int getIndex() {
        return picker.getValue();
    }

    public String getValueAtIndex(int index) {
        return values.get(index);
    }

    public void setValue(Calendar date) {
        format.setTimeZone(state.getTimeZone());
        this.userSetValue = date;
        int index = getIndexOfDate(date);

        if(index > -1) {
            // Set value directly during initializing. After init, always smooth scroll to value
            if(picker.getValue() == 0) picker.setValue(index);
            else picker.smoothScrollToValue(index);
        }
    }

    public void refresh() {
        this.format = new SimpleDateFormat(getFormatPattern(), state.getLocale());
        if (!this.visible()) return;
        init();
    }

    public String getDisplayValue(){
        return toDisplayValue(getValueAtIndex(getIndex()));
    }

    private String[] getDisplayValues(ArrayList<String> values){
        ArrayList<String> displayValues = new ArrayList<>();
        for (String value: values) {
            displayValues.add(this.toDisplayValue(value));
        }
        return displayValues.toArray(new String[0]);
    }

    protected void init(){
        picker.setMinValue(0);
        picker.setMaxValue(0);
        values = getValues();
        picker.setDisplayedValues(getDisplayValues(values));
        picker.setMaxValue(values.size() -1);
    }

    public void updateVisibility(){
        int visibility = visible() ? View.VISIBLE: View.GONE;
        picker.setVisibility(visibility);
    }

    private SimpleDateFormat getFormat(Locale locale) {
        return new SimpleDateFormat(this.getFormatPattern(), locale);
    }

    String getLocaleString(Calendar cal) {
        return getString(cal, this.state.getLocale());
    }

    private String getString(Calendar cal, Locale locale){
        return getFormat(locale).format(cal.getTime());
    }

    public void setDividerColor(String color) {
        picker.setDividerColor(color);
    }

    public void setDividerVisibility(boolean visible) {
        picker.setDividerVisibility(visible);
    }

    public void updateSelectorWheelItemCount(int count) {
        picker.updateSelectorWheelItemCount(count);
    }
}
