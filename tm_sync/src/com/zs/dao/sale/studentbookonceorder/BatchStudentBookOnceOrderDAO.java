package com.zs.dao.sale.studentbookonceorder;

import com.zs.domain.sale.StudentBookOnceOrder;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
public interface BatchStudentBookOnceOrderDAO {

    public void batchAdd(List list, int num) throws Exception;
}
