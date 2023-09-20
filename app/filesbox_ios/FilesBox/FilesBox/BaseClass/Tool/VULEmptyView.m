//
//  VULEmptyView.m
//  Tairong-Test
//
//  Created by yuekewei on 2019/8/7.
//

#import "VULEmptyView.h"
#import "AFNetworkReachabilityManager.h"
#import "MJRefresh.h"

@interface VULEmptyView ()


@property (nonatomic, copy) void(^reloadBlock)(void);
@end

@implementation VULEmptyView

- (instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];//UIColorHex(#F6F6F6);
        [self addSubview:self.imageView];
        [self addSubview:self.infoLabel];
        [self addSubview:self.reloadButton];
        
        
        [self.imageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerX.mas_equalTo(self.mas_centerX);
            make.centerY.mas_equalTo(self.mas_centerY);
        }];
        
        [self.infoLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.imageView.mas_bottom).offset(10);
            make.left.right.mas_equalTo(0);
        }];
        
        [self.reloadButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.infoLabel.mas_bottom).offset(15);
            make.centerX.mas_equalTo(self.mas_centerX);
            make.size.mas_equalTo(CGSizeMake(FontAuto(150), FontAuto(35)));
        }];
    }
    return self;
}

- (void)configEmptyViewWithHasData:(BOOL)hasData
                          hasError:(BOOL)hasError
                            insets:(UIEdgeInsets)insets
                             image:(nullable UIImage *)image
                              info:(nullable NSString *)info
                       reloadBlock:(nullable void (^)(void))reloadBlock {
    
    _reloadBlock = reloadBlock;
    self.hidden = hasData && !hasError;
    
    if ([self.superview isKindOfClass:[UIScrollView class]]) {
        UIScrollView *scroller = (UIScrollView *)self.superview;
        if (scroller.mj_footer) {
            scroller.mj_footer.hidden = !self.hidden;
        }
    }
    
    if (!self.hidden) {
        NSString *emptyInfo = info;
        UIImage *emptyImage = image;
        
        if (!emptyImage) {
            emptyImage = [UIImage imageNamed:@"no_data"];
        }
        if (hasError) {
            emptyInfo = @"网络异常";
            if (![AFNetworkReachabilityManager sharedManager].reachable) {
                emptyInfo = @"当前网络不可用，请检查网络设置";
            }
            emptyImage = [UIImage imageForIconBundle:@"image_empty_error"];
        }
        
        self.imageView.image = emptyImage;
        self.infoLabel.text = emptyInfo;
        
        [self.imageView mas_updateConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(emptyImage.size);
        }];
        
        [self mas_updateConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(insets.top);
            make.left.mas_equalTo(insets.left);
            make.width.mas_equalTo(self.superview.mas_width).offset(- (insets.left + insets.right));
            make.height.mas_equalTo(self.superview.mas_height).offset(- (insets.top + insets.bottom));
        }];
        
        [self.superview bringSubviewToFront:self];
    }
}

- (void)buttonClick {
    if (_reloadBlock) {
        _reloadBlock();
    }
}

#pragma mark - Lazy
- (UIImageView *)imageView {
    if (!_imageView) {
        _imageView = [UIImageView new];
    }
    return _imageView;
}

- (UILabel *)infoLabel {
    if (!_infoLabel) {
        _infoLabel = [UILabel new];
        _infoLabel.font = [UIFont systemFontOfSize:FontAuto(14)];
        _infoLabel.textColor = UIColorHex(#4A4A4A);
        _infoLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _infoLabel;
}

- (UIButton *)reloadButton {
    if (!_reloadButton) {
        _reloadButton = [UIButton buttonWithType:UIButtonTypeCustom];
        _reloadButton.titleLabel.font = [UIFont systemFontOfSize:FontAuto(14)];
        [_reloadButton setTitle:@"重新加载" forState:UIControlStateNormal];
        [_reloadButton setBackgroundImage:[UIImage imageWithColor:[UIColor colorWithHexString:@"#108EE9"]] forState:UIControlStateNormal];
        [_reloadButton setBackgroundImage:[UIImage imageWithColor:[[UIColor colorWithHexString:@"#108EE9"] colorWithAlphaComponent:0.5]] forState:UIControlStateHighlighted];
        _reloadButton.layer.cornerRadius = 4;
        _reloadButton.layer.masksToBounds = YES;
        [_reloadButton addTarget:self action:@selector(buttonClick) forControlEvents:UIControlEventTouchUpInside];
        _reloadButton.hidden = YES;
    }
    return _reloadButton;
}
@end


@implementation UIView (Empty)

- (void)setEmptyView:(nullable VULEmptyView *)emptyView {
    objc_setAssociatedObject(self, @selector(emptyView), emptyView, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (VULEmptyView *)emptyView {
    VULEmptyView *emptyView = objc_getAssociatedObject(self, _cmd);
    if (!emptyView) {
        emptyView = [[VULEmptyView alloc] init];
        emptyView.hidden = YES;
        [self setEmptyView:emptyView];
        [self addSubview:emptyView];
    }
    return emptyView;
}

- (void)configEmptyViewClass:(Class)emptyViewClass {
    VULEmptyView *emptyView = objc_getAssociatedObject(self, _cmd);
    if (emptyView) {
        [emptyView removeFromSuperview];
        [self setEmptyView:nil];
    }
    emptyView = [[emptyViewClass alloc] init];
    emptyView.hidden = YES;
    [self setEmptyView:emptyView];
    [self addSubview:emptyView];
}

- (void)configEmptyViewWithHasData:(BOOL)hasData
                          hasError:(BOOL)hasError
                            insets:(UIEdgeInsets)insets
                             image:(nullable UIImage *)image
                              info:(nullable NSString *)info
                       reloadBlock:(nullable void (^)(void))reloadBlock {
    [self.emptyView configEmptyViewWithHasData:hasData
                                      hasError:hasError
                                        insets:insets
                                         image:image
                                          info:info
                                   reloadBlock:reloadBlock];
}


@end
