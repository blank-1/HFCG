<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="keywords" content=""/>
      <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="${loanApplicationListVO.loanApplicationTitle}；${loanApplicationListVO.desc}"/>
    <title>${loanApplicationListVO.loanApplicationTitle} - 财富派</title>
    <%@include file="../common/common_js.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/bigpicture.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/financebidding.css" />
</head>

<%--登陆--%>
<%@include file="../person/authenticationShenfen.jsp" %>
<!-- line2 start -->
<%@include file="../common/headLine1.jsp" %>
<%@include file="../common/hengfengCard.jsp" %>
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
				<c:if test="${loanApplicationListVO.loanType ne '5' && loanApplicationListVO.loanType ne '8'}">
					<span class="loanlist-top-ri"><a href="javascript:;" data-id="serverProtocol" data-mask="mask">《借款及服务协议》 范本</a></span>
				</c:if>
				<c:if test="${loanApplicationListVO.loanType eq '8'}">
					<span class="loanlist-top-ri"><a href="javascript:;" data-id="payshowstate0" data-mask="mask">《借款及服务协议》 范本</a></span>
				</c:if>
	            <c:if test="${loanApplicationListVO.loanType eq '5'}">
	                <span class="loanlist-top-ri"><a href="javascript:;" data-id="payshowstate" data-mask="mask">《定向委托投资资产转让交易协议》 范本</a></span>
	                <span class="loanlist-top-ri"><a href="javascript:;" data-id="payshowstate2" data-mask="mask" >《定向委托投资管理协议》 范本</a></span>
	            </c:if>
			</div>
			<div class="loanlist-bot">
				<div class="loanlist-bot-le">
					<div class="loanlist-bot-le-tp money">
							<div class="loanlist-bot-div">预期年化收益<br />
								<big data-pay="${loanApplicationListVO.annualRate}">${loanApplicationListVO.annualRate}<small>%</small>
									<c:if test="${loanApplicationListVO.rewardsPercent != '0' && loanApplicationListVO.rewardsPercent != null}">
									<i class="colorRed">+${loanApplicationListVO.rewardsPercent}<small>%</small></i><img class="ml-5" src="${ctx}/images/borrow_06.png" />
									</c:if>
								</big>
							</div>
							<div class="loanlist-bot-div">
								<c:if test="${loanApplicationListVO.loanType eq '5'}">项目期限</c:if>
								<c:if test="${loanApplicationListVO.loanType ne '5'}">借款期限</c:if><br />
								<big class="bigyear" data-term="${loanApplicationListVO.cycleCount}">${loanApplicationListVO.cycleCount}</big>个月
							</div>
							<div class="loanlist-bot-div lb-br-none">
								<c:if test="${loanApplicationListVO.loanType eq '5'}">项目金额</c:if>
								<c:if test="${loanApplicationListVO.loanType ne '5'}">借款金额</c:if>：<br />
								<big class="bigyear" id="confirmBalance"><fmt:formatNumber value="${loanApplicationListVO.confirmBalance}" pattern="#,#00" /> </big>元
								<div class="lb-jdt">
									<div class="lb-word">进度</div>
									<div class="lb-prored">
										<div class="prored" style="width:${loanApplicationListVO.ratePercent}%;"></div>
									</div>
									<div class="lb-word">${loanApplicationListVO.ratePercent}%</div>
								</div>
							</div>
					</div>
					<div class="clear"></div>
					
					<div class="loanlist-bot-le-bt">
						<div class="loanlist-bot-le-bt-le">
							<p><i class="list-bot-icon1"></i>起投金额：<i class="list-bot-word" id="qitou">${loanApplicationListVO.startAmount}元</i></p>
							<p><i class="list-bot-icon2"></i>项目类型：<i class="list-bot-word">
								<c:if test="${loanApplicationListVO.loanType eq '4'}">保理项目</c:if>
								<c:if test="${loanApplicationListVO.loanType eq '5'}">基金</c:if>
								<c:if test="${loanApplicationListVO.loanType eq '3'}">信用贷</c:if>
								<c:if test="${loanApplicationListVO.loanType eq '6'}">企业标</c:if>
								<c:if test="${loanApplicationListVO.loanType eq '2'}">抵押车</c:if>
							</i></p>
						</div>
						<div class="loanlist-bot-le-bt-ri">
							<p><i class="list-bot-icon3"></i>
								<span class="cut_str" title='<c:if test="${loanApplicationListVO.loanType ne '4'}">${enterpriseInfo.enterpriseName}</c:if><c:if test="${loanApplicationListVO.loanType eq '4'}">${factoringSnapshot.sourceOfRepayment}</c:if>' >
									还款来源：
									<i class="list-bot-word">
										<c:if test="${loanApplicationListVO.loanType ne '4'}">
											<!-- 如果不为保理 -->
												<c:if test="${loanApplicationListVO.loanType eq '3' || loanApplicationListVO.loanType eq '6' }">
													<!-- 如果为企业信贷 -->
				            						经营收入
			            						</c:if>
												<c:if test="${loanApplicationListVO.loanType ne '3' && loanApplicationListVO.loanType ne '6' }">
													<!-- 如果不为企业信贷 -->
			            							${enterpriseInfo.enterpriseName}
		            							</c:if>
										</c:if>
										<c:if test="${loanApplicationListVO.loanType eq '4'}">${factoringSnapshot.sourceOfRepayment}</c:if></span>
           							</i>
							</p>
							<p><i class="list-bot-icon4"></i>还款方式：<i class="list-bot-word">
								<c:if test="${loanApplicationListVO.repayMethod eq '1'}">
									<c:forEach items="${repayMentMethod}" var="method">
										<c:if test="${method.value eq loanApplicationListVO.repayMentMethod}">${method.desc}</c:if>
									</c:forEach>
									</c:if>
								<c:if test="${loanApplicationListVO.repayMethod ne '1'}">
									<customUI:dictionaryTable constantTypeCode="repaymentMode"
										desc="true" key="${loanApplicationListVO.repayMethod}" />
								</c:if>
								</i></p>
						</div>
						<c:if test="${not empty guaranteeCompany}">
							<div class="loanlist-bot-le-bt-le">
								<p>
									<i class="list-bot-icon1"></i>担保公司：<label>${guaranteeCompany.companyName}</label>
								</p>
							</div>
						</c:if>
						 
					</div>
				</div>
				<div class="loanlist-bot-ri">
					<!--定向标start-->
					<c:if test="${not empty sessionScope.currentUser}">
      	    			<c:if test="${loanApplicationListVO.begin eq'true'}"> 
						<div class="detail-dxb" style="top:25px">
							<h2 class="detail-dxb-title">定向密码</h2>
							<h2 class="detail-dxb-tip">此标的为密码<span>定向标</span>，输入密码进行投标</h2>
							<div class="phonevacs">
								<input type="text" autocomplete="off" maxlength="1" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" class="pipt" id="piptime" >
								<input type="text" autocomplete="off" maxlength="1" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" class="pipt" id="piptime2" >
								<input type="text" autocomplete="off" maxlength="1" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" class="pipt" id="piptime3" >
								<input type="text" autocomplete="off" maxlength="1" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" class="pipt" id="piptime4" >
								<input type="text" autocomplete="off" maxlength="1" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" class="pipt" id="piptime5" >
								<input type="text" autocomplete="off" maxlength="1" onkeyup="value=value.replace(/[^\w\.\/]/ig,'')" class="pipt" id="piptime6" >
								<span class="imgre"></span>
							</div>
							<em id="emPass"></em>
						</div>
						</c:if>
					</c:if>
					<!--定向标end-->
					<!--水印图 预热中-->
					<c:if test="${loanApplicationListVO.begin eq'false'}"><b id="yure" class="biao biao5"></b></c:if>
					<!--水印图 已结清-->
					<c:if test="${loanApplicationListVO.applicationState eq '7'||loanApplicationListVO.applicationState eq '8'}"><b id="biao" class="biao biao4"></b></c:if>
					<!--水印图 满标-->
					<c:if test="${loanApplicationListVO.applicationState eq '4'||loanApplicationListVO.applicationState eq '5'}"><b id="biao" class="biao biao3"></b></c:if>
					<!--水印图 还款中-->
					<c:if test="${loanApplicationListVO.applicationState eq '6'}"><b id="biao" class="biao biao2"></b></c:if>
					<div class="clear_20"></div>
					<p>剩余金额：<span id="mony2" data-money="${loanApplicationListVO.remain}">
						<fmt:formatNumber value="${loanApplicationListVO.remain}" pattern="#,##0.00" /></span><span id="jiliang">元</span><i>（每人限投：<font id="limited">
						<fmt:formatNumber value="${loanApplicationListVO.maxBuyBalanceNow}" pattern="#,##0.00" /></font><span>元</span>）</i></p>
						<input id="limit_input" type="hidden" value="${loanApplicationListVO.maxBuyBalanceNow}" />
						<p>账户余额：
						<c:if test="${not empty sessionScope.currentUser}">
	                        <label id="yue"><fmt:formatNumber value="${cashAccount.availValue2}"
	                                                          pattern="#,##0.00"/>元</label>
	                        <input type="hidden" id="yue_input" value="${cashAccount.availValue2}"/>
	                    </c:if>
	                    <c:if test="${empty sessionScope.currentUser}">
	                        <font><a href="javascript:" style="color:#35B0EB;padding:0 5px;" data-mask='mask'
	                                 data-id="login">登录</a><a>查看</a></font>
	                    </c:if>
						</p>
					<c:if test="${not empty sessionScope.currentUser}">
						<input type="hidden" id="sessionScope" value="${sessionScope.currentUser}">
						<p class="mt-10">
							<span class="list-cfq">财富劵：<font>${useageSum}</font>元 <i id="list-cfq-icon-i" class="shang"></i><em>(${useageCount}张)</em></span>
						</p>
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
					</c:if>
					<p class="mt-20">
						<span class="list-inpit">
							<input type="text" id="money" style="margin-bottom:5px;" placeholder="请输入投标金额" maxlength="10" class="ipt-input"/><em class="yuan1">元</em>
						</span>
						<span class="list-btn">
							<c:if test="${not empty sessionScope.currentUser}">
								<c:if test="${userExt.isVerified ne '2'}">
									<button type="button" id="unpay" >
										提交订单，去支付
									</button>
								</c:if>
								<c:if test="${userExt.isVerified eq '2'}">
									<button type="button" id="pay" >
										提交订单，去支付
									</button>
								</c:if>
							</c:if>
							<c:if test="${empty sessionScope.currentUser}">
								<button type="button" data-mask='mask' data-id="login" >
									定向标，请先登录后再投标
								</button>
							</c:if>
						</span>
					</p>
					<div class="clear"></div>
					<p id="errorMoneyInfo" class="list-tip hui"><img src="../images/fdetail/dingpai.png" />实际支付金额满5000元，奖5元财富券</p>
					<div class="clear"></div>
					<p class="mt-20">
						<span class="list-toub">
							<button type="button" id="firstfinan" class="btn-small">最大可投</button>
							<button type="button" id="allbalance" <c:if test="${empty sessionScope.currentUser}">data-mask='mask' data-id="login"</c:if> class="btn-big">全部余额</button>
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
		<c:if test="${loanApplicationListVO.loanType eq '2'}">
	        <!--车贷-->
            <span class="current">项目信息</span>
            <span class="">公司信息</span>
            <span class="">抵押信息</span>
            <span class="">投标信息</span>
	    </c:if>
	    <c:if test="${loanApplicationListVO.loanType eq '3'}">
	        <!--信贷-->
            <span class="current">项目信息</span>
            <span class="">公司信息</span>
            <span class="">投标信息</span>
	    </c:if>
	    <c:if test="${loanApplicationListVO.loanType eq '4'}">
	        <!--保理-->
            <span class="current">项目信息</span>
            <span class="">风控信息</span>
            <span class="">项目保障方案</span>
            <span class="">保理公司</span>
            <span class="">投标信息</span>
	    </c:if>
	    <c:if test="${loanApplicationListVO.loanType eq '5'}">
	        <!--信贷-->
            <span class="current">项目详情</span>
            <span>常见问题</span>
	    </c:if>
	    <c:if test="${loanApplicationListVO.loanType eq '6'}">
	        <!--信贷-->
            <span class="current">项目信息</span>
            <span class="">公司信息</span>
            <span class="">投标信息</span>
	    </c:if>
	    <span class="border-right-none">还款列表</span>
	    
    </h6>

    
        <c:if test="${loanApplicationListVO.loanType eq '5'}">
            <div class="block">
				<!-- context start -->
                <div class="projectDetail">
                    <div class="prl pryhui">
                        <span class="prl1">委托人预期年化收益率：</span>
                        <span class="prl2">${loanApplicationListVO.annualRate}%，为定向委托投资标的到期产生的预期年化收益率扣除受托投资管理机构的管理费率。</span>
                    </div>
                    <div class="prl prybai">
                        <span class="prl1">收益起始日：</span>
                        <span class="prl2">
                            <c:if test="${empty loanApplicationListVO.paymentDate}">
                                	资金筹集完成时间
                            </c:if>
                            <c:if test="${not empty loanApplicationListVO.paymentDate}">
                                <fmt:formatDate value="${loanApplicationListVO.paymentDate}" pattern="yyyy年MM月dd日" />
                            </c:if>
                        </span>
                    </div>
                    <div class="prl pryhui">
                        <span class="prl1">收益到期日：</span>
                        <span class="prl2">
                            <c:if test="${empty loanApplicationListVO.lastRepaymentDate}">
                                	资金筹集完成后显示
                            </c:if>
                            <c:if test="${not empty loanApplicationListVO.lastRepaymentDate}">
                                <fmt:formatDate value="${loanApplicationListVO.lastRepaymentDate}" pattern="yyyy年MM月dd日" />
                            </c:if>
                        </span>
                    </div>
                    <div class="prl prybai">
                        <span class="prl1">期限：</span>
                        <span class="prl2">${loanApplicationListVO.cycleCount}个月</span>
                    </div>
                    <div class="prl pryhui">
                        <span class="prl1">预期收款日：</span>
                        <span class="prl2">不晚于<c:if test="${empty loanApplicationListVO.lastRepaymentDate}">
                           	 资金筹集完成后显示
                        </c:if>
                            <c:if test="${not empty loanApplicationListVO.lastRepaymentDate}">
                                <fmt:formatDate value="${loanApplicationListVO.lastRepaymentDate}" pattern="yyyy年MM月dd日" />
                            </c:if></span>
                    </div>
                    <div class="prl prybai">
                        <span class="prl1">定向委托投资标的：</span>
                        <span class="prl2"><customUI:dictionaryTable constantTypeCode="INVESTMENT_TYPE" desc="true" key="${foundationSnapshot.investmentType}"/></span>
                    </div>
                    <div class="prl pryhui">
                        <span class="prl1">标的说明：</span>
                        <span class="prl2"><a class="proa" href="${basePath}${attachment.url}"  target="_blank" >点击下载</a></span>
                    </div>
                    <div class="prl prybai">
                        <span class="prl1">定向委托投资标的风险提示：</span>
                        <span class="prl2">详见《风险提示函》下载<a class="proa" href="${basePath}${riskTip.url}"  target="_blank" >《风险提示函》pdf</a></span>
                    </div>
                    <div class="prl pryhui">
                        <span class="prl1">受托投资管理机构：</span>
                        <span class="prl2">${enterpriseInfo.enterpriseName}</span>
                    </div>
                    <div class="prl prybai">
                        <span class="prl1">托管机构：</span>
                        <span class="prl2">${coltd.companyName}</span>
                    </div>
                    <div class="prl pryhui">
                        <span class="prl1">相关下载：</span>
                        <span class="prl2"><a  href="${basePath}${tradeBook.url}" target="_blank">查看交易说明书</a>   &nbsp;&nbsp;   <a href="${ctx}/finance/download/disclaimer" target="_blank">免责声明</a></span>
                    </div>
                </div>
                <!-- context end -->
			</div>
	            <!--基金项目常见问题-->
	            <div class="div" id="question">
	                <!-- context start -->
	                <div class="usrallyQuest">
	                    <h2>什么是财富派一定向委托投资系列（下称"财富派系统"）？</h2>
	                    <p>“财富派系列”是北京汇聚融达公司网络科技有限公司（“蚂蚁投资”）为定向委托投资关系的委托人及受托人提供的居间服务。通过蚂蚁投资的居间服务，委托人将自有资金委托给受托人ＸＸＸ公司进行管理，投资于指定投资标的，以获取投资收益。</p>
	                    <h2>"财富派系列"定向委托投资是指什么？</h2>
	                    <p>定向委托投资是委托方基于对受托人的信任，自愿将合法所有的资金委托给受托人进行管理，投资于指定投资标的，受托人以委托人利益最大化为原则对委托资金按约定进行定向投资管理，以获取投资收益的法律行为。</p>
	                    <h2>"财富派系列”服务的特点是什么？</h2>
	                    <p>委托人具有特定性，为蚂蚁投资经特定条件筛选的注册会员。<br />
				                        受托人为具备受托投资管理经营范围的资产管理公司。<br />
				                        定向委托投资的标的为指定标的，且均为国内知名金融机构发行的正规金融产品。<br />
				                        定向委托投资在募集开始即明示指定投资标的类型、期限、收益以及投资风险等情况，投资人可根据自身投资偏好选择适合自己的投资项目。</p>
	                    <h2>“财富派系列”服务中委托人的收益方式是什么？</h2>
	                    <p>定向委托投资采用的是到期一次兑付法，受托人在不晚于定向投资标的收益到期日后5个工作日内兑付委托投资收益。如因发行机构未及时偿付等原因，致使委托人收款日迟于收益到期日后5个工作日的，受托人应向委托人发出通知，并保证在收到发行机构支付的投资标的收益后3个工作日内，将委托投资收益扣除相关费用后剩余部分足额支付至委托人收款账户内。</p>
	                    <h2>我可以提前收回投资吗？</h2>
	                    <p>在委托方交付委托资金日起，至委托投资清算期终止日止，委托方不得要求提前提取委托资金。</p>
	                    <h2>如果定向委托投资不成功怎么办？</h2>
	                    <p>委托方足额支付了委托投资资金的前提下，如因委托投资标的未成立等非受托人原因致使投资不成功的，受托人将通过蚂蚁投资平台通知委托人，并在收回委托资金后起3个工作日将委托资金本金足额支付至委托方收款账户内。</p>
	                    <h2>"财富派系列"投资开放期内是否可以撤销投资？</h2>
	                    <p>投资人在确认投资该项目后，资金被冻结，即进入“项目申请中”状态，或该投资项目正在“等待审核中”状态。资金冻结期间投资人是不能撤销投资的，请投资前谨慎考虑！</p>
	
	                </div>
	                <!-- context end -->
	            </div>
            
        </c:if>
        <!-- tab start -->
        <c:if test="${loanApplicationListVO.loanType eq '4'}">
            <div class="block">
	            <!--保理项目信息-->
	            <div class="list-box-main box-mb-none" id="diya">
	            	<ul>
						<li>融资方：${financeParty.companyNameStr}</li>
	                </ul>
	            	<ul>
						<li>融资金额：<fmt:formatNumber value="${factoringSnapshot.financingAmount}" pattern="#,##0.00"/>元</li>
	                </ul>
	            	<ul>
						<li>还款来源：${factoringSnapshot.sourceOfRepaymentStr}</li>
	                </ul>
	            </div> 
	            <div class="list-box-main box-mb-none">
	            	<ul>
						<li>年化利率：${loanApplicationListVO.annualRate}%</li>
	                </ul>
	            	<ul>
						<li>授信金额：<fmt:formatNumber value="${loanApplicationListVO.loanBalance}" pattern="#,##0.00"/>元</li>
	                </ul>
	            	<ul>
						<li>借款用途：<customUI:dictionaryTable constantTypeCode="enterpriseLoanUseage" desc="true"
		                                                    key="${loanApplicationListVO.loanUseage}"/></li>
	                </ul>
	            </div> 
	            <div class="list-box-main box-mb-none">
	            	<ul>
						<li>借款用途描述：${loanApplicationListVO.useageDesc}</li>
	                </ul>
	            </div> 
	            <div class="clear"></div>
            </div>
            <div class="div" id="thissitu">
	            <div class="list-box-main box-mb-none">
	            	<ul>
						<li>${factoringSnapshot.accountReceivableDescription}</li>
	                </ul>
	            	<ul>
						<li>款项风险评价：${factoringSnapshot.moneyRiskAssessment}</li>
	                </ul>
	            	<ul>
						<li>项目综合评价：${factoringSnapshot.projectComprehensiveEvaluati}</li>
	                </ul>
	            </div>
	            <div class="clear"></div>
	        </div>
            <div class="div" id="thissitu">
	            <div class="list-box-main box-mb-none">
	            	<p>360度实地尽调－大数据思维保障项目质量：${factoringSnapshot.fieldAdjustmentValue}</p>
	            	<p>还款保证金 - 构建风险缓释空间：${factoringSnapshot.repaymentGuaranteeValue}</p>
	            	<p>法律援助基金 - 平台资金支持护航维权启动：${factoringSnapshot.aidFundValue}</p>
	            </div>
	        </div>
        </c:if>
        <!--车贷、信贷项目信息-->
        <!-- tab start -->
        <c:if test="${loanApplicationListVO.loanType eq '2'||loanApplicationListVO.loanType eq '3'||loanApplicationListVO.loanType eq '6'}">
            <div class="block">
                <!-- colspan start -->
               	<div class="list-box-main">
	            	<p class="list-box-title"><i class="list-i-2"></i>标的信息</p>
					<ul>
							<li>借款用途： <customUI:dictionaryTable constantTypeCode="enterpriseLoanUseage" desc="true"
	                                                    key="${loanApplicationListVO.loanUseage}"/> </li>
	                </ul>
	                <ul>
							<li>项目地区： ${address} </li>
					</ul>
					<ul>
							<li>内部评级：<customUI:dictionaryTable constantTypeCode="internalRating" desc="true"
	                                                   key="${enterpriseCarLoanSnapshot.internalRating}"/></li>
					</ul>
					<div class="clear"></div>
                </div>
                <!-- colspan start -->
                <div class="list-box-main">
	            	<p class="list-box-title"><i class="list-i-11"></i>用途描述</p>
	            	<p>
	                    ${loanApplicationListVO.useageDesc}
	                </p>
                </div>
                <!-- colspan start -->
                <!-- colspan start -->
                <div class="list-box-main">
	            	<p class="list-box-title"><i class="list-i-10"></i>
                	项目描述
                	</p>
                	<p>${enterpriseCarLoanSnapshot.projectDescription}</p>
                </div>
                <!-- colspan start -->
                <!-- colspan start -->
                <div class="list-box-main">
				<p class="list-box-title"><i class="list-i-7"></i>审核信息</p>
				<ul class="shxx-add">
					<!-- 基本证照、财务报表、公司章程、法人信息、股东决议、保证措施、实地考察、经营规模、行业前景、失信调查 -->
						<li><img src="${ctx }/images/icon_shxx/icon_jbzz.png" /><br />基本证照</li>
						<li><img src="${ctx }/images/icon_shxx/icon_cwbb.png" /><br />财务报表</li>
						<li><img src="${ctx }/images/icon_shxx/icon_gszc.png" /><br />公司章程</li>
						<li><img src="${ctx }/images/icon_shxx/icon_frxx.png" /><br />法人信息</li>
						<li><img src="${ctx }/images/icon_shxx/icon_gdjy.png" /><br />股东决议</li>
						<li><img src="${ctx }/images/icon_shxx/icon_bzcs.png" /><br />保证措施</li>
						<li><img src="${ctx }/images/icon_shxx/icon_sdkc.png" /><br />实地考察</li>
						<li><img src="${ctx }/images/icon_shxx/icon_jygm.png" /><br />经营规模</li>
				   </ul>
					<ul class="shxx-add">
						<li><img src="${ctx }/images/icon_shxx/icon_hyqj.png" /><br />行业前景</li>
						<li><img src="${ctx }/images/icon_shxx/icon_sxdc.png" /><br />失信调查</li>
					</ul>
				<div class="clear"></div>
			</div>
			
                <!-- colspan start -->
                <div class="list-box-main ">
	            	<p class="list-box-title"><i class="list-i-8"></i>
                	风控信息
                	</p>
	            	<p>
                		${enterpriseCarLoanSnapshot.riskControlInformation}
                	</p>
                </div>
                <!-- colspan start -->
                <div class="list-box-main ">
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
													<img src="${customersnapshot.attachment.thumbnailUrl}" alt="" />
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
                <div class="list-box-main box-mb-none">
					<p class="list-box-title">
						<i class="list-i-9"></i>公司证明
					</p>
					<!-- 左右滚动部分 begin -->
					<div class="main_lunbo">
						<c:if test="${not empty enterpriseInfoSnapshots}">
							<div class="leftbtn" id="left2"></div>
						</c:if>
						<div id="photo">
							<div class="Cont">
								<div class="ScrCont" id="viewer2">
									<div class="List1" id="viewerFrame2">
										<c:forEach items="${enterpriseInfoSnapshots}"
											var="customersnapshot">
											<div class="list-pic-p">
												<a href="javascript:" rel="lightbox[plants]" title="">
													<img src="${customersnapshot.attachment.thumbnailUrl}" alt="" />
												</a>
												<p>${customersnapshot.attachment.fileName}</p>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
						<c:if test="${not empty enterpriseInfoSnapshots}">
							<div class="rightbtn" id="right2"></div>
						</c:if>
					</div>
					<!-- 左右滚动部分 end -->
					<div class="clear"></div>
				</div>
			</div>
         </c:if>
        <!-- tab end -->
        <!--公司信息-->
        <div class="div" id="thissitu">
            <div class="list-box-main box-mb-none">
            	<p class="list-box-title"><i class="list-i-qy"></i>公司信息</p>
                <ul>
						<li>企业名称：  ${enterpriseInfo.jmEnterpriseName}</li>
						<li>组织机构代码： ${enterpriseInfo.jmOrganizationCode}</li>
				</ul>
                <ul>
						<li>经营年限： ${enterpriseInfo.operatingPeriod}年</li>
						<li>注册资金：<fmt:formatNumber value="${enterpriseInfo.registeredCapital}" pattern="#,##0.00"/>元</li>
				</ul>
                <ul>
						<li>企业信息：${enterpriseInfo.information}</li>
				</ul>
				<div class="clear"></div>
            </div>
        </div>
        
        <!--车贷抵押信息-->
        <c:if test="${loanApplicationListVO.loanType eq '2'}">
        	<div class="div" id="thissitu">
            <!-- tab start -->
            <div class="list-box-main box-mb-none" id="wangsitu">
                <ul>
						<li>授信上限： ${enterpriseCarLoanSnapshot.creditLimit}万元</li>
				</ul>
				<ul>
						<li>授信比例： ${enterpriseCarLoanSnapshot.creditLimitRate}%</li>
				</ul>
				<ul>		
						<li>描述：${enterpriseCarLoanSnapshot.mortgageDescription}</li>
				</ul>
                <p>
						汽车列表： 总计&nbsp;&nbsp;${totalPrice}万元
				</p>
				<p>
				<table id="fbtable" class="ftable" border="0" cellpadding="0" cellspacing="0">
		            
				</table>
                <div class="finance-page"></div></p>
				<div class="clear"></div>
            </div>
            </div>
            <!-- tab end -->
        </c:if>
        <!--借款详情end-->
		<!-- 投标信息 start -->
		<div class="div">
		    <div class="bid_information">
		        <ul class="list-ul">
		            <li class="list-ul-first">
		                <ul>
							<li>投标人</li>
							<li>投标金额（元）</li>
							<li>投标日期</li>
						</ul>
		            </li>
		        </ul>
		        <ul id="tbtable" class="list-ul">
		
		        </ul>
		    </div>
		    <div id="pageList" class="wrapper">
		
		    </div>
		</div>
		<!-- 投标信息 end -->
		
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
				  <c:if test="${isRepaying}">
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
				  </c:if>
				  <c:if test="${!isRepaying}">
				  <c:forEach items="${showRepaypaymentList }" var="repay"  >
						   <tr>
							 <td>-</td>
							 <td>${repay.sectionCode}</td>
							 <td><fmt:formatNumber value="${repay.shouldCapital2 }" pattern="#,##0.00"/>元</td>
							 <td><fmt:formatNumber value="${repay.shouldInterest2 }" pattern="#,##0.00"/>元</td>
							 <td><fmt:formatNumber value="${repay.shouldBalance2 }" pattern="#,##0.00"/>元</td>
							 <td>未生效</td>
						  </tr>
				    </c:forEach>		
				  </c:if>
				  
			 </table>
    	</div>
    </div>
    
