package com.henninghall.date_picker.wheels;

import android.graphics.Paint;

import com.henninghall.date_picker.Label;
import com.henninghall.date_picker.LocaleUtils;
import com.henninghall.date_picker.State;
import com.henninghall.date_picker.Utils;
import com.henninghall.date_picker.models.Mode;
import com.henninghall.date_picker.pickers.Picker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kotlin.ranges.IntRange;

/**
 * Need an extra wheel for duration as a day/date wheel doesn't support 0 value and also has only 31 days limit.
 * For the duration, potentially we want to support a full range <0,+∞)
 */
public class DaysDurationWheel extends Wheel {

    private final static int defaultMax = 99; // limit for performance purposes
    private final static int defaultMin = 0;

    public DaysDurationWheel(Picker picker, Label label, State state) {
        super(picker, label, state);
    }

    @Override
    public ArrayList<String> getValues() {
        return IntStream.range(defaultMin, defaultMax)
                .mapToObj(Integer::toString)
                .collect(Collectors.toCollection(ArrayList::new));
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
