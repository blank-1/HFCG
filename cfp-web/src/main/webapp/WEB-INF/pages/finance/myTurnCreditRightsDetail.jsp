<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<meta charset="utf-8">
<title>债权转让-转让详情</title>
<%@include file="../common/common_js.jsp"%>
<link href="${ctx}/css/creditor/jquery-ui.css" rel="stylesheet" type="text/css">
<link href="${ctx}/css/creditor/zhaiquan.css" rel="stylesheet" type="text/css">
<style>
.ui-widget-header {background:#ccc;}
.ui-widget-content{ width:99%; margin-top:15px; margin-left:1%; }
.ui-slider-horizontal{ height:6px;} 
.ui-slider .ui-slider-handle{ width:14px; height:14px;}
.ui-slider .ui-slider-handle strong{ text-decoration:none; position:absolute; left:-10px; top:-22px; background:url(${ctx}/images/licai/em-bj.png) no-repeat; width:32px; height:22px; text-align:center; line-height:15px; font-weight:normal; color:white;}
.ui-slider .ui-state-default{ background:url(${ctx}/images/licai/z9.png) no-repeat; border:none;}
.ui-slider-horizontal .ui-slider-handle {
    margin-left: -0.6em;
    top: -0.25em;
}
</style>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-ui.js"></script>
<script>
$(function() {
	var s=parseFloat($('#s-val').html().replace(/,/g,""));
	var num=parseFloat($('#s-num').html().replace(/,/g,""));
	$( "#slider-range-max1" ).slider({
	  range: "max",
	  min: -5,
	  max: 0,
	  value: 0,
	  slide: function( event, ui ) {
		var temp = Math.round((s*(ui.value/100)+s)*100)/100;
        $("#amount").html(fmoney(temp,2) +'元' );
		$("#s-total").html(fmoney(temp,2));	
		$("#rightAcount").val(fmoney(temp,2));	
		$("#changePoint").val(ui.value);	
		$( "#slider-range-max1" ).find('strong').html(ui.value);
      }
	});

/* 	$( "#slider-range-max2" ).slider({
	  range: "max",
	  min: 3,
	  max: 10,
	  value: 3,
	  slide: function( event, ui ) {		
		$( "#slider-range-max2" ).find('strong').html(ui.value);
      }
	}); */
	
	
});

</script>
</head>
<body>
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="3"/>
<!-- navindex end -->

<!-- person-link start -->
<div class="person-link">
    <ul class="container clearFloat">
        <li><a href="${ctx }/person/account/overview">账户中心</a>></li>
        <li><a href="javascript:;">我的理财</a>></li>
        <li><span>出借债权</span></li>
    </ul>
</div>
<!-- person-link end -->

<div class="clear_25"></div>

<div class="container">
	<div class="con-t">
    	<h2 class="con-h2">${loanApplicationListVO.loanApplicationTitle}</h2>
        <div class="con-nr clearFloat">
        	<div class="con-nrl">
            	<ul class="clearFloat con-u">
                	<li class="lw1">
                    	<h6>预期年化收益</h6>
                        <h5><b>${loanApplicationListVO.annualRate}</b><i>%</i>
                        	<c:if test="${loanApplicationListVO.awardRate != '0' && loanApplicationListVO.awardRate != null }">
								<c:if test="${loanApplicationListVO.awardPoint != null && loanApplicationListVO.awardPoint != '' && loanApplicationListVO.awardPoint != 1 }">									
									<i>+</i><b style="color:red;">${loanApplicationListVO.awardRate}</b><i>%</i>
								</c:if>
							</c:if>
                        </h5>
                    </li>
                    <li class="lw2">
                    	<h6>借款期限</h6>
                        <h5><b>${loanApplicationListVO.cycleCount}</b><i>个月</i></h5>
                    </li>
                    <li class="lw3">
                    	<h6>剩余期限</h6>
                        <h5><b>${surpMonth }</b><i>个月</i></h5>
                    </li>
                    <li class="lw4">
                    	<h6>投标金额</h6>
                        <h5><b><fmt:formatNumber value="${creditorRights.buyPrice }" pattern="#,##0.00"/></b><i>元</i></h5>
                    </li>
                    <li class="lw5">
                    	<h6>待还金额</h6>
                        <h5><b><fmt:formatNumber value="${shouldCapital }" pattern="#,##0.00"/></b><i>元</i></h5>
                    </li>
                </ul>
                <ol class="ol-item clearFloat">
                	<!-- <li class="item1">保障说明：
		                
          			</li> -->
                    <li class="item2">还款方式：
                    	<c:if test="${loanApplicationListVO.repayMethod eq '1'}">
				            <c:forEach items="${repayMentMethod}" var="method">
				              <c:if test="${method.value eq loanApplicationListVO.repayMentMethod}">${method.desc}</c:if>
				            </c:forEach>
				        </c:if>
				        <c:if test="${loanApplicationListVO.repayMethod ne '1'}">
				            <customUI:dictionaryTable constantTypeCode="repaymentMode" desc="true" key="${loanApplicationListVO.repayMethod}"/>
				        </c:if>
                    </li>
                    <li class="item3">抵押信息：
                    	<c:if test="${loanApplicationListVO.loanType eq '0'}">无抵押</c:if>
				        <c:if test="${loanApplicationListVO.loanType eq '1'}">抵押房</c:if>
				        <c:if test="${loanApplicationListVO.loanType eq '7'}">抵押房</c:if>
                    </li>
                    <li class="item4">借款描述：${loanApplicationListVO.desc}</li>
                </ol>
            </div>
        	<div class="con-nrr">
            	<p>可转让本金：<strong class="con-nrr-s" id="s-val"><fmt:formatNumber value="${shouldCapital }" pattern="#,##0.00"/></strong>元</p>
                <dl class="con-nrr-dl">
                	<dt>转账溢价：</dt>
                    <dd>
                    	<div id="slider-range-max1"></div>
                        <em>-5%</em>
                        <i>0%</i>
                    </dd>
                </dl>
                <dl class="con-nrr-dl">
                	<dt>转让价格：</dt>
                    <dd>
                    	<code id="amount"><fmt:formatNumber value="${shouldCapital }" pattern="#,##0.00"/>元</code>
                        <del>(服务费：<strong id="s-num" style="font-weight:normal">0</strong>元)</del>
                    </dd>
                </dl>
                <p style="margin-top:10px;">实际到账金额：
                <strong id="s-total" style="font-weight:normal"><fmt:formatNumber value="${shouldCapital }" pattern="#,##0.00"/></strong>元</p>
                <dl class="con-nrr-dl">
                	<dt style="width:40%">转账有效期：${termDay }天</dt>
                </dl>
                <dl class="con-nrr-dl">
                	<dt>转让原因：</dt>
                    <dd>
                    	<select>
                        	<option>资金周转</option>
                            <option>个人原因</option>
                            <option>对平台收益率不满意</option>
                            <option>对平台理财产品不满意</option>
                            <option>选择其他理财平台</option>
                            <option>投资股市</option>
                            <option>其他原因</option>
                        </select>
                    </dd>
                </dl>
                <form action="${ctx }/finance/turnCreditRight" method="post" id="frm">
                	<input type="hidden" name="creditorRightsId" value="${creditorRights.creditorRightsId }"/>
                	<input type="hidden" name="rightAcount" id="rightAcount" value="${shouldCapital }"/>
                	<input type="hidden" name="changePoint" id="changePoint" value="0"/>
                	<input type="hidden" name="token" value="${token }"/>
                </form>
                <button class="con-nrr-btn" id="creditor">确认转让</button>
            </div>
        </div>
    </div>
    <div class="con-t" style="margin-top:20px;">
    	<h2 class="con-h2">债权转让说明</h2>
        <ol class="list-w">
        	<li>
            	<h3>
                	<img src="${ctx}/images/licai/z5.png" alt=""><span>债权转让规则</span>
                </h3>
                <p>若借款人提前还款，则转让申请自动撤销。转让达成后，债权出让人将损失从上一个收款日到转让达成日期间的利息。</p>
                <p>（备注：即转让达成后出让人将损失申请当月的利息归受让人所有）</p>
            </li>
            <li>
            	<h3>
                	<img src="${ctx}/images/licai/z6.png" alt=""><span>债权转让时间</span>
                </h3>
                <p>债权转让无有效期限制，转让申请提交截止下一个还款日仍未转让的，视为转让失败，系统自动撤销该申请。</p>
            </li>
            <li>
            	<h3>
                	<img src="${ctx}/images/licai/z7.png" alt=""><span>债权转让费用</span>
                </h3>
                <p>财富派平台向出让人收取转让本金*0.5%的转让管理费，不向受让人收取任何费用，即<b>债权转让费用=转让本金*转让管理费率</b>；</p>
                <p>债权转让管理费在成交后直接从成交金额中扣除，不成交债权不收取任何费用。</p>
            </li>
            <li>
            	<h3>
                	<img src="${ctx}/images/licai/z8.png" alt=""><span>取消奖励说明</span>
                </h3>
                <p>债权转让标的不参与财富券奖励，即财富券只在原始出让人满标放款时发放，受让人不享受财富券奖励；</p>
                <p>债权转让受让人也不享受平台月度(或季度)活动的返现奖励。</p>
            </li>
        </ol>
    </div>
</div>
<script type="text/javascript">
	$("#creditor").click(function(){
	/* $.ajax({
		url : rootPath + "/finance/turnCreditRight",
		type : "post",
		data : {
			turnCreditRight:sf_name,
			creditorRightsId:sf_card
		},
		async : false,
		error : function(XHR) {
			
		},
		success : function(data) {
			if(data.result == 'success'){
					
      	    	}
      	    }
	}); */
	$("#frm").submit();
})
</script>

<!-- login end -->
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- fbottom end -->

</body>
</html>
