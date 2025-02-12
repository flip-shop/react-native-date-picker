//
//  DatePicker.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 07/06/2024.
//

import UIKit

@objc(DatePicker)
@objcMembers public class DatePicker: UIPickerView {
    public var selectedDate: Date = .init()
    public var minimumDate: Date?
    public var maximumDate: Date?

    public var onChange: (([String: Any]) -> Void)?
    public var onStateChange: (([String: Any]) -> Void)?

    public var locale: Locale = .current {
        didSet {
            dataManager = createDataManager()
        }
    }

    public var minuteInterval: Int = 1 {
        didSet {
            if [.dateAndTime, .time].contains(pickerMode) {
                dataManager = createDataManager()
            }
        }
    }

    public var isPickerScrolling = false {
        didSet {
            onStateChange?(["state": isPickerScrolling ? Constants.spinningState : Constants.idleState])
        }
    }

    var selectedDuration: Int?
    private(set) var pickerMode: DatePickerMode = .date
    private(set) var minDuration: Int?
    private(set) var maxDuration: Int?
    private(set) var calendar: Calendar = .init(identifier: .gregorian)
    private(set) var pickerFont: UIFont?
    private(set) var pickerTextColor: UIColor?
    private(set) lazy var dayUnitLabel = makeUnitLabel()
    private(set) lazy var hourUnitLabel = makeUnitLabel()
    private(set) lazy var minuteUnitLabel = makeUnitLabel()

    private(set) var dataManager: DataManager = .init(collections: []) {
        didSet {
            reloadAllComponents()
            setDate(selectedDate)
        }
    }

    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }

    override public func layoutSubviews() {
        super.layoutSubviews()
        guard pickerMode == .duration else { return }
        positionUnitLabels()
    }

    public func setup() {
        nativeID = "ignoreScroll"
        overrideUserInterfaceStyle = .light
        calendar.locale = locale
        delegate = self
        dataSource = self
    }

    public func setMinimumDuration(_ duration: Int) {
        guard duration >= 0, minDuration != duration else { return }
        minDuration = roundUpToNearestMinute(duration)
        guard pickerMode == .duration else { return }
        dataManager = createDataManager()
        setDuration(selectedDuration ?? minDuration ?? 0)
    }

    public func setMaximumDuration(_ duration: Int) {
        guard duration >= 0, maxDuration != duration else { return }
        maxDuration = roundUpToNearestMinute(duration)
        guard pickerMode == .duration else { return }
        dataManager = createDataManager()
    }

    public func setTextColorProp(_ hexColor: String?) {
        if hexColor == Constants.lightColor {
            overrideUserInterfaceStyle = .light
        } else if hexColor?.lowercased() == Constants.darkColor {
            overrideUserInterfaceStyle = .dark
        }
    }

    public func setDatePickerMode(_ mode: String?) {
        guard let mode, let pickerMode = DatePickerMode(rawValue: mode) else { return }

        self.pickerMode = pickerMode
        dataManager = createDataManager()
        guard pickerMode == .duration else { return }
        configureUnitLabels()
        setDuration(selectedDuration ?? minDuration ?? 0)
    }

    public func setTimeZoneOffsetInMinutes(_ timeZoneOffsetInMinutes: String) {
        if timeZoneOffsetInMinutes.isEmpty {
            calendar.timeZone = TimeZone.current
        } else if let minutes = Int(timeZoneOffsetInMinutes) {
            calendar.timeZone = TimeZone(secondsFromGMT: minutes * 60) ?? TimeZone.current
        }
        dataManager = createDataManager()
    }

    public func setDuration(_ duration: Int) {
        selectedDuration = roundUpToNearestMinute(duration)
        guard pickerMode == .duration, let selectedDuration else { return }
        let days = selectedDuration / Constants.secsInDay
        let remainderAfterDays = selectedDuration % Constants.secsInDay
        let hours = remainderAfterDays / Constants.secsInHour
        let remainderAfterHours = remainderAfterDays % Constants.secsInHour
        let minutes = remainderAfterHours / Constants.secsInMinute

        for (index, collection) in dataManager.collections.enumerated() {
            var row: Int? = nil
            switch collection.component {
            case .day:
                row = collection.getRowForValue("\(days)")
            case .hour:
                row = collection.getRowForValue("\(hours)")
            case .minute:
                row = collection.getRowForValue("\(minutes)")
            default:
                break
            }
            if let row {
                selectRow(row, inComponent: index, animated: false)
            }
        }
    }

    public func setDate(_ date: Date) {
        guard pickerMode != .duration else { return }
        let components = calendar.dateComponents(dataManager.components, from: date)
        dataManager.collections.enumerated().map { index, collection in
            var row: Int? = nil
            switch collection.component {
            case .year:
                row = collection.getRowForValue("\(components.year ?? 0)")
            case .day where pickerMode == .dateAndTime:
                row = (calendar.ordinality(of: .day, in: .year, for: date) ?? 1) - 1
            case .month, .day:
                row = collection.middleRow + (components.value(for: collection.component) ?? 0) - 1
            case .hour:
                row = collection.middleRow + (components.value(for: collection.component) ?? 0)
                if let amPmIndex = dataManager.componentIndex(component: .nanosecond),
                   let hourComponent = components.value(for: collection.component)
                {
                    selectRow(hourComponent > 11 ? 1 : 0, inComponent: amPmIndex, animated: false)
                }
            case .minute:
                if let minutes = components.value(for: .minute) {
                    let minutesValue = Int(
                        (Double(minutes) / Double(minuteInterval))
                            .rounded() * Double(minuteInterval)
                    )
                    row = collection.getRowForValue(String(format: "%02d", minutesValue))
                }
            default: break
            }
            if let row {
                selectRow(row, inComponent: index, animated: false)
            }
        }

        selectedDate = date
    }

    public func setFontOptions(_ options: RNFontOptions) {
        pickerFont = UIFont(name: options.name, size: options.size)
        pickerTextColor = UIColor(hexString: options.color)
        reloadAllComponents()
        updateUnitLabelsFont()
    }

    func is24HourFormat() -> Bool {
        let dateFormat = DateFormatter.dateFormat(fromTemplate: "j", options: 0, locale: locale) ?? ""
        return dateFormat.contains("H") || dateFormat.contains("k")
    }

    private func roundUpToNearestMinute(_ seconds: Int) -> Int {
        seconds % 60 == 0 ? seconds : ((seconds / 60) + 1) * 60
    }
}
