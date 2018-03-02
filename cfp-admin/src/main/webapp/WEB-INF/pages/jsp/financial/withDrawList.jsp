<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

<div class="cf" style="width:1200px;margin:0 10px 5px 10px;">
    <mis:PermisTag code="4000201">
    <fieldset style="height:100px">
        <legend>查询条件</legend>
        <form name="bondSourceUserQuery" action="${ctx}/bondSource/bondSourceUserlist" class="fitem"
              autocomplete="off">
            <table>
                <tbody>
                <tr>
                    <td nowrap="nowrap"><label>开户名：</label>
                    </td>
                    <td align="center"><input id="operateName"
                                              name="operateName" value=""
                                              type="text" />
                    </td>
                    <td nowrap="nowrap"><label>提交时间：</label>
                    </td>
                    <td>
                        <input id="startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>
                        <font style="font-size: 12px;">至</font>
                        <input id="endDate" style="width:100px;" name="endDate" class="easyui-datebox"/>
                    </td>
                    <td nowrap="nowrap"><label>审核结果：</label>
                    </td>
                    <td>
                        <select class="easyui-combobox" id="withDrawStatus" style="width:150px" name="withDrawStatus" >
                            <option value="" >全部</option>
                            <option value="0">待审核</option>
                            <option value="1">审核通过</option>
                            <option value="2" >驳回</option>
                        </select>
                    </td>
                </tr>
                <tr>
                	<td nowrap="nowrap"><label>打款结果：</label>
                    </td>
                    <td>
                        <select class="easyui-combobox" id="tranStatus" style="width:150px" name="tranStatus" >
                            <option value="" >全部</option>
                            <option value="0" >未打款</option>
                            <option value="1" >未提交</option>
                            <option value="2" >处理中</option>
                            <option value="3" >打款成功</option>
                            <option value="4" >打款失败</option>
                        </select>
                    </td>
                    
                    <td nowrap="nowrap"><label>申请来源：</label>
                    </td>
                    <td>
                        <select class="easyui-combobox" id="orderResource" style="width:150px" name="orderResource" >
                            <option value="" >全部</option>
                            <option value="PC" >web</option>
                            <option value="Wechat" >微信</option>
                            <option value="Andriod" >安卓</option>
                            <option value="IOS" >苹果</option>
                        </select>
                    </td>
                    
                    <td nowrap="nowrap"><label>支付渠道：</label>
                    </td>
                    <td>
                        <select class="easyui-combobox" id="belongChannel" style="width:150px" name="belongChannel" >
                            <option value="" >全部</option>
                            <option value="0" >易宝</option>
                            <option value="1" >连连</option>
                        </select>
                    </td>
                    <td nowrap="nowrap"></td>
                    <td>
                         <a href="javascript:void(0);" class="easyui-linkbutton" onclick="toQueryList();" iconCls="icon-search">查询</a>&nbsp;&nbsp;&nbsp;
                         <a href="javascript:doExport();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">查询结果导出Excel</a>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </fieldset>
    </mis:PermisTag>
</div>

<div id="withDraw" class="container-fluid" style="padding: 5px 0px 0px 10px">
    <div id="withDraw_list_toolbar" style="height:auto;display:none;">
 	<mis:PermisTag code="04000203">
       		 <a href="${ctx}/withdraw/exportExcel"  class="easyui-linkbutton" iconCls="icon-save" plain="true">导出待打款</a>
        </mis:PermisTag>
        <mis:PermisTag code="04000202">
       		 <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="importExcel();">导入打款结果</a>
        </mis:PermisTag>
    </div>
    <table id="withDraw_list"></table>
</div>

<!-- 导出报表表单开始 -->
<form method="post" class="form-inline" id="withDrawForm" action="${ctx}/report/exportWithDrawListExcel">
	<input type="hidden" id="export_startDate" name="export_startDate">
    <input type="hidden" id="export_endDate" name="export_endDate">
    <input type="hidden" id="export_operateName" name="export_operateName">
    <input type="hidden" id="export_verifyStatus" name="export_verifyStatus">
    <input type="hidden" id="export_transStatus" name="export_transStatus">
    <input type="hidden" id="export_belongChannel" name="export_belongChannel">
</form>
<!-- 导出报表表单结束 -->

