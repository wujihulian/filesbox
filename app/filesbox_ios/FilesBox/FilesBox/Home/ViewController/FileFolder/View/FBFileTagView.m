//
//  FBFileTagView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import "FBFileTagView.h"

@implementation FBTagModel
@end
@interface TTGCustomBossTagRedView : UIView

@property (nonatomic, strong) UILabel *tagLabel;
@property (nonatomic, strong) UIImageView *imageV;
@property (nonatomic, strong) VULButton *closeButton;
@property (nonatomic, strong) UIView *bgView;
@property (nonatomic, copy) void (^ deleteWithIndex)(NSInteger index);


@property (nonatomic, copy)   NSString *label;
@property (nonatomic, assign) CGRect preAvailableFrame;
@property (nonatomic, assign) CGRect endAvailableFrame;
@property (nonatomic, assign) NSInteger index;
@property (nonatomic, strong) FBTagModel *model;

@property (nonatomic, copy)   void(^frameChangeCallBack)(TTGCustomBossTagRedView *tagView);
@end

@implementation TTGCustomBossTagRedView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.preAvailableFrame = CGRectZero;
        self.endAvailableFrame = CGRectZero;
        [self addSubview:self.bgView];
        [self.bgView addSubview:self.tagLabel];
        [self.bgView addSubview:self.closeButton];
        [self.bgView addSubview:self.imageV];

    }
    return self;
}
-(void)setModel:(FBTagModel *)model{
    _model = model;
    
    if(model.labelId.intValue == -1){
        self.imageV.image = VULGetImage(model.image);
    }else{
        self.imageV.image = getImageWithColor(getColorWithGgb(model.style));

    }
    self.tagLabel.text = model.labelName;
    self.closeButton.hidden = !model.isEdit;
    [self.tagLabel sizeToFit];
    self.imageV.left =fontAuto(8);
    self.bgView .left = fontAuto(6);
    self.bgView .top = fontAuto(6);

    self.imageV.height = fontAuto(12);
    self.imageV.width = fontAuto(12);
    self.tagLabel.left =fontAuto(20);
    self.tagLabel.width += fontAuto(15);
    self.tagLabel.width = MIN(self.tagLabel.width, kScreenWidth - 30 - fontAuto(7) - 8);
    self.tagLabel.height += 10;
    self.tagLabel.top = 0;
    self.bgView.height = self.tagLabel.bottom;
    if(model.isEdit){
        self.closeButton.left =     self.tagLabel.right-5;
        self.closeButton.width = fontAuto(20);
        self.closeButton.height = fontAuto(20);
        self.bgView.width =self.tagLabel.width + fontAuto(7)+fontAuto(20)+fontAuto(12);
        self.closeButton.top = (self.bgView.height-fontAuto(20))/2;
    }else{
        self.bgView.width =self.tagLabel.width + fontAuto(7)+fontAuto(20);
    }
    self.bounds = CGRectMake(0, 0,  self.bgView.width+fontAuto(6),     self.bgView.height+fontAuto(6));
    self.imageV.top = (self.bgView.height-fontAuto(12))/2;
    self.bgView.layer.masksToBounds = YES;
    self.bgView.layer.cornerRadius =    self.bgView.bounds.size.height/2;
  
}

- (void)setFrame:(CGRect)frame {
    [super setFrame:frame];
    self.preAvailableFrame = CGRectZero;
    self.endAvailableFrame = CGRectZero;
    if (self.frameChangeCallBack) {
        self.frameChangeCallBack(self);
    }
}



