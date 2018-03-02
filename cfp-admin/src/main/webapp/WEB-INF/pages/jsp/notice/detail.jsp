<%@page import="com.xt.cfp.core.constants.WechatNoticeConstants"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
</head>
<%
	//发布状态
	WechatNoticeConstants.WechatNoticeStateEnum[] wechatNoticeState = WechatNoticeConstants.WechatNoticeStateEnum.values();
	request.setAttribute("wechatNoticeState", wechatNoticeState);
%>
<body>
<div id="showWechatNotice" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <form class="form-horizontal" id="showWechatNotice_form" method="post" action="">

        <div class="control-group">
            <label class="control-label">公告ID：</label>

           	<div class="controls">
            	${wechatNotice.noticeId }
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">标题：</label>
            
            <div class="controls">
            	${wechatNotice.noticeTitle }
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">公告简介：</label>
            
            <div class="controls">
                ${wechatNotice.noticeSynopsis }
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">发布时间：</label>

            <div class="controls">
                <fmt:formatDate value="${wechatNotice.publishTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">公告状态：</label>

            <div class="controls">
                <c:forEach items="${wechatNoticeState}" var="wns">
                	<c:if test="${wechatNotice.noticeState eq wns.value}">${wns.desc}</c:if>
                </c:forEach>
            </div>
        </div>
        
        <div class="control-group">
            <label class="control-label">公告内容：</label>
            
            <div class="controls">
                ${wechatNotice.noticeContent }
            </div>
        </div>
        
    </form>
</div>
</body>
</html>