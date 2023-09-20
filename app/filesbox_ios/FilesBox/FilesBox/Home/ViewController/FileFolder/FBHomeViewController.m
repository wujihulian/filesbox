//
//  FBHomeViewController.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/16.
//

#import "FBHomeViewController.h"
#import "FBNavgationOperateView.h"
#import "FBFileTopView.h"
#import "VULFileModel.h"
#import "FBFileNodeChooseView.h"
#import "VULFileGridCell.h"
#import "VULFileListCell.h"
#import "UBWaterWaveButton.h"
#import "FBOperationView.h"
#import "VULInputTitleView.h"
#import "YCMenuView.h"
#import "FBAddFileView.h"
#import "BaseWebViewController.h"
//#import "OLDZFPlayerController.h"
//#import "OLDZFAVPlayerManager.h"
//#import "OLDZFPlayerControlView.h"
#import "VULDocumentVC.h"
//#import "UBUploadManager.h"
#import "ChunkUploader.h"
#import "VULPlayMp3VC.h"
#import "FBFileAddrituteRootVC.h"
#import "FBFromTopView.h"
#import "FBSearchView.h"
#import "FBFileTagManagementVC.h"
#import "FBShareVC.h"
#import "FBPreviewURLVC.h"
#import "FBDownOrUploadVC.h"
#import "AppDelegate.h"
#import "FBZIPLookViewController.h"
#import "FBHeaderView.h"
#import "VULInfoCollectionViewCell.h"
#import "VULInfoListCell.h"
#import "FBInfoTypeView.h"
#import "PSImageEditor.h"
#import <Vision/Vision.h>
#import "FBTextShowView.h"
#import "VULShareSpecialCardView.h"
#import "VULCreateLinkCode.h"
#import "FBTakePhotoVC.h"
#import "BasePreviewHtmlVC.h"
#import "BasePreviewTextVC.h"



#define KDownloadManager [GKDownloadManager sharedInstance]

typedef void(^VULRequestCompletion)(__kindof VULBaseRequest * _Nonnull request);


@interface FBHomeViewController ()<UICollectionViewDelegate,UICollectionViewDataSource,UIImagePickerControllerDelegate,DZNEmptyDataSetSource,DZNEmptyDataSetDelegate,UIDocumentPickerDelegate,XLPhotoBrowserDatasource,CLLocationManagerDelegate>


@property (nonatomic, assign) NSInteger currentPage;
@property (nonatomic,strong) FBNavgationOperateView *navView;
@property (nonatomic,strong) FBHeaderView *headerView;


@property (nonatomic,strong) FBFileTopView *topView;
@property (nonatomic,strong) FBFileNodeChooseView *leftView;
@property (nonatomic,strong) VULAllFileModel *allModel;
@property (nonatomic,strong) VULAllFileModel *conentModel;
@property (nonatomic,strong) NSMutableArray  *dataArray;
@property (nonatomic,strong) NSMutableArray  *moveArray;
@property (nonatomic,strong) NSMutableArray  *directoryArray;
@property (nonatomic,strong) NSMutableArray  *tierTitleArray;
@property (nonatomic,strong) NSMutableArray  *imageArray;

@property (nonatomic, strong) UBWaterWaveButton *addButton;

@property (nonatomic, strong) TZImagePickerUtil *imagePicker;
@property (nonatomic,copy) NSString  *sourceID;
@property (nonatomic,copy) NSString  *fitstSourceID;
@property (nonatomic,copy) NSString  *sourceLevel;
@property (nonatomic,copy) NSString  *infoTypeID;
@property (nonatomic,copy) NSString  *parentID;
@property (nonatomic,copy) NSString  *tagID;
@property (nonatomic,assign) BOOL  sortType;
@property (nonatomic,copy) NSString  *keyWord;

@property (nonatomic,copy) NSString  *tgblock;

@property (nonatomic,assign) BOOL  isUpload;
@property (nonatomic,assign) NSInteger  imageCount;

@property (nonatomic,assign) BOOL  isZip;
@property (nonatomic,assign) BOOL  isFav;


@property (nonatomic, assign) BOOL isGrid;//是否是网格(默认列表)


@property (nonatomic,strong) zhPopupController *popup;

@property (nonatomic,strong) UICollectionView *collectionView;
@property (nonatomic,strong) NSMutableArray  *selectModelArray;
@property (nonatomic, assign) BOOL isEdit;//是否编辑
@property (nonatomic,strong) FBOperationView  *operationView;
@property (nonatomic,strong) zhPopupController *popup1;
@property (nonatomic,strong) VULButton * cancel;
@property (nonatomic,strong) VULButton * changeBtn;

@property (nonatomic, strong) UIView *containerView;
@property (nonatomic, strong) ZFPlayerController *player;
@property (nonatomic, strong) ZFAVPlayerManager *playerManager;
@property (nonatomic, strong) ZFPlayerControlView *controlView;/**< 视频播放器控制层 */
@property (nonatomic, strong) UIDocumentPickerViewController *documentPickerView;
@property (nonatomic, strong) DownloadProgress *downloadProgress; // 单例对象
@property (nonatomic, strong) FBSearchView *searchView; // 单例对象

@property (nonatomic, strong) NSMutableDictionary *searchDic; // 单例对象
@property (nonatomic,strong) NSMutableArray  *bosFirstArray;
@property (nonatomic,strong) VULFileObjectModel  *parentModel;
@property (nonatomic,strong) VULFileObjectModel  *upNewModel;
@property (nonatomic, strong) TZImagePickerUtil *videoPicker;
@property (nonatomic, strong) UIImageView *colorImageV;

@property (nonatomic,strong) VULFileObjectModel  *previewModel;
@property (nonatomic,strong) NSString  *repeatSourceID;
@property (nonatomic,strong) NSString  *repeatName;
@property (nonatomic, strong) CLLocationManager *locationManager;
@property (nonatomic,strong) FBNewHtmlView  *htmlView;


@end

@implementation FBHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.locationManager = [[CLLocationManager alloc] init];
       self.locationManager.delegate = self;
       
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(pasteboardChanged:) name:UIMenuControllerDidHideMenuNotification object:nil];

    if (!self.isHome) {
        [self.view addSubview:self.colorImageV];
    }
    self.repeatSourceID  = @"";
    self.repeatName  = @"";

    self.isGrid = YES;
    self.isZip = NO;
    self.isFav = NO;
    self.view.backgroundColor = HEXCOLOR(0xffffff);
    
    [self.view addSubview:self.navView];
//    0, (K_NavBar_Height-K_StatusBar_Height-40)/2+K_StatusBar_Height, VULSCREEN_WIDTH, 40)
    [self.navView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo((K_NavBar_Height-K_StatusBar_Height-40)/2+K_StatusBar_Height);
        make.left.right.mas_equalTo(0);
        make.height.mas_equalTo(40);
    }];
    [self.view addSubview:self.topView];

    self.keyWord = @"";
    MJWeakSelf
    
    self.navView.clickSearch = ^(BOOL flag) {
        [self dissmissOperationView];

        if (flag) {
            [weakSelf.searchView show];
        }else{
            [weakSelf.searchView hide];
        }
    };
    self.navView.reloadDataFromUp = ^{
        [weakSelf.collectionView.mj_header beginRefreshing];
    };
    self.headerView.reloadDataFromUp = ^{
        [weakSelf.collectionView.mj_header beginRefreshing];
    };
    self.navView.searchWtihTextField = ^(NSString * _Nonnull text) {
        weakSelf.keyWord = text;
        if (text.length == 0) {
            if (self.flag && [self.tgblock  isEqualToString: @"firstBox"]  &&     self.tierTitleArray .count == 1 &&self.keyWord.length == 0) {
                [weakSelf.dataArray removeAllObjects];
                [weakSelf.dataArray addObjectsFromArray:weakSelf.bosFirstArray];
                [weakSelf.directoryArray removeAllObjects];
                NSString *title = weakSelf.tierTitleArray[0];
                [weakSelf.tierTitleArray removeAllObjects];
                [weakSelf.tierTitleArray addObject:title];
        
                [weakSelf.collectionView reloadData];
                weakSelf.addButton.hidden = YES;
                return;
            }

        }
        [weakSelf.collectionView.mj_header beginRefreshing];
    };
    self.navView.clickLeftViewBlock = ^(NSInteger tag) {
        weakSelf.fileType  =@"";
        self.tagID  =@"";
        weakSelf.isEdit = NO;
        for (VULFileObjectModel *model in weakSelf.selectModelArray) {
            model.isSelect = NO;
        }
        [weakSelf.selectModelArray removeAllObjects];
        [weakSelf.collectionView reloadData];
        [weakSelf dissmissOperationView];
        
        weakSelf.popup.layoutType = zhPopupLayoutTypeLeft;
        weakSelf.popup.presentationStyle = zhPopupSlideStyleFromLeft;
        weakSelf.popup.maskAlpha = 0.35;
        [weakSelf.popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
        [((AppDelegate *)[UIApplication sharedApplication].delegate).watermarkView addWaterMarkView];

    };
    self.directoryArray = [NSMutableArray array];
    self.navView.clickBlock = ^(NSString * title) {
        NSMutableDictionary *dic = backBlockAndFileType(title);
        self.addButton.hidden = YES;
        self.isZip = YES;
        self.sourceID = @"";
        self.infoTypeID = @"";
        self.keyWord = @"";
        self.navView.searchField.text = @"";
        [self.searchDic removeAllObjects];
        self.navView.moreSearchBtn.hidden = YES;
        _searchView = nil;
        weakSelf.fitstSourceID = @"";
        weakSelf.icon = dic[@"block"];
        weakSelf.fileType =  dic[@"fileType"];
//        垃圾接口 有block不用 非要我们本地判断
                if ([ weakSelf.icon  isEqualToString:@"recentDoc"]) {
                    weakSelf.icon = @"userRencent";
                } else if([self.icon  isEqualToString:@"other"]){
                    weakSelf.icon = @"";
                }else{
                    if ([ weakSelf.icon isEqualToString:@"photo"]) {
                        weakSelf.icon = @"photo";
                        weakSelf.fileType = @"jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai";
                    }
                   
            }
        weakSelf.tgblock = weakSelf.icon;
        [weakSelf.directoryArray removeAllObjects];
        [weakSelf.tierTitleArray removeAllObjects];
        [weakSelf.tierTitleArray addObject:title];
        self.topView.dataArr = self.tierTitleArray;
        [weakSelf.collectionView.mj_header beginRefreshing];


    };
    self.headerView.clickBlock = ^(NSString * title) {
        NSMutableDictionary *dic = backBlockAndFileType(title);
        self.addButton.hidden = YES;
        self.isZip = YES;
        self.sourceID = @"";
        self.infoTypeID = @"";
        self.keyWord = @"";
        self.navView.searchField.text = @"";
        [self.searchDic removeAllObjects];
        self.navView.moreSearchBtn.hidden = YES;
        _searchView = nil;
        weakSelf.fitstSourceID = @"";
        weakSelf.icon = dic[@"block"];
        weakSelf.fileType =  dic[@"fileType"];
//        垃圾接口 有block不用 非要我们本地判断
                if ([ weakSelf.icon  isEqualToString:@"recentDoc"]) {
                    weakSelf.icon = @"userRencent";
                } else if([self.icon  isEqualToString:@"other"]){
                    weakSelf.icon = @"";
                }else{
                    if ([ weakSelf.icon isEqualToString:@"photo"]) {
                        weakSelf.icon = @"photo";
                        weakSelf.fileType = @"jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai";
                    }
                   
            }
        weakSelf.tgblock = weakSelf.icon;
        [weakSelf.directoryArray removeAllObjects];
        [weakSelf.tierTitleArray removeAllObjects];
        [weakSelf.tierTitleArray addObject:title];
        self.topView.dataArr = self.tierTitleArray;
        [weakSelf.collectionView.mj_header beginRefreshing];


    };
    self.sortType = YES;
    self.dataArray = [NSMutableArray array];
    [self.view addSubview:self.collectionView];
    self.topView.changeSortBlock = ^(NSString * _Nonnull title, BOOL tag) {
        [weakSelf.collectionView.mj_header beginRefreshing];
    };
    self.topView.clickInfoTypeBlock = ^(BOOL tag) {
        [self gotoInfoType];
    };
//    self.navView.clickRightBlock = ^(BOOL tag) {
//        weakSelf.isGrid = !weakSelf.isGrid;
//        [weakSelf.collectionView reloadData];
//    };
    self.topView.clickRightBlock = ^(BOOL tag) {
        weakSelf.isGrid = tag;
        [weakSelf.collectionView reloadData];
    };
    self.topView.selectImageWithRow = ^(NSInteger tag, UIImageView * _Nonnull rightArrow) {
        NSString *soureID = @"";
        if (tag == 0 &&   !self.isFav) {
            if (self.flag && [weakSelf.tgblock  isEqualToString: @"firstBox"]) {
                [weakSelf showViewWithData:self.bosFirstArray fromWith:rightArrow withRow:tag];
                return;

            }else{
                if (weakSelf.tgblock ) {
                    weakSelf.icon = weakSelf.tgblock;
                }
                soureID =   weakSelf.fitstSourceID;
                if (!soureID) {
                    return;
                }
            }
        }else{
            if (tag >= self.directoryArray.count) {
                soureID= weakSelf.directoryArray[weakSelf.directoryArray.count-1];
            }else{
                
                soureID= weakSelf.directoryArray[tag];
            }
        }
        if(soureID.integerValue==0){
//            需求变更 导致这里无法获取
            return;
        }
        [weakSelf getDataWithSoureId:soureID completion:^(__kindof VULBaseRequest * _Nonnull request) {
            
            if (request.success) {
                NSArray *folderList = request.data[@"folderList"];
                NSMutableArray *arr = [NSMutableArray array];
                for (NSDictionary *dic in folderList) {
                    VULFileObjectModel *model = [VULFileObjectModel modelWithDictionary:dic];
                    [arr addObject:model];
                }
                [self showViewWithData:arr fromWith:rightArrow withRow:tag];
            }
            
        }];
    };
    self.topView.clickFileNameBlock = ^(NSInteger tag) {
        weakSelf.currentPage =1;
        [weakSelf dissmissOperationView];
        self.infoTypeID = @"";

        if (tag == 0 &&   !self.isFav) {
            if (self.flag && [weakSelf.tgblock  isEqualToString: @"firstBox"]) {
                weakSelf.icon = weakSelf.tgblock;
                [weakSelf.dataArray removeAllObjects];
                [weakSelf.dataArray addObjectsFromArray:weakSelf.bosFirstArray];
                [weakSelf.directoryArray removeAllObjects];
                NSString *title = weakSelf.tierTitleArray[0];
                [weakSelf.tierTitleArray removeAllObjects];
                [weakSelf.tierTitleArray addObject:title];
        
                [weakSelf.collectionView reloadData];
                weakSelf.addButton.hidden = YES;

                return;
            }
            if (weakSelf.isUpload) {
                weakSelf.addButton.hidden = YES;
            }
            if (weakSelf.tgblock ) {
                weakSelf.icon = weakSelf.tgblock;
            }
            weakSelf.sourceID =   weakSelf.fitstSourceID;
            [weakSelf.directoryArray removeAllObjects];
            NSString *title = weakSelf.tierTitleArray[0];
            [weakSelf.tierTitleArray removeAllObjects];
            [weakSelf.tierTitleArray addObject:title];
            if (weakSelf.sourceID) {
                [weakSelf.directoryArray addObject:weakSelf.sourceID];

            }
            [weakSelf requestList];

        } else {
            weakSelf.tierTitleArray = [NSMutableArray arrayWithArray:[weakSelf.tierTitleArray subarrayWithRange:NSMakeRange(0, tag + 1)]];
            if (tag >= self.directoryArray.count) {
                weakSelf.directoryArray = [NSMutableArray arrayWithArray:[weakSelf.directoryArray subarrayWithRange:NSMakeRange(0, weakSelf.directoryArray.count)]];            weakSelf.sourceID = weakSelf.directoryArray[weakSelf.directoryArray.count-1];
//                weakSelf.infoTypeID = weakSelf.directoryArray[weakSelf.directoryArray.count-1];
            }else{
                weakSelf.directoryArray = [NSMutableArray arrayWithArray:[weakSelf.directoryArray subarrayWithRange:NSMakeRange(0, tag + 1)]];
                weakSelf.sourceID = weakSelf.directoryArray[tag];
//                weakSelf.infoTypeID = weakSelf.directoryArray[tag];
            }
            if([self.icon isEqualToString:@"info"] && weakSelf.sourceID.integerValue ==10000){
                self.icon  = @"info";
                weakSelf.infoTypeID = @"";
            }

            if([ weakSelf.sourceID  isEqualToString:@"shareLink"]){
                // 需求挖坑 导致维护成本
                self.icon  = @"shareLink";
                self.sourceID  = @"";
                weakSelf.infoTypeID = @"";
                [weakSelf requestList];
    //                self.icon  = @"";
                return;
            }
            [weakSelf requestList];

        }

    };
    self.isUpload = NO;

    self.leftView.backChooseWithModel = ^(VULFileObjectModel * _Nonnull model) {
        self.addButton.hidden = NO;
        self.isFav = NO;
        [weakSelf.popup dismiss];
        [self.searchDic removeAllObjects];
        _searchView = nil;
        self.isZip = NO;

        self.keyWord = @"";
        self.infoTypeID = @"";
        self.navView.searchField.text = @"";
        self.infoTypeID = model.infoTypeID;
        if (model.sourceID &&model.sourceID.length>0) {
            self.navView.moreSearchBtn.hidden = NO;
            if (isPermissionWithModel(KLanguage(@"上传"), @[model]) || isPermissionWithModel(KLanguage(@"文件夹"), @[model]) ) {
                self.addButton.hidden = NO;

            }else{
                self.addButton.hidden = YES;
                self.isUpload = YES;

            }
            if (self.operation.length>0) {
                self.changeBtn.hidden = NO;
                [self.cancel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.left.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH/2-1);
                }];
                [self.changeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.right.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH/2);
                }];
            }
            weakSelf.icon = model.icon;
            weakSelf.tgblock = model.icon;
            [weakSelf.directoryArray removeAllObjects];
            [weakSelf.tierTitleArray removeAllObjects];
            weakSelf.parentID = model.parentID;

            weakSelf.currentPage = 1;
            [weakSelf.directoryArray removeAllObjects];
          
            if (model.titleArr.count>0) {
                weakSelf.fitstSourceID = model.sourceIDArr[0];
            
                weakSelf.tierTitleArray = [NSMutableArray arrayWithArray:model.titleArr] ;
                [weakSelf.tierTitleArray addObject:KLanguage(model.name)];
                weakSelf.directoryArray= [NSMutableArray arrayWithArray:model.sourceIDArr];
                [weakSelf.directoryArray  addObject:model.sourceID];
            }else{
                weakSelf.fitstSourceID = model.sourceID;
                weakSelf.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(model.name.length>0?model.name:model.groupName)]] ;
                weakSelf.directoryArray = [NSMutableArray array];
                [weakSelf.directoryArray addObject:weakSelf.fitstSourceID];
            }
            if (weakSelf.flag) {
                [weakSelf.tierTitleArray insertObject:KLanguage(@"共享") atIndex:0];
                [weakSelf.directoryArray insertObject:@"firstBox" atIndex:0];
                weakSelf.tgblock = @"firstBox";
            }
            weakSelf.sourceID = model.sourceID;
            weakSelf.topView.dataArr =     weakSelf.tierTitleArray;
            [weakSelf requestList];
        }else{
            self.navView.moreSearchBtn.hidden = YES;
            self.addButton.hidden = YES;
            self.isUpload = YES;
            self.isZip = YES;
            self.infoTypeID = model.infoTypeID;
            if (self.operation.length>0) {
                self.changeBtn.hidden = YES;
                [self.cancel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.left.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH);
                }];
                [self.changeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.right.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(0);
                }];
            }
            if (model.ext.length>0) {
                weakSelf.fileType = model.ext;
            }
            if (model.labelId.length>0) {
                weakSelf.tagID = model.labelId;
            }
//            垃圾接口 有block不用 非要我们本地判断
            if ([model.icon  isEqualToString:@"recentDoc"]) {
                weakSelf.icon = @"userRencent";
            } else if([self.icon  isEqualToString:@"other"]){
                weakSelf.icon = @"";
            }else{
                if ([model.icon  isEqualToString:@"photo"]) {
                    weakSelf.icon = @"photo";
                    weakSelf.fileType = @"jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai";
                }else{
                    weakSelf.icon = model.icon;

                }

            }
            weakSelf.tgblock = weakSelf.icon;
            [weakSelf.directoryArray removeAllObjects];
            [weakSelf.tierTitleArray removeAllObjects];
            weakSelf.fitstSourceID = model.sourceID;
            weakSelf.sourceID = model.sourceID;
            weakSelf.parentID = model.parentID;
            weakSelf.currentPage = 1;
            [weakSelf.directoryArray removeAllObjects];
            weakSelf.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(model.name.length>0?model.name:model.groupName)]] ;
