package com.zs.dao.basic.semester;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.Semester;
import org.springframework.data.jpa.repository.Query;

/**
 * 获取当前学期
 * Created by Allen on 2015/5/7.
 */
public interface FindNowSemesterDAO extends EntityJpaDao<Semester,Long> {
    @Query("from Semester where isNowSemester = 0")
    public Semester getNowSemester();
}
