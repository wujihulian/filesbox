//
//  VULPlayMp3VC.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/9/18.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULPlayMp3VC.h"
#import "UUMarqueeView.h"
#import "VULSelectSpeedView.h"
@interface VULPlayMp3VC ()<UITextViewDelegate,UUMarqueeViewDelegate,ZFSliderViewDelegate>

@property (nonatomic, strong) UIView *mainView;

@property (nonatomic, strong) UUMarqueeView       *marqueeView;

/** 指针 */
@property (nonatomic, strong) UIImageView       *needleView;

@property (nonatomic, strong) UIImageView       *diskImageView;

@property (nonatomic, strong) UIImageView       *coverImageView;

/** 定时器 */
@property (nonatomic, strong) CADisplayLink     *displayLink;

@property (nonatomic, assign) BOOL     isAnimate;


@property (nonatomic, strong) ZFSliderView    *slider;
@property (nonatomic, strong) UILabel   *currentLabel;
@property (nonatomic, strong) UILabel   *totalLabel;

@property (nonatomic, strong) UIButton  *listBtn;
@property (nonatomic, strong) UIButton  *speedBtn;

@property (nonatomic, strong) UIButton  *playBtn;
@property (nonatomic, strong) UIButton  *prevBtn;
@property (nonatomic, strong) UIButton  *nextBtn;

@property (nonatomic, assign) NSInteger speedIndex;
@end

@implementation VULPlayMp3VC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.navigationTitle = self.title;
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];

    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    CGFloat height = kScreenHeight  - K_NavBar_Height;
    _mainView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, height)];
    [self.view addSubview:self.marqueeView];
    [self.view addSubview:self.diskImageView];
    [self.diskImageView addSubview:self.coverImageView];
    [self.view addSubview:self.needleView];
    
    [self.needleView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.marqueeView.height + K_NavBar_Height);
        make.left.mas_equalTo(self.view.mas_centerX).offset(- 8);
        make.size.mas_equalTo(self.needleView.size);
    }];
    
    [self.diskImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.needleView.mas_bottom);
        make.centerX.mas_equalTo(self.view.mas_centerX);
        make.size.mas_equalTo(self.diskImageView.size);
    }];
    
    [self.coverImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.mas_equalTo(self.diskImageView.mas_centerX);
        make.centerY.mas_equalTo(self.diskImageView.mas_centerY).offset(- 8);
        make.width.height.mas_equalTo(134);
    }];
    _speedIndex = 2;
    
    [self.view addSubview:self.currentLabel];
    [self.view addSubview:self.totalLabel];
    [self.view addSubview:self.slider];
    self.listBtn.hidden = YES;
    [self.view addSubview:self.listBtn];
    [self.view addSubview:self.speedBtn];
    
    [self.view addSubview:self.playBtn];
    [self.view addSubview:self.prevBtn];
    [self.view addSubview:self.nextBtn];
    
    [self.currentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(FontAuto(16));
        make.centerY.mas_equalTo(self.slider.mas_centerY);
        make.size.mas_equalTo(self.totalLabel.size);
    }];
    
    [self.slider mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.top.mas_equalTo(self.coverImageView.mas_bottom).offset(fontAuto(200));
        make.left.mas_equalTo(self.currentLabel.mas_right).offset(FontAuto(10));
        make.right.mas_equalTo(self.totalLabel.mas_left).offset(- FontAuto(10));
        make.height.mas_equalTo(self.slider.height);
    }];
    
    [self.totalLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo( - FontAuto(16));
        make.centerY.mas_equalTo(self.slider.mas_centerY);
        make.size.mas_equalTo(self.totalLabel.size);
    }];
    
    [self.listBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.playBtn.mas_centerY);
        make.left.mas_equalTo(FontAuto(10));
        make.size.mas_equalTo(self.listBtn.size);
    }];
    
    [self.speedBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.playBtn.mas_centerY);
        make.right.mas_equalTo( - FontAuto(10));
        make.size.mas_equalTo(self.speedBtn.size);
    }];
    
    
    [self.playBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.slider.mas_bottom).offset(FontAuto(30));
        make.centerX.mas_equalTo(self.view.mas_centerX);
        make.size.mas_equalTo(self.playBtn.size);
        make.bottom.mas_equalTo(-(K_TabBar_Height-fontAuto(20)));
    }];
    
    [self.prevBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.playBtn.mas_centerY);
        make.right.mas_equalTo(self.playBtn.mas_left).offset(- FontAuto(35));
        make.size.mas_equalTo(self.prevBtn.size);
    }];
    
    [self.nextBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.playBtn.mas_centerY);
        make.left.mas_equalTo(self.playBtn.mas_right).offset(FontAuto(35));
        make.size.mas_equalTo(self.nextBtn.size);
    }];
    [self configPlayer];
    [self.marqueeView reloadData];
    [self setCoursewareDetail];
    [self play];
}
-(void)viewWillDisappear:(BOOL)animated{
    [self stopPaly];
}
#pragma mark 播放器回调
- (void)configPlayer {
    MJWeakSelf
    [self.player setPlayerPlayTimeChanged:^(id<ZFPlayerMediaPlayback>  _Nonnull asset, NSTimeInterval currentTime, NSTimeInterval duration) {
        [weakSelf updateCurrentTime:currentTime];
    }];
    
  
    
    self.player.playerDidToEnd = ^(id<ZFPlayerMediaPlayback>  _Nonnull asset) {
        [self stopPaly];
    };
}


