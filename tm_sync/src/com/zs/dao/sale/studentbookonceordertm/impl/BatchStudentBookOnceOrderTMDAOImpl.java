package com.zs.dao.sale.studentbookonceordertm.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sale.studentbookonceordertm.BatchStudentBookOnceOrderTMDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
@Service("batchStudentBookOnceOrderTM")
public class BatchStudentBookOnceOrderTMDAOImpl extends BaseQueryDao implements BatchStudentBookOnceOrderTMDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }
}
