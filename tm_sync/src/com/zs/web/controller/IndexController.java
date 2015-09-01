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
    public String index(@RequestParam(value = "path", required = false, defaultValue = "") String path,
                        @RequestParam(value = "name", required = false, defaultValue = "") String name,
                        HttpServletRequest request){
        //获取文件夹下面的内容
        if(!StringUtils.isEmpty(path) && !StringUtils.isEmpty(name)){
            File file = new File(path+"/"+name);
            if(file.isDirectory()) {
                JSONArray jsonArray = FileTools.getPathFile(path + "/" + name);
                request.setAttribute("result", jsonArray);
            }else{
                try {
                    String content = FileTools.readTxtFile(file);
                    request.setAttribute("content", content.replaceAll("\r\n", "<br />"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            PropertiesTools propertiesTools =  new PropertiesTools("resource/commons.properties");
            String rootPath = propertiesTools.getProperty("sync.log.file.path");
            JSONArray jsonArray = FileTools.getPathFile(rootPath);
            request.setAttribute("result", jsonArray);
        }
        return "index";
    }
}
