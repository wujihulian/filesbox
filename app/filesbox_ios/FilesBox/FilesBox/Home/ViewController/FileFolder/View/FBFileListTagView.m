//
//  FBFileListTagView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/3/22.
//

#import "FBFileListTagView.h"
#import "FBFileTagView.h"
#import "FBFileTagManagementVC.h"
@interface FBFileListTagView ()
@property (nonatomic, strong) UIView *lineV;
@property (nonatomic, strong) UIImageView *leftImageV;
@property (nonatomic, strong) VULLabel *titleLbael;

@end
@implementation FBFileListTagView
- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        self.backgroundColor = [UIColor whiteColor];
        [self addSubview:self.bossView];
        [self addSubview:self.lineV];
        [self addSubview:self.leftImageV];
        [self addSubview:self.titleLbael];

        [self.bossView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(4));
            make.left.mas_equalTo(fontAuto(12));
            make.right.mas_equalTo(-fontAuto(12));
            make.height.mas_equalTo(_bossView.tagCollectionView.contentSize.height);
        }];
        [self.lineV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.bossView.mas_bottom).offset(10);
            make.left.mas_equalTo(fontAuto(0));
            make.right.mas_equalTo(-fontAuto(0));
            make.height.mas_equalTo(1);
        }];
        [self.leftImageV mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.centerY.mas_equalTo(self.titleLbael.mas_centerY);
            make.width.mas_equalTo(fontAuto(20));
        }];
        [self.titleLbael mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.leftImageV.mas_right).offset(5);
            make.height.mas_equalTo(50);
            make.top.mas_equalTo(self.lineV.mas_bottom).offset(fontAuto(0));

        }];
        self.leftImageV.userInteractionEnabled = YES;
        self.titleLbael.userInteractionEnabled = YES;
        UITapGestureRecognizer *top = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(gotoTag)];
        [self.leftImageV addGestureRecognizer:top];
        UITapGestureRecognizer *top1 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(gotoTag)];
        [self.titleLbael addGestureRecognizer:top1];
    }
    return self;
    
}
-(void)gotoTag{
    if (self.gotoTagManageMent) {
        self.gotoTagManageMent();
    }
}
-(void)setTagList:(NSMutableArray *)tagList{
    _tagList = tagList;
    [self.bossView removeAllTags];
    for (FBTagModel *model in self.tagList ) {
        [self.bossView addTag:model];
    }
    [self.bossView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(fontAuto(4));
        make.left.mas_equalTo(fontAuto(12));
        make.right.mas_equalTo(-fontAuto(12));
        make.height.mas_equalTo(_bossView.tagCollectionView.contentSize.height);
    }];
    for (NSDictionary *dic in self.model.tagList ) {
        NSInteger count = [self backIndexWtihDic:dic];
        if (count>=0) {
            [self.bossView selectCellWtihIndex:count];
        }
    }
}

-(NSInteger )backIndexWtihDic:(NSDictionary *)dic {
    NSInteger count =-1;
    for (int i=0; i <self.tagList.count; i ++) {
        FBTagModel *model = self.tagList[i];
        NSString *tagId = [NSString stringWithFormat:@"%@",dic[@"tagID"]];

        if (tagId.intValue  == model.labelId.integerValue) {
            count = i;
            break;
        }
    }
    return count;
}
-(UIImageView *)leftImageV{
    if (!_leftImageV) {
        _leftImageV = [UIImageView new];
        _leftImageV.image = VULGetImage(@"icon_operation_tag");
        _leftImageV.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _leftImageV;
}
-(UIView *)lineV{
    if (!_lineV) {
        _lineV = [UIView new];
        _lineV.backgroundColor = HEXCOLOR(0xEBEDF0);
    }
    return _lineV;
}
- (VULLabel *)titleLbael {
    if (!_titleLbael) {
        _titleLbael = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"标签管理") TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
//        1px solid #d9d9d9
 

    }
    return _titleLbael;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(FBFileTagView *)bossView{
    if (!_bossView) {
        _bossView = [[FBFileTagView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth-fontAuto(12) , 0)];
        _bossView.selectCollectType = ^(NSInteger index) {
            FBTagModel *model  = self.tagList[index];
            BOOL isTag =NO;
            for (NSDictionary *dic in self.model.tagList ) {
                NSString *tagId = [NSString stringWithFormat:@"%@",dic[@"tagID"]];
                if (tagId.intValue  == model.labelId.integerValue) {
                    isTag = YES;
                    break;
                }

            }
            if (self.selectCollectType) {
                self.selectCollectType(isTag?@"cancel":@"add",model.labelId);
            }
            
            
        };
    }
    return _bossView;
}
@end
