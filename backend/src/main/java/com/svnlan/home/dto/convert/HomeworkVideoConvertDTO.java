package com.svnlan.home.dto.convert;

import java.util.List;

/**
 * @Author:
 * @Description:
 */
public class HomeworkVideoConvertDTO {
    private List<Long> sourceIdList;

    public List<Long> getSourceIdList() {
        return sourceIdList;
    }

    public void setSourceIdList(List<Long> sourceIdList) {
        this.sourceIdList = sourceIdList;
    }
}
