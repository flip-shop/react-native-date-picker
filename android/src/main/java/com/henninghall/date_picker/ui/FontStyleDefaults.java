package com.henninghall.date_picker.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FontStyleDefaults {
    private static final String TAG = "DatePicker.FontStyleDefaults";
    private static final String DEFAULT_ASSETS_FONT_PATH = "fonts/";
    private static final Typeface DEFAULT_TYPEFACE = Typeface.DEFAULT;
    private static final float DEFAULT_FONT_SIZE_SP = 20;
    private static final @ColorInt int DEFAULT_FONT_COLOR = Color.WHITE;

    public static @NonNull Typeface getFontOrDefault(@Nullable String name, @NonNull Context context) {
        Typeface font = DEFAULT_TYPEFACE;
        if (name == null) {
            return font;
        }
        try {
            if (!name.endsWith(".ttf")) {
                name+=".ttf";
            }
            AssetManager assetManager = context.getAssets();
            font = Typeface.createFromAsset(assetManager, DEFAULT_ASSETS_FONT_PATH + name);
        } catch (Exception e) {
            Log.e(TAG, "Cannot find font " + name + " in " + DEFAULT_ASSETS_FONT_PATH + "assets. Using default.");
        }
        return font;
    }

    public static float getFontSizeSpOrDefault(@Nullable Double sizeSp, @NonNull Context context) {
        float fontSize = sizeSp != null ? sizeSp.floatValue() : DEFAULT_FONT_SIZE_SP;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, fontSize, context.getResources().getDisplayMetrics());
    }

    public static @ColorInt int getFontColorOrDefault(@Nullable String color, @NonNull Context context) {
        @ColorInt int colorInt = DEFAULT_FONT_COLOR;
        if (color == null) {
            return colorInt;
        }
        try {
            colorInt = Color.parseColor(color);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Cannot parse color " + color + ". Using default.");
        }

        return colorInt;
    }
}
