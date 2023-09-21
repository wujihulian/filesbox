//
//  FBFileNodeChooseView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/21.
//

#import <UIKit/UIKit.h>
#import "VULFileModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBFileNodeChooseView : UIView
@property (nonatomic, strong) NSMutableArray *treeNodes;
@property (nonatomic,copy) void (^ backChooseWithModel)(VULFileObjectModel *model);

@end

NS_ASSUME_NONNULL_END
