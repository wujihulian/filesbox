//
//  FBFileAddrituteRootVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/8.
//

#import "FBFileAddrituteRootVC.h"
#import "JXCategoryCustomScrollView.h"
#import "FBFileAddrituteDetailVC.h"
#import "FBFileAttributeTopView.h"
#import "FBFileAttributeTopImageView.h"
#import "FBFileVersionManagentVC.h"
#import "FBFileDiscussViewController.h"
#import "FBDiscussionVC.h"
@interface FBFileAddrituteRootVC ()<JXCategoryViewDelegate,JXCategoryListContainerViewDelegate>

@property (nonatomic, strong) JXCategoryTitleView *categoryView;
@property (nonatomic, strong) JXCategoryListContainerView *listContainerView;
@property (nonatomic, strong) VULButton *clostBtn;
@property (nonatomic, strong) UIView *bgView;

@property (nonatomic, strong) FBFileAttributeTopView *viewTop;
@property (nonatomic, strong) FBFileAttributeTopImageView *viewImageTop;

@property (nonatomic, strong) UIView *lineV;
@property (nonatomic, strong) UIView *lineBottomV;
@property (nonatomic, assign) NSInteger topHight;

@end

@implementation FBFileAddrituteRootVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationView.hidden = YES;
    if (self.model.path.length>0) {
        self.bgView.userInteractionEnabled = YES;
        [self.view addSubview:self.viewImageTop];
        [self.viewImageTop addSubview:self.bgView];
        [self.viewImageTop addSubview:self.clostBtn];
        [self.view addSubview:self.categoryView];
        [self.view addSubview:self.listContainerView];
        [self.view addSubview:self.lineV];
        [self.view addSubview:self.lineBottomV];
        [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.viewImageTop.mas_bottom);
            make.width.mas_equalTo(VULSCREEN_WIDTH);
            make.height.mas_equalTo(1);
        }];
        self.viewImageTop.model = self.model;
        UIImage *iamge = VULGetImage(@"file_attribute_close");
        iamge = [iamge imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];

        [_clostBtn setImage:iamge forState:UIControlStateNormal];
        _clostBtn.imageView.tintColor = [UIColor whiteColor];
        [self.clostBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_StatusBar_Height+fontAuto(5));
            make.width.height.mas_equalTo(fontAuto(30));
            make.right.mas_equalTo(-fontAuto(10));
        }];
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_StatusBar_Height+fontAuto(5));
            make.width.height.mas_equalTo(fontAuto(30));
            make.right.mas_equalTo(-fontAuto(10));
        }];
    }else{
        [self.view addSubview:self.viewTop];
        [self.viewTop addSubview:self.clostBtn];
        [self.view addSubview:self.categoryView];
        [self.view addSubview:self.listContainerView];
        [self.view addSubview:self.lineV];
        [self.view addSubview:self.lineBottomV];
        self.viewTop.model = self.model;
        [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.viewTop.mas_bottom);
            make.width.mas_equalTo(VULSCREEN_WIDTH);
            make.height.mas_equalTo(1);
        }];
        [_clostBtn setImage:VULGetImage(@"file_attribute_close") forState:UIControlStateNormal];
        [self.clostBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(K_StatusBar_Height+fontAuto(17.5));
            make.width.height.mas_equalTo(fontAuto(30));
            make.right.mas_equalTo(-fontAuto(10));
        }];
    }
 

   
   
    [self.categoryView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.lineV.mas_bottom);
        make.left.mas_equalTo(-fontAuto(5));
        make.width.mas_equalTo(VULSCREEN_WIDTH);
        make.height.mas_equalTo(fontAuto(40));
    }];
    [self.lineBottomV mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.categoryView.mas_bottom);
        make.width.mas_equalTo(VULSCREEN_WIDTH);
        make.height.mas_equalTo(1);
    }];
    [self.listContainerView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.lineBottomV.mas_bottom);
        make.width.mas_equalTo(VULSCREEN_WIDTH);
        make.bottom.mas_equalTo(fontAuto(0));
    }];

    // Do any additional setup after loading the view.
}
#pragma mark - JXCategoryListContainerViewDelegate
- (id<JXCategoryListContentViewDelegate>)listContainerView:(JXCategoryListContainerView *)listContainerView initListForIndex:(NSInteger)index {
    NSString *title = _categoryView.titles [index];

    if ([title isEqualToString:KLanguage(@"版本")]) {
        FBFileVersionManagentVC *vc = [FBFileVersionManagentVC new];
        vc.model = self.model;
        vc.uploadRefreshBlock = ^(VULFileObjectModel * _Nonnull model) {
            self.model = model;
            self.viewImageTop.model = model;
            [self.listContainerView reloadData];
            if (self.saveAndRefreshBlock) {
                self.saveAndRefreshBlock();
            }
        };
        vc.saveAndRefreshBlock = ^{
            if (self.saveAndRefreshBlock) {
                self.saveAndRefreshBlock();
            }
        };
        vc.openDetialWithModel = ^(VULFileObjectModel * _Nonnull model) {
            if (self.openDetialWithModel) {
                self.openDetialWithModel(model);
            }
            [self dismissViewControllerAnimated:YES completion:^{
                    
            }];
        };
        return vc;

    }
    if ([title isEqualToString:KLanguage(@"动态")]) {
        FBFileDiscussViewController *vc = [FBFileDiscussViewController new];
        vc.model = self.model;
        vc.saveAndRefreshBlock = ^{
            if (self.saveAndRefreshBlock) {
                self.saveAndRefreshBlock();
            }
        };
        return vc;

    }
    if ([title isEqualToString:KLanguage(@"讨论")]) {
        FBDiscussionVC *vc = [FBDiscussionVC new];
        vc.model = self.model;
        vc.topH = self.listContainerView.top;
        return vc;

    }
    
    
    
    FBFileAddrituteDetailVC *vc = [FBFileAddrituteDetailVC new];
    vc.model = self.model;
    vc.saveAndRefreshBlock = ^{
        if (self.saveAndRefreshBlock) {
            self.saveAndRefreshBlock();
        }
    };
    vc.gotoTagManageMent = ^{
        [self dismissViewControllerAnimated:YES completion:^{
                
        }];
        if (self.gotoTagManageMent) {
            self.gotoTagManageMent();
        }
    };
    return vc;
}

