package com.zs.service.kuaidi.request;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.kuaidi.KuaidiRequest;

import java.util.List;

/**
 * Created by Allen on 2015/10/9.
 */
public interface FindKuaidiReqByNumberService extends EntityService<KuaidiRequest>{
    public List<KuaidiRequest> find(String number);
}
