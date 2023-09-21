//
//  VULChatMemberData.m
//  VideoULimit
//
//  Created by ZCc on 2018/11/6.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULChatMemberData.h"

@implementation VULChatMemberData

static VULChatMemberData *chatMemberManager = nil;

+ (instancetype)shareChatMemberData {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        chatMemberManager = [[self alloc] init];
    });
    return chatMemberManager;
}

#pragma mark - public Method
//教师端互动交流-举手列表
- (void)addRaiseUser:(WebSocketLoginUserInfo *)userinfo {
    __block NSInteger isDelIndex = -1;
    [self.raiseList enumerateObjectsUsingBlock:^(WebSocketLoginUserInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj.uid == userinfo.uid) {
            isDelIndex = idx;
            *stop = YES;
        }
    }];
    if (isDelIndex >= 0 && isDelIndex < _raiseList.count) {
        [self.raiseList removeObjectAtIndex:isDelIndex];
    }
    [self.raiseList addObject:userinfo];
    NSLog(@"raise: %ld - %@",[VULChatMemberData shareChatMemberData].raiseList.count,[VULChatMemberData shareChatMemberData].raiseList);
}

- (void)removeRaiseUserWithUid:(NSString *)uid {
    if (!uid) {
        return;
    }
    __block NSInteger delIndex = -1;
    [_raiseList enumerateObjectsUsingBlock:^(WebSocketLoginUserInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj.uid == [uid integerValue]) {
            delIndex = idx;
            *stop = YES;
        }
    }];
    if (delIndex >= 0) {
        [_raiseList removeObjectAtIndex:delIndex];
    }
}

//学生端互动旁路列表
- (void)addBypassMember:(WebSocketClientInfo *)member {
    __block NSInteger isDelIndex = -1;
    [_camera_list enumerateObjectsUsingBlock:^(WebSocketClientInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj.uid == member.uid) {
            isDelIndex = idx;
            *stop = YES;
        }
    }];
    if (isDelIndex >= 0 && isDelIndex < _camera_list.count) {
        [_camera_list removeObjectAtIndex:isDelIndex];
    }
    
    [_camera_list addObject:member];
}


- (void)removeBypassMemberWithUid:(NSString *)uid {
    if (!uid) {
        return;
    }
    
    __block NSInteger delIndex = -1;
    [_camera_list enumerateObjectsUsingBlock:^(WebSocketClientInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj.uid == [uid integerValue]) {
            delIndex = idx;
            *stop = YES;
        }
    }];
    if (delIndex >= 0) {
        [_camera_list removeObjectAtIndex:delIndex];
    }
}

//黑名单(禁言)列表
- (void)addBlackListWithUser:(WebSocketLoginUserInfo *)user {
    __block NSInteger isDelIndex = -1;
    [self.blackList enumerateObjectsUsingBlock:^(WebSocketLoginUserInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj != NULL) {
            if (obj.uid == user.uid) {
                isDelIndex = idx;
                *stop = YES;
            }
        }
    }];
    if (isDelIndex >= 0 && isDelIndex < _blackList.count) {
        [self.blackList removeObjectAtIndex:isDelIndex];
    }
    [self.blackList addObject:user];
}

- (void)removeBlackListWithUserId:(NSInteger)uid {
    if (!uid) {
        return;
    }
    __block NSInteger delIndex = -1;
    [_blackList enumerateObjectsUsingBlock:^(WebSocketLoginUserInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj != NULL) {
            if (obj.uid == uid) {
                delIndex = idx;
                *stop = YES;
            }
        }
    }];
    if (delIndex >= 0) {
        [_blackList removeObjectAtIndex:delIndex];
    }
}

- (BOOL)judgeUserInBlackListWithUserId:(NSInteger)uid {
    __block BOOL isInSide = NO;
    
    for (WebSocketLoginUserInfo *obj in _blackList) {
        if (obj != NULL) {
            if (obj.uid == uid) {
                isInSide = YES;
            }
        }
    }
    
//    [_blackList enumerateObjectsUsingBlock:^(WebSocketLoginUserInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
//        if (obj.uid) {
//            if (obj.uid == uid) {
//                *stop = YES;
//                isInSide = YES;
//            }
//        }
//    }];
    return isInSide;
}

- (BOOL)isHaveTeacherInChatMember {
    __block BOOL ret = NO;
    [_chatMemberData enumerateObjectsUsingBlock:^(WebSocketClientInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj.groupid isEqualToString:@"5"]) {
            ret = YES;
            *stop = YES;
        }
    }];
    return ret;
}

- (BOOL)judgeUserIsMainTeacherWithId:(NSString *)uid {
    
    BOOL isMainTeacher = NO;
    WebSocketLoginUserInfo *currentUser = self.loginMessages.userinfo;
//    [VULChatMemberData shareChatMemberData].loginMessages.userinfo;
    if (currentUser.uid == [uid integerValue]) {
        isMainTeacher = YES;
    }
    return isMainTeacher;
}


#pragma mark - getters and setters

- (NSMutableArray<WebSocketClientInfo *> *)chatMemberData {
    if (!_chatMemberData) {
        _chatMemberData = [NSMutableArray arrayWithCapacity:0];
    }
    return _chatMemberData;
}

- (NSMutableArray *)exam {
    if (!_exam) {
        _exam = [NSMutableArray arrayWithCapacity:0];
    }
    return _exam;
}

- (NSMutableArray *)annex {
    if (!_annex) {
        _annex = [NSMutableArray arrayWithCapacity:0];
    }
    return _annex;
}

//- (NSMutableArray<VULChatMessage *> *)groupChatData {
//    if (!_groupChatData) {
//        _groupChatData = [NSMutableArray arrayWithCapacity:0];
//    }
//    return _groupChatData;
//}

- (NSMutableArray<WebSocketClientInfo *> *)camera_list {
    if (!_camera_list) {
        _camera_list = [NSMutableArray arrayWithCapacity:0];
    }
    return _camera_list;
}


- (NSMutableArray<WebSocketLoginUserInfo *> *)raiseList {
    if (!_raiseList) {
        _raiseList = [NSMutableArray arrayWithCapacity:0];
    }
    return _raiseList;
}

- (NSMutableArray<WebSocketLoginUserInfo *> *)blackList {
    if (!_blackList) {
        _blackList = [NSMutableArray arrayWithCapacity:0];
    }
    return _blackList;
}


@end
