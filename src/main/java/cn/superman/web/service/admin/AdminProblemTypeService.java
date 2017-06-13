package cn.superman.web.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.dao.ProblemTypeDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.AddProblemTypeDTO;
import cn.superman.web.dto.UpdateProblemTypeDTO;
import cn.superman.web.po.ProblemType;
import cn.superman.web.service.page.PageService;

@Service
public class AdminProblemTypeService extends PageService<ProblemType, ProblemType> {
    @Autowired
    private ProblemTypeDao problemTypeDao;

    public List<ProblemType> findAll() {
        return problemTypeDao.find();
    }

    public ProblemType findById(Integer id) {
        return problemTypeDao.findById(id);
    }

    public void add(AddProblemTypeDTO dto) {
        ProblemType problemType = BeanMapperUtil.map(dto, ProblemType.class);
        problemTypeDao.add(problemType);
    }

    public void update(UpdateProblemTypeDTO dto) {
        ProblemType problemType = BeanMapperUtil.map(dto, ProblemType.class);
        problemTypeDao.update(problemType);
    }

    public void deleteById(Integer id) {
        problemTypeDao.deleteById(id);
    }

    @Override
    public BaseDao<ProblemType, ProblemType> getUseDao() {
        return problemTypeDao;
    }
}
