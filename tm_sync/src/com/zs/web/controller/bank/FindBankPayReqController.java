package com.zs.web.controller.bank;

import com.zs.service.bank.FindBankPayReqService;
import com.zs.web.controller.LoggerController;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2016/5/4.
 */
@Controller
@RequestMapping(value = "/findBankPayReq")
public class FindBankPayReqController extends LoggerController {

    private static Logger log = Logger.getLogger(FindBankPayReqController.class);

    @Resource
    private FindBankPayReqService findBankPayReqService;

    @RequestMapping(value = "req")
    @ResponseBody
    public JSONObject index(@RequestParam("orderNo") String orderNo, HttpServletRequest request){
        JSONObject json = new JSONObject();
        try{
            String result = findBankPayReqService.find(orderNo);
            json.put("state", 0);
            json.put("result", result);
        }catch (Exception e){
            String msg = super.outputException(request, e, log, "");
            json.put("state", 1);
            json.put("msg", msg);
        }
        return json;
    }

    @RequestMapping(value = "open")
    public String openRep(){
        return "bank/findBankPayReq";
    }
}
