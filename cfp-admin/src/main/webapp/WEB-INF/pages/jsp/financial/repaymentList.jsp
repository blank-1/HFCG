<%@ page import="com.xt.cfp.core.constants.RepaymentPlanStateEnum" %>
<%--
  User: Ren yulin
  Date: 15-7-20 下午1:47
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<body>
<div id="repayment" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="repayment_toolbar" style="height:auto;" >

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
                    <option value="6">企业标</option>
                    <option value="7">房产直投</option>
                    <option value="8">个人车贷</option>
                    <option value="9">现金贷</option>
                </select>
                <span>客户姓名:</span><input type="text" id="realName" style="width: 100px" value="" size=10
                                         onkeydown="if(event.keyCode==13){search();}"/>
                <span>身份证号(后4位):</span><input type="text" id="idCard" value="" name="idCard" style="width: 100px"
                                              size=10/>
                <span>手机号:</span><input type="text" id="mobileNo" value="" name="mobileNo" style="width: 100px"
                                        size=10/><br />
                <span>应还款日:</span>
                <input type="text" class="easyui-datebox" editable="false" style="width: 150px" name="searchBeginRepaymentDay" id="searchBeginRepaymentDay">至
				<input type="text" class="easyui-datebox" editable="false" style="width: 150px" name="searchEndRepaymentDay" id="searchEndRepaymentDay">&nbsp;&nbsp;&nbsp;
                <span>当期状态:</span>
                <select id="searchPlanState" style="width: 100px" class="easyui-combobox">
                    <option value="">全部</option>
                    <option value="1">未还款</option>
                    <option value="2">部分还款</option>
                    <option value="3">已还清</option>
                    <option value="4">逾期</option>
                    <option value="5">提前还款</option>
                </select>
                             
                <mis:PermisTag code="4000401">
            	    <a href="javascript:search();" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;&nbsp;&nbsp;
            	    <a href="javascript:doExport();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">查询结果导出Excel</a>
                </mis:PermisTag>
            </form>

        </div>
    </div>

    <table id="repayment_loan_list">

    </table>
    
    <!-- 导出报表表单开始 -->
    <form method="post" class="form-inline" id="repaymentForm" action="${ctx}/report/exportRepaymentListExcel">
    	<input type="hidden" id="export_loanApplicationCode" name="export_loanApplicationCode">
        <input type="hidden" id="export_loanApplicationName" name="export_loanApplicationName">
        <input type="hidden" id="export_channel" name="export_channel">
        <input type="hidden" id="export_loanType" name="export_loanType">
        <input type="hidden" id="export_realName" name="export_realName">
        <input type="hidden" id="export_idCard" name="export_idCard">
        <input type="hidden" id="export_mobileNo" name="export_mobileNo">
        <input type="hidden" id="export_planState" name="export_planState">
        <input type="hidden" id="export_beginRepaymentDay" name="export_beginRepaymentDay">
        <input type="hidden" id="export_endRepaymentDay" name="export_endRepaymentDay">
    </form>
    <!-- 导出报表表单结束 -->
</div>

