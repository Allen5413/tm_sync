package com.zs.service.scheduler.impl;

import com.alibaba.fastjson.JSONObject;
import com.zs.dao.basic.kuaidi.BatchKuaidiDAO;
import com.zs.dao.basic.kuaidi.FindKuaidiByNuDAO;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.placeorder.placeorder.EditPlaceOrderStateByPackageIdDAO;
import com.zs.dao.placeorder.placeorderpackage.BatchPlaceOrderPackageDAO;
import com.zs.dao.placeorder.placeorderpackage.FindPlaceOrderPackageForNotSignDAO;
import com.zs.dao.sale.studentbookorder.EditStudentOrderStateByPackageIdDAO;
import com.zs.dao.sale.studentbookorderpackage.BatchStudentBookPackageOrderDAO;
import com.zs.dao.sale.studentbookorderpackage.FindStudentBookOrderPackageForNotSignDAO;
import com.zs.domain.basic.Kuaidi;
import com.zs.domain.basic.Semester;
import com.zs.domain.placeorder.PlaceOrderPackage;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import com.zs.domain.sale.StudentBookOrderPackage;
import com.zs.service.kuaidi.KuaidiService;
import com.zs.service.kuaidi.KuaidiwoService;
import com.zs.service.kuaidi.bean.KuaidiOrder;
import com.zs.service.kuaidi.bean.KuaidiRecordInfo;
import com.zs.service.scheduler.OrderKuaidiTaskService;
import com.zs.tools.DateTools;
import com.zs.tools.FileTools;
import com.zs.tools.PropertiesTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 向快递100同步订单的快递状态
 * Created by Allen on 2015/8/30.
 */
@Service("orderKuaidiTaskService")
public class OrderKuaidiTaskServiceImpl extends Thread implements OrderKuaidiTaskService {

    @Resource
    private FindStudentBookOrderPackageForNotSignDAO findStudentBookOrderPackageForNotSignDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private FindPlaceOrderPackageForNotSignDAO findPlaceOrderPackageForNotSignDAO;
    @Resource
    private KuaidiService kuaidiService;
    @Resource
    private FindKuaidiByNuDAO findKuaidiByNuDAO;
    @Resource
    private EditStudentOrderStateByPackageIdDAO editStudentOrderStateByPackageIdDAO;
    @Resource
    private EditPlaceOrderStateByPackageIdDAO editPlaceOrderStateByPackageIdDAO;
    @Resource
    private BatchStudentBookPackageOrderDAO batchStudentBookPackageOrderDAO;
    @Resource
    private BatchPlaceOrderPackageDAO batchPlaceOrderPackageDAO;
    @Resource
    private BatchKuaidiDAO batchKuaidiDAO;
    @Resource
    private KuaidiwoService kuaidiwoService;

    //用来记录调用接口的快递单数量
    private int tempNum = 0;

