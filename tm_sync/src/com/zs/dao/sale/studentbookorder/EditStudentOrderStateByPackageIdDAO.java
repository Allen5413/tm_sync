package com.zs.dao.sale.studentbookorder;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Allen on 2015/8/31.
 */
public interface EditStudentOrderStateByPackageIdDAO extends EntityJpaDao<StudentBookOrder,Long> {
    @Modifying
    @Query("update StudentBookOrder set state = ?1, operator = ?2, operateTime = ?3 where packageId =?4")
    public void editStudentOrderStateByPackageId(int state, String operator, Date operateTime, long packageId);
}
