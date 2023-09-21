//
//  FBHeaderView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/5/10.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBHeaderView : UIView
@property (nonatomic, strong) NSMutableArray *labelArray;
@property (nonatomic,copy) void (^ clickBlock)(NSString *title);
@property (nonatomic,copy) void (^ reloadDataFromUp)();

@end

NS_ASSUME_NONNULL_END
