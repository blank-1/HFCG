<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>

<!-- Modal -->
<%@include file="../../common/header.jsp" %>
<div style="width: 100%;margin-left: 10px;">用户管理>>用户注册</div>
<c:if test="${type eq '1'}">
    <div id="login_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">用户注册成功</h3>
        </div>
        <div class="modal-body">

            <div class="control-group">
                <label class="control-label" >用户已注册成功！</label>
            </div>


        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" id="regist">&nbsp;&nbsp;继续录入&nbsp;&nbsp;</button>
            <button class="btn btn-primary" id="detail" onclick="showUserInfoDetail('${user.userId}')">&nbsp;&nbsp;查看客户信息&nbsp;&nbsp;</button>
        </div>
    </div>
</c:if>
<c:if test="${type ne '1'}">
    <div id="login_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">用户注册失败</h3>
        </div>
        <div class="modal-body">

            <div class="control-group">
                <label class="control-label" >该手机号已被注册，不能重复使用！</label>
            </div>


        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" id="regist">&nbsp;&nbsp;继续录入&nbsp;&nbsp;</button>

        </div>
    </div>
</c:if>


<script>


    function showUserInfoDetail(userId){
        $("#login_modal").after("<div id='showUser' style=' padding:10px; '></div>");
        $("#showUser").dialog({
            resizable: false,
            title: '客户信息',
            href: '${ctx}/user/detail?userId='+userId,
            width: 800,
            height: 350,
            modal: true,
            top: 200,
            left: 300,
            buttons: [
                {
                    text: '关闭',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $("#showUser").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }

    $('#login_modal').modal({
        show:true,
        backdrop:false
    })

    $("#regist").click(function(){

        window.location.href = "${ctx}/user/regist";

    });


    //session超时，跳转登录页
    if('${timeout}' == "true") {
    	window.location.href="${basePath}?time=out";
    }
    
    //session失效提示
    var url = location.search;
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("=");
        if(strs[1] == "out") {
    		$.messager.alert("系统提示", '登录超时，请重新登录！', "info");
        }
    }
</script>
</body>
</html>
