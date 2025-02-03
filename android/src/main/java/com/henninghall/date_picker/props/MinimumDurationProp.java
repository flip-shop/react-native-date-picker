package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;

public class MinimumDurationProp extends Prop<Integer> {
    public static final String name = "minimumDuration";

    @Override
    public Integer toValue(Dynamic value){
        return value.asInt();
    }
}
