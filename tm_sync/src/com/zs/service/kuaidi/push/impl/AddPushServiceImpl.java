package com.zs.service.kuaidi.push.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.kuaidi.push.KuaidiPushDAO;
import com.zs.dao.placeorder.placeorder.EditPlaceOrderStateByNuDAO;
import com.zs.dao.placeorder.placeorderlog.PlaceOrderLogDAO;
import com.zs.dao.placeorder.placeorderpackage.EditPlaceOrderPackageSignByNuDAO;
import com.zs.dao.placeorder.placeorderpackage.FindPlaceOrderPackageByNuDAO;
import com.zs.dao.sale.onceorder.EditOnceOrderStateByNuDAO;
import com.zs.dao.sale.onceorder.FindOnceOrderByPackageIdDAO;
import com.zs.dao.sale.onceorderlog.OnceOrderLogDAO;
import com.zs.dao.sale.studentbookorder.EditStudentOrderStateByNuDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderByPackageIdDAO;
import com.zs.dao.sale.studentbookorderlog.StudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookorderpackage.EditStudentBookOrderPackageSignByNuDAO;
import com.zs.dao.sale.studentbookorderpackage.FindStudentBookOrderPackageByNuDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.kuaidi.KuaidiPush;
import com.zs.domain.placeorder.PlaceOrderPackage;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import com.zs.domain.sale.StudentBookOnceOrder;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderPackage;
import com.zs.domain.sync.Student;
import com.zs.service.kuaidi.push.AddPushService;
import com.zs.tools.DateTools;
import com.zs.weixin.mp.api.WxMpInMemoryConfigStorage;
import com.zs.weixin.mp.api.WxMpService;
import com.zs.weixin.mp.api.WxMpServiceImpl;
import com.zs.weixin.mp.bean.WxMpTemplateData;
import com.zs.weixin.mp.bean.WxMpTemplateMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private OnceOrderLogDAO onceOrderLogDAO;
    @Resource
    private EditOnceOrderStateByNuDAO editOnceOrderStateByNuDAO;
    @Resource
    private FindOnceOrderByPackageIdDAO findOnceOrderByPackageIdDAO;
    @Resource
    private FindStudentBookOrderByPackageIdDAO findStudentBookOrderByPackageIdDAO;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;

    @Override
    public Map<String, Object> add(String json)throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        KuaidiPush kuaidiPush = null;
        String sendNewData = "";
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
                    String oldNewData = "", newNewData = "";
                    if(null != kuaidiPush.getData()) {
                        JSONArray kuaidiJSON = JSONArray.fromObject(kuaidiPush.getData());
                        if (null != kuaidiJSON && 0 < kuaidiJSON.size()) {
                            oldNewData = kuaidiJSON.get(0).toString();
                        }
                    }
                    if(null != lastResult.get("data")){
                        JSONArray kuaidiJSON = JSONArray.fromObject(lastResult.get("data"));
                        if (null != kuaidiJSON && 0 < kuaidiJSON.size()) {
                            newNewData = kuaidiJSON.get(0).toString();
                        }
                    }
                    kuaidiPush.setStatus(null == pushInfo.get("status") ? null : pushInfo.get("status").toString());
                    kuaidiPush.setState(null == lastResult.get("state") ? null : Integer.parseInt(lastResult.get("state").toString()));
                    kuaidiPush.setMessage(null == pushInfo.get("message") ? null : pushInfo.get("message").toString());
                    kuaidiPush.setData(null == lastResult.get("data") ? null : lastResult.get("data").toString());
                    kuaidiPush.setOperateTime(operateTime);
                    super.update(kuaidiPush);

                    if(!StringUtils.isEmpty(newNewData) && !newNewData.equals(oldNewData)){
                        sendNewData = JSONObject.fromObject(newNewData).get("context").toString();
                    }
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

                    JSONArray kuaidiJSON = JSONArray.fromObject(kuaidiPush.getData());
                    if(null != kuaidiJSON && 0 < kuaidiJSON.size()){
                        sendNewData = ((JSONObject)kuaidiJSON.get(0)).get("context").toString();
                    }
                }
            }
        }
        map.put("kuaidiPush", kuaidiPush);
        map.put("sendNewData", sendNewData);
        return map;
    }

    @Override
    @Transactional
    public void editOrderState(String nu) throws Exception {
        Timestamp operateTime = DateTools.getLongNowTime();
        //查询该快递号的学生订单包
        Long count = findStudentBookOrderPackageByNuDAO.findStudentBookOrderPackageByNu(nu);
        if(null != count && 0 < count) {
            //修改该快递单号下的学生订单包信息
            editStudentBookOrderPackageSignByNuDAO.editStudentBookOrderPackageSignByNu(StudentBookOrderPackage.IS_SIGN_YES, operateTime, nu);
            //修改该快递单号下的学生订单信息
            editStudentOrderStateByNuDAO.editStudentOrderStateByNu(StudentBookOrder.STATE_SIGN, operateTime, nu);
            //增加订单状态日志记录
            studentBookOrderLogDAO.addSignOrderLog(nu);
            //修改该快递单号下的一次性订单信息
            editOnceOrderStateByNuDAO.editor(StudentBookOnceOrder.STATE_SIGN, operateTime, nu);
            //增加一次性订单状态日志记录
            onceOrderLogDAO.addSignOrderLog(nu);
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

    @Override
    public void sendOrderForWX(String nu, String newData)throws Exception {

        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        WxMpService wxMpService;
        /**
         * 测试账号
         config.setAppId("wx9670fc1f7d7fc941"); // 设置微信公众号的appid
         config.setSecret("2c4698cb1c075da218691476f0e5f482"); // 设置微信公众号的app corpSecret
         config.setOauth2redirectUri("http://allen.ittun.com/wxSearch/openPay.htm");*/

        /**
         * 正式账号*/
        config.setAppId("wx79ba7069388a101a"); // 设置微信公众号的appid
        config.setSecret("1bc0d069914b1f904168fe57c0e65102"); // 设置微信公众号的app corpSecret
        config.setOauth2redirectUri("http://xiwang.attop.com/wxPay/openPay.htm");

        config.setToken("XIWANG_TOKEN"); // 设置微信公众号的token
        config.setAesKey("XIWANG_KEY"); // 设置微信公众号的EncodingAESKey
        wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);

        //查询该快递下的订单包信息
        List<StudentBookOrderPackage> studentBookOrderPackageList = findStudentBookOrderPackageByNuDAO.find("%"+nu+"%");
        if(null != studentBookOrderPackageList && 0 < studentBookOrderPackageList.size()){
            for (StudentBookOrderPackage studentBookOrderPackage : studentBookOrderPackageList){
                //一次性订单
                if(studentBookOrderPackage.getIsOnce() == StudentBookOrderPackage.IS_ONCE_YES){
                    List<StudentBookOnceOrder> orderList = findOnceOrderByPackageIdDAO.find(studentBookOrderPackage.getId());
                    if(null != orderList && 0 < orderList.size()){
                        for(StudentBookOnceOrder onceOrder : orderList){
                            Student student = findStudentByCodeDAO.getStudentByCode(onceOrder.getStudentCode());
                            if(!StringUtils.isEmpty(student.getOpenId())){
                                String state = "";
                                if(onceOrder.getState() == StudentBookOnceOrder.STATE_DOING){
                                    state = "处理中";
                                }
                                if(onceOrder.getState() == StudentBookOnceOrder.STATE_SORTING){
                                    state = "分拣中";
                                }
                                if(onceOrder.getState() == StudentBookOnceOrder.STATE_PACK){
                                    state = "已打包";
                                }
                                if(onceOrder.getState() == StudentBookOnceOrder.STATE_SEND){
                                    state = "已发出";
                                }
                                if(onceOrder.getState() == StudentBookOnceOrder.STATE_SIGN){
                                    state = "已签收";
                                }

                                //把结果发送给用户
                                WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
                                templateMessage.setToUser(student.getOpenId());
                                //正式id
                                templateMessage.setTemplateId("jfYK-S8a7G3897PSyy2PTfZv1Zp8pO4bAiXw-6th9EY");
                                //测试id
                                //templateMessage.setTemplateId("8bimC8PR_ZAGassSRJqHSarDgv0pMVVdMMHDM9dLdFI");
                                templateMessage.getDatas().add(new WxMpTemplateData("first", "尊敬的" + student.getName() + "您好，您有订单的快递信息发生变化如下"));
                                templateMessage.getDatas().add(new WxMpTemplateData("OrderSn", onceOrder.getOrderCode()));
                                templateMessage.getDatas().add(new WxMpTemplateData("OrderStatus", state));
                                templateMessage.getDatas().add(new WxMpTemplateData("remark", "最新快递信息："+newData+"\n点击下面“详情”，可以查看更多订单信息"));
                                templateMessage.setUrl("http://xiwang.attop.com/wxSearch/order.htm?code=" + student.getCode());
                                wxMpService.templateSend(templateMessage);
                            }
                        }
                    }
                }else{
                    //普通学生订单
                    List<StudentBookOrder> orderList = findStudentBookOrderByPackageIdDAO.find(studentBookOrderPackage.getId());
                    if(null != orderList && 0 < orderList.size()){
                        for(StudentBookOrder studentBookOrder : orderList){
                            Student student = findStudentByCodeDAO.getStudentByCode(studentBookOrder.getStudentCode());
                            if(!StringUtils.isEmpty(student.getOpenId())){
                                String state = "";
                                if(studentBookOrder.getState() == StudentBookOrder.STATE_SORTING){
                                    state = "分拣中";
                                }
                                if(studentBookOrder.getState() == StudentBookOrder.STATE_PACK){
                                    state = "已打包";
                                }
                                if(studentBookOrder.getState() == StudentBookOrder.STATE_SEND){
                                    state = "已发出";
                                }
                                if(studentBookOrder.getState() == StudentBookOrder.STATE_SIGN){
                                    state = "已签收";
                                }

                                //把结果发送给用户
                                WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
                                templateMessage.setToUser(student.getOpenId());
                                //正式id
                                templateMessage.setTemplateId("jfYK-S8a7G3897PSyy2PTfZv1Zp8pO4bAiXw-6th9EY");
                                //测试id
                                //templateMessage.setTemplateId("8bimC8PR_ZAGassSRJqHSarDgv0pMVVdMMHDM9dLdFI");
                                templateMessage.getDatas().add(new WxMpTemplateData("first", "尊敬的" + student.getName() + "您好，您有订单的快递信息发生变化如下"));
                                templateMessage.getDatas().add(new WxMpTemplateData("OrderSn", studentBookOrder.getOrderCode()));
                                templateMessage.getDatas().add(new WxMpTemplateData("OrderStatus", state));
                                templateMessage.getDatas().add(new WxMpTemplateData("remark", "最新快递信息："+newData+"\n点击下面“详情”，可以查看更多订单信息"));
                                templateMessage.setUrl("http://xiwang.attop.com/wxSearch/order.htm?code=" + student.getCode());
                                wxMpService.templateSend(templateMessage);
                            }
                        }
                    }
                }
            }
        }
    }
}
