package cn.superman.constant;

public final class ConstantParameter {
    public static final String PROBLEM_ROOT_PATH = "D:\\JavaOJSystem\\problem";
    public static final String COMPETITION_ROOT_PATH = "D:\\JavaOJSystem\\competition";
    public static final String CLOSE_COMPETITION_JOB_ROOT_PATH = "D:\\JavaOJSystem\\closeCompetitionJobs";
    public static final String JUDGE_COMPETITION_JOB_ROOT_PATH = "D:\\JavaOJSystem\\judgeCompetitionJobs";
    // 用于保存用户提交代码的顶层文件夹（不包括比赛的代码）
    public static final String USER_SUBMIT_CODE_ROOT_PATH = "D:\\JavaOJSystem\\codeFiles";
    // 用于临时保存用于代码编译后的class文件
    public final static String CLASS_FILE_ROOT_PATH = "D:\\JavaOJSystem\\classFiles";
    public static final String PROBLEM_IN_DIR_NAME = "in";
    public static final String PROBLEM_OUT_DIR_NAME = "out";
    // 题目标准输入输出加密用的盐
    public static final String PROBLEM_STANDARD_FILE_PATH_SEED = "asdsada";
    public static final int SUBMIT_RECORD_TABLE_CREATE_GAP = 10;
    public static final String SUBMIT_RECORD_TOKEN_NAME = "token";
    // 代码提交间隔时间
    public static final int SUBMIT_RECORD_GAP = 5000;
    public static final String DEFAULT_CHARSET_CODE = "UTF-8";
    // 对外显示的系统名字
    public static final String SYSTEM_NAME = "Online Judge";
}
