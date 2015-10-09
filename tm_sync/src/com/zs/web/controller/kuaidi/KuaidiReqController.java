package com.zs.web.controller.kuaidi;

import com.zs.service.kuaidi.request.AddReqService;
import com.zs.web.controller.LoggerController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Allen on 2015/10/9.
 */
@Controller
@RequestMapping(value = "/kuaidiReq")
public class KuaidiReqController extends LoggerController {

    @Resource
    private AddReqService addReqService;

    @RequestMapping(value = "req")
    @ResponseBody
    public JSONObject index(@RequestParam("com") String com, @RequestParam("nu") String nu){
        JSONObject json = new JSONObject();
        try{
            addReqService.add(com, nu);
            json.put("state", 0);
        }catch (Exception e){
            e.printStackTrace();
            json.put("state", 1);
        }
        return json;
    }
}
