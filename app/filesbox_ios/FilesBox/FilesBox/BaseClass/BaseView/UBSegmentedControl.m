//
//  UBSegmentedControl.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/2/2.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "UBSegmentedControl.h"

@implementation UBSegmentedControl

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.layer.cornerRadius = self.cornerRadius;
    self.layer.masksToBounds = YES;
}

@end
