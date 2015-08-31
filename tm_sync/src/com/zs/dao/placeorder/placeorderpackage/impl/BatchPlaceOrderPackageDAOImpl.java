package com.zs.dao.placeorder.placeorderpackage.impl;

import com.zs.dao.BaseQueryDao;
import com.zs.dao.placeorder.placeorderpackage.BatchPlaceOrderPackageDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Allen on 2015/8/31.
 */
@Service("batchPlaceOrderPackageDAO")
public class BatchPlaceOrderPackageDAOImpl extends BaseQueryDao implements BatchPlaceOrderPackageDAO {
    @Override
    public void batchAdd(List list, int num) throws Exception {
        super.batchInsert(list, num);
    }

    @Override
    public void batchEdit(List list, int num) throws Exception {
        super.batchUpdate(list, num);
    }
}