<script type="text/javascript">




    function _getURL() {
        var loanApplicationCode ='';
        <c:if test="${not empty loanApplicationId}">
            loanApplicationCode = '${loanApplicationCode}';
            $("#repayment_toolbar").hide();
            var targetURL = '${ctx}/jsp/financial/repaymentList?loanApplicationCode='+loanApplicationCode;
        </c:if>
        <c:if test="${empty loanApplicationId}">
            var targetURL = '${ctx}/jsp/financial/repaymentList';
        </c:if>
        return targetURL;
    }


    function doSearch() {
        var targetURL = _getURL();

        $("#repayment_loan_list").datagrid({
            idField: 'repaymentPlanId',
            title: '到期还款列表',
            url: targetURL,
            pagination: true,
            pageSize: 20,
            width: document.body.clientWidth * 0.97,
            height: document.body.clientHeight * 0.87,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#repayment_toolbar',
            columns: [
                [

                    {field: 'loanApplicationCode', width: 120, title: '标的编号'},
                    {field: 'repaymentDay', width: 80, title: '应还款日', formatter: formatDate},

                    {field: 'loanApplicationName', width: 180, title: '借款名称',
                        formatter: function (value, row, index) {
                            if (value != null)
                                var value = '<a href="#" onclick="detail(' + index + ')">' + value + '</a> ';
                            return value;
                        }
                    },
                    {field: 'loanTitle', width: 160, title: '标的名称'},
                    {field: 'sectionCode', width: 40, title: '期号'},
                    {field: 'userRealName', width: 80, title: '借款人姓名',
                        formatter: function (value, row, index) {
                            var value = '<a href="#" onclick="userDetail(' + index + ')">' + value + '</a> ';
                            return value;
                        }
                    },

                    {field: 'shouldCapital2', width: 90, title: '应还本金', formatter: formatNumber},
                    {field: 'shouldInterest2', width: 90, title: '应还利息', formatter: formatNumber},
                    {field: 'shouldFees', width: 80, title: '应还费用', formatter: formatNumber},
                    {field: 'shouldDefaultInterest', width: 90, title: '应还罚息', formatter: formatNumber},
                    {field: 'shouldBalance2', width: 90, title: '应还金额', formatter: formatNumber},
                    {field: 'factCalital', width: 90, title: '已还本金', formatter: formatNumber},
                    {field: 'factInterest', width: 90, title: '已还利息', formatter: formatNumber},
                    {field: 'factFees', width: 80, title: '已还费用', formatter: formatNumber},
                    {field: 'faceDefaultInterest', width: 80, title: '已还罚息', formatter: formatNumber},
                    {field: 'factBalance', width: 90, title: '已还金额', formatter: formatNumber},
                    {field: 'planState', width: 70, title: '当期状态', formatter: formatPlanState},
                    {field: 'action', align: 'left', title: '操作', width: 90,
                        formatter: function (value, row, index) {
                            var value = "";
                            
                            if (row.planState == '<%=RepaymentPlanStateEnum.UNCOMPLETE.getValue()%>'
                                    || row.planState == '<%=RepaymentPlanStateEnum.PART.getValue()%>'
                                    || row.planState == '<%=RepaymentPlanStateEnum.DEFAULT.getValue()%>'
                                    ) {
                            	<mis:PermisTag code="04000402">
                                value += '<a class="label label-important" onclick="toRepayment(' + index + ')">还款</a>&nbsp;&nbsp;';
                                value += '<a class="label label-important" onclick="toAheadRepayment(' + index + ')">提前还款</a>&nbsp;&nbsp;';
                                </mis:PermisTag >
                               //
                            }
                           
                            return value;
                        }
                    }
                    
                ]
            ]
        });
    }

    function formatDate(val) {
        if (val != null) {
            return getDateStr(new Date(val));
        }
    }

    function formatNumber(val) {
        if (val != null) {
            return formatNum(val, 2);
        }
    }

    function formatPlanState(val) {

        if (val == '<%=RepaymentPlanStateEnum.UNCOMPLETE.getValue()%>') {
            return "未还款";
        } else if (val == '<%=RepaymentPlanStateEnum.PART.getValue()%>') {
            return "部分还款";
        } else if (val == '<%=RepaymentPlanStateEnum.COMPLETE.getValue()%>') {
            return "已还清";
        } else if (val == '<%=RepaymentPlanStateEnum.DEFAULT.getValue()%>') {
            return "逾期";
        } else if (val == '<%=RepaymentPlanStateEnum.BEFORE_COMPLETE.getValue()%>') {
            return "提前还款";
        }
    }


    function toRepayment(index) {
        $("#repayment_loan_list").datagrid("selectRow", index);
        var selRow = $("#repayment_loan_list").datagrid("getSelected");
        if (selRow) {
            $("#repayment_loan_list").after("<div id='repaymentDialog' style=' padding:10px; '></div>");

            $("#repaymentDialog").dialog({
                resizable: false,
                title: '借款申请还款',
                href: '${ctx}/jsp/financial/toRepayment?repaymentPlanId='+selRow.repaymentPlanId,
                width: 800,
                height: 500,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '确认还款',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $("#repaymentDialog").contents().find("#repayment_form").submit();
                        }
                    },
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#repaymentDialog").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $("#repaymentDialog").dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要还款的借款申请!", "info");
        }
    }



    function toAheadRepayment(index) {
        $("#repayment_loan_list").datagrid("selectRow", index);
        var selRow = $("#repayment_loan_list").datagrid("getSelected");
        if (selRow) {
            $("#repayment_loan_list").after("<div id='repaymentDialog' style=' padding:10px; '></div>");

            $("#repaymentDialog").dialog({
                resizable: false,
                title: '借款申请提前还款',
                href: '${ctx}/jsp/financial/toAheadRepayment?repaymentPlanId=' + selRow.repaymentPlanId,
                width: 800,
                height: 500,
                modal: true,
                top: 100,
                left: 300,
                buttons: [
                    {
                        text: '确认还款',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $.messager.confirm("操作提示", "确认提前还款?", function (r) {
                                if (r) {
                                    $("#repaymentDialog").contents().find("#repayment_form").submit();
                                }
                            });

                        }
                    },
                    {
                        text: '关闭',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $("#repaymentDialog").dialog('close');
                        }
                    }
                ],
                onClose: function () {
                    $("#repaymentDialog").dialog('destroy');
                }
            });
        } else {
            $.messager.alert("系统提示", "请选择要还款的借款申请!", "info");
        }
    }
    function search() {
        var code = $('#loanApplicationCode').val();

        $('#repayment_loan_list').datagrid('load', {
            loanApplicationCode: code,
            loanApplicationName: $('#loanApplicationName').val(),
            channel: $('#channel').combobox('getValue'),
            loanType: $('#loanType').combobox('getValue'),
            realName: $('#realName').val(),
            idCard: $('#idCard').val(),
            mobileNo: $('#mobileNo').val(),
            planState: $('#searchPlanState').combobox('getValue'),
            beginRepaymentDay: $('#searchBeginRepaymentDay').datebox('getValue'),
            endRepaymentDay: $('#searchEndRepaymentDay').datebox('getValue')

        });
    }
    
    //查询结果导出excel功能
    function doExport(){
    	$.messager.confirm('确认信息','确认要导出Excel吗？(温馨提示：数据导出需要几秒钟，请耐心等待!)',function(r){
			if(r){
                // 处理表单提交
                $("#export_loanApplicationCode").attr("value", $('#loanApplicationCode').val());
	            $("#export_loanApplicationName").attr("value", $('#loanApplicationName').val());
	            $("#export_channel").attr("value", $('#channel').combobox('getValue'));
	            $("#export_loanType").attr("value", $('#loanType').combobox('getValue'));
	            $("#export_realName").attr("value", $('#realName').val());
	            $("#export_idCard").attr("value", $('#idCard').val());
	            $("#export_mobileNo").attr("value", $('#mobileNo').val());
	            $("#export_planState").attr("value", $('#searchPlanState').combobox('getValue'));
	            $("#export_beginRepaymentDay").attr("value", $('#searchBeginRepaymentDay').datebox('getValue'));
	            $("#export_endRepaymentDay").attr("value", $('#searchEndRepaymentDay').datebox('getValue'));
                $("#repaymentForm").submit();
			}
    	});
    }
    
    doSearch();
    <c:if test="${not empty loanApplicationId}">
    $("#repayment_toolbar").hide();
    </c:if>
</script>
</body>