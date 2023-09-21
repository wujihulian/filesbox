//
//  FBDiscussionVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/8/28.
//

#import "FBDiscussionVC.h"
#import "YKChatToolBoxView.h"
#import "VULJLTextCell.h"
#import "UITableView+FDTemplateLayoutCell.h"
#import "VULMeTitleCell.h"
#import "VULTMeImageViewCell.h"
#import "VULImageViewSeviceCell.h"
#import "FBHomeViewController.h"

static NSString *const jlTextCell = @"VULJLTextCell";
static NSString *const meTitleCell = @"VULMeTitleCell";
static NSString *const meImageViewCell = @"VULTMeImageViewCell";
static NSString *const imageViewSeviceCell = @"VULImageViewSeviceCell";
@interface FBDiscussionVC ()<ICChatBoxViewDelegate,YKChatBoxMoreViewDelegate>
@property (nonatomic, strong) UIImageView *iconImageV;
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *detiallLabel;
@property (nonatomic, strong) UIView *lineV;
@property (nonatomic, strong) UIView *topView;
@property (nonatomic, strong) YKChatToolBoxView *chatBox;
@property (nonatomic, strong) NSMutableArray *dataArray;
@property (nonatomic, strong) TZImagePickerUtil *imagePicker;
@property (nonatomic, strong) TZImagePickerUtil *imagePickerPhoto;
@property (nonatomic, assign) NSInteger total;

@end

@implementation FBDiscussionVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.dataArray = [NSMutableArray array];
    self.navigationView.hidden = YES;

    [self.view addSubview:self.topView];
    [self.topView addSubview:self.iconImageV];
    [self.topView addSubview:self.titleLabel];
    [self.topView addSubview:self.detiallLabel];
    [self.topView addSubview:self.lineV];
    [self.topView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(0);
        make.right.left.mas_equalTo(fontAuto(0));
        make.height.mas_equalTo(fontAuto(40));
    }];
    [self.iconImageV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(12));
        make.centerY.mas_equalTo(self.topView.mas_centerY);
    }];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.iconImageV.mas_right).offset(fontAuto(5));
        make.centerY.mas_equalTo(self.topView.mas_centerY);
    }];
    [self.detiallLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.titleLabel.mas_right).offset(fontAuto(5));
        make.centerY.mas_equalTo(self.topView.mas_centerY);
    }];
    [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo(-1);
        make.height.mas_equalTo(1);
        make.left.right.mas_equalTo(0);
    }];

    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo(-49-K_BottomBar_Height);
        make.top.mas_equalTo(fontAuto(40));
        make.left.right.mas_equalTo(0);
    }];
    self.tableView.height =  kScreenHeight-self.topH-fontAuto(40)-K_BottomBar_Height-49;

    
    [self.tableView registerClass:[VULMeTitleCell class] forCellReuseIdentifier:meTitleCell];
    [self.tableView registerClass:[VULTMeImageViewCell class] forCellReuseIdentifier:meImageViewCell];
    [self.tableView registerClass:[VULImageViewSeviceCell class] forCellReuseIdentifier:imageViewSeviceCell];
    [self.tableView registerClass:[VULJLTextCell class] forCellReuseIdentifier:jlTextCell];

   
    
    [self.view addSubview:self.chatBox];

    if(!isPermissionWithModel(KLanguage(@"编辑"), @[self.model])){
        self.chatBox.hidden = YES;
    }else{
        self.chatBox.hidden = NO;
        [_chatBox.chatToolBar.textView becomeFirstResponder];
//        [NSThread sleepForTimeInterval:0.24]; // 让主线程睡眠1秒

    }
   
    [self setupRefreshHeader];
//    [self setupRefreshFooter];
    //获取最新数据
    
    NSDictionary *dic = @{
        @"currentPage": @(self.pageIndex),
        @"pageSize": @(20),
        @"targetID": self.model.sourceID,

    };

    [VULBaseRequest requestWithUrl:@"/api/disk/comment/list" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        NSString *total1 = request.data[@"total"];
        if(total1.integerValue%20 ==0){
            self.pageIndex = total1.integerValue/20;
        }else{
            self.pageIndex = total1.integerValue/20+1;
        }
        [self requestList];

    }];

  
    // Do any additional setup after loading the view.
}
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [self.chatBox mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo(0);
        make.top.mas_equalTo(0);
        make.left.right.mas_equalTo(0);
    }];
    // 在视图已经显示时执行操作
}
- (void)topRefreshing {
    if(self.pageIndex>1){
        self.pageIndex = self.pageIndex-1;
        [self requestList];
    }else{
        [self dissmissHudView];
        [self endRefreshing];
    }
}


