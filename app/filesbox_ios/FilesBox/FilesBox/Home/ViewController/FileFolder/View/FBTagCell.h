//
//  FBTagCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/31.
//

#import "BaseTableViewCell.h"
#import "FBFileTagView.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBTagCell : BaseTableViewCell
@property (nonatomic, strong) FBFileTagView *bossView;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UIImageView *leftImageV;
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, copy) void (^ gotoTagManageMent)();

@end

NS_ASSUME_NONNULL_END
