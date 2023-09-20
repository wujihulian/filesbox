//
//  VULWebSocket.m
//  VideoULimit
//
//  Created by ZCc on 2018/11/5.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULWebSocket.h"
#import "YYTimer.h"
#import <sys/utsname.h>

@interface VULWebSocket ()<SRWebSocketDelegate>{
    YYTimer *heartBeat;
    NSTimeInterval reConnectTime;
    NSString *_cwid;
}


@end

static VULWebSocket *shareVULWebSocket = nil;

@implementation VULWebSocket

#pragma mark - 初始化相关
/**
 单例
 
 @return 单例初始化后的对象
 */
+ (instancetype)sharedVULWebSocket{
    static dispatch_once_t onceToken ;
    dispatch_once(&onceToken, ^{
        shareVULWebSocket = [[self alloc] init];
    }) ;
    return shareVULWebSocket;
}

- (instancetype)init{
    if(self = [super init]){
//        NSString *socketURL = [NSString stringWithFormat:@"wss://%@/api/webchat/", [VULAccountManager sharedInstance].currentSchoolModel.secondLevelDomain];
//        NSString *scoketURL = [NSString stringWithFormat:@"%@",WEBSocketURL];
        self.webSocketUrlStr = WEBSocketURL;
//        self.webSocketUrlStr = socketURL;
    }
    return self;
}

#pragma mark - 发送消息相关
- (void)sendPingToSocket{
    [self sendData:@"{\"type\":\"ping\"}"];
}

- (void)sendData:(id)data{
    __weak typeof(self)weakSelf = self;
    
    dispatch_queue_t queue = dispatch_queue_create("sendData.queue", NULL);
    
    //串行异步 可能会开辟一个线程
    dispatch_async(queue, ^{
        if(weakSelf.webSocket != nil){
            //只有在SR_OPEN的情况下才能调用 send 方法 否则会崩溃
            if(weakSelf.webSocket.readyState == SR_OPEN){
                [weakSelf.webSocket send:data];
            }else if(weakSelf.webSocket.readyState == SR_CONNECTING){
                NSLog(@"正在连接中， 重连后其他方法回去自动同步数据");
                // 每隔2秒检测一次 socket.readyState 状态，检测 10 次左右
                // 只要有一次状态是 SR_OPEN 的就调用 [ws.socket send:data] 发送数据
                // 如果 10 次都还是没连上的，那这个发送请求就丢失了，这种情况是服务器的问题了，小概率的
                // 代码有点长，我就写个逻辑在这里好了
                [self reConnect];
            } else if (weakSelf.webSocket.readyState == SR_CLOSING || weakSelf.webSocket.readyState == SR_CLOSED) {
                // websocket 断开了，调用 reConnect 方法重连
                NSLog(@"重连");
                [self reConnect];
            }else{
                NSLog(@"没网络  socket已被释放");
            }
        }
    });
}


#pragma mark - 链接配置相关
//TODO: 重连
- (void)reConnect{
    [self closeWebSocket];
    
    //超过一分钟就不再重连 所以只会重连5次 2^5 = 64
    if (reConnectTime > 64) {
        //您的网络状况不是很好，请检查网络后重试
        return;
    }
    __weak typeof(self)weakSelf = self;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(reConnectTime * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        __strong typeof(weakSelf)sself = weakSelf;
        self.webSocket = nil;
        [weakSelf openWebSocketWithcwid:sself->_cwid];
        NSLog(@"重连");
    });
    
    //重连时间2的指数级增长
    if (reConnectTime == 0) {
        reConnectTime = 2;
    }else{
        reConnectTime *= 2;
    }
}

/**
 WebSocket是否在线
 
 @return 是否在线
 */
- (BOOL)isWebSocketing {
    if (!_webSocket) {
        return NO;
    } else {
        return YES;
    }
}

/**
 打开WebSocket
 */
