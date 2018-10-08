package com.zs.dao.sync;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.sync.EduwestUser;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Allen on 2016/3/3.
 */
public interface EduwestUserDAO extends EntityJpaDao<EduwestUser, Long> {
    @Query("from EduwestUser where pin = ?1")
    public EduwestUser findByPin(String pin)throws Exception;
}
