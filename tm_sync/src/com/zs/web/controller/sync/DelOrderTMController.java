package com.zs.web.controller.sync;

import com.zs.service.scheduler.TempService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2015/10/26.
 */
@Controller
@RequestMapping(value = "/delOrderTM")
public class DelOrderTMController extends LoggerController {

    @Resource
    private TempService tempService;

    @RequestMapping(value = "sync")
    public void sync(HttpServletRequest request){
        tempService.doSync();
    }

    @RequestMapping(value = "sync2")
    public void sync2(@RequestParam("code") String code, HttpServletRequest request){
        tempService.doSync2(code);
    }

    @RequestMapping(value = "sync3")
    public void sync3(HttpServletRequest request){
        tempService.doSync3();
    }

    @RequestMapping(value = "sync4")
    public void sync4(@RequestParam("code") String code){
        tempService.doSync4(code);
    }

    @RequestMapping(value = "sync5")
    public void sync5(){
        tempService.doSync5();
    }
}
