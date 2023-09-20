//
//  NSString+EXTENSION.m
//  VideoULimit
//
//  Created by ZCc on 2018/11/6.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "NSString+EXTENSION.h"
#import <CommonCrypto/CommonCryptor.h>
#import <CommonCrypto/CommonDigest.h>
#import <ifaddrs.h>
#import <arpa/inet.h>
#import <net/if.h>

#define IOS_CELLULAR @"pdp_ip0"
#define IOS_WIFI     @"en0"
#define IOS_VPN      @"utun0"
#define IP_ADDR_IPv4 @"ipv4"
#define IP_ADDR_IPv6 @"ipv6"

@implementation NSString (EXTENSION)

- (CGSize)sizewithFont:(UIFont *)font andMaxSize:(CGSize)maxSize {
    return [self boundingRectWithSize:maxSize options:NSStringDrawingUsesLineFragmentOrigin attributes:@{ NSFontAttributeName: font } context:nil].size;
}

- (CGSize)sizeWithAttributes:(NSDictionary *)attribute andMaxSize:(CGSize)maxSize {
    return [self boundingRectWithSize:maxSize options:NSStringDrawingUsesLineFragmentOrigin attributes:attribute context:nil].size;
}

/**
 字典转换为json字符串

 @param dic json字典
 */
+ (NSString *)getJsonStringWithDic:(NSDictionary *)dic {
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&error];
    NSString *jsonStr = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    NSMutableString *mutStr = [NSMutableString stringWithString:jsonStr];

    //去回车和空格
    NSRange range = { 0, jsonStr.length };
    [mutStr replaceOccurrencesOfString:@" " withString:@"" options:NSLiteralSearch range:range];
    NSRange range2 = { 0, mutStr.length };
    [mutStr replaceOccurrencesOfString:@"\n" withString:@"" options:NSLiteralSearch range:range2];

    return mutStr;
}

+ (NSString *)dateStrWithTimeInterval:(NSInteger)createTimeInterval {
    createTimeInterval = createTimeInterval / 1000;
    NSDate *date = [NSDate date];

    NSDate *createDate = [NSDate dateWithTimeIntervalSince1970:createTimeInterval];

    NSTimeInterval timeIntervalNow = [date timeIntervalSince1970];

    NSInteger dNum = timeIntervalNow - createTimeInterval; //现在秒数 - 过去秒数

    if (dNum < 60) {
        NSString *rTime;
        if (dNum <= 3) {
            rTime = @"刚刚";
        } else {
            rTime = [NSString stringWithFormat:@"%ld秒前", (long)dNum];
        }
        return rTime;
    } else if (dNum >= 60 && dNum < 3600) {
        NSInteger timeNum = dNum / 60;
        return [NSString stringWithFormat:@"%ld分钟前", (long)timeNum];
    } else if (dNum >= 3600 && dNum <= 86400) {
        NSInteger timeNum = dNum / 3600;
        return [NSString stringWithFormat:@"%ld小时前", (long)timeNum];
    } else if (dNum >= 86400 && dNum < 86400 * 7) {
        NSInteger timeNum = dNum / 86400;
        return [NSString stringWithFormat:@"%ld天前", (long)timeNum];
    } else {
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        df.timeZone = [NSTimeZone systemTimeZone];
        df.dateFormat = @"yyyy年M月d日";

        NSString *createYearStr = [df stringFromDate:createDate];
        NSInteger createYearNum = [[createYearStr substringToIndex:4] integerValue];
        NSString *dateNowStr = [df stringFromDate:date];
        NSInteger dateNowNum = [[dateNowStr substringToIndex:4] integerValue];

        if (dateNowNum != createYearNum) {
            return createYearStr;
        } else {
            return [createYearStr substringFromIndex:5];
        }
    }
}

#pragma mark - 判断图片是否是gif
+ (BOOL)isGifWithImageData:(NSData *)data {
    if ([[self contentTypeWithImageData:data] isEqualToString:@"gif"]) {
        return YES;
    }
    return NO;
}

