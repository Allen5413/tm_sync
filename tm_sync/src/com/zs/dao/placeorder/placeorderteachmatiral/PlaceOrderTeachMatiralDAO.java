package com.zs.dao.placeorder.placeorderteachmatiral;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.PlaceOrderTeachMaterial;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/10/29.
 */
public interface PlaceOrderTeachMatiralDAO extends EntityJpaDao<PlaceOrderTeachMaterial, Long> {
    @Query("from PlaceOrderTeachMaterial where orderId = ?1")
    public List<PlaceOrderTeachMaterial> getPlaceOrderTeachMaterialByOrderID(long orderId);
}
