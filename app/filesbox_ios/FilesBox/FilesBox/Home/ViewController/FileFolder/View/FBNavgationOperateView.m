//
//  FBNavgationOperateView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/18.
//

#import "FBNavgationOperateView.h"
#import "FBFileProgress.h"
#import "FBMessageView.h"
#import "YCMenuView.h"
#import "FBHomeViewController.h"
#import "FBDownOrUploadVC.h"
#import "FBFromTopView.h"
#import "FBFileMessageViewController.h"
@interface FBNavgationOperateView ()<UITextFieldDelegate>

@property (nonatomic, strong) VULButton *moreBtn;
//@property (nonatomic, strong) VULLabel *sizeLabel;

@end
@implementation FBNavgationOperateView
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.userInteractionEnabled = YES;
        [self setView];
    }
    return self;
}
-(void)setView{
 
//    [self addSubview:self.progress];
//    [self addSubview:self.sizeLabel];
//    [self addSubview:self.sizeAllLabel];
    [self addSubview:self.leftBtn];
    [self addSubview:self.searchField];
//    [self addSubview:self.trashBtn];
//    [self addSubview:self.cloudBtn];
//    [self addSubview:self.messageBtn];
//    [self addSubview:self.moreBtn];
    [self addSubview:self.moreSearchBtn];

    CGFloat width = VULSCREEN_WIDTH - fontAuto(12+130+82);
    CGFloat width1 = VULSCREEN_WIDTH-width-fontAuto(135)-40-fontAuto(3);

    [self.leftBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(3));
        make.width.height.mas_equalTo(40);
        make.top.mas_equalTo(0);
    }];
//    [self.sizeAllLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.left.mas_equalTo(self.progress.mas_right).offset(fontAuto(5));
//        make.width.mas_equalTo(fontAuto(85)-40);
//        make.top.mas_equalTo(self.sizeLabel.mas_bottom);
//        make.height.mas_equalTo(15);
//    }];
    [self.searchField mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.leftBtn.mas_right).offset(fontAuto(10));
        make.right.mas_equalTo(-fontAuto(12));
        make.height.mas_equalTo(30);
        make.centerY.mas_equalTo(self.leftBtn.mas_centerY);

    }];
    [self.moreSearchBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.searchField.mas_right).offset(-2);
        make.height.mas_equalTo(30);
        make.centerY.mas_equalTo(self.searchField.mas_centerY);
    }];
