<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="couponList">
	<div class="page">
		<c:forEach items="${vouchers}" var="voucher">
			<div class="couponBox cfqBj" data-id="${voucher.voucherId}" data-type="1" data-il="${voucher.isOverly}" data-val="${voucher.voucherValue}">
				<div class="couponCon">
					<p class="couponText1">
						<i class="coupon_i color_red">￥</i>
						<span class="color_red">${voucher.voucherValue}</span>
						<i class="coupon_pre color_red">元</i>
						<font class="color_red">财富券</font>
					</p>
					<p class="couponText2 color_666">
						条件：
						<c:if test="${empty voucher.voucherRemark}">——</c:if>
						<c:if test="${not empty voucher.voucherRemark}">${voucher.voucherRemark}</c:if>
					</p>
					<p class="couponText3 color_333">
						<c:if test="${empty voucher.endDate}">有效期限：长期有效</c:if>
						<c:if test="${not empty voucher.endDate}">有效期<fmt:formatDate value="${voucher.endDate}" pattern="yyyy-MM-dd" /></c:if>
					</p>
				</div>
			</div>
		</c:forEach>
		<c:forEach items="${rateUsers}" var="voucher">
			<div class="couponBox jxqBj" data-id="${voucher.rateUserId}" data-type="2" data-il="${voucher.isOverlay}" data-val="${voucher.rateValue}">
				<div class="couponCon">
					<p class="couponText1">
						<span class="color_yellow">${voucher.rateValue}</span>
						<i class="coupon_pre color_yellow">%</i>
						<font class="color_yellow">加息券</font>
					</p>
					<p class="couponText2 color_666">
						条件：
						<c:if test="${empty voucher.conditionStr}">无</c:if><c:if test="${not empty voucher.conditionStr}">${voucher.conditionStr}</c:if>
					</p>
					<p class="couponText3 color_333">
						<c:if test="${empty voucher.endDate}">有效期限：长期有效</c:if>
						<c:if test="${not empty voucher.endDate}">有效期<fmt:formatDate value="${voucher.endDate}" pattern="yyyy-MM-dd" /></c:if>
					</p>
				</div>
			</div>
		</c:forEach>
	</div>
</div>
<button class="w_close">确定</button>