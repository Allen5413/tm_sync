package com.zs.web.Interceptor;

import com.zs.domain.basic.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证拦截器
 * Created by Allen on 2015/4/26.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{
    //不需要拦截的路径
    private static final String[] IGNORE_URI = {"/login", "/css", "/kuaidiReq/req", "/kuaidiPush/push"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        String url = request.getRequestURL().toString();
        for (String s : IGNORE_URI) {
            if (url.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            String loginName = null == request.getSession().getAttribute("loginName") ? "" : request.getSession().getAttribute("loginName").toString();
            String loginType = null == request.getSession().getAttribute("loginType") ? "" : request.getSession().getAttribute("loginType").toString();
            if (!StringUtils.isEmpty(loginName) && !StringUtils.isEmpty(loginType) && User.TYPE_ADMIN == Integer.parseInt(loginType)) {
                flag = true;
            }else{
                response.sendRedirect("/login.jsp");
            }

        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

}
