<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="keywords" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
  <meta name="description" content="${loanApplicationListVO.loanApplicationTitle}；${loanApplicationListVO.desc}" />
  <title>${loanApplicationListVO.loanApplicationTitle} - 财富派</title>
  <%@include file="../common/common_js.jsp"%>
  <link rel="stylesheet" type="text/css" href="${ctx}/css/bigpicture.css" />
  <link rel="stylesheet" type="text/css" href="${ctx}/css/financebidding.css" />
  <style>
  	.zr-loanlist-pd{padding-left:15px;}
  </style>
</head>

<body>
<%--登陆--%>
<%@include file="../person/authenticationShenfen.jsp"%>
<!-- line2 start -->
<%@include file="../common/headLine1.jsp"%>
<!-- line2 start -->

<!-- navindex start -->
<customUI:headLine action="2"/>
<!-- navindex end -->

<%@include file="../common/mianbaoxie1.jsp"%>

<div class="content">
	<div class="loanlist">
		<div class="loanlist-con">
			<div class="loanlist-top">
				<span class="loanlist-top-le">${loanApplicationListVO.loanApplicationTitle}</span>
				<span class="loanlist-top-ri"><a href="javascript:;" data-id="serverProtocol" data-mask="mask">《借款及服务协议》 范本</a></span>
			</div>
			<div class="loanlist-bot">
				<div class="loanlist-bot-le">
					<div class="loanlist-bot-le-tp money">
							<div class="loanlist-bot-div1 ">预期年化收益：<br />
								<big class="bigyear" data-pay="${loanApplicationListVO.annualRate}">${loanApplicationListVO.annualRate}<small>%</small>
									<c:if test="${loanApplicationListVO.rewardsPercent != '0' && loanApplicationListVO.rewardsPercent != null && loanApplicationListVO.awardPoint != null && loanApplicationListVO.awardPoint != 1}">
										<i class="colorRed">+${loanApplicationListVO.rewardsPercent}</i><small>%</small>
										<img class="ml-5" src="${ctx}/images/borrow_06.png" />
									</c:if>
								</big>
							</div>
							<div class="loanlist-bot-div2 zr-loanlist-pd">剩余期限：<br />
								<big class="bigyear" data-term="${surpMonth}">${surpMonth}</big>个月
							</div>
							<div class="loanlist-bot-div zr-loanlist-pd">剩余本金（元）：<br />
								<big class="bigyear" id="confirmBalance"><fmt:formatNumber value="${shouldCapital}" pattern="#,#00.00" /> </big>
									<div class="lb-jdt">
										<div class="lb-word">进度</div>
										<div class="lb-prored">
											<div class="prored" style="width:${ratePercent}%;"></div>
										</div>
										<div class="lb-word">${ratePercent}%</div>
									</div>
							</div>
							<div class="loanlist-bot-div1 lb-br-none zr-loanlist-pd">转出价格（元）<br />
	                        	<big data-term="4" class="loanlist-bot-div1-big-zcjg" ><fmt:formatNumber value="${loanApplicationListVO.confirmBalance}" pattern="#,##0.00" /></big>
							</div>
					</div>
					<div class="clear_0"></div>
					<div class="loanlist-bot-le-bt">
						<div class="loanlist-bot-le-bt-le">
							<p><i class="list-bot-icon1"></i>转让人：<i class="list-bot-word"  style="color:#666;" id="qitou">${lendCustomerName}</i></p>
							<p><i class="list-bot-icon2"></i>抵押信息：<i class="list-bot-word">
								<c:if test="${loanApplicationListVO.loanType eq '0'}">无抵押</c:if>
								<c:if test="${loanApplicationListVO.loanType eq '1'}">抵押房</c:if>
								<c:if test="${loanApplicationListVO.loanType eq '7'}">抵押房</c:if>
							</i></p>
							
							<!-- 添加截止日期（开始）-->
							<c:if test="${not empty guaranteeCompany}">
								<p><i class="list-bot-icon5"></i>倒计时：<i class="list-bot-word residualTime"></i></p>
								<input type="hidden" id="residualTime" value="${residualTime}"/>
							</c:if>
							<!-- 添加截止日期（结束） -->
							
						</div>
						<div class="loanlist-bot-le-bt-ri">
							<c:if test="${not empty guaranteeCompany}">
								<p><i class="list-bot-icon3"></i>担保公司：<i class="list-bot-word">${guaranteeCompany.companyName}</i></p>
							</c:if>
							<p><i class="list-bot-icon4"></i>还款方式：<i class="list-bot-word">
								<c:if test="${loanApplicationListVO.repayMethod eq '1'}">
									<c:forEach items="${repayMentMethod}" var="method">
										<c:if test="${method.value eq loanApplicationListVO.repayMentMethod}">${method.desc}</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${loanApplicationListVO.repayMethod ne '1'}">
						            <customUI:dictionaryTable constantTypeCode="repaymentMode" desc="true" key="${loanApplicationListVO.repayMethod}"/>
						        </c:if>
							</i></p>
							
							<!-- 添加截止日期（开始）-->
							<c:if test="${empty guaranteeCompany}">
								<p><i class="list-bot-icon5"></i>倒计时：<i class="list-bot-word residualTime"></i></p>
								<input type="hidden" id="residualTime" value="${residualTime}"/>
							</c:if>
							<!-- 添加截止日期（结束）-->
							
						</div>
					</div>
				</div>
				<div class="loanlist-bot-ri">
					<%-- <!--水印图 预热中-->
					<c:if test="${loanApplicationListVO.begin eq'false'}"><b id="biao" class="biao biao5"></b></c:if>
					<!--水印图 已结清-->
					<c:if test="${loanApplicationListVO.applicationState eq '7'||loanApplicationListVO.applicationState eq '8'}"><b id="biao" class="biao biao4"></b></c:if>
					<!--水印图 满标-->
					<c:if test="${loanApplicationListVO.applicationState eq '4'||loanApplicationListVO.applicationState eq '5'}"><b id="biao" class="biao biao3"></b></c:if>
					<!--水印图 还款中-->
					<c:if test="${loanApplicationListVO.applicationState eq '6'}"><b id="biao" class="biao biao2"></b></c:if> --%>

					<div class="clear_30"></div>
					<p>
							出售金额：
							<font id="limited">
							<fmt:formatNumber value="${loanApplicationListVO.confirmBalance}" pattern="#,##0.00" /></font>
							<span>元</span>
						<i>
							/剩余金额：
							<span id="mony2" data-money="${lendRightsBalance}">
							<fmt:formatNumber value="${lendRightsBalance}" pattern="#,##0.00" /></span>
							<span id="jiliang">元</span>
						</i>
					</p>
						<input id="limit_input" type="hidden" value="${loanApplicationListVO.maxBuyBalanceNow}" />
					<p>账户余额：<c:if test="${not empty sessionScope.currentUser}">
									<label id="yue"><fmt:formatNumber value="${cashAccount.availValue2}" pattern="#,##0.00" />元</label>
									<input type="hidden" id="yue_input" value="${cashAccount.availValue2}" />
								</c:if>
								<c:if test="${empty sessionScope.currentUser}">
									<font><a href="javascript:" style="color: #35B0EB; padding: 0 5px;" data-mask='mask' data-id="login">登录</a><a>查看</a></font>
								</c:if>
							</p>
					<c:if test="${not empty sessionScope.currentUser}">
						<p class="mt-10">
							<span class="list-cfq">财富劵：<font>${useageSum}</font>元 <i id="list-cfq-icon-i" class="shang"></i><em>(${useageCount}张)</em></span>
						</p>
					</c:if>
					<div class="clear"></div>
					<div class="list-cfq-xl" >
						<div class="list-cfq-je">
							<div class="list-cfq-img">
								<i class="list-img-ile" style="float:left;"><img src="${ctx }/images/news_icon/le.png" /></i>
								<i class="list-img-iri" style="float:right;"><img src="${ctx }/images/news_icon/ri.jpg" /></i>
							</div>
							<c:forEach items="${voucherProductVOs}" var="vp">
								<p>
									<em>￥${vp.amount}</em><span>${vp.voucherRemark}
									<i style="margin-left: 15px;">${vp.usageCount}张</i></span>
								</p>
							</c:forEach>
							<p class="list-p-bor-none"><i><a href="http://help.caifupad.com/guide/caifuquan/">如何获取财富券？</a></i></p>
							<div class="clear"></div>
						</div>
						<div class="clear"></div>
					</div>
					<p class="mt-20">
						<span class="list-inpit">
							<input type="text" id="money" style="margin-bottom:5px;" placeholder="请输入投标金额" maxlength="10" class="ipt-input"/><em class="yuan1">元</em>
						</span>
						<span class="list-btn">
							<c:if test="${not empty sessionScope.currentUser}">
								<c:if test="${userExt.isVerified ne '1'}">
									<button type="button" id="unpay" >
										提交订单，去支付
									</button>
								</c:if>
								<c:if test="${userExt.isVerified eq '1'}">
									<button type="button" id="pay" >
										提交订单，去支付
									</button>
								</c:if>
							</c:if>
							<c:if test="${empty sessionScope.currentUser}">
								<button type="button" data-mask='mask' data-id="login" >
									提交订单，去支付
								</button>
							</c:if>
						</span>
					</p>
					<div class="clear"></div>
					<p id="errorMoneyInfo" class="list-tip hui"></p>
					<div class="clear"></div>
						<p class="mt-20">
							<span class="list-toub">
								<button type="button" id="firstfinan" class="btn-big">最大可投</button>
								<button type="button" id="allbalance" class="btn-small" <c:if test="${empty sessionScope.currentUser}">data-mask='mask' data-id="login"</c:if>>全部余额</button>
							</span>
							<span class="list-shouy">预期收益：<em id="yqsy1">0.00</em><i id="yqsy" style="display: none;">0.00元</i></span>
						<div class="clear"></div>
					</p>
					<div style="clear:both;height:6px;"></div>
					<p class="tips_red"><font>!</font><em>市场有风险，投资需谨慎</em></p>
					<div style="clear:both;height:6px;"></div>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>

