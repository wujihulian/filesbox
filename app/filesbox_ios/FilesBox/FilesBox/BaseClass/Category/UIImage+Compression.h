//
//  UIImage+Compression.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (Compression)

+ (UIImage *)imageCompressed:(UIImage *)sourceImage withdefineWidth:(CGFloat)defineWidth;

+ (UIImage *)clipImage:(UIImage *)image cornerRadius:(CGFloat)radius;

+ (UIImage*)getVideoPreViewImage:(NSURL *)path;

@end
