package com.svnlan.user.dao.Impl;

import com.svnlan.jooq.tables.Share;
import com.svnlan.user.dao.AdminShareDao;
import com.svnlan.user.dto.ShareDTO;
import com.svnlan.user.vo.ShareVo;
import com.svnlan.utils.DateUtil;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectOnConditionStep;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.svnlan.jooq.tables.Share.SHARE;
import static com.svnlan.jooq.tables.Users.USERS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/21 9:08
 */
@Service
public class AdminShareDaoImpl implements AdminShareDao {

    @Autowired
    private DSLContext context;

    @Override
    public List<ShareVo> shareList(ShareDTO dto, boolean needAll) {
        SelectJoinStep select = context.select(SHARE.ID.as("shareID"),SHARE.TITLE,SHARE.SOURCE_PATH.as("sourcePath"),SHARE.IS_LINK.as("isLink"),SHARE.PASSWORD,SHARE.URL
        ,SHARE.SHARE_HASH.as("shareHash"),SHARE.USER_ID.as("userID"),SHARE.SOURCE_ID.as("sourceID"),SHARE.NUM_VIEW.as("numView"),SHARE.NUM_DOWNLOAD.as("numDownload")
        ,SHARE.CREATE_TIME.as("createTime"),SHARE.TIME_TO.as("timeTo"),SHARE.IS_SHARE_TO.as("isShareTo"),USERS.AVATAR,USERS.NICKNAME,USERS.NAME)
                .from(SHARE)
                .leftJoin(USERS).on(SHARE.USER_ID.eq(USERS.ID));
        SelectConditionStep where = select.where(SHARE.TENANT_ID.eq(dto.getTenantId())).and(SHARE.STATUS.eq(1));
        if(!ObjectUtils.isEmpty(dto.getType()) && "isShareTo".equals(dto.getType())){
            where.and(SHARE.IS_SHARE_TO.eq(1));
        }else {
            where.and(SHARE.IS_SHARE_TO.eq(0));
        }
        if (!ObjectUtils.isEmpty(dto.getUserId())){
            where.and(SHARE.USER_ID.eq(dto.getUserId()));
        }
        if (!ObjectUtils.isEmpty(dto.getWords())){
            where.and(SHARE.TITLE.like((DSL.concat("%",dto.getWords(),"%"))));
        }
        if (!ObjectUtils.isEmpty(dto.getTimeTo())){
            where.and(SHARE.TIME_TO.le(dto.getTimeTo()));
        }
        if (!ObjectUtils.isEmpty(dto.getTimeFrom())){
            String timeForm = DateUtil.LongTimeToString(dto.getTimeFrom(),DateUtil.yyyy_MM_dd_HH_mm_ss);
            where.and(SHARE.CREATE_TIME.ge( LocalDateTime.parse(timeForm, DateTimeFormatter.ofPattern(DateUtil.yyyy_MM_dd_HH_mm_ss))));
        }
        if (!"create_time".equals(dto.getSortField())){
            select.orderBy("up".equals(dto.getSortType()) ? DSL.field(dto.getSortField()).asc() : DSL.field(dto.getSortField()).desc() , SHARE.CREATE_TIME.desc());
        }else {
            select.orderBy("up".equals(dto.getSortType()) ? SHARE.CREATE_TIME.asc() : SHARE.CREATE_TIME.desc());
        }
        if (needAll){
            select.limit(dto.getStartIndex().intValue(),dto.getPageSize());
        }
        return select.fetchInto(ShareVo.class);
    }

    @Override
    public Integer shareListCount(ShareDTO dto) {
        SelectJoinStep select = context.selectCount().from(SHARE);
        SelectConditionStep where = select.where(SHARE.TENANT_ID.eq(dto.getTenantId())).and(SHARE.STATUS.eq(1));
        if(!ObjectUtils.isEmpty(dto.getType()) && "isShareTo".equals(dto.getType())){
            where.and(SHARE.IS_SHARE_TO.eq(1));
        }else {
            where.and(SHARE.IS_SHARE_TO.eq(0));
        }
        if (!ObjectUtils.isEmpty(dto.getUserId())){
            where.and(SHARE.USER_ID.eq(dto.getUserId()));
        }
        if (!ObjectUtils.isEmpty(dto.getWords())){
            where.and(SHARE.TITLE.like((DSL.concat("%",dto.getWords(),"%"))));
        }
        if (!ObjectUtils.isEmpty(dto.getTimeTo())){
            where.and(SHARE.TIME_TO.le(dto.getTimeTo()));
        }
        if (!ObjectUtils.isEmpty(dto.getTimeFrom())){
            String timeForm = DateUtil.LongTimeToString(dto.getTimeFrom(),DateUtil.yyyy_MM_dd_HH_mm_ss);
            where.and(SHARE.CREATE_TIME.ge( LocalDateTime.parse(timeForm, DateTimeFormatter.ofPattern(DateUtil.yyyy_MM_dd_HH_mm_ss))));
        }
        return (Integer) where.fetchOneInto(Integer.class);
    }

    @Override
    public int cancelShare(Long id) {
        return context.delete(SHARE)
                .where(SHARE.ID.eq(id))
                .execute();
    }

    @Override
    public void cancelMultiShare(List<Long> ids) {
        context.update(SHARE).set(SHARE.STATUS,4).where(SHARE.ID.in(ids)).execute();
    }

    @Override
    public ShareVo getById(Long id) {
        return context.select(SHARE.ID.as("shareID"),SHARE.TITLE,SHARE.USER_ID.as("userID"),SHARE.SOURCE_ID.as("sourceID"),SHARE.SOURCE_PATH.as("sourcePath")
                ,SHARE.URL,SHARE.IS_LINK.as("isLink"),SHARE.STATUS,SHARE.IS_SHARE_TO.as("isShareTo"),SHARE.PASSWORD,SHARE.TIME_TO.as("timeTo"),SHARE.NUM_VIEW.as("numView")
                ,SHARE.NUM_DOWNLOAD.as("numDownload"),SHARE.OPTIONS,SHARE.CREATE_TIME.as("createTime"),SHARE.MODIFY_TIME.as("modifyTime"),SHARE.TENANT_ID.as("modifyTime")
        ).from(SHARE).where(SHARE.ID.eq(id)).fetchOneInto(ShareVo.class);
    }

    @Override
    public List<ShareVo> getByIds(ArrayList<Integer> idList) {
        return context.select(SHARE.ID.as("shareID"),SHARE.USER_ID.as("userID"),SHARE.SOURCE_ID.as("sourceID")
        ).from(SHARE).where(SHARE.ID.in(idList)).fetchInto(ShareVo.class);
    }
}
