//
//  FBShareVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/29.
//

#import "FBShareVC.h"
#import "FBShareTopCell.h"
#import "FBShareOpenCell.h"
#import "FBShareLineCell.h"
#import "FBShareTextCell.h"
#import "FBShareChooseView.h"
#import "VULShareSpecialCardView.h"
#import "VULCreateLinkCode.h"
@interface FBShareVC ()<DZNEmptyDataSetSource,DZNEmptyDataSetDelegate>
@property (nonatomic, strong) NSArray *titleArr;
@property (nonatomic, assign) BOOL isMore;
@property (nonatomic, strong) NSMutableArray *rightArr;
@property (nonatomic, strong) UIButton *modeBtn;
@property (nonatomic, strong) NSMutableDictionary *optionsDic;
@property (nonatomic, copy) NSString *timeStr;

@property (nonatomic,strong) VULButton *cancelBtn;
@property (nonatomic,strong) VULButton *sureBtn;
@end

@implementation FBShareVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.optionsDic = [NSMutableDictionary dictionary];
    NSString *shareHash = [NSString stringWithFormat:@"%@&&%@",self.model.sourceID,[VULRealmDBManager getUserId]];
    NSString *aesPwd = [NSString encyptPKCS5:shareHash WithKey:AESENCRYKEY];
    NSString *encode = [self URLEncodedString:aesPwd];//编码
    self.model.shareUrl = [NSString stringWithFormat:@"%@#/sharePage/%@",ChooseUrl,encode];
    self.isMore = NO;
    self.navigationTitle = KLanguage(@"外链分享");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.left.right.mas_equalTo(0);
        make.top.mas_equalTo(K_NavBar_Height);
    }];
    self.view.backgroundColor = HEXCOLOR(0xf2f2f2);
    self.tableView.backgroundColor = HEXCOLOR(0xf2f2f2);

    self.tableView.emptyDataSetSource = self;
    self.tableView.emptyDataSetDelegate = self;
    [self baseAddNavRightBtnWithTitle:KLanguage(@"保存") selector:@selector(clickSave) color:BtnColor];
    [self.tableView registerClass:[FBShareTopCell class] forCellReuseIdentifier:@"FBShareTopCell"];
    [self.tableView registerClass:[FBShareOpenCell class] forCellReuseIdentifier:@"FBShareOpenCell"];
    [self.tableView registerClass:[FBShareLineCell class] forCellReuseIdentifier:@"FBShareLineCell"];
    [self.tableView registerClass:[FBShareTextCell class] forCellReuseIdentifier:@"FBShareTextCell"];
    self.tableView.estimatedRowHeight = fontAuto(200);
    self.titleArr = @[KLanguage(@"分享标题:"),KLanguage(@"到期时间:"),KLanguage(@"下载次数:"),KLanguage(@"仅用户登录可用:"),KLanguage(@"禁止预览:")];
    self.rightArr = [NSMutableArray array];
    [self.rightArr addObject:self.model.name];
    [self.rightArr addObject:KLanguage(@"永久有效")];
    [self.rightArr addObject:KLanguage(@"不限制")];
//https://dev.filesbox.cn/#/sharePage/
    [self setDicFirst];
    UIView *new = [UIView new];
    new.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:new];
    [new mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.left.mas_equalTo(fontAuto(0));
        make.bottom.mas_equalTo(-fontAuto(0));
        make.height.mas_equalTo(fontAuto(38+30));
    }];
    [self.view addSubview:self.cancelBtn];
    [self.view addSubview:self.sureBtn];
    CGFloat width = (VULSCREEN_WIDTH -fontAuto(50+fontAuto(20)))/2;
    [self.cancelBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(25));
        make.bottom.mas_equalTo(-fontAuto(15));
        make.height.mas_equalTo(fontAuto(38));
        make.width.mas_equalTo(width);
    }];
    [self.sureBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.cancelBtn.mas_right).offset(fontAuto(20));
        make.bottom.mas_equalTo(-fontAuto(15));
        make.height.mas_equalTo(fontAuto(38));
        make.width.mas_equalTo(width);
    }];
    self.cancelBtn.hidden = YES;
    self.sureBtn.hidden = YES;

    if (self.model.isShare) {
        [self getList];

    }else{
        [self save];

    }

    // Do any additional setup after loading the view.
}

