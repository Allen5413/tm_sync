package com.zs.tools;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录用户相关信息
 * Created by Allen on 2015/4/27.
 */
public class UserTools {

    /**
     * 获取登录用户的登录名
     * @param request
     * @return
     */
    public static String getLoginUserForLoginName(HttpServletRequest request){
        if(null != request.getSession().getAttribute("loginName")){
            return request.getSession().getAttribute("loginName").toString();
        }else{
            return "";
        }
    }

    /**
     * 获取登录用户的用户姓名
     * @param request
     * @return
     */
    public static String getLoginUserForName(HttpServletRequest request){
        if(null != request.getSession().getAttribute("name")){
            return request.getSession().getAttribute("name").toString();
        }else{
            return "";
        }
    }
}
