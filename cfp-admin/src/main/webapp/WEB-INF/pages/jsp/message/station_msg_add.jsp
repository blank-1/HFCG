<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="station_msg_add" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="station_msg_add_form" method="post">

    <div class="control-group">
    
        <label class="control-label">站内信标题:</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="标题不能为空"
                   name="msgName" validType="length[0,20]">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">站内信正文:</label>
        <div class="controls">
            <textarea id="tzw" name="msgContent" autocomplete="off" class="textbox-text" placeholder="" style="margin-left: 0px; margin-right: 0px; height: 300px; width: 300px;"></textarea>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >站内信发送至:<span style="color: red;">*</span></label>
        <div class="controls">
            <input type="radio" name="reciverType" checked="checked" value="0">全体用户
            <input type="radio" name="reciverType" value="1" >借款用户
            <input type="radio" name="reciverType" value="2" >出借用户
            <input type="radio" name="reciverType" value="3" >导入用户
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">图片地址:</label>
        <div class="controls">
            <input type="text" style="width: 300px"
             class="easyui-validatebox" required="true" missingMessage="地址不能为空"
                   name="imgAddress" >
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">发送人:</label>
        <div class="controls">
            <select name="senderName" class="easyui-combobox" editable="false" style="width: 120px"><option value="财富派团队">财富派团队</option></select>
        </div>
    </div>
    
</form>
</div>
<script language="javascript" >
	// 执行：保存操作
    $("#station_msg_add_form").form({
        url:'${ctx}/jsp/message/station_notice_msg_add',
        onSubmit:function() {
        		if($("#tzw").val().length >= 150){
        			$.messager.alert("系统提示", "正文内容不能超过150个字符!", "info");
        			return false;
        		}
             return $(this).form('validate');
        },
        success:function(data) {
        	
            if (data == "success") {
                $.messager.alert("系统提示", "操作成功!", "info");
                parent.$("#station_msg_add").dialog("close");
                parent.$("#msg_msgsList_list").datagrid("reload");
            }else if(data == "error") {
            	$.messager.alert("系统提示", "操所失败!", "info");
            }
        }
    })
    
   
</script>
</body>
</html>