package cn.superman.web.controller.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.superman.util.BeanMapperUtil;
import cn.superman.web.bean.ResponseMap;
import cn.superman.web.controller.PageController;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.dto.AddProblemTypeDTO;
import cn.superman.web.dto.UpdateProblemTypeDTO;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.ProblemType;
import cn.superman.web.service.admin.AdminProblemTypeService;
import cn.superman.web.service.page.PageResult;
import cn.superman.web.service.page.PageService;
import cn.superman.web.vo.request.AddProblemTypeVO;
import cn.superman.web.vo.request.UpdateProblemTypeVO;

@Controller
@RequestMapping("/AdminProblemTypeController")
public class AdminProblemTypeController extends
		PageController<ProblemType, ProblemType, ProblemType> {
	@Autowired
	private AdminProblemTypeService adminProblemTypeService;

	@AdminPermission(value = Permissions.RoleFind)
	@Override
	public PageResult<ProblemType> list(int pageShowCount, int wantPageNumber) {
		return super.list(pageShowCount, wantPageNumber);
	}

	@AdminPermission(value = Permissions.ProblemTypeFind)
	@Override
	public PageResult<ProblemType> listWithCondition(int pageShowCount,
			int wantPageNumber, ProblemType condition) {
		return super
				.listWithCondition(pageShowCount, wantPageNumber, condition);
	}

	@AdminPermission(value = Permissions.ProblemTypeFind)
	@RequestMapping(value = "/find", method = RequestMethod.GET)
	@ResponseBody
	public ResponseMap find(@RequestParam("id") Integer id) {
		ResponseMap responseMap = new ResponseMap().buildSucessResponse();
		responseMap.append("problemType", adminProblemTypeService.findById(id));
		return responseMap;
	}

	@AdminPermission(value = Permissions.ProblemTypeFind)
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	@ResponseBody
	public ResponseMap findAll() {
		ResponseMap responseMap = new ResponseMap().buildSucessResponse();
		responseMap.append("problemTypes", adminProblemTypeService.findAll());
		return responseMap;
	}

	@AdminPermission(value = Permissions.ProblemTypeAdd)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMap add(@Valid AddProblemTypeVO vo) {
		AddProblemTypeDTO dto = BeanMapperUtil.map(vo, AddProblemTypeDTO.class);
		adminProblemTypeService.add(dto);
		ResponseMap responseMap = new ResponseMap().buildSucessResponse();
		return responseMap;
	}

	@AdminPermission(value = Permissions.ProblemTypeUpdate)
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMap update(@Valid UpdateProblemTypeVO vo) {
		UpdateProblemTypeDTO dto = BeanMapperUtil.map(vo,
				UpdateProblemTypeDTO.class);
		adminProblemTypeService.update(dto);
		ResponseMap responseMap = new ResponseMap().buildSucessResponse();
		return responseMap;
	}

	@AdminPermission(value = Permissions.ProblemTypeDelete)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMap delete(@RequestParam("id") Integer id) {
		adminProblemTypeService.deleteById(id);
		ResponseMap responseMap = new ResponseMap().buildSucessResponse();
		return responseMap;
	}

	@Override
	public PageService<ProblemType, ProblemType> getPageService() {
		return adminProblemTypeService;
	}

	@Override
	public Class<ProblemType> returnVoClass() {
		return ProblemType.class;
	}
}
