<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../common/taglibs.jsp"%>
<!DOCTYPE html>

					<p class="quickPay">
						<img src="${ctx}/images/user-center/pay_061.jpg" style="margin-left:10px;" />一键支付
					</p>
					<div class="cz-list-le">
						<div class="input-group">
							<label>
								<span class="cz-span">开户名：</span>
									 <c:if test="${userExt.isVerified ne '1'}">
            							<small>＊您未进行身份验证，请先进行
											<a  data-mask='mask' data-id="shenfen" href="javascript:;">身份验证</a>再进行充值操作
										</small>
						    		</c:if>
					     		   <c:if test="${userExt.isVerified eq '1'}">
					         			${userExt.realName}
					        	  </c:if>
							</label>
						</div>
						<div class="input-group mt-30">
							<label for="bankid">
								<span class="cz-span fash-3">银行卡号：</span>
								<input type="text" id="bankid" size="25" onKeyUp="formatBankNo(this)" onKeyDown="formatBankNo(this)"  name="cardNo" class="ipt-input user-cz-input" 
								 <c:if test="${not empty hisCustomerCard && isSupport eq 'true' }">value="${hisCustomerCard.cardCode}"</c:if>  />
								<i id="bankshow">
								<%-- <img src="${ctx }/images/news_icon/bangka.png" class="buttonimgdetail ml-20" />中国银行 --%></i>
							</label>
							<em class="hui cz-em">*仅支持<font color="#fe2a4d">${userExt.realName}</font>的银行卡进行充值</em>
							<span id="userBankName" style="display: none;">*仅支持"<font color="#fe2a4d">${userExt.realName}</font>"的储蓄卡进行充值</span>
						</div>
						<div class="input-group mt-20">
							<label for="password">
								<span class="cz-span">付款限额：</span>
								<a href="javascript:;" class="cz-list-a"  data-mask="mask" data-id="p_bank"  >点击查看</a>
							</label>
						</div>
						<div class="input-group mt-30">
							<label for="moneyp">
								<span class="cz-span fash-3">充值金额：</span>
								<input type="text"  id="moneyp" name="rechargeAmount"   class="ipt-input user-cz-input" />
							</label>
							<em class="hui cz-em">*充值最小金额不得小于<font color="#fe2a4d">100</font>元</em>
						</div>
					</div>
					<div class="cz-list-ri">
						<span><img src="${ctx }/images/news_icon/text2_icon.png" />绑卡提醒</span>
						<p>为使您的投资更加方便快捷，我司的第三方支付平台已全面升级为连连支付，您需要在连连支付平台重新绑定银行卡。如有问题，请咨询客服400-061-8080</p>
					</div>
					<div class="clear_0"></div>
