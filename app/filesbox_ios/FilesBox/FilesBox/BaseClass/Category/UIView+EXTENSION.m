//
//  UIView+EXTENSION.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "UIView+EXTENSION.h"

@implementation UIView (EXTENSION)

- (void)setSize:(CGSize)size
{
    self.frame = (CGRect){{self.frame.origin.x, self.frame.origin.y}, size};
}

- (void)setOrigin:(CGPoint)origin
{
    self.frame = (CGRect){origin, {self.frame.size.width, self.frame.size.height}};
}

- (void)setX:(CGFloat)x
{
    self.frame = (CGRect){{x, self.frame.origin.y}, self.frame.size};
}

- (void)setY:(CGFloat)y
{
    self.frame = (CGRect){{self.frame.origin.x, y}, self.frame.size};
}

- (void)setWidth:(CGFloat)width
{
    self.frame = (CGRect){self.frame.origin, {width, self.frame.size.height}};
}

- (void)setHeight:(CGFloat)height
{
    self.frame = (CGRect){self.frame.origin, {self.frame.size.width, height}};
}

- (void)setCenterX:(CGFloat)centerX
{
    self.center = CGPointMake(centerX, self.center.y);
}

- (void)setCenterY:(CGFloat)centerY
{
    self.center = CGPointMake(self.center.x, centerY);
}

- (CGSize)size
{
    return self.frame.size;
}

- (CGPoint)origin
{
    return self.frame.origin;
}

- (CGFloat)x
{
    return self.frame.origin.x;
}

- (CGFloat)y
{
    return  self.frame.origin.y;
}

- (CGFloat)width
{
    return self.frame.size.width;
}

- (CGFloat)height
{
    return self.frame.size.height;
}

- (CGFloat)centerX
{
    return self.center.x;
}

- (CGFloat)centerY
{
    return self.center.y;
}

- (void)overlayClippingWithView:(UIView *)view cropRect:(CGRect)cropRect andColor:(UIColor *)color{
    
    UIBezierPath *path = [UIBezierPath bezierPathWithArcCenter:CGPointMake(self.width / 2, self.height / 2) radius:cropRect.size.width / 2 startAngle:0 endAngle:2 * M_PI clockwise:YES];
    CAShapeLayer *layer = [CAShapeLayer layer];
    
    layer.path = path.CGPath;
    layer.fillRule = kCAFillRuleNonZero;
    layer.fillColor = color.CGColor;
    [view.layer insertSublayer:layer below:view.layer];
}

- (void)addShadowWithView:(UIView *)mainView shadowColor:(UIColor *)shadowColor shadowDirection:(ShadowDirectionType)direction {
    mainView.layer.shadowColor = shadowColor.CGColor;
    mainView.layer.shadowOffset = CGSizeMake(0, 0);// 偏移
    mainView.layer.shadowOpacity = 0.3; // 透明
    mainView.layer.shadowRadius = 5; // 半径
    CGFloat shadowPathWidth = mainView.layer.shadowRadius;
    switch (direction) {
        case ShadowDirectionTypeAllDirection:
        {
            
        }
            break;
        case ShadowDirectionTypeTop:
        {
            CGRect shadowRect = CGRectMake(0, -shadowPathWidth / 2.0, mainView.bounds.size.width, shadowPathWidth);
            UIBezierPath *path = [UIBezierPath bezierPathWithRect:shadowRect];
            mainView.layer.shadowPath = path.CGPath;
        }
            break;
        case ShadowDirectionTypeLeft:
        {
            CGRect shadowRect = CGRectMake(-shadowPathWidth / 2.0, 0, shadowPathWidth, mainView.bounds.size.height);
            UIBezierPath *path = [UIBezierPath bezierPathWithRect:shadowRect];
            mainView.layer.shadowPath = path.CGPath;
        }
            break;
        case ShadowDirectionTypeBottom:
        {
            CGRect shadowRect = CGRectMake(0, shadowPathWidth / 2.0, mainView.bounds.size.width, shadowPathWidth);
            UIBezierPath *path = [UIBezierPath bezierPathWithRect:shadowRect];
            mainView.layer.shadowPath = path.CGPath;
        }
            break;
        case ShadowDirectionTypeRight:
        {
            CGRect shadowRect = CGRectMake(+shadowPathWidth / 2.0, 0, shadowPathWidth, mainView.bounds.size.height);
            UIBezierPath *path = [UIBezierPath bezierPathWithRect:shadowRect];
            mainView.layer.shadowPath = path.CGPath;
        }
            break;
        default:
            break;
    }
}

