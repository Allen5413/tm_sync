package com.zs.service.sync.student.impl;

import com.alibaba.fastjson.JSONObject;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.sync.Student;
import com.zs.service.sync.student.SendMsgStudentService;
import com.zs.tools.AttopUtil;
import com.zs.tools.HttpRequestTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Allen on 2018/8/6.
 */
@Service("sendMsgStudentService")
public class SendMsgStudentServiceImpl extends EntityServiceImpl<Student, FindStudentByCodeDAO> implements SendMsgStudentService {

    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;

    @Override
    public void send(int year, int quarter, int isNew)throws Exception{
        List<Student> studentList = findStudentByCodeDAO.getStudentByIsSendMsgAndYearAndQuarter(Student.ISSENDMSG_NOT, year, quarter);
        if(null != studentList && 0 < studentList.size()){
            int i=0, j=0;
            for(Student student : studentList){
                String mobile = student.getMobile();
                if(StringUtils.isEmpty(mobile) || 11 != mobile.length()){
                    mobile = student.getHomeTel();
                }
                if(!StringUtils.isEmpty(mobile) && 11 == mobile.length()){
                    j++;
                    //发短信
                    String name = student.getName();
                    net.sf.json.JSONObject json = HttpRequestTools.getStudentFinance(student.getCode());
                    if(json.get("state").toString().equals("0") && Double.parseDouble(json.get("price").toString()) > 0) {
                        i++;
                        String msg = URLEncoder.encode("name=" + name + "&expense=" + json.get("price"), "UTF-8");
                        int result = AttopUtil.sendMsg(mobile, isNew, msg);
                        if(result == 1){
                            student.setIsSendMsg(Student.ISSENDMSG_YES);
                            findStudentByCodeDAO.update(student);
                        }
                    }
                }
            }
            System.out.println("i ------  "+i);
            System.out.println("j ------  "+j);
        }
    }
}
