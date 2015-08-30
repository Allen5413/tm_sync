package com.zs.dao.sale.studentbookordertm;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderTM;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/7/11.
 */
public interface FindStudentBookOrderTMByTMIdAndStateDAO extends EntityJpaDao<StudentBookOrderTM, Long> {
    @Query("select sbotm from StudentBookOrder sbo, StudentBookOrderTM sbotm where sbo.orderCode = sbotm.orderCode and sbotm.courseCode is not null and sbotm.teachMaterialId = ?1 and sbo.state < ?2 order by sbotm.orderCode")
    public List<StudentBookOrderTM> findStudentBookOrderTMByTMIdAndState(long teachMaterialId, int state);
}
