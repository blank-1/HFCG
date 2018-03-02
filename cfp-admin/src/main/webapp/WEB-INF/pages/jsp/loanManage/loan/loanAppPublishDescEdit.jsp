<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.xt.cfp.core.pojo.LoanApplication" %>
<%@ include file="../../../common/common.jsp" %>
<body>
<div id="loan_contract_List" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="loanContractList_toolbar" style="height:auto">

        <div id="searchtool" style="padding:5px">
            <form method="post" class="form-inline">
                <span>标的编号:</span><input type="text" id="loanApplicationCode" value="" name="loanApplicationCode"
                                         style="width: 100px" size=10/>
                <span>标的名称:</span><input type="text" id="loanApplicationName" value="" name="loanApplicationName"
                                         style="width: 100px" size=10/>
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
                    <option value="2">企业车贷</option>
                    <option value="3">企业信贷</option>
                    <option value="4">企业保理</option>
                    <option value="5">企业基金</option>
                    <option value="6">企业标</option>
                    <option value="7">个人房产直投</option>
                    <option value="8">个人车贷</option>
                    <option value="9">现金贷</option>
                </select>
                <span>客户姓名:</span><input type="text" id="realName" style="width: 100px" value="" size=10
                                         onkeydown="if(event.keyCode==13){search();}"/>
                <span>身份证号(后4位):</span><input type="text" id="idCard" value="" name="idCard" style="width: 100px"
                                              size=10/>
                <span>手机号:</span><input type="text" id="mobileNo" value="" name="mobileNo" style="width: 100px"
                                        size=10/>

                <a href="javascript:search();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addEnterpriseLoan();">新增企业借款</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addConstant();">新增个人借款</a>

            </form>
        </div>
    </div>

    <table id="loan_app_list"></table>
    <div id="loan_detail"></div>

</div>

<script type="text/javascript">

/**
 * 执行：弹出添加窗口,企业借款申请。
 */
function addEnterpriseLoan(){
    var url = '${ctx}/jsp/enterprise/loan/to_enterprise_loan';
    window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));
}

/**
 * 执行：弹出添加窗口,个人借款申请。
 */
function addConstant(){
    var url = '${ctx}/jsp/loanManage/loan/to_loan_add_part1';
    window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));
}

function _getURL() {
    var targetURL = '${ctx}/jsp/loanManage/loan/loanAppPublishDescEditList';
    return targetURL;
}