//            [weakSelf.directoryArray addObject:weakSelf.fitstSourceID];
//            weakSelf.sourceID = model.sourceID;
            weakSelf.topView.dataArr =     weakSelf.tierTitleArray;

            [weakSelf requestList];
        }
      
      
    };
    self.isEdit = NO;
    self.selectModelArray = [NSMutableArray array];

    if (!self.isHome) {
//        [self.view addSubview:self.colorImageV];
//        self.navigationViewBackgroundImageView.image = VULGetImage(@"image_radarHeader");
        [self.view addSubview:self.headerView];
        self.headerView.hidden = NO;

//        self.headerView.frame = CGRectMake(0, K_NavBar_Height, VULSCREEN_WIDTH, self.headerView.height);

//        self.colorImageV.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, K_NavBar_Height+self.headerView.height);
        [self.colorImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_offset(0);
            make.top.mas_equalTo(0);
            make.height.mas_equalTo(K_NavBar_Height+self.headerView.height);
        }];
        [self.headerView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_offset(0);
            make.top.mas_equalTo(K_NavBar_Height);
            make.height.mas_equalTo(self.headerView.height);
        }];
        [self.topView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_offset(0);
            make.top.mas_equalTo(self.colorImageV.mas_bottom);
            make.height.mas_equalTo(40);
        }];
//        self.topView.frame = CGRectMake(0, self.colorImageV.bottom, VULSCREEN_WIDTH, 40);

        [self.collectionView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_offset(0);
            make.top.mas_equalTo(  self.topView.mas_bottom).offset(10);
            make.bottom.mas_equalTo(-K_TabBar_Height);
        }];
        [self.view addSubview:self.addButton];
        self.currentPage = 1;
        if (self.isMove ) {;
//            self.colorImageV.frame = CGRectMake(0, 0, VULSCREEN_WIDTH, K_NavBar_Height);
            
            [self.colorImageV mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.left.right.mas_offset(0);
                make.top.mas_equalTo(0);
                make.height.mas_equalTo(K_NavBar_Height);
            }];
            [self.headerView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.left.right.mas_offset(0);
                make.top.mas_equalTo(K_NavBar_Height);
                make.height.mas_equalTo(0);
            }];
            [self.topView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.left.right.mas_offset(0);
                make.top.mas_equalTo(self.colorImageV.mas_bottom);
                make.height.mas_equalTo(40);
            }];
//            self.topView.frame = CGRectMake(0, self.colorImageV.bottom, VULSCREEN_WIDTH, 40);

            self.headerView.hidden = YES;
            [self.collectionView mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.left.right.mas_offset(0);
                make.top.mas_equalTo(  self.topView.bottom+10);
                make.bottom.mas_equalTo(-K_TabBar_Height);
            }];
//            self.navView.trashBtn.hidden = YES;
//            self.navView.cloudBtn.hidden = YES;
//            self.navView.messageBtn.hidden = YES;
//            self.navView.moreSearchBtn.hidden = YES;
//            [self.navView.searchField mas_makeConstraints:^(MASConstraintMaker *make) {
//                make.left.mas_equalTo(self.navView.leftBtn.mas_right).offset(fontAuto(10));
//                make.right.mas_equalTo(-35);
//                make.height.mas_equalTo(25);
//                make.centerY.mas_equalTo(self.navView .mas_centerY);
//            }];
//            [self.collectionView mas_remakeConstraints:^(MASConstraintMaker *make) {
//                make.left.right.mas_offset(0);
//                make.top.mas_equalTo(K_NavBar_Height+50);
//                make.bottom.mas_equalTo(-fontAuto(50));
//            }];
            [self.view addSubview:self.cancel];
            [self.view addSubview:self.changeBtn];
            [self.cancel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.bottom.mas_equalTo(0);
                make.left.mas_offset(0);
                make.height.mas_equalTo(fontAuto(50));
                make.width.mas_equalTo(VULSCREEN_WIDTH/2-1);
            }];
            [self.changeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
                make.bottom.mas_equalTo(0);
                make.right.mas_offset(0);
                make.height.mas_equalTo(fontAuto(50));
                make.width.mas_equalTo(VULSCREEN_WIDTH/2);
            }];
            
            if (self.operation.length>0 && self.flag) {
                self.changeBtn.hidden = YES;
                [self.cancel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.left.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH);
                }];
                [self.changeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.right.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(0);
                }];
            }
        }
        [self getLeftData];
        [self getMessage];
        [self getMessageCount];

        [self getFavMessage];

        
        [VULNotificationCenter addObserver:self selector:@selector(getMessage) name:@"getMessageChangeNotificationCenter" object:nil];

    }else{
        [self.view addSubview:self.addButton];

        [self.collectionView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_offset(0);
            make.top.mas_equalTo(K_NavBar_Height+10);
            make.bottom.mas_equalTo(-10);
        }];
        self.navView.hidden = YES;
        self.addButton.hidden = YES;
        self.navigationTitleColor = [UIColor whiteColor];
        VULButton *timeBtn = [VULButton new];
        [timeBtn setImage:VULGetImage(@"icon_sort") forState:UIControlStateNormal];
        [timeBtn setImage:VULGetImage(@"icon_sort_up") forState:UIControlStateSelected];
        [timeBtn addTarget:self action:@selector(clickTimeBtnTypeBtn:) forControlEvents:UIControlEventTouchUpInside];
        
        VULButton *changeTypeBtn = [VULButton new];
        [changeTypeBtn setImage:VULGetImage(@"icon_block") forState:UIControlStateNormal];
        [changeTypeBtn addTarget:self action:@selector(clickChangeTypeBtn:) forControlEvents:UIControlEventTouchUpInside];
        
        VULButton *changeTypeBtn1 = [VULButton new];
        [changeTypeBtn1 setImage:VULGetImage(@"icon_list") forState:UIControlStateNormal];
        changeTypeBtn1.tag =  1000000;
        [changeTypeBtn1 addTarget:self action:@selector(clickChangeTypeBtn:) forControlEvents:UIControlEventTouchUpInside];
        self.navigationTitleColor = [UIColor blackColor];
        UIImage *image = self.leftButton.imageView.image;
        self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
        [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];

        [self.leftButton.imageView setTintColor:[UIColor blackColor]];
        if ([self.icon isEqualToString:@"recycle"]) {
          
            VULButton *moreBtn = [VULButton new];
            UIImage *image = VULGetImage(@"icon_more_color");
            
            [moreBtn setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
            [moreBtn.imageView setTintColor:HEXCOLOR(0xB2B2B2)];
            [moreBtn addTarget:self action:@selector(operationChangeFile:) forControlEvents:UIControlEventTouchUpInside];
       
            [self baseAddNavRightBtnWithBtns:@[changeTypeBtn,changeTypeBtn1,moreBtn]];

//            [self baseAddNavRightBtnWithBtns:@[timeBtn,changeTypeBtn,moreBtn]];
        }else  if ([self.icon isEqualToString:@"info"]) {
            VULButton *moreBtn = [VULButton new];
            [moreBtn setImage:VULGetImage(@"icon_screen") forState:UIControlStateNormal];
            [moreBtn addTarget:self action:@selector(gotoInfoType) forControlEvents:UIControlEventTouchUpInside];
       
            [self baseAddNavRightBtnWithBtns:@[moreBtn,changeTypeBtn,changeTypeBtn1]];
        }else{
            [self baseAddNavRightBtnWithBtns:@[changeTypeBtn,changeTypeBtn1]];
//            [self baseAddNavRightBtnWithBtns:@[timeBtn,changeTypeBtn]];

        }
        
        self.navigationTitle = backTitleWithBlock(self.icon);
//        垃圾接口 有block不用 非要我们本地判断
                if ([self.icon  isEqualToString:@"recentDoc"]) {
                    weakSelf.icon = @"userRencent";
                } else if([self.icon  isEqualToString:@"other"]){
                    weakSelf.icon = @"";
                }else{
                    if ([self.icon  isEqualToString:@"photo"]) {
                        weakSelf.fileType = @"jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai";
                    }else{
                        if ([self.icon  isEqualToString:@"tag"] ) {
                            self.icon  = @"fileTag";
                            self.navView.hidden = YES;
                            self.topView.leftBtn.hidden =self.topView.timeBtn.hidden = self.topView.changeTypeBtn.hidden = YES;
                            self.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(@"标签")]] ;
                            self.topView.dataArr =     self.tierTitleArray;
//                            [self.topView.leftBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
//                                    make.left.mas_equalTo(fontAuto(12));
//                                    make.height.width.mas_equalTo(0);
//                                    make.centerY.mas_equalTo(self.topView.mas_centerY);
//                            }];
                            self.topView.tierView.collectionView.frame = CGRectMake(15, 0, VULSCREEN_WIDTH -30, 40);

                            [self.collectionView mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.left.right.mas_offset(0);
                                make.top.mas_equalTo(K_NavBar_Height+50);
                                make.bottom.mas_equalTo(0);
                            }];
                     }
                    if ([self.icon  isEqualToString:@"shareLink"] ) {
                        self.navView.hidden = YES;
                        self.topView.leftBtn.hidden =self.topView.timeBtn.hidden = self.topView.changeTypeBtn.hidden = YES;
                        self.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(@"分享的")]] ;
                        self.topView.dataArr =     self.tierTitleArray;
//                        [self.topView.leftBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
//                                make.left.mas_equalTo(fontAuto(12));
//                                make.height.width.mas_equalTo(0);
//                                make.centerY.mas_equalTo(self.topView.mas_centerY);
//                        }];
                        [self.collectionView mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.left.right.mas_offset(0);
                            make.top.mas_equalTo(K_NavBar_Height+50);
                            make.bottom.mas_equalTo(0);
                        }];
                        self.topView.tierView.collectionView.frame = CGRectMake(15, 0, VULSCREEN_WIDTH -30, 40);

                    }
                        
             }
                    self.tgblock = self.icon;
        }
        self.currentPage = 1;
        [self requestList];
    }
    getUploadFileWithModel();
    
    [VULNotificationCenter addObserver:self selector:@selector(uploadChange) name:@"uploadChangeNotificationCenter" object:nil];
    [VULNotificationCenter addObserver:self selector:@selector(uploadShareImage) name:@"shareChangeNotificationCenter" object:nil];
    [VULNotificationCenter addObserver:self selector:@selector(shareWaterView) name:@"shareWaterViewNotificationCenter" object:nil];

    if(self.isSelect){
        [self baseAddNavLeftBtnWithTitle:KLanguage(@"取消") selector:@selector(dismissView)];
    }
    
    
    [self getUpload];
    // Do any additional setup after loading the view.
}


- (void)pasteboardChanged:(NSNotification *)notification {
    // 获取当前剪贴板的字符串内容
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    NSString *copiedString = pasteboard.string;
    self.htmlView.textView.text =copiedString;
    // 判断是否为直接粘贴
    if ([copiedString isEqualToString:pasteboard.string]) {
        NSLog(@"User directly pasted: %@", copiedString);
        // 在这里执行您需要的操作
    }
}
-(void)dismissView{
    [self dismissViewControllerAnimated:YES completion:nil];
}
-(void)gotoInfoType{
    FBInfoTypeView *addView = [[FBInfoTypeView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH * 0.8, VULSCREEN_HEIGHT)];
    zhPopupController *popup = [[zhPopupController alloc] initWithView:addView size:CGSizeMake(VULSCREEN_WIDTH * 0.8, VULSCREEN_HEIGHT)];
    popup.layoutType = zhPopupLayoutTypeRight;
    popup.presentationStyle = zhPopupSlideStyleFromRight;
    popup.maskAlpha = 0.35;
    [popup showInView:self.view duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
    addView.selectBackModel = ^(NSString *typeString){
            [self.dataArray removeAllObjects];
            self.infoTypeID = typeString;
            [self requestList];
    };
}
-(void)shareWaterView{
    [VULQueue executeInMainQueue:^{
        [((AppDelegate *)[UIApplication sharedApplication].delegate).watermarkView addWaterMarkView];
    } delay:0.05];

}
-(void)uploadShareImage{
    FBDownOrUploadVC *vc = [FBDownOrUploadVC new];
    [self.navigationController pushViewController:vc animated:YES];
}
  
-(void)showViewWithData:(NSMutableArray *)arr fromWith:(UIImageView *)image withRow:(NSInteger )tag{
    NSMutableArray *titleArr  = [NSMutableArray array];
    NSMutableArray *imageArr =[NSMutableArray array];
    for (VULFileObjectModel *model in arr) {
        [titleArr addObject:model.name];
        [imageArr addObject:@"icon_folder_main"];
    }
    
    

    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (NSInteger i = 0; i < titleArr.count; i++) {
        UIImage *rightImage = nil;
        YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:VULGetImage(imageArr[i]) rightImage:rightImage handler:^(YCMenuAction *action) {
            self.tierTitleArray = [NSMutableArray arrayWithArray:[self.tierTitleArray subarrayWithRange:NSMakeRange(0, tag + 1)]];
            if (tag >= self.directoryArray.count) {
                self.directoryArray = [NSMutableArray arrayWithArray:[self.directoryArray subarrayWithRange:NSMakeRange(0, self.directoryArray.count)]];
            }else{
                self.directoryArray = [NSMutableArray arrayWithArray:[self.directoryArray subarrayWithRange:NSMakeRange(0, tag + 1)]];
            }
            

            NSInteger row =  [array indexOfObject:action];
            VULFileObjectModel *model =arr[row];
            self.parentModel = model;
             if (isPermissionWithModel(KLanguage(@"上传"), @[model])||isPermissionWithModel(KLanguage(@"文件夹"), @[model])) {
                 self.addButton.hidden = NO;
             }else{
                 self.addButton.hidden = YES;

             }
             if ([self.icon isEqualToString:@"shareLink"]) {
                 self.icon = @"";
             }
             self.collectionView.contentOffset = CGPointMake(0, 0);
             self.sourceID = model.sourceID;
             self.sourceLevel = model.parentLevel;
             self.parentID = model.parentID;
             self.isFav = NO;
             if (([self.tgblock isEqualToString:@"fav"] ||  self.tagID.integerValue>0)&&model.parentID.integerValue>0) {
                 
                 [self getParentSourceListWtihModel:model];
                 return;
             }
             
             [self.tierTitleArray addObject:model.name.length>0?model.name:model.groupName];
             self.topView.dataArr = self.tierTitleArray;
             [self.directoryArray addObject:model.sourceID];
             if (self.tierTitleArray.count>self.directoryArray .count) {
                 [self.directoryArray insertObject:@"0" atIndex:0];
             }
             self.currentPage = 1;
             [self requestList];

        }];
        [array addObject:action];
    }
    NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
    CGFloat width = 160;
      if ([appLanguage isEqualToString:@"en"]) {
          width =200;
      }
    YCMenuView *menuView = [YCMenuView menuWithActions:array width:width relyonView:image];
    menuView.menuColor = [UIColor whiteColor];
    menuView.separatorColor = [UIColor redColor];
    menuView.maxDisplayCount = 14;
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
-(void)getDataWithSoureId:(NSString *)soureId completion:(VULRequestCompletion)requestSucess{

    [VULBaseRequest requestWithUrl:@"/api/disk/list/path" params:@{
        @"fileType":@"folder",@"fromType":@(true),@"sourceID":soureId,@"currentPage":@(1),@"pageSize":@"100"
    } requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
    
        if (requestSucess) {
            requestSucess(request);
        }


    }];
    
}
-(void)getMessageCount{
//
    
    [VULBaseRequest requestWithUrl:@"/api/notice/unread" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (request.success) {
            [VULQueue executeInMainQueue:^{
                NSString *total =  request.data;
                if(self.headerView){
                    [VULQueue executeInMainQueue:^{
                        NSInteger count = 2;
                        if(!isTreeOpen(@"myFav")){
                            count = count-1;
                        }
                        UILabel *label = self.headerView.labelArray[count];

                        if(total.integerValue == 0){
                            label.hidden = YES;
                        }else{
                            label.hidden = NO;
                        }
                        
                        if (total.integerValue>99) {
                            label.text =  @"99+";

                        }else{
                            label.text =  [NSString stringWithFormat:@"%@",total];

                        }
                    }];
                }
            
          
            }];

        } else
            [self makeToast:request.message];
   
    }];
}
-(void)getMessage{
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:@{@"fileType":@"all",@"currentPage":@(self.currentPage),@"pageSize":@"100"} ];
  
    [dic setValue:@"recycle" forKey:@"block"];

    

    [VULBaseRequest requestWithUrl:@"/api/disk/list/path" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (request.success) {
            [VULQueue executeInMainQueue:^{
                NSString *total =  request.data[@"total"];
                if(self.headerView){
                    [VULQueue executeInMainQueue:^{
                        NSInteger count = 1;
                        if(!isTreeOpen(@"myFav")){
                            count = count-1;
                        }
                        UILabel *label = self.headerView.labelArray[count];

                        if(total.integerValue == 0){
                            label.hidden = YES;
                        }else{
                            label.hidden = NO;
                        }
                        
                        if (total.integerValue>99) {
                            label.text =  @"99+";

                        }else{
                            label.text =  [NSString stringWithFormat:@"%@",request.data[@"total"]];

                        }
                    }];
                }
              
          
            }];

        } else
            [self makeToast:request.message];
   
    }];
    
}

-(void)getFavMessage{
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:@{@"fileType":@"all",@"currentPage":@(self.currentPage),@"pageSize":@"100"} ];
  
    [dic setValue:@"fav" forKey:@"block"];

    if(!isTreeOpen(@"myFav")){
        return;
    }

    [VULBaseRequest requestWithUrl:@"/api/disk/list/path" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (request.success) {
            if(self.headerView){
                [VULQueue executeInMainQueue:^{
                    NSString *total =  request.data[@"total"];
                    if(!isTreeOpen(@"myFav")){
                        return;
                    }
                    [VULQueue executeInMainQueue:^{
                        UILabel *label = self.headerView.labelArray[0];
                        
                        if(total.integerValue == 0){
                            label.hidden = YES;
                        }else{
                            label.hidden = NO;
                        }
                        
                        if (total.integerValue>99) {
                            label.text =  @"99+";
                            
                        }else{
                            label.text =  [NSString stringWithFormat:@"%@",request.data[@"total"]];
                            
                        }
                    }];
                    
                }];}

        } else
            [self makeToast:request.message];
   
    }];
    
}

