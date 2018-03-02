<%@ page import="com.xt.cfp.core.constants.LoanApplicationStateEnum" %>
<%@ page import="com.xt.cfp.core.constants.LoanAppLendAuditStatusEnums" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<body>
<div id="wait_loan" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="waitLoan_toolbar" style="height:auto">

        <div id="searchtool" style="padding:5px">
            <form method="post" class="form-inline">
                <span>标的编号:</span><input type="text" id="loanApplicationCode" value=""  name="loanApplicationCode" style="width: 100px" size=10 />
                <span>标的名称:</span><input type="text" id="loanApplicationName" value=""  name="loanApplicationName" style="width: 100px" size=10 />
                <span>标的来源:</span>
                <select id="channel" style="width: 120px" class="easyui-combobox">
                	<option value="-1">全部</option>
                	<option value="1">渠道</option>
                	<option value="2">门店</option>
                </select>
                <span>类型:</span>
                <select id="loanType" style="width: 100px" class="easyui-combobox">
                	<option value="-1">全部</option>
                	<option value="0">信贷</option>
                	<option value="1">房贷</option>
                	<option value="2">车贷</option>
                	<option value="6">企业标</option>
                	<option value="7">房产直投</option>
                	<option value="8">个人车贷</option>
                </select>
                <span>客户姓名:</span><input type="text" id="realName" style="width: 100px" value="" size=10 onkeydown="if(event.keyCode==13){search();}"/>
				<span>身份证号(后4位):</span><input type="text" id="idCard" value=""  name="idCard" style="width: 100px" size=10 />
            	<span>手机号:</span><input type="text" id="mobileNo" value=""  name="mobileNo" style="width: 100px" size=10 />        	
                <a href="javascript:search();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
            </form>

        </div>
    </div>

    <table id="wait_loan_list">

    </table>
</div>

