<%@ page language="java" import="java.Math.*"  pageEncoding="UTF-8"%>
<%@include file="../common/taglibs.jsp"%>

<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes" />    
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
	<meta name="format-detection" content="telephone=no"/>
	<meta name="msapplication-tap-highlight" content="no" />
	<%@include file="../common/common_js.jsp"%>
	<link rel="stylesheet" href="${ctx }/css/reset2.css" type="text/css">
	<link rel="stylesheet" href="${ctx }/css/register2.css" type="text/css">
	<title>体验店查询</title>
	<style>
		.animate{
			width: 20px;
			height: 20px;
			display: block;
			background: url(../images/icon_phone.png) no-repeat;
			animation: myfirst .5s linear .1s infinite;
			-moz-animation: myfirst .5s linear .1s infinite;
			-webkit-animation: myfirst .5s linear.1s infinite;
			-o-animation: myfirst .5s linear.1s infinite;
			-ms-animation: myfirst .5s linear.1s infinite;
		}
		.animate img{
			width: 100%;
			height: 100%;
		}
		@keyframes myfirst{
		      0%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
			  50%{
			    -webkit-transform:rotate(-20deg);
			    -moz-transform:rotate(-20deg);
			    -o-transform:rotate(-20deg);
			    transform:rotate(-20deg);
			  }
			  100%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
		}
		@-webkit-keyframes myfirst{
		      0%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
			  50%{
			    -webkit-transform:rotate(-20deg);
			    -moz-transform:rotate(-20deg);
			    -o-transform:rotate(-20deg);
			    transform:rotate(-20deg);
			  }
			  100%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
		}
		@-moz-keyframes myfirst{
		      0%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
			  50%{
			    -webkit-transform:rotate(-20deg);
			    -moz-transform:rotate(-20deg);
			    -o-transform:rotate(-20deg);
			    transform:rotate(-20deg);
			  }
			  100%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
		}
		@-o-keyframes myfirst{
		      0%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
			  50%{
			    -webkit-transform:rotate(-20deg);
			    -moz-transform:rotate(-20deg);
			    -o-transform:rotate(-20deg);
			    transform:rotate(-20deg);
			  }
			  100%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
		}
		@-ms-keyframes myfirst{
		      0%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
			  50%{
			    -webkit-transform:rotate(-20deg);
			    -moz-transform:rotate(-20deg);
			    -o-transform:rotate(-20deg);
			    transform:rotate(-20deg);
			  }
			  100%{
			    -webkit-transform:rotate(20deg);
			    -moz-transform:rotate(20deg);
			    -o-transform:rotate(20deg);
			    transform:rotate(20deg);
			  }
		}
		
	</style>
</head>
<body style="padding-bottom:5rem">
	<section class="l_NewScroll" style="position:absolute; overflow:scroll; -webkit-overflow-scrolling: touch; top:0; left:0; bottom:0; right:0">
	<div class="w_banner">
		<img src="${ctx }/images/jm_banner2.jpg" alt="">
	</div>
	<div class="w_shop">
		<h2>天津市</h2>
		<dl>
			<dd>
				<h3>天津汇德行资产管理有限公司</h3>
				<p>地址：天津市和平区重庆道146号</p>
				<a href="tel:02259956367">
				<p style="display:-webkit-box;">
					<span class="animate">
						<img src="${ctx }/images/icon_phone.png" alt="">
					</span>电话：022-59956367</p></a>
				<p>营业时间：周一至周五,9:00-18:00</p>
			</dd>
			<dt><img src="${ctx }/images/jms_pic_1.jpg" alt=""></dt>
		</dl>
	</div>
	<div class="w_shop" style="border-bottom:1px solid #ccc;">
		<h2>山西省</h2>
		<dl>
			<dd>
				<h3>山西融聚同创网络科技有限公司</h3>
				<p>地址： 太原市高新技术开发区南中环街 529 号清控创新基地 C 座 23 层</p>
				<a href="tel:03512346970">
				<p style="display:-webkit-box;">
					<span class="animate">
						<img src="${ctx }/images/icon_phone.png" alt="">
					</span>电话：0351-2346970</p></a>
				<p>营业时间：周一至周六,9:00-18:00</p>
			</dd>
			<dt><img src="${ctx }/images/jms_pic_2.jpg" alt=""></dt>
		</dl>
	</div>
	<div class="w_shop" style="border-bottom:1px solid #ccc;">
		<h2>江苏省</h2>
		<dl>
			<dd>
				<h3>无锡道可道商务咨询有限公司</h3>
				<p>地址：无锡滨湖区蠡溪西苑38-4德道堂</p>
				<a href="tel:18601593126">
				<p style="display:-webkit-box;">
					<span class="animate">
						<img src="${ctx }/images/icon_phone.png" alt="">
					</span>电话：18601593126</p></a>
				<p>营业时间：周一至周日,9:00-18:00</p>
			</dd>
			<dt><img style="margin-bottom: 1rem;" src="${ctx }/images/jms_pic_3.jpg" alt=""></dt>
		</dl>
	</div>
	<div class="l_more">
		<div class="w_imgBox">
			<img src="${ctx }/images/bottom_text2.png" alt="">
		</div>
	</div>
	</section>
</body>


</html>