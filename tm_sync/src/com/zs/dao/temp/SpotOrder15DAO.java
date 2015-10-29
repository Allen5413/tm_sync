package com.zs.dao.temp;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.Student;
import com.zs.domain.temp.SpotOrder15;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;

/**
 * Created by Allen on 2015/10/29.
 */
public interface SpotOrder15DAO extends EntityJpaDao<Student, Long> {
    @Query(nativeQuery = true, value = "select * from spot_order15 where is_do = 0 and spot_code = ?1")
    public List<Object[]> find(String code);

    @Query(nativeQuery = true, value = "select sso.*, ifnull(sp.money, 0) money from student_order15 sso INNER JOIN sync_student s on sso.student_code = s.code and s.spot_code = ?1 " +
            "LEFT JOIN student_expense_pay sp on sso.student_code = sp.student_code " +
            "order by money desc, student_code")
    public List<Object[]> findStudent(String code);
}
