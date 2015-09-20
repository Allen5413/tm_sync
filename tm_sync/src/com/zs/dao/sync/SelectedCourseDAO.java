package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.SelectedCourse;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2015/5/8.
 */
public interface SelectedCourseDAO extends EntityJpaDao<SelectedCourse, Long> {

    /**
     * 通过学号查询学生的所有选课信息
     * @param studentCode
     * @return
     * @throws Exception
     */
    @Query("FROM SelectedCourse WHERE studentCode = ?1")
    public List<SelectedCourse> findByStudentCode(String studentCode)throws Exception;
}
