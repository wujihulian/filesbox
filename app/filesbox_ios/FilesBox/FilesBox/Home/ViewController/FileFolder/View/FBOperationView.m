//
//  FBOperationView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/22.
//



#import "FBOperationView.h"
#import "VULInputTitleView.h"
#import "VULFileModel.h"
#import "YCMenuView.h"
#import "FBFromTopView.h"
#import "FBFileTagView.h"
#import "FBFileTagView.h"
#import "FBFileListTagView.h"
#import "FBFileTagManagementVC.h"
#import "VULActionSheetView.h"
#import "FBTranView.h"
@interface VULOperationIconCell ()



@end

@implementation VULOperationIconCell

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
       
        [self.contentView addSubview:self.label];
        [self.contentView addSubview:self.iconImageV];
        [self.iconImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_offset(K_BottomBar_Height>0?14:4);
            make.width.height.mas_equalTo(26);
            make.centerX.mas_equalTo(self.contentView.mas_centerX);
        }];
        [self.label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.iconImageV.mas_bottom).offset(0);
            make.left.mas_equalTo(5);
            make.right.mas_equalTo(-5);
            make.height.mas_equalTo(25);
            make.centerX.mas_equalTo(self.contentView.mas_centerX);
        }];
    }
    return self;
}



- (VULLabel *)label {
    if (!_label) {
        _label = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentCenter Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#ffffff) BgColor:nil];
    }
    return _label;
}

