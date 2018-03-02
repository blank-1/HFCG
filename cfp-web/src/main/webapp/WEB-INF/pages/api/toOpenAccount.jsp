<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="/WEB-INF/pages/common/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <!-- <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">  -->
    <title>关于我们 - 财富派</title>
    <script type="text/javascript" src="${ctx }/js/jquery.min.js"></script>
</head>

<body>
<div>
    <form action="/api/openAccount/doOpenAccount" method="post">

        用户身份证：<input type="text" name="certif_id" value="130723198710081215"/><br/>
        卡号：<input type="text" name="capAcntNo" value="6226221205644755"/><br/>
        <%--用户登录名：<input type="text" name="cust_no" value="15701612615"/>--%>
        用户姓名：<input type="text" name="cust_nm" value="李小峰"/>
        <button type="submit" >Go</button>
    </form>
    <hr />
    <form action="/api/openAccount/doOpenAccount2" method="post">
        用户身份证：<input type="text" name="certif_id" value="130723198710000015"/><br/>
        卡号：<input type="text" name="capAcntNo" value="6226221205600015"/><br/>
        企业名称：<input type="text" name="cust_nm" value="李小峰企业"/>
        法人：<input type="text" name="artif_nm" value="李小峰"/>
        法人手机号：<input type="text" name="mobile_no" value="15555550000"/>
        <button type="submit" >Go</button>
    </form>
</div>
<div style="clear:both;"></div>
<script type="text/javascript">

</script>
</body>
</html>
