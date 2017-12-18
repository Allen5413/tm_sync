package com.zs.service.scheduler;

/**
 * Created by Allen on 2017/12/18.
 */
public interface SyncOldSelectedCourseService {
    public void sync(int stuYear, int stuTerm, String courseYearTerms)throws Exception;
}
