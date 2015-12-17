package com.zs.web.controller.finance;

import com.zs.service.finance.studentexpensepay.SetStudentPayService;
import com.zs.web.controller.LoggerController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 重新设置一下一个学习中心下的所有学生的缴费信息
 * 通过合计student_expense_pay表的数据，重新计算student_expense表的值
 * Created by Allen on 2015/12/17.
 */
@Controller
@RequestMapping(value = "/setStudentPayForSpotCode")
public class SetStudentPayForSpotCodeController extends LoggerController {

    @Resource
    private SetStudentPayService setStudentPayService;

    @RequestMapping(value = "set")
    public void set(HttpServletRequest request,
                    @RequestParam("spotCode") String spotCode){
        try {
            setStudentPayService.setStudentPayForSpotCode(spotCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
