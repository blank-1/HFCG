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
    

    <div class="control-group">
        <label class="control-label">联系电话：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="联系电话不能为空"
                   validtype="mobile"  invalidMessage="联系电话格式错误"
                   name="mobileNo" id="contactPhone" value="${bondSourceUser.mobileNo}"><span style="color: red">*</span>
        </div>
    </div>

  


</form>
</div>
<script language="javascript" >

</script>
</body>
</html>