//
//  VULWebSocket.h
//  VideoULimit
//
//  Created by ZCc on 2018/11/5.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
//webscoket
#import "SRWebSocket.h"

NS_ASSUME_NONNULL_BEGIN

@protocol VULWebSocketDelegate <NSObject>

@optional
/**
 WebSocket接收到消息
 
 @param webSocket WebSocket
 @param message 接收到的消息
 */
- (void)vul_webSocket:(SRWebSocket *)webSocket didReceiveMessage:(id)message;

/**
 打开WebSocket
 
 @param webSocket WebSocket
 */
- (void)vul_webSocketDidOpen:(SRWebSocket *)webSocket;

@end

@interface VULWebSocket : NSObject

@property (nonatomic, copy) NSString *webSocketUrlStr;/**< 聊天地址 */

/**
 单例
 
 @return 单例初始化后的对象
 */
+ (instancetype)sharedVULWebSocket;
/**
 WebSocket是否在线
 
 @return 是否在线
 */
- (BOOL)isWebSocketing;
/**
 打开WebSocket
 */
- (void)openWebSocketWithcwid:(NSString *)cwid;
/**
 关闭WebSocket
 */
- (void)closeWebSocket;


/**
 发送消息

 @param data json格式消息
 */
- (void)sendData:(id)data;

@property (nonatomic, strong) SRWebSocket *__nullable webSocket;/**< 聊天类 */   //真正的webSocket在这里上面只是 生成 单例。实现方法
@property (nonatomic, weak) id <VULWebSocketDelegate> webSocketDelegate;/**< 代理 */

@end

NS_ASSUME_NONNULL_END
