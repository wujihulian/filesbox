//
//  FBSearchView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/18.
//

#import "FBSearchView.h"
#import "FBLabelChooseView.h"
#import "FBFMoreFileView.h"
#import "FBMoreTimeView.h"
#import "FBFileSizeMoreView.h"
@interface FBSearchView ()
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UISegmentedControl *topBtn;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, strong) VULLabel *fileTypeLabel;
@property (nonatomic, strong) FBLabelChooseView *fileTypeConentLabel;
@property (nonatomic, strong) FBLabelChooseView *modeFileType;
@property (nonatomic, strong) VULLabel *timeLabel;
@property (nonatomic, strong) FBLabelChooseView *timeConentLabel;
@property (nonatomic, strong) FBMoreTimeView *timeMore;
@property (nonatomic, strong) VULLabel *fileSizeLabel;
@property (nonatomic, strong) FBLabelChooseView *fileSizeConentLabel;
@property (nonatomic, strong) FBFileSizeMoreView *fileSizeMore;
@property (nonatomic, strong) VULLabel *userLabel;
@property (nonatomic, strong) FBLabelChooseView *userConentLabel;
@property (nonatomic, strong) VULLabel *userAttrLabel;
@property (nonatomic, strong) VULButton *searchBtn;
@property (nonatomic, strong) VULButton *resetBtn;
@property (nonatomic, strong) NSMutableDictionary *searchDic;

@end

@implementation FBSearchView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor =[UIColor whiteColor];
        self.backgroundColor = [HEXCOLOR(0x333333) colorWithAlphaComponent:0.4];
        self.bgView.userInteractionEnabled = YES;
        [self addSubview:self.bgView];
        [self.bgView addSubview:self.titleLabel];
        [self.bgView  addSubview:self.topBtn];
        [self.bgView  addSubview:self.fileTypeLabel];
        [self.bgView  addSubview:self.fileTypeConentLabel];
        [self.bgView  addSubview:self.modeFileType];
        [self.bgView  addSubview:self.fileSizeLabel];
        [self.bgView  addSubview:self.fileSizeConentLabel];
        [self.bgView  addSubview:self.fileSizeMore];
        [self.bgView  addSubview:self.timeLabel];
        [self.bgView  addSubview:self.timeConentLabel];
        [self.bgView  addSubview:self.timeMore];
        [self.bgView  addSubview:self.userLabel];
        [self.bgView  addSubview:self.userConentLabel];
        [self.bgView  addSubview:self.userAttrLabel];
        [self.bgView  addSubview:self.searchBtn];
        [self.bgView  addSubview:self.resetBtn];
        

        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
        }];
        [self.topBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.width.mas_equalTo(fontAuto(200));
            make.height.mas_equalTo(fontAuto(28));
        }];
        [self.fileTypeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.topBtn.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
        }];
        [self.fileTypeConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.fileTypeLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));

        }];
        [self.modeFileType mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.fileTypeConentLabel.mas_bottom).offset(fontAuto(0));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(0));
        }];
        [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.modeFileType.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
        }];
        [self.timeConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.timeLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));

        }];
        [self.timeMore mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.timeConentLabel.mas_bottom).offset(fontAuto(0));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(0));
        }];
        [self.fileSizeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.timeMore.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
        }];
        [self.fileSizeConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.fileSizeLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));

        }];
        [self.fileSizeMore mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.fileSizeConentLabel.mas_bottom).offset(fontAuto(0));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(0));
        }];
        [self.userLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.fileSizeMore.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
        }];
        [self.userConentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.userLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(30));

        }];
        [self.userAttrLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.userConentLabel.mas_bottom).offset(fontAuto(0));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(fontAuto(20));
        }];
        
        [self.searchBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.userAttrLabel.mas_bottom).offset(fontAuto(20));
            make.width.mas_equalTo(fontAuto(80));
            make.height.mas_equalTo(fontAuto(30));
            make.left.mas_equalTo((VULSCREEN_WIDTH-fontAuto(170))/2);
        }];
        [self.resetBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.userAttrLabel.mas_bottom).offset(fontAuto(20));
            make.left.mas_equalTo(self.searchBtn.mas_right).offset(fontAuto(10));
            make.width.mas_equalTo(fontAuto(80));
            make.height.mas_equalTo(fontAuto(30));
        }];
  
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(hide)];
        [self addGestureRecognizer:tap];
        UITapGestureRecognizer *tap1 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickBg)];
        [self addGestureRecognizer:tap];
        [self.bgView addGestureRecognizer:tap1];
        [self layoutIfNeeded];
        self.bgView.frame = CGRectMake(0, 0, VULSCREEN_WIDTH,self.searchBtn.bottom+fontAuto(20));

    }
    return self;
}
-(NSMutableDictionary *)searchDic{
    if (!_searchDic) {
        _searchDic = [NSMutableDictionary dictionary];
    }
    return _searchDic;
}
-(void)clickBg{
    
}
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = [UIColor whiteColor];
        _bgView.clipsToBounds = YES;
    }
    return _bgView;
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"搜索范围") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
- (VULLabel *)fileTypeLabel {
    if (!_fileTypeLabel) {
        _fileTypeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"文件类型") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _fileTypeLabel.numberOfLines = 1;
    }
    return _fileTypeLabel;
}

