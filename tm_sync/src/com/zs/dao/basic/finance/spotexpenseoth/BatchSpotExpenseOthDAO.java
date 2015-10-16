package com.zs.dao.basic.finance.spotexpenseoth;

import java.util.List;

/**
 * Created by Allen on 2015/10/16.
 */
public interface BatchSpotExpenseOthDAO {
    public void batchAdd(List list, int num) throws Exception;

    public void batchEdit(List list, int num) throws Exception;
}
