//
//  FBTakePhotoVC.h
//  FilesBox
//
//  Created by 无极互联 on 2023/8/8.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface FBTakePhotoVC : VULBaseViewController
@property (nonatomic, copy) void (^uploadImage)(UIImage *image);
@property (nonatomic, copy) NSString *sourceID;


@end

NS_ASSUME_NONNULL_END
