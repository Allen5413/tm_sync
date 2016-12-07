package com.zs.web.controller.sale.studentbookorder;

import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.domain.basic.Semester;
import com.zs.service.sale.studentbookorder.EditOrderForSendBySemesterIdService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2016/12/7.
 */
@Controller
@RequestMapping(value = "/editOrderForSendBySemesterId")
public class EditOrderForSendBySemesterIdController extends LoggerController {
    @Resource
    private EditOrderForSendBySemesterIdService editOrderForSendBySemesterIdService;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;

    @RequestMapping(value = "editor")
    public void editor(HttpServletRequest request, @RequestParam("id")long id){
        try {
            Semester semester = findNowSemesterDAO.getNowSemester();
            //editOrderForSendBySemesterIdService.edit(id);
            //如果不是当前学期，还需要把添加成当前学期的student_expense表的数据返回到传进来的学期
            if(id != semester.getId()){
                editOrderForSendBySemesterIdService.edit2(id, semester.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
