//
//  VULBaseViewController.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/4.
//  Copyright © 2018年 zcc. All rights reserved.
//

#import "VULBaseViewController.h"
#import "NSString+VULDeviceModel.h"
#import "VULNavigationViewController.h"
#import "MBProgressHUD.h"
#import "UIViewController+VULNavgationItem.h"
#import "UIButton+VULEdgeInsets.h"
#import "Toast+UIView.h"
#import "VULLoginViewController.h"
#import "FBWatermarkView.h"
@interface VULBaseViewController ()
@property (nonatomic, weak) UIImageView *lineView;



@property (nonatomic, strong) UIImageView *noDataBackgroundImage;/**< 暂无背景图片 */
@property (nonatomic, strong) MBProgressHUD *hudView;
@property (strong, nonatomic) VULButton *retryButton;

@end

@implementation VULBaseViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self updateEquipmentModels];
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self.navigationController setNavigationBarHidden:YES];
    
    [self.view addSubview:self.navigationView];
    [self.navigationView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.mas_equalTo(self.view);
        make.height.mas_equalTo(@(self.navigationHeight));
    }];
    
    //解决iOS11，仅实现heightForHeaderInSection，没有实现viewForHeaderInSection方法时,section间距大的问题
    [UITableView appearance].estimatedRowHeight = 0;
    [UITableView appearance].estimatedSectionHeaderHeight = 0;
    [UITableView appearance].estimatedSectionFooterHeight = 0;
    
    //iOS11 解决SafeArea的问题，同时能解决pop时上级页面scrollView抖动的问题
    if (@available(iOS 11, *)) {
        [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever; //iOS11 解决SafeArea的问题，同时能解决pop时上级页面scrollView抖动的问题
        self.additionalSafeAreaInsets = UIEdgeInsetsMake(0, 0, 0, 0);
    }
    if (@available(iOS 15.0, *)) {
        [UITableView appearance].sectionHeaderTopPadding = 0;
    }
    
    self.noDataBackgroundImage = [[UIImageView alloc] initWithImage:VULGetImage(@"no_data")];
    self.noDataBackgroundImage.contentMode = UIViewContentModeCenter;
    [self.view addSubview:self.noDataBackgroundImage];
    [self.noDataBackgroundImage mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.view);
    }];
    self.noDataBackgroundImage.alpha = 0;
    
    //设置字体的颜色
    self.navigationTitleLabel.textColor = HEXCOLOR(0x333333);
    
//    [self setStatusBarBackgroundColor:HEXCOLOR(0xf6f6f6)];
    
    if (self.navigationController && self.navigationController.viewControllers.count > 1) {
        [self defaultNavBackItemIcon];
    }
    
  

}

//设置状态栏颜色
- (void)setStatusBarBackgroundColor:(UIColor *)color {
    if (@available(iOS 13,*)) {
        if (!_statusBarView) {
            [[UIApplication sharedApplication].keyWindow addSubview:self.statusBarView];
        }
        self.statusBarView.backgroundColor = color;
    } else {
        UIView *statusBar = [[[UIApplication sharedApplication] valueForKey:@"statusBarWindow"] valueForKey:@"statusBar"];
        if ([statusBar respondsToSelector:@selector(setBackgroundColor:)]) {
            statusBar.backgroundColor = color;
        }
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    //点击背景收回键盘
    [IQKeyboardManager sharedManager].shouldResignOnTouchOutside = YES;
    [self startEyeCareMode];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self.statusBarView removeFromSuperview];
    
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    [self.view endEditing:YES];
    
    //当前控制器如果已经取消全屏右滑 必须在新控制器加回去
    [(VULNavigationViewController *)self.navigationController enablePopGesture:YES];
}

- (void)setPopGestureEnable:(BOOL)enable {
    [(VULNavigationViewController *)self.navigationController enablePopGesture:enable];
}

- (void)startEyeCareMode {
    // 护眼模式配置
//    if ([[EyeCareManage sharedUtil] queryEyeCareModeStatus]) {
//        [[EyeCareManage sharedUtil] switchEyeCareMode2:YES];
//    }
}

