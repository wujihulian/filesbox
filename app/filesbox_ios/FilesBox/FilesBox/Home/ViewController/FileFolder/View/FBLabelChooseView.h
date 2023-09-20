//
//  FBLabelChooseView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/20.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBLabelChooseView : UIView
@property (nonatomic, strong) VULLabel *fileTypeConentLabel;
@property (nonatomic, strong) UIImageView *rightImageV;
@property (nonatomic, assign) NSInteger index;// 0文件类型 1时间范围 2文件大小 3用户
@property (nonatomic, assign) NSInteger selectIndex;
@property (nonatomic,copy) void (^ clickViewBlock)(NSString *title);
@property (nonatomic, assign) BOOL isFlag;// yes 是否是自定义 no不是

@end

NS_ASSUME_NONNULL_END
