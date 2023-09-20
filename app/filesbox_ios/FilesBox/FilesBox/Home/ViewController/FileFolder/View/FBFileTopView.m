//
//  FBFileTopView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/20.
//

#import "FBFileTopView.h"
#import "YCMenuView.h"

@interface FBFileTopView ()<UITextFieldDelegate>


@property (nonatomic, strong) UIView *lineV;

@end
@implementation FBFileTopView
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self setView];
        self.isSort = YES;
        self.sorTiTle = KLanguage(@"名称");
    }
    return self;
}
-(void)setView{
    [self addSubview:self.tierView];
    [self addSubview:self.timeBtn];
    [self addSubview:self.changeTypeBtn];
    [self addSubview:self.leftBtn];
    [self addSubview:self.lineV];
//    [self.leftBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(fontAuto(12));
//            make.height.width.mas_equalTo(20);
//            make.centerY.mas_equalTo(self.mas_centerY);
//    }];
    [self.tierView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(0));
        make.top.bottom.mas_equalTo(0);
        make.right.mas_equalTo(-100);
        make.height.mas_equalTo(40);
    }];
    [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.bottom.mas_equalTo(0);
        make.right.mas_equalTo(0);
        make.height.mas_equalTo(1);
        make.top.mas_equalTo(39);
    }];
    [self.leftBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.tierView.mas_centerY);
        make.right.mas_equalTo(-73);
        make.width.height.mas_equalTo(22);
    }];
    [self.timeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.tierView.mas_centerY);
        make.right.mas_equalTo(-44);
        make.width.height.mas_equalTo(22);
    }];
    [self.changeTypeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.tierView.mas_centerY);
        make.right.mas_equalTo(-15);
        make.width.height.mas_equalTo(22);
    }];
//    self.changeTypeBtn.hidden = YES;
    self.tierView.fileTierViewBlock = ^(NSInteger tag) {
        if (self.clickFileNameBlock) {
            self.clickFileNameBlock(tag);
        }
    };
    self.tierView.selectImageWithRow = ^(NSInteger tag, UIImageView * _Nonnull rightArrow) {
        if (self.selectImageWithRow) {
            self.selectImageWithRow(tag,rightArrow);
        }
    };
}
-(void)setDataArr:(NSArray *)dataArr{
    _dataArr = dataArr;
    self.tierView.dataArray =  [NSMutableArray arrayWithArray:dataArr];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(UIView *)lineV{
    if (!_lineV) {
        _lineV = [UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xe5e5e5);
    }
    return _lineV;
    
}
-(VULButton *)timeBtn{
    if (!_timeBtn) {
        _timeBtn = [VULButton new];
        [_timeBtn setImage:VULGetImage(@"icon_block") forState:UIControlStateNormal];
//        [_timeBtn setImage:VULGetImage(@"icon_new_sort_up") forState:UIControlStateSelected];
        [_timeBtn addTarget:self action:@selector(clickTimeBtnTypeBtn:) forControlEvents:UIControlEventTouchUpInside];

    }
    return _timeBtn;
}
-(void)clickTimeBtnTypeBtn:(VULButton *)sender{
    if (self.clickRightBlock) {
        self.clickRightBlock(YES);
    }
}
-(VULButton *)changeTypeBtn{
    if (!_changeTypeBtn) {
        _changeTypeBtn = [VULButton new];
        [_changeTypeBtn setImage:VULGetImage(@"icon_list") forState:UIControlStateNormal];
//        [_changeTypeBtn setImage:VULGetImage(@"icon_list") forState:UIControlStateSelected];
        [_changeTypeBtn addTarget:self action:@selector(clickChangeTypeBtn:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _changeTypeBtn;
}
-(VULButton *)leftBtn{
    if (!_leftBtn) {
        _leftBtn = [VULButton new];
        [_leftBtn setImage:VULGetImage(@"icon_new_sort_up") forState:UIControlStateNormal];
        [_leftBtn setImage:VULGetImage(@"icon_new_sort") forState:UIControlStateSelected];

        [_leftBtn addTarget:self action:@selector(clickleftBtn:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _leftBtn;
}
-(void)clickleftBtn:(VULButton *)sender{
    if([sender.imageView.image isEqual:VULGetImage(@"icon_screen")]){
        if(self.clickInfoTypeBlock)
        {
            self.clickInfoTypeBlock(YES);
        }
        return;
    }
    NSArray *  titleArr = @[KLanguage(@"名称"),KLanguage(@"类型"),KLanguage(@"大小"),KLanguage(@"修改时间"),KLanguage(@"递增"),KLanguage(@"递减")];

    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (NSInteger i = 0; i < titleArr.count; i++) {
        NSString *title1 = titleArr[i] ;
        UIImage *rightImage = nil;
        UIImage *image = nil;
        if ([self.sorTiTle isEqualToString:title1]) {
            image = VULGetImage(@"icon_choose_right");
        }
        if (self.isSort) {
            if([title1 isEqualToString:KLanguage(@"递增")] ){
                image = VULGetImage(@"icon_choose_right");

            }
        }else{
            if([title1 isEqualToString:KLanguage(@"递减")] ){
                image = VULGetImage(@"icon_choose_right");
            }
        }
        
        
        YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:image rightImage:rightImage handler:^(YCMenuAction *action) {

            
        
            self.isClick = YES;
            if ([action.title isEqualToString:KLanguage(@"递增")] || [action.title isEqualToString:KLanguage(@"递减")]) {
                self.isSort  = [action.title isEqualToString:KLanguage(@"递增")];
                self.leftBtn.selected = !self.isSort;
            }else{
                self.sorTiTle  = action.title;
            }
            if (self.changeSortBlock) {
                self.changeSortBlock(self.sorTiTle,self.isSort);
            }
        }];
        [array addObject:action];
    }
    NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
    CGFloat width = 200;
      if ([appLanguage isEqualToString:@"en"]) {
          width =220;
      }
    YCMenuView *menuView = [YCMenuView menuWithActions:array width:width relyonView:sender];
    menuView.menuColor = [UIColor whiteColor];
    menuView.separatorIndexArray = @[@(3)];

    menuView.separatorColor = [UIColor redColor];
    menuView.maxDisplayCount = 20;
    menuView.cornerRaius = 0;
    //menuView.offset = 150;
    menuView.textColor = UIColorHex(333333);
    menuView.textFont = [UIFont yk_pingFangRegular:14];
    menuView.menuCellHeight = 35;
    menuView.dismissOnselected = YES;
    menuView.dismissOnTouchOutside = YES;
    menuView.backgroundColor = [UIColor whiteColor];
    [menuView show];
}
-(void)clickChangeTypeBtn:(VULButton *)sender{
    if (self.clickRightBlock) {
        self.clickRightBlock(NO);
    }
}

-(VULFileTierView *)tierView{
    if (!_tierView) {
        _tierView = [[VULFileTierView alloc] init];
    }
    return _tierView;
}
@end
