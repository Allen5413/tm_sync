package com.zs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/5/16.
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController extends LoggerController {
    @RequestMapping(value = "main")
    public String index(HttpServletRequest request){
        //获取文件夹下面的内容

        return "index";
    }
}
