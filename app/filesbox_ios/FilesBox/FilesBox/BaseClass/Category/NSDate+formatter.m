//
//  NSDate+formatter.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/17.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "NSDate+formatter.h"

@implementation NSDate (formatter)

+ (NSString *)timeWithTimeIntervalString:(NSString *)timeString Format:(NSString *)format {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    NSTimeZone *zone = [NSTimeZone systemTimeZone];
    formatter.timeZone = zone;
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:format];

    NSDate *date = [NSDate dateWithTimeIntervalSince1970:[timeString doubleValue] / 1000];
    NSString *dateString = [formatter stringFromDate:date];

    return dateString;
}

+ (NSDate *)date:(NSString *)datestr WithFormat:(NSString *)format {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setLocale:[NSLocale currentLocale]];
    [dateFormatter setTimeZone:[NSTimeZone localTimeZone]];
    [dateFormatter setDateFormat:format];
    NSDate *date = [dateFormatter dateFromString:datestr];
#if !__has_feature(objc_arc)
    [dateFormatter release];
#endif
    return date;
}

- (NSDate *)dateWithFormatter:(NSString *)formatter {
    NSDateFormatter *fmt = [[NSDateFormatter alloc] init];
    fmt.dateFormat = formatter;
    NSString *selfStr = [fmt stringFromDate:self];
    return [fmt dateFromString:selfStr];
}

+ (NSInteger)getIntervalByTime:(NSString *)string withFomat:(NSString *)fomat {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    //设定时间格式,这里可以设置成自己需要的格式
    [dateFormatter setDateFormat:fomat];
    NSDate *date = [dateFormatter dateFromString:string];
    NSInteger recordTime = [date timeIntervalSince1970] * 1000;
    return recordTime;
}

+ (NSString *)getRecentTimeWithTime:(NSString *)stampStr {
    if (!NSStringIsNotEmpty(stampStr)) {
        return nil;
    }
    NSString *resultStr;
    NSTimeInterval currentTime = [[NSDate date] timeIntervalSince1970];
    NSTimeInterval createTime = [stampStr integerValue] / 1000;
    // 时间差
    NSTimeInterval time = currentTime - createTime;
    NSInteger seconds = time / 60;
    NSInteger hours = time / 3600;
    if (seconds < 60) {
        if (seconds == 0) {
            resultStr = @"刚刚";
        } else {
            resultStr = [NSString stringWithFormat:@"%ld分钟前", (long)seconds];
        }
    } else if (hours < 24) {
        resultStr = [NSString stringWithFormat:@"%ld小时前", (long)hours];
    } else {
        resultStr = [self timeWithTimeIntervalString:stampStr Format:@"yyyy/MM/dd HH:mm"];
    }
    return resultStr;
}

+ (NSString *)getChatRecentTimeWithTime:(NSString *)stampStr {
    if (!NSStringIsNotEmpty(stampStr)) {
        return nil;
    }
    NSString *resultStr;
    NSTimeInterval currentTime = [[NSDate date] timeIntervalSince1970];
    NSTimeInterval createTime = [stampStr integerValue] / 1000;
    // 时间差
    NSTimeInterval time = currentTime - createTime;
    NSInteger seconds = time / 60;
    NSInteger hours = time / 3600;
    if (seconds < 60) {
        if (seconds == 0) {
            resultStr = @"刚刚";
        } else {
            resultStr = [NSString stringWithFormat:@"%ld分钟前", (long)seconds];
        }
    } else if (hours < 24) {
        resultStr = [self timeWithTimeIntervalString:stampStr Format:@"HH:mm"];
    } else {
        resultStr = [self timeWithTimeIntervalString:stampStr Format:@"yyyy/MM/dd HH:mm"];
    }
    return resultStr;
}
+ (NSString *)updateTimeForTimeLong:(NSString *)stampStr {
    if ([stampStr isEqualToString:@""]) {
        return nil;
    }
    NSString *resultStr;
    NSTimeInterval createTime = [stampStr integerValue] / 1000;    // 时间差
        return backTimeChange(createTime);
}

+ (NSString *)updateTimeForRow:(NSString *)stampStr {
    if ([stampStr isEqualToString:@""]) {
        return nil;
    }
    NSString *resultStr;
    NSTimeInterval currentTime = [[NSDate date] timeIntervalSince1970];
    NSTimeInterval createTime = [stampStr integerValue] / 1000;    // 时间差
    NSTimeInterval time = currentTime - createTime;

    NSInteger seconds = time / 60;
    if (seconds < 60) {
        if (seconds == 0) {
            resultStr = [NSString stringWithFormat:@"%ld秒前", (long)time];
        } else {
            resultStr = [NSString stringWithFormat:@"%ld分钟前", (long)seconds];
        }
        return resultStr;
    }
    // 秒转小时
    NSInteger hours = time / 3600;
    if (hours < 24) {
        return [NSString stringWithFormat:@"%ld小时前", hours];
    }
    //秒转天数
    NSInteger days = time / 3600 / 24;
    if (days < 30) {
        if (days < 7) {
            return [NSString stringWithFormat:@"%ld天前", days];
        } else {
            return [NSString stringWithFormat:@"%ld星期前", days / 7];
        }
    }
    //秒转月
    NSInteger months = time / 3600 / 24 / 30;
    if (months < 12) {
        return [NSString stringWithFormat:@"%ld个月前", months];
    }
    //秒转年
    NSInteger years = time / 3600 / 24 / 30 / 12;
    return [NSString stringWithFormat:@"%ld年前", years];
}

