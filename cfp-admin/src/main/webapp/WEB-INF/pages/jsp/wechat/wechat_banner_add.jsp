<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addAppBanner" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="addAppBanner_form" method="post" enctype="multipart/form-data">
	
	 
  
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>Banner顺序：</label>
        <div class="controls">
            <input id="orderBy" name="orderBy" class="easyui-numberspinner" style="width:80px;" required="required" missingMessage="顺序编号不能为空" data-options="min:1,max:9999,editable:false">
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>活动名称：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="活动名称不能为空"
             name="bannerName" id="bannerName" >
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>Banner跳转地址：</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="跳转地址不能为空"
             name="httpUrl" id="httpUrl" >
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>Banner图片：</label>
        <div class="controls">
            <input type="file" style="width: 200px" name="imageSrc" id="imageSrc">
        </div>
    </div>
    
    
    
    <div class="control-group">
        <label class="control-label">状态：</label>
        <div class="controls">
            <input type="radio" id="state" name="state" value="1" >上线&nbsp;&nbsp;
            <input type="radio" id="state" name="state" value="0" checked="checked">关闭
        </div>
    </div>
    
    <input type="hidden" id="appType" name="appType" value="4" >
      <div class="control-group">
        <label class="control-label">发布时间：</label>
        <div class="controls">
            <input type="radio" id="state" name="publishState" value="1" >立即&nbsp;&nbsp;
            </br>
            <input type="radio" id="state" name="publishState" value="0" checked="checked">定时
            <input type="text" class="easyui-datebox" editable="false" style="width: 150px" name="publishDate"    id="searchBeginTime">
        </div>
    </div>
   			<!-- <input type="hidden" id="appType" name="appType" value="4" >微信状态
			<input type="hidden" id="httpMethod" name="httpMethod" value="1">
			是否传UserToken：
			<input type="hidden" id="httpIsToken" name="httpIsToken" value="1">
			分享标题：：
			<input type="hidden" id="title" name="title" value="1">
			分享文案：
			<input type="hidden" id="desc" name="desc" value="1">
			分享链接：
			<input type="hidden" id="link" name="link" value="1">
			监控分享链接：
			<input type="hidden" id="shareCloseUrl" name="shareCloseUrl" value="1">
			监控活动关闭链接：
			<input type="hidden" id="closeUrl" name="closeUrl" value="1">
			分享结果回调地址：
			<input type="hidden" id="shareBackUrl" name="shareBackUrl" value="1">
			分享结果回调地址：
			<input type="hidden" id="imgUrl" name="imgUrl" value="1"> -->
		</form>
</div>
<script language="javascript" >
	// 执行：保存操作
	$("#addAppBanner_form").form({
		url : '${ctx}/jsp/app/wechat_banner_edit?action=add',
		dataType : 'json',
		onSubmit : function() {
			return $(this).form('validate');
		},
		success : function(data) {
			var json = eval('(' + data + ')');
			if (json.result == 'success') {
				$.messager.alert("系统提示", "操作成功!", "info");
				parent.$("#addAppBanner").dialog("close");
				parent.$("#AppBannerList_list").datagrid("reload");
			} else if (json.result == 'error') {
				if (json.errCode == 'check') {
					$.messager.alert("验证提示", json.errMsg, "info");
				} else {
					$.messager.alert("系统提示", json.errMsg, "warning");
				}
			} else {
				$.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
			}
		}
	});
	
	 /*  var beginTime=$("#publishDate1").datebox("getValue");  
       console.log( "======\t "+beginTime); */
</script>
</body>
</html>