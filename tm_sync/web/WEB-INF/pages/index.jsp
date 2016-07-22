<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
<table>
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
            <a href="${pageContext.request.contextPath}/delOrderTM/sync5.htm">合并还未确认的学生订单</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/syncStudentOnceOrder/sync.htm?isOnlyAdd=1">开始同步学生一次性订单</a>
        </td>
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/syncStudentOnceOrder/sync.htm?isOnlyAdd=0">只同步还未生成的一次性订单</a>
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
</script>
