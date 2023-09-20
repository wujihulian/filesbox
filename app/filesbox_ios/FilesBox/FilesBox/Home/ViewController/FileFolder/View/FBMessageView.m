//
//  FBMessageView.m
//  FilesBox
//
//  Created by 无极互联 on 2023/2/18.
//

#import "FBMessageView.h"
@interface FBMessageView ()

@end
@implementation FBMessageView
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self setView];
    }
    return self;
}
-(void)setView{
    [self addSubview:self.iconImage];
    [self addSubview:self.messageLabel];
    [self.iconImage mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(3);
        make.left.mas_equalTo(0);
        make.width.mas_equalTo(18);
    }];
    [self.messageLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(0);
        make.right.mas_equalTo(0);
        make.height.width.mas_equalTo(16);
    }];
}
-(void)setIconName:(UIImage *)iconName{
    self.iconImage.image = iconName;
}
-(void)setMessageCount:(NSString *)messageCount{
    if (messageCount.intValue == 0) {
        _messageLabel.hidden = YES;
        self.messageLabel.text  = @"";
    }else{
        self.messageLabel.text = messageCount;
        _messageLabel.hidden = NO;
    }
}
-(UIImageView*)iconImage{
    if (!_iconImage) {
        _iconImage = [UIImageView new];
        _iconImage.contentMode = UIViewContentModeScaleAspectFit;
    }
    return _iconImage;
}
-(UILabel *)messageLabel{
    if (!_messageLabel) {
        _messageLabel = [UILabel new];
        _messageLabel.textColor = HEXCOLOR(0xffffff);
        _messageLabel.layer.masksToBounds = YES;
        _messageLabel.layer.cornerRadius = 8;
        _messageLabel.backgroundColor = HEXCOLOR(0xE00000);
        _messageLabel.textAlignment = NSTextAlignmentCenter;
        _messageLabel.font = [UIFont yk_pingFangRegular:7];
    }
    return _messageLabel;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
