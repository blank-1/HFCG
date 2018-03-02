<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>

<hr/>
<br/>

<form class="form-horizontal"  method="post" style='width: 470px;display:block;word-break: break-all;word-wrap: break-word;'>
    <div class="control-group">
        <label class="control-label">分销名称:</label>
        <div class="controls-text">
            ${disActivity.disName }
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">期号:</label>
        <div class="controls-text">
            ${disActivity.sectionCode }
        </div>
    </div>
    <div class="control-group">
          <label class="control-label">目标用户：</label>
            <div class="controls-text">
               ${targetUserMap[disActivity.targetUser]}
            </div>
          </div>
      </div>
    <div class="control-group">
        <label class="control-label">规则期限:</label>
        <div class="controls-text" >
            <fmt:formatDate value="${disActivity.ruleStartDate }" pattern="yyyy-MM-dd"/></td> -- 
            <fmt:formatDate value="${disActivity.ruleEndDate }" pattern="yyyy-MM-dd"/></td>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">分销奖励限制:</label>
        <div class="controls-text" >
            ${salesPointStart } -- ${salesPointEnd }
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">分销描述:</label>
        <div class="controls-text" >
            ${disActivity.disDiscription }
        </div>
    </div>
</form>

<hr/>
<br/>

<div style="margin-left: 10px;margin-bottom: 50px;">
	<table border="1" width="1000px;" style="font-size: 12px;">
		<tr style="background-color: #6699CC;text-align: center;">
			<td>适用产品</td>
			<td>发放节点</td>
			<td>一级佣金比例(%)</td>
			<td>二级佣金比例(%)</td>
			<td>三级佣金比例(%)</td>
			<td>操作</td>
		</tr>
		<c:forEach items="${disActivityRuleList }" var="rule">
			<tr style="text-align: center;">
				<td>${rule.productName }</td>
				<td>${rule.commiPaidNodeStr }</td>
				<td>${rule.firstRate }</td>
				<td>${rule.secondRate }</td>
				<td>${rule.thirdRate }</td>
				<!-- <td><a style="cursor: pointer;" href="/cfp-admin/disActivity/toActivitProductDetail?productId=${rule.lendProductId }"  >查看报表</a></td> -->
				<td><a style="cursor: pointer;" href="#"  onclick="toQuery(${rule.lendProductId })" >查看报表</a></td>
			</tr>
		</c:forEach>
	</table>
</div>
  <div id="dis_activity_list111" class="container-fluid" style="padding: 5px 0px 0px 10px"></div>
  <script language="javascript">
  function toQuery(id){
	  console.log("------");
	    $("#dis_activity_list111").after("<div id='addDisActivity111' style=' padding:10px; '></div>");
        $("#addDisActivity111").dialog({
            resizable: false,
            title: '产品详情报表',
            href: '${ctx}/disActivity/toActivitProductDetail?productId='+id,
            width: 900,
            height: 550,
            modal: true,
            buttons: [
              
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#addDisActivity111").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
  }
  
  </script>
</body>
</html>