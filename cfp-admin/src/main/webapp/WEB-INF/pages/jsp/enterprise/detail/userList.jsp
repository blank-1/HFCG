<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<body>
    <div id="enterprise_user" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="enterprise_user_list_toolbar" style="height:auto">
        	<div class="container-fluid" style="padding: 5px 0px 0px 10px">
        		<form action="${ctx}/jsp/enterprise/userList" method="post" class="form-inline">
                    <label>姓名：</label>
                    <input id="realName" name="realName" type="text" />
                    
                    <label>用户名：</label>
                    <input id="loginName" name="loginName" type="text" />
                    
                    <label>状态：</label>
                    <select class="easyui-combobox" id="status" style="width:150px" name="status" >
                        <option value="" >全部</option>
                        <option value="0">正常</option>
                        <option value="2">禁用</option>
                    </select>
                 
                    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="doSearch();" iconCls="icon-search">查询</a>
                    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addUser();">新增</a>
	                
            	</form>
        	</div>
        </div>

        <table id="enterprise_user_list"></table>
    </div>

    <script language="javascript">
        function doSearch(){
            $("#enterprise_user_list").datagrid('reload', {
                'realName' : $("#realName").val(),
                'loginName' : $("#loginName").val(),
                'enterpriseId' : ${enterpriseId},
                'status' : $("#status").combobox("getValue")
            });
        }
        
        /**
         * 执行：列表加载。
         */
        function loadUserList(){
            $("#enterprise_user_list").datagrid({
            	idField: 'enterpriseUserId',
                title: '企业人员列表',
                url: '${ctx}/jsp/enterprise/userList?enterpriseId=' + ${enterpriseId},
                pagination: true,
                pageSize: 10,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#enterprise_user_list_toolbar',
                columns:[[
                	{field:'userId', width:120, hidden:'true'},
                    {field:'realName', width:120,title:'姓名'},
                    {field:'loginName', width:120,title:'用户名'},
                    {field:'availValue', width:80,title:'可用金额'},
                    {field:'frozeValue', width:80,title:'冻结金额'},
                    {field:'value', width:80,title:'账户余额'},
                    {field:'borrowCount', width:80,title:'借款个数'},
                    {field:'mobileNo', width:100,title:'联系电话'},
                    {field:'email', width:150,title:'邮箱'},
                    {field:'status', width:60,title:'状态'},
                    {field:'action',title:'操作',width:100,align:'center',
                        formatter:function(value,row,index){
                            var result = "";
                            result += "<a class='label label-info' onclick='editUser(" + row.enterpriseUserId + ")'>编辑</a> &nbsp;";
                            if(row.status == '0')
                                result += "<a class='label' onclick='delUser(" + row.userId + ")'>禁用</a> &nbsp;";
                            else
                                result += "<a class='label label-important' onclick='startUser(" + row.userId + ")'>启用</a> &nbsp;";
                            return result;
                        }
                    }
                ]],
                onBeforeLoad: function (value, rec) {
                    var adminState = $(this).datagrid("getColumnOption", "status");
                    if (adminState) {
                        adminState.formatter = function (value, rowData, rowIndex) {
                            if (value == '0') {
                                return "<font style='color: green;'>正常</font>";
                            } else if(value == '2') {
                                return "<font style='color: red;'>禁用</font>";
                            }
                        }
                    }
                }
            });
        }


        /**
         * 禁用
         * */
        function delUser(id){
            $.ajax({
                url:'${ctx}/bondSource/deleteUser?userId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                        	doSearch();
                        });
                    }else {
                        $.messager.alert('提示', '操作失败！！', 'info');
                    }
                }
            });
        }

        /**
         * 解禁
         * */
        function startUser(id){
            $.ajax({
                url:'${ctx}/bondSource/free?userId='+id,
                type:"POST",
                success:function(msg){
                    if(msg=='success'){
                        $.messager.alert('提示', '操作成功！！', 'info',function(){
                        	doSearch();
                        });
                    }else {
                        $.messager.alert('提示', '操作失败！！', 'info');
                    }
                }
            });
        }

        /**
         * 执行：弹出添加层。
         */
        function addUser() {
            $("#enterprise_user_list").after("<div id='addUser' style=' padding:10px; '></div>");
            $("#addUser").dialog({
                resizable: false,
                title: '新增企业用户',
                href: '${ctx}/jsp/enterprise/toAddUser?enterpriseId=' + ${enterpriseId},
                width: 500,
                height: 300,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var form = $("#addUser").contents().find("#addUser_form");
                            var validate = form.form('validate');
                            if(validate){
                                $.post(
                                    '${ctx}/jsp/enterprise/saveUser',
                                    form.serialize(),
                                    function(data){
                                        if(data.result == 'success'){
                 	                       $.messager.alert("操作提示", "操作成功！", "info");
                 	                       $('#enterprise_user_list').datagrid('reload');
                 	                   }else if(data.result == 'error'){
                 	                       if(data.errCode == 'check'){
                 	                           $.messager.alert("验证提示", data.errMsg, "info");
                 	                       }else{
                 	                           $.messager.alert("系统提示", data.errMsg, "warning");
                 	                       }
                 	                   }else{
                 	                       $.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
                 	                   }
                                    },'json');
                                $("#addUser").dialog('close');
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addUser").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }
        
        /**
         * 执行：修改添加层。
         */
        function editUser(enterpriseUserId) {
            $("#enterprise_user_list").after("<div id='addUser' style=' padding:10px; '></div>");
            $("#addUser").dialog({
                resizable: false,
                title: '编辑企业用户',
                href: '${ctx}/jsp/enterprise/toEditUser?enterpriseId=' + ${enterpriseId} + '&enterpriseUserId=' + enterpriseUserId,
                width: 500,
                height: 300,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-ok',
                        handler: function () {
                            var form = $("#addUser").contents().find("#addUser_form");
                            var validate = form.form('validate');
                            if(validate){
                            	$.post(
                                    '${ctx}/jsp/enterprise/saveUser',
                                    form.serialize(),
                                    function(data){
                                        if(data.result == 'success'){
                 	                       $.messager.alert("操作提示", "操作成功！", "info");
                 	                       $('#enterprise_user_list').datagrid('reload');
                 	                   }else if(data.result == 'error'){
                 	                       if(data.errCode == 'check'){
                 	                           $.messager.alert("验证提示", data.errMsg, "info");
                 	                       }else{
                 	                           $.messager.alert("系统提示", data.errMsg, "warning");
                 	                       }
                 	                   }else{
                 	                       $.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
                 	                   }
                                    },'json');
                            	$("#addUser").dialog('close');
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addUser").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        $(function(){
            loadUserList();
        });
    </script>

</body>