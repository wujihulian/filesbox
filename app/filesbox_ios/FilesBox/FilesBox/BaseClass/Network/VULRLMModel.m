//
//  VULRLMModel.m
//  VideoULimit
//
//  Created by svnlan on 2018/11/10.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULRLMModel.h"

@implementation VULRLMModel

@end


@implementation VULSaveUserToken

- (instancetype)initWithToken:(NSString *)token andUserID:(NSString *)userId {
    if (self = [super init]) {
        self.token = token;
        self.userID = userId;
    }
    return self;
}

@end
@implementation VULSaveUserInformation

- (instancetype)initWithTargetModel:(VULResponseLoginModel *)model {
    if (self = [super init]) {
        NSDictionary *json = [model modelToJSONObject];
        self = [VULSaveUserInformation modelWithJSON:json];
    }
    return self;

}
@end
