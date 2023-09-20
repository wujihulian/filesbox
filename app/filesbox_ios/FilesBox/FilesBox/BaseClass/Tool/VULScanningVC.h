//
//  VULScanningVC.h
//  VideoULimit
//
//  Created by svnlan on 2019/8/20.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULScanningVC : VULBaseViewController

@property (nonatomic, copy) void(^scanOutQRCodeBlock)(NSString *scanQRString);

@end

NS_ASSUME_NONNULL_END
