<%@ page import="com.xt.cfp.core.constants.RateEnum"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../../common/common.jsp" %>
<head>
</head>
<%
    //状态
    RateEnum.RateUserStatusEnum[] rateUserStatus = RateEnum.RateUserStatusEnum.values();
    request.setAttribute("rateUserStatus", rateUserStatus);
    
    //使用场景
    RateEnum.RateProductScenarioEnum[] rateProductScenario = RateEnum.RateProductScenarioEnum.values();
    request.setAttribute("rateProductScenario", rateProductScenario);
%>
<body>
    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px;with:1000px;">
            <legend>查询条件</legend>
            <form method="post" id="rateUser_form" action="${ctx}/jsp/rate/user/exportExcel" class="form-inline">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>用户名：</label></td>
                        <td align="center">
                        	<input id="userName" name="userName" value="" type="text" />
                        </td>

                        <td nowrap="nowrap"><label>手机号：</label></td>
                        <td align="center">
                        	<input id="mobileNo" name="mobileNo" value="" type="text" />
                        </td>
                        
                        <td nowrap="nowrap"><label>使用场景：</label></td>
                        <td>
                            <select id="usageScenario" class="easyui-combobox" name="usageScenario" style="width:160px;">
                                <option value="">全部</option>
                                <c:forEach items="${rateProductScenario}" var="v">
                                    <option value="${v.value}">${v.desc}</option>
                                </c:forEach>
                            </select>
                        </td>

                        <td nowrap="nowrap"><label>状态：</label></td>
                        <td>
                            <select id="status" class="easyui-combobox" name="status" style="width:160px;">
                                <option value="">全部</option>
                                <c:forEach items="${rateUserStatus}" var="v">
                                    <option value="${v.value}">${v.desc}</option>
                                </c:forEach>
                            </select>
                        </td>

                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryRateUser();" iconCls="icon-search">查询</a>
								
                                <a href="javascript:doExport()" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-save'">导出Excel</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="rateUser_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="rateUser_list_toolbar" style="height:auto">

            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addRateUser();">发放加息券</a>
        </div>

        <table id="rateUser_list_list"></table>
    </div>

    <div id="detail"></div>
    
    <script language="javascript">
    	//导出查询数据
        function doExport(){
            $("#rateUser_form").submit();
        }

    	//查询
        function toQueryRateUser(){
            $('#rateUser_list_list').datagrid('reload', {
                'userName' : Trim($('#userName').val(),"g"),
                'mobileNo' : Trim($('#mobileNo').val(),"g"),
                'status' : Trim($("#status").combobox("getValue"),"g"),
                'usageScenario' : Trim($("#usageScenario").combobox("getValue"),"g")
            });
        }
        
    	//列表加载
        function loadList(){
            $("#rateUser_list_list").datagrid({
                idField: 'rateUserId',
                title: '加息券发放列表',
                url: '${ctx}/jsp/rate/user/list',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#rateUser_list_toolbar',
                columns:[[
                    {field:'rateUserId', width:60, title:'ID'},
                    {field:'loginName', width:120, title:'用户名'},
                    {field:'realName', width:70, title:'姓名'},
                    {field:'mobileNo', width:90, title:'手机号'},
                    {field:'rateProductName', width:200, title:'加息券名称'},
                    {field:'rateValue', width:80, align:'center', title:'加息利率(%)'},
                    {field:'usageScenarioStr', width:80, align:'center', title:'使用场景'},
                    {field:'conditionStr', width:410, title:'使用条件'},
                    {field:'totalTimes', width:60, align:'center', title:'次数限制',
                    	formatter:function(value,row,index){
                    		if(value > 0){
                    			return value;
                    		}else{
                    			return '无限制';
                    		}
                    	}	
                    },
                    {field:'startDate', width:90, align:'center', title:'发放时间',
                    	formatter:function(value,row,index){
                    		if(value != null && value != "")
                        		return getDateStr(new Date(value));
                    	}	
                    },
                    {field:'endDate', width:90, align:'center', title:'失效时间',
                    	formatter:function(value,row,index){
                    		if(value != null && value != "")
                        		return getDateStr(new Date(value));
                    	}	
                    },
                    {field:'statusStr', width:80, align:'center', title:'状态'},
                    {field:'action',title:'操作',width:80,align:'center',
                        formatter:function(value,row,index){

                            var result = "";
                            result += "<a class='label label-info' onclick='detail(" + row.rateUserId + ")'>查看</a> &nbsp;";
                            return result;
                            
                        }
                    }
                ]]
            });
        }

	  //发放加息券
      function addRateUser() {
          $("#rateUser_list").after("<div id='addRateUser' style=' padding:10px; '></div>");
          $("#addRateUser").dialog({
              resizable: false,
              title: '发放加息券',
              href: '${ctx}/jsp/rate/user/to_add',
              width: 900,
              height: 450,
              modal: true,
              top: 100,
              left: 200,
              buttons: [
                  {
                      text: '保存',
                      iconCls: 'icon-ok',
                      handler: function () {
                          var form = $("#addRateUser").contents().find("#addRateUser_form");
                          var validate = form.form('validate');

                          if(validate){
                              $.messager.progress({
                                  title:'请稍后',
                                  msg:'页面加载中...'
                              });

                              $.ajax({
                                  url:'${ctx}/jsp/rate/user/add',
                                  data:form.serialize(),
                                  type:"POST",
                                  success:function(data){
                                      $.messager.progress('close');
                                      if(data == 'success'){
                                          $.messager.alert('提示', '保存成功！', 'info',function(){
                                              $("#addRateUser").dialog('close');
                                              toQueryRateUser();
                                          });
                                      }else
                                          $.messager.alert('提示', '保存失败！'+msg, 'info');
                                  }
                              });
                          }
                      }
                  },
                  {
                      text: '取消',
                      iconCls: 'icon-cancel',
                      handler: function () {
                          $("#addRateUser").dialog('close');
                      }
                  }
              ],
              onClose: function () {
                  $(this).dialog('destroy');
              }
          });
      }
		//详情
        function detail(id){
            $("#detail").after("<div id='rateUser_detail' style=' padding:10px; '></div>");
            $("#rateUser_detail").dialog({
                resizable: false,
                title: '加息券信息',
                href: '${ctx}/jsp/rate/user/detail?rateUserId='+id,
                width: 650,
                height: 650,
                modal: true,
                top: 50,
                left: 400,
                buttons: [
                    {
                        text: '确定',
                        iconCls: 'icon-ok',
                        handler: function () {
                            $("#rateUser_detail").dialog('close');
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
