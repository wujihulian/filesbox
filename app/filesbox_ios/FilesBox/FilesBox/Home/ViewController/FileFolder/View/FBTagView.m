//
//  FBTagView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/17.
//

#import "FBTagView.h"

@implementation FBTagView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        
        
    }
    return self;
}
-(void)addTagWithColorArr:(NSArray *)arr{
    [self removeAllSubviews];
    UIView *defaultView = nil;
    for (int i= 0; i<arr.count;i++) {
        UIView *colorView = [UIView new];
        colorView.backgroundColor = arr[i];
        colorView.layer.masksToBounds = YES;
        colorView.layer.cornerRadius = 6;
        colorView.layer.borderColor = UIColor.whiteColor.CGColor;
        colorView.layer.borderWidth = 1;
        [self addSubview:colorView];
        [colorView mas_makeConstraints:^(MASConstraintMaker *make) {
            if (defaultView) {
                make.left.mas_equalTo(defaultView.mas_right).offset(-6);

            }else{
                make.left.mas_equalTo(0);
            }
            make.top.mas_equalTo(0);
            make.width.height.mas_equalTo(12);
        }];
        defaultView = colorView;
    }
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
