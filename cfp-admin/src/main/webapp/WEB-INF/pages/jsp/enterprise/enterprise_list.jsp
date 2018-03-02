<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../common/common.jsp" %>
<body>
<div id="enterpriseList" class="container-fluid" style="padding: 5px 0px 0px 10px">

	<div id="enterpriseList_toolbar" style="height:auto">
        <div class="container-fluid" style="padding: 5px 0px 0px 10px">
            <form action="${ctx}/jsp/enterprise/enterprise_list" method="post" class="form-inline">
            	<span>企业名称:</span><input type="text" id="searchEnterpriseName" size=6 maxlength="50"/>&nbsp;&nbsp;&nbsp;&nbsp;
            	<span>企业类型:</span>
				<select id="searchEnterpriseType" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <option value="0">保理</option>
                    <option value="2">资管公司</option>
                    <option value="1">其它</option>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <span>状态:</span>
				<select id="searchState" class="easyui-combobox" style="width: 130px;" >
                    <option value="">全部</option>
                    <option value="0">正常</option>
                    <option value="1">禁用</option>
                </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <a onclick="doSearch();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a onclick="addEnterprise();" class="easyui-linkbutton" data-options="iconCls:'icon-add'">新增</a>
            </form>
        </div>
    </div>
	
    <table id="enterpriseList_list"></table>
</div>

