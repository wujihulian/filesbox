package com.svnlan.user.service;

/**
 * 概览 服务
 *
 * @author lingxu 2023/04/10 13:10
 */
public interface OverviewService {

    Object getCumulativeVisitTotal();

    Object getFileRankingAndProportion(Integer topCount);
}
