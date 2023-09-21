//
//  NSArray+Extension.m
//  VideoULimit
//
//  Created by ZCc on 2019/3/12.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "NSArray+Extension.h"

@implementation NSArray (Extension)

+ (NSArray *)arrayFromJsonStr:(NSString *)jsonStr {
    NSError *error = nil;
    id tmp = [NSJSONSerialization JSONObjectWithData:[jsonStr dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingAllowFragments error:&error];
    if (tmp) {
        if ([tmp isKindOfClass: [NSArray class]]){
            return tmp;
        } else if ([tmp isKindOfClass:[NSString class]] || [tmp isKindOfClass:[NSDictionary class]]) {
            return [NSArray arrayWithObject:tmp];
        } else {
            return nil;
        }
    } else {
        NSLog(@"arrayFromJsonStr字符串转json出错%@", error);
        return nil;
    }
}

@end
