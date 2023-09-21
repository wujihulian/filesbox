//
//  FBFindViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/20.
//

#import "FBFindViewController.h"
#import "WSLWaterFlowLayout.h"
#import "FBFindCollectionCell.h"
#import "FBHomeViewController.h"
static NSString *collectionCellIdentifier = @"FBFindCollectionCell";

@interface FBFindViewController ()<UICollectionViewDelegate, UICollectionViewDataSource, DZNEmptyDataSetDelegate, DZNEmptyDataSetSource,UINavigationControllerDelegate, UIImagePickerControllerDelegate,TZImagePickerControllerDelegate,UITextFieldDelegate,WSLWaterFlowLayoutDelegate>
@property (nonatomic,strong) UICollectionView *allCommodityCollectionView;
@property (nonatomic,strong) NSMutableArray *titleArray;
@property (nonatomic,strong) NSMutableArray *iconrray;
@property (nonatomic,strong) NSMutableArray *colorArray;

@end

@implementation FBFindViewController

- (void)viewDidLoad {
    [super viewDidLoad];
//    self.navigationViewBackgroundColor = HEXCOLOR(0xffffff);
//    self.navigationViewBackgroundImageView.image = VULGetImage(@"image_radarHeader");
    self.view.backgroundColor = [UIColor whiteColor];
    self.navigationTitleColor = HEXCOLOR(0xffffff);
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
//    self.navigationTitleColor = HEXCOLOR(0x333333);
//    self.navigationView.hidden = YES;
    self.navigationTitle = KLanguage(@"发现") ;
    
    self.titleArray = [NSMutableArray array];
    self.colorArray = [NSMutableArray array];
    self.iconrray = [NSMutableArray array];

    if(isTreeOpen(@"fileType")){
        [self.titleArray addObject:@[KLanguage(@"音乐"),KLanguage(@"视频"),KLanguage(@"文档"),KLanguage(@"图片"),KLanguage(@"压缩"),KLanguage(@"其他")]];
        
        [self.colorArray addObject:@[HEXCOLOR(0xFFFAF3),HEXCOLOR(0xF4F8FC),HEXCOLOR(0xF2F4FE),HEXCOLOR(0xFFFAFB),HEXCOLOR(0xFFFDF3),HEXCOLOR(0xF7FFF0)]];
        [self.iconrray addObject:@[@"find_icon_music",@"find_icon_video",@"find_icon_document",@"find_icon_image",@"find_icon_zip",@"find_icon_other"]];
    }
    NSMutableArray *arr = [NSMutableArray array];
    NSMutableArray *arr1 = [NSMutableArray array];
    NSMutableArray *arr2 = [NSMutableArray array];

    if(isTreeOpen(@"myFav")){
        [arr addObject:KLanguage(@"收藏夹")];
        [arr1 addObject:HEXCOLOR(0xF4F8FC)];
        [arr2 addObject:@"find_icon_fav"];

    }
    
    NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"role"];
    NSString *roleStr = [NSString stringWithFormat:@"%@",roleDic[@"explorer.informationView"]];
    if(isTreeOpen(@"information") && roleStr.boolValue){
        [arr addObject:KLanguage(@"资讯")];
        [arr1 addObject:HEXCOLOR(0xF2F2FE)];
        [arr2 addObject:@"find_icon_news"];
    }
    
    if(isTreeOpen(@"recentDoc")){
        [arr addObject:KLanguage(@"最近的")];
        [arr1 addObject:HEXCOLOR(0xF2F2FE)];
        [arr2 addObject:@"find_icon_recent"];
    }
    
    if(isTreeOpen(@"shareLink")){
        [arr addObject:KLanguage(@"分享的")];
        [arr1 addObject:HEXCOLOR(0xF4F8FC)];
        [arr2 addObject:@"find_icon_share"];
    }
    
    if(isTreeOpen(@"fileTag")){
        [arr addObject:KLanguage(@"标签")];
        [arr1 addObject:HEXCOLOR(0xF4F8FC)];
        [arr2 addObject:@"find_icon_label"];
    }
    
    [arr addObject:KLanguage(@"回收站")];
    [arr1 addObject:HEXCOLOR(0xF2F2FE)];
    [arr2 addObject:@"find_icon_delete"];
    
    [self.titleArray addObject:arr];
    [self.colorArray addObject:arr1];
    [self.iconrray addObject:arr2];

    [self.view addSubview:self.allCommodityCollectionView];
    [self.allCommodityCollectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height);
        make.left.right.mas_equalTo(0);
        make.bottom.mas_equalTo(-K_TabBar_Height-10);

    }];
    // Do any additional setup after loading the view.
}

#pragma mark - UICollectionView Delegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return self.titleArray.count;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    NSArray *countArr = self.titleArray[section];
    return countArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    FBFindCollectionCell *myClassifyCell = [collectionView dequeueReusableCellWithReuseIdentifier:collectionCellIdentifier forIndexPath:indexPath];
    return myClassifyCell;
}

- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath {
    FBFindCollectionCell *commodityCell = (FBFindCollectionCell *)cell;
    commodityCell.titleLabel.text =self.titleArray[indexPath.section][indexPath.row];
    commodityCell.imageV.image =VULGetImage(self.iconrray[indexPath.section][indexPath.row]);
    commodityCell.bgView.backgroundColor =self.colorArray[indexPath.section][indexPath.row];

}

// 选中item
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {

    NSString *title = self.titleArray[indexPath.section][indexPath.row];
    NSMutableDictionary *dic = backBlockAndFileType(title);
    FBHomeViewController *vc = [FBHomeViewController new];
    vc.isHome = YES;
    vc.icon = dic[@"block"];
    vc.fileType = dic[@"fileType"];
    [self.navigationController pushViewController:vc animated:YES];
}
- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath
{
    //如果是头部视图
    if (kind == UICollectionElementKindSectionHeader) {
        UICollectionReusableView *header = [collectionView dequeueReusableSupplementaryViewOfKind:kind withReuseIdentifier:@"placeholderHeader" forIndexPath:indexPath];
        [header removeAllSubviews];
        UILabel *lab = [UILabel new];
        lab.font = [UIFont yk_pingFangMedium:14+2];
        lab.textColor = HEXCOLOR(0x333333);
        
        if(isTreeOpen(@"fileType")){
            lab.text = indexPath.section == 0?KLanguage(@"常用"):KLanguage(@"服务");

        }else{
            lab.text = KLanguage(@"服务");
        }

        [header addSubview:lab];
        [lab mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(15);
            make.top.bottom.mas_equalTo(0);
        }];
        return header;
    } else {
        UICollectionReusableView *header = [collectionView dequeueReusableSupplementaryViewOfKind:kind withReuseIdentifier:@"placeholderHeader1" forIndexPath:indexPath];
        return header;
    }
}
//设置边距
#pragma mark - WSLWaterFlowLayoutDelegate
- (CGSize)waterFlowLayout:(WSLWaterFlowLayout *)waterFlowLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    
 return CGSizeMake((self.view.width - 30) / 2, fontAuto(75));
    
}

/** 头视图Size */
-(CGSize )waterFlowLayout:(WSLWaterFlowLayout *)waterFlowLayout sizeForHeaderViewInSection:(NSInteger)section {
    return CGSizeMake(self.view.width, fontAuto(30));
}
/** 脚视图Size */
-(CGSize )waterFlowLayout:(WSLWaterFlowLayout *)waterFlowLayout sizeForFooterViewInSection:(NSInteger)section{
    
    return CGSizeZero;
}

/** 列数*/
-(CGFloat)columnCountInWaterFlowLayout:(WSLWaterFlowLayout *)waterFlowLayout {
    return 2;
}

/** 列间距*/
-(CGFloat)columnMarginInWaterFlowLayout:(WSLWaterFlowLayout *)waterFlowLayout {
    return 10;
}
/** 行间距*/
-(CGFloat)rowMarginInWaterFlowLayout:(WSLWaterFlowLayout *)waterFlowLayout {
    return 10;
}
/** 边缘之间的间距*/
-(UIEdgeInsets)edgeInsetInWaterFlowLayout:(WSLWaterFlowLayout *)waterFlowLayout {
    return UIEdgeInsetsMake(10, 10, 0, 10);
}

- (CGFloat)verticalOffsetForEmptyDataSet:(UIScrollView *)scrollView {
    return 40.0f;
}

- (UIImage *)imageForEmptyDataSet:(UIScrollView *)scrollView {
    return [UIImage imageNamed:@"no_data"];
}
- (UICollectionView *)allCommodityCollectionView {
    if (!_allCommodityCollectionView) {
        WSLWaterFlowLayout * flowlayout = [[WSLWaterFlowLayout alloc] init];
        flowlayout.delegate = self;
        flowlayout.flowLayoutStyle = WSLWaterFlowVerticalEqualWidth;

        _allCommodityCollectionView = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:flowlayout];
        _allCommodityCollectionView.delegate = self;
        _allCommodityCollectionView.dataSource = self;
        _allCommodityCollectionView.emptyDataSetSource = self;
        _allCommodityCollectionView.emptyDataSetDelegate = self;
        _allCommodityCollectionView.backgroundColor = [UIColor whiteColor];
        _allCommodityCollectionView.showsHorizontalScrollIndicator = NO;
        [_allCommodityCollectionView registerClass:[FBFindCollectionCell class] forCellWithReuseIdentifier:collectionCellIdentifier];
        [_allCommodityCollectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"placeholderHeader"];
        [_allCommodityCollectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"placeholderHeader1"];

    }
    return _allCommodityCollectionView;
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