<script language="javascript">

    function importExcel(){

        $("#withDraw").after("<div id='withDrawImport' style=' padding:10px; '></div>");
        $("#withDrawImport").dialog({
            resizable: false,
            title: '导入打款结果',
            href: '${ctx}/withdraw/toImportExcel',
            width: 700,
            height: 170,
            modal: true,
            top: 100,
            left: 200,
            buttons: [
                {
                    text: '导入',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var form = $("#withDrawImport").contents().find("#importExcel");
                        form.submit();
                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#withDrawImport").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });

    }

    function verify(id){
        $("#withDraw").after("<div id='verify' style=' padding:10px; '></div>");
        $("#verify").dialog({
            resizable: false,
            title: '提现审核',
            href: '${ctx}/withdraw/verify?withdrawId='+id,
            width: 500,
            height: 550,
            modal: true,
            top: 50,
            left: 400,
            buttons: [
                {
                    text: '确定',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var form = $("#verify").contents().find("#doVerify_form");
                        $.ajax({
                            url:'${ctx}/withdraw/doVerify',
                            data:form.serialize(),
                            type:"POST",
                            success:function(msg){
                                if(msg=='success'){
                                    $.messager.alert('提示', '提交成功！！', 'info',function(){
                                        $("#verify").dialog('close');
                                        toQueryList();
                                    });
                                }else
                                    $.messager.alert('提示', '提交失败！！'+msg+'', 'info');
                            }
                        });

                    }
                },
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#verify").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    }

    function closeDialog(){
        $.messager.alert('提示', '导入成功！！', 'info', function () {
            $("#withDrawImport").dialog('close');
            toQueryList();
        });
    }

    //确认打款
    function withDraw(id){
        $.messager.progress({
            title:'请稍后',
            msg:'页面加载中...'
        });
        $.ajax({
            url:'${ctx}/withdraw/doWithDraw?withDrawId='+id,
            type:"POST",
            success:function(msg){
                $.messager.progress('close');
                if(msg=='success'){
                    $.messager.alert('提示', '操作成功！！', 'info',function(){
                        toQueryList();
                    });
                }else {
                    $.messager.alert('提示', msg, 'info',function(){
                        toQueryList();
                    });
                }
            }
        });
    }

    //刷新
    function wrefresh(id){
        $.ajax({
            url:'${ctx}/withdraw/refresh?withDrawId='+id,
            type:"POST",
            success:function(msg){
//                if(msg=='success'){
                    $.messager.alert('提示',msg, 'info',function(){
                        toQueryList();
                    });
//                }else {
//                    $.messager.alert('提示', '操作失败！！', 'info');
//                }
            }
        });
    }

    //查询
    function toQueryList(){
        $("#withDraw_list").datagrid("reload", {
            "startDate" : Trim($("#startDate").datebox("getValue"),"g"),
            "endDate" : Trim($("#endDate").datebox("getValue"),"g"),
            "operateName" : Trim($("#operateName").val(),"g"),
            "verifyStatus":Trim($("#withDrawStatus").combobox("getValue"),"g"),
            "transStatus":Trim($("#tranStatus").combobox("getValue"),"g"),
            "orderResource":Trim($("#orderResource").combobox("getValue"),"g"),
            "belongChannel":Trim($("#belongChannel").combobox("getValue"),"g")
        });
    }
    /**
     * 执行：列表加载。
     */
    function loadWithDrawList(){
        $("#withDraw_list").datagrid({
            title: '提现列表',
            url: '${ctx}/withdraw/showWithDrawList?bondSourceId='+'${bondSourceId}',
            pagination: true,
            pageSize: 10,
            singleSelect: true,
            rownumbers: true,
            toolbar: '#withDraw_list_toolbar',
            columns:[[
                {field:'withdrawId', width:50,title:'提现单号' },
                {field:'withDrawFlowId', width:150,title:'交易流水号' },
                {field:'operateName', width:80,title:'操作人用户名'},
                {field:'loginName', width:100,title:'平台用户名'},
                {field:'withdrawAmount', width:100,title:'提现金额',align:'right',formatter:formatNumber},
                {field:'commissionFee', width:70,title:'提现手续费',align:'right',formatter:formatNumber},
                {field:'bankName', width:180,title:'开户行'},
                {field:'cardNo', width:150,title:'银行卡号'},
                {field:'realName', width:80,title:'提现用户名'},
                {field:'userName', width:60,title:'开户名'},
                {field:'happenType', width:60,title:'来源',formatter:happenTypeFormatter},
                {field:'resourceDesc', width:60,title:'申请来源',formatter:resourceFormatter},
                {field:'belongChannel', width:60,title:'支付渠道',formatter:belongChannelFormatter},
                {field:'verifyStatus', width:60,title:'审核状态' ,formatter:verifyStatusFormatter},
                {field:'transStatus', width:60,title:'打款状态' ,formatter:transStatusFormatter},
                {field:'createTime', width:130,title:'提交时间' ,formatter:dateTimeFormatter},
                {field:'operateTime', width:130,title:'审核时间' ,formatter:dateTimeFormatter},
                {field:'resultTime', width:130,title:'完成时间' ,formatter:dateTimeFormatter},
                {field:'confirmWithdrawTime', width:130,title:'确认打款时间' ,formatter:dateTimeFormatter},
                {field:'resultDesc', width:180,title:'结果描述'},
                {field:'remark', width:80,title:'操作', formatter:function(value,row,index){
                    if(row.transStatus!=5){
                    	<mis:PermisTag code="04000204">
                        if(row.verifyStatus == '0'){
                            var result = "<a class='label label-info' onclick='verify(" +  row.withdrawId + ")'>审核</a> &nbsp;";
                            return result;
                        }
                        </mis:PermisTag >
                        <mis:PermisTag code="04000205">
                        if(row.verifyStatus == '1'&& (row.transStatus=='0'||row.transStatus=='1')){
                            var result = "<a class='label label-info' onclick='withDraw(" +  row.withdrawId + ")'>确认打款</a> &nbsp;";
                            return result;
                        }
                        </mis:PermisTag >
                        <mis:PermisTag code="04000206">
                        if(row.transStatus=='2'){
                            var result = "<a class='label label-info' onclick='wrefresh(" +  row.withdrawId + ")'>刷新</a> &nbsp;";
                            return result;
                        }
                        </mis:PermisTag >
                    }
                }},
                
            ]],
        });
    }

    function happenTypeFormatter(val){

        if (val == undefined || val == "")
            return "";
        if(val == '2')
            return "用户手动";
        if(val == '3')
            return "系统手动";
        else
            return val;
    }
    
    function resourceFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == 'PC')
            return "web";
        else if(val == 'Wechat')
        	return "微信";
        else if(val == 'Andriod')
        	return "安卓";
        else if(val == 'IOS')
        	return "苹果";
        else
            return val;
    }
    
    function belongChannelFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "易宝";
        else if(val == '1')
        	return "连连";
        else
            return val;
    }

    function verifyStatusFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "待审核";
        if(val == '1')
            return "审核通过";
        if(val == '2')
            return "驳回";
    }

    function transStatusFormatter(val){
        if (val == undefined || val == "")
            return "";
        if(val == '0')
            return "未打款";
        if(val == '1')
            return "未提交";
        if(val == '2')
            return "处理中";
        if(val == '3')
            return "打款成功";
        if(val == '4')
            return "打款失败";
        if(val == '5')
            return "提交失败";
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
    function formatNumber(val) {
        if (val != null) {
            return formatNum(val, 2);
        }
    }
    
    //查询结果导出excel功能
    function doExport(){
    	$.messager.confirm('确认信息','确认要导出Excel吗？(温馨提示：数据导出需要几秒钟，请耐心等待!)',function(r){
			if(r){
                // 处理表单提交
                $("#export_startDate").attr("value", Trim($("#startDate").datebox("getValue"),"g"));
	            $("#export_endDate").attr("value", Trim($("#endDate").datebox("getValue"),"g"));
	            $("#export_operateName").attr("value", Trim($("#operateName").val(),"g"));
	            $("#export_verifyStatus").attr("value", Trim($("#withDrawStatus").combobox("getValue"),"g"));
	            $("#export_transStatus").attr("value", Trim($("#tranStatus").combobox("getValue"),"g"));
	            $("#export_belongChannel").attr("value", Trim($("#belongChannel").combobox("getValue"),"g"));
	            $("#withDrawForm").submit();
			}
    	});
    }

    $(function(){
        loadWithDrawList();
    });
</script>

</body>