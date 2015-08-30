package com.zs.dao.sale.studentbookordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderTM;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/7/8.
 */
public interface FindStudentBookOrderTMByOrderCodeDAO extends EntityJpaDao<StudentBookOrderTM, Long> {
    @Query("from StudentBookOrderTM where orderCode = ?1")
    public List<StudentBookOrderTM> findStudentBookOrderTMByOrderCode(String orderCode);
}
