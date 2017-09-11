package com.zs.web.controller.sale.studentbookorder;

import com.zs.service.sale.studentbookorder.EditOrderForSendService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * Created by Allen on 2017/5/16.
 */
@Controller
@RequestMapping(value = "/editOrderForSend")
public class EditOrderForSendController extends LoggerController {

    @Resource
    private EditOrderForSendService editOrderForSendService;

    @RequestMapping(value = "editor")
    public void editor(@RequestParam("orderCodes")String orderCodes){
        try{
            editOrderForSendService.edit(orderCodes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
