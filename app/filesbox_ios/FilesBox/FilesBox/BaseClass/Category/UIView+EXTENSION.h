//
//  UIView+EXTENSION.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, ShadowDirectionType) {
    ShadowDirectionTypeAllDirection, // 所有方向
    ShadowDirectionTypeLeft,   //图片在左，标题在右，默认风格
    ShadowDirectionTypeRight,  //图片在右，标题在左
    ShadowDirectionTypeTop,    //图片在上，标题在下
    ShadowDirectionTypeBottom,  //图片在下，标题在上
};

typedef NS_ENUM(NSInteger, VULGradientChangeDirection) {
    VULGradientChangeDirectionLevel, //水平方向渐变
    VULGradientChangeDirectionVertical, //垂直方向渐变
    VULGradientChangeDirectionUpwardDiagonalLine, //主对角线方向渐变
    VULGradientChangeDirectionDownDiagonalLine //副对角线方向渐变
};
    

@interface UIView (EXTENSION)

@property (nonatomic, assign) CGSize size;

@property (nonatomic, assign) CGPoint origin;

@property (nonatomic, assign) CGFloat x;

@property (nonatomic, assign) CGFloat y;

@property (nonatomic, assign) CGFloat width;

@property (nonatomic, assign) CGFloat height;

@property (nonatomic, assign) CGFloat centerX;

@property (nonatomic, assign) CGFloat centerY;

//填充
- (void)overlayClippingWithView:(UIView *)view cropRect:(CGRect)cropRect andColor:(UIColor *)color;
// 添加阴影
- (void)addShadowWithView:(UIView *)mainView shadowColor:(UIColor *)shadowColor shadowDirection:(ShadowDirectionType)direction;

// 渐变色View
+ (instancetype)createColorGradientChangeWithSize:(CGSize)size
                direction:(VULGradientChangeDirection)direction
                startColor:(UIColor*)startcolor
                endColor:(UIColor*)endColor;

- (UIImage *)sl_imageByViewInRect:(CGRect)range;

@end
