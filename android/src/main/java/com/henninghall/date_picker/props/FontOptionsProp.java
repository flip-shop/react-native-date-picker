package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableMap;
import com.henninghall.date_picker.models.FontOptions;

public class FontOptionsProp extends Prop<FontOptions> {
    public static final String name = "fontOptions";

    public static final String FIELD_NAME = "name";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_COLOR = "color";

    @Override
    public FontOptions toValue(Dynamic value){
        ReadableMap map = value.asMap();
        return new FontOptions(
                map.getString(FIELD_NAME),
                map.getDouble(FIELD_SIZE),
                map.getString(FIELD_COLOR)
        );
    }
}