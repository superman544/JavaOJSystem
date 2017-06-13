package cn.superman.web.job;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.superman.web.dao.CompetitionAccountDao;
import cn.superman.web.dao.CompetitionApplicationDao;
import cn.superman.web.dao.CompetitionDao;
import cn.superman.web.po.Competition;
import cn.superman.web.po.CompetitionAccount;
import cn.superman.web.po.CompetitionApplication;
import cn.superman.web.service.admin.AdminCompetitionService;

public class CloseCompetitionJob implements Runnable, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6815804692316223095L;
    @Autowired
    private transient CompetitionAccountDao competitionAccountDao;
    @Autowired
    private transient CompetitionDao competitionDao;
    @Autowired
    private transient CompetitionApplicationDao competitionApplicationDao;
    @Autowired
    private transient AdminCompetitionService adminCompetitionService;

    private Integer competitionId;

    public CloseCompetitionJob() {

    }

    public CloseCompetitionJob(Integer competitionId) {
        this.competitionId = competitionId;
    }

    @Override
    public void run() {
        closeCompetition();
    }

    // TODO 以后加上事务吧
    private void closeCompetition() {
        CompetitionAccount competitionAccountCondition = new CompetitionAccount();
        competitionAccountCondition.setCompetitionId(competitionId);
        List<CompetitionAccount> competitionAccounts = competitionAccountDao.findWithCondition(competitionAccountCondition);
        Competition competition = competitionDao.findById(competitionId);
        // 1.生成最后的比赛报表，是可以和下面合并成一个循环的，但是这样的话整个函数会被拆开来，感觉代码看起来很奇怪
        adminCompetitionService.createReport(competitionId);

        for (CompetitionAccount account : competitionAccounts) {
            // 2.格式化所有账号的文件夹，统一命名文件夹名字为账号
            resetAccountDirName(account.getAccountCodeRootPath(), account.getLoginAccount());
        }
        // 3.重新设置这些账号为未使用
        competitionAccountDao.releaseAllAccountByCompetitionId(competitionId);
        // 4.如果是公开比赛的话，清理所有相关的报名信息
        if (competition.getIsCanDeclare()) {
            CompetitionApplication competitionApplicationCondition = new CompetitionApplication();
            competitionApplicationCondition.setCompetitionId(competitionId);
            competitionApplicationDao.deleteWithCondition(competitionApplicationCondition);
        }
        // 设置关闭标志
        Competition condition = new Competition();
        condition.setCompetitionId(competitionId);
        condition.setIsClose(true);
        competitionDao.update(condition);
    }

    private void resetAccountDirName(String accountCodeRootPath, String loginAccount) {
        File fromDir = null;
        File toDir = null;

        fromDir = new File(accountCodeRootPath);
        toDir = new File(accountCodeRootPath.substring(0, accountCodeRootPath.lastIndexOf(File.separator)) + File.separator + loginAccount);
        if (!fromDir.getAbsolutePath().equals(toDir.getAbsolutePath())) {
            fromDir.renameTo(toDir);
        }

    }
}
