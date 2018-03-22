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
    public KuaidiRequest add(String com, String nu, String msg, String returnCode) throws Exception {
        //不再请求快递100接口，改用阿里云接口2017-12-29
        //TaskResponse taskResponse = PostOrder.reqKuaidi100(com, nu.trim());

        KuaidiRequest kuaidiRequest = new KuaidiRequest();
        kuaidiRequest.setCompany(com);
        kuaidiRequest.setNumber(nu);
        kuaidiRequest.setMessage(msg);
        kuaidiRequest.setResult("true");
        kuaidiRequest.setReturnCode(returnCode);
        kuaidiRequestDAO.save(kuaidiRequest);
        return kuaidiRequest;
    }
}