- (UIImageView *)iconImageV {
    if (!_iconImageV) {
        _iconImageV = [UIImageView new];
        _iconImageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _iconImageV;
}

@end
@interface FBOperationView ()<UICollectionViewDelegate,UICollectionViewDataSource>

@property (nonatomic, strong) VULButton *allChooseBtn;;
@property (nonatomic, strong) VULButton *cancelBtn;
@property (nonatomic, strong) VULLabel *leftLabel;
@property (nonatomic, strong) UIView *bottomView;
@property (nonatomic, strong) NSArray *titleArr;
@property (nonatomic, strong) NSArray *iconArr;

@end
@implementation FBOperationView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
       
        self.layer.shadowColor = [UIColor blackColor].CGColor;
        self.layer.shadowOpacity = 0.5;
        self.layer.shadowRadius = 10;
        self.layer.shadowOffset = CGSizeMake(0, 0);
        self.backgroundColor =[UIColor whiteColor];
        [self setView];
    }
    return self;
}
-(void)setView{
    [self addSubview:self.collectionView];
    [self addSubview:self.bottomView];
    [self.bottomView addSubview:self.leftLabel];
    [self.bottomView addSubview:self.cancelBtn];
    [self.bottomView addSubview:self.allChooseBtn];
    [self.bottomView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(0);
        make.left.right.mas_equalTo(0);
        make.height.mas_equalTo(25);
    }];
    [self.leftLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(0);
        make.left.mas_equalTo(10);
        make.height.mas_equalTo(25);
    }];
    [self.cancelBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(-10);
        make.height.mas_equalTo(25);
        make.width.mas_equalTo(50);
    }];
    NSString *appLanguage = [[NSUserDefaults standardUserDefaults] objectForKey:@"appLanguage"];
    CGFloat width = 50;
      if ([appLanguage isEqualToString:@"en"]) {
          width =100;
      }
    [self.allChooseBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.cancelBtn.mas_left).offset(-10);
        make.height.mas_equalTo(25);
        make.width.mas_equalTo(width);

    }];
    [self.collectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.bottomView.mas_bottom);;
        make.left.right.mas_equalTo(0);
        make.height.mas_equalTo(55+K_BottomBar_Height);
    }];
}
-(void)setDataArray:(NSMutableArray *)dataArray
{
    self.leftLabel.text = [NSString stringWithFormat:@"%ld/%lu",dataArray.count,(unsigned long)self.allDataArr.count];
    _dataArray = dataArray;
    _titleArr = @[KLanguage(@"复制"),KLanguage(@"剪切"),KLanguage(@"删除"),KLanguage(@"重命名"),KLanguage(@"更多")];
    _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_cut"),VULGetImage(@"icon_operation_del"),VULGetImage(@"icon_operation_rename"),VULGetImage(@"icon_operation_more")];
    BOOL isFile = YES;

    for (VULFileObjectModel *model in self.dataArray) {
        if (model.isFolder.boolValue) {
            isFile  = NO;
        }
    }

    if (self.dataArray.count>1) {
        
        if (!isFile) {
            _titleArr = @[KLanguage(@"复制"),KLanguage(@"剪切"),KLanguage(@"删除"),KLanguage(@"创建压缩包")];
            _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_cut"),VULGetImage(@"icon_operation_del"),VULGetImage(@"icon_operation_zip")];
            if ([self.icon isEqualToString:@"shareLink"]){
                _titleArr = @[KLanguage(@"复制"),KLanguage(@"剪切"),KLanguage(@"删除")];
                _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_cut"),VULGetImage(@"icon_operation_del")];
            }

        }else{
            _titleArr = @[KLanguage(@"复制"),KLanguage(@"剪切"),KLanguage(@"删除"),KLanguage(@"下载"),KLanguage(@"创建压缩包")];
            _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_cut"),VULGetImage(@"icon_operation_del"),VULGetImage(@"icon_operation_download"),VULGetImage(@"icon_operation_zip")];
            if ([self.icon isEqualToString:@"shareLink"] ||  self.isSHow){
                if([self.icon isEqualToString:@"shareLink"]){
                    
                    _titleArr = @[KLanguage(@"复制"),KLanguage(@"剪切"),KLanguage(@"取消分享"),KLanguage(@"删除"),KLanguage(@"下载")];
                    _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_cut"),VULGetImage(@"icon_operation_share"),VULGetImage(@"icon_operation_del"),VULGetImage(@"icon_operation_download")];
                    
                }else{
                    
                    _titleArr = @[KLanguage(@"复制"),KLanguage(@"剪切"),KLanguage(@"删除"),KLanguage(@"下载")];
                    _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_cut"),VULGetImage(@"icon_operation_del"),VULGetImage(@"icon_operation_download")];
                }
            }
        }
       
        
    }else{
        VULFileObjectModel *model  = self.dataArray[0];
//        if (self.isSHow) {
//            _titleArr = @[KLanguage(@"复制"),KLanguage(@"属性"),KLanguage(model.isFav?@"取消收藏":@"收藏")];
//            _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_attribute"),VULGetImage(@"icon_operation_collect")];
//        }
        
        if ([self.icon isEqualToString:@"shareLink"]  ||  (self.isSHow && model.isShare)) {
            if([self.icon isEqualToString:@"shareLink"]){
                _titleArr = @[KLanguage(@"复制"),KLanguage(@"分享"),KLanguage(@"取消分享"),KLanguage(@"属性"),KLanguage(model.isFav?@"取消收藏":@"收藏")];
                _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_share"),VULGetImage(@"icon_operation_share"),VULGetImage(@"icon_operation_attribute"),VULGetImage(@"icon_operation_collect")];
            }else{
                _titleArr = @[KLanguage(@"复制"),KLanguage(@"分享"),KLanguage(@"属性"),KLanguage(model.isFav?@"取消收藏":@"收藏")];
                _iconArr = @[VULGetImage(@"icon_operation_copy"),VULGetImage(@"icon_operation_share"),VULGetImage(@"icon_operation_attribute"),VULGetImage(@"icon_operation_collect")];
            }
            
        }
        
        if(model.oexeContent.length>0){
            NSDictionary *dic = turnStringToDictionary(model.oexeContent);
            NSString *type =  dic[@"type"];
            if([model.fileType isEqualToString:@"oexe"] && [type isEqualToString:@"lnk"]){
                _titleArr = @[KLanguage(@"打开"),KLanguage(@"删除")];
                    _iconArr = @[VULGetImage(@"icon_operation_open"),VULGetImage(@"icon_operation_del")];
            }else{
         
            }

        }
    }
    if ([self.icon isEqualToString:@"recycle"]) {
        _titleArr = @[KLanguage(@"彻底删除"),KLanguage(@"还原")];
        _iconArr = @[VULGetImage(@"icon_operation_del"),VULGetImage(@"icon_operation_reduction")];
    }
    if (_dataArray.count == 0) {
        self.collectionView.userInteractionEnabled = NO;
        _collectionView.backgroundColor = [BtnColor colorWithAlphaComponent:0.3];

    }else{
        self.collectionView.userInteractionEnabled = YES;
        _collectionView.backgroundColor = BtnColor;

    }
    if ([self.icon isEqualToString:@"fav"]) {
        if (self.dataArray.count>1) {
        _titleArr = @[KLanguage(@"取消收藏")];
            _iconArr = @[VULGetImage(@"icon_operation_collect")];
            
        }else{
            _titleArr = @[KLanguage(@"打开"),KLanguage(@"取消收藏"),KLanguage(@"重命名"), KLanguage(@"属性")];
                _iconArr = @[VULGetImage(@"icon_operation_open"),VULGetImage(@"icon_operation_collect"),VULGetImage(@"icon_operation_rename"),VULGetImage(@"icon_operation_attribute")];
        }
    }
  
   
    [self.collectionView reloadData];
    if (self.dataArray.count == self.allDataArr.count) {
        self.allChooseBtn.selected = YES;
    }else{
        self.allChooseBtn.selected = NO;
    }
    
}
#pragma mark -
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    int count = _titleArr.count;
//    if (self.dataArray.count>1) {
//        count =_titleArr.count;
//    }
//    if ([self.icon isEqualToString:@"recycle"]) {
//        count =2;
//    }
    
    return CGSizeMake(VULSCREEN_WIDTH/count, 55+K_BottomBar_Height);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {//{top, left, bottom, right};
    return UIEdgeInsetsMake(0, 0, 0, 0);
}

