<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../../common/common.jsp" %>
<html>
<head>
    <title></title>
</head>
<body>
    <div style="width:90%;height:90%;border-right: 1px none black;float:left;">
     <form class="form-horizontal" >
      <div style="border-bottom: 1px none black;clear: both;">
        <fieldset style="height:750px;">
          <legend>抵押信息</legend>
          
     	  <div class="control-group">
     	  	<label class="control-label" style="padding-top: 0px;"> 抵押车列表：</label>
               <div class="controls" >
	               <c:if test="${not empty mortgageCarList }">
	               		<table style="font-size: 12px;width:80%;" class="table table-striped" id="mortgageCarList">
					        <tr>
					          <td>产品类型</td>
					          <td>汽车品牌</td>
					          <td>汽车型号</td>
					          <td>市场价格</td>
					          <td>车架号</td>
					          <td>操作</td>
					        </tr>
	               			<c:forEach items="${mortgageCarList }" var="mortgageCar">
						        <tr>
						          <td>${mortgageCar.arrived==1?'[一抵]':'[二抵]' }</td>
						          <td>${mortgageCar.automobileBrand }</td>
						          <td>${mortgageCar.carModel }</td>
						          <td>${mortgageCar.marketPrice }万元</td>
						          <td>${mortgageCar.frameNumber }</td>
						          <td>
						          	<c:if test="${mortgageCar.state == 0}">
						          		<a onclick="showChange(${mortgageCar.mortgageCarId});" style="cursor: pointer;">变更</a>
						          	</c:if>
						          </td>
						        </tr>
	               			</c:forEach>
	               		</table>
	               </c:if>
	               <c:if test="${empty mortgageCarList }">
	               		空的
	               </c:if>
               </div>
           </div>
          
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 抵押总价值：</label>
            <div class="controls">
                ${car.totalMortgageValue }万元
            </div>
          </div>
          
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 授信上限：</label>
              <div class="controls">
                  ${car.creditLimit }万元
              </div>
          </div>
          
          <div class="control-group">
            <label class="control-label" style="padding-top: 0px;"> 描述：</label>
              <div class="controls">
                  ${car.mortgageDescription }
              </div>
          </div>

        </fieldset>
      </div>
  </div>
<script type="text/javascript">
//变更
function showChange(mortgageCarId) {
	
	if (mortgageCarId) {
        $("#mortgageCarList").after("<div id='showManageDetail' style=' padding:10px; '></div>");
        $("#showManageDetail").dialog({
            resizable: false,
            title: '抵押车变更',
            href: '${ctx}/jsp/enterprise/loan/to_chargeInfo_change?mortgageCarId=' + mortgageCarId,
            width: 700,
            height: 450,
            modal: true,
            top: 100,
            left: 300,
            buttons: [
				{
				    text: '确定',
				    iconCls: 'icon-ok',
				    handler: function () {
				    	saveChange();
				    }
				},
                {
                    text: '取消',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        $("#showManageDetail").dialog('close');
                    }
                }
            ],
            onClose: function () {
                $(this).dialog('destroy');
            }
        });
    } else {
        $.messager.alert("系统提示", "请选择要操作的数据!", "info");
    }
}
</script>
</body>
</html>
