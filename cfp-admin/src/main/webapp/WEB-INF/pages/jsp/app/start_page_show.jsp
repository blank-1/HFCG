<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="showStartPage">
<form class="form-horizontal" id="showStartPage_form" method="post">
    
    <div class="control-group">
        <label class="control-label">APP类型</label>
        <div class="controls-text" id="appType" style="width:300px;">
        	<c:if test="${appStartPage.appType == 2}">IOS</c:if>
        	<c:if test="${appStartPage.appType == 3}">ANDROID</c:if>
        &nbsp;</div>
    </div>
      
    <div class="control-group">
        <label class="control-label">标题</label>
        <div class="controls-text" id="pageTitle" style="width:300px;">${appStartPage.pageTitle}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">图片请求路径</label>
        <div class="controls-text" id="picUrl" style="width:600px;">${appStartPage.picUrl}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">图片物理路径</label>
        <div class="controls-text" id="picPath" style="width:600px;">${appStartPage.picPath}&nbsp;</div>
    </div>

    <div class="control-group">
        <label class="control-label">状态</label>
        <div class="controls-text" id="status" style="width:300px;">
        <c:if test="${appStartPage.status == 1}">正常</c:if>
        <c:if test="${appStartPage.status == 0}">禁用</c:if>
        &nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">最后更改时间</label>
        <div class="controls-text" id="updateTime" style="width:300px;">
        <fmt:formatDate value="${appStartPage.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">操作人</label>
        <div class="controls-text" id="adminDisplayName" style="width:300px;">${appStartPage.adminDisplayName}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">图片预览</label>
        <img src="${basePath }${appStartPage.picUrl}">
    </div>

</form>
</div>

</body>
</html>