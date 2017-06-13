package cn.superman.web.controller.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.dto.UpdateCompetitionAccountDTO;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.CompetitionAccount;
import cn.superman.web.service.admin.AdminCompetitionAccountService;
import cn.superman.web.vo.request.UpdateCompetitionAccountVO;

@Controller
@RequestMapping("/AdminCompetitionAccountController")
public class AdminCompetitionAccountController {
    @Autowired
    private AdminCompetitionAccountService adminCompetitionAccountService;

    @AdminPermission(value = Permissions.CompetitionAccountUpdate)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap update(@Valid UpdateCompetitionAccountVO vo) {
        UpdateCompetitionAccountDTO dto = BeanMapperUtil.map(vo, UpdateCompetitionAccountDTO.class);
        adminCompetitionAccountService.update(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionAccountFind)
    @RequestMapping(value = "/getCompetitionAccount/{competitionId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap getCompetitionAccount(@PathVariable("competitionId") Integer competitionId) {
        List<CompetitionAccount> allAccounts = adminCompetitionAccountService.findCompetitionAccount(competitionId);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("allAccounts", allAccounts);
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionAccountFind)
    @RequestMapping(value = "/getAllCompetitionIds", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap getAllCompetitionIds() {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("allCompetitionIds", adminCompetitionAccountService.getAllCompetitionIds());
        return responseMap;
    }

}
