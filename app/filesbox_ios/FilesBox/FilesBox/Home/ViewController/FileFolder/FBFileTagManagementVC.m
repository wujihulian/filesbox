//
//  FBFileTagManagementVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import "FBFileTagManagementVC.h"
#import "FBFileTagView.h"
#import "FBAddTagView.h"
@interface FBFileTagManagementVC ()
@property (nonatomic, strong) FBFileTagView *bossView;

@end

@implementation FBFileTagManagementVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = KLanguage(@"标签管理");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];

    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    [self.view addSubview:self.bossView];
    [self.bossView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height+fontAuto(4));
        make.left.mas_equalTo(fontAuto(12));
        make.right.mas_equalTo(-fontAuto(12));
        make.height.mas_equalTo(_bossView.tagCollectionView.contentSize.height);
    }];
    [self getTagList];
    // Do any additional setup after loading the view.
}
-(void)getTagList{
    [self.tagList  removeAllObjects];
    
    [VULBaseRequest requestWithUrl:@"/api/disk/tag/list" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        NSString *success =request.responseObject[@"success"];
        if (success.boolValue) {
            //TODO: 获取登录信息
            NSArray *list = request.data;
            for (NSDictionary *dic in list) {
                FBTagModel *model = [FBTagModel modelWithDictionary:dic];
                model.isEdit = YES;
                [self.tagList addObject:model];
            }
            FBTagModel *model = [FBTagModel new];
            model.labelId = @"-1";
            model.labelName = KLanguage(@"添加");
            model.image = @"icon_add_tag";
            model.style = @"rgb(193, 28, 123)";
            
            [self.tagList addObject:model];
            [self.bossView removeAllTags];
            for (FBTagModel *model in self.tagList ) {
                [self.bossView addTag:model];
            }
      
        
            [self.bossView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(K_NavBar_Height+fontAuto(4));
                make.left.mas_equalTo(fontAuto(12));
                make.right.mas_equalTo(-fontAuto(12));
                make.height.mas_equalTo(_bossView.tagCollectionView.contentSize.height);
            }];
        }
    }];
}
-(FBFileTagView *)bossView{
    if (!_bossView) {
        _bossView = [[FBFileTagView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth-fontAuto(12) , 0)];
        _bossView.selectCollectType = ^(NSInteger index) {
            [self.bossView selectOneCellWtihIndex:index];
            FBAddTagView *tagV = [[FBAddTagView alloc] initWithFrame:CGRectMake(0, 0, fontAuto(283), 185)];
            tagV.model = self.tagList[index];
            zhPopupController *popup2 =[[zhPopupController alloc] initWithView:tagV size:CGSizeMake(fontAuto(283), tagV.height)];
            popup2.offsetSpacing =(VULSCREEN_HEIGHT_VARIABLE - fontAuto(189))/2 - fontAuto(100);
            popup2.layoutType = zhPopupLayoutTypeTop;
            popup2.presentationStyle = zhPopupSlideStyleFromRight;
            popup2.maskAlpha = 0.35;
            tagV.dismiss = ^{
                [popup2 dismiss];
            };
            tagV.addOrEditTag = ^(NSString * _Nonnull rgbString, NSString * _Nonnull labelName, NSString * _Nonnull labelId) {
                [self updateDataWithColorString:rgbString labelName:labelName labelId:labelId];
                [popup2 dismiss];
               
            };
          [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
        };
        _bossView.deleteWithIndex = ^(NSInteger index) {
            [self removeTagWithIndex:index];

        };
    }
    return _bossView;
}
-(void)updateDataWithColorString:(NSString *)colorStr labelName:(NSString *)labelName labelId:(NSString *)labelId{
    NSString *url = @"/api/disk/tag/add";
    NSDictionary *dic = @{
        @"labelName":labelName,@"style":colorStr
    };
    if (labelId.intValue>0) {
        url =@"/api/disk/tag/edit";
        dic = @{
            @"labelName":labelName,
            @"style":colorStr,
            @"labelId":labelId
        };
    }
    [VULBaseRequest requestWithUrl:url params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest * _Nonnull request) {
        NSString *success =request.responseObject[@"success"];
        if (success.boolValue) {
            [self getTagList];
        }
        if (self.saveAndRefreshBlock) {
            self.saveAndRefreshBlock();
        }
    }];
}
-(void)removeTagWithIndex:(NSInteger )index{
    FBTagModel *model = self.tagList[index];
    NSString *alter = [NSString stringWithFormat:@"%@%@？",KLanguage(@"是否删除"),model.labelName];
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:alter message:nil preferredStyle:UIAlertControllerStyleAlert];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
             [VULBaseRequest requestWithUrl:@"/api/disk/tag/del" params:@{@"labelId":model.labelId} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest * _Nonnull request) {
                 NSString *success =request.responseObject[@"success"];
                 if (success.boolValue) {
                     [self.tagList removeObjectAtIndex:index];
                     [self.bossView removeTagAtIndex:index];

                 }
                 if (self.saveAndRefreshBlock) {
                     self.saveAndRefreshBlock();
                 }
             }];
        }]];
         [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {

                   NSLog(@"取消");

           }]];
        [self presentViewController:alertController animated:YES completion:nil];
}

-(NSMutableArray *)tagList{
    if (!_tagList) {
        _tagList = [NSMutableArray array];
    }
    return _tagList;
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
