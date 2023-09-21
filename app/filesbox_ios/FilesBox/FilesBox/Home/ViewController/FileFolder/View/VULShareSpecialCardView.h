//
//  VULShareSpecialCardView.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/11/9.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULShareSpecialCardView : UIView
@property (nonatomic, assign) BOOL isNews;
@property (nonatomic, retain) VULLabel *label;


@property (nonatomic, copy) void (^ menuViewBtnClickedBlock)(NSInteger index);
@end

NS_ASSUME_NONNULL_END
