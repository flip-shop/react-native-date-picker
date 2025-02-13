package com.henninghall.date_picker.ui;

import static com.henninghall.date_picker.ui.FontStyleDefaults.getFontColorOrDefault;
import static com.henninghall.date_picker.ui.FontStyleDefaults.getFontOrDefault;
import static com.henninghall.date_picker.ui.FontStyleDefaults.getFontSizeSpOrDefault;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.henninghall.date_picker.R;
import com.henninghall.date_picker.State;
import com.henninghall.date_picker.models.FontOptions;
import com.henninghall.date_picker.models.Mode;
import com.henninghall.date_picker.wheelFunctions.AddOnChangeListener;
import com.henninghall.date_picker.wheelFunctions.AnimateToDate;
import com.henninghall.date_picker.wheelFunctions.FontAndTextColor;
import com.henninghall.date_picker.wheelFunctions.Refresh;
import com.henninghall.date_picker.wheelFunctions.SetDate;
import com.henninghall.date_picker.wheelFunctions.SetDividerColor;
import com.henninghall.date_picker.wheelFunctions.SetDuration;
import com.henninghall.date_picker.wheelFunctions.TextColor;
import com.henninghall.date_picker.wheelFunctions.UpdateVisibility;
import com.henninghall.date_picker.wheels.Wheel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UIManager {
    private final State state;
    private final View rootView;
    private final View backgroundView;
    private Wheels wheels;
    private WheelScroller wheelScroller = new WheelScroller();
    private WheelChangeListenerImpl onWheelChangeListener;

    public UIManager(State state, View rootView){
        this.state = state;
        this.rootView = rootView;
        backgroundView = rootView.findViewById(R.id.pickerBackground);
        wheels = new Wheels(state, rootView);
        addOnChangeListener();
    }

    public void updateWheelVisibility(){
        wheels.applyOnAll(new UpdateVisibility());
    }

    public void updateFontsAndTextColors() {
        if (state.getFontOptions() != null) { // if fontOptions prop available, update font,size and color at once -> ignore textColor standalone prop
            FontOptions options = state.getFontOptions();
            Context context = rootView.getContext();
            Typeface fontType = getFontOrDefault(options.getFontName(), context);
            float fontSize = getFontSizeSpOrDefault(options.getFontSize(), context);
            int fontColor = getFontColorOrDefault(options.getFontColor(), context);
            wheels.applyOnAll(new FontAndTextColor(fontType, fontSize, fontColor));
        } else if (state.getTextColor() != null) { // if fontOptions prop not available, update only textColor prop
            wheels.applyOnAll(new TextColor(state.getTextColor()));
        }
    }

    public void updateWheelOrder() {
        wheels.updateWheelOrder();
    }

    public void updateDisplayValues(){
        wheels.applyOnAll(new Refresh());
    }

    public void setWheelsToDate(){
        wheels.applyOnAll(new SetDate(state.getPickerDate()));
    }

    public void scroll(int wheelIndex, int scrollTimes) {
        Wheel wheel = wheels.getWheel(state.derived.getOrderedVisibleWheels().get(wheelIndex));
        wheelScroller.scroll(wheel, scrollTimes);
    }

    SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(wheels.getFormatPattern(), state.getLocale());
    }

    String getDisplayValueString() {
        return wheels.getDisplayValue();
    }

    void animateToDate(Calendar date) {
        wheels.applyOnInVisible(new SetDate(date));
        wheels.applyOnVisible(new AnimateToDate(date));
    }

    void animateToDuration(int durationS) {
        wheels.applyOnVisible(new SetDuration(durationS, true));
    }

    private void addOnChangeListener(){
        onWheelChangeListener = new WheelChangeListenerImpl(wheels, state, this, rootView);
        wheels.applyOnAll(new AddOnChangeListener(onWheelChangeListener));
    }

    public void addStateListener(SpinnerStateListener listener){
        onWheelChangeListener.addStateListener(listener);
    }

    public void updateLastSelectedDate(Calendar date) {
        state.setLastSelectedDate(date);
    }

    public void updateLastSelectedDuration(int durationS) {
        state.setLastSelectedDuration(durationS);
    }

    public void setDividerColor(String color) {
        wheels.applyOnAll(new SetDividerColor(color));
    }

    public void updateBackgroundVisibility() {
        int visibility = state.getMode() == Mode.duration ? View.VISIBLE : View.GONE;
        backgroundView.setVisibility(visibility);
    }
}
