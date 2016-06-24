package com.zs.dao.sale.onceorderlog.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sale.onceorderlog.BatchStudentBookOnceOrderLogDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
@Service("batchStudentBookOnceOrderLog")
public class BatchStudentBookOnceOrderLogDAOImpl extends BaseQueryDao implements BatchStudentBookOnceOrderLogDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }
}
