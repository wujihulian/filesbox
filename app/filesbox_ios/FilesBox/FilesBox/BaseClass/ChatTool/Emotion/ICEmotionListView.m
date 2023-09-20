//
//  ICEmotionListView.m


#import "ICEmotionListView.h"
#import "YKChatPrefixHeader.h"
#import "XZEmotion.h"
#import "ICChatBoxFaceView.h"

@interface YKEmotionCell : UICollectionViewCell

@property (nonatomic, strong) XZEmotion *emotion;
@property (nonatomic, strong) YYAnimatedImageView *emotionImgView;
@property (nonatomic, assign) BOOL isDelete;
@end

@implementation YKEmotionCell

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.emotionImgView];
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.emotionImgView.center = CGPointMake(self.width / 2, self.height / 2);
}

- (void)setEmotion:(XZEmotion *)emotion {
    _emotion = emotion;
    [self updateContent];
}

- (void)setIsDelete:(BOOL)isDelete {
    if (_isDelete == isDelete) return;
    _isDelete = isDelete;
    [self updateContent];
}

- (void)updateContent {
    self.emotionImgView.image = nil;
    if (self.isDelete) {
        [self.emotionImgView setImage:[UIImage imageNamed:@"emotion_delete"]];
    }
    else {
        if (self.emotion.face_name) {
            NSBundle *imageBundle =  [NSBundle bundleWithPath:[[NSBundle mainBundle] pathForResource:@"Emotion" ofType:@"bundle"]];
            NSArray *exts = @[@"png", @"jpeg", @"jpg", @"gif", @"webp", @"apng"];
            NSString *path = @"";
            for (NSString *type in exts) {
                path = [imageBundle pathForScaledResource:[self.emotion.face_name substringWithRange:NSMakeRange(1, self.emotion.face_name.length - 2)] ofType:type];
                if (path) {
                    break;
                }
            }
            YYImage *image = [YYImage imageWithContentsOfFile:path];
            [self.emotionImgView setImage:image];
        }
    }
}

#pragma mark - Lazy
- (YYAnimatedImageView *)emotionImgView {
    if (!_emotionImgView) {
        _emotionImgView = [YYAnimatedImageView new];
        _emotionImgView.contentMode = UIViewContentModeScaleAspectFit;
        _emotionImgView.width = _emotionImgView.height = 24;
    }
    return _emotionImgView;
}
@end




@interface ICEmotionListView ()<UIScrollViewDelegate,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@property (nonatomic, strong)UIView *topLine;
@property (nonatomic, strong)UIPageControl *pageControl;
@property (nonatomic, strong) UICollectionView *collectionView;

@end

@implementation ICEmotionListView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.topLine];
        [self addSubview:self.collectionView];
        [self addSubview:self.pageControl];
    }
    return self;
}


#pragma mark - Priate

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.collectionView.frame = CGRectMake(0, 0, self.width, kOneEmoticonHeight * 3);
    self.pageControl.frame = CGRectMake(0, self.collectionView.bottom + 10, self.width, 10);
}

- (void)setEmotions:(NSArray *)emotions {
    _emotions = emotions;
    
    self.pageControl.numberOfPages = ceil(self.emotions.count / (float)kOnePageCount);
    [self.collectionView reloadData];
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    double pageNum                = scrollView.contentOffset.x/scrollView.width;
    self.pageControl.currentPage  = (int)(pageNum+0.5);
}

#pragma mark UICollectionViewDataSource

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    NSInteger count = ceil(self.emotions.count / (float)kOnePageCount);
    return count;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return kOnePageCount + 1;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    YKEmotionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
    
    if (indexPath.row == kOnePageCount) {
        cell.isDelete = YES;
        cell.emotion = nil;
    }
    else {
        cell.isDelete = NO;
        
        NSUInteger page = indexPath.section;
        NSUInteger index = page * kOnePageCount + indexPath.row;
        
        // transpose line/row
        NSUInteger ip = index / kOnePageCount;
        NSUInteger ii = index % kOnePageCount;
        NSUInteger reIndex = (ii % 3) * 7 + (ii / 3);
        index = reIndex + ip * kOnePageCount;
        
        if (index < self.emotions.count) {
            cell.emotion = [self.emotions objectAtIndex:index];
        }
        else {
            cell.emotion = nil;
        }
    }
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row == kOnePageCount) {
        [[NSNotificationCenter defaultCenter] postNotificationName:GXEmotionDidDeleteNotification object:nil];
    }
    else {
        
        NSUInteger page = indexPath.section;
        NSUInteger index = page * kOnePageCount + indexPath.row;
        
        // transpose line/row
        NSUInteger ip = index / kOnePageCount;
        NSUInteger ii = index % kOnePageCount;
        NSUInteger reIndex = (ii % 3) * 7 + (ii / 3);
        index = reIndex + ip * kOnePageCount;
        
        if (index < self.emotions.count) {
            NSMutableDictionary *userInfo = [NSMutableDictionary dictionary];
            userInfo[GXSelectEmotionKey]  = [self.emotions objectAtIndex:index];
            [[NSNotificationCenter defaultCenter] postNotificationName:GXEmotionDidSelectNotification object:nil userInfo:userInfo];
        }
    }
}
#pragma mark - Getter and Setter

- (UIPageControl *)pageControl {
    if (!_pageControl) {
        _pageControl = [[UIPageControl alloc] init];
        _pageControl.currentPageIndicatorTintColor = [UIColor grayColor];
        _pageControl.pageIndicatorTintColor = [UIColor lightGrayColor];
        _pageControl.userInteractionEnabled = NO;
    }
    return _pageControl;
}

- (UIView *)topLine {
    if (!_topLine) {
        UIView * topLine = [[UIView alloc] initWithFrame:CGRectMake(0, 0, k_CH_ScreenWidth,1 / [UIScreen mainScreen].scale)];
        topLine.backgroundColor = IColor(188.0, 188.0, 188.0);
        topLine.hidden = YES;
        _topLine = topLine;
    }
    return _topLine;
}

- (UICollectionView *)collectionView {
    if (!_collectionView) {
        _collectionView = ({
            
            CGFloat itemWidth = (kScreenWidth - 10 * 2) / 7.0;
            itemWidth = CGFloatPixelRound(itemWidth);
            CGFloat padding = (kScreenWidth - 7 * itemWidth) / 2.0;
            CGFloat paddingLeft = CGFloatPixelRound(padding);
            CGFloat paddingRight = kScreenWidth - paddingLeft - itemWidth * 7;
            
            UICollectionViewFlowLayout *layout = [UICollectionViewFlowLayout new];
            layout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
            layout.itemSize = CGSizeMake(itemWidth, 50);
            layout.minimumLineSpacing = 0;
            layout.minimumInteritemSpacing = 0;
            layout.sectionInset = UIEdgeInsetsMake(0, paddingLeft, 0, paddingRight);
            
            UICollectionView *collectionView = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
            collectionView.delegate = self;
            collectionView.dataSource = self;
            
            collectionView.backgroundColor = [UIColor clearColor];
            collectionView.backgroundView = [UIView new];
            collectionView.pagingEnabled = YES;
            collectionView.showsHorizontalScrollIndicator = NO;
            collectionView.clipsToBounds = NO;
            collectionView.canCancelContentTouches = NO;
            collectionView.multipleTouchEnabled = NO;
            [collectionView registerClass:[YKEmotionCell class] forCellWithReuseIdentifier:@"cell"];
            collectionView;
        });
    }
    return _collectionView;
}

@end
