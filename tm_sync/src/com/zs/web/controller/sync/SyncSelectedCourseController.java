package com.zs.web.controller.sync;

import com.zs.service.scheduler.SyncSelectedCourseTaskService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/10/26.
 */
@Controller
@RequestMapping(value = "/syncSelectedCourse")
public class SyncSelectedCourseController extends LoggerController {

    @Resource
    private SyncSelectedCourseTaskService syncSelectedCourseTaskService;

    @RequestMapping(value = "sync")
    public void sync(HttpServletRequest request){
        syncSelectedCourseTaskService.syncSelectedCourse();
        syncSelectedCourseTaskService.delChangeSelectedCourse();
        syncSelectedCourseTaskService.delHSTmBySermesterId();
    }
}
