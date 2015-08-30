package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/7/25.
 */
public interface FindStudentBookOrderByPackageIdDAO extends EntityJpaDao<StudentBookOrder, Long> {
    @Query("FROM StudentBookOrder where packageId = ?1")
    public List<StudentBookOrder> findStudentBookOrderByPackageId(long packageId);
}