//    [self.trashBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.left.mas_equalTo(self.searchField.mas_right).offset(fontAuto(10));
//        make.width.mas_equalTo(fontAuto(25));
//        make.height.mas_equalTo(25);
//        make.centerY.mas_equalTo(self.leftBtn.mas_centerY);
//    }];
//    [self.cloudBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.left.mas_equalTo(self.trashBtn.mas_right).offset(fontAuto(5));
//        make.width.mas_equalTo(fontAuto(25));
//        make.height.mas_equalTo(25);
//        make.centerY.mas_equalTo(self.leftBtn.mas_centerY);
//    }];
//    [self.messageBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.left.mas_equalTo(self.cloudBtn.mas_right).offset(fontAuto(5));
//        make.width.mas_equalTo(fontAuto(25));
//        make.height.mas_equalTo(25);
//        make.centerY.mas_equalTo(self.leftBtn .mas_centerY);
//    }];
//    [self.moreBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.right.mas_equalTo(-fontAuto(10));
//        make.width.mas_equalTo(fontAuto(20));
//        make.height.mas_equalTo(25);
//        make.centerY.mas_equalTo(self.leftBtn .mas_centerY);
//    }];
//    NSString *sizeMax = [[NSUserDefaults standardUserDefaults] objectForKey:@"sizeMax"];
//    NSString *sizeUse = [[NSUserDefaults standardUserDefaults] objectForKey:@"sizeUse"];
//
//    if (sizeMax.intValue > 0) {
//        self.sizeLabel.text = [self formattedFileSize:sizeUse.longLongValue];
//        self.sizeAllLabel.text = [NSString stringWithFormat:@"%@ GB",sizeMax];
//
//        CGFloat pre = sizeUse.longLongValue/ (sizeMax.longLongValue* 1024.0 * 1024.0 * 1024.0);
//        self.progress.progress =pre>0.01?pre:0.001;
//
//    }else{
//
//        self.sizeLabel.text = [self formattedFileSize:sizeUse.longLongValue];
//        self.sizeAllLabel.text = KLanguage(@"无限制");
//        self.progress.progress =0.5;
//        self.progress.progressLabel.hidden = YES;
//
//    }
    self.isBlock = YES;
    self.isSort = YES;
    self.sorTiTle = KLanguage(@"名称");

    [self layoutIfNeeded];


}
- (NSString *)formattedFileSize:(long long)bytes {
    CGFloat convertedValue;
    NSString *sizeSuffix;
    
    if (bytes >= 1024*1024*1024) {
        convertedValue = bytes / (1024.0 * 1024.0 * 1024.0);
        sizeSuffix = @"GB";
    } else {
        convertedValue = bytes / (1024.0 * 1024.0);
        sizeSuffix = @"MB";
    }
    
    NSString *result = [NSString stringWithFormat:@"%.2f %@", convertedValue, sizeSuffix];
    return result;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(VULButton *)leftBtn{
    if (!_leftBtn) {
        _leftBtn = [VULButton new];
        [_leftBtn setImage:VULGetImage(@"left_align_ment") forState:UIControlStateNormal];
        [_leftBtn addTarget:self action:@selector(clickleftBtn:) forControlEvents:UIControlEventTouchUpInside];

    }
    return _leftBtn;
}
-(void)clickleftBtn:(VULButton *)btn{
    if (self.clickLeftViewBlock) {
        self.clickLeftViewBlock(0);
    }
}
//- (VULLabel *)sizeLabel {
//    if (!_sizeLabel) {
//        _sizeLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:9] Color:UIColorHex(#ffffff) BgColor:nil];
//    }
//    return _sizeLabel;
//}
//- (VULLabel *)sizeAllLabel {
//    if (!_sizeAllLabel) {
//        _sizeAllLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:9] Color:UIColorHex(#ffffff) BgColor:nil];
//    }
//    return _sizeAllLabel;
//}
//-(CHDWaveView *)progress{
//    if (!_progress) {
//        _progress = [[CHDWaveView alloc]initWithFrame:CGRectMake(fontAuto(12), 5, 30, 30)];
//        _progress.firstWaveColor = [UIColor.whiteColor colorWithAlphaComponent:0.4];
//        _progress.secondWaveColor = [UIColor.whiteColor colorWithAlphaComponent:0.6] ;
//        _progress.waveHeight = 4;
//        _progress.speed = 0.8;
//    }
//    return _progress;
//}
-(FBMessageView *)trashBtn{
    if (!_trashBtn) {
        _trashBtn = [[FBMessageView alloc] initWithFrame:CGRectZero];
        _trashBtn.iconName = VULGetImage(@"icon_trash");
        _trashBtn.messageCount = @"0";
        _trashBtn.userInteractionEnabled = YES;
        UITapGestureRecognizer *sender = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickTrashBtn)];
        [_trashBtn addGestureRecognizer:sender];

//        [_trashBtn addTarget:self action:@selector(clickTrashBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _trashBtn;
}