-(void)setDicFirst{
    [self.optionsDic setValue:@1 forKey:@"down"];
    [self.optionsDic setValue:@0 forKey:@"downNum"];
    [self.optionsDic setValue:@1 forKey:@"preview"];
    [self.optionsDic setValue:@0 forKey:@"upload"];
    [self.optionsDic setValue:@0 forKey:@"login"];

}
-(void)save{
//    sourceID + '&&' + userID
    NSString *aesPwd = [NSString encyptPKCS5:[NSString stringWithFormat:@"%@&&%@",self.model.sourceID,[VULRealmDBManager getUserId]] WithKey:AESENCRYKEY];
    self.model.options = @"";
    self.model.shareHash = aesPwd;
    self.model.shareTitle = self.model.name;
    self.model.timeTo = @"0";
    self.model.userID = [VULRealmDBManager getUserId];
    self.model.numDownload = @"0";
    self.model.numView = @"0";

    NSDictionary *dic = @{
        @"options":  self.model.options,
        @"password":@"",
        @"shareHash":aesPwd,
        @"sourceID":self.model.sourceID,
        @"timeTo":@0,
        @"title":self.model.name,

    };
    
    [VULBaseRequest requestWithUrl:@"/api/disk/userShare/save" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (request.success) {
            self.sureBtn.hidden = NO;


        } else
            [self makeToast:request.message];
   
    }];
}
/**
 *  URLEncode
 */
- (NSString *)URLEncodedString:(NSString *)str
{
    // CharactersToBeEscaped = @":/?&=;+!@#$()~',*";
    // CharactersToLeaveUnescaped = @"[].";
    
    NSString *unencodedString = str;
    NSString *encodedString = (NSString *)
    CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                              (CFStringRef)unencodedString,
                                                              NULL,
                                                              (CFStringRef)@"!*'();:@&=+$,/?%#[]",
                                                              kCFStringEncodingUTF8));
    
    return encodedString;
}





