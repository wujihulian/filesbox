//
//  VULRealmDBManager.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/10.
//  Copyright © 2018年 svnlan. All rights reserved.
//
//数据库 增删查改
//

#import <Foundation/Foundation.h>
#import <Realm/Realm.h>
#import "VULRLMModel.h"
#import "VULResponseModel.h"


@interface VULRealmDBManager : NSObject


/**
 增

 @param model 增加的model
 */
+ (void)increased:(RLMObject *)model;

/**
 删
 
 @param model 删除的model
 */
+ (void)deleted:(RLMObject *)model;


/**
 更新
 
 @param model 更新的model
 */
+ (void)update:(RLMObject *)model;


/**
 删除用户信息
 */
+ (void)clearLocalUserInformation;

/**
 更新token

 @param token 新token
 */
+ (void)updateLocaToken:(NSString *)token andUserID:(NSString *)userId;


/**
 获取数据库中的token
 
 */
+ (NSString *)getLocalToken;
/**
 更新本地用户信息
 
 @param model 用户信息Model
 */
+ (void)updateLocaPersonalInformation:(VULResponseLoginModel *)model;


/**
 获取本地用户信息

 @return 本地保存的用户信息
 */
+ (VULSaveUserInformation *)getLocalUserInformational;


/**
 删除用户信息
 */
+ (void)clearLocalUserInformation;

+ (NSString *)getUserId ;

@end
