//
//  UBWaterWaveButton.h
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/1/9.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UBWaterWaveButton : UIButton

//动画时间，默认为1秒
@property (nonatomic,assign) NSTimeInterval AnimationDuration;
//动画颜色
@property (nonatomic,strong) UIColor        *highlightedColor;

@end

NS_ASSUME_NONNULL_END
