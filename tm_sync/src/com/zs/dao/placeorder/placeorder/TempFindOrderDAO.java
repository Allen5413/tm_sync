package com.zs.dao.placeorder.placeorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/3/17.
 */
public interface TempFindOrderDAO extends EntityJpaDao<TeachMaterialPlaceOrder, Long> {

    @Query("from TeachMaterialPlaceOrder where orderStatus > 3 and specCode is null and semesterId = 3")
    public List<TeachMaterialPlaceOrder> find();
}
