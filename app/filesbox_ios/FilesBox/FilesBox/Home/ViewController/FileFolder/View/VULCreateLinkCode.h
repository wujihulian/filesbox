//
//  VULCreateLinkCode.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/4/16.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface VULCreateLinkCode : UIView
@property (nonatomic, copy) id imageConent;
@property (nonatomic, copy) NSString *url;
@property (nonatomic, copy) void (^ dismiss)(void);

@end

NS_ASSUME_NONNULL_END
