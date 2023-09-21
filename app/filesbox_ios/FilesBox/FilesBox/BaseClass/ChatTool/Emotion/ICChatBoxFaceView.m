//
//  ICChatBoxFaceView.m
//

#import "ICChatBoxFaceView.h"
#import "ICChatBoxMenuView.h"
#import "ICEmotionListView.h"
#import "ICFaceManager.h"

#define bottomViewH 36.0

@interface ICChatBoxFaceView ()<UIScrollViewDelegate,ICChatBoxMenuDelegate>

@property (nonatomic, weak) ICEmotionListView *showingListView;
@property (nonatomic, strong) ICEmotionListView *emojiListView;
@property (nonatomic, strong) ICEmotionListView *custumListView;
@property (nonatomic, strong) ICEmotionListView *gifListView;

@property (nonatomic, weak) ICChatBoxMenuView *menuView;

@end

@implementation ICChatBoxFaceView

- (id) initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {

        ICChatBoxMenuView *menuView = [[ICChatBoxMenuView alloc] init];
        [menuView setDelegate:self];
        [self addSubview:menuView];
        _menuView = menuView;
        
    }
    return self;
}

#pragma mark - Private

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.menuView.frame = CGRectMake(0, CGRectGetHeight(self.frame) - bottomViewH, CGRectGetWidth(self.frame), bottomViewH);
    self.menuView.hidden = YES;
    self.showingListView.frame = CGRectMake(0, 0, CGRectGetWidth(self.frame), CGRectGetHeight(self.frame));
}


#pragma mark - ICChatBoxMenuDelegate

- (void)emotionMenu:(ICChatBoxMenuView *)menu didSelectButton:(ICEmotionMenuButtonType)buttonType {
    [self.showingListView removeFromSuperview];
    switch (buttonType) {
        case ICEmotionMenuButtonTypeEmoji:
            [self addSubview:self.emojiListView];
            break;
        case ICEmotionMenuButtonTypeCuston:
            [self addSubview:self.custumListView];
            break;
        case ICEmotionMenuButtonTypeGif:
            [self addSubview:self.gifListView];
            break;
        default:
            break;
    }
    self.showingListView = [self.subviews lastObject];
    [self setNeedsLayout];
}


#pragma mark - Getter

- (ICEmotionListView *)emojiListView {
    if (!_emojiListView) {
        _emojiListView           = [[ICEmotionListView alloc] init];
        _emojiListView.emotions  = [ICFaceManager emotions];
    }
    return _emojiListView;
}

- (ICEmotionListView *)gifListView {
    if (!_gifListView) {
        _gifListView             = [[ICEmotionListView alloc] init];
    }
    return _gifListView;
}

- (ICEmotionListView *)custumListView {
    if (!_custumListView) {
        _custumListView          = [[ICEmotionListView alloc] init];
        _custumListView.emotions = [ICFaceManager customEmotion];
    }
    return _custumListView;
}


@end