</div>


<!-- masklayer start  -->
<div class="masklayer masklback" id="cjd">
    <h2 class="clearFloat"><span>投标</span> <a href="javascript:" data-id="close"></a></h2>
    <!-- equity start -->
    <div class="flbuy">
        <p class="clearFloat"><span class="fsp1">借款名称：</span> <span 
                                                                    id="title">${loanApplicationListVO.loanApplicationTitle}</span>
        </p>

        <p class="clearFloat"><span class="fsp1">投标金额：</span> <span ><big class="c_red"><i id="buymoney">10,000.00</i></big>元</span>
        </p>

        <p class="clearFloat"><span class="fsp1">预期收益：</span> <span ><small id="expected" class="c_red">
            300.05
        </small>元</span></p>
        <p class="clearFloat"><span class="fsp1">账户余额：</span> <span ><small id="account"><fmt:formatNumber
                value="${cashAccount.availValue2}" pattern="#,##0.00"/></small>元</span></p>


        <form action="${ctx}/finance/bidLoanByAccountBalance" id="lendForm" method="post" name="form" class=" mt-20">
            <input type="hidden" id="loanApplicationId" name="loanApplicationId"
                   value="${loanApplicationListVO.loanApplicationId}"/>
            <input type="hidden" id="amount" name="amount" value=""/>
            <input type="hidden" id="token" name="token" value="${token}"/>
            <input type="hidden" id="oPaww" name="oPass" value=""/>
            <input style="display:none"/>

            <div class="input-group">
                <label for="jypassword">
                    <span class="fsp1">交易密码：</span>
                    <input type="password" value="" id="jypassword" name="password" placeholder="请输入交易密码"
                           onfocus="if(this.value==defaultValue) {this.value='';this.type='password'}"
                           onblur="if(!value) {value=defaultValue; this.type='text';}"
                           class="ipt-input"/>
                    <a href="#" id="ed_ex_psdad" class="ml-20">找回交易密码</a>
                </label>

                <em class="ml-90"></em>
            </div>
            <div class="input-group">
                <label for="checkBox">
                    <input type="checkbox" name="checkBox" id="checkBox" checked class="mr-5 ml-90 ipt-check"/>
                    <a href="javascript:" class="ahui" data-mask="mask" data-id="serverProtocol">《出借咨询与服务协议》</a>
                </label>
                <em class=" ml-90"></em>
            </div>
            <div style="height:10px;clear:both;"></div>
            <div class="groupbtn clearFloat">
                <label>
                    <button type="button" class="btn btn-error ml-90 mt-0" id="tbpay">确认支付</button>
                </label>
                <label>
                   <button type="button" id="otherPay" class="btn btn-write ml-20 mt-0 help-tips" style="cursor: pointer;">其它方式支付<p><img src="${ctx}/images/cfq_icon.jpg" />戳我，戳我，使用优惠券吧~</p></button>
                </label>
            </div>
        </form>
		<div style="height:50px;clear:both;"></div>
    </div>
    <!-- equity start -->