#pragma mark 播放
- (void)play {
    
//    NSArray *array = [self.mp3Url componentsSeparatedByString:@"?"];
//    NSString *encodedString = [array[0] stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
//    NSString *resultStr = [NSString stringWithFormat:@"%@?%@",encodedString,array[1]];
    NSURL *URL = [NSURL URLWithString:self.mp3Url];
    
    self.player.assetURL = URL;
    [self startPlay];
}

-(void)startPlay{
    if (self.player.currentPlayerManager.playState == ZFPlayerPlayStatePlayStopped) {
        [self.player.currentPlayerManager replay];
        [self play];
    }
    else {
        [self.player.currentPlayerManager play];
    }
    [self playedWithAnimated:YES];
    [self.marqueeView start];
    self.playBtn.selected = YES;
}

-(void)stopPaly{
    if (self.player.currentPlayerManager.playState == ZFPlayerPlayStatePlayStopped) {
        [self.player.currentPlayerManager stop];
    }
    else {
        [self.player.currentPlayerManager pause];
    }
   
    [self pausedWithAnimated:YES];
    [self.marqueeView pause];
    self.playBtn.selected = NO;

}


#pragma mark - Setter
- (void)setCoursewareDetail {
//    _coursewareDetail = coursewareDetail;
    self.totalLabel.text = [NSString stringWithFormat:@"%.2ld:%.2ld",self.playLength.integerValue / 60, self.playLength.integerValue % 60];
    [self.totalLabel sizeToFit];
    self.totalLabel.width = self.totalLabel.width + 3;
    
    [self.currentLabel mas_updateConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(self.totalLabel.size);
    }];
    
    [self.totalLabel mas_updateConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(self.totalLabel.size);
    }];
}


#pragma mark - OLDZFSliderViewDelegate
- (void)sliderTouchBegan:(float)value {
    self.slider.isdragging = YES;
}

- (void)sliderTouchEnded:(float)value {
    if (self.player.totalTime > 0) {
        @weakify(self)
        [self.player seekToTime:self.player.totalTime * value completionHandler:^(BOOL finished) {
            @strongify(self)
            if (finished) {
                self.slider.isdragging = NO;
                [self startPlay];


            }
        }];
    }
    else {
        self.slider.isdragging = NO;
    }
}

- (void)sliderValueChanged:(float)value {
    if (self.player.totalTime == 0) {
        self.slider.value = 0;
        return;
    }
    self.slider.isdragging = YES;

    NSTimeInterval currentTime = self.player.totalTime * value;
    self.currentLabel.text = [NSString stringWithFormat:@"%.2ld:%.2ld",(NSInteger)currentTime / 60, (NSInteger)currentTime % 60];
}

- (void)sliderTapped:(float)value {
    if (self.player.totalTime > 0) {
        self.slider.isdragging = YES;
        @weakify(self)
        [self.player seekToTime:self.player.totalTime * value completionHandler:^(BOOL finished) {
            @strongify(self)
            if (finished) {
                self.slider.isdragging = NO;
                [self startPlay];
            }
        }];
    }
    else {
        self.slider.isdragging = NO;
        self.slider.value = 0;
    }
}

#pragma mark - 事件
- (void)playAction {
    self.playBtn.selected = !self.playBtn.selected;
 
    if (self.playBtn.selected) {
        [self startPlay];

    
    }else{
        [self stopPaly];
    }
}

