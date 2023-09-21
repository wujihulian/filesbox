//
//  FBHeaderView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/5/10.
//

#import "FBHeaderView.h"
#import "FBFileProgress.h"
#import "FBMessageView.h"
#import "YCMenuView.h"
#import "FBHomeViewController.h"
#import "FBDownOrUploadVC.h"
#import "FBFromTopView.h"
#import "FBFileMessageViewController.h"
@implementation FBHeaderView
- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {


     
        
        NSMutableArray *titleArray = [NSMutableArray array];
        NSMutableArray *iconArray = [NSMutableArray array];
//        myFav,my,information,recentDoc,shareLink,fileType,fileTag
//        收藏夹、个人空间、资讯、最近文档、外链分享、文件类型、标签
        
        if(isTreeOpen(@"myFav")){
            [titleArray addObject:KLanguage(@"收藏夹")];
            [iconArray addObject:@"icon_header_fav"];
        }
        [titleArray addObject:KLanguage(@"回收站")];
        [iconArray addObject:@"icon_trash"];
        [titleArray addObject:KLanguage(@"消息")];
        [iconArray addObject:@"icon_message"];
        [titleArray addObject:KLanguage(@"传输")];
        [iconArray addObject:@"icon_cloud"];
        
        if(isTreeOpen(@"menu")){
            [titleArray addObject:KLanguage(@"菜单")];
            [iconArray addObject:@"icon_menu"];
        }
        self.labelArray = [NSMutableArray array];
        NSInteger count =titleArray.count;
        UIView *leftView = nil;
        for (NSInteger i = 0; i < titleArray.count; i++) {

            UIControl *backView = [UIControl new];
            backView.tag = 1000 +i;
            [backView addTarget:self action:@selector(clickAction:) forControlEvents:UIControlEventTouchUpInside];
            [self addSubview:backView];

            [backView mas_makeConstraints:^(MASConstraintMaker *make) {
                if (leftView) {
                    make.left.mas_equalTo(leftView.mas_right);
                } else {
                    make.left.mas_equalTo(0);
                }
                if (i == 5) {
                    make.top.mas_equalTo(leftView.mas_bottom).offset(22);
                    make.left.mas_equalTo(0);
                } else if (i > 5) {
                    make.top.mas_equalTo(leftView.mas_top);
                } else {
                    make.top.mas_equalTo(fontAuto(10));
                }
                make.width.mas_equalTo(VULSCREEN_WIDTH/count);
            }];

            UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed: iconArray[i]]];
            imageView.contentMode = UIViewContentModeScaleAspectFit;
            imageView.clipsToBounds = YES;
            [backView addSubview:imageView];

            UILabel *titleLabel = [UILabel new];
            titleLabel.textColor = UIColorHex(#FFFFFF);
            titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(14)];
            titleLabel.textAlignment = NSTextAlignmentCenter;
            titleLabel.text = [titleArray objectAtIndex:i];
            [backView addSubview:titleLabel];
            
            if (i!=4) {
                UIView *view = [UIView new];
//                view.layer.shadowOffset = CGSizeMake(0,-5);
//                view.layer.cornerRadius = 8;
//                view.layer.masksToBounds = NO;
//                view.layer.shadowOffset =  CGSizeMake(1, 1);
//                view.layer.shadowOpacity = 0.6;
//                view.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.3].CGColor;//HEXCOLOR(0x51449C).CGColor;

                [backView addSubview:view];

                UILabel *number = [[UILabel alloc] init];
                number.backgroundColor = HEXCOLOR(0xD32F2F);
                number.textColor  = [UIColor whiteColor];
                number.layer.cornerRadius = 8;
                number.layer.masksToBounds = YES;
                number.textAlignment = NSTextAlignmentCenter;
                number.font = [UIFont yk_pingFangRegular:fontAuto(10)];
                number.tag =  1000000+ i;
                number.hidden = YES;
                [_labelArray addObject:number];
                [view addSubview:number];
                [view mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.left.mas_equalTo(imageView.mas_right).offset(-38);
                    make.top.mas_equalTo(-5);
                    make.height.mas_equalTo(16);
                    make.width.mas_greaterThanOrEqualTo(30);
                }];
                [number mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.top.right.left.bottom.offset(0);
                }];
            }
        
            [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.left.right.mas_equalTo(0);
                make.centerX.mas_equalTo(backView.mas_centerX);
                make.height.mas_equalTo(34);
            }];
         


            [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(imageView.mas_bottom).offset(8);
                make.left.right.mas_equalTo(0);
                make.centerX.mas_equalTo(backView.mas_centerX);
                make.bottom.mas_equalTo(0);
            }];
            leftView = backView;
        }

        [self layoutIfNeeded];
 
        self.height = leftView.bottom +fontAuto(10);

    }
    return self;
}
-(void)clickAction:(UIControl *)sender{
    NSInteger count = sender.tag - 1000;
    if(!isTreeOpen(@"myFav")){
        count = count+1;
    }
    switch (count) {
        case 0:
        {
            NSMutableDictionary *dic = backBlockAndFileType(KLanguage(@"收藏夹"));
            FBHomeViewController *vc = [FBHomeViewController new];
            vc.isHome = YES;
            vc.icon = dic[@"block"];
            vc.fileType = dic[@"fileType"];
            [self.viewController.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 1:
        {
   
            FBHomeViewController *vc = [FBHomeViewController new];
            vc.isHome = YES;
            vc.icon = @"recycle";
            [self.viewController.navigationController pushViewController:vc animated:YES];
         
        }
            break;
            
        case 2:
        {
            FBFileMessageViewController *vc = [FBFileMessageViewController new];
            [self.viewController.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 3:
        {
            FBDownOrUploadVC *vc = [FBDownOrUploadVC new];
            vc.saveAndRefreshBlock = ^{
                if (self.reloadDataFromUp) {
                    self.reloadDataFromUp();
                }
            };
            [self.viewController.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 4:
        {
            NSMutableArray *titleArr = [NSMutableArray array];
            NSMutableArray *imageArr =  [NSMutableArray array];
            if(isTreeOpen(@"myFav")){
                [titleArr addObject:KLanguage(@"收藏夹")];
                [imageArr addObject:@"icon_left_fav"];

            }
            if(isTreeOpen(@"shareLink")){
                [titleArr addObject:KLanguage(@"我分享的")];
                [imageArr addObject:@"icon_left_shareLink"];

            }
            if(isTreeOpen(@"recentDoc")){
                [titleArr addObject:KLanguage(@"最近打开的")];
                [imageArr addObject:@"icon_left_recentDoc"];

            }
            if(isTreeOpen(@"fileType")){
                
                [titleArr addObjectsFromArray:@[KLanguage(@"视频"), KLanguage(@"音乐"),KLanguage(@"文档"),KLanguage(@"图片"),KLanguage(@"压缩"),KLanguage(@"其他")]];
                [imageArr addObjectsFromArray:@[@"icon_left_movie",@"icon_left_music",@"icon_left_doc",@"icon_left_image",@"icon_left_zip",@"icon_left_other"]];

            }
            NSMutableArray *array = [[NSMutableArray alloc] init];
            for (NSInteger i = 0; i < titleArr.count; i++) {
                UIImage *rightImage = nil;
                rightImage = VULGetImage(@"icon_right");

                YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:VULGetImage(imageArr[i]) rightImage:rightImage handler:^(YCMenuAction *action) {

                    

                    
                    
                    if (self.clickBlock) {
                        self.clickBlock(action.title);
                    }
                }];
                [array addObject:action];
            }
            NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
            CGFloat width = 200;
              if ([appLanguage isEqualToString:@"en"]) {
                  width =220;
              }
            YCMenuView *menuView = [YCMenuView menuWithActions:array width:width relyonView:sender];
            menuView.menuColor = [UIColor whiteColor];
            menuView.separatorIndexArray = @[@(2),@(8)];
            menuView.separatorColor = [UIColor redColor];
            menuView.maxDisplayCount = 20;
            menuView.cornerRaius = 0;
            //menuView.offset = 150;
            menuView.textColor = UIColorHex(333333);
            menuView.textFont = [UIFont yk_pingFangRegular:14];
            menuView.menuCellHeight = 35;
            menuView.dismissOnselected = YES;
            menuView.dismissOnTouchOutside = YES;
            menuView.backgroundColor = [UIColor whiteColor];
            [menuView show];
        }
            break;
        default:
            break;
    }

    
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
