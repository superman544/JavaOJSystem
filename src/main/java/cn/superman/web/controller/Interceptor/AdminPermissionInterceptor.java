package cn.superman.web.controller.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.superman.web.constant.WebConstant;
import cn.superman.web.controller.annotation.AdminPermission;
import cn.superman.web.permission.Permissions;
import cn.superman.web.po.Manager;

public class AdminPermissionInterceptor extends HandlerInterceptorAdapter {

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView view)
			throws Exception {
		super.postHandle(request, response, handler, view);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Manager manager = (Manager) request.getSession().getAttribute(
				WebConstant.MANAGER_SESSION_ATTRIBUTE_NAME);

		// 处理Permission Annotation，实现方法级权限控制
		HandlerMethod method = (HandlerMethod) handler;
		AdminPermission permission = method
				.getMethodAnnotation(AdminPermission.class);
		// 如果为空在表示该方法不需要进行权限验证
		if (permission == null) {
			return true;
		}

		if (manager.getRole() == null
				|| !checkManagerPermission(manager.getRole().getPermissions(),
						permission.value())) {
			response.setStatus(403);
			response.getOutputStream().write("没有该操作的权限".getBytes("UTF-8"));
			return false;
		}

		return true;
	}

	private boolean checkManagerPermission(String managerPermissions,
			Permissions[] permissions) {
		if (managerPermissions == null || managerPermissions.isEmpty()) {
			return false;
		}
		for (int i = 0; i < permissions.length; i++) {
			if (!managerPermissions.contains(permissions[i].name())) {
				return false;
			}
		}

		return true;
	}
}
