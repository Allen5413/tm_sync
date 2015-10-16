package com.zs.dao.sync;

import java.util.List;

/**
 * Created by Allen on 2015/10/16.
 */
public interface BatchSelectedCourseDAO {
    public void batchAdd(List list, int num) throws Exception;

    public void batchEdit(List list, int num) throws Exception;
}