+ (NSString *)contentTypeWithImageData:(NSData *)data {
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
        case 0x52:
            if ([data length] < 12) {
                return nil;
            }
            NSString *testString = [[NSString alloc] initWithData:[data subdataWithRange:NSMakeRange(0, 12)] encoding:NSASCIIStringEncoding];

            if ([testString hasPrefix:@"RIFF"] && [testString hasSuffix:@"WEBP"]) {
                return @"webp";
            }
            return nil;
    }
    return nil;
}

#pragma mark - 拼接cookie
+ (NSString *)readCurrentCookieWith:(NSDictionary *)dic {
    if (dic == nil) {
        return nil;
    } else {
        NSHTTPCookieStorage *cookieJar = [NSHTTPCookieStorage sharedHTTPCookieStorage];
        NSMutableString *cookieString = [[NSMutableString alloc] init];
        for (NSHTTPCookie *cookie in [cookieJar cookies]) {
            [cookieString appendFormat:@"%@=%@;", cookie.name, cookie.value];
        }
        //删除最后一个“；”
        [cookieString deleteCharactersInRange:NSMakeRange(cookieString.length - 1, 1)];
        return cookieString;
    }
}

#pragma mark - 正则<img>src
+ (NSArray *)getImageSrcWithString:(NSString *)htmlString {
    NSString *regularStr = @"<\\s*img\\s+[^>]*?src\\s*=\\s*[\'\"](.*?)[\'\"]\\s*(alt=[\'\"](.*?)[\'\"])?[^>]*?\\/?\\s*>";
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:regularStr options:NSRegularExpressionCaseInsensitive error:nil];
    NSArray *arrayOfAllMatches = [regex matchesInString:htmlString options:0 range:NSMakeRange(0, [htmlString length])];
    NSMutableArray *srcArr = [NSMutableArray arrayWithCapacity:0];
    for (NSTextCheckingResult *match in arrayOfAllMatches) {
        NSRange range = match.range;
        range.location = 1;
        NSString *substringForMatch = [htmlString substringWithRange:match.range];
        NSLog(@"=== substringForMatch %@", substringForMatch);
        // 提取 src =》
        NSString *srcStr = [htmlString substringWithRange:[match rangeAtIndex:1]];
        NSLog(@"=== substringForMatch %@", srcStr);
        if (NSStringIsNotEmpty(srcStr)) {
            [srcArr addObject:srcStr];
        }
    }
    return srcArr;
}

/**
 * 时间(秒)转String, 格式(xx:xx:xx)
 */
+ (NSString *)timeStringFromInteger:(NSInteger)seconds {
    NSInteger hours, remainder, minutesm, secs;

    hours = seconds / 3600;

    remainder = seconds % 3600;

    minutesm = remainder / 60;

    secs = remainder % 60;

    return [NSString stringWithFormat:@"%@:%@:%@",
            (hours < 10 ? [NSString stringWithFormat:@"0%@", [[NSNumber numberWithInteger:hours] stringValue]] : [[NSNumber numberWithInteger:hours] stringValue]),
            (minutesm < 10 ? [NSString stringWithFormat:@"0%@", [[NSNumber numberWithInteger:minutesm] stringValue]] : [[NSNumber numberWithInteger:minutesm] stringValue]),
            (secs < 10 ? [NSString stringWithFormat:@"0%@", [[NSNumber numberWithInteger:secs] stringValue]] : [[NSNumber numberWithInteger:secs] stringValue])];
}

