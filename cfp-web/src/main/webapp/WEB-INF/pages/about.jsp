<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.xt.cfp.core.constants.Constants" %>
<%@include file="./common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">  -->
	<title>关于我们 - 财富派</title>
	<%@include file="./common/common_js.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx }/css/index.css" />
	<link rel="stylesheet" type="text/css" href="${ctx }/css/about_style.css" />
	<link rel="stylesheet" type="text/css" href="${ctx }/css/about_scroll.css" />
	<style type="text/css">
		/*about 页面背景*/
		.section1{background:url(${ctx }/images/women/w1.jpg) no-repeat center top!important;}
		.section3{background:url(${ctx }/images/women/w2.png) no-repeat center center!important;}
		.section4{background:url(${ctx }/images/women/w3.jpg) no-repeat center center!important;}
		.header .dlogo{width:auto;}
		.header .dNav .uNav li{width:auto;}
		.header .dNav{width:auto;}
		.dlogo{width:auto;float:left;}
	</style>
	<script type="text/javascript" src="${ctx }/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/sectionscroll.js"></script><!--整屏滚动插件-->
	<script type="text/javascript" src="${ctx }/js/jquery.mousewheel.min.js"></script> <!--控制滚动条插件-->
	<script type="text/javascript" src="${ctx }/js/women3.js"></script><!--页面效果js-->
</head>

<body>
<ul id="nav">
		<li class="current li-1"><a href="#1">
        	<span><img src="${ctx }/images/women/about_us.png" alt=""></span>
            <em>公司简介<img src="${ctx }/images/women/about_us.png" alt=""></em></a>
        </li>
        <li class="li-2"><a href="#2">
        	<span><img src="${ctx }/images/women/base.png" alt=""></span>
            <em>基本信息<img src="${ctx }/images/women/base.png" alt=""></em></a>
        </li>
        <li class="li-3"><a href="#3">
        	<span><img src="${ctx }/images/women/team.png" alt=""></span>
            <em>团队管理<img src="${ctx }/images/women/team.png" alt=""></em></a>
        </li>
        <li class="li-4"><a href="#4">
        	<span><img src="${ctx }/images/women/partner.png" alt=""></span>
            <em>合作伙伴<img src="${ctx }/images/women/partner.png" alt=""></em></a>
        </li>
        
        <li class="li-5"><a href="#5">
        	<span><img src="${ctx }/images/women/join_us.png" alt=""></span>
            <em>联系我们<img src="${ctx }/images/women/join_us.png" alt=""></em></a>
        </li>
        <li class="li-6"><a href="#6">
        	<span><img src="${ctx }/images/women/contract_us.png" alt=""></span>
            <em>加入我们<img src="${ctx }/images/women/contract_us.png" alt=""></em></a>
    </li>
</ul>
<div class="section" id="1" name="1">
		<section id="section1" class="section section1">
			<div class="about_page1">
				<!--  about_page1 start -->
				<!--头部开始-->
	        	<%@include file="./common/headLine1.jsp"%>
	        	<!-- navindex start -->
				<customUI:headLine action="4"/>
				<!-- navindex end -->
				<%@include file="./login/login.jsp"%>
	            <div class="clear"></div>
				<!--头部结束-->
					<div class="container">
						<h1 class="h1-tit">公司介绍</h1>
						<p class="p-text">财富派是由北京汇聚融达网络科技有限公司推出的互联网金融服务平台，总部位于北京市朝阳区西店村86号</p>
						<p class="p-text">财富派采用先进的管理模式，具备完善的金融风险评估体系、以及资深政策法规的专业操作团队，帮助小微企业主、工薪阶层等不同群体建立信用体制</p>
						<p class="p-text">释放信用价值，获取信用资金。并始终以“安全”为核心，构筑可信赖的互联网金融服务平台。财富派平台已引入昆明御金资本的千万级A轮投资</p>
						<p class="p-text">北京汇聚融达网络科技有限公司创使人及管理团队从业年限超过10年，公司设立产品创新、风险控制、品牌推广、技术支持、运营结算等多个部门</p>
						<p class="p-text">利用互联网技术搭建起融资担保、商业保理、融资租赁、债权、基金、众筹等全方位的信息中介平台，基于大数据的分析，为企业及个人提供量身定制的金融服务</p>
						<div class="ul-women">
							<div><img src="${ctx }/images/women/w4.jpg" alt=""></div>
							<div><img src="${ctx }/images/women/w5.jpg" alt=""></div>
							<div><img src="${ctx }/images/women/w6.jpg" alt=""></div>
							<div><img src="${ctx }/images/women/w7.jpg" alt=""></div>
							<div><img src="${ctx }/images/women/w8.jpg" alt=""></div>
							<div><img src="${ctx }/images/women/w9.jpg" alt=""></div>
							<div><img src="${ctx }/images/women/w10.jpg" alt=""></div>
							<div><img src="${ctx }/images/women/w11.jpg" alt=""></div>
						</div>
					</div>
				<!--  about_page1 end -->
			</div>
		</section>