-(void)requestList{
    NSDictionary *dic = @{
        @"currentPage": @(self.pageIndex),
        @"pageSize": @(20),
        @"targetID": self.model.sourceID,

    };

    [VULBaseRequest requestWithUrl:@"/api/disk/comment/list" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self dissmissHudView];
        [self endRefreshing];
        if (request.success) {
            //TODO: 获取登录信息
            NSArray *list = request.data[@"list"];
            NSMutableArray *arr = [NSMutableArray array];
            for(NSDictionary *dic in list){
                VULServiceChatMessageModel *model = [VULServiceChatMessageModel new];
                NSString *content = dic[@"content"];
                NSString *userID = dic[@"userID"];
                NSString *userID1 = [VULRealmDBManager getUserId];

                if ([content rangeOfString:@"<img"].location != NSNotFound) {
                    model.dType = @"1";
                     NSError *error = nil;
                           NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:@"<img\\s+src=\"([^\"]+)\"" options:0 error:&error];
                           
                NSTextCheckingResult *match = [regex firstMatchInString:content options:0 range:NSMakeRange(0, [content length])];
                           if (match) {
                               NSRange srcRange = [match rangeAtIndex:1];
                               NSString *src = [content substringWithRange:srcRange];
                               NSLog(@"src: %@", src);
                               model.msg = src;
                    }
                } else {
                   model.dType = @"0";
                    model.msg = content;

                }
                if(userID.integerValue== userID1.integerValue){
                    model.isMe = YES;
                }else{
                    model.isMe = NO;

                }
                NSString *string =dic[@"nickname"];
                if(string.length==0){
                    string = dic[@"name"];
                }
                if(string.length>5){
                    string = [string substringToIndex:5];
                }
                model.avatar = dic[@"avatar"];
                model.timestamp = string;
                model.commentID = dic[@"commentID"];

                [arr addObject:model];
            }
            [self.dataArray insertObjects:arr atIndexes:[NSIndexSet indexSetWithIndexesInRange:NSMakeRange(0, arr.count)]];
            NSString *total1 = request.data[@"total"];
            self->_total = total1.integerValue;
            
            self.detiallLabel.text = [NSString stringWithFormat:@"(%@%@)",total1,KLanguage(@"条记录")];
            [self.tableView reloadData];
            [self scrollToBottom];
            [VULQueue executeInMainQueue:^{
                [_chatBox.chatToolBar.textView resignFirstResponder];
            }];

        }
   
    }];
}
#pragma mark - tableviewDelegate , tableviewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {

    return self.dataArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    
    VULServiceChatMessageModel *model = self.dataArray[indexPath.section];
    if (model.isMe) {
     
        if (model.dType.intValue == 0 ) {
            VULMeTitleCell *videoCell = [tableView dequeueReusableCellWithIdentifier:meTitleCell forIndexPath:indexPath];
            videoCell.selectionStyle = UITableViewCellSelectionStyleNone;
            videoCell.model = model;
            videoCell.deletCommentWithModel = ^(VULServiceChatMessageModel * _Nonnull model) {
                [self deletCommentWithModel:model];
            };
            return videoCell;
        }else  {
            VULTMeImageViewCell *videoCell = [tableView dequeueReusableCellWithIdentifier:meImageViewCell forIndexPath:indexPath];
            videoCell.selectionStyle = UITableViewCellSelectionStyleNone;
            videoCell.model = model;
            videoCell.deletCommentWithModel = ^(VULServiceChatMessageModel * _Nonnull model) {
                [self deletCommentWithModel:model];
            };
            return videoCell;
        }
    }else{
        if (model.dType.intValue == 0 ) {
            VULJLTextCell *videoCell = [tableView dequeueReusableCellWithIdentifier:jlTextCell forIndexPath:indexPath];
            videoCell.selectionStyle = UITableViewCellSelectionStyleNone;
            videoCell.model = model;
            videoCell.deletCommentWithModel = ^(VULServiceChatMessageModel * _Nonnull model) {
                [self deletCommentWithModel:model];
            };
            return videoCell;
        }else   {
            VULImageViewSeviceCell *videoCell = [tableView dequeueReusableCellWithIdentifier:imageViewSeviceCell forIndexPath:indexPath];
            videoCell.selectionStyle = UITableViewCellSelectionStyleNone;
            videoCell.model = model;
            videoCell.deletCommentWithModel = ^(VULServiceChatMessageModel * _Nonnull model) {
                [self deletCommentWithModel:model];
            };
            return videoCell;
        }
        
    }
}

