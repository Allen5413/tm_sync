package com.zs.web.controller.sale.onceorder;

import com.zs.service.sale.onceorder.DelOnceOrderForNotTMService;
import com.zs.web.controller.LoggerController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Allen on 2016/10/28.
 */
@Controller
@RequestMapping(value = "/delOnceOrderForNotTM")
public class DelOnceOrderForNotTMController extends LoggerController {

    @Resource
    private DelOnceOrderForNotTMService delOnceOrderForNotTMService;

    @RequestMapping(value = "del")
    @ResponseBody
    public JSONObject del(){
        JSONObject jsonObject = new JSONObject();
        try{
            delOnceOrderForNotTMService.del();
            jsonObject.put("result", "true");
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("result", "false");
        }
        return jsonObject;
    }
}
