//
//  VULUpdatePopupView.h
//  VideoULimit
//
//  Created by svnlan on 2020/3/7.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULUpdateTool.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULUpdatePopupView : UIView

- (instancetype)initWithFrame:(CGRect)frame withUpdateInfo:(VULAppstoreInfo *)info;


@property (nonatomic, copy) void (^GoToUpgradeBlock)(void);/**< 立即升级*/

/**
 弹出展示
 */
- (void)showUpdatePopupView;

/**
 关闭
 */
- (void)closeUpdatePopupView;


@end

NS_ASSUME_NONNULL_END