+ (BOOL)isSameDay:(NSDate *)date1 date2:(NSDate *)date2 {
    NSCalendar *calendar = [NSCalendar currentCalendar];

    unsigned unitFlags = NSCalendarUnitYear | NSCalendarUnitMonth | NSCalendarUnitDay;
    NSDateComponents *comp1 = [calendar components:unitFlags fromDate:date1];
    NSDateComponents *comp2 = [calendar components:unitFlags fromDate:date2];

    return [comp1 day]   == [comp2 day] &&
           [comp1 month] == [comp2 month] &&
           [comp1 year]  == [comp2 year];
}

+ (BOOL)isToday:(NSString *)dateString {
    NSDate *inputDate = [NSDate dateWithTimeIntervalSince1970:[dateString doubleValue] / 1000];
//    NSTimeZone *zone = [NSTimeZone systemTimeZone];
//    NSInteger goalInterval = [zone secondsFromGMTForDate: inputDate];
//    NSDate *date = [inputDate  dateByAddingTimeInterval: goalInterval];

    return [[NSCalendar currentCalendar] isDateInToday:inputDate];
}

+ (BOOL)isYesterday:(NSString *)dateString {
    NSDate *nowDate = [NSDate date];
    NSDate *inputDate = [NSDate dateWithTimeIntervalSince1970:[dateString doubleValue] / 1000];
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *cmps = [calendar components:NSCalendarUnitDay fromDate:inputDate toDate:nowDate options:0];
    return cmps.day == 1;
}

+ (BOOL)isThisYear:(NSString *)dateString {
    NSDate *inputDate = [NSDate dateWithTimeIntervalSince1970:[dateString doubleValue] / 1000];
    NSCalendar *calendar = [NSCalendar currentCalendar];
    int unit = NSCalendarUnitYear;
//    NSTimeZone *zone = [NSTimeZone systemTimeZone];
//    NSInteger goalInterval = [zone secondsFromGMTForDate: inputDate];
//    NSDate *date = [inputDate  dateByAddingTimeInterval: goalInterval];

    // 1.获得当前时间的年月日
    NSDateComponents *nowCmps = [calendar components:unit fromDate:[NSDate date]];
    // 2.需要确认的年月日
    double publishLong = [inputDate timeIntervalSince1970] * 1000;
    NSDate *publishDate = [NSDate dateWithTimeIntervalSince1970:publishLong / 1000];
    NSDateComponents *selfCmps = [calendar components:unit fromDate:publishDate];

    return nowCmps.year == selfCmps.year;
}

+ (NSString *)getNowTimeTimestamp {
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss SSS"];
    //设置时区
    NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Shanghai"];
    [formatter setTimeZone:timeZone];
    NSDate *datenow = [NSDate date];
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[datenow timeIntervalSince1970] * 1000];
    return timeSp;
}

///< 获取当前时间的: 前一周(day:-7)丶前一个月(month:-30)丶前一年(year:-1)的时间戳
+ (NSString *)getExpectTimestampYear:(NSInteger)year month:(NSUInteger)month day:(NSUInteger)day {
    NSDate *currentdata = [NSDate date];
    ///< NSCalendar -- 日历类，它提供了大部分的日期计算接口，并且允许您在NSDate和NSDateComponents之间转换
    NSCalendar *calendar = [[NSCalendar alloc] initWithCalendarIdentifier:NSCalendarIdentifierGregorian];
    NSDateComponents *datecomps = [[NSDateComponents alloc] init];
    [datecomps setYear:year?:0];
    [datecomps setMonth:month?:0];
    [datecomps setDay:day?:0];
    ///< dateByAddingComponents: 在参数date基础上，增加一个NSDateComponents类型的时间增量
    NSDate *calculatedate = [calendar dateByAddingComponents:datecomps toDate:currentdata options:0];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd hh:mm:ss SSS"];
    NSString *calculateStr = [formatter stringFromDate:calculatedate];
    NSString *result = [NSString stringWithFormat:@"%ld", (long)[calculatedate timeIntervalSince1970] * 1000];
    return result;
}

