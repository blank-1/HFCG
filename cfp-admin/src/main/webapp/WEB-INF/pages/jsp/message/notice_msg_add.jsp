<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="notice_msg_add" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="notice_msg_add_form" method="post" >

    <div class="control-group">
    
        <label class="control-label">公告标题:</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="标题不能为空"
                   name="msgName" >
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">公告正文:</label>
        <div class="controls">
            <textarea name="msgContent" autocomplete="off" class="textbox-text" placeholder="" style="margin-left: 0px; margin-right: 0px; height: 300px; width: 300px;"></textarea>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >有效期:<span style="color: red;">*</span></label>
        <div class="controls">
         	<input id="endTime" name="endTime" class="easyui-datebox"></input>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >是否置顶:<span style="color: red;">*</span></label>
         <div class="controls">
         	<input  type="radio" name="topSign" value="1" data-options="required:true" checked="checked">否
         	<input  type="radio" name="topSign" value="0" data-options="required:true">是
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">发送人:</label>
        <div class="controls">
            <select name="senderName" class="easyui-combobox" editable="false" style="width: 120px"><option value="Arabic">Arabic</option><option value="Bulgarian">Bulgarian</option><option value="Catalan">Catalan</option></select>
        </div>
    </div>
    
</form>
</div>
<script language="javascript" >
	// 执行：保存操作
    $("#notice_msg_add_form").form({
        url:'${ctx}/jsp/message/station_notice_msg_add',
        onSubmit:function() {
             return $(this).form('validate');
        },
        success:function(data) {
        	
            if (data == "success") {
                $.messager.alert("系统提示", "操作成功!", "info");
                parent.$("#notice_msg_add").dialog("close");
                parent.$("#msg_msgsList_list").datagrid("reload");
            }else if(data == "error") {
            	$.messager.alert("系统提示", "操作失败!", "info");
            }
        }
    })
    
   
</script>
</body>
</html>