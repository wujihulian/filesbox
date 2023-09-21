//
//  FBFileTopView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/20.
//

#import <UIKit/UIKit.h>
#import "VULFileTierView.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBFileTopView : UIView
@property (nonatomic, strong) NSArray *dataArr;
@property (nonatomic,copy) void (^ clickRightBlock)(BOOL tag);//yes 切换样式 no 是排序
@property (nonatomic,copy) void (^ clickFileNameBlock)(NSInteger tag);
@property (nonatomic, strong) VULButton *leftBtn;
@property (nonatomic,copy) void (^ clickLeftViewBlock)(NSInteger tag);
@property (nonatomic, assign) BOOL  isSort;//yes是递增 n是递减
@property (nonatomic, copy) NSString  *sorTiTle;
@property (nonatomic, assign) BOOL  isClick;//yes排序 n
@property (nonatomic,copy) void (^ changeSortBlock)(NSString *title,BOOL tag);
@property (nonatomic, strong) VULButton *timeBtn;;
@property (nonatomic, strong) VULButton *changeTypeBtn;
@property (nonatomic, strong) VULFileTierView *tierView;
@property (nonatomic,copy) void (^ selectImageWithRow)(NSInteger tag,UIImageView *rightArrow);
@property (nonatomic,copy) void (^ clickInfoTypeBlock)(BOOL tag);//资讯

@end

NS_ASSUME_NONNULL_END
