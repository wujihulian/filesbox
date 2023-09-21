//
//  VULUpdateTool.h
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <StoreKit/StoreKit.h>

#ifndef VULUserDefaults
#define VULUserDefaults [NSUserDefaults standardUserDefaults]
#endif


@interface VULAppstoreInfo : NSObject

@property (nonatomic, copy) NSString *version;
@property (nonatomic, copy) NSString *releaseNotes;

- (instancetype)initWithDictionary:(NSDictionary*)dictionary;

@end



@interface VULUpdateTool : NSObject
@property (nonatomic, copy) NSString *appleID;
@property (nonatomic, copy) NSString *curAppVersion;
@property (nonatomic, assign) NSInteger remindDay;//选择稍后提醒时，距离下次提醒时间间隔。默认3天
@property (nonatomic, assign) BOOL isUpdate;
@property (nonatomic, weak) UIViewController *controller;//当需要更新时，会弹出alertController进行提示。因此需要提供一个展示的controller

+ (VULUpdateTool *)shareInstance;

- (void)begin;

- (BOOL)isUpdateBool;


@end
