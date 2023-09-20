//
//  VULDocumentVC.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/9/18.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULDocumentVC : VULBaseViewController
@property (nonatomic, strong) NSString *pptPreviewUrl;
@property (nonatomic, strong) NSNumber *playLength;
@property (nonatomic, strong) NSString *title;
@property (nonatomic, assign) BOOL  isZip ;

@end

NS_ASSUME_NONNULL_END
