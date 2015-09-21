package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.SelectedCourseTemp;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/9/17.
 */
public interface SelectedCourseTempDAO extends EntityJpaDao<SelectedCourseTemp,Long> {
    /**
     * 通过学号查询学生的所有选课信息
     * @param studentCode
     * @return
     * @throws Exception
     */
    @Query("FROM SelectedCourseTemp WHERE studentCode = ?1")
    public List<SelectedCourseTemp> findByStudentCode(String studentCode)throws Exception;
}
