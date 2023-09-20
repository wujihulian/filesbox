//
//  YKChatToolBar.m
//  YKChat
//
//  Created by yuekewei on 2020/5/25.
//  Copyright © 2020 yuekewei. All rights reserved.
//

#import "YKChatToolBar.h"
#import "XZEmotion.h"

@interface YKChatToolBar ()<UITextViewDelegate, YYTextViewDelegate>

@property (nonatomic, strong) UIButton *enbleButton;
@end

@implementation YKChatToolBar

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self setBackgroundColor:IColor(247, 247, 247)];
        [self addSubview:self.topLine];
        [self addSubview:self.voiceButton];
        [self addSubview:self.textView];
        [self addSubview:self.faceButton];
        [self addSubview:self.moreButton];
        [self addSubview:self.sendButton];
        [self addSubview:self.talkButton];
        [self addSubview:self.enbleButton];
        
        _canChat = YES;
        self.status = ICChatBoxStatusNothing; // 起始状态
        
        [self addNotification];
        
        [self.enbleButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(self.textView);
        }];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.topLine.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame), 0.5);
    
    CGFloat yy =  (ToolBarHeight - CHATBOX_BUTTON_WIDTH)/2.0 + (self.height - ToolBarHeight);
    _voiceButton.frame = CGRectMake(0,
                                    yy,
                                    10,
                                    0);
    _sendButton.frame = CGRectMake(k_CH_ScreenWidth - CHATBOX_BUTTON_WIDTH - 10,
                                   ((ToolBarHeight - 25)/2.0 + (self.height - ToolBarHeight)),
                                   CHATBOX_BUTTON_WIDTH,
                                   25);
    _moreButton.frame = CGRectMake(k_CH_ScreenWidth - CHATBOX_BUTTON_WIDTH - 10,
                                   yy,
                                   CHATBOX_BUTTON_WIDTH,
                                   CHATBOX_BUTTON_WIDTH);
    _faceButton.frame = CGRectMake(self.moreButton.frame.origin.x - CHATBOX_BUTTON_WIDTH,
                                   yy,
                                   CHATBOX_BUTTON_WIDTH,
                                   CHATBOX_BUTTON_WIDTH);
    
    _talkButton.frame = CGRectMake(self.voiceButton.x + self.voiceButton.width + 4, ((ToolBarHeight - HEIGHT_TEXTVIEW)/2.0 + (self.height - ToolBarHeight)), self.faceButton.x - self.voiceButton.x - self.voiceButton.width - 8, HEIGHT_TEXTVIEW);
    
    [self updateHeight];
}

- (void)setStatus:(ICChatBoxStatus)status {
    self.lastStatus = self.status;
    _status = status;
    
    if (status == ICChatBoxStatusNothing &&
        (self.lastStatus == ICChatBoxStatusShowVoice ||
         self.lastStatus == ICChatBoxStatusShowVoice)) {
        _status = _lastStatus;
    }
    
    [_voiceButton setImage:[UIImage imageNamed:_status == ICChatBoxStatusShowVoice ? @"icon_keyboard" : @"icon_voice"] forState:UIControlStateNormal];
    [_faceButton setImage:[UIImage imageNamed: _status == ICChatBoxStatusShowFace ? @"icon_keyboard" : @"icon_emotion"] forState:UIControlStateNormal];
    
    if (self.canChat) {
        self.talkButton.hidden = _status != ICChatBoxStatusShowVoice;
        self.textView.hidden = !self.talkButton.hidden;
    }
    
    if (status == ICChatBoxStatusShowKeyboard) {
        [self.textView becomeFirstResponder];
    }
    else {
        [self.textView resignFirstResponder];
    }
    
    [self updateHeight];
    
    if (_delegate && [_delegate respondsToSelector:@selector(chatBox:changeStatusForm:to:)]) {
        [_delegate chatBox:self changeStatusForm:self.lastStatus to:self.status];
    }
}

- (void)setCanChat:(BOOL)canChat {
    _canChat = canChat;
    if (!_canChat) {
        self.status = 0;
    }
    
    self.textView.hidden = !canChat;
    self.voiceButton.enabled = canChat;
    self.faceButton.enabled = canChat;
    self.moreButton.enabled = canChat;
    self.sendButton.enabled = canChat;
    self.talkButton.enabled = canChat;
    self.enbleButton.hidden = canChat;    
}

