//
//  FBSpaceVC.m
//  FilesBox
//
//  Created by 无极互联 on 2023/7/18.
//

#import "FBSpaceVC.h"
#import "YLProgressBar.h"
#import "FBUserInfoModel.h"
@interface FBSpaceVC ()
@property (nonatomic, strong) YLProgressBar *completProgress;
@property (nonatomic, strong) VULLabel *beatRateLabel; ///完善度、击败率
@property (nonatomic, strong) AAChartView *chartView;
@property (nonatomic, strong) AAChartModel *aaChartModel;
@property (nonatomic, strong) NSMutableArray *dataArr;

@end

@implementation FBSpaceVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationTitle = KLanguage(@"空间");
    self.navigationTitleColor = UIColor.whiteColor;
    self.navigationTitleColor = [UIColor blackColor];
    UIImage *image = self.leftButton.imageView.image;
    self.leftButton.imageView.image = [image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate];
    [self.leftButton setImage:[image imageWithRenderingMode:UIImageRenderingModeAlwaysTemplate] forState:UIControlStateNormal];
    [self.leftButton.imageView setTintColor:[UIColor blackColor]];
    
    
    self.dataArr = [NSMutableArray array];
    [self.view addSubview:self.completProgress];
    [self.view addSubview:self.beatRateLabel];
    
    [self.completProgress mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height+20);
        make.left.mas_equalTo(fontAuto(12));
        make.right.mas_equalTo(-fontAuto(12));
        make.height.mas_equalTo(fontAuto(16));
    }];
    
    [self.beatRateLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(K_NavBar_Height+10+30);
        make.right.mas_equalTo(-fontAuto(12));
    }];

    
    
    VULSaveUserInformation *model = [VULRealmDBManager getLocalUserInformational];
    NSString *detial = @"";
    if (model.sizeMax.intValue > 0) {
        detial = [NSString stringWithFormat:@"%@/%@GB",[FBUserInfoModel formattedFileSize:model.sizeUse.longLongValue],model.sizeMax];
        CGFloat pre = model.sizeUse.longLongValue/ (model.sizeMax.longLongValue* 1024.0 * 1024.0 * 1024.0);
        [self.completProgress setProgress:pre animated:YES];

    }else{
        detial = [NSString stringWithFormat:@" %@/%@",[FBUserInfoModel formattedFileSize:model.sizeUse.longLongValue],KLanguage(@"无限制") ];
        
    }
    NSString *userSize  =[FBUserInfoModel formattedFileSize:model.sizeUse.longLongValue];
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc]initWithString:detial];
    [attributedString setAttributes:[NSDictionary dictionaryWithObjectsAndKeys:
                                     HEXCOLOR(0x333333), NSForegroundColorAttributeName,
                                     [UIFont yk_pingFangRegular:FontAuto(12)],
                                     NSFontAttributeName, nil] range:NSMakeRange(0, userSize.length)];
    self.beatRateLabel.attributedText =  attributedString;
    
    [self.view addSubview:self.chartView];
    self.aaChartModel = AAChartModel.new
        .titleSet(@"") //近30天访问量
    .titleStyleSet(AAStyle.new.colorSet(@"#666666").fontSizeSet(@18))
    

        .markerRadiusSet(@0)
        .subtitleSet(@"")//图表副标题 次
    .subtitleStyleSet(AAStyle.new.colorSet(@"#999999").fontSizeSet(@14))
  
        .subtitleAlignSet(AAChartAlignTypeLeft)
        .yAxisTitleSet(@"")
        .chartTypeSet(AAChartTypeSpline)//图表类型
        .colorsThemeSet(@[@"#18E0E0", @"#0277FE"])//设置主题颜色数组
        .tooltipValueSuffixSet(@"")//设置浮动提示框单位后缀
        .yAxisLineWidthSet(@1)//Y轴轴线线宽为0即是隐藏Y轴轴线
        .seriesSet(@[
                       AASeriesElement.new
                       .nameSet(@"浏览量")
                       .dataSet(@[@12,@17,@87,@66,@98,@40,@35]),
//                           .dataSet(@[NSNull.null, NSNull.null, NSNull.null, NSNull.null, NSNull.null, NSNull.null, NSNull.null]),
                       AASeriesElement.new
                       .nameSet(@"反馈量")
                       .dataSet(@[@20, @66, @40, @77, @60 ,@20,@55])
//                           .dataSet(@[NSNull.null, NSNull.null, NSNull.null, NSNull.null, NSNull.null, NSNull.null, NSNull.null])
                   ]);

    AAOptions *aaOptions = [AAOptionsConstructor configureChartOptionsWithAAChartModel:self.aaChartModel];
    aaOptions.xAxis
    .tickWidthSet(@0); //隐藏 X轴刻度线
    aaOptions.yAxis
    .minSet(@0)
    .minRangeSet(@1)
    .maxSet(@100)
    .lineWidthSet(@0)
    .tickColorSet(@"#2B89F0")
    .tickPositionsSet(@[@20, @40, @60, @80, @100]); //设置 Y轴刻度值为一组指定值数组

    //禁用图例点击事件
    aaOptions.plotOptions.series.events = AAEvents.new
    .legendItemClickSet(@AAJSFunc(function() {
        return false;
    }));
    aaOptions.legend
    .itemMarginBottomSet(@10)
    .verticalAlignSet(AAChartVerticalAlignTypeTop)
    .alignSet(AAChartAlignTypeRight)
    .layoutSet(AAChartLayoutTypeHorizontal)
    .itemStyleSet(AAItemStyle.new
                  .fontSizeSet(@"13px")
                  .fontWeightSet(AAChartFontWeightTypeThin)
                  .colorSet(AAColor.grayColor))
    ;

    [self.chartView aa_drawChartWithOptions:aaOptions];
    [self userProportion];
    // Do any additional setup after loading the view.
}
-(void)userProportion{

    [VULBaseRequest requestWithUrl:@"/api/disk/userProportion" params:nil requestType:YTKRequestMethodGET completion:^(__kindof VULBaseRequest * _Nonnull request) {
        [self dissmissHudView];
        NSString *success =request.responseObject[@"success"];

        if (success.boolValue) {
            NSArray *fileTypeProportion = request.data[@"fileTypeProportion"];
            for(NSDictionary *dic in  fileTypeProportion){
                NSString *type = dic[@"type"];
                NSString *count = [NSString stringWithFormat:@"%@",dic[@"count"]] ;
                NSMutableArray *arr =[NSMutableArray array];
                [arr addObject:[self titleWithType:type]];
                [arr addObject:count.numberValue];
                if(type.integerValue>0){
                    [self.dataArr addObject:arr];
                }
            }
            [self setUI];
        }
    }];
}
-(NSString *)titleWithType:(NSString*)type{
    if(type.integerValue == 1){
        return  KLanguage(@"文档");
    }
    if(type.integerValue == 2){
        return  KLanguage(@"图片");
    }
    if(type.integerValue == 3){
        return  KLanguage(@"音乐");
    }
    if(type.integerValue == 4){
        return  KLanguage(@"音频");
    }
    if(type.integerValue == 5){
        return  KLanguage(@"压缩包");
    }
    if(type.integerValue == 6){
        return  KLanguage(@"其他");
    }
    return @"";
}
-(void)setUI{

    AASeriesElement *element = AASeriesElement.new
      .innerSizeSet(@"0%")//内部圆环半径大小占比
      .borderWidthSet(@0)//描边的宽度
        .sizeSet(@(VULSCREEN_WIDTH-fontAuto(180)))
      .nameSet(@"")//尺寸大小
      .allowPointSelectSet(YES)//是否允许在点击数据点标记(扇形图点击选中的块发生位移)
    .dataSet( self.dataArr );
    self.aaChartModel.seriesSet(@[element]);
    self.aaChartModel.dataLabelsEnabledSet(true);
    self.aaChartModel.chartType = AAChartTypePie;
    self.aaChartModel.colorsThemeSet(@[@"#37A7FF",@"#FD9FC1",@"#FB9C3F",@"#FCD537",@"#EDEEEF",@"#7459E3"]);
    AAOptions *aaOptions = [AAOptionsConstructor configureChartOptionsWithAAChartModel:self.aaChartModel];
     aaOptions.legendSet(AALegend.new
     .enabledSet(YES))
 //是否直接显示扇形图数据
;
//            aaOptions.tooltip.enabled = NO;
    aaOptions.plotOptions.pie.allowPointSelectSet(NO);
    [self.chartView aa_refreshChartWithOptions:aaOptions];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (YLProgressBar *)completProgress {
    if (!_completProgress) {
        _completProgress = [[YLProgressBar alloc] initWithFrame:CGRectZero];
        _completProgress.height = fontAuto(16);
        _completProgress.type = YLProgressBarTypeRounded;
        _completProgress.progressTintColors = @[UIColorHex(#AC92F3), UIColorHex(#7459E3)];
        _completProgress.hideStripes  = YES;
        _completProgress.hideTrack = YES;
        _completProgress.behavior = YLProgressBarBehaviorDefault;
        _completProgress.backgroundColor =  UIColorHex(#F6F6F6) ;
        _completProgress.layer.cornerRadius = 4;
        _completProgress.layer.masksToBounds = YES;
        _completProgress.progressBarInset = 0;
        _completProgress.progress = 0;
        _completProgress.hideInnerWhiteShadow = YES;

    }
    return _completProgress;
}

- (VULLabel *)beatRateLabel {
    if (!_beatRateLabel) {
        _beatRateLabel = [VULLabel getLabelWithFrame:CGRectZero Text:@"名片完成度 50% 超过20%用户" TxtAlignment:NSTextAlignmentLeft Font:[UIFont yk_pingFangRegular:FontAuto(12)] Color:HEXCOLOR(0xB1B1B0) BgColor:nil];
    }
    return _beatRateLabel;
}

- (AAChartView *)chartView {
    if (!_chartView) {
        _chartView = [[AAChartView alloc]initWithFrame:CGRectMake(0, K_NavBar_Height+70, VULSCREEN_WIDTH, VULSCREEN_WIDTH)];
    }
    return _chartView;
}
@end
