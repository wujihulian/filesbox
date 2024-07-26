package com.svnlan.user.dao;

import com.svnlan.user.domain.VisitCountRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lingxu
 * @description 针对表【visit_count_record】的数据库操作Mapper
 * @createDate 2023-04-07 15:39:54
 * @Entity com.svnlan.user.vo.VisitCountRecordVo
 */
public interface VisitCountRecordDao {


    int updateByPrimaryKeySelective(VisitCountRecord record, Integer isIncrease);


    void insertBatch(List<VisitCountRecord> records);

    // deviceType is null, 表示是合并的用户访问数据
//    @Select("SELECT SUM(visitCount) FROM visit_count_record WHERE type = 5")
    Long getCumulativeVisitTotal();

    List<VisitCountRecord> selectByOsNameAndType(List<VisitCountRecord> list);

    List<VisitCountRecord> selectListByOsNameList(List<String> nullOsName, Long tenantId);

    //    @Select("SELECT visitCount, deviceType, visitDay FROM visit_count_record WHERE visitDay >= #{startDate} AND visitDay <= #{endDate} AND type = 1 AND deviceType IS NOT NULL")
    List<VisitCountRecord> queryDeviceClientVisitData(LocalDate startDate, LocalDate endDate);

    //    @Select("SELECT id FROM visit_count_record WHERE type = #{type} AND DATE_FORMAT(visitDay,'%Y-%m') = #{visitPerMonth}")
    VisitCountRecord selectByVisitDayAndType(String visitPerMonth, int type);

    //    @Update("UPDATE visit_count_record SET visitCount = visitCount + #{visitCountOneDay} WHERE id = #{id}")
    int updateVisitCount(Long id, Long visitCountOneDay);

    //    @Insert("insert into visit_count_record ( visitCount, visitDay, type, modifyTime, createTime ) values ( #{record.visitCount}, #{record.visitDay}, #{record.type}, #{record.modifyTime}, #{record.createTime})")
    void insertUserVisitRecord(VisitCountRecord record);


    //    @Select("SELECT osName FROM visit_count_record WHERE type = 4")
    List<String> selectOSNameList();

}
