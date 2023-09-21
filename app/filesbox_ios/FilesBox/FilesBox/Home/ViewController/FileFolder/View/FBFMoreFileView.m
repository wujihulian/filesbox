//
//  FBFMoreFileView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/20.
//

#import "FBFMoreFileView.h"

@implementation FBFMoreFileView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 5;
        self.layer.borderColor =UIColorHex(#d9d9d9).CGColor;
        self.layer.borderWidth =1;

    }
    return self;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
