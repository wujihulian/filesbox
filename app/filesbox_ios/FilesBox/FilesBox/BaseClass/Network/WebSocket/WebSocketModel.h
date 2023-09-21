//
//  WebSocketModel.h
//  VideoULimit
//
//  Created by ZCc on 2018/11/5.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface WebSocketModel : NSObject

@end

//登陆参数
@interface WebSocketLogin : NSObject

@property (nonatomic, copy) NSString *type;
@property (nonatomic, copy) NSString *token;
@property (nonatomic, assign) BOOL mic;/**< 是否有麦克风 */
@property (nonatomic, assign) BOOL cam;/**< 摄像头 */
@property (nonatomic, assign) BOOL vulbrowser;

@property (nonatomic, copy) NSString *c_t; //设备信息

@end


@interface WebSocketClientInfo : NSObject

@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *groupid; /**< id=4助教  id=5教师 id=6学生  */
@property (nonatomic, assign) BOOL assistant;  /**< 判断是否为助教 ！！已弃用 */
@property (nonatomic, assign) BOOL vulbrowser;
@property (nonatomic, assign) BOOL is_black_list;
@property (nonatomic, assign) BOOL cam;
@property (nonatomic, assign) BOOL mic;
@property (nonatomic, assign) BOOL open_cam;
@property (nonatomic, assign) BOOL open_mic;
@property (nonatomic, copy) NSString *c_t; //设备信息
@property (nonatomic, assign) BOOL raise;
@property (nonatomic, assign) NSInteger uid;
@property (nonatomic, assign) NSInteger client_id;
@property (nonatomic, assign) NSTimeInterval login_time;
@property (nonatomic, assign) NSInteger trophyNum;/**< 奖杯数量*/
@property (nonatomic, assign) BOOL draw;
@property (nonatomic, assign) BOOL singleGag;
@property (nonatomic, assign) BOOL stage;
@end

//登录用户信息
@interface WebSocketLoginUserInfo : NSObject

@property (nonatomic, assign) BOOL assistant;
@property (nonatomic, copy) NSString *avatar;/**< 头像链接 */
@property (nonatomic, assign) BOOL cam;/**< 摄像头是否可开启 */
@property (nonatomic, assign) NSInteger client_id;/**< 客户端编号 */
@property (nonatomic, assign) NSTimeInterval login_time;/**< 登陆时间 */
@property (nonatomic, assign) NSInteger uid;/**< uid */
@property (nonatomic, assign) BOOL vulbrowser;/**< 是否是ebh浏览器 */
@property (nonatomic, copy) NSString *groupid;/**<id=4助教  id=5主讲教师 id=6学生或其他老师 */
@property (nonatomic, assign) BOOL is_black_list;/**< 是否黑名单 */
@property (nonatomic, assign) BOOL mic;/**< 麦克风是否开启 */
@property (nonatomic, copy) NSString *name;/**< 名字 */
@property (nonatomic, assign) BOOL open_cam;/**< 是否开启摄像头 */
@property (nonatomic, assign) BOOL open_mic;/**< 是否开启麦克风 */
@property (nonatomic, assign) BOOL raise;/**< 是否举手 */
@property (nonatomic, assign) BOOL takeoff;/**< 是否被踢出当前教室 */
@property (nonatomic, assign) BOOL questions;/**< 是否允许提问 */
@property (nonatomic, assign) NSInteger trophyNum;/**< 奖杯数量*/
@property (nonatomic, assign) BOOL draw;
@property (nonatomic, assign) BOOL singleGag;

/// 上台或下台  1：上台
@property (nonatomic, assign) NSInteger stage;

@end

//房间配置
@interface WebSocketRoomConfig : NSObject

@property (nonatomic, assign) BOOL allow_raise;
@property (nonatomic, assign) NSInteger checkSmsNum;
@property (nonatomic, assign) BOOL checkin;
@property (nonatomic, assign) BOOL firstCall;
@property (nonatomic, assign) BOOL friendGag;
@property (nonatomic, assign) BOOL gag;
@property (nonatomic, assign) BOOL lock;
@property (nonatomic, copy) NSString *resolution;/**< 双师分辨率 */

@end

//登陆返回参数
@interface WebSocketLoginResponse : NSObject

