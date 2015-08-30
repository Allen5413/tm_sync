<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <%@ include file="/common/meta.jsp"%>
  <%@ include file="/common/taglibs.jsp"%>
</head>
<body class="body-bg">
<div id="page">
  <div id="header">
    <div class="logo_admin">
      <div class="logo"><h2>重庆西网文化传播有限公司教材管理系统</h2></div>
      <a class="quit_ico" href="${pageContext.request.contextPath}/logoutUser/logou.htm">退出</a>
    </div>
    <div class="barNews_left">
      <div class="barNews_right">
        <label class="right">
          <span class="icoTime">日期：${year}年${month}月${day}日 ${week}</span>
          <em class="hlr">&nbsp;</em>
          <span class="point">&nbsp;</span>
        </label>
        <a class="toHome" href="${pageContext.request.contextPath}/index/main.htm"></a>
        <a class="setPwd" href="#" onclick="editPwd()"></a>
      </div>
    </div>
  </div>
  <div id="centerAdmin">
    <div class="admin_leftMenu">
      <dl class="list-menu">
        <c:forEach var="menu" items="${menu}" varStatus="status">
          <dt lang="${status.index}" onclick="clickMenu(${status.index})">
            <a class="tOff" href="#">${menu.key}</a>
          </dt>
          <dd lang="${status.index}" class="display_none">
            <ul>
              <c:forEach var="resource" items="${menu.value}">
                <li onclick="clickResource('${pageContext.request.contextPath}${resource.url}', this, '${resource.name}')">
                  <a href="#">${resource.name}</a>
                </li>
              </c:forEach>
            </ul>
          </dd>
        </c:forEach>
      </dl>
    </div>
    <div class="admin_rightContent">
      <div class="content_main">
        <div class="arrow_btn">
        </div>
        <div class="mainContent">
          <div class="mainTab">
            <span class="opt_right">
              <a class="optPrev" href="#" onclick="prevTab()">&nbsp;</a>
              <a class="optNext" href="#" onclick="nextTab()">&nbsp;</a>
              <a class="optClose" href="#" onclick="closeTab()">&nbsp;</a>
            </span>
            <a lang="${pageContext.request.contextPath}/prompt/openPromptPage.htm" class='tab_1 on' href='#' onclick='clickTab(this)'>首页</a>
          </div>
          <div class="contain_blockBg">
            <div class="container">
              <iframe id="index_iframe" frameborder="no" class="iframe-noborder" src="${pageContext.request.contextPath}/prompt/openPromptPage.htm" width="87%"></iframe>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div id="dialogDiv"></div>
  <div id="indexOutDiv"><div id="paydialogDiv"></div></duv>
  <div id="ownOutDiv"><div id="owndialogDiv"></div></div>
  <div id="footAdmin">
    <p><span class="right"></span>欢迎您，<a href="#">${loginName}</a> <em class="hrl_2">&nbsp;</em> 姓名：<a href="#">${name}</a></p>
  </div>
</div>
</body>
</html>
<script>
  function closeDialog(){
    closeD($("#dialogDiv"));
  }
  function openDialog(title, width, height, url){
    openD($("#dialogDiv"), title, width, height, url);
  }
  function refreshDialog(){
    refreshD($("#dialogDiv"));
  }
  function detoryDialog(){
	destroyD($("#dialogDiv"));
  }
  function detoryPayDialog(){
	destroyD($("#paydialogDiv"));
  }
  function removePayDialog(){
	removeD($("#paydialogDiv"));
  }
  function isPayClear(){
	  return document.getElementById("paydialogDiv").innerHTML == "";
  }
  function editPwd(){
    openD($("#dialogDiv"), '修改密码', 0.3, 0.3, '${pageContext.request.contextPath}/editPwd/openEditUserPwdPage.htm');
  }
  function getDialogDivHeight(){
    return $("#dialogDiv").height();
  }
  
  function openPayDialog(url){
	  $("#paydialogDiv").dialog({
	        title: "中心交费",
	        width: getWindowWidthSize() * 0.6,
	        height: getWindowHeightSize() * 0.9,
	        href: url,
	        modal: true,
	        onClose : function(){
	        	 $(this).dialog("destroy").remove();
	        	 var createDiv = $("<div></div>");
				 createDiv.attr("id","paydialogDiv");
		         createDiv.appendTo($("#indexOutDiv"));
	        }
	  }).dialog("open");
  }
  
  function openOwnDialog(title, width, height, url){
	  
	 // var dialogParent = $("#owndialogDiv").parent();  
	  //var dialogOwnClone = $("#owndialogDiv").clone();  
	  //dialogOwnClone.hide();  
	  
	  $("#owndialogDiv").dialog({
		 title: "中心交费",
	     width: getWindowWidthSize() * 0.6,
	     height: getWindowHeightSize() * 0.9,
	     href: url,
	     modal: true,
	     onClose : function(){
	    	  $(this).dialog("destroy").remove();
    		  var createDiv = $("<div></div>");
			  createDiv.attr("id","owndialogDiv");
	          createDiv.appendTo($("#ownOutDiv"));
	     }
	  }).dialog("open");
  }
  
  function closeOwnDialog(){
	  closeD($("#owndialogDiv"));  
  }
  
  function closePayDialog(){
	  closeD($("#paydialogDiv"));
  }
  
  $(function(){
	    $("#index_iframe").css("height", getWindowHeightSize()*0.7);
	  });
</script>