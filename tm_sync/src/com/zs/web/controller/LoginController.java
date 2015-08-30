package com.zs.web.controller;


import com.zs.domain.basic.User;
import com.zs.service.basic.user.ValidateLoginService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录Controller
 * Created by Allen on 2015/4/25.
 */
@Controller
@RequestMapping(value = "/loginUser")
public class LoginController extends LoggerController<User, ValidateLoginService> {
    private static Logger log = Logger.getLogger(LoginController.class);

    @Resource
    private ValidateLoginService validateLoginService;

    /**
     * 用户登录
     * @param request
     * @return
     */
    @RequestMapping(value = "login")
    @ResponseBody
    public JSONObject login(@RequestParam(value="loginName") String loginName,
                        @RequestParam(value="pwd", required = false, defaultValue = "") String pwd,
                        @RequestParam(value="type", required = false, defaultValue = "0") String type,
            HttpServletRequest request, HttpServletResponse response){
        String msg = "";
        JSONObject jsonObject = new JSONObject();
        try{
            msg = loginUser(request, loginName, pwd);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            jsonObject.put("msg", msg);
        }
        return jsonObject;
    }

    protected String loginUser(HttpServletRequest request, String loginName, String pwd)throws Exception{
        User user = validateLoginService.validateLogin(loginName, pwd);
        if(null != user && !StringUtils.isEmpty(user.getLoginName())){
            return "success";
        }else {
            return "用户名密码错误";
        }
    }
}
