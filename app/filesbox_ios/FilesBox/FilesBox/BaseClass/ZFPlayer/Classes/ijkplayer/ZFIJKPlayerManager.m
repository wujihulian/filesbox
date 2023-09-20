//
//  ZFIJKPlayerManager.m
//  ZFPlayer
//
// Copyright (c) 2016年 任子丰 ( http://github.com/renzifeng )
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#import "ZFIJKPlayerManager.h"
#if __has_include(<ZFPlayer/ZFPlayer.h>)
#import <ZFPlayer/ZFPlayer.h>
#import <ZFPlayer/ZFPlayerConst.h>
#else
#import "ZFPlayer.h"
#import "ZFPlayerConst.h"
#endif
#if __has_include(<IJKMediaFramework/IJKMediaFramework.h>)

@interface ZFIJKPlayerManager ()

@property (nonatomic, strong) IJKFFMoviePlayerController *player;
@property (nonatomic, strong) IJKFFOptions *options;
@property (nonatomic, assign) CGFloat lastVolume;
@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, assign) BOOL isReadyToPlay;

@end

@implementation ZFIJKPlayerManager
@synthesize view                           = _view;
@synthesize currentTime                    = _currentTime;
@synthesize totalTime                      = _totalTime;
@synthesize playerPlayTimeChanged          = _playerPlayTimeChanged;
@synthesize playerBufferTimeChanged        = _playerBufferTimeChanged;
@synthesize playerDidToEnd                 = _playerDidToEnd;
@synthesize bufferTime                     = _bufferTime;
@synthesize playState                      = _playState;
@synthesize loadState                      = _loadState;
@synthesize assetURL                       = _assetURL;
@synthesize playerPrepareToPlay            = _playerPrepareToPlay;
@synthesize playerReadyToPlay              = _playerReadyToPlay;
@synthesize playerPlayStateChanged         = _playerPlayStateChanged;
@synthesize playerLoadStateChanged         = _playerLoadStateChanged;
@synthesize seekTime                       = _seekTime;
@synthesize muted                          = _muted;
@synthesize volume                         = _volume;
@synthesize presentationSize               = _presentationSize;
@synthesize isPlaying                      = _isPlaying;
@synthesize rate                           = _rate;
@synthesize isPreparedToPlay               = _isPreparedToPlay;
@synthesize shouldAutoPlay                 = _shouldAutoPlay;
@synthesize scalingMode                    = _scalingMode;
@synthesize playerPlayFailed               = _playerPlayFailed;
@synthesize presentationSizeChanged        = _presentationSizeChanged;

- (void)dealloc {
    [self stop];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        _scalingMode = ZFPlayerScalingModeAspectFit;
        _shouldAutoPlay = YES;
    }
    return self;
}

- (void)prepareToPlay {
    if (!_assetURL) return;
    _isPreparedToPlay = YES;
    [self initializePlayer];
    if (self.shouldAutoPlay) {
        [self play];
    }
    self.loadState = ZFPlayerLoadStatePrepare;
    if (self.playerPrepareToPlay) self.playerPrepareToPlay(self, self.assetURL);
}

- (void)reloadPlayer {
    [self prepareToPlay];
}

- (void)play {
    if (!_isPreparedToPlay) {
        [self prepareToPlay];
    } else {
        [self.player play];
        if (self.timer) [self.timer setFireDate:[NSDate date]];
        self.player.playbackRate = self.rate;
        _isPlaying = YES;
        self.playState = ZFPlayerPlayStatePlaying;
    }
}

- (void)pause {
    if (self.timer) [self.timer setFireDate:[NSDate distantFuture]];
    [self.player pause];
    _isPlaying = NO;
    self.playState = ZFPlayerPlayStatePaused;
}

- (void)stop {
    [self removeMovieNotificationObservers];
    [self.player shutdown];
    [self.player.view removeFromSuperview];
    self.player = nil;
    _assetURL = nil;
    [self.timer invalidate];
    self.presentationSize = CGSizeZero;
    self.timer = nil;
    _isPlaying = NO;
    _isPreparedToPlay = NO;
    self->_currentTime = 0;
    self->_totalTime = 0;
    self->_bufferTime = 0;
    self.isReadyToPlay = NO;
    self.playState = ZFPlayerPlayStatePlayStopped;
}

