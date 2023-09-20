//
//  WaveView.m
//  MemoryProgress
//
//  Created by xiongan on 2017/11/15.
//  Copyright © 2017年 xiongan. All rights reserved.
//

#define LXDefaultFirstWaveColor [UIColor colorWithRed:34/255.0 green:116/255.0 blue:210/255.0 alpha:1]
#define LXDefaultSecondWaveColor [UIColor colorWithRed:34/255.0 green:116/255.0 blue:210/255.0 alpha:0.3]

#import "CHDWaveView.h"
@protocol WaveViewWeakDelegate <NSObject>

- (void)waveAnimationStart;
@end
@interface WeakTarget : NSObject<WaveViewWeakDelegate>
@property (weak,nonatomic)id <WaveViewWeakDelegate> delegate;
@end

@implementation WeakTarget
- (void)waveAnimationStart {
    [self.delegate waveAnimationStart];
}
@end


@interface CHDWaveView ()<WaveViewWeakDelegate>
@property (nonatomic,assign)CGFloat yHeight;
@property (nonatomic,assign)CGFloat offset;
@property (nonatomic,assign)BOOL start;
@property (nonatomic,strong)CADisplayLink *timer;

@end
@implementation CHDWaveView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.bounds = CGRectMake(0, 0, MIN(frame.size.width, frame.size.height), MIN(frame.size.width, frame.size.height));
        self.layer.cornerRadius = MIN(frame.size.width, frame.size.height) * 0.5;
        self.layer.masksToBounds = YES;
        self.layer.borderColor = [UIColor colorWithRed:244/255.0 green:244/255.0 blue:248/255.0 alpha:1].CGColor;
        self.layer.borderWidth = 1.0f;
        
        self.waveHeight = 5.0;
        self.firstWaveColor = LXDefaultFirstWaveColor;
        self.secondWaveColor = LXDefaultSecondWaveColor;
        self.yHeight = self.bounds.size.height;
        self.speed=1.0;
        
        [self.layer addSublayer:self.firstWaveLayer];
        if (!self.isShowSingleWave) {
            [self.layer addSublayer:self.secondWaveLayer];
        }
        [self addSubview:self.progressLabel];
        [self addSubview:self.fpsLabel];
    }
    return self;
}
-(void)setProgress:(CGFloat)progress {    
//    progress = 0.3;
    _progress = progress;
    if (progress>=1) {
        _progressLabel.text = @"100%";
    }else{
        _progressLabel.text = [NSString stringWithFormat:@"%.1f%%",[[NSNumber numberWithFloat:progress * 100] floatValue]];

    }
//    _progressLabel.textColor=[UIColor colorWithWhite:progress*1.8 alpha:1];
    self.yHeight = self.bounds.size.height * (1 - progress);
    CGFloat top = progress * self.bounds.size.height;
//    self.firstWaveLayer.frame = CGRectMake(0,self.yHeight, self.firstWaveLayer.frame.size.width, self.firstWaveLayer.frame.size.width);
//    self.secondWaveLayer.frame = CGRectMake(0,self.yHeight, self.secondWaveLayer.frame.size.width, self.secondWaveLayer.frame.size.width);
//    [self stopWaveAnimation];
//    [self startWaveAnimation];
    
    [self.firstWaveLayer setValue:@(30-top) forKeyPath:@"position.y"];
    [self.secondWaveLayer setValue:@(30-top) forKeyPath:@"position.y"];
//    [self.secondWaveLayer setValue:@(100-top) forKey:@"position.y"];
//    self.secondWaveLayer.position = CGPointMake(0, 100-top);
//    self.secondWaveLayer.position = CGPointMake(150, 150 - top);
    if (!self.start) {
        [self startWaveAnimation];
    }

}
#pragma mark -- 开始波动动画
- (void)startWaveAnimation {
//    WeakTarget *target = [WeakTarget new];
//    target.delegate = self;
//    [self waveAnimationStart ];
    self.start = YES;
    [self waveAnimationForCoreAnimation];
//    self.timer = [CADisplayLink displayLinkWithTarget:target selector:@selector(waveAnimationStart)];
//    [self.timer addToRunLoop:[NSRunLoop currentRunLoop] forMode:NSRunLoopCommonModes];
    
}


