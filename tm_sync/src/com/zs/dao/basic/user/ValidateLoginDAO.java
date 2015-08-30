package com.zs.dao.basic.user;

import com.feinno.framework.common.dao.jpa.EntityJpaDao;
import com.zs.domain.basic.User;
import org.springframework.data.jpa.repository.Query;

/**
 * 验证用户登录
 * Created by Allen on 2015/4/27.
 */
public interface ValidateLoginDAO extends EntityJpaDao<User,Long> {

    @Query("from User where loginName = ?1 and password = ?2")
    public User validateLogin(String loginName, String pwd);
}
