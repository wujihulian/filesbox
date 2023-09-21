//
//  NSData+EXTENSION.h
//  VideoULimit
//
//  Created by ZCc on 2018/11/7.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSData (EXTENSION)

/**
 判断图片类型
 
 @param data 图片data
 */
+ (NSString *)typeForImageData:(NSData *)data;

@end

NS_ASSUME_NONNULL_END
