package com.zs.dao.sync;

import java.util.List;

/**
 * Created by Allen on 2015/10/16.
 */
public interface BatchStudentTempDAO {
    public void batchAdd(List list, int num) throws Exception;
}
