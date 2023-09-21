//
//  FBLogCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/7/19.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBLogCell : BaseTableViewCell
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *titleLabel1;
@property (nonatomic, strong) UILabel *titleLabel2;
@property (nonatomic, strong) UILabel *titleLabel3;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) UIView *lineV;

@end

NS_ASSUME_NONNULL_END