- (VULLabel *)timeLabel {
    if (!_timeLabel) {
        _timeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"时间范围") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _timeLabel.numberOfLines = 1;
    }
    return _timeLabel;
}
- (VULLabel *)fileSizeLabel {
    if (!_fileSizeLabel) {
        _fileSizeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"文件大小") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _fileSizeLabel.numberOfLines = 1;
    }
    return _fileSizeLabel;
}
- (VULLabel *)userLabel {
    if (!_userLabel) {
        _userLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"用户") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _userLabel.numberOfLines = 1;
    }
    return _userLabel;
}
- (VULLabel *)userAttrLabel {
    if (!_userAttrLabel) {
        _userAttrLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"按创建者/修改者搜索") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#999999) BgColor:nil];
        _userAttrLabel.numberOfLines = 1;
    }
    return _userAttrLabel;
}
-(VULButton *)searchBtn{
    if (!_searchBtn) {
        _searchBtn = [VULButton new];
        _searchBtn.backgroundColor = HEXCOLOR(0x722ed1);
        _searchBtn.layer.masksToBounds = YES;
        _searchBtn.layer.cornerRadius = 5;
        [_searchBtn setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
        [_searchBtn setTitle:KLanguage(@"搜索") forState:UIControlStateNormal];
        _searchBtn.layer.masksToBounds = YES;
        _searchBtn.layer.cornerRadius = 5;
        _searchBtn.titleLabel.font =[UIFont yk_pingFangRegular:15];
        _searchBtn.layer.borderColor =UIColorHex(#722ed1).CGColor;
        _searchBtn.layer.borderWidth =1;
        [_searchBtn addTarget:self action:@selector(clickSearch) forControlEvents:UIControlEventTouchUpInside];
    }
    return _searchBtn;
}
-(void)clickSearch{
    if (self.clickViewBlock) {
        self.clickViewBlock(self.searchDic);
    }
    [self hide];
}
//clickViewBlock
-(VULButton *)resetBtn{
    if (!_resetBtn) {
        _resetBtn = [VULButton new];
        _resetBtn.backgroundColor = HEXCOLOR(0xffffff);
        _resetBtn.layer.masksToBounds = YES;
        _resetBtn.layer.cornerRadius = 5;
        [_resetBtn setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        _resetBtn.titleLabel.font =[UIFont yk_pingFangRegular:15];
        [_resetBtn setTitle:KLanguage(@"重置") forState:UIControlStateNormal];
        _resetBtn.layer.masksToBounds = YES;
        _resetBtn.layer.cornerRadius = 5;
        _resetBtn.layer.borderColor =UIColorHex(#d9d9d9).CGColor;
        _resetBtn.layer.borderWidth =1;
        [_resetBtn addTarget:self action:@selector(clickResetBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _resetBtn;
}
-(void)clickResetBtn{
    [self.searchDic removeAllObjects];
    if (self.clickViewBlock) {
        self.clickViewBlock(self.searchDic);
    }
    [self hide];
}

- (FBLabelChooseView *)userConentLabel {
    if (!_userConentLabel) {
        _userConentLabel = [[FBLabelChooseView alloc] initWithFrame:CGRectZero];
        _userConentLabel.fileTypeConentLabel.text = KLanguage(@"请选择");
        _userConentLabel.index =3;
        _userConentLabel.selectIndex = 0;
        _userConentLabel.clickViewBlock = ^(NSString * _Nonnull title) {
            [self.searchDic setValue:title forKey:@"userID"];

        };
    }
    return _userConentLabel;
}
- (FBLabelChooseView *)fileSizeConentLabel {
    if (!_fileSizeConentLabel) {
        _fileSizeConentLabel = [[FBLabelChooseView alloc] initWithFrame:CGRectZero];
        _fileSizeConentLabel.fileTypeConentLabel.text = KLanguage(@"不限大小");
        _fileSizeConentLabel.index = 2;
        _fileSizeConentLabel.selectIndex = 0;
        _fileSizeConentLabel.clickViewBlock = ^(NSString * _Nonnull title) {
            if ([title isEqualToString:KLanguage(@"自定义")]) {
                [self.fileSizeMore mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.fileSizeConentLabel.mas_bottom).offset(fontAuto(10));
                    make.left.mas_equalTo(fontAuto(12));
                    make.right.mas_equalTo(-fontAuto(12));
                    make.height.mas_equalTo(fontAuto(44));
                }];
            }else{
                [self.fileSizeMore mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.fileSizeConentLabel.mas_bottom).offset(fontAuto(0));
                    make.left.mas_equalTo(fontAuto(12));
                    make.right.mas_equalTo(-fontAuto(12));
                    make.height.mas_equalTo(fontAuto(0));
                }];
//                titleArr = @[KLanguage(@"不限大小"),KLanguage(@"0~100KB"),KLanguage(@"100KB~1MB"),KLanguage(@"1MB~10MB"),KLanguage(@"10MB~100MB"),KLanguage(@"100MB~1GB"),KLanguage(@"1GB以上"),KLanguage(@"自定义")];
                if ([title isEqualToString:KLanguage(@"不限大小")]) {
                    if ([self.searchDic.allKeys containsObject:@"minSize"] ) {
                        [self.searchDic removeObjectForKey:@"maxSize"];
                        [self.searchDic removeObjectForKey:@"minSize"];
                    }
                }else if([title isEqualToString:KLanguage(@"0~100KB")]){
                    [self.searchDic setValue:@(100*1024) forKey:@"maxSize"];
                    [self.searchDic setValue:@(0) forKey:@"minSize"];
                }else if([title isEqualToString:KLanguage(@"100KB~1MB")]){
                    [self.searchDic setValue:@(1024*1024) forKey:@"maxSize"];
                    [self.searchDic setValue:@(100*1024) forKey:@"minSize"];
                }else if([title isEqualToString:KLanguage(@"1MB~10MB")]){
                    [self.searchDic setValue:@(1024*1024*10) forKey:@"maxSize"];
                    [self.searchDic setValue:@(1024*1024*1) forKey:@"minSize"];
                }else if([title isEqualToString:KLanguage(@"10MB~100MB")]){
                    [self.searchDic setValue:@(1024*1024*100) forKey:@"maxSize"];
                    [self.searchDic setValue:@(1024*1024*10) forKey:@"minSize"];
                }else if([title isEqualToString:KLanguage(@"100MB~1GB")]){
                    [self.searchDic setValue:@(1024*1024*1024) forKey:@"maxSize"];
                    [self.searchDic setValue:@(1024*1024*100) forKey:@"minSize"];
                }else if([title isEqualToString:KLanguage(@"1GB以上")]){
                    [self.searchDic setValue:@(1024*1024*1024) forKey:@"minSize"];
                    [self.searchDic removeObjectForKey:@"maxSize"];
                }
            }
            [self changeViewHight];
        };
    }
    return _fileSizeConentLabel;
}
- (FBFileSizeMoreView *)fileSizeMore {
    if (!_fileSizeMore) {
        _fileSizeMore = [[FBFileSizeMoreView alloc] initWithFrame:CGRectZero];
        _fileSizeMore.clickViewBlock = ^(NSString * _Nonnull startTime, NSString * _Nonnull endTime) {
            if (startTime.floatValue>endTime.floatValue) {
                [kWindow makeToast:KLanguage(@"区间填写错误")];
                return;
            }
            if (startTime.length>0) {
                [self.searchDic setValue:[NSString stringWithFormat:@"%.1f",1024*1024*startTime.floatValue] forKey:@"minSize"];
            }else{
                [self.searchDic removeObjectForKey:@"minSize"];

            }
            if (endTime.length>0) {
                [self.searchDic setValue:[NSString stringWithFormat:@"%.1f",1024*1024*endTime.floatValue] forKey:@"maxSize"];
            }else{
                [self.searchDic removeObjectForKey:@"maxSize"];

            }
        };
    }
    return _fileSizeMore;
}

- (FBLabelChooseView *)fileTypeConentLabel {
    if (!_fileTypeConentLabel) {
        _fileTypeConentLabel = [[FBLabelChooseView alloc] initWithFrame:CGRectZero];
        _fileTypeConentLabel.index = 0;
        _fileTypeConentLabel.selectIndex = 0;
        _fileTypeConentLabel.clickViewBlock = ^(NSString * _Nonnull title) {
            if ([title isEqualToString:KLanguage(@"自定义")]) {
                [self.modeFileType mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.fileTypeConentLabel.mas_bottom).offset(fontAuto(10));
                    make.left.mas_equalTo(fontAuto(12));
                    make.right.mas_equalTo(-fontAuto(12));
                    make.height.mas_equalTo(fontAuto(30));
                }];

            }else{
                [self.modeFileType mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.fileTypeConentLabel.mas_bottom).offset(fontAuto(0));
                    make.left.mas_equalTo(fontAuto(12));
                    make.right.mas_equalTo(-fontAuto(12));
                    make.height.mas_equalTo(fontAuto(0));
                }];
                [self.searchDic setValue:getTypeWithTitle(title) forKey:@"fileType"];
            }
            [self changeViewHight];
        };
    }
    return _fileTypeConentLabel;
}

