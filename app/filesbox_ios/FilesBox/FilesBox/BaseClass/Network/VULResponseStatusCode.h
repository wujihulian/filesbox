//
//  VULResponseStatusCode.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/6.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
@class YTKBaseRequest;

@protocol VULResponseStatusCodeDelegate <NSObject>

@optional
//成功
- (void)success:(YTKBaseRequest *)request;

- (void)responseError:(YTKBaseRequest *)request;


//TODO: 这些暂时没用到
//参数错误
- (void)parametersError:(YTKBaseRequest *)request;
//服务器错误
- (void)serverError:(YTKBaseRequest *)request;
//设备限制
- (void)equipmentRestriction:(YTKBaseRequest *)request;

@end

@interface VULResponseStatusCode : NSObject

@property (nonatomic, weak) id <VULResponseStatusCodeDelegate>responseStatusCodeDelegate; /**< 状态码处理 代理 */

/**
 处理状态码

 @param request 返回数据
 */
- (void)responseStatusCode:(YTKBaseRequest *)request;

@end

