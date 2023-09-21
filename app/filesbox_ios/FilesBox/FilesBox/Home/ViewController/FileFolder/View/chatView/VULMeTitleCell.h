//
//  VULMeTitleCell.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/6/1.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "BaseTableViewCell.h"
#import "VULServiceMessageModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULMeTitleCell : UITableViewCell
@property (nonatomic, assign) BOOL hideTime;
@property (nonatomic, strong) VULServiceChatMessageModel *model;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) VULSvgImageView *iconImageView;
@property (nonatomic, strong) UIView *bgView;

@property (nonatomic, strong) YYLabel *commentLabel;
@property (nonatomic, copy) void (^ deletCommentWithModel)(VULServiceChatMessageModel *model);

@end

NS_ASSUME_NONNULL_END