#pragma mark - 监听通知
- (void)addNotification {
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(emotionDidSelected:) name:GXEmotionDidSelectNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(deleteBtnClicked) name:GXEmotionDidDeleteNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(sendMessage) name:GXEmotionDidSendNotification object:nil];
}

- (void)emotionDidSelected:(NSNotification *)notifi {
    XZEmotion *emotion = notifi.userInfo[GXSelectEmotionKey];
    [self.textView replaceRange:self.textView.selectedTextRange withText:emotion.face_name];
}

// 删除
- (void)deleteBtnClicked {
    [self.textView deleteBackward];
}

- (void)sendMessage {
    if (self.textView.text.length > 0) {
        if (_delegate && [_delegate respondsToSelector:@selector(chatBox:sendTextMessage:)]) {
            [_delegate chatBox:self sendTextMessage:self.textView.text];
        }
    }
    [self.textView setText:@""];
}

#pragma mark - UITextViewDelegate
- (void) textViewDidBeginEditing:(UITextView *)textView {
    self.status = ICChatBoxStatusShowKeyboard;
}

- (void)textViewDidChange:(YYTextView *)textView {
    NSStringEncoding encoding = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    NSData* stringData = [textView.text dataUsingEncoding:encoding];
    
//    NSInteger byte = [self byteLengthOfString:textView.text];
    if (stringData.length > 3000) {
        NSString *newString = [self subStringByMaxByte:3000 string:textView.text];
        textView.text = newString;
    }
    
    self.sendButton.hidden = textView.text.length == 0;
    self.moreButton.hidden = !self.sendButton.hidden;
    [self updateHeight];
    [textView scrollToBottom];
}

- (NSUInteger)byteLengthOfString:(NSString *)string {
    if (string.length ==0) return 0;
    NSInteger strByteLength = 0;
    char* p = (char*)[string cStringUsingEncoding:NSUnicodeStringEncoding];
    for (NSInteger i=0 ; i < [string lengthOfBytesUsingEncoding:NSUnicodeStringEncoding] ;i++) {
        if (*p) {
            p++;
            strByteLength++;
        }
        else {
            p++;
        }
    }
    return strByteLength;
}

- (NSString *)subStringByMaxByte:(NSInteger)maxByte string:(NSString *)string {
    NSInteger sum = 0;
    NSString *subStr = [[NSString alloc] init];
    
    for(NSInteger i = 0; i < [string length]; i++){
        unichar strChar = [string characterAtIndex:i];
        
        if(strChar < 256){
            sum += 1;
        }
        else {
            sum += 2;
        }
        
        if (sum > maxByte) {
            
            subStr = [string substringToIndex:i];
            return subStr;
        }
        
    }
    return subStr;
}

- (BOOL)textView:(YYTextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if (self.textViewDelegate && [self.textViewDelegate respondsToSelector:_cmd]) {
        return  [self.textViewDelegate textView:textView shouldChangeTextInRange:range replacementText:text];
    }
    return YES;
}

- (void)updateHeight {
    CGFloat lastHeight = self.height;
    CGFloat lastBottom = self.bottom;
    
    if (self.status == ICChatBoxStatusShowVoice) {
        self.height = ToolBarHeight;
        self.bottom = lastBottom;
    }
    else {
        CGFloat maxHeight = 80.0f;
        CGFloat textHeight = HEIGHT_TEXTVIEW;
        CGSize size = [self.textView sizeThatFits:CGSizeMake(self.talkButton.frame.size.width, MAXFLOAT)];
        
        textHeight = MAX(textHeight, size.height);
        textHeight = MIN(textHeight, maxHeight);
        
        self.height = MAX(textHeight + 10, ToolBarHeight);
        self.bottom = lastBottom;
        
        self.textView.frame = CGRectMake(self.talkButton.x, (self.height - textHeight) / 2.0, self.talkButton.frame.size.width, textHeight);
    }
    
    if (lastHeight != self.height) {
        if (self.delegate && [self.delegate respondsToSelector:@selector(chatBox:changeChatBoxHeight:)]) {
            [self.delegate chatBox:self changeChatBoxHeight:self.height];
        }
    }
}

