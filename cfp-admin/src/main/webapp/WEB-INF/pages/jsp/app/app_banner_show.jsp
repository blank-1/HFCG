<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="showAppBanner" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="showAppBanner_form" method="post" enctype="multipart/form-data">
	<div class="control-group">
        <label class="control-label">平台：</label>
        <div class="controls">
        	${appBanner.appType==2?'Ios':'Android' }
        </div>
    </div>
    
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
        <label class="control-label">请求方式：</label>
        <div class="controls">
        	${appBanner.httpMethod=='get'?'get':'post' }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">是否传UserToken：</label>
        <div class="controls">
        	${appBanner.httpIsToken=='true'?'是':'否' }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">分享标题：</label>
        <div class="controls">
            ${appBanner.title }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">分享文案：</label>
        <div class="controls">
            ${appBanner.desc }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">分享链接：</label>
        <div class="controls">
            ${appBanner.link }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">监控分享链接：</label>
        <div class="controls">
            ${appBanner.shareCloseUrl }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">监控活动关闭链接：</label>
        <div class="controls">
            ${appBanner.closeUrl }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">分享结果回调地址：</label>
        <div class="controls">
            ${appBanner.shareBackUrl }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">Banner图片：</label>
        <div class="controls">
        	<img src="${appBanner.imageSrc }"><br/>
        	${appBanner.imageSrc }
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">分享小图：</label>
        <div class="controls">
        	<img src="${appBanner.imgUrl }"><br/>
            ${appBanner.imgUrl }
        </div>
    </div>
    
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