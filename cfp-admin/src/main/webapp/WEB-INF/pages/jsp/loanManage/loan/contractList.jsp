<%@ page import="com.xt.cfp.core.constants.LoanApplicationStateEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<body>
<div id="loan_contract_List" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="loanContractList_toolbar" style="height:auto">

        <div id="searchtool" style="padding:5px">
            <form method="post" class="form-inline" id="contractList_form" action="${ctx}/jsp/loanManage/loan/exportExcel">
                <span>标的编号:</span><input type="text" id="loanApplicationCode" value=""  name="loanApplicationCode" style="width: 100px" size=10 />
                <span>借款名称:</span><input type="text" id="loanApplicationName" value=""  name="loanApplicationName" style="width: 100px" size=10 />
                <span>标的来源:</span>
                <select id="channel" name="channel" style="width: 120px" class="easyui-combobox">
                	<option value="-1">全部</option>
                	<option value="1">渠道</option>
                	<option value="2">门店</option>
                </select>
                <span>类型:</span>
                <select id="loanType" name="loanType" style="width: 100px" class="easyui-combobox">
                	<option value="-1">全部</option>
                	<option value="0">信贷</option>
                	<option value="1">房贷</option>
                	<option value="7">个人房产直投</option>
                	<option value="8">个人车贷</option>
                	<option value="9">现金贷</option>
                </select>
                <span>客户姓名:</span><input type="text" id="realName" name="realName" style="width: 100px" value="" size=10 onkeydown="if(event.keyCode==13){search();}"/>
				<span>身份证号(后4位):</span><input type="text" id="idCard" value=""  name="idCard" style="width: 100px" size=10 />
            	<span>手机号:</span><input type="text" id="mobileNo" value=""  name="mobileNo" style="width: 100px" size=10 />
                <span>标的状态:</span>
                
                <select id="applicationState" name="applicationState" style="width: 120px" class="easyui-combobox">
                	<option value='-1'>全部</option>
                	<option value='0'>草稿</option>
                	<option value='1'>风控审核中</option>
                	<option value='2'>发标审核中</option>
                	<option value='3'>投标中</option>
                	<option value='4'>放款审核中</option>
                	<option value='5'>待放款</option>
                	<option value='6'>还款中</option>
                	<option value='7'>已结清</option>
                	<option value='8'>提前还贷</option>
                	<option value='9'>取消</option>
                	<option value='A'>流标</option>
                </select>

                <a href="javascript:search();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
				<a href="javascript:doExport()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">导出Excel</a>
            </form>

        </div>
    </div>

    <table id="contract_list">

    </table>
    <div id="loan_detail"></div>

</div>

