package com.zs.service.kuaidi.push;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.kuaidi.KuaidiPush;

/**
 * Created by Allen on 2015/9/30.
 */
public interface AddPushService extends EntityService<KuaidiPush>{
    public void add(String json)throws Exception;
}
