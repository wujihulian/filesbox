//
//  VULNavigationViewController.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 zcc. All rights reserved.
//
// 默认导航栏控制器

#import <UIKit/UIKit.h>

#define iOS7Later ([UIDevice currentDevice].systemVersion.floatValue >= 7.0f)
#define iOS8Later ([UIDevice currentDevice].systemVersion.floatValue >= 8.0f)

@interface FullscreenPopGestureRecognizer : UIPanGestureRecognizer

@end

@interface VULNavigationViewController : UINavigationController

/// 首选语言，如果设置了就用该语言，不设则取当前系统语言。
/// 由于目前只支持中文、繁体中文、英文。故该属性只支持zh-Hans、zh-Hant、en、三种值，其余值无效。
@property (copy, nonatomic) NSString *preferredLanguage;

/// 语言bundle，preferredLanguage变化时languageBundle会变化
/// 可通过手动设置bundle，让选择器支持新的的语言（需要在设置preferredLanguage后设置languageBundle）。
@property (strong, nonatomic) NSBundle *languageBundle;


#pragma mark - Appearance 外观 文字
@property (nonatomic, strong) UIColor *naviBgColor; /**< 导航栏颜色 */
@property (nonatomic, strong) UIColor *naviTitleColor; /**< 标题颜色 */

@property (nonatomic, copy) NSString *backBtnTitleStr; /**< 返回按钮 */
@property (nonatomic, copy) NSString *doneBtnTitleStr; /**< 完成按钮 */
@property (nonatomic, copy) NSString *quitBtnTitleStr; /**< 取消按钮 */

- (void)enablePopGesture:(BOOL)enable;

@end


@interface UINavigationController (Extension)

/**
 pop时返回到一个新Controller，适用于传值
 @param newController pop时返回到一个新Controller
 @param animated animated
 */
- (void)popToNewController:(UIViewController *)newController
                  animated:(BOOL)animated;

/**
 导航push，返回时返回到一个新控制器，不适用于需要传值的需求
 @param viewController 显示 Controller
 @param backController 返回时返回到一个新控制器
 @param animated animated
 */
- (void)pushViewController:(UIViewController *)viewController
          backToController:(UIViewController *)backController
                  animated:(BOOL)animated;

- (void)pushViewController:(UIViewController *)viewController
     backToControllerIndex:(NSInteger)controllerIndex
                  animated:(BOOL)animated;

@end
