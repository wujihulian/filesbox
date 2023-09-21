//
//  FBAutomaticFilesVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/5/22.
//

#import "FBAutomaticFilesVC.h"
#import "VULMineCell.h"
#import "FBHomeViewController.h"
#import "FBFailFileVC.h"
@interface FBAutomaticFilesVC ()
@property (nonatomic, copy) NSArray *titleArray;
@property (nonatomic, retain) NSMutableDictionary *infoDic;

@end

@implementation FBAutomaticFilesVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // 弹框请求用户授权

    [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status) {
     }];
    self.navigationTitle = KLanguage(@"自动备份");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    [self.tableView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height);
        make.left.bottom.right.mas_equalTo(0);
    }];
    
    self.titleArray = @[@[KLanguage(@"仅在WIFI下备份"),KLanguage(@"自动备份照片"),KLanguage(@"自动备份视频")],@[KLanguage(@"自动备份位置"),KLanguage(@"备份失败列表")]];
    NSMutableArray *backupArr = [NSMutableArray arrayWithArray:[NSArray bg_arrayWithName:@"backInfo"]];
    self.infoDic = [NSMutableDictionary dictionary];
    if(backupArr.count == 0){
        NSDictionary *dic = @{@"wifi":@1,@"photo":@0,@"video":@0,@"path":@"",@"sourceID":@0};
        [backupArr addObject:dic];
        [backupArr bg_saveArrayWithName:@"backInfo"];
        [self.infoDic addEntriesFromDictionary:dic];
    }else{
        NSDictionary *dic = backupArr[0];
        [self.infoDic addEntriesFromDictionary:dic];
    }

    
    // Do any additional setup after loading the view.
}