</div>
<div class="section" id="2" name="2">
        <section id="section1" class="section section1" style="">
            <div class="about_page2_new">
                <div class="container">
                    <h1 class="h1-tit" style="color:white;">公司基本信息</h1>
                    <div class="text_main">
                        <p><i>◆</i>全称：北京汇聚融达网络科技有限公司； 简称：汇聚融达。</p>
                        <p><i>◆</i>注册资本：2000万元。</p>
                        <p><i>◆</i>实缴资本：2000万元。</p>
                        <p><i>◆</i>注册地址：北京市朝阳区光华路9号楼10层1012室。</p>
                        <p><i>◆</i>成立时间：2014年7月30日。</p>
                        <p><i>◆</i>经营范围：技术开发、技术转让、技术咨询、技术服务；经济贸易咨询；投资咨询；投资管理；资产管理；会议服务；承办展览展示活动；数据处理（PUE值在1.5以下的云计算数据中心除外）；市场调查；计算机系统服务；接受金融机构委托从事金融信息技术外包服务、接受金融机构委托从事金融业务流程外包服务、接受金融机构委托从事金融知识流程外包服务；互联网信息服务。</p>
                        <p><i>◆</i>从业机构及其各分支机构经营场所：北京市朝阳区西店村86号，无分支机构。</p>
                        <p><i>◆</i>资金存管情况：资金存管项目正与一家全国性商业银行洽谈中，目前使用连连支付作为客户资金的第三方存管机构。</p>
                        <p><i>◆</i>与保险机构、担保机构等各类第三方：无合作的保险机构和担保机构。</p>
                        <p>网上支付与第三方支付公司连连支付合作；在中信银行开立平台风险准备金账户。</p>
                    </div>
                </div>
            </div>
        </section>
</div>
<div class="section" id="3" name="3">
	<section id="section1" class="section section2">
		<div class="about_page2">
			<!--  about_page2 start -->
			<div class="container clear">
        		<h1 class="h1-tit">管理团队</h1>
                <div class="jianjie" id="jianjie">
                	<div class="jianjie-lay" style="display:block;">
                    	<h2>王加武</h2>
                        <h6>创始人兼董事</h6>
                        <span></span>
                        <p>1994年王加武先生参加银行工作，先后在中国农业银行、渣打银行、东亚银行任职累计近20年。在各级领导岗位上工作超过15年。</p>
                        <p>通过贷款业务的调查、审查、审批岗的历练，积累了丰富的信贷和风险控制工作知识和经验。</p>
                    </div>
                    <div class="jianjie-lay">
                        <h2>倪鹏钧</h2>
                        <h6>CEO</h6>
                        <span></span>
                        <p>倪鹏钧先生毕业于厦门大学，并获得北京大学经济管理学院EMBA学位。拥有丰富的高层管理资历，熟悉掌握现代化企业经营、管理与市场运作。</p>
                        <p>历任招商证券、西南证券资深投资顾问，晋中市志合信投资咨询有限责任公司创始人。</p>
                    </div>
                    <!-- <div class="jianjie-lay">
                    	<h2>石晗</h2>
                        <h6>联合创始人兼副总经理</h6>
                        <span></span>
                        <p>1982年出生，曾任职于九城关贸、用友软件；2011年任职宜信互联网事业部，负责宜人贷、宜车贷运营，创新推出宜定盈等产品，在互联网金融行业具有丰富的经验和优异的成绩。</p>
                    </div> -->
                    <div class="jianjie-lay">
                    	<h2>张征</h2>
                        <h6>联合创始人兼首席风控官</h6>
                        <span></span>
                        <p>毕业于英国拉夫堡大学荣获银行与金融经济学硕士学位，随后在渣打银行服务九年，曾先后负责北方区个人及中小企业信贷风险部，2006年实际操作了中国第一批企业无抵押类信用贷款，及2007年中国第一批个人无抵押信用贷款，操作及反欺诈的实战经验,管理资产超过五十亿。</p>
                    </div>
                    <div class="jianjie-lay">
                    	<h2>肖志通</h2>
                        <h6>联合创始人兼市场营销总监</h6>
                        <span></span>
                        <p>肖志通先生曾在渣打银行服务5年以上，组建过北京地区的高端客户拓展部门。拥有丰富的金融市场开拓、销售部门组建经验和突出的风险控制能力,任职北方销售导师期间管理辅导北京、天津、青岛、重庆、成都等城市50多个团队。</p>
                        
                    </div>
                    <div class="jianjie-lay">
                    	<h2>卢晓东</h2>
                        <h6>联合创始人兼企业发展总监</h6>
                        <span></span>
                        <p>对外经贸大学经济学学士和英国南安普顿大学金融学硕士，拥有13年银行从业经验，先后任职于中信银行总行、中信控股和渣打银行，熟悉对公业务和电子银行业务，曾负责全行对公客户经理的业务培训。</p>
                    </div>
                    <div class="jianjie-lay">
                    	<h2>韩鹏</h2>
                        <h6>设计总监</h6>
                        <span></span>
                        <p>拥有8年视觉及UI设计经验，曾就职于凡客诚品，中青旅，海航集团等公司，主导并完成诸多设计类工作。