#pragma mark - Lazy
- (UILabel *)tagLabel {
    if (!_tagLabel) {
        _tagLabel = ({
            UILabel *label = [UILabel new];
            label.font = [UIFont systemFontOfSize:fontAuto(14)];
            label.textAlignment = NSTextAlignmentCenter;
            label.textColor = UIColorHex(#333333);
            label.layer.masksToBounds = YES;
            label.layer.cornerRadius = 3.0f;
//            label.layer.borderWidth = 1.0;
//            label.layer.borderColor = UIColorHex(#83B1FE).CGColor;
            label;
        });
    }
    return _tagLabel;
}

-(UIImageView *)imageV{
    if (!_imageV) {
        _imageV = [UIImageView new];
        _imageV.layer.masksToBounds = YES;
        _imageV.layer.cornerRadius = fontAuto(6);
        _imageV.layer.borderColor = HEXCOLOR(0x7459E3).CGColor;
        _imageV.layer.borderWidth=1;
    }
    return _imageV;
}
- (VULButton *)closeButton {
    if (!_closeButton) {
        _closeButton = [VULButton new];
        [_closeButton setImage:VULGetImage(@"icon_delete_tag") forState:UIControlStateNormal];
        [_closeButton addTarget:self action:@selector(clickCloseButton: ) forControlEvents:UIControlEventTouchUpInside];
    }
    return _closeButton;
}
-(UIView *)bgView{
    if (!_bgView) {
        _bgView = [UIView new];
        _bgView.backgroundColor =HEXCOLOR(0xF7F8FA);
        _bgView.layer.borderColor =HEXCOLOR(0xF7F8FA).CGColor;
        _bgView.layer.borderWidth =1;

    }
    return _bgView;
}
-(void)clickCloseButton:(VULButton *)sender{
    if(self.deleteWithIndex){
        self.deleteWithIndex(self.index -100);
        
    }
}

@end

@interface FBFileTagView ()<TTGTagCollectionViewDelegate, TTGTagCollectionViewDataSource>

@property (strong, nonatomic) NSMutableArray <UIView *> *tagLabels;
@property (nonatomic, strong) UIView *tempMoveTagView;
@property (nonatomic, assign) CGPoint lastLocation;
@property (nonatomic, assign) NSInteger moveIndex;
@end

@implementation FBFileTagView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.tagLabels = [NSMutableArray new];
        [self addSubview:self.tagCollectionView];
        self.tagCollectionView.bounds = self.frame;
        
       
    }
    return self;
}

#pragma mark - Private methods

- (void)reload {
    [_tagCollectionView reload];
}

- (void)addTag:(FBTagModel *)tag {
    [self insertTag:tag atIndex:_tagLabels.count];
}


- (void)addTags:(NSArray <FBTagModel *> *)tags {
    [self insertTags:tags atIndex:_tagLabels.count];
}


- (void)insertTag:(FBTagModel *)tag atIndex:(NSUInteger)index {
    if ([tag isKindOfClass:[FBTagModel class]]) {
        [self insertTags:@[tag] atIndex:index];
    }
}

- (void)insertTags:(NSArray<FBTagModel *> *)tags atIndex:(NSUInteger)index {
    if (![tags isKindOfClass:[NSArray class]] || index > self.tagLabels.count ) {
        return;
    }
    
    
    NSMutableArray *newTagLabels = [NSMutableArray new];
    for (FBTagModel *tagText in tags) {
        TTGCustomBossTagRedView *label = [self tagLabelWithText:tagText];
        [newTagLabels addObject:label];
    }
    [self.tagLabels insertObjects:newTagLabels atIndexes:[NSIndexSet indexSetWithIndexesInRange:NSMakeRange(index, newTagLabels.count)]];
    [self reload];
}

- (void)removeTag:(FBTagModel *)tag {
    if (![tag isKindOfClass:[FBTagModel class]]) {
        return;
    }
    
    NSMutableArray *labelsToRemoved = [NSMutableArray new];
    for (TTGCustomBossTagRedView *label in self.tagLabels) {
        if ([label.tagLabel.text isEqualToString:tag.labelName]) {
            [labelsToRemoved addObject:label];
        }
    }
    [self.tagLabels removeObjectsInArray:labelsToRemoved];
    [self reload];
}

- (void)removeTagAtIndex:(NSUInteger)index {
    if (index >= self.tagLabels.count) {
        return;
    }
    
    [self.tagLabels removeObjectAtIndex:index];
    [self reload];
}

- (void)removeAllTags {
    [self.tagLabels removeAllObjects];
    [self reload];
}

