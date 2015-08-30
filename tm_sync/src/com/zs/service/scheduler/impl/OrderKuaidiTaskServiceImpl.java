package com.zs.service.scheduler.impl;

import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.placeorder.FindPlaceOrderPackageForNotSignDAO;
import com.zs.dao.sale.studentbookorderpackage.FindStudentBookOrderPackageForNotSignDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.placeorder.PlaceOrderPackage;
import com.zs.domain.sale.StudentBookOrderPackage;
import com.zs.service.kuaidi.KuaidiService;
import com.zs.service.kuaidi.bean.KuaidiOrder;
import com.zs.service.scheduler.OrderKuaidiTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 向快递100同步订单的快递状态
 * Created by Allen on 2015/8/30.
 */
@Service("orderKuaidiTaskService")
public class OrderKuaidiTaskServiceImpl implements OrderKuaidiTaskService {

    @Resource
    private FindStudentBookOrderPackageForNotSignDAO findStudentBookOrderPackageForNotSignDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private FindPlaceOrderPackageForNotSignDAO findPlaceOrderPackageForNotSignDAO;
    @Resource
    private KuaidiService kuaidiService;

    @Override
    public void orderKuaidiState() {
        //要修改的学生订单包
        List<StudentBookOrderPackage> editStudentPackageList = new ArrayList<StudentBookOrderPackage>();
        //要修改的预订单包
        List<PlaceOrderPackage> editPlacePackageList = new ArrayList<PlaceOrderPackage>();

        //获取当前学期
        Semester semester = findNowSemesterDAO.getNowSemester();
        //获取快递状态是未签收的学生订单包
        List<StudentBookOrderPackage> studentBookOrderPackageList = findStudentBookOrderPackageForNotSignDAO.findStudentBookOrderPackageForNotSign(semester.getId());
        //获取快递状态是未签收的预订单包
        List<PlaceOrderPackage> placeOrderPackageList = findPlaceOrderPackageForNotSignDAO.findPlaceOrderPackageForNotSign(semester.getId());

        if(null != studentBookOrderPackageList && 0 < studentBookOrderPackageList.size()){
            for(StudentBookOrderPackage studentBookOrderPackage : studentBookOrderPackageList){
                String logisticCode = studentBookOrderPackage.getLogisticCode();
                KuaidiOrder kuaidiOrder = kuaidiService.queryForEMSObject(logisticCode);
                if(null != kuaidiOrder){
                    /** 快递单当前状态
                     * 0：在途，即货物处于运输过程中
                     * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息
                     * 2：疑难，货物寄送过程出了问题
                     * 3：签收，收件人已签收
                     * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收
                     * 5：派件，即快递正在进行同城派件
                     * 6：退回，货物正处于退回发件人的途中
                     */
                    if("2".equals(kuaidiOrder.getState())){
                        studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_DIFFICULT);
                        editStudentPackageList.add(studentBookOrderPackage);
                    }
                    if("3".equals(kuaidiOrder.getState())){
                        studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_YES);
                        editStudentPackageList.add(studentBookOrderPackage);
                    }
                    if("4".equals(kuaidiOrder.getState())){
                        studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_RETURNSIGN);
                        editStudentPackageList.add(studentBookOrderPackage);
                    }
                    if("6".equals(kuaidiOrder.getState())){
                        studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_RETURN);
                        editStudentPackageList.add(studentBookOrderPackage);
                    }
                }
            }
        }

        if(null != placeOrderPackageList && 0 < placeOrderPackageList.size()){
            for(PlaceOrderPackage placeOrderPackage : placeOrderPackageList){
                String logisticCode = placeOrderPackage.getLogisticCode();
                KuaidiOrder kuaidiOrder = kuaidiService.queryForEMSObject(logisticCode);
                if(null != kuaidiOrder){
                    /** 快递单当前状态
                     * 0：在途，即货物处于运输过程中
                     * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息
                     * 2：疑难，货物寄送过程出了问题
                     * 3：签收，收件人已签收
                     * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收
                     * 5：派件，即快递正在进行同城派件
                     * 6：退回，货物正处于退回发件人的途中
                     */
                    if("2".equals(kuaidiOrder.getState())){
                        placeOrderPackage.setIsSign(PlaceOrderPackage.IS_SIGN_DIFFICULT);
                        editPlacePackageList.add(placeOrderPackage);
                    }
                    if("3".equals(kuaidiOrder.getState())){
                        placeOrderPackage.setIsSign(PlaceOrderPackage.IS_SIGN_YES);
                        editPlacePackageList.add(placeOrderPackage);
                    }
                    if("4".equals(kuaidiOrder.getState())){
                        placeOrderPackage.setIsSign(PlaceOrderPackage.IS_SIGN_RETURNSIGN);
                        editPlacePackageList.add(placeOrderPackage);
                    }
                    if("6".equals(kuaidiOrder.getState())){
                        placeOrderPackage.setIsSign(PlaceOrderPackage.IS_SIGN_RETURN);
                        editPlacePackageList.add(placeOrderPackage);
                    }
                }
            }
        }
    }
}