-(void)clickTimeBtnTypeBtn:(VULButton *)sender{
    sender.selected = !sender.selected;
    self.sortType = !self.sortType;
    [self.collectionView.mj_header beginRefreshing];
}
-(void)clickChangeTypeBtn:(VULButton *)sender{
    if(sender.tag ==1000000){
        self.isGrid = NO;
    }else{
        self.isGrid  = YES;
    }
    [self.collectionView reloadData];
}
-(void)getUpload{
    NSInteger count = 0 ;
    NSMutableDictionary *progressDict = [DownloadProgress sharedInstance].progressDict;
    NSArray *arr =  progressDict.allKeys;
    for (NSString *filePath in arr) {
        UBUploadModel *model = progressDict[filePath];
        if (!model.isSucess) {
            count =count+1;
        }
    }
    NSInteger count1 = 3;
    if(!isTreeOpen(@"myFav")){
        count1 = count1-1;
    }
    if(self.headerView){
        UILabel *label = self.headerView.labelArray[count1];
        if(count == 0){
            label.hidden = YES;
        }else{
            label.hidden = NO;
        }
        
        //    NSMutableArray *downloading = KDownloadManager.downinglist;
        //    count = count+downloading.count;
        [VULQueue executeInMainQueue:^{
            if (count>99) {
                
                label.text =  @"99+";
                
            }else{
                label.text =  [NSString stringWithFormat:@"%ld",count];
                
            }
        }];
        
    }
}
// 实现观察者方法
- (void)uploadChange{
        [self getUpload];
}
-(VULButton *)cancel{
    if (!_cancel) {
        _cancel = [VULButton new];
        [_cancel setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
        [_cancel setBackgroundColor:BtnColor];
        [_cancel setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
        [_cancel addTarget:self action:@selector(clickCancelBtn) forControlEvents:UIControlEventTouchUpInside];
    }
    return _cancel;
}
-(VULButton *)changeBtn{
    if (!_changeBtn) {
        _changeBtn = [VULButton new];
        if ([self.operation isEqualToString:@"ZIP"]) {
            [_changeBtn setTitle:KLanguage(@"解压到当前") forState:UIControlStateNormal];
        }else if ([self.operation isEqualToString:@"copy"]) {
            [_changeBtn setTitle:KLanguage(@"复制到此处") forState:UIControlStateNormal];
        }else if ([self.operation isEqualToString:@"upload"]) {
            [_changeBtn setTitle:KLanguage(@"上传到此处") forState:UIControlStateNormal];
        }else if ([self.operation isEqualToString:@"backUp"]) {
            [_changeBtn setTitle:KLanguage(@"选择当前文件夹") forState:UIControlStateNormal];
        }else{
            [_changeBtn setTitle:KLanguage(@"剪切到此处") forState:UIControlStateNormal];
        }
        
        
        
        [_changeBtn setBackgroundColor:BtnColor];
        [_changeBtn setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
        [_changeBtn addTarget:self action:@selector(clickChangeBtn) forControlEvents:UIControlEventTouchUpInside];
    }
    return _changeBtn;
}
-(void)clickCancelBtn{
    
    [self dismissViewControllerAnimated:YES completion:^{
            
    }];
}
#pragma mark 复制和移动
-(void)clickChangeBtn{
    if ([self.operation isEqualToString:@"backUp"]){
        if(self.selectFolder){
            self.selectFolder([self.topView.dataArr componentsJoinedByString:@"/"], self.sourceID);
        }
        [self clickCancelBtn];
        return;
    }
    
    if ([self.operation isEqualToString:@"upload"]) {
        if( [DownloadProgress sharedInstance].isOther){
            [DownloadProgress sharedInstance].isOther = NO;
            [ChunkUploader writeDataWithModel:[DownloadProgress sharedInstance].otherUrl sourceID:self.sourceID isPic:NO  success:^(bool sucess) {
                if (sucess) {
           
                }
            }];
            [self dismissViewControllerAnimated:YES completion:^{
                [VULNotificationCenter postNotificationName:@"shareChangeNotificationCenter" object:nil];
            }];
            return;
        }
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"yyyy-MM-dd-HH:mm:ss"];
        NSString *key = [[formatter stringFromDate:[NSDate date]] stringByAppendingString: [NSString stringWithFormat:@"%ld.jpg",0]];
        NSUserDefaults *userDefaults = [[NSUserDefaults alloc] initWithSuiteName:@"group.com.wuji.share.filesbox"];
        NSData *data = [userDefaults objectForKey:@"sharedImageData"];
        NSString *number = [userDefaults objectForKey:@"sharedImage"];

        if ([number isEqualToString:@"mp4"]) {
            NSString *filename = [userDefaults objectForKey:@"sharedUrl"];
            NSURL   *sharedContainerURL = [[NSFileManager defaultManager] containerURLForSecurityApplicationGroupIdentifier:@"group.com.wuji.share.filesbox"];
            NSURL *url = [sharedContainerURL URLByAppendingPathComponent:filename];

            [ChunkUploader writeDataWithModel:url  sourceID:self.sourceID isPic:NO  success:^(bool sucess) {
                if (sucess) {
                    self.currentPage =1;
                    [self requestList];
                }
            }];
            [self dismissViewControllerAnimated:YES completion:^{
                [VULNotificationCenter postNotificationName:@"shareChangeNotificationCenter" object:nil];
            }];
            return;
        }

        UIImage *image = [UIImage imageWithData:data];
    
        // 解决图片偏转90度的问题
        if(image.imageOrientation != UIImageOrientationUp) {
            UIGraphicsBeginImageContext(image.size);
            [image drawInRect:CGRectMake(0, 0, image.size.width, image.size.height)];
            image = UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();
        }
        [[SDImageCache sharedImageCache] storeImage:image forKey:key toDisk:YES completion:^{
            NSString *filePath = [[SDImageCache sharedImageCache] cachePathForKey:key];
            [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:filePath]  sourceID:self.sourceID isPic:YES  success:^(bool sucess) {
                if (sucess) {
                    self.currentPage =1;
                    [self requestList];
            
                }
            }];
            [self dismissViewControllerAnimated:YES completion:^{
                [VULNotificationCenter postNotificationName:@"shareChangeNotificationCenter" object:nil];
            }];
          
        }];
        return;
    }
    if ([self.operation isEqualToString:@"ZIP"]) {
        if(self.isZipModel){
            if(self.isZipModel.ispassWord){
                VULInputTitleView *view = [[VULInputTitleView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) title:KLanguage(@"该压缩包需要密码")];
                view.textField.placeholder = KLanguage(@"请输入密码");
                view.alertInputViewBlock = ^(NSString * _Nonnull text) {
                    
                    [view hiddenView];
                    [self unZipWtihModel:self.isZipModel password:text isPassword:YES];
                
                };
                [view showInView];
            }else{
                [self unZipWtihModel:self.isZipModel password:@"" isPassword:NO];

            }
            
        }else{
            [self unZIPFileWithType:@"current"];
        }
        return;
    }
    [VULAllFileModel copyFile:self.changeArray operationType:self.operation sourceID:self.sourceID sourceLevel:self.sourceLevel completion:^(__kindof VULBaseRequest * _Nonnull request) {
        if (request.success) {
            if (self.saveAndRefreshBlock) {
                self.saveAndRefreshBlock();
            }
            [self clickCancelBtn];
        }else
            [self makeToast:request.message];
        
    }];
}
-(void)unZipWtihModel:(VULFileZIPObjectModel *)model password:(NSString *)passWord isPassword:(BOOL)flag{
    NSDictionary *dic = @{@"sourceID":model.sourceID,@"fullName":model.fileName,@"index":model.index,@"directory":@(model.directory.boolValue),@"sourceIDTo":self.sourceID};
    if(flag){
        dic = @{@"sourceID":model.sourceID,@"fullName":model.fileName,@"index":model.index,@"directory":@(model.directory.boolValue),@"sourceIDTo":self.sourceID,@"password":passWord};
    }
    [VULBaseRequest requestWithUrl:@"/api/disk/unZip" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
           
           [self dissmissHudView];
           if (request.success) {
               if(self.saveAndRefreshBlock){
                   self.saveAndRefreshBlock();
               }
               [self dismissViewControllerAnimated:YES completion:^{
                       
               }];
           } else
               [self makeToast:request.message];
       }];
}
-(NSString *)getTitleWithIcon{
    if ([self.icon isEqualToString:@"recycle"]) {
        return KLanguage(@"回收站");
    }
    if ([self.icon isEqualToString:@"copy"]) {
        return KLanguage(@"个人");
    }
    return @"";
}

#pragma mark -回收站所有文件操作
-(void)operationChangeFile:(VULButton *)sender{
    
    if ([self.icon isEqualToString:@"copy"]) {
        return;
    }

    NSArray *titleArr = @[KLanguage(@"还原所有"), KLanguage(@"清空回收站")];
    NSArray *imageArr = @[@"icon_operation_reduction", @"icon_operation_all_del"];
    
//    FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, titleArr.count*BtnCell+K_StatusBar_Height)];
//    top.index =1;
//    top.titleArr = titleArr;
//    top.iconArr = imageArr;
//    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
//    popup2.layoutType = zhPopupLayoutTypeTop;
//    popup2.presentationStyle = zhPopupSlideStyleFromTop;
//    popup2.maskAlpha = 0.35;
//    top.clickViewBlock = ^(NSString * _Nonnull title) {
//        [popup2 dismiss];
//
//    };
//    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];

    NSMutableArray *array = [[NSMutableArray alloc] init];
    for (NSInteger i = 0; i < titleArr.count; i++) {
        YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:VULGetImage(imageArr[i])  handler:^(YCMenuAction *action) {
            if ([action.title isEqualToString:KLanguage(@"还原所有")]) {
                [self showWaitHudWithString:nil];

                [VULAllFileModel  operationFile:self.dataArray operationType:@"restore" completion:^(__kindof VULBaseRequest * _Nonnull request) {
                    [self dissmissHudView];
                    if (request.success) {
                        [self dissmissOperationView];
                        self.currentPage =1;
                        [self requestList];
                        [VULNotificationCenter postNotificationName:@"getMessageChangeNotificationCenter" object:nil];

                    } else
                        [self makeToast:request.message];
                }];
            }else{
                UIAlertController *alertController = [UIAlertController alertControllerWithTitle:KLanguage(@"确定清空回收站吗?文件无法还原") message:nil preferredStyle:UIAlertControllerStyleAlert];
                     [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
                         MJWeakSelf
                         [self showWaitHudWithString:nil];
                         
                         [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:@{@"operation":@"removeAll"} requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                             [self dissmissHudView];
                             if (request.success) {
                                 [self dissmissOperationView];
                                 self.currentPage =1;
                                 [self requestList];
                                 [VULNotificationCenter postNotificationName:@"getMessageChangeNotificationCenter" object:nil];
                             } else
                                 [self makeToast:request.message];
                         }];
                    }]];
                     [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {

                               NSLog(@"取消");

                       }]];
                    [self presentViewController:alertController animated:YES completion:nil];
            
                return;
              
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
    menuView.cornerRaius = 0;
    menuView.separatorIndexArray = @[@(2)];
    menuView.separatorColor = [UIColor redColor];
    menuView.maxDisplayCount = 20;
    //menuView.offset = 150;
    menuView.textColor = UIColorHex(333333);
    menuView.textFont = [UIFont yk_pingFangRegular:14];
    menuView.menuCellHeight = 35;
    menuView.dismissOnselected = YES;
    menuView.dismissOnTouchOutside = YES;
    menuView.backgroundColor = [UIColor whiteColor];
    [menuView show];
}
-(void)viewWillDisappear:(BOOL)animated{
    [self dissmissOperationView];
    [self.collectionView reloadData];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"firstFL"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"secondFL"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"thirdFL"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"getInfoTypeList"];

    
}

-(void)viewWillAppear:(BOOL)animated{
//    [self.navView.progress.firstWaveLayer addAnimation:self.navView.progress.anim forKey:@"translate"];
//    [self.navView.progress.secondWaveLayer addAnimation:self.navView.progress.anim forKey:@"translate"];
    [self getFileBoxInfo];
    if (!self.isHome) {
        [self getMessage];
        [self getMessageCount];
        [self getFavMessage];
    }
//    if (self.dataArray.count==0) {
//        [self.collectionView.mj_header beginRefreshing];
//    }
}
-(void)getFileBoxInfo{
    [VULBaseRequest requestWithUrl:@"/api/disk/options" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        NSString *success =request.responseObject[@"success"];

        if (success.boolValue) {
            //TODO: 获取登录信息
//            NSString *zh_CN = request.data[@"lang"];
//            if ([zh_CN isEqualToString:@"zh_CN"]) {
//                [[NSUserDefaults standardUserDefaults] setObject:@"zh-Hans" forKey:@"appLanguage"];
//            }else{
//                [[NSUserDefaults standardUserDefaults] setObject:@"en" forKey:@"appLanguage"];
//            }
            VULResponseLoginModel *loginModel = [VULResponseLoginModel modelWithDictionary:request.responseObject[@"data"][@"user"]];
            [VULRealmDBManager updateLocaPersonalInformation:loginModel];
            
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"role"] forKey:@"role"];
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"markConfig"] forKey:@"markConfig"];
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"markConfig"][@"shareLinkAllow"] forKey:@"shareLinkAllow"];
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"desktop"][@"sourceID"] forKey:@"sourceID"];

            
            
            [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%@",request.responseObject[@"data"][@"targetSpace"][@"sizeMax"]] forKey:@"sizeMax"];
            [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%@",request.responseObject[@"data"][@"targetSpace"][@"sizeUse"]] forKey:@"sizeUse"];
    
            [[NSUserDefaults standardUserDefaults] setObject:request.data[@"treeOpen"] forKey:@"treeOpen"];

            [((AppDelegate *)[UIApplication sharedApplication].delegate).watermarkView addWaterMarkView];
        }
    }];
}
-(void)getLeftData{

    [VULBaseRequest requestWithUrl:@"/api/disk/list/path" params:@{@"block":@"root"} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        
        if (request.success) {
            if([ChunkUploader sharedManager].isUploadModel.isOpen){
                [[ChunkUploader sharedManager] backupFile];
            }
            self.allModel = [VULAllFileModel modelWithDictionary:request.responseObject[@"data"]];
            self.leftView.treeNodes =[NSMutableArray arrayWithArray:self.allModel.folderList];
            if (self.flag) {
                VULFileObjectModel *model  ;
                for ( int i = 0; i <self.allModel.folderList.count; i ++ ) {
                    VULFileObjectModel *model1 = self.allModel.folderList[i];
                    if ([model1.icon isEqualToString:@"position"]) {
                        model = model1;
                        break;
                    }
                }
                for (int i= 0 ; i <model.children.folderList.count; i ++) {
                    VULFileObjectModel *model1 = model.children.folderList[i];
                    model1.isFolder = @"1";
                    if (![model1.icon isEqualToString:@"fav"]&& model1.sourceID.intValue>0) {
//                        需求换菜了
                        if(![model1.icon isEqualToString:@"space"]){
                            model1.name = KLanguage(  model1.name);
                            [self.dataArray addObject:model1];
                        }

                    }
                }
                self.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(@"共享")]] ;
                self.tgblock = @"firstBox";
                self.topView.dataArr =     self.tierTitleArray;
                VULFileObjectModel *model2 = [VULFileObjectModel new];
                model2.icon = @"shareLink";
                model2.name = KLanguage(@"我的分享");
                model2.type = @"folder";
                model2.isFolder = @"1";
                [self.dataArray insertObject:model2 atIndex:0];
                self.bosFirstArray = [NSMutableArray arrayWithArray:self.dataArray];
                [self.collectionView reloadData];
                self.addButton.hidden = YES;

            }else{
                if (isPermissionWithModel(KLanguage(@"上传"), @[[VULFileObjectModel new]]) || isPermissionWithModel(KLanguage(@"文件夹"), @[[VULFileObjectModel new]])) {
                    self.addButton.hidden = NO;
                }else{
                    self.addButton.hidden = YES;
                }
                [self setSourceID];
                [self requestList];
                self.topView.dataArr =     self.tierTitleArray;

            }
         

        } else
            [self makeToast:request.message];

    }];


}
-(void)setSourceID{

    [self getPersonModelWithCode:self.flag?@"box":@"space"];
    if(self. sourceID.intValue<=0){
        [self getPersonModelWithCode:@"box"];
    }
    
}
-(void )getPersonModelWithCode:(NSString *)icon{
    for ( int i = 0; i <self.allModel.folderList.count; i ++ ) {
        VULFileObjectModel *model = self.allModel.folderList[i];
        VULFileObjectModel *resultModel  = [self getPersonModelWithChildModel:model WithCode:icon];
        if ([resultModel.icon isEqualToString:icon]) {
            
            self.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(resultModel.name.length>0?resultModel.name:resultModel.groupName)]] ;
            self.fitstSourceID = resultModel.sourceID;
            self. sourceID = resultModel.sourceID;
            self.sourceLevel = resultModel.parentLevel;
            self.parentID = resultModel.parentID;

            self.directoryArray  = [NSMutableArray arrayWithObject:resultModel.sourceID?resultModel.sourceID:@""];

            break;
        }
    }
}
-(VULFileObjectModel *)getPersonModelWithChildModel:(VULFileObjectModel *)model WithCode:(NSString *)icon{
    VULFileObjectModel *resultModel = nil;
    for ( int i = 0; i <model.children.folderList.count; i ++ ) {
        VULFileObjectModel *model1 = model.children.folderList[i];
        if (model1.children.folderList.count>0) {
            VULFileObjectModel *mode2 = [self getPersonModelWithChildModel:model1 WithCode:icon];
            if ([mode2.icon isEqualToString:icon]) {
                resultModel =mode2;break;
            }
        }else{
            if ([model1.icon isEqualToString:icon]) {
                resultModel = model1;break;
            }
        }
    
    }
    return resultModel;
}
-(NSMutableArray *)imageArray{
    if (!_imageArray) {
        _imageArray =[NSMutableArray array];;
    }
    return _imageArray;
}
#pragma mark -请求
-(void)requestList{
    if (self.flag && [self.tgblock  isEqualToString: @"firstBox"]  &&     self.tierTitleArray .count == 1 &&self.keyWord.length == 0) {
        [self.collectionView.mj_header endRefreshing];
        [self.collectionView.mj_footer endRefreshing];
        [self dissmissHudView];
        return;
    }
    
    NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithDictionary:@{@"fileType":@"all",@"currentPage":@(self.currentPage),@"pageSize":@"100"} ];
    if (self.sourceID &&self.sourceID.length>0){
        [dic setValue:self.sourceID forKey:@"sourceID"];
    }
    if (self.icon && !self.isMove) {
        if (self.sourceID.length == 0 || !self.sourceID) {
            [dic setValue:self.icon forKey:@"block"];
        }
    }
    if (self.fileType.length>0) {
        [dic setValue:self.fileType forKey:@"fileType"];
    }
    if (self.tagID.length>0) {
        [dic setValue:self.tagID forKey:@"tagID"];
    }
    if (self.keyWord.length>0) {
        [dic setValue:   self.keyWord forKey:@"keyword"];
    }
    if (self.topView.isClick) {
        [dic setValue:getSortFieldWithTitle(self.topView.sorTiTle) forKey:@"sortField"];
        [dic setValue:self.topView.isSort?@"up":@"down" forKey:@"sortType"];
    }
    if (self.repeatSourceID.length>0) {
        [dic setValue:   self.repeatSourceID forKey:@"repeatSourceID"];
        [dic setValue:   @"" forKey:@"hashMd5"];
        [dic setValue:   @"0" forKey:@"sourceID"];
        
        
    }
    if (self.repeatName.length>0) {
        [dic setValue:   self.repeatName forKey:@"repeatName"];
    }
    //    [dic setValue:self.sortType?@"asc":@"desc" forKey:@"sortType"];
    
    [dic addEntriesFromDictionary:self.searchDic ];
    
    if (self.currentPage ==1) {
        [self.dataArray removeAllObjects];
    }
    NSString *url =@"/api/disk/list/path";
    //    为啥一个公用的东西 要换新接口 不理解
    if ([self.icon isEqualToString:@"shareLink"] || [self.icon isEqualToString:@"shareToMe"]) {
        url =@"/api/disk/userShare/list";
        [dic removeAllObjects];
        [dic addEntriesFromDictionary:@{@"isShare":@"1",@"currentPage":@(self.currentPage),@"pageSize":@"100"} ];
        
    }
    [self.imageArray removeAllObjects];
    [self.topView.leftBtn setImage:VULGetImage(@"icon_new_sort_up") forState:UIControlStateNormal];

    if ([self.icon isEqualToString:@"info"]||self.infoTypeID .integerValue>0 ){
        [self.topView.leftBtn setImage:VULGetImage(@"icon_screen") forState:UIControlStateNormal];
        self.addButton.hidden = YES;
        url =@"/api/disk/getInfoList";
        [dic removeAllObjects];
        [dic addEntriesFromDictionary:@{@"currentPage":@(self.currentPage),@"pageSize":@"100",@"status":@(1)} ];
        if(self.infoTypeID .integerValue>0){
            [dic setValue:self.infoTypeID forKey:@"infoTypeID"];
        }
        
        
        [VULBaseRequest requestWithUrl:url params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
            self.repeatSourceID  = @"";
            self.repeatName  = @"";
            [self dissmissHudView];
            if (request.success) {
                
                NSArray *arr = request.data[@"list"];
                for(NSDictionary *dic in arr){
                    VULInfoModel *model = [VULInfoModel modelWithDictionary:dic];
                    [self.dataArray addObject:model];
                }
           
//                NSSortDescriptor *sortSD = [NSSortDescriptor sortDescriptorWithKey:@"sort" ascending:NO];//ascending:YES 代表升序 如果为NO 代表降序
//                self.dataArray = [[self.dataArray sortedArrayUsingDescriptors:@[sortSD]] mutableCopy];
                [self.collectionView reloadData];
                
                [self.collectionView configEmptyViewWithHasData:self.dataArray.count > 0 hasError:!request.success insets:UIEdgeInsetsMake(0, 0, 0, 0) image:VULGetImage(@"no_data") info:nil reloadBlock:^{
                    
                }];
             
            } else
                [self makeToast:request.message];
            [self.collectionView.mj_header endRefreshing];
            [self.collectionView.mj_footer endRefreshing];
        }];

    }else{
     
        [VULBaseRequest requestWithUrl:url params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
            self.repeatSourceID  = @"";
            self.repeatName  = @"";
            [self dissmissHudView];
            if (request.success) {
                self.conentModel =[VULAllFileModel modelWithDictionary:request.responseObject[@"data"]];
                [self.dataArray addObjectsFromArray:self.conentModel.folderList];
                if (!self.isMove) {
                    [self.dataArray addObjectsFromArray:self.conentModel.fileList];
                }
           
                NSSortDescriptor *sortSD = [NSSortDescriptor sortDescriptorWithKey:@"sort" ascending:NO];//ascending:YES 代表升序 如果为NO 代表降序
                self.dataArray = [[self.dataArray sortedArrayUsingDescriptors:@[sortSD]] mutableCopy];
                NSMutableArray *selectArr = [NSMutableArray array];
                for (VULFileObjectModel *model1 in self.selectModelArray) {
                    for (VULFileObjectModel *model in self.dataArray) {
                        if (model.sourceID.integerValue == model1.sourceID.integerValue) {
                            model.isSelect = YES;
                            [selectArr addObject:model];
                        }
                    }
                }
                [self getImageData];
                [self.selectModelArray removeAllObjects];
                [self.selectModelArray addObjectsFromArray:selectArr];
                [self.collectionView reloadData];
                [self.collectionView configEmptyViewWithHasData:self.dataArray.count > 0 hasError:!request.success insets:UIEdgeInsetsMake(0, 0, 0, 0) image:VULGetImage(@"no_data") info:nil reloadBlock:^{
                    
                }];
                if (self.isHome && [self.tgblock  isEqualToString: @"fileTag"]  &&     self.tierTitleArray .count == 1 &&self.dataArray.count==0) {
                    [self gotoDetil];
                }
            } else
                [self makeToast:request.message];
            [self.collectionView.mj_header endRefreshing];
            [self.collectionView.mj_footer endRefreshing];
        }];

    }
    
 
    
}
-(void)getImageData{
    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd",@"webp"];

    for (VULFileObjectModel *model in  self.dataArray) {
        if ([picArray containsObject:model.fileType]) {
            [self.imageArray addObject:model];
        }
    }
}
-(void)gotoDetil{
    if (isPermissionWithModel(KLanguage(@"标签"), @[[VULFileObjectModel new]])) {
        NSArray *titleArr = @[ KLanguage(@"标签管理")];
        NSArray *imageArr = @[@"icon_operation_tag"];
        FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, titleArr.count*BtnCell+K_BottomBar_Height)];
        top.dataArray = [NSMutableArray arrayWithArray:self.dataArray];
        top.index =0;
        top.titleArr = titleArr;
        top.iconArr = imageArr;
        zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
        popup2.layoutType = zhPopupLayoutTypeBottom;
        popup2.presentationStyle = zhPopupSlideStyleFromBottom;
        popup2.maskAlpha = 0.35;
        top.clickViewBlock = ^(NSString * _Nonnull title) {
            [popup2 dismiss];
            
            if ([title isEqualToString: KLanguage(@"标签管理")]) {
                FBFileTagManagementVC *vc =[FBFileTagManagementVC new];
                vc.saveAndRefreshBlock = ^{
                    [self.collectionView.mj_header beginRefreshing];
                };
                [self.navigationController pushViewController:vc animated:YES];
                return;
            }
        
          
        };
        [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
    }
}