#pragma mark - Event
// 录音按钮点击事件
- (void) voiceButtonDown:(UIButton *)sender {
    ICChatBoxStatus lastStatus = self.status;
    if (lastStatus == ICChatBoxStatusShowVoice) {
        //正在显示talkButton，改为键盘状态
        self.status = ICChatBoxStatusShowKeyboard;
    }
    else {
        // 变成talkButton的状态
        self.status = ICChatBoxStatusShowVoice;
    }
}

// 更多（+）按钮
- (void) moreButtonDown:(UIButton *)sender {
    self.status = ICChatBoxStatusShowMore;
}

// 表情按钮
- (void) faceButtonDown:(UIButton *)sender {
    ICChatBoxStatus lastStatus = self.status;
    if (lastStatus == ICChatBoxStatusShowFace) {
        // 正在显示表情,改为现实键盘状态
        self.status = ICChatBoxStatusShowKeyboard;
    }
    else {
        self.status = ICChatBoxStatusShowFace;
    }
}

- (void) sendButtonDown:(UIButton *)sender {
    if (self.textView.text.length > 0) {
        if (_delegate && [_delegate respondsToSelector:@selector(chatBox:sendTextMessage:)]) {
            [_delegate chatBox:self sendTextMessage:self.textView.text];
        }
    }
    [self.textView setText:@""];
}

#pragma mark 说话按钮
- (void)talkButtonDown:(UIButton *)sender {
    if (_delegate && [_delegate respondsToSelector:@selector(chatBoxDidStartRecordingVoice:)]) {
        [_delegate chatBoxDidStartRecordingVoice:self];
    }
}

- (void)talkButtonUpInside:(UIButton *)sender {
    if (_delegate && [_delegate respondsToSelector:@selector(chatBoxDidStopRecordingVoice:)]) {
        [_delegate chatBoxDidStopRecordingVoice:self];
    }
}

- (void)talkButtonUpOutside:(UIButton *)sender {
    if (_delegate && [_delegate respondsToSelector:@selector(chatBoxDidCancelRecordingVoice:)]) {
        [_delegate chatBoxDidCancelRecordingVoice:self];
    }
}

- (void)talkButtonDragOutside:(UIButton *)sender {
    if ([_delegate respondsToSelector:@selector(chatBoxDidDrag:)]) {
        [_delegate chatBoxDidDrag:NO];
    }
}

- (void)talkButtonDragInside:(UIButton *)sender {
    if ([_delegate respondsToSelector:@selector(chatBoxDidDrag:)]) {
        [_delegate chatBoxDidDrag:YES];
    }
}

- (void)talkButtonTouchCancel:(UIButton *)sender {
    if (_delegate && [_delegate respondsToSelector:@selector(chatBoxDidCancelRecordingVoice:)]) {
        [_delegate chatBoxDidCancelRecordingVoice:self];
    }
}

#pragma mark - Lazy
- (UIView *) topLine {
    if (_topLine == nil) {
        _topLine = [UIView new];
        [_topLine setBackgroundColor:[IColor(0, 0, 0) colorWithAlphaComponent:0.05]];
    }
    return _topLine;
}

