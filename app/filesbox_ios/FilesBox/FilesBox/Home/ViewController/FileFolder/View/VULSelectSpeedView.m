//
//  VULSelectSpeedView.m
//  VideoULimit
//
//  Created by yuekewei on 2020/4/20.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULSelectSpeedView.h"

@interface VULSelectSpeedView ()<UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) BaseTableView *tableView;
@property (nonatomic, strong) NSArray *typeArray;
@property (nonatomic, assign) NSInteger selectIndex;
@end

@implementation VULSelectSpeedView

- (instancetype)initWithSelectIndex:(NSInteger)selectIndex {
    self = [super init];
    if (self) {
        
        self.backgroundColor = [UIColor whiteColor];
        self.selectIndex = selectIndex;
        
        _typeArray = @[@"0.5X",@"0.75X",@"1.0X",@"1.5X",@"2.0X"];
        
        self.frame = CGRectMake(0, 0, kScreenWidth, _typeArray.count * FontAuto(45) + FontAuto(10));
        
        [self addSubview:self.tableView];
        [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(FontAuto(10));
            make.left.right.bottom.mas_equalTo(0);
        }];
        
        
        self.layer.mask = ({
            UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:self.bounds
                                                           byRoundingCorners:UIRectCornerTopLeft|UIRectCornerTopRight
                                                                 cornerRadii:CGSizeMake(10, 10)];
            CAShapeLayer *maskLayer = [CAShapeLayer layer];
            maskLayer.frame = self.bounds;
            maskLayer.path = maskPath.CGPath;
            maskLayer;
        });
        self.clipsToBounds = YES;
    }
    return self;
}

#pragma mark - UITableViewDelegate\UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.typeArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    BaseTableViewCell *cell = [BaseTableViewCell dequeueReusableCellWithTableView:tableView reuseIdentifier:@"cell"];
    UILabel *titleLabel = [cell.contentView viewWithTag:101];
    if (!titleLabel) {
        titleLabel = ({
            UILabel *label = [[UILabel alloc] init];
            label.font = [UIFont yk_pingFangSemibold:FontAuto(14)];
            label.textColor = UIColorHex(#333333);
            label;
        });
        titleLabel.tag = 101;
        [cell.contentView addSubview:titleLabel];
        
        [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(FontAuto(16));
            make.centerY.mas_equalTo(cell.contentView.mas_centerY);
        }];
    }
    titleLabel.text = [_typeArray objectAtIndex:indexPath.row];
    
    UIButton *selectButton = [cell.contentView viewWithTag:102];
    if (!selectButton) {
        selectButton = ({
            UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
            [button setImage:[UIImage imageForIconBundle:@"icon_audio_select"] forState:UIControlStateNormal];
            button;
        });
        selectButton.tag = 102;
        selectButton.enabled = NO;
        [cell.contentView addSubview:selectButton];
        
        [selectButton mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(- FontAuto(16));
            make.centerY.mas_equalTo(cell.contentView.mas_centerY);
        }];
    }
    
    selectButton.hidden = _selectIndex != indexPath.row;
    
    UIView *line = [cell.contentView viewWithTag:103];
    if (!line) {
        line = [UIView new];
        line.tag = 103;
        line.backgroundColor = UIColorHex(#F8F8F8);
        [cell.contentView addSubview:line];
        [line mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(FontAuto(16));
            make.right.mas_equalTo(- FontAuto(16));
            make.bottom.mas_equalTo(0);
            make.height.mas_equalTo(0.5);
        }];
    }
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return FontAuto(45);
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    self.selectIndex = indexPath.row;
    [self.tableView reloadData];
    if (_selectSpeedBlock) {
        _selectSpeedBlock(self.selectIndex, [_typeArray objectAtIndex:indexPath.row]);
    }
}

#pragma mark - Lazy
- (BaseTableView *)tableView {
    if (!_tableView) {
        _tableView = [[BaseTableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _tableView.bounces = NO;
    }
    return _tableView;
}
@end
