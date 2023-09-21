//
//  VULCustomLabel.m
//  VideoULimit
//
//  Created by yuekewei on 2020/5/27.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULCustomLabel.h"

@implementation VULCustomLabel

- (instancetype)initWithFrame:(CGRect)frame {
    if(self = [super initWithFrame:frame]) {
        [self setup];
    }
    return self;
}

- (void)setup {
    self.edgeInsets = UIEdgeInsetsZero;
}

- (CGRect)textRectForBounds:(CGRect)bounds limitedToNumberOfLines:(NSInteger)numberOfLines {

    CGRect rect = [super textRectForBounds:UIEdgeInsetsInsetRect(bounds,self.edgeInsets) limitedToNumberOfLines:numberOfLines];
    
    rect.origin.x -= self.edgeInsets.left;
    rect.origin.y -= self.edgeInsets.top;
    rect.size.width += self.edgeInsets.left + self.edgeInsets.right;
    rect.size.height += self.edgeInsets.top + self.edgeInsets.bottom;
    return rect;
}

- (void)drawTextInRect:(CGRect)rect {

    [super drawTextInRect:UIEdgeInsetsInsetRect(rect, self.edgeInsets)];
}

@end