- (FBLabelChooseView *)timeConentLabel {
    if (!_timeConentLabel) {
        _timeConentLabel = [[FBLabelChooseView alloc] initWithFrame:CGRectZero];
        _timeConentLabel.fileTypeConentLabel.text = KLanguage(@"不限时间");
        _timeConentLabel.index = 1;
        _timeConentLabel.selectIndex = 0;
        _timeConentLabel.clickViewBlock = ^(NSString * _Nonnull title) {
            if ([title isEqualToString:KLanguage(@"自定义")]) {
                [self.timeMore mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.top.mas_equalTo(self.timeConentLabel.mas_bottom).offset(fontAuto(10));
                    make.left.mas_equalTo(fontAuto(12));
                    make.right.mas_equalTo(-fontAuto(12));
                    make.height.mas_equalTo(fontAuto(30));
                }];
            }else{
                [self.timeMore mas_remakeConstraints:^(MASConstraintMaker *make) {
                   make.top.mas_equalTo(self.timeConentLabel.mas_bottom).offset(fontAuto(0));
                   make.left.mas_equalTo(fontAuto(12));
                   make.right.mas_equalTo(-fontAuto(12));
                   make.height.mas_equalTo(fontAuto(0));
               }];
                if ([title isEqualToString:KLanguage(@"不限时间")]) {
                    if ([self.searchDic.allKeys containsObject:@"timeFrom"] ) {
                        [self.searchDic removeObjectForKey:@"timeFrom"];
                        [self.searchDic removeObjectForKey:@"timeTo"];
                    }
                }else if([title isEqualToString:KLanguage(@"最近1天")]){
                    NSDate * date = [NSDate date];//当前时间
                    NSDate *lastDay = [NSDate dateWithTimeInterval:-24*60*60 sinceDate:date];//前一天
                    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                    [formatter setDateFormat:@"yyyy-MM-dd"];
                    [self.searchDic setValue:[formatter stringFromDate:lastDay] forKey:@"timeFrom"];
                    [self.searchDic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
                }else if([title isEqualToString:KLanguage(@"最近7天")]){
                    NSDate * date = [NSDate date];//当前时间
                    NSDate *lastDay = [NSDate dateWithTimeInterval:-24*60*60*7 sinceDate:date];//前一天
                    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                    [formatter setDateFormat:@"yyyy-MM-dd"];
                    [self.searchDic setValue:[formatter stringFromDate:lastDay] forKey:@"timeFrom"];
                    [self.searchDic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
                }else if([title isEqualToString:KLanguage(@"最近30天")]){
                    NSDate * date = [NSDate date];//当前时间
                    NSDate *lastDay = [NSDate dateWithTimeInterval:-24*60*60*30 sinceDate:date];//前一天
                    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                    [formatter setDateFormat:@"yyyy-MM-dd"];
                    [self.searchDic setValue:[formatter stringFromDate:lastDay] forKey:@"timeFrom"];
                    [self.searchDic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
                }else if([title isEqualToString:KLanguage(@"最近一年")]){
                    NSDate * date = [NSDate date];//当前时间
                    NSCalendar *cal = [NSCalendar currentCalendar];
                            unsigned int unitFlags = NSYearCalendarUnit|NSMonthCalendarUnit|
                                                        NSDayCalendarUnit;//这句是说你要获取日期的元素有哪些。获取年就要写NSYearCalendarUnit，获取小时就要写NSHourCalendarUnit，中间用|隔开；
                            NSDateComponents *d = [cal components:unitFlags fromDate:date];//把要从date中获取的unitFlags标示的日期元素存放在NSDateComponents类型的d里面；
                    //然后就可以从d中获取具体的年月日了；
                            NSInteger year = [d year];
                            NSInteger month = [d month];
                            NSInteger day  =  [d day];
    
                    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                    [formatter setDateFormat:@"yyyy-MM-dd"];
                    [self.searchDic setValue:[NSString stringWithFormat:@"%ld-%ld-%ld",year-1,month,day] forKey:@"timeFrom"];
                    [self.searchDic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
                }
            }
            [self changeViewHight];
        };
    }
    return _timeConentLabel;
}

- (FBLabelChooseView *)modeFileType {
    if (!_modeFileType) {
        _modeFileType = [[FBLabelChooseView alloc] initWithFrame:CGRectZero];
        _modeFileType.index = 4;
        _modeFileType.fileTypeConentLabel.text = @"";
        _modeFileType.rightImageV.hidden = YES;
        _modeFileType.clickViewBlock = ^(NSString * _Nonnull title) {
            [self.searchDic setValue:title forKey:@"fileType"];
        };


    }
    return _modeFileType;
}
- (FBMoreTimeView *)timeMore {
    if (!_timeMore) {
        _timeMore = [[FBMoreTimeView alloc] initWithFrame:CGRectZero];
        _timeMore.clickViewBlock = ^(NSString * _Nonnull startTime, NSString * _Nonnull endTime) {
            [self.searchDic setValue:startTime forKey:@"timeFrom"];
            [self.searchDic setValue:endTime forKey:@"timeTo"];
        };
    }
    return _timeMore;
}

-(UISegmentedControl *)topBtn{
    if (!_topBtn) {
        _topBtn = [[UISegmentedControl alloc] initWithItems:@[KLanguage(@"全部文件"),KLanguage(@"当前文件夹")]];
        _topBtn.tintColor = HEXCOLOR(0x722ed1);
        _topBtn.selectedSegmentIndex = 1;
        [_topBtn addTarget:self action:@selector(change:) forControlEvents:UIControlEventValueChanged];

    }
    return _topBtn;
}
-(void)change:(UISegmentedControl *)sender{
    NSUInteger segIndex = [sender selectedSegmentIndex];
    if (segIndex == 0) {
        [self.searchDic setValue:@0 forKey:@"sourceID"];
        [self.searchDic setValue:@"" forKey:@"block"];

    }else{

        if ([self.searchDic.allKeys containsObject:@"sourceID"] ) {
            [self.searchDic removeObjectForKey:@"sourceID"];
        }
        if ([self.searchDic.allKeys containsObject:@"block"] ) {
            [self.searchDic removeObjectForKey:@"block"];
        }

    }
    
}
-(void)changeViewHight{
    [self layoutIfNeeded];
    self.bgView.frame = CGRectMake(0, 0, VULSCREEN_WIDTH,self.searchBtn.bottom+fontAuto(20));
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"bounds"];
    animation.duration = 0.1;
    animation.fromValue = [NSValue valueWithCGRect:CGRectMake(0, 0, VULSCREEN_WIDTH,0)];
    animation.toValue = [NSValue valueWithCGRect:CGRectMake(0, 0, VULSCREEN_WIDTH,   self.bgView.height)];
    animation.speed = 2;
    //    设置动画在完成的时候 固定在完成的状态
    //    这个属性 必须把remocedOnCompletion 设置成NO 这个属性 才可以效果
        animation.removedOnCompletion = NO;
        animation.fillMode = kCAFillModeBoth;
    animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseOut];
    self.bgView.layer.anchorPoint = CGPointZero;
    self.bgView.frame  = CGRectMake(0, 0, VULSCREEN_WIDTH,  self.bgView.height);
    [self.bgView.layer addAnimation:animation forKey:@""];
}
-(void)show{
    [kWindow addSubview:self];
    if (self.bgView.height == 0) {
        self.bgView.frame = CGRectMake(0, 0, VULSCREEN_WIDTH,self.searchBtn.bottom+fontAuto(20));

    }
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"bounds"];
    animation.duration = 1;
    animation.fromValue = [NSValue valueWithCGRect:CGRectMake(0, 0, VULSCREEN_WIDTH,0)];
    animation.toValue = [NSValue valueWithCGRect:CGRectMake(0, 0, VULSCREEN_WIDTH,   self.bgView.height)];
    animation.speed = 2;
    //    设置动画在完成的时候 固定在完成的状态
    //    这个属性 必须把remocedOnCompletion 设置成NO 这个属性 才可以效果
        animation.removedOnCompletion = NO;
        animation.fillMode = kCAFillModeBoth;
    animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseOut];
    self.bgView.layer.anchorPoint = CGPointZero;
    self.bgView.frame  = CGRectMake(0, 0, VULSCREEN_WIDTH,  self.bgView.height);
    [self.bgView.layer addAnimation:animation forKey:@""];
}
-(void)hide{
    if (self.dismiss) {
        self.dismiss();
    }
    
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"bounds"];
    animation.duration = 1;
    animation.fromValue = [NSValue valueWithCGRect:CGRectMake(0, 0, VULSCREEN_WIDTH, self.bgView.height)];
    animation.toValue = [NSValue valueWithCGRect:CGRectMake(0, 0, VULSCREEN_WIDTH, 0)];
    animation.speed = 2;
    //    设置动画在完成的时候 固定在完成的状态
    //    这个属性 必须把remocedOnCompletion 设置成NO 这个属性 才可以效果
        animation.removedOnCompletion = NO;
        animation.fillMode = kCAFillModeBoth;
    animation.timingFunction = [CAMediaTimingFunction functionWithName:kCAMediaTimingFunctionEaseOut];
    self.bgView.layer.anchorPoint = CGPointZero;
    self.bgView.frame  = CGRectMake(0, 0, VULSCREEN_WIDTH,  0);
    [self.bgView.layer addAnimation:animation forKey:@""];
    [self performSelector:@selector(startP) withObject:nil afterDelay:0.5];
}
-(void)startP{
    [self removeFromSuperview];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
