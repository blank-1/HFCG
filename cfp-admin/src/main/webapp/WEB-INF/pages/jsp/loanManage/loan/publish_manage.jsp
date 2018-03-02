<%@ page import="com.xt.cfp.core.constants.LoanApplicationStateEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<body>
<div id="loan_contract_List" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="loanContractList_toolbar" style="height:auto">

        <div id="searchtool" style="padding:5px">
            <form method="post" class="form-inline">
				
				<c:if test="${mainState == 0}"><!-- 主借款状态,为未发标时显示 -->
                	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="update_loan_application_desc();">编辑发标描述</a>
                </c:if>
                <c:if test="${mainState != 2 && mainIsPublish == 1 }"><!-- 主发标表，已经有数据,并且主借款状态不是发标完成 -->
					<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="toPublish();">发标</a>
				</c:if>

            </form>

        </div>
    </div>

    <table id="contract_list"></table>
    
    <div id="loan_detail"></div>

</div>

<script type="text/javascript">

	//获取页面传值
	var loanApplicationId = '${loanApplicationId}';

	/**
	 * 执行：弹出添加窗口。
	 */
	function addEnterpriseLoan(){
	    var url = '${ctx}/jsp/enterprise/loan/to_enterprise_loan';
	    window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));
	}
	
	/**
	 * 执行：编辑发标描述。
	 */
	function update_loan_application_desc() {
	    window.open("${ctx}/jsp/loanPublish/loan/to_loan_app_front_bidding?loanApplicationId=" + loanApplicationId);
	}
	
	/**
	 * 执行：跳转到发标页面。
	 */
	function publish(index) {
	    window.open("${ctx}/jsp/loanPublish/loan/to_publish_review?loanApplicationId=" + loanApplicationId);
	}
	
	// 直接执行发标操作（替换上面跳转）。
	function toPublish() {
        $("#contract_list").before("<div id='loan_publish' style=' padding:10px; '></div>");
        $("#loan_publish").dialog({
            resizable: false,
            title: '借款申请发标',
            href: '${ctx}/jsp/loanPublish/loan/to_publish?loanApplicationId='+loanApplicationId,
            width: 800,
            modal: true,
            height: 560,
            onClose: function () {
                $(this).dialog('destroy');
            },
            buttons: [
                {
                    text: '发标',
                    iconCls: 'icon-ok',
                    handler: function () {
                        $("#loan_publish").contents().find("#loan_publish_form").submit();
                    }
                },

                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#loan_publish").dialog('close');
                    }
                }
            ]
        });
    }

	//列表加载
    function doSearch() {
    	var targetURL = '${ctx}/jsp/loanManage/loan/getLoanAppPagingByMainId?mainLoanApplicationId=' + loanApplicationId;
        $("#contract_list").datagrid({
            idField: 'loanApplicationId',
            title: '待发标列表',
            url: targetURL,
            pagination: true,
            pageSize: 20,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.87,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#loanContractList_toolbar',
            columns:[[
                {field:'loanType', width:80,title:'类型',
                	formatter:function(value,row,index){
                		if(value == "0"){
                        	return "个人信贷";
                        }else if(value == "1"){
                        	return "个人房贷";
                        }else if(value == "2"){
                        	return "企业车贷";
                        }else if(value == "3"){
                        	return "企业信贷";
                        }else if(value == "4"){
                        	return "企业保理";
                        }else if(value == "5"){
                        	return "企业基金";
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
                {field:'loanApplicationCode', width:120,title:'标的编号'},
                {field:'loanApplicationName', width:250,title:'借款名称',
                	formatter:function(value,row,index){
						if(value==null)
							return "";
                        var value = '<a href="#" onclick="detail('+index+')">'+ value +'</a> ';

                        return value;
                    }	
                },
                {field:'userRealName', width:100,title:'借款人姓名',
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
                {field:'awardRate', width:60,title:'奖励利率',
					formatter:function(value,row,index){
						if(value != null && value != "")
                        	return value;
						else
							return 0;
                    }	
                },
                {field:'createTime', width:122,title:'创建时间',
                	formatter:function(value,row,index){
                		if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}
                },
                {field:'publishTime', width:120,title:'发标时间',
					formatter:function(value,row,index){
						if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}	
                },
                {field:'publishPreheatTime', width:120,title:'预热时间',
					formatter:function(value,row,index){
						if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}	
                },
                {field:'publishOpenTime', width:120,title:'开标时间',
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
                },
                {field:'paymentDate', width:80,title:'放款日期',
                	formatter:function(value,row,index){
                		if(value != null && value != "")
                		return getDateStr(new Date(value));
                	}	
                },
                {field:'agreementStatus', width:80,title:'合同状态'},
                {field: 'action', align: 'left', title: '操作', width: 200,
                	formatter: function (value, row, index) {
                	var value = '';
                	if( row.applicationState == '3' ){
	                    value += '<a class="label label-success" onclick="editForDropLoan(' + index + ')">流标</a> &nbsp;&nbsp;';
                	}
                    return value;
                }}

            ]]
       
        });
    }

	
  //编辑流标
    function editForDropLoan(index) {
        $("#contract_list").datagrid("selectRow", index);
        var selRow = $("#contract_list").datagrid("getSelected");
        if (selRow) {
        	
        	var url = '';
        	if(selRow.loanType == "0" || selRow.loanType == "1" || selRow.loanType == "7"){
        		// 如果是个人
            	url = '${ctx}/jsp/loanManage/loan/to_drop_loan?loanApplicationId=' + selRow.loanApplicationId+'&r=' + Math.random();	
        	}else if(selRow.loanType == "2" || selRow.loanType == "3" || selRow.loanType == "4" || selRow.loanType == "5" || selRow.loanType == "6" ){
            	// 如果是企业
        		url = '${ctx}/jsp/loanManage/loan/to_drop_loan?loanApplicationId=' + selRow.loanApplicationId+'&r=' + Math.random();		
        	}
        	$("#loan_contract_List").after("<div id='failLoanDialog' style=' padding:10px; '></div>");
        	
	   	    	$('#failLoanDialog').dialog({
	                title : '流标原因',
	                href: url,
	                width: 1000,
	                height:document.body.clientHeight * 0.90,
	                maximizable : true,
	                minimizable : false,
	                collapsible : false,
	                modal:true,
	                resizable : true,
	                buttons: [
	                          {
	                              text: '确认流标',
	                              iconCls: 'icon-ok',
	                              handler: function () {
	                                 submitFailForm();
	                                  
	                                  return false;
	                              }
	                        
	                          },
	                          {
	                              text: '关闭',
	                              iconCls: 'icon-cancel',
	                              handler: function () {
	                                  $("#failLoanDialog").dialog('close');
	                              }
	                          }
	                      ],
	                      onClose: function () {
	                          $("#failLoanDialog").dialog('destroy');
	                      }
	            });
	   	    	
	   	    	
	   	    	//doSearch();
	   	  
        	
        	//window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));

        } else {
            $.messager.alert("系统提示", "请选择要操作的标的!", "info");
        }
    }
  
    //查看详情(需要判断是企业详情，还是个人详情)
    function detail(index) {
    	var httpUrl = '${ctx}/jsp/enterprise/loan/to_loan_detail?loanApplicationId=';//企业详情
    	$("#contract_list").datagrid("selectRow",index);
        var selRow = $("#contract_list").datagrid("getSelected");
        if (selRow) {
        	if(selRow.loanType == "0" || selRow.loanType == "1" || selRow.loanType == "7"){
        		httpUrl = '${ctx}/jsp/loanManage/loan/showLoanDetail?loanApplicationId=';// 个人详情
        	}
			$("#loan_detail").after("<div id='loan_detail_d' style=' padding:5px; '></div>");
            $('#loan_detail_d').dialog({
                title : '借款申请详情',
                href: httpUrl + selRow.loanApplicationId,
                width: 1000,
                height:document.body.clientHeight * 0.90,
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

	$(document).ready(function(){
	    doSearch();
	});

</script>
</body>