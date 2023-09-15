package com.svnlan.utils.timer;

import com.svnlan.utils.VisitRecordExecutor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static com.svnlan.utils.VisitRecordExecutor.DATE_COMPACT_FORMATTER;

/**
 * 访问数据统计 定时器
 *
 * @author lingxu 2023/04/11 16:04
 */
@Slf4j
public class VisitRecordSchedule {

    @Resource
    private VisitRecordExecutor visitRecordExecutor;

    @Resource
    private RedissonClient redissonClient;

    @SneakyThrows
    @Scheduled(cron = "0 0 0 * * ? ")
    public void executeDailyTask() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_COMPACT_FORMATTER));
        RLock lock = redissonClient.getLock("eDT:" + dateStr);
        if (lock.tryLock(0L, 2L, TimeUnit.HOURS)) {
            try {
                visitRecordExecutor.executeLastDayVisitRecord();
                // 将数据库中记录的访客操作系统列表更新到缓存
                visitRecordExecutor.updateOsNameSetFromDb();
                // 清理31天前的数据
                visitRecordExecutor.deleteHistoryVisitRecordDataBeforeDays(31);
            } finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
        } else {
            log.warn("另一个实例正在进行定时任务，ignore!");
        }
    }

}
