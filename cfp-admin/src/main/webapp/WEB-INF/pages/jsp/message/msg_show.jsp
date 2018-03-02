<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<form class="form-horizontal"  method="post" style='width: 560px;display:block;word-break: break-all;word-wrap: break-word;'>
    <div class="control-group">
        <label class="control-label">标题:</label>

        <div class="controls-text">
            ${userMessage.name}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">消息正文:</label>

        <div class="controls-text">
            ${userMessage.content}&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">发送至:</label>

        <div class="controls-text">
            <c:if test="${userMessage.reciverType == 0}">全部用户</c:if>
    		<c:if test="${userMessage.reciverType == 1}">借贷用户</c:if>
    		<c:if test="${userMessage.reciverType == 2}">出借用户</c:if>
    		<c:if test="${userMessage.reciverType == 3}">${userMessage.loginName}</c:if>&nbsp;
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">发送人:</label>

        <div class="controls-text" id="itemTypeId">
            ${userMessage.senderName}&nbsp;
        </div>
    </div>
 
</form>
<script language="javascript" >

</script>
</body>
</html>