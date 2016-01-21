package com.zs.dao.placeorder.aopeng;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询奥鹏订购教材临时表
 * Created by Allen on 2016/1/21.
 */
public interface FindAoPengTeachMaterialDAO extends EntityJpaDao<TeachMaterialPlaceOrder, Long> {
    @Query(nativeQuery = true, value = "select * from aopeng_book order by spot_code")
    public List<Object[]> find();

    @Query(nativeQuery = true, value = "select spot_code from aopeng_book group by spot_code")
    public List<String> findSpotCode();
}
