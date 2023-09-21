//
//  YKRecordTool.m
//  VideoULimit
//
//  Created by yuekewei on 2020/3/17.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "YKRecordTool.h"

@interface YKRecordTool ()

@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, strong) YKVoiceHud *voiceHud;
@end

@implementation YKRecordTool

- (void)startRecordingWithHUDShowView:(UIView *)showView {
    self.recordName = [YKRecordManager creatRecordFileName];
    
    if (!_voiceHud) {
         _voiceHud = [YKVoiceHud new];
           _voiceHud.hidden = YES;
    }
    
    CGFloat width = MIN(FontAuto(150), 150);
    _voiceHud.frame = CGRectMake((CGRectGetWidth(showView.frame) - width) /2.0,
                                 (CGRectGetHeight(showView.frame) - width) /2.0,
                                 width,
                                 width);
    [showView addSubview:_voiceHud];
    
    [[YKRecordManager shareManager] startRecordingWithFileName:self.recordName completion:^(NSError *error) {
        if (error) {
            // 加了录音权限的判断
        } else {
            [self timerInvalue];
            self.voiceHud.hidden = NO;
            [self timer];
        }
    }];
}

- (void)completionRecording:(void(^)(NSString *recordPath)) recordComplete {
    __weak typeof(self) weakSelf = self;
    [[YKRecordManager shareManager] stopRecordingWithCompletion:^(NSString *recordPath) {
        if ([recordPath isEqualToString:shortRecord]) {
            [weakSelf voiceRecordSoShort];
            [[YKRecordManager shareManager] removeCurrentRecordFile:self.recordName];
            
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                // 发送语音
                [weakSelf timerInvalue];
                weakSelf.voiceHud.hidden = YES;
                if (recordPath) {
                    if (recordComplete) {
                        recordComplete(recordPath);
                    }
                }
            });
        }
    }];
}

- (void)voiceRecordSoShort {
    [self timerInvalue];
    self.voiceHud.animationImages = nil;
    self.voiceHud.image = [UIImage imageNamed:@"voiceShort"];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.voiceHud.hidden = YES;
    });
}

- (void)cancelRecording {
    [self timerInvalue];
    self.voiceHud.hidden = YES;
    [[YKRecordManager shareManager] removeCurrentRecordFile:self.recordName];
}

// 向外或向里移动
- (void)voiceWillDragout:(BOOL)inside {
    if (inside) {
        [_timer setFireDate:[NSDate distantPast]];
        _voiceHud.image  = [UIImage imageNamed:@"voice_1"];
    } else {
        [_timer setFireDate:[NSDate distantFuture]];
        self.voiceHud.animationImages  = nil;
        self.voiceHud.image = [UIImage imageNamed:@"cancelVoice"];
    }
}

- (void)progressChange {
    AVAudioRecorder *recorder = [[YKRecordManager shareManager] recorder] ;
    [recorder updateMeters];
    float power= [recorder averagePowerForChannel:0];//取得第一个通道的音频，注意音频强度范围时-160到0,声音越大power绝对值越小
    CGFloat progress = (1.0/160)*(power + 160);
    self.voiceHud.progress = progress;
}

- (void)timerInvalue {
    if (_timer) {
        [_timer invalidate];
        _timer  = nil;
    }
}

#pragma mark - Lazy
- (NSTimer *)timer {
    if (!_timer) {
        _timer =[NSTimer scheduledTimerWithTimeInterval:0.3f target:self selector:@selector(progressChange) userInfo:nil repeats:YES];
    }
    return _timer;
}

@end


@interface YKVoiceHud ()
{
    NSArray *_images;
}

@end

@implementation YKVoiceHud

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.animationDuration    = 0.5;
        self.animationRepeatCount = -1;
        _images = @[
            [UIImage imageNamed:@"voice_1"],
            [UIImage imageNamed:@"voice_2"],
            [UIImage imageNamed:@"voice_3"],
            [UIImage imageNamed:@"voice_4"],
            [UIImage imageNamed:@"voice_5"],
            [UIImage imageNamed:@"voice_6"]
        ];
    }
    return self;
}


- (void)setProgress:(CGFloat)progress {
    _progress         = MIN(MAX(progress, 0.f),1.f);
    [self updateImages];
}

- (void)updateImages {
    if (_progress == 0) {
        self.animationImages = nil;
        [self stopAnimating];
        return;
    }
    if (_progress >= 0.8 ) {
        self.animationImages = @[_images[3],_images[4],_images[5],_images[4],_images[3]];
    } else {
        self.animationImages = @[_images[0],_images[1],_images[2],_images[1]];
    }
    [self startAnimating];
}

@end
