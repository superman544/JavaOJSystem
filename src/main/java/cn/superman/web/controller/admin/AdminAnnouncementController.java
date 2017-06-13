package cn.superman.web.controller.admin;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.constant.WebConstant;
import cn.superman.web.controller.PageController;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.dto.AddAnnouncementDTO;
import cn.superman.web.dto.UpdateAnnouncementDTO;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.Announcement;
import cn.superman.web.po.Manager;
import cn.superman.web.service.admin.AdminAnnouncementService;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.service.page.PageService;
import cn.superman.web.vo.request.AddAnnouncementVO;
import cn.superman.web.vo.request.UpdateAnnouncementVO;

@Controller
@RequestMapping("/AdminAnnouncementController")
public class AdminAnnouncementController extends PageController<Announcement, Announcement, Announcement> {
    @Autowired
    private AdminAnnouncementService adminAnnouncementService;

    @AdminPermission(value = Permissions.AnnouncementFind)
    @Override
    public PageResult<Announcement> list(int pageShowCount, int wantPageNumber) {
        return super.list(pageShowCount, wantPageNumber);
    }

    @AdminPermission(value = Permissions.AnnouncementFind)
    @Override
    public PageResult<Announcement> listWithCondition(int pageShowCount, int wantPageNumber, Announcement condition) {
        return super.listWithCondition(pageShowCount, wantPageNumber, condition);
    }

    @AdminPermission(value = Permissions.AnnouncementFind)
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap find(@RequestParam("id") Integer id) {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("announcement", adminAnnouncementService.findById(id));
        return responseMap;
    }

    @AdminPermission(value = Permissions.AnnouncementAdd)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap add(@Valid AddAnnouncementVO vo, HttpSession session) {
        Manager manager = (Manager) session.getAttribute(WebConstant.MANAGER_SESSION_ATTRIBUTE_NAME);
        AddAnnouncementDTO dto = BeanMapperUtil.map(vo, AddAnnouncementDTO.class);
        dto.setAnnouncementCreateManagerId(manager.getManagerId());
        adminAnnouncementService.add(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.AnnouncementUpdate)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap update(@Valid UpdateAnnouncementVO vo, HttpSession session) {
        Manager manager = (Manager) session.getAttribute(WebConstant.MANAGER_SESSION_ATTRIBUTE_NAME);
        UpdateAnnouncementDTO dto = BeanMapperUtil.map(vo, UpdateAnnouncementDTO.class);
        dto.setAnnouncementCreateManagerId(manager.getManagerId());
        adminAnnouncementService.update(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.AnnouncementDelete)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap delete(@RequestParam("id") Integer id) {
        adminAnnouncementService.deleteById(id);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.AnnouncementPublish)
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap publish(@RequestParam("id") Integer id) {
        adminAnnouncementService.publish(id);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @Override
    public PageService<Announcement, Announcement> getPageService() {
        return adminAnnouncementService;
    }

    @Override
    public Class<Announcement> returnVoClass() {
        return Announcement.class;
    }
}
