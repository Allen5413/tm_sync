package com.zs.web.controller.sync;

import com.feinno.framework.common.dao.support.PageInfo;
import com.zs.domain.sync.Spot;
import com.zs.service.scheduler.SyncStudentOnceOrderService;
import com.zs.tools.UserTools;
import com.zs.web.controller.LoggerController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = "syncTempAdjust")
    public void find(@RequestParam(value="spotCode", required=false, defaultValue="") String spotCode,
                       @RequestParam(value="specCode", required=false, defaultValue="") String specCode,
                       @RequestParam(value="levelCode", required=false, defaultValue="") String levelCode,
                       @RequestParam(value="studentCodes", required=false, defaultValue="") String studentCodes,
                       @RequestParam(value="year", required=false, defaultValue="") String year,
                       @RequestParam(value="quarter", required=false, defaultValue="") String quarter){
        try{
            Map<String, String> params = new HashMap<String, String>();
            params.put("spotCode", spotCode);
            params.put("specCode", specCode);
            params.put("levelCode", levelCode);
            params.put("studentCodes", studentCodes);
            params.put("year", year);
            params.put("quarter", quarter);
            syncStudentOnceOrderService.syncTempAdjust(params);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
