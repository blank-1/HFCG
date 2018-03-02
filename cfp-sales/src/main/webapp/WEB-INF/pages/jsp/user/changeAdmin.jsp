<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<div id="changeAdmin" class="container-fluid" style="padding: 5px 0px 0px 10px">


    <form class="form-horizontal" id="changeAdmin_form" method="post" >
        <input type="hidden" name="userId" value="${userInfo.userId}"/>

        <div class="control-group">
            <label class="control-label" style="padding-top: 0;">用户名：</label>
            <div class="controls" >${userInfo.loginName}
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" style="padding-top: 0;">用户姓名：</label>
            <div class="controls" >${userInfoExt.realName}
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" style="padding-top: 0;">手机号：</label>
            <div class="controls" >${userInfo.mobileNo}
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" style="padding-top: 0;">现归属客服：</label>
            <div class="controls" >${salesAdmin.displayName}(工号：${salesAdmin.loginName})
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" style="padding-top: 0;">变更为：</label>
            <div class="controls">
                <select name="adminId">
                    <c:forEach items="${allSalesAdmin}" var="admin">
                        <option value="${admin.adminId}">${admin.displayName}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </form>
</div>
</body>
</html>