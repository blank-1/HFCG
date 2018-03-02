<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.ext.UserInfoVO" %>
<%@ page import="com.xt.cfp.core.constants.UserType" %>
<%@ include file="../../../common/common.jsp" %>
<body>
<div id="custom_customerList" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="custom_customerList_toolbar" style="height: auto">
    <form method="post" id="customer_form" action="${ctx}/jsp/custom/customer/exportExcel" class="form-inline">
        <div style="height:auto;padding-left: 30px;">
            客户状态:<select id="searchStatus" name="status" class="easyui-combobox">
            <option value="-1">全部</option>
            <option value="0">正常</option>
            <option value="1">冻结</option>
        </select>
            客户姓名:<input type="text" name="realName" id="searchRealName">
            用户名:<input type="text" name="loginName" id="searchLoginName">
            手机号:<input type="text" name="mobileNo" id="searchMobileNo">
            身份证号:<input type="text" name="idCard" id="searchIdCard">
            <!-- 判断是否为客服用户 -->
          <input   name="roleID" id="roleID" value="test" type="hidden" disabled="disabled">
            <mis:PermisTag code="3000201">
	            <a href="javascript:doSearchCustomer();" class="easyui-linkbutton"
	               data-options="iconCls:'icon-search'">查询</a>
            </mis:PermisTag>
            

	<mis:PermisTag code="03000202">
            <a href="javascript:doExport()" class="easyui-linkbutton"
               data-options="iconCls:'icon-save'">导出Excel</a>
        </mis:PermisTag>
        </div>
    </form>
    </div>
    <table id="customerList_list">
        <thead>
        <th data-options="field:'loginName', width:100 ">用户名</th>
        <th data-options="field:'realName', width:100 ">客户姓名</th>
        <th data-options="field:'type', width:100,formatter:formatterCustomerType">客户类型</th>
        <th data-options="field:'mobileNo',align:'center',width:100">手机号</th>
        <th data-options="field:'idCard',align:'center',width:120">身份证号</th>
        <th data-options="field:'value',align:'center',width:100">总资产</th>
        <th data-options="field:'availValue',align:'center',width:80">可用资金</th>
        <th data-options="field:'frozeValue',align:'center',width:80">冻结资金</th>
        <th data-options="field:'balance',align:'center',width:80">当前负债</th>
        <th data-options="field:'status',align:'center',width:60,formatter:formatterCustomerState">客户状态</th>
        <th data-options="field:'createTime',align:'center',width:120">注册时间</th>
        <th data-options="field:'verifyTime',align:'center',width:120">实名认证时间</th>
        <th data-options="field:'firstLenderTime',align:'center',width:120">首次投标时间</th>
        <th data-options="field:'handle',width:120,align:'center',formatter:formatterHandle">操作</th>
        </thead>
    </table>
