//
//  YKChatToolBoxView.m
//  VideoULimit
//
//  Created by yuekewei on 2020/6/5.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "YKChatToolBoxView.h"

@interface YKChatToolBoxView ()<YKChatToolBarDelegate>

/** emoji face */
@property (nonatomic, strong) ICChatBoxFaceView *chatBoxFaceView;

@property (nonatomic, strong) UIImagePickerController *imagePicker;
@property (nonatomic, strong) YKRecordTool *recordTool;
@property (nonatomic, assign) BOOL canPulishWork;
@property (nonatomic, assign) BOOL canSendFile;
@property (nonatomic, assign) BOOL isInteraction;

@end

@implementation YKChatToolBoxView

- (void)dealloc{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (instancetype)initWithFrame:(CGRect)frame canPulishWork:(BOOL)canPulishWork canSendFile:(BOOL)canSendFile canSendShop:(BOOL)canService {
    self = [super initWithFrame:frame];
    if (self) {
        self.canSendFile = canSendFile;
        self.canPulishWork = canPulishWork;
        self.isService = canService;
        self.backgroundColor = [UIColor clearColor];
        _recordTool = [YKRecordTool new];
        
        
        [self addSubview:self.chatToolBar];
        [self addSubview:self.chatBoxFaceView];
        [self addSubview:self.chatBoxMoreView];
        
        self.chatToolBar.bottom = self.height - K_BottomBar_Height;
        self.chatBoxFaceView.top = self.height;
        self.chatBoxMoreView.top = self.height;
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardFrameWillChange:) name:UIKeyboardWillChangeFrameNotification object:nil];
    }
    return self;
}

- (instancetype)initInteractionWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.isInteraction = YES;
        self.backgroundColor = [UIColor clearColor];
        _recordTool = [YKRecordTool new];
        
        
        [self addSubview:self.chatToolBar];
        [self addSubview:self.chatBoxFaceView];
        [self addSubview:self.chatBoxMoreView];
        
        self.chatToolBar.bottom = self.height - K_BottomBar_Height;
        self.chatBoxFaceView.top = self.height;
        self.chatBoxMoreView.top = self.height;
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardFrameWillChange:) name:UIKeyboardWillChangeFrameNotification object:nil];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
}

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
    UIView *view = [super hitTest:point withEvent:event];
    if (view == self) {
        [self endEditing:YES];
        [self.chatToolBar setStatus:ICChatBoxStatusNothing];
        return nil;
    }
    return view;
}

#pragma mark - 键盘通知
- (void)keyboardFrameWillChange:(NSNotification *)notification {
    
    NSDictionary    * info = notification.userInfo;
    CGFloat animationDuration = [info[UIKeyboardAnimationDurationUserInfoKey] floatValue];
    CGRect  keyboardAimFrame = [info[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    
    BOOL showKeyBoard = keyboardAimFrame.origin.y < [UIScreen mainScreen].bounds.size.height;
    
    //    if (self.chatToolBar.status == ICChatBoxStatusShowFace &&
    //        self.chatBoxFaceView.bottom == self.height &&
    //        self.chatToolBar.bottom == self.height - self.chatBoxFaceView.height) {
    //        return;
    //    }
    
    if ( !showKeyBoard &&
        (self.chatToolBar.status == ICChatBoxStatusShowFace ||
         self.chatToolBar.status == ICChatBoxStatusShowMore)) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            
            [UIView animateWithDuration:0.15
                                  delay:0
                                options:UIViewAnimationOptionCurveEaseInOut
                             animations:^{
                if (self.chatToolBar.status == ICChatBoxStatusShowFace) {
                    self.chatToolBar.bottom = self.height - self.chatBoxFaceView.height;
                    self.chatBoxFaceView.bottom = self.height;
                }
                else {
                    self.chatToolBar.bottom = self.height - self.chatBoxMoreView.height;
                    self.chatBoxMoreView.bottom = self.height;
                }
            } completion:nil];
            if (self.delegate && [self.delegate respondsToSelector:@selector(chatBoxViewDidChangeChatBarFrame)]) {
                [self.delegate chatBoxViewDidChangeChatBarFrame];
            }
        });
    }
    else {
        self.chatBoxFaceView.top = self.bottom;
        self.chatBoxMoreView.top = self.bottom;
        
        [UIView animateWithDuration:animationDuration
                              delay:0
                            options:UIViewAnimationOptionCurveEaseInOut
                         animations:^{
            self.chatToolBar.bottom = showKeyBoard ? keyboardAimFrame.origin.y - (VULSCREEN_HEIGHT - self.height): self.height - K_BottomBar_Height;
            
        } completion:nil];
        if (self.delegate && [self.delegate respondsToSelector:@selector(chatBoxViewDidChangeChatBarFrame)]) {
            [self.delegate chatBoxViewDidChangeChatBarFrame];
        }
    }
    
    
}