#pragma mark -- 停止波动动画
- (void)stopWaveAnimation {
    [self.timer invalidate];
    self.timer = nil;
}
- (void)waveAnimationForCoreAnimation {
    UIBezierPath *bezierFristWave = [UIBezierPath bezierPath];
    CGFloat waveHeight = self.waveHeight ;
    CGMutablePathRef pathRef = CGPathCreateMutable();
    CGFloat startOffY = waveHeight * sinf(self.offset * M_PI * 2 / self.bounds.size.width);
    CGFloat orignOffY = 0.0;
    CGPathMoveToPoint(pathRef, NULL, 0, startOffY);
    
    [bezierFristWave moveToPoint:CGPointMake(0, startOffY)];
    for (CGFloat i = 0.f; i <= self.bounds.size.width * 400; i++) {
        orignOffY = waveHeight * sinf(2 * M_PI / self.bounds.size.width * i + self.offset * M_PI * 2 / self.bounds.size.width) ;
        [bezierFristWave addLineToPoint:CGPointMake(i, orignOffY)];
    }
    [bezierFristWave addLineToPoint:CGPointMake(self.bounds.size.width * 400, orignOffY)];
    [bezierFristWave addLineToPoint:CGPointMake(self.bounds.size.width * 400, self.bounds.size.height)];
    [bezierFristWave addLineToPoint:CGPointMake(0, self.bounds.size.height)];
    [bezierFristWave addLineToPoint:CGPointMake(0, startOffY)];
    [bezierFristWave closePath];
    // 说明这个动画对象要对CALayer的position属性执行动画
    self.anim = [CABasicAnimation animationWithKeyPath:@"transform.translation.x"];
    self.anim .duration = 2;
    self.anim .fromValue = @(- self.frame.size.width * 0.5);
    self.anim .toValue =@(-self.frame.size.width  - self.frame.size.width * 0.5);
    self.anim .repeatCount = MAXFLOAT;
    self.anim .fillMode = kCAFillModeForwards;
    self.anim .removedOnCompletion = NO;
    
    self.firstWaveLayer.fillColor = self.firstWaveColor.CGColor;
    self.firstWaveLayer.path = bezierFristWave.CGPath;
     // 添加动画对象到图层上
     [self.firstWaveLayer addAnimation:    self.anim  forKey:@"translate"];
    
    //第二个波纹
    if (!self.isShowSingleWave) {
        UIBezierPath *bezierSecondeWave = [UIBezierPath bezierPath];
        CGFloat startOffY1 = waveHeight * sinf(self.offset * M_PI * 2 / self.bounds.size.width);
        CGFloat orignOffY1 = 0.0;
        [bezierSecondeWave moveToPoint:CGPointMake( 0, startOffY1)];
        for (CGFloat i = 0.f; i <= self.bounds.size.width * 400; i++) {
            orignOffY1 = waveHeight * cosf(2 * M_PI / self.bounds.size.width * i + self.offset * M_PI * 2 / self.bounds.size.width);
            [bezierSecondeWave addLineToPoint:CGPointMake( i, orignOffY1)];
        }
        
        [bezierSecondeWave addLineToPoint:CGPointMake( self.bounds.size.width*400, orignOffY1)];
        [bezierSecondeWave addLineToPoint:CGPointMake( self.bounds.size.width*400, self.bounds.size.height)];
        [bezierSecondeWave addLineToPoint:CGPointMake( 0, self.bounds.size.height)];
        [bezierSecondeWave addLineToPoint:CGPointMake( 0, startOffY1)];
        [bezierSecondeWave closePath];
        self.secondWaveLayer.path = bezierSecondeWave.CGPath;
        self.secondWaveLayer.fillColor = self.secondWaveColor.CGColor;
        [self.secondWaveLayer addAnimation:    self.anim  forKey:@"translate"];
    }
}
#pragma mark -- 波动动画实现
- (void)waveAnimationStart
{
    CGFloat waveHeight = self.waveHeight;
    if (self.progress == 0.0f || self.progress == 1.0f) {
        waveHeight = 0.f;
    }
    
    self.offset += self.speed;
    //第一个波纹
    CGMutablePathRef pathRef = CGPathCreateMutable();
    CGFloat startOffY = waveHeight * sinf(self.offset * M_PI * 2 / self.bounds.size.width);
    CGFloat orignOffY = 0.0;
    CGPathMoveToPoint(pathRef, NULL, 0, startOffY);
    for (CGFloat i = 0.f; i <= self.bounds.size.width; i++) {
        orignOffY = waveHeight * sinf(2 * M_PI / self.bounds.size.width * i + self.offset * M_PI * 2 / self.bounds.size.width) + self.yHeight;
        CGPathAddLineToPoint(pathRef, NULL, i, orignOffY);
    }
    
    CGPathAddLineToPoint(pathRef, NULL, self.bounds.size.width, orignOffY);
    CGPathAddLineToPoint(pathRef, NULL, self.bounds.size.width, self.bounds.size.height);
    CGPathAddLineToPoint(pathRef, NULL, 0, self.bounds.size.height);
    CGPathAddLineToPoint(pathRef, NULL, 0, startOffY);
    CGPathCloseSubpath(pathRef);
    self.firstWaveLayer.path = pathRef;
    self.firstWaveLayer.fillColor = self.firstWaveColor.CGColor;
    CGPathRelease(pathRef);
    
    //第二个波纹
    if (!self.isShowSingleWave) {
        CGMutablePathRef pathRef1 = CGPathCreateMutable();
        CGFloat startOffY1 = waveHeight * sinf(self.offset * M_PI * 2 / self.bounds.size.width);
        CGFloat orignOffY1 = 0.0;
        CGPathMoveToPoint(pathRef1, NULL, 0, startOffY1);
        for (CGFloat i = 0.f; i <= self.bounds.size.width; i++) {
            orignOffY1 = waveHeight * cosf(2 * M_PI / self.bounds.size.width * i + self.offset * M_PI * 2 / self.bounds.size.width) + self.yHeight;
            CGPathAddLineToPoint(pathRef1, NULL, i, orignOffY1);
        }
        
        CGPathAddLineToPoint(pathRef1, NULL, self.bounds.size.width, orignOffY1);
        CGPathAddLineToPoint(pathRef1, NULL, self.bounds.size.width, self.bounds.size.height);
        CGPathAddLineToPoint(pathRef1, NULL, 0, self.bounds.size.height);
        CGPathAddLineToPoint(pathRef1, NULL, 0, startOffY1);
        CGPathCloseSubpath(pathRef1);
        self.secondWaveLayer.path = pathRef1;
        self.secondWaveLayer.fillColor = self.secondWaveColor.CGColor;
        
        CGPathRelease(pathRef1);
    }
}

