package com.zs.dao.basic.finance.spotexpenseoth.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.basic.finance.spotexpenseoth.BatchSpotExpenseOthDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/10/16.
 */
@Service("BatchSpotExpenseOthDAO")
public class BatchSpotExpenseOthDAOImpl extends BaseQueryDao implements BatchSpotExpenseOthDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }

    @Override
    public void batchEdit(List list, int num) throws Exception {
        super.batchUpdate(list, num);
    }
}
