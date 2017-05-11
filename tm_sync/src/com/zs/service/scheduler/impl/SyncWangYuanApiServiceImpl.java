package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.zs.dao.api.WangYuanApiDAO;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.sync.*;
import com.zs.domain.api.WangYuanApi;
import com.zs.domain.basic.Semester;
import com.zs.domain.sync.SelectedCourseTemp;
import com.zs.domain.sync.SpotTemp;
import com.zs.domain.sync.StudentTemp;
import com.zs.service.scheduler.SyncSelectedCourseTaskService;
import com.zs.service.scheduler.SyncSpotTaskService;
import com.zs.service.scheduler.SyncStudentTaskService;
import com.zs.service.scheduler.SyncWangYuanApiService;
import com.zs.tools.HttpRequestTools;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过网院接口同步数据，学习中心，学生，选课
 * Created by Allen on 2017/5/4.
 */
@Service("syncWangYuanApiService")
public class SyncWangYuanApiServiceImpl implements SyncWangYuanApiService {

    @Resource
    private WangYuanApiDAO wangYuanApiDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private SpotTempDAO spotTempDAO;
    @Resource
    private StudentTempDAO studentTempDAO;
    @Resource
    private SelectedCourseTempDAO selectedCourseTempDAO;
    @Resource
    private SyncSpotTaskService syncSpotTaskService;
    @Resource
    private SyncStudentTaskService syncStudentTaskService;
    @Resource
    private SyncSelectedCourseTaskService syncSelectedCourseTaskService;

