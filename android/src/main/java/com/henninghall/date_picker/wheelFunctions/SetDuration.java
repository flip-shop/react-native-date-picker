package com.henninghall.date_picker.wheelFunctions;

import com.henninghall.date_picker.wheels.Wheel;
import com.henninghall.date_picker.wheels.duration.DurationWheel;

import java.util.Calendar;

public class SetDuration implements WheelFunction {

    private int durationS;
    private boolean forceAnimation;

    public SetDuration(int durationS, boolean forceAnimation) {
        this.durationS = durationS;
        this.forceAnimation = forceAnimation;
    }

    @Override
    public void apply(Wheel wheel) {
        if (wheel instanceof DurationWheel durationWheel) {
            durationWheel.setDuration(durationS, forceAnimation);
        }
    }
}


