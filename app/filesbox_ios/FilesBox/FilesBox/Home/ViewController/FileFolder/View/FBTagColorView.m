//
//  FBTagColorView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import "FBTagColorView.h"

@implementation FBTagColorView
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = UIColor.whiteColor;
        [self setView];
    }
    return self;
}
-(void)setView{
 
self.tagColorList = @[@"rgb(247,247,247)",@"rgb(252,219,217)",@"rgb(253,227,207)",
    @"rgb(255,243,207)",@"rgb(167,225,196)",@"rgb(207,237,240)",
    @"rgb(210,234,251)",@"rgb(228,226,250)",@"rgb(253,216,231)",
    @"rgb(191,191,191)",@"rgb(244,110,101)",@"rgb(247,142,61)",
    @"rgb(255,206,61)",@"rgb(61,189,125)",@"rgb(61,184,193)",
    @"rgb(16,142,233)",@"rgb(148,138,236)",@"rgb(247,98,158)",
    @"rgb(90,90,90)",@"rgb(189,38,54)",@"rgb(185,54,0)",
    @"rgb(193,117,0)",@"rgb(0,123,67)",@"rgb(0,112,127)",
    @"rgb(12,96,170)",@"rgb(83,62,180)",@"rgb(193,28,123)"];
    UIButton *leftBtn = nil;
    for (int i = 0; i < self.tagColorList.count; i ++) {
        UIButton *btn = [UIButton new];
        btn.backgroundColor = getColorWithGgb(self.tagColorList[i]);
        btn.layer.masksToBounds = YES;
        btn.layer.cornerRadius = 10;
        btn.tag = 10000+i;
        [btn addTarget:self action:@selector(clickTagColor:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:btn];
        CGFloat left = (VULSCREEN_WIDTH-9*20)/10;
        [btn mas_makeConstraints:^(MASConstraintMaker *make) {
        
            if (leftBtn) {
                if (i==9 || i==18 ) {
                    make.top.mas_equalTo(leftBtn.mas_bottom).offset(left);
                    make.left.mas_equalTo(left);
                }else{
                    make.centerY.mas_equalTo(leftBtn.mas_centerY);
                    make.left.mas_equalTo(leftBtn.mas_right).offset(left);
                }
            }else{
                make.top.mas_equalTo(left);
                make.left.mas_equalTo(left);
            }
            make.width.height.mas_equalTo(20);
            
        }];
        leftBtn= btn;
    }
}
-(void)clickTagColor:(UIButton *)sender{
    if (self.selectTagColorWithString) {
        self.selectTagColorWithString(self.tagColorList[sender.tag-10000]);
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
