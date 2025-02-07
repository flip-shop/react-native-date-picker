//
//  DatePicker+UIPickerViewDelegate.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 07/06/2024.
//

import UIKit

extension DatePicker: UIPickerViewDelegate {
    public func pickerView(_ pickerView: UIPickerView, widthForComponent component: Int) -> CGFloat {
        guard let component = dataManager.collections[safe: component]?.component else { return 0 }
        switch component {
        case _ where pickerMode == .duration:
            return Constants.durationRowWidth
        case .year:
            return pickerView.bounds.size.width * 0.25
        case .month, .day where pickerMode == .dateAndTime:
            return pickerView.bounds.size.width * 0.35
        case .hour, .minute, .nanosecond, .day:
            return pickerView.bounds.size.width * 0.1
        default:
            return 0
        }
    }

    public func pickerView(
        _ pickerView: UIPickerView,
        viewForRow row: Int,
        forComponent component: Int,
        reusing view: UIView?
    )
        -> UIView
    {
        let label: UILabel
        if let reusedLabel = view as? UILabel {
            label = reusedLabel
        } else {
            label = UILabel()
            let rowSize = pickerView.rowSize(forComponent: component)
            label.frame = CGRect(x: 0, y: 0, width: rowSize.width, height: rowSize.height)
            label.textAlignment = .center
            label.font = UIFont.systemFont(ofSize: 20)
            label.backgroundColor = .clear
        }
        label.text = dataManager.getValueInComponentForRow(component: component, row: row)

        if !isPickerScrolling, isScrolling() { isPickerScrolling = true }

        return label
    }

    public func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if isPickerScrolling { isPickerScrolling = false }
        guard pickerMode != .duration else {
            handleDurationSelection()
            return
        }
        var newComponents = DateComponents()
        dataManager.collections.enumerated().map { index, collection in
            var value: Int? = nil
            switch collection.component {
            case .year:
                value = Int(collection.getValueForRow(selectedRow(inComponent: index)) ?? "")
            case .day, .month:
                value = collection.getOriginalRow(selectedRow(inComponent: index)) + 1
            case .hour where !is24HourFormat():
                let amPmComponentIndex = dataManager.componentIndex(component: .nanosecond) ?? 0
                let amPmValue = selectedRow(inComponent: amPmComponentIndex)
                if let hour = collection.getValueForRow(selectedRow(inComponent: index)), let valueHour = Int(hour) {
                    value = valueHour == 12 ? 12 * amPmValue : valueHour + 12 * amPmValue
                }
            case .hour, .minute:
                value = collection.getOriginalRow(selectedRow(inComponent: index))
            default: break
            }
            if let value {
                newComponents.setValue(value, for: collection.component)
            }
        }

        let oldComponents = calendar.dateComponents([.day, .year, .month], from: selectedDate)
        switch pickerMode {
        case .time:
            newComponents.day = oldComponents.day
            newComponents.year = oldComponents.year
            newComponents.month = oldComponents.month
        case .dateAndTime:
            newComponents.year = oldComponents.year
        default: break
        }

        guard var date = calendar.date(from: adjustDateComponents(newComponents)) else { return }

        if let minimumDate, date < minimumDate, pickerMode == .date {
            date = minimumDate
        }
        if let maximumDate, date > maximumDate, pickerMode == .date {
            date = maximumDate
        }

        setDate(date)

        onChange?(["timestamp": date.timeIntervalSince1970 * 1000])
    }

    private func adjustDateComponents(_ components: DateComponents) -> DateComponents {
        var newComponents = components
        newComponents.day = 1

        guard let date = calendar.date(from: newComponents),
              let range = calendar.range(of: .day, in: .month, for: date)
        else { return components }
        newComponents.day = components.day

        if let day = components.day {
            if day > range.count {
                newComponents.day = range.count
            } else if day < 1 {
                newComponents.day = 1
            }
        }
        if let newDay = newComponents.day, let oldDay = components.day, newDay != oldDay {
            let disct = oldDay - newDay
            if let comp = dataManager.collections.firstIndex(where: { $0.component == .day }) {
                selectRow(selectedRow(inComponent: comp) - disct, inComponent: comp, animated: true)
            }
        }
        return newComponents
    }

    private func handleDurationSelection() {
        var timeInterval = 0

        for (index, collection) in dataManager.collections.enumerated() {
            if let value = dataManager.getValueInComponentForRow(
                component: index,
                row: selectedRow(inComponent: index)
            ),
                let intValue = Int(value)
            {
                let multipleConstant: Int = switch collection.component {
                case .day: Constants.secsInDay
                case .hour: Constants.secsInHour
                case .minute: Constants.secsInMinute
                default: 0
                }

                timeInterval += multipleConstant * intValue
            }
        }

        if let minDuration, timeInterval < minDuration {
            timeInterval = minDuration
            setDuration(timeInterval)
        }
        if let maxDuration, timeInterval > maxDuration {
            timeInterval = maxDuration
            setDuration(timeInterval)
        }
        selectedDuration = timeInterval
        onChange?(["timestamp": timeInterval])
    }
}
