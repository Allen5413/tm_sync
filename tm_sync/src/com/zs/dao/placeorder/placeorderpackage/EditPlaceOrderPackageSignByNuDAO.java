package com.zs.dao.placeorder.placeorderpackage;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.PlaceOrderPackage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Allen on 2015/10/6.
 */
public interface EditPlaceOrderPackageSignByNuDAO extends EntityJpaDao<PlaceOrderPackage, Long> {
    @Modifying
    @Query("update PlaceOrderPackage set isSign = ?1, operator = '管理员', operateTime = ?2 where logisticCode = ?3")
    public void editPlaceOrderPackageSignByNu(int isSign, Date operateTime, String nu);
}
