package com.henninghall.date_picker.wheelFunctions;

import android.graphics.Typeface;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import com.henninghall.date_picker.wheels.Wheel;
import com.henninghall.date_picker.wheels.duration.DurationWheel;

public class FontAndTextColor implements WheelFunction {

    private final @NonNull Typeface fontType;
    private final @FloatRange(from = 0.0, fromInclusive = false) float fontSize;
    private final @ColorInt int fontColor;

    public FontAndTextColor(@NonNull Typeface fontType, @FloatRange(from = 0.0, fromInclusive = false) float fontSize, @ColorInt int fontColor) {
        this.fontType = fontType;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
    }

    @Override
    public void apply(Wheel wheel) {
        wheel.picker.setTextStyle(fontType, fontSize, fontColor);
        if (wheel instanceof DurationWheel durationWheel) {
            durationWheel.label.setTextStyle(fontType, fontSize, fontColor);
        }
    }
}


