<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<%@ page import="com.xt.cfp.core.constants.LoanAppLendAuditStatusEnums" %>
<%@ page import="com.xt.cfp.core.constants.AccountConstants" %>
<head>
</head>
<body>
    <div class="cf" style="width:auto;margin:0 10px 5px 10px;">
        <fieldset style="height:80px">
            <legend>查询条件</legend>
            <form name="systemFlowQuery" id="systemFlowQuery" action="" class="fitem" autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                    	<td nowrap="nowrap"><label>账户：</label></td>
	                    <td>
	                        <select class="easyui-combobox" id="accTypeCode" style="width:150px" name="accTypeCode" >
	                            <option value="" >全部</option>
	                            <option value="<%=AccountConstants.AccountTypeEnum.PLATFORM_ACCOUNT.getValue()%>" >平台收支账户</option>
	                            <option value="<%=AccountConstants.AccountTypeEnum.PLATFORM_PAYMENT.getValue()%>" >平台逾期垫付资金账户</option>
	                            <option value="<%=AccountConstants.AccountTypeEnum.PLATFORM_RISK.getValue()%>" >平台风险准备金账户</option>
	                            <option value="<%=AccountConstants.AccountTypeEnum.PLATFORM_OPERATING.getValue()%>" >平台运营账户</option>
	                        </select>
	                    </td>
                    	<td nowrap="nowrap"><label>收/支：</label></td>
	                    <td>
	                        <select class="easyui-combobox" id="changeType" style="width:150px" name="changeType" >
	                            <option value="" >全部</option>
	                            <option value="1">收入</option>
	                            <option value="2">支出</option>
	                            <option value="3">冻结资金支出</option>
	                            <option value="4">冻结</option>
	                            <option value="5">解冻</option>
	                        </select>
	                    </td>
                    	<td nowrap="nowrap"><label>费用类型：</label></td>
	                    <td>
	                        <select class="easyui-combobox" id="busType" style="width:150px" name="busType" >
	                            <option value="" >全部</option>
	                            <c:forEach items="${businessType}" var="business">
					            	<option value="${business.value}">${business.desc}</option>
					            </c:forEach>
	                        </select>
	                    </td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap"><label>创建时间：</label>
                        </td>
                        <td colspan="4">
                            <input id="startDate" name="startDate"  readonly="readonly"  class="easyui-datebox"/>至<input id="endDate" name="endDate" readonly="readonly" class="easyui-datebox"/>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQuerySystemFlowList();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="crash_flow" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <table id="systemFlow_list"></table>
    </div>

    <script language="javascript">
        /**
         * 执行：列表加载。
         */
        function loadCrashList(){
            $("#systemFlow_list").datagrid({
                title: '平台账户流水',
                url: '${ctx}/cashFlow/getSystemFlowList',
                pagination: true,
                pageSize: 10,
                singleSelect: true,
                rownumbers: true,
                columns:[[
                    {field:'changeTime', width:140,title:'创建时间' ,formatter:dateTimeFormatter},
                    {field:'changeType', width:150,title:'收/支'},
                    {field:'busType', width:150,title:'费用类型'},
                    {field:'changeValue2', width:150,title:'金额（2位）'},
                    {field:'changeValue', width:150,title:'金额（18位）'},
                    {field:'valueAfter', width:150,title:'当前账户余额'},
                    {field:'desc', width:400,title:'备注'}
                ]],
                onBeforeLoad: function (value, rec) {
                    var changeType = $(this).datagrid("getColumnOption", "changeType");
                    if (changeType) {
                    	changeType.formatter = function (value, rowData, rowIndex) {
                            if (value == '1') {
                                return "<font style='color: green;'>收入</font>";
                            } else if(value == '2') {
                                return "<font style='color: red;'>支出</font>";
                            } else if(value == '3') {
                                return "冻结资金支出";
                            } else if(value == '4') {
                                return "冻结";
                            } else if(value == '5') {
                                return "解冻";
                            }
                        }
                    }
                    var busType = $(this).datagrid("getColumnOption", "busType");
                    if (busType) {
                    	busType.formatter = function (value, rowData, rowIndex) {
                    		<c:forEach items="${businessType}" var="business">
                    			if('${business.value}' == value){
                    				return '${business.desc}'
                    			}
				            </c:forEach>
                        }
                    }
                }
            });
        }
        
        function toQuerySystemFlowList(){
        	$('#systemFlow_list').datagrid('load', {
        		accTypeCode: $('#accTypeCode').combobox('getValue'),
                changeType: $('#changeType').combobox('getValue'),
                busType: $('#busType').combobox('getValue'),
                realName: $('#realName').val(),
                startDate: $('#startDate').datebox('getValue'),
                endDate: $('#endDate').datebox('getValue')
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
            loadCrashList();
        });
    </script>

</body>