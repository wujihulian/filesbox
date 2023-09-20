//
//  AppDelegate.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/13.
//

#import "AppDelegate.h"
#import "VULLoginViewController.h"
#import "FBTabBarControllerConfig.h"
#import "AFNetworkActivityIndicatorManager.h"
#import "FBHomeViewController.h"
#import <DTShareKit/DTOpenAPI.h>  // 替换成钉钉 SDK 对应的头文件
#import "WeiboSDK.h"

@interface AppDelegate ()
@property (nonatomic) UIBackgroundTaskIdentifier backgroundTaskID;

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    if (@available(iOS 11.0, *)) {
        [[UIScrollView appearance] setContentInsetAdjustmentBehavior:UIScrollViewContentInsetAdjustmentNever];
    }
    [DownloadProgress sharedInstance].isShare = NO;
    [DTOpenAPI registerApp:@"dingyw0a2j25u6cjqifl"];  // 替换为您的钉钉 App ID
    [WeiboSDK registerApp:@"3988608636" universalLink:@"www.filesbox.cn"];

    // 数据库 数据迁移
    RLMRealmConfiguration *realmConfig = [RLMRealmConfiguration defaultConfiguration];
    realmConfig.schemaVersion = 2;
    realmConfig.migrationBlock = ^(RLMMigration *migration, uint64_t oldSchemaVersion) {
    };
    [RLMRealmConfiguration setDefaultConfiguration:realmConfig];
    [RLMRealm defaultRealm];
    self.window = [[UIWindow alloc] init];
    self.window.frame = [UIScreen mainScreen].bounds;
    [self.window makeKeyAndVisible];
    
    if(@available(iOS 13.0, *)) {
        self.window.overrideUserInterfaceStyle = UIUserInterfaceStyleLight;
    }
    
    // 无关请求放在子线程中,加快启动速度
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        [self networkChange];

    });
    if (![[NSUserDefaults standardUserDefaults]objectForKey:@"appLanguage"]) {
            //获得当前语言
            NSArray *languages = [NSLocale preferredLanguages];
            NSString *language = [languages objectAtIndex:0];
            if([language hasPrefix:@"en"]){
                [[NSUserDefaults standardUserDefaults] setObject:@"en" forKey:@"appLanguage"];

            }else{
                [[NSUserDefaults standardUserDefaults] setObject:@"zh-Hans" forKey:@"appLanguage"];

            }
            [[NSUserDefaults standardUserDefaults]synchronize];
    }else{
        MJRefreshConfig.defaultConfig.languageCode = [[NSUserDefaults standardUserDefaults]objectForKey:@"appLanguage"] ;

    }
    [WXApi registerApp:WechatAppID universalLink:WeChatUniversalLink];
      
    NSString *tokenStr = [VULRealmDBManager getLocalToken];
    if (NSStringIsNotEmpty(tokenStr)) {
        
        // 开始登录加载动画
        YTKNetworkConfig *config = [YTKNetworkConfig sharedConfig];
        config.baseUrl = [[NSUserDefaults standardUserDefaults] objectForKey:@"baseUrl"];;
        FBTabBarControllerConfig *tabBarStuControllerConfig = [[FBTabBarControllerConfig alloc] init];
        self.window.rootViewController = tabBarStuControllerConfig.tabBarController;
    }else{
        VULLoginViewController *login = [[VULLoginViewController alloc] init];
        VULNavigationViewController *nav = [[VULNavigationViewController alloc] initWithRootViewController:login];
        self.window.rootViewController = nav;
    }

    return YES;
}

#pragma mark - 网络请求
-(FBWatermarkView *)watermarkView{
    if (!_watermarkView) {
        _watermarkView = [[FBWatermarkView alloc] initWithFrame:self.window.bounds];
    }
    return _watermarkView;
}
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
// TODO:实时监控网络状态
- (void)networkChange {
    [[AFNetworkActivityIndicatorManager sharedManager] setEnabled:YES];
    NSURL *url = [NSURL URLWithString:@"http://baidu.com"];
    AFHTTPSessionManager *manager = [[AFHTTPSessionManager alloc] initWithBaseURL:url];
    [manager.reachabilityManager setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status) {
        self.netWorkStatesCode = status;
        switch (status) {
            case AFNetworkReachabilityStatusReachableViaWWAN:
                NSLog(@"当前使用的是流量模式");
                break;
            case AFNetworkReachabilityStatusReachableViaWiFi:
                NSLog(@"当前使用的是wifi模式");
                break;
            case AFNetworkReachabilityStatusNotReachable:
                NSLog(@"断网了");
                break;
            case AFNetworkReachabilityStatusUnknown:
                NSLog(@"变成了未知网络状态");
                break;
                
            default:
                break;
        }
        //        [VULNotificationCenter postNotificationName:@"netWorkChangeEventNotification" object:@(status)];
    }];
    [manager.reachabilityManager startMonitoring];
    
}


