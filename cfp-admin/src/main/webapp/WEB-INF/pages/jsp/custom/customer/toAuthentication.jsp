<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp"%>
<html>
<head>
    <title></title>
</head>
<body>
<div id="div_authentication_form" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="authentication_form" method="post" action="">
	<input type="hidden" name="userId" id="userId" value="${userId}"/>
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>姓名：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="姓名不能为空"
                   name="realName" id="realName">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label"><span style="color: red">*</span>身份证号：</label>
		<input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="身份证号不能为空"
                   name="idCard" id="idCard">
    </div>
</form>
</div>
<script language="javascript" >
$("#authentication_form").form({
    url: '${ctx}/jsp/custom/customer/customerAuthentication',
    onSubmit: function() {
        if(!isCardNo($("#idCard").val())){
            $.messager.alert("系统提示", "身份证号码不合法！", "info");
            return false;
        }
    },
	success : function(data) {
		var data = eval("(" + data + ")");
		if (data.isSuccess == true) {
			$.messager.alert("系统提示", data.info, "info");
			parent.$("#toAuthentication").dialog("close");
			parent.$("#customerList_list").datagrid("reload");
		}else{
			$.messager.alert("系统提示", data.info, "info");
		}
	}
})
</script>
</body>
</html>