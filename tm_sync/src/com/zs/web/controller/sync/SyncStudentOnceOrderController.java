package com.zs.web.controller.sync;

import com.zs.service.scheduler.SyncStudentOnceOrderService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2016/6/16.
 */
@Controller
@RequestMapping(value = "/syncStudentOnceOrder")
public class SyncStudentOnceOrderController extends LoggerController {

    @Resource
    private SyncStudentOnceOrderService syncStudentOnceOrderService;

    @RequestMapping(value = "sync")
    public void sync(){
        try {
            syncStudentOnceOrderService.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
