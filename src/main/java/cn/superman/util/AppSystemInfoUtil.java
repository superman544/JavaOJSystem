package cn.superman.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;

public class AppSystemInfoUtil {
    private AppSystemInfoUtil() {
        throw new AssertionError("不要实例化工具类哦");
    }

    private static final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final long beginTime = System.currentTimeMillis();
    private static final String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

    public static AppInfo getAppInfo() {
        AppInfo info = new AppInfo();
        info.setThreadCount(threadMXBean.getThreadCount());
        info.setRunTime(System.currentTimeMillis() - beginTime);
        info.setUseMemory(memoryMXBean.getHeapMemoryUsage().getUsed() + memoryMXBean.getNonHeapMemoryUsage().getUsed());
        info.setPid(pid);
        return info;
    }

    public static class AppInfo {
        private int threadCount;
        private long runTime;
        private long useMemory;
        private String pid;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public int getThreadCount() {
            return threadCount;
        }

        public void setThreadCount(int threadCount) {
            this.threadCount = threadCount;
        }

        public long getRunTime() {
            return runTime;
        }

        public void setRunTime(long runTime) {
            this.runTime = runTime;
        }

        public long getUseMemory() {
            return useMemory;
        }

        public void setUseMemory(long useMemory) {
            this.useMemory = useMemory;
        }

    }
}
