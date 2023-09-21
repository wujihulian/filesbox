//
//  VULAlertController.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>


NS_ASSUME_NONNULL_BEGIN

#pragma mark -
#pragma mark - I.VULAlertController构造
@class VULAlertController;

/**
 VULAlertController: alertAction配置链
 
 @param title 标题
 @return      VULAlertController对象
 */
typedef VULAlertController * _Nonnull (^VULAlertActionTitle)(NSString *title);

/**
 VULAlertController: alert按钮执行回调
 
 @param buttonIndex 按钮index(根据添加action的顺序)
 @param action      UIAlertAction对象
 @param alertSelf   本类对象
 */
typedef void (^VULAlertActionBlock)(NSInteger buttonIndex, UIAlertAction *action, VULAlertController *alertSelf);


@interface VULAlertController : UIAlertController

/**
 VULAlertController: 禁用alert弹出动画，默认执行系统的默认弹出动画
 */
- (void)alertAnimateDisabled;

/**
 VULAlertController: alert弹出后，可配置的回调
 */
@property (nullable, nonatomic, copy) void (^alertDidShown)(void);

/**
 VULAlertController: alert关闭后，可配置的回调
 */
@property (nullable, nonatomic, copy) void (^alertDidDismiss)(void);

/**
 VULAlertController: 设置toast模式展示时间：如果alert未添加任何按钮，将会以toast样式展示，这里设置展示时间，默认1s
 */
@property (nonatomic, assign) NSTimeInterval toastStyleDuration; //deafult VUL_alertShowDurationDefault = 1s

/**
 VULAlertController: 链式构造alert视图按钮，添加一个alertAction按钮，默认样式，参数为标题
 
 @return VULAlertController对象
 */
- (VULAlertActionTitle)addActionDefaultTitle;

/**
 VULAlertController: 链式构造alert视图按钮，添加一个alertAction按钮，取消样式，参数为标题(warning:一个alert该样式只能添加一次!!!)
 
 @return VULAlertController对象
 */
- (VULAlertActionTitle)addActionCancelTitle;

/**
 VULAlertController: 链式构造alert视图按钮，添加一个alertAction按钮，警告样式，参数为标题
 
 @return VULAlertController对象
 */
- (VULAlertActionTitle)addActionDestructiveTitle;


@end



#pragma mark -
#pragma mark - UIViewController扩展使用VULAlertController
/**
 VULAlertController: alert构造块
 
 @param alertMaker VULAlertController配置对象
 */
typedef void(^VULAlertAppearanceProcess)(VULAlertController *alertMaker);

@interface UIViewController (VULAlertController)

/**
 VULAlertController: show-alert(iOS8)
 
 @param title             title
 @param message           message
 @param appearanceProcess alert配置过程
 @param actionBlock       alert点击响应回调
 */
- (void)vul_showAlertWithTitle:(nullable NSString *)title
                       message:(nullable NSString *)message
             appearanceProcess:(VULAlertAppearanceProcess)appearanceProcess
                  actionsBlock:(nullable VULAlertActionBlock)actionBlock NS_AVAILABLE_IOS(8_0);

/**
 VULAlertController: show-actionSheet(iOS8)
 
 @param title             title
 @param message           message
 @param appearanceProcess actionSheet配置过程
 @param actionBlock       actionSheet点击响应回调
 */
- (void)vul_showActionSheetWithTitle:(nullable NSString *)title
                             message:(nullable NSString *)message
                   appearanceProcess:(VULAlertAppearanceProcess)appearanceProcess
                        actionsBlock:(nullable VULAlertActionBlock)actionBlock NS_AVAILABLE_IOS(8_0);

@end

NS_ASSUME_NONNULL_END


