//
//  VULChoseDateView.m
//  VideoULTeacher
//
//  Created by 无极互联 on 2020/5/25.
//  Copyright © 2020 svnlan. All rights reserved.
//

#import "VULChoseDateView.h"
#import "FSCalendar.h"

@interface VULChoseDateView ()<FSCalendarDataSource, FSCalendarDelegate>

@property (nonatomic, strong) VULLabel *titleLabel;
@property (nonatomic, strong) VULButton *startButton;
@property (nonatomic, strong) VULButton *endButton;
@property (nonatomic, strong) VULButton *confirmButton;
@property (nonatomic, strong) NSString *startDateStr;
@property (nonatomic, strong) NSString *endDateStr;
@property (weak, nonatomic) FSCalendar *calendar;

@property (nonatomic, assign) NSInteger currentTag;
@property (nonatomic, strong) NSDate *currentStartDate;
@property (nonatomic, strong) NSDate *currentEndDate;

@end

@implementation VULChoseDateView

- (instancetype)initWithStartDate:(NSString *)startDateStr EndDate:(NSString *)endDateStr {
    self = [super init];
    if (self) {
        self.backgroundColor = [UIColor whiteColor];
        self.startDateStr = startDateStr;
        self.endDateStr = endDateStr;
        self.currentTag = 141;
        self.frame = CGRectMake(0, 0, kScreenWidth, FontAuto(95) + (IS_IPAD ? 450 : 300) + FontAuto(10)  + 44 + 15 + 20 + K_BottomBar_Height / 2);

        [self createUI];

        self.layer.mask = ({
            UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:self.bounds
                                                           byRoundingCorners:UIRectCornerTopLeft | UIRectCornerTopRight
                                                                 cornerRadii:CGSizeMake(10, 10)];
            CAShapeLayer *maskLayer = [CAShapeLayer layer];
            maskLayer.frame = self.bounds;
            maskLayer.path = maskPath.CGPath;
            maskLayer;
        });
        self.clipsToBounds = YES;
    }
    return self;
}

- (void)createUI {
    [self addSubview:self.titleLabel];
    [self addSubview:self.startButton];
    [self addSubview:self.endButton];

    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.offset(0);
        make.height.mas_equalTo(FontAuto(50));
    }];
    [self.startButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.titleLabel.mas_bottom);
        make.left.offset(0);
        make.height.width.mas_equalTo(self.endButton);
    }];
    [self.endButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.titleLabel.mas_bottom);
        make.right.offset(0);
        make.height.mas_equalTo(FontAuto(45));
        make.left.equalTo(self.startButton.mas_right);
    }];

    CGFloat height = IS_IPAD ? 450 : 300;
    FSCalendar *calendar = [[FSCalendar alloc] initWithFrame:CGRectMake(0, K_NavBar_Height, self.frame.size.width, height)];
    calendar.dataSource = self;
    calendar.delegate = self;
    calendar.backgroundColor = [UIColor whiteColor];
    calendar.appearance.headerMinimumDissolvedAlpha = 0;
    calendar.appearance.caseOptions = FSCalendarCaseOptionsWeekdayUsesSingleUpperCase | FSCalendarCaseOptionsHeaderUsesUpperCase;
    calendar.headerHeight = 0;
    calendar.appearance.todayColor = [UIColor clearColor];
    calendar.appearance.borderDefaultColor = HEXCOLOR(0xFF0000);
    calendar.appearance.headerTitleColor = HEXCOLOR(0x333333);
    calendar.appearance.headerTitleFont = [UIFont fontWithName:@"PingFang SC" size:16];
    calendar.appearance.weekdayTextColor = HEXCOLOR(0x333333);
    calendar.appearance.selectionColor = HEXCOLOR(0xFF3B30); //FF3B30 07C161
    calendar.allowsSelection = YES;
    calendar.scrollEnabled = YES;
    calendar.appearance.isAttendanceStyle = YES;
    calendar.appearance.isTeacherSignin = YES;

    [self addSubview:calendar];
    self.calendar = calendar;
    [self.calendar mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.startButton.mas_bottom).offset(1);
        make.left.right.offset(0);
        make.height.mas_equalTo(height);
    }];

    [self addSubview:self.confirmButton];
    [self.confirmButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.calendar.mas_bottom).offset(2 * kSpace);
        make.height.equalTo(@44);
        make.width.equalTo(@(VULSCREEN_WIDTH * 0.8));
        make.centerX.equalTo(self.mas_centerX);
        make.bottom.offset(-K_BottomBar_Height / 2 - 15);
    }];

    NSDate *startDate = [NSDate dateWithTimeIntervalSince1970:[self.startDateStr doubleValue] / 1000];
    NSDate *endDate = [NSDate dateWithTimeIntervalSince1970:[self.endDateStr doubleValue] / 1000];

    self.currentStartDate = startDate;
    self.currentEndDate = endDate;
    [self configStyleWithDate:startDate whichItem:YES];
    [self configStyleWithDate:endDate whichItem:NO];

    [self.calendar selectDate:startDate];
}

- (void)configStyleWithDate:(NSDate *)dateValue whichItem:(BOOL)isFirst {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    //NSDate转NSString
    NSString *currentDateString = [dateFormatter stringFromDate:dateValue];

    NSMutableAttributedString *titleAttributed = [self getAttributedStringWithDateString:currentDateString isStart:isFirst];
    if (isFirst) {
        [self.startButton setAttributedTitle:titleAttributed forState:UIControlStateNormal];
    } else {
        [self.endButton setAttributedTitle:titleAttributed forState:UIControlStateNormal];
    }
}

