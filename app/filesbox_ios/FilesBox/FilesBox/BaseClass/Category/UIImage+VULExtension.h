//
//  UIImage+VULExtension.h
//  VideoULimit
//
//  Created by ZCc on 2018/11/6.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImage (VULExtension)

+ (UIImage *)createImageWithColor:(UIColor *)color;

+ (UIImage *)resizebleImageWithName:(NSString *)imageName;

@end

NS_ASSUME_NONNULL_END
