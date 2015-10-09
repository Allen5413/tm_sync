package com.zs.service.kuaidi.request.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.kuaidi.request.KuaidiRequestDAO;
import com.zs.domain.kuaidi.KuaidiRequest;
import com.zs.service.kuaidi.request.FindKuaidiReqByNumberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Allen on 2015/10/9.
 */
@Service("findKuaidiReqByNumberService")
public class FindKuaidiReqByNumberServiceImpl extends EntityServiceImpl<KuaidiRequest, KuaidiRequestDAO> implements FindKuaidiReqByNumberService {

    @Resource
    private KuaidiRequestDAO kuaidiRequestDAO;

    @Override
    public List<KuaidiRequest> find(String number) {
        return kuaidiRequestDAO.findByNumber(number);
    }
}
