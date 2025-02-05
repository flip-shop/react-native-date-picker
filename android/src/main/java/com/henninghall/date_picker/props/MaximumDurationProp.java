package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;

public class MaximumDurationProp extends Prop<Integer> {
    public static final String name = "maximumDuration";

    @Override
    public Integer toValue(Dynamic value){
        return value.asInt();
    }
}
