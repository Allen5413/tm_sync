package com.zs.service.scheduler.impl;

import com.zs.dao.basic.issuerange.FindIssueRangeBySpotCodeDAO;
import com.zs.dao.sync.SpotDAO;
import com.zs.dao.sync.SpotTempDAO;
import com.zs.dao.sync.SpotProvinceDAO;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.sync.Spot;
import com.zs.domain.sync.SpotProvince;
import com.zs.domain.sync.SpotTemp;
import com.zs.service.scheduler.SyncSpotTaskService;
import com.zs.tools.DateTools;
import com.zs.tools.FileTools;
import com.zs.tools.PropertiesTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by Allen on 2015/10/19.
 */
@Service("syncSpotTaskService")
public class SyncSpotTaskServiceImpl implements SyncSpotTaskService {

    @Resource
    private SpotTempDAO spotTempDAO;
    @Resource
    private SpotDAO spotDAO;
    @Resource
    private SpotProvinceDAO spotProvinceDAO;
    @Resource
    private FindIssueRangeBySpotCodeDAO findIssueRangeBySpotCodeDAO;

    //变更信息描述
    private String detail = "";

    @Override
    @Transactional
    public void syncSpot() {
        StringBuilder msg = new StringBuilder(DateTools.getLongNowTime()+": 开始执行学习中心信息同步\r\n");
        String code = "";
        int tempNum = 0;
        try {
            //查询临时表的学习中心数据
            List<SpotTemp> spotTempList = spotTempDAO.getAll();
            //查询我们现在的学习中心数据
            List<Spot> spotList = spotDAO.getAll();

            if (null != spotTempList && 0 < spotTempList.size()) {
                for (SpotTemp spotTemp : spotTempList) {
                    String spotCode = spotTemp.getCode();
                    //是否新增中心
                    boolean isNewSpot = true;
                    if (null != spotList && 0 < spotList.size()) {
                        for (Spot spot : spotList) {
                            if (spotTemp.getCode().equals(spot.getCode())) {
                                detail += "\r\n中心编号["+spotCode+"]: ";
                                boolean isUpdate = false;
                                isNewSpot = false;
                                //查询以前的省中心学习中心关联
                                SpotProvince spotProvince = spotProvinceDAO.findBySpotCode(spot.getCode());
                                //省中心关联
                                if(null == spotProvince && null != spotTemp.getProvCode()){
                                    detail += "省中心 null 改为 "+spotTemp.getProvCode()+" 、";
                                    spot.setName(spotTemp.getName());
                                    spot.setOperateTime(DateTools.getLongNowTime());
                                    isUpdate = true;

                                    spotProvince = new SpotProvince();
                                    spotProvince.setProvinceCode(spotTemp.getProvCode());
                                    spotProvince.setSpotCode(spotCode);
                                    spotProvinceDAO.save(spotProvince);
                                }
                                if(null != spotProvince && null == spotTemp.getProvCode()){
                                    detail += "省中心"+spotProvince.getProvinceCode()+" 改为 null 、";
                                    spot.setName(spotTemp.getName());
                                    spot.setOperateTime(DateTools.getLongNowTime());
                                    isUpdate = true;

                                    spotProvince.setProvinceCode(null);
                                    spotProvinceDAO.update(spotProvince);
                                }
                                if(null != spotProvince && null != spotTemp.getProvCode() && !spotProvince.getProvinceCode().equals(spotTemp.getProvCode())){
                                    detail += "省中心"+spotProvince.getProvinceCode()+" 改为 "+spotTemp.getProvCode()+" 、";
                                    spot.setName(spotTemp.getName());
                                    spot.setOperateTime(DateTools.getLongNowTime());
                                    isUpdate = true;

                                    spotProvince.setProvinceCode(spotTemp.getProvCode());
                                    spotProvinceDAO.update(spotProvince);
                                }
                                //中心名称
                                if (!spotTemp.getName().equals(spot.getName())) {
                                    detail += "名称 "+spot.getName()+" 改为 "+spotTemp.getName()+" 、";
                                    spot.setName(spotTemp.getName());
                                    spot.setOperateTime(DateTools.getLongNowTime());
                                    isUpdate = true;
                                    spotDAO.update(spot);
                                }
                                if(!isUpdate){
                                    detail += "无变化";
                                }
                                break;
                            }

                        }
                    }
                    if (isNewSpot) {
                        //新增中心
                        Spot spot = new Spot();
                        spot.setCode(spotCode);
                        spot.setName(spotTemp.getName());
                        spot.setPostalCode(spotTemp.getPostalCode());
                        spot.setAddress(spotTemp.getAddress());
                        spot.setAdminName(spotTemp.getAdminName());
                        spot.setEmail(spotTemp.getEmail());
                        spot.setPhone(spotTemp.getPhone());
                        spot.setSex(spotTemp.getSex());
                        spot.setTel(spotTemp.getTel());
                        spotDAO.save(spot);
                        //新增中心与省中心关联
                        SpotProvince spotProvince = new SpotProvince();
                        spotProvince.setSpotCode(spotCode);
                        spotProvince.setProvinceCode(spotTemp.getProvCode());
                        spotProvinceDAO.save(spotProvince);
                        //增加中心与发行商的关联
                        IssueRange issueRange = new IssueRange();
                        issueRange.setSpotCode(spotCode);
                        issueRange.setIssueChannelId(1l);
                        issueRange.setCreator("管理员");
                        issueRange.setIsIssue(IssueRange.ISISSUE_YES);
                        issueRange.setOperator("管理员");
                        findIssueRangeBySpotCodeDAO.save(issueRange);

                        detail += "\r\n中心编号["+spotCode+"]: 为新增中心";
                    }
                    tempNum++;
                }
            }
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.append("学习中心编号："+code+" \r\n");
            msg.append("异常信息：" + sw.toString() + "\r\n");
        }finally {
            msg.append("\r\n"+detail+"\r\n");
            msg.append("执行了"+tempNum+"条数据\r\n");
            msg.append(DateTools.getLongNowTime()+": 学习中心信息同步结束");

            PropertiesTools propertiesTools =  new PropertiesTools("resource/commons.properties");
            String rootPath = propertiesTools.getProperty("sync.log.file.path");
            String filePath = propertiesTools.getProperty("sync.spot.log.file.path");
            String nowDate = DateTools.transferLongToDate("yyyy-MM-dd", System.currentTimeMillis());
            FileTools.createFile(rootPath + filePath, nowDate + ".txt");
            FileTools.writeTxtFile(msg.toString(), rootPath + filePath + nowDate + ".txt");
        }
    }
}
