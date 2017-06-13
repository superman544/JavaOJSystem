package cn.superman.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.web.bean.ResponseMap;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.permission.Permissions;
import cn.superman.web.service.admin.AdminOJSystemService;

@Controller
@RequestMapping("/OJSystemController")
public class AdminOJSystemController {

    @Autowired
    private AdminOJSystemService ojSystemService;

    @AdminPermission(value = Permissions.SandboxOpen)
    @RequestMapping(path = "/openNewJavaSandbox", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap openNewJavaSandbox() {
        ojSystemService.openNewJavaSandbox();
        return new ResponseMap().buildSucessResponse();
    }

    @AdminPermission(value = Permissions.SandboxClose)
    @RequestMapping(path = "/closeAllJavaSandbox", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap closeAllJavaSandbox() {
        ojSystemService.closeAllJavaSandbox();
        return new ResponseMap().buildSucessResponse();
    }

    @AdminPermission(value = Permissions.SandboxClose)
    @RequestMapping(path = "/closeJavaSandboxByIdCard", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap closeJavaSandboxByIdCard(@RequestParam("idCard") String idCard) {
        ojSystemService.closeSandboxById(idCard);
        return new ResponseMap().buildSucessResponse();
    }

}
