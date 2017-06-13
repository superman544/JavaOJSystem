package cn.superman.web.service.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.util.BeanMapperUtil;
import cn.superman.util.Log4JUtil;
import cn.superman.util.UUIDUtil;
import cn.superman.web.dao.CompetitionApplicationDao;
import cn.superman.web.dto.CompetitionApplyDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.CompetitionApplication;

@Service
public class CompetitionApplicationService {

    @Autowired
    private CompetitionApplicationDao competitionApplicationDao;

    public void addApplication(CompetitionApplyDTO dto) throws ServiceLogicException {

        CompetitionApplication competitionApplication = BeanMapperUtil.map(dto, CompetitionApplication.class);
        // 不是自增长的主键，所以需要生成一个唯一的ID号
        competitionApplication.setCompetitionApplicationId(UUIDUtil.getUUID());
        competitionApplication.setIsHaveSendEmail(false);
        competitionApplication.setIsHaveHandle(false);
        try {
            competitionApplicationDao.add(competitionApplication);
        } catch (Exception e) {
            Log4JUtil.logError(e);
            throw new ServiceLogicException("申请失败，你的邮箱已经被申请过了，请不要再申请");
        }

    }
}
