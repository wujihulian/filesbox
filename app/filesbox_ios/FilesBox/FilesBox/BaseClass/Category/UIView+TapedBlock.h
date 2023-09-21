//
//  UIView+TapedBlock.h
//  Tairong
//
//  Created by yuekewei on 2019/8/5.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void (^TapedBlock)(void);

@interface UIView (TapedBlock)<UIGestureRecognizerDelegate>

- (void)tapped:(TapedBlock)block;
- (void)doubleTapped:(TapedBlock)block;
- (void)twoFingerTapped:(TapedBlock)block;
- (void)touchedDown:(TapedBlock)block;
- (void)touchedCancel:(TapedBlock)block;
- (void)touchedUp:(TapedBlock)block;
- (void)touchedBgColorWithDownColor:(UIColor *)downColor  upColor:(UIColor *)upColor cancelColor:(UIColor *)cancelColor;
@end

NS_ASSUME_NONNULL_END
