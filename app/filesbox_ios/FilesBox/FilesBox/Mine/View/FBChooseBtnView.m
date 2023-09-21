//
//  FBChooseBtnView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/19.
//

#import "FBChooseBtnView.h"

@implementation FBChooseBtnView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        [self createUI];
    }
    return self;
}
-(void)createUI{
    NSArray *titleArr = @[KLanguage(@"今天"),KLanguage(@"昨天"),KLanguage(@"近7天"),KLanguage(@"近30天"),KLanguage(@"全部"),KLanguage(@"自定义")];
    UIView *leftView = nil;
    self.btnArr = [NSMutableArray array];
    CGFloat left = (VULSCREEN_WIDTH - fontAuto(300))/7;
    for(int i= 0;i<titleArr.count;i++){
        UIButton *btn = [UIButton new];
        [btn setTitle:titleArr[i] forState:UIControlStateNormal];
        [btn setTitleColor:HEXCOLOR(0x777777) forState:UIControlStateNormal];
        btn.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(12)];
        btn.layer.borderColor = HEXCOLOR(0xF1F1F1).CGColor;
        ;
        btn.layer.borderWidth = 1;
        btn.tag = 100+i;
        [btn addTarget:self action:@selector(clickBtn:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:btn];
        [btn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            if(leftView){
                make.left.mas_equalTo(leftView.mas_right).offset(left);
            }else{
                make.left.mas_equalTo(left);

            }
            make.height.mas_equalTo(fontAuto(26));
            make.width.mas_equalTo(fontAuto(50));
        }];
        [self.btnArr addObject:btn];
        leftView = btn;
    }
    [self selectBtnWithIndex:2];
}
-(void)selectBtnWithIndex:(NSInteger )index{
    
    for(int i =0;i<self.btnArr.count;i++){
        UIButton *btn = self.btnArr[i];
        if(index == i){
            [btn setTitleColor:BtnColor forState:UIControlStateNormal];
            btn.layer.borderColor = BtnColor.CGColor;
            ;
            btn.layer.borderWidth = 1;
        }else{
            [btn setTitleColor:HEXCOLOR(0x777777) forState:UIControlStateNormal];
            btn.layer.borderColor = HEXCOLOR(0xF1F1F1).CGColor;
            ;
            btn.layer.borderWidth = 1;
        }
    }
}
-(void)clickBtn:(UIButton *)sender{
    [self selectBtnWithIndex:sender.tag-100];
    if(self.clickBtn){
        self.clickBtn(sender.tag-100);
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