<!--借款详情start-->
<div class="list_tab">
	<h6>
		<span class="current">借款详情</span> <span class="border-right-none">投标信息</span><span class="border-right-none">还款列表</span>
	</h6>
	<div class="block">
			<div class="list-box-main">
				<p class="list-box-title"><i class="list-i-1"></i>借款描述</p>
				<p>${loanApplicationListVO.desc}</p>
			</div>
			<div class="list-box-main">
				<p class="list-box-title"><i class="list-i-2"></i>标的信息</p>
				<dl>
						<dd>借款用途： <customUI:dictionaryTable constantTypeCode="loanUseage" desc="true" key="${loanApplicationListVO.loanUseage}"/> </dd>
						<dd>用途描述：${loanApplicationListVO.useageDesc} </dd>
				</dl>
			</div>
			<div class="list-box-main">
				<p class="list-box-title"><i class="list-i-3"></i>借款人基本信息</p>
				<ul>
					<li>性别：<customUI:dictionaryTable constantTypeCode="sex" desc="true" key="${basicSnapshot.sex}" /></li>
					<li>信用卡额度：<fmt:formatNumber value="${basicSnapshot.maxCreditValue}" pattern="#,##0.00" />元</li>
					<li>现居地：${adress.provinceStr}${adress.cityStr}${adress.districtStr}${adress.detail}</li>
				</ul>
				<ul>
					<li>婚姻状况： <customUI:dictionaryTable
							constantTypeCode="isMarried" desc="true"
							key="${basicSnapshot.isMarried}" /> <c:if
							test="${not empty basicSnapshot.isMarried&&not empty basicSnapshot.childStatus }">/</c:if>
						<customUI:dictionaryTable desc="true" constantTypeCode="childStatus" key="${basicSnapshot.childStatus}" /></li>
					<li>月均收入：<fmt:formatNumber value="${basicSnapshot.monthlyIncome}" pattern="#,##0.00" />元
					</li>
				</ul>
				<ul>
					<li>最高学历：<customUI:dictionaryTable desc="true" constantTypeCode="education" key="${basicSnapshot.education}" /></li>
					<li>年收入：<fmt:formatNumber value="${basicSnapshot.monthlyIncome*12}" pattern="#,##0.00" />元
					</li>
				</ul>
				<div class="clear"></div>
			</div>
			 <!-- colspan start -->
              <div class="list-box-main">
				<p class="list-box-title"><i class="list-i-7"></i>审核信息</p>
				<c:if test="${loanApplicationListVO.loanType eq '0'}">
					<ul class="shxx-add">
					<!-- 信贷 身份证、户口本、收入证明、工资流水、电话核实、征信报告、关联调查、数据验证、评分系统、失信调查 -->
						<li><img src="${ctx }/images/icon_shxx/icon_sfz.png" /><br />身份证</li>
						<li><img src="${ctx }/images/icon_shxx/icon_hkb.png" /><br />户口本</li>
						<li><img src="${ctx }/images/icon_shxx/icon_srzm.png" /><br />收入证明</li>
						<li><img src="${ctx }/images/icon_shxx/icon_gzls.png" /><br />工资流水</li>
						<li><img src="${ctx }/images/icon_shxx/icon_dhhs.png" /><br />电话核实</li>
						<li><img src="${ctx }/images/icon_shxx/icon_zxbg.png" /><br />征信报告</li>
						<li><img src="${ctx }/images/icon_shxx/icon_gldc.png" /><br />关联调查</li>
						<li><img src="${ctx }/images/icon_shxx/icon_sjyz.png" /><br />数据验证</li>
				   </ul>
					<ul class="shxx-add">
						<li><img src="${ctx }/images/icon_shxx/icon_pfxt.png" /><br />评分系统</li>
						<li><img src="${ctx }/images/icon_shxx/icon_sxdc.png" /><br />失信调查</li>
					</ul>
				</c:if>
				<c:if test="${loanApplicationListVO.loanType ne '0'}">
				<!--  房贷  身份证、户口本、房产证、电话核实、实地考察、房产估值、征信报告、经营考察、系统验证、失信调查 -->
					<ul class="shxx-add">
						<li><img src="${ctx }/images/icon_shxx/icon_sfz.png" /><br />身份证</li>
						<li><img src="${ctx }/images/icon_shxx/icon_hkb.png" /><br />户口本</li>
						<li><img src="${ctx }/images/icon_shxx/icon_fcz.png" /><br />房产证</li>
						<li><img src="${ctx }/images/icon_shxx/icon_dhhs.png" /><br />电话核实</li>
						<li><img src="${ctx }/images/icon_shxx/icon_sdkc.png" /><br />实地考察</li>
						<li><img src="${ctx }/images/icon_shxx/icon_fcgz.png" /><br />房产估值</li>
						<li><img src="${ctx }/images/icon_shxx/icon_zxbg.png" /><br />征信报告</li>
						<li><img src="${ctx }/images/icon_shxx/icon_jykc.png" /><br />经营考察</li>
					</ul>
					<ul class="shxx-add">
						<li><img src="${ctx }/images/icon_shxx/icon_xtyz.png" /><br />系统验证</li>
						<li><img src="${ctx }/images/icon_shxx/icon_sxdc.png" /><br />失信调查</li>
					</ul>
				</c:if>
				<div class="clear"></div>
			</div>
			<div class="list-box-main">
				<p class="list-box-title"><i class="list-i-4"></i>认证报告</p>
				<c:forEach items="${authInfo}" var="auth" varStatus="stat">
					<ul>
						<li><em></em><customUI:dictionaryTable constantTypeCode="authReport" desc="true" key="${auth}" /></li>
					</ul>
				</c:forEach>
				<div class="clear"></div>
			</div>
			 <!-- colspan start -->
              <div class="list-box-main list-box-bnone-last">
				<p class="list-box-title"><i class="list-i-8"></i>风控步骤</p>	</p>
		           	<p>
	              		${loanApplicationListVO.riskControlInformation}
	              	</p>
              <div class="clear"></div>
			</div>
			<c:if test="${loanApplicationListVO.loanType ne '0'}">
				<div class="list-box-main list-box-bnone-last">
					<p class="list-box-title"><i class="list-i-5"></i>抵押信息</p>
					<ul>
						<li>抵押物类型： <c:if test="${house.mortgageType eq '1'}">一抵</c:if>
							<c:if test="${house.mortgageType eq '2'}">二抵</c:if>
						</li>
						<li>总评估值：<fmt:formatNumber value="${loanPublish.assessValue}" pattern="#,##0.00" />万元</li>
					</ul>
					<ul>
						<li>房屋地址：${houseAdress.provinceStr}${houseAdress.cityStr}${houseAdress.districtStr}${houseAdress.detail}</li>
						<li>市值：<fmt:formatNumber value="${loanPublish.marketValue}" pattern="#,##0.00" />万元</li>
					</ul>
					<ul>
						<li>房屋面积：<fmt:formatNumber value="${loanPublish.hourseSize}" pattern="#,##0.00" />平方米</li>
					</ul>
					<div class="clear"></div>
					<p>抵押物说明：${loanPublish.hourseDesc}</p>
					<div class="clear"></div>
				</div>
			</c:if>
			<div class="list-box-main box-mb-none">
				<p class="list-box-title">
					<i class="list-i-6"></i>项目证明
				</p>
				<!-- 左右滚动部分 begin -->
				<div class="main_lunbo">
					<c:if test="${not empty customerUploadSnapshots}">
						<div class="leftbtn" id="left"></div>
					</c:if>
					<div id="photo">
						<div class="Cont">
							<div class="ScrCont" id="viewer">
								<div class="List1" id="viewerFrame">
									<c:forEach items="${customerUploadSnapshots}"
										var="customersnapshot">
										<div class="list-pic-p">
											<a href="javascript:" rel="lightbox[plants]" title="">
												<img src="${picPath }${customersnapshot.attachment.thumbnailUrl}" alt="" />
											</a>
											<p>${customersnapshot.attachment.fileName}</p>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
					<c:if test="${not empty customerUploadSnapshots}">
						<div class="rightbtn" id="right"></div>
					</c:if>
				</div>
				<!-- 左右滚动部分 end -->
				<div class="clear"></div>
			</div>
		</div>
		<div class="div">
        	<ul class="list-ul">
				<li class="list-ul-first">
					<ul>
						<li>投标人</li>
						<li>投标金额（元）</li>
						<li>投标日期</li>
					</ul>
				</li>
				<p id="tbtable" class="bid_information_tab2">

      			</p>
			</ul>
		<div id="pageList" class="tcdPageCode mt-20"></div>
    </div>
	<div class="div">
		<div class="repayment-list">
			<p>已还本息：<span class="c-orange"><fmt:formatNumber value="${hasPaidBalance}" pattern="#,##0.00"/>元</span>   待还本息：<span class="c-orange"><fmt:formatNumber value="${waitPaidBalance}" pattern="#,##0.00"/>元</span>  (待还本息因算法不同可能会存误差，实际金额以到账金额为准！)</p>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<th>合约还款日期</th>
					<th>期数</th>
					<th>应还本金</th>
					<th>应还利息</th>
					<th>应还本息</th>
					<th>还款状态</th>
				</tr>
				<c:forEach items="${showRepaypaymentList }" var="repay"  >
					<tr>
						<td><fmt:formatDate value="${repay.repaymentDay}" pattern="yyyy-MM-dd" type="date"/></td>
						<td>${repay.sectionCode}</td>
						<td><fmt:formatNumber value="${repay.shouldCapital2 }" pattern="#,##0.00"/>元</td>
						<td><fmt:formatNumber value="${repay.shouldInterest2 }" pattern="#,##0.00"/>元</td>
						<td><fmt:formatNumber value="${repay.shouldBalance2 }" pattern="#,##0.00"/>元</td>
						<td>${stateMap[repay.planState] }</td>
					</tr>
				</c:forEach>				  
			</table>
		</div>
	</div>