- (void)dealloc {
    NSLog(@"\n----dealloc-----%@",NSStringFromClass([self class]));
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - 旋转
- (BOOL)shouldAutorotate {
    return !((AppDelegate *)[UIApplication sharedApplication].delegate).zfplayer_allowOrentitaionRotation;
}


// TODO:三方授权失败 alert弹窗
- (void)makeAuthorizeFailAlert {
    //    [self ebh_showAlertWithTitle:@"授权失败" message:@"请重新尝试" appearanceProcess:^(EBHAlertController * _Nonnull alertMaker) {
    //        alertMaker.
    //        addActionCancelTitle(@"知道了");
    //    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, EBHAlertController * _Nonnull alertSelf) {
    //    }];
}

// TODO:直播结束 alert弹窗
- (void)makeLiveOverAlert {
    //    [self ebh_showAlertWithTitle:@"谢谢观看" message:@"当前直播已结束" appearanceProcess:^(EBHAlertController * _Nonnull alertMaker) {
    //        alertMaker.
    //        addActionCancelTitle(@"知道了");
    //    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, EBHAlertController * _Nonnull alertSelf) {
    //    }];
}

// TODO:toast弹窗
- (void)makeToast:(NSString *)title {
    if (self.view.window == nil) {
        return;
    }
    if (!NSStringIsNotEmpty(title)) {
        return;
    }
    if (self.popToastCenter <= 0) {
        self.popToastCenter = (VULSCREEN_HEIGHT - K_NavBar_Height) / 2;
    }
    [self.view.window makeToast:title duration:1.6 position:[NSValue valueWithCGPoint:CGPointMake(self.view.center.x, self.popToastCenter)]];
}

// TODO:字典转字符串
- (NSString *)dictionaryToJson:(NSDictionary *)dictionary {
    NSError *parseError = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dictionary options:NSJSONWritingPrettyPrinted error:&parseError];
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}

/**
 自定义导航栏左边标题按钮隐藏
 */
- (void)baseSetLeftBtnhide {
    _leftButton.hidden = YES;
}

/**
 自定义导航栏左边标题按钮显示
 */
- (void)baseSetLeftBtnShow {
    _leftButton.hidden = NO;
}

#pragma mark -
#pragma mark - NavBtn
- (void)baseAddNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector {
    [self.navigationViewLeftButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_leftButton removeAllTargets];
    _leftButton = [self addNavLeftBtnWithTitle:title selector:selector];
    _leftButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [self.navigationViewLeftButton addSubview:_leftButton];
    [_leftButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewLeftButton);
    }];
}

- (void)baseAddNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector {
    [self.navigationViewRightButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_rightButton removeAllTargets];
    _rightButton = [self addNavRightBtnWithTitle:title selector:selector];
    _rightButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
    [self.navigationViewRightButton addSubview:_rightButton];
    [_rightButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewRightButton);
    }];
}

- (void)baseAddNavLeftBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color {
    [self.navigationViewLeftButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_leftButton removeAllTargets];
    _leftButton = [self addNavLeftBtnWithTitle:title selector:selector color:color];
    [self.navigationViewLeftButton addSubview:_leftButton];
    [_leftButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewLeftButton);
    }];
}

- (void)baseAddNavRightBtnWithTitle:(NSString *)title selector:(SEL)selector color:(UIColor *)color {
    [self.navigationViewRightButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_rightButton removeAllTargets];
    _rightButton = [self addNavRightBtnWithTitle:title selector:selector color:color];
    _rightButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
    [self.navigationViewRightButton addSubview:_rightButton];
    [_rightButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewRightButton);
    }];
}

