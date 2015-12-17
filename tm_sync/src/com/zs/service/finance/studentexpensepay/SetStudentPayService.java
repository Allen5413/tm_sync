package com.zs.service.finance.studentexpensepay;

import com.feinno.framework.common.service.EntityService;

/**
 * Created by Allen on 2015/12/17.
 */
public interface SetStudentPayService extends EntityService {
    public void setStudentPayForSpotCode(String spotCode)throws Exception;
}
