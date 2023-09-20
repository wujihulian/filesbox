//
//  FBFileAddrituteDetailVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "FBFileAddrituteDetailVC.h"
#import "FBAttributeCell.h"
#import "FBTagCell.h"
#import "FBFileTagView.h"
#import "FBFileTagManagementVC.h"
@interface FBFileAddrituteDetailVC ()
{
    NSString *remark;
}
@property (nonatomic, strong) NSMutableArray *titleArr; //
@property (nonatomic, strong) NSMutableArray *detailArr; //

@end

@implementation FBFileAddrituteDetailVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationView.hidden = YES;
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.mas_equalTo(0);
        make.top.mas_equalTo(0);
    }];
    [self.tableView registerClass:[FBAttributeCell class] forCellReuseIdentifier:@"FBAttributeCell"];
    [self.tableView registerClass:[FBTagCell class] forCellReuseIdentifier:@"FBTagCell"];

    self.tableView.estimatedRowHeight = fontAuto(50);
    self.titleArr = [NSMutableArray array];
    [self.titleArr  addObject:KLanguage(@"大小:")];
    if (self.model.pathDisplay.length>0) {
        [self.titleArr  addObject:KLanguage(@"位置:")];
    }
    [self.titleArr  addObject:KLanguage(@"创建时间:")];
    [self.titleArr  addObject:KLanguage(@"修改时间:")];
    [self.titleArr  addObject:KLanguage(@"创建者:")];
    if (self.model.modifyUserJson) {
        [self.titleArr  addObject:KLanguage(@"修改者:")];
    }
   
    [self.titleArr  addObject:KLanguage(@"描述说明:")];


    self.detailArr = [NSMutableArray array];
    NSString *createTime = [NSDate getRecentTimeWithTime:getTimeWithTime(self.model.createTime)];
    NSString *modifyTime = [NSDate getRecentTimeWithTime:getTimeWithTime(self.model.modifyTime)];

    [self.detailArr  addObject:formattedFileSize(self.model.size.integerValue)];
    if (self.model.pathDisplay.length>0) {
        [self.detailArr  addObject:self.model.pathDisplay];
    }
    if (self.model.createTime) {
        [self.detailArr  addObject:createTime];

    }else{
        [self.detailArr  addObject:@"--"];
    }
    
    if (self.model.modifyTime) {
        [self.detailArr  addObject:modifyTime];

    }else{
        [self.detailArr  addObject:@"--"];
    }
    [self.detailArr  addObject:self.model.createUserJson];
    if (self.model.modifyUserJson) {
        [self.detailArr  addObject:self.model.modifyUserJson?self.model.modifyUserJson:@""];
    }
    [self.detailArr  addObject:self.model.description?self.model.description:@""];
    if (!self.model.isFolder.boolValue) {
        [self.detailArr  addObject:self.model.hashMd5];
        [self.titleArr  addObject:@"MD5:"];
    }
    remark = self.model.description?self.model.description:@"";
    if (self.model.resolution && self.model.resolution.length>0) {
        [self.detailArr  addObject:self.model.resolution];
        [self.titleArr  addObject:KLanguage(@"画布尺寸:")];
    }
    
    if (self.model.tagList.count>0 ) {
        [self.titleArr  addObject:KLanguage(@"标签:")];
    }
    [((AppDelegate *)[UIApplication sharedApplication].delegate).watermarkView addWaterMarkView];

    // Do any additional setup after loading the view.
}
- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    //注册键盘出现通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidShow:) name:UIKeyboardDidShowNotification object:nil];
    // 注册键盘隐藏通知
    [[NSNotificationCenter defaultCenter] addObserver:self selector: @selector(keyboardDidHide:) name:UIKeyboardDidHideNotification object:nil];
}

-(void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:animated];
    // 注销键盘出现通知
    [[NSNotificationCenter defaultCenter]removeObserver:self name:UIKeyboardDidShowNotification object:nil];
    // 注销键盘隐藏通知
    [[NSNotificationCenter defaultCenter]removeObserver: self name:UIKeyboardDidHideNotification object: nil];
}

-(void)keyboardDidShow:(NSNotification *) notification{
    NSLog(@"键盘打开了");
}
-(void)keyboardDidHide: (NSNotification *) notification{
    NSLog(@"键盘关闭了");
    remark = [remark stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];

    if (remark.length==0) {
        return;
    }
    NSDictionary *dic;
    dic = @{
        @"desc":remark,
        @"operation":   @"desc",
        @"sourceID":self.model.sourceID,
    };
    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (self.saveAndRefreshBlock) {
            self.saveAndRefreshBlock();
        }
        
    }];
}

