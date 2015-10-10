<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
<a href="${pageContext.request.contextPath}/findSyncTxt/find.htm">查看同步日志</a>
<br /><br />
<a href="${pageContext.request.contextPath}/kuaidiPush/open.htm">模拟快递100推送</a>
<br /><br />
<a href="${pageContext.request.contextPath}/kuaidiPush/find.htm">查询快递100推送记录</a>
<br /><br />
<a href="${pageContext.request.contextPath}/kuaidiReq/findByNumber.htm">查询快递100请求结果</a>
<br /><br />
<a href="${pageContext.request.contextPath}/kuaidiReq/openRep.htm">请求快递100</a>
</body>
</html>
