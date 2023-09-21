//
//  VULFromBaseRequest.m
//  UnlimitedBusiness
//
//  Created by SunTory on 2021/3/23.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULFromBaseRequest.h"
#import "YTKNetworkAgent.h"
#import "VULRealmDBManager.h"
#import "NSObject+CYLTabBarControllerExtention.h"

@implementation VULFromBaseRequest

+ (VULFromBaseRequest *)requestWithUrl:(NSString *)url
                            params:(nullable NSDictionary *)params
                       requestType:(YTKRequestMethod)requestMethod
                        completion:(VULRequestCompletion)completion {
    
    VULFromBaseRequest *httpRequest = [VULFromBaseRequest new];
    httpRequest.requestMethod = requestMethod;
    httpRequest.src = url;
    httpRequest.params = [NSMutableDictionary dictionaryWithDictionary:params];
    
    [httpRequest startWithCompletionBlockWithSuccess:^(__kindof YTKBaseRequest * _Nonnull request) {
        [httpRequest handleRequest];
        if (completion) {
            completion(request);
        }
    } failure:^(__kindof YTKBaseRequest * _Nonnull request) {
        [httpRequest handleRequest];
        if (completion) {
            completion(request);
        }
    }];
    
    return httpRequest;
}

- (void)startWithCompletionBlockWithSuccess:(YTKRequestCompletionBlock)success failure:(YTKRequestCompletionBlock)failure {
    
    [super startWithCompletionBlockWithSuccess:^(__kindof YTKBaseRequest * _Nonnull request) {
        NetLog(@"\n发起请求: %@ %@\nHeader:\n%@\n传入参数:\n%@\n",
               [self getRequestMethodName],
               [[YTKNetworkAgent sharedAgent] buildRequestUrl:request],
               request.requestHeaderFieldValueDictionary,
               [request requestArgument] ?: [NSDictionary new]
               );
        if (ISNetLog) {
            NSString *jsonString = [NSString stringWithFormat: @"请求地址:\n%@\n返回数据:\n%@",
                                    [[YTKNetworkAgent sharedAgent] buildRequestUrl:request],
                                    request.responseObject];
            fprintf(stderr, "%s:%d\t%s\n",
                    [[[NSString stringWithUTF8String: __FILE__] lastPathComponent] UTF8String],
                    __LINE__,
                    [jsonString UTF8String]);
        }
        
        if (success) {
            success(request);
        }
        
    } failure:^(__kindof YTKBaseRequest * _Nonnull request) {
        NetLog(@"\n发起请求: %@ %@\nHeader:\n%@\n传入参数:\n%@\n",
               [self getRequestMethodName],
               [[YTKNetworkAgent sharedAgent] buildRequestUrl:request],
               request.requestHeaderFieldValueDictionary,
               [request requestArgument] ?: [NSDictionary new]
               );
        
        NetLog(@"请求地址:\n%@\nResponse:\n%@\n错误信息:\n%@",
               [[YTKNetworkAgent sharedAgent] buildRequestUrl:request],
               request.response,
               request.error
               );
        if (failure) {
            failure(request);
        }
    }];
}

- (NSString *)requestUrl {
    return _src;
}

- (YTKRequestMethod)requestMethod {
    return _requestMethod;
}

- (id)requestArgument {
    return _params;
}

- (YTKRequestSerializerType)requestSerializerType {
    return YTKRequestSerializerTypeHTTP;
}

- (NSDictionary<NSString *,NSString *> *)requestHeaderFieldValueDictionary {
    
    NSString *token = [VULRealmDBManager getLocalToken];
    if(token == nil) {
        token = @"";
    }
    NSString *strSysName = [[UIDevice currentDevice] systemName];
    NSString *strSysVersion = [[UIDevice currentDevice] systemVersion];
    NSString *deviceName =  [NSString getDeviceModelName];
    NSString *resolution = [NSString stringWithFormat:@"%.0lf*%.0lf", VULSCREEN_WIDTH, VULSCREEN_HEIGHT];
    NSString *client_type = @"4";
    NSString *uuidStr = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    NSString *nowDate = [NSDate getNowTimeTimestamp];
    [uuidStr stringByReplacingOccurrencesOfString:@"-" withString:@""];
    NSDictionary *headerDic = @{@"token" : token,
                                @"version" : kCurrentVersion,
                                @"device" : [NSString stringWithFormat:@"%@:%@-%@",deviceName,strSysName, strSysVersion],
                                @"resolution" : resolution,
                                @"client-type": client_type,
                                @"browsername": VULOSTYPE,
                                @"User-Agent" : [NSString getCurrentDeviceUserAgent],
                                @"uvcid" : [NSString stringWithFormat:@"%@%@",uuidStr,nowDate]
    };
    return headerDic;
}

- (void)handleRequest {
    [self modelSetWithJSON:self.responseJSONObject];
    _success = self.code.integerValue == 200;
    if (!_success && !self.response) {
        self.message = @"网络错误,请稍候再试";
    }
    if (self.code.integerValue == 401) {
//        [[VULAccountManager sharedInstance] needLoginAgain];
//        [[VULAccountManager sharedInstance] changeToken];

    }
}

- (NSString *)getRequestMethodName {
    NSArray *array = @[@"GET",@"POST",@"HEAD",@"PUT",@"DELETE",@"PATCH"];
    return [array objectAtIndex:(NSInteger)[self requestMethod]];
}
@end
