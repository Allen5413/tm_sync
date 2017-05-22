package com.zs.dao.temp.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sync.BatchStudentTempDAO;
import com.zs.dao.temp.BatchAaaDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/10/16.
 */
@Service("batchAaaDAO")
public class BatchAaaDAOImpl extends BaseQueryDao implements BatchAaaDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }
}
