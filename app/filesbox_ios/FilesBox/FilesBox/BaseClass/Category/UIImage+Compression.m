//
//  UIImage+Compression.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import "UIImage+Compression.h"
#import <AVFoundation/AVAsset.h>
#import <AVFoundation/AVAssetImageGenerator.h>
#import <AVFoundation/AVTime.h>

@implementation UIImage (Compression)

+ (UIImage *)imageCompressed:(UIImage *)sourceImage withdefineWidth:(CGFloat)defineWidth {
    
    UIImage *newImage = nil;
    CGSize imageSize = sourceImage.size;
    CGFloat width = imageSize.width;
    width = width == 0 ? 1 : width;
    CGFloat height = imageSize.height;
    CGFloat targetWidth = defineWidth;
    CGFloat targetHeight = height * targetWidth / width;
    CGSize size = CGSizeMake(targetWidth, targetHeight);
    CGFloat scaleFactor = 0.0;
    CGFloat scaleWidth = targetWidth;
    CGFloat scaleHeight = targetHeight;
    CGPoint thumbnailPoint = CGPointMake(0.0, 0.0);
    if(CGSizeEqualToSize(imageSize, size) == NO){
        CGFloat widthFactor = targetWidth / height;
        CGFloat heightFactor = targetHeight / height;
        if(widthFactor > heightFactor){
            scaleFactor = widthFactor;
        }else{
            scaleFactor = heightFactor;
        }
        
        scaleWidth = width * scaleWidth;
        scaleHeight = height * scaleHeight;
        if(widthFactor > heightFactor){
            thumbnailPoint.y = (targetHeight - scaleHeight) * 0.5;
        }else if (widthFactor <= heightFactor){
            thumbnailPoint.x = (targetWidth - scaleWidth) * 0.5;
        }
    }
    UIGraphicsBeginImageContext(size);
    CGRect thumbnailRect = CGRectZero;
    thumbnailRect.origin = thumbnailPoint;
    thumbnailRect.size.width = scaleWidth;
    thumbnailRect.size.height = scaleHeight;
    
    [sourceImage drawInRect:thumbnailRect];
    
    newImage = UIGraphicsGetImageFromCurrentImageContext();
    
    if(newImage == nil){
        NSAssert(!newImage, @"图片压缩失败");
    }
    
    UIGraphicsEndImageContext();
    return newImage;
}

+ (UIImage *)clipImage:(UIImage *)image cornerRadius:(CGFloat)radius {
    UIGraphicsBeginImageContextWithOptions(image.size, NO, 0.0);
    
    // 获取上下文
    CGContextRef contextRef = UIGraphicsGetCurrentContext();
    
    // 添加圆角
    CGRect rect = CGRectMake(0, 0, image.size.width, image.size.height);
//    CGContextAddPath(contextRef,
//                     [UIBezierPath bezierPathWithRoundedRect:rect cornerRadius:radius].CGPath);
    
    CGContextAddPath(contextRef, [UIBezierPath bezierPathWithRoundedRect:rect byRoundingCorners:UIRectCornerAllCorners cornerRadii:CGSizeMake(radius, radius)].CGPath);
    
    CGContextClip(contextRef);
    
    // 画图片
    [image drawInRect:rect];
    
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    
    UIGraphicsEndImageContext();
    
    return newImage;
}


// 获取视频第一帧
+ (UIImage*)getVideoPreViewImage:(NSURL *)path {
    NSDictionary *opts = [NSDictionary dictionaryWithObject:[NSNumber numberWithBool:NO] forKey:AVURLAssetPreferPreciseDurationAndTimingKey];
    AVURLAsset *urlAsset = [AVURLAsset URLAssetWithURL:path options:opts];
    AVAssetImageGenerator *generator = [AVAssetImageGenerator assetImageGeneratorWithAsset:urlAsset];
    generator.appliesPreferredTrackTransform = YES;
    CMTime actualTime;
    NSError *error = nil;
    CGImageRef img = [generator copyCGImageAtTime:CMTimeMake(0, 600) actualTime:&actualTime error:&error];
    if (error) {
        NSLog(@"imageGenerationError: %@",error);
        return nil;
    }
    return [UIImage imageWithCGImage:img];
}

@end
