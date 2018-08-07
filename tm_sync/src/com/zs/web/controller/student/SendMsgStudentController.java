package com.zs.web.controller.student;

import com.zs.service.sync.student.SendMsgStudentService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/10/19.
 */
@Controller
@RequestMapping(value = "/sendMsgStudent")
public class SendMsgStudentController extends LoggerController {

    @Resource
    private SendMsgStudentService sendMsgStudentService;

    @RequestMapping(value = "send")
    public void sync(HttpServletRequest request,
                     @RequestParam("year")int year,
                     @RequestParam("quarter")int quarter,
                     @RequestParam("isNewMb")int isNewMb) throws Exception {
        try {
            sendMsgStudentService.send(year, quarter, isNewMb);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
