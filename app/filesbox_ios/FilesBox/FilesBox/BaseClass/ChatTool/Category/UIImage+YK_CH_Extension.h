//
//  UIImage+YK_CH_Extension.h
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
@interface UIImage (YK_CH_Extension)


+ (UIImage *)gxz_imageWithColor:(UIColor *)color;

+ (UIImage *)videoFramerateWithPath:(NSString *)videoPath;

// 压缩图片
+ (UIImage *)simpleImage:(UIImage *)originImg;

+ (UIImage *)makeArrowImageWithSize:(CGSize)imageSize
                              image:(UIImage *)image
                           isSender:(BOOL)isSender;

+ (UIImage *)addImage2:(UIImage *)firstImg
               toImage:(UIImage *)secondImg;

+ (UIImage *)addImage:(UIImage *)firstImg
              toImage:(UIImage *)secondImg;

+ (UIImage *)imageWithData:(NSData *)data
                      size:(CGSize)size
                     scale:(CGFloat)scale
               orientation:(UIImageOrientation)orientation;

@end
