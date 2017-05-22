package com.zs.service.scheduler;

/**
 * 同步网院接口数据。学习中心，学生，选课
 * Created by Allen on 2015/10/19.
 */
public interface SyncWangYuanApiService {
    public void sync();

    public void allStudent(int year, int term);
}
