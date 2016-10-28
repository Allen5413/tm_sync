package com.zs.dao.sale.onceorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2016/10/28.
 */
public interface DelOnceOrderForNotTMDAO extends EntityJpaDao<StudentBookOnceOrder, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete sbo from student_book_once_order sbo " +
            "where not EXISTS(select * from student_book_once_order_tm sbotm where sbo.id = sbotm.order_id)")
    public void del();
}
