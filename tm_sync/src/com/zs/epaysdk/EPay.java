/**
 * 收付直通车代收接口类<br />
 * <b>[注意]</b> 首次调用API时必须先设置Configure类中的商户号和商户密钥等参数<br />
 * 收付直通车代收模块中的所有接口都在该类中，商户可以直接调用该类中的API方法<br />
 * 该SDK中代码仅供学习和参考，商户可以自己根据收付直通车代收接口文档进行编码实现，而不使用该SDK中的实现<br />
 * 各字段长度、类型等请参考收付直通车代收接口文档中的定义，SDK不检查这些长度，而直接发送给收付直通车<br />
 * [重要]各传入参数SDK都不作任何检查、过滤，请务必在传入前进行安全检查或过滤，特别是生成HTML代码的方法，务必保证传入参数的安全性，否则会导致安全问题。
 *
 * @author xiezz
 * @version 1.1.3
 */
package com.zs.epaysdk;

import com.zs.epaysdk.common.Configure;
import com.zs.epaysdk.service.dl.DlFile;
import com.zs.epaysdk.service.dl.DlSettleFile;
import com.zs.epaysdk.service.ep.*;
import com.zs.epaysdk.service.gp.GpPay;
import com.zs.epaysdk.service.gp.GpQuery;
import com.zs.epaysdk.service.gp.GpRefund;
import com.zs.epaysdk.service.gp.GpRefundQuery;
import com.zs.epaysdk.service.py.*;
import com.zs.epaysdk.util.FileUtil;
import com.zs.epaysdk.util.IPUtil;
import com.zs.epaysdk.util.Signature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class EPay {

    /**
     * 快捷支付认证接口（异步接口）<br />
     * 该方法将生成跳转页面的全部HTML代码，商户直接输出该HTML代码至某个URL所对应的页面中，即可实现跳转，可以参考示例epay_redirect.jsp<br />
     * [重要]各传入参数SDK都不作任何检查、过滤，请务必在传入前进行安全检查或过滤，保证传入参数的安全性，否则会导致安全问题。
     *
     * @param trac_no    商户跟踪号
     * @param acct_type  卡类型：0-储蓄卡,1-信用卡
     * @param bank_no    人行联网行号
     * @param card_no    账号
     * @param user_name  姓名（可选，若为非null，则用户界面显示该值且不可输）
     * @param cert_no    证件号码（可选，若为非null，则用户界面显示该值且不可输）
     * @param card_phone 联系电话（可选，若为非null，则用户界面显示该值且不可输）
     * @param expireDate 信用卡有效期（仅信用卡有效，格式MMYY，可选，若为非null，则用户界面显示该值且不可输）
     * @param cvn        信用卡CVN（仅信用卡有效，可选，若为非null，则用户界面显示该值且不可输）
     * @return 跳转页面HTML代码
     */
    public static String epAuth(String trac_no, String acct_type, String bank_no, String card_no, String user_name, String cert_no, String card_phone, String expireDate, String cvn) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("trac_no", trac_no);
        params.put("acct_type", acct_type);
        if (bank_no != null && !("".equals(bank_no)))
            params.put("bank_no", bank_no);
        params.put("card_no", card_no);

        if (user_name != null) params.put("user_name", user_name);
        if (cert_no != null) {
            params.put("cert_no", cert_no);
            params.put("cert_type", "0");
        }
        if (card_phone != null) params.put("card_phone", card_phone);

        if (expireDate != null) params.put("expireDate", expireDate);
        if (cvn != null) params.put("cvn", cvn);

        return new EpAuth().build(params);
    }

    public static String epAuth(String trac_no, String acct_type, String bank_no, String card_no) {
        return epAuth(trac_no, acct_type, bank_no, card_no, null, null, null, null, null);
    }

    /**
     * 快捷支付认证接口（同步接口，需短信确认）
     *
     * @param trac_no    商户跟踪号
     * @param acct_type  卡类型：0-储蓄卡,1-信用卡
     * @param bank_no    人行联网行号
     * @param card_no    账号
     * @param user_name  姓名
     * @param cert_no    证件号码
     * @param card_phone 联系电话
     * @param expireDate 信用卡有效期（仅信用卡时必输，格式MMYY）
     * @param cvn        信用卡CVN（仅信用卡时必输）
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epAuthSyncWithSms(String trac_no, String acct_type, String bank_no, String card_no, String user_name, String cert_no, String card_phone, String expireDate, String cvn) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("trac_no", trac_no);
        params.put("acct_type", acct_type);
        if (bank_no != null && !("".equals(bank_no)))
            params.put("bank_no", bank_no);
        params.put("card_no", card_no);

        params.put("user_name", user_name);
        params.put("cert_no", cert_no);
        params.put("card_phone", card_phone);

        if (expireDate != null) params.put("expireDate", expireDate);
        if (cvn != null) params.put("cvn", cvn);

        return new EpAuthQuickSms().exec(params);
    }

    /**
     * 快捷认证短信验证码确认接口
     *
     * @param trac_no  发起同步认证时的商户跟踪号
     * @param sms_code 6位数字短信验证码
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epAuthCheckSms(String trac_no, String sms_code) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("trac_no", trac_no);
        params.put("sms_code", sms_code);

        return new EpAuthCheckSms().exec(params);
    }

    /**
     * 快捷支付账户解绑接口
     *
     * @param card_no 账号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epAuthCancel(String card_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("card_no", card_no);

        return new EpAuthCancel().exec(params);
    }

    /**
     * 快捷支付账户认证结果查询接口
     *
     * @param trac_no 商户跟踪号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epAuthQuery(String trac_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("trac_no", trac_no);

        return new EpAuthQuery().exec(params);
    }

    /**
     * 快捷支付交易接口
     *
     * @param order_no     订单号
     * @param order_amount 金额，单位元，两位小数，例：8.00
     * @param order_title  订单标题
     * @param order_desc   订单描述
     * @param card_no      支付卡号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epPay(String order_no, String order_amount, String order_title, String order_desc, String card_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_amount", order_amount);
        params.put("order_title", order_title);
        params.put("order_desc", order_desc);
        params.put("card_no", card_no);

        return new EpPay().exec(params);
    }

    /**
     * 快捷支付交易查询接口（查询指定日期）
     *
     * @param order_no   订单号
     * @param order_date 订单日期，格式yyyyMMdd
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epQuery(String order_no, String order_date) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_date", order_date);

        return new EpQuery().exec(params);
    }

    /**
     * 快捷支付交易查询接口（查询当天交易）
     *
     * @param order_no 订单号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epQuery(String order_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);

        return new EpQuery().exec(params);
    }

    /**
     * 快捷支付退款交易接口
     *
     * @param order_no     待退款订单号
     * @param order_date   订单下单日期，格式yyyyMMdd
     * @param order_amount 退款金额（不能大于原订单金额）
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epRefund(String order_no, String order_date, String order_amount) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_date", order_date);
        params.put("order_amount", order_amount);

        return new EpRefund().exec(params);
    }

    /**
     * 快捷支付退款交易结果查询接口（查询指定日期）
     *
     * @param order_no   退款的订单号
     * @param order_date 订单下单日期，格式yyyyMMdd
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epRefundQuery(String order_no, String order_date) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_date", order_date);

        return new EpRefundQuery().exec(params);
    }

    /**
     * 快捷支付退款交易结果查询接口（查询当天订单）
     *
     * @param order_no 退款的订单号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String epRefundQuery(String order_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);

        return new EpRefundQuery().exec(params);
    }

    /**
     * 无绑定账户快捷支付跳转页面生成接口<br />
     * 该方法将生成跳转页面的全部HTML代码，商户直接输出该HTML代码至某个URL所对应的页面中，即可实现跳转，可以参考示例epay_redirect.jsp<br />
     * [重要]各传入参数SDK都不作任何检查、过滤，请务必在传入前进行安全检查或过滤，保证传入参数的安全性，否则会导致安全问题。<br />
     * 参数bank_no,acct_type,card_no需要全为null或者全不为null。
     *
     * @param order_no     订单号
     * @param order_amount 金额，单位元，两位小数，例：8.00
     * @param order_title  订单标题
     * @param order_desc   订单描述
     * @param remote_ip    客户端IP地址
     * @param bank_no      人行联网行号（可选，若为非null，则用户界面显示该值且不可输）
     * @param acct_type    卡类型：0-储蓄卡,1-信用卡（可选，若为非null，则用户界面显示该值且不可输）
     * @param card_no      账号（可选，若为非null，则用户界面显示该值且不可输）
     * @param user_name    姓名（可选，若为非null，则用户界面显示该值且不可输）
     * @param cert_no      证件号码（可选，若为非null，则用户界面显示该值且不可输）
     * @param card_phone   联系电话（可选，若为非null，则用户界面显示该值且不可输）
     * @param expireDate   信用卡有效期（仅信用卡有效，格式MMYY，可选，若为非null，则用户界面显示该值且不可输）
     * @param cvn          信用卡CVN（仅信用卡有效，可选，若为非null，则用户界面显示该值且不可输）
     * @return 跳转页面HTML代码
     */
    public static String epAuthPay(String order_no, String order_amount, String order_title, String order_desc, String remote_ip,
                                   String bank_no, String acct_type, String card_no, String user_name, String cert_no, String card_phone, String expireDate, String cvn) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_amount", order_amount);
        params.put("order_title", order_title);
        params.put("order_desc", order_desc);
        params.put("order_ip", remote_ip);

        if (bank_no != null) params.put("bank_no", bank_no);
        if (acct_type != null) params.put("acct_type", acct_type);
        if (card_no != null) params.put("card_no", card_no);
        if (user_name != null) params.put("user_name", user_name);
        if (cert_no != null) {
            params.put("cert_no", cert_no);
            params.put("cert_type", "0");
        }
        if (card_phone != null) params.put("card_phone", card_phone);
        if (expireDate != null) params.put("expireDate", expireDate);
        if (cvn != null) params.put("cvn", cvn);

        return new EpAuthPay().build(params);
    }

    public static String epAuthPay(String order_no, String order_amount, String order_title, String order_desc, String remote_ip) {
        return epAuthPay(order_no, order_amount, order_title, order_desc, remote_ip, null, null, null, null, null, null, null, null);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 网关支付交易跳转页面生成接口<br />
     * 该方法将生成跳转页面的全部HTML代码，商户直接输出该HTML代码至某个URL所对应的页面中，即可实现跳转，可以参考示例epay_redirect.jsp<br />
     * [重要]各传入参数SDK都不作任何检查、过滤，请务必在传入前进行安全检查或过滤，保证传入参数的安全性，否则会导致安全问题。
     *
     * @param order_no     订单号
     * @param order_amount 金额，单位元，两位小数，例：8.00
     * @param order_title  订单标题
     * @param order_desc   订单描述
     * @return 跳转页面HTML代码
     */
    public static String gpPay(String order_no, String order_amount, String order_title, String order_desc) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_amount", order_amount);
        params.put("order_title", order_title);
        params.put("order_desc", order_desc);
        params.put("order_ip", IPUtil.getLocalIp());

        return new GpPay().build(params);
    }

    /**
     * 网关支付交易查询接口（查询指定日期）
     *
     * @param order_no   订单号
     * @param order_date 订单日期，格式yyyyMMdd
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String gpQuery(String order_no, String order_date) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_date", order_date);

        return new GpQuery().exec(params);
    }

    /**
     * 网关支付交易查询接口（查询当天交易）
     *
     * @param order_no 订单号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String gpQuery(String order_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);

        return new GpQuery().exec(params);
    }

    public static void main(String[] args){
        EPay ePay = new EPay();
        System.out.print(ePay.gpQuery("20160429143642003"));
    }

    /**
     * 网关支付退款交易接口
     *
     * @param order_no     待退款订单号
     * @param order_date   订单下单日期，格式yyyyMMdd
     * @param order_amount 退款金额（不能大于原订单金额）
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String gpRefund(String order_no, String order_date, String order_amount) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_date", order_date);
        params.put("order_amount", order_amount);

        return new GpRefund().exec(params);
    }

    /**
     * 网关支付退款交易结果查询接口（查询指定日期）
     *
     * @param order_no   退款的订单号
     * @param order_date 订单下单日期，格式yyyyMMdd
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String gpRefundQuery(String order_no, String order_date) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("order_date", order_date);

        return new GpRefundQuery().exec(params);
    }

    /**
     * 网关支付退款交易结果查询接口（查询当天订单）
     *
     * @param order_no 退款的订单号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String gpRefundQuery(String order_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);

        return new GpRefundQuery().exec(params);
    }

    /**
     * 对账文件下载接口（不生成文件）
     *
     * @param rcpt_type  回单类型：0-快捷入账回单；1-快捷出账回单；2-快捷手续费回单；3-网关支付入账回单；4-网关支付出账回单；5-网关支付手续费回单；6-代付入账回单；7-代付出账回单；8-代付手续费回单
     * @param trans_date 交易日期，格式yyyyMMdd
     * @return 当下载成功时，返回文件内容（byte[]类型）；当下载失败时，返回失败信息json字符串（String类型）
     */
    public static Object dlSettleFile(String rcpt_type, String trans_date) {

        HashMap<String, String> params = new HashMap<String, String>();
        if ("6".equals(rcpt_type)) {
            params.put("rcpt_type", "0");
            params.put("trans_date", trans_date);
            return new DlSettleFile(1).download(params);
        } else if ("7".equals(rcpt_type)) {
            params.put("rcpt_type", "1");
            params.put("trans_date", trans_date);
            return new DlSettleFile(1).download(params);
        } else if ("8".equals(rcpt_type)) {
            params.put("rcpt_type", "2");
            params.put("trans_date", trans_date);
            return new DlSettleFile(1).download(params);
        } else {
            params.put("rcpt_type", rcpt_type);
            params.put("trans_date", trans_date);
            return new DlSettleFile().download(params);
        }
    }

    /**
     * 对账文件下载接口（生成文件）
     *
     * @param rcpt_type  回单类型：0-快捷入账回单；1-快捷出账回单；2-快捷手续费回单；3-网关支付入账回单；4-网关支付出账回单；5-网关支付手续费回单；6-代付入账回单；7-代付出账回单；8-代付手续费回单
     * @param trans_date 交易日期，格式yyyyMMdd
     * @param filename   保存文件名（可带路径）
     * @return 下载结果json字符串
     */
    public static String dlSettleFile(String rcpt_type, String trans_date, String filename) {

        HashMap<String, String> params = new HashMap<String, String>();
        if ("6".equals(rcpt_type)) {
            params.put("rcpt_type", "0");
            params.put("trans_date", trans_date);
            return new DlSettleFile(1).downloadToFile(params, filename);
        } else if ("7".equals(rcpt_type)) {
            params.put("rcpt_type", "1");
            params.put("trans_date", trans_date);
            return new DlSettleFile(1).downloadToFile(params, filename);
        } else if ("8".equals(rcpt_type)) {
            params.put("rcpt_type", "2");
            params.put("trans_date", trans_date);
            return new DlSettleFile(1).downloadToFile(params, filename);
        } else {
            params.put("rcpt_type", rcpt_type);
            params.put("trans_date", trans_date);
            return new DlSettleFile().downloadToFile(params, filename);
        }
    }

    /**
     * 行号文件下载接口（不生成文件）
     *
     * @param download_type 文件类型：01-行号文件
     * @return 当下载成功时，返回文件内容（byte[]类型）；当下载失败时，返回失败信息json字符串（String类型）
     */
    public static Object dlFile(String download_type) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("download_type", download_type);

        return new DlFile().download(params);
    }

    /**
     * 行号文件下载接口（生成文件）
     *
     * @param download_type 文件类型：01-行号文件
     * @param filename      保存文件名（可带路径）
     * @return 下载结果json字符串
     */
    public static String dlFile(String download_type, String filename) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("download_type", download_type);

        return new DlFile().downloadToFile(params, filename);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 智能代付单笔付款接口
     *
     * @param order_no     订单号
     * @param to_bank_no   收款行行号
     * @param to_acct_no   收款人账户
     * @param to_acct_name 收款人户名
     * @param acct_type    账户类型：0-储蓄卡,1-信用卡,2-对公账户
     * @param trans_amt    付款金额
     * @param trans_usage  用途
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String pyPay(String order_no, String to_bank_no, String to_acct_no, String to_acct_name, String acct_type, String trans_amt, String trans_usage) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);
        params.put("to_bank_no", to_bank_no);
        params.put("to_acct_no", to_acct_no);
        params.put("to_acct_name", to_acct_name);
        params.put("acct_type", acct_type);
        params.put("trans_amt", trans_amt);
        params.put("trans_usage", trans_usage);

        return new PyPay().exec(params);
    }

    /**
     * 智能代付单笔订单查询接口
     *
     * @param order_no 订单号
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String pyQuery(String order_no) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_no", order_no);

        return new PyQuery().exec(params);
    }

    /**
     * 智能代付商户信息查询接口
     *
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String pyGetMrch() {

        HashMap<String, String> params = new HashMap<String, String>();
        return new PyGetMrch().exec(params);
    }

    /**
     * 智能代付批量付款接口
     *
     * @param file_name 文件名
     * @param file      文件路径（带文件名）
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String pyBatchPay(String file_name, String file) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("file_name", file_name);
        params.put("file_content", FileUtil.getBase64FromFile(file));

        return new PyBatchPay().exec(params);
    }

    /**
     * 智能代付批量付款结果查询接口
     *
     * @param file_name      文件名
     * @param order_date     订单日期
     * @param save_file_name 保存内容至该文件（置为null为不保存至文件），写入文件失败将返回Configure.FILE_ERROR_RESULT
     * @return json格式结果，返回结果包含字段请参看收付直通车代收接口文档
     */
    public static String pyBatchPayQuery(String file_name, String order_date, String save_file_name) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("file_name", file_name);
        params.put("order_date", order_date);

        String result = new PyBatchPayQuery().exec(params);
        if (file_name != null && result.contains("file_content")) {
            try {
                Map<String, String> resMap = Signature.jsonToMap(result);
                FileUtil.writeFileFromBase64(resMap.get("file_content"), save_file_name);
                return result;
            } catch (IOException e) {
                return Configure.FILE_ERROR_RESULT;
            }
        } else {
            return result;
        }
    }
}
