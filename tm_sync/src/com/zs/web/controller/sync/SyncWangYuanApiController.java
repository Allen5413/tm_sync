package com.zs.web.controller.sync;

import com.zs.service.scheduler.SyncWangYuanApiService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2017/5/8.
 */
@Controller
@RequestMapping(value = "/syncWangYuanApi")
public class SyncWangYuanApiController extends LoggerController {

    @Resource
    private SyncWangYuanApiService syncWangYuanApiService;

    @RequestMapping(value = "sync")
    public void sync(HttpServletRequest request){
        syncWangYuanApiService.sync();
    }

    @RequestMapping(value = "syncAllStudent")
    public void syncAllStudent(HttpServletRequest request){
        for(int year = 1995; year < 2018; year++){
            syncWangYuanApiService.allStudent(year, 1);
            syncWangYuanApiService.allStudent(year, 2);
        }
    }
}
