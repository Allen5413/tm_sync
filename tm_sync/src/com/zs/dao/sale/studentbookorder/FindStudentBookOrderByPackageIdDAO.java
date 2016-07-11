package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/7/11.
 */
public interface FindStudentBookOrderByPackageIdDAO extends EntityJpaDao<StudentBookOrder,Long> {
    @Query("from StudentBookOrder where packageId = ?1")
    public List<StudentBookOrder> find(long packageId);
}
