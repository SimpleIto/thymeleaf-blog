package org.ito.blog.interceptor;

import org.ito.blog.controller.LoginAdminController;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String admin = (String)request.getSession().getAttribute("admin");
       if(StringUtils.isEmpty(admin)||!admin.equals(LoginAdminController.USERNAME)) {
           response.sendRedirect("/blog/login");
           return false;
       }
       return true;
    }
}
