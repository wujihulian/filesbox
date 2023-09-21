//
//  FBHomeViewController.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/16.
//

#import "VULBaseViewController.h"
#import "SDImageCache.h"
#import "VULActionSheetView.h"
#import <CoreLocation/CoreLocation.h>
#import "FBNewHtmlView.h"
NS_ASSUME_NONNULL_BEGIN

@interface FBHomeViewController : VULBaseViewController
@property (nonatomic, assign) BOOL flag;//yes 是组织
@property (nonatomic, assign) BOOL isMove;//是否移动 复制 剪切用
@property (nonatomic, assign) BOOL isHome;//是否是主页
@property (nonatomic, copy) NSString *icon;//文件类型
@property (nonatomic, copy) NSString *operation;//操作类型

@property (nonatomic, copy) NSMutableArray *changeArray;//复制对象
@property (nonatomic,copy) NSString  *fileType;
@property (nonatomic,retain) VULFileZIPObjectModel  *isZipModel;
@property (nonatomic, copy) void (^ selectFolder)(NSString *path,NSString*sourceID);
@property (nonatomic, strong) UISwitch *switchh;
@property (nonatomic, assign) BOOL isSelect;//选择图片
@property (nonatomic, copy) void (^ selectModel)(VULFileObjectModel *model);


@end

NS_ASSUME_NONNULL_END
