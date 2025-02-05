//
//  TimeUtils.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 03/02/2025.
//

enum TimeUtils {
    static func maxDayValueForInterval(_ interval: Int) -> Int {
        interval / Constants.secsInDay
    }

    static func maxHourValueForInterval(_ interval: Int) -> Int {
        min(Constants.maxHours, interval / Constants.secsInHour)
    }

    static func maxMinuteValueForInterval(_ interval: Int) -> Int {
        min(Constants.maxMinutes, interval / Constants.secsInMinute)
    }
}
