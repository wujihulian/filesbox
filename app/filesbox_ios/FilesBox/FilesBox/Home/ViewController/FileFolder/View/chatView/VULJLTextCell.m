//
//  VULJLTextCell.m
//  UnlimitedBusiness
//
//  Created by 无极互联 on 2021/5/21.
//  Copyright © 2021 svnlan. All rights reserved.
//

#import "VULJLTextCell.h"
#import "VULChatTextParser.h"
@interface VULJLTextCell ()
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) VULSvgImageView *iconImageView;
@property (nonatomic, strong) UIView *bgView;

@property (nonatomic, strong) YYLabel *commentLabel;
@property (nonatomic, strong) UIImageView *imageV;

@end

@implementation VULJLTextCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.backgroundColor = [UIColor clearColor];
       
        [self setView];
    }
    return self;
}
-(void)setView{
    [self.contentView addSubview:self.bgView];
    [self.contentView addSubview:self.iconImageView];
    [self.contentView addSubview:self.nameLabel];
    [self.bgView addSubview:self.commentLabel];
    [self.contentView addSubview:self.imageV];

    [self.iconImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(fontAuto(14));
        make.top.mas_equalTo(fontAuto(10));
        make.width.height.mas_equalTo(fontAuto(40));
    }];
    
    [self.nameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.iconImageView.mas_right).offset(9);
        make.top.mas_equalTo(fontAuto(5));
        make.height.mas_equalTo(fontAuto(15));
        make.right.mas_equalTo(-fontAuto(14));
    }];
    CGFloat width = VULSCREEN_WIDTH -fontAuto(124);
    [self.bgView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.nameLabel.mas_left);
        make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(5);
        make.width.mas_lessThanOrEqualTo(width);
        make.bottom.mas_equalTo(-fontAuto(10)).priorityHigh();

    }];
    [self.commentLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(fontAuto(5));
        make.left.mas_equalTo(fontAuto(10));
        make.right.mas_equalTo(-fontAuto(10));
        make.bottom.mas_equalTo(-fontAuto(5)).priorityHigh();
    }];
    self.commentLabel.preferredMaxLayoutWidth = width-fontAuto(20);

    // 添加长按手势
     UILongPressGestureRecognizer *longPressGesture = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPress:)];
     [self addGestureRecognizer:longPressGesture];
    
}
- (void)handleLongPress:(UILongPressGestureRecognizer *)gestureRecognizer {
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan) {
        NSArray *  titleArr = @[KLanguage(@"删除")];

        NSMutableArray *array = [[NSMutableArray alloc] init];
        for (NSInteger i = 0; i < titleArr.count; i++) {
            NSString *title1 = titleArr[i] ;
            UIImage *rightImage = nil;
            UIImage *image = nil;
     
            
            
            YCMenuAction *action = [YCMenuAction actionWithTitle:titleArr[i] image:image rightImage:rightImage handler:^(YCMenuAction *action) {

                
            
                if ([action.title isEqualToString:KLanguage(@"删除")]) {
                    if(self.deletCommentWithModel){
                        self.deletCommentWithModel(self.model);
                    }
                }else{
                }
               
            }];
            [array addObject:action];
        }
        CGFloat width = 120;

        YCMenuView *menuView = [YCMenuView menuWithActions:array width:width relyonView:self.iconImageView];
        menuView.menuColor = [UIColor whiteColor];
        menuView.separatorIndexArray = @[@(3)];
        menuView. isNoPre = YES;
        menuView.separatorColor = [UIColor redColor];
        menuView.maxDisplayCount = 20;
        menuView.cornerRaius = 0;
        //menuView.offset = 150;
        menuView.textColor = UIColorHex(333333);
        menuView.textFont = [UIFont yk_pingFangRegular:14];
        menuView.menuCellHeight = 35;
        menuView.dismissOnselected = YES;
        menuView.dismissOnTouchOutside = YES;
        menuView.backgroundColor = [UIColor whiteColor];
        [menuView show];
    }
}
- (void)setModel:(VULServiceChatMessageModel *)model{
    _model = model;

    NSMutableAttributedString *text = [[NSMutableAttributedString alloc] initWithString:model.msg ];
    
    NSArray<NSTextCheckingResult *> *emoticonResults = [[VULChatTextParser regexEmoticon] matchesInString:text.string options:kNilOptions range:text.rangeOfAll];
    [self.iconImageView svg_setImageWithURL:[NSURL URLWithString:resultsUrl(model.avatar)] placeholderImage:VULGetImage(@"placeholder_face")];
    CGFloat width = VULSCREEN_WIDTH -fontAuto(124);
    if (emoticonResults.count ==1  && [model.msg hasPrefix:@"["] && [model.msg hasSuffix:@"]"]) {
        self.bgView.backgroundColor = [UIColor clearColor];
        self.commentLabel.attributedText = [VULChatTextParser parserEmoticonMessage:model.msg  font: [UIFont yk_pingFangMedium:FontAuto(14)] lineHeight:MAX(40, fontAuto(40))];
      
        [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.nameLabel.mas_left);
            make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(-8);
            make.width.mas_lessThanOrEqualTo(width);
            make.bottom.mas_equalTo(-fontAuto(10)).priorityHigh();

        }];
    }else{
        [self.bgView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(self.nameLabel.mas_left);
            make.top.mas_equalTo(self.nameLabel.mas_bottom).offset(5);
            make.width.mas_lessThanOrEqualTo(width);
            make.bottom.mas_equalTo(-fontAuto(10)).priorityHigh();

        }];
        self.bgView.backgroundColor = [UIColor whiteColor];
        self.commentLabel.attributedText = [VULChatTextParser parserEmoticonMessage:model.msg  font:self.commentLabel.font lineHeight:fontAuto(14)];
    }
    self.nameLabel.text = model.timestamp ;

    
    [self layoutIfNeeded];

    UIBezierPath *maskOnePath = [UIBezierPath bezierPathWithRoundedRect:CGRectMake(0, 0, self.bgView.width, self.bgView.height) byRoundingCorners:UIRectCornerTopRight | UIRectCornerBottomLeft |  UIRectCornerBottomRight cornerRadii:CGSizeMake(8, 8)];

    CAShapeLayer *maskOneLayer = [[CAShapeLayer alloc] init];

    maskOneLayer.frame = self.bgView.bounds;

    maskOneLayer.path = maskOnePath.CGPath;
    self.bgView.clipsToBounds = YES;
    self.bgView.layer.mask = maskOneLayer;
}
- (CGSize)sizeThatFits:(CGSize)size {
    CGFloat totalHeight = 0;
    totalHeight += [self.nameLabel sizeThatFits:size].height;
    totalHeight += [self.commentLabel sizeThatFits:size].height;
    totalHeight += fontAuto(36); // margins
    return CGSizeMake(size.width, totalHeight);
}
-(void)setHideTime:(BOOL)hideTime{
    if (hideTime) {
//        [self.nameLabel mas_updateConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(self.iconImageView.mas_right).offset(9);
//            make.top.mas_equalTo(fontAuto(5));
//            make.height.mas_equalTo(fontAuto(0));
//            make.right.mas_equalTo(-fontAuto(14));
//        }];
    }
}
#pragma mark - Lazy

