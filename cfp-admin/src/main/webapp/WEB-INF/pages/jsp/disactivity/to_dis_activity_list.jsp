<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<head>
</head>
<body>

    <div class="" style="float:left; width:1200px;margin:0 10px 5px 10px;">
        <fieldset style="height:65px">
            <legend>查询条件</legend>
            <form name="voucherProductList" id="voucherProductList" action="${ctx}/disActivity/disActivityList" class="fitem"
                  autocomplete="off">
                <table>
                    <tbody>
                    <tr>
                        <td nowrap="nowrap"><label>分销名称：</label>
                        </td>
                        <td align="center"><input id="dis_Name"
                                   name="disName" value=""
                                   type="text" />
                        </td>
                        <td nowrap="nowrap"><label>分销产品：</label>
                        </td>
                        <td>
                            <select id="dis_Product" class="easyui-combobox" name="disProductId" style="width:100px;">
                                <option value="">请选择</option>
                                <c:forEach items="${disProductList}" var="pro">
                                    <option value="${pro.disProductId}" >${pro.disProductName}</option>
                                </c:forEach>
                            </select>
                        </td>

                        <td nowrap="nowrap"><label>规则时间：</label>
                        </td>
                        <td>
                            <input id="v_ruleStartDate" name="ruleStartDate" style="width:100px;" class="easyui-datebox"/>至
                            <input id="v_ruleEndDate" style="width:100px;" name="ruleEndDate" class="easyui-datebox"/>
                        </td>

 						<td nowrap="nowrap"><label>状态：</label>
                        </td>
                        <td>
                            <select id="v_status" class="easyui-combobox" name="state"
                                    style="width:160px;">
                                <option value="">全部</option>
                                <c:forEach items="${disActivityState}" var="type">
                                    <option value="${type.value}"  >${type.desc}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        
                        <td nowrap="nowrap">
                            <div style="margin:10px;">
                                <a href="javascript:void(0);" class="easyui-linkbutton"
                                   onclick="javascript:toQueryVoucherProduct();" iconCls="icon-search">查询</a>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </fieldset>
    </div>

    <div id="dis_activity_list" class="container-fluid" style="padding: 5px 0px 0px 10px">
        <div id="dis_activity_list_toolbar" style="height:auto">

            <a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDisActivity();">新增</a>
        </div>

        <table id="dis_activity_list_list"></table>
    </div>


    <div id="detail"></div>
    <script language="javascript">

        function toQueryVoucherProduct(){
            $('#dis_activity_list_list').datagrid('reload', {
                'ruleStartDateStr' : Trim($('#v_ruleStartDate').datebox('getValue'),"g"),
                'ruleEndDateStr' : Trim($('#v_ruleEndDate').datebox('getValue'),"g"),
                'state' : Trim($("#v_status").combobox("getValue"),"g"),
                'disName' : Trim($("#dis_Name").val(),"g"),
                'disProductId':Trim($("#dis_Product").combobox("getValue"),"g")
            });
        }
        function loadList(){
            $("#dis_activity_list_list").datagrid({
                idField: 'voucherProductId',//todo ID更改
                title: '分销产品列表',
                url: '${ctx}/disActivity/disActivityList',
                pagination: true,
                pageSize: 10,
                width: document.body.clientWidth * 0.97,
                height: document.body.clientHeight * 0.6,
                singleSelect: true,
                rownumbers: true,
                toolbar: '#dis_activity_list_toolbar',
                columns:[[
                    {field:'sectionCode', width:120,title:'当前期号'},
                    {field:'disName', width:120,title:'分销名称'},
                    {field:'disProductName', width:250,title:'分销产品'},
                    {field:'targetUser', width:100,title:'目标用户',formatter:formatTargetUser},
                    {field:'amountStr', width:200,title:'分销奖励机制'},
                    {field:'ruleStartDate', width:100,title:'规则开始时间',formatter:dateTimeFormatter},
                    {field:'ruleEndDate', width:100,title:'规则结束时间',formatter:dateTimeFormatter},
                    {field:'state', width:80,title:'状态',formatter:formatDisState},
                    {field:'createDate', width:80,title:'录入时间',formatter:dateTimeFormatter},
                    {field:'action',title:'操作',width:200,align:'center',
                        formatter:function(value,row,index){

                            var result = "";
                            
                            if(row.state == '3'){
                            	result += "<a class='label label-info' onclick='detail(" + row.disId + ")'>查看</a> &nbsp;";
                                result += "<a class='label label-info' onclick='publishDisActivity(" + row.disId + ")'>发布</a> &nbsp;";
                                result += "<a class='label label-info' onclick='updateDis(" + row.disId + ")'>修改</a> &nbsp;";
                            }else if(row.state=='2'){
                            	result += "<a class='label label-info' onclick='detail(" + row.disId + ")'>查看</a> &nbsp;";
                                result += "<a class='label label-info' onclick='disabledDisActivity(" + row.disId + ")'>失效</a> &nbsp;";
                            }else{
                            	result += "<a class='label label-info' onclick='detail(" + row.disId + ")'>查看</a> &nbsp;";
                            }
                            return result;
                        }
                    }
                ]]
            });
        }


      function addDisActivity() {
          $("#dis_activity_list").after("<div id='addDisActivity' style=' padding:10px; '></div>");
          $("#addDisActivity").dialog({
              resizable: false,
              title: '新建分销产品',
              href: '${ctx}/disActivity/add',
              width: 900,
              height: 550,
              modal: true,
              buttons: [
                  {
                      text: '保存',
                      iconCls: 'icon-ok',
                      handler: function () {
                          /* var form = $("#addDisActivity").contents().find("#addDisActivity_form");
                          var validate = form.form('validate');

                          if(validate){
                              $.ajax({
                                  url:'${ctx}/disActivity/save',
                                  data:form.serialize(),
                                  type:"POST",
                                  success:function(msg){
                                      if(msg=='success'){
                                          $.messager.alert('提示', '保存成功！！', 'info',function(){
                                              $("#addDisActivity").dialog('close');
                                              toQueryVoucherProduct();
                                          });
                                      }else
                                          $.messager.alert('提示', '保存失败！！'+msg, 'info');
                                  }
                              });
                          } */
                    	  $.messager.confirm('确认信息','确认提交信息？',function(r){
                  			if(r){
		                            $("#addDisActivity").contents().find("#addDisActivity_form").submit();
                  			}
                      	  });
                      }
                  },
                  {
                      text: '取消',
                      iconCls: 'icon-cancel',
                      handler: function () {
                          $("#addDisActivity").dialog('close');
                      }
                  }
              ],
              onClose: function () {
                  $(this).dialog('destroy');
              }
          });
      }

        
        function updateDis(id){
            $("#dis_activity_list").after("<div id='edit_dis_activity' style=' padding:10px; '></div>");
            $("#edit_dis_activity").dialog({
                resizable: false,
                title: '产品修改',
                href: '${ctx}/disActivity/to_edit_activity?disId='+id,
                width: 900,
                height: 550,
                modal: true,
                buttons: [
                          {
                              text: '保存',
                              iconCls: 'icon-ok',
                              handler: function () {
                            	  $.messager.confirm('确认信息','确认提交信息？',function(r){
                          			if(r){
        		                            $("#edit_dis_activity").contents().find("#editDisActivity_form").submit();
                          			}
                              	  });
                              }
                          },
                          {
                              text: '取消',
                              iconCls: 'icon-cancel',
                              handler: function () {
                                  $("#edit_dis_activity").dialog('close');
                              }
                          }
                      ],
                onClose: function () {
                    $(this).dialog('destroy');
                }
            });
        }

        //查看详情(分销活动表ID)
        function detail(disId){
        	$("#detail").after("<div id='dis_activity_detail' style=' padding:10px; '></div>");
        	$("#dis_activity_detail").dialog({
        	    resizable: false,
        	    title: '分销产品详情',
        	    href: '${ctx}/disActivity/disActivityDetail?disId=' + disId,
        	    width: document.body.clientWidth * 0.6,
        	    height: document.body.clientHeight * 0.7,
        	    modal: true,
        	    buttons: [
        	        {
        	            text: '确定',
        	            iconCls: 'icon-ok',
        	            handler: function () {
        	                $("#dis_activity_detail").dialog('close');
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

        return dateStr;// + ' ' + TimeStr;
    }
  
  
  /**
  *STATE_YES("0", "有效"), 
	STATE_NO("1", "无效"), 
	STATE_PUBLISH("3","发布"),
	STATE_DISPUBLISH("4","未发布"),
  */
  function formatDisState(state){
	  switch(state){
	    	case "0" :{
	    		return "有效"; 
	    	}
	    	case "1": {
	    		return "无效"; 
	    	}
	    	case "2": {
	    		return "发布"; 
	    	}
	    	case "3": {
	    		return "未发布"; 
	    	}
	  }
  }
  
  /**
  * ("0", "全部用户"), 
	 ("1", "平台用户"), 
	 ("2","销售加盟"),
  */
  function formatTargetUser(type){
	  switch(type){
	    	case "0" :{
	    		return "全部用户"; 
	    	}
	    	case "1": {
	    		return "平台用户"; 
	    	}
	    	case "2": {
	    		return "加盟销售"; 
	    	}
	  }
  }
  
  /**
  *发布分销活动
  */
  function publishDisActivity(disId){
	  $.messager.confirm('确认信息','确认发布该分销产品？',function(r){
			if(r){
				$.ajax({
                    url:'${ctx}/disActivity/publishDisAct?disId='+disId+'&r_='+Math.random(),
                    data:{},
                    type:"POST",
                    success:function(data){
                    	var _data =  eval("("+data+")");
						if(_data.isSuccess){
							$.messager.alert('提示', '发布成功！！', 'info',function(){
                                toQueryVoucherProduct();
                            });
						}else{
							$.messager.alert('提示', _data.info, 'info');
						}
                    }
                });
			}
    	  });
  }
/**
 * 失效分销活动
 */
  function disabledDisActivity(disId){
	  $.messager.confirm('确认信息','确认失效该分销产品？',function(r){
			if(r){
				$.ajax({
                    url:'${ctx}/disActivity/disabledDisAct?disId='+disId+'&r_='+Math.random(),
                    data:{},
                    type:"POST",
                    success:function(msg){
                        if(msg=='success'){
                            $.messager.alert('提示', '失效成功！！', 'info',function(){
                                toQueryVoucherProduct();
                            });
                        }else{
	                       $.messager.alert('提示', '失效失败！！'+msg, 'info');
                        }
                    }
                });
			}
    	  });
  }
  
    </script>

</body>

