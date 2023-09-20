//
//  FBInfoTypeView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/6/15.
//

#import "FBInfoTypeView.h"

#define firstTag     100000
#define secondTag     1000000

@interface FBInfoTypeView()
{
    NSInteger top;
    NSInteger level;
    NSInteger levelTag;
    NSInteger levelTagSecond;
    UIView *secondV;
    UIView *thirdV;
    NSInteger top1;
    UIScrollView *scroller;
    BOOL isFirst;//是否默认
    UIButton *firstAllBtn;
    UIButton *secondAllBtn;
    UIButton *thirdAllBtn;

}
@property (nonatomic, strong) NSMutableArray *listArray;
@property (nonatomic, strong) NSMutableArray *secondConent;
@property (nonatomic, strong) NSMutableArray *thirdConent;
@property (nonatomic, strong) NSMutableArray *firstListArray;
@property (nonatomic, strong) NSMutableArray *secondListArray;
@property (nonatomic, strong) NSMutableArray *thirdListArray;
@end
@implementation FBInfoTypeView
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.userInteractionEnabled = YES;
        self.backgroundColor = [UIColor whiteColor];
        self.listArray = [NSMutableArray array];
        self.firstListArray = [NSMutableArray array];
        self.thirdListArray = [NSMutableArray array];
        self.secondListArray = [NSMutableArray array];
        self.secondConent = [NSMutableArray array];
        self.thirdConent = [NSMutableArray array];
        scroller = [UIScrollView new];
        scroller.frame =CGRectMake(0, 0,  VULSCREEN_WIDTH * 0.8, frame.size.height);
        level = 0;
        top = 0;
        top1 = 0;
        isFirst = YES;
        [self addSubview:scroller];
        NSArray *arr  =    [[NSUserDefaults standardUserDefaults] objectForKey:@"getInfoTypeList"];
        if(arr.count==0){
            [self createInfoType];
        }else{
            for(NSDictionary *dic in arr){
                VULInfoTypeModel *model = [VULInfoTypeModel modelWithDictionary:dic];
                [self.listArray addObject:model];
            }
            [self setView];
        }
    }
    return self;
}

#pragma mark - 获取分类列表

- (void)createInfoType{
    WeakSelf(self);
    [VULBaseRequest requestWithUrl:@"/api/disk/getInfoTypeList" params:@{@"currentPage":@(1),@"fileType":@"folder",@"pageSize":@(500)} requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest *_Nonnull request) {
        if (request.success) {
            NSArray *arr = request.data;
            for(NSDictionary *dic in arr){
                VULInfoTypeModel *model = [VULInfoTypeModel modelWithDictionary:dic];
                [weakself.listArray addObject:model];
            }
            [[NSUserDefaults standardUserDefaults] setObject:arr forKey:@"getInfoTypeList"];
            [self setView];

        }else {
            [self makeToast:request.responseObject[@"message"]];
        }
    
    }];
}


