package cn.superman.web.controller.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.superman.web.constant.WebConstant;
import cn.superman.web.po.Manager;

/**
 * 确认管理员是否已经登陆了的拦截器
 *
 * @author 梁浩辉
 */
public class ManagerLoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView view) throws Exception {
        super.postHandle(request, response, handler, view);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Manager manager = (Manager) request.getSession().getAttribute(WebConstant.MANAGER_SESSION_ATTRIBUTE_NAME);

        if (manager == null) {
            if ("/JavaOJSystem/manager/index.html".equals(request.getRequestURI())) {
                // 这个时候还没加载angularJs，所以不能用下面的方式来重定向
                response.sendRedirect("login.html");
            } else {
                response.setStatus(301);
                String loginUrl = request.getContextPath() + "/manager/login.html";
                response.getOutputStream().write(loginUrl.getBytes());
            }
            return false;
        }
        return true;
    }
}
