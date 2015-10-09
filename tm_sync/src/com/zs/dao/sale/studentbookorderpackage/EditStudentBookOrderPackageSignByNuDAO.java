package com.zs.dao.sale.studentbookorderpackage;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderPackage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Allen on 2015/10/6.
 */
public interface EditStudentBookOrderPackageSignByNuDAO extends EntityJpaDao<StudentBookOrderPackage, Long> {
    @Modifying
    @Query("update StudentBookOrderPackage set isSign = ?1, operator = '管理员', operateTime = ?2 where logisticCode = ?3")
    public void editStudentBookOrderPackageSignByNu(int isSign, Date operateTime, String nu);
}