//列表加载
function doSearch() {
    var targetURL = _getURL();
    $("#loan_app_list").datagrid({
        idField: 'mainLoanApplicationId',
        title: '借款申请发标列表',
        url: targetURL,
        pagination: true,
        pageSize: 20,
        width: document.body.clientWidth * 0.97,
        height: document.body.clientHeight * 0.87,
        singleSelect: true,
        rownumbers: true,
        toolbar: '#loanContractList_toolbar',
        columns: [
            [
                {field: 'loanType', width: 60, title: '类型',
                    formatter: function (value, row, index) {
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
                {field: 'loanApplicationCode', width: 110, title: '标的编号'},
                {field: 'loanApplicationName', width: 250, title: '标的名称',
                    formatter: function (value, row, index) {
                        if (value != null)
                            var value = '<a href="#" onclick="detail(' + index + ')">' + value + '</a> ';
                        return value;
                    }
                },
                {field: 'loanTitle', width: 150, title: '借款名称'},
                {field: 'loanBalance', width: 100, title: '总借款金额',
                    formatter: function (value, row, index) {
                        if (value != null && value != "")
                            return formatNum(value, 2);
                    }
                },
                {field: 'mainPublishBalance', width: 80, title: '已发标金额',
                    formatter: function (value, row, index) {
                        if (value != null && value != "")
                            return formatNum(value, 2);
                    }
                },
                {field: 'userRealName', width: 100, title: '借款人姓名',
                    formatter: function (value, row, index) {
                        var value = '<a href="#" onclick="userDetail(' + index + ')">' + value + '</a> ';
                        return value;
                    }
                },
                {field: 'idCard', align: 'center', width: 140, title: '身份证号'},
                {field: 'mobileNo', width: 90, title: '手机号'},
                {field: 'recordTime', width: 130, title: '创建时间',
                    formatter: function (value, row, index) {
                        if (value != null && value != "")
                            return getDateTimeStr(new Date(value));
                    }
                },
                
                /*
                {field: 'applicationState', width: 80, title: '标的状态',
                    formatter: function (value, row, index) {
                        if (value == '0') {
                            return "草稿";
                        } else if (value == '1') {
                            return "风控审核中";
                        } else if (value == '2') {
                            return "发标审核中";
                        } else if (value == '3') {
                            return "投标中";
                        } else if (value == '4') {
                            return "放款审核中";
                        } else if (value == '5') {
                            return "待放款";
                        } else if (value == '6') {
                            return "还款中";
                        } else if (value == '7') {
                            return "已结清";
                        } else if (value == '8') {
                            return "提前还贷";
                        } else if (value == '9') {
                            return "取消";
                        } else if (value == 'A') {
                            return "流标";
                        }
                    }
                },
                {field: 'publishState', width: 80, title: '发标状态',
                    formatter: function (value, row, index) {
                        if (value == '1') {
                            return "未提交审核";
                        } else if (value == '2') {
                            return "编写发标描述";
                        } else if (value == '3') {
                            return "发标复审中";
                        } else if (value == '4') {
                            return "发标复审通过";
                        } else if (value == '5') {
                            return "发标驳回";
                        }
                    }
                },
                */
                
                {field: 'displayMainState', width: 80, title: '总借款状态',
                    formatter: function (value, row, index) {
                        if (value == '0') {
                            return "<font style='color: green'>未发标</font>";
                        } else if (value == '1') {
                            return "<font style='color: blue'>发标中</font>";
                        } else if (value == '2') {
                            return "<font style='color: red'>发标完成</font>";
                        }
                    }
                },
                {field: 'mainAutoPublish', width: 60, title: '自动发标',
                    formatter: function (value, row, index) {
                        if (value == '1') {
                            return "<font style='color: orange;'>已开启</font>";
                        } else {
                            return "<font style='color: gray'>已关闭</font>";
                        }
                    }
                },
                {field: 'action', align: 'left', title: '操作', width: 200,
                    formatter: function (value, row, index) {
                    	var value = '';
                        if(row.applicationState == '0' || row.applicationState == '1' || row.applicationState == '2'){
                        	value += '<a class="label label-success" onclick="editDetail(' + index + ')">编辑 </a> &nbsp;&nbsp;';
                        }
                        if(row.applicationState != '0' && row.applicationState != '1'){
                        	value += '<a class="label label-important" onclick="publish_manage(' + index + ')">标的管理</a>  &nbsp;&nbsp;';	
                        }
                        if(row.mainState == '1' || row.mainState == '2'){
                        	if(row.mainAutoPublish != '1'){
                        		value += '<a class="label label-warning" onclick="doAutoPublish(' + index + ',1)">开启自动发标</a>  &nbsp;&nbsp;';	
                        	}else{
                        		value += '<a class="label" onclick="doAutoPublish(' + index + ',0)">关闭自动发标</a>  &nbsp;&nbsp;';
                        	}
                        }
                        return value;
                    }
                }

            ]
        ]
    });
}

//查询
function search() {
    $('#loan_app_list').datagrid('load', {
        loanApplicationCode: $('#loanApplicationCode').val(),
        loanApplicationName: $('#loanApplicationName').val(),
        channel: $('#channel').combobox('getValue'),
        loanType: $('#loanType').combobox('getValue'),
        userRealName: $('#realName').val(),
        idCard: $('#idCard').val(),
        mobileNo: $('#mobileNo').val()
    });
}

//编辑
function editDetail(index) {
    $("#loan_app_list").datagrid("selectRow", index);
    var selRow = $("#loan_app_list").datagrid("getSelected");
    if (selRow) {
    	
    	var url = '';
    	if(selRow.loanType == "0" || selRow.loanType == "1" || selRow.loanType == "7"){
    		// 如果是个人
        	url = '${ctx}/jsp/loanManage/loan/to_loan_add_part234?actionType=edit&loanApplicationId=' + selRow.mainLoanApplicationId;	
    	}else if(selRow.loanType == "2" || selRow.loanType == "3" || selRow.loanType == "4" || selRow.loanType == "5" || selRow.loanType == "6" ){
        	// 如果是企业
        	url = '${ctx}/jsp/enterprise/loan/to_part?actionType=edit&loanApplicationId=' + selRow.mainLoanApplicationId + '&loanType=' + selRow.loanType;
    	}else if(selRow.loanType == "8"){
            url = '${ctx}/jsp/loanManage/loan/to_loan_add_part234?actionType=edit&loanApplicationId=' + selRow.mainLoanApplicationId;
        }else if(selRow.loanType == "9"){
            url = '${ctx}/jsp/loanManage/loan/to_loan_add_part234?actionType=edit&loanApplicationId=' + selRow.mainLoanApplicationId;
        }
    	window.open(url,'_blank',"top=0,left=0,width=" + document.body.clientWidth + ",height=" + (document.body.clientHeight+100));

    } else {
        $.messager.alert("系统提示", "请选择要查看的借款合同详细信息!", "info");
    }
}

//标的管理
function publish_manage(index) {
    $("#loan_app_list").datagrid("selectRow", index);
    var selRow = $("#loan_app_list").datagrid("getSelected");
    if (selRow) {
        window.open("${ctx}/jsp/loanPublish/loan/to_publish_manage?loanApplicationId=" + selRow.mainLoanApplicationId);
    } else {
        $.messager.alert("系统提示", "请选择要发标的借款申请!", "info");
    }
}

/**
 * 执行自动发标：启用、关闭。
 */
function doAutoPublish(index, status){
	$("#loan_app_list").datagrid("selectRow", index);
    var selRow = $("#loan_app_list").datagrid("getSelected");
    if (selRow) {
    	var msg = "确定要关闭吗？";
    	if(status == 1){
    		msg = "确定要启用吗？";
    	}
        $.messager.confirm("操作提示", msg, function (r) {
            if (r) {
    			$.post('${ctx}/jsp/loanManage/loan/doAutoPublish',
                   {
    					mainLoanApplicationId:selRow.mainLoanApplicationId,
    					status:status
                   },
                   function(data){
                       if(data.result == 'success'){
                           $.messager.alert("操作提示", "操作成功！", "info");
                           $('#loan_app_list').datagrid('reload');
                       }else if(data.result == 'error'){
                           if(data.errCode == 'check'){
                               $.messager.alert("验证提示", data.errMsg, "warning");
                           }else{
                               $.messager.alert("系统提示", data.errMsg, "warning");
                           }
                       }else{
                           $.messager.alert("系统提示", "网络异常，请稍后操作！", "error");
                       }
                },'json');
            }
        });
    } else {
        $.messager.alert("系统提示", "请选择要自动发标的借款申请!", "info");
    }
}

$(document).ready(function () {
    doSearch();
});


</script>
</body>