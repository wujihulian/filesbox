//
//  FBFileVersionCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/4/12.
//

#import "BaseTableViewCell.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBFileVersionCell : BaseTableViewCell
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, assign) NSInteger row;
@property (nonatomic, assign) NSInteger version;
@property (nonatomic, copy) void (^ clickMore)(NSInteger row);
@property (nonatomic, strong) UITextField *textField;

@end

NS_ASSUME_NONNULL_END