- (void)updateCurrentTime:(NSTimeInterval) currentTime {
    self.currentLabel.text = [NSString stringWithFormat:@"%.2ld:%.2ld",(NSInteger)currentTime / 60, (NSInteger)currentTime % 60];
    
    if (!self.slider.isdragging) {
        [self.slider setValue:currentTime / self.playLength.floatValue];
    }
    
}

- (void)goToBack {
    [self.player seekToTime:self.slider.value * self.playLength.doubleValue  - 15 completionHandler:nil];
;
}

- (void)goToForward {
    [self.player seekToTime:self.slider.value * self.playLength.doubleValue  + 15 completionHandler:nil];

}

- (void)speedBtnClick {
    VULSelectSpeedView *sheet  = [[VULSelectSpeedView alloc] initWithSelectIndex:_speedIndex];
    
    zhPopupController *popup = [[zhPopupController alloc] initWithView:sheet size:sheet.size];
    popup.layoutType = zhPopupLayoutTypeBottom;
    popup.presentationStyle = zhPopupSlideStyleFromBottom;
    [popup showInView:self.view duration:0.35 delay:0 options:UIViewAnimationOptionCurveEaseInOut bounced:NO completion:nil];
    
    MJWeakSelf
    [sheet setSelectSpeedBlock:^(NSInteger selectIndex, NSString * _Nonnull speed) {
        weakSelf.speedIndex = selectIndex;
        weakSelf.player.currentPlayerManager.rate = selectIndex;
        [popup dismiss];
    }];
}

//播放列表
- (void)nextBtnClick {
//    [self.viewModel.playlistSubject sendNext:self.playBtn.selected ? @"1" : @"0"];
}

