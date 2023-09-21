//
//  VULWaitingAnimationView.m
//  VideoULimit
//
//  Created by svnlan on 2019/11/13.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "VULWaitingAnimationView.h"

@implementation VULWaitingAnimationView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        [self setSubView];
    }
    return self;
}

- (void)setSubView {
//    self.loadWaitView = [[VULGIFImageView alloc] initWithFrame:CGRectMake((VULSCREEN_WIDTH - 60) / 2, (VULSCREEN_HEIGHT - 60  - K_TabBar_Height) / 2, 60, 60)];
    self.loadWaitView = [[VULGIFImageView alloc] initWithFrame:CGRectMake(0, 0, 60, 60)];
    self.loadWaitView.image = [VULGIFImage imageNamed:@"ic_loading.gif"];
    [self addSubview:self.loadWaitView];
}

- (void)endWaitingView {
    [self removeFromSuperview];
}

@end
