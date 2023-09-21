//
//  VULPlayMp3VC.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/9/18.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface VULPlayMp3VC : VULBaseViewController
@property (nonatomic, strong) ZFPlayerController *player;

@property (nonatomic, strong) NSString *mp3Url;
@property (nonatomic, strong) NSNumber *playLength;
@property (nonatomic, strong) NSString *title;

@end

NS_ASSUME_NONNULL_END