-(void)selectCellWtihIndex:(NSInteger) index{
    TTGCustomBossTagRedView *tagView1 = _tagLabels[index];
    for (int i = 0; i<_tagLabels.count; i++) {
        TTGCustomBossTagRedView *tagView = _tagLabels[i];
        if (i == index) {
            tagView.bgView.layer.borderColor =HEXCOLOR(0x7459E3).CGColor;
            tagView.bgView.backgroundColor = [HEXCOLOR(0xCABAFF) colorWithAlphaComponent:0.2];
            
        }
    }
}
-(void)selectOneCellWtihIndex:(NSInteger) index{
    TTGCustomBossTagRedView *tagView1 = _tagLabels[index];
    for (int i = 0; i<_tagLabels.count; i++) {
        TTGCustomBossTagRedView *tagView = _tagLabels[i];
        if (i == index) {
            tagView.bgView.layer.borderColor =HEXCOLOR(0x7459E3).CGColor;
            tagView.bgView.backgroundColor = [HEXCOLOR(0xCABAFF) colorWithAlphaComponent:0.2];
            
        }else{
            tagView.bgView.layer.borderColor =HEXCOLOR(0xF7F8FA).CGColor;
            tagView.bgView.backgroundColor = [HEXCOLOR(0xF7F8FA) colorWithAlphaComponent:0.2];
        }
    }
}
- (TTGCustomBossTagRedView *)tagLabelWithText:(FBTagModel *)text {
    TTGCustomBossTagRedView *label = [TTGCustomBossTagRedView new];
    label.model = text ;
    return label;
}



#pragma mark - TTGTagCollectionViewDelegate

- (CGSize)tagCollectionView:(TTGTagCollectionView *)tagCollectionView sizeForTagAtIndex:(NSUInteger)index {
    return _tagLabels[index].frame.size;
}

- (void)tagCollectionView:(TTGTagCollectionView *)tagCollectionView didSelectTag:(UIView *)tagView atIndex:(NSUInteger)index {
   
    if (self.selectCollectType) {
        self.selectCollectType(index);
    }
}

- (void)tagCollectionView:(TTGTagCollectionView *)tagCollectionView updateContentSize:(CGSize)contentSize {
        NSLog(@"contentSize =====%@",NSStringFromCGSize(contentSize));
    self.height = contentSize.height;
    self.tagCollectionView.height = contentSize.height;

}

#pragma mark - TTGTagCollectionViewDataSource

- (NSUInteger)numberOfTagsInTagCollectionView:(TTGTagCollectionView *)tagCollectionView {
    return _tagLabels.count;
}

- (UIView *)tagCollectionView:(TTGTagCollectionView *)tagCollectionView tagViewForIndex:(NSUInteger)index {
    TTGCustomBossTagRedView *tagView = _tagLabels[index];
    tagView.index = 100 + index;
    tagView.deleteWithIndex = ^(NSInteger index) {
        if (self.deleteWithIndex) {
            self.deleteWithIndex(index);
        }
    };
  
    MJWeakSelf
    [tagView setFrameChangeCallBack:^(TTGCustomBossTagRedView *tagView) {
        if (tagView.index > 100) {
            TTGCustomBossTagRedView *preTagView = [weakSelf.tagLabels objectAtIndex:tagView.index - 100 - 1];

            CGFloat x = preTagView.right + self.tagCollectionView.horizontalSpacing;
            CGFloat y = preTagView.y;
            CGFloat width = 0;
            CGFloat height = preTagView.height;
            if (tagView.centerY > preTagView.centerY) {
                width = self.tagCollectionView.width - x;
            }
            tagView.preAvailableFrame = CGRectMake(x, y, width, height);

            if (tagView.index - 100 == weakSelf.tagLabels.count - 1) {
                x = tagView.right + self.tagCollectionView.horizontalSpacing;
                width = self.tagCollectionView.width - x;
                tagView.endAvailableFrame = CGRectMake(x, tagView.top, width, tagView.height);
            }
        }
    }];
    return tagView;
}



#pragma mark - Lazy
- (TTGTagCollectionView *)tagCollectionView {
    if (!_tagCollectionView) {
        _tagCollectionView = [[TTGTagCollectionView alloc] initWithFrame:self.bounds];
        _tagCollectionView.delegate = self;
        _tagCollectionView.dataSource = self;
        _tagCollectionView.horizontalSpacing = 0;
        _tagCollectionView.verticalSpacing = 0;
    }
    return _tagCollectionView;
}
@end
