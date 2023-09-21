//
//  WatermarkWindow.m
//  FilesBox
//
//  Created by 无极互联 on 2023/4/25.
//

#import "WatermarkWindow.h"

@implementation WatermarkWindow
- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
        
        // Create and add the watermark view
        self.watermarkView = [[FBWatermarkView alloc] initWithFrame:self.bounds];
        [self addSubview:self.watermarkView];
        [self bringSubviewToFront:self.watermarkView];

    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    // Ensure the watermark view fills the window
    self.watermarkView.frame = self.bounds;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
