package com.zs.service.kuaidi.request;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.kuaidi.KuaidiRequest;

/**
 * Created by Allen on 2015/10/9.
 */
public interface AddReqService extends EntityService<KuaidiRequest> {
    public void add(String com, String nu)throws Exception;
}