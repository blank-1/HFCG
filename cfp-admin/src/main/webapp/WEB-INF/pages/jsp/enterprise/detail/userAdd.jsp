<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addUser" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="addUser_form" method="post" action="${ctx}/jsp/enterprise/saveUser">
	<input type="hidden" name="enterpriseId" id="enterpriseId" value="${enterpriseId}"/>
	<input type="hidden" name="enterpriseUserId" id="enterpriseUserId" value="${enterpriseUserId}"/>

    <div class="control-group">
    
        <label class="control-label">姓名：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="姓名为必填"
                   name="realName" id="realName" value="${realName}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >用户名：</label>
        <div class="controls">
            <input type="text" style="width: 200px;"
                   class="easyui-validatebox" required="true" missingMessage="用户名为必填"
                   validtype="userName"
                   name="loginName" id="loginName" value="${loginName}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">联系电话：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="联系电话不能为空"
                   validtype="mobile"  invalidMessage="联系电话格式错误"
                   name="mobileNo" id="mobileNo" value="${mobileNo}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">邮箱：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" 
                   validtype="email"
                   name="email" id="" value="${email}">
        </div>
    </div>
  
</form>
</div>
</body>
</html>