//字符串AES加密(16进制)
+ (NSString *)encyptPKCS5:(NSString *)plainText WithKey:(NSString *)key {
    //把string 转NSData
    NSData *data = [plainText dataUsingEncoding:NSUTF8StringEncoding];

    //length
    size_t plainTextBufferSize = [data length];

    const void *vplainText = (const void *)[data bytes];

    uint8_t *bufferPtr = NULL;
    size_t bufferPtrSize = 0;
    size_t movedBytes = 0;

    bufferPtrSize = (plainTextBufferSize + kCCBlockSizeAES128) & ~(kCCBlockSizeAES128 - 1);
    bufferPtr = malloc(bufferPtrSize * sizeof(uint8_t));
    memset((void *)bufferPtr, 0x0, bufferPtrSize);

    const void *vkey = (const void *)[key UTF8String];
    //配置CCCrypt
    CCCryptorStatus ccStatus = CCCrypt(kCCEncrypt,
                                       kCCAlgorithmAES128, //3DES
                                       kCCOptionECBMode | kCCOptionPKCS7Padding, //设置模式
                                       vkey,    //key
                                       kCCKeySizeAES128,
                                       nil,     //偏移量，这里不用，设置为nil;不用的话，必须为nil,不可以为@“”
                                       vplainText,
                                       plainTextBufferSize,
                                       (void *)bufferPtr,
                                       bufferPtrSize,
                                       &movedBytes);
    if (ccStatus == kCCSuccess) {
        NSData *myData = [NSData dataWithBytes:(const char *)bufferPtr length:(NSUInteger)movedBytes];
        //base64
        NSString *encodeStr = [myData base64EncodedStringWithOptions:NSDataBase64Encoding64CharacterLineLength];
//        //16进制(你也可以换成base64等)
//        NSUInteger          len = [myData length];
//        char *              chars = (char *)[myData bytes];
//        NSMutableString *   hexString = [[NSMutableString alloc] init];
//
//        for(NSUInteger i = 0; i < len; i++ )
//            [hexString appendString:[NSString stringWithFormat:@"%0.2hhx", chars[i]]];
//        return hexString;
        return encodeStr;
    }

    free(bufferPtr);
    return nil;
}

//视频切片上传 md5加密
+ (NSString *)getMD5WithFilePath:(NSURL *)path {
    NSFileHandle *handle = [NSFileHandle fileHandleForReadingFromURL:path error:nil];
    if (handle == nil) {
        return nil;
    }
    CC_MD5_CTX md5;
    CC_MD5_Init(&md5);
    BOOL done = NO;
    while (!done) {
        NSData *fileData = [handle readDataOfLength:256 ];
        CC_MD5_Update(&md5, [fileData bytes], (unsigned)[fileData length]);
        //        CC_MD5_Update(&md5, [fileData bytes], [fileData length]);
        if ([fileData length] == 0) done = YES;
    }
    unsigned char digest[CC_MD5_DIGEST_LENGTH];
    CC_MD5_Final(digest, &md5);
    NSString *s = [NSString stringWithFormat:@"%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x",
                   digest[0], digest[1],
                   digest[2], digest[3],
                   digest[4], digest[5],
                   digest[6], digest[7],
                   digest[8], digest[9],
                   digest[10], digest[11],
                   digest[12], digest[13],
                   digest[14], digest[15]];

    return s;
}

#pragma mark - MD5加密 32位 小写
+ (NSString *)MD5ForLower32Bate:(NSString *)str {
    //要进行UTF8的转码
    const char *input = [str UTF8String];
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    CC_MD5(input, (CC_LONG)strlen(input), result);

    NSMutableString *digest = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];
    for (NSInteger i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [digest appendFormat:@"%02x", result[i]];
    }

    return digest;
}

#pragma mark -获取网络ip
+ (void)getDeviceIPAddressResult:(void (^)(NSString *ipAddress))ipStr {
    NSURL *url = [NSURL URLWithString:@"http://ipinfo.io/json"];
//    NSURL *url = [NSURL URLWithString:@"https://www.ipify.org/"];

    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestReloadRevalidatingCacheData timeoutInterval:20];
    //不使用保存的 cookie
    [request setHTTPShouldHandleCookies:NO];
    request.HTTPMethod = @"GET";

    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *dataTask = [session dataTaskWithRequest:request completionHandler:^(NSData *_Nullable data, NSURLResponse *_Nullable response, NSError *_Nullable error) {
        if (!error) {
            NSString *jsonString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
            NSDictionary *resultDict = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers error:nil];

            dispatch_async(dispatch_get_main_queue(), ^{
                               ipStr([resultDict valueForKeyPath:@"ip"]);
                           });
        } else {
            NSLog(@"获取IP信息错误 - %@", error);
        }
    }];
    [dataTask resume];
}

