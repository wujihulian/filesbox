//
//  AppDelegate+AppUpdate.h
//  UnlimitedBusiness
//
//  Created by yuekewei on 2021/4/6.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "AppDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface AppDelegate (AppUpdate)

+ (void)checkAndShowUpdateAlert;

+ (void)hasNewVersion:(void(^)(BOOL hasNewVersion))completion;
@end

NS_ASSUME_NONNULL_END
