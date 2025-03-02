package com.henninghall.date_picker;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Dynamic;
import com.henninghall.date_picker.models.FontOptions;
import com.henninghall.date_picker.models.Is24HourSource;
import com.henninghall.date_picker.models.Mode;
import com.henninghall.date_picker.props.DateProp;
import com.henninghall.date_picker.props.DividerColorProp;
import com.henninghall.date_picker.props.DurationProp;
import com.henninghall.date_picker.props.FontOptionsProp;
import com.henninghall.date_picker.props.HeightProp;
import com.henninghall.date_picker.props.IdProp;
import com.henninghall.date_picker.props.Is24hourSourceProp;
import com.henninghall.date_picker.props.LocaleProp;
import com.henninghall.date_picker.props.MaximumDateProp;
import com.henninghall.date_picker.props.MaximumDurationProp;
import com.henninghall.date_picker.props.MinimumDateProp;
import com.henninghall.date_picker.props.MinimumDurationProp;
import com.henninghall.date_picker.props.MinuteIntervalProp;
import com.henninghall.date_picker.props.ModeProp;
import com.henninghall.date_picker.props.Prop;
import com.henninghall.date_picker.props.TextColorProp;
import com.henninghall.date_picker.props.TimezoneOffsetInMinutesProp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class State {

    private Calendar lastSelectedDate = null;

    private Integer lastSelectedDuration = null; // for duration mode only
    private final DateProp dateProp = new DateProp();
    private final DurationProp durationProp = new DurationProp();
    private final ModeProp modeProp = new ModeProp();
    private final LocaleProp localeProp = new LocaleProp();
    private final TextColorProp textColorProp = new TextColorProp();
    private final MinuteIntervalProp minuteIntervalProp = new MinuteIntervalProp();
    private final MinimumDateProp minimumDateProp = new MinimumDateProp();
    private final MaximumDateProp maximumDateProp = new MaximumDateProp();
    private final TimezoneOffsetInMinutesProp timezoneOffsetInMinutesProp = new TimezoneOffsetInMinutesProp();
    private final HeightProp heightProp = new HeightProp();
    private final Is24hourSourceProp is24hourSourceProp = new Is24hourSourceProp();
    private final IdProp idProp = new IdProp();

    private final DividerColorProp dividerColorProp = new DividerColorProp();
    private final MinimumDurationProp minimumDurationProp = new MinimumDurationProp();
    private final MaximumDurationProp maximumDurationProp = new MaximumDurationProp();
    private final FontOptionsProp fontOptionsProp = new FontOptionsProp();
    private final HashMap props = new HashMap<String, Prop>() {{
        put(DateProp.name, dateProp);
        put(DurationProp.name, durationProp);
        put(ModeProp.name, modeProp);
        put(LocaleProp.name, localeProp);
        put(TextColorProp.name, textColorProp);
        put(MinuteIntervalProp.name, minuteIntervalProp);
        put(MinimumDateProp.name, minimumDateProp);
        put(MaximumDateProp.name, maximumDateProp);
        put(TimezoneOffsetInMinutesProp.name, timezoneOffsetInMinutesProp);
        put(HeightProp.name, heightProp);
        put(Is24hourSourceProp.name, is24hourSourceProp);
        put(IdProp.name, idProp);
        put(DividerColorProp.name, dividerColorProp);
        put(MinimumDurationProp.name, minimumDurationProp);
        put(MaximumDurationProp.name, maximumDurationProp);
        put(FontOptionsProp.name, fontOptionsProp);
    }};
    public DerivedData derived;

    public State() {
        derived = new DerivedData(this);
    }

    private Prop getProp(String name) {
        return (Prop) props.get(name);
    }

    void setProp(String propName, Dynamic value) {
        getProp(propName).setValue(value);
    }

    public Mode getMode() {
        return (Mode) modeProp.getValue();
    }

    public String getTextColor() {
        return (String) textColorProp.getValue();
    }

    public int getMinuteInterval() {
        return (int) minuteIntervalProp.getValue();
    }

    public Locale getLocale() {
        return (Locale) localeProp.getValue();
    }

    public Calendar getMinimumDate() {
        return boundaryPropToCal(minimumDateProp);
    }

    public Calendar getMaximumDate() {
        return boundaryPropToCal(maximumDateProp);
    }

    private Calendar boundaryPropToCal(Prop<String> prop){
        Calendar cal = Utils.isoToCalendar(prop.getValue(), getTimeZone());
        clearSecondsAndMilliseconds(cal);
        return cal;
    }

    private void clearSecondsAndMilliseconds(Calendar cal) {
        if (cal == null) return;
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public TimeZone getTimeZone() {
        try{
            String offsetString = timezoneOffsetInMinutesProp.getValue();
            if(offsetString == null || offsetString.equals("")) return TimeZone.getDefault();
            int offset = Integer.parseInt(offsetString);
            int totalOffsetMinutes = Math.abs(offset);
            char offsetDirection = offset < 0 ? '-' : '+';
            int offsetHours = (int) Math.floor(totalOffsetMinutes / 60f);
            int offsetMinutes = totalOffsetMinutes - offsetHours * 60;
            String timeZoneId = "GMT" + offsetDirection + offsetHours + ":" + Utils.toPaddedMinutes(offsetMinutes);
            TimeZone zone = TimeZone.getTimeZone(timeZoneId);
            return zone;
        } catch (Exception e){
            e.printStackTrace();
            return TimeZone.getDefault();
        }
    }

    public String getIsoDate() {
        return (String) dateProp.getValue();
    }

    public Integer getDuration() {
        Integer duration = durationProp.getValue();
        return (duration != null) ? Math.max(0, duration) : null; // ignore negative values
    }

    private Calendar getDate() {
        Calendar calendar = Utils.isoToCalendar(getIsoDate(), getTimeZone());
        if (calendar == null) { // default fallback to minDate prop
            calendar = getMinimumDate();
        }
        return calendar;
    }

    // The date the picker is suppose to display.
    // Includes minute rounding to desired minute interval.
    public Calendar getPickerDate() {
        Calendar cal = getDate();
        int minuteInterval = getMinuteInterval();
        if(minuteInterval <= 1) return cal;
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm", getLocale());
        int exactMinute = Integer.parseInt(minuteFormat.format(cal.getTime()));
        int minutesSinceLastInterval = exactMinute % minuteInterval;
        cal.add(Calendar.MINUTE, -minutesSinceLastInterval);
        return (Calendar) cal.clone();
    }

    public String getLocaleLanguageTag() {
        return localeProp.getLanguageTag();
    }

    public String getId() {
        return idProp.getValue();
    }

    public Is24HourSource getIs24HourSource() {
        return is24hourSourceProp.getValue();
    }

    public Calendar getLastSelectedDate() {
        return lastSelectedDate;
    }

    public void setLastSelectedDate(Calendar date) {
        lastSelectedDate = date;
    }

    @Nullable public Integer getLastSelectedDuration() {
        return lastSelectedDuration;
    }

    public void setLastSelectedDuration(int lastSelectedDuration) {
        this.lastSelectedDuration = lastSelectedDuration;
    }

    public String getDividerColor() {
        return dividerColorProp.getValue();
    }

    public Integer getMinimumDurationS() {
        Integer min = minimumDurationProp.getValue();
        return (min != null) ? Math.max(0, min) : null; // ignore negative values
    }
    public Integer getMaximumDurationS() {
        Integer max = maximumDurationProp.getValue();
        return (max != null) ? Math.max(0, max) : null; // ignore negative values
    }

    public FontOptions getFontOptions() {
        return fontOptionsProp.getValue();
    }
}