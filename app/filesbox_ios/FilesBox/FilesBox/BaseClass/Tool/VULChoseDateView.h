//
//  VULChoseDateView.h
//  VideoULTeacher
//
//  Created by 无极互联 on 2020/5/25.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>
NS_ASSUME_NONNULL_BEGIN

@interface VULChoseDateView : UIView

@property (nonatomic, copy) void(^choseDateBlock)(NSInteger startStamp, NSInteger endStamp);


- (instancetype)initWithStartDate:(NSString *)startDateStr EndDate:(NSString *)endDateStr;

@end

NS_ASSUME_NONNULL_END
