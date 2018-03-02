<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<body>

<div class="cf" style=" width:900px;margin:0 10px 5px 10px;">
  <fieldset style="height:80px">
    <legend>查询条件</legend>
    <form name="bondSourceUserQuery" action="${ctx}/bondSource/bondSourceUserlist" class="fitem"
          autocomplete="off">
      <table>
        <tbody>
        <tr>
          <td nowrap="nowrap"><label>操作类型：</label>
          </td>
          <td>
            <select class="easyui-combobox" id="operate" style="width:150px" name="withDrawStatus" >
              <option value="" >全部</option>
              <c:forEach items="${operate}" var="opt">
                <option value="${opt.value}">${opt.desc}</option>
              </c:forEach>
            </select>
          </td>
          <td nowrap="nowrap"><label>费用类型：</label>
          </td>
          <td>
            <select class="easyui-combobox" id="businessType" style="width:150px" name="withDrawStatus" >
              <option value="" >全部</option>
              <c:forEach items="${businessType}" var="business">
                <option value="${business.value}">${business.desc}</option>
              </c:forEach>
            </select>
          </td>
          <td nowrap="nowrap"><label>时间：</label>
          </td>
          <td>
            <input id="c_startDate" name="startDate" style="width:100px;" class="easyui-datebox"/>至<input id="c_endDate" style="width:100px;" name="endDate" class="easyui-datebox"/>
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

<div id="crashflow" class="container-fluid" style="padding: 5px 0px 0px 10px">

  <table id="crashflow_list"></table>
</div>
<div style="margin-bottom: 70px;"></div>
<script language="javascript">
  function toQueryCrashFlowList(){
    $('#crashflow_list').datagrid('reload', {
      'startDate' : Trim($('#c_startDate').datebox('getValue'),"g"),
      'endDate' : Trim($('#c_endDate').datebox('getValue'),"g"),
      'loanApplicationId':${loanApplicationId},
      'businessType':Trim($("#businessType").combobox("getValue"),"g"),
      'operate':Trim($("#operate").combobox("getValue"),"g"),
    });
  }
  /**
   * 执行：列表加载。
   */
  function loadCrashFlowList(){
    $("#crashflow_list").datagrid({
      title: '账务流水列表',
      url: '${ctx}/cashFlow/crashFlowLoanList?loanApplicationId='+'${loanApplicationId}',
      pagination: true,
      pageSize: 10,
      singleSelect: true,
      rownumbers: true,
      columns:[[
        {field:'changeTime', width:125,title:'时间' ,formatter:dateTimeFormatter},
        {field:'changeType', width:60,title:'流水类型'},
        {field:'busType', width:60,title:'费用类型'},
        {field:'changeValue2', width:60,title:'收入/支出',formatter:inOrOutComeValue},
        {field:'changeValueBak', width:60,title:'冻结/解冻',formatter:freezeValue},
        {field:'valueAfter', width:60,title:'账户余额'},
        {field:'desc', width:330,title:'备注'},
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
    loadCrashFlowList();
  });
</script>

</body>