-(void)getParentSourceListWtihModel:(VULFileObjectModel *)model{
//    api/disk/parentSourceList
    
    
    NSDictionary *dic;
    dic = @{
        @"parentLevel":model.parentLevel,
        @"parentID":model.parentID,
    };
    [VULBaseRequest requestWithUrl:@"api/disk/parentSourceList" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
           
            NSArray *arr = request.data;
            [self.tierTitleArray  removeAllObjects];
            [self.directoryArray  removeAllObjects];

            for (NSDictionary *dic in arr) {
                VULFileObjectModel *model1 = [VULFileObjectModel modelWithDictionary:dic];
                [self.tierTitleArray addObject:model1.name];
                [self.directoryArray addObject:model1.sourceID];

            }
            [self.tierTitleArray addObject:model.name.length>0?model.name:model.groupName];
            self.topView.dataArr = self.tierTitleArray;
            [self.directoryArray addObject:model.sourceID];
            if (self.tierTitleArray.count>self.directoryArray .count) {
                [self.directoryArray insertObject:@"0" atIndex:0];
            }
            if ([self.icon isEqualToString:@"shareLink"]) {
                self.icon = @"";
            }
            self.navView.moreSearchBtn.hidden = NO;
            self.tagID = @"";
            self.isFav = YES;
            self.currentPage = 1;
            [self requestList];
            if (self.operation.length>0) {
                self.changeBtn.hidden = NO;
                [self.cancel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.left.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH/2-1);
                }];
                [self.changeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.right.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH/2);
                }];
            }
        }else{
            
        }
    }];
}
#pragma mark -长按
- (void)longPress:(UILongPressGestureRecognizer *)gesture {
    if(gesture.state==UIGestureRecognizerStateBegan) {
        CGPoint point = [gesture locationInView:self.collectionView];
        NSIndexPath *indexPath = [self.collectionView indexPathForItemAtPoint:point];
        if (self.flag && [self.tgblock  isEqualToString: @"firstBox"] &&     self.tierTitleArray .count == 1){
            return;
        }
        if ([self.tgblock  isEqualToString: @"fileTag"] &&     self.tierTitleArray .count == 1){
            return;
        }

        if (!self.isEdit) {
            self.isEdit = YES;
            [self menuBlock:indexPath view:gesture.view];
        }
    }
}
- (void)menuBlock:(NSIndexPath *)indexPath view:(UIView *)view {
    // 添加弹出视图到当前视图控制器的视图层级中
//    self.addButton.hidden = YES;
    // 创建阴影层
   
    
    if (!self.isFav) {
        self.operationView.isSHow = self.isHome ||  self.isZip ;
    }else{
        self.operationView.isSHow = NO;
    }
    [kWindow addSubview:    self.operationView];

    VULFileObjectModel *model = self.dataArray[indexPath.item];

    if (self.icon && !self.isMove) {
        if (self.sourceID.length == 0 || !self.sourceID) {
            self.operationView.icon = self.icon;
        }else{
            self.operationView.icon = model.icon;
        }
    }else{
        self.operationView.icon = model.icon;
    }
    // 添加阴影层到视图中
    self.operationView.allDataArr = [NSMutableArray arrayWithArray:self.dataArray];
    model.isSelect = YES;
    [self.selectModelArray addObject:model];
    self.operationView.dataArray = [NSMutableArray arrayWithArray:self.selectModelArray];

    [UIView animateWithDuration:0.3 animations:^{
        CGRect frame =  self.operationView.frame;
        frame.origin.y = self.view.frame.size.height -  self.operationView.frame.size.height;
        self.operationView.frame = frame;
    }];
    [self.collectionView reloadData];

    
}
- (CGFloat)heightForLabel:(NSString *)text font:(UIFont *)font width:(CGFloat)width {
    CGRect rect = [text boundingRectWithSize:CGSizeMake(width, 0) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:font} context:nil];
    return rect.size.height;
}
- (CGFloat)getMaxHeight:(NSIndexPath *)indexPath Array:(NSArray *)array {
    NSMutableArray *arr = [NSMutableArray array];
    NSInteger num = indexPath.item / 4;
    for (NSInteger i = 4 * num; i < 4*num + 4; i++) {
        
        if (i>array.count-1) {
            break;
        }
        VULFileObjectModel *model = array[i];
        NSString *name = model.name;
        CGFloat height = [self heightForLabel:name font:[UIFont yk_pingFangRegular:15] width:VULSCREEN_WIDTH / 4 - 10];
        height = height > 60 ? 63 : height;
        [arr addObject:@(height)];
    }
    CGFloat maxValue = [[arr valueForKeyPath:@"@max.floatValue"] floatValue];
    return maxValue;
}
#pragma mark -
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if ([self.icon isEqualToString:@"info"]||self.infoTypeID .integerValue>0 ){
        if (self.isGrid) {
            return CGSizeMake((VULSCREEN_WIDTH-30)/ 2, fontAuto(210));//80);

        }
        return CGSizeMake(VULSCREEN_WIDTH, fontAuto(100));
    }
    
    
    if (self.isGrid) {
        VULFileObjectModel *model = [VULFileObjectModel new];
        NSMutableArray *array = [NSMutableArray array];
        model = self.dataArray[indexPath.item];
        array = self.dataArray;
        
        NSString *name = model.name;
        
       
        CGFloat height = [self heightForLabel:name font:[UIFont yk_pingFangRegular:15] width:VULSCREEN_WIDTH / 4 - 10];
        height = height > 60 ? 63 : height;
        CGFloat h = [self getMaxHeight:indexPath Array:array];
        return CGSizeMake(VULSCREEN_WIDTH / 4, 50 + h + 10);//80);
    } else
        return CGSizeMake(VULSCREEN_WIDTH, 60);
}
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    if ([self.icon isEqualToString:@"info"]||self.infoTypeID .integerValue>0 ){
        if (self.isGrid) {
            return UIEdgeInsetsMake(fontAuto(0), 10, fontAuto(0), 10);

        }
    }
    return UIEdgeInsetsMake(fontAuto(0), fontAuto(0), fontAuto(0), fontAuto(0));

}

//item 列间距(纵)
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    if ([self.icon isEqualToString:@"info"]||self.infoTypeID .integerValue>0 ){
        if (self.isGrid) {
            return 10;

        }
    }
    return 0;
}

//item 行间距(横)
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    if ([self.icon isEqualToString:@"info"]||self.infoTypeID .integerValue>0 ){
        if (self.isGrid) {
            return 10;

        }
    }
    return 0;}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return  1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.dataArray.count;
}
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    if ([self.icon isEqualToString:@"info"]||self.infoTypeID .integerValue>0 ){
        if (self.isGrid) {
            VULInfoCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"VULInfoCollectionViewCell" forIndexPath:indexPath];
            cell.model = self.dataArray[indexPath.item];
            cell.shareWithModel = ^(VULInfoModel * _Nonnull model) {
                [self shareWithModel:model];
            };
            return cell;

        }
        VULInfoListCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"VULInfoListCell" forIndexPath:indexPath];
        cell.model = self.dataArray[indexPath.item];
        cell.shareWithModel = ^(VULInfoModel * _Nonnull model) {
            [self shareWithModel:model];
        };
        return cell;
    }
    VULFileObjectModel *model = [VULFileObjectModel new];
    model = self.dataArray[indexPath.item];

    if (self.isGrid) {
        VULFileGridCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"VULFileGridCell" forIndexPath:indexPath];
        cell.model = model;
        cell.selectImageV.hidden = !self.isEdit;
        if (self.isEdit) {
            cell.selectImageV.image =  model.isSelect?VULGetImage(@"login_agree"): VULGetImage(@"login_agree_no");
        }
        UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPress:)];
        longPress.minimumPressDuration = 0.5;
        longPress.view.tag = indexPath.row + 1000;
        if (!self.isMove) {
            [cell addGestureRecognizer:longPress];
        }
        return cell;
    } else {
        VULFileListCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"VULFileListCell" forIndexPath:indexPath];
        cell.model = model;
        cell.selectImageV.hidden = !self.isEdit;
        if (self.isEdit) {
            cell.selectImageV.image =  model.isSelect?VULGetImage(@"login_agree"): VULGetImage(@"login_agree_no");
        }
        UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPress:)];
        longPress.minimumPressDuration = 0.5;
        longPress.view.tag = indexPath.row + 1000;
        if (!self.isMove) {
            [cell addGestureRecognizer:longPress];
        }
        return cell;
    }
    return nil;
}
-(void)shareWithModel:(VULInfoModel *)model{
    VULShareSpecialCardView *add = [[VULShareSpecialCardView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(300))];
     zhPopupController *popup = [[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
    add.isNews = YES;
     popup.layoutType = zhPopupLayoutTypeBottom;
     popup.presentationStyle = zhPopupSlideStyleFromBottom;
     popup.maskAlpha = 0.35;
     [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
 
  add.menuViewBtnClickedBlock = ^(NSInteger index) {
      [popup dismiss];
      NSString *string;
      if(model.infoUrl.length>0){
          string = model.infoUrl;
      }else{
          string = [NSString stringWithFormat:@"%@pubinfo/%@.shtml",ChooseUrl,model.infoID];
      }
      if (index ==0) {
          [[WXApiManger shareInstance] shareInfoWithModel:model scene:WXSceneSession];
          return;

      }
      if (index ==1) {
          [[WXApiManger shareInstance] shareInfoWithModel:model scene:WXSceneTimeline];
          return;

      }
      if (index ==2) {
          [[WXApiManger shareInstance] shareDingdingWithModel:model];
          return;

      }
      if (index ==3) {
          [[WXApiManger shareInstance] shareWeiboWithModel:model];
          return;

      }
      if (index ==4) {
          VULCreateLinkCode *linkCode = [[VULCreateLinkCode alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH * 0.9, VULSCREEN_WIDTH * 0.9)];
          zhPopupController *popup = [[zhPopupController alloc] initWithView:linkCode size:CGSizeMake(VULSCREEN_WIDTH * 0.9, linkCode.height)];
          popup.layoutType = zhPopupLayoutTypeCenter;
          popup.presentationStyle = zhPopupSlideStyleFromBottom;
          popup.maskAlpha = 0.35;
          linkCode.url =string;
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
          //
          
          NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"role"];
          NSString *roleStr = [NSString stringWithFormat:@"%@",roleDic[@"explorer.informationView"]];
          if(!roleStr.boolValue){
              [kWindow makeToast:KLanguage(@"暂无权限")];
              return;
          }
          BaseWebViewController *webVc = [[BaseWebViewController alloc] init];
          webVc.model = model;
          webVc.nvaTitle = model.title;
          if(model.infoUrl.length>0){
              webVc.currentURL = model.infoUrl;
          }else{
              webVc.currentURL = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml?isApp=1",ChooseUrl,model.infoID];
          }
          [self.navigationController pushViewController:webVc animated:YES];

          return;

      }
      if (index ==7) {
          NSString *conl =model.title ;
          NSString *string ;
          if(model.infoUrl.length>0){
              string = model.infoUrl;
          }else{
              string = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml",ChooseUrl,model.infoID];
          }
          NSURL *shareURL = [NSURL URLWithString:string];
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


}
- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath {

}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    
    if ([self.icon isEqualToString:@"info"]||self.infoTypeID .integerValue>0 ){
        //
        
        NSDictionary *roleDic = [[NSUserDefaults standardUserDefaults] objectForKey:@"role"];
        NSString *roleStr = [NSString stringWithFormat:@"%@",roleDic[@"explorer.informationView"]];
        if(!roleStr.boolValue){
            [kWindow makeToast:KLanguage(@"暂无权限")];
            return;
        }
        VULInfoModel *model = [VULInfoModel new];
        model = self.dataArray[indexPath.item];
        
        BaseWebViewController *webVc = [[BaseWebViewController alloc] init];
        webVc.model = model;
        webVc.nvaTitle = model.title;
        if(model.infoUrl.length>0){
            webVc.currentURL = model.infoUrl;
        }else{
            webVc.currentURL = [NSString stringWithFormat:@"%@/pubinfo/%@.shtml?isApp=1",ChooseUrl,model.infoID];
        }
        [self.navigationController pushViewController:webVc animated:YES];
        return;
    }
        VULFileObjectModel *model = [VULFileObjectModel new];
    
        model = self.dataArray[indexPath.item];
//    选择图片
        if(self.isSelect){
            if(self.selectModel){
                self.selectModel(model);
            }
            [self dismissViewControllerAnimated:YES completion:nil];
            return;
        }
    
//    手动加我的分享
    if([model.icon isEqualToString:@"shareLink"]){
        [self shareLinkWithModel:model];


        return;
    }
    
    
        if (self.isEdit) {
            if (model.isSelect) {
                model.isSelect = NO;
                [self.selectModelArray removeObject:model];
            }else{
                model.isSelect = YES;
                [self.selectModelArray addObject:model];
            }
            self.operationView.dataArray = [NSMutableArray arrayWithArray:self.selectModelArray];
            [self.collectionView reloadData];
            return;
        }
        if ([self.icon isEqualToString:@"recycle"] && [model.icon isEqualToString:@"folder"]) {
            [self makeToast:KLanguage(@"该文档在回收站中，请还原后再试！") ];
            return;
        }
        if (model.labelId.integerValue>0) {
            self.tagID = model.labelId;
            self.icon = model.icon;
            [self.directoryArray removeAllObjects];
            [self.tierTitleArray removeAllObjects];
            self.fitstSourceID = model.sourceID;
            self.sourceID = model.sourceID;
            self.parentID = model.parentID;
            self.currentPage = 1;
            [self.directoryArray removeAllObjects];
            self.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(@"标签"),model.name.length>0?model.name:model.groupName]] ;
    //            [weakSelf.directoryArray addObject:weakSelf.fitstSourceID];
    //            weakSelf.sourceID = model.sourceID;
            self.topView.dataArr =     self.tierTitleArray;

            [self requestList];
            return;
        }
        if (!model.isFolder.boolValue  && !model.oexeIsFolder.boolValue) { //是文件
            [self openFileWithModel:model withIndexPath:indexPath];
        } else {
        
           self.parentModel = model;
            if (isPermissionWithModel(KLanguage(@"上传"), @[model])||isPermissionWithModel(KLanguage(@"文件夹"), @[model])) {
                self.addButton.hidden = NO;
            }else{
                self.addButton.hidden = YES;
            }
            
            if(model.oexeContent.length>0){
                NSDictionary *dic = turnStringToDictionary(model.oexeContent);
                NSString *type =  dic[@"type"];
                NSString *sourceID = [NSString stringWithFormat:@"%@",dic[@"sourceID"]];
                
                if([model.fileType isEqualToString:@"oexe"] && [type isEqualToString:@"lnk"]){
                    model.sourceID = sourceID;
                    NSArray *arr = [model.name componentsSeparatedByString:@"."];
                    model.name =  arr[0];
                }
            }
            self.collectionView.contentOffset = CGPointMake(0, 0);
            self.sourceID = model.sourceID;
            self.sourceLevel = model.parentLevel;
            self.parentID = model.parentID;
            self.isFav = NO;
            if (([self.tgblock isEqualToString:@"shareLink"] || [self.tgblock isEqualToString:@"fav"] ||   self.tagID.integerValue>0)&&model.parentID.integerValue>0) {
                [self getParentSourceListWtihModel:model];
                return;
            }
//            共享-我的分享点击文件夹
            if([self.icon isEqualToString:@"shareLink"]){
                self.icon = @"";
            }
            
            [self.tierTitleArray addObject:model.name.length>0?model.name:model.groupName];
            self.topView.dataArr = self.tierTitleArray;
            [self.directoryArray addObject:model.sourceID];
            if (self.tierTitleArray.count>self.directoryArray .count) {
                [self.directoryArray insertObject:@"0" atIndex:0];
            }
            
            self.currentPage = 1;
            [self requestList];
            if (self.operation.length>0) {
                self.changeBtn.hidden = NO;
                [self.cancel mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.left.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH/2-1);
                }];
                [self.changeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.mas_equalTo(0);
                    make.right.mas_offset(0);
                    make.height.mas_equalTo(fontAuto(50));
                    make.width.mas_equalTo(VULSCREEN_WIDTH/2);
                }];
            }
        }
    
}
//需求换菜 手动添加我的分享
-(void)shareLinkWithModel:(VULFileObjectModel *)model{
    MJWeakSelf

    self.addButton.hidden = NO;
        self.isFav = NO;
    [self.searchDic removeAllObjects];
    _searchView = nil;
    self.isZip = NO;

    self.keyWord = @"";
    self.infoTypeID = @"";
    self.navView.searchField.text = @"";
    self.infoTypeID = model.infoTypeID;
    if (model.sourceID &&model.sourceID.length>0) {
        self.navView.moreSearchBtn.hidden = NO;
        if (isPermissionWithModel(KLanguage(@"上传"), @[model]) || isPermissionWithModel(KLanguage(@"文件夹"), @[model]) ) {
            self.addButton.hidden = NO;

        }else{
            self.addButton.hidden = YES;
            self.isUpload = YES;

        }
        if (self.operation.length>0) {
            self.changeBtn.hidden = NO;
            [self.cancel mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.bottom.mas_equalTo(0);
                make.left.mas_offset(0);
                make.height.mas_equalTo(fontAuto(50));
                make.width.mas_equalTo(VULSCREEN_WIDTH/2-1);
            }];
            [self.changeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.bottom.mas_equalTo(0);
                make.right.mas_offset(0);
                make.height.mas_equalTo(fontAuto(50));
                make.width.mas_equalTo(VULSCREEN_WIDTH/2);
            }];
        }
        weakSelf.icon = model.icon;
        weakSelf.tgblock = model.icon;
        [weakSelf.directoryArray removeAllObjects];
        [weakSelf.tierTitleArray removeAllObjects];
        weakSelf.parentID = model.parentID;

        weakSelf.currentPage = 1;
        [weakSelf.directoryArray removeAllObjects];
      
        if (model.titleArr.count>0) {
            weakSelf.fitstSourceID = model.sourceIDArr[0];
        
            weakSelf.tierTitleArray = [NSMutableArray arrayWithArray:model.titleArr] ;
            [weakSelf.tierTitleArray addObject:KLanguage(model.name)];
            weakSelf.directoryArray= [NSMutableArray arrayWithArray:model.sourceIDArr];
            [weakSelf.directoryArray  addObject:model.sourceID];
        }else{
            weakSelf.fitstSourceID = model.sourceID;
            weakSelf.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(model.name.length>0?model.name:model.groupName)]] ;
            weakSelf.directoryArray = [NSMutableArray array];
            [weakSelf.directoryArray addObject:weakSelf.fitstSourceID];
        }
        if (weakSelf.flag) {
            [weakSelf.tierTitleArray insertObject:KLanguage(@"共享") atIndex:0];
            [weakSelf.directoryArray insertObject:@"firstBox" atIndex:0];
            weakSelf.tgblock = @"firstBox";
        }
        weakSelf.sourceID = model.sourceID;
        weakSelf.topView.dataArr =     weakSelf.tierTitleArray;
        [weakSelf requestList];
    }else{
        self.navView.moreSearchBtn.hidden = YES;
        self.addButton.hidden = YES;
        self.isUpload = YES;
        self.isZip = YES;
        self.infoTypeID = model.infoTypeID;
        if (self.operation.length>0) {
            self.changeBtn.hidden = YES;
            [self.cancel mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.bottom.mas_equalTo(0);
                make.left.mas_offset(0);
                make.height.mas_equalTo(fontAuto(50));
                make.width.mas_equalTo(VULSCREEN_WIDTH);
            }];
            [self.changeBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
                make.bottom.mas_equalTo(0);
                make.right.mas_offset(0);
                make.height.mas_equalTo(fontAuto(50));
                make.width.mas_equalTo(0);
            }];
        }
        if (model.ext.length>0) {
            weakSelf.fileType = model.ext;
        }
        if (model.labelId.length>0) {
            weakSelf.tagID = model.labelId;
        }
