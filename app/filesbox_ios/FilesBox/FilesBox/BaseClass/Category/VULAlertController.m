//
//  VULAlertController.m
//  VideoULimit
//
//  Created by svnlan on 2018/10/12.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULAlertController.h"


//toast默认展示时间
static NSTimeInterval const VULAlertShowDurationDefault = 1.0f;

#pragma mark -
#pragma mark - I.AlertActionModel
@interface VULAlertActionModel : NSObject

@property (nonatomic, copy) NSString * title;
@property (nonatomic, assign) UIAlertActionStyle style;

@end

@implementation VULAlertActionModel
- (instancetype)init {
    if (self = [super init]) {
        self.title = @"";
        self.style = UIAlertActionStyleDefault;
    }
    return self;
}
@end




#pragma mark - VULAlertController

/**
 AlertActions配置
 
 @param actionBlock VULAlertActionBlock
 */
typedef void (^VULAlertActionsConfig)(VULAlertActionBlock actionBlock);


@interface VULAlertController ()

//VULAlertActionModel数组
@property (nonatomic, strong) NSMutableArray <VULAlertActionModel *>* vul_alertActionArray;
//是否操作动画
@property (nonatomic, assign) BOOL VUL_setAlertAnimated;
//action配置
- (VULAlertActionsConfig)alertActionsConfig;


@end

@implementation VULAlertController
#pragma mark - life cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    if (self.alertDidDismiss) {
        self.alertDidDismiss();
    }
}

- (void)dealloc {
    //    NSLog(@"test-dealloc");
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Private
//action-title数组
- (NSMutableArray<VULAlertActionModel *> *)VUL_alertActionArray {
    if (_vul_alertActionArray == nil) {
        _vul_alertActionArray = [NSMutableArray array];
    }
    return _vul_alertActionArray;
}

//action配置
- (VULAlertActionsConfig)alertActionsConfig {
    return ^(VULAlertActionBlock actionBlock) {
        if (self.vul_alertActionArray.count > 0) {
            //创建action
            __weak typeof(self)weakSelf = self;
            [self.vul_alertActionArray enumerateObjectsUsingBlock:^(VULAlertActionModel *actionModel, NSUInteger idx, BOOL * _Nonnull stop) {
                UIAlertAction *alertAction = [UIAlertAction actionWithTitle:actionModel.title style:actionModel.style handler:^(UIAlertAction * _Nonnull action) {
                    __strong typeof(weakSelf)strongSelf = weakSelf;
                    if (actionBlock) {
                        actionBlock(idx, action, strongSelf);
                    }
                }];
                //可利用这个改变字体颜色，但是不推荐！！！
                //                [alertAction setValue:[UIColor grayColor] forKey:@"titleTextColor"];
                //action作为self元素，其block实现如果引用本类指针，会造成循环引用
                [self addAction:alertAction];
            }];
        } else {
            NSTimeInterval duration = self.toastStyleDuration > 0 ? self.toastStyleDuration : VULAlertShowDurationDefault;
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(duration * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [self dismissViewControllerAnimated:!(self.VUL_setAlertAnimated) completion:NULL];
            });
        }
    };
}

#pragma mark -
#pragma mark - Public
- (instancetype)initAlertControllerWithTitle:(NSString *)title message:(NSString *)message preferredStyle:(UIAlertControllerStyle)preferredStyle {
    if (!(title.length > 0) && (message.length > 0) && (preferredStyle == UIAlertControllerStyleAlert)) {
        title = @"";
    }
    self = [[self class] alertControllerWithTitle:title message:message preferredStyle:preferredStyle];
    if (!self) return nil;
    
    self.VUL_setAlertAnimated = NO;
    self.toastStyleDuration = VULAlertShowDurationDefault;
    
    return self;
}

- (void)alertAnimateDisabled {
    self.VUL_setAlertAnimated = YES;
}

- (VULAlertActionTitle)addActionDefaultTitle {
    //该block返回值不是本类属性，只是局部变量，不会造成循环引用
    return ^(NSString *title) {
        VULAlertActionModel *actionModel = [[VULAlertActionModel alloc] init];
        actionModel.title = title;
        actionModel.style = UIAlertActionStyleDefault;
        [self.VUL_alertActionArray addObject:actionModel];
        return self;
    };
}

- (VULAlertActionTitle)addActionCancelTitle {
    return ^(NSString *title) {
        VULAlertActionModel *actionModel = [[VULAlertActionModel alloc] init];
        actionModel.title = title;
        actionModel.style = UIAlertActionStyleCancel;
        [self.VUL_alertActionArray addObject:actionModel];
        return self;
    };
}

- (VULAlertActionTitle)addActionDestructiveTitle {
    return ^(NSString *title) {
        VULAlertActionModel *actionModel = [[VULAlertActionModel alloc] init];
        actionModel.title = title;
        actionModel.style = UIAlertActionStyleDestructive;
        [self.VUL_alertActionArray addObject:actionModel];
        return self;
    };
}

@end

#pragma mark -
#pragma mark - III.UIViewController扩展
@implementation UIViewController (VULAlertController)
- (void)vul_showAlertWithPreferredStyle:(UIAlertControllerStyle)preferredStyle title:(NSString *)title message:(NSString *)message appearanceProcess:(VULAlertAppearanceProcess)appearanceProcess actionsBlock:(VULAlertActionBlock)actionBlock {
    if (appearanceProcess) {
        VULAlertController *alertMaker = [[VULAlertController alloc] initAlertControllerWithTitle:title message:message preferredStyle:preferredStyle];
        //防止nil
        if (!alertMaker) {
            return ;
        }
        //加工链
        appearanceProcess(alertMaker);
        //配置响应
        alertMaker.alertActionsConfig(actionBlock);
        //        alertMaker.alertActionsConfig(^(NSInteger buttonIndex, UIAlertAction *action){
        //            if (actionBlock) {
        //                actionBlock(buttonIndex, action);
        //            }
        //        });
        
        if (alertMaker.alertDidShown) {
            [self presentViewController:alertMaker animated:!(alertMaker.VUL_setAlertAnimated) completion:^{
                alertMaker.alertDidShown();
            }];
        } else {
            [self presentViewController:alertMaker animated:!(alertMaker.VUL_setAlertAnimated) completion:NULL];
        }
    }
}

- (void)vul_showAlertWithTitle:(NSString *)title message:(NSString *)message appearanceProcess:(VULAlertAppearanceProcess)appearanceProcess actionsBlock:(VULAlertActionBlock)actionBlock {
    [self vul_showAlertWithPreferredStyle:UIAlertControllerStyleAlert title:title message:message appearanceProcess:appearanceProcess actionsBlock:actionBlock];
}

- (void)vul_showActionSheetWithTitle:(NSString *)title message:(NSString *)message appearanceProcess:(VULAlertAppearanceProcess)appearanceProcess actionsBlock:(VULAlertActionBlock)actionBlock {
    [self vul_showAlertWithPreferredStyle:UIAlertControllerStyleActionSheet title:title message:message appearanceProcess:appearanceProcess actionsBlock:actionBlock];
}



@end
