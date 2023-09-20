//
//  VULChatMemberData.h
//  VideoULimit
//
//  Created by ZCc on 2018/11/6.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "WebSocketModel.h"
NS_ASSUME_NONNULL_BEGIN

@interface VULChatMemberData : NSObject
//聊天成员
//聊天成员推拉流
//聊天数据
//s推送作业
//推送附件
//websocket是否登陆
//屏蔽
//是否允许举手
//是否开始签到
//是否已经签到
//登陆回馈消息 member

//@property (nonatomic, assign) BOOL isLiveShop;/**< 是否开启 直播带货 */
@property (nonatomic, assign) BOOL webSocketLogin;/**< 是否登陆 */
@property (nonatomic, strong, nullable) WebSocketLoginResponse *loginMessages;/**< 登陆返回消息 */

@property (nonatomic, assign) BOOL allowRaise;/**< 是否允许举手 */

@property (nonatomic, assign) BOOL isRaise; /**< 举手了 不确定是否通过 */

@property (nonatomic, assign) BOOL isRaisePass;/**< 是否已经举手通过 */

@property (nonatomic, assign) BOOL gag;/**< ture 全局禁言 false 不禁言*/
@property (nonatomic, assign) BOOL friendgag;/**< 是否禁止私聊 */

@property (nonatomic, assign) BOOL blockSpeech; /**< 主动屏蔽聊天 */
@property (nonatomic, assign) NSInteger online_count;

@property (nonatomic, strong) NSMutableArray<WebSocketClientInfo *> *chatMemberData;/**< 聊天成员 */
//@property (nonatomic, strong) NSMutableArray<VULChatMessage *> *groupChatData;/**< 聊天数据 */
@property (nonatomic, strong) NSMutableArray<WebSocketLoginUserInfo *> *blackList;/**< 黑名单列表 */
@property (nonatomic, strong) NSMutableArray *surveys;/**< 问卷调查 */
@property (nonatomic, strong) NSMutableArray *annex; /**< 推送附件 */
@property (nonatomic, strong) NSMutableArray *exam;/**< 推送作业 */
@property (nonatomic, strong) NSMutableArray<WebSocketClientInfo *> *camera_list; /**< 直播互动 */
@property (nonatomic, strong) NSMutableArray<WebSocketLoginUserInfo *> *raiseList; /**< 直播互动 */

+ (instancetype)shareChatMemberData;


- (void)addBypassMember:(WebSocketClientInfo *)member;

- (void)removeBypassMemberWithUid:(NSString *)uid;

- (void)addRaiseUser:(WebSocketLoginUserInfo *)userinfo;

- (void)removeRaiseUserWithUid:(NSString *)uid;

- (void)addBlackListWithUser:(WebSocketLoginUserInfo *)user;

- (void)removeBlackListWithUserId:(NSInteger)uid;

- (BOOL)judgeUserInBlackListWithUserId:(NSInteger)uid;

- (BOOL)isHaveTeacherInChatMember;

- (BOOL)judgeUserIsMainTeacherWithId:(NSString *)uid;

@end

NS_ASSUME_NONNULL_END
