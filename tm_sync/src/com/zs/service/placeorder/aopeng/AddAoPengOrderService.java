package com.zs.service.placeorder.aopeng;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;

/**
 * Created by Allen on 2016/1/21.
 */
public interface AddAoPengOrderService extends EntityService<TeachMaterialPlaceOrder> {
    public void add(long semesterId)throws Exception;
}
