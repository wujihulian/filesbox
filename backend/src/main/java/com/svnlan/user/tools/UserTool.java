package com.svnlan.user.tools;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.FindTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.utils.DateUtil;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/20 13:38
 */
@Component
public class UserTool {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    public void validateBindEmailSendLimit(String prefix, Long userId, String email) {
        //用户发送绑定邮件验证码每日限制
        String emailBindLimitKey = String.format(GlobalConfig.EMAIL_BIND_SEND_LIMIT_KEY, userId);
        //用户发送绑定 同一邮箱 邮件验证码每日限制次数
        String singleEmailBindLimitKey = String.format(GlobalConfig.SINGLE_EMAIL_BIND_SEND_LIMIT_KEY, userId, email);

        //1秒内最多1次限制
        ValueOperations operations = this.stringRedisTemplate.opsForValue();
        String sendFrequencyKey = String.format(GlobalConfig.EMAIL_BIND_SEND_FREQUENCY_KEY, userId);
        Boolean isLock = operations.setIfAbsent(sendFrequencyKey, "1");
        if(null != isLock && isLock) {
            this.stringRedisTemplate.expire(sendFrequencyKey, 1, TimeUnit.SECONDS);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.codeErrorFreq.getCode());
        }

        int singleSendNum = 0;
        int totalSendNum = 0;
        //计算当天剩余的过期时间
        long currentTime = System.currentTimeMillis();
        Date nextDate = DateUtil.strToDate(DateUtil.yyyy_MM_dd, DateUtil.getPlusDaysStr(DateUtil.yyyy_MM_dd, new Date(), 1));
        long nextDateTime = nextDate.getTime();
        //当天的剩余时间
        long remainingTime = nextDateTime - currentTime;
        if(remainingTime < 0) {
            remainingTime = 0;
        }

        // 3600000
        Double a = Double.valueOf(remainingTime) / (60 * 60 * 1000);
        int hours = a.intValue();

        if (remainingTime > 0 && hours <= 0){
            hours = 1;
        }


        //改为先优先判断当天总次数已用完
        //一个用户， 任意邮箱 ，每天总共10次
        String totalSendLimitKey = String.format(GlobalConfig.EMAIL_BIND_SEND_LIMIT_KEY, userId);
        Object totalSendLimitCountObj = operations.get(totalSendLimitKey);
        if(null != totalSendLimitCountObj) {
            int totalSendLimitCount = Integer.parseInt(totalSendLimitCountObj.toString());
            //若超出用户发送绑定邮件验证码每日限制次数，则返回提示
            if(totalSendLimitCount >= GlobalConfig.EMAIL_BIND_SEND_LIMIT_NUM) {
                throw new SvnlanRuntimeException(CodeMessageEnum.codeErrorCnt.getCode(), hours);
            } else {
                this.stringRedisTemplate.expire(totalSendLimitKey, remainingTime, TimeUnit.MILLISECONDS);
                operations.increment(totalSendLimitKey, 1);
            }
        } else {
            this.stringRedisTemplate.expire(totalSendLimitKey, remainingTime, TimeUnit.MILLISECONDS);
            operations.increment(totalSendLimitKey, 1);
        }

        //一个用户， 一个邮箱 ，一天10次
        String singleSendLimitKey = String.format(GlobalConfig.SINGLE_EMAIL_BIND_SEND_LIMIT_KEY, userId, email);
        Object singleSendLimitCountObj = operations.get(singleSendLimitKey);
        if(null != singleSendLimitCountObj) {
            int singleSendLimitCount = Integer.parseInt(singleSendLimitCountObj.toString());
            //若超出用户发送绑定邮件验证码每日限制次数，则返回提示
            if(singleSendLimitCount >= GlobalConfig.SINGLE_EMAIL_BIND_SEND_LIMIT_NUM) {
                throw new SvnlanRuntimeException(CodeMessageEnum.codeErrorCnt.getCode(), hours);
            } else {
                this.stringRedisTemplate.expire(singleSendLimitKey, remainingTime, TimeUnit.MILLISECONDS);
                operations.increment(singleSendLimitKey, 1);
            }
        } else {
            this.stringRedisTemplate.expire(singleSendLimitKey, remainingTime, TimeUnit.MILLISECONDS);
            operations.increment(singleSendLimitKey, 1);
        }
    }
    public Boolean checkEmailCode(UserDTO optionDTO, boolean check) {
        if (ObjectUtils.isEmpty(optionDTO.getCode()) || ObjectUtils.isEmpty(optionDTO.getValue())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!checkCode(FindTypeEnum.EMAIL.getCode() + "_" + optionDTO.getOpType(), optionDTO.getCode(), optionDTO.getValue(), check)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.codeError.getCode());
        }
        return true;
    }

    public Boolean checkCode(String type, String code, String detail, boolean check) {
        String key = getKey(type, detail);
        LogUtil.info("checkCode key=" + key);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String codeValue = operations.get(key);
        LogUtil.info("checkCode key=" + key + "， code=" + codeValue);
        // 验证码失效
        if (StringUtil.isEmpty(codeValue) || !code.equals(codeValue)) {
            return false;
        }
        if (check) {
            stringRedisTemplate.delete(key);
        }
        return true;
    }
    public String getKey(String type, String detail) {
        return GlobalConfig.REDIS_KEY_USER_CODE + type + "_" + detail;
    }
}
