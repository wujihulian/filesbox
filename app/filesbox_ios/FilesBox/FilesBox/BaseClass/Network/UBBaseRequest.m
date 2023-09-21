//
//  UBBaseRequest.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/1/11.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "UBBaseRequest.h"

@implementation UBBaseRequest

+ (UBBaseRequest *)requestWithUrl:(NSString *)url
                            params:(nullable NSDictionary *)params
                       requestType:(YTKRequestMethod)requestMethod {
    
    UBBaseRequest *httpRequest = [UBBaseRequest new];
    httpRequest.requestMethod = requestMethod;
    httpRequest.src = url;
    httpRequest.params = [NSMutableDictionary dictionaryWithDictionary:params];
    
    return httpRequest;
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
    return YTKRequestSerializerTypeJSON;
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
    NSDictionary *headerDic = @{@"token" : token,
                                @"version" : kCurrentVersion,
                                @"device" : [NSString stringWithFormat:@"%@:%@-%@",deviceName,strSysName, strSysVersion],
                                @"resolution" : resolution,
                                @"client-type": client_type,
                                @"browsername": VULOSTYPE
    };
    return headerDic;
}


- (NSString *)getRequestMethodName {
    NSArray *array = @[@"GET",@"POST",@"HEAD",@"PUT",@"DELETE",@"PATCH"];
    return [array objectAtIndex:(NSInteger)[self requestMethod]];
}


@end
