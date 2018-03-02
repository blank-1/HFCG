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
        <label class="control-label"><span style="color: red">*</span>Banner图片：</label>
        <div class="controls">
            <input type="file" style="width: 200px" name="imageSrc" id="imageSrc">
        </div>
    </div>
    
 
    
    <div class="control-group">
        <label class="control-label">状态：</label>
        <div class="controls">
            <input type="radio" id="state" name="state" value="1" ${appBanner.state==1?'checked':'' }>上线&nbsp;&nbsp;
            <input type="radio" id="state" name="state" value="0" ${appBanner.state==0?'checked':'' }>关闭
        </div>
    </div>
       <div class="control-group">
        <label class="control-label">发布时间：</label>
        <div class="controls">
            <input type="radio" id="state1" name="publishState" value="1"  ${appBanner.publishState ==1?'checked':'' }>立即&nbsp;&nbsp;
            </br>
            <input type="radio" id="state0" name="publishState" value="0" ${appBanner.publishState == 0?'checked':'' }>定时
            <input type="text" class="easyui-datebox"  style="width: 150px" name="publishDate"   id="publishDate1">
        </div>
    </div>
    	<input type="hidden" id="appType" name="appType" value="4" ><!-- 微信状态 -->
    	<input type="hidden" id="publishStatehidden" name="publishStatehidden" value="${appBanner.publishState}" ><!-- 微信状态 -->
    	<input type="hidden" id="publishDateHidden" name="publishDateHidden" value="${publishDate}" ><!-- 微信状态 -->
</form>
</div>
<script language="javascript" >



	// 执行：保存操作
    $("#editAppBanner_form").form({
        url:'${ctx}/jsp/app/wechat_banner_edit?action=edit',
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
	$(function() {
		$("#publishDate1").val($("#publishDateHidden").val());
		
		var count =$("#publishStatehidden").val();
		console.log(count+"    con")
		if(0==count){
			console.log("000000")
			$("#state0").val(count);
			$("#state0").attr('checked','checked');
		}else{
			console.log("1111111");
			$("#state1").val(count);
			$("#state1").attr('checked','checked');
		
		}
		
		
	});
</script>
</body>
</html>