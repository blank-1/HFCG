<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
<%@include file="../../common/header.jsp" %>
<div style="width: 100%;margin-left: 10px;">用户管理>>用户注册</div>
<!-- Modal -->

    <div id="login_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-header">
            <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>--%>
            <h3 id="myModalLabel">用户注册</h3>
        </div>
        <div class="modal-body">
            <form class="form-horizontal" id="loginForm" target="_self" action="${ctx}/user/registUser" method="post">
                <div class="control-group">
                    <label class="control-label" for="phone">用户手机号</label>
                    <div class="controls">
                        <input type="text" id="phone" name="phone" placeholder="用户手机号" value="">
                    </div>
                </div>

            </form>

        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" id="regist">&nbsp;&nbsp;注册&nbsp;&nbsp;</button>
            <button class="btn btn-primary" id="clear">&nbsp;&nbsp;清空&nbsp;&nbsp;</button>
        </div>
    </div>
<script>
    $('#login_modal').modal({
        show:true,
        backdrop:false
    })

    $("#regist").click(function(){
        $.ajax({
            url:'${ctx}/user/registUser',
            data: $("#loginForm").serialize(),
            type:"POST",
            success:function(d){
                var phone = $("#phone").val();
                if(null != d && "" != d){
                    if(d.result == 'success'){
                        if(d.data!=null)
                           window.location.href =  '${ctx}/user/registSuccess?phone='+phone+'&type=0';
                        else
                            window.location.href = "${ctx}/user/registSuccess?phone="+phone+"&type=1";
                    }else if(d.result == 'error'){
                        $.messager.alert('提示', d.errMsg,'info');
                    }

                }else{
                    $.messager.alert('提示', '用户注册失败！','info');
                }
            }
        });
    });
    $("#clear").click(function(){
        $("#phone").val("");
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
