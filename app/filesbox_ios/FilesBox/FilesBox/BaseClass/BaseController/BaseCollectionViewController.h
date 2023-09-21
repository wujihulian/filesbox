//
//  BaseCollectionViewController.h
//  VideoULimit
//
//  Created by yuekewei on 2020/5/11.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface BaseCollectionViewController : VULBaseViewController<UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout>

@property (nonatomic, strong) UICollectionView *collectionView;

@property (nonatomic, assign) NSInteger pageIndex;

- (void)setupRefreshHeader;

- (void)setupRefreshFooter;

- (void)topRefreshing NS_REQUIRES_SUPER;

- (void)bottomRefreshing;

- (void)endRefreshing;

#pragma mark - 重写
- (UICollectionViewFlowLayout *)collectionViewFlowLayout;

@end

NS_ASSUME_NONNULL_END
