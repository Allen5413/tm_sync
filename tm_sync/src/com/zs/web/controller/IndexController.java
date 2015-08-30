package com.zs.web.controller;

import com.zs.domain.basic.User;
import com.zs.service.basic.user.ValidateLoginService;
import com.zs.tools.DateTools;
import com.zs.tools.UserTools;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/5/16.
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController extends LoggerController {
    private static Logger log = Logger.getLogger(IndexController.class);
    @RequestMapping(value = "main")
    public String index(HttpServletRequest request){
        //获取当前年月日星期
        String year = DateTools.getThisYear();
        String month = DateTools.getThisMonth();
        String day = DateTools.getThisDay();
        String week = DateTools.getThisWeek();
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("day", day);
        request.setAttribute("week", week);
        request.setAttribute("loginName", UserTools.getLoginUserForLoginName(request));
        request.setAttribute("name", UserTools.getLoginUserForName(request));
        request.setAttribute("menu", UserTools.getLoginUserForMenu(request));
        return "index";
    }
}
