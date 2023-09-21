//
//  NSDate+formatter.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/17.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDate (formatter)

/**
 以毫秒为整数值 时间戳转string

 @param timeString 毫秒级 时间戳
 @param format format格式
 */
+ (NSString *)timeWithTimeIntervalString:(NSString *)timeString Format:(NSString *)format;

+ (NSDate *)date:(NSString *)datestr WithFormat:(NSString *)format;

- (NSDate *)dateWithFormatter:(NSString *)formatter;

/**
 fomat格式的时间str 转换为 时间戳
 */
+ (NSInteger)getIntervalByTime:(NSString *)string withFomat:(NSString *)fomat;

/**
 传入时间戳 返回 刚刚、几分钟前、...
 */
+ (NSString *)getRecentTimeWithTime:(NSString *)stampStr;

+ (NSString *)updateTimeForRow:(NSString *)stampStr;

+ (NSString *)updateTimeForTimeLong:(NSString *)stampStr;

+ (BOOL)isSameDay:(NSDate *)date1 date2:(NSDate *)date2;

+ (BOOL)isToday:(NSString *)dateString;

+ (BOOL)isYesterday:(NSString *)dateString;

+ (BOOL)isThisYear:(NSString *)dateString;

+ (NSArray *)getCurrentWeek;

/// 获取某月第一天和最后一天
/// @param dateStr 2020-11-06
+ (NSArray *)getMonthFirstAndLastDayWith:(NSString *)dateStr;

+ (NSArray *)getYearFirstAndLastDayWith:(NSString *)dateStr;


/// 获取 指定日期的N天前
/// @param currentDate 日期
/// @param day 多少天
+ (NSArray *)getRecentDateWith:(NSDate *)currentDate day:(NSInteger)day;

/**
 获取当前时间戳（毫秒级）
 */
+ (NSString *)getNowTimeTimestamp;
/**
 获取当前时间的: 前一周(day:-7)丶前一个月(month:-30)丶前一年(year:-1)的时间戳
 */
+ (NSString *)getExpectTimestampYear:(NSInteger)year month:(NSUInteger)month day:(NSUInteger)day;
+ (NSString *)getChatRecentTimeWithTime:(NSString *)dateStr;
+ (NSString *)dateWithTimeString:(NSString *)timeString fromFormat:(NSString *)format toFormat:(NSString *)toFormat;

//获取当前时间
+ (NSString*)getCurrentTimeWithFormat:(NSString *)format;

//NSDate转时间戳
+ (NSString *)dateConversionTimeStamp:(NSDate *)date;

@end
