package com.zs.web.controller;

import com.zs.tools.FileTools;
import com.zs.tools.PropertiesTools;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by Allen on 2015/5/16.
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController extends LoggerController {
    @RequestMapping(value = "main")
    public String index(){
        return "index";
    }
}
