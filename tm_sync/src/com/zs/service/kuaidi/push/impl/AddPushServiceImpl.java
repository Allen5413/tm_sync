package com.zs.service.kuaidi.push.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.kuaidi.push.KuaidiPushDAO;
import com.zs.dao.placeorder.placeorder.EditPlaceOrderStateByNuDAO;
import com.zs.dao.placeorder.placeorderlog.PlaceOrderLogDAO;
import com.zs.dao.placeorder.placeorderpackage.EditPlaceOrderPackageSignByNuDAO;
import com.zs.dao.placeorder.placeorderpackage.FindPlaceOrderPackageByNuDAO;
import com.zs.dao.sale.studentbookorder.EditStudentOrderStateByNuDAO;
import com.zs.dao.sale.studentbookorderlog.StudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookorderpackage.EditStudentBookOrderPackageSignByNuDAO;
import com.zs.dao.sale.studentbookorderpackage.FindStudentBookOrderPackageByNuDAO;
import com.zs.domain.kuaidi.KuaidiPush;
import com.zs.domain.placeorder.PlaceOrderPackage;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderPackage;
import com.zs.service.kuaidi.push.AddPushService;
import com.zs.tools.DateTools;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * Created by Allen on 2015/9/30.
 */
@Service("addPushService")
public class AddPushServiceImpl extends EntityServiceImpl<KuaidiPush, KuaidiPushDAO> implements AddPushService{

    @Resource
    private KuaidiPushDAO kuaidiPushDAO;
    @Resource
    private FindStudentBookOrderPackageByNuDAO findStudentBookOrderPackageByNuDAO;
    @Resource
    private FindPlaceOrderPackageByNuDAO findPlaceOrderPackageByNuDAO;
    @Resource
    private EditStudentBookOrderPackageSignByNuDAO editStudentBookOrderPackageSignByNuDAO;
    @Resource
    private EditStudentOrderStateByNuDAO editStudentOrderStateByNuDAO;
    @Resource
    private StudentBookOrderLogDAO studentBookOrderLogDAO;
    @Resource
    private PlaceOrderLogDAO placeOrderLogDAO;
    @Resource
    private EditPlaceOrderPackageSignByNuDAO editPlaceOrderPackageSignByNuDAO;
    @Resource
    private EditPlaceOrderStateByNuDAO editPlaceOrderStateByNuDAO;

    @Override
    public KuaidiPush add(String json)throws Exception{
        KuaidiPush kuaidiPush = null;
        Timestamp operateTime = DateTools.getLongNowTime();
        //把json字符串转成json对象
        JSONObject pushInfo = JSONObject.fromObject(json);
        if(null != pushInfo) {
            if(null != pushInfo.get("lastResult")) {
                JSONObject lastResult = (JSONObject) pushInfo.get("lastResult");
                //得到快递单号
                String nu = null == lastResult.get("nu") ? null : lastResult.get("nu").toString();
                //查询当前快递单号信息是否存在，存在就修改，不存在就新增
                kuaidiPush = kuaidiPushDAO.findByNu(nu);
                if (null != kuaidiPush) {
                    kuaidiPush.setStatus(null == pushInfo.get("status") ? null : pushInfo.get("status").toString());
                    kuaidiPush.setState(null == lastResult.get("state") ? null : Integer.parseInt(lastResult.get("state").toString()));
                    kuaidiPush.setMessage(null == pushInfo.get("message") ? null : pushInfo.get("message").toString());
                    kuaidiPush.setData(null == lastResult.get("data") ? null : lastResult.get("data").toString());
                    kuaidiPush.setOperateTime(operateTime);
                    super.update(kuaidiPush);
                } else {
                    kuaidiPush = new KuaidiPush();
                    kuaidiPush.setCom(null == lastResult.get("com") ? null : lastResult.get("com").toString());
                    kuaidiPush.setNu(nu);
                    kuaidiPush.setStatus(null == pushInfo.get("status") ? null : pushInfo.get("status").toString());
                    kuaidiPush.setState(null == lastResult.get("state") ? null : Integer.parseInt(lastResult.get("state").toString()));
                    kuaidiPush.setMessage(null == pushInfo.get("message") ? null : pushInfo.get("message").toString());
                    kuaidiPush.setData(null == lastResult.get("data") ? null : lastResult.get("data").toString());
                    kuaidiPush.setOperateTime(operateTime);
                    super.save(kuaidiPush);
                }
            }
        }
        return kuaidiPush;
    }

    @Override
    @Transactional
    public void editOrderState(String nu) throws Exception {
        Timestamp operateTime = DateTools.getLongNowTime();
        //查询该订单号的学生订单包
        Long count = findStudentBookOrderPackageByNuDAO.findStudentBookOrderPackageByNu(nu);
        if(null != count && 0 < count) {
            //修改该快递单号下的学生订单包信息
            editStudentBookOrderPackageSignByNuDAO.editStudentBookOrderPackageSignByNu(StudentBookOrderPackage.IS_SIGN_YES, operateTime, nu);
            //修改该快递单号下的学生订单信息
            editStudentOrderStateByNuDAO.editStudentOrderStateByNu(StudentBookOrder.STATE_SIGN, operateTime, nu);
            //增加订单状态日志记录
            studentBookOrderLogDAO.addSignOrderLog(nu);
        }else{
            //查询该订单号的预订单包
            count = findPlaceOrderPackageByNuDAO.findPlaceOrderPackageForNotSign(nu);
            if(null != count && 0 < count) {
                //修改该快递单号下的预订单包信息
                editPlaceOrderPackageSignByNuDAO.editPlaceOrderPackageSignByNu(PlaceOrderPackage.IS_SIGN_YES, operateTime, nu);
                //修改该快递单号下的预订单信息
                editPlaceOrderStateByNuDAO.editStudentOrderStateByNu(TeachMaterialPlaceOrder.STATE_SIGN, operateTime, nu);
                //增加订单状态日志记录
                placeOrderLogDAO.addSignOrderLog(nu);
            }
        }
    }
}
