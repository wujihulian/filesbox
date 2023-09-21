//
//  VULRequestHeaderDictionary.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/22.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULRequestHeaderDictionary.h"


@interface VULRequestHeaderDictionary()

@property (nonatomic, copy) NSString *version;/**< 当前版本号 */
@property (nonatomic, copy) NSString *os;/**< 当前操作系统(包括版本号) */
@property (nonatomic, copy) NSString *ostype;/**< 操作系统名称 不需要版本号(如：android,ios) */
@property (nonatomic, copy) NSString *token;/**< 用户登录后的jwt */
@property (nonatomic, copy) NSString *system;/**< 操作系统名称 不需要版本号(如：android,ios) */
@property (nonatomic, copy) NSString *systemversion;/**< 系统版本号 */
@property (nonatomic, copy) NSString *browsername;/**< 浏览器 如果APP 则传入ostype相同的值 */
@property (nonatomic, copy) NSString *broversion;/**< 浏览器版本号 APP传入APP版本号 */
@property (nonatomic, copy) NSString *vendor;/**< 手机型号 */
@property (nonatomic, copy) NSString *resolution;/**< 屏幕大小(1920x1080) */
@end

@implementation VULRequestHeaderDictionary

- (instancetype)init {
    if (self = [super init]) {
        NSString *strSysName = [[UIDevice currentDevice] systemName];
        NSString *strSysVersion = [[UIDevice currentDevice] systemVersion];
        self.version = kCurrentVersion;
        self.os = [NSString stringWithFormat:@"%@%@", strSysName, strSysVersion];
        self.ostype = VULOSTYPE;
        
        if (![VULRealmDBManager getLocalToken]) {
            self.token = @"";
        } else {
            self.token = [VULRealmDBManager getLocalToken];
        }
        self.system = @"ios";
        self.systemversion = strSysVersion;
        self.browsername = VULOSTYPE;
        self.broversion = self.version;
        self.vendor = [NSString getDeviceModelName];
        self.resolution = [NSString stringWithFormat:@"%f*%f", VULSCREEN_WIDTH, VULSCREEN_HEIGHT];
    }
    return self;
}


@end