#pragma mark -获取本地局域网ip
+ (NSString *)getIPv4Address {
    NSArray *searchArray = @[ IOS_VPN @"/" IP_ADDR_IPv4, IOS_VPN @"/" IP_ADDR_IPv6, IOS_WIFI @"/" IP_ADDR_IPv4, IOS_WIFI @"/" IP_ADDR_IPv6, IOS_CELLULAR @"/" IP_ADDR_IPv4, IOS_CELLULAR @"/" IP_ADDR_IPv6 ];

    NSDictionary *addresses = [self getIPAddresses];

    __block NSString *address;
    [searchArray enumerateObjectsUsingBlock:^(NSString *key, NSUInteger idx, BOOL *stop) {
        address = addresses[key];
        //筛选出IPv4地址
        if ([self isValidatIP:address]) {
            *stop = YES;
        }
    }];
    return address ? address : @"0.0.0.0";
}

+ (NSDictionary *)getIPAddresses {
    NSMutableDictionary *addresses = [NSMutableDictionary dictionaryWithCapacity:8];

    // retrieve the current interfaces - returns 0 on success
    struct ifaddrs *interfaces;
    if (!getifaddrs(&interfaces)) {
        // Loop through linked list of interfaces
        struct ifaddrs *interface;
        for (interface = interfaces; interface; interface = interface->ifa_next) {
            if (!(interface->ifa_flags & IFF_UP) /* || (interface->ifa_flags & IFF_LOOPBACK) */) {
                continue; // deeply nested code harder to read
            }
            const struct sockaddr_in *addr = (const struct sockaddr_in *)interface->ifa_addr;
            char addrBuf[ MAX(INET_ADDRSTRLEN, INET6_ADDRSTRLEN) ];
            if (addr && (addr->sin_family == AF_INET || addr->sin_family == AF_INET6)) {
                NSString *name = [NSString stringWithUTF8String:interface->ifa_name];
                NSString *type;
                if (addr->sin_family == AF_INET) {
                    if (inet_ntop(AF_INET, &addr->sin_addr, addrBuf, INET_ADDRSTRLEN)) {
                        type = IP_ADDR_IPv4;
                    }
                } else {
                    const struct sockaddr_in6 *addr6 = (const struct sockaddr_in6 *)interface->ifa_addr;
                    if (inet_ntop(AF_INET6, &addr6->sin6_addr, addrBuf, INET6_ADDRSTRLEN)) {
                        type = IP_ADDR_IPv6;
                    }
                }
                if (type) {
                    NSString *key = [NSString stringWithFormat:@"%@/%@", name, type];
                    addresses[key] = [NSString stringWithUTF8String:addrBuf];
                }
            }
        }
        // Free memory
        freeifaddrs(interfaces);
    }
    return [addresses count] ? addresses : nil;
}

+ (BOOL)isValidatIP:(NSString *)ipAddress {
    if (ipAddress.length == 0) {
        return NO;
    }

    NSString *urlRegEx = @"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    NSError *error;
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:urlRegEx options:0 error:&error];

    if (regex != nil) {
        NSTextCheckingResult *firstMatch = [regex firstMatchInString:ipAddress options:0 range:NSMakeRange(0, [ipAddress length])];
        if (firstMatch) {
            NSRange resultRange = [firstMatch rangeAtIndex:0];
            NSString *result = [ipAddress substringWithRange:resultRange];
            NSLog(@"IP = %@", result);
            return YES;
        }
    }
    return NO;
}

+ (id)jsonToObject:(NSString *)json {
    if (!NSStringIsNotEmpty(json)) {
        return nil;
    }
    NSData *jsonData = [json dataUsingEncoding:NSUTF8StringEncoding];

    NSError *err;
    //json解析
    id obj = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableContainers error:&err];
    if (err) {
        NSLog(@"json解析失败：%@", err);
        return nil;
    }
    return obj;
}