</div>


<!-- masklayer start  -->
<div class="masklayer masklback" id="cjd">
  <h2 class="clearFloat"><span>投标</span> <a href="javascript:" data-id="close"></a></h2>
  <!-- equity start -->
  <div class="flbuy">
    <p class="clearFloat"><span class="fsp1">借款名称：</span>  <span class="fsp2" id="title">${loanApplicationListVO.loanApplicationTitle}</span></p>
    <p class="clearFloat"><span class="fsp1">投标金额：</span>  <span class="fsp2"><big  class="c_red"><i id="buymoney">10,000.00</i></big>元</span></p>
    <p class="clearFloat"><span class="fsp1">预期收益：</span>  <span class="fsp2"><small id="expected" class="c_red">300.05</small>元</span></p>
    <p class="clearFloat"><span class="fsp1">账户余额：</span>  <span class="fsp2"><small id="account"><fmt:formatNumber value="${cashAccount.availValue2}" pattern="#,##0.00"/></small>元</span></p>


    <form action="${ctx}/finance/bidLoanByAccountBalance" id="lendForm" method="post" name="form" class=" mt-20">
		<input type="hidden" id="loanApplicationId" name = "loanApplicationId" value="${loanApplicationListVO.loanApplicationId}"/>
		<input type="hidden" id="creditorRightsId" name = "creditorRightsId" value="${creditorRightsId}"/>
		<input type="hidden" id="creditorRightsApplyId" name = "creditorRightsApplyId" value="${creditorRightsApplyId}"/>
		<input type="hidden" id="amount" name="amount" value=""/>
		<input type="hidden" id="token" name="token" value="${token}"/>
      <input style="display:none" />
      <div class="input-group">
        <label for="jypassword">
          <span class="fsp1">交易密码：</span>
          <input type="password" value="" id="jypassword"  name="password" placeholder="请输入交易密码"
                 onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}" onblur="if(!value) {value=defaultValue; this.type='text';}"
                 class="ipt-input" />
                 <a href="#"  id="ed_ex_psdad" class="ml-20">找回交易密码</a>
        </label>

        <em class="ml-90"></em>
      </div>
      <div class="input-group">
        <label for="checkBox">
          <input type="checkbox" name="checkBox" id="checkBox" checked class="mr-5 ml-90 ipt-check" /><a href="javascript:" class="ahui" data-mask="mask" data-id="serverProtocol">《出借咨询与服务协议》</a>
        </label>
        <em class=" ml-90"></em>
      </div>
      <div class="groupbtn clearFloat">
        <label>
          <button type="button" class="btn btn-error ml-90 mt-0" id="tbpay">确认支付</button>
        </label>
        <label>
          <button type="button" id="otherPay" class="btn btn-write ml-20 mt-0 help-tips" style="cursor: pointer;">使用其它方式支付<p><img src="${ctx}/images/cfq_icon.jpg" />戳我，戳我，使用优惠券吧~</p></button>
        </label>
      </div>
    </form>
	<div style="height:50px;clear:both;"></div>
  </div><!-- equity start -->

