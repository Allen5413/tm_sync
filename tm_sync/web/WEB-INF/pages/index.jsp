<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
<table width="100%">
    <tr>
        <th>同步</th>
        <th>快递</th>
        <th>重置</th>
        <th>奥鹏</th>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/findSyncTxt/find.htm">查看同步日志</a>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/kuaidiPush/open.htm">模拟快递100推送</a>
        </td>
        <td>
            <a href="#" onclick="setStudentPayForSpotCode()">重置中心学生缴费</a>
        </td>
        <td>
            学期id：<input type="text" id="semesterId" />
            <a href="#" onclick="addAoPengOrder()">添加订单</a>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/findBankPayReq/open.htm">请求查询银行支付结果</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/syncSpot/sync.htm">开始同步学习中心信息</a>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/kuaidiPush/find.htm">查询快递100推送记录</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/syncStudent/sync.htm">开始同步学生信息</a>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/kuaidiReq/findByNumber.htm">查询快递100请求结果</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/syncSelectedCourse/sync.htm">开始同步选课信息</a>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/kuaidiReq/openRep.htm">请求快递100</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/syncWangYuanApi/sync.htm">开始同步网院接口信息</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/delOrderTM/sync5.htm">合并还未确认的学生订单</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/syncStudentOnceOrder/sync.htm">开始同步学生一次性订单</a>
            <a href="${pageContext.request.contextPath}/delOnceOrderForNotTM/del.htm">删除没有明细的一次性订单</a>
            <br/>
            <input id="spotCode" size="1">中心<input id="levelCode" size="1">层次<input id="specCode" size="1">专业<input id="year" size="1">入学年<input id="quarter" size="1">入学季<input id="studentCodes" size="1">学号
            <a href="#" onclick="syncTempAdjust()">一次性订单临时改动同步</a>
            <a href="#" onclick="syncNewStudentForNotOrder()">一次性订单临时新增同步</a>
        </td>
    </tr>
    <tr>
        <td>
            学期id：<input id="semesterId2">
            <a href="#" onclick="editOrderForSendBySemesterId()">设置已打包的学生订单为已发出(操作之前看下逻辑，每次情况了能不一样)</a>
        </td>
    </tr>
</table>

<a href="${pageContext.request.contextPath}/delOrderTM/sync6.htm">临时处理</a>

<input type="text" id="code" />
<a href="#" onclick="spring()">15春新生</a>
<a href="#" onclick="spring2()">15春剩余新生</a>


</body>
</html>
<script>
    function spring(){
        var code = $("#code").val();
        location.href = "${pageContext.request.contextPath}/delOrderTM/sync2.htm?code="+code
    }

    function spring2(){
        var code = $("#code").val();
        location.href = "${pageContext.request.contextPath}/delOrderTM/sync4.htm?code="+code
    }

    function setStudentPayForSpotCode(){
        var code = $("#code").val();
        location.href = "${pageContext.request.contextPath}/setStudentPayForSpotCode/set.htm?spotCode="+code
    }

    function addAoPengOrder(){
        var semesterId = $("#semesterId").val();
        location.href = "${pageContext.request.contextPath}/addAoPengOrder/add.htm?semesterId="+semesterId
    }

    function syncTempAdjust(){
        var spotCode = $("#spotCode").val();
        var year = $("#year").val();
        var quarter = $("#quarter").val();
        var specCode = $("#specCode").val();
        var levelCode = $("#levelCode").val();
        var studentCodes = $("#studentCodes").val();
        location.href = "${pageContext.request.contextPath}/syncStudentOnceOrder/syncTempAdjust.htm?spotCode="+spotCode+"&specCode="+specCode+"&levelCode="+levelCode+"&year="+year+"&quarter="+quarter+"&studentCodes="+studentCodes;
    }

    function syncNewStudentForNotOrder(){
        var studentCodes = $("#studentCodes").val();
        location.href = "${pageContext.request.contextPath}/syncStudentOnceOrder/syncNewStudentForNotOrder.htm?studentCodes="+studentCodes;
    }

    function editOrderForSendBySemesterId(){
        var id = $("#semesterId2").val();
        location.href = "${pageContext.request.contextPath}/editOrderForSendBySemesterId/editor.htm?id="+id;
    }
</script>