对互联网及移动端产品的视觉设计风格有着独到的见解并拥有很强的把控力，善于通过设计来更好地完善产品，提升用户体验与使用效率。</p>
                    </div>
                    <div class="jianjie-lay">
                    	<h2>于雷</h2>
                        <h6>技术总监</h6>
                        <span></span>
                        <p>2005年参加工作，曾先后就职于中国数码、宜信、证大向上等公司，从初级开发到技术总监，经历了多个成功项目的从无到有，从小到大，从大到强。最近几年一直在互联网金融领域深耕。</p>
                    </div>
                    <div class="jianjie-lay">
                    	<h2>王少雄</h2>
                        <h6>产品总监</h6>
                        <span></span>
                        <p>自2007年加入工作后，先后效力于中软，新浪乐居，宜信，和信贷，负责产品设计及团队管理工作。对互联网金融产品，移动端产品，门户及移动平台广告形式等领域，有深入的研究和丰富的工作经验。善于把握产品方向及细节，提升用户体验。</p>
                    </div>
                    <div class="jianjie-lay">
                    	<h2>刘乐</h2>
                        <h6>质量总监</h6>
                        <span></span>
                        <p>2005年毕业后，先后就职于microsoft,alibaba旗下中国雅虎，淘宝网，宜信等公司，专注于互联网，移动互联网产品的质量保障工作。在10年的工作经历中，参与，负责了多个众所周知的大型互联网产品，并积累了丰富的产品质量管理，网络安全等经验。
                        </p>
                    </div>
                </div>
                <div class="pic-img" id="picImg">
                	<div class="pic-img-lay" style="display:block;"><img src="${ctx }/images/women/w12.jpg" alt=""></div>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/npj.jpg" alt=""></div>
                    <%-- <div class="pic-img-lay"><img src="${ctx }/images/women/sh.jpg" alt=""></div> --%>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/zz.jpg" alt=""></div>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/xzt.jpg" alt=""></div>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/lxd.jpg" alt=""></div>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/hp.jpg" alt=""></div>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/yl.jpg" alt=""></div>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/wsx.jpg" alt=""></div>
                    <div class="pic-img-lay"><img src="${ctx }/images/women/ll.jpg" alt=""></div>
                </div>
                <div class="pic-list" id="picList">
                	<div class="pic-list-img">
                    <em class="cur"><img src="${ctx }/images/women/w13.jpg" alt=""></em>
                    </div>
                    <div class="pic-list-img">
                    <em><img src="${ctx }/images/women/wnpj.jpg" alt="" style="margin-left:-3px;"></em>
                    </div>
                    <%-- <div class="pic-list-img">
                    <em><img src="${ctx }/images/women/w14.jpg" alt="" style="margin-left:-3px;"></em>
                    </div> --%>
                    <div class="pic-list-img">
                    <em><img src="${ctx }/images/women/w15.jpg" alt="" style="margin-left:-5px;"></em>
                    </div>
                    <div class="pic-list-img">
                    <em style="background:#eae1e4;">
                    <img src="${ctx }/images/women/w16.jpg" alt="" style="margin-left:-2px; margin-top:10px;"></em>
                    </div>
                    <div class="pic-list-img"><em><img src="${ctx }/images/women/w17.jpg" alt=""></em></div>
                    <div class="pic-list-img">
                    <em><img src="${ctx }/images/women/w19.jpg" alt="" style="margin-left:-10px;"></em>
                    </div>
                    <div class="pic-list-img">
                    <em><img src="${ctx }/images/women/w21.jpg" alt="" style="margin-left:-10px;"></em>
                    </div>
                    <div class="pic-list-img">
                    <em><img src="${ctx }/images/women/w22.jpg" alt="" style="margin-left:-4px;"></em>
                    </div>
                    <div class="pic-list-img">
                    <em>
                    <img src="${ctx }/images/women/w23.jpg" alt="" style="margin-left:-6px; margin-bottom:-14px;"></em>
                    </div>
                </div>
            </div>
			<!--  about_page2 end -->
		</div>	
	</section>
