//
//  UIColor+Utils.m
//  VideoULimit
//
//  Created by ZCc on 2019/6/18.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "UIColor+Utils.h"

@implementation UIColor (Utils)

- (NSString *)toHexRGB {
    CGColorRef color = self.CGColor;
    size_t count = CGColorGetNumberOfComponents(color);
    const CGFloat *components = CGColorGetComponents(color);
    
    static NSString *stringFormat = @"#%02x%02x%02x";
    
    if (count == 2) {
        // Grayscale
        NSUInteger grey = (NSUInteger)(components[0] * (CGFloat)255);
        return [NSString stringWithFormat:stringFormat, grey, grey, grey];
    }
    else if (count == 4) {
        // RGB
        return [NSString stringWithFormat:stringFormat,
                (NSUInteger)(components[0] * (CGFloat)255),
                (NSUInteger)(components[1] * (CGFloat)255),
                (NSUInteger)(components[2] * (CGFloat)255)];
    }
    
    // Unsupported color space
    return nil;
}

@end
