package com.zs.dao.kuaidi;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.PlaceOrderPackage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询还没有签收的快递单号
 * Created by Allen on 2015/10/9.
 */
public interface FindNotSignNuDAO extends EntityJpaDao<PlaceOrderPackage, Long> {
    @Query(nativeQuery = true, value = "select DISTINCT t.logistic_code from (" +
            "select DISTINCT sbop.logistic_code from student_book_order_package sbop " +
            "WHERE sbop.is_sign = 1 " +
            "union all " +
            "select DISTINCT pop.logistic_code from place_order_package pop " +
            "where pop.is_sign = 1) t")
    public List<String> findNotSignNu();
}