</div>
	
<div class="section" id="4" name="4">
	<section id="section1" class="section section3">
		<div class="about_page3">
		<!--  about_page3 start -->
		<div class="container">
           	<h1 class="h1-tit" style="color:white;">合作伙伴</h1>
           	<div class="idx-focus" id="j_idx_focus">
				<ul rel="xtaberTabs" class="xtaber-tabs">
					<li rel="xtaberTabItem" class="currents"><a href="http://www.cfca.com.cn/" target="_blank"><img src="${ctx }/images/women/us_logo1.jpg"></a></li>
					<li rel="xtaberTabItem"><a href="http://www.experian.com.cn/" target="_blank"><img src="${ctx }/images/women/us_logo2.jpg"></a></li>
					<li rel="xtaberTabItem"><a href="https://www.fraudmetrix.cn/" target="_blank"><img src="${ctx }/images/women/us_logo3.jpg"></a></li>
					<li rel="xtaberTabItem"><a href="http://www.itrus.com.cn/" target="_blank"><img src="${ctx }/images/women/us_logo4.jpg"></a></li>
					<li rel="xtaberTabItem"><a href="https://www.juxinli.com/" target="_blank"><img src="${ctx }/images/women/us_logo5.jpg"></a></li>
					<%-- <li rel="xtaberTabItem"><a href="http://www.yeepay.com/" target="_blank"><img src="${ctx }/images/women/us_logo6.jpg"></a></li> --%>
					<li rel="xtaberTabItem"><a href="https://www.fuiou.com/" target="_blank"><img src="${ctx }/images/women/us_logo7.jpg"></a></li>
					<li rel="xtaberTabItem"><a href="http://www.longanlaw.com/" target="_blank"><img src="${ctx }/images/women/us_logo8.jpg"></a></li>
					<li rel="xtaberTabItem"><a href="http://www.pycredit.cn/" target="_blank"><img src="${ctx }/images/women/us_logo9.jpg"></a></li>
					<li rel="xtaberTabItem"><a href="http://www.lianlianpay.com/" target="_blank"><img src="${ctx }/images/women/us_logo10.jpg"></a></li>
				</ul>
				<div class="idx-foc-tmp">
					<ul class="focus-pic" rel="xtaberItems" style="height:500px;">
						<li class="xtaber-item">
								<p>中国金融认证中心</p>
								<p>（China Financial Certification Authority，简称CFCA）</p>
								<p>是经中国人民银行和国家信息安全管理机构批准成立的国家级权威安全认证机构，是国家重要的金融信息安全基础设施之一。                                                      在《中华人民共和国电子签名法》颁布后，CFCA成为首批获得电子认证服务许可的电子认证服务机构。</p>
						</li>
						
						<li class="xtaber-item">
								<p>益博睿</p>
								<p>益博睿是全球领先的信息服务公司，向世界各地的客户提供数据和分析工具。拥有20年以上在亚太市场经营的经验。尖端技术、专业知识与对客户的重视，是亚太地区数百家企业选择他们成为战略合作伙伴的关键。</p>
						</li>
						<li class="xtaber-item">
								<p>同盾科技</p>
								<p>为互联网金融行业提供风险控制与反欺诈的实时数据分析服务。已获得顶级VC的A轮及A+轮投资，且获得了创业邦年度创新成长百强企业、清科中国最具投资价值企业50强等殊荣。</p>
						</li>
						<li class="xtaber-item">
								<p>天威诚信</p>
								<p>是信息产业部批准的第一家全国性电子认证机构。是虚拟网络空间身份认证、数据电文认证、证据认证及其应用的专业服务提供商。</p>
						</li>
						<li class="xtaber-item">
								<p>聚信立</p>
								<p>拥有国内第一批从事风险评分卡开发的行业精英。提供的产品和服务全方位的覆盖消费者和小微企业的信用评估、风险管理、市场营销、运营管理等领域，通过数据平台、决策产品和服务，跨越式地提升金融机构的核心竞争力。</p>
						</li>
						<!-- <li class="xtaber-item">
								<p>易宝支付</p>
								<p>易宝支付是获得首批央行颁发支付牌照的支付公司，是北京市网贷行业协会的创始会员，并成为网贷协会的监事长单位。领跑电子支付、移动互联和互联网金融。</p>
						</li> -->
						<li class="xtaber-item">
								<p>富友支付</p>
								<p>富友支付是一家大型综合性金融支付服务集团公司，是“上海市网络信贷服务业企业联盟”成员单位，获得中国人民银行颁发的银行卡收单和互联网支付牌照，同时获得人民银行颁发的预付卡发行与受理牌照的第三方支付公司。</p>
						</li>
						<li class="xtaber-item">
								<p>隆安律师事务所</p>
								<p>隆安律师事务所是中国最早的合伙制律师事务所之一，率先获得ISO-9002国际标准质量体系认证，是一家国际性的综合律师事务所。</p>
						</li>
						<li class="xtaber-item">
								<p>鹏元征信</p>
								<p>鹏元征信是八家首批获个人征信牌照机构，是国内唯一通过高新技术企业认定、双软企业认定、国家信息安全认证及国际软件成熟度（CMMI）认证的专业征信机构。</p>
						</li>
						<li class="xtaber-item">
								<p>连连支付</p>
								<p>连连支付2011年8月获得人民银行颁发的支付许可证，成为浙江省第二家获得该业务许可的企业。业务涵盖全国范围的互联网支付、移动手机支付业务。</p>
						</li>
					</ul>
				</div>
				<a href="javascript:;" class="btn-prev"></a>
				<a href="javascript:;" class="btn-next"></a>
			</div>
			<script type="text/javascript" src="${ctx }/js/jquery.taber.js"></script>
			<script type="text/javascript">
			$(function(){
				/* 合作伙伴js */
				if(isNeeded('#j_idx_focus')){
					var obj = $('#j_idx_focus');
					$.fn.xTaber({
						content: obj,
						tab: obj,
						auto: true,
						style: 'left',
						prev: obj.find('.btn-prev'),
						next: obj.find('.btn-next')
					});
				}
			});
			</script>
        </div>
		<!--  about_page3 end -->
		</div>
	</section>
