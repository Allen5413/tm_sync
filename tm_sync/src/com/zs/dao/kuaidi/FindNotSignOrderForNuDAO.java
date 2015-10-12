package com.zs.dao.kuaidi;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.placeorder.PlaceOrderPackage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询已发出还没有签收的订单的快递单号
 * Created by Allen on 2015/10/11.
 */
public interface FindNotSignOrderForNuDAO extends EntityJpaDao<PlaceOrderPackage, Long> {
    @Query(nativeQuery = true, value = "select DISTINCT t.logistic_code from(" +
            "select DISTINCT sbop.logistic_code from student_book_order sbo, student_book_order_package sbop " +
            "where sbo.state = 4 and sbo.package_id = sbop.id and sbop.logistic_code is not null " +
            "UNION ALL " +
            "select DISTINCT pop.logistic_code from teach_material_place_order tmpo, place_order_package pop " +
            "where tmpo.order_status = '4' and tmpo.package_id = pop.id and pop.logistic_code is not null) t")
    public List<String> find();
}
