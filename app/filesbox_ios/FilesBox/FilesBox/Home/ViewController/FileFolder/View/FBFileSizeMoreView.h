//
//  FBFileSizeMoreView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/20.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBFileSizeMoreView : UIView
@property (nonatomic,copy) void (^ clickViewBlock)(NSString *startTime,NSString *endTime);

@end

NS_ASSUME_NONNULL_END
