package com.zs.dao.sale.studentbookorder.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
@Service("batchStudentBookOrderDAO")
public class BatchStudentBookOrderDAOImpl extends BaseQueryDao implements BatchStudentBookOrderDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }

    @Override
    public void batchEdit(List list, int num) throws Exception {
        super.batchUpdate(list, num);
    }
}
