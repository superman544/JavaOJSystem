package cn.superman.web.util;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

import cn.superman.util.Log4JUtil;

public class ThreadFactoryUtil {
	public static ThreadFactory getLogThreadFactory(final String threadName) {
		return new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setName(threadName);
				thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						Log4JUtil.logError(e);
					}
				});
				return thread;
			}
		};
	}
}