+ (NSString *)objectToJson:(id)obj {
    if (obj == nil) {
        return nil;
    }
    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:obj
                                                       options:0
                                                         error:&error];

    if ([jsonData length] && error == nil) {
        return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    } else {
        return nil;
    }
}

//获取文字内容
+ (NSString *)getZZwithString:(NSString *)string {/* <[^>]*>|\n */
    //    />(.+)</  <span[^>]*>
    if (!NSStringIsNotEmpty(string)) {
        return nil;
    }
    NSRegularExpression *regularExpretion = [NSRegularExpression regularExpressionWithPattern:@"<[^>]*>|\n" options:0 error:nil];
    string = [regularExpretion stringByReplacingMatchesInString:string options:NSMatchingReportProgress range:NSMakeRange(0, string.length) withTemplate:@""];
    return string;
}

//HTML标签<> 转义
+ (NSString *)getTransferredwithString:(NSString *)string sort:(BOOL)isPositive {
    if (isPositive) {
        string = [string stringByReplacingOccurrencesOfString:@"<"withString:@"&lt;"];
        string = [string stringByReplacingOccurrencesOfString:@">"withString:@"&gt;"];
    } else {
        string = [string stringByReplacingOccurrencesOfString:@"&lt;"withString:@"<"];
        string = [string stringByReplacingOccurrencesOfString:@"&gt;"withString:@">"];
    }
    return string;
}

+ (BOOL)isPureInteger:(NSString *)string {
    NSScanner *scan = [NSScanner scannerWithString:string];
    NSInteger val;
    return [scan scanInteger:&val] && [scan isAtEnd];
}

+ (BOOL)isUrlAddress:(NSString *)url {
    NSString *reg = @"((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";

    NSPredicate *urlPredicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", reg];
    return [urlPredicate evaluateWithObject:url];
}

+ (NSInteger)getRandomNumberFrom:(int)fromNum to:(int)toNum {
    return (NSInteger)(fromNum + (arc4random() % (toNum - fromNum + 1)));
}

//判断手机号
+ (BOOL)validateMobile:(NSString *)mobile {
    
    NSString *phoneRegex = @"^1(3[0-9]|4[56789]|5[0-9]|6[6]|7[0-9]|8[0-9]|9[189])\\d{8}$";

//    NSString *phoneRegex = @"^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[5,7])|(17[0-9]))\\d{8}$";

    NSString *mobileString = [mobile stringByReplacingOccurrencesOfString:@" " withString:@""];
    NSPredicate *phoneTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", phoneRegex];
    return [phoneTest evaluateWithObject:mobileString];
}

//判断是否以字母开头
+ (BOOL)isEnglishFirst:(NSString *)str {
    NSString *regular = @"^[A-Za-z].+$";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", regular];

    if ([predicate evaluateWithObject:str] == YES) {
        return YES;
    } else {
        return NO;
    }
}

+ (NSString *)weekdayStringWithDate:(NSDate *)date {
    //获取周几
    NSDateComponents *componets = [[NSCalendar autoupdatingCurrentCalendar] components:NSCalendarUnitWeekday fromDate:date];
    NSInteger weekday = [componets weekday];//1代表周日，2代表周一，后面依次
    NSArray *weekArray = @[@"周日", @"周一", @"周二", @"周三", @"周四", @"周五", @"周六"];
    NSString *weekStr = weekArray[weekday - 1];
    return weekStr;
}

+ (NSString *)timeFormatted:(NSString *)seconds {
//    int seconds = totalSeconds % 60;
    int totalSeconds = [seconds intValue];
    int minutes = (totalSeconds / 60) % 60;
    int hours = totalSeconds / 3600;

    NSString *resultStr = [NSString stringWithFormat:@"%d小时%d分钟", hours, minutes];
    if (hours == 0) {
        resultStr = [NSString stringWithFormat:@"%d分钟", minutes];
    }
    return resultStr;
}

