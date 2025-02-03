//
//  DatePicker+DataManager.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 11/06/2024.
//

import Foundation

extension DatePicker {
    public func createDataManager() -> DataManager {
        switch pickerMode {
        case .time:
            createTimeModeManager()
        case .date:
            createDateModeManager()
        case .dateAndTime:
            createDateAndTimeModeManager()
        case .duration:
            createDurationModeManager()
        }
    }

    private func createDateModeManager() -> DataManager {
        let months = ComponentInfinityDataSource(data: calendar.monthSymbols, component: .month)
        let days = ComponentInfinityDataSource(data: (1 ... 31).map { "\($0)" }, component: .day)
        let years = ComponentDataSource(data: (1800 ... 2200).map { "\($0)" }, component: .year)

        return DataManager(collections: [months, days, years])
    }

    private func createTimeModeManager() -> DataManager {
        let hours = ComponentInfinityDataSource(data: generateHours(), component: .hour)
        let minutes = ComponentInfinityDataSource(
            data: stride(from: 0, to: 60, by: minuteInterval).map { String(format: "%02d", $0) },
            component: .minute
        )
        let nanoseconds: DataSource? = is24HourFormat()
            ? nil
            : ComponentDataSource(
                data: [calendar.amSymbol, calendar.pmSymbol],
                component: .nanosecond
            )

        return DataManager(collections: [hours, minutes, nanoseconds].compactMap { $0 })
    }

    private func createDateAndTimeModeManager() -> DataManager {
        let days = ComponentDataSource(data: generateAllDaysInYear(), component: .day)
        let hours = ComponentInfinityDataSource(data: generateHours(), component: .hour)
        let minutes = ComponentInfinityDataSource(
            data: stride(from: 0, to: 60, by: minuteInterval).map { String(format: "%02d", $0) },
            component: .minute
        )
        let nanoseconds: DataSource? = is24HourFormat()
            ? nil
            : ComponentDataSource(
                data: [calendar.amSymbol, calendar.pmSymbol],
                component: .nanosecond
            )

        return DataManager(collections: [days, hours, minutes, nanoseconds].compactMap { $0 })
    }

    private func createDurationModeManager() -> DataManager {
        var daysMinValue = 0
        var daysMaxValue = Constants.maxDays
        var hoursMaxValue = Constants.maxHours
        var minutesMaxValue = Constants.maxMinutes
        switch (minDuration, maxDuration) {
        case let (.some(min), .some(max)):
            daysMinValue = TimeUtils.maxDayValueForInterval(min)
            daysMaxValue = TimeUtils.maxDayValueForInterval(max)
            hoursMaxValue = TimeUtils.maxHourValueForInterval(max)
            minutesMaxValue = TimeUtils.maxMinuteValueForInterval(max)
        case let (.none, .some(max)):
            daysMaxValue = TimeUtils.maxDayValueForInterval(max)
            hoursMaxValue = TimeUtils.maxHourValueForInterval(max)
            minutesMaxValue = TimeUtils.maxMinuteValueForInterval(max)
        case let (.some(min), .none):
            daysMinValue = TimeUtils.maxDayValueForInterval(min)
        case (.none, .none):
            break
        }
        let days = ComponentDataSource(data: (daysMinValue ... daysMaxValue).map { "\($0)" }, component: .day)
        let hours = ComponentDataSource(data: (0 ... hoursMaxValue).map { "\($0)" }, component: .hour)
        let minutes = ComponentDataSource(data: (0 ... minutesMaxValue).map { "\($0)" }, component: .minute)

        return DataManager(collections: [days, hours, minutes])
    }

    private func generateAllDaysInYear() -> [String] {
        let year = calendar.component(.year, from: selectedDate)
        let dateComponents = DateComponents(year: year, month: 1, day: 1)
        guard let startDate = calendar.date(from: dateComponents),
              let range = calendar.range(of: .day, in: .year, for: startDate)
        else { return [] }

        let dateFormatter = DateFormatter()
        dateFormatter.locale = locale
        dateFormatter.dateFormat = "EEE MMM d"

        var dates: [String] = []
        for day in range {
            if let date = calendar.date(byAdding: .day, value: day - 1, to: startDate) {
                let formattedDate = dateFormatter.string(from: date)
                dates.append(formattedDate)
            }
        }
        return dates
    }

    private func generateHours() -> [String] {
        var hoursArray: [String] = []
        for value in 0 ..< 24 {
            let hour = is24HourFormat() ? value : (value % 12 == 0 ? 12 : value % 12)
            let hourString = is24HourFormat() ? String(format: "%02d", hour) : "\(hour)"
            hoursArray.append(hourString)
        }
        return hoursArray
    }
}
