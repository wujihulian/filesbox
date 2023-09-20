//
//  FBTranView.h
//  FilesBox
//
//  Created by 无极互联 on 2023/7/3.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface FBTranView : UIView
@property (nonatomic,strong) VULFileObjectModel  *model;
@property (nonatomic, copy) void (^changeType)(NSString *type);

@end

NS_ASSUME_NONNULL_END