@property (nonatomic, strong) NSMutableArray *attachment;/**< 附件推送 */
@property (nonatomic, strong) NSMutableArray *camera_list;/**< 互动列表 */
@property (nonatomic, strong) NSMutableArray *raise_list;/**<学生举手列表 */
@property (nonatomic, assign) BOOL is_checkin;/**< 是否已签到 */
@property (nonatomic, strong) NSMutableArray<WebSocketClientInfo *> *client_list;/**< 用户列表 */
@property (nonatomic, strong) NSMutableArray *exam;/**< 作业推送列表 */
@property (nonatomic, assign) NSInteger online_count;/**< 在线人数 */
@property (nonatomic, strong) WebSocketRoomConfig *room_config;
@property (nonatomic, strong) NSMutableArray *survey;/**< 问卷调查 */
@property (nonatomic, strong) NSString *type;/**< 类型 */
@property (nonatomic, strong) WebSocketLoginUserInfo *userinfo;/**< 登陆人信息 */
@property (nonatomic, strong) NSMutableArray<WebSocketLoginUserInfo *> *black_list;/**< 黑名单列表 */

@end

@interface WebSocketGroupChatFrom : NSObject

@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, assign) NSInteger client_id;
@property (nonatomic, copy) NSString *groupid; /**< id=4助教  id=5教师 id=6学生  */
@property (nonatomic, copy) NSString *name;
@property (nonatomic, assign) NSInteger uid;
@property (nonatomic, assign) BOOL banChat;
@property (nonatomic, assign) BOOL cam;
@property (nonatomic, assign) BOOL mic;
@property (nonatomic, assign) BOOL assistant;
@property (nonatomic, assign) BOOL is_black_list;
@property (nonatomic, assign) NSInteger login_time;
@property (nonatomic, assign) BOOL open_cam;
@property (nonatomic, assign) BOOL open_mic;
@property (nonatomic, assign) BOOL raise;

@end

@interface WebSocketGroupChat : NSObject

@property (nonatomic, copy) NSString *type;
@property (nonatomic, strong) WebSocketGroupChatFrom *from;
@property (nonatomic, copy) NSString *to;
@property (nonatomic, copy) NSString *content;
@property (nonatomic, copy) NSString *avatar;
@property (nonatomic, copy) NSString *uid;
@property (nonatomic, copy) NSString *groupid;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *timestamp;
@property (nonatomic, strong) WebSocketClientInfo *userOnline;/**< 发消息用户的详细信息 */

/// 是否是弹幕
@property (nonatomic, assign) BOOL isBullet;
@end

@interface WebSocketRoomGroupShield : NSObject

@property (nonatomic, copy) NSString *type;
@property (nonatomic, copy) NSString *content;
@property (nonatomic, assign) BOOL gag;

@end

@interface WebSocketRoomGroupRaise : NSObject

@property (nonatomic, copy) NSString *type;
@property (nonatomic, copy) NSString *content;
@property (nonatomic, assign) BOOL allow_raise;

@end

@interface WebSocketRoomUserShield : NSObject

@property (nonatomic, copy) NSString *type;
@property (nonatomic, assign) NSInteger uid;
@property (nonatomic, strong) WebSocketLoginUserInfo *user;

@end

@interface WebSocketRoomUserWantRaise : NSObject

@property (nonatomic, strong) WebSocketLoginUserInfo *userinfo;
@property (nonatomic, copy) NSString *action;
@property (nonatomic, copy) NSString *type;
@property (nonatomic, assign) BOOL raise;

@property (nonatomic, assign) NSInteger client_id;

@end

//打开摄像头消息
@interface WebSocketRoomUserRaise : NSObject

@property (nonatomic, copy) NSString *realname;
@property (nonatomic, copy) NSString *type;
@property (nonatomic, assign) BOOL assistant;
@property (nonatomic, assign) NSInteger client_id;
@property (nonatomic, assign) BOOL vulbrowser;
@property (nonatomic, assign) BOOL interactCount;
@property (nonatomic, assign) BOOL isaudio;
@property (nonatomic, assign) NSInteger uid;

@end

//聊天通知信息
@interface WebSocketRoomNotice : NSObject

@property (nonatomic, copy) NSString *type;
@property (nonatomic, copy) NSString *notice;

@end

//挤下线
@interface WebSocketRoomOffLine : NSObject

@property (nonatomic, copy) NSString *type;
@property (nonatomic, copy) NSString *msg;

@end

//有人退出直播互动
@interface WebSocketCloseInteraction : NSObject

@property (nonatomic, copy) NSString *realname;/**< 名字 */
@property (nonatomic, copy) NSString *type;/**< socket类型 close_camera */
@property (nonatomic, assign) BOOL vulbrowser; /**< 是否是EBH浏览器  false */
@property (nonatomic, assign) NSInteger client_id; /**< 用户id */
@property (nonatomic, assign) NSInteger uid;/**< 用户uid */
@property (nonatomic, assign) NSInteger interactCount;/**< 互动人数 */

@end


NS_ASSUME_NONNULL_END
