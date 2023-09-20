//
//  VULAiProgressView.m
//  UnlimitedBusiness
//
//  Created by SunTory on 2021/3/22.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULAiProgressView.h"
@interface VULAiProgressView()

@property (nonatomic, strong) NSString *title;

@end
@implementation VULAiProgressView
- (instancetype)initWithFrame:(CGRect)rec textArray:(NSString *)titleStr{
    self = [super initWithFrame:rec];
    if(self) {
        _title =titleStr;
        [self setView];
    }
    return  self;
}
- (void)setView{
    
    _numberLabel = [[UILabel alloc] init];
    _numberLabel.frame = CGRectMake(fontAuto(3), 0, fontAuto(16), fontAuto(12));
    _numberLabel.font = [UIFont yk_pingFangMedium:15];
    _numberLabel.textColor = HEXCOLOR(0x999999);
    [self addSubview:_numberLabel];

    _titleLabel = [[UILabel alloc] init];
    _titleLabel.frame = CGRectMake(fontAuto(3), 0,  self.size.width-FontAuto(36), fontAuto(12));
    _titleLabel.font = [UIFont yk_pingFangRegular:12];
    _titleLabel.textColor = HEXCOLOR(0x999999);
    _titleLabel.text =_title;
    [self addSubview:_titleLabel];
    
    _defultView = [[UIView alloc] init];
    _defultView.frame  = CGRectMake(0, _numberLabel.bottom+fontAuto(8), self.size.width, fontAuto(10));
    _defultView.backgroundColor = HEXCOLOR(0xF0F0F0);
    _defultView.layer.masksToBounds = YES;
    _defultView.layer.cornerRadius = fontAuto(10)/2;
    [self addSubview:_defultView];
    
    _percentView = [[UIView alloc] init];
    _percentView.backgroundColor = HEXCOLOR(0xFFD234);
    _percentView.layer.masksToBounds = YES;
    _percentView.layer.cornerRadius = fontAuto(10)/2;
    _percentView.frame  = CGRectMake(0,_numberLabel.bottom+fontAuto(8), self.size.width*0, fontAuto(10));
     [self addSubview:_percentView];
    
    
}
- (void)setProgressWithValue:(float)value{
//    动画从左到右加大
    
//    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"bounds"];
//    animation.duration = 3;
//    animation.fromValue = [NSValue valueWithCGRect:CGRectMake(0, 0, self.size.width*0, fontAuto(10))];
//    animation.toValue = [NSValue valueWithCGRect:CGRectMake(0, 0, self.size.width*value, fontAuto(10))];
//    animation.speed = 2;
//    //    设置动画在完成的时候 固定在完成的状态
//    //    这个属性 必须把remocedOnCompletion 设置成NO 这个属性 才可以效果
//        animation.removedOnCompletion = NO;
//        animation.fillMode = kCAFillModeBoth;
//    animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseOut];
//    self.percentView.layer.anchorPoint = CGPointZero;
    _percentView.frame  = CGRectMake(0, _numberLabel.bottom+fontAuto(8), self.size.width*value, fontAuto(10));
//    [self.percentView.layer addAnimation:animation forKey:@""];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
