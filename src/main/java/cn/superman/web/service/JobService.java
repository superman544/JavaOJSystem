package cn.superman.web.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import cn.superman.util.Log4JUtil;
import cn.superman.web.util.SerializingUtil;

@Service
public class JobService {

    // 由于夜晚进行的，大多都是IO密集型的，所以线程数量稍微调大一点。
    private static ExecutorService jobExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3 + 2);

    public void executeNow(Runnable job) {
        jobExecutorService.execute(job);
    }

    public <T extends Runnable & Serializable> void executeDelay(T job, Class<T> jobClass, String jobPath) {
        // 生成一个对象，通过对象流写出去，并在指定的时间，再去执行
        try {
            SerializingUtil.writeBeanToFile(job, jobClass, new File(jobPath));
        } catch (IOException e) {
            Log4JUtil.logError(e);
        }
    }

}
