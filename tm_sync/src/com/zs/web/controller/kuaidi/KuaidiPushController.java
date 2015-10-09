package com.zs.web.controller.kuaidi;

import com.zs.domain.kuaidi.KuaidiPush;
import com.zs.service.kuaidi.push.AddPushService;
import com.zs.tools.FileTools;
import com.zs.tools.PropertiesTools;
import com.zs.web.controller.LoggerController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by Allen on 2015/9/30.
 */
@Controller
@RequestMapping(value = "/kuaidiPush")
public class KuaidiPushController extends LoggerController {

    @Resource
    private AddPushService addPushService;

    @RequestMapping(value = "open")
    public String open(){
        return "kuaidi/kuaidiPush";
    }

    @RequestMapping(value = "push")
    @ResponseBody
    public JSONObject index(@RequestParam(value = "param", required = false, defaultValue = "") String param){
        JSONObject jsonObject = new JSONObject();
        try{
            KuaidiPush kuaidiPush = addPushService.add(param);
            if(null != kuaidiPush){
                if(3 == kuaidiPush.getState()){
                    if(!StringUtils.isEmpty(kuaidiPush.getNu())) {
                        addPushService.editOrderState(kuaidiPush.getNu());
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jsonObject.put("result", "true");
            jsonObject.put("returnCode", "200");
            jsonObject.put("message", "成功");
        }
        return jsonObject;
    }

    @RequestMapping(value = "find")
    public String index(HttpServletRequest request){
        request.setAttribute("list", addPushService.getAll());
        return "kuaidi/findKuaidiPush";
    }
}
