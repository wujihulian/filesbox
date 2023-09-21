//
//  YKChatBoxMoreView.m
//  VideoULimit
//
//  Created by yuekewei on 2020/6/22.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "YKChatBoxMoreView.h"

#define topLineH  0.5
#define bottomH  18

@interface YKChatBoxMoreView() <UIScrollViewDelegate,UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@property (nonatomic, strong) UICollectionView *collectionView;

@end

@implementation YKChatBoxMoreView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self addSubview:self.collectionView];
    }
    return self;
}


- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.collectionView.frame = self.bounds;
}

#pragma mark - Public Methods

    - (void)setItems:(NSMutableArray *)items {
    _items = items;
    [self.collectionView reloadData];
}

#pragma mark UICollectionViewDataSource

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    NSInteger count = ceil(self.items.count / 4.0);
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.items.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    UICollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"cell" forIndexPath:indexPath];
    UIImageView *imageView = [cell.contentView viewWithTag:101];
    if (!imageView) {
        imageView = [UIImageView new];
        imageView.contentMode = UIViewContentModeScaleAspectFill;
        imageView.clipsToBounds = YES;
        imageView.tag = 101;
        imageView.layer.borderColor = HEXCOLOR(0xececec).CGColor;
        imageView.layer.borderWidth = 1;
        imageView.layer.masksToBounds = YES;
        imageView.layer.cornerRadius = 3;

        [cell.contentView addSubview:imageView];
        
        [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(0);
            make.centerX.mas_equalTo(cell.contentView.mas_centerX);
            make.size.mas_equalTo(CGSizeMake(65, 65));
        }];
    }
    
    UILabel *label = [cell.contentView viewWithTag:102];
    if (!label) {
        label  = [UILabel new];
        label.font = [UIFont yk_pingFangRegular:FontAuto(14)];
        label.textColor = UIColorHex(#333333);
        label.textAlignment = NSTextAlignmentCenter;
        label.tag = 102;
        [cell.contentView addSubview:label];
        
        [label mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(imageView.mas_bottom).offset(4);
            make.left.right.mas_equalTo(0);
        }];
    }
    
     YKChatBoxMoreItem *item = [self.items objectAtIndex:indexPath.row];
           imageView.image = [UIImage imageNamed:item.imageName];
           label.text = item.title;
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (_delegate && [_delegate respondsToSelector:@selector(chatBoxMoreView:didSelectItem:)]) {
        [_delegate chatBoxMoreView:self didSelectItem:[self.items objectAtIndex:indexPath.row]];
    }
}


#pragma mark - Getter and Setter
- (UICollectionView *)collectionView {
    if (!_collectionView) {
        _collectionView = ({
                        
            CGFloat paddingLeft = AdaptedWidth(25);
            CGFloat paddingRight = AdaptedWidth(25);
            CGFloat itemWidth = 65;
            
            UICollectionViewFlowLayout *layout = [UICollectionViewFlowLayout new];
            layout.scrollDirection = UICollectionViewScrollDirectionVertical;
            layout.itemSize = CGSizeMake(itemWidth, itemWidth + 30);
            layout.minimumLineSpacing = 20;
            layout.minimumInteritemSpacing = (kScreenWidth - paddingLeft - paddingRight - itemWidth * 4) / 3.0;
            layout.sectionInset = UIEdgeInsetsMake(15, paddingLeft, 20, paddingLeft);
            
            UICollectionView *collectionView = [[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
            collectionView.delegate = self;
            collectionView.dataSource = self;
            
            collectionView.backgroundColor = [UIColor clearColor];
            collectionView.scrollEnabled = NO;
            [collectionView registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"cell"];
            collectionView;
        });
    }
    return _collectionView;
}
@end

