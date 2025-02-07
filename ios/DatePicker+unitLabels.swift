//
//  DatePicker+unitLabels.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 31/01/2025.
//
extension DatePicker {
    func configureUnitLabels() {
        removeUnitLabels()
        guard pickerMode == .duration else { return }
        showUnitLabels()
    }

    func makeUnitLabel() -> UILabel {
        let label = UILabel()
        label.textAlignment = .right
        label.font = Constants.labelUnitFont
        label.isOpaque = false
        return label
    }

    func setUnitLabelsText() {
        UIView.performWithoutAnimation { [weak self] in
            guard let self else { return }

            dayUnitLabel.setAnimatedText(Constants.unitDayText)
            dayUnitLabel.sizeToFitNearestPixel(baselineOffset: Constants.unitLabelBaselineOffset)

            hourUnitLabel.setAnimatedText(Constants.unitHourText)
            hourUnitLabel.sizeToFitNearestPixel(baselineOffset: Constants.unitLabelBaselineOffset)

            minuteUnitLabel.setAnimatedText(Constants.unitMinuteText)
            minuteUnitLabel.sizeToFitNearestPixel(baselineOffset: Constants.unitLabelBaselineOffset)
        }
    }

    func positionUnitLabels() {
        UIView.performWithoutAnimation { [weak self] in
            guard let self else { return }

            dayUnitLabel.frame.origin.x = unitLabelOriginX(forComponent: dataManager.componentIndex(component: .day))
            dayUnitLabel.center.y = center.y + Constants.unitLabelVerticalPositionAdjustment
            dayUnitLabel.frame.origin.y.roundToNearestPixel()

            hourUnitLabel.frame.origin.x = unitLabelOriginX(forComponent: dataManager.componentIndex(component: .hour))
            hourUnitLabel.center.y = center.y + Constants.unitLabelVerticalPositionAdjustment
            hourUnitLabel.frame.origin.y.roundToNearestPixel()

            minuteUnitLabel.frame.origin
                .x = unitLabelOriginX(forComponent: dataManager.componentIndex(component: .minute))
            minuteUnitLabel.center.y = center.y + Constants.unitLabelVerticalPositionAdjustment
            minuteUnitLabel.frame.origin.y.roundToNearestPixel()
        }
    }

    private func showUnitLabels() {
        addSubview(dayUnitLabel)
        addSubview(hourUnitLabel)
        addSubview(minuteUnitLabel)

        setUnitLabelsText()
        positionUnitLabels()
    }

    private func removeUnitLabels() {
        dayUnitLabel.removeFromSuperview()
        hourUnitLabel.removeFromSuperview()
        minuteUnitLabel.removeFromSuperview()
    }

    private func unitLabelOriginX(forComponent component: Int?) -> CGFloat {
        guard let component else { return 0 }

        let rowWidth: CGFloat = rowSize(forComponent: component).width
        let rowHalfWidth: CGFloat = rowWidth / 2
        let unitLabelMargin: CGFloat = rowHalfWidth + Constants.unitLabelSpacing
        let originX = frame.minX + unitLabelMargin + CGFloat(component) * (rowWidth + Constants.columnSpacing)
        return originX.roundedToNearestPixel(roundingRule: .up)
    }
}
