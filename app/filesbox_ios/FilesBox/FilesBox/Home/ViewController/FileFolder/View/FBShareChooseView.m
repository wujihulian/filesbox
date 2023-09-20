//
//  FBShareChooseView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/30.
//

#import "FBShareChooseView.h"
#import "BRDatePickerView.h"
#import "VULRadarInputView.h"
@interface FBTitleCell : BaseTableViewCell

@property (nonatomic, strong) UIImageView *img;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *detailLabel;

@end
@implementation FBTitleCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self.contentView addSubview:self.img];
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.detailLabel];

        [self.img mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(12));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.detailLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.titleLabel.mas_right).offset(fontAuto(10));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];

    }
    return self;
    
};
- (UIImageView *)img {
    if (!_img) {
        _img = [UIImageView new];
        _img.contentMode = UIViewContentModeScaleAspectFit;
        _img.image = VULGetImage(@"icon_choose_right");
    }
    return _img;
}


- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.  lineBreakMode = NSLineBreakByTruncatingMiddle;
    }
    return _titleLabel;
}
- (VULLabel *)detailLabel {
    if (!_detailLabel) {
        _detailLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:fontAuto(14)] Color:UIColorHex(#999999) BgColor:nil];
        _detailLabel.  lineBreakMode = NSLineBreakByTruncatingMiddle;
    }
    return _detailLabel;
}

@end
@interface FBShareChooseView ()<UITableViewDelegate,UITableViewDataSource>
@end

@implementation FBShareChooseView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor =[UIColor whiteColor];
        [self addSubview:self.titleLabel];
        [self addSubview:self.confirmBtn];
        [self addSubview:self.tableView];

        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.top.mas_equalTo(0);
            make.height.mas_equalTo(50);
    
        }];
        [self.confirmBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.top.mas_equalTo(0);
            make.right.mas_equalTo(-fontAuto(12));
            make.width.mas_equalTo(fontAuto(80));
            make.height.mas_equalTo(50);
        }];
        [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom);
            make.right.bottom.left.mas_equalTo(0);
        }];
    }
    return self;
}
-(void)setListArray:(NSMutableArray *)listArray{
    _listArray = listArray;
    [self.tableView reloadData];
}

#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return self.listArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBTitleCell *videoCell = [tableView dequeueReusableCellWithIdentifier:@"FBTitleCell" forIndexPath:indexPath];
    NSString *title  =self.listArray[indexPath.row];;

    videoCell.titleLabel.text = title;
    videoCell.img.hidden = indexPath.row != self.row;
    videoCell.detailLabel.text = @"";
    if ([title isEqualToString:KLanguage(@"自定义次数")]) {
        videoCell.detailLabel.text =self.onceStr && self.onceStr.length>0?self.onceStr:@"";
    }
    if ([title isEqualToString:KLanguage(@"设置到期时间")]) {
        videoCell.detailLabel.text =self.onceStr && self.onceStr.length>0?self.onceStr:KLanguage(@"不限制");
    }
    return videoCell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    self.row = indexPath.row;
    NSString *title  =self.listArray[indexPath.row];;
    [self.tableView reloadData];
    if ([title isEqualToString:KLanguage(@"设置到期时间")]) {
        BRDatePickerView *datePickerView = [[BRDatePickerView alloc]init];
        datePickerView.pickerMode = BRDatePickerModeYMDHMS;
        datePickerView.title = KLanguage(@"请选择年月日");
        datePickerView.minDate = [NSDate date];
        datePickerView.isAutoSelect = NO;
        datePickerView.resultBlock = ^(NSDate *selectDate, NSString *selectValue) {
            
            NSTimeInterval currentTime = [selectDate timeIntervalSince1970];
            self.timeStr = [NSString stringWithFormat:@"%.0f",currentTime];
            self.onceStr=   [NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",self.timeStr.integerValue*1000] Format:@"yyyy-MM-dd HH:mm:ss"];
            [self.tableView reloadData];
            
        };
        BRPickerStyle *customStyle = [[BRPickerStyle alloc]init];
        customStyle.language = [[NSUserDefaults standardUserDefaults]objectForKey:@"appLanguage"];
        customStyle.pickerColor = [UIColor whiteColor];
        customStyle.selectRowTextColor = HEXCOLOR(0x4191ff);
        datePickerView.pickerStyle = customStyle;
        [datePickerView show];
        return;
    }
    
    if ([title isEqualToString:KLanguage(@"自定义次数")]) {
        [self.inputView show];
    }

    
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return BtnCell;
}
- (UITableViewStyle )tableViewStyle {
    return UITableViewStylePlain;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01;

}
- (CGFloat)verticalOffsetForEmptyDataSet:(UIScrollView *)scrollView {
    return 0.1f;
}

- (UIImage *)imageForEmptyDataSet:(UIScrollView *)scrollView {
    return [UIImage imageNamed:@"no_data"];
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"到期时间") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:fontAuto(16)] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _titleLabel;
}
-(VULButton *)confirmBtn{
    if (!_confirmBtn) {
        _confirmBtn = [VULButton new];
        [_confirmBtn setTitle:KLanguage(@"确定") forState:UIControlStateNormal];
        _confirmBtn.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(16)];
        _confirmBtn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
        [_confirmBtn setTitleColor:BtnColor forState:UIControlStateNormal];
        [_confirmBtn addTarget:self action:@selector(clickConfimBtn) forControlEvents:UIControlEventTouchUpInside];
    }
    return _confirmBtn;
}
-(void)clickConfimBtn{
  
    if (self.clickCell) {
        self.clickCell(self.listArray[self.row],self.onceStr,self.timeStr);
    }
}

#pragma mark - Lazy
- (BaseTableView *)tableView {
    if (!_tableView) {
        _tableView = [[BaseTableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = [UIColor clearColor];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        [_tableView registerClass:[FBTitleCell class] forCellReuseIdentifier:@"FBTitleCell"];
        
    }
    return _tableView;
}


- (VULRadarInputView *)inputView {
    if (!_inputView) {
        _inputView = [VULRadarInputView new];
        _inputView.textField.keyboardType = UIKeyboardTypeNumberPad;
        _inputView.searchCallBack = ^(NSString * _Nonnull keyword) {
            self.onceStr = keyword;
            [self.tableView reloadData];
        };
    }
    return _inputView;
}
@end
