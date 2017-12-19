package com.zs.tools;

import java.util.Calendar;

/**
 * 生成订单编号工具类
 * Created by Allen on 2015/5/7.
 */
public class OrderCodeTools {

    public static final String PURCHASE_ORDER_CODE_PREFIX_AUTO = "GSA";
    public static final String PURCHASE_ORDER_CODE_PREFIX_MANUAL = "GSM";
    public static final String STUDENT_ORDER_CODE_PREFIX_AUTO = "FSA";
    public static final String STUDENT_ORDER_CODE_PREFIX_MANUAL = "FSM";
    public static final String STUDENT_ORDER_CODE_ONCE = "FSO";
    public static final String STUDENT_ORDER_CODE_ONCE_CONFIRM = "FSOC";
    public static final String SPOT_ORDER_CODE_PREFIX = "YD";


    public static final String TWO_DIGIT = "%02d";
    public static final String FOUR_DIGIT = "%04d";
    public static final String SIX_DIGIT = "%06d";

    /**
     * 生成采购单编号,自动生成的采购单
     * @return
     */
    public static String createPurchaseOrderCodeAuto(int year, int quarter, int number){
        StringBuilder sb = new StringBuilder(PURCHASE_ORDER_CODE_PREFIX_AUTO);
        sb.append(assembleCode(year, quarter, number, FOUR_DIGIT));
        return sb.toString();
    }

    /**
     * 生成采购单编号,手动添加的采购单
     * @return
     */
    public static String createPurchaseOrderCodeManual(int year, int quarter, int number){
        StringBuilder sb = new StringBuilder(PURCHASE_ORDER_CODE_PREFIX_MANUAL);
        sb.append(assembleCode(year, quarter, number, FOUR_DIGIT));
        return sb.toString();
    }

    /**
     * 生成学习中心预订单编号
     * @return
     */
    public static String createSpotOrderCode(int year, int quarter, String spotCode,int number){
        StringBuilder sb = new StringBuilder(SPOT_ORDER_CODE_PREFIX);
        sb.append(assembleCode(year, quarter, spotCode,number,SIX_DIGIT));
        return sb.toString();
    }

    /**
     * 生成学生订书单编号 自动添加订单
     * @return
     */
    public static String createStudentOrderCodeAuto(int year, int quarter, int number){
        StringBuilder sb = new StringBuilder(STUDENT_ORDER_CODE_PREFIX_AUTO);
        sb.append(assembleCode(year, quarter, number, SIX_DIGIT));
        return sb.toString();
    }

    /**
     * 生成学生一次性订单编号
     * @return
     */
    public static String createStudentOnceOrderCode(int year, int quarter, int number){
        StringBuilder sb = new StringBuilder(STUDENT_ORDER_CODE_ONCE);
        sb.append(assembleCode(year, quarter, number, SIX_DIGIT));
        return sb.toString();
    }

    /**
     * 生成学生一次性订单编号, 已经确认了订单重新生成过
     * @return
     */
    public static String createStudentOnceOrderCodeForConfirm(int year, int quarter, int number){
        StringBuilder sb = new StringBuilder(STUDENT_ORDER_CODE_ONCE_CONFIRM);
        sb.append(assembleCode(year, quarter, number, SIX_DIGIT));
        return sb.toString();
    }

    /**
     * 生成学生订书单编号 手动添加订单
     * @return
     */
    public static String createStudentOrderCodeManual(int year, int quarter, int number){
        StringBuilder sb = new StringBuilder(STUDENT_ORDER_CODE_PREFIX_MANUAL);
        sb.append(assembleCode(year, quarter, number, SIX_DIGIT));
        return sb.toString();
    }

    protected static String assembleCode(int year, int quarter, int number, String numDigit){
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append(String.format(TWO_DIGIT, quarter));
        sb.append(String.format(numDigit, number));
        return sb.toString();
    }
    
    private static String assembleCode(int year,int quarter,String spotCode,int number,String numDigit){
    	StringBuilder sb = new StringBuilder();
    	sb.append(year);
    	sb.append(String.format(TWO_DIGIT, quarter));
    	sb.append(spotCode);
    	sb.append(String.format(numDigit, number));
    	return sb.toString();
    }

    /**
     * 生成学生发书单打包编号
     * @param spotCode
     * @param number
     * @return
     */
    public static String createStudentOrderPacageCode(String spotCode, int number){
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR)+"";
        String month = (calendar.get(Calendar.MONTH)+1) < 10 ? "0"+(calendar.get(Calendar.MONTH)+1) : (calendar.get(Calendar.MONTH)+1)+"";
        String day = calendar.get(Calendar.DATE) < 10 ? "0"+calendar.get(Calendar.DATE) : calendar.get(Calendar.DATE)+"";
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append(month);
        sb.append(day);
        sb.append(spotCode);
        sb.append(String.format(FOUR_DIGIT, number));
        return sb.toString();
    }
    
    /**
     * 生成预订单打包编号
     * @param spotCode
     * @param number
     * @return
     */
    public static String createPlaceOrderPacageCode(String spotCode, int number){
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        String year = calendar.get(Calendar.YEAR)+"";
        String month = (calendar.get(Calendar.MONTH)+1) < 10 ? "0"+(calendar.get(Calendar.MONTH)+1) : (calendar.get(Calendar.MONTH)+1)+"";
        String day = calendar.get(Calendar.DATE) < 10 ? "0"+calendar.get(Calendar.DATE) : calendar.get(Calendar.DATE)+"";
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append(month);
        sb.append(day);
        sb.append(spotCode);
        sb.append(String.format(FOUR_DIGIT, number));
        return sb.toString();
    }
    
}
