<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="com.xt.cfp.core.util.PropertiesUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
<c:forEach items="${result}" var="att">
  <img border=0 src="<%=PropertiesUtils.getInstance().get("PIC_PATH") %>${att.attachment.url}"  style="filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);width: 200px;height: 200px;">
  ${att.attachment.fileName}
</c:forEach>
</body>
</html>
