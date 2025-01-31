//
//  CGFloat+roundToNearestPixel.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 31/01/2025.
//

extension CGFloat {
    func roundedToNearestPixel(roundingRule: FloatingPointRoundingRule = .down) -> CGFloat {
        let scale = UIScreen.main.scale
        return (self * scale).rounded(roundingRule) / scale
    }

    mutating func roundToNearestPixel(roundingRule: FloatingPointRoundingRule = .down) {
        self = roundedToNearestPixel(roundingRule: roundingRule)
    }
}
