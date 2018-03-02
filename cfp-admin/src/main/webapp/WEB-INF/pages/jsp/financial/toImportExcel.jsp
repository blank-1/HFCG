<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>

</head>
<body>
<iframe name="_test" style="display:none;"></iframe>
<div id="" class="container-fluid" style="padding: 5px 0px 0px 10px">

    <form method="post" action="${ctx}/withdraw/importExcel" id="importExcel" enctype="multipart/form-data" target="_test">
        <div class="control-group">
            <label class="control-label" style="padding-top: 0;">选择导入文件：</label>
            <div class="controls" ><input type="file" name="importFile"/>
            </div>
        </div>
    </form>
</div>
</body>
</html>