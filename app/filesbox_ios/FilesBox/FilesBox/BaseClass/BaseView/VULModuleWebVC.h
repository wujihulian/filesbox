//
//  VULModuleWebVC.h
//  VideoULimit
//
//  Created by svnlan on 2019/3/26.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULBaseWebViewVC.h"
#import "UBBrochureModel.h"
#import "VULhiringModel.h"
#import "VULCePingModel.h"
//#import "VULBaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface VULModuleWebVC : VULBaseWebViewVC
@property (nonatomic, strong) NSString *naviTitle;
@property (nonatomic, copy) void(^backBlock)(void);
@property (nonatomic, copy) void(^backMessBlock)(void);

@property (nonatomic, strong) NSDictionary *sourceDic; ///线索来源
@property (nonatomic, assign) BOOL showShare;
@property (nonatomic, assign) BOOL isCourseware;

@property (nonatomic, assign) BOOL folderTitle;

@property (nonatomic, assign) BOOL isGoin;
@property (nonatomic, assign) BOOL isUser;



@property (nonatomic, strong) NSString *busTypeNew;
@property (nonatomic, strong) NSString *busIdNew;
@property (nonatomic, strong) NSString *infoType;
@property (nonatomic, strong) NSString *formId;


@property (nonatomic, strong) VULCourseListModel *courseWareModel;

// 返回
@property (nonatomic, assign) BOOL isBack;

@property (nonatomic, copy) void(^photoSucessBlock)(NSString *photoDic);
@property (nonatomic, copy) void(^contentIdBack)(NSDictionary *dic);
@property (nonatomic, assign) BOOL association;


@end

@interface WeakScriptMessageDelegate : NSObject<WKScriptMessageHandler>

@property (nonatomic, weak) id<WKScriptMessageHandler> scriptDelegate;

- (instancetype)initWithDelegate:(id<WKScriptMessageHandler>)scriptDelegate;

@end

NS_ASSUME_NONNULL_END