- (void)replay {
//    @zf_weakify(self)
    [self seekToTime:0 completionHandler:^(BOOL finished) {
//        @zf_strongify(self)
        if (finished) {
            [self play];
        }
    }];
}

- (void)seekToTime:(NSTimeInterval)time completionHandler:(void (^ __nullable)(BOOL finished))completionHandler {
    if (self.player.duration > 0) {
        self.player.currentPlaybackTime = time;
        if (completionHandler) completionHandler(YES);
    } else {
        self.seekTime = time;
    }
}

- (UIImage *)thumbnailImageAtCurrentTime {
    return [self.player thumbnailImageAtCurrentTime];
}

#pragma mark - private method

- (void)initializePlayer {
    // IJKFFMoviePlayerController 初始化后，必须手动进行释放，否则会依然存在内存中对资源进行播放。
    if (self.player) {
        [self removeMovieNotificationObservers];
        [self.player shutdown];
        [self.player.view removeFromSuperview];
        self.player = nil;
    }

    self.player = [[IJKFFMoviePlayerController alloc] initWithContentURL:self.assetURL withOptions:self.options];
    self.player.shouldAutoplay = self.shouldAutoPlay;
    [self.player prepareToPlay];
    self.view.playerView = self.player.view;
    self.scalingMode = self->_scalingMode;
    [self addPlayerNotificationObservers];
}

- (void)addPlayerNotificationObservers {
    /// 准备播放
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(loadStateDidChange:)
                                                 name:IJKMPMoviePlayerLoadStateDidChangeNotification
                                               object:_player];
    /// 播放完成或者用户退出
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayBackFinish:)
                                                 name:IJKMPMoviePlayerPlaybackDidFinishNotification
                                               object:_player];
    /// 准备开始播放了
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(mediaIsPreparedToPlayDidChange:)
                                                 name:IJKMPMediaPlaybackIsPreparedToPlayDidChangeNotification
                                               object:_player];
    /// 播放状态改变了
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(moviePlayBackStateDidChange:)
                                                 name:IJKMPMoviePlayerPlaybackStateDidChangeNotification
                                               object:_player];
    
    /// 视频的尺寸变化了
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(sizeAvailableChange:)
                                                 name:IJKMPMovieNaturalSizeAvailableNotification
                                               object:self.player];
}

- (void)removeMovieNotificationObservers {
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:IJKMPMoviePlayerLoadStateDidChangeNotification
                                                  object:_player];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:IJKMPMoviePlayerPlaybackDidFinishNotification
                                                  object:_player];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:IJKMPMediaPlaybackIsPreparedToPlayDidChangeNotification
                                                  object:_player];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:IJKMPMoviePlayerPlaybackStateDidChangeNotification
                                                  object:_player];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:IJKMPMovieNaturalSizeAvailableNotification
                                                  object:_player];
}

- (void)timerUpdate {
    if (self.player.currentPlaybackTime > 0 && !self.isReadyToPlay) {
        self.isReadyToPlay = YES;
        self.loadState = ZFPlayerLoadStatePlaythroughOK;
    }
    self->_currentTime = self.player.currentPlaybackTime > 0 ? self.player.currentPlaybackTime : 0;
    self->_totalTime = self.player.duration;
    self->_bufferTime = self.player.playableDuration;
    if (self.playerPlayTimeChanged) self.playerPlayTimeChanged(self, self.currentTime, self.totalTime);
    if (self.playerBufferTimeChanged) self.playerBufferTimeChanged(self, self.bufferTime);
}

#pragma - notification

