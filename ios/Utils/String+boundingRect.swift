//
//  String+boundingRect.swift
//  FlipDatePicker
//
//  Created by Halina Smolskaya on 31/01/2025.
//

extension String {
    func boundingRectRoundedToNearestPixel(
        font: UIFont,
        baselineOffset: CGFloat = 0
    )
        -> CGRect
    {
        let boundingRect = boundingRect(
            font: font,
            baselineOffset: baselineOffset
        )
        return CGRect(
            origin: boundingRect.origin,
            size: CGSize(
                width: boundingRect.width.roundedToNearestPixel(roundingRule: .up),
                height: boundingRect.height.roundedToNearestPixel(roundingRule: .up)
            )
        )
    }

    func boundingRect(
        font: UIFont,
        baselineOffset: CGFloat = 0
    )
        -> CGRect
    {
        boundingRect(
            with: CGSize.infinity,
            options: [],
            attributes: [
                .font: font,
                .baselineOffset: baselineOffset,
            ],
            context: nil
        )
    }
}