    @Override
    public void sync() {
        try {
            List<StudentTemp> studentTempList = new ArrayList<StudentTemp>();
            List<SelectedCourseTemp> selectedCourseTempList = new ArrayList<SelectedCourseTemp>();

            int year = 0, year2 = 0, year3 = 0, year4 = 0;
            int quarter = 0, quarter2 = 0, quarter3 = 0, quarter4 = 0;

            //查询当前学期
            Semester nowSemester = findNowSemesterDAO.getNowSemester();
            year = nowSemester.getYear();
            quarter = nowSemester.getQuarter();
            //计算前面3个学期，分学期来获取学生信息
            if (0 == quarter) {
                year2 = year - 1;
                quarter2 = 1;
            } else {
                year2 = year;
                quarter2 = 0;
            }
            if (0 == quarter2) {
                year3 = year2 - 1;
                quarter3 = 1;
            } else {
                year3 = year2;
                quarter3 = 0;
            }
            if (0 == quarter3) {
                year4 = year3 - 1;
                quarter4 = 1;
            } else {
                year4 = year3;
                quarter4 = 0;
            }

            //同步学习中心
            this.syncSpot();
            //同步学生
            studentTempList = this.syncStudent(year, quarter, studentTempList);
            studentTempList = this.syncStudent(year2, quarter2, studentTempList);
            studentTempList = this.syncStudent(year3, quarter3, studentTempList);
            studentTempList = this.syncStudent(year4, quarter4, studentTempList);
            if (0 < studentTempList.size()) {
                //先清空临时表
                studentTempDAO.deleteAll();
                if(null != studentTempList && 0 < studentTempList.size()){
                    for(StudentTemp studentTemp : studentTempList){
                        studentTempDAO.save(studentTemp);
                    }
                }
                //batchStudentTempDAO.batchAdd(studentTempList, 1000);
            }
            //同步选课信息
            selectedCourseTempList = this.syncSelectCourse(year, quarter, year, quarter, selectedCourseTempList);
            if (0 < selectedCourseTempList.size()) {
                selectedCourseTempDAO.deleteAll();
                if(null != selectedCourseTempList && 0 < selectedCourseTempList.size()){
                    for(SelectedCourseTemp selectedCourseTemp : selectedCourseTempList){
                        selectedCourseTempDAO.save(selectedCourseTemp);
                    }
                }
                //batchSelectedCourseTempDAO.batchAdd(selectedCourseTempList, 1000);
            }

            //执行同步；顺序 学习中心 - 学生 -选课
            syncSpotTaskService.syncSpot();
            syncStudentTaskService.syncStudent();
            syncSelectedCourseTaskService.syncSelectedCourse();
            syncSelectedCourseTaskService.delChangeSelectedCourse();
            syncSelectedCourseTaskService.delHSTmBySermesterId();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void syncSpot()throws Exception{
        List<SpotTemp> spotTempList = new ArrayList<SpotTemp>();
        JSONObject spotJSON = HttpRequestTools.getSpot();
        this.addWangYuanApi(spotJSON);
        if(!(Boolean)spotJSON.get("result")){
            throw new BusinessException("学习中心同步失败："+spotJSON.get("errorMsg"));
        }
        List list = (List) spotJSON.get("scList");
        if(null != list && 0 < list.size()){
            for(int i=0; i<list.size(); i++){
                JSONObject json = (JSONObject)list.get(i);
                String spotCode =json.get("code").toString();
                SpotTemp spotTemp = new SpotTemp();
                if(spotCode.length() == 2){
                    spotTemp.setCode("0"+spotCode);
                }else{
                    spotTemp.setCode(spotCode);
                }
                spotTemp.setName(json.get("name").toString());
                spotTemp.setAdminName(null == json.get("leaderName") ? "" : "null".equals(json.get("leaderName").toString()) ? "" : json.get("leaderName").toString());
                spotTemp.setAddress(null == json.get("address") ? "" : "null".equals(json.get("address").toString()) ? "" : json.get("address").toString());
                spotTemp.setPostalCode(null == json.get("zipCode") ? "" : "null".equals(json.get("zipCode").toString()) ? "" : json.get("zipCode").toString());
                spotTemp.setPhone(null == json.get("leaderMobile") ? "" : "null".equals(json.get("leaderMobile").toString()) ? "" : json.get("leaderMobile").toString());
                spotTemp.setTel(null == json.get("leaderTel") ? "" : "null".equals(json.get("leaderTel").toString()) ? "" : json.get("leaderTel").toString());
                spotTempList.add(spotTemp);
            }
        }

        //添加接口过来的数据
        if (0 < spotTempList.size()) {
            //先清空临时表
            spotTempDAO.deleteAll();
            for (SpotTemp spotTemp : spotTempList) {
                spotTempDAO.save(spotTemp);
            }
        }
    }

    private List<StudentTemp> syncStudent(int year, int term, List<StudentTemp> studentTempList)throws Exception{
        JSONObject studentJSON = HttpRequestTools.getStudent(year, 0 == term ? 1 : 2);
        this.addWangYuanApi(studentJSON);
        if(!(Boolean)studentJSON.get("result")){
            throw new BusinessException("学生信息同步失败："+studentJSON.get("errorMsg"));
        }
        List list = (List) studentJSON.get("stuList");
        if(null != list && 0 < list.size()){
            for(int i=0; i<list.size(); i++){
                JSONObject json = (JSONObject)list.get(i);
                String spotCode = json.get("studyCenterCode").toString();
                StudentTemp studentTemp = new StudentTemp();
                if(2 == spotCode.length()){
                    studentTemp.setSpotCode("0" + spotCode);
                }else{
                    studentTemp.setSpotCode(spotCode);
                }
                studentTemp.setCode(null == json.get("code") ? "" : "null".equals(json.get("code").toString()) ? "" : json.get("code").toString());
                studentTemp.setName(null == json.get("name") ? "" : "null".equals(json.get("name").toString()) ? "" : json.get("name").toString());
                studentTemp.setSex(null == json.get("sex") ? null : "null".equals(json.get("sex").toString()) ? null : Integer.parseInt(json.get("sex").toString()));
                studentTemp.setIdcardType(null == json.get("idCardType") ? null : "null".equals(json.get("idCardType").toString()) ? null : Integer.parseInt(json.get("idCardType").toString()));
                studentTemp.setIdcardNo(null == json.get("idCardNo") ? "" : "null".equals(json.get("idCardNo").toString()) ? "" : json.get("idCardNo").toString());
                studentTemp.setPostalCode(null == json.get("zipCode") ? "" : "null".equals(json.get("zipCode").toString()) ? "" : json.get("zipCode").toString());
                studentTemp.setAddress(null == json.get("address") ? "" : "null".equals(json.get("address").toString()) ? "" : json.get("address").toString());
                studentTemp.setMobile(null == json.get("mobile") ? "" : "null".equals(json.get("mobile").toString()) ? "" : json.get("mobile").toString());
                studentTemp.setEmail(null == json.get("email") ? "" : "null".equals(json.get("email").toString()) ? "" : json.get("email").toString());
                studentTemp.setSpecCode(null == json.get("majorCode") ? "" : "null".equals(json.get("majorCode").toString()) ? "" : json.get("majorCode").toString());
                studentTemp.setLevelCode(null == json.get("levelCode") ? "" : "null".equals(json.get("levelCode").toString()) ? "" : json.get("levelCode").toString());
                studentTemp.setType(null == json.get("studentType") ? "" : "null".equals(json.get("studentType").toString()) ? "" : json.get("studentType").toString());
                studentTemp.setState(null == json.get("studyStatus") ? null : "null".equals(json.get("studyStatus").toString()) ? null : Integer.parseInt(json.get("studyStatus").toString()));
                studentTemp.setStudyEnterYear(null == json.get("year") ? null : "null".equals(json.get("year").toString()) ? null : Integer.parseInt(json.get("year").toString()));
                studentTemp.setStudyQuarter(null == json.get("term") ? null : "null".equals(json.get("term").toString()) ? null : Integer.parseInt(json.get("term").toString()));
                studentTempList.add(studentTemp);
            }
        }
        return studentTempList;
    }

    private List<SelectedCourseTemp> syncSelectCourse(int year, int trem, int courseYear, int courseTrem, List<SelectedCourseTemp> selectedCourseTempList)throws Exception{
        JSONObject selectedCourseJSON = HttpRequestTools.getSelectCourse(year, 0 == trem ? 1 : 2, courseYear, 0 == courseTrem ? 1 : 2);
        this.addWangYuanApi(selectedCourseJSON);
        if(!(Boolean)selectedCourseJSON.get("result")){
            throw new BusinessException("学生选课信息同步失败："+selectedCourseJSON.get("errorMsg"));
        }
        List list = (List) selectedCourseJSON.get("scList");
        if(null != list && 0 < list.size()){
            for(int i=0; i<list.size(); i++){
                JSONObject json = (JSONObject)list.get(i);
                SelectedCourseTemp selectedCourseTemp = new SelectedCourseTemp();
                selectedCourseTemp.setStudentCode(null == json.get("stuCode") ? "" : json.get("stuCode").toString());
                selectedCourseTemp.setCourseCode(null == json.get("courseCode") ? "" : json.get("courseCode").toString());
                selectedCourseTempList.add(selectedCourseTemp);
            }
        }
        return selectedCourseTempList;
    }

    private void addWangYuanApi(JSONObject json)throws Exception{
        String reqUrl = null == json ? null : json.get("reqUrl").toString();
        json.remove("reqUrl");

        String jsonStr = json.toString();
        WangYuanApi wangYuanApi = new WangYuanApi();
        wangYuanApi.setReqData(reqUrl);
        wangYuanApi.setResultData(jsonStr);
        wangYuanApiDAO.save(wangYuanApi);
//        int num = jsonStr.length() / 2000000000 + 1;
//        for(int i=0; i<num; i++){
//            String resultData = "";
//            if(i * 2000000000 + 2000000000 > jsonStr.length()) {
//                resultData = jsonStr.substring(i * 2000000000, jsonStr.length());
//            }else{
//                resultData = jsonStr.substring(i * 2000000000, i * 2000000000 + 2000000000);
//            }
//            WangYuanApi wangYuanApi = new WangYuanApi();
//            wangYuanApi.setReqData(reqUrl);
//            wangYuanApi.setResultData(resultData);
//            wangYuanApiDAO.save(wangYuanApi);
//        }
    }
}
