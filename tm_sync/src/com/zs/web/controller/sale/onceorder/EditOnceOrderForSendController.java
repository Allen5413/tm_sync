package com.zs.web.controller.sale.onceorder;

import com.zs.service.sale.onceorder.EditOnceOrderForSendService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Created by Allen on 2017/5/16.
 */
@Controller
@RequestMapping(value = "/editOnceOrderForSend")
public class EditOnceOrderForSendController extends LoggerController {

    @Resource
    private EditOnceOrderForSendService editOnceOrderForSendService;

    @RequestMapping(value = "editor")
    public void editor(@RequestParam("orderCode")String orderCode){
        try{
            editOnceOrderForSendService.edit(orderCode);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
