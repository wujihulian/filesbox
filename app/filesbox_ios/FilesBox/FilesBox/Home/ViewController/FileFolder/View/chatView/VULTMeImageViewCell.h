//
//  VULTMeImageViewCell.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/6/1.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "BaseTableViewCell.h"
#import "VULServiceMessageModel.h"
#import "YCMenuView.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULTMeImageViewCell : UITableViewCell
@property (nonatomic, assign) BOOL hideTime;
@property (nonatomic, strong) VULServiceChatMessageModel *model;
@property (nonatomic, copy) void (^ deletCommentWithModel)(VULServiceChatMessageModel *model);

@end

NS_ASSUME_NONNULL_END
