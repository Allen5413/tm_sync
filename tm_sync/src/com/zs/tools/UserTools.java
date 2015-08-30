package com.zs.tools;

import com.zs.domain.basic.Resource;
import com.zs.domain.basic.User;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取登录用户的类型
     * @param request
     * @return
     */
    public static String getLoginUserForLoginType(HttpServletRequest request){
        if(null != request.getSession().getAttribute("loginType")){
            return request.getSession().getAttribute("loginType").toString();
        }else{
            return "";
        }
    }

    /**
     * 获取登录用户的省中心编号
     * @param request
     * @return
     */
    public static String getLoginUserForProvCode(HttpServletRequest request){
        if(null != request.getSession().getAttribute("provCode")){
            return request.getSession().getAttribute("provCode").toString();
        }else{
            return "";
        }
    }

    /**
     * 获取登录用户的学习中心编号
     * @param request
     * @return
     */
    public static String getLoginUserForSpotCode(HttpServletRequest request){
        if(null != request.getSession().getAttribute("spotCode")){
            return request.getSession().getAttribute("spotCode").toString();
        }else{
            return "";
        }
    }

    /**
     * 得到登录用户的所属菜单资源信息
     * @param request
     * @return
     */
    public static Map<String, List<Resource>> getLoginUserForMenu(HttpServletRequest request){
        if(null != request.getSession().getAttribute("loginType")){
            return (Map<String, List<Resource>>)request.getSession().getAttribute("menuMap");
        }else{
            return null;
        }
    }
}