</div>

<div class="section" id="5" name="5">
	<section id="section1" class="section section4">
		<div class="about_page4" style="background:url(${ctx }/images/women/w3new1.jpg) no-repeat center center;">
		<!--  about_page4 start -->
		<div class="container clear">
            	<div id="divHei">
                	<h1 class="h1-tit" style="padding-top:70px;">联系我们</h1>
                </div>
                <dl class="women-dl">
                	<dt><img src="${ctx }/images/women/w35.jpg" alt=""></dt>
                    <dd>
                    	<p>客服热线</p>
                        <b>400-061-8080</b>
                        <p>工作时间：工作日：9:00-18:00  周六：9:00-17:00</p>
                    </dd>
                </dl>
                <dl class="women-dl">
                	<dt><img src="${ctx }/images/women/w36.jpg" alt=""></dt>
                    <dd>
                    	<p>企业邮箱</p>
                        <b><a href="###">myservice@mayitz.com</a></b>
                    </dd>
                </dl>
                <dl class="women-dl">
                	<dt><img src="${ctx }/images/women/w37.jpg" alt=""></dt>
                    <dd>
                    	<p>联系地址</p>
                        <b>北京市朝阳区西店村86号</b>
                    </dd>
                </dl>
            </div>	
		<!--  about_page4 end -->
		</div>
	</section>
</div>

