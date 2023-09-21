//
//  VULDocumentTabbarView.h
//  VideoULimit
//
//  Created by mac on 2020/7/22.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@protocol DocumentTabbarViewDelegate <NSObject>

- (void)documentExpandClick;
- (void)documentRotateClick:(UIButton *)rotateBtn;
- (void)documentNextPageClick:(UIButton *) nextBtn;
- (void)documentLastPageClick:(UIButton *) lastBtn;

@end

@interface VULDocumentTabbarView : UIView

@property (nonatomic, strong) VULButton * expandBtn;
@property (nonatomic, strong) UILabel * pageLabel;
@property (nonatomic, strong) VULButton *leftBtn;
@property (nonatomic, strong) VULButton *rightBtn;
@property (nonatomic, weak) id<DocumentTabbarViewDelegate>delegate;

@end

NS_ASSUME_NONNULL_END
