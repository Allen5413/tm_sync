package com.zs.service.kuaidi.request.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.kuaidi.request.KuaidiRequestDAO;
import com.zs.domain.kuaidi.KuaidiRequest;
import com.zs.service.kuaidi.request.AddReqService;
import com.zs.tools.HttpRequestTools;
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
    public void add(String com, String nu) throws Exception {
        JSONObject reqJSON = HttpRequestTools.reqKuaidi100(com, nu);

        KuaidiRequest kuaidiRequest = new KuaidiRequest();
        kuaidiRequest.setCompany(com);
        kuaidiRequest.setNumber(nu);
        if(null != reqJSON){
            kuaidiRequest.setMessage(null == reqJSON.get("message") ? null : reqJSON.get("message").toString());
            kuaidiRequest.setResult(null == reqJSON.get("result") ? null : reqJSON.get("result").toString());
            kuaidiRequest.setReturnCode(null == reqJSON.get("returnCode") ? null : reqJSON.get("returnCode").toString());
        }
        kuaidiRequestDAO.save(kuaidiRequest);
    }
}
