<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>
<%@include file="../../common/header.jsp" %>
<div style="width: 100%;margin-left: 10px;">用户管理>>用户信息</div>

    <div class="cf" style="float:left; width:900px;margin:0 10px 5px 10px;">
        <fieldset style="height:115px">
            <legend>查询条件</legend>
            <form name="userQuery" id="userQuery" action="${ctx}/user/userList" class="fitem"
                  autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>用户名：</label>
                        </td>
                        <td align="center"><input id="loginName"
                                   name="loginName" value=""
                                   type="text" />
                        </td>

                        <td nowrap="nowrap"><label>用户手机：</label>
                        </td>
                        <td align="center"><input id="mobileNo"
                                                  name="mobileNo" value=""
                                                  type="text" />
                        </td>

                        <td nowrap="nowrap"><label>用户姓名：</label>
                        </td>
                        <td align="center"><input id="realName"
                                                  name="realName" value=""
                                                  type="text" />
                        </td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap"><label>员工编号：</label>
                        </td>
                        <td align="center"><input id="salesAdminCode"
                                                  name="salesAdminCode" value=""
                                                  type="text" />
                        </td>

                        <td nowrap="nowrap"><label>员工姓名：</label>
                        </td>
                        <td align="center"><input id="salesAdminName"
                                                  name="salesAdminName" value=""
                                                  type="text" />
                        </td>

                        <td nowrap="nowrap"><label>所属团队：</label>
                        </td>
                        <td align="center"><input id="superiorsOrganize"
                                                  name="superiorsOrganize" value=""
                                                  type="text" />
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryUser();" iconCls="icon-search">查询</a>

                                <mis:PermisTag code="10001">
                                    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="goto('${ctx}/user/regist', '用户管理>>用户注册')"
                                       iconCls="icon-add">注册</a>
                                </mis:PermisTag>
                            </div>


                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="user_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="user_list_toolbar" style="height:auto">

        </div>

        <table id="user_list_list"></table>
    </div>

    <div id="detailuser">

    </div>
    <script language="javascript">

        function toQueryUser(){
            $('#user_list_list').datagrid('load', {
                'loginName' : Trim($("#loginName").val(),"g"),
                'mobileNo' : Trim($("#mobileNo").val(),"g"),
                'realName' : Trim($("#realName").val(),"g"),
                'salesAdminCode' : Trim($("#salesAdminCode").val(),"g"),
                'salesAdminName' : Trim($("#salesAdminName").val(),"g"),
                'superiorsOrganize' : Trim($("#superiorsOrganize").val(),"g")
            });
        }
        /**
         * 执行：列表加载。
         */
        function loadList(){
            $("#user_list_list").datagrid({
                idField: 'userId',
                title: '客户列表',
                url: '${ctx}/user/list',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                columns:[[
                    {field:'loginName', width:130,title:'用户名',formatter:function(value,row,index){
                        var result ="";

                        result += '<a href="#" onclick="showUserInfoDetail('+row.userId+')">'+row.loginName+'</a> &nbsp;';

                        return result;
                    }},
                    {field:'mobileNo', width:120,title:'用户手机'},
                    {field:'realName', width:80,title:'用户姓名'},
                    {field:'idCardNoStr', width:150,title:'用户身份证号'},
                    {field:'createTime', width:130,title:'注册时间',formatter:dateTimeFormatter},
                    {field:'lastLoginDate', width:130,title:'最后登陆时间',formatter:dateTimeFormatter},
                    {field:'salesAdminLoginName', width:120,title:'员工编号'},
                    {field:'salesAdminName', width:120,title:'员工姓名'},
                    {field:'superiorsOrganize', width:120,title:'所属团队'},
                    {field:'superiorsAdmin', width:80,title:'团队经理'},
                    {field:'action',title:'操作',width:100,align:'center',
                        formatter:function(value,row,index){
                            var result ="";
                            <mis:PermisTag code="010003">
                                result += "<a class='label label-info' onclick='changeAdmin(" + row.userId + ")'>客服变更</a> &nbsp;";
                            </mis:PermisTag>
                            return result;
                        }
                    }
                ]]
            });

        }

        function changeAdmin(userId){
            $("#user_list").after("<div id='changeAdmin' style=' padding:10px; '></div>");
            $("#changeAdmin").dialog({
                resizable: false,
                title: '客户变更',
                href: '${ctx}/user/changAdmin?userId='+userId,
                width: 450,
                height: 300,
                modal: true,
                top: 200,
                left: 300,
                buttons: [
                    {
                        text: '提交',
                        iconCls: 'icon-ok',
                        handler: function () {
//                            $("#changeAdmin").dialog('close');
                            var form = $("#changeAdmin").contents().find("#changeAdmin_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.ajax({
                                    url:'${ctx}/user/change',
                                    data:form.serialize(),
                                    type:"POST",
                                    success:function(msg){
                                        if(msg=='success'){
                                            $.messager.alert('提示', '保存成功！！', 'info',function(){
                                                $("#changeAdmin").dialog('close');
                                                toQueryUser();
                                            });
                                        }else
                                            $.messager.alert('提示', '保存失败！！'+msg, 'info');
                                    }
                                });
                            }
                        }

                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#changeAdmin").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        function showUserInfoDetail(userId){
            $("#user_list").after("<div id='showUser' style=' padding:10px; '></div>");
            $("#showUser").dialog({
                resizable: false,
                title: '客户信息',
                href: '${ctx}/user/detail?userId='+userId,
                width: 800,
                height: 750,
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


        /**
         * 详情：弹出详情层。
         */
        function showDetails(id){
            $("#detailBondSource").after("<div id='detailBondSource_1' style=' padding:10px; '></div>");
            $('#detailBondSource_1').dialog({
                title : '渠道详情',
                href: '${ctx}/bondSource/detail?bondSourceId='+id,
                width: 1200,
                height: 620,
                maximizable : true,
                minimizable : false,
                collapsible : false,
                modal:true,
                resizable : true,
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        //格式化时间
        function dateTimeFormatter(val) {

            if (val == undefined || val == "")
                return "";
            var date;
            if(val instanceof Date){
                date = val;
            }else{
                date = new Date(val);
            }
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();

            var h = date.getHours();
            var mm = date.getMinutes();
            var s = date.getSeconds();

            var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
                    + (d < 10 ? ('0' + d) : d);
            var TimeStr = h + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
                    + (s < 10 ? ('0' + s) : s);

            return dateStr + ' ' + TimeStr;
        }
        $(function(){
            loadList();
        });
    </script>

</body>