//item 列间距(纵)
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    return fontAuto(0);
}

//item 行间距(横)
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    return fontAuto(0);
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return  1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.titleArr.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    VULOperationIconCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"VULOperationIconCell" forIndexPath:indexPath];
    cell.label.text =_titleArr[indexPath.row];
    UIImage *img = _iconArr[indexPath.row];;
    
    cell.iconImageV.image  = img;

    if (self.dataArray.count >0) {
        if (isPermissionWithModel(_titleArr[indexPath.row],self.dataArray)) {
            cell.label.textColor = HEXCOLOR(0xffffff);
            cell.backgroundColor = [BtnColor colorWithAlphaComponent:0.3];
            cell.userInteractionEnabled = YES;

        }else{
            cell.label.textColor = HEXCOLOR(0x999999);
            cell.backgroundColor = BtnColor;
            cell.userInteractionEnabled = NO;
        }
    }else{
        cell.label.textColor = HEXCOLOR(0xffffff);
        cell.backgroundColor = [BtnColor colorWithAlphaComponent:0.3];
    }
  

    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView willDisplayCell:(UICollectionViewCell *)cell forItemAtIndexPath:(NSIndexPath *)indexPath {

}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    NSString *title =    _titleArr[indexPath.row];
    
    if ([title isEqualToString:KLanguage(@"更多")]) {
        VULOperationIconCell *cell = (VULOperationIconCell *)[self.collectionView cellForItemAtIndexPath:indexPath];
        NSArray *LineArr = @[@(0),@(1),@(3),@(4)];
        VULFileObjectModel *model = _dataArray[0];
        NSString *zipName =isZIP(model.fileType)?KLanguage(@"解压到..."): KLanguage(@"创建压缩包");
        NSString *icon =isZIP(model.fileType)?@"icon_operation_unZip": @"icon_operation_zip";

//        NSArray   *docMap =@[@"ppt",@"pptx",@"pdf",@"eid",@"docx",@"doc",@"dot",@"rtf",@"txt",@"uot",@"htm",@"wps",@"wpt",@"eis",@"xlsx",@"xltx",@"xls",@"xlt",@"uos",@"dbf",@"csv",@"xml",@"et",@"ett",@"pptx",@"potx",@"ppt",@"pot",@"ppsx",@"pps",@"dps",@"dpt",@"uop"];
        
        NSArray *titleArr = @[KLanguage(@"打开"), KLanguage(@"下载"), KLanguage(@"分享"),KLanguage(model.sort>0?@"取消置顶": @"置顶"), KLanguage(model.isFav?@"取消收藏":@"收藏"), KLanguage(@"标签"),KLanguage(@"上传新版本"),zipName, KLanguage(@"属性")];
        NSArray *imageArr = @[@"icon_operation_open", @"icon_operation_download", @"icon_operation_share",@"icon_operation_stick", @"icon_operation_collect",@"icon_operation_tag",@"icon_opersion_upload",icon,@"icon_operation_attribute"];
//        if ([docMap containsObject:model.fileType]) {
//            LineArr = @[@(0),@(1),@(2),@(4),@(5)];
//            titleArr = @[KLanguage(@"打开"),KLanguage(@"编辑"), KLanguage(@"下载"), KLanguage(@"分享"),KLanguage(model.sort>0?@"取消置顶": @"置顶"), KLanguage(model.isFav?@"取消收藏":@"收藏"), KLanguage(@"标签"),KLanguage(@"上传新版本"),zipName, KLanguage(@"属性")];
//            imageArr = @[@"icon_operation_open",@"icon_edit", @"icon_operation_download", @"icon_operation_share",@"icon_operation_stick", @"icon_operation_collect",@"icon_operation_tag",@"icon_opersion_upload",icon,@"icon_operation_attribute"];
//        }
        
        if (model.isFolder.boolValue || model.oexeIsFolder.boolValue) { //是文件夹
            LineArr = @[@(0),@(2),@(3)];
            titleArr = @[KLanguage(@"打开"), KLanguage(@"分享"),KLanguage(model.sort>0?@"取消置顶": @"置顶"),  KLanguage(model.isFav?@"取消收藏":@"收藏"), KLanguage(@"标签"),zipName, KLanguage(@"属性")];
          imageArr = @[@"icon_operation_open", @"icon_operation_share",@"icon_operation_stick", @"icon_operation_collect",@"icon_operation_tag",icon,@"icon_operation_attribute"];
        }else{
            
            titleArr = @[KLanguage(@"打开"), KLanguage(@"下载"), KLanguage(@"分享"),KLanguage(model.sort>0?@"取消置顶": @"置顶"), KLanguage(model.isFav?@"取消收藏":@"收藏"), KLanguage(@"标签"),KLanguage(@"上传新版本"),zipName, KLanguage(@"文件查重"), KLanguage(@"文件名查重"), KLanguage(@"属性")];
            imageArr = @[@"icon_operation_open", @"icon_operation_download", @"icon_operation_share",@"icon_operation_stick", @"icon_operation_collect",@"icon_operation_tag",@"icon_opersion_upload",icon,@"icon_operaition_file",@"icon_operaition_filename",@"icon_operation_attribute"];
            LineArr = @[@(0),@(2),@(3),@(8)];
            NSArray *arr = @[@"jpg",@"png",@"doc",@"docx",@"ppt",@"pptx",@"pdf"];
            
            if([arr containsObject:model.fileType]){
                titleArr = @[KLanguage(@"打开"), KLanguage(@"下载"), KLanguage(@"分享"),KLanguage(model.sort>0?@"取消置顶": @"置顶"), KLanguage(model.isFav?@"取消收藏":@"收藏"), KLanguage(@"标签"),KLanguage(@"上传新版本"),KLanguage(@"格式转换"),zipName, KLanguage(@"文件查重"), KLanguage(@"文件名查重"), KLanguage(@"属性")];
                imageArr = @[@"icon_operation_open", @"icon_operation_download", @"icon_operation_share",@"icon_operation_stick", @"icon_operation_collect",@"icon_operation_tag",@"icon_opersion_upload",@"icon_operation_trun",icon,@"icon_operaition_file",@"icon_operaition_filename",@"icon_operation_attribute"];
                LineArr = @[@(0),@(2),@(4),@(9)];
            }
  
        }
        NSMutableArray *titleArr1 = [NSMutableArray arrayWithArray:titleArr];
        NSMutableArray *imageArr1 = [NSMutableArray arrayWithArray:imageArr];
        NSMutableArray *LineArr1 = [NSMutableArray arrayWithArray:LineArr];
        if(model.oexeContent.length>0){
            
            NSDictionary *dic = turnStringToDictionary(model.oexeContent);
            NSString *type =  dic[@"type"];
            if([model.fileType isEqualToString:@"oexe"] && [type isEqualToString:@"lnk"]){
            
            }else{
                [titleArr1 addObject:KLanguage(@"发送到桌面快捷方式")];
                [titleArr1 addObject:KLanguage(@"创建快捷方式")];
                [imageArr1 addObject:@"icon_operaition_desktop"];
                [imageArr1 addObject:@"icon_operation_link"];
                [LineArr1 addObject:@(titleArr.count)];
            }

        }else{
            [titleArr1 addObject:KLanguage(@"发送到桌面快捷方式")];
            [titleArr1 addObject:KLanguage(@"创建快捷方式")];
            [imageArr1 addObject:@"icon_operaition_desktop"];
            [imageArr1 addObject:@"icon_operation_link"];
            [LineArr1 addObject:@(titleArr.count)];
        }
        
    
        
        if (self.dataArray.count>1) {
            titleArr = @[KLanguage(@"创建压缩包")];
            imageArr = @[@"icon_operation_zip"];
            [titleArr1 removeAllObjects];
            [imageArr1 removeAllObjects];
            [LineArr1 removeAllObjects];
            [titleArr1 addObjectsFromArray:titleArr];
            [imageArr1 addObjectsFromArray:imageArr];

        }
        NSInteger count = titleArr1.count;
        if(titleArr1.count>=9){
            count =9;
        }
        
        FBFromTopView *top = [[FBFromTopView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, count*BtnCell+K_BottomBar_Height)];
        top.dataArray = [NSMutableArray arrayWithArray:self.dataArray];
        top.index =0;
        top.isSHow = self.isSHow;
        top.lineArr = [NSMutableArray arrayWithArray: LineArr1];
        top.titleArr = titleArr1;
        top.iconArr = imageArr1;
        zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
        popup2.layoutType = zhPopupLayoutTypeBottom;
        popup2.presentationStyle = zhPopupSlideStyleFromBottom;
        popup2.maskAlpha = 0.35;
        top.clickViewBlock = ^(NSString * _Nonnull title) {
            [popup2 dismiss];
            
            if ([title isEqualToString: KLanguage(@"标签")]) {
                [self getTagList];
                return;
            }
        
            if ([title isEqualToString: KLanguage(@"创建压缩包")]) {
                [self getGreateZip];
                return;
            }
            if ([title isEqualToString: KLanguage(@"解压到...")]) {
                [self getUnzip];
                return;
            }
            if ([title isEqualToString: KLanguage(@"格式转换")]) {
                [self getTrun];
                return;
            }
       
            if (self.selectOperationWithTitle) {
                self.selectOperationWithTitle(title);
            }
        };
        [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
//        NSMutableArray *array = [[NSMutableArray alloc] init];
//        for (NSInteger i = 0; i < titleArr.count; i++) {
//            YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:VULGetImage(imageArr[i])  handler:^(YCMenuAction *action) {
//                if (self.selectOperationWithTitle) {
//                    self.selectOperationWithTitle(action.title);
//                }
//            }];
//            [array addObject:action];
//        }
//
//        YCMenuView *menuView = [YCMenuView menuWithActions:array width:120 relyonView:cell];
//        menuView.menuColor = [UIColor whiteColor];
//        menuView.separatorColor = [UIColor redColor];
//        menuView.maxDisplayCount = 20;
//        //menuView.offset = 150;
//        menuView.textColor = UIColorHex(333333);
//        menuView.textFont = [UIFont yk_pingFangRegular:14];
//        menuView.menuCellHeight = 35;
//        menuView.dismissOnselected = YES;
//        menuView.dismissOnTouchOutside = YES;
//        menuView.backgroundColor = [UIColor whiteColor];
//        [menuView show];
    }else{
        
        if ([title isEqualToString: KLanguage(@"创建压缩包")]) {
            [self getGreateZip];
            return;
        }
        if (self.selectOperationWithTitle) {
            self.selectOperationWithTitle(title);
        }
    }
   



}
-(void)getTrun{
    
    FBTranView *top = [[FBTranView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH*0.9, VULSCREEN_HEIGHT)];
    VULFileObjectModel *model = _dataArray[0];
    top.model = model;
   
    zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH*0.9, top.height)];
    popup2.layoutType = zhPopupLayoutTypeCenter;
    popup2.presentationStyle = zhPopupSlideStyleFromRight;
    popup2.maskAlpha = 0.35;
    top.changeType = ^(NSString * _Nonnull type) {
        if(type.length == 0){
            [popup2 dismiss];
            return;
        }
        if(self.changeType){
            self.changeType(type);
        }
        [popup2 dismiss];
    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
-(void)getUnzip{
    NSArray *titleArr  = @[KLanguage(@"解压到当前"),KLanguage(@"解压到文件夹"), KLanguage(@"解压到...")];
    NSArray *imageArr = @[@"icon_operation_unZip",@"icon_operation_unZip",@"icon_operation_unZip"];

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
        
     
        if (self.selectOperationWithTitle) {
            self.selectOperationWithTitle(title);
        }
    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
-(void)getGreateZip{
    NSArray *titleArr  = @[KLanguage(@"压缩"), KLanguage(@"打包")];
    NSArray *imageArr = @[@"icon_operation_zip",@"icon_operation_zip"];

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
        
     
        if (self.selectOperationWithTitle) {
            self.selectOperationWithTitle(title);
        }
    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
-(void)getTagList{
//    if (self.tagList.count>0) {
//        [self showList];
//        return;
//    }
    
    [self.tagList  removeAllObjects];
    [VULBaseRequest requestWithUrl:@"/api/disk/tag/list" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        NSString *success =request.responseObject[@"success"];
        if (success.boolValue) {
            //TODO: 获取登录信息
            NSArray *list = request.data;
            for (NSDictionary *dic in list) {
                FBTagModel *model = [FBTagModel modelWithDictionary:dic];
                [self.tagList addObject:model];
            }
            [self showList];
        
        }
    }];
}
-(void)showList{
    FBFileTagView *bossView =  [[FBFileTagView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth , 0)];
    for (FBTagModel *model in self.tagList ) {
        [bossView addTag:model];
    }
    VULFileObjectModel *model = _dataArray[0];
    FBFileListTagView *top = [[FBFileListTagView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, bossView.tagCollectionView.contentSize.height+K_BottomBar_Height+60)];
     top.model =model;
      top.tagList = [NSMutableArray arrayWithArray:self.tagList];
   
      zhPopupController *popup2 =[[zhPopupController alloc] initWithView:top size:CGSizeMake(VULSCREEN_WIDTH, top.height)];
      popup2.layoutType = zhPopupLayoutTypeBottom;
      popup2.presentationStyle = zhPopupSlideStyleFromBottom;
      popup2.maskAlpha = 0.35;
      top.selectCollectType = ^(NSString * _Nonnull block, NSString * _Nonnull tag) {
          [popup2 dismiss];
        if(self.selectCollectType){
            self.selectCollectType(block,tag);
        }
        
     };
    top.gotoTagManageMent = ^{
        [popup2 dismiss];
        if (self.gotoTagManageMent) {
            self.gotoTagManageMent();
        }
    

    };
    [popup2 showInView:[UIApplication sharedApplication].keyWindow duration:0.25 delay:0 options:UIViewAnimationOptionCurveLinear bounced:NO completion:nil];
}
#pragma mark -setting

-(NSMutableArray *)tagList{
    if (!_tagList) {
        _tagList = [NSMutableArray array];
    }
    return _tagList;
}
- (UICollectionView *)collectionView {
    UICollectionViewFlowLayout *layout = [[UICollectionViewFlowLayout alloc] init];
    if (!_collectionView) {
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 0, VULSCREEN_WIDTH, 40) collectionViewLayout:layout];
        //_collectionView.showsHorizontalScrollIndicator = NO;
        //_collectionView.alwaysBounceVertical = YES;
        _collectionView.showsHorizontalScrollIndicator = NO;
        _collectionView.alwaysBounceVertical = NO;
        //_collectionView.alwaysBounceHorizontal = NO;
        if (@available(iOS 11.0, *)) {
            _collectionView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
        }
        _collectionView.backgroundColor = BtnColor;
        [_collectionView registerClass:[VULOperationIconCell class] forCellWithReuseIdentifier:@"VULOperationIconCell"];
        _collectionView.delegate = self;
        _collectionView.dataSource = self;
    }
    return _collectionView;
}