#pragma mark - UITableViewDelegate\UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.titleArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSArray *arr = self.titleArray[section];
    return arr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    VULMineCell *cell = [VULMineCell dequeueReusableCellWithTableView:tableView reuseIdentifier:@"cell"];
    cell.backgroundColor = [UIColor whiteColor];
    if(indexPath.section == 0){
       cell.briefLabel.hidden = YES;
       cell.switchh.hidden = NO;
       cell.redView.hidden = YES;
       cell.cellImageView.hidden = YES;
      cell.accessoryType = UITableViewCellAccessoryNone;
        if(indexPath.row == 0){
            NSString *wifi = [NSString stringWithFormat:@"%@",self.infoDic[@"wifi"]];
            [cell.switchh setOn:wifi.boolValue];
        }else if(indexPath.row == 1){
            NSString *photo = [NSString stringWithFormat:@"%@",self.infoDic[@"photo"]];
            [cell.switchh setOn:photo.boolValue];
        }else{
            NSString *video = [NSString stringWithFormat:@"%@",self.infoDic[@"video"]];
            [cell.switchh setOn:video.boolValue];
        }
        [[cell.switchh rac_signalForControlEvents:UIControlEventValueChanged] subscribeNext:^(__kindof UIControl *_Nullable x) {
            BOOL value = ((UISwitch *)x).on;
            if(indexPath.row == 0){
                [self.infoDic setValue:value?@1:@0 forKey:@"wifi"];
                [NSArray bg_clearArrayWithName:@"backInfo"];
                NSMutableArray *arr = [NSMutableArray array];
                [arr addObject:self.infoDic];
                [arr bg_saveArrayWithName:@"backInfo"];
                [[ChunkUploader sharedManager] backupFile];;
                if(self.saveAndRefreshBlock){
                    self.saveAndRefreshBlock();
                }
            }else{
                if(value){
                    NSString *message = [NSString stringWithFormat:@"%@%@%@",KLanguage(@"如果确定自动备份照片,那么您的照片会自动备份到地址为"),ChooseUrl,KLanguage(@"的服务器上,该服务器的管理员可以看到您的照片")];
                    if(indexPath.row == 2){
                        message = [NSString stringWithFormat:@"%@%@%@",KLanguage(@"如果确定自动备份视频,那么您的视频会自动备份到地址为"),ChooseUrl,KLanguage(@"的服务器上,该服务器的管理员可以看到您的视频")];
                    }
                    [cell.switchh setOn:NO];
                    [self vul_showAlertWithTitle:KLanguage(@"提示") message:message appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
                        alertMaker.
                        addActionCancelTitle(KLanguage(@"取消")).
                        addActionDestructiveTitle(KLanguage(@"确定"));
                    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
                        if (buttonIndex == 1) {
                          
                            if(indexPath.row == 1){
                                [cell.switchh setOn:YES];
                                [self.infoDic setValue:@1 forKey:@"photo"];
                            }else{
                                [cell.switchh setOn:YES];
                                [self.infoDic setValue:@1 forKey:@"video"];

                            }

                            [NSArray bg_clearArrayWithName:@"backInfo"];
                            NSMutableArray *arr = [NSMutableArray array];
                            [arr addObject:self.infoDic];
                            [arr bg_saveArrayWithName:@"backInfo"];
                            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"isClose"];

                            [[ChunkUploader sharedManager] backupFile];;
                            if(self.saveAndRefreshBlock){
                                self.saveAndRefreshBlock();
                            }
                        }
                     
                    }];
                    
                }else{
                    if(indexPath.row == 1){
                        [self.infoDic setValue:@0 forKey:@"photo"];
                    }else{
                        [self.infoDic setValue:@0 forKey:@"video"];
                    }
                    [NSArray bg_clearArrayWithName:@"backInfo"];
                    NSMutableArray *arr = [NSMutableArray array];
                    [arr addObject:self.infoDic];
                    [arr bg_saveArrayWithName:@"backInfo"];
                    [[ChunkUploader sharedManager] backupFile];;
                    if(self.saveAndRefreshBlock){
                        self.saveAndRefreshBlock();
                    }
                }
            }
            
       
        }];

    }else{
        cell.switchh.hidden = YES;
        cell.briefLabel.hidden = NO;
        cell.cellImageView.hidden = YES;
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        if(indexPath.section ==1&&indexPath.row ==0){
            cell.briefLabel.text = self.infoDic[@"path"];
        }else{
            cell.briefLabel.text = @"";
        }
    }
    [cell.cellImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
               make.left.mas_equalTo(0);
               make.centerY.mas_equalTo(cell.contentView.mas_centerY);
              make.size.mas_equalTo(CGSizeMake(0, 0));
    }];
    cell.titleLabel.text = _titleArray[indexPath.section][indexPath.row];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return fontAuto(51);
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
//    if (section == 2) {
//        return fontAuto(10);
//    }
    return 0.001;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
            return fontAuto(10);

}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *sectionHeader = [UIView new];
    sectionHeader.backgroundColor = HEXCOLOR(0xf6f6f6);
    return sectionHeader;
}

- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section {
    UIView *footView = [UIView new];
    footView.backgroundColor = [UIColor whiteColor];
    return footView;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if(indexPath.section ==1&&indexPath.row ==0){
        FBHomeViewController *vc = [FBHomeViewController new];
        vc.isMove = YES;
        vc.icon = @"backUp";
        vc.operation = @"backUp";
        vc.flag = NO;
        vc.selectFolder = ^(NSString * _Nonnull path, NSString * _Nonnull sourceID) {
            NSString *sourceId1 =  self.infoDic[@"sourceID"];
            if(sourceId1.intValue  != sourceID.integerValue){
                [NSArray bg_clearArrayWithName:@"uploadedIdentifiers"];
                [[ChunkUploader sharedManager].uploadedIdentifiers removeAllObjects];
            }
            [self.infoDic setValue:path forKey:@"path"];
            [self.infoDic setValue:sourceID forKey:@"sourceID"];

            [NSArray bg_clearArrayWithName:@"backInfo"];
            NSMutableArray *arr = [NSMutableArray array];
            [arr addObject:self.infoDic];
            [arr bg_saveArrayWithName:@"backInfo"];
            [self.tableView reloadData];
            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"isClose"];
            if(self.saveAndRefreshBlock){
                self.saveAndRefreshBlock();
            }
   
            [[ChunkUploader sharedManager] backupFile];;
        };
        [self presentViewController:vc animated:YES completion:nil];
        return;
    }
    
    if(indexPath.section ==1&&indexPath.row ==1){
        FBFailFileVC *vc = [FBFailFileVC new];
        [self.navigationController pushViewController:vc animated:YES];
        return;
    }

    
   
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
