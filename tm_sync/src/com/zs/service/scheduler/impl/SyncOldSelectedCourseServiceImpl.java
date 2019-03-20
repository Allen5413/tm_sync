package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.sync.OldSelectedCourseTempDAO;
import com.zs.domain.sync.OldSelectedCourseTemp;
import com.zs.service.scheduler.SyncOldSelectedCourseService;
import com.zs.tools.HttpRequestTools;
import com.zs.tools.StringTools;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Allen on 2017/12/18.
 */
@Service("syncOldSelectedCourseService")
public class SyncOldSelectedCourseServiceImpl extends EntityServiceImpl<OldSelectedCourseTemp, OldSelectedCourseTempDAO> implements SyncOldSelectedCourseService {

    @Override
    @Transactional
    public void sync(int stuYear, int stuTerm, String courseYearTerms) throws Exception {
        if (!StringUtils.isEmpty(courseYearTerms)) {
            String[] courseYearTermArray = courseYearTerms.split(",");
            for(String courseYearTerm :courseYearTermArray) {
                String courseYear = courseYearTerm.split("_")[0];
                String courseTerm = courseYearTerm.split("_")[1];
                JSONObject json = HttpRequestTools.getSelectCourse(stuYear, stuTerm, Integer.parseInt(courseYear), Integer.parseInt(courseTerm));
                if (!(Boolean) json.get("result")) {
                    throw new BusinessException("学生选课信息同步失败：" + json.get("errorMsg"));
                }
                List list = (List) json.get("scList");
                if (null != list && 0 < list.size()) {
                    for (int i = 0; i < list.size(); i++) {
                        JSONObject json2 = (JSONObject) list.get(i);
                        OldSelectedCourseTemp oldSelectedCourseTemp = new OldSelectedCourseTemp();
                        oldSelectedCourseTemp.setStudentCode(null == json2.get("stuCode") ? "" : json2.get("stuCode").toString());
                        oldSelectedCourseTemp.setCourseCode(null == json2.get("courseCode") ? "" : json2.get("courseCode").toString());
                        oldSelectedCourseTemp.setScore(0);
                        super.save(oldSelectedCourseTemp);
                    }
                }
            }
            System.out.println("oldselectedcourse sync end........................................................");
        }
    }
}
