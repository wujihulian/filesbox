//
//  FBFromTopView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/18.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBFromTopView : UIView
@property (nonatomic, strong) NSArray *titleArr;
@property (nonatomic, strong) NSArray *iconArr;
@property (nonatomic,copy) void (^ clickViewBlock)(NSString *title);
@property (nonatomic, assign) NSInteger index;//1 是从上面弹出来
@property (nonatomic, assign) NSInteger indexLine;
@property (nonatomic, copy) NSString *selectIndex;
@property (nonatomic,copy) void (^ clickViewWithRowBlock)(NSString *title,NSInteger row);
@property (nonatomic, strong) NSMutableArray *selectArr;
@property (nonatomic, strong) NSMutableArray *lineArr;
@property (nonatomic,strong) NSMutableArray *dataArray;
@property (nonatomic,assign) BOOL isSHow;

@end

NS_ASSUME_NONNULL_END
