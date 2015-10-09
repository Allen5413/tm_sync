package com.zs.dao.placeorder.placeorderlog;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.PlaceOrderLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PlaceOrderLogDAO extends EntityJpaDao<PlaceOrderLog, Long>{
    @Modifying
    @Query(nativeQuery = true, value = "INSERT into place_order_log(order_id, state, operator, operate_time) select DISTINCT tmpo.id, '5', '管理员', now() " +
            "from teach_material_place_order tmpo, place_order_package pop " +
            "where tmpo.package_id = pop.id and pop.logistic_code= ?1")
    public void addSignOrderLog(String nu);
}
