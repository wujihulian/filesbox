//
//  FBAttributeCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBAttributeCell : BaseTableViewCell

@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *detialLabel;
@property (nonatomic, strong)  UIImageView *userImageV;
@property (nonatomic, strong)  UITextField *textField;
@property (nonatomic, strong)  VULButton *editBtn;

@end

NS_ASSUME_NONNULL_END
