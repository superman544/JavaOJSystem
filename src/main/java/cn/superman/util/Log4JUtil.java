package cn.superman.util;

import org.apache.log4j.Logger;

import cn.superman.constant.Log4JConstant;

/**
 * Log4J工具类
 *
 * @author 梁浩辉
 */
public class Log4JUtil {
    public static void logError(Throwable t) {
        Logger.getLogger(Log4JConstant.errorLogName).error("", t);
    }
}
