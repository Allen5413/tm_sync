package com.zs.dao.sale.studentbookorderlog;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderLog;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/6/16.
 */
public interface FindStudentBookOrderLogByCodeDAO extends EntityJpaDao<StudentBookOrderLog, Long> {
    @Query("FROM StudentBookOrderLog WHERE orderCode = ?1")
    public List<StudentBookOrderLog> find(String orderCode);
}