</div><!-- masklayer end -->
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script><!-- jquery 类库 javascript -->
<script type="text/javascript" src="${ctx}/js/rightsBidding.js"></script><!-- financeDetail javascript -->
<script type="text/javascript" src="${ctx}/js/jquery_page.js"></script><!-- public javascript -->
<script src="${ctx}/js/slides-1.1.1-min.js" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
    $('.succesny').olvSlides({
      thumb: true,
      thumbPage: true,
      thumbDirection: "X",
      effect: 'fade'
    });
  })
</script>

<script src="${ctx}/js/script/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctx}/js/script/jquery.imageScroller.js"></script>
<script type="text/javascript">
  //点击确认支付
  $("#otherPay").click(function(){
    //其他方式支付
    $("#amount").val($(".yuan1").html()=="万"?parseFloat($("#money").val())*10000:$("#money").val());
    $("#lendForm").attr("action", rootPath + "/finance/toBuyRightsByPayAmount").submit();
  });

  var j = jQuery.noConflict();
  j("#viewer").imageScroller({
    next:"right",
    prev:"left",
    frame:"viewerFrame",
    width:120,
    child: "div",
    auto: false
  });
  j(function(){
    j(".masklayer1").hide();
    j(document).on("click","#viewerFrame>div img",function(){
      j(".zhezhao").show();
      j("#bigpicture").slideDown(500);
    });
    j(".successlunbo i.close").click(function(){
      j(".zhezhao").hide();
      j(".masklayer1").hide();
    });
  })

