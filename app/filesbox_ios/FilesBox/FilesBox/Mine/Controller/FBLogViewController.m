//
//  FBLogViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/18.
//

#import "FBLogViewController.h"
#import "JXCategoryCustomScrollView.h"
#import "FBLogDetailVC.h"
@interface FBLogViewController ()
@property (nonatomic, strong) JXCategoryTitleView *categoryView;
@property (nonatomic, strong) JXCategoryListContainerView *listContainerView;

@end

@implementation FBLogViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = KLanguage(@"日志");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    
    [self.view addSubview:self.categoryView];
    [self.view addSubview:self.listContainerView];
    self.view.backgroundColor = HEXCOLOR(0xf2f2f2);
     [self.categoryView mas_makeConstraints:^(MASConstraintMaker *make) {
         make.top.mas_equalTo(K_NavBar_Height);
         make.left.mas_equalTo(-fontAuto(5));
         make.right.mas_equalTo(0);
         make.height.mas_equalTo(fontAuto(40));
     }];
     [self.listContainerView mas_makeConstraints:^(MASConstraintMaker *make) {
         make.top.mas_equalTo(self.categoryView.mas_bottom).offset(1);
         make.left.mas_equalTo(0);
         make.right.mas_equalTo(0);
         make.bottom.mas_equalTo(fontAuto(0));
     }];
    // Do any additional setup after loading the view.
}
#pragma mark - JXCategoryListContainerViewDelegate
- (id<JXCategoryListContentViewDelegate>)listContainerView:(JXCategoryListContainerView *)listContainerView initListForIndex:(NSInteger)index {
    FBLogDetailVC *vc = [FBLogDetailVC new];
    vc.index = index;
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
        _categoryView.backgroundColor = [UIColor whiteColor];
       
        _categoryView.titles = @[KLanguage(@"登录日志"),KLanguage(@"登录设备"),KLanguage(@"文件传输")];
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
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