//            垃圾接口 有block不用 非要我们本地判断
        if ([model.icon  isEqualToString:@"recentDoc"]) {
            weakSelf.icon = @"userRencent";
        } else if([self.icon  isEqualToString:@"other"]){
            weakSelf.icon = @"";
        }else{
            if ([model.icon  isEqualToString:@"photo"]) {
                weakSelf.icon = @"photo";
                weakSelf.fileType = @"jpg,jpeg,png,gif,bmp,ico,svg,webp,tif,tiff,cdr,svgz,xbm,eps,pjepg,heic,raw,psd,ai";
            }else{
                weakSelf.icon = model.icon;

            }

        }
        weakSelf.tgblock = weakSelf.icon;
        [weakSelf.directoryArray removeAllObjects];
        [weakSelf.tierTitleArray removeAllObjects];
        weakSelf.fitstSourceID = model.sourceID;
        weakSelf.sourceID = model.sourceID;
        weakSelf.parentID = model.parentID;
        weakSelf.currentPage = 1;
        [weakSelf.directoryArray removeAllObjects];
        weakSelf.fitstSourceID = model.sourceID;
        weakSelf.tierTitleArray = [NSMutableArray arrayWithArray:@[KLanguage(model.name.length>0?model.name:model.groupName)]] ;
        weakSelf.directoryArray = [NSMutableArray array];
        [weakSelf.directoryArray addObject:weakSelf.icon];
        if (weakSelf.flag) {
            [weakSelf.tierTitleArray insertObject:KLanguage(@"共享") atIndex:0];
            [weakSelf.directoryArray insertObject:@"firstBox" atIndex:0];
            weakSelf.tgblock = @"firstBox";
        }

        weakSelf.topView.dataArr =     weakSelf.tierTitleArray;

        [weakSelf requestList];
//        weakSelf.icon = @"";
    }
  
}


#pragma mark -打开文件
-(void)openFileWithModel:(VULFileObjectModel *)model withIndexPath:(NSIndexPath *)indexPath{

    if (!isPermissionWithModel(KLanguage(@"查看"),@[model])) {
        [kWindow makeToast:KLanguage(@"暂无权限")];
        return;
    }
//    "tar", "zip", "gzip", "bz2", "rar", "7z", "gz", "iso", "ar", "bz", "xz", "arj"

    if(model.oexeContent.length>0){
        NSDictionary *dic = turnStringToDictionary(model.oexeContent);
        NSString *type =  dic[@"type"];
        NSString *fileType =  dic[@"fileType"];
        NSString *sourceID =  dic[@"sourceID"];

        if([model.fileType isEqualToString:@"oexe"] && [type isEqualToString:@"lnk"]){
            model.sourceID = sourceID;
            model.fileType = fileType;

            
        }
    }
    
    
    NSArray *tarArr = @[@"tar",@"zip",@"gzip",@"bz2",@"rar",@"7z",@"gz",@"iso",@"ar",@"bz",@"xz",@"arj"];

    if ([tarArr containsObject:model.fileType] &&!model.id) {
        FBZIPLookViewController *vc = [FBZIPLookViewController new];
        vc.flag = self.flag;
        vc.model = model;
        vc.saveAndRefreshBlock = ^{
            self.currentPage =1;
            [self requestList];
        };
        MJWeakSelf
        vc.selectOperationWithTitle = ^(NSString * _Nonnull title, VULFileObjectModel * _Nonnull model) {
            self.previewModel = nil;
            self.previewModel = model;
            
            if ([title isEqualToString:KLanguage(@"属性")]) {
                FBFileAddrituteRootVC *vc = [FBFileAddrituteRootVC new];
                vc.model = model;
                vc.saveAndRefreshBlock = ^{
//                    weakSelf.sourceID =   weakSelf.fitstSourceID;
                    self.currentPage =1;
                    [self requestList];
                };
                vc.openDetialWithModel = ^(VULFileObjectModel * _Nonnull model) {
                    [self openFileWithModel:model withIndexPath:nil];
                };
                vc.gotoTagManageMent = ^{
                    FBFileTagManagementVC *vc =[FBFileTagManagementVC new];
                    [self.navigationController pushViewController:vc animated:YES];
                };
                [self presentViewController:vc animated:YES completion:nil];
                return;
            }
            if ([title isEqualToString:KLanguage(@"解压到当前")]) {
       
                [self unZIPFileWithType:@"current"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"解压到文件夹")]) {
                [self unZIPFileWithType:@"folder"];
                return;
            }
    
            if ([title isEqualToString:KLanguage(@"解压到...")]) {
                FBHomeViewController *vc = [FBHomeViewController new];
                vc.isMove = YES;
                vc.flag = self.flag;
                vc.icon = @"ZIP";
                vc.operation = @"ZIP";
                
                vc.changeArray = [NSMutableArray arrayWithArray:@[model]];
                vc.saveAndRefreshBlock = ^{
//                    weakSelf.sourceID =   weakSelf.fitstSourceID;
                    self.currentPage =1;
                    [self requestList];
                };
                [self presentViewController:vc animated:YES completion:nil];
                return;

            }
        };
        [self presentViewController:vc animated:YES completion:nil];
        return;
    }
    NSArray *videoArray = @[@"mp4",@"rm",@"rmvb",@"3gp",@"mov",@"m4v",@"wmv",@"asf",@"asx",@"avi",@"dat",@"mkv",@"flv",@"vob",@"webm",@"mpg"];
    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd",@"webp"];

    NSArray *mp3Array = @[@"mp3",@"wav",@"wma",@"m4a",@"ogg",@"omf",@"amr",@"aa3",@"flac",@"aac",@"cda",@"aif",@"aiff",@"mid",@"ra",@"ape"];

//    fileType = @"mp3,wav,wma,m4a,ogg,omf,amr,aa3,flac,aac,cda,aif,aiff,mid,ra,ape";

//    NSArray *preArray = @[@"swf",@"xls",@"xlsx",@"doc",@"pdf",@"docx",@"ppt",@"pptx",@"pps"];

    if ([picArray containsObject:model.fileType]) {
        XLPhotoBrowser *photoBrowser = [XLPhotoBrowser showPhotoBrowserWithImages:[self getImageUrlArr] currentImageIndex:[self backIndexWithModel:model]];
        photoBrowser.browserStyle = XLPhotoBrowserStyleIndexLabel;
        photoBrowser.eidtWithImage = ^(UIImage *image, NSInteger idn) {
            self.upNewModel = nil;
            [photoBrowser dismiss];
            self.upNewModel = model;
            VULFileObjectModel *model = self.imageArray[idn];
            if (isPermissionWithModel(KLanguage(@"编辑"), @[model])) {
                PSImageEditor *imageEditor = [[PSImageEditor alloc] initWithImage:image delegate:self dataSource:self];
                [self.navigationController pushViewController:imageEditor animated:YES];
            }else{
                [[UIApplication sharedApplication].keyWindow makeToast: KLanguage(@"暂无权限") duration:1 position:@"center"];

            }
           
        };
     
        // 微博样式
//        [XLPhotoBrowser showPhotoBrowserWithImages:[self getImageUrlArr] currentImageIndex:[self backIndexWithModel:model]];

        return;

    }else if([mp3Array containsObject:model.fileType] && model.h264Path.length>0){
        NSString *urlString = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.h264Path];
        urlString = [urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
         VULPlayMp3VC *vc = [[VULPlayMp3VC alloc] init];
         vc.title = model.name;
         vc.mp3Url = urlString;
         vc.playLength = [NSNumber numberWithInteger:model.length.integerValue];
         [self.navigationController pushViewController:vc animated:YES];
//
//            NSDictionary *dic;
//            dic = @{
//                @"busType":@"cloud",
//                @"path":@"info",
//                @"sourceID":model.sourceID
//            };
//            [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
//                if (request.success) {
//                    NSString *downloadUrl = request.data[@"downloadUrl"];
//                    NSString *length = [NSString stringWithFormat:@"%@",request.data[@"length"]] ;
//                    NSString *urlString = [NSString stringWithFormat:@"%@%@",ChooseUrl,downloadUrl];
//                    urlString = [urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
//                     VULPlayMp3VC *vc = [[VULPlayMp3VC alloc] init];
//                     vc.title = model.name;
//                     vc.mp3Url = urlString;
//                     vc.playLength = [NSNumber numberWithInteger:length.integerValue];
//                     [self.navigationController pushViewController:vc animated:YES];
//                }else{
//                    
//                }
//            }];
          
          
            return;
        
    }else if([mp3Array containsObject:model.fileType]){
                    NSDictionary *dic;
                    dic = @{
                        @"busType":@"cloud",
                        @"path":@"info",
                        @"sourceID":model.sourceID
                    };
        if(model.id &&model.id.intValue>0){
            dic = @{
                @"busType":@"cloud",
                @"path":@"info",
                @"sourceID":model.sourceID,
                @"f":model.id
            };
        }
                    [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
                        if (request.success) {
                            NSString *downloadUrl = request.data[@"downloadUrl"];
                            NSString *urlString = [NSString stringWithFormat:@"%@%@",ChooseUrl,downloadUrl];
                            urlString = [urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                             VULPlayMp3VC *vc = [[VULPlayMp3VC alloc] init];
                             vc.title = model.name;
                             vc.mp3Url = urlString;
                             vc.playLength = [NSNumber numberWithInteger:model.length.integerValue];
                             [self.navigationController pushViewController:vc animated:YES];
                        }else{
        
                        }
                    }];
    }
    else if([videoArray containsObject:model.fileType]){
        

    
//
        [VULAllFileModel getPlayVideoUrlFromModel:model sucess:^(NSString * _Nonnull pathUrl) {
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,pathUrl];
            [kWindow addSubview:self.containerView];
            [self.containerView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.mas_equalTo(0);
            }];
            ZFAVPlayerManager *manager = [[ZFAVPlayerManager alloc] init];

            self.player = [[ZFPlayerController alloc] initWithPlayerManager:manager containerView:self.containerView];
            self.player.controlView = self.controlView;
            self.player.shouldAutoPlay = YES;
            self.player.assetURL = [NSURL URLWithString:url];
//            if(@available(iOS 16.0, *)){
//                self.player.allowOrentitaionRotation = NO;
//            }else{
////                [self.player enterFullScreen:YES animated:YES];
//            }
//                [self.player enterFullScreen:YES animated:YES];

            WeakSelf(self);
            self.controlView.backBtnClickCallback = ^{
                [weakself.player stop];
                [weakself.player enterFullScreen:NO animated:true];
                [weakself.containerView removeFromSuperview];
                weakself.containerView = nil;
               
            };
           
            self.player.playerDidToEnd = ^(id _Nonnull asset) {
                [weakself.player stop];
//                [weakself.player enterFullScreen:NO animated:true];
                [weakself.containerView removeFromSuperview];
                weakself.containerView = nil;
            };
            self.player.orientationDidChanged = ^(ZFPlayerController * _Nonnull player, BOOL isFullScreen) {
                   // 使用YYTextView转屏失败
                   for (UIWindow *window in [UIApplication sharedApplication].windows) {
                       if ([window isKindOfClass:NSClassFromString(@"YYTextEffectWindow")]) {
                           window.hidden = isFullScreen;
                       }
                   }
               };
        }];
      
       
    }
//    else if([preArray containsObject:model.fileType]){
//        [VULAllFileModel getPlayVideoUrlFromModel:model sucess:^(NSString * _Nonnull pathUrl) {
//        VULDocumentVC *vc = [[VULDocumentVC alloc] init];
//        vc.title = model.name;
//        vc.pptPreviewUrl = pathUrl;
//            [self.navigationController pushViewController:vc animated:YES];
//
//        }];
//    }
    else{
        
//        'rar', 'zip', '7z', 'tar', 'tar.gz', 'gz'

        NSArray   *docMap =@[@"ppt",@"pptx",@"pdf",@"eid",@"docx",@"doc",@"dot",@"rtf",@"txt",@"uot",@"htm",@"wps",@"wpt",@"eis",@"xlsx",@"xltx",@"xls",@"xlt",@"uos",@"dbf",@"csv",@"xml",@"et",@"ett",@"pptx",@"potx",@"ppt",@"pot",@"ppsx",@"pps",@"dps",@"dpt",@"uop",@"zip",@"tar",@"rar",@"7z",@"tar.gz",@"gz"];
        if ([docMap containsObject:model.fileType]) {
            NSDictionary *dic;
            dic = @{
                @"busType":@"cloud",
                @"path":@"info",
                @"sourceID":model.sourceID
            };
            if(model.id &&model.id.intValue>0){
                dic = @{
                    @"busType":@"cloud",
                    @"path":@"info",
                    @"sourceID":model.sourceID,
                    @"f":model.id
                };
            }
            [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
                if (request.success) {
                    NSString *yzViewData = request.data[@"yzViewData"];
                    NSString *pptPreviewUrl;
                    if (yzViewData.length>0 && yzViewData) {
                        NSDictionary *dicData = turnStringToDictionary(yzViewData);
                        pptPreviewUrl = dicData[@"viewUrl"];
                    }else{
                        pptPreviewUrl = request.data[@"pptPreviewUrl"];

                    }
                  
                    pptPreviewUrl = [pptPreviewUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                    VULDocumentVC *vc = [[VULDocumentVC alloc] init];
                    vc.title = model.name;
                    vc.pptPreviewUrl = pptPreviewUrl;
                   [self.navigationController pushViewController:vc animated:YES];

                }else{
                    
                }
            }];
        }else if([model.fileType isEqualToString:@"svg"] ) {
            NSDictionary *dic;
            dic = @{
                @"busType":@"cloud",
                @"path":@"info",
                @"sourceID":model.sourceID
            };
            if(model.id &&model.id.intValue>0){
                dic = @{
                    @"busType":@"cloud",
                    @"path":@"info",
                    @"sourceID":model.sourceID,
                    @"f":model.id
                };
            }
            [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
                if (request.success) {
                    NSString *pptPreviewUrl = request.data[@"downloadUrl"];;
                    NSString *urlString = [NSString stringWithFormat:@"%@%@",ChooseUrl,pptPreviewUrl];

                    urlString = [urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                    VULDocumentVC *vc = [[VULDocumentVC alloc] init];
                    vc.title = model.name;
                    vc.pptPreviewUrl = urlString;
                   [self.navigationController pushViewController:vc animated:YES];

                }else{
                    
                }
            }];
        }else if([model.fileType isEqualToString:@"html"] ) {
            NSDictionary *dic;
            dic = @{
                @"busType":@"cloud",
                @"path":@"info",
                @"sourceID":model.sourceID
            };
           
            [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
                if (request.success) {
                    NSString *text = request.data[@"text"];
                    BasePreviewHtmlVC *vc = [[BasePreviewHtmlVC alloc] init];
                    vc.nvaTitle = model.name;
                    vc.currentURL = text;
                   [self.navigationController pushViewController:vc animated:YES];

                }else{
                    
                }
            }];
        }else if([model.fileType isEqualToString:@"srt"] ) {
            NSDictionary *dic;
            dic = @{
                @"busType":@"cloud",
                @"path":@"info",
                @"sourceID":model.sourceID
            };
           
            [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
                if (request.success) {
                    NSString *text = request.data[@"text"];
                    BasePreviewTextVC *vc = [[BasePreviewTextVC alloc] init];
                    vc.nvaTitle = model.name;
                    vc.text = text;
                   [self.navigationController pushViewController:vc animated:YES];

                }else{
                    
                }
            }];
        }else if([model.fileType isEqualToString:@"xmind"] ) {

            NSString *encodedString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
                kCFAllocatorDefault,
                (CFStringRef)model.downloadUrl,
                NULL,
                CFSTR(":/?#[]@!$&'()*+,;="),
                kCFStringEncodingUTF8
            ));

            NSString *urlString = [NSString stringWithFormat:@"%@xmindviewer.html?xmindUrl=%@",ChooseUrl,encodedString];

//            urlString = [urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            VULBaseWebViewVC *vc = [[VULBaseWebViewVC alloc] init];
            vc.nvaTitle = model.name;
            vc.currentURL = urlString;
           [self.navigationController pushViewController:vc animated:YES];
        } else if([model.fileType isEqualToString:@"oexe"]){
            NSDictionary *dicData = turnStringToDictionary(model.oexeContent);
            NSString *pptPreviewUrl = dicData[@"value"];

            pptPreviewUrl = [pptPreviewUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            VULDocumentVC *vc = [[VULDocumentVC alloc] init];
            vc.title = model.name;
            vc.pptPreviewUrl = pptPreviewUrl;
           [self.navigationController pushViewController:vc animated:YES];
        }else if([model.fileType isEqualToString:@"smm"] || [model.fileType isEqualToString:@"km"] || [model.fileType isEqualToString:@"mind"]){
            
            NSString *encodedString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
                kCFAllocatorDefault,
                (CFStringRef)model.downloadUrl,
                NULL,
                CFSTR(":/?#[]@!$&'()*+,;="),
                kCFStringEncodingUTF8
            ));

            NSString *urlString = [NSString stringWithFormat:@"%@/newmindMap/index.html?mindUrl=%@&sourceID=%@&action=&fileType=%@",ChooseUrl,encodedString,model.sourceID,model.fileType];
            VULBaseWebViewVC *vc = [[VULBaseWebViewVC alloc] init];
            vc.nvaTitle = model.name;
            vc.currentURL = urlString;
           [self.navigationController pushViewController:vc animated:YES];
         
            
        }else if([model.fileType isEqualToString:@"epub"] ){
            
            NSString *encodedString = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(
                kCFAllocatorDefault,
                (CFStringRef)model.downloadUrl,
                NULL,
                CFSTR(":/?#[]@!$&'()*+,;="),
                kCFStringEncodingUTF8
            ));

            NSString *urlString = [NSString stringWithFormat:@"%@reader/index.html?fileUrl=%@",ChooseUrl,encodedString];
            VULBaseWebViewVC *vc = [[VULBaseWebViewVC alloc] init];
            vc.nvaTitle = model.name;
            vc.currentURL = urlString;
           [self.navigationController pushViewController:vc animated:YES];
         
            
        }else if([model.fileType isEqualToString:@"md"]){
            NSDictionary *dic;
            dic = @{
                @"busType":@"cloud",
                @"path":@"info",
                @"sourceID":model.sourceID
            };
            if(model.id &&model.id.intValue>0){
                dic = @{
                    @"busType":@"cloud",
                    @"path":@"info",
                    @"sourceID":model.sourceID,
                    @"f":model.id
                };
            }
            [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
                if (request.success) {
                    NSString *pptPreviewUrl = request.data[@"text"];;
                    pptPreviewUrl = [pptPreviewUrl stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                    FBPreviewURLVC *vc = [[FBPreviewURLVC alloc] init];
                    vc.title = model.name;
                    vc.text =pptPreviewUrl;
                   [self.navigationController pushViewController:vc animated:YES];

                }else{
                    
                }
            }];
         
            
    }else if([model.fileType isEqualToString:@"drawio"] &&!model.id){
            NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
            NSString *tlocale = @"zh";
            if ([appLanguage isEqualToString:@"en"]) {
                tlocale = @"en";
            }
            NSString *token = [VULRealmDBManager getLocalToken];
            NSString *baseurl = ChooseUrl;

            NSString *urlString = [NSString stringWithFormat:@"%@plugin/draw/?lang=%@&lightbox=1&offline=1&sourceID=%@&token=%@&domain=%@",ChooseUrl,tlocale,model.sourceID,token,[baseurl substringToIndex:(baseurl.length-1)]];
            VULDocumentVC *vc = [[VULDocumentVC alloc] init];
            vc.title = model.name;
            vc.pptPreviewUrl = urlString;
           [self.navigationController pushViewController:vc animated:YES];
            
        }else if([model.fileType isEqualToString:@"vsdx"] &&!model.id){
            NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
            NSString *tlocale = @"zh";
            if ([appLanguage isEqualToString:@"en"]) {
                tlocale = @"en";
            }
            NSString *token = [VULRealmDBManager getLocalToken];
            NSString *baseurl = ChooseUrl;

            NSString *urlString = [NSString stringWithFormat:@"%@plugin/draw/?lang=%@&lightbox=1&offline=1&sourceID=%@&token=%@&domain=%@",ChooseUrl,tlocale,model.sourceID,token,[baseurl substringToIndex:(baseurl.length-1)]];
            VULDocumentVC *vc = [[VULDocumentVC alloc] init];
            vc.title = model.name;
            vc.pptPreviewUrl = urlString;
           [self.navigationController pushViewController:vc animated:YES];
            
        }else if([model.fileType isEqualToString:@"dcm"]){
            NSString *token = [VULRealmDBManager getLocalToken];
            NSString *urlString = [NSString stringWithFormat:@"%@#/filesPreview?sourceID=%@&token=%@",ChooseUrl,model.sourceID,token];
            VULBaseWebViewVC *webVc = [[VULBaseWebViewVC alloc] init];
            webVc.nvaTitle =  model.name;
            webVc.currentURL = urlString;
            [self.navigationController pushViewController:webVc animated:YES];

        }else{
            [[UIApplication sharedApplication].keyWindow makeToast: KLanguage(@"无法预览当前文件") duration:1 position:@"center"];

        }
        
    }
}

