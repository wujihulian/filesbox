//
//  UBWaterWaveButton.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/1/9.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "UBWaterWaveButton.h"

@interface UBButtonCircleSet : NSObject

@property CGFloat circleCenterX;
@property CGFloat circleCenterY;
@property CGFloat circleWidth;
@property CGFloat circleRait;

@end

@implementation UBButtonCircleSet

@end

@interface UBWaterWaveButton ()

@property (nonatomic, assign) int loopCount;

@property (nonatomic, strong) NSMutableDictionary *circles;

@property (nonatomic, assign) int circleFlag;

@end

@implementation UBWaterWaveButton

- (void)setAnimationDuration:(NSTimeInterval)AnimationDuration {
    _AnimationDuration = AnimationDuration;
    self.loopCount = self.AnimationDuration / 0.02;
}

- (void)drawRect:(CGRect)rect {
    CGContextRef context = UIGraphicsGetCurrentContext();

    CGFloat endAngle = M_PI * 2;

    for (UBButtonCircleSet *circleSet in self.circles.allValues) {
        CGContextAddArc(context,
                        circleSet.circleCenterX,
                        circleSet.circleCenterY,
                        circleSet.circleWidth * circleSet.circleRait,
                        0,
                        endAngle,
                        NO);
        [[self.highlightedColor colorWithAlphaComponent:(1 - circleSet.circleRait)] setStroke];
        [[self.highlightedColor colorWithAlphaComponent:(1 - circleSet.circleRait)] setFill];

        CGContextFillPath(context);
    }
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];

    self.highlightedColor = [UIColor clearColor];

    self.AnimationDuration = 1;
    self.loopCount = self.AnimationDuration / 0.02;

    self.circles = [NSMutableDictionary dictionary];
    self.circleFlag = 0;

    [self addTarget:self action:@selector(touchedDown:event:) forControlEvents:UIControlEventTouchDown];

    return self;
}

- (void)touchedDown:(UIButton *)btn event:(UIEvent *)event {
    UITouch *touch = event.allTouches.allObjects.firstObject;
    CGPoint touchePoint = [touch locationInView:btn];

    NSString *key = [NSString stringWithFormat:@"%d", self.circleFlag];
    UBButtonCircleSet *set = [UBButtonCircleSet new];
    set.circleCenterX = touchePoint.x;
    set.circleCenterY = touchePoint.y;
    set.circleRait = 0;

    CGFloat maxX = touchePoint.x > (self.frame.size.width - touchePoint.x) ? touchePoint.x : (self.frame.size.width - touchePoint.x);
    CGFloat maxY = touchePoint.y > (self.frame.size.width - touchePoint.y) ? touchePoint.y : (self.frame.size.height - touchePoint.y);
    set.circleWidth = maxX > maxY ? maxX : maxY;

    [self.circles setObject:set forKey:key];

    NSTimer *timer = [NSTimer timerWithTimeInterval:0.01
                                             target:self
                                           selector:@selector(TimerFunction:)
                                           userInfo:@{ @"key": key }
                                            repeats:YES];

    [NSRunLoop.mainRunLoop addTimer:timer forMode:NSRunLoopCommonModes];

    self.circleFlag++;
}

- (void)TimerFunction:(NSTimer *)timer {
    [self setNeedsDisplay];

    NSDictionary *userInfo = timer.userInfo;

    NSString *key = userInfo[@"key"];

    UBButtonCircleSet *set = self.circles[key];

    if (set.circleRait <= 1) {
        set.circleRait += (1.0 / self.loopCount);
    } else {
        [self.circles removeObjectForKey:key];

        [timer invalidate];
    }
}

@end
