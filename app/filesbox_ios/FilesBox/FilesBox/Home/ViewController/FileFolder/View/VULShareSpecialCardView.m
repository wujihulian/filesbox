//
//  VULShareCardView.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/8/12.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULShareSpecialCardView.h"
@implementation VULShareSpecialCardView


- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor = HEXCOLOR(0xF9F9F9);

        UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopLeft | UIRectCornerTopRight cornerRadii:CGSizeMake(15, 15)];
        CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
        //设置大小
        maskLayer.frame = frame;
        //设置图形样子
        maskLayer.path = maskPath.CGPath;
        self.layer.mask = maskLayer;

        NSArray *titleArray =  @[KLanguage(@"微信") ,KLanguage(@"朋友圈"),KLanguage(@"钉钉"),KLanguage(@"微博"),KLanguage(@"二维码"),KLanguage(@"复制链接"),KLanguage(@"预览"),KLanguage(@"更多")];
        NSArray *iconArray = @[@"wx_weixin_icon",@"wx_friends_icon",@"wx_dingding_icon",@"wx_weibo_icon" ,@"wx_code_icon",@"wx_link_icon",@"wx_yulan_icon",@"wx_more_icon"];
        UIView *leftView = nil;
        for (NSInteger i = 0; i < titleArray.count; i++) {
            UIView *backView = [UIView new];
            backView.tag =100+i;
            [self addSubview:backView];

            [backView mas_makeConstraints:^(MASConstraintMaker *make) {
                if (leftView) {
                    make.left.mas_equalTo(leftView.mas_right);
                } else {
                    make.left.mas_equalTo(0);
                }
                
                if (i==4) {
                    make.top.mas_equalTo(leftView.mas_bottom).offset(fontAuto(15));
                    make.left.mas_equalTo(0);

                }else if(i>4){
                    make.top.mas_equalTo(leftView.mas_top);
                }else
                {
                    make.top.mas_equalTo(fontAuto(58));
                }
               
                make.height.mas_equalTo(fontAuto(80));
                make.width.mas_equalTo(self.mas_width).multipliedBy(1 / 4.0);
            }];
            
            UIView *imgBgView = [UIView new];
            imgBgView.backgroundColor = [UIColor clearColor];
            imgBgView.layer.cornerRadius = 6.f;
            imgBgView.layer.masksToBounds = YES;
            [backView addSubview:imgBgView];
            
            UIImageView *imageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:[iconArray objectAtIndex:i]]];
            imageView.tag = 100000+i;
//            imageView.contentMode = UIViewContentModeScaleAspectFit;
            [backView addSubview:imageView];

            UILabel *titleLabel = [UILabel new];
            titleLabel.tag = 10000+i;

            titleLabel.textColor = UIColorHex(#333333);
            titleLabel.font = IS_IPAD ? [UIFont systemFontOfSize:17] : [UIFont systemFontOfSize:14];
            titleLabel.textAlignment = NSTextAlignmentCenter;
            titleLabel.text = [titleArray objectAtIndex:i];
            [backView addSubview:titleLabel];

            [imgBgView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(0);
                make.centerX.mas_equalTo(backView.mas_centerX);
                make.size.mas_equalTo(CGSizeMake(fontAuto(50), fontAuto(50)));
            }];
            
            [imageView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.centerX.mas_equalTo(imgBgView.mas_centerX);
                make.centerY.mas_equalTo(imgBgView.mas_centerY);
                make.size.mas_equalTo(CGSizeMake(fontAuto(50), fontAuto(50)));
            }];

            [titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.top.mas_equalTo(imgBgView.mas_bottom).offset(fontAuto(13));
                make.left.right.mas_equalTo(0);
                make.centerX.mas_equalTo(backView.mas_centerX);
                make.bottom.mas_equalTo(0);
            }];
            
            UIControl *btnControl = [UIControl new];
            btnControl.tag = 1616 + i;
            [btnControl addTarget:self action:@selector(clickAction:) forControlEvents:UIControlEventTouchUpInside];
            [backView addSubview:btnControl];
            [btnControl mas_makeConstraints:^(MASConstraintMaker *make) {
                make.edges.mas_equalTo(backView);
            }];
            
            leftView = backView;
        }

        VULButton *closeBtn = [VULButton createImgBtnWithFrame:CGRectZero imgNamed:@"goodsList_close" Target:self Sel:@selector(clickAction:)];
        closeBtn.tag = 1616;
        [self addSubview:closeBtn];
        [closeBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.height.width.mas_equalTo(40);
            make.right.offset(-10);
            make.top.offset(10);
        }];
        self.label = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"分享文件") TxtAlignment:NSTextAlignmentLeft Font:[UIFont boldSystemFontOfSize:fontAuto(16)] Color:HEXCOLOR(0x333333) BgColor:[UIColor clearColor]];
      
        
        [self addSubview:     self.label ];
        [     self.label  mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.offset(fontAuto(25));
            make.top.offset(10);
        }];

        [self layoutIfNeeded];
        self.height = leftView.bottom + fontAuto(35);
    }
    return self;
}
-(void)setIsNews:(BOOL)isNews{
    if(isNews){
        self.label .text = [NSString stringWithFormat:@"%@%@",KLanguage(@"分享"),KLanguage(@"资讯")];
    }
}
- (void)clickAction:(UIControl *)sender {
    if (self.menuViewBtnClickedBlock) {
        self.menuViewBtnClickedBlock(sender.tag - 1616);
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
