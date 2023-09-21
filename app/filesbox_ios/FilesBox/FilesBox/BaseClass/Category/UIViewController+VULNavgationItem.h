//
//  UIViewController+VULNavgationItem.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/11.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UIViewController (VULNavgationItem)

/**
 *  使用文案增加标题栏按钮
 *
 *  @param title    文案
 *  @param selector 回调方法
 */
- (UIButton *)addNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector;
- (UIButton *)addNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector;
- (UIButton *)addNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color;
- (UIButton *)addNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color;

/**
 *  使用图片增加标题栏按钮
 *
 *  @param image    图片
 *  @param selector 回调方法
 */
- (UIButton *)addNavLeftBtnWithImage:(UIImage *)image selector:(SEL)selector;
- (UIButton *)addNavRightBtnWithImage:(UIImage *)image selector:(SEL)selector;

/**
 *  返回标题按钮，不添加
 *
 *  @param selector 回调方法
 */
- (UIButton *)getNavButtonWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color;
- (UIButton *)getNavButtonWithImage:(UIImage *)image selector:(SEL)selector;

/**
 默认返回按钮
 */
- (UIButton *)defaultNavBackItemIconAndselector:(SEL)selector;


@end
