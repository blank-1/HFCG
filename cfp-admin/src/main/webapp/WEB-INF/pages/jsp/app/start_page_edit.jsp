<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="editStartPage" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="editStartPage_form" method="post" enctype="multipart/form-data">
	<input type="hidden" name="appStartPageId" id="appStartPageId" value="${appStartPage.appStartPageId }"/>
	
	<div class="control-group">
        <label class="control-label">APP类型</label>
        <div class="controls">
            <input type="text" style="width: 200px" disabled="disabled" 
            name="appType" id="appType" value="${appStartPage.appType==2?'IOS':'ANDROID' }">
        </div>
    </div>
	
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>标题</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="标题不能为空"
                   name="pageTitle" id="pageTitle" value="${appStartPage.pageTitle }">
        </div>
    </div>

    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>图片</label>
        <div class="controls">
            <input type="file" style="width: 200px" name="picPath" id="picPath">
        </div>
    </div>
    
</form>
</div>
<script language="javascript" >

	// 执行：保存操作
    $("#editStartPage_form").form({
        url:'${ctx}/jsp/app/start_page_edit',
        dataType:'json',
        onSubmit:function() {
             return $(this).form('validate');
        },
        success:function(data) {
            var json = eval('(' + data + ')'); 
            if(json.result == 'success'){
            	 $.messager.alert("系统提示", "操作成功!", "info");
                 parent.$("#editStartPage").dialog("close");
                 parent.$("#AppStartPageList_list").datagrid("reload");
   	    	}else if(json.result == 'error'){
   	    		if(data.errCode == 'check'){
                    $.messager.alert("验证提示", json.errMsg, "warning");
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