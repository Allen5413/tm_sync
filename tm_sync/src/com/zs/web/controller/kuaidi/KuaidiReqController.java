package com.zs.web.controller.kuaidi;

import com.zs.service.kuaidi.request.AddReqService;
import com.zs.service.kuaidi.request.FindKuaidiReqByNumberService;
import com.zs.web.controller.LoggerController;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/10/9.
 */
@Controller
@RequestMapping(value = "/kuaidiReq")
public class KuaidiReqController extends LoggerController {

    @Resource
    private AddReqService addReqService;
    @Resource
    private FindKuaidiReqByNumberService findKuaidiReqByNumberService;

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

    @RequestMapping(value = "findByNumber")
    public String findByNumber(@RequestParam(value = "number", required = false, defaultValue = "") String number,
                               HttpServletRequest request){
        if(!StringUtils.isEmpty(number)) {
            request.setAttribute("list", findKuaidiReqByNumberService.find(number));
        }
        return "kuaidi/findKuaidiReq";
    }
}
