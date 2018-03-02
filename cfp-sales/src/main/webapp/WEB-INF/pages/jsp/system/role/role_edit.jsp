<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal" id="role_editRole_form" method="post">
	<input type="hidden" id="roleId" name="roleId" value="${info.roleId }">
    <div class="control-group">
        <label class="control-label">角色名称<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="角色名称不能为空"
                   name="roleName" id="roleName" value="${info.roleName }">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">角色编码<span style="color: red">*</span></label>

        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="角色编码不能为空"
                   name="roleCode" id="roleCode" value="${info.roleCode }">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">相关描述<span style="color: white">*</span></label>

        <div class="controls">
            <textarea class="easyui-validatebox" style="width: 200px; height: 100px;" validType="length[2,200]"
                         name="roleDesc" id="roleDesc">${info.roleDesc }</textarea>
        </div>
    </div>
</form>
<script language="javascript" >
	
	/**
	 * 执行：修改。
	 */
    $("#role_editRole_form").form({
        url:'${ctx}/jsp/system/role/edit',
        onSubmit:function() {
        	return $(this).form('validate');
        },
        success:function(data) {
            if (data == "success") {
                $.messager.alert("系统提示", "操作成功!", "info");
                parent.$("#editRole").dialog("close");
                parent.$("#role_rolesList_list").datagrid("reload");
            }else if(data == "exist") {
            	$.messager.alert("系统提示", "角色已存在!", "info");
            }else{
            	$.messager.alert("系统提示", "操作失败!", "info");
            }
        }
    })
</script>
</body>
</html>