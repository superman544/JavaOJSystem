package cn.superman.web.service.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;

import cn.superman.web.dao.ProblemDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.po.Problem;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.service.page.PageService;

@Service
public class ProblemService extends PageService<Problem, Problem> {
    @Autowired
    private ProblemDao problemDao;

    private static Problem defaultCondition;
    static {
        defaultCondition = new Problem();
        defaultCondition.setIsPublish(true);
    }

    public PageResult<Problem> getPageByLikeName(int pageShowCount, int wantPaegNumber, String likeProblemName) {
        List<Problem> list = problemDao.findPulishProblemByLikeName(likeProblemName);

        PageInfo<Problem> info = new PageInfo<Problem>(list);
        PageResult<Problem> pageResult = new PageResult<Problem>();
        pageResult.setResult(list);
        pageResult.setTotalCount(info.getTotal());
        pageResult.setCurrentPage(info.getPageNum());
        pageResult.setTotalPage(info.getPages());
        return pageResult;
    }

    @Override
    public BaseDao<Problem, Problem> getUseDao() {
        return problemDao;
    }

    @Override
    public Problem getDefaultCondition() {
        return defaultCondition;
    }

    public Problem getProblemById(Integer problemId) {
        return problemDao.findById(problemId);
    }

}
