package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.OldSelectedCourseTemp2;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Allen on 2016/10/27.
 */
public interface OldSelectedCourseTemp2DAO extends EntityJpaDao<OldSelectedCourseTemp2,Long> {

    @Query("from OldSelectedCourseTemp2 where studentCode = ?1")
    public List<OldSelectedCourseTemp2> find(String studentCode);
}
