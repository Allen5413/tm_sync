package com.zs.web.controller.sync;

import com.zs.service.scheduler.SyncSpotTaskService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/10/19.
 */
@Controller
@RequestMapping(value = "/syncSpot")
public class SyncSpotController extends LoggerController {

    @Resource
    private SyncSpotTaskService syncSpotTaskService;

    @RequestMapping(value = "sync")
    public void sync(HttpServletRequest request){
        syncSpotTaskService.syncSpot();
    }
}
