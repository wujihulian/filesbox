//
//  FBNavgationOperateView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/18.
//

#import <UIKit/UIKit.h>
#import "FBMessageView.h"
#import "FBFileProgress.h"
#import "CHDWaveView.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBNavgationOperateView : UIView
@property (nonatomic,copy) void (^ clickLeftViewBlock)(NSInteger tag);
@property (nonatomic, strong) FBMessageView *trashBtn;
@property (nonatomic, strong) FBMessageView *cloudBtn;
@property (nonatomic, strong) FBMessageView *messageBtn;
@property (nonatomic, strong) UITextField *searchField;
//@property (nonatomic, strong) CHDWaveView *progress;
@property (nonatomic, strong) VULButton *leftBtn;
@property (nonatomic,copy) void (^ clickBlock)(NSString *title);
@property (nonatomic,copy) void (^ searchWtihTextField)(NSString *text);
@property (nonatomic,copy) void (^ reloadDataFromUp)();
@property (nonatomic,copy) void (^ clickSearch)(BOOL flag);
@property (nonatomic, strong) VULButton *moreSearchBtn;
@property (nonatomic, assign) BOOL  isBlock;
@property (nonatomic,copy) void (^ clickRightBlock)(BOOL tag);//yes 切换样式 no 是排序
@property (nonatomic, assign) BOOL  isSort;//yes是递增 n是递减
@property (nonatomic, copy) NSString  *sorTiTle;
@property (nonatomic,copy) void (^ changeSortBlock)(NSString *title,BOOL tag);
@property (nonatomic, assign) BOOL  isClick;//yes排序 n
//@property (nonatomic, strong) VULLabel *sizeAllLabel;

@end

NS_ASSUME_NONNULL_END