-(VULButton *)allChooseBtn{
    if (!_allChooseBtn) {
        _allChooseBtn = [VULButton new];
        [_allChooseBtn setTitle:KLanguage(@"全选") forState:UIControlStateNormal];
        [_allChooseBtn setTitle:KLanguage(@"全不选") forState:UIControlStateSelected];

        [_allChooseBtn setTitleColor:BtnColor forState:UIControlStateNormal];
        _allChooseBtn.titleLabel.font = [UIFont yk_pingFangRegular:14];
        [_allChooseBtn addTarget:self action:@selector(clickAllBtn:) forControlEvents:UIControlEventTouchUpInside];

    }
    return _allChooseBtn;
}
-(VULButton *)cancelBtn{
    if (!_cancelBtn) {
        _cancelBtn = [VULButton new];
        [_cancelBtn setTitle:KLanguage(@"取消") forState:UIControlStateNormal];
        [_cancelBtn setTitleColor:BtnColor forState:UIControlStateNormal];
        _cancelBtn.titleLabel.font = [UIFont yk_pingFangRegular:14];
        [_cancelBtn addTarget:self action:@selector(clickCancelBtn) forControlEvents:UIControlEventTouchUpInside];

    }
    return _cancelBtn;
}
-(void)clickCancelBtn{
    if (self.dismisssView) {
        self.dismisssView();
    }
}
-(void)clickAllBtn:(VULButton *)sender{
    sender.selected = !sender.selected;
    if (self.allSelectModel) {
        self.allSelectModel( sender.selected );
    }
}
- (VULLabel *)leftLabel {
    if (!_leftLabel) {
        _leftLabel =  [VULLabel getLabelWithFrame:CGRectZero Text:@"添加选项" TxtAlignment:0 Font:[UIFont yk_pingFangRegular:14] Color:BtnColor BgColor:nil];
    }
    return _leftLabel;
}

-(UIView *)bottomView{
    if (!_bottomView) {
        _bottomView = [UIView new];
    }
    return _bottomView;
}



@end