#pragma mark - Lazy
- (ZFSliderView *)slider {
    if (!_slider) {
        _slider = ({
            ZFSliderView * slider = [ZFSliderView new];
            slider.frame = CGRectMake(0, 0, 200, 20);
            slider.minimumTrackTintColor = UIColorHex(#108EE9);
            slider.maximumTrackTintColor = [UIColorHex(#333333) colorWithAlphaComponent:0.1];
            
            CGRect rect = CGRectMake(0, 0, 20.0, 20.0);

            UIImage *thumbBgImage = [UIImage imageWithSize:rect.size
                                               drawBlock:^(CGContextRef  _Nonnull context) {
                CGContextAddEllipseInRect(context, rect);
                CGContextSetFillColorWithColor(context, [UIColorHex(#108EE9) colorWithAlphaComponent:0.3].CGColor);
                CGContextFillPath(context);
            }];
            
           CGRect thumbImageRect = CGRectMake(0 , 0, 14, 14);
            UIImage *thumbImage = [UIImage imageWithSize:thumbImageRect.size
                                               drawBlock:^(CGContextRef  _Nonnull context) {
                CGContextAddEllipseInRect(context, thumbImageRect);
                CGContextSetFillColorWithColor(context, UIColorHex(#108EE9).CGColor);
                CGContextFillPath(context);
            }];
            
            [slider setThumbImage:thumbImage forState:UIControlStateNormal];
            [slider setBackgroundImage:thumbBgImage forState:UIControlStateNormal];
            slider.delegate = self;
            slider;
        });
    }
    return _slider;
}

- (UILabel *)currentLabel {
    if (!_currentLabel) {
        _currentLabel = [UILabel new];
        _currentLabel.textColor = UIColorHex(#333333);
        _currentLabel.font = [UIFont systemFontOfSize:FontAuto(11)];
        _currentLabel.text = @"00:00";
    }
    return _currentLabel;
}

- (UILabel *)totalLabel {
    if (!_totalLabel) {
        _totalLabel = [UILabel new];
        _totalLabel.textColor = UIColorHex(#333333);
        _totalLabel.font = [UIFont systemFontOfSize:FontAuto(11)];
        _totalLabel.text = @"00:00";
    }
    return _totalLabel;
}

- (UIButton *)listBtn {
    if (!_listBtn) {
        _listBtn = ({
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            [button setTitle:@"列表" forState:UIControlStateNormal];
            [button setTitleColor:UIColorHex(#666666) forState:UIControlStateNormal];
            [button.titleLabel setFont:[UIFont systemFontOfSize:FontAuto(12)]];
            [button setImage:[UIImage imageForIconBundle:@"icon_audio_list"] forState:UIControlStateNormal];
                        
            [button sizeToFit];
            CGFloat spacing = 8;
            button.width = MAX(button.imageView.width, button.titleLabel.width) + 10;
            button.height = button.imageView.height + spacing + button.titleLabel.height + 10;
            [button setImagePositionWithType:VULImagePositionTypeTop spacing:spacing];
            [button addTarget:self action:@selector(nextBtnClick) forControlEvents:UIControlEventTouchUpInside];
            button;
        });
        
    }
    return _listBtn;
}

- (UIButton *)speedBtn {
    if (!_speedBtn) {
        _speedBtn = ({
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            [button setTitle:@"倍速" forState:UIControlStateNormal];
            [button setTitleColor:UIColorHex(#666666) forState:UIControlStateNormal];
            [button.titleLabel setFont:[UIFont systemFontOfSize:FontAuto(12)]];
            [button setImage:[UIImage imageForIconBundle:@"icon_speed"] forState:UIControlStateNormal];
            
            [button sizeToFit];
            CGFloat spacing = 8;
            button.width = MAX(button.imageView.width, button.titleLabel.width) + 10;
            button.height = button.imageView.height + spacing + button.titleLabel.height + 10;
            [button setImagePositionWithType:VULImagePositionTypeTop spacing:spacing];
            
            [button addTarget:self action:@selector(speedBtnClick) forControlEvents:UIControlEventTouchUpInside];
            button;
        });
        
    }
    return _speedBtn;
}

- (UIButton *)playBtn {
    if (!_playBtn) {
        _playBtn = ({
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            [button setImage:[UIImage imageForIconBundle:@"icon_play"] forState:UIControlStateNormal];
            [button setImage:[UIImage imageForIconBundle:@"icon_pause"] forState:UIControlStateSelected];
            [button setBackgroundImage:[UIImage imageForIconBundle:@"icon_play_bg"] forState:UIControlStateNormal];
            [button setBackgroundImage:[UIImage imageForIconBundle:@"icon_play_bg"] forState:UIControlStateHighlighted];
            [button sizeToFit];
            [button addTarget:self action:@selector(playAction) forControlEvents:UIControlEventTouchUpInside];
            button;
        });
        
    }
    return _playBtn;
}

- (UIButton *)prevBtn {
    if (!_prevBtn) {
        _prevBtn = ({
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            [button setImage:[UIImage imageForIconBundle:@"icon_play_back"] forState:UIControlStateNormal];
            [button sizeToFit];
            [button addTarget:self action:@selector(goToBack) forControlEvents:UIControlEventTouchUpInside];
            button;
        });
        
    }
    return _prevBtn;
}

- (UIButton *)nextBtn {
    if (!_nextBtn) {
        _nextBtn = ({
            UIButton *button = [UIButton new];
            [button setImage:[UIImage imageForIconBundle:@"icon_audio_forward"] forState:UIControlStateNormal];
            [button sizeToFit];
            [button addTarget:self action:@selector(goToForward) forControlEvents:UIControlEventTouchUpInside];
            button;
        });
        
    }
    return _nextBtn;
}



#pragma mark - Lazy
- (ZFPlayerController *)player {
    if (!_player) {
        _player = ({
            UIView *containerView = [[UIView alloc] initWithFrame:CGRectMake(0, K_NavBar_Height, kScreenWidth, 0)];
            [self.mainView addSubview:containerView];
            
            ZFAVPlayerManager *playerManager = [[ZFAVPlayerManager alloc] init];
            ZFPlayerController *player = [[ZFPlayerController alloc] initWithPlayerManager:playerManager containerView:containerView];
            player.pauseWhenAppResignActive = NO;
            player.allowOrentitaionRotation = NO;
            player;
        });
    }
    return _player;;
}










#pragma mark - UUMarqueeViewDelegate
- (NSUInteger)numberOfVisibleItemsForMarqueeView:(UUMarqueeView*)marqueeView {
    return self.mp3Url ? 1 : 0;
}

- (NSUInteger)numberOfDataForMarqueeView:(UUMarqueeView*)marqueeView {
    return self.mp3Url ? 1 : 0;
}

- (void)createItemView:(UIView*)itemView forMarqueeView:(UUMarqueeView*)marqueeView {
    UILabel *content = [[UILabel alloc] initWithFrame:itemView.bounds];
    content.font = [UIFont boldSystemFontOfSize:FontAuto(20)];
    content.textColor = UIColorHex(#333333);
    content.textAlignment = NSTextAlignmentCenter;
    content.tag = 1001;
    [itemView addSubview:content];
}

- (void)updateItemView:(UIView*)itemView atIndex:(NSUInteger)index forMarqueeView:(UUMarqueeView*)marqueeView {
    UILabel *content = [itemView viewWithTag:1001];
    content = @"MP3";
}

- (CGFloat)itemViewWidthAtIndex:(NSUInteger)index forMarqueeView:(UUMarqueeView*)marqueeView {
    UILabel *content = [[UILabel alloc] init];
    content.font = [UIFont boldSystemFontOfSize:FontAuto(20)];
    content.text = @"MP3";
    return content.intrinsicContentSize.width;
}

#pragma mark - 事件、私有方法
- (void)playedWithAnimated:(BOOL)animated {
    if (!_isAnimate) {
        [self setAnchorPoint:CGPointMake(4 / self.needleView.size.width, 4 / self.needleView.size.height) forView:self.needleView];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            if (animated) {
                [UIView animateWithDuration:0.5f animations:^{
                    self.needleView.transform = CGAffineTransformMakeRotation(M_PI_2 / 2.9);
                }];
            }
            else {
                self.needleView.transform = CGAffineTransformMakeRotation(M_PI_2 / 2.9);
            }
        });
        
        self.displayLink = [CADisplayLink displayLinkWithTarget:self selector:@selector(diskAnimation)];
        [self.displayLink addToRunLoop:[NSRunLoop mainRunLoop] forMode:NSRunLoopCommonModes];
        
        _isAnimate = YES;
    }
}

- (void)pausedWithAnimated:(BOOL)animated {
    if (_isAnimate) {
        [self setAnchorPoint:CGPointMake(4 / self.needleView.size.width, 4 / self.needleView.size.height) forView:self.needleView];
        
        
        dispatch_async(dispatch_get_main_queue(), ^{
               if (animated) {
                   [UIView animateWithDuration:0.5f animations:^{
                       self.needleView.transform = CGAffineTransformIdentity;
                   }];
               }
               else {
                   self.needleView.transform = CGAffineTransformIdentity;
               }
           });
        
        if (self.displayLink) {
            [self.displayLink invalidate];
            self.displayLink = nil;
        }
        
        _isAnimate = NO;
    }
}

- (void)diskAnimation {
    self.coverImageView.transform = CGAffineTransformRotate(self.coverImageView.transform, M_PI_4 / 100.0f);
}

- (void)setAnchorPoint:(CGPoint)anchorPoint forView:(UIView *)view {
    CGPoint oldOrigin = view.frame.origin;
    view.layer.anchorPoint = anchorPoint;
    
    CGPoint newOrigin = view.frame.origin;
    
    CGPoint transition;
    transition.x = newOrigin.x - oldOrigin.x;
    transition.y = newOrigin.y - oldOrigin.y;
    
    view.center = CGPointMake(view.center.x - transition.x, view.center.y - transition.y);
}

#pragma mark - Lazy
- (UUMarqueeView *)marqueeView {
    if (!_marqueeView) {
        _marqueeView = [[UUMarqueeView alloc] initWithFrame:CGRectMake(0, 0, AdaptedWidth(200), FontAuto(25)) direction:UUMarqueeViewDirectionLeftward];
        _marqueeView.centerX  = kScreenWidth / 2.0;
        _marqueeView.delegate = self;
        _marqueeView.timeIntervalPerScroll = 0.0f;
        _marqueeView.scrollSpeed = 30.0f;
        _marqueeView.itemSpacing = 20.0f;
        _marqueeView.stopWhenLessData = YES;
    }
    return _marqueeView;
}
- (UIImageView *)needleView {
    if (!_needleView) {
        _needleView = ({
            UIImageView *imageView = [UIImageView new];
            imageView.image = [UIImage imageForIconBundle:@"image_needle"];
            [imageView sizeToFit];
            imageView;
        });
        
    }
    return _needleView;
}

- (UIImageView *)diskImageView {
    if (!_diskImageView) {
        _diskImageView = ({
            UIImageView *imageView  = [UIImageView new];
            imageView.image = [UIImage imageForIconBundle:@"image_disk"];
            [imageView sizeToFit];
            imageView;
        });
        
    }
    return _diskImageView;
}

- (UIImageView *)coverImageView {
    if (!_coverImageView) {
        _coverImageView = ({
            UIImageView *imageView  = [UIImageView new];
            imageView.image = [UIImage imageForIconBundle:@"文件夹_default"];
            imageView.contentMode = UIViewContentModeScaleAspectFill;
            [imageView sizeToFit];
            imageView.layer.cornerRadius = imageView.width / 2.0;
            imageView.layer.masksToBounds = YES;
            imageView;
        });
        
    }
    return _coverImageView;
}
@end


