//
//  FBFileListTagView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import <UIKit/UIKit.h>
#import "FBFileTagView.h"
NS_ASSUME_NONNULL_BEGIN

@interface FBFileListTagView : UIView
@property (nonatomic, strong) FBFileTagView *bossView;
@property (nonatomic,strong) NSMutableArray *tagList;
@property (nonatomic, strong) VULFileObjectModel *model;
@property (nonatomic, copy) void (^ selectCollectType)(NSString *block,NSString *tag);
@property (nonatomic, copy) void (^ gotoTagManageMent)();
@end

NS_ASSUME_NONNULL_END
