<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

<div class="cf" style="float:left; width:900px;margin:0 10px 5px 10px;">
  <fieldset style="height:65px">
    <legend>查询条件</legend>
    <form name="constantDefineQuery" id="constantDefineQuery" action="${ctx}/constantDefine/list" class="fitem"
          autocomplete="off">
      <table>
        <tbody>
        <tr>
          <td nowrap="nowrap"><label>常量名称：</label>
          </td>
          <td align="center"><input id="constantName"
                                    name="constantDefine.constantName" value=""
                                    type="text" />
          </td>
          <td nowrap="nowrap"><label>常量类型：</label>
          </td>
          <td><input id="constantType"
                     name="constantDefine.constantType" value=""
                     type="text" />
          </td>
          <td nowrap="nowrap"><label>常量类型编码：</label>
          </td>
          <td><input id="constantTypeCode"
                     name="constantDefine.constantTypeCode" value=""
                     type="text" />
          </td>
          <td nowrap="nowrap">
            <div style="margin:10px;">
              <a href="javascript:void(0);" class="easyui-linkbutton"
                 onclick="javascript:toQueryConstant();" iconCls="icon-search">查询</a>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </form>
  </fieldset>
</div>

<div id="constant_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
  <div id="constant_list_toolbar" style="height:auto">

    <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="toAddConstant();">新增</a>
  </div>

  <table id="constant_list_list"></table>
</div>

<div id="detailBondSource">

</div>
<script language="javascript">

  function toQueryConstant(){
    $('#constant_list_list').datagrid('reload', {
      'constantName' : Trim($("#constantName").val(),"g"),
      'constantType' : Trim($("#constantType").val(),"g"),
      'constantTypeCode':Trim($("#constantTypeCode").val(),"g")
    });
  }
  /**
   * 执行：列表加载。
   */
  function loadList(){
    $("#constant_list_list").datagrid({
      idField: 'constantDefineId',
      title: '字典列表',
      url: '${ctx}/constant/showConstantList',
      pagination: true,
      pageSize: 10,
      width: document.body.clientWidth * 0.97,
      height: document.body.clientHeight * 0.6,
      singleSelect: true,
      rownumbers: true,
      toolbar: '#constant_list_toolbar',
      columns:[[
        {field:'constantName', width:100,title:'常量名称'},
        {field:'constantType', width:120,title:'常量类型'},
        {field:'constantTypeCode', width:80,title:'常量类型编码'},
        {field:'constantValue', width:80,title:'常量值'},
        {field:'constantStatus', width:80,title:'状态',formatter:function(value,row,index){
          var result = "";
          if(row.constantStatus=='0')
                return "有效";
          else
              return "无效";
        }},
        {field:'parentConstant', width:60,title:'上级常量'},
        {field:'action',title:'操作',width:200,align:'center',
          formatter:function(value,row,index){
            var result = "";
            result += "<a id='rechargeButton' class='label label-info' onclick='addConstantChild(" + row.constantDefineId  + ")'>新增子常量</a> &nbsp;";
            result += "<a class='label label-info' onclick='editConstant(" + row.constantDefineId + ")'>编辑</a> &nbsp;";
            return result;
          }
        }
      ]],
      onBeforeLoad: function (value, rec) {
        var adminState = $(this).datagrid("getColumnOption", "parentConstant");
        if (adminState) {
          adminState.formatter = function (value, rowData, rowIndex) {
            if (value == '0') {
              return "<font style='color: green;'></font>";
            } else{
              return value;
            }
          }
        }
        var sourceName = $(this).datagrid("getColumnOption", "sourceName");
        if (sourceName) {
          sourceName.formatter = function (value, rowData, rowIndex) {
            return "<a onclick='showDetails(" + rowData.bondSourceId  + ")'>"+value+"</a>";
          }
        }
      }
    });
  }


  /**
   * 执行：弹出添加层。
   */
  function toAddConstant() {
    $("#constant_list").after("<div id='toAddConstant' style=' padding:10px; '></div>");
    $("#toAddConstant").dialog({
      resizable: false,
      title: '新增常量',
      href: '${ctx}/constant/add',
      width: 500,
      height: 250,
      modal: true,
      top: 100,
      left: 200,
      buttons: [
        {
          text: '保存',
          iconCls: 'icon-ok',
          handler: function () {
            var form = $("#toAddConstant").contents().find("#toAddConstant_form");
            var validate = form.form('validate');
            if(validate){
              $.ajax({
                url:'${ctx}/constant/save',
                data:form.serialize(),
                type:"POST",
                success:function(msg){
                  if(msg=='success'){
                    $.messager.alert('提示', '保存成功！！', 'info',function(){
                      $("#toAddConstant").dialog('close');
                      toQueryConstant();
                    });
                  }else
                    $.messager.alert('提示', '保存失败！！'+msg, 'info');
                }
              });
            }

          }
        },
        {
          text: '取消',
          iconCls: 'icon-cancel',
          handler: function () {
            $("#toAddConstant").dialog('close');
          }
        }
      ],
      onClose: function () {
        $(this).dialog('destroy');
      }
    });
  }


  function addConstantChild(index){
    $("#constant_list").after("<div id='toAddConstant' style=' padding:10px; '></div>");
    $("#toAddConstant").dialog({
      resizable: false,
      title: '编辑常量',
      href: '${ctx}/constant/add?addChild=true&constantDefineId='+index,
      width: 500,
      height: 250,
      modal: true,
      top: 100,
      left: 200,
      buttons: [
        {
          text: '保存',
          iconCls: 'icon-ok',
          handler: function () {
            var form = $("#toAddConstant").contents().find("#toAddConstant_form");
            $.ajax({
              url:'${ctx}/constant/save',
              data:form.serialize(),
              type:"POST",
              success:function(msg){
                if(msg=='success'){
                  $.messager.alert('提示', '保存成功！！', 'info',function(){
                    $("#toAddConstant").dialog('close');
                    toQueryConstant();
                  });
                }else
                  $.messager.alert('提示', '保存失败！！'+msg, 'info');
              }
            });
          }
        },
        {
          text: '取消',
          iconCls: 'icon-cancel',
          handler: function () {
            $("#toAddConstant").dialog('close');
          }
        }
      ],
      onClose: function () {
        $(this).dialog('destroy');
      }
    });
  }
  /**
   * 执行：弹出修改层。
   */
  function editConstant(index) {
    $("#constant_list").after("<div id='toAddConstant' style=' padding:10px; '></div>");
    $("#toAddConstant").dialog({
      resizable: false,
      title: '编辑常量',
      href: '${ctx}/constant/add?constantDefineId='+index,
      width: 500,
      height: 250,
      modal: true,
      top: 100,
      left: 200,
      buttons: [
        {
          text: '保存',
          iconCls: 'icon-ok',
          handler: function () {
            var form = $("#toAddConstant").contents().find("#toAddConstant_form");
            $.ajax({
              url:'${ctx}/constant/save',
              data:form.serialize(),
              type:"POST",
              success:function(msg){
                if(msg=='success'){
                  $.messager.alert('提示', '保存成功！！', 'info',function(){
                    $("#toAddConstant").dialog('close');
                    toQueryConstant();
                  });
                }else
                  $.messager.alert('提示', '保存失败！！'+msg, 'info');
              }
            });
          }
        },
        {
          text: '取消',
          iconCls: 'icon-cancel',
          handler: function () {
            $("#toAddConstant").dialog('close');
          }
        }
      ],
      onClose: function () {
        $(this).dialog('destroy');
      }
    });
  }



  $(function(){
    loadList();
  });
</script>

</body>