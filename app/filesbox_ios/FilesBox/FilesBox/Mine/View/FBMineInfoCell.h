//
//  FBMineInfoCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/7/17.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBMineInfoCell : BaseTableViewCell
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *rightLabel;
@property (nonatomic, strong) UIImageView *rightImageV;
@property (nonatomic, strong) UIImageView *iconRight;
@property (nonatomic, strong) VULButton *bingBtn;
@property (nonatomic, copy) void(^clickBtnTitle)(NSString *title,NSString*btnTilte);

@end

NS_ASSUME_NONNULL_END
