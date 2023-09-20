//
//  VULDropDownView.m
//  VideoULimit
//
//  Created by svnlan on 2019/3/15.
//  Copyright © 2019 svnlan. All rights reserved.
//

#import "VULDropDownView.h"
#import "VULDropDownCell.h"
#import "VULDropDownConfig.h"

@interface VULDropDownView ()<UITableViewDelegate,UITableViewDataSource>
/**
 *  table最大高度限制
 *  默认：5 * cellHeight
 */
@property (nonatomic, assign) CGFloat menuMaxHeight;
/**
 *  小三角高度
 *  45°等腰三角形
 */
@property (nonatomic, assign) CGFloat triangleHeight;

/**
 *  相关配置
 */
@property (nonatomic, strong) VULDropDownConfig *zw_menuConfg;

@property (nonatomic, strong) UIView *contentView;

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic, assign) CGRect anchorRect; //箭头位置的rect

@property (nonatomic, assign) NSInteger defaultIndex;
@property (nonatomic, assign) BOOL isNewStyle;

@end

@implementation VULDropDownView

+ (instancetype)pullMenuAnchorView:(UIView *)anchorView
                        titleArray:(NSArray *)titleArray
                        imageArray:(NSArray *)imageArray {
    UIWindow *window = [[[UIApplication sharedApplication] delegate] window];
    VULDropDownView *menuView = [[VULDropDownView alloc] init];
    menuView.frame = [UIScreen mainScreen].bounds;
    [window addSubview:menuView];
    menuView.anchorRect = [anchorView convertRect:anchorView.bounds
                                           toView:window];
    menuView.titleArray = titleArray;
    menuView.imageArray = imageArray;
    return menuView;
}

+ (instancetype)pullMenuAnchorPoint:(CGPoint)anchorPoint
                         titleArray:(NSArray *)titleArray
                         imageArray:(NSArray *)imageArray {
    UIWindow * window = [[[UIApplication sharedApplication] delegate] window];
    VULDropDownView *menuView = [[VULDropDownView alloc] init];
    menuView.frame = [UIScreen mainScreen].bounds;
    [window addSubview:menuView];
    menuView.anchorRect = CGRectMake(anchorPoint.x, anchorPoint.y, 0, 0);
    menuView.titleArray = titleArray;
    menuView.imageArray = imageArray;
    return menuView;
}

+ (instancetype)pullMenuAnchorView:(UIView *)anchorView titleArray:(nullable NSArray *)titleArray defaultIndex:(NSInteger)defaultIndex {
    UIWindow *window = [[[UIApplication sharedApplication] delegate] window];
    VULDropDownView *menuView = [[VULDropDownView alloc] init];
    menuView.frame = [UIScreen mainScreen].bounds;
    [window addSubview:menuView];
    menuView.anchorRect = [anchorView convertRect:anchorView.bounds
                                           toView:window];
    menuView.defaultIndex = defaultIndex;
    menuView.isNewStyle = YES;
    menuView.tableView.backgroundColor = [UIColor blackColor];
    menuView.menuCellHeight = 36;
    menuView.titleArray = titleArray;
    return menuView;
}


- (instancetype)init {
    self = [super init];
    if (self) {
        [self configUI];
    }
    return self;
}

#pragma mark - config
- (void)configUI {
    [self configDefault];
    [self configTable];
}
- (void)configDefault {
    self.triangleHeight = 8;
    self.menuCellHeight = self.menuCellHeight ? self.menuCellHeight : 45;
    self.menuMaxHeight = 10 * self.menuCellHeight;
    self.backgroundColor = [UIColor colorWithWhite:0 alpha:0.4];
}
- (void)configTable {
    [self addSubview:self.contentView];
    [self.tableView registerClass:[VULDropDownCell class]
        forCellReuseIdentifier:@"VULDropDownCell"];
}

#pragma mark- UITableView delegate
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.menuArray.count;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return self.menuCellHeight;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    VULDropDownCell *cell = [tableView dequeueReusableCellWithIdentifier:@"VULDropDownCell"
                                                           forIndexPath:indexPath];
    VULDropDownModel *cellModel = self.menuArray[indexPath.row];
//    cell.zw_menuConfg = self.zw_menuConfg;
    cell.menuModel = cellModel;
