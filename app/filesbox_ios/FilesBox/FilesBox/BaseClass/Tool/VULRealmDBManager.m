//
//  VULRealmDBManager.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/10.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULRealmDBManager.h"

@implementation VULRealmDBManager

+ (void)increased:(RLMObject *)model {
    RLMRealm *realm = [RLMRealm defaultRealm];
    [realm beginWriteTransaction];
    [realm addObject:model];
    [realm commitWriteTransaction];
}

+ (void)deleted:(RLMObject *)model {
    RLMRealm *realm = [RLMRealm defaultRealm];
    [realm beginWriteTransaction];
    [realm deleteObject:model];
    [realm commitWriteTransaction];
}


+ (void)update:(RLMObject *)model {
    RLMRealm *realm = [RLMRealm defaultRealm];
    [realm beginWriteTransaction];
    [realm addOrUpdateObject:model];
    [realm commitWriteTransaction];
}


/**
 更新token
 
 @param token 新token
 */
+ (void)updateLocaToken:(NSString *)token andUserID:(NSString *)userId {
    RLMRealm *realm = [RLMRealm defaultRealm];
    RLMResults<VULSaveUserToken *> *savedTokens = [VULSaveUserToken allObjects];
    VULSaveUserToken *userToken = [[VULSaveUserToken alloc] initWithToken:token andUserID:userId];
    [realm beginWriteTransaction];
    if (savedTokens.count != 0) {
        [realm deleteObjects:savedTokens];
    }
    [realm addObject:userToken];
    [realm commitWriteTransaction];
}

+ (NSString *)getLocalToken {
    RLMResults<VULSaveUserToken *> *userTokens = [VULSaveUserToken allObjects];
    return userTokens.count == 0 ? nil : userTokens.firstObject.token;
    return @"";
}
+ (NSString *)getUserId {
    RLMResults<VULSaveUserToken *> *userTokens = [VULSaveUserToken allObjects];
    return userTokens.count == 0 ? nil : userTokens.firstObject.userID;
    return @"";
}


+ (void)updateLocaPersonalInformation:(VULResponseLoginModel *)model {
    RLMRealm *realm = [RLMRealm defaultRealm];
    
    VULSaveUserInformation *userInfomation = [[VULSaveUserInformation alloc] initWithTargetModel:model];
    
    RLMResults<VULSaveUserInformation *> *allUserInfoModels = [VULSaveUserInformation allObjects];
    
    [realm beginWriteTransaction];
    if (allUserInfoModels.count == 0) {
        [realm addObject:userInfomation];
    } else {
        [realm deleteObjects:allUserInfoModels];
        [realm addObject:userInfomation];
    }
    [realm commitWriteTransaction];
    
}

/**
 获取本地个人信息

 @return 本地个人信息
 */
+ (VULSaveUserInformation *)getLocalUserInformational {
    RLMResults<VULSaveUserInformation *> *userInformations = [VULSaveUserInformation allObjects];
    return userInformations.count == 0 ? nil : userInformations.firstObject;
}

/**
 删除用户信息
 */
+ (void)clearLocalUserInformation {
    RLMResults<VULSaveUserInformation *> *userInformations = [VULSaveUserInformation allObjects];
    RLMRealm *realm = [RLMRealm defaultRealm];
    
    [realm beginWriteTransaction];
    [realm deleteObjects:userInformations];
    [realm commitWriteTransaction];
}


@end
