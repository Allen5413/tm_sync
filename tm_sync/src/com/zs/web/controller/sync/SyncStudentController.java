package com.zs.web.controller.sync;

import com.zs.service.scheduler.SyncStudentTaskService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/10/15.
 */
@Controller
@RequestMapping(value = "/syncStudent")
public class SyncStudentController extends LoggerController {

    @Resource
    private SyncStudentTaskService syncStudentTaskService;

    @RequestMapping(value = "sync")
    public void sync(HttpServletRequest request){
        syncStudentTaskService.syncStudent();
    }
}
