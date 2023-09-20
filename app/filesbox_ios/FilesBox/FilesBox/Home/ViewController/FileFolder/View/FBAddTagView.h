//
//  FBAddTagView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import <UIKit/UIKit.h>
#import "FBFileTagView.h"
NS_ASSUME_NONNULL_BEGIN

@interface FBAddTagView : UIView
@property (nonatomic, strong) FBTagModel *model;
@property (nonatomic, copy) NSString *colorStr;
@property (nonatomic, copy) void (^ addOrEditTag)(NSString *rgbString,NSString *labelName,NSString *labelId);
@property (nonatomic, copy) void (^ dismiss)();

@end

NS_ASSUME_NONNULL_END