#pragma mark - ICChatBoxDelegate
/**
 *  输入框状态改变
 *
 *  @param chatBox    chatToolBar
 *  @param fromStatus 起始状态
 *  @param toStatus   目的状态
 */
- (void)chatBox:(YKChatToolBar *)chatBox changeStatusForm:(ICChatBoxStatus)fromStatus to:(ICChatBoxStatus)toStatus {
    if (fromStatus == toStatus ||
        fromStatus == ICChatBoxStatusShowKeyboard ||
        toStatus == ICChatBoxStatusShowKeyboard) {
        return;
    }
    
    if (toStatus == ICChatBoxStatusShowFace) {
        self.chatBoxFaceView.top = self.height;
        self.chatBoxMoreView.top = self.height;
        [UIView animateWithDuration:0.25
                              delay:0
                            options:UIViewAnimationOptionCurveEaseInOut
                         animations:^{
            self.chatBoxFaceView.bottom = self.height;
            self.chatToolBar.bottom = self.height - self.chatBoxFaceView.height;
            
        } completion:nil];
        if (self.delegate && [self.delegate respondsToSelector:@selector(chatBoxViewDidChangeChatBarFrame)]) {
            [self.delegate chatBoxViewDidChangeChatBarFrame];
        }
    }
    else if (toStatus == ICChatBoxStatusShowMore) {
        self.chatBoxFaceView.top = self.height;
        self.chatBoxMoreView.top = self.height;
        [UIView animateWithDuration:0.25
                              delay:0
                            options:UIViewAnimationOptionCurveEaseInOut
                         animations:^{
            self.chatBoxMoreView.bottom = self.height;
            self.chatToolBar.bottom = self.height - self.chatBoxMoreView.height;
            
        } completion:nil];
        if (self.delegate && [self.delegate respondsToSelector:@selector(chatBoxViewDidChangeChatBarFrame)]) {
            [self.delegate chatBoxViewDidChangeChatBarFrame];
        }
    }
    else {
        if (self.chatBoxFaceView.top < self.height ||
            self.chatBoxMoreView.top < self.height) {
            [UIView animateWithDuration:0.25
                                  delay:0
                                options:UIViewAnimationOptionCurveEaseInOut
                             animations:^{
                self.chatBoxFaceView.top = self.height;
                self.chatBoxMoreView.top = self.height;
                self.chatToolBar.bottom = self.height - K_BottomBar_Height;
                
            } completion:nil];
            if (self.delegate && [self.delegate respondsToSelector:@selector(chatBoxViewDidChangeChatBarFrame)]) {
                [self.delegate chatBoxViewDidChangeChatBarFrame];
            }
        }
        
    }
    
    if (toStatus == ICChatBoxStatusShowPic) {
        if (self.delegate && [self.delegate respondsToSelector:@selector(chatBoxViewStartSelectImage)]) {
            [self.delegate chatBoxViewStartSelectImage];
        }
    }
}

- (void)chatBox:(YKChatToolBar *)chatBox sendTextMessage:(NSString *)textMessage {
    if (_delegate && [_delegate respondsToSelector:@selector(chatBoxView:sendTextMessage:)]) {
        [_delegate chatBoxView:self sendTextMessage:textMessage];
    }
}

/**
 *  输入框高度改变
 *
 *  @param chatToolBar chatToolBar
 *  @param height  height
 */
- (void)chatBox:(YKChatToolBar *)chatBox changeChatBoxHeight:(CGFloat)height {
    if (self.delegate && [self.delegate respondsToSelector:@selector(chatBoxViewDidChangeChatBarFrame)]) {
        [self.delegate chatBoxViewDidChangeChatBarFrame];
    }
}

