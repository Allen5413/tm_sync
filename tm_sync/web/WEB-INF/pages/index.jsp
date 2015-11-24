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
    </tr>
    <tr>
        <td>
            <a href="${pageContext.request.contextPath}/findSyncTxt/find.htm">查看同步日志</a>
        </td>
        <td>
            <a href="${pageContext.request.contextPath}/kuaidiPush/open.htm">模拟快递100推送</a>
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
        <td>

        </td>
    </tr>
</table>

<a href="${pageContext.request.contextPath}/delOrderTM/sync3.htm">del</a>

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
</script>