<div class="section" id="6" name="6">
	<section id="section1" class="section section5" name="section5">
		<!--  about_page5 start -->	
		<div class="container clear">
            	<h1 class="h1-tit">加入我们</h1>
                <div class="jr-l">
                	<div><img src="${ctx }/images/women/w34.jpg" alt=""></div>
                    <div class="jr-l-b clear" id="jr-l-b">
                    	<span class="active">web前端开发工程师</span>
                        <span>测试工程师</span>
                        <span>JAVA开发工程师</span>
                        <span>SEO</span>
                        <span>产品经理</span>
                        <span>视觉设计师</span>
                        <span>设计经理</span>
                        <span>设计师</span>
                        <span>HTML5前端开发工程师(移动端)</span>
                        <span>JAVA后端开发工程师(移动端)</span>
                        <span>移动测试工程师(移动端)</span>
                        <span>文案编辑</span>
                    </div>
                </div>
                <div class="jr-r">
                	<div class="jr-wrap">
                    	<div class="jr-em">
                        	<em id="jr-left" style="border-right:1px solid #ccc;">&gt;</em>
                            <em id="jr-rig">&lt;</em>
                        </div>
                    	<div class="jr-nr" id="jr">
                        	<div class="jr-item">
                            	<div class="jr-t">
                                	<b>web前端开发工程师</b>
                                </div>
                                <h5>岗位职责</h5>
                                <p>1.根据要求，利用HTML5/CSS3/JavaScript等Web技术进行WEB交互界面开发；</p>
                                <p>2.能够快速解决主流浏览器的兼容性问题；</p>
                                <p>3.对用户体验，交互设计、前端页面代码结构及前端交互性能有较深入的理解；</p>
                                <p>4.参与讨论制订产品web前端框架结构，实现Web前端用户界面编码，并撰写相应的技术开发文档；</p>
                                <p>5.负责将设计师提供的设计图转化成静态页面。</p>
                                <h5>任职要求</h5>
                                <p>1.熟悉WEB开发流程, 有和后台代码工程师合作开发的经验；</p>
                                <p>2.能利用JavaScript完成复杂的页面UI交互，能够手写JS脚本；</p>
                                <p>3.精通DIV/CSS制作，代码写作习惯良好，代码与CSS完全分离，并且符合W3C标准；</p>
                                <p>4.精通jquery, backbone, node.js等js类库，掌握JavaScript语言核心技术DOM、BOM、Ajax、JSON等；</p>
                                <p>5.一年以上前端开发工程师经验，有iPhone、iPad、Android等智能手机和平板开发经验优先考虑；</p>
                                
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>高级安全测试工程师</b>
                                </div>
                                <h5>岗位职责</h5>
                                <p>1.负责 web应用的安全测试，引导和辅助开发人员修复安全问题；</p>
                                <p>2.负责网站的安全测试方案、安全测试用例编写和安全测试执行工作；</p>
                                <p>3.定期对公司网络、服务器、网站进行渗透测试；</p>
                                <p>4.负责web安全测试技术的研究和培训安全测试工作。</p>
                                <h5>任职资格</h5>
                                <p>1.具有丰富的软件测试经验，熟悉各种测试方法、技术、工具；能独立分析和处理网络系统、操作系统、数据库系统的安全问题；</p>
                                <p>2.1-3年渗透测试、漏洞利用、防入侵、系统应急处置等相关工作经验，熟练掌握相关渗透、漏扫等工具；
                                </p>
                                <p>3.熟悉安全欺骗和攻防工具，如：ARP/IP/DNS/SQL注入和COOKIE的欺骗攻击，WEB常见攻击方式；</p>
                                <p>4.根据产品需求，从技术的角度进行评估，并给出建议，推动用户体验的优化。</p>
                                <p>5.熟悉常见脚本语言，能够进行WEB渗透测试，恶意代码测试和分析；</p>
                                <p>6.执行针对网站的渗透测试，定期执行风险评估，给出风险预警和应对方案；</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>JAVA高级开发工程师</b>
                                </div>
                                <h5>岗位职责</h5>
                                <p>1.负责公司网站、移动服务、管理平台等JAVA方向BS类产品的日常设计、开发和维护工作；</p>
                                <p>2.负责核心交易平台的设计、开发和维护工作，负责公司各JAVA方向BS类产品的重构工作；</p>
                                <p>3.参与技术难点攻关，参与技术选型及设计评审，参与代码Review；</p>
                                <p>4.带领小队完成小型项目或任务，协助初中级程序员问题排查；</p>
                                <h5>任职要求</h5>
                                <p>1.具备4年或以上的JAVA方向BS系统的开发经验；</p>
                                <p>2.具备扎实的JAVA基础技术功底；</p>
                                <p>3.熟悉常用JAVA开发框架、缓存、开源组件，并能灵活运用；</p>
                                <p>4.熟练使用Mysql或Oracle等数据库产品，并可书写SQL完成复杂业务查询；</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>SEO</b>
                                </div>
                                <h5>岗位职责</h5>
                                <p>1.制定网站SEO优化方案，尤其对新站有一定的操作经验。</p>
                                <p>2.评估、分析、拓展网站所属行业和行业相关关键词，满足用户的搜索需求。</p>
                                <p>3.了解多种外链建设的方式，有良好链接资源的优先；</p>
                                <p>4.对于网站代码有一定了解，有一定html、asp、php基础；</p>
                                <p>5.熟悉GA、百度统计工具，关注爬虫日志，能从日志中发现网站对应的SEO问题；</p>
                                <h5>任职要求</h5>
                                <p>1.熟悉Google、Baidu、搜搜、雅虎、有道、必应等各大搜索引擎的原理和特点。</p>
                                <p>2.熟悉目前主要搜索引擎的搜索排名原理，熟悉影响关键词排名的主要因素；</p>
                                <p>3.5以上SEO相关工作经验</p>
                                <p>4.能针对关键词优化进行合理内容设计，防止和避免使用作弊</p>
                                <p>5.熟悉金融行业的优先，独立做过站点的优先，熟悉移动SEO优化的优先；</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>产品经理</b>
                                </div>
                                <h5>岗位职责</h5>
                                <p>1.针对公司业务需求，进行用户需求分析、优化、明确用户需求；</p>
                                <p>2.编写公司业务系统需求调研报告、需求设计文档、需求规格说明书、需求开发计划等；</p>
                                <p>3.与开发人员沟通需求的理解和实现，在开发过程中进行需求验证，确保产品开发方向正确，根据业务需求进行原型开发（Demo设计）；</p>
                                <p>4.收集和跟踪已上线系统的用户反馈，在分析和论证后，制定优化改造计划。</p>
                                <h5>任职要求</h5>
                                <p>1.有3年方案设计、编写工作经验，业务系统需求工程师两年以上经验；</p>
                                <p>2.良好的业务理解、沟通能力及文档撰写能力，良好的跨部门协调和工作开展能力；</p>
                                <p>3.了解计算机专业的基础技术，能顺畅的与开发人员进行沟通，有CRM等开发工作经验者优先。</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>视觉设计师</b>
                                </div>
                                <h5>工作内容：</h5>
                                <p>1.网站视觉设计类相关项目：如营销专题、活动banner</p>
                                <p>2.优化网站的视觉设计风格，提升用户体验</p>
                                <h5>技能要求：</h5>
                                <p>1.精通PS/AI等设计软件，简单了解前端制作知识</p>
                                <p>2.喜欢设计，热衷于用户体验的提升，熟悉互联网产品的设计开发流程</p>
                                <p>3.2年以上的设计类工作经验</p>
                                <h5>加分项</h5>
                                <p>拥有金融理财或影视娱乐行业的设计工作经验</p>
                                <p>关注设计趋势，拥有先进的设计理念</p>
                                <p>热爱生活，喜欢尝鲜，能聊爱笑的乐天派</p>
                                <p>+简历请附您的设计作品或下载链接</p>
                                <p>+请附上您的期望薪酬</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>设计经理</b>
                                </div>
                                <h5>工作内容：</h5>
                                <p>1.参与重点项目的规划构思和创意过程，并负责相关设计工作：如频道UI设计、营销专题设计</p>
                                <p>2.主导网站设计风格的制定与执行</p>
                                <p>3.优化设计部工作流程及设计规范</p>
                                <p>4.分享经验，带领团队成员共同成长</p>
                                <h5>技能要求：</h5>
                                <p>1.精通PS/AI等设计软件，了解前端制作相关知识</p>
                                <p>2.优秀的设计表现能力，参与过各类设计项目</p>
                                <p>3.关注用户体验细节，对流行趋势具有敏锐的洞察力</p>
                                <p>4.3年以上UI/视觉设计工作经验，1年以上团队管理经验</p>
                                <h5>加分项</h5>
                                <p>拥有丰富的团队管理经验，善于激发团队成员的潜力，乐于分享自己的知识</p>
                                <p>能够主导相关项目的设计方向，给予有效的设计策略支持</p>
                                <p>热爱生活，喜欢尝鲜，能聊爱笑的乐天派</p>
                                <p>+简历请附您的设计作品或下载链接</p>
                                <p>+请附上您的期望薪酬</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>UI设计师</b>
                                </div>
                                <h5>工作内容：</h5>
                                <p>1.APP项目的UI设计工作</p>
                                <p>2.网站UI设计相关项目：如频道建设、改版</p>
                                <p>3.部分项目的视觉设计工作</p>
                                <h5>技能要求：</h5>
                                <p>1.精通PS/AI等设计软件，了解前端制作相关知识</p>
                                <p>2.能与前端工程师密切配合，共同完成网站UI建设工作</p>
                                <p>3.热衷于用户体验的提升，熟悉互联网产品的设计开发流程</p>
                                <p>4.2年以上UI设计工作经验极其注重细节体验，为节省用户的一次操作所付出的艰辛，你都乐在其中；</p>
                                <h5>加分项</h5>
                                <p>拥有金融理财或影视娱乐行业的设计工作经验</p>
                                <p>关注设计趋势，拥有先进的设计理念</p>
                                <p>热爱生活，喜欢尝鲜，善于沟通的乐天派</p>
                                <p>+简历请附您的设计作品或下载链接</p>
                                <p>+请附上您的期望薪酬</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>HTML5前端开发工程师(移动端)</b>
                                </div>
                                <h5>岗位要求：</h5>
                                <p>1.3以上前端开发经验，1年以上HTML5前端开发经验；</p>
                                <p>2.精通各类Web前端开发技术，包括javascript、Ajax、CSS等web开发领域相关技术，有前端框架设计经验；</p>
                                <p>3.精通HTTP协议；熟悉与服务器端API的交互，熟悉Json，熟练运用HTML5特性构建移动端的WebApp，了解Html5的离线存储机制，能应用HTML5开发 Mobile APP 界面和体验效果的应用；</p>
                                <p>4.有微信公众平台应用开发经验者优先。</p>
                                
                                <h5>工作职责</h5>
                                <p>1.负责公司触屏版本相关产品的HTML5页面开发；</p>
                                <p>2.能够独立完成详细设计及编码、进行代码Review；</p>
                                <p>3.负责微信接口设计、开发和维护；负责微信公众平台所需要的接口和数据管理，api数据接口开发工作；
                                </p>
                                <p>4.根据产品需求，从技术的角度进行评估，并给出建议，推动用户体验的优化。</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>JAVA后端开发工程师(移动端)</b>
                                </div>
                                <h5>岗位要求：</h5>
                                <p>1.3以上前端开发经验，1年以上HTML5前端开发经验；</p>
                                <p>2.精通各类Web前端开发技术，包括javascript、Ajax、CSS等web开发领域相关技术，有前端框架设计经验；</p>
                                <p>3.精通HTTP协议；熟悉与服务器端API的交互，熟悉Json，熟练运用HTML5特性构建移动端的WebApp，了解Html5的离线存储机制，能应用HTML5开发 Mobile APP 界面和体验效果的应用；</p>
                                <p>4.有微信公众平台应用开发经验者优先。</p>
                                
                                <h5>工作职责</h5>
                                <p>1.负责公司触屏版本相关产品的HTML5页面开发；</p>
                                <p>2.能够独立完成详细设计及编码、进行代码Review；</p>
                                <p>3.负责微信接口设计、开发和维护；负责微信公众平台所需要的接口和数据管理，api数据接口开发工作；
                                </p>
                                <p>4.根据产品需求，从技术的角度进行评估，并给出建议，推动用户体验的优化。</p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>移动测试工程师 (移动端)</b>
                                </div>
                                <h5>岗位要求：</h5>
                                <p>1.两年以上软件行业测试经验；熟悉规范测试流程；</p>
                                <p>2.精通测试方案和用例设计，熟悉移动端相关测试方法，</p>
                                <p>3.熟练使用linux操作系统，熟悉oracle数据库及sql脚本语言；</p>
                                <p>4.具备与开发人员沟通并推进问题解决的能力，能够迅速定位bug、跟踪并编写测试报告；</p>
                                <p>5.熟练使用主流缺陷管理工具和测试工具；</p>
                                <h5>工作职责</h5>
                                <p>1.根据移动端产品设计制定相应的测试计划，并全程跟踪计划的实施情况，根据需要调整计划；</p>
                                <p>2.根据产品需求，设计测试用例并组织实施测试，根据测试结果编写测试报告；</p>
                                <p>3.执行软件产品的性能测试和分析，预测系统性能瓶颈， 风险和安全隐患；
                                </p>
                            </div>
                            <div class="jr-item">
                            	<div class="jr-t">
                                	<b>文案编辑</b>
                                </div>
                                <h5>岗位要求：</h5>
                                <p>1.负责公司期刊的策划、组稿、稿件撰写编辑以及排版制作等工作；</p>
                                <p>2.负责公司微信公众号、微博的方向制定、内容更新、专题规划、日常网络管理等；</p>
                                <p>3.负责新闻、撰写各类稿件，能够熟悉运用PS，能够对金融行业内新闻进行伪原创编辑；</p>
                                <p>4.熟悉互联网金融行业优先考虑。</p>
                                <h5>工作职责</h5>
                                <p>1.大学本科及以上，新闻、中文、等相关专业；</p>
                                <p>2.2年及以上工作经验，具有大型金融企业总部工作经验者优先；</p>
                                <p>3.具有2年以上刊物编辑 /企划案设计等经验者优先；</p>
                                <p>4.备有较强的企划能力；性格开朗、工作热情、积极主动、责任心强；沟通协调能力、组织能力、文字及语言表达能力强；有良好的逻辑分析能力，能快速理解公司的业务。</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>	
		<!--  about_page5 end -->
	</section>
</div>
<div style="clear:both;"></div>
<div class="section">
	<%@include file="common/footLine1.jsp"%>
</div>
<script type="text/javascript" src="${ctx }/js/jquery.nav.js"></script>
<script type="text/javascript">
$(function(){
	$('#nav').onePageNav();
});
</script>
</body>
</html>
