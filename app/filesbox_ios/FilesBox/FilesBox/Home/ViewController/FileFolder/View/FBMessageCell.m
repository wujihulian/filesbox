//
//  FBMessageCell.m
//  FilesBox
//
//  Created by 无极互联 on 2023/6/13.
//

#import "FBMessageCell.h"
@interface FBMessageCell ()
@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULLabel *timeLabel;
@property (nonatomic, strong) UIImageView *leftImage;
@property (nonatomic, strong) UIView *redView;
@end
@implementation FBMessageCell
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self.contentView addSubview:self.leftImage];
        [self.leftImage addSubview:self.redView];
        [self.contentView addSubview:self.titleLabel];
        [self.contentView addSubview:self.timeLabel];

        [self.leftImage mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(fontAuto(12));
            make.width.height.mas_equalTo(fontAuto(50));
            make.centerY.mas_equalTo(self.contentView.mas_centerY);
        }];
        [self.redView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.mas_equalTo(-fontAuto(5));
            make.top.mas_equalTo(fontAuto(5));
            make.width.height.mas_equalTo(fontAuto(8));
    
        }];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(fontAuto(10));
            make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(12));
        }];
        [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo(self.titleLabel.mas_bottom).offset(fontAuto(10));
            make.left.mas_equalTo(self.leftImage.mas_right).offset(fontAuto(10));
            make.right.mas_equalTo(-fontAuto(12));
            make.bottom.mas_equalTo(-fontAuto(10));
        }];
        
    }
    return self;
}
-(void)setModel:(FBMessageModel *)model{
    _model = model;
    self.titleLabel.text = model.title;
    self.timeLabel.text =  [model.sendTime stringByReplacingOccurrencesOfString:@"T" withString:@" "];;

    if(model.isRead){
        self.titleLabel.textColor = HEXCOLOR(0x999999);
        self.timeLabel.textColor = HEXCOLOR(0x999999);
        self.redView.hidden  = YES;
        self.leftImage.alpha = 0.5;
    }else{
        self.titleLabel.textColor = HEXCOLOR(0x333333);
        self.timeLabel.textColor = HEXCOLOR(0x333333);
        self.redView.hidden  = NO;
        self.leftImage.alpha = 1;
    }
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:15] Color:UIColorHex(#333333) BgColor:nil];
        _titleLabel.numberOfLines = 2;
    }
    return _titleLabel;
}

- (VULLabel *)timeLabel {
    if (!_timeLabel) {
        _timeLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:14] Color:UIColorHex(#333333) BgColor:nil];
    }
    return _timeLabel;
}
-(UIImageView *)leftImage{
    if(!_leftImage){
        _leftImage = [UIImageView new];
        _leftImage.image = VULGetImage(@"icon_message_icon");
        
    }
    return _leftImage;
}
-(UIView *)redView{
    if(!_redView){
        _redView = [UIView new];
        _redView.backgroundColor = UIColor.redColor;
        _redView.layer.masksToBounds = YES;
        _redView.layer.cornerRadius = fontAuto(4);

    }
    return _redView;
}

@end
