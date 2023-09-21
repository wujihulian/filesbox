//
//  YKChatToolBar.h
//  YKChat
//
//  Created by yuekewei on 2020/5/25.
//  Copyright © 2020 yuekewei. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YKChatPrefixHeader.h"
#import "VULChatTextParser.h"

#define ToolBarHeight 49.0

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, ICChatBoxStatus) {
    ICChatBoxStatusNothing,     // 默认状态
    ICChatBoxStatusShowVoice,   // 录音状态
    ICChatBoxStatusShowFace,    // 输入表情状态
    ICChatBoxStatusShowPic,     // 选照片状态
    ICChatBoxStatusShowMore,    // 显示“更多”页面状态
    ICChatBoxStatusShowKeyboard,// 正常键盘
    ICChatBoxStatusShowVideo    // 录制视频
};

@class YKChatToolBar;
@protocol YKChatToolBarDelegate <NSObject>

@optional
/**
 *  输入框状态(位置)改变
 *
 *  @param chatToolBar    chatToolBar
 *  @param fromStatus 起始状态
 *  @param toStatus   目的状态
 */
- (void)chatBox:(YKChatToolBar *)chatBox changeStatusForm:(ICChatBoxStatus)fromStatus to:(ICChatBoxStatus)toStatus;

/**
 *  发送消息
 *
 *  @param chatToolBar     chatToolBar
 *  @param textMessage 消息
 */
- (void)chatBox:(YKChatToolBar *)chatBox sendTextMessage:(NSString *)textMessage;

/**
 *  输入框高度改变
 *
 *  @param chatToolBar chatToolBar
 *  @param height  height
 */
- (void)chatBox:(YKChatToolBar *)chatBox changeChatBoxHeight:(CGFloat)height;

/**
 *  开始录音
 *
 *  @param chatToolBar chatToolBar
 */
- (void)chatBoxDidStartRecordingVoice:(YKChatToolBar *)chatBox;
- (void)chatBoxDidStopRecordingVoice:(YKChatToolBar *)chatBox;
- (void)chatBoxDidCancelRecordingVoice:(YKChatToolBar *)chatBox;
- (void)chatBoxDidDrag:(BOOL)inside;

@end

@interface YKChatToolBar : UIView

/// 是否可以聊天
@property (nonatomic, assign) BOOL canChat;

/** 保存状态 */
@property (nonatomic, assign) ICChatBoxStatus status;
@property (nonatomic, assign) ICChatBoxStatus lastStatus;

@property (nonatomic, weak) id<YKChatToolBarDelegate>delegate;

@property (nonatomic, weak) id<YYTextViewDelegate> textViewDelegate;

/** 输入框 */
@property (nonatomic, strong) YYTextView *textView;
/** 录音按钮 */
@property (nonatomic, strong) UIButton *voiceButton;
/** 表情按钮 */
@property (nonatomic, strong) UIButton *faceButton;
/** (+) \ 图片 按钮 */
@property (nonatomic, strong) UIButton *moreButton;

/** 发送按钮 */
@property (nonatomic, strong) UIButton *sendButton;
/** 按住说话 */
@property (nonatomic, strong) UIButton *talkButton;
/** chotBox的顶部边线 */
@property (nonatomic, strong) UIView *topLine;
@end

NS_ASSUME_NONNULL_END
