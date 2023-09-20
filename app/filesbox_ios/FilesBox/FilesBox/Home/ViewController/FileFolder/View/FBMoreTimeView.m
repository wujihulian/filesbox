//
//  FBMoreTimeView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/20.
//

#import "FBMoreTimeView.h"
#import "VULChoseDateView.h"
@interface FBMoreTimeView ()
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) NSString *startDateStr;
@property (nonatomic, strong) NSString *endDateStr;
@property (nonatomic, strong) UIImageView *imageV;

@end
@implementation FBMoreTimeView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 5;
        self.layer.borderColor =UIColorHex(#d9d9d9).CGColor;
        self.layer.borderWidth =1;
        [self addSubview:self.titleLabel];
        [self addSubview:self.imageV];

        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(10));
            make.top.bottom.mas_equalTo(0);
        }];
        
        [self.imageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(10));
            make.centerY.mas_equalTo(self.mas_centerY);
        }];
        NSDate *lastMonthDate = [[NSDate date] dateBySubtractingMonths:1];
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"yyyy-MM-dd"];
        self.startDateStr = [formatter stringFromDate:[NSDate date]];
        self.endDateStr = [formatter stringFromDate:[NSDate date]];
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickMe)];
        [self addGestureRecognizer:tap];
    }
    return self;
}
-(void)clickMe{
    self.layer.borderColor =UIColorHex(#722ed1).CGColor;
    self.titleLabel.textColor = HEXCOLOR(0x999999);
    NSString *startStr = [NSString stringWithFormat:@"%ld",[NSDate getIntervalByTime:self.startDateStr withFomat:@"yyyy-MM-dd"]];
    NSString *endStr = [NSString stringWithFormat:@"%ld",[NSDate getIntervalByTime:self.endDateStr withFomat:@"yyyy-MM-dd"]];
    WeakSelf(self);

    VULChoseDateView *choseDateView = [[VULChoseDateView alloc] initWithStartDate:startStr EndDate:endStr];
    zhPopupController *popup = [[zhPopupController alloc] initWithView:choseDateView size:choseDateView.size];
    popup.layoutType = zhPopupLayoutTypeBottom;
    popup.presentationStyle = zhPopupSlideStyleFromBottom;
    popup.maskAlpha = 0.35;
    [popup showInView:kWindow duration:0.15 delay:0 options:UIViewAnimationOptionCurveEaseInOut bounced:NO completion:nil];
    
    choseDateView.choseDateBlock = ^(NSInteger startStamp, NSInteger endStamp) {
        [popup dismiss];
        if (endStamp<startStamp) {
            [kWindow makeToast:KLanguage(@"结束时间不能小于开始时间")];
            return;
        }
        weakself.startDateStr = [NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",startStamp] Format:@"yyyy-MM-dd"];
        weakself.endDateStr = [NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",endStamp] Format:@"yyyy-MM-dd"];
        
        NSString *choseDateStr = [NSString stringWithFormat:@"%@ ~ %@",weakself.startDateStr,  weakself.endDateStr ];
        self.titleLabel.text = choseDateStr;
        if (self.clickViewBlock) {
            self.clickViewBlock(   weakself.startDateStr,   weakself.endDateStr );
        }
           
    };
    popup.didDismissBlock = ^(zhPopupController * _Nonnull popupController) {
        self.layer.borderColor =UIColorHex(#d9d9d9).CGColor;
       self.titleLabel.textColor = HEXCOLOR(0x333333);
    };
}
-(UIImageView *)imageV{
    if (!_imageV) {
        _imageV = [UIImageView new];
        _imageV.image = VULGetImage(@"icon_calendar");
        
    }
    return _imageV;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"日期选择") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
//        1px solid #d9d9d9
 

    }
    return _titleLabel;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
