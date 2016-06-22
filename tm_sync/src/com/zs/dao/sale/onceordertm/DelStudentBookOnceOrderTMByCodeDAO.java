package com.zs.dao.sale.onceordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOnceOrderTM;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2016/6/17.
 */
public interface DelStudentBookOnceOrderTMByCodeDAO extends EntityJpaDao<StudentBookOnceOrderTM, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "delete from student_book_once_order_tm where order_code = ?1")
    public void del(String CODE);
}
