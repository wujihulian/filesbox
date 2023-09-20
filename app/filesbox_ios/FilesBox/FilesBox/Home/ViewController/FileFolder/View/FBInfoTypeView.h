//
//  FBInfoTypeView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/6/15.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBInfoTypeView : UIView
@property (nonatomic, copy) void (^selectBackModel)(NSString *typeSequence);
@property (nonatomic, copy) void (^selectBlock)(NSString *classifySequence);
@end

NS_ASSUME_NONNULL_END
