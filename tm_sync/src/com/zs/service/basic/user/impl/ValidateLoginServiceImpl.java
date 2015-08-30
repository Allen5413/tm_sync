package com.zs.service.basic.user.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.user.ValidateLoginDAO;
import com.zs.domain.basic.User;
import com.zs.service.basic.user.ValidateLoginService;
import com.zs.tools.MD5Tools;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 验证用户登录
 * Created by Allen on 2015/4/27.
 */
@Service("validateLoginService")
public class ValidateLoginServiceImpl extends EntityServiceImpl<User, ValidateLoginDAO> implements ValidateLoginService {

    @Resource
    private ValidateLoginDAO validateLoginDAO;

    @Override
    public User validateLogin(String loginName, String pwd) throws Exception {
        return validateLoginDAO.validateLogin(loginName, MD5Tools.MD5(pwd));
    }
}