</script>
<!--借款详情end-->

<!-- masklayer start  -->
<div class="masklayer1" id="bigpicture" style="display: none;">
  <!-- successlunbo start -->
  <div class="successlunbo">
    <i class="close"></i>
    <div class="succesny">
      <div class="control">
        <ul class="change">
        </ul>
      </div>
      <div class="thumbWrap">
        <div class="thumbCont">
          <ul>
            <!-- img属性, url=url, text=描述, bigimg=大图, alt=标题  -->
            <c:forEach items="${customerUploadSnapshots}" var="customersnapshot">
              <li>
                <div><img src="${picPath }${customersnapshot.attachment.thumbnailUrl}" url="url" bigImg="${picPath }${customersnapshot.attachment.url}" alt="${customersnapshot.attachment.fileName}"></div>
              </li>
            </c:forEach>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- masklayer end -->

<!-- toopInfo start -->
<div class="masklayer" id="toopInfo">
	<h2 class="clearFloat"><span>提示信息 </span><a href="javascript:;" id="closeCFQ"></a></h2>
    <div class="tipsInfo">
    	<!--<p class="titleRed">
        	财富券正式上线
        </p>
       <p class="contextBack">
       	参与投标，来获取您的财富券吧~
       </p> -->
       <img src="${ctx}/images/fdetail/detail_03.jpg" /><br />
       <a href="javascript:;" class="btn btn-error" data-id="close">好的</a>
    </div>