- (VULSvgImageView *)iconImageView {
    if (!_iconImageView) {
        _iconImageView = [[VULSvgImageView alloc] init];
        _iconImageView.width = _iconImageView.height = FontAuto(40);
        _iconImageView.layer.cornerRadius = _iconImageView.height / 2.0;
        _iconImageView.layer.masksToBounds = YES;
        _iconImageView.image = VULGetImage(@"login_logo");
    }
    return _iconImageView;
}
- (UIView *)bgView {
    if (!_bgView) {
        _bgView = ({
            UIView *view = [[UIView alloc] init];
            view.frame = CGRectMake(65, 361, 246, 105);
            view.alpha = 1;
            // Background Code
            view.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.00];
            // Border Code
            // Radius Code
            view;
        });
    }
    return _bgView;
}
- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = ({
            UILabel *label = [[UILabel alloc] init];
            label.font = [UIFont yk_pingFangMedium:FontAuto(14)];
            label.textColor = UIColorHex(#CFCFCF);
            label;
        });
    }
    return _nameLabel;
}

- (YYLabel *)commentLabel {
    if (!_commentLabel) {
        _commentLabel = ({
            YYLabel *label = [[YYLabel alloc] init];
            label.font = [UIFont yk_pingFangMedium:FontAuto(16)];
            label.textColor = UIColorHex(#333333);
            label.numberOfLines = 0;
            label;
        });
    }
    return _commentLabel;
}
- (UIImageView *)imageV {
    if (!_imageV) {
        _imageV = ({
            UIImageView *imageView = [[UIImageView alloc] init];
            imageView.layer.maskedCorners = YES;
            imageView.layer.cornerRadius = 6;
            imageView;
        });
    }
    return _imageV;
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
