<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addBondSource" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="addBindSourceUser_form" method="post" action="${ctx}/bondSource/saveUser">
	<input type="hidden" name="bondSourceId" id="bondSourceId" value="${bondSourceId}"/>
	<input type="hidden" name="userId" id="userId" value="${bondSourceUser.userId}"/>
	<input type="hidden" name="userSourceId" id="userSourceId" value="${bondSourceUser.userSourceId}"/>

    <div class="control-group">
    
        <label class="control-label">原始债权人姓名：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="原始债权人姓名"
                   name="bondName" id="bondName" value="${bondSourceUser.bondName}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >原始债权人用户名：</label>
        <div class="controls">
            <input type="text" style="width: 200px;"
                   class="easyui-validatebox" required="true" missingMessage="原始债权人用户名"
                   validtype="userName"
                   name="loginName" id="locatioin" value="${bondSourceUser.loginName}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">联系电话：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="联系电话不能为空"
                   validtype="mobile"  invalidMessage="联系电话格式错误"
                   name="mobileNo" id="contactPhone" value="${bondSourceUser.mobileNo}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">身份证号：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="身份证号"
                   validtype="idCard"
                   name="idCard" id="" value="${bondSourceUser.idCard}"><span style="color: red">*</span>
        </div>
    </div>
  
    <div class="control-group" id="status">
        <label class="control-label">状态：</label>
        <div class="controls">
        	<div id="removeWhenUpdate">
        	<select id="adminState" class="easyui-combobox" name="status" style="width:200px;">
				<option value="0" <c:if test="${bondSourceUser.status eq '0'}">selected="selected"</c:if> >正常</option>
				<option value="2" <c:if test="${bondSourceUser.status eq '2'}">selected="selected"</c:if>>禁用</option>
			</select>
			</div>
        </div>
    </div>

</form>
</div>
<script language="javascript" >

</script>
</body>
</html>