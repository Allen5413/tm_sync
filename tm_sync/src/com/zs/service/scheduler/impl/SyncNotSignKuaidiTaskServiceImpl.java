package com.zs.service.scheduler.impl;

import com.zs.dao.kuaidi.FindNotSignNuDAO;
import com.zs.dao.kuaidi.request.KuaidiRequestDAO;
import com.zs.domain.kuaidi.KuaidiRequest;
import com.zs.service.kuaidi.request.AddReqService;
import com.zs.service.scheduler.SyncNotSignKuaidiTaskService;
import com.zs.tools.HttpRequestTools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 同步还没有签收的快递信息
 * Created by Allen on 2015/10/9.
 */
@Service("syncNotSignKuaidiTaskService")
public class SyncNotSignKuaidiTaskServiceImpl implements SyncNotSignKuaidiTaskService {

    @Resource
    private FindNotSignNuDAO findNotSignNuDAO;
    @Resource
    private KuaidiRequestDAO kuaidiRequestDAO;
    @Resource
    private AddReqService addReqService;

    @Override
    public void syncKuaidi() {
        int i = 1;
        try {
            //查询还没有签收的快递单号
            List<String> logisticCodeList = findNotSignNuDAO.findNotSignNu();
            if (null != logisticCodeList && 0 < logisticCodeList.size()) {
                for (String logisticCode : logisticCodeList) {
                    String[] nus = logisticCode.split(",");
                    if (null != nus) {
                        for (String nu : nus) {
                            //查询nu是否已经请求过，如果没有请求过，或者请求的返回状态是[500: 服务器错误],我们就重新在再请求一次
                            List<KuaidiRequest> kuaidiRequestList = kuaidiRequestDAO.findByNumber(nu);
                            boolean isReqAgain = true;
                            if (null != kuaidiRequestList && 0 < kuaidiRequestList.size()) {
                                for (KuaidiRequest kuaidiRequest : kuaidiRequestList) {
                                    if (!"500".equals(kuaidiRequest.getReturnCode())) {
                                        isReqAgain = false;
                                        break;
                                    }
                                }
                            }
                            if (isReqAgain) {
                                Thread.sleep(10000);
                                addReqService.add("ems", nu);
                                System.out.println("i     "+i);
                                i++;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
