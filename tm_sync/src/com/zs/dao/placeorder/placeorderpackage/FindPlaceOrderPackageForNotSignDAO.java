package com.zs.dao.placeorder.placeorderpackage;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.PlaceOrderPackage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/8/31.
 */
public interface FindPlaceOrderPackageForNotSignDAO extends EntityJpaDao<PlaceOrderPackage, Long> {
    @Query("from PlaceOrderPackage where isSign = 1 and logisticCode is not null and semesterId = ?1")
    public List<PlaceOrderPackage> findPlaceOrderPackageForNotSign(long semesterId);
}
