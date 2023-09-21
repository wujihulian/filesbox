package com.svnlan.user.dao;

import com.svnlan.user.domain.VisitCountRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.util.List;

/**
* @author lingxu
* @description 针对表【visit_count_record】的数据库操作Mapper
* @createDate 2023-04-07 15:39:54
* @Entity com.svnlan.user.vo.VisitCountRecordVo
 */
public interface VisitCountRecordDao {

    int deleteByPrimaryKey(Long id);

    int insert(VisitCountRecord record);

    int insertSelective(VisitCountRecord record);

    VisitCountRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(@Param("record") VisitCountRecord record, @Param("isIncrease") Integer isIncrease);

    int updateByPrimaryKey(VisitCountRecord record);

    void insertBatch(@Param("records") List<VisitCountRecord> records);

    // deviceType is null, 表示是合并的用户访问数据
    @Select("SELECT SUM(visitCount) FROM visit_count_record WHERE `type` = 5")
    Long getCumulativeVisitTotal();

    List<VisitCountRecord> selectByOsNameAndType(@Param("records") List<VisitCountRecord> list);

    List<VisitCountRecord> selectListByOsNameList(@Param("list") List<String> nullOsName);

    @Select("SELECT visitCount, deviceType, visitDay FROM visit_count_record WHERE visitDay >= #{startDate} AND visitDay <= #{endDate} AND `type` = 1 AND deviceType IS NOT NULL")
    List<VisitCountRecord> queryDeviceClientVisitData(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    @Select("SELECT id FROM visit_count_record WHERE `type` = #{type} AND DATE_FORMAT(visitDay,'%Y-%m') = #{visitPerMonth}")
    VisitCountRecord selectByVisitDayAndType(@Param("visitPerMonth") String visitPerMonth,@Param("type") int type);

    @Update("UPDATE visit_count_record SET visitCount = visitCount + #{visitCountOneDay} WHERE id = #{id}")
    int updateVisitCount(@Param("id")Long id,@Param("visitCountOneDay") Long visitCountOneDay);

    @Insert("insert into visit_count_record ( visitCount, visitDay, `type`, modifyTime, createTime ) values ( #{record.visitCount}, #{record.visitDay}, #{record.type}, #{record.modifyTime}, #{record.createTime})")
    void insertUserVisitRecord(@Param("record") VisitCountRecord record);


    @Select("SELECT osName FROM visit_count_record WHERE type = 4")
    List<String> selectOSNameList();

}