<script type="text/javascript">

	

	//var adminId = '${loginUser.adminId}';
	//var roleCode = '${currentRole.roleCode}';

	function _getURL(){
    	var targetURL = '${ctx}/jsp/financial/waitLoanList';

    	return targetURL;
	}


    function doSearch() {
    	var targetURL = _getURL();
    	//console.log(targetURL);
        $("#wait_loan_list").datagrid({
            idField: 'loanApplicationId',
            title: '满标复审待放款列表',
            url: targetURL,
            pagination: true,
            pageSize: 20,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.87,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#waitLoan_toolbar',
            columns:[[
               
                {field:'loanApplicationCode', width:80,title:'标的编号'},
                {field:'loanApplicationName', width:200,title:'借款名称',
                	formatter:function(value,row,index){
                		if(value != null)
                        var value = '<a href="#" onclick="detail('+index+')">'+ value +'</a> ';
                       	return value;
                       }	
                },
                {field:'loanTitle', width:200,title:'标的名称'},
                {field:'userRealName', width:100,title:'借款人姓名',
                	formatter:function(value,row,index){
                        var value = '<a href="#" onclick="userDetail('+index+')">'+ value +'</a> ';
                       return value;
                       }	
                },
                
                {field:'loanBalance', width:130,title:'借款金额',
                	formatter:function(value,row,index){
                		if(value != null && value != "")
                        return formatNum(value,2);
                        }	
                },
                {field:'idCard', width:150,title:'身份证号'},
                {field:'mobileNo', width:100,title:'手机号'},
                {field:'createTime', width:130,title:'创建时间',
                	formatter:function(value,row,index){
                		if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}
                },
                {field:'fullTime', width:130,title:'满标时间',
					formatter:function(value,row,index){
						if(value != null && value != "")
                		return getDateTimeStr(new Date(value));
                	}	
                },
                {field:'applicationState', width:150,title:'状态',
					formatter:function(value,row,index){
                		if(value == "4"){
                			return "满标待审核";
                		}else if(value == "5"){
                			return "待放款";
                		}
                	}	
                },
                {field:'action',align:'left',title:'操作',width:200,
                    formatter:function(value,row,index){
                        var value = ""; 
                    	if(row.applicationState == '<%=LoanApplicationStateEnum.LOANAUDIT.getValue()%>'){
                            if (row.lendState = '<%=LoanAppLendAuditStatusEnums.FULL_AUDITING.getValue()%>') {
                                value += '<a class="label label-important" onclick="fullSubjectReview('+index+')">满标待复审</a>';
                            }

                         } else if(row.applicationState == '<%=LoanApplicationStateEnum.WAITMAKELOANAUDIT.getValue()%>') {
                            value += '<a class="label label-important" onclick="doLoan('+index+')">放款</a>';
                        } else if (row.applicationState == '<%=LoanApplicationStateEnum.REPAYMENTING.getValue()%>') {
                            value += '<a class="label label-important" onclick="agreement('+index+')">生成合同</a>';
                        }


                       return value;
                    }
                }

            ]]});
    }
	
    function fullSubjectReview(index){
    	$("#wait_loan_list").datagrid("selectRow",index);
    	var selRow = $("#wait_loan_list").datagrid("getSelected");
    	if(selRow){
    		$("#wait_loan_list").after("<div id='fullSubjectReview' style=' padding:10px; '></div>");
            $("#fullSubjectReview").dialog({
                resizable: false,
                title: '满标待复审',
                href: '${ctx}/jsp/financial/toFullSubjectReview?loanApplicationId='+selRow.loanApplicationId,
                width: 1200,
                height: 600,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '确认满标',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $.messager.confirm("系统提示", "是否确认满标复审通过?", function(r) {
                                if (r) {
                                	var fullContent = $("#fullSubjectReview").contents().find("#fullContent").val();
                                    $.post("${ctx}/jsp/financial/fullLoanApp",{loanApplicationId:selRow.loanApplicationId,fullContent:fullContent},function(data){
                                        if (data == "success") {
                                            $.messager.alert("系统提示", "借款申请满标复审通过!", "info",function(){
                                                $("#wait_loan_list").datagrid("reload");
                                                $("#fullSubjectReview").dialog('close');
                                            });

                                        } else {
                                            if(data.length<1000){
                                                $.messager.alert("系统提示", data, "info");
                                            }else{
                                                window.location.href = "${ctx}/toLogin";
                                            }
                                        }

                                    })
								
                                }
                            });

                        }
                    },
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#fullSubjectReview").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });		
    	}else{
    		$.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
    	}
    }

    function doLoan(index){
    	$("#wait_loan_list").datagrid("selectRow",index);
    	var selRow = $("#wait_loan_list").datagrid("getSelected");
    	if(selRow){

            window.open("${ctx}/jsp/financial/toMakeLoan?loanApplicationId=" + selRow.loanApplicationId);
    	}else{
    		$.messager.alert("系统提示", "请选择要查看的借款申请!", "info");
    	}
    }
    function agreement(index) {
        $("#wait_loan_list").datagrid("selectRow",index);
        var selRow = $("#wait_loan_list").datagrid("getSelected");
        $.post('${ctx}/jsp/financial/createAgreement',{loanApplicationId:selRow.loanApplicationId},function(data) {
            if (data == 'success') {
                $.messager.alert("系统提示", "借款合同生成成功!", "info");
            } else {
                $.messager.alert("系统提示", "借款合同生成失败!", "info");
            }
        })
    }
    
    function search(){
        
       // $("#contract_list").datagrid("options").queryParams = params;
    	//$("#contract_list").datagrid("reload");
    	$('#wait_loan_list').datagrid('load',{
    		loanApplicationCode:$('#loanApplicationCode').val(),
    		loanApplicationName:$('#loanApplicationName').val(),
    		channel: $('#channel').combobox('getValue'),
    		loanType: $('#loanType').combobox('getValue'),
    		realName:$('#realName').val(),
    		idCard: $('#idCard').val(),
    		mobileNo: $('#mobileNo').val()
    		
    	});
    }
    doSearch();
</script>
</body>