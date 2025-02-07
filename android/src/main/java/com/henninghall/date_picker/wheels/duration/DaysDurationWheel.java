package com.henninghall.date_picker.wheels.duration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.henninghall.date_picker.Label;
import com.henninghall.date_picker.State;
import com.henninghall.date_picker.pickers.Picker;

/**
 * Need an extra wheel for duration as a day/date wheel doesn't support 0 value and also has only 31 days limit.
 * For the duration, potentially we want to support a full range <0,+∞)
 */
public class DaysDurationWheel extends DurationWheel {

    private final static int defaultMax = 180; // limit for performance purposes
    private final static int defaultMin = 0;

    public DaysDurationWheel(@NonNull Picker picker, @NonNull Label label, State state) {
        super(picker, label, state);
    }

    @Override
    int getMaxValue() {
        if (state.getMaximumDurationS() == null) {
            return defaultMax;
        }
        return secondsToDays(state.getMaximumDurationS());
    }

    @Override
    int getMinValue() {
        if (state.getMinimumDurationS() == null) {
            return defaultMin;
        }
        int propsMin = secondsToDays(state.getMinimumDurationS());
        return Math.max(defaultMin, propsMin); // normalize
    }

    @Nullable
    @Override
    Integer getInitialValue() {
        Integer durationProp = state.getDuration();
        if (durationProp != null) {
            return secondsToDays(durationProp);
        }
        return null;
    }

    @Override
    public void setDuration(int durationS, boolean forceAnimation) {
        int days = secondsToDays(durationS);
        super.setValue(days, forceAnimation);
    }

    private int secondsToDays(int seconds) {
        return seconds / 60 / 60 / 24;
    }
}
