package com.zs.dao.placeorder.placeorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Allen on 2015/8/31.
 */
public interface EditPlaceOrderStateByPackageIdDAO extends EntityJpaDao<TeachMaterialPlaceOrder,Long> {
    @Modifying
    @Query("update TeachMaterialPlaceOrder set orderStatus = ?1, operator = ?2, operateTime = ?3 where packageId =?4")
    public void editPlaceOrderStateByPackageId(String state, String operator, Date operateTime, long packageId);
}