- (void)openWebSocketWithcwid:(NSString *)cwid{
    if (!_webSocket.delegate) {
        _cwid = cwid;
        //先关闭 再重联
        _webSocket.delegate = nil;
        [_webSocket close];
                
        NSURL *socketUrl = [NSURL URLWithString:[NSString stringWithFormat:@"%@%@",self.webSocketUrlStr,cwid]];
        NSMutableURLRequest *socketRequest = [[NSMutableURLRequest alloc] initWithURL:socketUrl];

//        struct utsname systemInfo;
//        uname(&systemInfo);
//        NSString *platform = [NSString stringWithCString:systemInfo.machine encoding:NSUTF8StringEncoding];
//        NSString *userAgent = [NSString stringWithFormat:@"%@/%@ (%@; iOS %@; Scale/%0.2f; uuid:%@)", @"kong zhong shou ke", [[NSBundle mainBundle] infoDictionary][@"CFBundleShortVersionString"] ? : [[NSBundle mainBundle] infoDictionary][(__bridge NSString *)kCFBundleVersionKey], platform, [[UIDevice currentDevice] systemVersion], [[UIScreen mainScreen] scale],[[[UIDevice currentDevice] identifierForVendor] UUIDString]];
//        [socketRequest addValue:userAgent forHTTPHeaderField:@"User-Agent"];
        
//        struct utsname systemInfo;
//        uname(&systemInfo);
//        NSString *platform = [NSString stringWithCString:systemInfo.machine encoding:NSUTF8StringEncoding];
        
        [socketRequest addValue:[NSString getCurrentDeviceUserAgent] forHTTPHeaderField:@"User-Agent"];
        
        _webSocket = [[SRWebSocket alloc] initWithURLRequest:socketRequest];
        _webSocket.delegate = self;
        [_webSocket open];
    }
}

//TODO: 初始化心跳包每隔60秒发送一次
- (void)initHeartBeat{
    
    __weak typeof(self)weakSelf = self;
    
    dispatch_main_async_safe(^{
        //        [self ]
        __strong typeof(weakSelf)sself = weakSelf;
        sself -> heartBeat = [YYTimer timerWithTimeInterval:40 target:self selector:@selector(sendPingToSocket) repeats:YES];
    });
}

//TODO: 销毁心跳包
- (void)destoryHeartBeat{
    if(heartBeat){
        [heartBeat invalidate];
    }
}

/**
 关闭WebSocket
 */
- (void)closeWebSocket{
    if(_webSocket){
        [self destoryHeartBeat];
        _webSocket.delegate = nil;
        [_webSocket close];
        _webSocket = nil;
    }
}

/**
 移除通知
 */
- (void)removeNotification{
    [VULNotificationCenter removeObserver:self];
}

#pragma mark -
#pragma mark - SRWebSocketDelegate
- (void)webSocketDidOpen:(SRWebSocket *)webSocket{
    //清零重连时间
    reConnectTime = 0;
    //开启心跳
    [self initHeartBeat];
    
    NSLog(@"************************** socket连接成功************************** ");
    
    if(self.webSocketDelegate && [self.webSocketDelegate respondsToSelector:@selector(vul_webSocketDidOpen:)]){
        [self.webSocketDelegate vul_webSocketDidOpen:webSocket];
    }
}

- (void)webSocket:(SRWebSocket *)webSocket didReceiveMessage:(id)message{
    if ([message isKindOfClass:[NSString class]]) {
       NSDictionary * dic = [NSJSONSerialization JSONObjectWithData:[(NSString *)message dataUsingEncoding : NSUTF8StringEncoding] options:kNilOptions error:NULL];
        NSLog(@"webSocket 消息\n%@",dic);
    }
    if(self.webSocketDelegate && [self.webSocketDelegate respondsToSelector:@selector(vul_webSocket:didReceiveMessage:)]){
        [self.webSocketDelegate vul_webSocket:webSocket didReceiveMessage:message];
    }else{
//        客户代理会被释放 所以做处理通知 多个页面链接
        if ([self.webSocketUrlStr hasSuffix:@"cService/10000_10086"]) {
            NSData *receiveMessageData = [(NSString *)message dataUsingEncoding:NSUTF8StringEncoding];
            NSDictionary *receiveMessage = [NSJSONSerialization JSONObjectWithData:receiveMessageData options:NSJSONReadingMutableContainers error:nil];
            [VULNotificationCenter postNotificationName:@"WebSocketDidReceiveMessageService" object:nil userInfo:receiveMessage];
        }
      
    }
}

- (void)webSocket:(SRWebSocket *)webSocket didFailWithError:(NSError *)error{
    NSLog(@":( Websocket Failed With Error %@", error);
//    self.webSocketUrlStr = WEBSocketURL;
//    _webSocket = nil;
    if (webSocket == self.webSocket) {
        _webSocket = nil;
        [self reConnect];
    }
}

- (void)webSocket:(SRWebSocket *)webSocket didCloseWithCode:(NSInteger)code reason:(NSString *)reason wasClean:(BOOL)wasClean {
    if(webSocket == self.webSocket){
        NSLog(@"************************** socket连接断开************************** ");
        NSLog(@"被关闭连接，code:%ld,reason:%@,wasClean:%d",(long)code,reason,wasClean);
        [self closeWebSocket];
    }
}

- (void)dealloc{
    [VULNotificationCenter removeObserver:self];
}

@end