+ (instancetype)createColorGradientChangeWithSize:(CGSize)size
                direction:(VULGradientChangeDirection)direction
                startColor:(UIColor*)startcolor
                endColor:(UIColor*)endColor {
    if (CGSizeEqualToSize(size,CGSizeZero) || !startcolor || !endColor) {
        return nil;
    }
    UIView *view = [[UIView alloc] init];
    CAGradientLayer *gradientLayer = [CAGradientLayer layer];
    gradientLayer.frame = CGRectMake(0,0, size.width, size.height);
    CGPoint startPoint = CGPointZero;
    if (direction == VULGradientChangeDirectionDownDiagonalLine) {
        startPoint = CGPointMake(0.0,1.0);
    }
    gradientLayer.startPoint = startPoint;
    CGPoint endPoint = CGPointZero;
    
    switch(direction) {
        case VULGradientChangeDirectionLevel:
            endPoint = CGPointMake(1.0,0.0);
            break;
        case VULGradientChangeDirectionVertical:
            endPoint = CGPointMake(0.0,1.0);
            break;
        case VULGradientChangeDirectionUpwardDiagonalLine:
            endPoint = CGPointMake(1.0,1.0);
            break;
            
        case VULGradientChangeDirectionDownDiagonalLine:
            endPoint = CGPointMake(1.0,0.0);
            break;
        default:
            break;
    }
    gradientLayer.endPoint = endPoint;
    gradientLayer.colors = @[(__bridge id)startcolor.CGColor, (__bridge id)endColor.CGColor];
    UIGraphicsBeginImageContext(size);
    
    [gradientLayer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    view.backgroundColor = [UIColor colorWithPatternImage:image];
    return view;
}

// View 转 Image
- (UIImage *)sl_imageByViewInRect:(CGRect)range {
    CGRect rect = self.bounds;
    /** 参数取整，否则可能会出现1像素偏差 */
    /** 有小数部分才调整差值 */
#define lfme_export_fixDecimal(d) ((fmod(d, (int)d)) > 0.59f ? ((int)(d + 0.5) * 1.f) : (((fmod(d, (int)d)) < 0.59f && (fmod(d, (int)d)) > 0.1f) ? ((int)(d) * 1.f + 0.5f) : (int)(d) * 1.f))
    rect.origin.x = lfme_export_fixDecimal(rect.origin.x);
    rect.origin.y = lfme_export_fixDecimal(rect.origin.y);
    rect.size.width = lfme_export_fixDecimal(rect.size.width);
    rect.size.height = lfme_export_fixDecimal(rect.size.height);
#undef lfme_export_fixDecimal
    CGSize size = rect.size;
    //1.开启上下文
    UIGraphicsBeginImageContextWithOptions(size, NO, [UIScreen mainScreen].scale);
    CGContextRef context = UIGraphicsGetCurrentContext();
    //2.绘制图层
    [self.layer renderInContext:context];
    //3.从上下文中获取新图片
    UIImage *fullScreenImage = UIGraphicsGetImageFromCurrentImageContext();
    //4.关闭图形上下文
    UIGraphicsEndImageContext();
    if (CGRectEqualToRect(rect, range)) {
        return fullScreenImage;
    }
    //上面我们获得了一个全屏的截图，下边的方法是对这个图片进行裁剪。
    CGImageRef imageRef = fullScreenImage.CGImage;
    //注意：这里的宽/高 CGImageGetWidth(imageRef) 是图片的像素宽/高，所以计算截图区域时需要按比例来 * [UIScreen mainScreen].scale；
    range = CGRectMake(range.origin.x * [UIScreen mainScreen].scale, range.origin.y * [UIScreen mainScreen].scale, range.size.width * [UIScreen mainScreen].scale, range.size.height * [UIScreen mainScreen].scale);
    CGImageRef imageRefRect = CGImageCreateWithImageInRect(imageRef, range);
    UIImage *image = [[UIImage alloc] initWithCGImage:imageRefRect];
    CGImageRelease(imageRefRect);
    return image;
}

@end