#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {

    return self.titleArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSString *title = self.titleArr[indexPath.row];

    if ([title isEqualToString:KLanguage(@"标签:")]) {
        FBTagCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBTagCell" forIndexPath:indexPath];
        cell.model = self.model;
        cell.gotoTagManageMent = ^{
            if (isPermissionWithModel(KLanguage(@"标签"), @[[VULFileObjectModel new]])) {
             if (self.gotoTagManageMent) {
                self.gotoTagManageMent();
            }
            }
        };
        return cell;

    }
    FBAttributeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBAttributeCell" forIndexPath:indexPath];
    cell.separatorLine.hidden = YES;
    if (indexPath.row ==2 ||[title isEqualToString:KLanguage(@"描述说明:")]) {
        cell.separatorLine.hidden = NO;
    }
    NSString *detial = self.detailArr[indexPath.row];
    cell.detialLabel.hidden = NO;
    cell.textField.hidden = YES;
    cell.editBtn.hidden = YES;

    if ([title isEqualToString:KLanguage(@"创建者:")] || [title isEqualToString:KLanguage(@"修改者:")]|| [title isEqualToString:KLanguage(@"描述说明:")]) {
        if ([title isEqualToString:KLanguage(@"描述说明:")]) {
            cell.titleLabel.text = title;

            [cell.userImageV mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.left.mas_equalTo(cell.titleLabel.mas_right).offset(fontAuto(10));
                make.centerY.mas_equalTo(cell.titleLabel.mas_centerY);
                make.height.mas_equalTo(fontAuto(0));
                make.width.mas_equalTo(fontAuto(0));
            }];
            cell.textField.text =detial;
            cell.detialLabel.hidden = YES;
            cell.textField.hidden = NO;
            cell.editBtn.hidden = NO;
            [[cell.textField rac_textSignal] subscribeNext:^(NSString *_Nullable x) {
                remark = x;
            }];

        }else{
            NSDictionary *dic = turnStringToDictionary(detial);
            cell.titleLabel.text = title;
            NSString *nickName = dic[@"nickname"];
            cell.detialLabel.text = [NSString stringWithFormat:@"[%@]",dic[@"name"]];
            if (nickName &&nickName.length>0) {
                cell.detialLabel.text = [NSString stringWithFormat:@"[%@]",nickName];
            }else{
                cell.detialLabel.text = [NSString stringWithFormat:@"[%@]",dic[@"name"]];
            }
            [cell.userImageV sd_setImageWithURL:[NSURL URLWithString:resultsUrl(dic[@"avatar"])] placeholderImage:VULGetImage(@"placeholder_face")];
            [cell.userImageV mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.left.mas_equalTo(cell.titleLabel.mas_right).offset(fontAuto(10));
                make.centerY.mas_equalTo(cell.titleLabel.mas_centerY);
                make.height.mas_equalTo(fontAuto(20));
                make.width.mas_equalTo(fontAuto(20));
            }];
        }
    

    }else{
        [cell.userImageV mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(cell.titleLabel.mas_right).offset(fontAuto(10));
            make.centerY.mas_equalTo(cell.titleLabel.mas_centerY);
            make.height.mas_equalTo(fontAuto(0));
            make.width.mas_equalTo(fontAuto(0));
        }];
        cell.titleLabel.text = title;
        cell.detialLabel.text = detial;
    }

    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *headerView = [UIView new];
    return headerView;
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    NSString *title = self.titleArr[indexPath.row];

    if ([title isEqualToString:KLanguage(@"标签:")]) {
        FBFileTagView *bossView =  [[FBFileTagView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth , 0)];
        for (NSDictionary *dic in self.model.tagList ) {
            FBTagModel *model = [FBTagModel modelWithDictionary:dic];
            [bossView addTag:model];
        }
        return fontAuto(60)+bossView.tagCollectionView.contentSize.height;
    }
    return fontAuto(50);
}

- (UITableViewStyle )tableViewStyle {
    return UITableViewStyleGrouped;
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
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
#pragma mark - JXCategoryListContentViewDelegate
- (UIView *)listView {
    return self.view;
}
@end