#pragma mark - PSImageEditorDelegate

- (void)imageEditor:(PSImageEditor *)editor didFinishEdittingWithImage:(UIImage *)image {
//    self.imageView.image = image;
    [editor dismiss];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd-HH:mm:ss"];
    NSString *key = [[formatter stringFromDate:[NSDate date]] stringByAppendingString: [NSString stringWithFormat:@"%ld.jpg",index]];
    
    // 解决图片偏转90度的问题
    if(image.imageOrientation != UIImageOrientationUp) {
        UIGraphicsBeginImageContext(image.size);
        [image drawInRect:CGRectMake(0, 0, image.size.width, image.size.height)];
        image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
    }
    [[SDImageCache sharedImageCache] storeImage:image forKey:key toDisk:YES completion:^{
        NSString *filePath = [[SDImageCache sharedImageCache] cachePathForKey:key];
        if (self.upNewModel) {
            
            [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:filePath] sourceID:self.upNewModel.parentID pathId:  self.upNewModel.sourceID isPic:YES success:^(bool sucess) {
                
                if (sucess) {
                    self.currentPage =1;
                    [self requestList];
                    
                }
                
            }];
            
        }}];
    NSLog(@"%s",__func__);
}

- (void)imageEditorDidCancel {
    NSLog(@"%s",__func__);
}
#pragma mark - PSImageEditorDelegate

- (UIColor *)imageEditorDefaultColor {
    return [UIColor redColor];
}

- (PSImageEditorMode)imageEditorDefalutEditorMode {
   return PSImageEditorModeDraw;
}

- (CGFloat)imageEditorDrawPathWidth {
  return 5;
}

- (UIFont *)imageEditorTextFont {
 return [UIFont boldSystemFontOfSize:24];
}
//获取图片下标
-(NSInteger )backIndexWithModel:(VULFileObjectModel *)model{
    NSInteger row = 0;
    for (int i = 0;i<self.imageArray.count ;i++) {
        VULFileObjectModel *infoModel = self.imageArray[i];
        if (model.fileID.intValue == infoModel.fileID.intValue) {
            row = i;
            break;
        }
    }
    return row;
}
//获取所有图片
-(NSMutableArray *)getImageUrlArr{
    NSMutableArray *arr = [NSMutableArray array];
    for (int i = 0;i<self.imageArray.count ;i++) {
        VULFileObjectModel *model = self.imageArray[i];
        if ([model.fileType isEqualToString:@"gif"] ) {
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,model.downloadUrl];
            url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            [arr addObject:url];
        }else{
            
            NSString *url = [NSString stringWithFormat:@"%@%@",ChooseUrl,fileImageWithUrl(model.path,0,model.fileType)];
//            url = [url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            [arr addObject:url];
        }
    }
    return arr;
}
#pragma mark -重命名

- (void)reName {
    VULFileObjectModel *model = self.selectModelArray[0];
    VULInputTitleView *view = [[VULInputTitleView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) title:KLanguage(@"重命名")];
    if (!model.isFolder.boolValue) { //是文件
        view.textField.placeholder = KLanguage(@"请输入文件名称");
        if ([model.name hasSuffix:model.fileType]) {
            view.textField.text = [model.name substringToIndex:(model.name.length - model.fileType.length - 1)];
        } else
            view.textField.text = model.name;
    } else{
        view.textField.placeholder = KLanguage(@"请输入文件夹名称");
        view.textField.text = model.name;
    }

    view.alertInputViewBlock = ^(NSString * _Nonnull text) {
        if (!model.isFolder.boolValue) {  //是文件
            if (!NSStringIsNotEmpty(text)) {
                [[UIApplication sharedApplication].keyWindow makeCenterToast:KLanguage(@"文件名不能为空")];
                return;
            }
            [view hiddenView];
            NSString *string = [NSString stringWithFormat:@"%@.%@",text,model.fileType];
            [self showWaitHudWithString:nil];

            [VULAllFileModel reNameFile:model name:string type:@"file" completion:^(__kindof VULBaseRequest * _Nonnull request) {
                [self dissmissHudView];
                if (request.success) {
                    NSLog(@"renameFile-----%@",request.data);
                    model.name = string;
                    [self dissmissOperationView];
                    [self.collectionView reloadData];
                    
                } else
                    [self makeToast:request.message];
                    
            }];
        } else {
            if (!NSStringIsNotEmpty(text)) {
                [[UIApplication sharedApplication].keyWindow makeCenterToast:KLanguage(@"文件夹不能为空")];
                return;
            }
            [view hiddenView];
            [self showWaitHudWithString:nil];
            [VULAllFileModel reNameFile:model name:text type:@"folder" completion:^(__kindof VULBaseRequest * _Nonnull request) {
                [self dissmissHudView];
                if (request.success) {
                    NSLog(@"renameFile-----%@",request.data);
                    model.name = text;
                    [self dissmissOperationView];
                    [self.collectionView reloadData];

                } else
                    [self makeToast:request.message];
                    
            }];
        }
    };
    [view showInView];
}
#pragma mark -删除和还原批量操作

-(void)operationFileWithType:(NSString *)operationType{

    [self showWaitHudWithString:nil];

    [VULAllFileModel  operationFile:self.selectModelArray operationType:operationType completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        if (request.success) {
            [self dissmissOperationView];
            self.currentPage =1;
            [self requestList];
            [VULNotificationCenter postNotificationName:@"getMessageChangeNotificationCenter" object:nil];

        } else
            [self makeToast:request.message];
    }];
}

- (NSString *)jsonStringFromObject:(id)jsonObject { // 一般为Map
    if ([NSJSONSerialization isValidJSONObject:jsonObject]) {
        NSError *error;
        NSData *jsonData = [NSJSONSerialization dataWithJSONObject:jsonObject options:0 error:&error];
        NSString *json = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        return json;
    }
    return nil;
}
-(void)mkfileFileWithType:(NSString *)operationType isDesktop:(BOOL)flag{
    [self showWaitHudWithString:nil];
    
//    flag  Yes是桌面快捷方式 no 快捷方式
    VULFileObjectModel *model = self.selectModelArray[0];
    NSDictionary *content  = @{@"type":@"lnk",@"sourceID":model.sourceID,@"isFolder":@(model.isFolder.intValue),@"fileType":model.fileType,@"name":model.name,@"fileID":model.fileID,@"icon":model.icon};
//     flag 取桌面的sourceID
    NSString *sourceID =flag? [[NSUserDefaults standardUserDefaults]objectForKey:@"sourceID"]:model.parentID;

    NSDictionary *dic = @{@"name":[NSString stringWithFormat:@"%@.oexe",model.name],@"sourceID":sourceID,@"operation":operationType,@"fileType":model.fileType,@"isFolder":@(model.isFolder.intValue),@"content":[self jsonStringFromObject:content]};
    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self dissmissHudView];
        if (request.success) {
            [self dissmissOperationView];
            self.currentPage =1;
            [self requestList];

        } else
            [self makeToast:request.message];
    }];
}
-(void)collectFileWithType:(NSString *)operationType{

    [self showWaitHudWithString:nil];
    VULFileObjectModel *model = self.selectModelArray[0];
    NSDictionary *dic = @{@"name":model.name,@"sourceID":model.sourceID,@"operation":operationType};

    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self dissmissHudView];
        if (request.success) {
            [self dissmissOperationView];
            self.currentPage =1;
            [self requestList];

        } else
            [self makeToast:request.message];
    }];
}
-(void)ZIPFileWithType:(NSString *)operationType{
    
    [self showWaitHudWithProgressString:@"0" title:KLanguage(@"正在压缩...")];
    NSMutableArray *dicArr = [NSMutableArray array];
    for (VULFileObjectModel *model  in self.selectModelArray ) {
        [dicArr addObject:@{ @"name":model.name,
                             @"parentID":model.parentID,
                             @"parentLevel":model.parentLevel,
                             @"sourceID":model.sourceID,
                             @"type":model.icon}];
    }
    NSDictionary *dic;
    NSInteger count = 0;
    NSString *name;
    VULFileObjectModel *model = self.selectModelArray[0];
    NSArray *nameArr= [model.name componentsSeparatedByString:@"."];
    
    NSString *reName =  self.selectModelArray.count == 1?nameArr[0]:[self.tierTitleArray lastObject];
    for (VULFileObjectModel *model in self.dataArray) {
        if ([model.name containsString:reName]) {
            count ++;
        }
    }
    name = count > 0 ? [NSString stringWithFormat:@"%@(%d)",reName,count] : reName;
    //    level=0
    
    dic = @{
        @"name":name,
        @"sourceID":self.sourceID,
        @"sourceLevel":self.sourceLevel,
        @"dataArr":dicArr,
        @"taskID":[self uuid],
        @"suffix":@"zip",
    };
    
    if([operationType isEqualToString:KLanguage(@"打包")]){
        dic = @{
            @"name":name,
            @"sourceID":self.sourceID,
            @"sourceLevel":self.sourceLevel,
            @"dataArr":dicArr,
            @"taskID":[self uuid],
            @"suffix":@"zip",
            @"level":@"0",

        };
    }
    
    
    [VULBaseRequest requestWithUrl:@"/api/disk/zip" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            [self taskActionWithUuid:dic[@"taskID"]];
            

        } else
            [self makeToast:request.message];
    }];
}
-(void)taskActionWithUuid:(NSString *)uuid{
    [NSThread sleepForTimeInterval:1.0f];
    [VULBaseRequest requestWithUrl:@"/api/disk/zip/taskAction" params:@{@"taskID":uuid} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            NSString *status = [NSString stringWithFormat:@"%@",request.data[@"status"]];
            [self showWaitHudWithProgressString:request.data[@"zipProgress"] title:KLanguage(@"正在压缩...")];

            if (status.boolValue) {
                [self dissmissOperationView];
                self.currentPage =1;
                [self requestList];
                [self makeToast:KLanguage(@"压缩成功")];
                [self dissmissHudView];

            }else{
                [self taskActionWithUuid:uuid];
            }

        } else
            [self makeToast:request.message];
    }];

}
-(void)taskActionWithConvertUuid:(NSString *)uuid{
    [NSThread sleepForTimeInterval:3.0f];
    [VULBaseRequest requestWithUrl:@"/api/disk/doc/convert/taskAction" params:@{@"taskID":uuid} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            NSString *status = [NSString stringWithFormat:@"%@",request.data[@"status"]];
            if (status.boolValue) {
                [self dissmissOperationView];
                self.currentPage =1;
                [self requestList];
                [self makeToast:KLanguage(@"转换成功")];
                [self dissmissHudView];

            }else{
                [self taskActionWithConvertUuid:uuid];
            }

        } else
            [self makeToast:request.message];
    }];

}
-(NSString *)uuid{
    NSArray *changeArray = [[NSArray alloc]initWithObjects:@"0",@"1",@"2",@"3",@"4",@"5",@"6",@"7",@"8",@"9",@"a",@"b",@"c",@"d",@"e",@"f",@"g",@"h",@"i",@"j",@"k",@"l",@"m",@"n",@"o",@"p",@"q",@"r",@"s",@"t",@"u",@"v",@"w",@"x",@"y",@"z",nil];//存放多个数，以备随机取,也可以加上大写字母和其他符号

     NSMutableString* getStr = [[NSMutableString alloc]initWithCapacity:5];

     NSMutableString *changeString= [[NSMutableString alloc]initWithCapacity:6];//申请内存空间

     for(int i =0; i<20; i++) {

     NSInteger index =arc4random()%([changeArray count]-1);//循环六次，得到一个随机数，作为下标值取数组里面的数放到一个可变字符串里，在存放到自身定义的可变字符串
     getStr =changeArray[index];
     changeString= (NSMutableString*)[changeString stringByAppendingString:getStr];

     }
    return changeString;
}

-(void)unZIPFileWithType:(NSString *)operationType{
    VULFileObjectModel *model ;
    if(self.selectModelArray.count == 0) {
        model = self.previewModel;//预览中的解压
    }else{
        model = self.selectModelArray[0];
    }
    if (self.isMove) {
        model = self.changeArray[0];
    }
    [VULBaseRequest requestWithUrl:@"api/disk/checkIsEncrypted" params:@{@"sourceID":model.sourceID} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
           
           [self dissmissHudView];
           if (request.success) {
               NSString *data = request.data;
               [self unZIPFileWithType:operationType  ispassWord:data.boolValue];
           } else
               [self makeToast:request.message];
    }];

}
-(void)unZIPFileWithType:(NSString *)operationType ispassWord:(BOOL)flag{
    if(flag){
        VULInputTitleView *view = [[VULInputTitleView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) title:KLanguage(@"该压缩包需要密码")];
        view.textField.placeholder = KLanguage(@"请输入密码");
        view.alertInputViewBlock = ^(NSString * _Nonnull text) {
            
            [view hiddenView];
            [self uploadUnZIPFileWithType:operationType password:text ispasWord:flag];
        
        };
        [view showInView];
    }else{
        [self uploadUnZIPFileWithType:operationType password:@"" ispasWord:flag];

    }
}

-(void)uploadUnZIPFileWithType:(NSString *)operationType password:(NSString *)password ispasWord:(BOOL)flag{
    [self showWaitHudWithProgressString:@"0" title:KLanguage(@"正在解压...") ];
    VULFileObjectModel *model ;
    if(self.selectModelArray.count == 0) {
        model = self.previewModel;//预览中的解压
    }else{
        model = self.selectModelArray[0];
    }
    
    if (self.isMove) {
        model = self.changeArray[0];
    }
   NSDictionary  *dic = @{
        @"sourceID":model.sourceID,
        @"sourceIDTo":self.sourceID,
        @"password":password
    };
    if ([operationType isEqualToString:@"folder"]) {
        NSArray *nameArr= [model.name componentsSeparatedByString:@"."];
        dic = @{
            @"pathTo":[nameArr firstObject],
              @"sourceID":model.sourceID,
              @"sourceIDTo":self.sourceID,
              @"password":password

          };
    }
    [VULBaseRequest requestWithUrl:@"/api/disk/unZip" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            [self dissmissOperationView];
            [self showWaitHudWithProgressString:@"100" title:KLanguage(@"正在解压...") ];

            if (self.isMove) {
                if (self.saveAndRefreshBlock) {
                    self.saveAndRefreshBlock();
                }
                [self clickCancelBtn];
            }else{
                [self makeToast:KLanguage(@"解压成功")];
                self.currentPage =1;
                [self requestList];
            }
            
        } else
            [self makeToast:request.message];
    }];
}

-(void)topFileWithType:(NSString *)operationType{
    [self showWaitHudWithString:nil];
    VULFileObjectModel *model = self.selectModelArray[0];
    NSDictionary *dic = @{@"parentID":model.parentID,@"sourceID":model.sourceID,@"operation":operationType};
    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self dissmissHudView];
        if (request.success) {
            [self dissmissOperationView];
            self.currentPage =1;
            [self requestList];

        } else
            [self makeToast:request.message];
    }];
}
-(void)cancelCollectFileWithType:(NSString *)operationType{

    [self showWaitHudWithString:nil];
    NSMutableArray *dicArr = [NSMutableArray array];
    for (VULFileObjectModel *model  in self.selectModelArray ) {
        [dicArr addObject:@{ @"name":model.name,
                            @"path":model.parentLevel,
                                @"id":model.sourceID}];
    }
    NSDictionary *dic;
    dic = @{
        @"dataArr":dicArr,
        @"operation":operationType
    };

    [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self dissmissHudView];
        if (request.success) {
            [self dissmissOperationView];
            self.currentPage =1;
            [self requestList];

        } else
            [self makeToast:request.message];
    }];
}
-(void)cancelOrAddTagType:(NSString *)operationType tagId:(NSString *)tagId{

    VULFileObjectModel *model = self.selectModelArray[0];
    NSDictionary *dic = @{@"labelId":tagId,@"files":model.sourceID,@"block":operationType};
    [VULBaseRequest requestWithUrl:@"/api/disk/tag/fileTag" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
        [self dissmissHudView];
        if (request.success) {
            [self dissmissOperationView];
            self.currentPage =1;
            [self requestList];

        } else
            [self makeToast:request.message];
    }];

}
#pragma mark -set
-(FBHeaderView *)headerView{
    if (!_headerView) {
        _headerView = [[FBHeaderView alloc] initWithFrame:CGRectZero];
    }
    return _headerView;
}
-(FBFileTopView *)topView{
    if (!_topView) {
        _topView = [[FBFileTopView alloc] initWithFrame:CGRectMake(0, K_NavBar_Height, VULSCREEN_WIDTH, 40)];
    }
    return _topView;
}


-(zhPopupController *)popup{
    if (!_popup) {
        _popup =[[zhPopupController alloc] initWithView:self.leftView size:CGSizeMake(VULSCREEN_WIDTH * 0.8, self.leftView.height)];
    }
    return _popup;
}

-(FBNavgationOperateView *)navView{
    if (!_navView) {
        _navView = [[FBNavgationOperateView alloc] initWithFrame:CGRectMake(0, (K_NavBar_Height-K_StatusBar_Height-40)/2+K_StatusBar_Height, VULSCREEN_WIDTH, 40)];
    }
    return _navView;
}
-(FBFileNodeChooseView *)leftView{
    if (!_leftView) {
        _leftView = [[FBFileNodeChooseView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH*0.8, VULSCREEN_HEIGHT)];
    }
    return _leftView;
}

