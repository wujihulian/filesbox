//
//  FBOperationView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/2/22.
//

#import <UIKit/UIKit.h>
#import "SDImageCache.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULOperationIconCell : UICollectionViewCell
@property (nonatomic,strong) VULLabel *label;
@property (nonatomic,strong) UIImageView *iconImageV;
@end
@interface FBOperationView : UIView

@property (nonatomic,copy) NSString *icon;
@property (nonatomic,assign) BOOL isSHow;

@property (nonatomic,strong) NSMutableArray *allDataArr;
@property (nonatomic,strong) NSMutableArray *dataArray;
@property (nonatomic,strong) UICollectionView *collectionView;
@property (nonatomic,strong) NSMutableArray *tagList;

@property (nonatomic,copy) void (^ dismisssView)(void);
@property (nonatomic,copy) void (^ selectOperationWithTitle)(NSString *title);
@property (nonatomic,copy) void (^ allSelectModel)(BOOL flag);
@property (nonatomic, copy) void (^ selectCollectType)(NSString *block,NSString *tag);
@property (nonatomic, copy) void (^ gotoTagManageMent)();
@property (nonatomic, copy) void (^ getInfoReloadData)();
@property (nonatomic, copy) void (^ changeType)(NSString *type);

@end

NS_ASSUME_NONNULL_END
