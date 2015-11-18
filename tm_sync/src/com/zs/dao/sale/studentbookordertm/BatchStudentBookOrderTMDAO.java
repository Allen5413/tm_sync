package com.zs.dao.sale.studentbookordertm;

import com.zs.domain.sale.StudentBookOrderTM;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
public interface BatchStudentBookOrderTMDAO {
    public void batchAdd(List list, int num) throws Exception;

    public void batchUpdate(List list, int num) throws Exception;

    public void batchDelete(List<StudentBookOrderTM> list) throws Exception;
}
