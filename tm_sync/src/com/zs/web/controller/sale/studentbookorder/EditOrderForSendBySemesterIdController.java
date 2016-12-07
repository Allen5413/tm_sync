package com.zs.web.controller.sale.studentbookorder;

import com.zs.service.sale.studentbookorder.EditOrderForSendBySemesterIdService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2016/12/7.
 */
@Controller
@RequestMapping(value = "/editOrderForSendBySemesterId")
public class EditOrderForSendBySemesterIdController extends LoggerController {
    @Resource
    private EditOrderForSendBySemesterIdService editOrderForSendBySemesterIdService;

    @RequestMapping(value = "editor")
    public void editor(HttpServletRequest request, @RequestParam("id")long id){
        try {
            editOrderForSendBySemesterIdService.edit(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
