//
//  WXApiManger.h
//  VideoULimit
//
//  Created by yuekewei on 2020/4/29.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "WXApi.h"
#import "VULFileModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface WXApiManger : NSObject<WXApiDelegate>

@property (nonatomic, copy) void(^getWeChatCodeCompletion)(NSString *code);

+ (instancetype)shareInstance;

- (void)oauth_authorization;
-(void)shareMinInfo:(NSString *)info webpageUrl:(id)webpageUrl hdImageData:(id) hdImageData title:(NSString *)title des:(NSString *)des;
-(void)shareFileWithModel:(VULFileObjectModel *)model;
-(void)shareFileWithString:(NSString *)string scene:(int)scene;
-(void)shareInfoWithModel:(VULInfoModel *)model scene:(int)scene;
-(void)shareDingdingWithModel:(VULInfoModel *)model;
-(void)shareDingdingWithString:(NSString *)string;
-(void)shareWeiboWithModel:(VULInfoModel *)model;
-(void)shareWeiboWithString:(NSString *)string;

@end

NS_ASSUME_NONNULL_END
