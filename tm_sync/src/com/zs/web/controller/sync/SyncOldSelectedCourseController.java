package com.zs.web.controller.sync;

import com.zs.service.scheduler.SyncOldSelectedCourseService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2017/12/18.
 */
@Controller
@RequestMapping(value = "/syncOldSelectedCourse")
public class SyncOldSelectedCourseController extends LoggerController {

    @Resource
    private SyncOldSelectedCourseService syncOldSelectedCourseService;

    @RequestMapping(value = "sync")
    public void sync(@RequestParam("stuYearTerm")String stuYearTerm,
                     @RequestParam("courseYearTerms")String courseYearTerms)throws Exception{
        syncOldSelectedCourseService.sync(Integer.parseInt(stuYearTerm.split("_")[0]), Integer.parseInt(stuYearTerm.split("_")[1]), courseYearTerms);
    }
}
