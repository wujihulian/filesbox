package com.svnlan.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * redis配置
 *
 * @author lingxu 2023/04/07 10:11
 */
@Configuration
public class RedisConfig {


    //读取配置文件中redis的配置
//    @Bean
//    @ConfigurationProperties(prefix = "spring.redis")
//    public JedisConnectionFactory jedisConnectionFactory() {
//        return new JedisConnectionFactory();
//    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
        //String
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //JdkSerialization
        //JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSgiterializationRedisSerializer();

        //Jackson2Json
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        //PropertyAccessor.ALL：所有；JsonAutoDetect.Visibility.ANY修饰范围：ANY-所有
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型，类必须是非final修饰的
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //连接工厂
        template.setConnectionFactory(factory);
        //全局key的序列化策略
        template.setKeySerializer(stringRedisSerializer);
        //全局value的序列化策略
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //全局HashKey的序列化策略
        template.setHashKeySerializer(stringRedisSerializer);
        //全局HashValue的序列化策略
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        //支持事务
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Resource
    private Environment environment;

    // 这些环境的 redis 配置将被认做 single
    private static final List<String> redisSingleList = Arrays.asList("local", "dev", "pro", "pufay");

    @ConditionalOnBean(name = "redisTemplate")
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(RedisProperties redisProperties) {
        // 创建配置 指定redis地址及节点信息
        Config config = new Config();
        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(redisSingleList::contains)) {
            SingleServerConfig singleServerConfig = config.useSingleServer()
                    .setDatabase(redisProperties.getDatabase())
                    .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
            if (StringUtils.hasText(redisProperties.getPassword())) {
                singleServerConfig.setPassword(redisProperties.getPassword());
            }
        } else {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            for (String node : redisProperties.getCluster().getNodes()) {
                clusterServersConfig
                        .addNodeAddress("redis://" + node);
            }
            clusterServersConfig.setScanInterval(2000);
            if (StringUtils.hasText(redisProperties.getPassword())) {
                clusterServersConfig.setPassword(redisProperties.getPassword());
            }
        }


        // 根据config创建出RedissonClient实例
        return Redisson.create(config);
    }

}
