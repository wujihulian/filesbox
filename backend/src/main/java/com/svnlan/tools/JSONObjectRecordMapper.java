package com.svnlan.tools;

import com.alibaba.fastjson.JSONObject;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;

@Component
public class JSONObjectRecordMapper implements RecordMapper<Record, JSONObject> {
    @Override
    public JSONObject map(Record record) {
        if (Objects.isNull(record)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Field<?>[] fields = record.fields();
        for (int i = 0; i < fields.length; i++) {
            jsonObject.fluentPut(toCamelCase(fields[i].getName()), record.get(i));
        }
        return jsonObject;
    }

    public static String toCamelCase(String str) {
        if (!StringUtils.hasText(str)) {
            return "";
        }
        String[] splits = str.split("_");
        if (splits.length == 1) {
            return splits[0];
        }
        if (splits.length == 2 && StringUtils.isEmpty(splits[0])) {
            return splits[1];
        }
        StringBuilder sb = new StringBuilder(splits[0]);
        for (int i = 1; i < splits.length; i++) {
            sb.append(StringUtils.capitalize(splits[i]));
        }
        return sb.toString();
    }
}