</div>
<script language="javascript">

    function formatterCustomerState(value, row, index) {
        if (1 == value) {
            return '冻结';
        } else if (2 == value) {
            return '禁用';
        } else {
            return '正常';
        }
    }
    function formatterHandle(value, row, index) {
        var value = "" ;
        <mis:PermisTag code="03000203">
       	 value = '<a class="label label-info" onclick="toView(' + index + ')">详情</a>&nbsp;';
        </mis:PermisTag >
        <mis:PermisTag code="03000204">
        value += '<a class="label label-important" onclick="toAuthentication(' + index + ')">认证</a>&nbsp;';
        </mis:PermisTag >
        var msg = row.status == '0' ? '冻结' : '解冻';
        <mis:PermisTag code="03000205">
        value += '<a class="label label-success" onclick="toEditCustomer(' + index + ')">' + msg + '</a>&nbsp;';
        </mis:PermisTag >
        return value;
    }
    function formatterCustomerType(value, row, index) {
        if (<%=UserType.COMMON.getValue()%> == value) {
            return '普通用户';
        } else if (<%=UserType.MEDIATOR.getValue()%> == value) {
            return '居间人';
        } else if (<%=UserType.CHANNEL.getValue()%> == value) {
            return '渠道用户';
        } else if (<%=UserType.SYSTEM.getValue()%> == value) {
            return '系统平台用户';
        } else if (<%=UserType.GUARANTEE_COMPANY.getValue()%> == value) {
            return '担保公司用户';
        }else if (<%=UserType.LINE.getValue()%> == value) {
            return '线下用户';
        }else if (<%=UserType.ENTERPRISE.getValue()%> == value) {
            return '企业虚拟用户';
        }else if (<%=UserType.ENTERPRISE_USER.getValue()%> == value) {
            return '企业人员用户';
        }
    }

    // 列表加载
    function doSearch() {
        $("#customerList_list").datagrid({
            idField: 'userId',
            title: '客户列表',
            url: '${ctx}/jsp/custom/customer/customerPage',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#custom_customerList_toolbar',
            onDblClickRow: function (index, data) {
                toView(index);
            },
            onBeforeLoad: function (value, rec) {
                var createTime = $(this).datagrid("getColumnOption", "createTime");
                if (createTime) {
                    createTime.formatter = function (value, rowData, rowIndex) {
                        var d = new Date(value);
                        return getDateTimeStr(d);
                    }
                }

                var verifyTime = $(this).datagrid("getColumnOption", "verifyTime");
                if (verifyTime) {
                    verifyTime.formatter = function (value, rowData, rowIndex) {
                        if(value!=null){

                            var d = new Date(value);
                            return getDateTimeStr(d);
                        }
                    }
                }

                var firstLenderTime = $(this).datagrid("getColumnOption", "firstLenderTime");
                if (firstLenderTime) {
                    firstLenderTime.formatter = function (value, rowData, rowIndex) {
                        if(value!=null){

                            var d = new Date(value);
                            return getDateTimeStr(d);
                        }
                    }
                }
                var idCard = $(this).datagrid("getColumnOption", "idCard");
                if (idCard) {
                    idCard.formatter = function (value, rowData, rowIndex) {
                        var str="";
                        if (value && (value.length==15 || value.length == 18)) {
                            str += value.substr(0,3);
                            str+="************";
                            str+= value.substr(value.length-3,3);
                        }
                        return str;
                    }
                }
                var value = $(this).datagrid("getColumnOption", "value");
                if (value) {
                    value.formatter = function (value, rowData, rowIndex) {
                        return formatNum(value, 2);
                    }
                }
                var availValue = $(this).datagrid("getColumnOption", "availValue");
                if (availValue) {
                    availValue.formatter = function (value, rowData, rowIndex) {
                        return formatNum(value, 2);
                    }
                }
                var frozeValue = $(this).datagrid("getColumnOption", "frozeValue");
                if (frozeValue) {
                    frozeValue.formatter = function (value, rowData, rowIndex) {
                        return formatNum(value, 2);
                    }
                }
                var balance = $(this).datagrid("getColumnOption", "balance");
                if (balance) {
                    balance.formatter = function (value, rowData, rowIndex) {
                        return formatNum(value, 2);
                    }
                }


            }
        });
    }
    doSearch();


    function toView(index) {
        $("#customerList_list").datagrid("selectRow", index);
        var selRow = $("#customerList_list").datagrid("getSelected");
        if (selRow) {
            window.open("${ctx}/jsp/custom/customer/toViewCustomer?userId=" + selRow.userId);
        } else {
            $.messager.alert("系统提示", "请选择要查看的客户信息!", "info");
        }
    }
    
    function toAuthentication(index) {
        $("#customerList_list").datagrid("selectRow", index);
        $("#custom_customerList").after("<div id='toAuthentication' style=' padding:10px;'></div>");
        var selRow = $("#customerList_list").datagrid("getSelected");
        if (selRow) {
	        $("#toAuthentication").dialog({
	            resizable: false,
	            title: '客户认证',
	            href: '${ctx}/jsp/custom/customer/toAuthentication?userId=' + selRow.userId,
	            width: 500,
	            modal: true,
	            height: 300,
	            buttons: [
	                {
	                    text: '提交',
	                    iconCls: 'icon-ok',
	                    handler: function () {
	                        try {
	                            $("#toAuthentication").contents().find("#authentication_form").submit();
	                        } catch (e) {
	                            alert(e);
	                        }
	
	                    }
	                },
	                {
	                    text: '取消',
	                    iconCls: 'icon-cancel',
	                    handler: function () {
	                        $("#toAuthentication").dialog('close');
	                    }
	                }
	            ],
	            onClose: function () {
	                $(this).dialog('destroy');
	            }
	        });
        } else {
            $.messager.alert("系统提示", "请选择要认证的客户!", "info");
        }
    }

    function toEditCustomer(index) {
        $("#customerList_list").datagrid("selectRow", index);
        var selRow = $("#customerList_list").datagrid("getSelected");
        var msg = selRow.status == '0' ? '是否冻结' : '是否解冻';
        var editstatus = selRow.status == '0' ? '1' : '0';
        if (selRow) {
            $.messager.confirm("确认", msg, function (r) {
                if (r) {
                    $.post('${ctx}/jsp/custom/customer/toEditCustomer?userId=' + selRow.userId + '&status=' + editstatus, function (date) {
                        if (date == "success") {
                            $.messager.alert("系统提示", "修改冻结状态成功", "info");
                            $("#customerList_list").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "修改冻结状态失败", "error");
                        }
                    });
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择你要修改的信息!", "info");
        }
    }

    function doExport(){
    	   <mis:PermisTag code="030004">
    	   $("#roleID").attr("disabled",false);
    	    </mis:PermisTag >
    		 $("#customer_form").submit();
    }

    function doSearchCustomer() {
        var customer = {};
        customer.status = $("#searchStatus").combobox("getValue");
        customer.realName = $("#searchRealName").val();
        customer.loginName = $("#searchLoginName").val();
        customer.mobileNo = $("#searchMobileNo").val();
        customer.idCard = $("#searchIdCard").val();
        $("#customerList_list").datagrid("options").queryParams = customer;
        $("#customerList_list").datagrid("load");
    }
</script>

</body>
