<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

    <div class="cf" style="width:900px;margin:0 10px 5px 10px;">
        <fieldset style="height:80px">
            <legend>查询条件</legend>
            <form name="bondSourceUserQuery" id="" action="" class="fitem"
                  autocomplete="off">
                <input type="hidden" id="userId"  name="userId" value="${userId}">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>流水类型：</label>
                        </td>
                        <td align="left">
                            <input id="selectAll"  type="checkbox" name="changeType" value="0"/><span style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">全部</span>
                            <input onclick="checkBoxValue()" type="checkbox" name="changeType" value="1"/><span style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">收入</span>
                            <input onclick="checkBoxValue()" type="checkbox" name="changeType" value="2"/><span style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">支出</span>
                            <input onclick="checkBoxValue()" type="checkbox" name="changeType" value="3"/><span style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">冻结</span>
                            <input onclick="checkBoxValue()" type="checkbox" name="changeType" value="4"/><span style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">解冻</span>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap"><label>创建时间：</label>
                        </td>
                        <td>
                            <input id="startDate" name="startDate"  readonly="readonly"  class="easyui-datebox"/>至<input id="endDate" name="endDate" readonly="readonly" class="easyui-datebox"/>
                            <a onclick="selectDate(0)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">今天</a>
                            <a onclick="selectDate(1)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近7天</a>
                            <a onclick="selectDate(2)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近3个月</a>
                            <a onclick="selectDate(3)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近6个月</a>
                            <a onclick="selectDate(4)" style="margin-bottom: 5px;font-size: 12px;font-weight: normal;line-height: 20px;">近12个月</a>
                        </td>
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryCrashFlowList();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="crash_flow" class="container-fluid" style="padding: 5px 0px 0px 10px">

        <table id="crash_flow_list${userId}"></table>
    </div>

    <script language="javascript">
        function checkBoxValue(){

            var boxes = $('input[type="checkbox"]');
            var changeType="";
           for(var i=0;i<boxes.length;i++){
                if(boxes[i].checked)
                    changeType +=boxes[i].value;
           }
            if(changeType.indexOf(1234)!=-1)
                $("#selectAll").attr("checked","checked");
            else
                $("#selectAll").removeAttr("checked");

            toQueryCrashFlowList();
        }

        function selectDate(val){
        	Utils.selectDate(val,'startDate','endDate',toQueryCrashFlowList); 
        }

        function toQueryCrashFlowList(){
            var boxes = $('input[type="checkbox"]');
            var changeType="";
            for(var i=0;i<boxes.length;i++){
                if(boxes[i].checked)
                    changeType +=boxes[i].value+",";
            }
            if(changeType.indexOf(0)!=-1){
                changeType='0';
            }
            $('#crash_flow_list${userId}').datagrid('reload', {
                'startDate' : Trim($('#startDate').datebox('getValue'),"g"),
                'endDate' : Trim($('#endDate').datebox('getValue'),"g"),
                'changeType' : Trim(changeType,"g"),
                'userId':${userId}
            });
        }
        /**
         * 执行：列表加载。
         */
        function loadCrashList(){
            $("#crash_flow_list${userId}").datagrid({
                title: '账户流水',
                url: '${ctx}/cashFlow/crashFlowList?userId='+'${userId}&r_'+Math.random(),
                pagination: true,
                pageSize: 10,
                singleSelect: true,
                rownumbers: true,
                columns:[[
                    {field:'changeTime', width:140,title:'创建时间' ,formatter:dateTimeFormatter},
                    {field:'changeType', width:100,title:'流水类型'},
                    {field:'busType', width:60,title:'费用类型'},
                    {field:'changeValue2', width:60,title:'收入/支出',formatter:inOrOutComeValue},
                    {field:'changeValueBak', width:60,title:'冻结/解冻',formatter:freezeValue},
                    {field:'valueAfter', width:60,title:'账户余额'},
                    {field:'desc', width:230,title:'备注'},
                ]],
            });
        }

        function freezeValue(val, row, index){

            var dj = /冻结/;
            var jd = /解冻/;
            if(dj.test(row.changeType)){
                return "<font style='color: red;'>"+val+"</font>";
            }else if(jd.test(row.changeType)){
                return "<font style='color: green;'>"+val+"</font>";
            }else{
                return "";
            }
        }

        function inOrOutComeValue(val, row, index){
            if (val == undefined || val == "")
                return "";
            var sr = /收入/;
            var zc = /支出/;

            var result = sr.test(row.changeType);
            if(result){
                return "<font style='color: green;'>+"+val+"</font>";
            }else if(zc.test(row.changeType)){
                return "<font style='color: red;'>-"+val+"</font>";
            }else{
                return "";
            }

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

            $("#selectAll").click(function() {
                $('input[name="changeType"]').attr("checked",this.checked);
                $(this).attr("checked",this.checked);
                toQueryCrashFlowList();
            });

            $("#startDate").datebox({editable:false});
            $("#endDate").datebox({editable:false});
        });
    </script>

</body>