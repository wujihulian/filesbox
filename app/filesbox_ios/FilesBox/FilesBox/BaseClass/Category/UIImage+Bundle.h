//
//  UIImage+Bundle.h
//  xiaolancang
//
//  Created by yuekewei on 2019/12/17.
//  Copyright © 2019 yeqiang. All rights reserved.
//


#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImage (Bundle)

+ (UIImage *)vul_imageNamed:(NSString *)name;

+ (UIImage *)imageForIconBundle:(NSString *)name;
@end

NS_ASSUME_NONNULL_END