</div>
<!-- toopInfo end -->
<!--飘窗登录前start-->
<div class="bay-window" id="bay-window">
  <c:if test="${not empty sessionScope.currentUser}">
    <div class="bay-window" id="bay-windows">
      <div class="bay-wd-main">
        <div class="bay-wd-main-le2">
          <span class="span-left1"><img src="${ctx}/images/pc-icon3.png" />${loanApplicationListVO.loanApplicationTitle}</span>
          <span class="span-right1"><img src="${ctx}/images/pc-icon4.jpg" />剩余可投金额 <font  class="bay-wd-main-red"><fmt:formatNumber value="${loanApplicationListVO.remain}" pattern="#,##0.00"/></font><i>元</i></span>
        </div>
        <div class="bay-wd-main-ri2">
          <span class="bay-input"><input type="text" id="bay_ipt_money" /><i class="bay-yuan" id="bay-yuan">元</i></span>
            <span class="bay-chujie"><a href="javascript:" id="bay_pay">立即出借</a></span>
            <div class="clear"></div>
          <em style="font-size: 12px; "></em>
        </div>
      </div>
    </div>
  </c:if>
  <c:if test="${empty sessionScope.currentUser}">
    <div class="bay-wd-main">
      <div class="bay-wd-main-le">
        <span class="span-left"><img src="${ctx}/images/pc-icon3.png" />${loanApplicationListVO.loanApplicationTitle}</span>
        <span class="span-right"><img src="${ctx}/images/pc-icon4.jpg" />剩余可投金额 <font  class="bay-wd-main-red"><fmt:formatNumber value="${loanApplicationListVO.remain}" pattern="#,##0.00"/></font><i>元</i></span>
      </div>
      <div class="bay-wd-main-ri">
        <a href="javascript:" data-mask='mask' data-id="login" class="btn-error bay-login">登录</a>
        <a href="${ctx}/user/regist/home" class="bay-register ml-20">快速注册</a>
      </div>
    </div>
  </c:if>
