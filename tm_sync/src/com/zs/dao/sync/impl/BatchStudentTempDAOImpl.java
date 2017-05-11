package com.zs.dao.sync.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sync.BatchStudentTempDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/10/16.
 */
@Service("batchStudentTempDAO")
public class BatchStudentTempDAOImpl extends BaseQueryDao implements BatchStudentTempDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }
}