- (void)baseAddNavLeftBtnWithImage:(UIImage *)image selector:(SEL)selector {
    [self.navigationViewLeftButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_leftButton removeAllTargets];
    _leftButton = [self addNavLeftBtnWithImage:image selector:selector];
    [_leftButton setEdgeInsetsWithType:VULEdgeInsetsTypeImage marginType:VULMarginTypeLeft margin:0.0f];
    [self.navigationViewLeftButton addSubview:_leftButton];
    [_leftButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewLeftButton);
    }];
}

- (void)baseAddNavRightBtnWithImage:(UIImage *)image selector:(SEL)selector {
    [self.navigationViewRightButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_rightButton removeAllTargets];
    _rightButton = [self addNavRightBtnWithImage:image selector:selector];
    [_rightButton setEdgeInsetsWithType:VULEdgeInsetsTypeImage marginType:VULMarginTypeRight margin:0.0f];
    [self.navigationViewRightButton addSubview:_rightButton];
    [_rightButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewRightButton);
    }];
}

- (void)setNavRightBtnWithColor:(UIColor *)color WithState:(UIControlState)state {
    [_rightButton setTitleColor:color forState:state];
}

- (void)setNavRightBtnEnable:(BOOL)enable {
    _rightButton.enabled = enable;
}

- (void)defaultNavBackItemIcon {
    [self.navigationViewLeftButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_leftButton removeAllTargets];
    _leftButton = [self defaultNavBackItemIconAndselector:@selector(defaultbackSelector:)];
    [_leftButton setEdgeInsetsWithType:VULEdgeInsetsTypeImage marginType:VULMarginTypeLeft margin:0.0f];
    [self.navigationViewLeftButton addSubview:_leftButton];
    [_leftButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewLeftButton);
    }];
}

- (void)defaultbackSelector:(id)sender {
    if ((self.presentedViewController || self.presentingViewController) && self.childViewControllers.count == 1) {
        [self dismissViewControllerAnimated:YES completion:nil];
    } else {
        [self.navigationController popViewControllerAnimated:YES];
    }
}

- (void)defaultNavBackItemIconSelector:(SEL)selector {
    [self.navigationViewLeftButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [_leftButton removeAllTargets];
    _leftButton = [self defaultNavBackItemIconAndselector:selector];
    [_leftButton setEdgeInsetsWithType:VULEdgeInsetsTypeImage marginType:VULMarginTypeLeft margin:0.0f];
    [self.navigationViewLeftButton addSubview:_leftButton];
    [_leftButton mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(self.navigationViewLeftButton);
    }];
}

- (void)baseAddNavRightBtnWithBtns:(NSArray *)btns {
    [self.navigationViewRightButton.subviews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    for (int i = 0; i < btns.count; i++) {
        UIButton *btn = btns[i];
        [self.navigationViewRightButton addSubview:btn];
        if (i == 0) {
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.centerY.mas_equalTo(self.navigationViewRightButton);
                make.left.mas_equalTo(0);
            }];
        } else {
            UIButton *preBtn = btns[i-1];
            [btn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.centerY.mas_equalTo(self.navigationViewRightButton);
                make.left.mas_equalTo(preBtn.mas_right).mas_offset(10);
            }];
        }
    }
    NSInteger width = 30;
    if (btns.count ==2) {
        width = 60;
    }else if (btns.count ==3){
        width = 90;

    }
    [self.navigationViewRightButton mas_updateConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.navigationView);
        make.top.equalTo(self.navigationView.mas_top).with.offset(self.navigationBarHeight);
        make.width.mas_equalTo(@(width));
        make.height.mas_equalTo(@(self.navigationViewHeight));
    }];
    
    [self.navigationTitleLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.navigationView.mas_bottom);
        make.left.mas_equalTo(0);
        make.right.mas_equalTo(0);
        make.height.mas_equalTo(@(self.navigationViewHeight));
    }];
}

#pragma mark -
#pragma mark - XSLProcessingStatusCodeDelegate
// 成功
- (void)success:(YTKBaseRequest *)request {
}

// 参数错误
- (void)parameterError:(YTKBaseRequest *)request {
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self makeToast:@"客户端异常"];
}

