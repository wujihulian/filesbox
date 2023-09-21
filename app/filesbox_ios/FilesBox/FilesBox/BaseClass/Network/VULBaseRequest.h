//
//  VULBaseRequest.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/10.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "YTKRequest.h"
#import "YTKNetworkAgent.h"
NS_ASSUME_NONNULL_BEGIN

@interface VULBaseRequest : YTKBaseRequest

typedef void(^VULRequestCompletion)(__kindof VULBaseRequest * _Nonnull request);

@property (nonatomic, copy) NSString *message;
@property (nonatomic, copy) NSString *code;
@property (nonatomic, strong) id data;
@property (nonatomic, assign) BOOL success;
//@property (nonatomic, assign) NSTimeInterval timeStamp;
@property (nonatomic, copy) NSString *timeStamp;

@property(nonatomic,   copy) NSString *src;
@property(nonatomic, strong) NSMutableDictionary *params;
@property(nonatomic, assign) YTKRequestMethod requestMethod;


+ (VULBaseRequest *)requestWithUrl:(NSString *)url
                            params:(nullable NSDictionary *)params
                       requestType:(YTKRequestMethod)requestMethod
                        completion:(VULRequestCompletion)completion;
@end

NS_ASSUME_NONNULL_END
