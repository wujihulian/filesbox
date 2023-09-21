//
//  VULDropDownView.h
//  VideoULimit
//
//  Created by svnlan on 2019/3/15.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULDropDownModel.h"

NS_ASSUME_NONNULL_BEGIN

typedef void(^BlockSelectedMenu)(NSInteger menuRow);
typedef void(^CancelBlockSelected)(void);


@interface VULDropDownView : UIView

@property (nonatomic, copy) BlockSelectedMenu blockSelectedMenu;
@property (nonatomic, copy) CancelBlockSelected cancelBlockSelected;

/**
 *  cel高度
 */
@property (nonatomic, assign) CGFloat menuCellHeight;

/**
 *  文字
 */
@property (nonatomic, copy) NSArray *titleArray;
/**
 *  图片
 */
@property (nonatomic, copy) NSArray *imageArray;

/**
 *  图文Model
 */
@property (nonatomic, copy) NSArray<VULDropDownModel *> *menuArray;

+ (instancetype)pullMenuAnchorView:(UIView *)anchorView titleArray:(nullable NSArray *)titleArray imageArray:(nullable NSArray *)imageArray;


+ (instancetype)pullMenuAnchorPoint:(CGPoint)anchorPoint titleArray:(nullable NSArray *)titleArray imageArray:(nullable NSArray *)imageArray;

+ (instancetype)pullMenuAnchorView:(UIView *)anchorView titleArray:(nullable NSArray *)titleArray defaultIndex:(NSInteger)defaultIndex;

@end

NS_ASSUME_NONNULL_END
