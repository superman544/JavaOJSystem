package cn.superman.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.web.bean.ResponseMap;
import cn.superman.web.controller.PageController;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.dto.UserBanDTO;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.User;
import cn.superman.web.service.admin.AdminUserService;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.service.page.PageService;

@Controller
@RequestMapping("/AdminUserController")
public class AdminUserController extends PageController<User, User, User> {
    @Autowired
    private AdminUserService adminUserService;

    @AdminPermission(value = Permissions.UserFind)
    @Override
    public PageResult<User> list(int pageShowCount, int wantPageNumber) {
        return super.list(pageShowCount, wantPageNumber);
    }

    @AdminPermission(value = Permissions.UserFind)
    @Override
    public PageResult<User> listWithCondition(int pageShowCount, int wantPageNumber, User condition) {
        return super.listWithCondition(pageShowCount, wantPageNumber, condition);
    }

    @AdminPermission(value = Permissions.UserFind)
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap find(@RequestParam("id") Integer id) {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("user", adminUserService.findById(id));
        return responseMap;
    }

    @AdminPermission(value = Permissions.UserBan)
    @RequestMapping(value = "/ban", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap update(@RequestParam("userId") Integer userId, @RequestParam("isBan") boolean isBan) {
        UserBanDTO dto = new UserBanDTO();
        dto.setIsBan(isBan);
        dto.setUserId(userId);
        adminUserService.banUser(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @Override
    public PageService<User, User> getPageService() {
        return adminUserService;
    }

    @Override
    public Class<User> returnVoClass() {
        return User.class;
    }
}