// 服务器错误
- (void)serverError:(YTKBaseRequest *)request {
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self makeToast:@"服务器未知错误"];
}

// 设备限制
- (void)equipmentRestrictions:(YTKBaseRequest *)request {
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self makeToast:@"设备受限"];
    VULNavigationViewController *navLogin = [[VULNavigationViewController alloc] initWithRootViewController:[[VULLoginViewController alloc] init]];
    [self changeRootViewController:navLogin];
}

// 用户未选择网校 (此状态码 请执行选择网校操作)
- (void)notSelectSchool:(YTKBaseRequest *)request {
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self makeToast:@"无法获取网校信息"];
    VULNavigationViewController *navLogin = [[VULNavigationViewController alloc] initWithRootViewController:[[VULLoginViewController alloc] init]];
    [self changeRootViewController:navLogin];
}

// 授权失败 (此状态码 请执行用户登录操作)
- (void)authorizationFailed:(YTKBaseRequest *)request {
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self makeToast:@"用户登录授权失败"];
    VULNavigationViewController *navLogin = [[VULNavigationViewController alloc] initWithRootViewController:[[VULLoginViewController alloc] init]];
    [self changeRootViewController:navLogin];
}

// 用户名密码错误
- (void)usernameOrPasswordIncorrect:(YTKBaseRequest *)request {
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self makeToast:request.responseObject[@"msg"]];
}

// TODO:切换rootViewController
- (void)changeRootViewController:(UIViewController *)rootViewController {
    typedef void (^Animation)(void);
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    rootViewController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    Animation animation = ^{
        BOOL oldState = [UIView areAnimationsEnabled];
        [UIView setAnimationsEnabled:NO];
        [UIApplication sharedApplication].keyWindow.rootViewController = rootViewController;
        [UIView setAnimationsEnabled:oldState];
    };
    
    [UIView transitionWithView:window duration:0.5f options:UIViewAnimationOptionTransitionCrossDissolve animations:animation completion:nil];
}

