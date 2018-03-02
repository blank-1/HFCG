<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.cfp-sales.com/jsp/taglib" prefix="mis" %>
<div class="navbar navbar-inverse">
    <div class="navbar-inner">
        <a class="brand" href="#" style="font-weight:bold;color: #ffffff" >电话销售系统</a>
        <ul class="nav">
            <ul class="nav">
                <mis:PermisTag code="100">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            用户管理
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <mis:PermisTag code="10001">
                                <li><a href="${ctx}/user/regist">用户注册</a></li>
                            </mis:PermisTag>
                            <mis:PermisTag code="10002">
                            <li><a href="${ctx}/user/userList">用户信息</a></li>
                            </mis:PermisTag>
                        </ul>
                    </li>
                </mis:PermisTag>
                <mis:PermisTag code="0101">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            数据统计
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <mis:PermisTag code="010101">
                            <li><a href="${ctx}/phoneSell/prepaid/prepaidList">充值记录</a></li>
                            </mis:PermisTag>
                            <mis:PermisTag code="010102">
                            <li><a href="${ctx}/phoneSell/order/orderList">投资记录</a></li>
                            </mis:PermisTag>
                            <mis:PermisTag code="010103">
                            <li><a href="${ctx}/phoneSell/withdraw/withdrawList">提现记录</a></li>
                            </mis:PermisTag>
                        </ul>
                    </li>
                </mis:PermisTag>
                <mis:PermisTag code="0102">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            系统设置
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <mis:PermisTag code="010204">
                            <li><a href="${ctx}/jsp/system/admin/to_admin_list">员工管理</a></li>
                            </mis:PermisTag>
                            <mis:PermisTag code="010201">
                            <li><a href="${ctx}/jsp/system/role/to_role_list">角色管理</a></li>
                            </mis:PermisTag>
                            <mis:PermisTag code="010202">
                            <li><a href="${ctx}/jsp/system/function/to_function_list">权限管理</a></li>
                            </mis:PermisTag>
                            <mis:PermisTag code="010203">
                            <li><a href="${ctx}/jsp/system/organize/to_organize_list">组织管理</a></li>
                            </mis:PermisTag>
                        </ul>
                    </li>
                </mis:PermisTag>
            </ul>

        </ul>
        <ul class="nav pull-right" style="position: static;font-size: 12px">
            <c:if test="${empty sessionScope.currentUser}">
                <li id="loginUser" name="loginUser">
                    <a href="${ctx}/toLogin">登录</a>
                </li>
            </c:if>
            <c:if test="${not empty sessionScope.currentUser}">
                <li id="loginUser" name="loginUser">
                    <a>当前用户：<span id="displayName">${sessionScope.currentUser.displayName}</span></a>
                </li>
                <li id="loginUser" name="loginUser">
                    <a href="${ctx}/jsp/system/admin/to_edit_pass" style="cursor: pointer;" title="密码修改">修改密码</a>
                </li>
            </c:if>
            <li id="logout" name="logout">
                <a href="#" onclick="logout();">退出</a>
            </li>
        </ul>
    </div>
</div>
<div id="changePwdDiv"></div>
<script>

    // 页面跳转。
    function goto(url, title) {
        $("#breadcrumb").html(title);
        $('#main').panel('refresh', url);
    }

    // 登出。
    function logout() {
        $.post("${ctx}/logout",
                function (data) {
                    if (data == "success") {
                        window.location.href = "${ctx}/toLogin";
                    }
                },'text');
    }

</script>