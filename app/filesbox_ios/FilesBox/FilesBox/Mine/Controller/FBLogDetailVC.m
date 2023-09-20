//
//  FBLogDetailVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/19.
//

#import "FBLogDetailVC.h"
#import "FBChooseBtnView.h"
#import "FBLogCell.h"
#import "VULChoseDateView.h"
@interface FBLogDetailVC ()
@property (nonatomic, strong) FBChooseBtnView *chooseBtn;
@property (nonatomic, retain) NSMutableArray *titleArr;
@property (nonatomic, assign) NSInteger timeIndex;
@property (nonatomic, strong) NSString *startDateStr;
@property (nonatomic, strong) NSString *endDateStr;

@end

@implementation FBLogDetailVC

- (void)viewDidLoad {
    [super viewDidLoad];
    NSDate *lastMonthDate = [[NSDate date] dateBySubtractingMonths:1];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    self.startDateStr = [formatter stringFromDate:[NSDate date]];
    self.endDateStr = [formatter stringFromDate:[NSDate date]];
    self.navigationView.hidden = YES;
    [self.view addSubview:self.chooseBtn];
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.mas_equalTo(0);
        make.top.mas_equalTo(fontAuto(46));
    }];
    self.titleArr = [NSMutableArray array];
    [self.tableView registerClass:[FBLogCell class] forCellReuseIdentifier:@"FBLogCell"];
    self.tableView.estimatedRowHeight = fontAuto(100);
    self.timeIndex = 2;
    [self setupRefreshFooter];
    [self setupRefreshHeader];
    [self topRefreshing];
    // Do any additional setup after loading the view.
}
-(void)topRefreshing{
    [super topRefreshing];
    [self.titleArr removeAllObjects];
    [self getLog];
}
- (void)bottomRefreshing {
    self.pageIndex ++;
    [self getLog];
}
-(NSDictionary *)chooseTime{
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    NSTimeZone *zone = [NSTimeZone systemTimeZone];
    formatter.timeZone = zone;
    [formatter setDateStyle:NSDateFormatterMediumStyle];
    [formatter setTimeStyle:NSDateFormatterShortStyle];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    NSDate * date = [NSDate date];//当前时间
    if(self.timeIndex == 1){
        NSDate *lastDay = [NSDate dateWithTimeInterval:-24*60*60 sinceDate:date];//前一天

        [dic setValue:[formatter stringFromDate:lastDay] forKey:@"timeFrom"];
        [dic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
    }else if(self.timeIndex == 0){
        [dic setValue:[formatter stringFromDate:date] forKey:@"timeFrom"];
        [dic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
    }else if(self.timeIndex == 2){
        NSDate *lastDay = [NSDate dateWithTimeInterval:-24*60*60*7 sinceDate:date];//前一天
        [dic setValue:[formatter stringFromDate:lastDay] forKey:@"timeFrom"];
        [dic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
    }else if(self.timeIndex == 3){
        NSDate *lastDay = [NSDate dateWithTimeInterval:-24*60*60*30 sinceDate:date];//前一天
        [dic setValue:[formatter stringFromDate:lastDay] forKey:@"timeFrom"];
        [dic setValue:[formatter stringFromDate:date] forKey:@"timeTo"];
    }else if(self.timeIndex == 5){
        [dic setValue:self.startDateStr forKey:@"timeFrom"];
        [dic setValue:self.endDateStr forKey:@"timeTo"];
    }
    return dic;
}
-(void)getLog{

    NSMutableDictionary *dic = [[NSMutableDictionary alloc] initWithDictionary:@{@"currentPage":@(self.pageIndex),@"pageSize":@"20"}];
    if(self.index == 0 || self.index == 1){
        [dic setValue:@"user.index.loginSubmit" forKey:@"logType"];
    }else{
        [dic setValue:@"file.upload,explorer.index.zipDownload,explorer.index.fileOut,explorer.index.fileDownload" forKey:@"logType"];
    }
    [dic addEntriesFromDictionary:[self chooseTime]];

    
    [VULBaseRequest requestWithUrl:@"api/disk/user/log/get" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        NSString *success =request.responseObject[@"success"];
        [self endRefreshing];
        if (success.boolValue) {
            
            NSArray *list = request.data[@"list"];
            for(NSDictionary *dic  in list){
                NSMutableArray *arr = [NSMutableArray array];
                NSString *createTime = dic[@"createTime"];
                NSString *desc = dic[@"desc"];

                
                NSDictionary *descDic =  turnStringToDictionary(desc);
                NSDictionary *logDescVo =  dic[@"logDescVo"];
                if(self.index == 0){
                    [arr addObject:[NSDate timeWithTimeIntervalString:getTimeWithTime(createTime) Format:@"yyyy-MM-dd HH:mm"]];
                    [arr addObject:descDic[@"op"]];
                    [arr addObject:descDic[@"browser"]];
                    [arr addObject:[NSString stringWithFormat:@"%@ %@",logDescVo[@"country"],logDescVo[@"network"]]];
                    [self.titleArr addObject:arr];

                }else if(self.index == 1){
                    [arr addObject:descDic[@"browser"]];
                    [arr addObject:descDic[@"op"]];
                    [arr addObject:[NSDate timeWithTimeIntervalString:getTimeWithTime(createTime) Format:@"yyyy-MM-dd HH:mm"]];
                    [arr addObject:[NSString stringWithFormat:@"%@ %@",logDescVo[@"country"],logDescVo[@"network"]]];
                    [self.titleArr addObject:arr];

                }else{
                    [arr addObject:[NSDate timeWithTimeIntervalString:getTimeWithTime(createTime) Format:@"yyyy-MM-dd HH:mm"]];
                    if([logDescVo.allKeys containsObject:@"pathDisplay"]){
                        [arr addObject:logDescVo[@"pathDisplay"]];
                    }else{
                        [arr addObject:@""];
                    }
                    
                    if([logDescVo.allKeys containsObject:@"status"]){
                        NSString *status = logDescVo[@"status"];
                        if(status.integerValue == 1){
                            NSString *type = logDescVo[@"type"];
                            if([type isEqualToString:@"upload" ]){
                                [arr addObject:KLanguage(@"上传成功")];
                            }else{
                                [arr addObject:KLanguage(@"下载成功")];
                            }
                        }else{
                            NSString *type = logDescVo[@"type"];
                            if([type isEqualToString:@"upload" ]){
                                [arr addObject:KLanguage(@"上传失败")];
                            }else{
                                [arr addObject:KLanguage(@"上传失败")];
                            }
                        }
                    }else{
                        NSString *type = logDescVo[@"type"];
                        if([type isEqualToString:@"upload" ]){
                            [arr addObject:KLanguage(@"上传成功")];
                        }else{
                            [arr addObject:KLanguage(@"下载成功")];
                        }
                    }

                    [arr addObject:logDescVo[@"pathName"]];
        
                    [self.titleArr addObject:arr];
                }
                
            }
            [self.tableView reloadData];
            
        }
            
    }];
}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
  
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if(self.titleArr.count ==0){
        return 0;
    }
    return self.titleArr.count+1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    FBLogCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBLogCell" forIndexPath:indexPath];
    cell.separatorLine.hidden = YES;
    if(indexPath.row == 0){
        cell.titleLabel.textColor  = HEXCOLOR(0x333333);
        cell.titleLabel1.textColor  = HEXCOLOR(0x333333);
        cell.titleLabel2.textColor  = HEXCOLOR(0x333333);
        cell.titleLabel3.textColor  = HEXCOLOR(0x333333);
        cell.bgView.backgroundColor = HEXCOLOR(0xf6f6f6);
        if(self.index == 0){
            cell.titleLabel.text = KLanguage(@"时间");
            cell.titleLabel1.text =KLanguage(@"系统");
            cell.titleLabel2.text =KLanguage(@"客户端");
            cell.titleLabel3.text =KLanguage(@"IP地址");
        }else if(self.index == 1){
            cell.titleLabel.text = KLanguage(@"名称");
            cell.titleLabel1.text =KLanguage(@"类型");
            cell.titleLabel2.text =KLanguage(@"最近登录时间");
            cell.titleLabel3.text =KLanguage(@"IP地址");
        }else{
            cell.titleLabel.text = KLanguage(@"时间");
            cell.titleLabel1.text =KLanguage(@"位置");
            cell.titleLabel2.text =KLanguage(@"状态");
            cell.titleLabel3.text =KLanguage(@"文件");
        }
        

    }else{
        cell.titleLabel.textColor  = HEXCOLOR(0x777777);
        cell.titleLabel1.textColor  = HEXCOLOR(0x777777);
        cell.titleLabel2.textColor  = HEXCOLOR(0x777777);
        cell.titleLabel3.textColor  = HEXCOLOR(0x777777);
        cell.bgView.backgroundColor = HEXCOLOR(0xfffffff);

        cell.titleLabel.text =self.titleArr[indexPath.row-1][0];
        cell.titleLabel1.text =self.titleArr[indexPath.row-1][1];
        cell.titleLabel2.text =self.titleArr[indexPath.row-1][2];
        cell.titleLabel3.text =self.titleArr[indexPath.row-1][3];

    }
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}


- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0.01;
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
-(FBChooseBtnView*)chooseBtn{
    if(!_chooseBtn){
        _chooseBtn = [[FBChooseBtnView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(46))];
        _chooseBtn.clickBtn = ^(NSInteger index) {
            if(index ==5){
                NSString *startStr = [NSString stringWithFormat:@"%ld",[NSDate getIntervalByTime:self.startDateStr withFomat:@"yyyy-MM-dd"]];
                NSString *endStr = [NSString stringWithFormat:@"%ld",[NSDate getIntervalByTime:self.endDateStr withFomat:@"yyyy-MM-dd"]];
                WeakSelf(self);

                VULChoseDateView *choseDateView = [[VULChoseDateView alloc] initWithStartDate:startStr EndDate:endStr];
                zhPopupController *popup = [[zhPopupController alloc] initWithView:choseDateView size:choseDateView.size];
                popup.layoutType = zhPopupLayoutTypeBottom;
                popup.presentationStyle = zhPopupSlideStyleFromBottom;
                popup.maskAlpha = 0.35;
                [popup showInView:kWindow duration:0.15 delay:0 options:UIViewAnimationOptionCurveEaseInOut bounced:NO completion:nil];
                
                choseDateView.choseDateBlock = ^(NSInteger startStamp, NSInteger endStamp) {
                    [popup dismiss];
                    if (endStamp<startStamp) {
                        [kWindow makeToast:KLanguage(@"结束时间不能小于开始时间")];
                        return;
                    }
                    weakself.startDateStr = [NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",startStamp] Format:@"yyyy-MM-dd"];
                    weakself.endDateStr = [NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",endStamp] Format:@"yyyy-MM-dd"];

                    self.timeIndex = index;
                    [self.tableView.mj_header beginRefreshing];
                       
                };
                popup.didDismissBlock = ^(zhPopupController * _Nonnull popupController) {
        
                };
                return;
            }
            self.timeIndex = index;
            [self.tableView.mj_header beginRefreshing];
    
        };
    }
    return _chooseBtn;
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
