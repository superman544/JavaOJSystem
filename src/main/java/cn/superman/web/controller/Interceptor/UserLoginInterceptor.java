package cn.superman.web.controller.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.superman.web.constant.WebConstant;
import cn.superman.web.po.User;

/**
 * 确认用户是否已经登陆了的拦截器
 * 
 * @author 梁浩辉
 */
public class UserLoginInterceptor extends HandlerInterceptorAdapter {

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
		User user = (User) request.getSession().getAttribute(
				WebConstant.USER_SESSION_ATTRIBUTE_NAME);
		if (user == null) {
			response.setStatus(301);
			response.getOutputStream().write("登录已经失效，请重新登录".getBytes("UTF-8"));
			return false;
		}

		return true;
	}
}
