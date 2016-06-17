package com.zs.dao.sale.studentbookonceorder.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sale.studentbookonceorder.BatchStudentBookOnceOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2016/6/16.
 */
@Service("batchStudentBookOnceOrderDAO")
public class BatchStudentBookOnceOrderDAOImpl extends BaseQueryDao implements BatchStudentBookOnceOrderDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }
}
