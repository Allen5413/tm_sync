<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body>
  <form action="${pageContext.request.contextPath}/kuaidiPush/push.htm" method="post">
    <textarea rows="10" cols="150" name="param">{"status":"polling","billstatus":"got","message":"","lastResult":{"message":"ok","state":"0","status":"200","condition":"F00","ischeck":"0","com":"yuantong","nu":"V030344422","data":[{"context":"上海分拨中心/装件入车扫描 ","time":"2012-08-28 16:33:19","ftime":"2012-08-28 16:33:19",},{"context":"上海分拨中心/下车扫描 ","time":"2012-08-27 23:22:42","ftime":"2012-08-27 23:22:42"}]}}</textarea>
    <br />
    <input type="submit" />
  </form>
</body>
</html>