</div>
<!--飘窗end-->
<script>
  //当滚动条的位置处于距顶部100像素以下时，跳转链接出现，否则消失
 /*  $(function () {
    $(window).scroll(function(){
      if ($(window).scrollTop()>100){
        $(".bay-window").fadeIn(1500);
      }else{
        $(".bay-window").fadeOut(1500);
      }
    });
  }); */
</script>

<%@include file="../common/serverProtocol.jsp"%>
<%@include file="../login/login.jsp"%>
<%@include file="../common/footLine3.jsp"%>

<!-- 找回交易密码-开始 -->
<script src="${ctx}/js/ed_ex_psd.js" type="text/javascript"></script>
<div class="zhezhao6"></div>
<div class="masklayer masklback" id="ed_ex_psd">
		<h2 class="clearFloat"><span>找回交易密码</span><a href="javascript:" data-id="close"></a></h2>
		<div class="xiugai_phone_main" id="xigai1">
        	<img src="${ctx}/images/ed_ex_psd1.jpg" class="mt-30 mb-30"/>
			<form action="" class="form" method="post">
				<div class="input_box_phone">
					<label>
						<span>绑定的手机号码</span>
						<i class="tal ex-plone">${sessionScope.currentUser.encryptMobileNo}</i>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone">
					<label>
						<span>手机验证码</span>
						<input type="text" id="ex_valid" value="" autocomplete="off" maxlength="6" placeholder="请输入验证码" style=""/>
						<button type="button" id="ex_get_valid" class="huoqu_yanzm" >获取验证码</button>
					</label>
					<em></em>
				</div>
				<div class="input_box_phone ipt_box_phone">
					<button type="button" id="next_sub1">下一步</button>
				</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d2">
        	<img src="${ctx}/images/ed_ex_psd2.jpg" class="mt-30 mb-30"  />
			<form action="" class="form" method="post">
					<div class="input_box_phone input_box ipt-box-ex">
						<label>
							<span>输入新交易密码</span>
							<input type="password" class="width200" id="ed_ex_psd1" value="" maxlength="16" style="width:160px;" autocomplete="off" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" onKeyUp="ed_ex_Check(this.value)" />
								
								<div type="button" id="rejc_ex_psd" class="Tcolor floatLeft">无</div>
						</label>
						<em class="hui fontsize12">交易密码为6 -16 位字符，支持字母及数字,字母区分大小写</em>
					</div>
					<div class="input_box_phone">
						<label>
							<span>再次输入新交易密码</span>
							<input type="password" class="width200" value="" onFocus="if(value==defaultValue){value=''}" onBlur="if(!value){value=defaultValue}" id="ed_ex_psd2" value="" maxlength="16" autocomplete="off" />
						</label>
						<em></em>
					</div>
					<div class="input_box_phone ipt_box_phone">
						<button type="button" id="next_sub2">下一步</button>
					</div>
			</form>
		</div>
		<div class="xiugai_phone_main display-none" id="phone_d3">
				
        	<img src="${ctx}/images/ed_ex_psd3.jpg" class="mt-30 mb-30" />
			<p class="mt-50"><img src="${ctx}/images/img/true.jpg" /><span>交易密码重置成功！</span></p>
				
			<div class="input_box_phone ipt_box_phone" style="margin-top:85px;">
				<a href="javascript:" id="ed_ex_psda"><button>确认</button></a>
			</div>
		</div>
