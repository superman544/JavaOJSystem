package cn.superman.web.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.web.bean.ResponseMap;
import cn.superman.web.controller.PageController;
import cn.superman.web.po.ProblemType;
import cn.superman.web.service.front.ProblemTypeService;
import cn.superman.web.service.page.PageService;

@Controller
@RequestMapping("/ProblemTypeController")
public class ProblemTypeController extends PageController<ProblemType, ProblemType, ProblemType> {
    @Autowired
    private ProblemTypeService problemTypeService;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap find(@RequestParam("id") Integer id) {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("problemType", problemTypeService.getProblemTypeById(id));
        return responseMap;
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMap findAll() {
        ResponseMap responseMap = new ResponseMap().buildSucessResponse();
        responseMap.append("allProblemType", problemTypeService.findAll());
        return responseMap;
    }

    @Override
    public PageService<ProblemType, ProblemType> getPageService() {
        return problemTypeService;
    }

    @Override
    public Class<ProblemType> returnVoClass() {
        return ProblemType.class;
    }
}
