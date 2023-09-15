package com.svnlan.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.svnlan.annotation.VisitRecord;
import com.svnlan.enums.ClientTypeEnum;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.VisitCountRecordDao;
import com.svnlan.user.domain.VisitCountRecord;
import io.jsonwebtoken.lang.Assert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.activation.UnsupportedDataTypeException;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.svnlan.utils.VisitRecordExecutor.DataType.DEVICE_VISIT;
import static com.svnlan.utils.VisitRecordExecutor.DataType.OS_NAME_VISIT;

/**
 * 访问统计处理
 *
 * @author lingxu 2023/04/07 11:12
 */
@Slf4j
@Component
public class VisitRecordExecutor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private VisitCountRecordDao visitCountRecordDao;

    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private RedissonClient redissonClient;


    private final static Integer perBatchCount = 500;

    private static final String PREFIX = "vr:";
    private static final String PREFIX_MERGE = "vrm:";
    private static final String PREFIX_OS = "os_vr:";
    private static final String PREFIX_OS_MERGE = "os_vrm:";
    private static final String PREFIX_OS_MERGE_TOTAL = "os_vrm_t:";
    public static final String VISIT_TOTAL_COUNT_KEY = "visit_total_count";
    private static final String FORMATTER = "%s_%d";
    private static final String FORMATTER_STRING = "%s_%s";
    public static final String DATE_COMPACT_FORMATTER = "yyyyMMdd";
    private static final String OS_NAME_COL_KEY = "os_name_col_key";

    public static final String DEVICE_TYPE_RECORD_CACHE_KEY = "DEVICE_TYPE_RECORD_CACHE_KEY";

    /**
     * 执行记录访问数据
     *
     * @param pair        客户端类型 操作系统类型
     * @param loginUser   当前登录用户
     * @param recordTypes 记录类型 see {@link VisitRecord.RecordType}
     * @param timeType    时间类型 see {@link VisitRecord.TimeType}
     */
    public void executeRecord(Pair<Integer, String> pair, LoginUser loginUser, VisitRecord.RecordType[] recordTypes, VisitRecord.TimeType[] timeType) {
        Long userId = loginUser.getUserID();

        if (isNeedToRecord(recordTypes, VisitRecord.RecordType.CLIENT_TYPE)) {
            doRecord(pair.getFirst(), userId, timeType, PREFIX, false);
        }
        if (isNeedToRecord(recordTypes, VisitRecord.RecordType.OS_NAME)) {
            doRecord(pair.getSecond(), userId, timeType, PREFIX_OS_MERGE_TOTAL, true);
            // 存入 redis set类型数据中
            stringRedisTemplate.opsForSet().add(OS_NAME_COL_KEY, pair.getSecond());
        }
    }

    private void doRecord(Object type, Long userId, VisitRecord.TimeType[] timeType, String prefix, boolean reverse) {
        if (type instanceof String) {
            // 去掉中间的空格，比如 Windows 10 需要变为 Windows10
            type = ((String) type).replace(" ", "");
        }
        // 当前时间戳
        LocalDateTime now = LocalDateTime.now();
        long timestamp = getTimestampByTimeType(now, timeType, reverse);
        if (timestamp != -1) {
            String key;
            if (PREFIX_OS_MERGE_TOTAL.equals(prefix)) {
                key = ((String) type);
            } else {
                key = now.format(DateTimeFormatter.ofPattern(DATE_COMPACT_FORMATTER)) + "_" + type;
            }
//            stringRedisTemplate.opsForHyperLogLog().add(prefix + key, String.format(FORMATTER, timestamp, userId));
            redisTemplate.opsForValue().increment(prefix + key);
            if (log.isInfoEnabled()) {
                Object s = redisTemplate.opsForValue().get(prefix + key);
                log.warn("s ==> prefix:{} key:{}  {}", prefix, key, s);
            }

        }
    }

    /**
     * 根据对应的时间类型获取时间戳
     *
     * @param now      当前时间
     * @param timeType 时间类型
     * @param reverse  是否反转匹配 对于 clientType 类型 为正向匹配  osName 为反向匹配
     * @return 对应时间类型的时间戳
     */
    private long getTimestampByTimeType(LocalDateTime now, VisitRecord.TimeType[] timeType, boolean reverse) {
        List<Pair<VisitRecord.TimeType, Supplier<Long>>> timeList = VisitRecord.TimeType.getTimeList(now);

        int index = reverse ? timeList.size() - 1 : 0;

        for (; ; ) {
            if (VisitRecord.TimeType.isContains(timeType, timeList.get(index).getFirst())) {
                System.out.println(timeList.get(index));
                return timeList.get(index).getSecond().get();
            }
            if (reverse) index--;
            else index++;

            if (index < 0 || index >= timeList.size()) {
                break;
            }
        }
        return -1;
    }


    /**
     * 判断是否需要记录数据
     */
    private boolean isNeedToRecord(VisitRecord.RecordType[] recordTypes, VisitRecord.RecordType recordType) {
        return Arrays.stream(recordTypes).anyMatch(it -> it == recordType)
                || Arrays.stream(recordTypes).anyMatch(it -> it == VisitRecord.RecordType.ALL);
    }

    /**
     * 通过给定的 localDate 计算出 设备序列key 还包含合并后的key
     */
    private Pair<String, String[]> calculateKeyWithLocalDate(LocalDate localDate, String prefix) {
        if (Objects.isNull(localDate)) {
            localDate = LocalDate.now();
        }
        // 设备code 集合
        List<String> deviceTypeCodeList = ClientTypeEnum.getCodeList();
        String key = localDate.format(DateTimeFormatter.ofPattern(DATE_COMPACT_FORMATTER));
        String[] deviceTypeKeyList = deviceTypeCodeList.stream().map(it -> String.format("%s%s_%s", prefix, key, it)).toArray(String[]::new);
        return Pair.of(key, deviceTypeKeyList);
    }


    private void executeVisitCountPerMonth(LocalDate localDate, Long visitCountOneDay) {

        String visitPerMonth = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        VisitCountRecord visitCountRecord = visitCountRecordDao.selectByVisitDayAndType(visitPerMonth, 5);
        if (Objects.nonNull(visitCountRecord)) {
            log.info("更新月份访问总数 nonNull => {}, {} ", localDate, visitPerMonth);
            // 更新
            Assert.isTrue(visitCountRecordDao.updateVisitCount(visitCountRecord.getId(), visitCountOneDay) == 1, "更新访问次数失败");
        } else {
            log.info("写入月份访问总数 isNull => {}, {} ", localDate, visitPerMonth);
            // 则写入数据库中
            VisitCountRecord record = new VisitCountRecord(visitCountOneDay, null, 5, localDate);
            log.info("VisitCountRecord record =>{}", record);
            visitCountRecordDao.insertUserVisitRecord(record);
        }
        log.info("总访问次数更新到缓存 => {}", visitCountRecord);
        // 更新到缓存上
        redisTemplate.opsForValue().increment(VISIT_TOTAL_COUNT_KEY, visitCountOneDay);

    }

    /**
     * 合并某一天的访问统计数据
     * 默认为当天
     */
    @Deprecated
    public void mergeRecordWithPerDay(LocalDate localDate) {
        // 【设备】
        // 按设备类型合并某一天的访问统计数据
        // key 为 localDate 格式化后的字符串 value 为 各种设备
        Pair<String, String[]> deviceTypeKeyList = calculateKeyWithLocalDate(localDate, PREFIX);
        List<String> clientTypeList = Arrays.asList(deviceTypeKeyList.getSecond());
        // 从缓存中查询设备访问数据
        List<Object> perClientNameCount = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            for (String clientType : clientTypeList) {
                conn.stringCommands().get(clientType.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
        // 根据查询出来的数据构造 VisitCountRecord集合
        List<VisitCountRecord> visitCountRecordInsert = new ArrayList<>();
        long visitCountOneDay = 0L;
        for (int index = 0; index < clientTypeList.size(); index++) {
            // 设备类型
            String clientType = clientTypeList.get(index);
            // 访问次数
            long count = Long.parseLong(Optional.ofNullable(perClientNameCount.get(index)).orElse(0).toString());
            visitCountOneDay += count;
            visitCountRecordInsert.add(new VisitCountRecord(count, Integer.parseInt(clientType.split("_")[1]), 1, localDate));
        }
        // 设备访问数据 写入数据库
        if (!CollectionUtils.isEmpty(visitCountRecordInsert)) {
            log.info("待写入的设备访问数据 => {}", JSONObject.toJSONString(visitCountRecordInsert));

            visitCountRecordDao.insertBatch(visitCountRecordInsert);
            log.info("当天累加的访问次数 => {}", visitCountOneDay);
            if (visitCountOneDay > 0) {
                executeVisitCountPerMonth(localDate, visitCountOneDay);
            }
        }

        // 【操作系统】
        // 按操作系统类型合并某一天的访问统计数据
        Set<String> osNameListTemp = stringRedisTemplate.opsForSet().members(OS_NAME_COL_KEY);
        if (CollectionUtils.isEmpty(osNameListTemp)) {
            return;
        }
        List<String> osNameList = new ArrayList<>(osNameListTemp);

        // 从缓存中查询到数据
        List<Long> cacheCountList = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            for (String s : osNameList) {
                conn.stringCommands().get((PREFIX_OS_MERGE_TOTAL + s).getBytes(StandardCharsets.UTF_8));
            }
            return null;
        }).stream().map(it -> Long.parseLong(Optional.ofNullable(it).orElse(0).toString())).collect(Collectors.toList());
        // 组装成 visitCountRecord
        List<VisitCountRecord> cacheVisitCountRecord = new ArrayList<>();
        for (int i = 0; i < osNameList.size(); i++) {
            cacheVisitCountRecord.add(new VisitCountRecord(cacheCountList.get(i), 4, localDate, osNameList.get(i)));
        }
        // 从数据库查询到数据
        List<VisitCountRecord> dbVisitCountRecordList = visitCountRecordDao.selectByOsNameAndType(cacheVisitCountRecord);

        // 分离出哪些需要新增到数据库， 哪些需要更新到数据库
        Pair<List<VisitCountRecord>, List<VisitCountRecord>> listListPair = extractVisitCountRecordList(cacheVisitCountRecord, dbVisitCountRecordList);
        List<VisitCountRecord> needUpdate = listListPair.getFirst();
        if (!CollectionUtils.isEmpty(needUpdate)) {
            needUpdate.stream()
                    .peek(it -> it.setModifyTime(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8))))
                    .forEach(it -> visitCountRecordDao.updateByPrimaryKeySelective(it, 0));
        }

        List<VisitCountRecord> needInsert = listListPair.getSecond();
        if (!CollectionUtils.isEmpty(needInsert)) {
            visitCountRecordDao.insertBatch(needInsert);
        }

        // 删除设备数据访问缓存
        stringRedisTemplate.unlink(DEVICE_TYPE_RECORD_CACHE_KEY);
    }

    private Pair<List<VisitCountRecord>, List<VisitCountRecord>> extractVisitCountRecordList(
            List<VisitCountRecord> cacheVisitCountRecord,
            List<VisitCountRecord> dbVisitCountRecord) {
        List<VisitCountRecord> needUpdate = new ArrayList<>();
        for (VisitCountRecord cacheData : cacheVisitCountRecord) {
            for (VisitCountRecord dbData : dbVisitCountRecord) {
                if (cacheData.equals(dbData)) {
                    Long cacheVisitCount = Optional.ofNullable(cacheData.getVisitCount()).orElse(0L);
                    Long dbVisitCount = Optional.ofNullable(dbData.getVisitCount()).orElse(0L);
                    cacheData.setId(dbData.getId());
                    if (cacheVisitCount < dbVisitCount) {
                        // 缓存中的数据比数据库中的小，可能是缓存中的数据被清理过
                        cacheData.setVisitCount(cacheVisitCount + dbVisitCount);
                    }
                    needUpdate.add(cacheData);
                    break;
                }
            }
        }
        if (!CollectionUtils.isEmpty(needUpdate)) {
            cacheVisitCountRecord.removeAll(needUpdate);
        }
        return Pair.of(needUpdate, cacheVisitCountRecord);
    }

    private void clearCacheData(List<String> osCurrentDaySet) {
        // 删除当天的 操作系统访问统计数据
        stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            byte[][] bytes = osCurrentDaySet.stream().map(it -> it.getBytes(StandardCharsets.UTF_8)).toArray(byte[][]::new);
            conn.unlink(bytes);
            return null;
        });
        // 删除缓存中的 设备访问数据
        stringRedisTemplate.unlink(DEVICE_TYPE_RECORD_CACHE_KEY);
    }

    /**
     * 上面👆方法的重载方法，默认合并当天的数据
     */
    @Deprecated
    public void mergeRecordWithPerDay() {
        mergeRecordWithPerDay(null);
    }


    /**
     * 获取访问数据
     *
     * @param daysBefore 多少天以前
     * @param dataType   数据类型 用户访问数据  用户设备端访问数据
     */
    @SuppressWarnings("unchecked")
    public List<?> getVisitRecordList(Integer daysBefore, DataType dataType) {

        List<?> daysStringList = getDaysBeforeDateList(daysBefore, dataType);

        if (dataType == DataType.USER_VISIT) {
            List<String> dateList = (List<String>) daysStringList;
            return queryWithHyperLogLog(dateList);
        } else {
            // 先判断缓存里是否还有数据 有可能被清理了，就需要查表
//            Set<String> keys = stringRedisTemplate.keys(PREFIX + "*");
            LocalDate startDate = LocalDate.now().minusDays(daysBefore);
            String resultStr = stringRedisTemplate.opsForValue().get(DEVICE_TYPE_RECORD_CACHE_KEY);
            if (StringUtils.hasText(resultStr)) {
                TypeReference<List<List<Long>>> reference = new TypeReference<List<List<Long>>>() {
                };
                return JSONObject.parseObject(resultStr, reference);
            } else {
                // 缓存中没有对应的key 或者没有最开始那天的key 都需要查表
                LocalDate endDate = LocalDate.now().minusDays(1);

                List<VisitCountRecord> visitCountRecords = visitCountRecordDao.queryDeviceClientVisitData(startDate, endDate);
                asyncTaskExecutor.execute(() -> clientVisitCountToCache(visitCountRecords));

                List<List<Long>> lists = executeVisitCountRecords(visitCountRecords, daysBefore);
                stringRedisTemplate.opsForValue().set(DEVICE_TYPE_RECORD_CACHE_KEY, JSONObject.toJSONString(lists));
                return lists;
            }
        }
    }

    private void clientVisitCountToCache(List<VisitCountRecord> visitCountRecords) {
        // 缓存中没有 vr: 开头的数据，需要把从数据库中查询的数据设置到缓存中
        Set<String> keys = stringRedisTemplate.keys(PREFIX);
        if (CollectionUtils.isEmpty(visitCountRecords) || !CollectionUtils.isEmpty(keys)) {
            return;
        }

        List<Pair<String, Long>> list = visitCountRecords.stream().map(it ->
                Pair.of(String.format("%s%s_%s", PREFIX, it.getVisitDay().format(DateTimeFormatter.ofPattern(DATE_COMPACT_FORMATTER)), it.getDeviceType()), it.getVisitCount())
        ).collect(Collectors.toList());
        stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            for (Pair<String, Long> pair : list) {
                conn.stringCommands().set(pair.getFirst().getBytes(StandardCharsets.UTF_8), pair.getSecond().toString().getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
    }

    private List<List<Long>> executeVisitCountRecords(List<VisitCountRecord> visitCountRecords, Integer daysBefore) {
        LocalDate startDate = LocalDate.now().minusDays(daysBefore);
        List<Integer> typeList = ClientTypeEnum.getCodeList(excludeList).stream().map(Integer::new).collect(Collectors.toList());

        List<List<Long>> result = typeList.stream().map(it -> Stream.iterate(0L, UnaryOperator.identity()).limit(daysBefore).collect(Collectors.toList())).collect(Collectors.toList());
        for (VisitCountRecord item : visitCountRecords) {
            int outIndex = typeList.indexOf(item.getDeviceType());
            int innerIndex = Period.between(startDate, item.getVisitDay()).getDays();
//            log.info("outIndex => {}, innerIndex => {}", outIndex, innerIndex);
//            log.info("vD => {} dT => {}  oT => {}  iI => {} c => {}",item.getVisitDay(),item.getDeviceType(), outIndex, innerIndex, item.getVisitCount());
            if (outIndex != -1) {
                result.get(outIndex).set(innerIndex, item.getVisitCount());
            }
        }
        return result;
    }

    public List<JSONObject> getOsNameRecordList() {
        Set<String> members = stringRedisTemplate.opsForSet().members(OS_NAME_COL_KEY);
        if (CollectionUtils.isEmpty(members)) {
            return Collections.emptyList();
        }
        List<String> osNameList = new ArrayList<>();

        List<byte[]> osNameBytes = members.stream()
                .map(it -> it.replaceAll("\"", ""))
                .peek(osNameList::add)
                .map(it -> (PREFIX_OS_MERGE_TOTAL + it).getBytes(StandardCharsets.UTF_8)).collect(Collectors.toList());
        List<Object> osNameVisitCountList = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            osNameBytes.forEach(it -> conn.stringCommands().get(it));
            return null;
        });

        log.warn("List<String> osNameList => {}", osNameList);
        osNameList = osNameList.stream().distinct().collect(Collectors.toList());
        List<JSONObject> list = new ArrayList<>();
        List<String> nullOsName = new ArrayList<>();
        for (int i = 0; i < osNameList.size(); i++) {
            Object obj = osNameVisitCountList.get(i);
            if (Objects.isNull(obj)) {
                // 记录下， 待会从数据库中查询，然后更新到缓存上
                nullOsName.add(osNameList.get(i));
            }
            list.add(new JSONObject().fluentPut("osName", osNameList.get(i))
                    .fluentPut("count", Optional.ofNullable(obj).orElse(0)));
        }


        if (!CollectionUtils.isEmpty(nullOsName)) {
            RLock lock = redissonClient.getLock(VisitRecordExecutor.class.getName());
            try {
                if (lock.tryLock()) {
                    asyncTaskExecutor.execute(() -> {
                        // 从数据库中查询出数据
                        List<VisitCountRecord> dbList = visitCountRecordDao.selectListByOsNameList(nullOsName);
                        if (CollectionUtils.isEmpty(dbList)) {
                            return;
                        }
                        // 设置到缓存上
                        dbList.forEach(it -> redisTemplate.opsForValue().set(PREFIX_OS_MERGE_TOTAL + it.getOsName(), it.getVisitCount()));
                    });
                }
            } finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
        }
        return list;
    }

    /**
     * 从 redis 中 根据时间序列集合获取对应的访问数据
     *
     * @param dateList 时间序列集合
     * @return 对应的访问次数
     */
    @Deprecated
    private List<Object> queryWithHyperLogLog(List<String> dateList) {
        List<Object> dayCount;
        if (dateList.size() <= perBatchCount) {
            dayCount = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
                dateList.stream().map(it -> it.getBytes(StandardCharsets.UTF_8))
                        .forEach(it -> conn.hyperLogLogCommands().pfCount(it));
                return null;
            });
        } else {
            int startIndex = 0;
            dayCount = new ArrayList<>();
            for (; ; ) {
                List<String> subDaysStringList = dateList.subList(startIndex, perBatchCount);
                if (subDaysStringList.size() == 0) {
                    break;
                }
                List<Object> tempDayCount = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
                    dateList.stream().map(it -> it.getBytes(StandardCharsets.UTF_8))
                            .forEach(it -> conn.hyperLogLogCommands().pfCount(it));
                    return null;
                });
                dayCount.addAll(tempDayCount);
                startIndex += perBatchCount;
                if (startIndex >= dateList.size()) {
                    break;
                }
            }
        }
        return dayCount;
    }

    /**
     * 客户端 用户访问数据
     * 获取前一天的访问数据
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    private Pair<Long, List<Object>> queryClientAndUserVisitWithHyperLogLogLastDay() {
        // 查询用户访问数据
        String dateStr = (String) getDaysBeforeDateList(1, DataType.USER_VISIT).get(0);
        Long userVisitCount = stringRedisTemplate.opsForHyperLogLog().size(dateStr);

        // 查询设备访问数据
        List<List<String>> dateStrListList = (List<List<String>>) getDaysBeforeDateList(1, DataType.DEVICE_VISIT);
        List<String> dateStrList = dateStrListList.stream().map(it -> it.get(0)).collect(Collectors.toList());
        List<Object> deviceVisitCountList = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            dateStrList.stream().map(it -> it.getBytes(StandardCharsets.UTF_8))
                    .forEach(it -> conn.hyperLogLogCommands().pfCount(it));
            return null;
        });
        return Pair.of(userVisitCount, deviceVisitCountList);
    }

    /**
     * 将数据库中记录的访客操作系统列表更新到缓存
     */
    public void updateOsNameSetFromDb() {
        List<String> osNameList = visitCountRecordDao.selectOSNameList();
        if (CollectionUtils.isEmpty(osNameList)) {
            return;
        }
        // 将 osNameList 数据写入缓存
        osNameList.forEach(it -> stringRedisTemplate.opsForSet().add(OS_NAME_COL_KEY, it));
    }

    public enum DataType {
        // 用户访问数据
        USER_VISIT,
        // 用户设备端访问数据
        DEVICE_VISIT,
        // 操作系统类型
        OS_NAME_VISIT
    }

    private static List<String> excludeList = Arrays.asList(ClientTypeEnum.other.getCode(), ClientTypeEnum.mini.getCode());

    /**
     * 获取 daysBefore 天前的时间序列字符串， 如果 dataType = DEVICE_VISIT ，还需要绑定对应的设备类型
     *
     * @param daysBefore 表示是多少天以前
     * @param dataType   数据类型
     * @return 时间序列 dataType = USER_VISIT 返回 List<String>
     * 如果 dataType = DEVICE_VISIT 返回 List<List<String>> 外层 List 表示设备类型
     */
    @SneakyThrows
    private List<?> getDaysBeforeDateList(Integer daysBefore, DataType dataType) {
        LocalDate startDate = LocalDate.now().minusDays(daysBefore);

        Stream<LocalDate> stream = Stream.iterate(startDate, date -> date.plusDays(1L))
                .limit(daysBefore);
        if (dataType == DataType.DEVICE_VISIT) {
            // 在这里排除了 其他 和 小程序 类型
            List<String> codeList = ClientTypeEnum.getCodeList(excludeList);
            List<List<String>> result = new ArrayList<>();
            List<String> dateStrList = stream.map(it -> PREFIX + it.format(DateTimeFormatter.ofPattern(DATE_COMPACT_FORMATTER))).collect(Collectors.toList());
            for (String code : codeList) {
                List<String> dataByCodeList = dateStrList.stream()
                        .map(it -> String.format(FORMATTER_STRING, it, code))
                        .collect(Collectors.toList());
                result.add(dataByCodeList);
            }
            return result;
        } else if (dataType == DataType.USER_VISIT) {
            return stream.map(it -> PREFIX_MERGE + it.format(DateTimeFormatter.ofPattern(DATE_COMPACT_FORMATTER)))
                    .collect(Collectors.toList());
        } else if (dataType == OS_NAME_VISIT) {
            Set<String> members = stringRedisTemplate.opsForSet().members(OS_NAME_COL_KEY);
            List<List<String>> result = new ArrayList<>();
            if (CollectionUtils.isEmpty(members)) {
                return result;
            }
//            List<String> dateStrList = stream.map(it -> PREFIX_OS + it.format(DateTimeFormatter.ofPattern(DATE_COMPACT_FORMATTER))).collect(Collectors.toList());
//            for (Object obj : members) {
//                List<String> dataByOsNameList = dateStrList.stream()
//                        .map(it -> it + "_" + obj)
//                        .collect(Collectors.toList());
//                result.add(dataByOsNameList);
//            }
//            return result;
            return members.stream().map(it -> PREFIX_OS_MERGE_TOTAL + it).collect(Collectors.toList());

        }
        throw new UnsupportedDataTypeException("不支持的类型");
    }

    /**
     * 处理前一天的访问数据，包含合并，写入数据库中
     * 用于定时任务
     */
    public void executeLastDayVisitRecord() {
        LocalDate lastLocalDate = LocalDate.now().minusDays(1L);
        // 前一天的数据
        mergeRecordWithPerDay(lastLocalDate);
    }

    /**
     * 处理 osName 类型的访问数据
     */
    @SuppressWarnings("unchecked")
    public void executeOsNameTypeVisitData(LocalDate lastLocalDate) {
        // 查询前一天的数据
        long daysBefore = lastLocalDate.until(LocalDate.now(), ChronoUnit.DAYS);
        // [[vr:20230411_Windows10]]
        // 外层为 操作系统 里层为每一天数据
        List<List<String>> daysBeforeDateList = (List<List<String>>) getDaysBeforeDateList((int) daysBefore, OS_NAME_VISIT);
        List<String> dataStrList = daysBeforeDateList.stream().map(it -> it.get(0)).collect(Collectors.toList());
        // 从缓存中查询出来
        List<Object> osNameVisitCountList = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            dataStrList.stream().map(it -> it.getBytes(StandardCharsets.UTF_8))
                    .forEach(it -> conn.hyperLogLogCommands().pfCount(it));
            return null;
        });
        if (CollectionUtils.isEmpty(osNameVisitCountList)) {
            return;
        }

        List<String> osNameList = new ArrayList<>();
        List<VisitCountRecord> needToInsertList = new ArrayList<>();
        for (int i = 0; i < dataStrList.size(); i++) {
            String[] names = dataStrList.get(i).split("_");
            String name = names[names.length - 1];
            osNameList.add(PREFIX_OS_MERGE_TOTAL + name);
            needToInsertList.add(new VisitCountRecord(((Long) osNameVisitCountList.get(i)),
                    3, lastLocalDate, name));
        }

        // 查询总数
        List<Object> osNameTotalCountList = stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {
            osNameList.stream()
                    .map(it -> it.getBytes(StandardCharsets.UTF_8))
                    .forEach(it -> conn.stringCommands().get(it));
            return null;
        });

        List<VisitCountRecord> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(osNameTotalCountList)) {
            for (int i = 0; i < osNameList.size(); i++) {
                String osName = osNameList.get(i);
                Object o = osNameTotalCountList.get(i);
                if (Objects.isNull(o)) {
                    continue;
                }
                list.add(new VisitCountRecord(((Integer) osNameTotalCountList.get(i)).longValue(),
                        4, null, osName));
            }
        }
        saveOrUpdateOsName(needToInsertList, list);
    }

    /**
     * 保存，或者更新 操作系统 访问统计数
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateOsName(List<VisitCountRecord> needToInsertList, List<VisitCountRecord> list) {
        // 写入数据库
        visitCountRecordDao.insertBatch(needToInsertList);

        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<VisitCountRecord> recordList = visitCountRecordDao.selectByOsNameAndType(list);
        if (CollectionUtils.isEmpty(recordList)) {
            // 表示 list 全部为新增的
            visitCountRecordDao.insertBatch(list);
        } else {
            // 表示有部分是需要更新的
            List<VisitCountRecord> needToInsert = new ArrayList<>();
            List<VisitCountRecord> needToUpdate = new ArrayList<>();
            for (VisitCountRecord visitCountRecord : list) {
                boolean isMatch = false;
                for (VisitCountRecord countRecord : recordList) {
                    if (Objects.equals(visitCountRecord.getType(), countRecord.getType()) || visitCountRecord.getOsName().equals(countRecord.getOsName())) {
                        // 表示已经存在， 需要更新
                        visitCountRecord.setId(countRecord.getId());
                        needToUpdate.add(visitCountRecord);
                        isMatch = true;
                        break;
                    }
                }
                if (!isMatch) {
                    needToInsert.add(visitCountRecord);
                }
            }
            if (!CollectionUtils.isEmpty(needToInsert)) {
                visitCountRecordDao.insertBatch(needToInsert);
            }
            if (!CollectionUtils.isEmpty(needToUpdate)) {
                needToUpdate.forEach(it -> visitCountRecordDao.updateByPrimaryKeySelective(it, 1));
            }
        }


    }

    /**
     * 处理 clientType 类型的访问数据
     */
    private void executeClientTypeVisitData(LocalDate lastLocalDate) {
        // 查询前一天的数据 前面为 用户访问数据 后面为各个设备访问的数据
        Pair<Long, List<Object>> longListPair = queryClientAndUserVisitWithHyperLogLogLastDay();
        // 需要写入数据库中的次数，最后一个为合并后的用户访问次数
        List<Long> needInsertCountList = longListPair.getSecond().stream()
                .map(it -> ((Long) it)).collect(Collectors.toList());
        // 加到缓存中总的访问次数上
        redisTemplate.opsForValue().increment(VISIT_TOTAL_COUNT_KEY, longListPair.getFirst());
        List<VisitCountRecord> needToInsertList = new ArrayList<>();
        // index 在这里代表 设备类型
        for (int index = 1; index <= needInsertCountList.size(); index++) {
            needToInsertList.add(new VisitCountRecord(needInsertCountList.get(index - 1), 1, index, lastLocalDate));
        }
        needToInsertList.add(new VisitCountRecord(longListPair.getFirst(), null, 2, lastLocalDate));

        visitCountRecordDao.insertBatch(needToInsertList);
    }

    /**
     * 删除多少天前的数据 只是那一天哦
     * 用于定时任务
     */
    public void deleteHistoryVisitRecordDataBeforeDays(Integer daysBefore) {
        LocalDate needToDeleteDay = LocalDate.now().minusDays(daysBefore);
        Pair<String, String[]> keyPair = calculateKeyWithLocalDate(needToDeleteDay, PREFIX);

        // 删除设备访问数据
        log.info("开始删除" + daysBefore + "天前设备访问数据");
//        stringRedisTemplate.delete(Arrays.asList(keyPair.getSecond()))
        log.info("删除" + stringRedisTemplate.delete(Arrays.asList(keyPair.getSecond())) + "条设备访问数据");
        // 删除用户访问数据
//        log.info("开始删除" + daysBefore + "天前用户访问数据");
//        stringRedisTemplate.delete(PREFIX_MERGE + keyPair.getFirst());
        // 删除操作系统访问数据
//        log.info("开始删除" + daysBefore + "天前操作系统访问数据");
//        stringRedisTemplate.delete(PREFIX_OS_MERGE + keyPair.getFirst());

    }

    /**
     * 将 JSONObject 提取出 Long ，并补全没有的数据，默认为 0L
     */
    public static List<Long> convertAndPopulateNullData(List<JSONObject> list, Integer daysBefore) {
        List<Long> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        LocalDate startDate = LocalDate.now().minusDays(daysBefore);
        List<LocalDate> localDateList = Stream.iterate(startDate, date -> date.plusDays(1L))
                .limit(daysBefore).collect(Collectors.toList());

        for (LocalDate localDate : localDateList) {
            boolean isMatch = false;
            long localLongTime = localDate.atStartOfDay(ZoneId.of("+8")).toEpochSecond();

            for (JSONObject json : list) {
                Date date = json.getDate("date");
                if (localLongTime == date.getTime() / 1000) {
                    isMatch = true;
                    result.add(json.getLong("count"));
                    break;
                }
            }
            if (!isMatch) {
                result.add(0L);
            }
        }
        return result;
    }

    public void test1(Integer daysBefore) {

        // 测试数据产生
        executeTest(OS_NAME_VISIT, daysBefore);
        executeTest(DEVICE_VISIT, daysBefore);
//////


    }

    public void test2(Integer daysBefore) {

        for (int i = daysBefore; i >= 1; i--) {
            mergeRecordWithPerDay(LocalDate.now().minusDays(i));
        }
    }

    private void executeTest(DataType dataType, Integer daysBefore) {
        List<?> daysBeforeDateList = getDaysBeforeDateList(daysBefore, dataType);

        stringRedisTemplate.executePipelined((RedisCallback<Object>) conn -> {

            if (OS_NAME_VISIT == dataType) {
                for (Object obj : daysBeforeDateList) {
                    long randomCount = (long) (Math.random() * 30 + 13);
                    String item = (String) obj;
                    for (int i = 0; i < randomCount; i++) {
//                        conn.hyperLogLogCommands().pfAdd(item.getBytes(StandardCharsets.UTF_8), UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
                        conn.stringCommands().incrBy(item.getBytes(StandardCharsets.UTF_8), randomCount);
                        log.info("item => {}", item);
                    }
                }
            } else if (DEVICE_VISIT == dataType) {
                for (Object obj : daysBeforeDateList) {
                    List<String> itemList = (List<String>) obj;
                    for (String item : itemList) {
                        long randomCount = (long) (Math.random() * 30 + 13);
                        for (int i = 0; i < randomCount; i++) {
//                        conn.hyperLogLogCommands().pfAdd(item.getBytes(StandardCharsets.UTF_8), UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
                            conn.stringCommands().incrBy(item.getBytes(StandardCharsets.UTF_8), randomCount);
                            log.info("item => {}", item);
                        }

                    }
                }
            }

            return null;
        });
    }

}
