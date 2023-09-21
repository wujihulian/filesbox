//
//  VULResponseModel.m
//  VideoULimit
//
//  Created by ZCc on 2018/9/10.
//  Copyright © 2018年 svnlan. All rights reserved.
//

#import "VULResponseModel.h"

@implementation VULResponseModel

@end

#pragma mark -登录
@implementation VULResponseToken

@end

#pragma mark -获取登录信息
@implementation VULResponseLoginModel

@end

#pragma mark -获取个人信息
@implementation VULResponseUserInfoModel

@end

@implementation VULAnswerModel

@end

@implementation VULHobbysModel

@end

@implementation VULUserDetailModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"descStr":@"description"};
}

@end



#pragma mark -最新课程
@implementation VULResponseCourseWareModel

@end

#pragma mark - 学习-我的单课-我购买的单课课件列表
@implementation VULResponseMyCourseWareListModel

@end

#pragma mark -购买的课程列表
@implementation VULResponseMyClassifyListModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"courseSubList" : [VULCourseListModel class]};
}


@end

@implementation VULCourseListModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"teacherList" : [VULTeacherListModel class]};
}

@end

@implementation VULTeacherListModel

@end

#pragma mark - 课程收藏列表
@implementation VULResponseCollectCourseListModel

@end

@implementation VULCollectCourseModel

@end

@implementation VULTeacherModel

@end

@implementation VULAssistantTeacherModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"userId":@"id"};
}

@end


#pragma mark -购买的课程列表
@implementation VULResponseClassifyListModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"courseList" : [VULResponseClassifySelectionListModel class]};
}

@end


#pragma mark - 选课列表
@implementation VULResponseClassifySelectionListModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"teacherList" : [VULTeacherListModel class]};
}
+ (VULResponseClassifySelectionListModel *)getClassifyModelWithInfoModel:(VULResponseCourseWareInfoModel *)infoModel {
    VULResponseClassifySelectionListModel *classifyModel = [VULResponseClassifySelectionListModel new];
    classifyModel.courseId = infoModel.courseId;
    classifyModel.introduce = infoModel.introduce;
    classifyModel.cover = infoModel.cover;
    classifyModel.courseName = @"购买课程";
    return classifyModel;
}
@end


#pragma mark- 获取分类列表
@implementation VULResponseClassifyModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"children" : [VULSubClassifyModel class]};
}

@end

@implementation VULSubClassifyModel

@end

#pragma mark- 答疑列表
@implementation VULResponseAnswerListModel

+ (VULResponseAnswerListModel *)getAnswerModelWithDic:(NSDictionary *)dic {
    VULResponseAnswerListModel *answerModel = [VULResponseAnswerListModel modelWithDictionary:dic];
    
    if ([answerModel.detail containsString:@"<img"] && [answerModel.detail containsString:@"src=\""]) {
        NSArray *imgUrlArr = [NSString getImageSrcWithString:answerModel.detail];
        NSMutableArray *dataSourseUrlArr = [NSMutableArray arrayWithCapacity:0];
        for (NSString *imgUrlString in imgUrlArr) {
            NSString *picUrlStr = [NSString stringWithFormat:@"%@%@",kSchoolServiceUrl, imgUrlString];
            NSString *subStr = [imgUrlString substringToIndex:2];
            if ([subStr containsString:@"//"]) {
                picUrlStr = [NSString stringWithFormat:@"https:%@",imgUrlString];
            }
            [dataSourseUrlArr addObject:picUrlStr];
        }
        
        CGFloat jgg_Width = VULSCREEN_WIDTH_VARIABLE - 2 * kSpace - VULPercentageHeight(10);
        CGFloat image_Width = (jgg_Width - kSpace) / 3;
        CGFloat jgg_height = 0;
        if (dataSourseUrlArr.count == 0) {
            jgg_height = 0;
        } else if (dataSourseUrlArr.count <= 3) {
            jgg_height = image_Width;
        } else if (dataSourseUrlArr.count > 3 && dataSourseUrlArr.count <= 6) {
            jgg_height = 2 * image_Width + kSpace / 2;
        } else  if (dataSourseUrlArr.count > 6) {
            jgg_height = 3 * image_Width + kSpace;
        }
        answerModel.imageContainerHeight = jgg_height;
        answerModel.imageSourceArray = dataSourseUrlArr;
    }
    
    return answerModel;
}

@end

#pragma mark- 我的课程列表 （答疑-提问时调用）
@implementation VULResponseMyCourseModel

@end

#pragma mark- 根据课程id获得课件列表
@implementation VULCourseWareModel

