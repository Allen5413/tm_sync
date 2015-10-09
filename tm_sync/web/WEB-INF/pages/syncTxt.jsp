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
  <c:forEach var="data" items="${result}">
    <a href="${pageContext.request.contextPath}/findSyncTxt/find.htm?path=${data.path}&name=${data.fileName}">${data.fileName}</a><br />
  </c:forEach>
  ${content}
</table>
</body>
</html>
