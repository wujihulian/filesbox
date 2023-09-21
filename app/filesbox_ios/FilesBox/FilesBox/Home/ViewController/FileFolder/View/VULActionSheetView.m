//
//  VULActionSheetView.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/6/7.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULActionSheetView.h"

@interface VULActionSheetView()
@property (nonatomic, strong) NSArray *titleArray;
@property (nonatomic, assign) BOOL  isCenter;
@property (nonatomic, strong) NSMutableArray *lineArr;

@end
@implementation VULActionSheetView
-(id)initWithFrame:(CGRect)frame actionTitle:(NSArray *)array isCenter:(BOOL)flag
{
    self = [super initWithFrame:frame];
    if (self) {
        self.isCenter = flag;
        self.frame = frame;
        self.backgroundColor = [UIColor whiteColor];
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 5;
        _titleArray  =array;
        _lineArr = [NSMutableArray array];
        [self setView];
    }
    return self;
}
- (void)setView{
    UIScrollView *scroller = [UIScrollView new];
   
    for (int i=0; i <_titleArray.count ; i ++) {
        VULButton *btn = [VULButton getCustomBtnWithFrame:CGRectMake((self.width-fontAuto(300))/2, fontAuto(47)*i,fontAuto(300), fontAuto(47)) title:_titleArray[i] Font:fontAuto(16) Bgcolor:[UIColor whiteColor]  Target:self action:@selector(confirmClick:)];
        [btn setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        if (!self.isCenter) {
            btn.titleLabel.textAlignment = NSTextAlignmentLeft;
            btn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
        }
       
        btn.tag = i;
        if (_titleArray.count>=7) {
            [scroller addSubview:btn];

        }else{
            [self addSubview:btn];

        }
        if (i != 0) {
            UIView *lineV = [UIView new];
            lineV.frame = CGRectMake(0, fontAuto(47)*i-1, self.width, 1);
            lineV.backgroundColor = HEXCOLOR(0xf6f6f6);
            if (_titleArray.count>=7) {
                [scroller addSubview:lineV];

            }else{
                [self addSubview:lineV];

            }
            [_lineArr addObject:lineV];
        }
    }
    if (_titleArray.count>=7) {
        scroller.frame =CGRectMake(0, 0, self.width, 7*fontAuto(47));
        scroller.contentSize = CGSizeMake(self.width, fontAuto(47)*_titleArray.count);
        [self addSubview:scroller];
    }
}
- (void)confirmClick:(VULButton *)sender{
    if (self.confirmBook) {
        self.confirmBook(sender.tag);
    }
    if (self.confirmBlock) {
        self.confirmBlock(self.titleArray[sender.tag]);
    }
}
-(void)setHiddenLineWithIndex:(NSInteger)index{
    UIView *line = [self.lineArr objectAtIndex:index];
    line.hidden = YES;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end

