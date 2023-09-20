//
//  NSData+EXTENSION.m
//  VideoULimit
//
//  Created by ZCc on 2018/11/7.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "NSData+EXTENSION.h"

@implementation NSData (EXTENSION)

+ (NSString *)typeForImageData:(NSData *)data {
    uint8_t c;
    [data getBytes:&c length:1];
    switch (c) {
        case 0xFF:
            return @"jpeg";
        case 0x89:
            return @"png";
        case 0x47:
            return @"gif";
        case 0x49:
        case 0x4D:
            return @"tiff";
    }
    return nil;
}

@end