-(void)deletCommentWithModel:(VULServiceChatMessageModel *)model{
    if(model.isMe){
        if(!isPermissionWithModel(KLanguage(@"编辑"), @[self.model])){
            [kWindow makeToast:KLanguage(@"暂无权限")];
            return;
        }
    }else{
        
        if(!isPermissionWithModel(KLanguage(@"管理权限"), @[self.model])){
            [kWindow makeToast:KLanguage(@"暂无权限")];
            return;
        }
    }
    
    
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:KLanguage(@"确定要删除吗？") message:nil preferredStyle:UIAlertControllerStyleAlert];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
             [VULBaseRequest requestWithUrl:@"/api/disk/comment/del" params:@{@"commentID":model.commentID} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                 if (request.success) {
                     NSMutableArray *allData = [NSMutableArray array];
                     for(VULServiceChatMessageModel *mode in self.dataArray){
                         if(model.commentID.intValue != mode.commentID.intValue){
                             [allData addObject:mode];
                         }
                         
                     }
                     [self.dataArray removeAllObjects];
                     [self.dataArray addObjectsFromArray:allData];
                     [self.tableView reloadData];
                     [self scrollToBottom];
                     [VULQueue executeInMainQueue:^{
                         [_chatBox.chatToolBar.textView resignFirstResponder];
                     }];
                 }else{
                    
                 }
              
             }];
        }]];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
                   NSLog(@"取消");
        }]];
        [self presentViewController:alertController animated:YES completion:nil];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {;
    return [UIView new];

}
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
 
    return  0.01f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    return  0.01f;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    VULServiceChatMessageModel *model = self.dataArray[indexPath.section];
    if (model.isMe) {
        if (model.dType.intValue == 0) {
            
            return [tableView fd_heightForCellWithIdentifier:meTitleCell configuration:^(VULMeTitleCell *cell) {
                cell.model = model;
            }];
        }else {
            return [tableView fd_heightForCellWithIdentifier:meImageViewCell configuration:^(VULMeTitleCell *cell) {
                cell.model = model;
            }];
        }
    }else{

         if (model.dType.intValue == 0) {
            
            return [tableView fd_heightForCellWithIdentifier:jlTextCell configuration:^(VULJLTextCell *cell) {
                cell.model = model;
            }];
        }else{
            return [tableView fd_heightForCellWithIdentifier:imageViewSeviceCell configuration:^(VULMeTitleCell *cell) {
                cell.model = model;
            }];
        }
    }

}

- (CGFloat)verticalOffsetForEmptyDataSet:(UIScrollView *)scrollView {
    return 0.1f;
}

- (UIImage *)imageForEmptyDataSet:(UIScrollView *)scrollView {
    return [UIImage imageNamed:@"no_data"];
}
- (void)scrollToBottomAnimated:(BOOL)animated {

    NSInteger rows = [self.tableView numberOfRowsInSection:0];
    if (rows > 0) {
        [self.tableView scrollToRow:rows - 1 inSection:0 atScrollPosition:UITableViewScrollPositionBottom animated:NO];
    }
}

- (void)scrollToBottom
{
    if (self.dataArray.count == 0) {
        return;
    }
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:self.dataArray.count-1];
    [self.tableView scrollToRowAtIndexPath:indexPath atScrollPosition:UITableViewRowAnimationTop animated:NO];
}
#pragma mark - YKChatBoxMoreViewDelegate
- (void)chatBoxMoreView:(YKChatBoxMoreView *)chatBoxMoreView didSelectItem:(YKChatBoxMoreItem *)item {
    if (item.tag < 102) {
        NSMutableDictionary *dic = backBlockAndFileType(KLanguage(@"图片"));
        FBHomeViewController *vc = [FBHomeViewController new];
        vc.isSelect = YES;
        vc.isHome = YES;
        vc.icon = dic[@"block"];
        vc.fileType = dic[@"fileType"];
        vc.selectModel = ^(VULFileObjectModel * _Nonnull model) {
     
                   // Create a mutable string for constructing the HTML
                   NSMutableString *htmlString = [NSMutableString string];
                   // Append the <img> tag with modified src attribute
                   [htmlString appendString:@"<img src=\""];
                   [htmlString appendString:model.path];
                   [htmlString appendString:@"\" />"];
            

            [self saveConent:htmlString];

        };
        [self presentViewController:vc animated:YES completion:nil];

    }
    
}
#pragma mark - ICChatBoxViewDelegate