@end

#pragma mark - 获取课程关联的所有教师列表
@implementation VULCourseTeachersModel

@end

//课件收藏列表
@implementation VULCollectCourseWareModel

@end

#pragma mark - 上传聊天图片 返回url
@implementation VULResponseChatPicture

@end


#pragma mark - 课件详情
@implementation VULResponseCourseWareInfoModel

- (BOOL)isHIKCamera {
    return self.courseWareType.integerValue == 4 && self.liveType.integerValue == 1;
}

- (BOOL)isNewCourseWare {
    return ![self.courseId isAvailable] || self.courseId.integerValue == 0;
}


@end

#pragma mark - 创建WebRTC房间
@implementation VULResponseRTCChannelInfoModel

@end

#pragma mark - WebRTC房间token
@implementation VULResponseRTCChannelTokenModel

@end


#pragma mark - 通知
@implementation VULResponseNoticeInfoModel

@end

#pragma mark - 课件列表的课件model
@implementation VULResponseCourseWareListModel

- (BOOL)isHIKCamera {
    return self.courseWareType.integerValue == 4 && self.liveType == 1;
}

- (BOOL)isNewCourseWare {
    return self.courseId == 0;
}

@end

#pragma mark - 直播推流地址等信息
@implementation VULResponseBuildLiveUrlModel

@end

#pragma mark - 问答、问卷数量model
@implementation VULResponseRelationCountModel

@end

#pragma mark - 章节/单元model
@implementation VULResponseCourseWareUnitModel

- (NSMutableArray<VULResponseCourseWareListModel *> *)courseWareList {
    if(!_courseWareList){
        _courseWareList = [NSMutableArray arrayWithCapacity:0];
    }
    return _courseWareList;
}

@end

#pragma mark - 根据id获取文件列表API 附件
@implementation VULResponseSourceModel

@end

#pragma mark - 多个问卷id查询多个问卷
@implementation VULResponseQuestionairesModel

@end

#pragma mark - 网校基本信息
@implementation VULSchoolorPlatformInfoModel

@end

#pragma mark - 获取学校列表
@implementation VULResponseSchoolInfoModel

+ (VULResponseSchoolInfoModel *)translateWithThirdModel:(VULThirdSchoolInfoModel *)thirdModel {
    if (thirdModel == nil) {
        return nil;
    }
    VULResponseSchoolInfoModel *schoolInfo = [[VULResponseSchoolInfoModel alloc] init];
    schoolInfo.cover = thirdModel.cover;
    schoolInfo.createTimeStamp = thirdModel.createTimeStamp;
    schoolInfo.gmtCreate = thirdModel.gmtCreate;
    schoolInfo.headPortrait = thirdModel.headPortrait;
    schoolInfo.introduce = thirdModel.introduce;
    schoolInfo.isHttps = thirdModel.https;
    schoolInfo.logo = thirdModel.logoURL;
    schoolInfo.schoolId = thirdModel.schoolId;
    schoolInfo.schoolName = thirdModel.schoolName;
    schoolInfo.secondLevelDomain = thirdModel.secondLevelDomain;
    schoolInfo.topLevelDomain = thirdModel.topLevelDomain;
    return schoolInfo;
}
@end

@implementation VULThirdSchoolInfoModel

@end

#pragma mark - 提问属性
@implementation VULResponseAskInfoModel

+ (VULResponseAskInfoModel *)getAskModelWithDic:(NSDictionary *)dic {
    VULResponseAskInfoModel *askModel = [VULResponseAskInfoModel modelWithDictionary:dic];
    
    if ([askModel.detail containsString:@"<img"] && [askModel.detail containsString:@"src=\""]) {
        NSArray *imgUrlArr = [NSString getImageSrcWithString:askModel.detail];
        NSMutableArray *dataSourseUrlArr = [NSMutableArray arrayWithCapacity:0];
        for (NSString *imgUrlString in imgUrlArr) {
            NSString *picUrlStr = [NSString stringWithFormat:@"%@%@",ChooseUrl, imgUrlString];
            NSString *subStr = [imgUrlString substringToIndex:2];
            if ([subStr containsString:@"//"]) {
                picUrlStr = [NSString stringWithFormat:@"https:%@",imgUrlString];
            }
            [dataSourseUrlArr addObject:picUrlStr];
        }
        
        CGFloat jgg_Width = VULSCREEN_WIDTH_VARIABLE - 2 * kSpace - VULPercentageHeight(10);
        CGFloat image_Width = (jgg_Width - kSpace) / 3;
        CGFloat jgg_height = 0;
        if (dataSourseUrlArr.count == 0) {
            jgg_height = 0;
        } else if (dataSourseUrlArr.count <= 3) {
            jgg_height = image_Width;
        } else if (dataSourseUrlArr.count > 3 && dataSourseUrlArr.count <= 6) {
            jgg_height = 2 * image_Width + kSpace / 2;
        } else  if (dataSourseUrlArr.count > 6) {
            jgg_height = 3 * image_Width + kSpace;
        }
        askModel.imageContainerHeight = jgg_height;
        askModel.imageSourceArray = dataSourseUrlArr;
    }
    
    return askModel;
}