+ (NSArray *)getCurrentWeek {
    NSDate *now = [NSDate date];
    NSCalendar *calendar = [NSCalendar currentCalendar];
    NSDateComponents *comp = [calendar components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay|NSCalendarUnitWeekday
                                         fromDate:now];
    // 得到星期几
    // 1(星期天) 2(星期二) 3(星期三) 4(星期四) 5(星期五) 6(星期六) 7(星期天)
    NSInteger weekDay = [comp weekday];
    // 得到几号
    NSInteger day = [comp day];
    NSLog(@"weekDay:%ld  day:%ld",weekDay,day);
    
    // 计算当前日期和这周的星期一和星期天差的天数
    long firstDiff,lastDiff;
    if (weekDay == 1) {
        firstDiff = 1;
        lastDiff = 0;
    } else {
        firstDiff = [calendar firstWeekday] - weekDay;
        lastDiff = 7 - weekDay;
    }

    NSDateComponents *firstDayComp = [calendar components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:now];
    [firstDayComp setDay:day + firstDiff + 1];
    NSDate *firstDayOfWeek= [calendar dateFromComponents:firstDayComp];
    NSDateComponents *lastDayComp = [calendar components:NSCalendarUnitYear|NSCalendarUnitMonth|NSCalendarUnitDay fromDate:now];
    [lastDayComp setDay:day + lastDiff + 1];
    NSDate *lastDayOfWeek = [calendar dateFromComponents:lastDayComp];
    
    NSDateFormatter *formater = [[NSDateFormatter alloc] init];
    [formater setDateFormat:@"yyyy-MM-dd"];
    
    NSString *firstString = [formater stringFromDate:firstDayOfWeek];
    NSString *lastString = [formater stringFromDate:lastDayOfWeek];
    
    NSLog(@"一周开始 %@", firstString);
    NSLog(@"当前 %@",[formater stringFromDate:now]);
    NSLog(@"一周结束 %@",lastString);
    return @[firstString, lastString];
}

+ (NSArray *)getMonthFirstAndLastDayWith:(NSString *)dateStr {
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"yyyy-MM-dd"];
    NSDate *newDate = [format dateFromString:dateStr];
    double interval = 0;
    NSDate *firstDate = nil;
    NSDate *lastDate = nil;
    NSCalendar *calendar = [NSCalendar currentCalendar];

    BOOL OK = [calendar rangeOfUnit:NSCalendarUnitMonth startDate:&firstDate interval:&interval forDate:newDate];

    if (OK) {
        lastDate = [firstDate dateByAddingTimeInterval:interval - 1];
    } else {
        return @[@"", @""];
    }

    NSDateFormatter *myDateFormatter = [[NSDateFormatter alloc] init];
    [myDateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *firstString = [myDateFormatter stringFromDate:firstDate];
    NSString *lastString = [myDateFormatter stringFromDate:lastDate];
    return @[firstString, lastString];
}

+ (NSArray *)getYearFirstAndLastDayWith:(NSString *)dateStr {
    
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"yyyy-MM-dd"];
    NSDate *newDate = [format dateFromString:dateStr];
    double interval = 0;
    NSDate *firstDate = nil;
    NSDate *lastDate = nil;
    NSCalendar *calendar = [NSCalendar currentCalendar];

    BOOL OK = [calendar rangeOfUnit:NSCalendarUnitYear startDate:& firstDate interval:&interval forDate:newDate];
    
    if (OK) {
        lastDate = [firstDate dateByAddingTimeInterval:interval - 1];
    } else {
        return @[@"",@""];
    }

    NSDateFormatter *myDateFormatter = [[NSDateFormatter alloc] init];
    [myDateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *firstString = [myDateFormatter stringFromDate: firstDate];
    NSString *lastString = [myDateFormatter stringFromDate: lastDate];
    return @[firstString, lastString];
}

+ (NSArray *)getRecentDateWith:(NSDate *)currentDate day:(NSInteger)day {
    NSTimeInterval days = 24 * 60 * 60 * day;
    NSDate *appointDate = [currentDate dateByAddingTimeInterval:-days];
    
    NSDateFormatter *myDateFormatter = [[NSDateFormatter alloc] init];
    [myDateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *firstString = [myDateFormatter stringFromDate: appointDate];
    
    NSDate *lastDate = [currentDate dateByAddingTimeInterval:-24 * 60 * 60];
    NSString *lastString = [myDateFormatter stringFromDate: lastDate];

    return @[firstString, lastString];
}

+ (NSString *)dateWithTimeString:(NSString *)timeString fromFormat:(NSString *)format toFormat:(NSString *)toFormat {
    NSDate *monthDate = [NSDate date:timeString WithFormat:format];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setLocale:[NSLocale currentLocale]];
    [dateFormatter setTimeZone:[NSTimeZone localTimeZone]];
    [dateFormatter setDateFormat:toFormat];
    NSString *dateString = [dateFormatter stringFromDate:monthDate];
    return dateString;
}
//获取当前时间
+ (NSString*)getCurrentTimeWithFormat:(NSString *)format
{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    // ----------设置你想要的格式,hh与HH的区别:分别表示12小时制,24小时制
    [formatter setDateFormat:format];
    //现在时间,你可以输出来看下是什么格式
    NSDate *datenow = [NSDate date];
    //----------将nsdate按formatter格式转成nsstring
    NSString *currentTimeString = [formatter stringFromDate:datenow];
    return currentTimeString;
}

//NSDate转时间戳
+ (NSString *)dateConversionTimeStamp:(NSDate *)date
{
    NSString *timeSp = [NSString stringWithFormat:@"%ld", (long)[date timeIntervalSince1970]*1000];
    return timeSp;
}

@end
