//
//  VULDocumentTabbarView.m
//  VideoULimit
//
//  Created by mac on 2020/7/22.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULDocumentTabbarView.h"
#import "UIButton+EnLargeEdge.h"

@interface VULDocumentTabbarView ()
@property (nonatomic, strong) UIButton * rotateBtn;

@end

@implementation VULDocumentTabbarView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(BOOL)pointInside:(CGPoint)point withEvent:(UIEvent *)event {
    for (UIView *view in self.subviews) {
        if (!view.hidden && view.userInteractionEnabled && [view pointInside:[self convertPoint:point toView:view] withEvent:event])
            return YES;
    }
    return NO;
}

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = VULRGBAColor(0, 0, 0, 0.5);
        self.userInteractionEnabled = YES;
        [self addViews];
    }
    return self;
}

- (void)addViews {
    [self addSubview:self.expandBtn];
    [self addSubview:self.rotateBtn];
    [self addSubview:self.pageLabel];
    [self addSubview:self.leftBtn];
    [self addSubview:self.rightBtn];
    
    self.pageLabel.text = @"0/0";
    
    [self.expandBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(@(2*kSpace));
        make.centerY.equalTo(self);
    }];
    
    [self.rotateBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(@(-2*kSpace));
        make.centerY.equalTo(self);
    }];
    
    [self.pageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self);
        make.centerY.equalTo(self);
    }];
    
    [self.leftBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.pageLabel.mas_left).offset(-15);
        make.centerY.equalTo(self);
    }];
    
    [self.rightBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.pageLabel.mas_right).offset(15);
        make.centerY.equalTo(self);
    }];
}

#pragma mark GET

- (VULButton *)expandBtn {
    if (!_expandBtn) {
        _expandBtn = [VULButton createImgBtnWithFrame:CGRectZero imgNamed:@"向上" Target:self Sel:@selector(expandBtnClicked)];
        [_expandBtn setEnlargeEdge:10];
    }
    return _expandBtn;
}

- (UIButton *)rotateBtn {
    if (!_rotateBtn) {
        _rotateBtn = [UIButton buttonWithType:(UIButtonTypeCustom)];
        [_rotateBtn setImage:VULGetImage(@"全屏-1") forState:(UIControlStateNormal)];
        [_rotateBtn setImage:VULGetImage(@"收起") forState:(UIControlStateSelected)];
        [_rotateBtn addTarget:self action:@selector(rotateBtnClicked:) forControlEvents:(UIControlEventTouchUpInside)];
        [_rotateBtn setEnlargeEdge:10];
    }
    return _rotateBtn;
}

- (UILabel *)pageLabel {
    if (!_pageLabel) {
        _pageLabel = [[UILabel alloc] init];
        _pageLabel.font = [UIFont boldSystemFontOfSize:12];
        _pageLabel.textColor = [UIColor whiteColor];
    }
    return _pageLabel;
}

- (VULButton *)leftBtn {
    if (!_leftBtn) {
        _leftBtn = [VULButton createImgBtnWithFrame:CGRectZero imgNamed:@"上一页" Target:self Sel:@selector(lastPageClick:)];
        [_leftBtn setEnlargeEdge:10];
    }
    return _leftBtn;
}

- (VULButton *)rightBtn {
    if (!_rightBtn) {
        _rightBtn = [VULButton createImgBtnWithFrame:CGRectZero imgNamed:@"下一页" Target:self Sel:@selector(nextPageClick:)];
        [_rightBtn setEnlargeEdge:10];
    }
    return _rightBtn;
}

- (void)expandBtnClicked {
    if ([self.delegate respondsToSelector:@selector(documentExpandClick)]) {
        [self.delegate documentExpandClick];
    }
}

- (void)rotateBtnClicked:(UIButton *)sender {
    if ([self.delegate respondsToSelector:@selector(documentRotateClick:)]) {
        [self.delegate documentRotateClick:sender];
    }
}

- (void)lastPageClick:(UIButton *) sender {
    if ([self.delegate respondsToSelector:@selector(documentLastPageClick:)]) {
        [self.delegate documentLastPageClick:sender];
    }
}

- (void)nextPageClick:(UIButton *) sender {
    if ([self.delegate respondsToSelector:@selector(documentNextPageClick:)]) {
        [self.delegate documentNextPageClick:sender];
    }
}

@end
