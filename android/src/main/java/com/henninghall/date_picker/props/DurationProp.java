package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;

public class DurationProp extends Prop<String> {
    public static final String name = "duration";

    @Override
    public String toValue(Dynamic value){
        return value.asString();
    }

}