// TODO:获取图像某一点颜色
- (UIColor *)colorAtPixel:(CGPoint)point {
    // Cancel if point is outside image coordinates
    if (!CGRectContainsPoint(CGRectMake(0.0f, 0.0f, VULSCREEN_WIDTH, VULSCREEN_HEIGHT), point)) {
        return nil;
    }
    // Create a 1x1 pixel byte array and bitmap context to draw the pixel into.
    // Reference: http://stackoverflow.com/questions/1042830/retrieving-a-pixel-alpha-value-for-a-uiimage
    NSInteger pointX = trunc(point.x);
    NSInteger pointY = trunc(point.y);
    CGImageRef cgImage = VULGetImage(@"unable_open").CGImage;
    NSUInteger width = VULSCREEN_WIDTH;
    NSUInteger height = VULSCREEN_HEIGHT;
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    int bytesPerPixel = 4;
    int bytesPerRow = bytesPerPixel * 1;
    NSUInteger bitsPerComponent = 8;
    unsigned char pixelData[4] = { 0, 0, 0, 0 };
    CGContextRef context = CGBitmapContextCreate(pixelData,
                                                 1,
                                                 1,
                                                 bitsPerComponent,
                                                 bytesPerRow,
                                                 colorSpace,
                                                 kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
    CGColorSpaceRelease(colorSpace);
    CGContextSetBlendMode(context, kCGBlendModeCopy);
    
    // Draw the pixel we are interested in onto the bitmap context
    CGContextTranslateCTM(context, -pointX, pointY - (CGFloat)height);
    CGContextDrawImage(context, CGRectMake(0.0f, 0.0f, (CGFloat)width, (CGFloat)height), cgImage);
    CGContextRelease(context);
    
    // Convert color values [0..255] to floats [0.0..1.0]
    CGFloat red = (CGFloat)pixelData[0] / 255.0f;
    CGFloat green = (CGFloat)pixelData[1] / 255.0f;
    CGFloat blue = (CGFloat)pixelData[2] / 255.0f;
    CGFloat alpha = (CGFloat)pixelData[3] / 255.0f;
    return [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}

// TODO:网络测试
- (NSInteger)networkTest {
    AFNetworkReachabilityManager *manager = [AFNetworkReachabilityManager sharedManager];
    [manager startMonitoring];
    [manager setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status) {
        switch (status) {
            case AFNetworkReachabilityStatusUnknown: {
                //未知网络
                NSLog(@"未知网络");
                self.networkReachabilityStatus = VULNetworkReachabilityStatusUnknown;
            }
                break;
            case AFNetworkReachabilityStatusNotReachable: {
                //无法联网
                NSLog(@"无法联网");
                self.networkReachabilityStatus = VULNetworkReachabilityStatusNotReachable;
            }
                break;
            case AFNetworkReachabilityStatusReachableViaWWAN: {
                //手机自带网络
                NSLog(@"当前使用的是2g/3g/4g网络");
                self.networkReachabilityStatus = VULNetworkReachabilityStatusReachableViaWWAN;
            }
                break;
            case AFNetworkReachabilityStatusReachableViaWiFi: {
                //WIFI
                NSLog(@"当前在WIFI网络下");
                self.networkReachabilityStatus = VULNetworkReachabilityStatusReachableViaWiFi;
            }
            default: {
                self.networkReachabilityStatus = VULNetworkReachabilityStatusUnknown;
            }
                break;
        }
    }];
    return self.networkReachabilityStatus;
}

- (void)updateEquipmentModels {
    // 设备机型
    self.navigationViewDefaultTitleSize = 18;//16;
    self.navigationViewHeight = 44;
    if (!VUL_IS_NOTCH) {
        self.navigationBarHeight = 20;
        self.navigationHeight = 64;
        self.tabBarHeight = 49;
        self.studentHomeHeaderHeight = 180;
    } else {
        self.navigationBarHeight = 44;
        self.navigationHeight = 88;
        self.tabBarHeight = 83;
        self.studentHomeHeaderHeight = 200;
    }
}

#pragma mark -
#pragma mark -
// 显示暂无数据图片
- (void)showNoDataImage {
    [self.view bringSubviewToFront:self.noDataBackgroundImage];
    [self.view bringSubviewToFront:self.navigationView];
    self.noDataBackgroundImage.alpha = 1;
}

// 隐藏暂无数据图片
- (void)hideNoDataImage {
    self.noDataBackgroundImage.alpha = 0;
}

#pragma mark -
#pragma mark - getter and setter

- (VULNavigationView *)navigationView {
    if (!_navigationView) {
        _navigationView = [[VULNavigationView alloc] init];
        [_navigationView addSubview:self.navigationViewBackgroundImageView];
        [_navigationView addSubview:self.navigationTitleLabel];
        [_navigationView addSubview:self.navigationViewLeftButton];
        [_navigationView addSubview:self.navigationViewRightButton];
        [self.navigationViewLeftButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self.navigationView.mas_left).with.offset(15);
            make.top.equalTo(self.navigationView.mas_top).with.offset(self.navigationBarHeight);
            make.width.mas_equalTo(@(self.navigationViewHeight));
            make.height.mas_equalTo(@(self.navigationViewHeight));
        }];
        [self.navigationViewRightButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.equalTo(self.navigationView.mas_right).with.offset(-15);
            make.top.equalTo(self.navigationView.mas_top).with.offset(self.navigationBarHeight);
            make.width.mas_equalTo(@(self.navigationViewHeight));
            make.height.mas_equalTo(@(self.navigationViewHeight));
        }];
        [self.navigationTitleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.bottom.equalTo(self.navigationView.mas_bottom);
            make.left.equalTo(self.navigationViewLeftButton.mas_right);
            make.right.equalTo(self.navigationViewRightButton.mas_left);
            make.height.mas_equalTo(@(self.navigationViewHeight));
        }];
        [self.navigationViewBackgroundImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(self.navigationView);
        }];
    }
    return _navigationView;
}