-(void)clickTrashBtn{
    if (self.clickSearch) {
        self.clickSearch(NO);
    }
    FBHomeViewController *vc = [FBHomeViewController new];
    vc.isHome = YES;
    vc.icon = @"recycle";
    [self.viewController.navigationController pushViewController:vc animated:YES];
}
-(FBMessageView *)cloudBtn{
    if (!_cloudBtn) {
        _cloudBtn = [[FBMessageView alloc] initWithFrame:CGRectZero];
        _cloudBtn.iconName = VULGetImage(@"icon_cloud");
        UITapGestureRecognizer *sender = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickCloudBtn)];
        [_cloudBtn addGestureRecognizer:sender];

    }
    return _cloudBtn;
}
-(void)clickCloudBtn{
    if (self.clickSearch) {
        self.clickSearch(NO);
    }
    FBDownOrUploadVC *vc = [FBDownOrUploadVC new];
    vc.saveAndRefreshBlock = ^{
        if (self.reloadDataFromUp) {
            self.reloadDataFromUp();
        }
    };
    [self.viewController.navigationController pushViewController:vc animated:YES];

}
-(FBMessageView *)messageBtn{
    if (!_messageBtn) {
        _messageBtn = [[FBMessageView alloc] initWithFrame:CGRectZero];
        _messageBtn.iconName = VULGetImage(@"icon_message");
        _messageBtn.messageCount = @"0";
        UITapGestureRecognizer *sender = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickMessageBtn)];
        [_messageBtn addGestureRecognizer:sender];
    }
    return _messageBtn;
}
-(void)clickMessageBtn{
    if (self.clickSearch) {
        self.clickSearch(NO);
    }
    FBFileMessageViewController *vc = [FBFileMessageViewController new];
    [self.viewController.navigationController pushViewController:vc animated:YES];
}
-(VULButton *)moreBtn{
    if (!_moreBtn) {
        _moreBtn = [VULButton new];
        [_moreBtn setImage:VULGetImage(@"icon_more") forState:UIControlStateNormal];
        [_moreBtn addTarget:self action:@selector(clickMoreBtn:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _moreBtn;
}
-(void)clickMoreBtn:(VULButton *)sender{
    if (self.clickSearch) {
        self.clickSearch(NO);
    }
    NSArray *titleArr = @[KLanguage(@"收藏夹"), KLanguage(@"我分享的"), KLanguage(@"最近打开的"), KLanguage(@"视频"), KLanguage(@"音乐"),KLanguage(@"文档"),KLanguage(@"图片"),KLanguage(@"压缩"),KLanguage(@"其它")];
    NSArray *imageArr = @[@"icon_left_fav", @"icon_left_shareLink", @"icon_left_recentDoc", @"icon_left_movie",@"icon_left_music",@"icon_left_doc",@"icon_left_image",@"icon_left_zip",@"icon_left_other"];
    

    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (NSInteger i = 0; i < titleArr.count; i++) {
        NSString *title = titleArr[i] ;
        UIImage *rightImage = nil;
        if([title isEqualToString:KLanguage(@"查看")]||[title isEqualToString:KLanguage(@"排序方式")]){
            rightImage = VULGetImage(@"icon_right");
        }
      
        YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:VULGetImage(imageArr[i]) rightImage:rightImage handler:^(YCMenuAction *action) {

            
            if ([action.title isEqualToString:KLanguage(@"查看")] || [action.title isEqualToString:KLanguage(@"排序方式")]) {
//                [self addViewWIthTitle:action.title FromBtn:sender];
                return;
            }
            
            
            if (self.clickBlock) {
                self.clickBlock(action.title);
            }
        }];
        [array addObject:action];
    }
    NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
    CGFloat width = 160;
      if ([appLanguage isEqualToString:@"en"]) {
          width =200;
      }
    YCMenuView *menuView = [YCMenuView menuWithActions:array width:width relyonView:sender];
    menuView.menuColor = [UIColor whiteColor];
    menuView.separatorIndexArray = @[@(2),@(8)];
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

#pragma mark - UITextFieldDelegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    if (self.searchWtihTextField) {
        self.searchWtihTextField(textField.text);
    }
    return YES;
}
- (UITextField *)searchField {
    if (!_searchField) {
        _searchField = ({
            UITextField *field = [UITextField new];
            field.frame = CGRectMake(50, 0, 0, 30);
            field.top = kStatusBarHeight + (44 - field.height) / 2.0;
            field.width = kScreenWidth - field.left - 44;
            field.font = [UIFont yk_pingFangRegular:14];
            field.textColor = UIColorHex(#EEEEEE);
            field.attributedPlaceholder = [[NSMutableAttributedString alloc] initWithString:KLanguage(@"搜索")attributes:@{ NSForegroundColorAttributeName: [UIColorHex(#EEEEEE) colorWithAlphaComponent:0.5]}];
            field.leftViewMode =  UITextFieldViewModeAlways;
            field.returnKeyType = UIReturnKeySearch;
            field.delegate = self;
            field.layer.cornerRadius = 6;
            field.layer.masksToBounds = YES;
            field.backgroundColor = [UIColorHex(#FFFFFF) colorWithAlphaComponent:0.1];
            
            field.leftView = ({
                UIView *leftView = [UIView new];
                
                UIImageView *leftImg = [[UIImageView alloc] initWithFrame:CGRectMake(10, 0, 20, 20)];
                leftImg.image = VULGetImage(@"icon_search");
                leftImg.contentMode = UIViewContentModeScaleAspectFit;
                [leftView addSubview:leftImg];
                
                leftView.size = CGSizeMake(leftImg.right + 10, leftImg.height);
                leftView;
            });
            field;
        });
    }
    return _searchField;
}
-(VULButton *)moreSearchBtn{
    if (!_moreSearchBtn) {
        _moreSearchBtn = [VULButton new];
        [_moreSearchBtn setImage:VULGetImage(@"icon_more_search") forState:UIControlStateNormal];
        [_moreSearchBtn setImage:  [self image:VULGetImage(@"icon_more_search") rotation:UIImageOrientationDown] forState:UIControlStateSelected];
        [_moreSearchBtn addTarget:self action:@selector(clickMoreSearchBtn:) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _moreSearchBtn;
}
-(void)clickMoreSearchBtn:(VULButton *)sender{
    [_searchField resignFirstResponder];

    sender.selected = !sender.selected;
    if (self.clickSearch) {
        self.clickSearch(sender.selected);
    }
}
- (UIImage *)image:(UIImage *)image rotation:(UIImageOrientation)orientation

{

    long double rotate = 0.0;

    CGRect rect;

    float translateX = 0;

    float translateY = 0;

    float scaleX = 1.0;

    float scaleY = 1.0;

    

    switch (orientation) {

        case UIImageOrientationLeft:

            rotate = M_PI_2;

            rect = CGRectMake(0, 0, image.size.height, image.size.width);

            translateX = 0;

            translateY = -rect.size.width;

            scaleY = rect.size.width/rect.size.height;

            scaleX = rect.size.height/rect.size.width;

            break;

        case UIImageOrientationRight:

            rotate = 3 * M_PI_2;

            rect = CGRectMake(0, 0, image.size.height, image.size.width);

            translateX = -rect.size.height;

            translateY = 0;

            scaleY = rect.size.width/rect.size.height;

            scaleX = rect.size.height/rect.size.width;

            break;

        case UIImageOrientationDown:

            rotate = M_PI;

            rect = CGRectMake(0, 0, image.size.width, image.size.height);

            translateX = -rect.size.width;

            translateY = -rect.size.height;

            break;

        default:

            rotate = 0.0;

            rect = CGRectMake(0, 0, image.size.width, image.size.height);

            translateX = 0;

            translateY = 0;

            break;

    }

    

    UIGraphicsBeginImageContext(rect.size);

    CGContextRef context = UIGraphicsGetCurrentContext();

    //做CTM变换

    CGContextTranslateCTM(context, 0.0, rect.size.height);

    CGContextScaleCTM(context, 1.0, -1.0);

    CGContextRotateCTM(context, rotate);

    CGContextTranslateCTM(context, translateX, translateY);

    

    CGContextScaleCTM(context, scaleX, scaleY);

    //绘制图片

    CGContextDrawImage(context, CGRectMake(0, 0, rect.size.width, rect.size.height), image.CGImage);

    

    UIImage *newPic = UIGraphicsGetImageFromCurrentImageContext();

    

    return newPic;

}
//moreSearchBtn
@end
