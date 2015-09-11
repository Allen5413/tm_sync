package com.zs.service.scheduler;

/**
 * 检查学生的选课发生变化没有，如果发生了变化，未确认的学生订单也要变
 * Created by Allen on 2015/9/11.
 */
public interface CheckStudentSelectedTaskService {
    public void checkStudentSelectedTask();
}