/// 播放完成
- (void)moviePlayBackFinish:(NSNotification *)notification {
    int reason = [[[notification userInfo] valueForKey:IJKMPMoviePlayerPlaybackDidFinishReasonUserInfoKey] intValue];
    switch (reason) {
        case IJKMPMovieFinishReasonPlaybackEnded: {
            ZFPlayerLog(@"playbackStateDidChange: 播放完毕: %d\n", reason);
            self.playState = ZFPlayerPlayStatePlayStopped;
            if (self.playerDidToEnd) self.playerDidToEnd(self);
        }
            break;
            
        case IJKMPMovieFinishReasonUserExited: {
            ZFPlayerLog(@"playbackStateDidChange: 用户退出播放: %d\n", reason);
        }
            break;
            
        case IJKMPMovieFinishReasonPlaybackError: {
            ZFPlayerLog(@"playbackStateDidChange: 播放出现错误: %d\n", reason);
            self.playState = ZFPlayerPlayStatePlayFailed;
            if (self.playerPlayFailed) self.playerPlayFailed(self, @(reason));
        }
            break;
            
        default:
            ZFPlayerLog(@"playbackPlayBackDidFinish: ???: %d\n", reason);
            break;
    }
}

// 准备开始播放了
- (void)mediaIsPreparedToPlayDidChange:(NSNotification *)notification {
    ZFPlayerLog(@"加载状态变成了已经缓存完成，如果设置了自动播放, 会自动播放");
    // 视频开始播放的时候开启计时器
    if (!self.timer) {
        self.timer = [NSTimer scheduledTimerWithTimeInterval:self.timeRefreshInterval > 0 ? self.timeRefreshInterval : 0.1 target:self selector:@selector(timerUpdate) userInfo:nil repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:self.timer forMode:NSRunLoopCommonModes];
    }
    
    if (self.isPlaying) {
        [self play];
        self.muted = self.muted;
        if (self.seekTime > 0) {
            [self seekToTime:self.seekTime completionHandler:nil];
            self.seekTime = 0; // 滞空, 防止下次播放出错
            [self play];
        }
    }
    if (self.playerReadyToPlay) self.playerReadyToPlay(self, self.assetURL);
}


#pragma mark - 加载状态改变
/**
 视频加载状态改变了
 IJKMPMovieLoadStateUnknown == 0
 IJKMPMovieLoadStatePlayable == 1
 IJKMPMovieLoadStatePlaythroughOK == 2
 IJKMPMovieLoadStateStalled == 4
 */
- (void)loadStateDidChange:(NSNotification*)notification {
    IJKMPMovieLoadState loadState = self.player.loadState;
    if ((loadState & IJKMPMovieLoadStatePlayable)) {
        ZFPlayerLog(@"加载状态变成了缓存数据足够开始播放，但是视频并没有缓存完全");
        if (self.player.currentPlaybackTime > 0) {
            self.loadState = ZFPlayerLoadStatePlayable;
        }
    } else if ((loadState & IJKMPMovieLoadStatePlaythroughOK)) {
        // 加载完成，即将播放，停止加载的动画，并将其移除
        ZFPlayerLog(@"加载状态变成了已经缓存完成，如果设置了自动播放, 会自动播放");
    } else if ((loadState & IJKMPMovieLoadStateStalled)) {
        // 可能由于网速不好等因素导致了暂停，重新添加加载的动画
        ZFPlayerLog(@"网速不好等因素导致了暂停");
        self.loadState = ZFPlayerLoadStateStalled;
    } else {
        ZFPlayerLog(@"加载状态变成了未知状态");
        self.loadState = ZFPlayerLoadStateUnknown;
    }
}

