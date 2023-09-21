//
//  UIViewController+VULNavgationItem.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/11.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "UIViewController+VULNavgationItem.h"

@implementation UIViewController (VULNavgationItem)
- (UIButton *)getNavButtonWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color {
    UIButton *navBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    navBtn.frame = CGRectMake(0, 0, 70, 30);
    [navBtn addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
    [navBtn.titleLabel setFont:[UIFont systemFontOfSize:16]];//15]];
    [navBtn setTitle:title forState:UIControlStateNormal];
    [navBtn setTitleColor:color forState:UIControlStateNormal];
    [navBtn setTitleColor:[color colorWithAlphaComponent:0.5] forState:UIControlStateDisabled];
    
    return navBtn;
}

- (UIButton *)getNavButtonWithImage:(UIImage *)image selector:(SEL)selector {
    UIButton *navBtn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 44, 44)];
    [navBtn setImage:image forState:UIControlStateNormal];
    [navBtn setImage:image forState:UIControlStateHighlighted];
    [navBtn addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
    
    return navBtn;
}

#pragma mark -
#pragma mark - navButton
- (UIButton *)addNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector {
    return [self addNavLeftBtnWithTitle:title selector:selector color:self.navigationController.navigationBar.tintColor];
}

- (UIButton *)addNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color {
    UIButton *leftBtn = [self getNavButtonWithTitle:title selector:selector color:color];
    return leftBtn;
}

- (UIButton *)addNavLeftBtnWithImage:(UIImage *)image selector:(SEL)selector {
    UIButton *leftBtn = [self getNavButtonWithImage:image selector:selector];
    return leftBtn;
}

- (UIButton *)addNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector {
    return [self addNavRightBtnWithTitle:title selector:selector color:self.navigationController.navigationBar.tintColor];
}

- (UIButton *)addNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color {
    UIButton *rightBtn = [self getNavButtonWithTitle:title selector:selector color:color];
    return rightBtn;
}

- (UIButton *)addNavRightBtnWithImage:(UIImage *)image selector:(SEL)selector {
    UIButton *rightBtn = [self getNavButtonWithImage:image selector:selector];
    rightBtn.imageEdgeInsets = UIEdgeInsetsMake(0, 25, 0, 0);
    return rightBtn;
}

- (UIButton *)defaultNavBackItemIconAndselector:(SEL)selector{
    return [self getNavButtonWithImage:[UIImage imageNamed:@"player_back"] selector:selector];
}

@end
