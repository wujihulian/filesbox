//
//  UIImage+XSLCompressImage.m
//  HCYKT
//
//  Created by fg on 2017/10/18.
//  Copyright © 2017年 svnlan.com. All rights reserved.
//

#import "UIImage+XSLCompressImage.h"

@implementation UIImage (XSLCompressImage)

- (UIImage *)imageByResizeToWidth:(CGFloat)width
{
    return [self imageByResizeToWidth:width scale:YES];
}

- (UIImage *)imageByResizeToWidth:(CGFloat)width scale:(BOOL)scale
{
    if (self.size.width <= 0 || self.size.height <= 0) return nil;
    CGFloat height = width * self.size.height / self.size.width;
    return [self imageByResizeToSize:CGSizeMake(width, height) scale:scale];
}

- (UIImage *)imageByResizeToSize:(CGSize)size
{
    return [self imageByResizeToSize:size scale:YES];
}

- (UIImage *)imageByResizeToSize:(CGSize)size scale:(BOOL)scale
{
    if (size.width <= 0 || size.height <= 0) return nil;
    CGFloat scaleFactor = scale ? self.scale : 1.0;
    UIGraphicsBeginImageContextWithOptions(size, NO, scaleFactor);
    [self drawInRect:CGRectMake(0, 0, size.width, size.height)];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}

NS_INLINE CGFloat clampCompressionFactor(CGFloat factor)
{
    return factor <= 1e-10 ? 1e-10 : factor > 0.1 ? 0.1 : factor;
}

- (NSData *)compressToJPEGFormatDataWithFactor:(CGFloat)factor maxFileSize:(u_int64_t)fileSize
{
    if (!self) return nil;
    
    NSData *tempImageData = UIImageJPEGRepresentation(self, 1.0);
    if ([tempImageData length] <= fileSize) return tempImageData;
    
    NSData *targetImageData = nil;
    CGFloat compressionFactor = clampCompressionFactor(factor);
    CGFloat minFactor = 0;
    CGFloat maxFactor = 1.0;
    CGFloat midFactor = 0;
    
    while (fabs(maxFactor-minFactor) > compressionFactor)
    {
        @autoreleasepool
        {
            midFactor = minFactor + (maxFactor - minFactor)/2;
            tempImageData = UIImageJPEGRepresentation(self, midFactor);
            
            if ([tempImageData length] > fileSize)
            {
                maxFactor = midFactor;
            }
            else
            {
                minFactor = midFactor;
                targetImageData = tempImageData;
            }
        }
    }
    
    return targetImageData;
}

- (NSData *)resetImageDataWithImageWidth:(CGFloat)width maxFileSize:(uint64_t)maxFileSize
{
    // Image Size
    UIImage *newImage = [self imageByResizeToWidth:width];
    
    // File Size
    return [newImage compressToJPEGFormatDataWithFactor:1e-10 maxFileSize:maxFileSize];
}

- (NSData *)resetImageDataWithImageSize:(CGSize)size maxFileSize:(uint64_t)maxFileSize
{
    // Image Size
    UIImage *newImage = [self imageByResizeToSize:size];
    
    // File Size
    return [newImage compressToJPEGFormatDataWithFactor:1e-10 maxFileSize:maxFileSize];
}

@end
