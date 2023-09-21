//
//  VULBaseViewController.h
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UIView+EXTENSION.h"
#import "VULCommonTool.h"
#import "VULResponseStatusCode.h"
#import "VULWaitingAnimationView.h"

#define iOS7Later ([UIDevice currentDevice].systemVersion.floatValue >= 7.0f)
#define iOS8Later ([UIDevice currentDevice].systemVersion.floatValue >= 8.0f)


// 网络状态
typedef NS_ENUM(NSInteger, VULNetworkReachabilityStatus) {
    VULNetworkReachabilityStatusUnknown          = -1,
    VULNetworkReachabilityStatusNotReachable     = 0,
    VULNetworkReachabilityStatusReachableViaWWAN = 1,
    VULNetworkReachabilityStatusReachableViaWiFi = 2,
};

@interface VULBaseViewController : UIViewController<VULResponseStatusCodeDelegate>

@property (nonatomic, strong)VULResponseStatusCode *responseStatusCode;/**< 处理状态码 接受数据代理 */
@property (nonatomic, strong) VULNavigationView *navigationView;
@property (nonatomic, strong) UIImageView *navigationViewBackgroundImageView;/**< 自定义导航视图背景图片 */
@property (nonatomic, strong) UILabel *navigationTitleLabel;/**< 自定义导航视图标题视图 */
@property (nonatomic, strong) UIView *navigationViewLeftButton;/**< 自定义导航视图左按钮视图 */
@property (nonatomic, strong) UIView *navigationViewRightButton;/**< 自定义导航视图右按钮视图 */
@property (nonatomic, strong) UIButton *leftButton;/**< 自定义导航视图左按钮 */
@property (nonatomic, copy) NSString *navigationViewBackgroundImage;/**< 自定义导航视图背景图片 */
@property (nonatomic, copy) NSString *navigationTitle;/**< 自定义导航视图标题 */
@property (nonatomic, copy) NSString *navigationTitleFont;/**< 自定义导航视图标题字体 */
@property (nonatomic, strong) UIColor *navigationViewBackgroundColor;/**< 自定义导航视图背景颜色 */
@property (nonatomic, strong) UIColor *navigationTitleColor;/**< 自定义导航视图标题颜色 */
@property (nonatomic, assign) CGFloat navigationTitleFontSize;/**< 自定义导航视图标题字体大小 */
@property (nonatomic, assign) CGFloat navigationViewAlpha;/**< 自定义导航视图透明度 */
@property (nonatomic, assign, getter=isNavigationViewHide) BOOL navigationViewHide;/**< 是否隐藏自定义导航视图 */
@property (nonatomic, strong) VULResponseStatusCode *processingStatusCode;/**< 处理状态码 */
@property (nonatomic, assign) VULNetworkReachabilityStatus networkReachabilityStatus;/**< 网络状态 */

@property (nonatomic, assign) NSInteger navigationBarHeight;/**< 状态栏高度 */
@property (nonatomic, assign) NSInteger navigationViewHeight;/**< 导航栏高度 */
@property (nonatomic, assign) NSInteger navigationHeight;/**< 状态栏高度+导航栏高度 */
@property (nonatomic, assign) NSInteger tabBarHeight;/**< 底部栏高度 */
@property (nonatomic, assign) NSInteger studentHomeHeaderHeight;/**< 学生首页头部高度 */
@property (nonatomic, assign) NSInteger navigationViewDefaultTitleSize;/**< 导航栏标题字体默认大小 */
@property (strong, nonatomic) UIView *retryView; //重新加载、刷新view
@property (assign, nonatomic) CGFloat popToastCenter; //吐丝中心点

@property (nonatomic, strong) VULWaitingAnimationView *waitView;/**< 加载动画效果 */
@property (nonatomic, strong) UIView *statusBarView;


@property (nonatomic, strong) UIButton *rightButton;/**< 自定义导航视图右按钮 */

@property (nonatomic, copy) void (^saveAndRefreshBlock)(void);

/**
设置导航栏的背景颜色
*/
- (void)setStatusBarBackgroundColor:(UIColor *)color;

/**
 自定义导航栏左边标题按钮隐藏
 */
-(void)baseSetLeftBtnhide;

/**
 自定义导航栏左边标题按钮显示
 */
- (void)baseSetLeftBtnShow;

/**
 自定义导航栏左边标题按钮
 
 @param title 按钮标题
 @param selector 自定义方法
 */
- (void)baseAddNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector;

/**
 自定义导航栏右边标题按钮
 
 @param title 按钮标题
 @param selector 自定义方法
 */
- (void)baseAddNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector;

/**
 自定义导航栏左边标题按钮+按钮颜色
 
 @param title 按钮标题
 @param selector 自定义方法
 @param color 按钮标题颜色
 */
- (void)baseAddNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color;

/**
 自定义导航栏右边标题按钮+按钮颜色
 
 @param title 按钮标题
 @param selector 自定义方法
 @param color 按钮标题颜色
 */
- (void)baseAddNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color;

/**
 设置右边导航按钮a颜色
 
 @param color 字体颜色
 */
- (void)setNavRightBtnWithColor:(UIColor *)color WithState:(UIControlState)state;

- (void)setNavRightBtnEnable:(BOOL)enable;

/**
 自定义导航栏左边图片按钮
 
 @param image 按钮图片
 @param selector 自定义方法
 */
- (void)baseAddNavLeftBtnWithImage:(UIImage *)image selector:(SEL)selector;

/**
 自定义导航栏右边图片按钮
 
 @param image 按钮图片
 @param selector 自定义方法
 */
- (void)baseAddNavRightBtnWithImage:(UIImage *)image selector:(SEL)selector;

/**
 导航栏左边默认返回按钮
 */
- (void)defaultNavBackItemIcon;

/**
 自定义导航栏左边默认返回按钮事件
 
 @param selector 自定义事件
 */
- (void)defaultNavBackItemIconSelector:(SEL)selector;

- (void)baseAddNavRightBtnWithBtns:(NSArray *)btns;

/**
 Totas弹窗
 
 @param title 弹窗内容
 */
- (void)makeToast:(NSString *)title;

/**
 三方授权失败弹窗
 */
- (void)makeAuthorizeFailAlert;

/**
 直播结束弹窗
 */
- (void)makeLiveOverAlert;

/**
 字典转字符串
 
 @param dictionary 目标字典
 @return 转换后的字符串
 */
- (NSString *)dictionaryToJson:(NSDictionary *)dictionary;

/**
 获取图片某点颜色
 
 @param point 点的位置
 @return 颜色
 */
- (UIColor *)colorAtPixel:(CGPoint)point;
/**
 显示暂无数据图片
 */
- (void)showNoDataImage;

/**
 隐藏暂无数据图片
 */
- (void)hideNoDataImage;

/**
 网络测试
 */
- (NSInteger)networkTest;

/**
 更新设备机型
 */
- (void)updateEquipmentModels;

//加载动画效果
-(void)showWebLoadingWait;

//结束动画
-(void)endWebLoading;


/**
 加载中...
 
 @param str 文字
 */
- (void)showWaitHudWithString:(NSString *)str;

- (void)showWaitHudWithProgressString:(NSString *)progress title:(NSString *)str;
- (void)dissmissHudView;

/**
 控制当前控制器取消全屏
 ## 此方法必须在viewWillAppear调用！！！ 否则有可能出现右滑 无效问题 !!! ##
 @param enable YES 全屏右滑返回  NO 取消全屏右滑返回
 */
- (void)setPopGestureEnable:(BOOL)enable;

@end
