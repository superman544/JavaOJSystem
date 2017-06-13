package cn.superman.web.controller.admin;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import cn.superman.util.Log4JUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.controller.PageController;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.dto.NewProblemDTO;
import cn.superman.web.dto.ProblemStandardFileDTO;
import cn.superman.web.dto.UpdateProblemDTO;
import cn.superman.web.dto.UploadProblemStandardFileDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.Manager;
import cn.superman.web.po.Problem;
import cn.superman.web.service.admin.AdminProblemService;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.service.page.PageService;
import cn.superman.web.vo.request.NewProblemVO;
import cn.superman.web.vo.request.UpdateProblemVO;

@Controller
@RequestMapping("/AdminProblemController")
public class AdminProblemController extends PageController<Problem, Problem, Problem> {
    @Autowired
    private AdminProblemService adminProblemService;

    @AdminPermission(value = Permissions.ProblemFind)
    @Override
    public PageResult<Problem> list(int pageShowCount, int wantPageNumber) {
        return super.list(pageShowCount, wantPageNumber);
    }

    @Override
    public Class<Problem> returnVoClass() {
        return Problem.class;
    }

    @AdminPermission(value = Permissions.ProblemFind)
    @Override
    public PageResult<Problem> listWithCondition(int pageShowCount, int wantPageNumber, Problem condition) {
        return super.listWithCondition(pageShowCount, wantPageNumber, condition);
    }

    @AdminPermission(value = Permissions.ProblemAdd)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap addProblem(@Valid NewProblemVO newProblem, HttpServletRequest request, HttpServletResponse response) {

        NewProblemDTO newProblemDTO = BeanMapperUtil.map(newProblem, NewProblemDTO.class);
        Manager manager = (Manager) request.getSession().getAttribute("manager");
        newProblemDTO.setProblemCreatorId(manager.getManagerId());
        adminProblemService.addProblem(newProblemDTO);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.ProblemDelete)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap deleteProblem(@RequestParam(value = "id") BigInteger problemId, HttpServletRequest request, HttpServletResponse response) {
        adminProblemService.deleteProblem(problemId);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.ProblemUpdate)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap updateProblem(@Valid UpdateProblemVO updateProblemVO, HttpServletRequest request, HttpServletResponse response) {

        UpdateProblemDTO dto = BeanMapperUtil.map(updateProblemVO, UpdateProblemDTO.class);
        adminProblemService.updateProblem(dto);

        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.ProblemStandardFile)
    @RequestMapping(value = "/uploadProblemStandardFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap uploadProblemStandardFile(@RequestParam(value = "problemId") Integer problemId, @RequestParam(value = "files") MultipartFile[] files) {

        // 单数为true
        if ((files.length & 1) == 1) {
            throw new ServiceLogicException("上传的文件数量不能是单数");
        }

        if (files.length > 10) {
            throw new ServiceLogicException("最大上传文件数量为10个");
        }

        Map<Integer, MultipartFile> inputFiles = new HashMap<Integer, MultipartFile>();
        Map<Integer, MultipartFile> outputFiles = new HashMap<Integer, MultipartFile>();
        for (MultipartFile file : files) {
            if (file.getOriginalFilename().toLowerCase().matches("input[1-5].txt")) {
                // 直接取出数字，放置到指定位置,input1.txt的数字在5,6之间
                inputFiles.put(Integer.parseInt(file.getOriginalFilename().substring(5, 6)), file);
            } else if (file.getOriginalFilename().toLowerCase().matches("output[1-5].txt")) {
                // 直接取出数字，放置到指定位置
                outputFiles.put(Integer.parseInt(file.getOriginalFilename().substring(6, 7)), file);
            }
        }

        if (inputFiles.size() != outputFiles.size() || inputFiles.size() > 5 || outputFiles.size() > 5 || inputFiles.size() < 1 || outputFiles.size() < 1) {
            throw new ServiceLogicException("上传文件的文件名非法，请查看规则");
        }

        for (int n = 1; n <= inputFiles.size(); n++) {
            UploadProblemStandardFileDTO dto = new UploadProblemStandardFileDTO();
            dto.setProblemId(problemId);
            try {
                dto.setStandardInputFileStream(inputFiles.get(n).getInputStream());
                dto.setStandardOutputFileStream(outputFiles.get(n).getInputStream());
                adminProblemService.uploadProblemStandardFile(dto);
            } catch (IOException e) {
                Log4JUtil.logError(e);
                throw new ServiceLogicException("服务器遇到错误，文件保存失败");
            }
        }

        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.ProblemStandardFile)
    @RequestMapping(value = "/showProblemStandardFiles", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap showProblemStandardFiles(@RequestParam(value = "problemId") Integer problemId) {
        List<ProblemStandardFileDTO> dtos = adminProblemService.getProblemStandardFilesById(problemId);
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("standardFiles", dtos);

        return responseMap;
    }

    @AdminPermission(value = Permissions.ProblemStandardFile)
    @RequestMapping(value = "/deleteProblemStandardFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMap deleteProblemStandardFile(@RequestParam(value = "inputFilePath", required = true) String inputFilePath,
            @RequestParam(value = "outputFilePath", required = true) String outputFilePath, HttpServletResponse response) {

        adminProblemService.deleteProblemStandardFile(inputFilePath, outputFilePath);

        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        return responseMap;
    }

    @AdminPermission(value = Permissions.ProblemStandardFile)
    @RequestMapping(value = "/downloadFileByPath", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFileByPath(@RequestParam("filePath") String filePath, @RequestParam("fileType") String fileType) {
        File file = adminProblemService.decodeToFileByFilePath(filePath);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentDispositionFormData("attachment", "in".equals(fileType) ? "input.txt" : "output.txt");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PageService<Problem, Problem> getPageService() {
        return adminProblemService;
    }

}
