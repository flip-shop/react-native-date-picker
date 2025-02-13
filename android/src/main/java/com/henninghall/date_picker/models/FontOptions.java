package com.henninghall.date_picker.models;

import androidx.annotation.Nullable;

public class FontOptions {

    private final @Nullable String name;
    private final @Nullable Double size;
    private final @Nullable String color;

    public FontOptions(@Nullable String name, @Nullable Double size, @Nullable String color) {
        this.name = name;
        this.size = size;
        this.color = color;
    }

    @Nullable
    public String getFontName() {
        return name;
    }

    @Nullable
    public Double getFontSize() {
        return size;
    }

    @Nullable
    public String getFontColor() {
        return color;
    }
}
