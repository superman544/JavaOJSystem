package cn.superman.web.permission;

/**
 * 枚举类，标识系统一共拥有多少权限
 *
 * @author 梁浩辉
 */
public enum Permissions {

    ManagerAdd("管理员添加"), //
    ManagerDelete("管理员删除"), //
    ManagerUpdate("管理员更新"), //
    ManagerFind("管理员查找"), //
    RoleAdd("角色添加"), //
    RoleDelete("角色删除"), //
    RoleUpdate("角色更新"), //
    RoleFind("角色查找"), //
    UserFind("用户查找"), //
    UserBan("用户封禁"), //
    AnnouncementAdd("公告添加"), //
    AnnouncementDelete("公告删除"), //
    AnnouncementUpdate("公告更新"), //
    AnnouncementFind("公告查找"), //
    AnnouncementPublish("公告发布"), //
    CompetitionAdd("比赛添加"), //
    CompetitionDelete("比赛删除"), //
    CompetitionUpdate("比赛更新"), //
    CompetitionFind("比赛查找"), //
    CompetitionPublish("比赛发布"), //
    CompetitionClose("比赛关闭"), //
    CompetitionReport("比赛报告"), //
    CompetitionJudge("比赛判题"), //
    CompetitionApplicationUpdate("比赛报名更新"), //
    CompetitionApplicationNotify("比赛报名通知"), //
    CompetitionApplicationFind("比赛报名查找"), //
    CompetitionApplicationDispatch("比赛报名账号分配"), //
    CompetitionApplicationReport("比赛报名报表"), //
    CompetitionAccountUpdate("比赛账号更新"), //
    CompetitionAccountFind("比赛账号查找"), //
    ProblemAdd("题目添加"), //
    ProblemDelete("题目删除"), //
    ProblemUpdate("题目更新"), //
    ProblemFind("题目查找"), //
    ProblemStandardFile("题目标准文件操作"), //
    ProblemTypeAdd("题目类型添加"), //
    ProblemTypeDelete("题目类型删除"), //
    ProblemTypeUpdate("题目类型更新"), //
    ProblemTypeFind("题目类型查找"), //
    SandboxWatch("沙箱查看"), //
    SandboxOpen("沙箱开启"), //
    SandboxClose("沙箱关闭");

    private String explanation;

    private Permissions(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getName() {
        return name();
    }
}
