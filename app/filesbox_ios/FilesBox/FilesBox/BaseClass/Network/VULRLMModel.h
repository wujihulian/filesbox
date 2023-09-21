//
//  VULRLMModel.h
//  VideoULimit
//
//  Created by svnlan on 2018/11/10.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import <Realm/Realm.h>
#import "VULResponseModel.h"

@interface VULRLMModel : RLMObject

@end

#pragma mark - 登录后的token
@interface VULSaveUserToken : RLMObject

@property (nonatomic, copy) NSString *token;              /**< 登录后的token 值 */

@property (nonatomic, copy) NSString *userID;              /**< 登录后的userID 值 */

- (instancetype)initWithToken:(NSString *)token andUserID:(NSString *)userId;

@end

#pragma mark - 用户登录信息
@interface VULSaveUserInformation : RLMObject
@property (nonatomic, copy) NSString *administrator;       /**< 是否系统管理员    */
@property (nonatomic, copy) NSString *avatar;       /**< 头像    */

@property (nonatomic, copy) NSString *ignoreFileSize;       /**< 上传文件大小(GB) 限制    */
@property (nonatomic, copy) NSString *label;       /**< css标签（颜色标签       */
@property (nonatomic, copy) NSString *lastLogin;       /**< 最后登录时间（时间戳        */
@property (nonatomic, copy) NSString *name;       /**< 登录名          */
@property (nonatomic, copy) NSString *nickname;       /**< 昵称         */
@property (nonatomic, copy) NSString *phone;       /**< 手机         */
@property (nonatomic, copy) NSString *roleID;       /**< 权限ID             */

@property (nonatomic, copy) NSString *roleName;       /**< 角色名称                 */
@property (nonatomic, copy) NSString *sex;       /**< 性别 (0女1男)                 */
@property (nonatomic, copy) NSString *userID;       /**< 用户ID
                                                     */
@property (nonatomic, copy) NSString *alipayOpenId;
@property (nonatomic, copy) NSString *code;
@property (nonatomic, copy) NSString *description;
@property (nonatomic, copy) NSString *dingOpenId;
@property (nonatomic, copy) NSString *email;
@property (nonatomic, copy) NSString *enWechatOpenId;
@property (nonatomic, copy) NSString *isSystem;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, copy) NSString *sizeMax;
@property (nonatomic, copy) NSString *sizeUse;
@property (nonatomic, copy) NSString *wechatOpenId;
- (instancetype)initWithTargetModel:(VULResponseLoginModel *)model;

@end
