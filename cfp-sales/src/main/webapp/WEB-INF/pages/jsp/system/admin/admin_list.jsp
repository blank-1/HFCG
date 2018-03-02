<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<body>
<%@include file="../../../common/header.jsp" %>
<div style="width: 100%;margin-left: 10px;">系统管理>>员工管理</div>

<div id="admin_adminsList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="admin_adminsList_toolbar" style="height:auto">
        <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addAdmin();">新增</a>
    </div>
	
    <table id="admin_adminsList_list"></table>
</div>

<script language="javascript">
	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#admin_adminsList_list").datagrid({
            idField: 'adminId',
            title: '员工列表',
            url: '${ctx}/jsp/system/admin/list',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#admin_adminsList_toolbar',
            columns:[[
                      {field:'adminId',checkbox:true},
                      {field:'loginName', width:100,title:'员工编号'},
                      {field:'displayName', width:160,title:'员工姓名'},
                      {field:'email', width:160,title:'邮箱'},
                      {field:'telephone', width:160,title:'手机号'},
                      {field:'roleName', width:200,title:'角色'},
                      {field:'adminState', width:80,title:'状态'},
                      {field:'action',title:'操作',width:160,align:'center',
                          formatter:function(value,row,index){
                               var result = "<a class='label label-info' onclick='showAdmin(" + index + ")'>详情</a> &nbsp;";
                               result += "<a class='label label-important' onclick='editAdmin(" + index + ")'>修改</a>";
                               return result;
                          }
                      }
            ]],
           	onBeforeLoad: function (value, rec) {
               var adminState = $(this).datagrid("getColumnOption", "adminState");
               if (adminState) {
            	   adminState.formatter = function (value, rowData, rowIndex) {
                       if (value == 1) {
                           return "<font style='color: green;'>正常</font>";
                       } else if(value == 2) {
                           return "<font style='color: red;'>禁用</font>";
                       }
                   }
               }
           }
       });
    }
    
    /**
     * 执行：弹出添加层。
     */
    function addAdmin() {
        $("#admin_adminsList").after("<div id='addAdmin' style=' padding:10px; '></div>");
        $("#addAdmin").dialog({
            resizable: false,
            title: '新增员工',
            href: '${ctx}/jsp/system/admin/to_admin_add',
            width: 900,
            height: 550,
            modal: true,
            top: 100,
            left: 200,
            buttons: [
                {
                    text: '提交',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $("#addAdmin").contents().find("#admin_addAdmin_form").submit();
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#addAdmin").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
	
    /**
     * 执行：弹出编辑层。
     */
    function editAdmin(index) {
    	$("#admin_adminsList_list").datagrid("selectRow",index);
        var selRow = $("#admin_adminsList_list").datagrid("getSelected");
        if (selRow) {
            $("#admin_adminsList").after("<div id='addAdmin' style=' padding:10px; '></div>");
            $("#addAdmin").dialog({
                resizable: false,
                title: '修改员工信息',
                href: '${ctx}/jsp/system/admin/to_admin_edit?adminId=' + selRow.adminId,
                width: 900,
                height: 550,
                modal: true,
                top: 100,
                left: 200,
                buttons: [
                    {
                        text: '提交',
                        iconCls: 'icon-ok',
                        handler: function () {
                        	$.messager.confirm('确认信息','是否确认修改员工信息？',function(r){
                    			if(r){
		                            $("#addAdmin").contents().find("#admin_addAdmin_form").submit();
                    			}
                        	});
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#addAdmin").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要修改的员工!", "info");
        }
    }
    
    /**
     * 执行：弹出详情层。
     */
    function showAdmin(index) {
    	$("#admin_adminsList_list").datagrid("selectRow",index);
    	var selRow = $("#admin_adminsList_list").datagrid("getSelected");
    	if (selRow) {
            $("#admin_adminsList").after("<div id='showAdmin' style=' padding:10px; '></div>");
            $("#showAdmin").dialog({
                resizable: false,
                title: '员工信息',
                href: '${ctx}/jsp/system/admin/to_admin_show?adminId=' + selRow.adminId,
                width: 700,
                height: 550,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#showAdmin").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要查看的员工!", "info");
        }
    }
    
    $(function(){
    	loadList();	
    });
</script>
</body>