-(FBOperationView *)operationView{
    if (!_operationView) {
        _operationView = [[FBOperationView alloc] initWithFrame:CGRectMake(0, VULSCREEN_HEIGHT, VULSCREEN_WIDTH, 80+K_BottomBar_Height)];
        MJWeakSelf
        
        _operationView.allSelectModel = ^(BOOL flag) {
            if (flag) {
                [weakSelf.selectModelArray removeAllObjects];
                [weakSelf.selectModelArray  addObjectsFromArray:weakSelf.dataArray];
                for (VULFileObjectModel *model in weakSelf.selectModelArray) {
                    model.isSelect = YES;
                }
                weakSelf.operationView.dataArray = [NSMutableArray arrayWithArray:weakSelf.selectModelArray];

                [weakSelf.collectionView reloadData];
            }else{
                [weakSelf.selectModelArray removeAllObjects];
                for (VULFileObjectModel *model in weakSelf.dataArray) {
                    model.isSelect = NO;
                }
                weakSelf.operationView.dataArray = [NSMutableArray arrayWithArray:weakSelf.selectModelArray];
                [weakSelf.collectionView reloadData];
            }
        };
      
        _operationView.dismisssView = ^{
            weakSelf.isEdit = NO;
            for (VULFileObjectModel *model in weakSelf.selectModelArray) {
                model.isSelect = NO;
            }
            [weakSelf.selectModelArray removeAllObjects];
            [weakSelf.collectionView reloadData];
            [weakSelf dissmissOperationView];
        };
        _operationView.getInfoReloadData = ^{
            weakSelf.currentPage =1;
            [weakSelf requestList];
        };
        _operationView.gotoTagManageMent = ^{
            FBFileTagManagementVC *vc =[FBFileTagManagementVC new];
            [self.navigationController pushViewController:vc animated:YES];
        };
        _operationView.selectCollectType = ^(NSString * _Nonnull block, NSString * _Nonnull tag) {
            [weakSelf cancelOrAddTagType:block tagId:tag];
        };
        _operationView.changeType = ^(NSString * _Nonnull type) {
            [self showWaitHudWithString: KLanguage(@"正在转换...")];

            VULFileObjectModel *model = weakSelf.selectModelArray[0];
            NSDictionary *dic = @{@"sourceID":model.sourceID,@"sourceIDTo":self.sourceID,@"suffix":type,        @"taskID":[self uuid]};
            [VULBaseRequest requestWithUrl:@"/api/disk/doc/convert" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                if (request.success) {
                    [self taskActionWithConvertUuid:dic[@"taskID"]];

                } else
                    [self makeToast:request.message];
            }];
        };
        _operationView.selectOperationWithTitle = ^(NSString * _Nonnull title) {
            if ([title isEqualToString:KLanguage(@"重命名")]) {
                [weakSelf reName];
                return;
            }
            if ([title isEqualToString:KLanguage(@"复制")]) {
                FBHomeViewController *vc = [FBHomeViewController new];
                vc.isMove = YES;
                vc.icon = @"copy";
                vc.operation = @"copy";
                vc.flag = weakSelf.flag;
                vc.changeArray = [NSMutableArray arrayWithArray:weakSelf.selectModelArray];
                vc.saveAndRefreshBlock = ^{
//                    weakSelf.sourceID =   weakSelf.fitstSourceID;
                    weakSelf.currentPage =1;
                    [weakSelf requestList];
                };
                [weakSelf presentViewController:vc animated:YES completion:nil];
                return;
            }
            if ([title isEqualToString:KLanguage(@"剪切")]) {
                FBHomeViewController *vc = [FBHomeViewController new];
                vc.isMove = YES;
                vc.flag = weakSelf.flag;
                vc.icon = @"move";
                vc.operation = @"move";
                vc.changeArray = [NSMutableArray arrayWithArray:weakSelf.selectModelArray];
                vc.saveAndRefreshBlock = ^{
//                    weakSelf.sourceID =   weakSelf.fitstSourceID;
                    weakSelf.currentPage =1;
                    [weakSelf requestList];
                };
                [weakSelf presentViewController:vc animated:YES completion:nil];
                return;
            }
            if ([title isEqualToString:KLanguage(@"属性")]) {
                FBFileAddrituteRootVC *vc = [FBFileAddrituteRootVC new];
                vc.model = weakSelf.selectModelArray[0];
                vc.saveAndRefreshBlock = ^{
//                    weakSelf.sourceID =   weakSelf.fitstSourceID;
                    weakSelf.currentPage =1;
                    [weakSelf requestList];
                };
                vc.openDetialWithModel = ^(VULFileObjectModel * _Nonnull model) {
                    [weakSelf openFileWithModel:model withIndexPath:nil];
                };
                vc.gotoTagManageMent = ^{
                    FBFileTagManagementVC *vc =[FBFileTagManagementVC new];
                    [weakSelf.navigationController pushViewController:vc animated:YES];
                };
                [weakSelf presentViewController:vc animated:YES completion:nil];
                return;
            }
            
            
            if ([title isEqualToString:KLanguage(@"压缩")]) {
                [weakSelf ZIPFileWithType:@"zip"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"打包")]) {
                [weakSelf ZIPFileWithType:KLanguage(@"打包")];
                return;
            }
            if ([title isEqualToString:KLanguage(@"解压到当前")]) {
                [weakSelf unZIPFileWithType:@"current"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"解压到文件夹")]) {
                [weakSelf unZIPFileWithType:@"folder"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"取消分享")]) {
                [self hiddenView];
                return;
            }

            if ([title isEqualToString:KLanguage(@"解压到...")]) {
                FBHomeViewController *vc = [FBHomeViewController new];
                vc.isMove = YES;
                vc.flag = weakSelf.flag;
                vc.icon = @"ZIP";
                vc.operation = @"ZIP";
                vc.changeArray = [NSMutableArray arrayWithArray:weakSelf.selectModelArray];
                vc.saveAndRefreshBlock = ^{
//                    weakSelf.sourceID =   weakSelf.fitstSourceID;
                    weakSelf.currentPage =1;
                    [weakSelf requestList];
                };
                [weakSelf presentViewController:vc animated:YES completion:nil];
                return;

            }
            
            if ([title isEqualToString:KLanguage(@"创建快捷方式")]) {
                [weakSelf mkfileFileWithType:@"mkfile" isDesktop:NO];
                return;
            }
            if ([title isEqualToString:KLanguage(@"发送到桌面快捷方式")]) {
                [weakSelf mkfileFileWithType:@"mkfile" isDesktop:YES];
                return;
            }
            if ([title isEqualToString:KLanguage(@"还原")]) {
                [weakSelf operationFileWithType:@"restore"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"收藏")]) {
                [weakSelf collectFileWithType:@"fav"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"取消收藏")]) {
                [weakSelf cancelCollectFileWithType:@"delFav"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"置顶")]) {
                [weakSelf topFileWithType:@"top"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"取消置顶")]) {
                [weakSelf topFileWithType:@"cancelTop"];
                return;
            }
            if ([title isEqualToString:KLanguage(@"打开")]) {
                VULFileObjectModel *model = weakSelf.selectModelArray[0];
                
                if (!model.isFolder.boolValue) { //是文件
                    [weakSelf openFileWithModel:model withIndexPath:[NSIndexPath indexPathForRow:0 inSection:0]];
                    
                } else {
                    self.collectionView.contentOffset = CGPointMake(0, 0);
                    self.sourceID = model.sourceID;
                    self.sourceLevel = model.parentLevel;
                    self.parentID = model.parentID;

                    [self.tierTitleArray addObject:model.name.length>0?model.name:model.groupName];
                    self.topView.dataArr = self.tierTitleArray;
                    [self.directoryArray addObject:model.sourceID];
                    self.currentPage = 1;
                    [self dissmissOperationView];
                    [self requestList];
                }
       
                return;
            }
            if ([title isEqualToString:KLanguage(@"删除")]) {
                NSString *title = @"";
                if ([weakSelf.icon isEqualToString:@"recycle"]) {
                    title = KLanguage(@"确定彻底删除？无法还原");
                }else{
                    title =KLanguage( @"确定移动到回收站吗？");

                }
                
                UIAlertController *alertController = [UIAlertController alertControllerWithTitle:title message:nil preferredStyle:UIAlertControllerStyleAlert];
                     [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
                         MJWeakSelf
                         if ([weakSelf.icon isEqualToString:@"recycle"]) {
                             [weakSelf operationFileWithType:@"remove"];
                         }else{
                             [weakSelf operationFileWithType:@"recycle"];
                         }
                    }]];
                     [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {

                               NSLog(@"取消");

                       }]];
                    [self presentViewController:alertController animated:YES completion:nil];
            
                return;

            }
            if ([title isEqualToString:KLanguage(@"彻底删除")]) {
                NSString *title = @"";
                if ([weakSelf.icon isEqualToString:@"recycle"]) {
                    title = KLanguage(@"确定彻底删除？无法还原");
                }else{
                    title =KLanguage( @"确定移动到回收站吗？");

                }
                
                UIAlertController *alertController = [UIAlertController alertControllerWithTitle:title message:nil preferredStyle:UIAlertControllerStyleAlert];
                     [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"确定") style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
                         MJWeakSelf
                         if ([weakSelf.icon isEqualToString:@"recycle"]) {
                             [weakSelf operationFileWithType:@"remove"];
                         }else{
                             [weakSelf operationFileWithType:@"recycle"];
                         }
                    }]];
                     [alertController addAction:[UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {

                               NSLog(@"取消");

                       }]];
                    [self presentViewController:alertController animated:YES completion:nil];
            
                return;

            }
            
            if ([title isEqualToString:KLanguage(@"下载")]) {
//                ZFDownloadManager
                for (VULFileObjectModel *model in weakSelf.selectModelArray ) {
                    [weakSelf downFileWithModel:model];
                }
                return;

            }
            if ([title isEqualToString:KLanguage(@"分享")] || [title isEqualToString:KLanguage(@"编辑外链")]) {
//
                VULFileObjectModel *model = weakSelf.selectModelArray[0];
                FBShareVC *vc = [FBShareVC new];
                vc.model = model;
                vc.saveAndRefreshBlock = ^{
                    weakSelf.currentPage =1;
                    [weakSelf requestList];
                };
                [weakSelf.navigationController pushViewController:vc animated:YES];;
                return;

            }
            if ([title isEqualToString:   KLanguage(@"上传新版本")]) {
                [self uploadData];
                return;
            }
            if ([title isEqualToString:   KLanguage(@"文件查重")]  ) {
                VULFileObjectModel   *model=  weakSelf.selectModelArray[0];

                self.repeatSourceID  = model.sourceID;
                self.repeatName  = @"";
                weakSelf.currentPage =1;
                [weakSelf requestList];
                [self dissmissOperationView];
                return;
            }
            if ([title isEqualToString:   KLanguage(@"文件名查重")] ) {
                VULFileObjectModel   *model=  weakSelf.selectModelArray[0];
                self.repeatSourceID  = model.sourceID;
                self.repeatName  = model.name;
                weakSelf.currentPage =1;
                [weakSelf requestList];
                [self dissmissOperationView];

                return;
            }
            if ([title isEqualToString:   KLanguage(@"编辑")]) {
                VULFileObjectModel   *model=  weakSelf.selectModelArray[0];
                NSDictionary *dic;
                dic = @{
                    @"busType":@"cloud",
                    @"path":@"edit",
                    @"sourceID":model.sourceID
                };
                [VULAllFileModel getPreview:dic completion:^(__kindof VULBaseRequest * _Nonnull request) {
                    if (request.success) {
                        NSString *yzEditData = request.data[@"yzEditData"];
                       
                        yzEditData = [yzEditData stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
                       
                        VULBaseWebViewVC *vc = [[VULBaseWebViewVC alloc] init];
                        vc.nvaTitle = model.name;
                        vc.currentURL = yzEditData;
                            [self.navigationController pushViewController:vc animated:YES];

                    }else{
                        
                    }
                }];
                return;
            }
         
          
            
            
            
        };
    }
    return _operationView;
}
-(void)hiddenView{
    
    [self vul_showAlertWithTitle:KLanguage(@"确定取消该外链分享？取消后外链将失效！")   message:@"" appearanceProcess:^(VULAlertController * _Nonnull alertMaker) {
        alertMaker.
        addActionCancelTitle(KLanguage(@"取消")).
        addActionDestructiveTitle(KLanguage(@"确定"));
    } actionsBlock:^(NSInteger buttonIndex, UIAlertAction * _Nonnull action, VULAlertController * _Nonnull alertSelf) {
        if (buttonIndex == 1) {
            VULFileObjectModel   *model=  self.selectModelArray[0];

           NSDictionary  *param = @{@"shareID":model.shareID};
            if(self.selectModelArray.count>1){
                NSMutableArray *arr = [NSMutableArray array];
                for(VULFileObjectModel * model in self.selectModelArray ){
                    [arr addObject:model.shareID];
                }
                param = @{@"shareIDStr":[arr componentsJoinedByString:@","]};
            }
            [VULBaseRequest requestWithUrl:@"/api/disk/userShare/cancel" params:param requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                if (request.success) {
                    [self dissmissOperationView];
                    self.currentPage =1;
                    [self requestList];
                } else {
                    [self makeToast:request.responseObject[@"message"]];
                }
            }];
        }
    }];
}
-(void)downFileWithModel:(VULFileObjectModel *)model{

    NSDictionary *dic;
    dic = @{
        @"busType":@"cloud",
        @"path":@"info",
        @"sourceID":model.sourceID
    };
    MJWeakSelf
    [VULBaseRequest requestWithUrl:@"/api/disk/preview" params:dic requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            NSString *name =  [NSString stringWithFormat:@"%@",model.name];
            NSString *downloadUrl = request.data[@"downloadUrl"];
            NSString *fileID =  [NSString stringWithFormat:@"%@",request.data[@"fileID"]];
            NSString *fileType =  [NSString stringWithFormat:@"%@",request.data[@"fileType"]];
            NSString *size =  [NSString stringWithFormat:@"%@",request.data[@"size"]];

//            // 此处是截取的下载地址，可以自己根据服务器的视频名称来赋值
            NSString *Url = [NSString stringWithFormat:@"%@%@",ChooseUrl,downloadUrl];
            Url = [Url stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLQueryAllowedCharacterSet]];
            UBUploadModel *downloadModel = [UBUploadModel new];
            downloadModel.fileUrl = [NSURL URLWithString:Url];
            downloadModel.fileName =name;
            downloadModel.isDown = YES;
            downloadModel.fileType = fileType;
            downloadModel.fileID = fileID;
            downloadModel.filePath = fileID;
            downloadModel.path = model.path;
            downloadModel.fileSize =size.integerValue;
            [FBDownloadFileAllManage.sharedManager addDownloadFileWithModel:downloadModel];
            [VULNotificationCenter postNotificationName:@"uploadChangeNotificationCenter" object:nil];
            for (VULFileObjectModel *model in weakSelf.selectModelArray) {
                model.isSelect = NO;
            }
            [weakSelf.selectModelArray removeAllObjects];
            [self dissmissOperationView];
            [self.collectionView reloadData];

        }else{
           
        }
     
    }];
}
-(void)dissmissOperationView{
    for (VULFileObjectModel *model in self.selectModelArray) {
        model.isSelect = NO;
    }
    [self.selectModelArray removeAllObjects];
    self.isEdit = NO;
    [self.collectionView reloadData];
    MJWeakSelf
    [UIView animateWithDuration:0.1 animations:^{
        CGRect frame = weakSelf.operationView.frame;
        frame.origin.y = weakSelf.view.frame.size.height;
        weakSelf.operationView.frame = frame;
    } completion:^(BOOL finished) {
        [weakSelf.operationView removeFromSuperview]; // 隐藏后从父视图中移除
    }];
}
-(zhPopupController *)popup1{
    if (!_popup1) {
        _popup1 =[[zhPopupController alloc] initWithView:self.operationView size:CGSizeMake(VULSCREEN_WIDTH, 100)];
    }
    return _popup1;
}
- (UICollectionView *)collectionView {
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    if (!_collectionView) {
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0,VULSCREEN_WIDTH, VULSCREEN_HEIGHT - K_NavBar_Height) collectionViewLayout:layout];
        _collectionView.showsHorizontalScrollIndicator = NO;
        _collectionView.alwaysBounceVertical = YES;
        //_collectionView.alwaysBounceHorizontal = NO;
        if (@available(iOS 11.0, *)) {
            _collectionView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        }
        _collectionView.backgroundColor = [UIColor whiteColor];
        [_collectionView registerClass:[VULFileListCell class] forCellWithReuseIdentifier:@"VULFileListCell"];
        [_collectionView registerClass:[VULFileGridCell class] forCellWithReuseIdentifier:@"VULFileGridCell"];
        [_collectionView registerClass:[VULInfoCollectionViewCell class] forCellWithReuseIdentifier:@"VULInfoCollectionViewCell"];
        [_collectionView registerClass:[VULInfoListCell class] forCellWithReuseIdentifier:@"VULInfoListCell"];

        
        
        WeakSelf(self)
        
        _collectionView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
//            self->infoTypeId  = @"";
            weakself.currentPage = 1;
            [weakself requestList];
        }];
        _collectionView.mj_footer = [MJRefreshBackNormalFooter footerWithRefreshingBlock:^{
            weakself.currentPage++;
            [weakself requestList];
        }];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
    }
    return _collectionView;
}
- (UBWaterWaveButton *)addButton {
    if (!_addButton) {
        _addButton = [UBWaterWaveButton new];
        _addButton.frame = CGRectMake(self.view.bounds.size.width - 75, self.view.bounds.size.height - VULSCREEN_HEIGHT / 6 - 40, 50, 50);
//        _addButton.highlightedColor = [HEXCOLOR(0x654EC3) colorWithAlphaComponent:0.2] ;
//        _addButton.backgroundColor = HEXCOLOR(0x654EC3);
        [_addButton setImage:[UIImage imageNamed:@"icon_add"] forState:UIControlStateNormal];
        [_addButton addTarget:self action:@selector(addButtonAction:) forControlEvents:UIControlEventTouchUpInside];
//        _addButton.layer.cornerRadius = _addButton.frame.size.width / 2;
//        _addButton.layer.masksToBounds = YES;
//        
//        _addButton.layer.shadowOpacity = 0.2; //阴影透明度
//        _addButton.layer.shadowColor = [UIColor blackColor].CGColor; //阴影的颜色
//        _addButton.layer.shadowRadius = 3; //阴影的圆角
//        _addButton.layer.shadowOffset = CGSizeMake(-3, 3); //阴影偏移量
        //阴影偏移量
        UIPanGestureRecognizer *panRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(buttonMoving:)];
                panRecognizer.cancelsTouchesInView = YES;
        [_addButton addGestureRecognizer:panRecognizer];

    }
    return _addButton;
}
- (void)buttonMoving:(UIPanGestureRecognizer *)recognizer {
    UIButton *button = (UIButton *)recognizer.view;
    CGPoint translation = [recognizer translationInView:button];
    NSLog(@"x====%.2f",button.center.x + translation.x);
    NSLog(@"y====%.2f", button.center.y + translation.y);
    button.center = CGPointMake(button.center.x + translation.x, button.center.y + translation.y);

    if (button.center.x + translation.x <35  ){
        button.center = CGPointMake(35, button.center.y + translation.y);
    }
    if (button.center.x + translation.x >VULSCREEN_WIDTH-35  ){
        button.center = CGPointMake(VULSCREEN_WIDTH-35 , button.center.y + translation.y);
    }
    if (button.center.y + translation.y <K_NavBar_Height+40+35  ){
        button.center = CGPointMake(button.center.x + translation.x, K_NavBar_Height+40+35);
    }
    if (button.center.y + translation.y >VULSCREEN_HEIGHT-K_TabBar_Height-35  ){
        button.center = CGPointMake(button.center.x + translation.x, VULSCREEN_HEIGHT-K_TabBar_Height-35);
    }
    [recognizer setTranslation:CGPointZero inView:button];

}
#pragma mark - 新增文件
-(void)addButtonAction:(UBWaterWaveButton *)sender{
    self.upNewModel = nil;
    FBAddFileView *add = [[FBAddFileView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, 150+30+10+K_BottomBar_Height)];
    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
    add.dismisssView = ^{
        [popup2 dismiss];
    
    };
    add.addFileWithTitle = ^(NSString * _Nonnull title) {
        if ([title isEqualToString:KLanguage(@"文件夹")]) {
            [popup2 dismiss];
            [self addFile];
            return;
        }
        if ([title isEqualToString:KLanguage(@"拍摄")]) {
            [popup2 dismiss];
            [self takephotos];
 
            return;
        }
        if ([title isEqualToString:KLanguage(@"剪贴板")]) {
            [popup2 dismiss];
//            [self takephotos];
            zhPopupController *popup2 =[[zhPopupController alloc] initWithView:
            self.htmlView  size:CGSizeMake(VULSCREEN_WIDTH*0.8,  self.htmlView .height)];
            NSString *clipboardText = [UIPasteboard generalPasteboard].string;
            if (clipboardText) {
                self.htmlView.textView.text = clipboardText;
              } else {
              }
            self.htmlView .selectBlock = ^(NSString * _Nonnull text, bool flag) {
                NSDictionary *dic;
                if(flag){
                    NSDictionary *textDic =@{@"type":@"url",@"value":text,@"width":@"80%",@"height":@"70%"};
                    dic = @{@"sourceID":self.sourceID,@"operation":@"mkfile",@"name":  @"新建网址.oexe",@"content":[self jsonStringFromObject:textDic]};
                }else{
                    dic = @{@"sourceID":self.sourceID,@"operation":@"mkfile",@"name":  @"新建文件.txt",@"content":text};

                }
                [VULBaseRequest requestWithUrl:@"/api/disk/operation" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
                    [self dissmissHudView];
                    if (request.success) {
                        [self makeToast:KLanguage(@"保存成功") ];
                        [self dissmissOperationView];
                        self.currentPage =1;
                        [self requestList];

                    } else
                        [self makeToast:request.message];
                }];
                    [popup2 dismiss];

            };
            popup2.layoutType = zhPopupLayoutTypeCenter;
            popup2.presentationStyle = zhPopupSlideStyleFromBottom;
            popup2.maskAlpha = 0.35;
            [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
            return;
        }
        if ([title isEqualToString:KLanguage(@"图片")]) {
            [popup2 dismiss];
            [self.imagePicker setImagePickerCofing:^(TZImagePickerController *_Nonnull pickerController) {
                pickerController.maxImagesCount = 9;
                pickerController.allowPickingVideo = YES;
            }];
            [self.imagePicker.selectedPhotos removeAllObjects];
            [self.imagePicker.selectedAssets removeAllObjects];
            [self.imagePicker pushTZImagePickerController];
            return;
        }
        if ([title isEqualToString:KLanguage(@"本地文件")]) {
            [popup2 dismiss];
            if (@available(iOS 11, *)) {
                [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAlways;
            }
            self.documentPickerView = [[UIDocumentPickerViewController alloc] initWithDocumentTypes:@[@"public.item"] inMode:UIDocumentPickerModeOpen];
            self.documentPickerView.delegate = self;
            self.documentPickerView.modalPresentationStyle = UIModalPresentationFullScreen;
            [self presentViewController:        self.documentPickerView animated:YES completion:nil];
        }
        
    };
    popup2.layoutType = zhPopupLayoutTypeBottom;
    popup2.presentationStyle = zhPopupSlideStyleFromBottom;
    popup2.maskAlpha = 0.35;
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
-(void)takephotos{
    if ([CLLocationManager locationServicesEnabled]) {
        // 请求定位权限
        [self.locationManager requestWhenInUseAuthorization];
    }
    

    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    UIAlertAction *action1 = [UIAlertAction actionWithTitle:KLanguage(@"打卡拍照") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
       
      
        if(![self isLocationServicesEnabled]){
            // 检查定位服务是否可用
            UIAlertController *alertController = [UIAlertController alertControllerWithTitle: KLanguage(@"该功能需要定位服务")
                                        message:KLanguage(@"请启用定位服务以使用该功能。")
                                                preferredStyle:UIAlertControllerStyleAlert];
                
                UIAlertAction *settingsAction = [UIAlertAction actionWithTitle:KLanguage(@"去设置") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    NSURL *settingsURL = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
                    [[UIApplication sharedApplication] openURL:settingsURL options:@{} completionHandler:nil];
                }];
                
                UIAlertAction *cancelAction = [UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:nil];
                
                [alertController addAction:settingsAction];
                [alertController addAction:cancelAction];
                
                [self presentViewController:alertController animated:YES completion:nil];
            return;
        }
        
        // 点击按钮1后的操作
        FBTakePhotoVC *vc = [FBTakePhotoVC new];
        vc.sourceID = self.sourceID;
        vc.saveAndRefreshBlock = ^{
            self.currentPage =1;
            [self requestList];
        };
//        图片转换会丢失经纬度 改成直接上传
//        vc.uploadImage = ^(UIImage * _Nonnull image) {
//
//
//            NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
//            [formatter setDateFormat:@"yyyy_MM_dd_HH_mm_ss"];
//            NSString *key = [formatter stringFromDate:[NSDate date]];
//
//            // 解决图片偏转90度的问题
//            if(image.imageOrientation != UIImageOrientationUp) {
//                UIGraphicsBeginImageContext(image.size);
//                [image drawInRect:CGRectMake(0, 0, image.size.width, image.size.height)];
//                image = UIGraphicsGetImageFromCurrentImageContext();
//                UIGraphicsEndImageContext();
//            }
//            [[SDImageCache sharedImageCache] storeImage:image forKey:key toDisk:YES completion:^{
//                NSString *filePath = [[SDImageCache sharedImageCache] cachePathForKey:key];
//
//
//
//                if (self.upNewModel) {
//
//                    [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:filePath] sourceID:self.upNewModel.parentID pathId:  self.upNewModel.sourceID isPic:YES success:^(bool sucess) {
//
//                        if (sucess) {
//                            self.currentPage =1;
//                                [self requestList];
//
//                        }
//
//                    }];
//                }else{
//                    [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:filePath]  sourceID:self.sourceID isPic:YES  success:^(bool sucess) {
//                        if (sucess) {
//                            self.imageCount ++;
//                            if (self.imagePicker.selectedAssets.count ==self.imageCount ) {
//                                self.currentPage =1;
//                                    [self requestList];
//                            }
//
//                        }
//                    }];
//                }
//
//
//
//            }];
//        };
        [self.navigationController pushViewController:vc animated:YES];
    }];
    UIAlertAction *action2 = [UIAlertAction actionWithTitle:KLanguage(@"拍摄") style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        // 点击按钮3后的操作
        [self.imagePicker.selectedPhotos removeAllObjects];
        [self.imagePicker.selectedAssets removeAllObjects];
        self.imagePicker.imagePickerVc.mediaTypes = [NSArray arrayWithObjects:@"public.movie", kUTTypeImage, nil];
        self.imagePicker.imagePickerVc.videoQuality = UIImagePickerControllerQualityTypeMedium;
        self.imagePicker.imagePickerVc.videoMaximumDuration = 90.0;
        [self.imagePicker takePhoto];
        // 点击按钮2后的操作
    }];
    UIAlertAction *action3 = [UIAlertAction actionWithTitle:KLanguage(@"取消") style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
 
    }];
    
    [alertController addAction:action1];
    [alertController addAction:action2];
    [alertController addAction:action3];
    
    UIPopoverPresentationController *popoverPresentationController = alertController.popoverPresentationController;
    if (popoverPresentationController) {
        popoverPresentationController.sourceView = self.view;
    }
    
    [self presentViewController:alertController animated:YES completion:nil];
}
#pragma mark - 创建文件夹
-(void)addFile{
    VULInputTitleView *view = [[VULInputTitleView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_HEIGHT) title:KLanguage(@"新建文件夹") ];
    view.alertInputViewBlock = ^(NSString * _Nonnull text) {
        [view hiddenView];
        NSInteger count = 0;
        NSString *name;
        for (VULFileObjectModel *model in self.dataArray) {
            if ([model.name containsString:KLanguage(@"新建文件夹")]) {
                count ++;
            }
        }
        name = count > 0 ? [NSString stringWithFormat:@"%@(%d)",KLanguage(@"新建文件夹"),count] : KLanguage(@"新建文件夹");
//        [self addFile:text.length ? text : name];
        [self showWaitHudWithString:nil];
        NSDictionary *dic = @{@"name":text.length ? text : name,@"parentID":self.sourceID};

        [VULBaseRequest requestWithUrl:@"/api/disk/createFolder" params:dic requestType:YTKRequestMethodPOST completion:^(__kindof VULBaseRequest *_Nonnull request) {
            [self dissmissHudView];
            if (request.success) {
                [self dissmissOperationView];
                [self.collectionView.mj_header beginRefreshing];

            } else
                [self makeToast:request.message];
        }];
    };
    [view showInView];
}
#pragma mark - 视频播放器
- (ZFPlayerControlView *)controlView {
    if (!_controlView) {
        _controlView = [ZFPlayerControlView new];
        _controlView.fastViewAnimated = YES;
    }
    return _controlView;
}

