<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="addGuaranteeCompany" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="addGuaranteeCompany_form" method="post" action="${ctx}/bondSource/save">
	<input type="hidden" name="companyId" id="companyId" value="${guaranteeCompany.companyId}"/>
	<input type="hidden" name="userId" id="accId" value="${guaranteeCompany.userId}"/>

    <div class="control-group">
    
        <label class="control-label">担保公司名称：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="渠道名称不能为空"
                   name="companyName" id="companyName" value="${guaranteeCompany.companyName}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >担保公司所在地：</label>
        <div class="controls">
            <input type="text" style="width: 200px;" class="easyui-validatebox" required="true" missingMessage="渠道公司所在地不能为空"
                   name="companyLocation" id="companyLocation" value="${guaranteeCompany.companyLocation}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">联系人：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-validatebox" required="true" missingMessage="联系人不能为空"
                   name="contactPersion" id="contactPersion" value="${guaranteeCompany.contactPersion}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">联系电话：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox"   invalidMessage="手机号格式错误" required="true" missingMessage="手机号不能为空"
                   name="contactPhone" id="contactPhone" value="${guaranteeCompany.contactPhone}"><span style="color: red">*</span>
        </div>
    </div>
  
    <div class="control-group" id="status">
        <label class="control-label">渠道状态：</label>
        <div class="controls">
        	<div id="removeWhenUpdate">
        	<select id="adminState" class="easyui-combobox" name="status" style="width:200px;">
				<option value="0" <c:if test="${guaranteeCompany.status eq '0'}">selected="selected"</c:if> >正常</option>
				<option value="1" <c:if test="${guaranteeCompany.status eq '1'}">selected="selected"</c:if>>禁用</option>
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