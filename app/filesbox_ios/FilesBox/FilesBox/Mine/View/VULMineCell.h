//
//  VULMineCell.h
//  VideoULTeacher
//
//  Created by yuekewei on 2020/3/24.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULMineCell : BaseTableViewCell

@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *briefLabel;
@property (nonatomic, strong) UIImageView *cellImageView;
@property (nonatomic, strong) UISwitch *switchh;
@property (nonatomic, strong) UIView *line;
@property (nonatomic, strong) UIView *redView;

- (void)hiddenImage;

@end

NS_ASSUME_NONNULL_END
