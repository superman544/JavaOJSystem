package cn.superman.web.controller.admin;

import java.io.File;
import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.controller.PageController;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.dto.AddCompetitionDTO;
import cn.superman.web.dto.UpdateCompetitionDTO;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.Competition;
import cn.superman.web.service.admin.AdminCompetitionService;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.service.page.PageService;
import cn.superman.web.vo.request.AddCompetitionVO;
import cn.superman.web.vo.request.UpdateCompetitionVO;

@Controller
@RequestMapping("/AdminCompetitionController")
public class AdminCompetitionController extends PageController<Competition, Competition, Competition> {
    @Autowired
    private AdminCompetitionService adminCompetitionService;

    @Override
    public PageService<Competition, Competition> getPageService() {
        return adminCompetitionService;
    }

    @Override
    public Class<Competition> returnVoClass() {
        return Competition.class;
    }

    @AdminPermission(value = Permissions.CompetitionFind)
    @Override
    public PageResult<Competition> list(int pageShowCount, int wantPageNumber) {
        return super.list(pageShowCount, wantPageNumber);
    }

    @AdminPermission(value = Permissions.CompetitionFind)
    @Override
    public PageResult<Competition> listWithCondition(int pageShowCount, int wantPageNumber, Competition condition) {
        return super.listWithCondition(pageShowCount, wantPageNumber, condition);
    }

    @AdminPermission(value = Permissions.CompetitionAdd)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap add(@Valid AddCompetitionVO vo) {
        AddCompetitionDTO dto = BeanMapperUtil.map(vo, AddCompetitionDTO.class);
        adminCompetitionService.add(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionUpdate)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap update(@Valid UpdateCompetitionVO vo) {
        UpdateCompetitionDTO dto = BeanMapperUtil.map(vo, UpdateCompetitionDTO.class);
        adminCompetitionService.update(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionDelete)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap delete(@RequestParam("id") Integer id) {
        adminCompetitionService.deleteById(id);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionFind)
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap find(@RequestParam("id") Integer id) {
        Competition competition = adminCompetitionService.findById(id);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("competition", competition);
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionPublish)
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap publish(@RequestParam("id") Integer id) {
        adminCompetitionService.publish(id);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionClose)
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap close(@RequestParam("id") Integer id, @RequestParam("isRunNow") boolean isRunNow) {
        adminCompetitionService.closeCompetition(id, isRunNow);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionReport)
    @RequestMapping(value = "/createReport", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap createReport(@RequestParam("id") Integer id) {
        adminCompetitionService.createReport(id);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionJudge)
    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap judge(@RequestParam("id") Integer id, @RequestParam("isRunNow") boolean isRunNow) {
        adminCompetitionService.judgeCompetitionAllCode(id, isRunNow);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionReport)
    @RequestMapping(value = "/downloadReport", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadCompetitionReport(@RequestParam(value = "competitionId") Integer competitionId) {
        File file = adminCompetitionService.getCompetitionReport(competitionId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
