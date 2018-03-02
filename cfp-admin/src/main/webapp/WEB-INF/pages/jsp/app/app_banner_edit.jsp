<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="editAppBanner" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="editAppBanner_form" method="post" enctype="multipart/form-data">
	<input type="hidden" name="appBannerId" id="appBannerId" value="${appBanner.appBannerId }"/>
	
	<div class="control-group">
        <label class="control-label">平台：</label>
        <div class="controls">
            <input type="radio" id="appType" name="appType" value="2" ${appBanner.appType==2?'checked':'' }>Ios&nbsp;&nbsp;
            <input type="radio" id="appType" name="appType" value="3" ${appBanner.appType==3?'checked':'' }>Android
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>Banner顺序：</label>
        <div class="controls">
            <input id="orderBy" name="orderBy" value="${appBanner.orderBy }" class="easyui-numberspinner" style="width:80px;" required="required" missingMessage="顺序编号不能为空" data-options="min:1,max:9999,editable:false">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>活动名称：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="活动名称不能为空"
             name="bannerName" id="bannerName" value="${appBanner.bannerName }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>Banner跳转地址：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="跳转地址不能为空"
             name="httpUrl" id="httpUrl" value="${appBanner.httpUrl }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">请求方式：</label>
        <div class="controls">
            <input type="radio" id="httpMethod" name="httpMethod" value="get" ${appBanner.httpMethod=='get'?'checked':'' }>get&nbsp;&nbsp;
            <input type="radio" id="httpMethod" name="httpMethod" value="post" ${appBanner.httpMethod=='post'?'checked':'' }>post
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">是否传UserToken：</label>
        <div class="controls">
            <input type="radio" id="httpIsToken" name="httpIsToken" value="true" ${appBanner.httpIsToken=='true'?'checked':'' }>是&nbsp;&nbsp;
            <input type="radio" id="httpIsToken" name="httpIsToken" value="false" ${appBanner.httpIsToken=='false'?'checked':'' }>否
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>分享标题：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="分享标题不能为空"
             name="title" id="title" value="${appBanner.title }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>分享文案：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="分享文案不能为空"
             name="desc" id="desc" value="${appBanner.desc }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>分享链接：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="分享链接不能为空"
             name="link" id="link" value="${appBanner.link }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">监控分享链接：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" 
             name="shareCloseUrl" id="shareCloseUrl" value="${appBanner.shareCloseUrl }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">监控活动关闭链接：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" 
             name="closeUrl" id="closeUrl" value="${appBanner.closeUrl }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">分享结果回调地址：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" 
             name="shareBackUrl" id="shareBackUrl" value="${appBanner.shareBackUrl }">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>Banner图片：</label>
        <div class="controls">
            <input type="file" style="width: 200px" name="imageSrc" id="imageSrc">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>分享小图：</label>
        <div class="controls">
            <input type="file" style="width: 200px" name="imgUrl" id="imgUrl">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">状态：</label>
        <div class="controls">
            <input type="radio" id="state" name="state" value="1" ${appBanner.state==1?'checked':'' }>上线&nbsp;&nbsp;
            <input type="radio" id="state" name="state" value="0" ${appBanner.state==0?'checked':'' }>关闭
        </div>
    </div>
    
</form>
</div>
<script language="javascript" >

	// 执行：保存操作
    $("#editAppBanner_form").form({
        url:'${ctx}/jsp/app/app_banner_edit?action=edit',
        dataType:'json',
        onSubmit:function() {
             return $(this).form('validate');
        },
        success:function(data) {
            var json = eval('(' + data + ')'); 
            if(json.result == 'success'){
            	 $.messager.alert("系统提示", "操作成功!", "info");
                 parent.$("#editAppBanner").dialog("close");
                 parent.$("#AppBannerList_list").datagrid("reload");
   	    	}else if(json.result == 'error'){
   	    		if(json.errCode == 'check'){
                    $.messager.alert("验证提示", json.errMsg, "info");
                }else{
                    $.messager.alert("系统提示", json.errMsg, "warning");
                }
   	    	}else{
   	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
   	    	}
        }
    });
</script>
</body>
</html>