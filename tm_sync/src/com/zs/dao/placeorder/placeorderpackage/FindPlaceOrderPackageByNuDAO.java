package com.zs.dao.placeorder.placeorderpackage;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.PlaceOrderPackage;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2015/10/6.
 */
public interface FindPlaceOrderPackageByNuDAO extends EntityJpaDao<PlaceOrderPackage, Long> {
    @Query("select count(*) from PlaceOrderPackage where logisticCode = ?1")
    public Long findPlaceOrderPackageForNotSign(String nu);
}
