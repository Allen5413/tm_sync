package com.zs.dao.sale.studentbookordertm.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.domain.sale.StudentBookOrderTM;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
@Service("batchStudentBookOrderTM")
public class BatchStudentBookOrderTMDAOImpl extends BaseQueryDao implements BatchStudentBookOrderTMDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }

    @Override
    public void batchUpdate(List list, int num) throws Exception{
        super.batchUpdate(list, num);
    }

    @Override
    public void batchDelete(List<StudentBookOrderTM> list) throws Exception {
        super.batchDeleteForStudentBookOrderTM(list);
    }
}
