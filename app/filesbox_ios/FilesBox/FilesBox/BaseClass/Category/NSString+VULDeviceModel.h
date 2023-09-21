//
//  NSString+VULDeviceModel.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/5.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (VULDeviceModel)

/**
 获取设备型号

 @return 返回设备名称
 */
+ (NSString *)getDeviceModelName;

/**
判断字符串不为空

@return 字符串是否为空
*/
- (BOOL)isAvailable;

/**
 判断字符串是否为空
 */
+(BOOL)nsstringIsNotEmpty:(NSString *)string;

/**
 校验邮箱
 */
+ (BOOL)validateEmail:(NSString *)email;


/**
 判断是否有中文
 */
+ (BOOL)checkIsChinese:(NSString *)string;


/**
 过滤html 标签
 */
+ (NSString *)flattenHTML:(NSString *)html;

+ (NSString *)getCurrentDeviceUserAgent;

@end