- (UILabel *)navigationTitleLabel {
    if (!_navigationTitleLabel) {
        _navigationTitleLabel = [[UILabel alloc] initWithFrame:CGRectMake(85, self.navigationBarHeight, VULSCREEN_WIDTH - 170, self.navigationViewHeight)];
        _navigationTitleLabel.font = [UIFont boldSystemFontOfSize:self.navigationViewDefaultTitleSize];
        _navigationTitleLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _navigationTitleLabel;
}

- (UIImageView *)navigationViewBackgroundImageView {
    if (!_navigationViewBackgroundImageView) {
        _navigationViewBackgroundImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, self.navigationViewHeight + self.navigationBarHeight)];
        _navigationViewBackgroundImageView.contentMode = UIViewContentModeScaleToFill;
        //        _navigationViewBackgroundImageView.image = VULGetImage(@"icon_navigation_back_view");
    }
    return _navigationViewBackgroundImageView;
}

- (UIView *)navigationViewLeftButton {
    if (!_navigationViewLeftButton) {
        _navigationViewLeftButton = [[UIView alloc] initWithFrame:CGRectZero];
    }
    return _navigationViewLeftButton;
}

- (UIView *)navigationViewRightButton {
    if (!_navigationViewRightButton) {
        _navigationViewRightButton = [[UIView alloc] initWithFrame:CGRectZero];
    }
    return _navigationViewRightButton;
}

- (void)setNavigationBackgroundImage:(NSString *)navigationViewBackgroundImage {
    self.navigationView.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, self.navigationViewHeight + self.navigationBarHeight);
    self.navigationViewBackgroundImageView.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, self.navigationViewHeight + self.navigationBarHeight);
    self.navigationViewBackgroundImageView.image = [UIImage imageNamed:navigationViewBackgroundImage];
}

- (void)setNavigationTitle:(NSString *)navigationTitle {
    self.navigationTitleLabel.text = navigationTitle;
}

- (void)setNavigationTitleFont:(NSString *)navigationTitleFont {
    if (!self.navigationTitleFontSize) {
        self.navigationTitleLabel.font = [UIFont fontWithName:navigationTitleFont size:self.navigationTitleFontSize];
        return;
    }
    //字体
    self.navigationTitleLabel.font = [UIFont fontWithName:navigationTitleFont size:self.navigationViewDefaultTitleSize];
}

- (void)setNavigationViewBackgroundColor:(UIColor *)navigationViewBackgroundColor {
    self.navigationView.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, self.navigationViewHeight + self.navigationBarHeight);
    self.navigationView.backgroundColor = navigationViewBackgroundColor;
}

- (void)setNavigationTitleColor:(UIColor *)navigationTitleColor {
    self.navigationTitleLabel.textColor = navigationTitleColor;
}

- (void)setNavigationTitleFontSize:(CGFloat)navigationTitleFontSize {
    if (!self.navigationTitleFont) {
        self.navigationTitleLabel.font = [UIFont boldSystemFontOfSize:navigationTitleFontSize];
        return;
    }
    self.navigationTitleLabel.font = [UIFont fontWithName:self.navigationTitleFont size:navigationTitleFontSize];
}

- (void)setNavigationViewHide:(BOOL)navigationViewHide {
    if (!navigationViewHide) {
        return;
    }
    self.navigationView.hidden = navigationViewHide;
}

- (void)setNavigationViewAlpha:(CGFloat)navigationViewAlpha {
    self.navigationView.alpha = navigationViewAlpha;
}

- (VULResponseStatusCode *)processingStatusCode {
    if (!_processingStatusCode) {
        _processingStatusCode = [[VULResponseStatusCode alloc] init];
        _processingStatusCode.responseStatusCodeDelegate = self;//关联代理
    }
    return _processingStatusCode;
}

- (VULWaitingAnimationView *)waitView {
    if (!_waitView) {
        _waitView = [[VULWaitingAnimationView alloc] initWithFrame:CGRectMake(0, 0, 60, 60)];
    }
    return _waitView;
}

