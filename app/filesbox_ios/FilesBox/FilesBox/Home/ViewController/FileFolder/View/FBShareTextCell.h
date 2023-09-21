//
//  FBShareTextCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/29.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBShareTextCell : BaseTableViewCell
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UITextField *codeTextField;
@property (nonatomic, strong)  UIImageView *rightImageV;
@property (nonatomic, strong) UISwitch *openSwitch;

@end

NS_ASSUME_NONNULL_END
