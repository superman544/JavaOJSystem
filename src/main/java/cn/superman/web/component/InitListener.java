package cn.superman.web.component;

import java.io.File;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.superman.constant.ConstantParameter;

@Component
public class InitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent ev) {
        // 防止重复执行。
        if (ev.getApplicationContext().getParent() == null) {
            // 初始化必要的路径文件夹
            initDir(ConstantParameter.PROBLEM_ROOT_PATH);
            initDir(ConstantParameter.COMPETITION_ROOT_PATH);
            initDir(ConstantParameter.CLOSE_COMPETITION_JOB_ROOT_PATH);
            initDir(ConstantParameter.JUDGE_COMPETITION_JOB_ROOT_PATH);
            initDir(ConstantParameter.USER_SUBMIT_CODE_ROOT_PATH);
            initDir(ConstantParameter.CLASS_FILE_ROOT_PATH);
        }
    }

    private void initDir(String path) {
        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