</div>
<!-- 找回交易密码-结束 -->


</body>
<script>
//返回顶部
$(function () {
	var lendRightsBalance = ${lendRightsBalance};
	if(lendRightsBalance == 0){
		$(".loanlist-bot-ri input.ipt-input[type=text]:visible,.loanlist-bot-ri button").each(function() {
			$(this).attr("disabled",true);
		});
	}
	
	searchHtml(1, 10, true);
	
	$(window).scroll(function(){
		if ($(window).scrollTop()>100){
			$("#top").fadeIn(500);
		}else{
			$("#top").fadeOut(500);
		}
	});
	//当点击跳转链接后，回到页面顶部位置
	$("#top").click(function(){
	$('body,html').animate({scrollTop:0},1000);
	return false;
	});
});

//出借列表查询
function searchHtml(page, rows, flag) {
    var thtml = "";
    if (flag) {
        $("#pageList").html("");
        $("#pageList").html('<div id="userPageCode" class="tcdPageCode"></div>');
    }

    $.ajax({
        url: rootPath + "/finance/getCreditorRightsLender",
        type: "post",
        data: {
            "pageSize": rows,
            "pageNo": page,
            "creditorRightsApplyId": ${creditorRightsApplyId}
        },
        success: function (data) {
            var d_rows = data.rows;
            var pageCount = data.totalPage;
            for (var i = 0; i < d_rows.length; i++) {
                var data = d_rows[i];
				if(i%2==0){
                	thtml += '<li>';
				}else{
					thtml += '<li class="list-ul-li-bg">';
				}
                thtml += '<ul>';
                thtml += '<li>' + data.lenderName + '</li>';
                thtml += '<li>' + fmoney(data.lendAmount, 2) + '</li>';
                thtml += '<li>' + dateTimeFormatter(data.lendTime) + '</li>';
                thtml += '</ul>';
                thtml += '</li>';
            }
            $('#tbtable').html(thtml);
            bottomB();
            if (d_rows.length > 0 && flag) {
                $("#userPageCode").createPage({
                    pageCount: pageCount,
                    current: 1,
                    backFn: function (p) {
                        //点击分页效果
                        searchHtml(parseInt(p), 10, false);
                    }
                });
            }
        }
    });
}
</script>
<style>
	.em-sfyz{margin-left:20px;font-size:12px;color:red;line-height:25px;}
	.div .c-orange{color:#ff6b00;}
	.list_tab .div .repayment-list table {border-top: 1px solid #e1e1e1; border-left: 1px solid #e1e1e1;margin: 15px 0 0; color: #666;margin-bottom:30px;}
	.list_tab .div .repayment-list{padding:0 30px;}
	.list_tab .div .repayment-list p{padding-top:20px;}
	.repayment-list td, .repayment-list th {border-bottom: 1px solid #e1e1e1;border-right: 1px solid #e1e1e1;padding: 10px 0;text-align: center;}
	.repayment-list th { background: #f5f9fd;}
	
	.shxx-add{width:100%!important;margin-top:15px;}
	.shxx-add li{width:12%!important;height:auto;float:left;text-align:center;}
	.list-i-7{width: 16px;height: 16px;display: inline-block;margin-bottom: -3px;margin-right: 10px;background:url(../images/icon_shxx/title_icon.png);}
	
	.list-fk-title{display:block;float:left;}
	.list-fk-main{display:block;float:left;width:95%;}
	
</style>
</html>

