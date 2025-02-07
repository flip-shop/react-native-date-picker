package com.henninghall.date_picker.wheels.duration;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.henninghall.date_picker.Label;
import com.henninghall.date_picker.State;
import com.henninghall.date_picker.pickers.Picker;

/**
 * Need an extra wheel for duration as a Hour wheel doesn't support 0 value and also has different max/min logic.
 * For the duration, we want to support a full range <0,24)
 */
public class HoursDurationWheel extends DurationWheel {

    private final static int defaultMax = 23;
    private final static int defaultMin = 0;

    public HoursDurationWheel(@NonNull Picker picker, @NonNull Label label, State state) {
        super(picker, label, state);
    }

    @Override
    int getMaxValue() {
        if (state.getMaximumDurationS() == null) {
            return defaultMax;
        }

        int propsMax = secondsToHours(state.getMaximumDurationS());
        return Math.min(defaultMax, propsMax); // normalize
    }

    @Override
    int getMinValue() {
        if (state.getMinimumDurationS() == null) {
            return defaultMin;
        }
        int propsMin = secondsToHours(state.getMinimumDurationS());
        return Math.max(defaultMin, propsMin); // normalize
    }

    @Nullable
    @Override
    Integer getInitialValue() {
        String durationProp = state.getDuration();
        if (durationProp != null) {
            return secondsToHours(Integer.parseInt(durationProp)) % 24; // hours carried-over from previous full days, if any
        }
        return null;
    }

    @Override
    public void setDuration(int durationS, boolean forceAnimation) {
        int dayOverflowHours = secondsToHours(durationS) % 24; // hours carried-over from previous full days, if any
        super.setValue(dayOverflowHours, forceAnimation);
    }

    private int secondsToHours(int seconds) {
        return seconds / 60 / 60;
    }
}