- (void) chatBoxViewDidChangeChatBarFrame {
    [self scrollToBottom];
}

- (void)chatBoxView:(YKChatToolBoxView *)chatboxView
    sendTextMessage:(NSString *)messageStr {
    if (messageStr && messageStr.length > 0) {
        [self saveConent:messageStr];
    }
}

- (void) chatBoxView:(YKChatToolBoxView *)chatboxView
    sendVoiceMessage:(NSString *)voicePath {
}

- (void) chatBoxViewStartSelectImage {

}
-(void)saveConent:(NSString *)content{
    [VULBaseRequest requestWithUrl:@"/api/disk/comment/save" params:@{@"content":content,@"targetID":self.model.sourceID} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
//            [self topRefreshing];
            NSDictionary *dic = request.data;

            VULServiceChatMessageModel *model = [VULServiceChatMessageModel new];
            NSString *userID1 = [VULRealmDBManager getUserId];

            if ([content rangeOfString:@"<img"].location != NSNotFound) {
                model.dType = @"1";
                 NSError *error = nil;
                       NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:@"<img\\s+src=\"([^\"]+)\"" options:0 error:&error];
                       
            NSTextCheckingResult *match = [regex firstMatchInString:content options:0 range:NSMakeRange(0, [content length])];
                       if (match) {
                           NSRange srcRange = [match rangeAtIndex:1];
                           NSString *src = [content substringWithRange:srcRange];
                           NSLog(@"src: %@", src);
                           model.msg = src;
                }
            } else {
               model.dType = @"0";
                model.msg = content;

            }
            model.isMe = YES;
            VULSaveUserInformation *userInfo = [VULRealmDBManager getLocalUserInformational];
            NSString *string =userInfo.nickname.length>0?userInfo.nickname:userInfo.name;
        
            if(string.length>5){
                string = [string substringToIndex:5];
            }
            model.avatar = userInfo.avatar;
            model.timestamp = string;
            model.commentID = dic[@"commentID"];
            [self.dataArray addObject:model];
            _total = _total +1;
            self.detiallLabel.text = [NSString stringWithFormat:@"(%ld%@)",self->_total,KLanguage(@"条记录")];
            [self.tableView reloadData];
            [self scrollToBottom];
        }else{
           
        }
     
    }];
}
#pragma mark - lz
- (YKChatToolBoxView *)chatBox {
    if (!_chatBox) {
   
        _chatBox = [[YKChatToolBoxView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth,   self.tableView.height+fontAuto(40)+49+K_BottomBar_Height) canPulishWork:NO canSendFile:NO canSendShop:NO];

        _chatBox.isService = NO;
        _chatBox.delegate = self;
        _chatBox.chatBoxMoreView.delegate = self;
        
    }
    return _chatBox;
}
#pragma mark - JXCategoryListContentViewDelegate
- (UIView *)listView {
    return self.view;
}
-(UIImageView *)iconImageV{
    if(!_iconImageV){
        _iconImageV =[UIImageView new];
        _iconImageV.image = VULGetImage(@"icon_discussion");
//
    }
    return _iconImageV;
}
-(UIView *)lineV{
    if(!_lineV){
        _lineV =[UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineV;
}
-(UIView *)topView{
    if(!_topView){
        _topView =[UIView new];
        _topView.backgroundColor = HEXCOLOR(0xffffff);
    }
    return _topView;
}

- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"交流讨论") TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 1;
    }
    return _titleLabel;
}
- (VULLabel *)detiallLabel {
    if (!_detiallLabel) {
        _detiallLabel = [VULLabel getLabelWithFrame:CGRectZero Text:nil TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:12] Color:UIColorHex(#999999) BgColor:nil];
        _detiallLabel.numberOfLines = 1;
    }
    return _detiallLabel;
}




/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
