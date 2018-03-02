<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

    <div class="cf" style="float:left; width:900px;margin:0 10px 5px 10px;">
        <div id="p" class="easyui-panel" title="出借人数据报表"
             style="width:1000px;height:150px;padding:10px;background:#fafafa;"
             data-options="iconCls:'icon-save',closable:false,
                    collapsible:true,minimizable:false,maximizable:false">
            <form method="post" id="lender_info_form" action="${ctx}/report/exportExcel" class="form-inline">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>出借时间：</label>
                        </td>
                        <td>
                            <input id="w_startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>至<input id="w_endDate" style="width:100px;" name="endDate" class="easyui-datebox"/>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                           <%--     <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryBondSource();" iconCls="icon-search">查询</a>--%>
                               <a href="javascript:doExport()" class="easyui-linkbutton"
                                  data-options="iconCls:'icon-save'">导出Excel</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    
    <div class="cf" style="float:left; width:900px;margin:0 10px 5px 10px;">
        <div id="p" class="easyui-panel" title="加盟商数据报表"
             style="width:1000px;height:190px;padding:10px;background:#fafafa;"
             data-options="iconCls:'icon-save',closable:false,
                    collapsible:true,minimizable:false,maximizable:false">
            <form method="post" id="alliance_form" action="${ctx}/report/exportAllianceExcel" class="form-inline">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>统计日期：</label>
                        </td>
                        <td>
                            <input id="alliance_form_date"  name="alliance_form_date" style="width:100px;" class="easyui-datebox"/>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:doAllianceExport()" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-save'">导出Excel</a>
                            </div>
                        </td>
                    </tr>
                 
                    </tbody>
                </table>
            </form>
              <form method="post" id="between_date_form" action="${ctx}/report/exportAllianceExcelBetween" class="form-inline">
                <table>
                    <tbody>
                      <tr>
                        <td nowrap="nowrap"><label>截止日期：</label>
                        </td>
                        <td>
                            <input id="between_startDate" name="startDate" editable="false" style="width:100px;" class="easyui-datebox"/>至<input id="between_endDate" editable="false" style="width:100px;" name="endDate" class="easyui-datebox"/>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:doAllianceExportBetween()" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-save'">导出Excel</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    
    <!-- 移动端报表开始 -->
    <div class="cf" style="float:left; width:900px;margin:0 10px 5px 10px;">
        <div id="p" class="easyui-panel" title="（充值 / 投资 / 提现）数据报表" style="width:900px;height:200px;padding:10px;background:#fafafa;" data-options="iconCls:'icon-save',closable:false,collapsible:true,minimizable:false,maximizable:false">
            <form method="post" id="mobile_info_form" action="${ctx}/report/exportMobileInfoExcel" class="form-inline">
                <table>
                    <tbody>
                    	<tr>
	                        <td nowrap="nowrap">
	                        	<label>（充值 / 投资 / 提现）时间：</label>
	                        </td>
	                        <td>
	                            <input id="mobile_startDate" name="mobile_startDate" style="width:100px;" class="easyui-datebox"/><label>至</label>
	                            <input id="mobile_endDate" name="mobile_endDate" style="width:100px;" class="easyui-datebox"/>
	                        </td>
	                        <td nowrap="nowrap">
	                            <div style="margin:10px;">
	                               <a href="javascript:doMobileInfoExport()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">导出Excel</a>
	                            </div>
	                        </td>
                        </tr>
                        <tr>
	                        <td nowrap="nowrap" style="text-align: right;">
	                        	<label>数据类型：</label>
	                        </td>
	                        <td>
                            	<label>充值</label><input checked="checked" type="radio" id="exportType" name="exportType" value="1">&nbsp;&nbsp;
           						<label>投资</label><input type="radio" id="exportType" name="exportType" value="0">&nbsp;&nbsp;
      							<label>提现</label><input type="radio" id="exportType" name="exportType" value="2">&nbsp;&nbsp;
	                        </td>
	                        <td nowrap="nowrap">
	                            
	                        </td>
                        </tr>
                        <tr>
	                        <td nowrap="nowrap" style="text-align: right;">
	                        	<label>来源类型：</label>
	                        </td>
	                        <td>
                            	<label>PC</label><input checked="checked" type="radio" id="sourceType" name="sourceType" value="1">&nbsp;&nbsp;
           						<label>Wechat</label><input type="radio" id="sourceType" name="sourceType" value="2">&nbsp;&nbsp;
      							<label>Android</label><input type="radio" id="sourceType" name="sourceType" value="3">&nbsp;&nbsp;
      							<label>IOS</label><input type="radio" id="sourceType" name="sourceType" value="4">&nbsp;&nbsp;
	                        </td>
	                        <td nowrap="nowrap">
	                            
	                        </td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </div>
    <!-- 移动端报表结束 -->
    
    <script language="javascript">

        function doExport(){
            $("#lender_info_form").submit();
        }

        function doAllianceExport() {
            $("#alliance_form").submit();
        }
        
        function doAllianceExportBetween(){
        	var startDate=$('#between_startDate').datebox('getValue');
        	var endDate=$('#between_endDate').datebox('getValue');
        	if(startDate!=null&&startDate!=""&&endDate!=null&&endDate!=""){
        		  $("#between_date_form").submit();
        	} 
        }
        
        // 移动端数据导出
        function doMobileInfoExport(){
        	var startDate = $('#mobile_startDate').datebox('getValue');
        	var endDate = $('#mobile_endDate').datebox('getValue');
        	if(startDate == null || startDate == "" || endDate == null || endDate == ""){
        		$.messager.alert("提示", "请填写完整的开始和结束时间！", "info");
        		return;
        	}else if(startDate > endDate){
        		$.messager.alert("提示", "开始时间不能早于结束时间！", "info");
        		return;
        	}
            $("#mobile_info_form").submit();
        }
    </script>

</body>
