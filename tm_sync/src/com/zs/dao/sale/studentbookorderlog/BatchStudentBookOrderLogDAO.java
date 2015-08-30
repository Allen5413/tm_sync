package com.zs.dao.sale.studentbookorderlog;

import java.util.List;

/**
 * Created by Allen on 2015/7/10.
 */
public interface BatchStudentBookOrderLogDAO {
    public void batchAdd(List list, int num) throws Exception;
}
