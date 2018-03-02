// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"]
	}
})
require(['jquery',"main"],function($,main) {
	var pageNo=1,pageSize=20,totalPage,inp;
	$(function(){
		//初始化数据
		pushData(0);
		
		
		
		//tab切换
		$(".headNav").on("click","li",function(){
			var num = $(this).index();
			$(".headNav li").eq(num).addClass("cur").siblings().removeClass("cur");
			$(".couponList>div").eq($(".headNav>li").index(this)).show().siblings("div").hide();
			inp =$(".headNav>li").index(this);
			pageNo=1;
			seachHtml(pageNo,pageSize,inp);// -1全部
			
		})
		//财富券选中状态
		$(".page .cfqBj").on("click",function(){
			$(this).addClass("cfqyxBj").siblings().removeClass("cfqyxBj");
		})
		//提现券选中状态
		$(".page .txqBj").on("click",function(){
			$(this).addClass("txqyxBj").siblings().removeClass("txqyxBj");
		})
		//加息券选中状态
		$(".page .jxqBj").on("click",function(){
			$(this).addClass("jxqyxBj").siblings().removeClass("jxqyxBj");
		})
		//滚动加载
		$(document).on("scroll",function() {
			if(main.scrollLoading("s_content","l_NewScroll")) {
				// 此处写加载列表的方法
				if(pageNo<=totalPage){
					pageNo++;
					seachHtml(pageNo,pageSize,inp);
				}
			}
			
		})
		
		
	//加载数据 王亚东
		function pushData(ind){
			seachHtml(pageNo,pageSize,ind);
		}

		function seachHtml(page,rows,inx){
			var div="";
			var state="-1";
			if(inx==1){
				state="0";
			}else if(inx==2){
				state="1";
			}
			$.ajax({
				url:rootPath+"/person/voucherList",
				type:"post",
				data: {
					"pageSize": rows,
					"pageNo": page,
					"state":state,
					"couponType":"-1"
				},
				success: function (data) {
					var d_rows = data.rows;
					totalPage = data.totalPage;
					if(data.rows == 0 && page==1){
						div="<dl class='noQuan'>"+
			    				"<dd>"+
			    					"<img src='"+rootPath+"/images/userCenter/noquan.png' />"+
			    				"</dd>"+
			    				"<dt>暂无优惠券</dt>"+
			    			"</dl>";
					}else{
						for(var i=0;i<d_rows.length;i++){
							var _data = d_rows[i];
							console.log(_data);
							var voucher="";//优惠卷名称
							var voucherCss="couponBox"; //优惠卷样式
							var voucherCss1=""; //优惠卷样式
							var voucherText="条件："+(_data.conditionAmountStr==undefined||_data.conditionAmountStr==null||_data.conditionAmountStr==""||_data.conditionAmountStr=="无限制"?"——":_data.conditionAmountStr); //优惠卷文本
							var voucherEnd=""; //优惠卷是元还是百分号
							var voucherFlag=""; //优惠卷是元还是百分号
							
							var deadline="有效期限：长期有效";
							if(_data.endDate!=null){
								deadline='有效期 '+getDateStr(new Date(_data.endDate));
							}
							if(_data.condition!=null){//使用条件
								voucherText+="";
							}
							if(_data.voucherCouponType=='2'){//加息券  voucherEnd不做处理 
								 if(_data.status=='2'||_data.status=='1'){
										voucherCss +="  ysyBj";
										voucherCss1="  color_hui";
									}else if(_data.status=='3'){
										voucherCss +="  ygqBj"
										voucherCss1="  color_hui";
									}else{
										voucherCss+=" jxqBj";
										voucherCss1="  color_yellow";
									}
								voucher="加息券";
								voucherEnd+="%";
								
								voucherText+=_data.conditionStr==undefined||_data.conditionStr==null||_data.conditionStr==""?" ":";"+_data.conditionStr;
							}else if(_data.voucherCouponType=='3'){	//提现券
								voucher+="提现券";
								 if(_data.status=='2'||_data.status=='1'){
									voucherCss +="  ysyBj";
									voucherCss1="  color_hui";
								}else if(_data.status=='3'){
									voucherCss +="  ygqBj";
									voucherCss1="  color_hui";
								}else{
									voucherCss +="  txqBj"
									voucherCss1="  color_red";
								}
								voucherFlag+="￥";
								voucherEnd+="元";
								voucherText="条件：无";
							}else{	//财富劵
								 if(_data.status=='2'||_data.status=='1'){
										voucherCss +="  ysyBj"
										voucherCss1="  color_hui";
									}else if(_data.status=='3'){
										voucherCss +="  ygqBj"
										voucherCss1="  color_hui";
									}else{
										voucherCss +="  cfqBj"
										voucherCss1="  color_red";
									}
								voucher+="财富劵"; 
								voucherEnd+="元";
								voucherFlag+="￥";

							}
							/***************************************************
							 * ******UNUSED("0","未使用"), USING("1","使用中"),
							 * USEUP("2","使用完"), TIMEOUT("3","已过期"),
							 **************************************************/
							
							/****************文本主体*******************/
							 div+= "<div class= '"+voucherCss+"'>";    
						   	 div+= "<div class='couponCon'>";
						   	 div+= "<p class='couponText1'>";
						   	 /*div+= "<i class='coupon_i "+voucherCss1+" '>"+voucherFlag+"</i>";*/
						   	 div+="<span class='"+voucherCss1+"'>"+voucherFlag+_data.amountStr.substr(0,_data.amountStr.length-1)+"</span>";
						   	 div+="<i class='coupon_pre "+voucherCss1+"'>"+voucherEnd+"</i>";
						   	 div+="<font class='"+voucherCss1+"'>"+voucher+"</font>";
						   	 div+="</p>";
						   	 div+="<p class='couponText2 color_666'>"+voucherText+"</p>";
						   	 div+="<p class='couponText3 color_333'>有效期前至："+deadline+"</p>";
						 	 div+="</div>";
						 	 div+="</div>";
						}
//						div+="	<p class='l_lastTip'>向下滑动加载更多</p>";
					}
				/**
				 * **************根据切换不同的tab 选择需要展示在的tab下
				 * 默认是全部 1是可用 2是不可用 0,1,2分别是tab键的index
				 * 位置******************
				 */
					if(inx==1){//可用
						$("#pageContent0").append(div);
						$("#pageContent1").html("");
						$("#pageContent").html("");
					}else if(inx==2){// 不可用
						$("#pageContent1").append(div);
						$("#pageContent0").html("");
						$("#pageContent").html("");
					}else{//默认是全部
						$("#pageContent").append(div);
						$("#pageContent0").html("");
						$("#pageContent1").html("");
					}
				}
			});	
		
		
		
		
		}
		
	})
})
