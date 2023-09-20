//
//  VULBaseScrollView.m
//  VideoULimit
//
//  Created by svnlan on 2018/12/29.
//  Copyright © 2018 svnlan. All rights reserved.
//

#import "VULBaseScrollView.h"

@implementation VULBaseScrollView


- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.scrollView = [[UIScrollView alloc] initWithFrame:frame];
        self.contentView = [[UIView alloc] initWithFrame:frame];
        self.contentView.backgroundColor = [UIColor orangeColor];
        [self.scrollView addSubview:self.contentView];
        [self addSubview:self.scrollView];
    }
    return self;
}

@end
