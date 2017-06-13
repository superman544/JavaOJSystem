package cn.superman.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import cn.superman.util.Log4JUtil;
import cn.superman.web.exception.ServiceLogicException;

public class DateConvert implements Converter<String, Date> {
    private static final String YYYYMMDDHHmmssRegex = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}";
    private static final String YYYYMMDDRegex = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";

    @Override
    public Date convert(String stringDate) {
        if (stringDate == null || stringDate.isEmpty()) {
            return null;
        }
        SimpleDateFormat dateFormat = null;
        if (stringDate.matches(YYYYMMDDRegex)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else if (stringDate.matches(YYYYMMDDHHmmssRegex)) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            throw new ServiceLogicException("不支持的日期类型");
        }

        try {
            return dateFormat.parse(stringDate);
        } catch (Exception e) {
            Log4JUtil.logError(e);
        }
        return null;
    }

}
