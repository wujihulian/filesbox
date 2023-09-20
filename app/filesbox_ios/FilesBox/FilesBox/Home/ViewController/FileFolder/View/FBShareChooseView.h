//
//  FBShareChooseView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/30.
//

#import <UIKit/UIKit.h>
#import "VULRadarInputView.h"
NS_ASSUME_NONNULL_BEGIN

@interface FBShareChooseView : UIView
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULButton *confirmBtn;
@property (nonatomic, strong) BaseTableView *tableView;
@property (nonatomic, strong) NSMutableArray *listArray;
@property (nonatomic, assign) NSInteger row;
@property (nonatomic, copy) void (^ clickCell)(NSString *title,NSString *onceStr,NSString *timeStr);
@property (nonatomic, strong) VULRadarInputView *inputView;
@property (nonatomic, copy) NSString *onceStr;
@property (nonatomic, copy) NSString *timeStr;

@end

NS_ASSUME_NONNULL_END
