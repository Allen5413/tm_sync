package com.zs.service.api;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.api.WangYuanApi;

/**
 * Created by Allen on 2017/5/3.
 */
public interface AddWangYuanApiService extends EntityService<WangYuanApi> {
    public void add(WangYuanApi wangYuanApi)throws Exception;
}
