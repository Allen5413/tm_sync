package com.zs.dao.basic.teachmaterialcourse;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.TeachMaterialCourse;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 通过课程编号查询关联的教材
 * Created by Allen on 2015/5/9.
 */
public interface FindTeachMaterialCourseByCourseCodeDAO extends EntityJpaDao<TeachMaterialCourse, Long> {
    @Query("from TeachMaterialCourse where courseCode = ?1")
    public List<TeachMaterialCourse> getTeachMaterialCourseByCourseCode(String courseCode);
}