<script type="text/javascript">

	function _getURL(){
    	var targetURL = '${ctx}/jsp/loanManage/loan/loanApplicationList';
    	return targetURL;
	}

    function doSearch() {
    	var targetURL = _getURL();
        $("#contract_list").datagrid({
            idField: 'loanApplicationId',
            title: '借款列表',
            url: targetURL,
            pagination: true,
            pageSize: 20,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.87,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#loanContractList_toolbar',
            columns:[[
                {field:'loanType', width:50,title:'类型',
                	formatter:function(value,row,index){
	                        if(value == "0"){
	                        	return "信贷";
	                        }else if(value == "1"){
	                        	return "房贷";
	                        }else if(value == "2"){
	                        	return "企业车贷";
	                        }else if(value == "3"){
	                        	return "企业信贷";
	                        }else if(value == "4"){
	                        	return "企业保理";
	                        }else if(value == "6"){
	                        	return "企业标";
	                        }else if(value == "7"){
	                        	return "个人房产直投";
	                        }else if(value == "8"){
                                return "个人车贷";
	                        }else if(value == "9"){
                                return "现金贷";
	                        }	
                   	}	
                },
                {field:'channel', width:50,title:'来源',
                	formatter:function(value,row,index){
                        if(value == "1"){
                        	return "渠道";
                        }else if(value == "2"){
                        	return "门店";
                        }
               	}		
                },
                {field:'loanApplicationCode', width:90,title:'标的编号'},
                {field:'loanApplicationName', width:150,title:'借款名称',
                	formatter:function(value,row,index){
                		if(value != null)
                        var value = '<a href="#" onclick="detail('+index+')">'+ value +'</a> ';
                       return value;
                       }	
                },
                {field:'loanTitle', width:90,title:'标的名称'},
                {field:'durationTime', width:70,title:'标的期限'},
                {field:'userRealName', width:70,title:'借款人姓名',
                	formatter:function(value,row,index){
                        var value = '<a href="#" onclick="userDetail('+index+')">'+ value +'</a> ';
                       return value;
                       }	
                },
                {field:'idCard', width:140,title:'身份证号'},
                {field:'mobileNo', width:90,title:'手机号'},
                {field:'loanBalance', width:90,title:'借款金额',
                	formatter:function(value,row,index){
                		if(value != null && value != "")
                        return formatNum(value,2);
                        }	
                },
                {field:'confirmBalance', width:90,title:'批复金额',
					formatter:function(value,row,index){
						if(value != null && value != "")
                        return formatNum(value,2);
                        }	
                },
                {field:'haveCast', width:80,title:'已投',
                	formatter:function(value,row,index){
                		if(row.applicationState == '0' || row.applicationState == '1' || row.applicationState == '2' || row.applicationState == '9' || row.applicationState == 'A'){
                			return "0.00";
                		}else if(row.applicationState == '3'){
                			return formatNum(value,2);
                		}else if(row.applicationState == '4' || row.applicationState == '5' || row.applicationState == '6' || row.applicationState == '7' || row.applicationState == '8'){
                			return formatNum(row.confirmBalance,2);
                		}
                		return "0.00";
                       }	
                },
                {field:'applicationState', width:80,title:'标的状态',
						formatter:function(value,row,index){
							if (value == '0') {
			                    return "草稿";
			                } else if (value == '1') {
			                    return "风控审核中";
			                } else if (value == '2') {
			                    return "发标审核中";
			                } else if (value == '3') {
			                    return "投标中";
			                } else if (value == '4'){
			                    return "放款审核中";
			                } else if (value == '5'){
			                    return "待放款";
			                } else if (value == '6'){
			                    return "还款中";
			                } else if (value == '7'){
			                    return "已结清";
			                } else if (value == '8'){
			                    return "提前还贷";
			                } else if (value == '9'){
			                    return "取消";
			                } else if (value == 'A'){
			                    return "流标";
			                }
                        }		
                },
                {field:'isDelay', width:60,title:'是否逾期',
                	formatter:function(value,row,index){
                		if(value == 0){
                			return "否";
                		}
                        return "是";
                        }	
                },
                {field:'createTime', width:122,title:'创建时间',
                	formatter:function(value,row,index)
                	{
                		if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}
                },
                {field:'publishTime', width:80,title:'发标时间',
					formatter:function(value,row,index){
						if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}	
                },
                {field:'fullTime', width:80,title:'满标时间',
                	formatter:function(value,row,index){
                		if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}	
                }
                ,{field:'paymentDate', width:80,title:'放款日期',
                	formatter:function(value,row,index){
                		if(value != null && value != "")
                		return getDateStr(new Date(value));
                	}	
                },
                {field:'opertionDate', width:120,title:'最后一期还款日',
                	formatter:function(value,row,index){
            			if(value != null && value != "")
            			return getDateTimeStr(new Date(value));
            		}
                },
                {field:'agreementStatus', width:80,title:'合同状态'},
                {field:'action',align:'left',title:'操作',width:150,
                    formatter:function(value,row,index){
                    	
                   
                        var value = '<a class="label label-info" onclick="detail('+index+')">详情</a> &nbsp;&nbsp;';
                        if (row.applicationState == '<%=LoanApplicationStateEnum.REPAYMENTING.getValue()%>') {
                            value += '<a class="label label-important" onclick="agreement('+index+')">生成合同</a>';
                        }
                             if(row.applicationState == '3' ){
                            //	 if(row.otype=='1'||row.otype=='2'){
                            		  value += '<a class="label label-info" onclick="oEdit('+index+')">定向设置</a> &nbsp;&nbsp;';
                            //	 }
                             }
                            return value;
                    }
                }  
            ]]
        
        });
    }


    function search(){
        
    	$('#contract_list').datagrid('load',{
    		loanApplicationCode:$('#loanApplicationCode').val(),
    		loanApplicationName:$('#loanApplicationName').val(),
    		channel: $('#channel').combobox('getValue'),
    		loanType: $('#loanType').combobox('getValue'),
    		realName:$('#realName').val(),
    		idCard: $('#idCard').val(),
    		mobileNo: $('#mobileNo').val(),
    		applicationState: $('#applicationState').combobox('getValue')
    	});
   
    }
    function doExport(){
    	$("#contractList_form").submit();
 	}
    
    /***定向设置**/
	function oEdit(index){
		$("#contract_list").datagrid("selectRow",index);
	     var selRow = $("#contract_list").datagrid("getSelected");
	        if (selRow) {
	        	 	
				$("#loan_detail").after("<div id='loan_orient_d' style=' padding:5px; '></div>");
	            $('#loan_orient_d').dialog({
	                title : '定向设置',
	                href: '${ctx}/jsp/loanManage/loan/showLoanOrient?loanApplicationId='+selRow.loanApplicationId,
	                width: 600,
	                height:550,
	                maximizable : true,
	                minimizable : false,
	                collapsible : false,
	                modal:true,
	                resizable : true,
					onClose: function () {
						$(this).dialog('destroy');
					}
	            });

	        } else {
	            $.messager.alert("系统提示", "请选择要修改的定向设置!", "info");
	        }

		
	}

    function detail(index) {


    	$("#contract_list").datagrid("selectRow",index);
        var selRow = $("#contract_list").datagrid("getSelected");
        if (selRow) {
			$("#loan_detail").after("<div id='loan_detail_d' style=' padding:5px; '></div>");
            $('#loan_detail_d').dialog({
                title : '借款申请详情',
                href: '${ctx}/jsp/loanManage/loan/showLoanDetail?loanApplicationId='+selRow.loanApplicationId,
                width: 1000,
                height:550,
                maximizable : true,
                minimizable : false,
                collapsible : false,
                modal:true,
                resizable : true,
				onClose: function () {
					$(this).dialog('destroy');
				}
            });

        } else {
            $.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
        }


    }

    function editDetail(index) {
    	$("#contract_list").datagrid("selectRow",index);
        var selRow = $("#contract_list").datagrid("getSelected");
        if (selRow) {
        	var url = '${ctx}/jsp/loanManage/loan/to_loan_add_part234?actionType=edit&loanApplicationId=' + selRow.loanApplicationId;
            window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));

        } else {
            $.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
        }
    }

    function update_loan_application_fee_item(index) {
    	$("#contract_list").datagrid("selectRow",index);
        var selRow = $("#contract_list").datagrid("getSelected");
        if (selRow) {
			        $("#contract_list").after("<div id='loan_application_fee_item_nav' style=' padding:10px; '></div>");
			        $("#loan_application_fee_item_nav").dialog({
			            resizable: false,
			            title: '修改费用利率',
			            href: '${path}/jsp/loan_application/controller/to_loan_application_fee_item_page?loanApplicationId='+selRow.loanApplicationId,
			            width: 800,
			            modal: true,
			            height: 600,
			            top: 100,
			            left: 400,
			            buttons: [

			                {
			                    text: '提交',
			                    iconCls: 'icon-ok',
			                    handler: function () {
			                        try {
			                        	$.messager.confirm('修改费率提示','您是否要修改'+selRow.loanApplicationName+'费用费率?',function(r){
			                        		if(r){
			                        		 	$("#loan_application_fee_item_nav").contents().find("#lendapplication_toselrights_form").submit();
			                        		}
			                        	});

			                        } catch (e) {
			                            alert(e);
			                        }

			                    }
			                },

			                {
			                    text: '取消',
			                    iconCls: 'icon-cancel',
			                    handler: function () {
			                        $("#loan_application_fee_item_nav").dialog('close');
			                    }
			                }
			            ],
			            onClose: function () {
			                $(this).dialog('destroy');
			            }
			        });

        } else {
            $.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
        }
    }

    function delete_loan_application(index) {
    	$("#contract_list").datagrid("selectRow",index);
        var selRow = $("#contract_list").datagrid("getSelected");
        if (selRow) {
			$.messager.confirm('删除借款合同提示','您是否要删除'+(selRow.loanApplicationName==null ? '' : selRow.loanApplicationName )+'借款合同?',function(r){
				if(r){
					$.post('${path}/jsp/loan_application/controller/delete',{'loanApplicationId':selRow.loanApplicationId},function(data){
						if(data.results = 'success'){
							$.messager.alert("系统提示", "删除借款合同成功!", "info");
							search();
						}else{
							$.messager.alert("系统提示", "删除借款合同失败!", "info");
						}
					});
				}
			});

        } else {
            $.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
        }
    }

    function agreement(index) {
        $("#contract_list").datagrid("selectRow",index);
        var selRow = $("#contract_list").datagrid("getSelected");
        Utils.loading();
        var result = $(this).form('validate');
        if(!result){
            Utils.loaded();
        }

        $.post('${ctx}/jsp/financial/createAgreement',{loanApplicationId:selRow.loanApplicationId},function(data) {
            if (data == 'success') {
                $.messager.alert("系统提示", "借款合同生成成功!", "info");
            } else {
                $.messager.alert("系统提示", "借款合同生成失败!", "info");
            }
            Utils.loaded();
        })
    }

    function refresh(url){
    	window.location.href = url;
    }


$(document).ready(function(){
    doSearch();
});



</script>
</body>
