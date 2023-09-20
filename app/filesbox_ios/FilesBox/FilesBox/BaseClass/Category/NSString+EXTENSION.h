//
//  NSString+EXTENSION.h
//  VideoULimit
//
//  Created by ZCc on 2018/11/6.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSString (EXTENSION)

/**
 根据字体大小 返回文本宽高

 @param font 字体
 @param maxSize 最大size
 */
- (CGSize)sizewithFont:(UIFont *)font andMaxSize:(CGSize)maxSize;


/**
 根据富文本字体 返回文本宽高

 @param attribute 富文本
 @param maxSize 最大宽高
 @return 适应后的宽高
 */
- (CGSize)sizeWithAttributes:(NSDictionary *)attribute andMaxSize:(CGSize)maxSize;

/**
 json字典转换为json字符串

 @param dic json 字典
 */
+ (NSString *)getJsonStringWithDic:(NSDictionary *)dic;

+ (NSString *)dateStrWithTimeInterval:(NSInteger)createTimeInterval;

/** 根据图片data 判断是否是gif图 */
+ (BOOL)isGifWithImageData: (NSData *)data;

/**
 根据image的data 判断图片类型
 
 @param data 图片data
 @return 图片类型(png、jpg...)
 */
+ (NSString *)contentTypeWithImageData: (NSData *)data;


/**
 拼接cookie
 */
+ (NSString*)readCurrentCookieWith:(NSDictionary*)dic;


/**
 正则判断img标签 返回src

 @param htmlString 网页内容
 @return src
 */
+ (NSArray *)getImageSrcWithString:(NSString *)htmlString;

/**
 * 时间(秒)转String, 格式(xx:xx:xx)
 */
+ (NSString *)timeStringFromInteger:(NSInteger)seconds;

//字符串加密(16进制)
+ (NSString *)encyptPKCS5:(NSString *)plainText WithKey:(NSString *)key;

+ (NSString *)getMD5WithFilePath:(NSURL *)path;

#pragma mark - MD5加密 32位 小写
+ (NSString *)MD5ForLower32Bate:(NSString *)str;

/**
 获取当前IP地址
 */
//+ (NSString *)getIpAddress;
+ (void)getDeviceIPAddressResult:(void(^)(NSString *ipAddress))ipStr;

+ (NSString *)getIPv4Address;

/**
 json字符串转为对象

 @param json json字符串
 @return 对象
 */
+ (id)jsonToObject:(NSString *)json;

/**
 对象转为json字符串

 @param obj 对象
 @return json字符串
 */
+ (NSString *)objectToJson:(id)obj;

/**
 过滤htmlh标签

 @param string 原内容
 @return 过滤后的内容
 */
+ (NSString *)getZZwithString:(NSString *)string;


+ (NSString *)getTransferredwithString:(NSString *)string sort:(BOOL)isPositive;

// 是否是integer
+ (BOOL)isPureInteger:(NSString *)string;

// 是否是网页url
+ (BOOL)isUrlAddress:(NSString*)url;

//获取随机数
+ (NSInteger)getRandomNumberFrom:(int)fromNum to:(int)toNum;

+ (BOOL)validateMobile:(NSString *)mobile;

+ (BOOL)isEnglishFirst:(NSString *)str;

+ (NSString *)weekdayStringWithDate:(NSDate *)date;

+ (NSString *)timeFormatted:(NSString *)seconds;

//根据allowbackview获取回看时间
+ (NSDictionary *)getBackViewTimeWithString:(NSString *)backString;

//小数点后position位 四舍五入
+ (NSString *)roundUp:(float)number afterPoint:(int)position;

/// 转换成 几K 几W
+ (NSString *)getDealNumwithstring:(NSString *)string;

+ (NSString *)stringWithDecimalNumber:(double)num;

+ (float)floatWithdecimalNumber:(double)num;

+ (double)doubleWithdecimalNumber:(double)num;

@end

NS_ASSUME_NONNULL_END
