package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;

public class DurationProp extends Prop<Integer> {
    public static final String name = "duration";

    @Override
    public Integer toValue(Dynamic value){
        return value.asInt();
    }

}