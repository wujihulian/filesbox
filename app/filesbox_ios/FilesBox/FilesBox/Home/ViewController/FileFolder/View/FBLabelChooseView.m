//
//  FBLabelChooseView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/20.
//

#import "FBLabelChooseView.h"
#import "FBFromTopView.h"
#import <TTGTagCollectionView/TTGTextTagCollectionView.h>
@interface FBLabelChooseView ()<TTGTextTagCollectionViewDelegate>
@property (nonatomic, strong) TTGTextTagCollectionView *tagCollectionView;
@property (nonatomic, strong) NSMutableArray *selectArr;
@property (nonatomic, strong) NSMutableArray *userListArr;
@property (nonatomic, strong) NSMutableArray *userNameListArr;

@end
@implementation FBLabelChooseView
- (instancetype)initWithFrame:(CGRect)frame {


    if (self = [super initWithFrame:frame]) {
        self.layer.masksToBounds = YES;
        self.layer.cornerRadius = 5;
        self.layer.borderColor =UIColorHex(#d9d9d9).CGColor;
        self.layer.borderWidth =1;
        [self addSubview:self.fileTypeConentLabel];
        [self addSubview:self.rightImageV];
        [self addSubview:self.tagCollectionView];
        [self.tagCollectionView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(3));
            make.top.mas_equalTo(fontAuto(3));
            make.bottom.mas_equalTo(-fontAuto(3));
            make.right.mas_equalTo(-fontAuto(3));
        }];
        [self.fileTypeConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(10));
            make.top.bottom.mas_equalTo(0);
        }];
        [self.rightImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(0));
            make.centerY. mas_equalTo(self.mas_centerY);
            make.height.mas_equalTo(fontAuto(12));
        }];
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickMe)];
        [self addGestureRecognizer:tap];
    }
    return self;
}
-(TTGTextTagCollectionView *)tagCollectionView{
    if (!_tagCollectionView) {
        _tagCollectionView = [[TTGTextTagCollectionView alloc] initWithFrame:CGRectZero];
        _tagCollectionView.hidden = YES;
        _tagCollectionView.delegate = self;
        _tagCollectionView.userInteractionEnabled = NO;
        TTGTextTagConfig *config = _tagCollectionView.defaultConfig;
        config.textFont = [UIFont yk_pingFangRegular:fontAuto(14)];
        config.textColor = UIColorHex(#333333);
        config.selectedTextColor = UIColorHex(#333333);
        config.backgroundColor = UIColorHex(#FFFFFF);
        config.selectedBackgroundColor = UIColorHex(#FFFFFF);
        config.borderWidth = 1;
        config.selectedBorderWidth = 1;
        config.borderColor = UIColorHex(#CCCCCC);
        config.selectedBorderColor = UIColorHex(#CCCCCC);
        config.shadowColor = [UIColor clearColor];
        config.cornerRadius = 2.0;
        config.exactHeight = fontAuto(20);
        config.extraData = @(1);
    }
    return _tagCollectionView;
}
-(NSMutableArray *)selectArr{
    if (!_selectArr) {
        _selectArr = [NSMutableArray array];
    }
    return _selectArr;
}
-(NSMutableArray *)userListArr{
    if (!_userListArr) {
        _userListArr = [NSMutableArray array];
    }
    return _userListArr;
}
-(NSMutableArray *)userNameListArr{
    if (!_userNameListArr) {
        _userNameListArr = [NSMutableArray array];
    }
    return _userNameListArr;
}
-(void)clickMe{
    NSArray *titleArr;
    if (self.index == 0) {
        titleArr = @[KLanguage(@"不限类型"),KLanguage(@"任意文件"),KLanguage(@"文件夹"),KLanguage(@"文档"),KLanguage(@"图片"),KLanguage(@"音乐"),KLanguage(@"视频"),KLanguage(@"压缩包"),KLanguage(@"自定义")];
    }else if(self.index == 1){
        titleArr = @[KLanguage(@"不限时间"),KLanguage(@"最近1天"),KLanguage(@"最近7天"),KLanguage(@"最近30天"),KLanguage(@"最近一年"),KLanguage(@"自定义")];
    }else if(self.index == 2){
        titleArr = @[KLanguage(@"不限大小"),KLanguage(@"0~100KB"),KLanguage(@"100KB~1MB"),KLanguage(@"1MB~10MB"),KLanguage(@"10MB~100MB"),KLanguage(@"100MB~1GB"),KLanguage(@"1GB以上"),KLanguage(@"自定义")];
    }else if(self.index == 3){
       
        if (self.userListArr.count == 0) {
                [self getUserList];
        }else{
            [self showViewWithArray:self.userNameListArr];

        }
        return;
        
    }else if(self.index == 4){
        _tagCollectionView.hidden = NO;
        _tagCollectionView.userInteractionEnabled = NO;

        titleArr = @[@"doc",@"docx",@"xls",@"xlsx",@"ppt",@"pptx",@"pdf"];
    }
    [self showViewWithArray:titleArr];
}
-(void)showViewWithArray:(NSMutableArray *)titleArr{
        
    self.layer.borderColor =UIColorHex(#722ed1).CGColor;
    self.fileTypeConentLabel.textColor = HEXCOLOR(0x999999);
    NSInteger count = titleArr.count;
    if (titleArr.count >10) {
        count =10;
    }
    
    
    FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, count*BtnCell+K_BottomBar_Height)];
    top.index =0;
    top.selectArr = [NSMutableArray arrayWithArray:self.selectArr] ;
    top.selectIndex = [NSString stringWithFormat:@"%ld",self.selectIndex] ;
    top.titleArr = titleArr;
    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
    popup2.layoutType = zhPopupLayoutTypeBottom;
    popup2.presentationStyle = zhPopupSlideStyleFromBottom;
    popup2.maskAlpha = 0.35;
    top.clickViewWithRowBlock = ^(NSString * _Nonnull title, NSInteger row) {
      
        if (self.index == 4) {
            if ([self.selectArr containsObject:title]) {
                [self.selectArr removeObject:title];
                [_tagCollectionView removeTag:title];
            }else{
                [self.selectArr addObject:title];
                [_tagCollectionView addTag:title];
            }
            if (self.clickViewBlock) {
                self.clickViewBlock([self.selectArr componentsJoinedByString:@","]);
            }
            
        }else{
            self.layer.borderColor =UIColorHex(#d9d9d9).CGColor;
            self.fileTypeConentLabel.textColor = HEXCOLOR(0x333333);

            self.selectIndex = row;
            self.fileTypeConentLabel.text = title;
            if (self.index == 3) {
                if (self.clickViewBlock) {
                    self.clickViewBlock(self.userListArr[row]);
                }
            }else{
                if (self.clickViewBlock) {
                    self.clickViewBlock(title);
                }
            }
            
        }
        if (self.index !=4) {
            [popup2 dismiss];
        }


    };
    popup2.didDismissBlock = ^(zhPopupController * _Nonnull popupController) {
        self.layer.borderColor =UIColorHex(#d9d9d9).CGColor;
        self.fileTypeConentLabel.textColor = HEXCOLOR(0x333333);
    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
   
-(void)getUserList{
//https://dev.filesbox.cn/api/disk/user/list?currentPage=1&pageSize=500&status=1
        
    [VULBaseRequest requestWithUrl:@"/api/disk/user/list" params:@{
        @"currentPage":@1,@"pageSize":@500,@"status":@1
    } requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (request.success) {
            [self.userListArr removeAllObjects];
            [self.userNameListArr removeAllObjects];

            NSArray *list = request.data[@"list"];
            for (NSDictionary *dic in list) {
                [self.userListArr addObject:dic[@"userID"]];
                NSString *nickname = dic[@"nickname"];
                if (nickname && nickname.length>0) {
                    [self.userNameListArr addObject:nickname];

                }else{
                    [self.userNameListArr addObject:dic[@"name"]];
                }
            }
            [self showViewWithArray:self.userNameListArr];

        } else
            [self makeToast:request.message];
   
    }];
}
-(UIImageView *)rightImageV{
    if (!_rightImageV) {
        _rightImageV = [UIImageView new];
        _rightImageV.image = VULGetImage(@"search_icon_up");
        _rightImageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _rightImageV;
}

- (VULLabel *)fileTypeConentLabel {
    if (!_fileTypeConentLabel) {
        _fileTypeConentLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"不限类型") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
//        1px solid #d9d9d9
 

    }
    return _fileTypeConentLabel;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
