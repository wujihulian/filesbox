//
//  YKChatToolBoxView.h
//  VideoULimit
//
//  Created by yuekewei on 2020/6/5.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YKChatToolBar.h"
#import "YKRecordTool.h"
#import "ICChatBoxFaceView.h"
#import "ICFaceManager.h"
#import "YKChatBoxMoreView.h"

NS_ASSUME_NONNULL_BEGIN

@class YKChatToolBoxView;

@protocol ICChatBoxViewDelegate <NSObject>

@optional

- (void)chatBoxView:(YKChatToolBoxView *)chatboxView
               sendTextMessage:(NSString *)messageStr;

- (void)chatBoxView:(YKChatToolBoxView *)chatboxView
              sendImageMessage:(UIImage *)image
                     imagePath:(NSString *)imgPath;

- (void) chatBoxView:(YKChatToolBoxView *)chatboxView
              sendVoiceMessage:(NSString *)voicePath;

- (void) chatBoxViewStartSelectImage;

- (void) chatBoxViewDidChangeChatBarFrame;

@end

@interface YKChatToolBoxView : UIView

- (instancetype)initWithFrame:(CGRect)frame canPulishWork:(BOOL)canPulishWork canSendFile:(BOOL)canSendFile canSendShop:(BOOL)canService ;

- (instancetype)initInteractionWithFrame:(CGRect)frame;

@property(nonatomic, weak) id<ICChatBoxViewDelegate>delegate;

@property (nonatomic, strong) YKChatToolBar *chatToolBar;

@property (nonatomic, strong) YKChatBoxMoreView *chatBoxMoreView;

@property (nonatomic, strong) ICFaceManager *faceManager;

@property (nonatomic, assign) BOOL isService;

@end

NS_ASSUME_NONNULL_END
