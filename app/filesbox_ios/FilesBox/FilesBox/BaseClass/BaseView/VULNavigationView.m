//
//  VULNavigationView.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/16.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULNavigationView.h"

@implementation VULNavigationView

- (instancetype)init {
    if (self = [super init]) {
        if (!VUL_IS_NOTCH) {
            self.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, 64);
        } else {
            self.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, 88);
        }
        self.backgroundColor = HEXCOLOR(0xF6F6F6);
//        HEXCOLOR(0x2796F0);
        [self addSubview:self.line];
        [self.line mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.bottom.mas_equalTo(self);
            make.height.mas_equalTo(@(1));
        }];
    }
    return self;
}

#pragma mark -
#pragma mark - getter and setter
- (UIView *)line {
    if (!_line) {
        if (!VUL_IS_NOTCH) {
            _line = [[UIView alloc] initWithFrame:CGRectMake(0, 63, self.width, 1)];
        } else {
            _line = [[UIView alloc] initWithFrame:CGRectMake(0, 87, self.width, 1)];
        }
        _line.backgroundColor = [UIColor colorWithHue:0.00 saturation:0.00 brightness:0.90 alpha:1.00];
    }
    return _line;
}
@end
