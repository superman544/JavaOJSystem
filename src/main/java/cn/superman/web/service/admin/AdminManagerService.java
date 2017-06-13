package cn.superman.web.service.admin;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.superman.util.BeanMapperUtil;
import cn.superman.util.EncryptUtility;
import cn.superman.web.dao.ManagerDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.AddManagerDTO;
import cn.superman.web.dto.UpdateManagerDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.Manager;
import cn.superman.web.po.Role;
import cn.superman.web.service.page.PageService;

@Service
public class AdminManagerService extends PageService<Manager, Manager> {
    @Autowired
    private ManagerDao managerDao;

    public Manager login(String account, String password) {
        Manager condition = new Manager();
        condition.setAccount(account);
        condition.setPassword(EncryptUtility.Md5Encoding(password));

        List<Manager> managers = managerDao.findWithCondition(condition);

        if (managers == null || managers.size() != 1) {
            throw new ServiceLogicException("账号或者密码不对");
        }

        return managers.get(0);
    }

    public void add(AddManagerDTO dto) {
        Manager manager = BeanMapperUtil.map(dto, Manager.class);
        manager.setPassword(EncryptUtility.Md5Encoding(dto.getPassword()));
        Role role = new Role();
        role.setRoleId(dto.getRoleId());
        manager.setRole(role);
        managerDao.add(manager);
    }

    public void deleteById(Integer id) {
        managerDao.deleteById(id);
    }

    public Manager findById(Integer id) {
        return managerDao.findById(id);
    }

    public void update(UpdateManagerDTO dto) {
        Manager manager = BeanMapperUtil.map(dto, Manager.class);
        if (StringUtils.isNotBlank(dto.getPassword())) {
            manager.setPassword(EncryptUtility.Md5Encoding(dto.getPassword()));
        } else {
            // 确保不修改密码
            manager.setPassword(null);
        }
        Role role = new Role();
        role.setRoleId(dto.getRoleId());
        manager.setRole(role);
        managerDao.update(manager);
    }

    @Override
    public BaseDao<Manager, Manager> getUseDao() {
        return managerDao;
    }
}