//加载动画效果
- (void)showWebLoadingWait {
    [self.view addSubview:self.waitView];
    [_waitView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.width.height.equalTo(@60);
        make.center.equalTo(self.view);
    }];
}

//结束动画
- (void)endWebLoading {
    [self.waitView removeFromSuperview];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self endWebLoading];
}

- (void)showWaitHudWithString:(NSString *)str {
    if (!self.view) {
        return;
    }
    if (_hudView == nil) {
        _hudView = [[MBProgressHUD alloc] initWithView:self.view];
        _hudView.label.text = str;
        _hudView.offset = CGPointMake(0.f, -30);
        [self.view addSubview:_hudView];
        [_hudView showAnimated:YES];
    }
}


- (void)showWaitHudWithProgressString:(NSString *)progress title:(NSString *)str{
    if (!self.view) {
        return;
    }
    if (_hudView == nil) {
        _hudView = [[MBProgressHUD alloc] initWithView:self.view];
        _hudView.label.text = str;
        _hudView.progress = progress.floatValue/100;
        _hudView.contentColor = BtnColor;
        _hudView.mode = MBProgressHUDModeAnnularDeterminate;
 
        [self.view addSubview:_hudView];
        [_hudView showAnimated:YES];
    }else{
        _hudView.progress = progress.floatValue/100;

    }
}

- (void)dissmissHudView {
    if (_hudView) {
        [_hudView hideAnimated:YES];
        [_hudView removeFromSuperview];
        _hudView = nil;
    }
}

- (void)responseError:(YTKBaseRequest *)request {
    NSLog(@"\n*********** 网络请求错误 *************\n*********** code:%@ *************\n*********** message:%@ *************\n*********** success:%@ *************\n", request.responseObject[@"code"], request.responseObject[@"message"], request.responseObject[@"success"]);
}

- (VULResponseStatusCode *)responseStatusCode {
    if (!_responseStatusCode) {
        _responseStatusCode = [[VULResponseStatusCode alloc] init];
        _responseStatusCode.responseStatusCodeDelegate = self;
    }
    return _responseStatusCode;
}

- (VULButton *)retryButton {
    if (_retryButton == nil) {
        _retryButton = [VULButton getCustomBtnWithFrame:CGRectMake((VULSCREEN_WIDTH - 80) / 2, 160, 80, 40) title:@"刷新" Font:18 Bgcolor:HEXCOLOR(0x73B6F5) Target:self action:@selector(retryButtonForGetData:)];
        _retryButton.titleLabel.font = VULPingFangSCHeavy(22);
        [_retryButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        _retryButton.layer.cornerRadius = 5.f;
    }
    return _retryButton;
}

//加载失败 重新获取
- (void)retryButtonForGetData:(id)sender {
}

- (UIView *)retryView {
    if (_retryView == nil) {
        _retryView = [[UIView alloc] initWithFrame:CGRectMake(0, self.view.center.y - 180, kScreenWidth, 200)];
        _retryView.backgroundColor = [UIColor clearColor];
        _retryView.center = CGPointMake(_retryView.center.x, self.view.center.y);
        UIImageView *imageV = [[UIImageView alloc] initWithFrame:CGRectMake((kScreenWidth - 160) / 2, 0, 160, 160)];
        imageV.contentMode = UIViewContentModeScaleAspectFit;
        imageV.userInteractionEnabled = YES;
        imageV.image = [UIImage imageNamed:@"no_data"];
        
        [_retryView addSubview:imageV];
        [_retryView addSubview:self.retryButton];
        [self.view addSubview:_retryView];
        self.retryView.hidden = YES;
    }
    return _retryView;
}

- (UIView *)statusBarView {
    if (!_statusBarView) {
        if (@available(iOS 13,*)) {
            _statusBarView = [[UIView alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.windowScene.statusBarManager.statusBarFrame];
        }
    }
    return _statusBarView;
}

@end
