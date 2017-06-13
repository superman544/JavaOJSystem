package cn.superman.web.service.admin;

import org.springframework.stereotype.Service;

import cn.superman.system.service.JavaSandboxService;
import cn.superman.system.service.observer.SandboxStatusObserver;
import cn.superman.util.AppSystemInfoUtil;
import cn.superman.util.AppSystemInfoUtil.AppInfo;
import cn.superman.util.OperationSystemInfoUtil;
import cn.superman.util.OperationSystemInfoUtil.OperationSystemInfo;

@Service
public class AdminOJSystemService {
	private JavaSandboxService javaSandboxService;

	public AdminOJSystemService() {
		javaSandboxService = JavaSandboxService.getInstance();
	}

	public void openNewJavaSandbox() {
		javaSandboxService.openNewJavaSandbox();
	}

	public void addJavaSandboxStatusListen(SandboxStatusObserver o) {
		javaSandboxService.addSandboxStatusObserver(o);
	}

	public void removeJavaSandboxStatusListen(SandboxStatusObserver o) {
		javaSandboxService.removeSandboxStatusObserver(o);
	}

	public void closeAllJavaSandbox() {
		javaSandboxService.closeAllSandbox();
	}

	public void closeSandboxById(String idCard) {
		javaSandboxService.closeSandboxById(idCard);
	}

	public int getPendingHandleProblemRequest() {
		return javaSandboxService.getPendingHandleProblemCount();
	}

	public OperationSystemInfo getOperationSystemInfo() {
		return OperationSystemInfoUtil.getOperationSystemInfo();
	}

	public AppInfo getAppInfo() {
		return AppSystemInfoUtil.getAppInfo();
	}
}