// 播放状态改变
- (void)moviePlayBackStateDidChange:(NSNotification *)notification {
    switch (self.player.playbackState) {
        case IJKMPMoviePlaybackStateStopped: {
            ZFPlayerLog(@"播放器的播放状态变了，现在是停止状态 %d: stoped", (int)_player.playbackState);
            // 这里的回调也会来多次(一次播放完成, 会回调三次), 所以, 这里不设置
            self.playState = ZFPlayerPlayStatePlayStopped;
        }
            break;
            
        case IJKMPMoviePlaybackStatePlaying: {
            ZFPlayerLog(@"播放器的播放状态变了，现在是播放状态 %d: playing", (int)_player.playbackState);
        }
            break;
            
        case IJKMPMoviePlaybackStatePaused: {
            ZFPlayerLog(@"播放器的播放状态变了，现在是暂停状态 %d: paused", (int)_player.playbackState);
        }
            break;
            
        case IJKMPMoviePlaybackStateInterrupted: {
            ZFPlayerLog(@"播放器的播放状态变了，现在是中断状态 %d: interrupted", (int)_player.playbackState);
        }
            break;
            
        case IJKMPMoviePlaybackStateSeekingForward: {
            ZFPlayerLog(@"播放器的播放状态变了，现在是向前拖动状态:%d forward",(int)self.player.playbackState);
        }
            break;
        case IJKMPMoviePlaybackStateSeekingBackward: {
            ZFPlayerLog(@"放器的播放状态变了，现在是向后拖动状态 %d: backward", (int)_player.playbackState);
        }
            break;
            
        default: {
            ZFPlayerLog(@"播放器的播放状态变了，现在是未知状态 %d: unknown", (int)_player.playbackState);
        }
            break;
    }
}

/// 视频的尺寸变化了
- (void)sizeAvailableChange:(NSNotification *)notify {
    self.presentationSize = self.player.naturalSize;
    self.view.presentationSize = self.presentationSize;
    if (self.presentationSizeChanged) {
        self.presentationSizeChanged(self, self->_presentationSize);
    }
}

#pragma mark - getter

- (ZFPlayerView *)view {
    if (!_view) {
        _view = [[ZFPlayerView alloc] init];
    }
    return _view;
}

- (float)rate {
    return _rate == 0 ?1:_rate;
}

- (IJKFFOptions *)options {
    if (!_options) {
        _options = [IJKFFOptions optionsByDefault];
        /// 精准seek
        [_options setPlayerOptionIntValue:1 forKey:@"enable-accurate-seek"];
        /// 解决http播放不了
        [_options setOptionIntValue:1 forKey:@"dns_cache_clear" ofCategory:kIJKFFOptionCategoryFormat];
    }
    return _options;
}

#pragma mark - setter

- (void)setPlayState:(ZFPlayerPlaybackState)playState {
    _playState = playState;
    if (self.playerPlayStateChanged) self.playerPlayStateChanged(self, playState);
}

- (void)setLoadState:(ZFPlayerLoadState)loadState {
    _loadState = loadState;
    if (self.playerLoadStateChanged) self.playerLoadStateChanged(self, loadState);
}

- (void)setAssetURL:(NSURL *)assetURL {
    if (self.player) [self stop];
    _assetURL = assetURL;
    [self prepareToPlay];
}

- (void)setRate:(float)rate {
    _rate = rate;
    if (self.player && fabsf(_player.playbackRate) > 0.00001f) {
        self.player.playbackRate = rate;
    }
}

- (void)setMuted:(BOOL)muted {
    _muted = muted;
    if (muted) {
        self.lastVolume = self.player.playbackVolume;
        self.player.playbackVolume = 0;
    } else {
        /// Fix first called the lastVolume is 0.
        if (self.lastVolume == 0) self.lastVolume = self.player.playbackVolume;
        self.player.playbackVolume = self.lastVolume;
    }
}

- (void)setScalingMode:(ZFPlayerScalingMode)scalingMode {
    _scalingMode = scalingMode;
    self.view.scalingMode = scalingMode;
    switch (scalingMode) {
        case ZFPlayerScalingModeNone:
            self.player.scalingMode = IJKMPMovieScalingModeNone;
            break;
        case ZFPlayerScalingModeAspectFit:
            self.player.scalingMode = IJKMPMovieScalingModeAspectFit;
            break;
        case ZFPlayerScalingModeAspectFill:
            self.player.scalingMode = IJKMPMovieScalingModeAspectFill;
            break;
        case ZFPlayerScalingModeFill:
            self.player.scalingMode = IJKMPMovieScalingModeFill;
            break;
        default:
            break;
    }
}

- (void)setVolume:(float)volume {
    _volume = MIN(MAX(0, volume), 1);
    self.player.playbackVolume = _volume;
}

@end

#endif