<script language="javascript">

	/**
	 * 执行：列表加载。
	 */
    function loadList(){
        $("#enterpriseList_list").datagrid({
            idField: 'enterpriseId',
            title: '企业列表',
            url: '${ctx}/jsp/enterprise/enterprise_list',
            pagination: true,
            pageSize: 10,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.8,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#enterpriseList_toolbar',
            columns:[[
					  {field:'enterpriseName',width:250,align:'center',title:'企业名称',
						  formatter:function(value,row,index){
                              return "<a style='cursor:pointer' onclick='showDetail(" + row.enterpriseId + ")'>" + value + "</a>";
                          }
					  },
                      {field:'enterpriseType',width:100,align:'center',title:'企业类型'},
                      {field:'legalPersonName',width:120,align:'center',title:'法人姓名'},
                      {field:'accountNumber',width:150,align:'center',title:'开户许可证'},
                      {field:'organizationCode',width:150,align:'center',title:'组织机构代码证'},
                      {field:'businessRegistrationNumber',width:150,align:'center',title:'注册号'},
                      {field:'singleMaximumAmount',width:120,align:'right',title:'单笔最大额度(万元)'},
                      {field:'annualMaximumLimit',width:120,align:'right',title:'年度最大限额(万元)'},
                      {field:'annualResidualAmount',width:120,align:'right',title:'年度剩余额度(万元)'},
                      {field:'action',width:200,align:'left',title:'操作',
                          formatter:function(value,row,index){
                          		var result = "<a class='label label-success' onclick='showEdit(" + row.enterpriseId + ")'>编辑</a>&nbsp;";
                          		result += "<a class='label label-info' onclick='income(" +  row.enterpriseId + ")'>充值</a>&nbsp;";
                                result += "<a class='label label-info' onclick='withDraw(" + row.enterpriseId  + ")'>提现</a>&nbsp;";
                          		if(row.state == 0){
                          			result += "<a class='label' onclick='doState(" + row.enterpriseId + ",1)'>禁用</a>&nbsp;";
                          		}else{
                          			result += "<a class='label label-important' onclick='doState(" + row.enterpriseId + ",0)'>启用</a>&nbsp;";
                                    result += "<a class='label label-important' onclick='doOpenAccount(" + row.enterpriseId + ")'>去开户</a>&nbsp;";
                          		}
                          		if(row.enterpriseType == 0){
                          			result += "<a class='label label-warning' onclick='showManage(" + row.enterpriseId + ")'>管理</a>";	
                          		}
                                return result;
                          }
                      }
            ]],
           	onBeforeLoad: function (value, rec) {
         	   
	               // 单笔最大额度
	               var singleMaximumAmount = $(this).datagrid("getColumnOption", "singleMaximumAmount");
	               if (singleMaximumAmount) {
	            	   singleMaximumAmount.formatter = function (value, rowData, rowIndex) {
	            		   if(value){
	            			   return formatNum(value,2);
	            		   }
	                   }
	               }
	               
	               // 年度最大限额
	               var annualMaximumLimit = $(this).datagrid("getColumnOption", "annualMaximumLimit");
	               if (annualMaximumLimit) {
	            	   annualMaximumLimit.formatter = function (value, rowData, rowIndex) {
	            		   if(value){
	            			   return formatNum(value,2);
	            		   }
	                   }
	               }
	               
	               // 年度剩余额度
	               var annualResidualAmount = $(this).datagrid("getColumnOption", "annualResidualAmount");
	               if (annualResidualAmount) {
	            	   annualResidualAmount.formatter = function (value, rowData, rowIndex) {
	            		   if(value){
	            			   return formatNum(value,2);
	            		   }
	                   }
	               }
	               
	               // 企业类型
	               var enterpriseType = $(this).datagrid("getColumnOption", "enterpriseType");
	               if (enterpriseType) {
	            	   enterpriseType.formatter = function (value, rowData, rowIndex) {
	            		   if(value == 0){
	            			   return "<font style='color: blue;'>保理</font>";
	            		   }else if(value == 2){
                               return "<font style='color: blue;'>资管公司</font>";
                           }else {
	            			   return "<font style='color: black;'>其它</font>";
	            		   }
	                   }
	               }
	               
           }
        
       });
    }
	
	// 添加
	function addEnterprise(){
		var url = '${ctx}/jsp/enterprise/to_enterprise_add_part';
	    window.open(url,'_blank',"top=0,left=0,width=" + (document.body.clientWidth*0.9) + ",height=" + (document.body.clientHeight*0.9));
	}
	
	// 编辑
	function showEdit(enterpriseId){
		var url = '${ctx}/jsp/enterprise/to_enterprise_edit_part?enterpriseId=' + enterpriseId;
	    window.open(url,'_blank',"top=0,left=0,width=" + (document.body.clientWidth*0.9) + ",height=" + (document.body.clientHeight*0.9));
	}

    function  doOpenAccount(enterpriseId){
        var url = '${ctx}/jsp/enterprise/doLegalPersonOpenAcc?enterpriseId=' + enterpriseId;
        window.open(url,'_blank',"top=0,left=0,width=" + (document.body.clientWidth*0.9) + ",height=" + (document.body.clientHeight*0.9));

    }
	// 启用、禁用
	function doState(enterpriseId, state){
		var msg = "确定要禁用该企业吗？";
		if(state == 0){
			msg = "确定要启用该企业吗？";
		}
	    $.messager.confirm("操作提示", msg, function (r) {
	        if (r) {
				$.post('${ctx}/jsp/enterprise/doState',
	               {
						enterpriseId:enterpriseId,
						state:state
	               },
	               function(data){
	                   if(data.result == 'success'){
	                       $.messager.alert("操作提示", "操作成功！", "info");
	                       $('#enterpriseList_list').datagrid('reload');
	                   }else if(data.result == 'error'){
	                       if(data.errCode == 'check'){
	                           $.messager.alert("验证提示", data.errMsg, "info");
	                       }else{
	                           $.messager.alert("系统提示", data.errMsg, "info");
	                       }
	                   }else{
	                       $.messager.alert("系统提示", "网络异常，请稍后操作！", "info");
	                   }
	            },'json');
	        }
	    });
	}
	
	// 管理
    function showManage(enterpriseId) {
    	if (enterpriseId) {
            $("#enterpriseList_list").after("<div id='showManageDetail' style=' padding:10px; '></div>");
            $("#showManageDetail").dialog({
                resizable: false,
                title: '企业管理',
                href: '${ctx}/jsp/enterprise/to_enterprise_manage?enterpriseId='+enterpriseId,
                width: 700,
                height: 450,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
					{
					    text: '确定',
					    iconCls: 'icon-ok',
					    handler: function () {
					    	saveManage();
					    }
					},
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#showManageDetail").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要操作的数据!", "info");
        }
    }
	
	/**
	 * 执行：详情。
	 */
	function showDetail(enterpriseId){
		var url = '${ctx}/jsp/enterprise/to_enterprise_detail_part?enterpriseId=' + enterpriseId;
	    window.open(url,'_blank',"top=0,left=0,width=" + (document.body.clientWidth*0.9) + ",height=" + (document.body.clientHeight*0.9));
	}
	
    /**
     * 执行：查询。
     */
    function doSearch(){
        var searchInfo = {};
        searchInfo.searchEnterpriseName=$("#searchEnterpriseName").val();
        if('' != $('#searchEnterpriseType').combobox('getValue')){
        	searchInfo.searchEnterpriseType=$('#searchEnterpriseType').combobox('getValue');
        }
        if('' != $('#searchState').combobox('getValue')){
        	searchInfo.searchState=$('#searchState').combobox('getValue');
        }
        $('#enterpriseList_list').datagrid('load',searchInfo);
    }
    
    // 提现
    function withDraw(enterpriseId){
        $("#enterpriseList_list").after("<div id='withDraw' style=' padding:10px; '></div>");
        $("#withDraw").dialog({
            resizable: false,
            title: '提现',
            href: '${ctx}/jsp/enterprise/toWithDraw?enterpriseId=' + enterpriseId,
            width: 700,
            height: 520,
            modal: true,
            top: 100,
            left: 200,
            buttons: [
                {
                    text: '提交申请',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var form = $("#withDraw").contents().find("#withDraw_form");
                        var validate = form.form('validate');

                        if(validate){
                            $.ajax({
                                url:'${ctx}/jsp/enterprise/withDraw',
                                data:form.serialize(),
                                type:"POST",
                                success:function(msg){
                                    if(msg=='success'){
                                        $.messager.alert('提示', '提交成功！！', 'info',function(){
                                            $("#withDraw").dialog('close');
                                            doSearch();
                                        });
                                    }else{
                                        $.messager.alert('提示', '提交失败！'+msg, 'info');
                                    }
                                }
                            });
                        }
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#withDraw").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }

    // 充值
    function income(enterpriseId){
        $("#enterpriseList_list").after("<div id='income' style=' padding:10px; '></div>");
        $("#income").dialog({
            resizable: false,
            title: '充值',
            href: '${ctx}/jsp/enterprise/toIncome?enterpriseId=' + enterpriseId,
            width: 700,
            height: 400,
            modal: true,
            top: 100,
            left: 200,
            buttons: [
                {
                    text: '充值',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var form = $("#income").contents().find("#income_form");
                        var validate = form.form('validate');
                        if(validate){
                             $.ajax({
                                url:'${ctx}/jsp/enterprise/income',
                                data:form.serialize(),
                                type:"POST",
                                success:function(msg){
                                    if(msg=='success'){
                                        $.messager.alert('提示', '充值成功！！', 'info',function(){
                                            $("#income").dialog('close');
                                            doSearch();
                                        });
                                    }else{
                                        $.messager.alert('提示', '充值失败！'+msg, 'info');
                                    }
                                }
                            });
                        }
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#income").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }
    
    $(function(){
    	loadList();	
    });
</script>
    
</body>