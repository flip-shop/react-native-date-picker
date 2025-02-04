package com.henninghall.date_picker.wheelFunctions;

import com.henninghall.date_picker.wheels.Wheel;
import com.henninghall.date_picker.wheels.duration.DurationWheel;

public class TextColor implements WheelFunction {

    private final String color;

    public TextColor(String color) {
        this.color = color;
    }

    @Override
    public void apply(Wheel wheel) {
        wheel.picker.setTextColor(color);
        if (wheel instanceof DurationWheel durationWheel) {
            durationWheel.label.setTextColor(color);
        }
    }
}


