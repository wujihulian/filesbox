//
//  VULDropDownCell.h
//  VideoULimit
//
//  Created by svnlan on 2019/3/15.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VULDropDownModel.h"
#import "VULDropDownConfig.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULDropDownCell : UITableViewCell

@property (strong, nonatomic) UIImageView *menuImageView;

@property (strong, nonatomic) UILabel *menuTitleLab;
/**
 *  model
 */
@property (nonatomic, strong) VULDropDownModel *menuModel;
/**
 *  相关配置
 */
@property (nonatomic, strong) VULDropDownConfig *zw_menuConfg;
/**
 *  线条颜色
 */
@property (nonatomic, strong) UIColor *lineColor;
/**
 * 最后一栏cell
 */
@property (nonatomic, assign) BOOL isFinalCell;

@end

NS_ASSUME_NONNULL_END