- (void)setView{
    
    CGFloat width = VULSCREEN_WIDTH * 0.7;
    CGFloat remainingW =width;
    int H = 0;
    CGFloat lastWidth =fontAuto(45);
    NSString * firstFL =  [[NSUserDefaults standardUserDefaults] objectForKey:@"firstFL"];
    if (firstFL.length>0) {
        isFirst = NO;
    }
    NSInteger index = -1;
    firstAllBtn = [[UIButton alloc] init];
    [firstAllBtn setTitle:@"全部" forState:UIControlStateNormal];
    firstAllBtn.frame  = CGRectMake(fontAuto(15), fontAuto(20)+kStatusBarHeight, fontAuto(45), fontAuto(16));
    if (firstFL.intValue == -1) {
        [firstAllBtn setTitleColor: BtnColor forState:UIControlStateNormal];
    }else{
        [firstAllBtn setTitleColor: HEXCOLOR(0x333333) forState:UIControlStateNormal];
    }
    firstAllBtn.contentHorizontalAlignment=UIControlContentHorizontalAlignmentLeft;

    [firstAllBtn addTarget:self action:@selector(firstAll:) forControlEvents:UIControlEventTouchUpInside];
    firstAllBtn.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(16)];
    [scroller  addSubview:firstAllBtn];
    BOOL isDefault = NO;
    for (int i = 0; i <_listArray.count; i ++) {
        UIButton *label = [[UIButton alloc] init];
        [label setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
//        label.titleLabel.textColor = HEXCOLOR(0x333333);
        label.userInteractionEnabled = YES;
//        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(click:)];
//        [label addGestureRecognizer:tap];
        [label addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
        [scroller addSubview:label];
        VULInfoTypeModel *mode = _listArray[i];
        [label setTitle:mode.typeName forState:UIControlStateNormal];
        if ([mode.infoTypeID isEqualToString:firstFL] && !isFirst) {
            [label setTitleColor:BtnColor forState:UIControlStateNormal];
            index =i;
            isDefault =YES;
        }

        label.font = [UIFont yk_pingFangRegular:fontAuto(16)];
        label.tag = i;
        CGSize size = [       label.titleLabel.text sizeWithAttributes:@{ NSFontAttributeName:        label.titleLabel.font }];
        label.titleLabel.numberOfLines = 1;
        if (remainingW >size.width) {
            label.frame = CGRectMake(fontAuto(15)+lastWidth, fontAuto(20)+fontAuto(36)*H+kStatusBarHeight +top, size.width, fontAuto(16));
            remainingW = width-fontAuto(15)-lastWidth - size.width;
           
        }else{
            H++;
            lastWidth = 0;
            remainingW = width;
            if(size.width>=width){
                label.frame = CGRectMake(fontAuto(15), fontAuto(20)+fontAuto(36)*H+kStatusBarHeight +top, width-fontAuto(15), fontAuto(16));
            }else{
                label.frame = CGRectMake(fontAuto(15), fontAuto(20)+fontAuto(36)*H+kStatusBarHeight +top, size.width, fontAuto(16));
            }
    
            remainingW = width-fontAuto(15)-lastWidth - label.size.width;
        }
        lastWidth = lastWidth + fontAuto(15)+ size.width;
     
        [self.firstListArray addObject:label];
    }
  
    top =top+ fontAuto(20)+fontAuto(36)*(H+1)+kStatusBarHeight;
    UIView *view = [UIView new];
    view.frame = CGRectMake(0, top, width, 1);
    view.backgroundColor =HEXCOLOR(0xECECEC);
    if (top > self.height) {
       [scroller setContentSize:CGSizeMake(VULSCREEN_WIDTH * 0.8, top +1 +50)];
    }
    [scroller addSubview:view];
    if ( isDefault) {
       [self defaultLabelWtihIndex:index];
    }
}
                                                                            
- (void)viewForChildrenArray:(NSArray *)array{
    if (array == nil || [array isEqual:[NSNull null]]) {
        return;
    }
    if (array.count == 0) {
        return;
    }
    NSString * fnConent;
    CGFloat lastWidth = fontAuto(45);
   
    if (level ==0) {
        if (!isFirst) {
            fnConent =  [[NSUserDefaults standardUserDefaults] objectForKey:@"secondFL"];
        }
        if(fnConent) {
            fnConent = [NSString stringWithFormat:@"%@",fnConent];
        }
        secondAllBtn = [[UIButton alloc] init];
        [secondAllBtn setTitle:@"全部" forState:UIControlStateNormal];
        secondAllBtn.frame  = CGRectMake(fontAuto(15), fontAuto(20), fontAuto(45), fontAuto(16));
        [secondAllBtn setTitleColor: HEXCOLOR(0x333333) forState:UIControlStateNormal];
        secondAllBtn.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(16)];
       secondV = [UIView new];
        secondV.userInteractionEnabled = YES;
        [secondV  addSubview:secondAllBtn];
        secondAllBtn.contentHorizontalAlignment=UIControlContentHorizontalAlignmentLeft;
        [secondAllBtn addTarget:self action:@selector(secondAll:) forControlEvents:UIControlEventTouchUpInside];
        if (fnConent.intValue == -1) {
            [secondAllBtn setTitleColor: BtnColor forState:UIControlStateNormal];
        }else{
            [secondAllBtn setTitleColor: HEXCOLOR(0x333333) forState:UIControlStateNormal];
        }
    }else{
        if (!isFirst) {
         fnConent =  [[NSUserDefaults standardUserDefaults] objectForKey:@"thirdFL"];
        }
        if(fnConent) {
            fnConent = [NSString stringWithFormat:@"%@",fnConent];
        }
        thirdAllBtn = [[UIButton alloc] init];
        [thirdAllBtn setTitle:@"全部" forState:UIControlStateNormal];
        thirdAllBtn.frame  = CGRectMake(fontAuto(15), fontAuto(20), fontAuto(45), fontAuto(16));
        [thirdAllBtn setTitleColor: HEXCOLOR(0x333333) forState:UIControlStateNormal];
        thirdAllBtn.titleLabel.font = [UIFont yk_pingFangRegular:fontAuto(16)];
        thirdV = [UIView new];
        thirdAllBtn.contentHorizontalAlignment=UIControlContentHorizontalAlignmentLeft;
        thirdV.userInteractionEnabled = YES;
        [thirdV  addSubview:thirdAllBtn];
        [thirdAllBtn addTarget:self action:@selector(thirdAll:) forControlEvents:UIControlEventTouchUpInside];
        if (fnConent.intValue == -1) {
            [thirdAllBtn setTitleColor: BtnColor forState:UIControlStateNormal];
        }else{
            [thirdAllBtn setTitleColor: HEXCOLOR(0x333333) forState:UIControlStateNormal];
        }

    }
   
  
    CGFloat width = VULSCREEN_WIDTH * 0.7;
    CGFloat remainingW =width;
    int H = 0;
    NSInteger index = 0;
    BOOL isDefault = NO;
    for (int i = 0; i <array.count; i ++) {
        UIButton *label = [[UIButton alloc] init];
//        label.userInteractionEnabled = YES;
//        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(click:)];
//        [label addGestureRecognizer:tap];
        [label addTarget:self action:@selector(click:) forControlEvents:UIControlEventTouchUpInside];
        [label setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
        NSString *textString;
        if (level == 0) {
            label.tag = i + firstTag;
        }else if(level == 1){
            label.tag = i + secondTag;
        }
        VULInfoTypeModel *mode = array[i];
        [label setTitle:mode.typeName forState:UIControlStateNormal];
        textString = mode.typeName;
        if ([mode.infoTypeID isEqualToString:fnConent] && !isFirst) {
//                label.textColor = HEXCOLOR(0x4F91FF);
            [label setTitleColor:BtnColor forState:UIControlStateNormal];

            index = label.tag;
            isDefault = YES;
        }
        label.font = [UIFont yk_pingFangRegular:fontAuto(16)];
        CGSize size = [self sizeWithText:textString font:[UIFont yk_pingFangRegular:fontAuto(16)] maxSize:CGSizeMake(MAXFLOAT, MAXFLOAT)];
        label.titleLabel.numberOfLines = 1;
        if (remainingW >size.width) {
            label.frame = CGRectMake(fontAuto(15)+lastWidth, fontAuto(20)+fontAuto(36)*H , size.width, fontAuto(16));
            remainingW = width-fontAuto(15)-lastWidth - size.width;
        }else{
            H++;
            lastWidth = 0;
            remainingW = width;
            if(size.width>width){
                label.frame = CGRectMake(fontAuto(15), fontAuto(20)+fontAuto(36)*H, width-fontAuto(15),fontAuto(16));
            }else{
                label.frame = CGRectMake(fontAuto(15), fontAuto(20)+fontAuto(36)*H, size.width, fontAuto(16));
            }
               remainingW = width-fontAuto(15)-lastWidth - label.size.width;
       }
        lastWidth = lastWidth + fontAuto(15)+ size.width;
        label.userInteractionEnabled = YES;
        if (level == 0) {
            [secondV addSubview:label];
            [self.secondListArray addObject:label];
        }else{
            [thirdV addSubview:label];
            [self.thirdListArray addObject:label];
        }
      }
    if (level == 0) {
        secondV.frame = CGRectMake(0, top, width, fontAuto(20)+fontAuto(36)*(H+1)+1);
        UIView *viewLine = [[UIView alloc] init];
        viewLine.frame = CGRectMake(0, secondV.size.height-1, width, 1);
        viewLine.backgroundColor = HEXCOLOR(0xECECEC);
        if (array.count >0) {
            [secondV addSubview:viewLine];
        }
        [scroller addSubview:secondV];
        top1 = top +fontAuto(20)+fontAuto(36)*(H+1)+1;
        if (top1 > self.height) {
            [scroller setContentSize:CGSizeMake(VULSCREEN_WIDTH * 0.8,top1 +50)];
        }
    }else{
        thirdV.frame = CGRectMake(0, top1, width, fontAuto(20)+fontAuto(36)*(H+1)+1);
        UIView *viewLine = [[UIView alloc] init];
        viewLine.frame = CGRectMake(0, thirdV.size.height-1, width, 1);
        viewLine.backgroundColor =HEXCOLOR(0xECECEC);
        if (array.count >0) {
            [thirdV addSubview:viewLine];
        }
        if (top1 +thirdV.size.height > self.height) {
            [scroller setContentSize:CGSizeMake(VULSCREEN_WIDTH * 0.8, top1 +thirdV.size.height +50)];
        }
       
        [scroller addSubview:thirdV];
    }
    if (isDefault) {
        [self defaultLabelWtihIndex:index];
    }

}
- (CGSize)sizeWithText:(NSString *)text font:(UIFont *)font maxSize:(CGSize)maxSize {

    NSDictionary *attrs = @{NSFontAttributeName : font};

    return [text boundingRectWithSize:maxSize options:NSStringDrawingUsesLineFragmentOrigin attributes:attrs context:nil].size;

}
- (void)firstAll:(UIButton *)sender{
    [sender setTitleColor: BtnColor forState:UIControlStateNormal];
    if (_selectBackModel) {
        _selectBackModel(@"");
    }
    if (_selectBlock) {
        _selectBlock(@"");
    }
    for (int i =0; i< self.firstListArray.count; i ++) {
        UIButton *label =  self.firstListArray[i];
//        label.textColor = HEXCOLOR(0x333333);
        
        [label setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
    }
    [[NSUserDefaults standardUserDefaults] setObject:@"-1" forKey:@"firstFL"];
    [secondV removeAllSubviews];
    [thirdV removeAllSubviews];
}
- (void)secondAll:(UIButton*)sender{
    [sender setTitleColor: BtnColor forState:UIControlStateNormal];
    if (_selectBackModel && isFirst) {
       NSString  *fnConent =  [[NSUserDefaults standardUserDefaults] objectForKey:@"firstFL"];
        if(fnConent) {
            fnConent = [NSString stringWithFormat:@"%@",fnConent];
        }else{
            fnConent = @"";
        }
        _selectBackModel(fnConent);
    }
    if (_selectBlock && isFirst) {
       NSString  *fnConent =  [[NSUserDefaults standardUserDefaults] objectForKey:@"firstFL"];
        if(fnConent) {
            fnConent = [NSString stringWithFormat:@"%@",fnConent];
        }else{
            fnConent = @"";
        }
        _selectBlock(fnConent);
    }
    for (int i =0; i< self.secondListArray.count; i ++) {
        UIButton *label =  self.secondListArray[i];
        [label setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
    }
    [[NSUserDefaults standardUserDefaults] setObject:@"-1" forKey:@"secondFL"];
    [thirdV removeAllSubviews];
}
- (void)thirdAll:(UIButton*)sender{
    [sender setTitleColor: BtnColor forState:UIControlStateNormal];
    [[NSUserDefaults standardUserDefaults] setObject:@"-1" forKey:@"thirdFL"];
    if (_selectBackModel) {
       NSString  *fnConent =  [[NSUserDefaults standardUserDefaults] objectForKey:@"secondFL"];
        if(fnConent) {
            fnConent = [NSString stringWithFormat:@"%@",fnConent];
        }else{
            fnConent = @"";
        }
        _selectBackModel(fnConent);
    }
    if (_selectBlock) {
       NSString  *fnConent =  [[NSUserDefaults standardUserDefaults] objectForKey:@"secondFL"];
        if(fnConent) {
            fnConent = [NSString stringWithFormat:@"%@",fnConent];
        }else{
            fnConent = @"";
        }
        _selectBlock(fnConent);
    }
    for (int i =0; i< self.thirdListArray.count; i ++) {
        UIButton *label =  self.thirdListArray[i];
        [label setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];
    }
}
- (void)defaultLabelWtihIndex:(NSInteger)index{
    NSInteger tagF = index;
    if (tagF<firstTag) {
        level = 0;
        [self.secondListArray removeAllObjects];
        [self.thirdListArray removeAllObjects];
    }else if(tagF>=firstTag && tagF<secondTag){
        level =1;
        [self.thirdListArray removeAllObjects];
        tagF = index -firstTag;
    }else if(tagF>=secondTag){
        level =2;
        tagF = index -secondTag;
    }
    [self addView:tagF];
}
- (void)click:(UIButton *)sender{
    NSInteger tagF = sender.tag;
    if (tagF<firstTag) {
        level = 0;
        [firstAllBtn setTitleColor:HEXCOLOR(0x3333333) forState:UIControlStateNormal];
        [self.secondListArray removeAllObjects];
        [self.thirdListArray removeAllObjects];
    }else if(tagF>=firstTag && tagF<secondTag){
        level =1;
        [secondAllBtn setTitleColor:HEXCOLOR(0x3333333) forState:UIControlStateNormal];
        [self.thirdListArray removeAllObjects];
        tagF = sender.tag -firstTag;
    }else if(tagF>=secondTag){
        level =2;
        [thirdAllBtn setTitleColor:HEXCOLOR(0x3333333) forState:UIControlStateNormal];
        tagF = sender.tag -secondTag;
    }
    [self addView:tagF];
    [self changeColorLabel:tagF];
    isFirst = YES;
   
}

- (void)addView:(NSInteger)row{
    NSMutableArray *arr;
    if (level == 0) {
        levelTag = row;
        [secondV removeAllSubviews];
        [thirdV removeAllSubviews];

        VULInfoTypeModel *mode = _listArray[row];
            arr = [NSMutableArray arrayWithArray:mode.children];
            if (arr || arr.count>0) {
                [self viewForChildrenArray:arr];
            }
            if (_selectBackModel && isFirst) {
                [[NSUserDefaults standardUserDefaults] setObject:mode.infoTypeID forKey:@"firstFL"];
                _selectBackModel(mode.infoTypeID);
            }

    }else if(level ==1){
        levelTagSecond = row;
        [thirdV removeAllSubviews];
        VULInfoTypeModel *mode = _listArray[levelTag];
        VULInfoTypeModel *childrenModel= mode.children[row];
          NSMutableArray *dicArr =  [NSMutableArray arrayWithArray:childrenModel.children];
        if (dicArr || dicArr.count>0) {
            [self viewForChildrenArray:dicArr];
        }
        
        if (_selectBackModel && isFirst) {
            [[NSUserDefaults standardUserDefaults] setObject: childrenModel.infoTypeID  forKey:@"secondFL"];

            _selectBackModel(childrenModel.infoTypeID );
        }

    }else{
        VULInfoTypeModel *mode = _listArray[levelTag];
       id changeModel =  mode.children[levelTagSecond];
        VULInfoTypeModel *changeModel1;

        if ([changeModel isKindOfClass:[NSDictionary class]]) {
            NSArray *arr =  changeModel[@"children"];
            NSDictionary *dic =  arr[row];
            changeModel1 = [VULInfoTypeModel modelWithDictionary:dic];

        }else{
            VULInfoTypeModel *changeModel =  mode.children[levelTagSecond];
            id dic =  changeModel.children[row];
            if ([dic isKindOfClass:[NSDictionary class]]) {
                changeModel1 = [VULInfoTypeModel modelWithDictionary:dic];
            }else{
                changeModel1 = changeModel.children[row];
            }
        }

       
//        NSArray *childFour = dic[@"children"];
    if (_selectBackModel && isFirst) {
        [[NSUserDefaults standardUserDefaults] setObject:changeModel1.infoTypeID forKey:@"thirdFL"];
              _selectBackModel(changeModel1.infoTypeID);
          }
    }
}
-(void)changeColorLabel:(NSInteger )row{
    NSMutableArray *array ;
    if (level == 0) {
        array = [NSMutableArray arrayWithArray:self.firstListArray];
    }else if (level == 1) {
        array = [NSMutableArray arrayWithArray:self.secondListArray];
    }else if(level == 2){
        array = [NSMutableArray arrayWithArray:self.thirdListArray];
    }
    for (int i =0; i< array.count; i ++) {
        UIButton *label =  array[i];
        if(i == row){
            [label setTitleColor:BtnColor forState:UIControlStateNormal];
        }else{
            [label setTitleColor:HEXCOLOR(0x333333) forState:UIControlStateNormal];

        }
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