- (UIView *)containerView {
    if (!_containerView) {
        _containerView = [[UIView alloc] init];
    }
    return _containerView;
}
- (FBNewHtmlView *)htmlView {
    if (!_htmlView) {
        _htmlView = [[FBNewHtmlView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, VULSCREEN_WIDTH)];
    }
    return _htmlView;
}

- (TZImagePickerUtil *)imagePicker {
    if (!_imagePicker) {
        _imagePicker = [[TZImagePickerUtil alloc] init];
        _imagePicker.imagePickerCofing = ^(TZImagePickerController * _Nonnull pickerController) {
            pickerController.maxImagesCount = 1;
            pickerController.allowPickingVideo = YES;
        };
        _imagePicker.selectedBolck = ^{
            [self uploadImage];
        };
    }
    return _imagePicker;
}
-(FBSearchView *)searchView{
    if (!_searchView) {
        _searchView = [[FBSearchView alloc] initWithFrame:CGRectMake(0, K_NavBar_Height, VULSCREEN_WIDTH, VULSCREEN_HEIGHT-K_NavBar_Height)];
        _searchView.clickViewBlock = ^(NSDictionary * _Nonnull dic) {
            [self.searchDic removeAllObjects];
            if (dic.allKeys.count == 0) {
                _searchView = nil;
//                return;
            }
           
            [self.searchDic addEntriesFromDictionary:dic];
            [self.collectionView.mj_header beginRefreshing];
        };
        _searchView.dismiss = ^{
            self.navView.moreSearchBtn.selected = NO;
        };
    }
    return _searchView;
}
-(NSMutableDictionary *)searchDic{
    if (!_searchDic) {
        _searchDic = [NSMutableDictionary dictionary];
    }
    return _searchDic;
}
- (void)uploadImage {
    self.imageCount = 0;
    if (self.imagePicker.selectedAssets.count > 0) {
        if (self.imagePicker.selectedAssets.count ==1) {
            PHAsset *asset = self.imagePicker.selectedAssets[0];
            UIImage *image = self.imagePicker.selectedPhotos[0] ;

            [self uploadWithPHAsset:asset withImage:image count:0];
        }else{
            for (int i = 0; i <self.imagePicker.selectedAssets.count; i ++) {
                PHAsset *asset = self.imagePicker.selectedAssets[i];
                UIImage *image = self.imagePicker.selectedPhotos[i] ;
                [self uploadWithPHAsset:asset withImage:image count:i];

            }
       
        }
        
    }
     
    
}
-(void)uploadWithPHAsset:(PHAsset *)asset withImage:(UIImage *) image count:(NSInteger)index{
    if (asset.mediaType == PHAssetMediaTypeImage) {
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateFormat:@"yyyy-MM-dd-HH:mm:ss"];
        NSString *key = [[formatter stringFromDate:[NSDate date]] stringByAppendingString: [NSString stringWithFormat:@"%ld.jpg",index]];
        
        // 解决图片偏转90度的问题
        if(image.imageOrientation != UIImageOrientationUp) {
            UIGraphicsBeginImageContext(image.size);
            [image drawInRect:CGRectMake(0, 0, image.size.width, image.size.height)];
            image = UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();
        }
        [[SDImageCache sharedImageCache] storeImage:image forKey:key toDisk:YES completion:^{
            NSString *filePath = [[SDImageCache sharedImageCache] cachePathForKey:key];

            if (self.upNewModel) {

                [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:filePath] sourceID:self.upNewModel.parentID pathId:  self.upNewModel.sourceID isPic:YES success:^(bool sucess) {
     
                    if (sucess) {
                        self.currentPage =1;
                            [self requestList];
                 
                    }

                }];
            }else{
                [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:filePath]  sourceID:self.sourceID isPic:YES  success:^(bool sucess) {
                    if (sucess) {
                        self.imageCount ++;
                        if (self.imagePicker.selectedAssets.count ==self.imageCount ) {
                            self.currentPage =1;
                                [self requestList];
                        }
                     
                    }
                }];
            }
         

          
        }];
    }
    else if (asset.mediaType == PHAssetMediaTypeVideo) {
        [[TZImageManager manager] getVideoOutputPathWithAsset:asset success:^(NSString *outputPath) {
            if (!outputPath) {
                return;
            }
            if (  self.upNewModel) {

                [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:outputPath]
                                           sourceID:self.upNewModel.parentID pathId:  self.upNewModel.sourceID isPic:NO  success:^(bool sucess) {
                    if (sucess) {
                        self.currentPage =1;
                        [self requestList];
                    }

                }];
            }else{
            [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:outputPath]  sourceID:self.sourceID isPic:NO  success:^(bool sucess) {
                if (sucess) {
                    self.currentPage =1;
                    [self requestList];
                }

            }];
            }
            
            
        } failure:^(NSString *errorMessage, NSError *error) {
          
        }];
    }
}

#pragma mark - UIDocumentPickerDelegate
- (void)documentPicker:(UIDocumentPickerViewController *)controller didPickDocumentAtURL:(NSURL *)url {
    BOOL fileUrlAuthozied = [url startAccessingSecurityScopedResource];
    if(fileUrlAuthozied) {
        NSFileCoordinator * fileCoordinator = [[NSFileCoordinator alloc] init];
        NSError * error;
        [fileCoordinator coordinateReadingItemAtURL:url options:0 error:&error byAccessor:^(NSURL *newURL) {
      
            
            if (  self.upNewModel) {
            [ChunkUploader writeDataWithModel:newURL
                                       sourceID:  self.upNewModel.parentID pathId:  self.upNewModel.sourceID isPic:NO  success:^(bool sucess) {
                if (sucess) {
                    self.currentPage =1;
                    [self requestList];
                }
                [url stopAccessingSecurityScopedResource];

            }];
                
            }else{
                [ChunkUploader writeDataWithModel:newURL  sourceID:self.sourceID isPic:NO  success:^(bool sucess) {
                    if (sucess) {
                        self.currentPage =1;
                        [self requestList];
                    }
                    [url stopAccessingSecurityScopedResource];

                }];
            }
          
           
       
//
        }];
    }
    else {
        //Error handling
    }
}
- (BOOL)isLocationServicesEnabled {
    if ([CLLocationManager locationServicesEnabled]) {
        CLAuthorizationStatus authorizationStatus = [CLLocationManager authorizationStatus];
        if (authorizationStatus == kCLAuthorizationStatusAuthorizedAlways || authorizationStatus == kCLAuthorizationStatusAuthorizedWhenInUse) {
            // 定位服务已开启，并且已授权
            return YES;
        } else {
            // 定位服务已开启，但尚未授权
            return NO;
        }
    } else {
        // 定位服务未开启
        return NO;
    }
}
-(void)uploadData{
    NSArray *picArray = @[@"bmp",@"jpg",@"jpeg",@"png",@"gif",@"arw", @"mrw", @"erf", @"raf",@"cr2", @"nrw", @"nef", @"orf", @"rw2", @"pef", @"srf", @"dcr", @"kdc", @"dng",@"psd"];
    VULFileObjectModel *model = self.selectModelArray[0];
    self.upNewModel  = model;

if ([picArray containsObject:model.fileType]) {

    
    VULActionSheetView *add = [[VULActionSheetView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(47*3)+K_BottomBar_Height) actionTitle:@[KLanguage(@"拍摄"),KLanguage(@"图片"),KLanguage(@"本地文件")] isCenter:YES];
    zhPopupController *popup = [[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
    popup.layoutType = zhPopupLayoutTypeBottom;
    popup.presentationStyle = zhPopupSlideStyleFromBottom;
    popup.maskAlpha = 0.35;
    [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
    
    add.confirmBook = ^(NSInteger index) {
        NSString *title =@[KLanguage(@"拍摄"),KLanguage(@"图片"),KLanguage(@"本地文件")][index];
        if ([title isEqualToString:KLanguage(@"拍摄")]) {
            [popup dismiss];
            [self takephotos];
            return;
        }
        if ([title isEqualToString:KLanguage(@"图片")]) {
            [popup dismiss];
            [_imagePicker setImagePickerCofing:^(TZImagePickerController *_Nonnull pickerController) {
                pickerController.maxImagesCount = 1;
                pickerController.allowPickingVideo = NO;
            }];
            [self.imagePicker.selectedPhotos removeAllObjects];
            [self.imagePicker.selectedAssets removeAllObjects];
            [self.imagePicker pushTZImagePickerController];
            return;
        }
        if ([title isEqualToString:KLanguage(@"本地文件")]) {
            [popup dismiss];
            if (@available(iOS 11, *)) {
                [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAlways;
            }
            self.documentPickerView = [[UIDocumentPickerViewController alloc] initWithDocumentTypes:@[@"com.compuserve.gif",@"public.png",@"public.jpeg"] inMode:UIDocumentPickerModeOpen];
            self.documentPickerView.delegate = self;
            self.documentPickerView.modalPresentationStyle = UIModalPresentationFullScreen;
            [self presentViewController:       self.documentPickerView animated:YES completion:nil];
        }
        

    };
   
    return;
}

    NSArray *videoArray = @[@"mp4",@"rm",@"rmvb",@"3gp",@"mov",@"m4v",@"wmv",@"asf",@"asx",@"avi",@"dat",@"mkv",@"flv",@"vob",@"webm",@"mpg"];
    if ([videoArray containsObject:model.fileType]) {
        
        VULActionSheetView *add = [[VULActionSheetView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, fontAuto(47*3)+K_BottomBar_Height) actionTitle:@[KLanguage(@"拍摄"),KLanguage(@"视频"),KLanguage(@"本地文件")] isCenter:YES];
        zhPopupController *popup = [[zhPopupController alloc] initWithView:add size:CGSizeMake(VULSCREEN_WIDTH, add.height)];
        popup.layoutType = zhPopupLayoutTypeBottom;
        popup.presentationStyle = zhPopupSlideStyleFromBottom;
        popup.maskAlpha = 0.35;
        [popup showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
        
        add.confirmBook = ^(NSInteger index) {
            NSString *title =@[KLanguage(@"拍摄"),KLanguage(@"视频"),KLanguage(@"本地文件")][index];
            if ([title isEqualToString:KLanguage(@"拍摄")]) {
                [popup dismiss];
                [self.imagePicker.selectedPhotos removeAllObjects];
                [self.imagePicker.selectedAssets removeAllObjects];
                self.imagePicker.imagePickerVc.mediaTypes = [NSArray arrayWithObjects: @"public.movie", nil];
                self.imagePicker.imagePickerVc.videoQuality = UIImagePickerControllerQualityTypeMedium;
                self.imagePicker.imagePickerVc.videoMaximumDuration = 90.0;
                [self.imagePicker takePhoto];
                return;
            }
            if ([title isEqualToString:KLanguage(@"视频")]) {
                [popup dismiss];
              
                [self.videoPicker.selectedPhotos removeAllObjects];
                [self.videoPicker.selectedAssets removeAllObjects];
                [self.videoPicker pushTZImagePickerController];
                return;
            }
            if ([title isEqualToString:KLanguage(@"本地文件")]) {
                [popup dismiss];
                if (@available(iOS 11, *)) {
                    [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAlways;
                }
                self.documentPickerView = [[UIDocumentPickerViewController alloc] initWithDocumentTypes:@[@"public.avi"] inMode:UIDocumentPickerModeOpen];
                self.documentPickerView.delegate = self;
                self.documentPickerView.modalPresentationStyle = UIModalPresentationFullScreen;
                [self presentViewController:       self.documentPickerView animated:YES completion:nil];
                return;
            }
            

        };
       
        return;
     

    }

    NSArray *type = @[@"public.item"];

    if ([model.fileType isEqualToString:@"pdf"]) {
        type = @[@"com.adobe.pdf"];
    }else if([model.fileType isEqualToString:@"ppt"] || [model.fileType isEqualToString:@"pptx"]) {
        type = @[@"com.microsoft.powerpoint.​ppt" ,@"org.openxmlformats.presentationml.presentation"];
    }
    else if([model.fileType isEqualToString:@"doc"] || [model.fileType isEqualToString:@"docx"]) {
        type = @[@"com.microsoft.word.doc" ,@"org.openxmlformats.wordprocessingml.document"];

    }else if([model.fileType isEqualToString:@"xls"] ||  [model.fileType isEqualToString:@"xlsx"]) {
        type = @[@"com.microsoft.excel.xls" ,@"org.openxmlformats.spreadsheetml.sheet"];

    }
   
    if (@available(iOS 11, *)) {
        [UIScrollView appearance].contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentAlways;
    }
    self.documentPickerView = [[UIDocumentPickerViewController alloc] initWithDocumentTypes:type inMode:UIDocumentPickerModeOpen];
    self.documentPickerView.delegate = self;
    self.documentPickerView.modalPresentationStyle = UIModalPresentationFullScreen;
    [self presentViewController:       self.documentPickerView animated:YES completion:nil];

}
- (TZImagePickerUtil *)videoPicker {
    if (!_videoPicker) {
        _videoPicker = [TZImagePickerUtil new];
        WeakSelf(self)
        [_videoPicker setSelectedBolck:^{
           
            PHAsset *asset = [weakself.videoPicker.selectedAssets objectAtIndex:0];
            [[TZImageManager manager] getVideoOutputPathWithAsset:asset success:^(NSString *outputPath) {
                if (!outputPath) {
                    return;
                }
                if (  self.upNewModel) {

                    [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:outputPath]
                                               sourceID:self.upNewModel.parentID pathId:  self.upNewModel.sourceID isPic:NO  success:^(bool sucess) {
                        if (sucess) {
                            self.currentPage =1;
                            [self requestList];
                        }

                    }];
                }else{
                [ChunkUploader writeDataWithModel:[NSURL fileURLWithPath:outputPath]  sourceID:self.sourceID isPic:NO  success:^(bool sucess) {
                    if (sucess) {
                        self.currentPage =1;
                        [self requestList];
                    }

                }];
                }
                
                
            } failure:^(NSString *errorMessage, NSError *error) {
              
            }];
        }];
        [_videoPicker setImagePickerCofing:^(TZImagePickerController *_Nonnull pickerController) {
            pickerController.maxImagesCount = 1;
            pickerController.allowTakeVideo = NO;
            pickerController.allowPickingVideo = YES;
            pickerController.allowPickingImage = NO;
            pickerController.allowTakePicture = NO;
            pickerController.allowPickingMultipleVideo = YES;
        }];
    }
    return _videoPicker;
}

-(void)dealloc{
//    uploadChangeNotificationCenter
    //移除指定的通知，不然会造成内存泄露
    [[NSNotificationCenter defaultCenter] removeObserver:self name:@"uploadChangeNotificationCenter" object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
//    [[NSNotificationCenter defaultCenter] removeObserver:self name:UIPasteboardChangedNotification object:nil];

    
}
-(UIImageView *)colorImageV{
    if(!_colorImageV){
        _colorImageV = [UIImageView new];
        _colorImageV.image = VULGetImage(@"image_radarHeader");
    }
    return _colorImageV;
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
