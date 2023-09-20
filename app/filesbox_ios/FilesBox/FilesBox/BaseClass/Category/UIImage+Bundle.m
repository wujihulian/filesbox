//
//  UIImage+Bundle.m
//  xiaolancang
//
//  Created by yuekewei on 2019/12/17.
//  Copyright © 2019 yeqiang. All rights reserved.
//

#import "UIImage+Bundle.h"

@implementation UIImage (Bundle)

+ (UIImage *)vul_imageNamed:(NSString *)name {
    UIImage *image = [UIImage imageNamed:name];
    if (!image) {
        image = [self imageForIconBundle:name];
    }
    return image;
}

+ (UIImage *)imageForIconBundle:(NSString *)name {
    NSBundle *imageBundle =  [NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:@"icon" ofType:@"bundle"]];
    NSArray *exts = @[@"png", @"jpeg", @"jpg", @"gif", @"webp", @"apng"];
    NSString *path = @"";
    for (NSString *type in exts) {
        path = [imageBundle pathForScaledResource:name ofType:type];
        if (path) {
            break;
        }
    }
    return [UIImage imageWithContentsOfFile:path];
}
@end
