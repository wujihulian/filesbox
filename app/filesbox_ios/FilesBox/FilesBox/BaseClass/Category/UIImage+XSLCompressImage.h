//
//  UIImage+XSLCompressImage.h
//  HCYKT
//
//  Created by fg on 2017/10/18.
//  Copyright © 2017年 svnlan.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIImage (XSLCompressImage)

/**
 *  根据指定宽度，等比例调整图片尺寸
 *
 *  @param width 指定宽度
 */
- (UIImage *)imageByResizeToWidth:(CGFloat)width;

/**
 *  根据指定宽度及是否按屏幕分辨比放大，等比例调整图片尺寸
 *
 *  @param width 指定宽度
 *  @param scale 是否按屏幕分辨比放大
 */
- (UIImage *)imageByResizeToWidth:(CGFloat)width scale:(BOOL)scale;

/**
 *  根据指定尺寸，调整图片尺寸(图片可能会被拉伸)
 *
 *  @param size 指定尺寸
 */
- (UIImage *)imageByResizeToSize:(CGSize)size;

/**
 *  根据指定尺寸及是否按屏幕分辨比放大，调整图片尺寸(图片可能会被拉伸)
 *
 *  @param size  指定尺寸
 *  @param scale 是否按屏幕分辨比放大
 */
- (UIImage *)imageByResizeToSize:(CGSize)size scale:(BOOL)scale;

/**
 *  根据压缩精度系数及文件大小，获取压缩后的JPEG格式的图片数据
 *
 *  @param factor   压缩精度系数[1e-10, 0.1]
 *  @param fileSize 文件大小
 */
- (NSData *)compressToJPEGFormatDataWithFactor:(CGFloat)factor maxFileSize:(u_int64_t)fileSize;

/**
 *  指定图片宽度及图片文件大小，压缩图片，图片将等比例缩放
 *
 *  @param width       指定图片宽度
 *  @param maxFileSize 文件大小
 */
- (NSData *)resetImageDataWithImageWidth:(CGFloat)width maxFileSize:(uint64_t)maxFileSize;

/**
 *  指定图片尺寸及图片文件大小，压缩图片(图片可能会被拉伸)
 *
 *  @param size     指定图片尺
 *  @param maxFileSize 文件大小
 */
- (NSData *)resetImageDataWithImageSize:(CGSize)size maxFileSize:(uint64_t)maxFileSize;

@end