- (NSMutableAttributedString *)getAttributedStringWithDateString:(NSString *)dateStr isStart:(BOOL)isStart {
    NSString *startStr = (isStart ? KLanguage(@"开始:")  :KLanguage (@"结束:"));
    NSString *allString = [NSString stringWithFormat:@"%@%@", startStr, dateStr];
    NSMutableAttributedString *attributed = [[NSMutableAttributedString alloc] initWithString:allString];

    [attributed addAttribute:NSForegroundColorAttributeName value:HEXCOLOR(0x333333) range:NSMakeRange(0, startStr.length)];
    UIColor *subColor = HEXCOLOR(0x666666);
    if ((isStart && self.startButton.isSelected) || (!isStart && self.endButton.isSelected)) {
        subColor = HEXCOLOR(0xFF0629);
    }
    [attributed addAttribute:NSForegroundColorAttributeName value:subColor range:NSMakeRange(startStr.length, dateStr.length)];

    return attributed;
}

#pragma mark - FSCalendarDelegate
//手动选中了某个日期
- (void)calendar:(FSCalendar *)calendar didSelectDate:(NSDate *)date atMonthPosition:(FSCalendarMonthPosition)monthPosition {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    //NSDate转NSString
    NSString *currentDateString = [dateFormatter stringFromDate:date];
    NSLog(@"did select %@", currentDateString);

    if (self.currentTag == 141) {
        self.currentStartDate = date;
        [self configStyleWithDate:self.currentStartDate whichItem:YES];
    } else {
        self.currentEndDate = date;
        [self configStyleWithDate:self.currentEndDate whichItem:NO];
    }
}

- (void)buttonAction:(VULButton *)sender {
    NSInteger tag = sender.tag;
    if (tag == self.currentTag) {
        return;
    }
    if (tag == 141) {
        self.startButton.selected = YES;
        self.startButton.backgroundColor = HEXCOLOR(0xFFE6E9);
        self.endButton.selected = NO;
        self.endButton.backgroundColor = [UIColor clearColor];
        [self.calendar selectDate:self.currentStartDate];
    } else {
        self.startButton.selected = NO;
        self.startButton.backgroundColor = [UIColor clearColor];
        self.endButton.selected = YES;
        self.endButton.backgroundColor = HEXCOLOR(0xFFE6E9);
        [self.calendar selectDate:self.currentEndDate];
    }
    [self configStyleWithDate:self.currentStartDate whichItem:YES];
    [self configStyleWithDate:self.currentEndDate whichItem:NO];
    self.currentTag = tag;
}

- (void)confirmButtonAction:(VULButton *)sender {
    NSTimeInterval startInterval = [self.currentStartDate timeIntervalSince1970];
    long long startMilliseconds = startInterval * 1000;
    NSTimeInterval endInterval = [self.currentEndDate timeIntervalSince1970];
    long long endMilliseconds = endInterval * 1000;

    NSLog(@"totalMilliseconds=%llu - %llu", startMilliseconds, endMilliseconds);
    
    if (startMilliseconds > endMilliseconds) {
        [self makeToast:@"结束时间必须大于开始时间" duration:2.5 position:@"center"];
        return;
    }    
    if (self.choseDateBlock) {
        self.choseDateBlock(startMilliseconds, endMilliseconds);
    }
}

#pragma mark - getter
- (VULLabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [VULLabel getLabelWithFrame:CGRectZero Text:KLanguage(@"日期选择") TxtAlignment:NSTextAlignmentCenter Font:VULPingFangSCMedium(20) Color:HEXCOLOR(0x333333) BgColor:nil];
    }
    return _titleLabel;
}

- (VULButton *)startButton {
    if (!_startButton) {
        _startButton = [VULButton getCustomBtnWithFrame:CGRectZero title:KLanguage(@"开始:") Font:18 Bgcolor:[UIColor clearColor] Target:self action:@selector(buttonAction:)];
        [_startButton.titleLabel setFont:VULPingFangSCMedium(18)];
        _startButton.backgroundColor = HEXCOLOR(0xFFE6E9);
        _startButton.selected = YES;
        _startButton.tag = 141;
    }
    return _startButton;
}

- (VULButton *)endButton {
    if (!_endButton) {
        _endButton = [VULButton getCustomBtnWithFrame:CGRectZero title:KLanguage(@"结束:") Font:18 Bgcolor:[UIColor clearColor] Target:self action:@selector(buttonAction:)];
        [_endButton.titleLabel setFont:VULPingFangSCMedium(18)];
        _endButton.tag = 142;
    }
    return _endButton;
}

- (VULButton *)confirmButton {
    if (!_confirmButton) {
        _confirmButton = [VULButton getCustomBtnWithFrame:CGRectZero title:KLanguage(@"确定") Font:20 Bgcolor:DefaultColor Target:self action:@selector(confirmButtonAction:)];
        _confirmButton.layer.cornerRadius = 6.f;
        _confirmButton.layer.masksToBounds = YES;
        [_confirmButton.titleLabel setFont:VULPingFangSCMedium(20)];
        [_confirmButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    }
    return _confirmButton;
}

@end
