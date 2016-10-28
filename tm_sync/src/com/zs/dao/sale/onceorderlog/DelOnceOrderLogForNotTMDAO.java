package com.zs.dao.sale.onceorderlog;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrderLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2016/10/28.
 */
public interface DelOnceOrderLogForNotTMDAO extends EntityJpaDao<StudentBookOnceOrderLog, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete sbol from student_book_once_order_log sbol " +
            "where not EXISTS(select * from student_book_once_order_tm sbotm where sbol.order_id = sbotm.order_id)")
    public void del();
}