#pragma mark ----- INITUI ----
-(CAShapeLayer *)firstWaveLayer {
    if (!_firstWaveLayer) {
        _firstWaveLayer = [CAShapeLayer layer];
        _firstWaveLayer.frame = self.bounds;
        _firstWaveLayer.anchorPoint = CGPointZero;
        _firstWaveLayer.fillColor = _firstWaveColor.CGColor;
    }
    return _firstWaveLayer;
}

-(CAShapeLayer *)secondWaveLayer{
    if (!_secondWaveLayer) {
        _secondWaveLayer = [CAShapeLayer layer];
        _secondWaveLayer.frame = self.bounds;
        _secondWaveLayer.anchorPoint = CGPointZero;
        _secondWaveLayer.fillColor = _secondWaveColor.CGColor;
    }
    return _secondWaveLayer;
}

-(UILabel *)fpsLabel {
    if (!_fpsLabel) {
        _fpsLabel=[[UILabel alloc] init];
        _fpsLabel.text=@"";
        _fpsLabel.frame=CGRectMake(0, 0, self.bounds.size.width, 30);
        _fpsLabel.center = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMidY(self.bounds)-20);
        _fpsLabel.font=[UIFont systemFontOfSize:14];
        _fpsLabel.textColor=[UIColor colorWithWhite:0 alpha:1];
        _fpsLabel.textAlignment=1;
    }
    return _fpsLabel;
}
-(UILabel *)progressLabel {
    if (!_progressLabel) {
        _progressLabel=[[UILabel alloc] init];
        _progressLabel.text=@"";
        _progressLabel.frame=CGRectMake(0, 5, self.bounds.size.width, 20);
//        _progressLabel.center = CGPointMake(CGRectGetMidX(self.bounds), CGRectGetMidY(self.bounds)+10);
        _progressLabel.font=[UIFont yk_pingFangSemibold:9];
        _progressLabel.textColor=HEXCOLOR(0xffffff);
        _progressLabel.textAlignment=NSTextAlignmentCenter;
    }
    return _progressLabel;
}



-(void)dealloc {
    
    [self.timer invalidate];
    self.timer = nil;
    
    if (_firstWaveLayer) {
        [_firstWaveLayer removeFromSuperlayer];
        _firstWaveLayer = nil;
    }
    
    if (_secondWaveLayer) {
        [_secondWaveLayer removeFromSuperlayer];
        _secondWaveLayer = nil;
    }
}


@end
