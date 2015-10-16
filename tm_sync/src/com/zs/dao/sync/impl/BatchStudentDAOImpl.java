package com.zs.dao.sync.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sync.BatchStudentDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/10/16.
 */
@Service("batchStudentDAO")
public class BatchStudentDAOImpl extends BaseQueryDao implements BatchStudentDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }

    @Override
    public void batchEdit(List list, int num) throws Exception {
        super.batchUpdate(list, num);
    }
}
