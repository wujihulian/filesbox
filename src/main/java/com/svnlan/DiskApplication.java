package com.svnlan;

import com.svnlan.annotation.EnableVisitRecord;
import com.svnlan.utils.SpringUtil;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
// mapper 接口类扫描包配置
@MapperScan("com.svnlan.**.dao")
@EnableAsync
@EnableVisitRecord
@ServletComponentScan(basePackages = {"com.svnlan.interceptor", "com.svnlan.webdav"})
@Import(value={SpringUtil.class})
@EnableEncryptableProperties
public class DiskApplication {
    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverterList = new ArrayList<>();
        for (HttpMessageConverter<?> mc : restTemplate.getMessageConverters()) {
            if (!(mc instanceof MappingJackson2XmlHttpMessageConverter)) {
                messageConverterList.add(mc);
            }
        }
        restTemplate.setMessageConverters(messageConverterList);
        return restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(DiskApplication.class, args);
    }

    /** 用做异步 */
    @Bean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //核心线程数
        taskExecutor.setCorePoolSize(3);
        //线程池维护线程的最大数量,只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(20);
        //缓存队列
        taskExecutor.setQueueCapacity(15);
        //许的空闲时间,当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        taskExecutor.setKeepAliveSeconds(200);
        //异步方法内部线程名称
        taskExecutor.setThreadNamePrefix("async-disk-");
        /**
         * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略
         * 通常有以下四种策略：
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：重试添加当前的任务，自动重复调用 execute() 方法，直到成功
         */
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * 默认线程池线程池
     *
     * @return Executor
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolDefault() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数目
        executor.setCorePoolSize(24);
        //指定最大线程数
        executor.setMaxPoolSize(64);
        //队列中最大的数目
        executor.setQueueCapacity(24);
        //线程名称前缀
        executor.setThreadNamePrefix("diskThreadPool_");
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(120);
        //加载
        executor.initialize();
        return executor;
    }

    @Bean(name="encryptorBean")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword("WJHL2023");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }


}