</div>
<!-- masklayer end -->
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<!-- jquery 类库 javascript -->
<script type="text/javascript" src="${ctx}/js/financeBidding.js"></script>
<!-- financeDetail javascript -->
<%--<script type="text/javascript" src="${ctx}/js/financeList.js"></script><!-- public javascript -->--%>
<script type="text/javascript" src="${ctx}/js/jquery_page.js"></script>
<!-- public javascript -->
<script src="${ctx}/js/slides-1.1.1-min.js" type="text/javascript"></script>
<script type="text/javascript">

    $(function () {
        $('.succesny').olvSlides({
            thumb: false,
            thumbPage: true,
            thumbDirection: "X",
            effect: 'fade'

        });
    });
</script>

<script src="${ctx}/js/script/jquery-1.7.2.min.js"></script>

<script type="text/javascript" src="${ctx}/js/script/jquery.imageScroller.js"></script>
<script type="text/javascript">
    //点击确认支付
    $("#otherPay").click(function () {
        //其他方式支付
        $("#amount").val($(".yuan1").html() == "万" ? parseFloat($("#money").val()) * 10000 : $("#money").val());
        $("#lendForm").attr("action", rootPath + "/finance/toBuyBidLoanByPayAmount").submit();
    });

    var j = jQuery.noConflict();
    j("#viewer").imageScroller({
        next: "right",
        prev: "left",
        frame: "viewerFrame",
        width: 120,
        child: "div",
        auto: false
    });
    j("#viewer2").imageScroller({
        next: "right2",
        prev: "left2",
        frame: "viewerFrame2",
        width: 120,
        child: "div",
        auto: false
    });
    j(function () {
        j(".masklayer1").hide();
        j(document).on("click", "#viewerFrame>div img", function () {
        	// --s
        	var Top = document.documentElement.clientHeight/2-(parseInt($(".masklayer1").height())/2) +'px',
            Left = document.documentElement.clientWidth/2-(parseInt($(".masklayer1").width()))/2 +'px';
        	// --e
            j(".zhezhao").show();
         	// --s
        	j("#bigpicture").css("top",Top);
          	j("#bigpicture").css("left",Left);
        	// --e
            j("#bigpicture").slideDown(500);
        });
        j(document).on("click", "#viewerFrame2>div img", function () {
        	// --s
        	var Top = document.documentElement.clientHeight/2-(parseInt($(".masklayer1").height())/2) +'px',
            Left = document.documentElement.clientWidth/2-(parseInt($(".masklayer1").width()))/2 +'px';
        	// --e
            j(".zhezhao").show();
         	// --s
        	j("#bigpicture2").css("top",Top);
          	j("#bigpicture2").css("left",Left);
        	// --e
            j("#bigpicture2").slideDown(500);
        });

        j(".successlunbo i.close").click(function () {

            j(".zhezhao").hide();
            j(".masklayer1").hide();
        });
    })

