package com.zs.service.api.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.api.WangYuanApiDAO;
import com.zs.domain.api.WangYuanApi;
import com.zs.service.api.AddWangYuanApiService;
import org.springframework.stereotype.Service;

/**
 * Created by Allen on 2017/5/3.
 */
@Service("addWangYuanApiService")
public class AddWangYuanApiServiceImpl extends EntityServiceImpl<WangYuanApi, WangYuanApiDAO> implements AddWangYuanApiService {

    @Override
    public void add(WangYuanApi wangYuanApi) throws Exception {
        super.save(wangYuanApi);
    }
}
