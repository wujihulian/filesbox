//
//  VULActionSheetView.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/6/7.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULActionSheetView : UIView
-(id)initWithFrame:(CGRect)frame actionTitle:(NSArray *)array isCenter:(BOOL)flag;
@property (nonatomic, copy) void (^ confirmBook)(NSInteger index);
@property (nonatomic, copy) void (^ confirmBlock)(NSString *string);

-(void)setHiddenLineWithIndex:(NSInteger)index;
@end

NS_ASSUME_NONNULL_END
