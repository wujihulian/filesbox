//
//  VULUpdateTool.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULUpdateTool.h"
#import "VULUpdatePopupView.h"
//#import "BGFMDB.h"

@implementation VULAppstoreInfo

- (instancetype)initWithDictionary:(NSDictionary *)dictionary {
    if (self = [super init]) {
        if (!dictionary || dictionary.count <= 0) {
            return nil;
        }
        _version = dictionary[@"version"];

        _releaseNotes = dictionary[@"releaseNotes"];
    }
    return self;
}

@end

@interface VULUpdateTool ()<SKStoreProductViewControllerDelegate>

typedef void (^CacheBlockData)(NSError *error, VULAppstoreInfo *info);

@end

@implementation VULUpdateTool

+ (VULUpdateTool *)shareInstance {
    static dispatch_once_t once;
    static id __singleton__;
    dispatch_once(&once, ^{ __singleton__ = [[self alloc] init]; });
    return __singleton__;
}

- (instancetype)init {
    if (self = [super init]) {
        _remindDay = 1;
    }
    return self;
}

- (void)begin {
    __weak __typeof(self) weakSelf = self;
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSAssert(weakSelf.appleID.length > 0, @"you must give \'appleID\' a value");
        NSAssert(weakSelf.curAppVersion.length > 0, @"you must give \'curAppVersion\' a value");
        [weakSelf updateInfo:^(NSError *error, VULAppstoreInfo *info) {
            [weakSelf updateAppWithInfo:info];
        }];
    });
}

/**
 * 从appstore获取app的信息
 */
- (void)updateInfo:(CacheBlockData)block {
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://itunes.apple.com/cn/lookup?id=%@", self.appleID]];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    request.HTTPMethod = @"POST";
    request.HTTPBody = [[NSString stringWithFormat:@"id=%@", self.appleID] dataUsingEncoding:NSUTF8StringEncoding];
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *task = [session dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
                           if (!error) {
                               NSDictionary *responseObject =  [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
                               NSArray *appInfos = responseObject[@"results"];
                               if (!appInfos || appInfos.count <= 0) {
                                   block(nil, nil);
                               }
                               NSDictionary *appInfo = appInfos[0];
                               VULAppstoreInfo *appstoreInfo = [[VULAppstoreInfo alloc] initWithDictionary:appInfo];
                               block(nil, appstoreInfo);
                           } else {
                               block(error, nil);
                           }
                       });
    }];
    [task resume];
}

/**
 * 根据app信息判断当前版本是否需要更新，以及更新逻辑。 用户点击忽略版本号时未记录版本号 会忽略所有的版本号
 */
- (void)updateAppWithInfo:(VULAppstoreInfo *)updateInfo {
    if (!updateInfo) {
        return;
    }
    self.isUpdate = NO;
    if ([self.curAppVersion compare:updateInfo.version options:NSNumericSearch] == NSOrderedAscending) {
        self.isUpdate = YES;
    }
    if (self.isUpdate) {
        //需要升级
        if ([VULUserDefaults boolForKey:@"ignoreVersion"]) {
            //忽略此版本，不需要更新
            NSLog(@"用户忽略此版本更新");
            return;
        }
        NSTimeInterval timeInterval = [[NSDate date] timeIntervalSince1970];
        NSTimeInterval savedInterval = [VULUserDefaults doubleForKey:@"newVersionTime"];
//        NSString *title = [NSString stringWithFormat:@"有新版本%@更新", updateInfo.version];
        //选择稍后更新，默认为3天后提示
        if (timeInterval - savedInterval > self.remindDay * 24 * 60 * 60) {
            dispatch_async(dispatch_get_main_queue(), ^{
                VULUpdatePopupView *updateview = [[VULUpdatePopupView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) withUpdateInfo:updateInfo];
                [kWindow addSubview:updateview];
                [updateview showUpdatePopupView];
                updateview.GoToUpgradeBlock = ^{
        
                    SKStoreProductViewController *storeViewController = [[SKStoreProductViewController alloc] init];
                    [storeViewController loadProductWithParameters:@{ SKStoreProductParameterITunesItemIdentifier: self.appleID } completionBlock:nil];
                    storeViewController.delegate = self;
                    [self.controller presentViewController:storeViewController animated:YES completion:nil];
                };

                /*
                UIAlertController *alertController = [UIAlertController alertControllerWithTitle:title message:updateInfo.releaseNotes preferredStyle:UIAlertControllerStyleAlert];
                UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:@"稍后提醒我" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
                    //稍后提醒 记录下当前时间
                    NSTimeInterval timeInterval = [[NSDate date] timeIntervalSince1970];
                    [VULUserDefaults setDouble:timeInterval forKey:@"newVersionTime"];
                }];
                [alertController addAction:cancelAction];
                UIAlertAction *updateAction = [UIAlertAction actionWithTitle:@"去更新" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    //去更新
                    SKStoreProductViewController *storeViewController = [[SKStoreProductViewController alloc] init];
                    [storeViewController loadProductWithParameters:@{SKStoreProductParameterITunesItemIdentifier:self.appleID} completionBlock:nil];
                    storeViewController.delegate = self;
                    [self.controller presentViewController:storeViewController animated:YES completion:nil];
                }];
                [alertController addAction:updateAction];
                UIAlertAction *laterAction = [UIAlertAction actionWithTitle:@"忽略此版本" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    [VULUserDefaults setBool:YES forKey:@"ignoreVersion"];
                }];
                [alertController addAction:laterAction];
                if (self.controller) {
                    [self.controller presentViewController:alertController animated:YES completion:nil];
                } else {
                    NSLog(@"Error:controller is nil, and the update alertController can not be shown!");
                }
                 */
            });
        } else {
            NSLog(@"稍后提醒，未到更新时间");
        }
    } else {
        NSLog(@"当前版本已是最新，不需要更新");
        //重置升级标识
        [VULUserDefaults removeObjectForKey:@"ignoreVersion"];
        [VULUserDefaults removeObjectForKey:@"newVersionTime"];
    }
}

- (void)productViewControllerDidFinish:(SKStoreProductViewController *)viewController {
    [viewController dismissViewControllerAnimated:YES completion:nil];
}

- (BOOL)isUpdateBool {
    return self.isUpdate;
}

@end
