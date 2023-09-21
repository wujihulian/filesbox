//
//  UBBaseRequest.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/1/11.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "YTKRequest.h"

NS_ASSUME_NONNULL_BEGIN

@interface UBBaseRequest : YTKRequest

//@property (nonatomic, copy) NSString *message;
//@property (nonatomic, copy) NSString *code;
//@property (nonatomic, strong) id data;
//@property (nonatomic, assign) BOOL success;
//@property (nonatomic, copy) NSString *timeStamp;

@property(nonatomic,   copy) NSString *src;
@property(nonatomic, strong) NSMutableDictionary *params;
@property(nonatomic, assign) YTKRequestMethod requestMethod;


+ (UBBaseRequest *)requestWithUrl:(NSString *)url
                            params:(nullable NSDictionary *)params
                      requestType:(YTKRequestMethod)requestMethod;
@end

NS_ASSUME_NONNULL_END
