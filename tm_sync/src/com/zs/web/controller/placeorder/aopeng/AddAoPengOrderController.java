package com.zs.web.controller.placeorder.aopeng;

import com.zs.service.placeorder.aopeng.AddAoPengOrderService;
import com.zs.web.controller.LoggerController;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Allen on 2016/1/21.
 */
@Controller
@RequestMapping(value = "/addAoPengOrder")
public class AddAoPengOrderController extends LoggerController {

    @Resource
    private AddAoPengOrderService addAoPengOrderService;

    @RequestMapping(value = "add")
    @ResponseBody
    public JSONObject add(@RequestParam("semesterId") long semesterId){
        JSONObject json = new JSONObject();
        try{
            addAoPengOrderService.add(semesterId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }
}