+ (NSDictionary *)getBackViewTimeWithString:(NSString *)backString {
    NSDictionary *resultDic = [NSMutableDictionary dictionaryWithCapacity:0];

    NSArray *array = [backString componentsSeparatedByString:@","];
    NSMutableString *firstStr = [NSMutableString stringWithCapacity:0];
    NSMutableString *lastStr = [NSMutableString stringWithCapacity:0];
    NSString *start = array.firstObject;
    NSString *end = array.lastObject;

    if (start.length > 3) {
        [firstStr appendFormat:@"%@", [start substringWithRange:NSMakeRange(2, start.length - 3)]];
    }
    if (end.length > 3) {
        [lastStr appendFormat:@"%@", [end substringWithRange:NSMakeRange(1, end.length - 3)]];
    }

    [resultDic setValue:firstStr forKey:@"start"];
    [resultDic setValue:lastStr forKey:@"end"];

    return resultDic;
}

//小数点后position位 四舍五入
+ (NSString *)roundUp:(float)number afterPoint:(int)position {
    NSDecimalNumberHandler *roundingBehavior = [NSDecimalNumberHandler decimalNumberHandlerWithRoundingMode:NSRoundUp scale:position raiseOnExactness:NO raiseOnOverflow:NO raiseOnUnderflow:NO raiseOnDivideByZero:NO];
    NSDecimalNumber *ouncesDecimal;
    NSDecimalNumber *roundedOunces;
    ouncesDecimal = [[NSDecimalNumber alloc] initWithFloat:number];
    roundedOunces = [ouncesDecimal decimalNumberByRoundingAccordingToBehavior:roundingBehavior];
    return [NSString stringWithFormat:@"%@", roundedOunces];
}

+ (NSString *)getDealNumwithstring:(NSString *)string {
    if (!string) {
        return @"0";
    }
    float numValue = [string floatValue];
    
    if (numValue >= 10000) {
        float newNumber = numValue / 10000.0;
        NSString *resultStr = [NSString stringWithFormat:@"%.1fw", newNumber];
        
        return resultStr;
    } else {
        return string;
    }
    
    
    
//    NSDecimalNumber *numberA = [NSDecimalNumber decimalNumberWithString:string];
//    NSDecimalNumber *numberB;
//    NSString *unitStr;
//    NSInteger numberV = [string integerValue];
//    NSString *newString = [NSString stringWithFormat:@"%ld", numberV];
//
////    if (newString.length > 3 && newString.length < 5) {
////        numberB =  [NSDecimalNumber decimalNumberWithString:@"1000"];
////        unitStr = @"K";
////        if ([newString integerValue] % 1000 != 0) {
////            unitStr = @"K+";
////        }
////    } else
//
//    if (newString.length >= 5) {
//        numberB =  [NSDecimalNumber decimalNumberWithString:@"10000"];
//        unitStr = @"W";
//        if ([newString integerValue] % 10000 != 0) {
//            unitStr = @"W+";
//        }
//    } else {
//        return string;
//    }
//    NSDecimalNumberHandler *roundingBehavior = [NSDecimalNumberHandler decimalNumberHandlerWithRoundingMode:NSRoundDown scale:1 raiseOnExactness:NO raiseOnOverflow:NO raiseOnUnderflow:NO raiseOnDivideByZero:NO];
//
//    /// 这里不仅包含Multiply还有加 减 乘。
//    NSDecimalNumber *numResult = [numberA decimalNumberByDividingBy:numberB withBehavior:roundingBehavior];
//    NSString *strResult = [NSString stringWithFormat:@"%@%@", [numResult stringValue], unitStr];
//    return strResult;
}

+ (NSString *)stringWithDecimalNumber:(double)num {
    return [[self decimalNumber:num] stringValue];
}

+ (NSDecimalNumber *)decimalNumber:(double)num {
    NSString *numString = [NSString stringWithFormat:@"%lf", num];
    return [NSDecimalNumber decimalNumberWithString:numString];
}

+ (float)floatWithdecimalNumber:(double)num {
    return [[self decimalNumber:num] floatValue];
}

+ (double)doubleWithdecimalNumber:(double)num {
    return [[self decimalNumber:num] doubleValue];
}

@end
