//
//  FBFromTopView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/18.
//

#import "FBFromTopView.h"
@interface FBButtomCell : BaseTableViewCell

@property (nonatomic, strong) UIImageView *img;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) UIView *bgView;

@end
@implementation FBButtomCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        
        [self.contentView addSubview:self.bgView];
        [self.contentView addSubview:self.img];
        [self.contentView addSubview:self.titleLabel];
        [self.img mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(15));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.img.mas_right).offset(fontAuto(5));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(3));
            make.top.mas_equalTo(fontAuto(3));
            make.bottom.mas_equalTo(-fontAuto(3));
            make.right.mas_equalTo(-fontAuto(3));
        }];
    }
    return self;
    
};
- (UIImageView *)img {
    if (!_img) {
        _img = [UIImageView new];
        _img.contentMode = UIViewContentModeScaleAspectFit;

    }
    return _img;
}
- (UIView *)bgView {
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = [UIColor whiteColor];
        _bgView.layer.masksToBounds = YES;
        _bgView.layer.cornerRadius = 5;

    }
    return _bgView;
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:15+1] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.  lineBreakMode = NSLineBreakByTruncatingMiddle;
    }
    return _titleLabel;
}




@end
@interface FBFromTopView ()<UITableViewDelegate,UITableViewDataSource>
@property (nonatomic, strong) BaseTableView *tableView;
@end

@implementation FBFromTopView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor =[UIColor whiteColor];
        [self addSubview:self.tableView];
      
    }
    return self;
}
-(void)setTitleArr:(NSArray *)titleArr{
    _titleArr = titleArr;
    [self.tableView reloadData];
}
-(void)setIndex:(NSInteger)index{
    if (index ==1) {
        [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_StatusBar_Height);
            make.left.right.bottom.mas_equalTo(0);
        }];
    }else{
        [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.left.right.bottom.mas_equalTo(0);
        }];
    }
}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return self.titleArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBButtomCell *videoCell = [tableView dequeueReusableCellWithIdentifier:@"FBButtomCell" forIndexPath:indexPath];
    if (self.indexLine) {
        videoCell.separatorLine.hidden =  indexPath.row!=self.indexLine;
    }
    if (self.lineArr.count>0) {
        videoCell.separatorLine.backgroundColor =   kLineColor;
        if( [self.lineArr containsObject:@(indexPath.row) ]) {
            videoCell.separatorLine.hidden = YES;
        }else{
            videoCell.separatorLine.hidden = NO;

        }
    }
    if (self.iconArr.count >0) {
        [videoCell.img mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(15));
            make.centerY.mas_equalTo(videoCell.contentView.mas_centerY);
        }];
        videoCell.img.image = VULGetImage(self.iconArr[indexPath.row]);
    }else{
        [videoCell.img mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(15));
            make.width.height.mas_equalTo(0);
            make.centerY.mas_equalTo(videoCell.contentView.mas_centerY);
        }];
    }
    if (self.selectArr) {
        if ([self.selectArr containsObject: self.titleArr[indexPath.row]]) {
            videoCell.bgView.backgroundColor =[HEXCOLOR(0x722ed1) colorWithAlphaComponent:0.1];
            videoCell.titleLabel.font =  [UIFont yk_pingFangSemibold:15+1];

        }else{
            videoCell.bgView.backgroundColor =UIColor.whiteColor;
            videoCell.titleLabel.font =  [UIFont yk_pingFangRegular:15+1];
        }
        
    }else{
        if (self.selectIndex) {
            if (self.selectIndex.integerValue == indexPath.row) {
                videoCell.bgView.backgroundColor =[HEXCOLOR(0x722ed1) colorWithAlphaComponent:0.1];
                videoCell.titleLabel.font =  [UIFont yk_pingFangSemibold:15+1];

            }else{
                videoCell.bgView.backgroundColor =UIColor.whiteColor;
                videoCell.titleLabel.font =  [UIFont yk_pingFangRegular:15+1];

            }
        }else{
            videoCell.bgView.backgroundColor =UIColor.whiteColor;
            videoCell.titleLabel.font =  [UIFont yk_pingFangRegular:15+1];
        }
    }
   
    videoCell.titleLabel.text= self.titleArr[indexPath.row];
    
    
    if (self.dataArray) {
        if (isPermissionWithModel(_titleArr[indexPath.row],self.dataArray)) {
            if ( [videoCell.titleLabel.text isEqualToString:KLanguage(@"创建压缩包")]&&self.isSHow) {
                videoCell.titleLabel.textColor = HEXCOLOR(0x999999);
                videoCell.userInteractionEnabled = NO;
            }else{
                videoCell.titleLabel.textColor = HEXCOLOR(0x333333);
                videoCell.userInteractionEnabled = YES;
            }
           
        }else{
            videoCell.titleLabel.textColor = HEXCOLOR(0x999999);
            videoCell.userInteractionEnabled = NO;

        }
    }
    return videoCell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (self.selectIndex) {
        if ([self.selectArr containsObject:self.titleArr[indexPath.row]]) {
            [self.selectArr removeObject:self.titleArr[indexPath.row]];
        }else{
            [self.selectArr addObject:self.titleArr[indexPath.row]];

        }
        [self.tableView reloadData];
        if (self.clickViewWithRowBlock) {
            self.clickViewWithRowBlock(self.titleArr[indexPath.row],indexPath.row);
        }
        return;
    }
    if (self.clickViewBlock) {
        self.clickViewBlock(self.titleArr[indexPath.row]);
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
#pragma mark - Lazy
- (BaseTableView *)tableView {
    if (!_tableView) {
        _tableView = [[BaseTableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = [UIColor clearColor];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        [_tableView registerClass:[FBButtomCell class] forCellReuseIdentifier:@"FBButtomCell"];
        
    }
    return _tableView;
}

@end
