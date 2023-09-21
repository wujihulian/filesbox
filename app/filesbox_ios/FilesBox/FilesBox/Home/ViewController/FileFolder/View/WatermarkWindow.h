//
//  WatermarkWindow.h
//  FilesBox
//
//  Created by 无极互联 on 2023/4/25.
//

#import <UIKit/UIKit.h>
#import "FBWatermarkView.h"
NS_ASSUME_NONNULL_BEGIN

@interface WatermarkWindow : UIWindow
@property (nonatomic, strong) FBWatermarkView *watermarkView;

@end

NS_ASSUME_NONNULL_END
