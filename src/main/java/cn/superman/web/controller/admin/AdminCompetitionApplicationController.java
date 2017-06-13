package cn.superman.web.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.constant.WebConstant;
import cn.superman.web.controller.PageController;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.dto.CompetitionApplicationEmailDTO;
import cn.superman.web.dto.UpdateApplicationDTO;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.CompetitionApplication;
import cn.superman.web.service.admin.AdminCompetitionApplicationService;
import cn.superman.web.service.page.PageService;
import cn.superman.web.vo.request.CompetitionApplicationEmailVO;
import cn.superman.web.vo.request.UpdateApplicationVO;

@Controller
@RequestMapping("/AdminCompetitionApplicationController")
public class AdminCompetitionApplicationController extends PageController<CompetitionApplication, CompetitionApplication, CompetitionApplication> {
    @Autowired
    private AdminCompetitionApplicationService adminCompetitionApplicationService;

    @AdminPermission(value = Permissions.CompetitionApplicationFind)
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap find(@RequestParam("id") Integer id) {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("competitionApplication", adminCompetitionApplicationService.findById(id));
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationFind)
    @RequestMapping(value = "/getAllApplication", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap getAllApplicationByCompetitionId(@RequestParam("competitionId") Integer competitionId) {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        List<CompetitionApplication> allCompetitionId = adminCompetitionApplicationService.getAllApplicationByCompetitionId(competitionId);
        responseMap.append("allApplications", allCompetitionId);
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationFind)
    @RequestMapping(value = "/getAllCompetitionIds", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap getAllCompetitionIds() {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("allCompetitionIds", adminCompetitionApplicationService.getAllCompetitionIds());
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationDispatch)
    @RequestMapping(value = "/dispatchAccount", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap dispatchAccount(@RequestParam("competitionId") Integer competitionId, @RequestParam("applicationId") String applicationId) {
        Integer dispatchAccountId = adminCompetitionApplicationService.dispatchAccount(competitionId, applicationId);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("dispatchAccountId", dispatchAccountId);
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationDispatch)
    @RequestMapping(value = "/cancelDispatchAccount", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap cancelDispatchAccount(@RequestParam("accountId") Integer accountId, @RequestParam("applicationId") String applicationId) {
        adminCompetitionApplicationService.cancelDispatchAccount(applicationId, accountId);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();

        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationUpdate)
    @RequestMapping(value = "/updateApplication", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap updateApplication(@Valid UpdateApplicationVO vo) {
        UpdateApplicationDTO dto = BeanMapperUtil.map(vo, UpdateApplicationDTO.class);
        adminCompetitionApplicationService.update(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationNotify)
    @RequestMapping(value = "/sendApplicationEmail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap sendApplicationEmail(@Valid CompetitionApplicationEmailVO vo) {
        CompetitionApplicationEmailDTO dto = BeanMapperUtil.map(vo, CompetitionApplicationEmailDTO.class);
        adminCompetitionApplicationService.sendEmail(dto);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationReport)
    @RequestMapping(value = "/uploadApplicationReport", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap uploadApplicationReport(@RequestParam(value = "competitionId") Integer competitionId,
            @RequestParam(value = "applicationReport") MultipartFile applicationReport) {
        adminCompetitionApplicationService.uploadApplicationReport(applicationReport, competitionId);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.CompetitionApplicationReport)
    @RequestMapping(value = "/downloadApplicationReport", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadApplicationReport(@RequestParam(value = "competitionId") Integer competitionId) {
        File file = adminCompetitionApplicationService.downApplicationReport(competitionId);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentDispositionFormData("attachment", WebConstant.APPLICATION_REPORT_EXCEL_SAVE_NAME);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PageService<CompetitionApplication, CompetitionApplication> getPageService() {
        return adminCompetitionApplicationService;
    }

    @Override
    public Class<CompetitionApplication> returnVoClass() {
        return CompetitionApplication.class;
    }
}
