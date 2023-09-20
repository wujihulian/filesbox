//
//  VULRequestModel.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULRequestModel.h"
#import "VULServerMacro.h"
#import "NSString+EXTENSION.h"

@implementation VULRequestModel

@end

@implementation VULRequestLoginModel {
    NSString *_username;
    NSString *_password;
    BOOL _encrypted;
}

- (id)initWithUsername:(NSString *)username password:(NSString *)password encrypted:(BOOL)encrypted {
    self = [super init];
    if (self) {
        _username = username;
        _encrypted = encrypted;
        if (encrypted) {
            NSString *aesPwd = [NSString encyptPKCS5:password WithKey:AESENCRYKEY];
            _password = aesPwd;
        } else {
            _password = password;
        }
    }
    return self;
}

- (NSString *)requestUrl {
    return LoginUrl;
}

- (YTKRequestMethod)requestMethod {
    return YTKRequestMethodPOST;
}

- (YTKRequestSerializerType)requestSerializerType {
    return YTKRequestSerializerTypeJSON;
}

- (id)requestArgument {
    
    return @{
             @"name": _username,
             @"password": _password,
             @"isGraphicCode": @"0"
             };
}

@end

#pragma mark --用户退出登录
@implementation VULRequestLogoutModel
- (NSString *)requestUrl {
    return LogoutUrl;
}

- (YTKRequestMethod)requestMethod {
    return YTKRequestMethodGET;
}

- (YTKRequestSerializerType)requestSerializerType {
    return YTKRequestSerializerTypeJSON;
}

- (id)requestArgument {
    return nil;
}

@end


