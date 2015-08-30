package com.zs.dao.sale.studentbookorderpackage;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderPackage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 查询未签收的包
 * Created by Allen on 2015/7/23.
 */
public interface FindStudentBookOrderPackageForNotSignDAO extends EntityJpaDao<StudentBookOrderPackage, Long> {
    @Query(nativeQuery = true, value = "from StudentBookOrderPackage where isSign = 1 and logisticCode is not null and semesterId = ?1")
    public List<StudentBookOrderPackage> findStudentBookOrderPackageForNotSign(long semesterId);
}
