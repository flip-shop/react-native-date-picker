//
//  UILabel+utils.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 31/01/2025.
//

extension UILabel {
    func setAnimatedText(_ text: String) {
        UIView.transition(
            with: self,
            duration: 0.3,
            options: [.transitionCrossDissolve, .curveEaseInOut]
        ) { [weak self] in
            self?.text = text
        }
    }

    func sizeToFitNearestPixel(baselineOffset: CGFloat = 0) {
        let boundingRect = (text ?? "").boundingRectRoundedToNearestPixel(
            font: font,
            baselineOffset: baselineOffset)
        frame.size = boundingRect.size
    }
}
