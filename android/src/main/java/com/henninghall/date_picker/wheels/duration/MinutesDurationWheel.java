package com.henninghall.date_picker.wheels.duration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.henninghall.date_picker.Label;
import com.henninghall.date_picker.State;
import com.henninghall.date_picker.pickers.Picker;

/**
 * Need an extra wheel for duration as a Minutes wheel has a different max/min logic.
 * For the duration, we want to support a full range <0,59)
 */
public class MinutesDurationWheel extends DurationWheel {

    private final static int defaultMax = 59;
    private final static int defaultMin = 0;

    public MinutesDurationWheel(@NonNull Picker picker, @NonNull Label label, State state) {
        super(picker, label, state);
    }

    @Override
    int getMaxValue() {
        if (state.getMaximumDurationS() == null) {
            return defaultMax;
        }
        int propsMax = secondsToMinutes(state.getMaximumDurationS());
        return Math.min(defaultMax, propsMax); // normalize
    }

    @Override
    int getMinValue() {
        if (state.getMinimumDurationS() == null) {
            return defaultMin;
        }
        int propsMin = secondsToMinutes(state.getMinimumDurationS());
        return Math.max(defaultMin, propsMin); // normalize
    }

    @Override
    public void setDuration(int durationS, boolean forceAnimation) {
        int dayOverflowMinutes = secondsToMinutes(durationS) % 60; // minutes carried-over from previous full hours, if any
        super.setValue(dayOverflowMinutes, forceAnimation);
    }

    private int secondsToMinutes(int seconds) {
        return seconds / 60;
    }
}
