package cn.superman.web.service.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.dao.UserDao;
import cn.superman.web.dao.UserDao.BatchUpdateData;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.UserBanDTO;
import cn.superman.web.po.User;
import cn.superman.web.service.page.PageService;

@Service
public class AdminUserService extends PageService<User, User> {
    @Autowired
    private UserDao userDao;

    public User findById(Integer id) {
        return userDao.findById(id);
    }

    public void banUser(UserBanDTO dto) {
        User user = BeanMapperUtil.map(dto, User.class);
        userDao.update(user);
    }

    /**
     * 统计用户信息，比如统计一共做了多少道题目，一共解决了多少道题目，一共解决的题目价值是多少,统计从执行该方法的时间开始，25小时以内有提交过代码的用户
     */
    public void countUserData() {
        Calendar calendar = Calendar.getInstance();
        Date endTime = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, -25);
        Date beginTime = calendar.getTime();

        List<User> users = userDao.findWithLastSubmitTimeGap(beginTime, endTime);
        List<BatchUpdateData> datas = BeanMapperUtil.mapList(users, UserDao.BatchUpdateData.class);

        userDao.countHaveDoneProblem(datas);
        userDao.countRightProblem(datas);
        userDao.countTotalSolveValue(datas);
    }

    @Override
    public BaseDao<User, User> getUseDao() {
        return userDao;
    }
}
