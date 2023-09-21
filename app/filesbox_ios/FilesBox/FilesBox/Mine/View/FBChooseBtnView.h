//
//  FBChooseBtnView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/7/19.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBChooseBtnView : UIView
@property (nonatomic, retain) NSMutableArray *btnArr;
@property (nonatomic, copy) void (^clickBtn)(NSInteger index);

@end

NS_ASSUME_NONNULL_END
