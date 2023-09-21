//
//  VULAiProgressView.h
//  UnlimitedBusiness
//
//  Created by SunTory on 2021/3/22.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULAiProgressView : UIView
- (instancetype)initWithFrame:(CGRect)rec textArray:(NSString *)titleStr;
- (void)setProgressWithValue:(float)value;
@property (nonatomic, strong) UILabel *numberLabel;
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UIView *defultView;
@property (nonatomic, strong) UIView *percentView;
@end

NS_ASSUME_NONNULL_END
