<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="showAppBanner" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="showAppBanner_form" method="post" enctype="multipart/form-data">
	<%-- <div class="control-group">
        <label class="control-label">平台：</label>
        <div class="controls">
        	${appBanner.appType==2?'Ios':'Android' }
        </div>
    </div> --%>
    
    <div class="control-group">
        <label class="control-label">Banner顺序：</label>
        <div class="controls">
            ${appBanner.orderBy }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">活动名称：</label>
        <div class="controls">
            ${appBanner.bannerName }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">Banner跳转地址：</label>
        <div class="controls">
            ${appBanner.httpUrl }
        </div>
    </div>

			<div class="control-group">
				<label class="control-label">发布时间：</label>
				<div  class="controls">
				
				   ${publishDateStatus }   
					 
				</div>
			</div>
			<input type="hidden" id="publishStatehidden" name="publishStatehidden" value="${appBanner.publishState}" ><!-- 微信状态 -->
    	    <input type="hidden" id="publishDateHidden" name="publishDateHidden" value="${publishDate}" ><!-- 微信状态 -->
    <div class="control-group">
        <label class="control-label">Banner图片：</label>
        <div class="controls">
        	<img src="${ctx }${appBanner.imageSrc }"><br/>
        	${ctx }${appBanner.imageSrc }
        </div>
    </div>
    
   <%--  <div class="control-group">
        <label class="control-label">分享小图：</label>
        <div class="controls">
        	<img src="${appBanner.imgUrl }"><br/>
            ${appBanner.imgUrl }
        </div>
    </div> --%>
    
    <div class="control-group">
        <label class="control-label">状态：</label>
        <div class="controls">
        	${appBanner.state==1?'上线':'关闭' }
        </div>
    </div>
    
</form>
</div>
</body>
</html>