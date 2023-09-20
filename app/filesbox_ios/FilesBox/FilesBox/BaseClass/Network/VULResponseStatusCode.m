//
//  VULResponseStatusCode.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/6.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULResponseStatusCode.h"
#import "VULRequestModel.h"
@implementation VULResponseStatusCode

- (void)responseStatusCode:(YTKBaseRequest *)request {
    if ([request.responseObject[@"code"] integerValue] == 200 || [request.responseObject[@"errorCode"] integerValue] == 200) {
        if (self.responseStatusCodeDelegate && [self.responseStatusCodeDelegate respondsToSelector:@selector(success:)]) {
            [self.responseStatusCodeDelegate success:request];
        }
        return ;
    } else {
        if (self.responseStatusCodeDelegate && [self.responseStatusCodeDelegate respondsToSelector:@selector(responseError:)]) {
            [self.responseStatusCodeDelegate responseError:request];
        }
    }
    /*
     403 签名验证不存在
     */
    
//    if()
    
}

@end