    @Override
    @Transactional
    public synchronized void orderKuaidiState() {
        StringBuilder msg = new StringBuilder(DateTools.getLongNowTime()+": 开始执行快递信息同步\r\n");
        String orderPackageCode = "";
        String kuaidiCode = "";
        try {
            //要修改的学生订单包
            List<StudentBookOrderPackage> editStudentPackageList = new ArrayList<StudentBookOrderPackage>();
            //要修改的预订单包
            List<PlaceOrderPackage> editPlacePackageList = new ArrayList<PlaceOrderPackage>();
            //要新增的快递信息
            List<Kuaidi> addKuaidiList = new ArrayList<Kuaidi>();
            //要修改的快递信息
            List<Kuaidi> editKuaidiList = new ArrayList<Kuaidi>();

            //获取当前学期
            Semester semester = findNowSemesterDAO.getNowSemester();
            //获取快递状态是未签收的学生订单包
            List<StudentBookOrderPackage> studentBookOrderPackageList = findStudentBookOrderPackageForNotSignDAO.findStudentBookOrderPackageForNotSign(semester.getId());
            //获取快递状态是未签收的预订单包
            List<PlaceOrderPackage> placeOrderPackageList = findPlaceOrderPackageForNotSignDAO.findPlaceOrderPackageForNotSign(semester.getId());

            Timestamp operateTime = DateTools.getLongNowTime();
            if (null != studentBookOrderPackageList && 0 < studentBookOrderPackageList.size()) {
                for (StudentBookOrderPackage studentBookOrderPackage : studentBookOrderPackageList) {
                    orderPackageCode = studentBookOrderPackage.getCode();
                    kuaidiCode = studentBookOrderPackage.getLogisticCode();
                    System.out.println("tempNum: "+tempNum);
                    //这里每2分钟调用5个快递单号，防止调用太频繁，锁ip
                    if (tempNum % 5 == 0 && 0 < tempNum) {
                        Thread.sleep(100000l);
                    }
                    String logisticCode = studentBookOrderPackage.getLogisticCode();
                    JSONObject jsonObject = kuaidiwoService.queryForEMSByJson(logisticCode);
                    System.out.println("tempNum:  "+jsonObject.toString());
//                    KuaidiOrder kuaidiOrder = kuaidiService.queryForEMSObject(logisticCode);
//                    if (null != kuaidiOrder && !StringUtils.isEmpty(kuaidiOrder.getNu())) {
//                        /** 快递单当前状态
//                         * 0：在途，即货物处于运输过程中
//                         * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息
//                         * 2：疑难，货物寄送过程出了问题
//                         * 3：签收，收件人已签收
//                         * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收
//                         * 5：派件，即快递正在进行同城派件
//                         * 6：退回，货物正处于退回发件人的途中
//                         */
//                        String state = kuaidiOrder.getState();
//                        if ("2".equals(state)) {
//                            studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_DIFFICULT);
//                            studentBookOrderPackage.setOperator("管理员");
//                            studentBookOrderPackage.setOperateTime(operateTime);
//                            editStudentPackageList.add(studentBookOrderPackage);
//                        }
//                        if ("3".equals(state)) {
//                            studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_YES);
//                            studentBookOrderPackage.setOperator("管理员");
//                            studentBookOrderPackage.setOperateTime(operateTime);
//                            editStudentPackageList.add(studentBookOrderPackage);
//                        }
//                        if ("4".equals(state)) {
//                            studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_RETURNSIGN);
//                            studentBookOrderPackage.setOperator("管理员");
//                            studentBookOrderPackage.setOperateTime(operateTime);
//                            editStudentPackageList.add(studentBookOrderPackage);
//                        }
//                        if ("6".equals(state)) {
//                            studentBookOrderPackage.setIsSign(StudentBookOrderPackage.IS_SIGN_RETURN);
//                            studentBookOrderPackage.setOperator("管理员");
//                            studentBookOrderPackage.setOperateTime(operateTime);
//                            editStudentPackageList.add(studentBookOrderPackage);
//                        }
//
//                        //修改学生发书单包下的订单状态
//                        if ("2".equals(state) || "3".equals(state) || "4".equals(state) || "6".equals(state)) {
//                            editStudentOrderStateByPackageIdDAO.editStudentOrderStateByPackageId(Integer.parseInt(state), "管理员", operateTime, studentBookOrderPackage.getId());
//                        }
//
//                        //查询快递信息是否存在数据库, 如果存在就修改信息，如果不存在就新增记录
//                        Kuaidi kuaidi = findKuaidiByNuDAO.findKuaidiByNu(kuaidiOrder.getNu());
//                        if (null == kuaidi) {
//                            kuaidi = new Kuaidi();
//                            kuaidi.setNu(kuaidiOrder.getNu());
//                        }
//                        kuaidi.setCom(kuaidiOrder.getCom());
//                        kuaidi.setComName(kuaidiOrder.getComName());
//                        kuaidi.setStatus(kuaidiOrder.getStatus());
//                        kuaidi.setConditions(kuaidiOrder.getCondition());
//                        kuaidi.setState(kuaidiOrder.getState());
//                        kuaidi.setMessage(kuaidiOrder.getMessage());
//                        kuaidi.setSyncTime(operateTime);
//                        kuaidi.setRecord(this.assembleRecord(kuaidiOrder.getRecordInfoList()));
//
//                        if (null != kuaidi && kuaidi.getNu().equals(kuaidiOrder.getNu())) {
//                            editKuaidiList.add(kuaidi);
//                        } else {
//                            addKuaidiList.add(kuaidi);
//                        }
//                    }
                    tempNum++;
                }
            }

            if (null != placeOrderPackageList && 0 < placeOrderPackageList.size()) {
                for (PlaceOrderPackage placeOrderPackage : placeOrderPackageList) {
                    orderPackageCode = placeOrderPackage.getCode();
                    if(!StringUtils.isEmpty(placeOrderPackage.getLogisticCode())) {
                        String[] logisticCodes = placeOrderPackage.getLogisticCode().split(",");
                        boolean isSign = true;
                        for(String logisticCode : logisticCodes) {
                            kuaidiCode = logisticCode;
                            //这里每2分钟调用5个快递单号，防止调用太频繁，锁ip
                            System.out.println("tempNum: "+tempNum);
                            if (tempNum % 5 == 0 && 0 < tempNum) {
                                Thread.sleep(100000l);
                            }
                            JSONObject jsonObject = kuaidiwoService.queryForEMSByJson(logisticCode);
                            System.out.println("tempNum:  "+jsonObject.toString());
//                            KuaidiOrder kuaidiOrder = kuaidiService.queryForEMSObject(logisticCode);
//                            if (null != kuaidiOrder && !StringUtils.isEmpty(kuaidiOrder.getNu())) {
//                                /** 快递单当前状态
//                                 * 0：在途，即货物处于运输过程中
//                                 * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息
//                                 * 2：疑难，货物寄送过程出了问题
//                                 * 3：签收，收件人已签收
//                                 * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收
//                                 * 5：派件，即快递正在进行同城派件
//                                 * 6：退回，货物正处于退回发件人的途中
//                                 */
//                                if (!"3".equals(kuaidiOrder.getState())) {
//                                    isSign = false;
//                                }
//
//                                //查询快递信息是否存在数据库, 如果存在就修改信息，如果不存在就新增记录
//                                Kuaidi kuaidi = findKuaidiByNuDAO.findKuaidiByNu(kuaidiOrder.getNu());
//                                if (null == kuaidi) {
//                                    kuaidi = new Kuaidi();
//                                    kuaidi.setNu(kuaidiOrder.getNu());
//                                }
//                                kuaidi.setCom(kuaidiOrder.getCom());
//                                kuaidi.setComName(kuaidiOrder.getComName());
//                                kuaidi.setStatus(kuaidiOrder.getStatus());
//                                kuaidi.setConditions(kuaidiOrder.getCondition());
//                                kuaidi.setState(kuaidiOrder.getState());
//                                kuaidi.setMessage(kuaidiOrder.getMessage());
//                                kuaidi.setSyncTime(operateTime);
//                                kuaidi.setRecord(this.assembleRecord(kuaidiOrder.getRecordInfoList()));
//
//                                if (null != kuaidi && kuaidi.getNu().equals(kuaidiOrder.getNu())) {
//                                    editKuaidiList.add(kuaidi);
//                                } else {
//                                    addKuaidiList.add(kuaidi);
//                                }
//                            }else{
//                                isSign = false;
//                            }
                        }
                        tempNum++;
//                        if(isSign){
//                            //修改预订单包的状态
//                            placeOrderPackage.setIsSign(PlaceOrderPackage.IS_SIGN_YES);
//                            placeOrderPackage.setOperator("管理员");
//                            placeOrderPackage.setOperateTime(operateTime);
//                            editPlacePackageList.add(placeOrderPackage);
//                            //修改预订单包下的订单状态
//                            editPlaceOrderStateByPackageIdDAO.editPlaceOrderStateByPackageId(TeachMaterialPlaceOrder.STATE_SIGN, "管理员", operateTime, placeOrderPackage.getId());
//                        }
                    }
                }
            }

            //修改学生包的标记状态
            if (null != editStudentPackageList && 0 < editStudentPackageList.size()) {
                batchStudentBookPackageOrderDAO.batchEdit(editStudentPackageList, 1000);
            }
            //修改预订单包的标记状态
            if (null != editPlacePackageList && 0 < editPlacePackageList.size()) {
                batchPlaceOrderPackageDAO.batchEdit(editPlacePackageList, 1000);
            }

            //新增快递信息
            if (null != addKuaidiList && 0 < addKuaidiList.size()) {
                batchKuaidiDAO.batchAdd(addKuaidiList, 1000);
            }
            //修改快递信息
            if (null != editKuaidiList && 0 < editKuaidiList.size()) {
                batchKuaidiDAO.batchAdd(editKuaidiList, 1000);
            }
        }catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.append("包裹编号："+orderPackageCode+" \r\n");
            msg.append("快递单号："+kuaidiCode+"\r\n");
            msg.append("异常信息："+sw.toString()+ "\r\n");
        }finally {
            msg.append("执行了"+tempNum+"条数据\r\n");
            msg.append(DateTools.getLongNowTime()+": 快递信息同步结束");

            PropertiesTools propertiesTools =  new PropertiesTools("resource/commons.properties");
            String rootPath = propertiesTools.getProperty("sync.log.file.path");
            String filePath = propertiesTools.getProperty("sync.kuaidi.log.file.path");
            String nowDate = DateTools.transferLongToDate("yyyy-MM-dd", System.currentTimeMillis());
            FileTools.createFile(rootPath + filePath,   nowDate + ".txt");
            FileTools.writeTxtFile(msg.toString(), rootPath + filePath + nowDate + ".txt");
        }
    }

    /**
     * 把快递信息组装成json字符串格式
     * @param kuaidiRecordInfoList
     * @return
     */
    protected String assembleRecord(List<KuaidiRecordInfo> kuaidiRecordInfoList){
        StringBuilder result = new StringBuilder("{info:{");
        if(null != kuaidiRecordInfoList && 0 < kuaidiRecordInfoList.size()){
            for (KuaidiRecordInfo kuaidiRecordInfo : kuaidiRecordInfoList){
                String time = DateTools.getFormattedString(kuaidiRecordInfo.getTime(), DateTools.longDatePattern);
                String content = kuaidiRecordInfo.getContent();
                result.append("{time:"+time+", content:"+content+"}");
            }
        }
        result.append("}}");
        return result.toString();
    }
}
