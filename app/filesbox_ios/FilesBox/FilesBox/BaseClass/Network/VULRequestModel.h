//
//  VULRequestModel.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULBaseRequest.h"

@interface VULRequestModel : VULBaseRequest

@end

//用户登录
@interface VULRequestLoginModel : VULBaseRequest
/**
 登陆接口

 @param username 用户名
 @param password 密码
 @param encrypted 是否加密 encrypted = YES加密 NO不加密 默认不加密
 @return LoginModel
 */
- (id)initWithUsername:(NSString *)username password:(NSString *)password encrypted:(BOOL)encrypted;

@end

#pragma mark --用户退出登录
@interface VULRequestLogoutModel : VULBaseRequest

@end


