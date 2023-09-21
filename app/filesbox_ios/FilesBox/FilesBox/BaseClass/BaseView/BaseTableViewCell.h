//
//  BaseTableViewCell.h
//  VideoULTeacher
//
//  Created by yuekewei on 2020/3/21.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface BaseTableViewCell : UITableViewCell

/**
 cell获取实例
 
 @param tableView tableView
 @param reuseIdentifier cell标识符
 @return cell实例
 */
+ (instancetype)dequeueReusableCellWithTableView:(UITableView *)tableView reuseIdentifier:(NSString *)reuseIdentifier;

/**
 cell获取实例
 
 @param tableView tableView
 @param reuseIdentifier cell标识符
 @param cellStyle UITableViewCellStyle
@return cell实例
 */
+ (instancetype)dequeueReusableCellWithTableView:(UITableView *)tableView reuseIdentifier:(NSString *)reuseIdentifier cellStyle:(UITableViewCellStyle)cellStyle;

@property (nonatomic, strong) UIImageView *arrowView;

@property (nonatomic, strong) UIView *separatorLine;

@property (nonatomic, assign) CGFloat separatorLineLeftInset;

@property (nonatomic, assign) CGFloat separatorLineRightInset;
@end

NS_ASSUME_NONNULL_END
