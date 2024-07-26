//package com.svnlan.config;
//
//import com.github.pagehelper.PageHelper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import java.util.Properties;
//
///**
// * mybatis配置
// *
// * @author lingxu 2023/05/25 10:02
// */
//@Configuration
//public class MybatisConfig {
//    /**
//     * mp 分页插件
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor interceptor = new PaginationInterceptor();
//        interceptor.setDialectType(DbType.MYSQL.getDb());
//        return interceptor;
//    }
//
//    /**
//     * 性能分析插件
//     */
//    @Bean
//    @Profile({"dev", "test"})
//    public PerformanceInterceptor performanceInterceptor() {
//        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//        performanceInterceptor.setMaxTime(15000);
//        performanceInterceptor.setFormat(false);
//        return performanceInterceptor;
//    }
//
//    /**
//     * 功能描述: 配置mybatis的分页插件pageHelper
//     *
//     * @param:
//     * @return:
//     */
//    @Bean
//    public PageHelper pageHelper() {
//        PageHelper pageHelper = new PageHelper();
//        Properties properties = new Properties();
//        properties.setProperty("offsetAsPageNum", "true");
//        properties.setProperty("rowBoundsWithCount", "true");
//        properties.setProperty("reasonable", "false");
//        //配置mysql数据库的方言
//        properties.setProperty("dialect", "mysql");
//        pageHelper.setProperties(properties);
//        return pageHelper;
//    }
//}
