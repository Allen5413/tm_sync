package com.zs.service.kuaidi.push;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.kuaidi.KuaidiPush;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * Created by Allen on 2015/9/30.
 */
public interface AddPushService extends EntityService<KuaidiPush>{
    public Map<String, Object> add(String json)throws Exception;

    public void editOrderState(String nu)throws Exception;

    public void sendOrderForWX(String nu, String newData)throws Exception;
}