+ (void)zfplayer_allowOrentitaionRotation:(BOOL)allowOrentitaionRotation {
    [(AppDelegate *)[UIApplication sharedApplication].delegate setZfplayer_allowOrentitaionRotation:allowOrentitaionRotation];
}
- (void)applicationDidEnterBackground:(UIApplication *)application {
   

    // 开启后台任务
    self.backgroundTaskID = [application beginBackgroundTaskWithExpirationHandler:^{
        // 后台任务结束
        [application endBackgroundTask:self.backgroundTaskID];
        self.backgroundTaskID = UIBackgroundTaskInvalid;
    }];
}


- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey, id> *)options {

    if ([url.scheme isEqualToString:@"FilesBox"]) {
        if ([url.host isEqualToString:@"share"]) {
            NSString *tokenStr = [VULRealmDBManager getLocalToken];
            if (NSStringIsNotEmpty(tokenStr)) {
                
                // 处理自定义 URL，例如打开特定的页面
                [NSObject cyl_dismissAll:^{
                    [[NSObject cyl_currentNavigationController] popToRootViewControllerAnimated:NO];
                    FBHomeViewController *vc = [FBHomeViewController new];
                    vc.isMove = YES;
                    vc.icon = @"upload";
                    vc.operation = @"upload";
                    [[NSObject cyl_currentNavigationController] presentViewController:vc animated:YES completion:nil];
                }];
                
            }else{
                [DownloadProgress sharedInstance].isShare = YES;
                return YES;

            }
            return YES;
        };
       
      }
    BOOL result =  [WXApi handleOpenURL:url delegate:[WXApiManger shareInstance]];
    if (!result) {
        if ([url.description hasPrefix:@"wx"]) {
            return [WXApi handleOpenURL:url delegate:[WXApiManger shareInstance]];
        }
        if ([url.description hasPrefix:@"dingyw0a2j25u6cjqifl"]) {
            return [DTOpenAPI handleOpenURL:url delegate:nil];
        }
        if ([url.description hasPrefix:@"wb3988608636"]) {
            return [WeiboSDK handleOpenURL:url delegate:nil];
        }
        if (url != nil) {
           
            [DownloadProgress sharedInstance].otherUrl =url;
//            NSData *data = [[NSData alloc] initWithContentsOfFile:string];
            [DownloadProgress sharedInstance].isOther = YES;
            NSString *tokenStr = [VULRealmDBManager getLocalToken];
            if (NSStringIsNotEmpty(tokenStr)) {
                
                // 处理自定义 URL，例如打开特定的页面
                [NSObject cyl_dismissAll:^{
                    [[NSObject cyl_currentNavigationController] popToRootViewControllerAnimated:NO];
                    FBHomeViewController *vc = [FBHomeViewController new];
                    vc.isMove = YES;
                    vc.icon = @"upload";
                    vc.operation = @"upload";
                    [[NSObject cyl_currentNavigationController] presentViewController:vc animated:YES completion:nil];
                }];
                
            }else{
                [DownloadProgress sharedInstance].isShare = YES;
                return YES;
                
            }
            
        }

    }
    return result;
}
- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(nonnull void (^)(NSArray<id<UIUserActivityRestoring>> * _Nullable))restorationHandler {
    if ([userActivity.activityType isEqualToString:NSUserActivityTypeBrowsingWeb]) {
        NSURL *url = userActivity.webpageURL;
        [WXApi handleOpenUniversalLink:userActivity delegate:[WXApiManger shareInstance]];

        NSLog(@"userActivity webpageURL: %@",url.absoluteString);
    }
    
   
    return YES;
}


@end
