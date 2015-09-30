package com.zs.service.kuaidi.push.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.kuaidi.push.KuaidiPushDAO;
import com.zs.domain.kuaidi.KuaidiPush;
import com.zs.service.kuaidi.push.AddPushService;
import com.zs.tools.DateTools;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Allen on 2015/9/30.
 */
@Service("addPushService")
public class AddPushServiceImpl extends EntityServiceImpl<KuaidiPush, KuaidiPushDAO> implements AddPushService{

    @Resource
    private KuaidiPushDAO kuaidiPushDAO;

    @Override
    public void add(String json)throws Exception{
        //把json字符串转成json对象
        JSONObject pushInfo = JSONObject.fromObject(json);
        //得到快递单号
        String nu = pushInfo.get("nu").toString();
        //查询当前快递单号信息是否存在，存在就修改，不存在就新增
        KuaidiPush kuaidiPush = kuaidiPushDAO.findByNu(nu);
        if(null != kuaidiPush){
            kuaidiPush.setStatus(pushInfo.get("status").toString());
            kuaidiPush.setState(Integer.parseInt(pushInfo.get("state").toString()));
            kuaidiPush.setMessage(pushInfo.get("message").toString());
            kuaidiPush.setData(pushInfo.get("data").toString());
            kuaidiPush.setOperateTime(DateTools.getLongNowTime());
            super.update(kuaidiPush);
        }else{
            kuaidiPush = new KuaidiPush();
            kuaidiPush.setCom(pushInfo.get("com").toString());
            kuaidiPush.setNu(nu);
            kuaidiPush.setStatus(pushInfo.get("status").toString());
            kuaidiPush.setState(Integer.parseInt(pushInfo.get("state").toString()));
            kuaidiPush.setMessage(pushInfo.get("message").toString());
            kuaidiPush.setData(pushInfo.get("data").toString());
            kuaidiPush.setOperateTime(DateTools.getLongNowTime());
            super.save(kuaidiPush);
        }
    }
}
