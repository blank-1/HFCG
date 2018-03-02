<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=100">
    <title></title>
    <%@include file="common/common.jsp" %>
</head>
<body>
<%@include file="common/header.jsp" %>
<p id="breadcrumb" class="muted"></p>
<div id="main" style="display: block;"
     data-options="closable:false,
                collapsible:false,minimizable:false,maximizable:false">

</div>
<script>
    $("#main").panel({
        width: document.body.clientWidth * 0.985,
        height: document.body.clientHeight * 0.9,
        fit:true,
        border: 0
    })
</script>
</body>
</html>