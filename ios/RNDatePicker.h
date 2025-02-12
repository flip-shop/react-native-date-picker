#ifdef RCT_NEW_ARCH_ENABLED

#import <React/RCTViewComponentView.h>
#import <UIKit/UIKit.h>
#import "RCTConvert.h"
#import "RNFontOptions.h"
 

NS_ASSUME_NONNULL_BEGIN

@interface RNDatePicker : RCTViewComponentView

@end

NS_ASSUME_NONNULL_END

#else
#import <UIKit/UIKit.h>

@interface RNDatePicker : UIView


@end

#endif

@interface RCTConvert (RNDatePicker)

+ (RNFontOptions)RNFontOptions:(id)json;

@end

