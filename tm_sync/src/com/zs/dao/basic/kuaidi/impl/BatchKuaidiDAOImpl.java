package com.zs.dao.basic.kuaidi.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.basic.kuaidi.BatchKuaidiDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/8/31.
 */
@Service("batchKuaidiDAO")
public class BatchKuaidiDAOImpl extends BaseQueryDao implements BatchKuaidiDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }

    @Override
    public void batchEdit(List list, int num) throws Exception {
        super.batchUpdate(list, num);
    }
}
