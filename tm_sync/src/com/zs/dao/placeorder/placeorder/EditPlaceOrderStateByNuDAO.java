package com.zs.dao.placeorder.placeorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Allen on 2015/10/6.
 */
public interface EditPlaceOrderStateByNuDAO extends EntityJpaDao<TeachMaterialPlaceOrder,Long> {
    @Modifying
    @Query(nativeQuery = true, value = "update teach_material_place_order tmpo INNER JOIN place_order_package pop on tmpo.package_id = pop.id " +
            "set tmpo.order_status = ?1, tmpo.operator = '管理员', tmpo.operate_time = ?2 where pop.logistic_code = ?3")
    public void editStudentOrderStateByNu(String state, Date operateTime, String nu);
}