//    cell.zwPullMenuStyle = self.zwPullMenuStyle;
    cell.lineColor = kLineColor;
    if (self.menuArray.count > 7) {
        BOOL haveLine = NO;
        if (indexPath.row == 3 || indexPath.row == 6) {
            haveLine = YES;
        }
        cell.isFinalCell = !haveLine;
    } else {
        cell.isFinalCell = indexPath.row == (self.menuArray.count - 1);
    }
    
    if (self.isNewStyle) {
        cell.isFinalCell = YES;
        if (indexPath.row == self.defaultIndex) {
            cell.menuTitleLab.textColor = HEXCOLOR(0xE3911C);
        } else {
            cell.menuTitleLab.textColor = [UIColor whiteColor];
        }
    }
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    //取消点击效果
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (self.blockSelectedMenu) {
        self.blockSelectedMenu(indexPath.row);
    }
    [self animateRemoveView];
}
- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self animateRemoveView];
    if (self.cancelBlockSelected) {
        self.cancelBlockSelected();
    }
}

#pragma mark - actionFunction
- (void)refreshUI {
    [self.contentView removeFromSuperview];
    self.contentView = nil;
    [self addSubview:self.contentView];
    [self drawtableViewFrame];
    [self.tableView reloadData];
}


- (void)drawtableViewFrame {
    CGPoint layerAnchor = CGPointZero;
    CGPoint layerPosition = CGPointZero;
    CGFloat x = CGRectGetMidX(self.anchorRect);
    CGFloat y = 0;
    CGFloat h = self.menuArray.count * self.menuCellHeight;
    CGFloat w = [self cacuateCellWidth];
    //最大高度围栏限制
    if (h > self.menuMaxHeight) {
        h = self.menuMaxHeight;
    }
    //X中点位置：
    //居左：table右偏
    //居右：table左偏
    if (x>CGRectGetMidX(self.bounds)) {
        x = x - 3 * w / 4.f;
        layerAnchor.x = 1;
        layerPosition.x = x+w;
    } else {
        x = x - w / 4.f;
        layerAnchor.x = 0;
        layerPosition.x = x;
    }
    //围栏
    if (x < self.zw_menuConfg.zw_menuBorderMinMargin) {
        x = self.zw_menuConfg.zw_menuBorderMinMargin;
        layerPosition.x = x;
    }
    if (x + w > self.bounds.size.width) {
        x = self.bounds.size.width - w - self.zw_menuConfg.zw_menuBorderMinMargin;
        layerPosition.x = x + w;
    }
    //Y中心位置
    //居上：下拉
    //居下：上拉
    if (CGRectGetMidY(self.anchorRect) < CGRectGetMidY(self.bounds)) {
        y = CGRectGetMaxY(self.anchorRect);
        self.tableView.frame = CGRectMake(0, self.triangleHeight, w, h);
        layerAnchor.y = 0;
        layerPosition.y = y;
    } else {
        y = CGRectGetMinY(self.anchorRect) - self.triangleHeight - h;
        self.tableView.frame = CGRectMake(0, 0, w, h);
        layerAnchor.y = 1;
        layerPosition.y = y + h;
    }
    self.contentView.frame = CGRectMake(x, y, w, h + self.triangleHeight);
    [self drawTriangle];
    //动画锚点
    self.contentView.layer.position = layerPosition;
    self.contentView.layer.anchorPoint = layerAnchor;
}
//三角形
- (void)drawTriangle {
    CGFloat x = CGRectGetMidX(self.anchorRect) - CGRectGetMinX(self.contentView.frame);
    CGFloat y = 0;
    CGPoint p = CGPointZero;
    CGPoint q = CGPointZero;
    //围栏
    if (x < 2 * self.triangleHeight) {
        x = 2 * self.triangleHeight;
    }
    if (x > CGRectGetWidth(self.contentView.bounds) - 2 * self.triangleHeight) {
        x = CGRectGetWidth(self.contentView.bounds) - 2 * self.triangleHeight;
    }
    //Y中心位置
    //居上：下拉
    //居下：上拉
    if (CGRectGetMidY(self.anchorRect) < CGRectGetMidY(self.bounds)) {
        y = 0;
        p = CGPointMake(x + self.triangleHeight, y + self.triangleHeight);
        q = CGPointMake(x - self.triangleHeight, y + self.triangleHeight);
    } else {
        y = CGRectGetHeight(self.contentView.frame);
        p = CGPointMake(x + self.triangleHeight, y - self.triangleHeight);
        q = CGPointMake(x - self.triangleHeight, y - self.triangleHeight);
    }
    CAShapeLayer *triangleLayer = [CAShapeLayer new];
    triangleLayer.frame = self.contentView.bounds;
    triangleLayer.fillColor = self.isNewStyle ? [UIColor blackColor].CGColor : [UIColor whiteColor].CGColor;
    UIBezierPath *bezier = [UIBezierPath bezierPath];
    [bezier moveToPoint:CGPointMake(x, y)];
    [bezier addLineToPoint:p];
    [bezier addLineToPoint:q];
    bezier.lineJoinStyle = kCGLineJoinRound;
    triangleLayer.path = bezier.CGPath;
    [self.contentView.layer addSublayer:triangleLayer];
}