-(void)getList{
//
    [VULBaseRequest requestWithUrl:@"/api/disk/userShare/get" params:@{@"sourceID":self.model.sourceID} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (request.success) {
            self.model.options = request.data[@"options"];
            self.model.passWord = request.data[@"password"];
            self.model.shareHash = request.data[@"shareHash"];
            self.model.shareID = request.data[@"shareID"];
            self.model.userID = [VULRealmDBManager getUserId];
            self.model.shareTitle = request.data[@"title"];
            self.model.timeTo = request.data[@"timeTo"];
            self.model.numDownload = request.data[@"numDownload"];
            self.model.numView = request.data[@"numView"];
            [self.optionsDic addEntriesFromDictionary:turnStringToDictionary(    self.model.options)];
            NSString *down =  [NSString stringWithFormat:@"%@",self.optionsDic[@"down"]];
            NSString *downNum =  [NSString stringWithFormat:@"%@",self.optionsDic[@"downNum"]];

            if (down.integerValue  == 1) {
                if (downNum.integerValue>0) {
                    [self.rightArr replaceObjectAtIndex:2 withObject: [NSString stringWithFormat:@"%@",downNum]];
                }
                
            }else{
                [self.rightArr replaceObjectAtIndex:2 withObject: KLanguage(@"禁止下载")];

            }
            if (self.model.timeTo.integerValue == 0) {
                [self.rightArr replaceObjectAtIndex:1 withObject:KLanguage(@"永久有效")];

            }else{
                NSInteger count =[self getDifferenceByDate:[NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",self.model.timeTo.integerValue*1000] Format:@"yyyy-MM-dd HH:mm:ss"]] ;
                if (count == 7) {
                    [self.rightArr replaceObjectAtIndex:1 withObject:@"7"];

                }else if(count == 14){
                    [self.rightArr replaceObjectAtIndex:1 withObject:@"14"];

                }else{
                    
                    self.timeStr =self.model.timeTo;
                    [self.rightArr replaceObjectAtIndex:1 withObject:[NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",self.model.timeTo.integerValue*1000] Format:@"yyyy-MM-dd"]];
                }
            }
            


            [self.optionsDic setValue:@1 forKey:@"down"];
            
            self.cancelBtn.hidden = NO;
            self.sureBtn.hidden = NO;

            [self.tableView reloadData];
            
        } else
            [self makeToast:request.message];
   
    }];
}
-(NSInteger)getDifferenceByDate:(NSString *)date {
       //获得当前时间
   NSDate *now = [NSDate date];
       //实例化一个NSDateFormatter对象
   NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
       //设定时间格式
   [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
   NSDate *oldDate = [dateFormatter dateFromString:date];
   NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
   unsigned int unitFlags = NSDayCalendarUnit;
   NSDateComponents *comps = [gregorian components:unitFlags fromDate:oldDate  toDate:now  options:0];
   return [comps day];
}
-(void)clickSave{
    [self saveBackSucess:^(VULBaseRequest * _Nonnull request) {
        if (request.success) {
            if (self.saveAndRefreshBlock) {
                self.saveAndRefreshBlock();
            }
            [self.navigationController popViewControllerAnimated:YES];

        } else
            [self makeToast:request.message];
    }];
}
-(void)saveWithOperation:(BOOL)flag{

    
    [self saveBackSucess:^(VULBaseRequest * _Nonnull request) {
        if (request.success) {
            NSString *string = [NSString stringWithFormat:@"%@：%@\n%@：%@",KLanguage(@"分享名称"),self.model.name,KLanguage(@"分享链接"),self.model.shareUrl];

            if (self.model.passWord &&self.model.passWord.length>0) {
                string = [NSString stringWithFormat:@"%@：%@\n%@：%@\n%@：%@",KLanguage(@"分享名称"),self.model.shareTitle,KLanguage(@"分享链接"),self.model.shareUrl,KLanguage(@"访问密码"),self.model.passWord];
            }
            
            if (flag) {
                VULBaseWebViewVC *vc = [VULBaseWebViewVC new];
                vc.currentURL = self.model.shareUrl;
                [self.navigationController pushViewController:vc animated:YES];
            }else{
                [[UIPasteboard generalPasteboard] setString:string];
                [kWindow makeToast:KLanguage(@"复制成功")];
            }

        } else
            [self makeToast:request.message];
    }];
    
}
-(void)saveBackSucess:(nullable void (^)(VULBaseRequest * _Nonnull))request1{
    self.model.passWord = [self.model.passWord stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    NSDictionary *dic = @{
        @"options": [self gs_jsonStringCompactFormatForNSArray:self.optionsDic],
        @"password": self.model.passWord.length>0?self.model.passWord:@"",
        @"shareHash": self.model.shareHash,
        @"sourceID":self.model.sourceID,
        @"timeTo": self.model.timeTo,
        @"title": self.model.shareTitle,

    };
    
    [VULBaseRequest requestWithUrl:@"/api/disk/userShare/save" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if(request1){
            request1(request);
        }
   
   
    }];
}
- (NSString *)gs_jsonStringCompactFormatForNSArray:(NSDictionary *)arrJson {

if (![arrJson isKindOfClass:[NSDictionary class]] || ![NSJSONSerialization isValidJSONObject:arrJson]) {

return nil;

}

NSData *jsonData = [NSJSONSerialization dataWithJSONObject:arrJson options:0 error:nil];

NSString *strJson = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

return strJson;

}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    if (  !self.isMore) {
        return 2;
    }
    return 3;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 2) {
        return  5;
    }
    return 1;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (indexPath.section == 0) {
        FBShareTopCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBShareTopCell" forIndexPath:indexPath];
        cell.model =self.model;
        return cell;
        
    }
    else if(indexPath.section == 1){
        FBShareLineCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBShareLineCell" forIndexPath:indexPath];
        cell.model =self.model;
        cell.clickShareOrCopy = ^(BOOL flag) {
            [self saveWithOperation:flag];
        };
        [[RACSignal merge:@[cell.codeTextField.rac_textSignal, RACObserve(cell.codeTextField, text)]] subscribeNext:^(NSString* text) {
            if (text.length<=6) {
            }else{
                cell.codeTextField.text = [text substringToIndex:6];
            }
            self.model.passWord =  cell.codeTextField.text;
        }];
        return cell;
    }else{
        FBShareTextCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FBShareTextCell" forIndexPath:indexPath];
        cell.titleLabel.text = self.titleArr[indexPath.row];
        if (indexPath.row<3) {
            cell.codeTextField.hidden = NO;
            cell.rightImageV.hidden = NO;
            cell.openSwitch.hidden = YES;

            cell.rightImageV.image = indexPath.row == 0?VULGetImage(@"icon_edit"):VULGetImage(@"icon_right_into");
            cell.codeTextField.userInteractionEnabled = indexPath.row == 0;
            cell.codeTextField.text = self.rightArr[indexPath.row];
          ;

            if (indexPath.row == 0) {
                cell.rightImageV.hidden = YES;

                [[cell.codeTextField rac_textSignal] subscribeNext:^(NSString *_Nullable x) {
                    self.model.shareTitle =  cell.codeTextField.text;
                }];
            }
          
        }else{
            cell.openSwitch.hidden = NO;
            cell.codeTextField.hidden = YES;
            cell.rightImageV.hidden = YES;
            if (indexPath.row ==3) {
                NSString *login = [NSString stringWithFormat:@"%@",self.optionsDic[@"login"]];
                
                NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"markConfig"];
                NSString *shareLinkAllowGuest = roleDic[@"shareLinkAllowGuest"];
                if(!shareLinkAllowGuest.boolValue){
                    cell.openSwitch.enabled = NO;
                    [cell.openSwitch setOn:YES];

                }else{
                    cell.openSwitch.enabled = YES;
                    [cell.openSwitch setOn:login.boolValue];
                }
    
            }else{
                cell.openSwitch.enabled = YES;
                NSString *preview = [NSString stringWithFormat:@"%@",self.optionsDic[@"preview"]];
                [cell.openSwitch setOn:!preview.boolValue];
            }
            [[cell.openSwitch rac_signalForControlEvents:UIControlEventValueChanged] subscribeNext:^(__kindof UIControl *_Nullable x) {
                BOOL value = ((UISwitch *)x).on;
                if (indexPath.row ==3) {
                    [self.optionsDic setValue:value?@1:@0 forKey:@"login"];
                }else{
                    [self.optionsDic setValue:value?@0:@1 forKey:@"preview"];

                }

            }];
        }
        return cell;
    }
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray *titleArr;
    if (indexPath.section ==2&&indexPath.row ==1) {
        titleArr = @[KLanguage(@"7天"), KLanguage(@"14天"), KLanguage(@"永久有效"), KLanguage(@"设置到期时间")];
        FBShareChooseView *top = [[FBShareChooseView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, titleArr.count*BtnCell+K_BottomBar_Height+50)];
        top.titleLabel.text = KLanguage(@"到期时间");
        NSString *title = self.rightArr[indexPath.row];
        if ([title isEqualToString:KLanguage(@"7天")]) {
            top.row = 0;
        }else if ([title isEqualToString:KLanguage(@"14天")]) {
            top.row = 1;
        }else  if ([title isEqualToString:KLanguage(@"永久有效")]) {
            top.row = 2;
        }else{
            top.onceStr = title;
            top.timeStr = self.timeStr  ;
            top.row = 3;
        }
        top.listArray =  [NSMutableArray arrayWithArray:titleArr];
        zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
        popup2.layoutType = zhPopupLayoutTypeBottom;
        popup2.presentationStyle = zhPopupSlideStyleFromBottom;
        popup2.maskAlpha = 0.35;
        top.clickCell = ^(NSString * _Nonnull title, NSString * _Nonnull onceStr, NSString * _Nonnull timeStr) {
    
            
            if ([title isEqualToString:KLanguage(@"永久有效")]) {
                self.model.timeTo = @"0";
                [self.rightArr replaceObjectAtIndex:1 withObject:title];
     
            }
            if ([title isEqualToString:KLanguage(@"7天")]) {
                NSDate *date = [self getDate:[NSDate date] day:7];
                NSTimeInterval currentTime = [date timeIntervalSince1970];
                self.model.timeTo = [NSString stringWithFormat:@"%.0f",currentTime];
                [self.rightArr replaceObjectAtIndex:1 withObject:title];

            }
            if ([title isEqualToString:KLanguage(@"14天")]) {
                NSDate *date = [self getDate:[NSDate date] day:14];
                NSTimeInterval currentTime = [date timeIntervalSince1970];
                self.model.timeTo = [NSString stringWithFormat:@"%.0f",currentTime];
                [self.rightArr replaceObjectAtIndex:1 withObject:title];

            }
            if ([title isEqualToString:KLanguage(@"设置到期时间")]) {
                
                self.model.timeTo = timeStr;
                self.timeStr =timeStr;
                [self.rightArr replaceObjectAtIndex:1 withObject:[NSDate timeWithTimeIntervalString:[NSString stringWithFormat:@"%ld",timeStr.integerValue*1000] Format:@"yyyy-MM-dd HH:mm:ss"]];

            }
            [self.tableView reloadData];
            

            [popup2 dismiss];
        };
       [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
    }
    if (indexPath.section ==2&&indexPath.row ==2) {
        titleArr = @[KLanguage(@"不限制"), KLanguage(@"禁止下载"), KLanguage(@"自定义次数")];
        FBShareChooseView *top = [[FBShareChooseView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, titleArr.count*BtnCell+K_BottomBar_Height+50)];
        top.titleLabel.text = KLanguage(@"下载权限");
        top.row = 0;
        NSString *title = self.rightArr[indexPath.row];
        if ([title isEqualToString:KLanguage(@"不限制")]) {
            top.row = 0;
        }else if ([title isEqualToString:KLanguage(@"禁止下载")]) {
            top.row = 1;
        }else{
            top.onceStr = title;
            top.row = 2;
        }
        top.listArray =  [NSMutableArray arrayWithArray:titleArr];
        zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
        popup2.layoutType = zhPopupLayoutTypeBottom;
        popup2.presentationStyle = zhPopupSlideStyleFromBottom;
        popup2.maskAlpha = 0.35;
        top.clickCell = ^(NSString * _Nonnull title, NSString * _Nonnull onceStr, NSString * _Nonnull timeStr) {
            
            if ([title isEqualToString:KLanguage(@"不限制")]) {
                [self.rightArr replaceObjectAtIndex:2 withObject:title];

                [self.optionsDic setValue:@1 forKey:@"down"];
                [self.optionsDic setValue:@0 forKey:@"downNum"];
            }
            if ([title isEqualToString:KLanguage(@"禁止下载")]) {
                [self.optionsDic setValue:@0 forKey:@"down"];
                [self.optionsDic setValue:@0 forKey:@"downNum"];
                [self.rightArr replaceObjectAtIndex:2 withObject:title];

            }
            if ([title isEqualToString:KLanguage(@"自定义次数")]) {
                
                if (!onceStr) {
                    onceStr = @"0";
                }
                [self.optionsDic setValue:onceStr.integerValue>0? @1:@0 forKey:@"down"];
                [self.optionsDic setValue:onceStr forKey:@"downNum"];
                [self.rightArr replaceObjectAtIndex:2 withObject:onceStr];
            }
            [self.tableView reloadData];

            [popup2 dismiss];
        };
       [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
    }
    
    
}

- (NSDate *)getDate:(NSDate *)currentDate day:(NSInteger)day {
    NSTimeInterval days = 24 * 60 * 60 * day; // 一天一共有多少秒
    NSDate *appointDate = [currentDate dateByAddingTimeInterval:days];
    return appointDate;
    
}
- (nullable UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
    if (section ==1) {
        UIView *headerView = [UIView new];
        headerView.backgroundColor = HEXCOLOR(0xf2f2f2);
        [headerView addSubview:self.modeBtn];
        [self.modeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.centerY.mas_equalTo(headerView.mas_centerY);
            make.centerX.mas_equalTo(headerView.mas_centerX);

        }];
        return headerView;
    }
    UIView *headerView = [UIView new];
    headerView.backgroundColor = HEXCOLOR(0xececec);
    
    return headerView;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *headerView = [UIView new];
    headerView.backgroundColor = HEXCOLOR(0xf2f2f2);
    
    return headerView;
}
-(void)clickMore:(UIButton *)sender{
    sender.selected = !sender.selected;
    self.isMore = !self.isMore;
    [self.tableView reloadData];
}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    if (section ==0 || section ==2) {
        return 0.01;
    }
 
    return 10;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    if (section ==1) {
        return 50;

    }
    return 0.01;

}




- (UITableViewStyle )tableViewStyle {
    return UITableViewStylePlain;
}


- (CGFloat)verticalOffsetForEmptyDataSet:(UIScrollView *)scrollView {
    return 0.1f;
}

- (UIImage *)imageForEmptyDataSet:(UIScrollView *)scrollView {
    return [UIImage imageNamed:@"no_data"];
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
-(UIButton *)modeBtn{
    if (!_modeBtn) {
        _modeBtn = [UIButton new];
        UIImage *image = [self image:VULGetImage(@"icon_right_into") rotation:UIImageOrientationLeft];
        [_modeBtn setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
        UIImage *image1 = [self image:VULGetImage(@"icon_right_into") rotation:UIImageOrientationRight];
        [_modeBtn setImage:[image1 imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateSelected];
        [_modeBtn.imageView setTintColor:BtnColor];
        [_modeBtn setTitle:KLanguage(@"更多设置") forState:UIControlStateNormal];
        [_modeBtn setSemanticContentAttribute:UISemanticContentAttributeForceRightToLeft];
        _modeBtn.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(14)];

        [_modeBtn setTitleColor:BtnColor forState:UIControlStateNormal];
        [_modeBtn addTarget:self action:@selector(clickMore:) forControlEvents:UIControlEventTouchUpInside];
    }
    return _modeBtn;
}

- (VULButton *)cancelBtn {
    if (!_cancelBtn) {
    
        _cancelBtn = [VULButton new];
        _cancelBtn.frame = CGRectZero;
        [_cancelBtn setTitle:KLanguage(@"取消分享") forState:UIControlStateNormal];
        _cancelBtn.layer.masksToBounds = YES;
        _cancelBtn.layer.cornerRadius = fontAuto(19);
        _cancelBtn.layer.borderColor = HEXCOLOR(0xDCDEE0).CGColor;
        _cancelBtn.layer.borderWidth =1;
        _cancelBtn.titleLabel.font =[UIFont yk_pingFangRegular:fontAuto(14)];
        [_cancelBtn setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        [_cancelBtn addTarget:self action:@selector(hiddenView) forControlEvents:UIControlEventTouchUpInside];

    }
    return _cancelBtn;
}
-(void)hiddenView{
    
    [self vul_showAlertWithTitle:KLanguage(@"确定取消该外链分享？取消后外链将失效！")   message:@"" appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
        alertMaker.
        addActionCancelTitle(KLanguage(@"取消")).
        addActionDestructiveTitle(KLanguage(@"确定"));
    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
        if (buttonIndex == 1) {
           NSDictionary  *param = @{@"shareID":self.model.shareID};
            [VULBaseRequest requestWithUrl:@"/api/disk/userShare/cancel" params:param requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                if (request.success) {
                    if (self.saveAndRefreshBlock) {
                        self.saveAndRefreshBlock();
                    }
                    [self.navigationController popViewControllerAnimated:YES];
                } else {
                    [self makeToast:request.responseObject[@"message"]];
                }
            }];
        }
    }];
}

- (VULButton *)sureBtn {
    if (!_sureBtn) {
        _sureBtn = [VULButton getCustomBtnWithFrame:CGRectZero title:KLanguage(@"立即分享") Font:14 Bgcolor:BtnColor Target:self action:@selector(sureBtnClicked)];
        _sureBtn.layer.cornerRadius = fontAuto(19);
        [_sureBtn setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
    }
    return _sureBtn;
}
-(void)sureBtnClicked{
    [self saveBackSucess:^(VULBaseRequest * _Nonnull) {
        VULShareSpecialCardView *add = [[VULShareSpecialCardView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(300))];
         zhPopupController *popup = [[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
         popup.layoutType = zhPopupLayoutTypeBottom;
         popup.presentationStyle = zhPopupSlideStyleFromBottom;
         popup.maskAlpha = 0.35;
         [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
     
      add.menuViewBtnClickedBlock = ^(NSInteger index) {
          NSString *string = [NSString stringWithFormat:@"%@：%@\n%@：%@",KLanguage(@"分享名称"),self.model.shareTitle,KLanguage(@"分享链接"),self.model.shareUrl];
          if (self.model.passWord &&self.model.passWord.length>0) {
              string= [NSString stringWithFormat:@"%@：%@\n%@：%@\n%@：%@",KLanguage(@"分享名称"),self.model.shareTitle,KLanguage(@"分享链接"),self.model.shareUrl,KLanguage(@"访问密码"),self.model.passWord];
          }
          [popup dismiss];

          if (index ==0) {
              [[WXApiManger shareInstance] shareFileWithString:string scene:WXSceneSession];
              return;
 
          }
          if (index ==1) {
              [[WXApiManger shareInstance] shareFileWithString:string scene:WXSceneTimeline];
              return;
 
          }
          if (index ==2) {
              [[WXApiManger shareInstance] shareDingdingWithString:string];
              return;
 
          }
          if (index ==3) {
              [[WXApiManger shareInstance] shareWeiboWithString:string];
              return;
 
          }
          if (index ==4) {
              VULCreateLinkCode *linkCode = [[VULCreateLinkCode alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH * 0.9, VULSCREEN_WIDTH * 0.9)];
              zhPopupController *popup = [[zhPopupController alloc] initWithView:linkCode size:CGSizeMake(VULSCREEN_WIDTH * 0.9, linkCode.height)];
              popup.layoutType = zhPopupLayoutTypeCenter;
              popup.presentationStyle = zhPopupSlideStyleFromBottom;
              popup.maskAlpha = 0.35;
              linkCode.url =self.model.shareUrl;
              linkCode.dismiss = ^{
                  [popup dismiss];
              };
              [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
      

              return;
 
          }
          if (index ==5) {
              [[UIPasteboard generalPasteboard] setString:string];
              [kWindow makeToast:KLanguage(@"复制成功")];
              return;
          }
          if (index ==6) {
              VULBaseWebViewVC *vc = [VULBaseWebViewVC new];
              vc.currentURL = self.model.shareUrl;
              [self.navigationController pushViewController:vc animated:YES];
              return;
          }

              if (index ==7) {
              NSString *conl =string ;
              NSURL *shareURL = [NSURL URLWithString:self.model.shareUrl];
              UIActivityViewController *activity = [[UIActivityViewController alloc] initWithActivityItems:@[shareURL,conl] applicationActivities:nil];
              UIPopoverPresentationController *popover = activity.popoverPresentationController;
              if (popover) {
    //              popover.sourceView = sheetItem;
    //              popover.sourceRect = CGRectMake(VULSCREEN_WIDTH_VARIABLE / 2, VULSCREEN_HEIGHT_VARIABLE - sheetItem.frame.size.height - 70, 1, 1);
    //              popover.permittedArrowDirections = UIPopoverArrowDirectionAny;
              }
              [self presentViewController:activity animated:YES completion:NULL];

              return;
          }
     
         };
        
        // 发送到聊天界面  WXSceneSession
        // 发送到朋友圈    WXSceneTimeline
        // 发送到微信收藏  WXSceneFavorite
      
    }];

    

        //
}
//确定取消该外链分享？取消后外链将失效！
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
