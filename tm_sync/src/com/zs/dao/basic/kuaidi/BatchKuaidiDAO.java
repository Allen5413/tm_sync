package com.zs.dao.basic.kuaidi;

import java.util.List;

/**
 * Created by Allen on 2015/8/31.
 */
public interface BatchKuaidiDAO {
    public void batchAdd(List list, int num) throws Exception;

    public void batchEdit(List list, int num) throws Exception;
}
