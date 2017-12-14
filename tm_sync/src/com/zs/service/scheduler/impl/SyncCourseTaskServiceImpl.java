package com.zs.service.scheduler.impl;

import com.zs.dao.api.WangYuanApiDAO;
import com.zs.dao.sync.CourseDAO;
import com.zs.domain.api.WangYuanApi;
import com.zs.domain.sync.Course;
import com.zs.service.scheduler.SyncCourseTaskService;
import com.zs.tools.HttpRequestTools;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Allen on 2017/12/14.
 */
@Service("syncCourseTaskService")
public class SyncCourseTaskServiceImpl implements SyncCourseTaskService {

    @Resource
    private CourseDAO courseDAO;
    @Resource
    private WangYuanApiDAO wangYuanApiDAO;

    @Override
    @Transactional
    public void sync()throws Exception{
        //首先删除掉现有课程表
        courseDAO.deleteAll();
        //同步网院数据
        JSONObject courseJSON = HttpRequestTools.getCourse();

        String reqUrl = null == courseJSON ? null : courseJSON.get("reqUrl").toString();
        courseJSON.remove("reqUrl");

        String jsonStr = courseJSON.toString();
        WangYuanApi wangYuanApi = new WangYuanApi();
        wangYuanApi.setReqData(reqUrl);
        wangYuanApi.setResultData(jsonStr);
        wangYuanApiDAO.save(wangYuanApi);

        if((Boolean)courseJSON.get("result")){
            List list = (List) courseJSON.get("courseList");
            if(null != list && 0 < list.size()){
                for(int i=0; i<list.size(); i++){
                    JSONObject json = (JSONObject)list.get(i);
                    Course course = new Course();
                    course.setName(null == json.get("name") ? "" : json.get("name").toString());
                    course.setCode(null == json.get("code") ? "" : json.get("code").toString());
                    courseDAO.save(course);
                }
            }
        }

    }
}
