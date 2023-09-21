//
//  AppDelegate+AppUpdate.m
//  UnlimitedBusiness
//
//  Created by yuekewei on 2021/4/6.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "AppDelegate+AppUpdate.h"
#import "VULUpdatePopupView.h"
//#import "BGFMDB.h"

static NSString *const kAppUpdateVersion = @"kAppUpdateVersion";

@implementation AppDelegate (AppUpdate)

+ (void)checkUpdate:(void(^)(NSDictionary *result))completion {
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://itunes.apple.com/cn/lookup?id=%@", kAppID]];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    request.HTTPMethod = @"POST";
    request.HTTPBody = [[NSString stringWithFormat:@"id=%@", kAppID] dataUsingEncoding:NSUTF8StringEncoding];
    
    [[[NSURLSession sharedSession] dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            if (!error) {
                NSDictionary *responseObject =  [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
                NSArray *appInfos = responseObject[@"results"];
                if (!appInfos || appInfos.count <= 0) {
                    completion(nil);
                }
                NSDictionary *appInfo = appInfos[0];
                
                completion(appInfo);
            }
            else {
                completion(nil);
            }
        });
    }] resume];
}

+ (void)checkAndShowUpdateAlert {
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:kAppUpdateVersion];
    [self checkUpdate:^(NSDictionary *result) {
        if (result && result.count > 0) {
            NSString *version = [result objectForKey:@"version"];
            [[NSUserDefaults standardUserDefaults] setObject:version forKey:kAppUpdateVersion];
            [[NSUserDefaults standardUserDefaults] synchronize];
            if ([kCurrentVersion compare:version options:NSNumericSearch] == NSOrderedAscending) {
                VULAppstoreInfo *updateInfo = [[VULAppstoreInfo alloc] initWithDictionary:result];
                VULUpdatePopupView *updateview = [[VULUpdatePopupView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) withUpdateInfo:updateInfo];
                [kWindow addSubview:updateview];
        
                [updateview showUpdatePopupView];
                updateview.GoToUpgradeBlock = ^{
              
                    NSString *downloadStr = [NSString stringWithFormat:@"https://itunes.apple.com/cn/app/id%@?mt=8", kAppID];
                    NSURL *url = [NSURL URLWithString:downloadStr];
                    [[UIApplication sharedApplication] openURL:url];
                };
            }
        }
        
    }];
}

+ (void)hasNewVersion:(void(^)(BOOL hasNewVersion))completion {
    NSString *version = [[NSUserDefaults standardUserDefaults] objectForKey:kAppUpdateVersion];
    if (version) {
        BOOL canUpdate = [kCurrentVersion compare:version options:NSNumericSearch] == NSOrderedAscending;
        if (completion) {
            completion(canUpdate);
        }
    }
    else {
        [self checkUpdate:^(NSDictionary *result) {
            BOOL canUpdate = NO;
            if (result && result.count > 0) {
                NSString *version = [result objectForKey:@"version"];
                [[NSUserDefaults standardUserDefaults] setObject:version forKey:kAppUpdateVersion];
                [[NSUserDefaults standardUserDefaults] synchronize];
                canUpdate = [kCurrentVersion compare:version options:NSNumericSearch] == NSOrderedAscending;
                
            }
            if (completion) {
                completion(canUpdate);
            }
        }];
    }    
}

@end
