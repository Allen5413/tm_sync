package com.zs.tools;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * JSON日期格式转换
 * Created by Allen on 2015/5/19.
 */
public class DateJsonValueProcessorTools implements JsonValueProcessor {

    private String longFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        String[] obj = {};
        if (value instanceof Date[]){
            SimpleDateFormat sf = new SimpleDateFormat(longFormat);
            Date[] dates = (Date[]) value;
            obj = new String[dates.length];
            for (int i = 0; i < dates.length; i++){
                obj[i] = sf.format(dates[i]);
            }
        }
        return obj;
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        if (value instanceof Date){
            String str = new SimpleDateFormat(longFormat).format((Date) value);
            return str;
        }
        return value;
    }
}
