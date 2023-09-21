//
//  VULServiceMessageModel.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/6/10.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULServiceMessageModel : NSObject
@property (nonatomic, strong) NSString *avatar;/**<     访客头像 */
@property (nonatomic, strong) NSString *cardIdStr;/**< 客服名片id，聊天消息的from */
@property (nonatomic, strong) NSString *name;/**< 访客名称 */
@property (nonatomic, copy) NSString *timestamp;/**< 时间戳 */
@property (nonatomic, strong) NSString *visitorId;/**<     访客id，聊天消息的to*/
@property (nonatomic, strong) NSDictionary *lastChat;/**<     访客id，聊天消息的to*/
@property (nonatomic, assign) NSInteger unreadCount;/**< 未读数 */
@property (nonatomic, assign) NSInteger visitorUserId;/**< 用户Id */
@property (nonatomic, retain) NSString *busType;/**< 订单类型 */
@property (nonatomic, retain) NSString *orderId;/**< 订单Id */


@end


@interface VULServiceChatMessageModel : NSObject
@property (nonatomic, strong) NSString *dType;/**<     访客头像 */
@property (nonatomic, strong) NSString *msg;/**< 客服名片id，聊天消息的from */
@property (nonatomic, assign) BOOL isMe;
@property (nonatomic, strong) NSString *avatar;/**<     访客头像 */
@property (nonatomic, strong) NSString *timestamp;
@property (nonatomic, assign) float duration;
@property (nonatomic, strong) NSString *commentID;


@end

@interface VULServiceChatListModel : NSObject
@property (nonatomic, assign) NSInteger fromTo;
@property (nonatomic, strong) NSString *message;
@property (nonatomic, strong) NSString *messageType;
@property (nonatomic, strong) NSString *avatar;/**<     访客头像 */
@property (nonatomic, strong) NSString *timestamp;
@property (nonatomic, assign) float duration;

@end

@interface VULCardListListModel : NSObject
@property (nonatomic, strong) NSString *avatar;
@property (nonatomic, strong) NSString *cardName;
@property (nonatomic, strong) NSString *sex;/**<     访客头像 */
@property (nonatomic, strong) NSString *visitingCardId;
@property (nonatomic, strong) NSString *visitingCardIdStr;


@property (nonatomic, strong) NSString *visitorId;/**<     访客id，聊天消息的to*/

@end
NS_ASSUME_NONNULL_END
