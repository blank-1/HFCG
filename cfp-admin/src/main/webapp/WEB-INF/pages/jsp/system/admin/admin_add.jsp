<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addAdmin" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="admin_addAdmin_form" method="post">
	<input type="hidden" name="addFlag" id="addFlag" value="${add}"/>
	<input type="hidden" name="adminId" id="adminId" value="${adminId}"/>
	<!-- for 员工匹配角色 -->
	<input type="hidden" name="departmentInfo" id="departmentInfo"/>
	
    <div class="control-group">
    
        <label class="control-label">工号<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="工号不能为空"
                   name="adminCode" id="adminCode" value="${adminInfo.adminCode}">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" style="display:none;">登录密码<span style="color: red;">*</span></label>
        <div class="controls">
            <input type="password" style="width: 200px;display:none;"
                   name="loginPwd" id="loginPwd" value="">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">姓名<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-validatebox" required="true" missingMessage="姓名不能为空"
                   name="displayName" id="displayName" value="${adminInfo.displayName}">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">邮箱<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" invalidMessage="邮箱格式错误" validType="email" required="true" missingMessage="邮箱不能为空"
                   name="email" id="email" value="${adminInfo.email}">
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">手机<span style="color: red">*</span></label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="手机号不能为空"
                   name="telephone" id="telephone" value="${adminInfo.telephone}">
        </div>
    </div>
  
    <div class="control-group" id="showAdminState">
        <label class="control-label">状态<span style="color: red">*</span></label>
        <div class="controls">
        	<div id="removeWhenUpdate">
        	<select id="adminState" class="easyui-combobox" name="adminState" style="width:200px;"> 
				<option value="1">有效</option> 
				<option value="2">无效</option> 
			</select>
			</div>
        </div>
    </div>
    
    <!-- 员工匹配角色 begin-->
    <div class="control-group">
	    <label class="control-label">角色<span style="color: red">*</span></label>
	    <div class="controls">
	    	<div id="dept_toolbar" style="height:auto">
		        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="appendIcon();">新增</a>
		        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="removeIcon();">删除</a>
		    </div>
		    <table id="department_list">
		        <thead>
		        <tr>
		        	<th data-options="checkbox:true"></th>
		        	
		        	<th data-options="field: 'userRoleId',hidden:true,editor: {
		           		type: 'text'
		            }"></th>
		       
		            <th data-options="field: 'roleId', width: 100,editor: {
				        type: 'combobox',
				        options: {
				        	url:'${ctx}/jsp/system/role/findRoleNames',
							valueField:'id', 
							textField:'text',
				            editable:false,
				            required: true,
				            panelHeight:'auto'
				        }
				    }">角色</th>
		        </tr>
		        </thead>
		    </table>
	    </div>
    </div>
    <!-- 员工匹配角色 end-->
</form>
</div>
<script language="javascript" >
	var editIndex = undefined;
	$("#admin_addAdmin_form #department_list").datagrid({
	    singleSelect: true,
	    rownumbers: false,
	    pagination: false,
	    width: 520,
	    height: 200,
	    toolbar: 'dept_toolbar',
	    onLoadSuccess:function(data){  
	        var rows = $('#department_list').datagrid('getRows');
	        $.each(rows,function(key,value){
	        	$('#department_list').datagrid('beginEdit',  $('#department_list').datagrid('getRowIndex',value));
	        })
	        
	    }  //获取加载回来的数据，循环每列，并回显选中
	
	})

	function init(){
		if(${add}) {
			$('#showAdminState').css("display","none");
		}
		//4update回显
    	if(${add}==false){
    		$('#adminCode').attr("readonly","readonly");
    		$('#loginPwd').attr("value","${adminInfo.loginPwd}");
    		
    		//状态栏的默认选中
    		if('${adminInfo.adminState}'==1)
    			$('#adminState').val(1).attr("selected","selected");
    		else if('${adminInfo.adminState}'==2)
    			$('#adminState').val(2).attr("selected","selected");
    		
    		
    		//部门权限回显
    		$("#admin_addAdmin_form #department_list").datagrid("options").url=
    			'${ctx}/jsp/system/role/showAdminRole?adminId=' + '${adminInfo.adminId}';
    	}
		
		$.extend($.fn.validatebox.defaults.rules, { 
			cardLength: { 
			validator: function(value, param){
			return value.length == param[0]; 
			}, 
			message: 'length wrong' 
			} 
		});
	}
	
	// 执行：保存操作
    $("#admin_addAdmin_form").form({
        url:'${ctx}/jsp/system/admin/saveAdmin',
        onSubmit:function() {
        	 endEditing();
        	 $('#department_list').datagrid("acceptChanges");
             $("#departmentInfo").val(JSON.stringify($("#department_list").datagrid("getData")));
             
             return $(this).form('validate');
        },
        success:function(data) {
        	
            if (data == "success") {
                $.messager.alert("系统提示", "操作成功!", "info");
                parent.$("#addAdmin").dialog("close");
                parent.$("#admin_adminsList_list").datagrid("reload");
            }else if(data == "exist") {
            	$.messager.alert("系统提示", "工号已存在，请更换!", "info");
            }else if(data == "error") {
            	$.messager.alert("系统提示", "操作失败!", "info");
            }else if(data == "emailExist") {
            	$.messager.alert("系统提示", "邮箱已存在，请更换!", "info");
            }else if(data == "telPhoneExist") {
            	$.messager.alert("系统提示", "手机号已存在，请更换!", "info");
            }
            
            $("#department_list").datagrid("reload");
           	var rows = $('#department_list').datagrid('getRows');
   	        $.each(rows,function(key,value){
   	        	$('#department_list').datagrid('beginEdit',  $('#department_list').datagrid('getRowIndex',value));
   	        })
    	        
        }
    })
    
    function appendIcon() {
        $('#department_list').datagrid('appendRow', {
        });
        editIndex = $('#department_list').datagrid('getRows').length - 1;
        $('#department_list').datagrid('selectRow', editIndex)
                .datagrid('beginEdit', editIndex);
    }

    function removeIcon() {
        var selRow = $("#department_list").datagrid("getSelected");
        if (selRow) {
            $.messager.confirm("系统提示", "是否确定删除该行？", function (data) {
                if (data) {
                    $("#department_list").datagrid("deleteRow",
                            $("#department_list").datagrid("getRowIndex", selRow));
                }
            })
        } else {
            $.messager.alert("系统提示", "请选择要删除的行!", "info");
        }
    }
    
    function endEditing() {
        if (editIndex == undefined) {
            return true;
        }
        if ($('#department_list').datagrid('validateRow', editIndex)) {
        	var userRole = $('#department_list').datagrid('getEditor', {index: editIndex, field: 'userRoleId'});
            var role = $('#department_list').datagrid('getEditor', {index: editIndex, field: 'roleId'});
            
            if(userRole != null) {
            	$('#department_list').datagrid('getRows')[editIndex]['userRoleId'] = $(userRole.target).text();
            }
            if(role != null) {
            	$('#department_list').datagrid('getRows')[editIndex]['roleId'] = $(role.target).combobox('getText');;
            }
            return true;
        }else {
            return false;
        }
    }
    
    $(function(){
        init();
    });
</script>
</body>
</html>