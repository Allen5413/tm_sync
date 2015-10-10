package com.zs.service.kuaidi.request.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.kuaidi.request.KuaidiRequestDAO;
import com.zs.domain.kuaidi.KuaidiRequest;
import com.zs.service.kuaidi.request.AddReqService;
import com.zs.tools.HttpRequestTools;
import com.zs.tools.kuaidi.demo.PostOrder;
import com.zs.tools.kuaidi.pojo.TaskResponse;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Allen on 2015/10/9.
 */
@Service("addReqService")
public class AddReqServiceImpl extends EntityServiceImpl<KuaidiRequest, KuaidiRequestDAO> implements AddReqService{
    @Resource
    private KuaidiRequestDAO kuaidiRequestDAO;

    @Override
    public KuaidiRequest add(String com, String nu) throws Exception {
        TaskResponse taskResponse = PostOrder.reqKuaidi100(com, nu);

        KuaidiRequest kuaidiRequest = new KuaidiRequest();
        kuaidiRequest.setCompany(com);
        kuaidiRequest.setNumber(nu);
        if(null != taskResponse){
            kuaidiRequest.setMessage(taskResponse.getMessage());
            kuaidiRequest.setResult(taskResponse.getResult().toString());
            kuaidiRequest.setReturnCode(taskResponse.getReturnCode());
        }
        kuaidiRequestDAO.save(kuaidiRequest);
        return kuaidiRequest;
    }
}