#pragma mark - function
- (void)animateRemoveView{
    [UIView animateWithDuration:0.5 animations:^{
        self.alpha = 0.f;
        self.contentView.transform = CGAffineTransformMakeScale(0.001f, 0.001f);
        self.contentView.alpha = 0.f;
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}
- (CGFloat)cacuateCellWidth{
    __block CGFloat maxTitleWidth = 0;
    [self.menuArray enumerateObjectsUsingBlock:^(VULDropDownModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        CGFloat width = [obj.title sizeWithAttributes:@{NSFontAttributeName:[UIFont systemFontOfSize:self.zw_menuConfg.zw_menuTitleFontSize]}].width;
        if (obj.imageName.length) {
            width = width + self.zw_menuConfg.zw_menuContentMargin + self.zw_menuConfg.zw_menuImageWidth;
        }
        if (width > maxTitleWidth) {
            maxTitleWidth = width;
        }
    }];
    return maxTitleWidth + self.zw_menuConfg.zw_menuContentMargin * 2;
}

- (void)setMenuArray:(NSArray<VULDropDownModel *> *)menuArray {
    _menuArray = menuArray;
    [self refreshUI];
}

- (void)setTitleArray:(NSArray *)titleArray {
    _titleArray = titleArray;
    if (!titleArray.count) return;
    if (self.menuArray.count&&
        self.menuArray.count != titleArray.count) {
        NSLog( @"文字图片数量不匹配！");
        return;
    }
    [self handleMenuModelArray:titleArray];
}

- (void)setImageArray:(NSArray *)imageArray {
    _imageArray = imageArray;
    if (!imageArray.count) return;
    if (self.menuArray.count&&
        self.menuArray.count != imageArray.count) {
        NSLog( @"文字图片数量不匹配！");
        return;
    }
    [self handleMenuModelArray:imageArray];
}

- (void)handleMenuModelArray:(NSArray *)array {
    NSMutableArray *tempArray = [NSMutableArray array];
    [array enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        VULDropDownModel *model  = [[VULDropDownModel alloc] init];
        model.title = self.titleArray[idx];
        model.imageName = self.imageArray[idx];
        [tempArray addObject:model];
    }];
    self.menuArray = tempArray;
}

#pragma mark - layzing getter
- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero
                                               style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.layer.cornerRadius = 5;
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    }
    return _tableView;
}
- (UIView *)contentView {
    if (!_contentView) {
        _contentView = [[UIView alloc] initWithFrame:CGRectZero];
        [_contentView addSubview:self.tableView];
    }
    return _contentView;
}

- (VULDropDownConfig *)zw_menuConfg {
    if (!_zw_menuConfg) {
        _zw_menuConfg = [[VULDropDownConfig alloc] init];
        _zw_menuConfg.zw_menuContentMargin      = MenuContentMargin;
        _zw_menuConfg.zw_menuImageWidth         = MenuImageWidth;
        _zw_menuConfg.zw_menuBorderMinMargin    = MenuBorderMinMargin;
        _zw_menuConfg.zw_menuTitleFontSize      = MenuTitleFontSize;
    }
    return _zw_menuConfg;
}

@end
