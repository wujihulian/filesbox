//
//  FBChangeLanguage.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/14.
//

#import "FBChangeLanguage.h"
@interface FBChangeLanguage ()
@property (nonatomic, strong) UILabel *titleLabel;


@end
@implementation FBChangeLanguage
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        self.layer.masksToBounds= YES;
        self.layer.cornerRadius = 5;
        [self setView];
    }
    return self;
}
-(void)setView{
    [self addSubview:self.titleLabel];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.mas_offset(0);
        make.height.mas_equalTo(fontAuto(50));
    }];
    
    NSArray *titleArr = @[@"简体中文",@"English",@"繁體中文",@"日本語",@"Français",@"Español",@"русский"];
    UIView *leftView = nil;
    for(int i= 0 ;i <titleArr.count;i++){
        UIView *aline3 = [UIView new];
        aline3.backgroundColor = [kLineColor colorWithAlphaComponent:0.3];
        [self addSubview:aline3];
        [aline3 mas_makeConstraints:^(MASConstraintMaker *make) {
            if(leftView){
                make.top.mas_equalTo(leftView.mas_bottom);
            }else{
                make.top.mas_equalTo(self.titleLabel.mas_bottom);
            }
            make.left.mas_equalTo(0);
            make.right.mas_equalTo(0);
            make.height.equalTo(@1);
        }];
        UIButton *sender= [VULButton new];
        [sender setTitle:titleArr[i] forState:UIControlStateNormal];
        [sender setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        sender .tag =  100+i;
        [sender addTarget:self action:@selector(clickWithBtn:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:sender];

        [sender mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_offset(0);
            make.top.mas_equalTo(aline3.mas_bottom);
            make.height.mas_equalTo(fontAuto(40));
        }];
        leftView = sender;
    }
 
    [self layoutIfNeeded];
    self.height = leftView.bottom + fontAuto(0);
}
-(UILabel *)titleLabel{
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.text = KLanguage(@"语言设置");
        _titleLabel.textColor = HEXCOLOR(0x333333);
        _titleLabel.textAlignment = NSTextAlignmentCenter;
        _titleLabel.font = [UIFont yk_pingFangSemibold:fontAuto(20)];
    }
    return _titleLabel;
}


-(void)clickWithBtn:(UIButton *)sender{
    
    NSArray *titleArr = @[@"zh-Hans",@"en",@"zh-Hant",@"ja",@"fr",@"es",@"ru"];
    [NSBundle setLanguage:titleArr[sender.tag-100]];
    [[NSUserDefaults standardUserDefaults] setObject:titleArr[sender.tag-100] forKey:@"appLanguage"];
    MJRefreshConfig.defaultConfig.languageCode  = titleArr[sender.tag-100];
    if (self.changeLanguageWithIndex) {
        self.changeLanguageWithIndex(sender.tag-100);
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