@end

#pragma mark - 评论 其他的的回答
@implementation VULResponseReplyModel

@end

#pragma mark - 回答问题
@implementation VULResponseAnswerModel

@end

#pragma mark - 钱包金额 余额之类信息
@implementation VULResponseWalletModel

@end

#pragma mark - 积分历史
@implementation VULResponseIntegralInfoModel

@end

@implementation VULResponseNoticeDetailModel

@end


#pragma mark- 试卷分类列表model
@implementation VULHomeworkClassifyListModel

@end

#pragma mark- 学生试卷列表model
@implementation VULHomeworkListModel

@end

#pragma mark - 学生端获取积分设置的配置
@implementation VULResponseGetScoreConfigModel

@end


#pragma mark - 首页轮播图
@implementation VULResponseGetSlideShowModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"slideId":@"id"};
}

@end

#pragma mark - 学习排名（TOP99)
@implementation VULResponseLearnRankModel

@end

#pragma mark -学习页统计数：
@implementation VULResponseStudyPageStatisticModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"homeworkCount":@"newHomeworkCount",@"replyCount": @"newReplyCount"};
}

@end

#pragma mark -获取热搜词
@implementation VULResponseHotSearchModel

@end

#pragma mark -购物车商品
@implementation VULResponseShoppingCarModel

@end

#pragma mark - 作品分类列表model
@implementation VULResponseAllOpusClassifyModel

@end

#pragma mark -学生作业列表model
@implementation VULResponseHomeworkModel

@end

#pragma mark -学生作业详情
@implementation VULResponseHomeworkDetailModel

//+ (NSDictionary *)modelContainerPropertyGenericClass {
//    return @{@"examInfo" : [VULExamInfoModel class],
//             @"examUser" : [VULExamUserModel class]
//    };
//}

@end

@implementation VULExamInfoModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"fileList" : [VULExamFileListInfoModel class],
             @"imageList" : [VULExamImageListInfoModel class]
    };
}

@end

@implementation VULExamFileListInfoModel

@end

@implementation VULExamImageListInfoModel

@end

@implementation VULExamUserModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"checkFileList" : [VULExamFileListInfoModel class],
             @"checkImageList" : [VULExamImageListInfoModel class],
             @"fileList" : [VULExamFileListInfoModel class],
             @"imageList" : [VULExamImageListInfoModel class]
    };
}

@end

#pragma mark - 获取商城分类列表
@implementation VULResponseShopClassifyModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"children" : [VULResponseSubShopClassifyModel class]};
}
@end

@implementation VULResponseSubShopClassifyModel

@end

#pragma mark - 获取资讯分类列表
@implementation VULHomepageListClassifyModel
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"children" : [VULSubHomepageListClassifyModel class]};
}
@end

@implementation VULSubHomepageListClassifyModel

@end

#pragma mark - 获取商城商品列表
@implementation VULResponseShopCommodityModel

@end

#pragma mark - 获取问卷列表model
@implementation VULResponseQuestionnaireModel

@end

#pragma mark - 教师直播页面各个操作链接
@implementation VULResponseTeacherOperateModel

@end

#pragma mark - 获取教师课程列表
@implementation VULResponseGetCourseListModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{@"teacherList" : [VULTeacherListModel class]};
}

@end

#pragma mark - 教师端 根据课件id获取作业列表
@implementation VULResponseGetHomeworkListModel

@end

#pragma mark - 获取网校成员列表
@implementation VULResponseSchoolStudentListModel

@end

#pragma mark - 教师端 获取网校班级列表
@implementation VULResponseGetClassListModel

@end

#pragma mark - 教师端 查询所有签到的日期，已签和未签
@implementation VULResponseSignDateModel

@end

#pragma mark - 教师端 根据日期查询某日的签到记录
@implementation VULResponseSignRecordModel

@end

#pragma mark -教师作业列表model
@implementation VULResponseExamModel

@end
