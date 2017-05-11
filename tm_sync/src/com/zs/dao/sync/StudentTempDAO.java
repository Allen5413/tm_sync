package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.StudentTemp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Allen on 2015/9/17.
 */
public interface StudentTempDAO extends EntityJpaDao<StudentTemp,Long> {

    @Modifying
    @Query("delete from StudentTemp")
    public void delAll()throws Exception;
}