- (Class)scrollViewClassInlistContainerView:(JXCategoryListContainerView *)listContainerView {
    return [JXCategoryCustomScrollView class];
}

- (NSInteger)numberOfListsInlistContainerView:(JXCategoryListContainerView *)listContainerView {
    return self.categoryView.titles.count;
}
  
- (void)categoryView:(JXCategoryBaseView *)categoryView didSelectedItemAtIndex:(NSInteger)index {
    NSLog(@"当前选中了:%ld", index);
}

- (JXCategoryTitleView *)categoryView {
    if (!_categoryView) {
        _categoryView = [JXCategoryTitleView new];
        _categoryView.backgroundColor = [UIColor clearColor];
        NSMutableArray *arr = [NSMutableArray array];
        [arr addObject:KLanguage(@"属性")];
        if (!self.model.isFolder.boolValue && isPermissionWithModel(KLanguage(@"编辑"), @[self.model])) {
            [arr addObject:KLanguage(@"版本")];
        }
        if (self.model.targetType.integerValue ==2){
            if (isPermissionWithModel(KLanguage(@"评论"), @[self.model])) {
                [arr addObject:KLanguage(@"讨论")];
            }
        }else{
            if (isPermissionWithModel(KLanguage(@"预览"), @[self.model])) {
                [arr addObject:KLanguage(@"讨论")];
            }
        }
        if (isPermissionWithModel(KLanguage(@"动态"), @[self.model])) {
            [arr addObject:KLanguage(@"动态")];
        }
        _categoryView.titles = arr;
        _categoryView.titleColor = UIColorHex(#333333);
        _categoryView.titleSelectedColor = UIColorHex(#722ed1);
        _categoryView.titleFont = [UIFont yk_pingFangRegular:fontAuto(15)];
        _categoryView.titleSelectedFont = [UIFont yk_pingFangSemibold:fontAuto(15)];
        _categoryView.listContainer = self.listContainerView;
        _categoryView.averageCellSpacingEnabled = NO;
        _categoryView.delegate = self;
        
        JXCategoryIndicatorLineView *lineView = [[JXCategoryIndicatorLineView alloc] init];
        lineView.indicatorHeight = 2.0;
        lineView.indicatorColor = UIColorHex(#722ed1);
        _categoryView.indicators = @[lineView];
    }
    return _categoryView;
}

- (JXCategoryListContainerView *)listContainerView {
    if (!_listContainerView) {
        _listContainerView = [[JXCategoryListContainerView alloc] initWithType:JXCategoryListContainerType_ScrollView delegate:self];
        
    }
    return _listContainerView;
}
-(VULButton *)clostBtn{
    if (!_clostBtn) {
        _clostBtn = [VULButton new];
        [_clostBtn addTarget:self action:@selector(clickClostBtn) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _clostBtn;
}
-(void)clickClostBtn{
    [self dismissViewControllerAnimated:YES completion:^{
            
    }];
}
-(FBFileAttributeTopView *)viewTop{
    if (!_viewTop) {
        _viewTop = [[FBFileAttributeTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, K_StatusBar_Height+fontAuto(65))];
    }
    return _viewTop;
}
-(FBFileAttributeTopImageView *)viewImageTop{
    if (!_viewImageTop) {
        _viewImageTop = [[FBFileAttributeTopImageView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(150))];
    }
    return _viewImageTop;
}

-(UIView *)lineBottomV{
    if (!_lineBottomV) {
        _lineBottomV  = [UIView new];
        _lineBottomV.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineBottomV;
}
-(UIView *)lineV{
    if (!_lineV) {
        _lineV  = [UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xececec);
    }
    return _lineV;
}
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor = [HEXCOLOR(0x000000) colorWithAlphaComponent:0.2];
        _bgView.layer.masksToBounds = YES;
        _bgView.layer.cornerRadius = fontAuto(15);
    }
    return _bgView;
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