#pragma mark  语音
- (void)chatBoxDidStartRecordingVoice:(YKChatToolBar *)chatBox {
    [self.recordTool startRecordingWithHUDShowView:self];
}

- (void)chatBoxDidStopRecordingVoice:(YKChatToolBar *)chatBox {
    [self.recordTool completionRecording:^(NSString * _Nonnull recordPath) {
        if (self->_delegate && [self->_delegate respondsToSelector:@selector(chatBoxView:sendVoiceMessage:)]) {
            [self->_delegate chatBoxView:self sendVoiceMessage:recordPath];
        }
    }];
}

- (void)chatBoxDidCancelRecordingVoice:(YKChatToolBar *)chatBox {
    [self.recordTool cancelRecording];
}

- (void)chatBoxDidDrag:(BOOL)inside {
    [self.recordTool voiceWillDragout:inside];
}

#pragma mark - Lazy
- (YKChatToolBar *) chatToolBar {
    if (_chatToolBar == nil) {
        _chatToolBar = [[YKChatToolBar alloc] initWithFrame:CGRectMake(0, 0, k_CH_ScreenWidth, HEIGHT_TABBAR)];
        _chatToolBar.backgroundColor  = UIColor.whiteColor;
        _chatToolBar.delegate = self;
    }
    return _chatToolBar;
}

- (ICChatBoxFaceView *)chatBoxFaceView {
    if (nil == _chatBoxFaceView) {
        _chatBoxFaceView = [[ICChatBoxFaceView alloc] initWithFrame:CGRectMake(0, 0, k_CH_ScreenWidth, HEIGHT_CHATBOXVIEW)];
        _chatBoxFaceView.backgroundColor  = UIColor.whiteColor;

    }
    return _chatBoxFaceView;
}

- (ICFaceManager *)faceManager {
    if (!_faceManager) {
        _faceManager = [ICFaceManager new];
    }
    return _faceManager;
}

- (YKChatBoxMoreView *)chatBoxMoreView {
    if (nil == _chatBoxMoreView) {
        _chatBoxMoreView = [[YKChatBoxMoreView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, kMoreViewHeight)];
        _chatBoxMoreView.backgroundColor  = UIColor.whiteColor;

//        _chatBoxMoreView.backgroundColor = HEXCOLOR(0xf2f2f2);
        NSMutableArray *items = [NSMutableArray new];
        if (self.isInteraction) {
            [items addObject:({
                YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:KLanguage(@"相册")
                                                                 imageName:@"interact_pic"
                                                                       tag:100];
                item;
            })];
            [items addObject:({
                YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:KLanguage(@"拍照")
                                                                 imageName:@"interact_camera"
                                                                       tag:101];
                item;
            })];
            [items addObject:({
                YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"设置"
                                                                 imageName:@"interact_set"
                                                                       tag:106];
                item;
            })];
            [items addObject:({
                YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"分享"
                                                                 imageName:@"interact_share"
                                                                       tag:107];
                item;
            })];
        } else {
            [items addObject:({
                YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"相册"
                                                                 imageName:@"photo_album"
                                                                       tag:100];
                item;
            })];
//            
//            [items addObject:({
//                YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"拍照"
//                                                                 imageName:@"camera"
//                                                                       tag:101];
//                item;
//            })];
            if (self.isService) {
                [items addObject:({
                    YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"商品"
                                                                     imageName:@"购物_商品"
                                                                           tag:106];
                    item;
                })];
                [items addObject:({
                    YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"会员卡"
                                                                     imageName:@"客服_VIP"
                                                                           tag:107];
                    item;
                })];
            }
            
            if (self.canSendFile) {
                [items addObject:({
                    YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"文件"
                                                                     imageName:@"icon_file"
                                                                           tag:105];
                    item;
                })];
            }
            if (self.canPulishWork) {
                [items addObject:({
                    YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"通知"
                                                                     imageName:@"icon_tongzhi"
                                                                           tag:103];
                    item;
                })];
                
                [items addObject:({
                    YKChatBoxMoreItem *item = [YKChatBoxMoreItem itemWithTitle:@"作业"
                                                                     imageName:@"icon_work"
                                                                           tag:104];
                    item;
                })];
            }
        }
            
        [_chatBoxMoreView setItems:items];
        
    }
    return _chatBoxMoreView;
}
@end
