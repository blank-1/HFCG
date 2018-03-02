<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="admin_showAdmin">
<form class="form-horizontal" id="admin_showAdmin_form" method="post">
    
    <div class="control-group">
        <label class="control-label">工号</label>
        <div class="controls-text" id="adminCode" style="width:300px;">${adminInfo.loginName}&nbsp;</div>
    </div>
      
    <div class="control-group">
        <label class="control-label">登录密码</label>
        <div class="controls-text" id="loginPwd" style="width:300px;">
        	<a href="javascript:void(0);" class="btn btn-primary" onclick="changePwd();">&nbsp;&nbsp;重置&nbsp;&nbsp;</a>
        </div>
    </div>
    
    <div class="control-group">
        <label class="control-label">姓名</label>
        <div class="controls-text" id="displayName" style="width:300px;">${adminInfo.displayName}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">邮箱</label>
        <div class="controls-text" id="email" style="width:300px;">${adminInfo.email}&nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">手机</label>
        <div class="controls-text" id="telephone" style="width:300px;">${adminInfo.telephone}&nbsp;</div>
    </div>

    <div class="control-group">
        <label class="control-label">状态</label>
        <div class="controls-text" id="adminStateName" style="width:300px;">
        <c:if test="${adminInfo.adminState == 1}">正常</c:if>
        <c:if test="${adminInfo.adminState == 2}">禁用</c:if>
        &nbsp;</div>
    </div>
    
    <div class="control-group">
        <label class="control-label">创建时间</label>
        <div class="controls-text" id="createTime" style="width:300px;">
        <fmt:formatDate value="${adminInfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;</div>
    </div>
    
    <!-- 角色信息 -->
    <div style="padding-left:110px;padding-top:10px;">
	    <table id="adminrole_list">
	        <thead>
	        <th data-options="field:'roleName',width:150">角色</th>
	        <th data-options="field:'roleDesc',width:250">描述</th>
	        </thead>
	    </table>
    </div>
</form>
</div>

<script language="javascript" >
	/**
	 * 执行：角色列表。
	 */
	function loadList() {
	    $("#adminrole_list").datagrid({
	        title: '角色信息',
	        url: '${ctx}/jsp/system/role/showAdminRole?adminId=' + '${adminInfo.adminId}',
	        width: 474
	    });
	}
	
	// 密码重置
	function changePwd() {
		$.post('${ctx}/jsp/system/admin/changePwd',
       		{
				adminId:${adminInfo.adminId}
       		},
       		function(data){
       	    	if(data.result == 'success'){
       	    		$.messager.alert("操作提示", "重置成功!", "info");
       	    	}else if(data.result == 'error'){
       	    		if(data.errCode == 'check'){
       	    			$.messager.alert("验证提示", data.errMsg, "info");
       	    		}else{
       	    			$.messager.alert("系统提示", data.errMsg, "info");
       	    		}
       	    	}else{
       	    		$.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
       	    	}
       	},'json');
    }
	
	$(function(){
		loadList();
	});
</script>
</body>
</html>