- (UIButton *) voiceButton {
    if (_voiceButton == nil) {
        _voiceButton = [UIButton new];
        [_voiceButton setImage:[UIImage imageNamed:@"icon_voice"] forState:UIControlStateNormal];
        [_voiceButton setContentEdgeInsets:UIEdgeInsetsMake(0, 15, 0, 3)];
        [_voiceButton sizeToFit];
        [_voiceButton addTarget:self action:@selector(voiceButtonDown:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _voiceButton;
}

- (UIButton *) moreButton {
    if (_moreButton == nil) {
        _moreButton = [UIButton new];
        [_moreButton setImage:[UIImage imageNamed:@"input_more"] forState:UIControlStateNormal];
        [_moreButton addTarget:self action:@selector(moreButtonDown:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _moreButton;
}

- (UIButton *) faceButton {
    if (_faceButton == nil) {
        _faceButton = [UIButton new];
        [_faceButton setImage:[UIImage imageNamed:@"icon_emotion"] forState:UIControlStateNormal];
        [_faceButton addTarget:self action:@selector(faceButtonDown:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _faceButton;
}

- (UIButton *)sendButton {
    if (!_sendButton) {
        _sendButton = ({
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            [button setTitleColor:UIColorHex(#FFFFFF) forState:UIControlStateNormal];
            [button.titleLabel setFont:[UIFont yk_pingFangMedium:14]];
            [button setTitle:KLanguage( @"发送") forState:UIControlStateNormal];
            [button setBackgroundImage:[UIImage imageWithColor:UIColorHex(#1890FF)] forState:UIControlStateNormal];
            [button setBackgroundImage:[UIImage imageWithColor:[UIColorHex(#1890FF) colorWithAlphaComponent:0.5]] forState:UIControlStateHighlighted];
            button.size = CGSizeMake(50, 25);
            button.layer.cornerRadius = 2.0;
            button.layer.masksToBounds = YES;
            [button addTarget:self action:@selector(sendButtonDown:) forControlEvents:UIControlEventTouchUpInside];
            button.hidden = YES;
            button;
        });
    }
    return _sendButton;
}

- (YYTextView *) textView {
    if (_textView == nil) {
        _textView = [YYTextView new];
        [_textView setFont:[UIFont systemFontOfSize:16.0f]];
        [_textView.layer setMasksToBounds:YES];
        [_textView.layer setCornerRadius:4.0f];
        [_textView.layer setBorderWidth:0.5f];
        [_textView.layer setBorderColor:self.topLine.backgroundColor.CGColor];
        [_textView setScrollsToTop:NO];
        [_textView setReturnKeyType:UIReturnKeyDefault];
        [_textView setDelegate:self];
        _textView.textParser = ({
            VULChatTextParser *textParser = [VULChatTextParser new];
            textParser.font = _textView.font;
            textParser.textColor = _textView.textColor;
            textParser;
        });
    }
    return _textView;
}

- (UIButton *) talkButton {
    if (_talkButton == nil) {
        _talkButton = [UIButton new];
        [_talkButton setTitle:@"按住 说话" forState:UIControlStateNormal];
        [_talkButton setTitle:@"松开 结束" forState:UIControlStateHighlighted];
        [_talkButton setTitleColor:UIColorHex(#333333) forState:UIControlStateNormal];
        [_talkButton setBackgroundImage:[UIImage gxz_imageWithColor:[UIColor whiteColor]] forState:UIControlStateNormal];
        [_talkButton setBackgroundImage:[UIImage gxz_imageWithColor:[UIColor lightGrayColor]] forState:UIControlStateHighlighted];
        [_talkButton.titleLabel setFont:[UIFont boldSystemFontOfSize:14.0f]];
        [_talkButton.layer setMasksToBounds:YES];
        [_talkButton.layer setCornerRadius:4.0f];
        [_talkButton setHidden:YES];
        [_talkButton addTarget:self action:@selector(talkButtonDown:) forControlEvents:UIControlEventTouchDown];
        [_talkButton addTarget:self action:@selector(talkButtonUpInside:) forControlEvents:UIControlEventTouchUpInside];
        [_talkButton addTarget:self action:@selector(talkButtonUpOutside:) forControlEvents:UIControlEventTouchUpOutside];
        [_talkButton addTarget:self action:@selector(talkButtonTouchCancel:) forControlEvents:UIControlEventTouchCancel];
        [_talkButton addTarget:self action:@selector(talkButtonDragOutside:) forControlEvents:UIControlEventTouchDragOutside];
        [_talkButton addTarget:self action:@selector(talkButtonDragInside:) forControlEvents:UIControlEventTouchDragInside];
    }
    return _talkButton;
}

- (UIButton *) enbleButton {
    if (_enbleButton == nil) {
        _enbleButton = [UIButton new];
        [_enbleButton setTitle:@"全体禁言" forState:UIControlStateNormal];
        [_enbleButton setTitleColor:UIColorHex(#333333) forState:UIControlStateNormal];
        [_enbleButton setBackgroundImage:[UIImage gxz_imageWithColor:[UIColor whiteColor]] forState:UIControlStateNormal];
        [_enbleButton setBackgroundImage:[UIImage gxz_imageWithColor:[UIColor lightGrayColor]] forState:UIControlStateHighlighted];
        [_enbleButton.titleLabel setFont:[UIFont boldSystemFontOfSize:14.0f]];
        [_enbleButton.layer setMasksToBounds:YES];
        [_enbleButton.layer setCornerRadius:4.0f];
        [_enbleButton setHidden:YES];
        _enbleButton.enabled = NO;
    }
    return _enbleButton;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
