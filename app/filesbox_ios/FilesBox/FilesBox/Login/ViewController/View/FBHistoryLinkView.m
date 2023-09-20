//
//  FBHistoryLinkView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/15.
//

#import "FBHistoryLinkView.h"
@interface FBHistoryLinkView ()<UITableViewDelegate,UITableViewDataSource,DZNEmptyDataSetSource,DZNEmptyDataSetDelegate>
@property (nonatomic, strong) BaseTableView *tableView;
@property (nonatomic, strong) NSMutableArray *dataArray;


@end
@implementation FBHistoryLinkView
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = [UIColor redColor];
        self.layer.masksToBounds= YES;
        self.layer.cornerRadius = 5;
        [self setView];
    }
    return self;
}
-(void)setView{
  self.dataArray = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"baseUrlArr"]];

    [self addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_offset(0);
    }];
    
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

#pragma mark - UITableViewDelegate\UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//    return 10;
    return self.dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    BaseTableViewCell *cell = [BaseTableViewCell dequeueReusableCellWithTableView:tableView reuseIdentifier:@"myInfoCell"];
    [cell removeAllSubviews];
    UILabel *label = [UILabel new];
    label.textColor = HEXCOLOR(0x666666);
    label.text = self.dataArray[indexPath.row];
    [cell addSubview:label];
    [label mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(0);
        make.left.mas_equalTo(fontAuto(10));
        make.height.mas_equalTo(fontAuto(40));
        make.right.mas_equalTo(-fontAuto(35));
    }];
    UIImageView *imageV = [UIImageView new];
    imageV.contentMode = UIViewContentModeScaleAspectFit;
    imageV.image =VULGetImage(@"link_delete");
    imageV.userInteractionEnabled = YES;
    [cell addSubview:imageV];
    [imageV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-10);
        make.top.mas_equalTo(fontAuto(10));
        make.height.mas_equalTo(fontAuto(20));
    }];
    imageV.tag = indexPath.row+1000;
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickClose:)];
    [imageV addGestureRecognizer:tap];
    return cell;
}
-(void)clickClose:(UITapGestureRecognizer *)sender{
    [self.dataArray removeObjectAtIndex:sender.view.tag-1000];
    [VULQueue executeInMainQueue:^{
        [self.tableView reloadData];
    } delay:0.2];
    [NSArray bg_clearArrayWithName:@"baseUrlArr"];

    NSMutableArray *saveSelect = [NSMutableArray arrayWithArray:self.dataArray];
    if (self.dataArray.count >0) {
        [saveSelect bg_saveArrayWithName:@"baseUrlArr"];
    }
  
    
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return fontAuto(40);
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0.01;
}

- (nullable UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView *view = [UIView new];
    view.backgroundColor = MyBackGroundColor;
    return  view;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

    if (self.chooseNowUrl) {
        self.chooseNowUrl(self.dataArray[indexPath.row]);
    }
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
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.estimatedRowHeight = 125.f;
        _tableView.emptyDataSetSource = self;
        _tableView.emptyDataSetDelegate = self;

    }
    return _tableView;
}
@end
