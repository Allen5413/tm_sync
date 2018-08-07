package com.zs.service.sync.student;

import com.feinno.framework.common.service.EntityService;
import com.zs.domain.sync.Student;

/**
 * Created by Allen on 2018/8/6.
 */
public interface SendMsgStudentService extends EntityService<Student> {
    public void send(int year, int quarter, int isNew)throws Exception;
}
