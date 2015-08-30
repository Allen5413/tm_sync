package com.zs.service.basic.user;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.basic.User;

/**
 * 验证用户登录
 * Created by Allen on 2015/4/27.
 */
public interface ValidateLoginService extends EntityService<User> {
    /**
     * 验证用户登录
     * @param loginName
     * @param pwd
     * @return
     */
    public User validateLogin(String loginName, String pwd)throws Exception;
}
