<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
<form action="${pageContext.request.contextPath}/kuaidiReq/findByNumber.htm">
  快递号：<input type="text" name="number">
  <input type="submit" />
  <br /><br />
  <table width="100%" border="1">
    <tr>
      <td>company</td>
      <td>number</td>
      <td>result</td>
      <td>returnCode</td>
      <td>message</td>
      <td>operateTime</td>
    </tr>
    <c:forEach var="data" items="${list}">
      <tr>
        <td>${data.company}</td>
        <td>${data.number}</td>
        <td>${data.result}</td>
        <td>${data.returnCode}</td>
        <td>${data.message}</td>
        <td>${data.operateTime}</td>
      </tr>
    </c:forEach>
  </table>
</form>
</body>
</html>