</script>

<script type="text/javascript">
    $(function () {
        //还款人列表
        searchHtml(1, 10, true);
        //汽车列表
        <c:if test="${loanApplicationListVO.loanType eq '2'}">
        	searchCardHtml(1, 5, true);
        </c:if>
        <c:if test="${loanApplicationListVO.begin eq 'false'||loanApplicationListVO.applicationState ne '3'}">
	        $(".loanlist-bot-ri input.ipt-input[type=text]:visible,.loanlist-bot-ri button").each(function() {
	          $(this).attr("disabled",true);
	        });
	      </c:if>
	  	<c:if test="${loanApplicationListVO.begin eq'false'&&loanApplicationListVO.applicationState eq '3'}">
	  		var btn = $("#p_button>button");
	  		timer(${secondBetwween},btn);
	  	</c:if>
    });
    //出借列表查询
    function searchHtml(page, rows, flag) {
        var thtml = "";
        $('.flcontext2').html("");
        if (flag) {
            $("#pageList").html("");
            $("#pageList").html('<div id="userPageCode" class="tcdPageCode"></div>');
        }

        $.ajax({
            url: rootPath + "/finance/getLender",
            type: "post",
            data: {
                "pageSize": rows,
                "pageNo": page,
                "loanApplicationId": ${loanApplicationListVO.loanApplicationId}
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
                    thtml += '<li class="list-ul-line-height">' + dateTimeFormatter(data.lendTime) + '</li>';
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

    //车贷列表查询
    function searchCardHtml(page, rows, flag) {
        var thtml = "";
        if (flag) {
            $(".finance-page").html("");
            $(".finance-page").html('<div id="cardPageCode" class="tcdPageCode"></div>');
        }

        $.ajax({
            url: rootPath + "/finance/getCardList",
            type: "post",
            data: {
                "pageSize": rows,
                "pageNo": page,
                "loanApplicationId": ${loanApplicationListVO.loanApplicationId}
            },
            success: function (data) {
                var d_rows = data.rows;
                var pageCount = data.totalPage;
                thtml += ' <tr class="ttitle"> ' +
		                '<td>序号</td> ' +
		                '<td>类型</td>' +
		                ' <td>汽车品牌</td>' +
		                ' <td>汽车型号</td>' +
		                ' <td>市场价格（万）</td>' +
		                ' <td>车架号</td>' +
		                ' <td>变更信息</td>' +
		                ' </tr>';
                for (var i = 0; i < d_rows.length; i++) {
                    var data = d_rows[i];
                    var change = data.changeDesc == null ? "" : data.changeDesc;
                    thtml += '<tr>';
                    thtml += '<td>' + ((page - 1) * 5 + i + 1) + '</td>';
                    thtml += '<td>' + data.arrived + '抵</td>';
                    thtml += '<td style="font-size:12px!important">' + data.automobileBrand + '</td>';
                    thtml += '<td style="font-size:12px!important">' + data.carModel + '</td>';
                    thtml += '<td style="font-size:12px!important">' + fmoney(data.marketPrice, 2) + '</td>';
                    thtml += '<td style="font-size:12px!important">' + data.frameNumber + '</td>';
                    thtml += '<td style="font-size:12px!important">' + change + '</td>';
                    thtml += '</tr>';
                }
                $('#fbtable').html(thtml);
                if (d_rows.length > 0 && flag) {
                    $("#cardPageCode").createPage({
                        pageCount: pageCount,
                        current: 1,
                        backFn: function (p) {
                            //点击分页效果
                            searchCardHtml(parseInt(p), 5, false);
                        }
                    });
                }
            }
        });

    }

    //调用不同倒计时方法
    var intDiff = parseInt(${secondBetwween});//倒计时总秒数量
    //年月日倒计时
    function timer(intDiff, DHtml) {

        var interval = window.setInterval(function () {
            var day = 0,
                    hour = 0,
                    minute = 0,
                    second = 0;//时间默认值
            if (intDiff > 0) {
                day = Math.floor(intDiff / (60 * 60 * 24));
                hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
                minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
                second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
            }
            if (intDiff < 0) {

            	$(".loanlist-bot-ri input.ipt-input[type=text]:visible,.loanlist-bot-ri button").each(function() {
                	$(this).attr("disabled",false);
                });
            	
                DHtml.html('提交订单，去支付');

                clearTimeout(interval);
                $("#yure").attr("class", "");
                return;
            }

            if (minute <= 9) minute = '0' + minute;
            if (second <= 9) second = '0' + second;
            DHtml.html('剩余时间' + day + "天" + hour + "时" + minute + '分' + second + '秒');

            intDiff--;
        }, 1000);
    }


</script>
<!--借款详情end-->
<!-- tabdiv start -->

<!-- masklayer start  -->


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
                                <div><img src="${picPath}${customersnapshot.attachment.thumbnailUrl}" url="url"
                                          bigImg="${picPath}${customersnapshot.attachment.url}"
                                          alt="${customersnapshot.attachment.fileName}"></div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>

<div class="masklayer1" id="bigpicture2" style="display: none;">
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
                        <c:forEach items="${enterpriseInfoSnapshots}" var="customersnapshot">
                            <li>
                                <div><img src="${picPath}${customersnapshot.attachment.thumbnailUrl}" url="url"
                                          bigImg="${picPath}${customersnapshot.attachment.url}"
                                          alt="${customersnapshot.attachment.fileName}"></div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
    </div>

</div>
<!-- masklayer end -->
<!--飘窗登录前start-->
 <div class="bay-window" id="bay-window">
    <c:if test="${not empty sessionScope.currentUser}">
        <div class="bay-window" id="bay-windows">
            <div class="bay-wd-main">
                <div class="bay-wd-main-le2">
                    <span class="span-left1"><img
                            src="${ctx}/images/pc-icon3.png"/>${loanApplicationListVO.loanApplicationTitle}</span>
                    <span class="span-right1"><img src="${ctx}/images/pc-icon4.jpg"/>剩余可投金额 <font
                            class="bay-wd-main-red"><fmt:formatNumber value="${loanApplicationListVO.remain}"
                                                                      pattern="#,##0.00"/></font><i>元</i></span>
                </div>
                <div class="bay-wd-main-ri2">

                    <span class="bay-input"><input type="text" id="bay_ipt_money"
                        <c:if test="${loanApplicationListVO.begin eq'false' || loanApplicationListVO.applicationState ne '3'}">class="iptdis" disabled </c:if>/><i class="bay-yuan" id="bay-yuan">元</i></span>

                        <!--预热中-->
                        <c:if test="${loanApplicationListVO.begin eq'false'}">
                            <span class="baycolor">预热中</span>
                        </c:if>
                        <!--非预热中-->
                        <c:if test="${loanApplicationListVO.begin ne'false'&& loanApplicationListVO.applicationState eq '3'}">
                            <c:if test="${userExt.isVerified ne '1'}" >
                                <span class="bay-chujie"><a href="javascript:" id="un_pay">立即出借</a></span>
                            </c:if>
                            <c:if test="${userExt.isVerified eq '1'}" >
                                <span class="bay-chujie"><a href="javascript:" id="bay_pay">立即出借</a></span>
                            </c:if>
                        </c:if>
                        <c:if test="${loanApplicationListVO.applicationState ne '3'}">
                            <span class="baycolor">立即出借</span>
                        </c:if>
                        <div class="clear"></div>

                    <em></em>
                </div>

            </div>
        </div>
    </c:if>
    <c:if test="${empty sessionScope.currentUser}">
        <div class="bay-wd-main">
            <div class="bay-wd-main-le">
                <span class="span-left"><img
                        src="${ctx}/images/pc-icon3.png"/>${loanApplicationListVO.loanApplicationTitle}</span>
                <span class="span-right"><img src="${ctx}/images/pc-icon4.jpg"/>剩余可投金额 <font
                        class="bay-wd-main-red"><fmt:formatNumber value="${loanApplicationListVO.remain}"
                                                                  pattern="#,##0.00"/></font><i>元</i></span>
            </div>
            <div class="bay-wd-main-ri">
                <a href="javascript:" data-mask='mask' data-id="login" class="btn btn-error bay-login">登录</a>
                <a href="${ctx}/user/regist/home" class="bay-register ml-20">快速注册</a>
            </div>
        </div>
    </c:if>
</div> 
<!--飘窗end-->
<script>
    //当滚动条的位置处于距顶部100像素以下时，跳转链接出现，否则消失
    $(function () {
        $(window).scroll(function () {
            if ($(window).scrollTop() > 100) {
                if ($(window).scrollTop() > 100) {
        			if($("#isLogin").val()=="true"){
        				if($("#isTrue").val()=="false"){
        					  $(".bay-window").fadeIn(1500);
        				}
        			}
        		}
              
            } else {
                $(".bay-window").fadeOut(1500);
            }
        });
    });
</script>

<!--registerer protocol-->
<%@include file="../common/serverProtocol.jsp" %>

<%@include file="../common/carPerson.jsp"%>

<%@include file="../login/login.jsp" %>
<!-- tabdiv end -->
<!-- footerindex start -->
<%@include file="../common/footLine3.jsp"%>
<!-- 返回顶部 -->
<%--<div class="Return_top">
    <div id="back-to-top">
        <dl>
            &lt;%&ndash;<dd><a href="javascript:;"><img src="${ctx}/images/Return_top1.jpg" /></a></dd>&ndash;%&gt;
            <dd id="top"><a href=""><img src="${ctx}/images/Return_top3.jpg"/></a></dd>
        </dl>
    </div>
</div>--%>
<!-- 返回顶部 end-->


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
                    <input type="text" id="ex_valid" value="" autocomplete="off" maxlength="6" placeholder="请输入验证码"
                           style=""/>
                    <button type="button" id="ex_get_valid" class="huoqu_yanzm">获取验证码</button>
                </label>
                <em></em>
            </div>
            <div class="input_box_phone ipt_box_phone">
                <button type="button" id="next_sub1">下一步</button>
            </div>
        </form>
    </div>
    <div class="xiugai_phone_main display-none" id="phone_d2">
        <img src="${ctx}/images/ed_ex_psd2.jpg" class="mt-30 mb-30"/>

        <form action="" class="form" method="post">
            <div class="input_box_phone input_box ipt-box-ex">
                <label>
                    <span>输入新交易密码</span>
                    <input type="password" class="width200" id="ed_ex_psd1" value="" maxlength="16" style="width:160px;"
                           autocomplete="off" value="" onFocus="if(value==defaultValue){value=''}"
                           onBlur="if(!value){value=defaultValue}" onKeyUp="ed_ex_Check(this.value)"/>

                    <div type="button" id="rejc_ex_psd" class="Tcolor floatLeft">无</div>
                </label>
                <em class="hui fontsize12">交易密码为6 -16 位字符，支持字母及数字,字母区分大小写</em>
            </div>
            <div class="input_box_phone">
                <label>
                    <span>再次输入新交易密码</span>
                    <input type="password" class="width200" value="" onFocus="if(value==defaultValue){value=''}"
                           onBlur="if(!value){value=defaultValue}" id="ed_ex_psd2" value="" maxlength="16"
                           autocomplete="off"/>
                </label>
                <em></em>
            </div>
            <div class="input_box_phone ipt_box_phone">
                <button type="button" id="next_sub2">下一步</button>
            </div>
        </form>
    </div>
    <div class="xiugai_phone_main display-none" id="phone_d3">

        <img src="${ctx}/images/ed_ex_psd3.jpg" class="mt-30 mb-30"/>

        <p class="mt-50"><img src="${ctx}/images/img/true.jpg"/><span>交易密码重置成功！</span></p>

        <div class="input_box_phone ipt_box_phone" style="margin-top:85px;">
            <a href="javascript:" id="ed_ex_psda">
                <button>确认</button>
            </a>
        </div>
    </div>
</div>
<!-- 找回交易密码-结束 -->

<!-- toopInfo start -->
<div class="masklayer" id="toopInfo">
	<h2 class="clearFloat"><span>提示信息 </span><a href="javascript:;" id="closeCFQ"></a></h2>
    <div class="tipsInfo">
    <!-- 	<p class="titleRed">
        	财富券正式上线
        </p>
       <p class="contextBack">
       	参与投标，来获取您的财富券吧~
       </p> -->
       <img src="../images/fdetail/detail_03.jpg" /><br />
       <a href="javascript:;" class="btn btn-error" data-id="close">好的</a>
    </div>
</div>
<!-- toopInfo end -->
<%@include file="agreement.jsp"%>
</body>
<script>
    //返回顶部
    $(function () {
        $(window).scroll(function () {
            if ($(window).scrollTop() > 100) {
                $("#top").fadeIn(500);
            }
            else {
                $("#top").fadeOut(500);
            }
        });
        //当点击跳转链接后，回到页面顶部位置
        $("#top").click(function () {
            $('body,html').animate({scrollTop: 0}, 1000);
            return false;
        });
    });
    $("#piptime").keyup(function(){
    	if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
    		$("#piptime2").focus();
    		tijiao();
    	}
    });
    $("#piptime2").keyup(function(e){
    	if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
    		$("#piptime3").focus();
    		tijiao();
    	}
    　 if(e.keyCode == 8){
    		$("#piptime").focus();
       }
    });
    $("#piptime3").keyup(function(e){
    	if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
    		$("#piptime4").focus();
    		tijiao();
    	}
    　 if(e.keyCode == 8){
    		$("#piptime2").focus();
       }
    });
    $("#piptime4").keyup(function(e){
    	if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
    		$("#piptime5").focus();
    		tijiao();
    	}
    　 if(e.keyCode == 8){
    		$("#piptime3").focus();
       }
    });
    $("#piptime5").keyup(function(e){
    	if (/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())) {
    		$("#piptime6").focus();
    		tijiao();
    	}
    　 if(e.keyCode == 8){
    		$("#piptime4").focus();
       }
    });
    $("#piptime6").keyup(function(e){
    　 if(e.keyCode == 8){
    		$("#piptime5").focus();
       }
    });
    $("#piptime6").keyup(function(){
    	if(/^([a-z]|[A-Z]|[0-9])$/.test($(this).val())){
    		tijiao();
    	}else{
    		$(".imgre").html('');
    	}
    });
    //定向密码输入后提交
    function tijiao(){
    	  <c:if test="${empty sessionScope.currentUser}">
    		$("#login").show();
    		$(".zhezhao").show();
    		$("#piptime").attr("disabled","disabled");
    		$("#piptime2").attr("disabled","disabled");
    		$("#piptime3").attr("disabled","disabled");
    		$("#piptime4").attr("disabled","disabled");
    		$("#piptime5").attr("disabled","disabled");
    		$("#piptime6").attr("disabled","disabled");
    		 // setInterval("myInterval()",3000);//1000为1秒钟
    	     
    		 
    	  </c:if>
    		var i1=$("#piptime").val();
    		var i2=$("#piptime2").val();
    		var i3=$("#piptime3").val();
    		var i4=$("#piptime4").val();
    		var i5=$("#piptime5").val();
    		var i6=$("#piptime6").val();
    	
    		if( i1!="" &&  i2!="" &&  i3!="" &&  i4!="" &&  i5!="" && i6!=""){
    			//$(".imgre").html('<img src="../images/loading220.gif">');
    			//$(".detail-dxb").fadeOut(1000);
    			var opass =i1+i2+i3+i4+i5+i6 ;
    			$("#oPaww").val(opass);
    		  
    			 
    		    $.ajax({
    		        url:rootPath+"/finance/getPass",
    		        type:"post",
    		        data: {
    		          "pass":i1+i2+i3+i4+i5+i6,
    		          "loanApplicationId": ${loanApplicationListVO.loanApplicationId}
    		        },
    		        success: function (data) {
    		        	if(data=="sucess"){
    		        	$(".detail-dxb").slideUp(500);
    					//$("#money").attr("disabled",false);
    		        	$("#isTrue").val("false");
    		        	}else if(data=="fail"){
    		        		$("#emPass").html("定向密码错误请重试");
    		        	}
    		        },error:function(data){
    		        	console.log("密码错误");
    		        	$("#emPass").html("定向密码错误或者没有登录请重试");
    		        }
    		      });
    			
    			//$(".detail-dxb").hide();
    		}else{
    			
    		}
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

