<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="toAddConstant" class="container-fluid" style="padding: 5px 0px 0px 10px">
<form class="form-horizontal" id="toAddConstant_form" action="${ctx}/constant/save">
	<input type="hidden" name="constantDefineId" id="constantDefineId" value="${constantDefine.constantDefineId}"/>
	<input type="hidden" name="parentConstant" id="parentConstant" value="${constantDefine.parentConstant}"/>
    <c:if test="${not empty constantDefine.constantStatus}">
        <input type="hidden" name="constantStatus" id="constantStatus" value="${constantDefine.constantStatus}"/>
    </c:if>
    <c:if test="${empty constantDefine.constantStatus}">
        <input type="hidden" name="constantStatus" id="constantStatus" value="0"/>
    </c:if>
    <div class="control-group">
    
        <label class="control-label">常量类型名称：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
             class="easyui-validatebox" required="true" missingMessage="常量类型名称不能为空"
                   name="constantType" id="constantType" value="${constantDefine.constantType}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label" >常量类型编码：</label>
        <div class="controls">
            <input type="text" style="width: 200px;" class="easyui-validatebox" required="true" missingMessage="常量类型编码不能为空"
                   name="constantTypeCode" id="constantTypeCode" value="${constantDefine.constantTypeCode}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">常量名称：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
            class="easyui-validatebox" required="true" missingMessage="常量名称"
                   name="constantName" id="constantName" value="${constantDefine.constantName}"><span style="color: red">*</span>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">常量值：</label>
        <div class="controls">
            <input type="text" style="width: 200px"
                   class="easyui-validatebox" required="true" missingMessage="常量值不能为空"
                   name="constantValue" id="constantValue" value="${constantDefine.constantValue}"><span style="color: red">*</span>
        </div>
    </div>
</form>
</div>
</body>
</html>