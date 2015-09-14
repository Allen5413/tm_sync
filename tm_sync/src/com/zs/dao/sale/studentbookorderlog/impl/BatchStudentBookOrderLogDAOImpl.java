package com.zs.dao.sale.studentbookorderlog.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sale.studentbookorderlog.BatchStudentBookOrderLogDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
@Service("batchStudentBookOrderLog")
public class BatchStudentBookOrderLogDAOImpl extends BaseQueryDao implements BatchStudentBookOrderLogDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }
}
