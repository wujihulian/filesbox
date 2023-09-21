//
//  VULFromBaseRequest.h
//  UnlimitedBusiness
//
//  Created by SunTory on 2021/3/23.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "YTKBaseRequest.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULFromBaseRequest : YTKBaseRequest

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


+ (VULFromBaseRequest *)requestWithUrl:(NSString *)url
                            params:(nullable NSDictionary *)params
                       requestType:(YTKRequestMethod)requestMethod
                        completion:(VULRequestCompletion)completion;
@end

NS_ASSUME_NONNULL_END
