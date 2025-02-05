//
//  Constants.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 31/01/2025.
//

enum Constants {
    static let columnSpacing: CGFloat = 5
    static let durationRowWidth: CGFloat = 80
    static let secsInDay: Int = 86400
    static let secsInHour: Int = 3600
    static let secsInMinute: Int = 60
    static let maxHours: Int = 23
    static let maxMinutes: Int = 59
    static let maxDays: Int = 365

    // MARK: picker colors

    static let lightColor = "#000000"
    static let darkColor = "#ffffff"

    // MARK: picker state

    static let spinningState = "spinning"
    static let idleState = "idle"

    // MARK: unit labels

    static let unitLabelVerticalPositionAdjustment: CGFloat = UIScreen.main.scale == 2 ? 2 : 1
    static let unitLabelSpacing: CGFloat = 8
    static let unitLabelBaselineOffset: CGFloat = 1
    static let unitDayText = "d"
    static let unitHourText = "h"
    static let unitMinuteText = "m"
    static let labelUnitFont = UIFont.systemFont(ofSize: 17, weight: .semibold)
    static let labelBaselineOffset = CGFloat(1.5).roundedToNearestPixel()
}
