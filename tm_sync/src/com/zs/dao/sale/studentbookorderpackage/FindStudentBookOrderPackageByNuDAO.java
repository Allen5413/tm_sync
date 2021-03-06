package com.zs.dao.sale.studentbookorderpackage;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sale.StudentBookOrderPackage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/10/6.
 */
public interface FindStudentBookOrderPackageByNuDAO extends EntityJpaDao<StudentBookOrderPackage, Long> {
    @Query("FROM StudentBookOrderPackage where logisticCode like ?1")
    public List<StudentBookOrderPackage> find(String logisticCode);

    @Query("select count(*) from StudentBookOrderPackage where logisticCode = ?1")
    public Long findStudentBookOrderPackageByNu(String nu);
}
