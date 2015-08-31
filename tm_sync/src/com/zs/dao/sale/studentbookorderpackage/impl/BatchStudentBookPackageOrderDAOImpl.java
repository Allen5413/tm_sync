package com.zs.dao.sale.studentbookorderpackage.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sale.studentbookorderpackage.BatchStudentBookPackageOrderDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/8/31.
 */
@Service("batchStudentBookPackageOrderDAO")
public class BatchStudentBookPackageOrderDAOImpl extends BaseQueryDao implements BatchStudentBookPackageOrderDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }

    @Override
    public void batchEdit(List list, int num) throws Exception {
        super.batchUpdate(list, num);
    }
}
