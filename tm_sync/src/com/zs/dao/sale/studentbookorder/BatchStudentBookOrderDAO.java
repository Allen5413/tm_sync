package com.zs.dao.sale.studentbookorder;

import com.zs.domain.sale.StudentBookOrder;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
public interface BatchStudentBookOrderDAO {

    public void batchAdd(List list, int num) throws Exception;

    public void batchEdit(List list, int num) throws Exception;

    public void batchDelete(List<StudentBookOrder> list) throws Exception;
}
