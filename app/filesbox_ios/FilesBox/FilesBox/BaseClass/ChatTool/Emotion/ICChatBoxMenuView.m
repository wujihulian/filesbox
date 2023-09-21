//
//  ICChatBoxMenuView.m
//  
//

#import "ICChatBoxMenuView.h"
#import "ICEmotionMenuButton.h"
#import "YKChatPrefixHeader.h"

@interface ICChatBoxMenuView ()

@property (nonatomic, weak) UIScrollView *scrollView;
@property (nonatomic, weak) UIButton     *sendBtn;

@property (nonatomic, weak) ICEmotionMenuButton *selectedBtn;

@end

@implementation ICChatBoxMenuView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {

        [self setupBtn:[@"0x1f603" emoji] buttonType:ICEmotionMenuButtonTypeEmoji];
        [self setupBtn:@"Custom" buttonType:ICEmotionMenuButtonTypeCuston];
//        [self setupBtn:@"Gif" buttonType:ICEmotionMenuButtonTypeGif];
    }
    return self;
}

/**
 *  创建按钮
 *
 *  @param title      按钮文字
 *  @param buttonType 类型
 *
 *  @return 按钮
 */
- (ICEmotionMenuButton *)setupBtn:(NSString *)title
                       buttonType:(ICEmotionMenuButtonType)buttonType {
    ICEmotionMenuButton *btn = [[ICEmotionMenuButton alloc] init];
    [btn addTarget:self action:@selector(btnClicked:) forControlEvents:UIControlEventTouchDown];
    btn.tag                  = buttonType; // 不要把0作为tag值
    
    if ([title isEqualToString:@"Custom"]) {
        [btn setImage:[UIImage imageNamed:@"[吓]"] forState:UIControlStateNormal];
    } else {
        [btn setTitle:title forState:UIControlStateNormal];
        btn.titleLabel.font = [UIFont systemFontOfSize:26.5];
    }
    [self.scrollView addSubview:btn];
    [btn setBackgroundImage:[UIImage gxz_imageWithColor:[UIColor whiteColor]]forState:UIControlStateNormal];
    [btn setBackgroundImage:[UIImage gxz_imageWithColor:IColor(241, 241, 244)] forState:UIControlStateSelected];
    return btn;
}

#pragma mark - Private

- (void)sendBtnClicked:(UIButton *)sendBtn {
    [[NSNotificationCenter defaultCenter] postNotificationName:GXEmotionDidSendNotification object:nil];
}

- (void)btnClicked:(ICEmotionMenuButton *)button {
    self.selectedBtn.selected = NO;
    button.selected           = YES;
    self.selectedBtn         = button;
    if ([self.delegate respondsToSelector
         :@selector(emotionMenu:didSelectButton:)]) {
        [self.delegate emotionMenu:self
                   didSelectButton:(int)button.tag];
    }
}

- (void)setDelegate:(id<ICChatBoxMenuDelegate>)delegate {
    _delegate = delegate;
    
    [self btnClicked:(ICEmotionMenuButton *)[self viewWithTag:ICEmotionMenuButtonTypeEmoji]];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    NSUInteger count      = self.scrollView.subviews.count;
    CGFloat btnW          = 60;
    self.scrollView.frame = CGRectMake(0, 0, self.width-btnW, self.height);
    self.sendBtn.frame    = CGRectMake(self.width-btnW, 0, btnW, self.height);
    CGFloat btnH          = self.height;
    for (int i = 0; i < count; i ++) {
        ICEmotionMenuButton *btn = self.scrollView.subviews[i];
        btn.frame = CGRectMake((int)btnW * i, 0, (int)btnW, btnH);
    }
}

#pragma mark - Getter
- (UIScrollView *)scrollView {
    if (!_scrollView) {
        UIScrollView *scrollView = [[UIScrollView alloc] init];
        [scrollView setShowsHorizontalScrollIndicator:NO];
        [scrollView setShowsVerticalScrollIndicator:NO];
        [scrollView setScrollsToTop:NO];
        [self addSubview:scrollView];
        [scrollView setBackgroundColor:[UIColor whiteColor]];
        _scrollView              = scrollView;
    }
    return _scrollView;
}

- (UIButton *)sendBtn {
    if (!_sendBtn) {
        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        [btn setTitle:@"发送" forState:UIControlStateNormal];
        btn.titleLabel.font = [UIFont systemFontOfSize:15.0];
        [btn setBackgroundColor:[UIColor colorWithRed:0.1 green:0.4 blue:0.8 alpha:1.0]];
        [self addSubview:btn];
        _sendBtn = btn;
        [btn addTarget:self action:@selector(sendBtnClicked:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _sendBtn;
}

@end
