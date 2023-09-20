//
//  FBFileNodeCell.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/21.
//

#import "BaseTableViewCell.h"
#import "VULFileModel.h"
@class FBFileNodeCell;
@protocol FBFileNodeCellDelegate <NSObject>
- (void)fileNodeCell:(FBFileNodeCell *)cell didTapExpandButton:(UIButton *)expandButton;
- (void)selectCell:(FBFileNodeCell *)cell ;

@end
NS_ASSUME_NONNULL_BEGIN

@interface FBFileNodeCell : BaseTableViewCell
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, strong) VULButton *upOrDownBtn;
@property (nonatomic, weak) id<FBFileNodeCellDelegate> delegate;
@property (nonatomic, strong) VULFileZIPObjectModel *zipModel;
@property (nonatomic, copy) void (^ clickMore)(VULFileZIPObjectModel *zipModel);



@end

NS_ASSUME_NONNULL_END
