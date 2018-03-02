// JavaScript Document
 $(document).ready(function() {
	// ${jsonProduct}
	 
	 
	 var flag = $("#flagCustomer").val();
	 if(flag!=null&&flag!=""&&flag=="new"){
		  $("#col-md-hidden").find("img").attr("src","../images/Novice.jpg");
		  searchHtmlSpeical(1,10);
	 }else{
		 searchHtml(1,10); 
	 }
	 
	 searchHtmlRight(1,10);
		//searchHtmlSpeical(1,10);
		 $("#loanQuery").click(function(){
			 searchHtml(1,10);
		 });
	//省心计划查询
	//点击查询按钮  排序
	$("#selectbtn").click(function(){
		var ftype=parseInt($("#Ftype dt").attr("data-id"));    //付息类型
		var cloperiod=parseInt($("#cloperiod dt").attr("data-id"));    //封闭期
		var expectpay=parseFloat($("#expectpay dt").attr("data-id"));	   //预期收益
		financeH(ftype,cloperiod,expectpay,jsonProduct.length);
	});
	
	
	//排序的箭头
	$(".xiala").click(function(){
		var lengthF=$(this).css("background-image").length;
		if($(this).css("background-image").substring(lengthF-7,lengthF-5).indexOf("3")=="-1"){
			if($(this).hasClass("xiala6")){
				
				$(this).removeClass("xiala6").removeClass("xiala7");
				$(this).attr("data-value",0);
				
			}else{
				
				$(this).addClass("xiala7").removeClass("xiala6");
				$(this).attr("data-value",1);
				//预期收益
				//预期收益
				if($(this).attr("data-id")=="yqsort"){
					
					if(fclisttwo.length==0){
					Array.prototype.push.apply(fclisttwo,xflist);
					}
					fclisttwo.sort(function(x,y){return x[6]-y[6]});
					sysH(fclisttwo.length,fclisttwo);
				}
				//封闭期
				if($(this).attr("data-id")=="fbsort"){
					
					if(fclisttwo.length==0){
					Array.prototype.push.apply(fclisttwo,xflist);
					}
					fclisttwo.sort(function(x,y){return x[2]-y[2]});
					sysH(fclisttwo.length,fclisttwo);
				}
			}
		}else{
				$(this).addClass("xiala6").removeClass("xiala7");
				$(this).attr("data-value",2);
				//预期收益
				if($(this).attr("data-id")=="yqsort"){
					
					if(fclisttwo.length==0){
					Array.prototype.push.apply(fclisttwo,xflist);
					}
					fclisttwo.sort(function(x,y){return y[6]-x[6]});
					sysH(fclisttwo.length,fclisttwo);
				}
				//封闭期
				if($(this).attr("data-id")=="fbsort"){
					
					if(fclisttwo.length==0){
					Array.prototype.push.apply(fclisttwo,xflist);
					}
					fclisttwo.sort(function(x,y){return y[2]-x[2]});
					sysH(fclisttwo.length,fclisttwo);
				}
		}
	});  
	
	searchHtml_fianance(1,6);
//	financeHtml(jsonProduct.length,xflist);

	function sortFL(){
		var ys=$("[data-id=sort]").attr("data-value");
		var fb=$("[data-id=sort]").attr("data-value");
	}
	
	//排序
	$(".th-sort.loanlist a.a-midden.a-sort").click(function(){
		var _this=$(this);
		var fin_up=_this.hasClass("a-midden-up");
		var fin_down=_this.hasClass("a-midden-down");
		if(fin_down){
			_this.removeClass("a-midden-up").removeClass("a-midden-down");
			reOrder(_this);
			 if(flag!=null&&flag!=""&&flag=="new"){			  
				  searchHtmlSpeical(1,10);
			 }else{
				 searchHtml(1,10);
			 }
			return false;
		}else if(!fin_up){
			_this.removeClass("a-midden-down").addClass("a-midden-up");
			reOrder(_this);
			 if(flag!=null&&flag!=""&&flag=="new"){			  
				  searchHtmlSpeical(1,10);
			 }else{
				 searchHtml(1,10);
			 }
			return false;
		}else if(!fin_down){
			_this.removeClass("a-midden-up").addClass("a-midden-down");
			reOrder(_this);
			 if(flag!=null&&flag!=""&&flag=="new"){			  
				  searchHtmlSpeical(1,10);
			 }else{
				 searchHtml(1,10);
			 }
			return false;
		}
	});
	//排序
	$(".th-sort.creditor a.a-midden.a-sort").click(function(){
		var _this=$(this);
		var fin_up=_this.hasClass("a-midden-up");
		var fin_down=_this.hasClass("a-midden-down");
		if(fin_down){
			_this.removeClass("a-midden-up").removeClass("a-midden-down");
			reOrder(_this);
			searchHtmlRight(1, 10);
			return false;
		}else if(!fin_up){
			_this.removeClass("a-midden-down").addClass("a-midden-up");
			reOrder(_this);
			searchHtmlRight(1, 10);
			return false;
		}else if(!fin_down){
			_this.removeClass("a-midden-up").addClass("a-midden-down");
			reOrder(_this);
			searchHtmlRight(1, 10);
			return false;
		}
	});
	/****wangyadong 省心计划 排序*/
	$(".th-sort.finance a.a-midden.a-sort").click(function(){
		var _this=$(this);
		var fin_up=_this.hasClass("a-midden-up");
		var fin_down=_this.hasClass("a-midden-down");
		if(fin_down){
			_this.removeClass("a-midden-up").removeClass("a-midden-down");
			reOrder(_this);
			searchHtml_fianance(1, 6);
			return false;
		}else if(!fin_up){
			_this.removeClass("a-midden-down").addClass("a-midden-up");
			reOrder(_this);
			searchHtml_fianance(1, 6);
			return false;
		}else if(!fin_down){
			_this.removeClass("a-midden-up").addClass("a-midden-down");
			reOrder(_this);
			searchHtml_fianance(1, 6);
			return false;
		}
	});
	
	//条件选择 --省心计划
	$("#finance-project").find("ul.lend-list li.li-context, ul.loan-list li.li-context, ul.income-list li.li-context").click(function(){
		$(this).addClass("action").siblings("li").removeClass("action");
		if($(this).attr("data-id") != null && $(this).attr("data-id")!= ''){
			$(this).parent().siblings("input[type=hidden][name=conditions]").attr("data-id",$(this).attr("data-id"));
		}else{
			$(this).parent().siblings("input[type=hidden][name=conditions]").removeAttr("data-id");
		}
		searchHtml_fianance(1, 6);
	});
	
	//条件选择 -- 出借列表
	$("#finance-list").find("ul.lend-list li.li-context, ul.loan-list li.li-context, ul.income-list li.li-context").click(function(){
		$(this).addClass("action").siblings("li").removeClass("action");
		if($(this).attr("data-id") != null && $(this).attr("data-id")!= ''){
			$(this).parent().siblings("input[type=hidden][name=conditions]").attr("data-id",$(this).attr("data-id"));
		}else{
			$(this).parent().siblings("input[type=hidden][name=conditions]").removeAttr("data-id");
		}
		 if(flag!=null&&flag!=""&&flag=="new"){			  
			  searchHtmlSpeical(1,10);
		 }else{
			 searchHtml(1,10);
		 }
	});
	
	//条件选择 -- 债权转让
	$("#turning-creditrights").find("ul.lend-list li.li-context, ul.loan-list li.li-context, ul.income-list li.li-context").click(function(){
		$(this).addClass("action").siblings("li").removeClass("action");
		if($(this).attr("data-id") != null && $(this).attr("data-id")!= ''){
			$(this).parent().siblings("input[type=hidden][name=conditions]").attr("data-id",$(this).attr("data-id"));
		}else{
			$(this).parent().siblings("input[type=hidden][name=conditions]").removeAttr("data-id");
		}
		searchHtmlRight(1, 10);
	});
	
	
});  

function financeH(ftype,cloperiod,expectpay,rows){
	var fslist1=[],f1=0;
	var fslist2=[],f2=0;
	var fslist3=[],f3=0;
	//判断付息类型
	if(ftype!=0){
		for(var i=0; i<parseInt(rows); i++){
			if(xflist[i][5]==ftype){
				fslist1[f1]=[];
				Array.prototype.push.apply(fslist1[f1],xflist[i]);
				//fslist1[f1].concat(xflist[i]);//将公共数组的值赋予给将要显示的数组
				
				f1++;
			}
		}
	}else{
		
		for(var i=0; i<parseInt(rows); i++){
			
				fslist1[i]=[];
				Array.prototype.push.apply(fslist1[i],xflist[i]);
				//fslist1[f1].concat(xflist[i]);//将公共数组的值赋予给将要显示的
				
		}
	}
	//判断封闭期
	if(cloperiod!=0){
		for(var i=0; i<parseInt(fslist1.length); i++){
			if(fslist1[i][2]==cloperiod){
				fslist2[f2]=[];
				Array.prototype.push.apply(fslist2[f2],fslist1[i]);
				//fslist1[f1].concat(xflist[i]);//将公共数组的值赋予给将要显示的数
				f2++;
			}
		}
	}else{
		
		for(var i=0; i<parseInt(fslist1.length); i++){
			
				fslist2[i]=[];
				Array.prototype.push.apply(fslist2[i],fslist1[i]);
				//fslist1[f1].concat(xflist[i]);//将公共数组的值赋予给将要显示的
				
		}
	}
	//console.log("fslist2数组长度="+fslist2.length);
	//判断预期收益
	if(expectpay!=0){
		for(var i=0; i<parseInt(fslist2.length); i++){
			//console.log("fslist2[i][6]="+fslist2[i][6]);
			//console.log("expectpay"+expectpay);
			if(fslist2[i][6]==expectpay){
				fslist3[f3]=[];
				Array.prototype.push.apply(fslist3[f3],fslist2[i]);
				//fslist1[f1].concat(xflist[i]);//将公共数组的值赋予给将要显示的数组
				f3++;
			}
		}
	}else{
		
		for(var i=0; i<parseInt(fslist2.length); i++){
			
				fslist3[i]=[];
				Array.prototype.push.apply(fslist3[i],fslist2[i]);
				//fslist1[f1].concat(xflist[i]);//将公共数组的值赋予给将要显示的
				
		}
	}
	//数组排序
	fclisttwo=fslist3.slice();
	//console.log("fslist3数组长度="+fslist3.length);
	sysH(fslist3.length,fslist3);
	
}


//声明省心计划列表
var xflist=[];
var fclisttwo=[];
//省心计划查询
function financeHtml(rows, flist) {
	/*
	 * 声明省心计划列表中各参数 for(var i=0; i<=parseInt(rows); i++){
	 * //1.标题，2.年化利率，3.封闭期，4.发行期数，5.余额，6.周期付息，7.预期收益，8.产品详情链接，9.详情文字
	 * flist[0]=["新手引导","15",3,"XTB20150001",1000000,1,15,"index.do?financeId=1",'是陆金所网站平台推出的个人投融资服务。陆金所向投资方（投资人）和融资方，帮助双方快捷方便地完成投资和投和方'];
	 * flist[1]=["新手引导","12",6,"XTB20150001",2000000,1,12,"index.do?financeId=1",'是陆金所网站平台推出的个人投融资服务。陆金所向投资方（投资人）和融资方，帮助双方快捷方便地完成投资和投和方'];
	 * flist[2]=["新手引导","9",12,"XTB20150001",300000,2,9,"index.do?financeId=1",'是陆金所网站平台推出的个人投融资服务。陆金所向投资方（投资人）和融资方，帮助双方快捷方便地完成投资和投和方'];
	 */
	for (var i = 0; i < jsonProduct.length; i++) {
		var data = jsonProduct[i];
		flist[i] = [
				data.productName,
				data.profitRate,
				data.timeLimit,
				data.publishCode,
				data.availableBalance,
				data.toInterestPoint,
				data.profitRate,
				data.lendProductPublishId,
				'是陆金所网站平台推出的个人投融资服务。陆金所向投资方（投资人）和融资方，帮助双方快捷方便地完成投资和投和方',
				data.timeLimitType,
				data.timeLimit,
				data.recommend, 
				data.publishBalance,
				data.soldBalance,
				data.publishBalanceType
				];
	}
	xflist = flist;
	sysH(rows, flist);
}
function sysH(rows,flist){
	//console.log(rows+"数组长度")
	var thtml="";
	if(rows!=0){
//		thtml='<ul class="finance-title clearFloat"><li class="li-01">项目名称</li><li class="li-02">年化利率</li><li class="li-03">封闭期</li><li class="li-04">借款金额</li><li class="li-05">借款进度</li><li class="li-06">&nbsp;</li></ul>';
			for(var i=0;i<parseInt(rows);i++){
//				if(i==0){
//					thtml +='<div class="flcontext flcontexthover clearFloat">';
//				}else{
//					thtml +=' <div class="flcontext clearFloat">';
//				}
//				thtml +='<div class="flleft">';
//				thtml +='<div class="clearFloat">';
//				thtml +='<span class="title"><a href="'+'toFinanceDetail?lendProductPublishId='+flist[i][7]+'" class="cut_str" >'+flist[i][0]+' </a></span>';
//				thtml +='<span class="nian"><img src="../images/fd_27.png" /><label>年化利率<em class="big lilv">'+flist[i][1]+'<small>%</small></em></label></span>';
//				thtml +='<span class="feng"><img src="../images/fd_32.png"><label>封闭期<br /><i>'+flist[i][2]+'</i>'+(flist[i][9]==1?"天":"个月")+'</label></span>';
//				thtml +=' <span class="faxing"><img src="../images/fd_29.png"><label>发行期数<br /><i>'+flist[i][3]+'</i></label></span>';
//				thtml +='</div>';
//				thtml +='<p class="cut_str mt-20">'+flist[i][8]+'</p>';
//				thtml +='</div>';
//				thtml +='<div class="flright">';
//				thtml +='<p>本期剩余金额'+ (flist[i][4] == '-1' ? '--' : fmoney(flist[i][4],2))+'元</p> ';
//				thtml +=' <div class="input-group">';
//				thtml +='<input type="hidden" class="borrowday" data-value="'+flist[i][5]+'" /><!-- 周期付息 -->';
//				thtml +='<input type="hidden" data-id="closePeriod" data-value="'+flist[i][3]+'" /><!-- 封闭期 -->';
//				thtml +='<input type="hidden" data-id="periodPay" data-value="'+flist[i][6]+'" /><!-- 预期收益 % -->';
//				thtml +='<input type="hidden" data-id="lendProductPublishId" data-value="'+flist[i][7]+'" /><!-- lendProductPublishId -->';
//				thtml +='<label>';
//				thtml +='<input type="text" placeholder="输入理财金额" class="ipt-input"/><i class="yuan1">元</i>';
//				thtml +='</label>';
//				thtml +='<input type="hidden" data-id="timeLimitType" data-value="'+flist[i][9]+'" /><!-- 期限类型 -->';
//				thtml +='<input type="hidden" data-id="timeLimit" data-value="'+flist[i][10]+'" /><!--期限值 -->';
//				thtml +='<em></em>';
//				thtml +='</div>';
//				thtml +='<div class="lookdetail clearFloat">';
//				thtml +=' <a href="'+'toFinanceDetail?lendProductPublishId='+flist[i][7]+'" class="ablue">查看产品详情</a>';
//				thtml +='<button type="button" data-id="licai" class="btn btn-error flbtn mt-0"><img src="../images/images/icon_03.png" class=" mt-10 mr-5" />去支付</button>';
//				thtml +=' </div></div></div>';
				
				
//				thtml +='<ul class="finance-list clearFloat"><li class="li-01"><a href="'+'toFinanceDetail?lendProductPublishId='+flist[i][7]+'" title="'+flist[i][0]+'">'+flist[i][0]+'</a></li>' ;
//				thtml +='<li class="li-02"><em>'+flist[i][1]+'</em>%</li>' ;
//				thtml +='<li class="li-03"><em>'+flist[i][2]+'</i>'+(flist[i][9]==1?"</em>天":"</em>个月")+'</li>' ;
//				thtml +='<li class="li-04"><em>'+ (flist[i][4] == '-1' ? '--' : fmoney(flist[i][4],2))+'</em>元</li>' ;
////				thtml +='<li class="li-05"><div class="pro-group"><div class="pro-schedule" style="width:75%"></div></div><span class="rate">75%</span></li>' ;
//				thtml +='<li class="li-06"><a href="'+rootPath+'/finance/toFinanceDetail?lendProductPublishId='+flist[i][7]+'" class="btn btn-write">立即加入</a></li></ul>' ;
//				l(rate,month,code,money){
				var fmoney1 = flist[i][4] == '-1' ? '--' : fmoney(flist[i][4],2);
//				console.log("asd"+fmoney1);
//				console.log(flist[i][10]+","+flist[i][11]);
				thtml +=makeFinanceHtml(flist[i][1],flist[i][2],fmoney1,"",flist[i][0],flist[i][7],"",data.startsAt,i,flist[i][11],flist[i][12],flist[i][13],flist[i][14]);
			} 
			if(parseInt(rows) ==1){
				thtml+="<div class='s_shengxinBox boxCenter'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
				thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
			}else if(parseInt(rows) ==2){
				thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
			}
	}else{
				thtml +='<div class="tac mt-30"><img src="'+rootPath+'/images/jqqdnone.jpg" /></div>';
	}
	 
	$('#financeProject').html(thtml);
	
 
		$("#pageListFinance").createPage({
			pageCount:rows,
			current:1,
			backFn:function(p){
				//点击分页效果
				searchHtml_fianance(parseInt(p),6);
			}
		});
 
	
	bottomB();
}
/***wangyadong 省心计划列表*/
function  makeFinanceHtml(rate,month,money,code,name,pId,status,at,index,isRecommend,publishBalance,soldBalance,publishBalanceType){
//	console.log("soldBalance"+soldBalance);
//	console.log("publishBalance"+publishBalance);
//	console.log("status"+status);
	var thtml= "" ; 
	if(index%3==0 && index != 7){
		thtml += "<div class='s_shengxinBox  boxLeft'  >";
	}else{
		thtml += "<div class='s_shengxinBox  boxCenter'  >";
	}
	//是否推荐
	if(isRecommend=='1' ){
		thtml += "<div class='s_tuijian'></div>";
	}
	//名称
	thtml += "<h2>"+name+"</h2>";
	//期限
	thtml += "<div class='s_shengxBox'><p class='s_text1'><font>"+month+"</font>个月</p><p class='s_text2'><span>省心期</span></p></div>";
	//进度条
	var soldPersent = 0 ;
	if(publishBalanceType == '2'){//是否有限额
		soldPersent = parseInt(soldBalance)* 100/parseInt(publishBalance) ;
		thtml += "<div class='s_jindu'><div class='s_jindut'>进度：</div><div class='progressBox'><div class='progressBoxBar' style='width:"+soldPersent +"%' ></div></div></div>";
	}
	
	thtml += "<div class='clear_0'></div>" ;
	//标的期限描述
	thtml += "<div class='s_text_main'><p>投资标的：优先享受热门1-12月期限标的</p><p>省心方式：自动投标、回款复投</p></div>";
	
	//立即加入
//	'<li class="li-01 c-dai"><a class="" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
	if(((publishBalanceType == '2'&&soldPersent >= 100) ||  status != '2' ) && status =='3'){
		thtml += "<div class='s_shengxinButton_hui'><a href='javascript:;' ><button  onclick='setWinName1();window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")' >已满额</button></a></div></div>";
	}else if(status == '4' ){
		thtml += "<div class='s_shengxinButton_hui'><a  href='javascript:;'  ><button onclick='setWinName1();window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")'  >已完成</button></a></div></div>";
	}else if(status == '2'){
		thtml += "<div class='s_shengxinButton'><a  href='javascript:;'  ><button  onclick='setWinName1();window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")'  >立即加入</button></a></div></div>";
	}else if(status == '1'){
		thtml += "<div class='s_shengxinButton'><a  href='javascript:;'  ><button onclick='setWinName1();window.location=(\""+rootPath+"/finance/toFinanceDetail?lendProductPublishId="+pId+"\")'  >预热中</button></a></div></div>";
	}

	return thtml;
}

function getOrderBy(value) {

	if(value=='0'){
		return true;
	}else if(value=='1'){
		return false;
	}else if(value=='2'){
		return null;
	}else{
		return true;
	}
}
/**
 * 排序操作
 */
var annRateOrder = null;
var durationOrder = null;
var creditRankOrder = null;
function reOrder(v){
	if($(v).hasClass('annRateOrder')){
		if($(v).hasClass("a-midden-down")){
			$(v).attr("data-value","1");
		}else if($(v).hasClass("a-midden-up")){
			$(v).attr("data-value","0");
		}else{
			$(v).attr("data-value","2");
		}
		annRateOrder = getOrderBy($(v).attr("data-value"));
	}else if($(v).hasClass("durationOrder")){
		if($(v).hasClass("a-midden-down")){
			$(v).attr("data-value","1");
		}else if($(v).hasClass("a-midden-up")){
			$(v).attr("data-value","0");
		}else{
			$(v).attr("data-value","2");
		}
		durationOrder = getOrderBy($(v).attr("data-value"));
	}
//	else{
//		creditRankOrder = getOrderBy($("#creditRankOrder").attr("data-value"));
//	}
//	searchHtml(1,10);
}

function defaultSortOrder(v){
	$(".annRateOrder").each(function(){
		$(this).attr("data-value","2");
	});
	$(".durationOrder").each(function(){
		$(this).attr("data-value","2");
	});
//	if($(v).attr("id")=='annRateOrder'){
		annRateOrder = getOrderBy($(v).siblings(".annRateOrder").attr("data-value"));
//	}else if($(v).attr("id")=='durationOrder'){
		durationOrder = getOrderBy($(v).siblings(".durationOrder").attr("data-value"));
		$(".annRateOrder").each(function(){
			$(this).removeClass("a-midden-up").removeClass("a-midden-down");
		});
		$(".durationOrder").each(function(){
			$(this).removeClass("a-midden-up").removeClass("a-midden-down");
		});
//	}
//	else{
//		creditRankOrder = getOrderBy($("#creditRankOrder").attr("data-value"));
//	}
		  var flag = $("#flagCustomer").val();
		 if(flag!=null&&flag!=""&&flag=="new"){			  
			  searchHtmlSpeical(1,10);
		 }else{
			 searchHtml(1,10);
		 }
}

function defaultSortOrder_creditor(v){
	$(".annRateOrder").each(function(){
		$(this).attr("data-value","2");
	});
	$("#durationOrder").each(function(){
		$(this).attr("data-value","2");
	});
//	if($(v).attr("id")=='annRateOrder'){
		annRateOrder = getOrderBy($(v).siblings(".annRateOrder").attr("data-value"));
//	}else if($(v).attr("id")=='durationOrder'){
		durationOrder = getOrderBy($(v).siblings(".durationOrder").attr("data-value"));
		$(".annRateOrder").each(function(){
			$(this).removeClass("a-midden-up").removeClass("a-midden-down");
		});
		$(".durationOrder").each(function(){
			$(this).removeClass("a-midden-up").removeClass("a-midden-down");
		});
//	}
//	else{
//		creditRankOrder = getOrderBy($("#creditRankOrder").attr("data-value"));
//	}
	searchHtmlRight(1, 10);
}

/**
 * 出借列表查询
 * */
function searchHtml(page,rows){
	var thtml='<ul class="finance-title clearFloat"> <li class="li-01">项目名称</li><li class="li-02">年化利率</li><li class="li-03">借款期限</li><li class="li-04">借款金额</li><li class="li-05">借款进度</li><li class="li-06">&nbsp;</li></ul>';
	//$('.flcontext2').html("");
	$("#pageList").html("");
	$("#pageList").html('<div class="tcdPageCode" id="page1"></div>');

	//组织查询、排序条件
	var loanRateType = $("#loanRateType-lend").attr("data-id");
	var loanType = $("#loanType-lend").attr("data-id");
	var durationTypes = $("#durationType-lend").attr("data-id");
	$.ajax({
		url:rootPath+"/finance/loanList?catch="+Math.random(),
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"loanType": loanType,
			"annualRate": loanRateType,
			"cycleCounts": durationTypes,
			"durationOrder": durationOrder,
			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				thtml += '<ul class="finance-list clearFloat">' ;
				var loanApplicationTitle = '';
				if(data.loanApplicationTitle.length > 24){					
					loanApplicationTitle = data.loanApplicationTitle.substr(0,25)+"...";
				}else{
					loanApplicationTitle = data.loanApplicationTitle;
				}
				if(data.oType=='1'||data.oType=='2'){
					thtml +='<li class="li-01  dx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'" >'+loanApplicationTitle+'</a></li>';
				}else if(data.oType=='3'){
					thtml +='<li class="li-01  dx-newdai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'" >'+loanApplicationTitle+'</a></li>';
				}else{
					if(data.loanType =='0')
						thtml +='<li class="li-01  x-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='1' || data.loanType =='7')
						thtml +='<li class="li-01 f-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='2'|| data.loanType =='8')
						thtml +='<li class="li-01 c-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='3')
						thtml +='<li class="li-01 qyx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='4')
						thtml +='<li class="li-01 bl-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='5')
						thtml +='<li class="li-01 j-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='6')
						thtml +='<li class="li-01 qyb-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
                    if(data.loanType =='9')
                        thtml +='<li class="li-01 xjd-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';

                }
				
				//奖励
				var reward = data.rewardsPercent;// <li class="li-02"><em>12</em>%</li>
				if(reward!=null&&reward!='0'){
					reward='<li class="li-02"><em>'+data.annualRate+'+'+reward+'</em>%<img src="../images/borrow_05.png" /></li>';
				}else{
					reward='<li class="li-02"><em>'+data.annualRate+'</em>%</li>';
				}
				thtml += reward;
	//			thtml += '<li class="li-02"><em>12</em>%</li>';
				//如果为现金贷,就将时间写死为14天
				if(data.loanType =='9'){
					thtml += '<li class="li-03"><em>'+14+'</em>天</li>';
				}else{
					thtml += '<li class="li-03"><em>'+data.cycleCount+'</em>个月</li>';
				}
				thtml += '<li class="li-04"><em>'+fmoney(data.confirmBalance,2)+'</em>元</li>';
				thtml += '<li class="li-05"><div class="pro-group"><div class="pro-schedule" style="width:'+data.ratePercent+'%"></div></div><span class="rate">'+data.ratePercent+'%</span></li>';
				if(data.ratePercent==100){
					if(data.applicationState=='6'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">还款中</a></li></ul>';
					}else if(data.applicationState=='7'||data.applicationState=='8'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已结清</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已满标</a></li></ul>';
					}
				}else{
					if(!data.begin){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">预热中</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">立即加入</a></li></ul>';
					}
				}
				
			}
			$('#loanlist-flcontext2').html(thtml);
//			$('.flcontext2').html(thtml);
			

			if(d_rows.length>=0){
				$("#lend_page").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//点击分页效果
						searchHtml_1(parseInt(p),10);
					}
				});
			}
			bottomB();
		}
	});
}


//债权市场列表查询
function searchHtmlRight(page,rows){
	var thtml='<ul class="finance-title clearFloat"><li class="li-01" style="text-align:left;"><span style="display:inline-block; margin-left:50px;">借款标题</span></li><li class="li-08" style="color:#666">年化利率</li><li class="li-03">剩余期限</li><li class="li-04" >剩余本金</li><li class="li-04" style="color:#f66">转出价格</li><li class="li-10 finance-list-zqzr-hide">进度</li></ul>';
	$("#rightsList").html("");
	$("#rightsList").html('<div class="aa tcdPageCode" id="rightId"></div>');

	//组织查询、排序条件
	var rightsType = $("#rightsType").attr("data-id");
	var rightsDurationType = $("#rightsDurationType").attr("data-id");
	var rightsRateType = $("#rightsRateType").attr("data-id");
	$.ajax({
		url:rootPath+"/finance/creditorRightsList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"loanType": rightsType,
			"annualRate": rightsRateType,
			"cycleCounts": rightsDurationType,
			"durationOrder": durationOrder,
//			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				var reward = data.rewardsPercent;
				if (reward != null && reward != '0' && data.awardPoint != 1 && data.awardPoint != null) {
					reward='<li class="li-08"><em>'+data.annualRate+'+'+reward+'</em>%<img src="../images/borrow_05.png" /></li>';
				}else{
					reward='<li class="li-08"><em>'+data.annualRate+'</em>%</li>';
				}
				var loanApplicationTitle = '';
				if(data.loanApplicationTitle.length > 20){					
					loanApplicationTitle = data.loanApplicationTitle.substr(0,21)+"...";
				}else{
					loanApplicationTitle = data.loanApplicationTitle;
				}
				thtml +='<ul class="dltitleul dlcontext clearFloat">';
				thtml +='<ul class="finance-list clearFloat">';
				if(data.loanType =='0')
					thtml +='<li class="li-01 x-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='1' || data.loanType =='7')
					thtml +='<li class="li-01 f-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='2' || data.loanType =='8')
					thtml +='<li class="li-01 c-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='3')
					thtml +='<li class="li-01 qyx-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='4')
					thtml +='<li class="li-01 bl-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='5')
					thtml +='<li class="li-01 j-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='6')
					thtml +='<li class="li-01 qyb-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';

                thtml += reward;

				thtml +='<li class="li-03"><em>'+data.cycleCount+'</em>个月</li>';
				thtml +='<li class="li-04"><em>'+fmoney(data.whenWorth,2)+'</em>元</li>';
				thtml +='<li class="li-04" style="color:#f66"><em>'+fmoney(data.applyPrice,2)+'</em>元</li>';
				var ratePercent = (data.totalAmountOfLoan / data.applyPrice) * 100 + "";
				var ratePercentStr = ratePercent.indexOf(".") != -1 ? ratePercent.substring(0, ratePercent.indexOf(".") + 3) : ratePercent;
				thtml +='<li class="li-10 finance-list-zqzr-hide "><div class="pro-group"><div class="pro-schedule" style="width:'+ratePercentStr+'%"></div></div><span class="rate">'+ratePercentStr+'%</span></li>';
				if(data.totalAmountOfLoan == data.applyPrice){
					thtml +='<li class="li-06"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" class="btn btn-deer-gray">已满标</a></li></ul>';
				}else{
					thtml +='<li class="li-06"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" class="btn btn-write">立即加入</a></li></ul>';
				}
			}

			$('#rights-flcontext2').html(thtml);

			if(d_rows.length>0){
				$("#rightId").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//点击分页效果
						searchHtmlRight_1(parseInt(p),10);
					}
				});
			}else{
				// 追加无数据提示
				$('#rights-flcontext2').append(
			        '<div class="finance_zw_img">'+
						'<img src="../images/finance_list/finance_img.png" />'+
						'<h2>不好意思，目前没债权转让的标的了<br />去出借列表购买其他的标的吧</h2>'+
					'</div>'
				);
			}
			bottomB();
		}
	});

}

function searchHtml_1(page,rows){
	var thtml='<ul class="finance-title clearFloat"> <li class="li-01">项目名称</li><li class="li-02">年化利率</li><li class="li-03">借款期限</li><li class="li-04">借款金额</li><li class="li-05">借款进度</li><li class="li-06">&nbsp;</li></ul>';
	//$('.flcontext2').html("");
	//组织查询、排序条件
	var loanRateType = $("#loanRateType-lend").attr("data-id");
	var loanType = $("#loanType-lend").attr("data-id");
	var durationTypes = $("#durationType-lend").attr("data-id");

	$.ajax({
		url:rootPath+"/finance/loanList?catch="+Math.random(),
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"loanType": loanType,
			"annualRate": loanRateType,
			"cycleCounts": durationTypes,
			"durationOrder": durationOrder,
			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				thtml += '<ul class="finance-list clearFloat">' ;
				var loanApplicationTitle = '';
				if(data.loanApplicationTitle.length > 24){					
					loanApplicationTitle = data.loanApplicationTitle.substr(0,25)+"...";
				}else{
					loanApplicationTitle = data.loanApplicationTitle;
				}
				if(data.oType=='1'||data.oType=='2'){
					thtml +='<li class="li-01  dx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
				}else if(data.oType=='3'){
					thtml +='<li class="li-01  dx-newdai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
				}else{
					if(data.loanType =='0')
						thtml +='<li class="li-01 x-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='1' || data.loanType =='7')
						thtml +='<li class="li-01 f-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='2'|| data.loanType =='8')
						thtml +='<li class="li-01 c-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='3')
						thtml +='<li class="li-01 qyx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='4')
						thtml +='<li class="li-01 bl-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='5')
						thtml +='<li class="li-01 j-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='6')
						thtml +='<li class="li-01 qyb-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
                    if(data.loanType =='9'){
                        thtml +='<li class="li-01 xjd-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					}

				}
				//奖励
				var reward = data.rewardsPercent;// <li class="li-02"><em>12</em>%</li>
				if(reward!=null&&reward!='0'){
					reward='<li class="li-02"><em>'+data.annualRate+'+'+reward+'</em>%<img src="../images/borrow_05.png" /></li>';
				}else{
					reward='<li class="li-02"><em>'+data.annualRate+'</em>%</li>';
				}
				thtml += reward;
				thtml += '<li class="li-03"><em>'+(data.dueTimeType == 1? data.cycleCount * data.dueTime: data.cycleCount)+'</em>'+(data.dueTimeType == 1?'天':'个月')+'</li>';
				thtml += '<li class="li-04"><em>'+fmoney(data.confirmBalance,2)+'</em>元</li>';
				thtml += '<li class="li-05"><div class="pro-group"><div class="pro-schedule" style="width:'+data.ratePercent+'%"></div></div><span class="rate">'+data.ratePercent+'%</span></li>';
				if(data.ratePercent==100){
					if(data.applicationState=='6'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">还款中</a></li></ul>';
					}else if(data.applicationState=='7'||data.applicationState=='8'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已结清</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已满标</a></li></ul>';
					}

				}else{
					if(!data.begin){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">预热中</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">立即加入</a></li></ul>';
					}
				}
			}
			$('#loanlist-flcontext2').html(thtml);
			bottomB();
		}
	});

}
/***wangyadong  省心计划排序******/
function defaultSortOrder_finance(v){
	$(".annRateOrder").each(function(){
		$(v).attr("data-value","");
	});
	$(".durationOrder").each(function(){
		$(v).attr("data-value","");
	});
	$(v).attr("data-value","");
	$(v).attr("data-value","");
//	console.log($(v).siblings(".annRateOrder").attr("data-value")+"=====");
//	console.log($(v).siblings(".durationOrder").attr("data-value")+"=====2");
//	if($(v).attr("id")=='annRateOrder'){
	//	annRateOrder = getOrderBy($(v).siblings(".annRateOrder").attr("data-value"));
//	}else if($(v).attr("id")=='durationOrder'){
	//	durationOrder = getOrderBy($(v).siblings(".durationOrder").attr("data-value"));
		$(".annRateOrder").each(function(){
			$(this).removeClass("a-midden-up").removeClass("a-midden-down");
		});
		$(".durationOrder").each(function(){
			$(this).removeClass("a-midden-up").removeClass("a-midden-down");
		});
		annRateOrder = null;
		durationOrder = null;

//	}
//	else{
//		creditRankOrder = getOrderBy($("#creditRankOrder").attr("data-value"));
//	}
		searchHtml_fianance(1,6);
}
/***wangyadong  省心计划分页******/
function searchHtml_fianance(page,rows){
	var thtml="";
	//$('.flcontext2').html("");
	//组织查询、排序条件
	var financeGuessList = $("#financeGuessList").attr("data-id");// 预期收益
	var financeduringList = $("#financeduringList").attr("data-id");//借款期限
//	var durationType = $("#rightsDurationType").attr("data-id");
	var financeLendList = $("#financeLendList").attr("data-id");//付息类型

	$.ajax({
		url:rootPath+"/finance/lendList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"financeGuessList": financeGuessList,
			"financeduringList": financeduringList,
			"financeLendList": financeLendList,
			"durationOrder": durationOrder,
			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var currentPage=data.currentPage;
			var pageCount = data.totalPage;
			var totalCounts = data.total;

			if(d_rows.length < 1){
				thtml += '<div class="tac mt-30"><img src="' + rootPath+'/images/jqqdnone.jpg" /></div>' ;
				$('#financeProject').html(thtml);
			}else{
				for(var i=0;i<d_rows.length;i++){
					var data = d_rows[i];
	//				 makeFinanceHtml(rate,month,money,code,name,pId)lendProductId
					//timeLimitType timeLimit
					var closing = "";
					if(data.timeLimitType=='1'){
						closing =data.timeLimit+"月";
					}
					if(data.timeLimitType=='2'){
						closing =data.timeLimit+"日";
					}
//					console.log("data.soldBalance,data.publishBalanceType"+data.soldBalance+";.;"+data.publishBalanceType);
					thtml+=makeFinanceHtml(data.profitRate,data.timeLimit,data.money,
							data.publishCode,data.publishName,data.lendProductPublishId,data.publishState,data.startsAt,i,data.recommend,data.publishBalance,data.soldBalance,data.publishBalanceType);

				}
				if(parseInt(totalCounts) ==1){
					thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
					thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
				}else if(parseInt(totalCounts) ==2){
					thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
				}
				$('#financeProject').html(thtml);
				$("#pageListFinance").createPage({
					pageCount:pageCount,
					current:currentPage,
					backFn:function(p){
						//点击分页效果
						searchHtml_fianance_1(parseInt(p),6);
					}
				});
			}
			bottomB();
		}
	});


}
/***wangyadong  省心计划分页******/
function searchHtml_fianance_1(page,rows){
	var thtml="";
	//$('.flcontext2').html("");
	//组织查询、排序条件
	var financeGuessList = $("#financeGuessList").attr("data-id");// 预期收益
	var financeduringList = $("#financeduringList").attr("data-id");//借款期限
//	var durationType = $("#rightsDurationType").attr("data-id");
	var financeLendList = $("#financeLendList").attr("data-id");//付息类型

	$.ajax({
		url:rootPath+"/finance/lendList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"financeGuessList": financeGuessList,
			"financeduringList": financeduringList,
			"financeLendList": financeLendList,
			"durationOrder": durationOrder,
			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var currentPage=data.currentPage;
			var pageCount = data.totalPage;
			var totalCounts = data.total;

			if(d_rows.length < 1){
				thtml += '<div class="tac mt-30"><img src="' + rootPath+'/images/jqqdnone.jpg" /></div>' ;
			}else{

					for(var i=0;i<d_rows.length;i++){
						var data = d_rows[i];
		//				 makeFinanceHtml(rate,month,money,code,name,pId)lendProductId
						//timeLimitType timeLimit
						var closing = "";
						if(data.timeLimitType=='1'){
							closing =data.timeLimit+"月";
						}
						if(data.timeLimitType=='2'){
							closing =data.timeLimit+"日";
						}
						thtml+=makeFinanceHtml(data.profitRate,data.timeLimit,data.money,
								data.publishCode,data.publishName,data.lendProductPublishId,data.publishState,data.startsAt,i,data.recommend,data.publishBalance,data.soldBalance,data.publishBalanceType);
					}
					if(parseInt(totalCounts) ==1){
						thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
						thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
					}else if(parseInt(totalCounts) ==2){
						thtml+="<div class='s_shengxinBox boxRight'><img src='"+rootPath+"/images/shengxin/sx_pic_01.jpg' /></div>";
					}
			}
			$('#financeProject').html(thtml);
			bottomB();
		}
	});


}
function searchHtmlRight_1(page,rows){
	var thtml='<ul class="finance-title clearFloat"><li class="li-01" style="text-align:left;"><span style="display:inline-block; margin-left:50px;">借款标题</span></li><li class="li-08" style="color:#666">年化利率</li><li class="li-03">剩余期限</li><li class="li-04">剩余本金</li><li class="li-04" style="color:#f66">转出价格</li><li class="li-10 finance-list-zqzr-hide">进度</li></ul>';
	//组织查询、排序条件
	var loanRateType = $("#rightsRateType").attr("data-id");
	var loanType = $("#rightsType").attr("data-id");
//	var durationType = $("#rightsDurationType").attr("data-id");
	var durationTypes = $("#rightsDurationType").attr("data-id");

	$.ajax({
		url:rootPath+"/finance/creditorRightsList",
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"loanType": loanType,
			"annualRate": loanRateType,
			"cycleCounts": durationTypes,
			"durationOrder": durationOrder,
			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				var reward = data.rewardsPercent;
				if (reward != null && reward != '0' && data.awardPoint != 1 && data.awardPoint != null) {
					reward='<li class="li-08"><em>'+data.annualRate+'+'+reward+'</em>%<img src="../images/borrow_05.png" /></li>';
				}else{
					reward='<li class="li-08"><em>'+data.annualRate+'</em>%</li>';
				}
				var loanApplicationTitle = '';
				if(data.loanApplicationTitle.length > 20){					
					loanApplicationTitle = data.loanApplicationTitle.substr(0,21)+"...";
				}else{
					loanApplicationTitle = data.loanApplicationTitle;
				}
				thtml +='<ul class="dltitleul dlcontext clearFloat">';
				thtml +='<ul class="finance-list clearFloat">';
				if(data.loanType =='0')
					thtml +='<li class="li-01 x-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='1' || data.loanType =='7')
					thtml +='<li class="li-01 f-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='2')
					thtml +='<li class="li-01 c-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='3')
					thtml +='<li class="li-01 qyx-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='4')
					thtml +='<li class="li-01 bl-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='5')
					thtml +='<li class="li-01 j-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				if(data.loanType =='6')
					thtml +='<li class="li-01 qyb-dai"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" title="'+data.loanApplicationTitle+'">'+loanApplicationTitle+'</a></li>';
				thtml += reward;
				
				thtml +='<li class="li-03"><em>'+data.cycleCount+'</em>个月</li>';
				thtml +='<li class="li-04"><em>'+fmoney(data.whenWorth,2)+'</em>元</li>';
				thtml +='<li class="li-04" style="color:#f66"><em>'+fmoney(data.applyPrice,2)+'</em>元</li>';
				var ratePercent = (data.totalAmountOfLoan / data.applyPrice)*100 +"";
				var ratePercentStr = ratePercent.indexOf(".") != -1 ? ratePercent.substring(0, ratePercent.indexOf(".") + 3) : ratePercent;
				thtml +='<li class="li-10 finance-list-zqzr-hide"><div class="pro-group"><div class="pro-schedule" style="width:'+ratePercentStr+'%"></div></div><span class="rate">'+ratePercentStr+'%</span></li>';
				if(data.totalAmountOfLoan == data.applyPrice){
					thtml +='<li class="li-06"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" class="btn btn-deer-gray">已满标</a></li></ul>';
				}else{
					thtml +='<li class="li-06"><a onclick="setWinName3()" href="'+rootPath+'/finance/creditRightBidding?creditorRightsApplyId='+data.creditorRightsApplyId+'" class="btn btn-write">立即加入</a></li></ul>';
				}
			}
			$('#rights-flcontext2').html(thtml);
			bottomB();
		}
	});
	
	
	
	

}

/**
 * 出借新手列表查询
 * */
function searchHtmlSpeical(page,rows){
//	$('#loanlist-flcontext2').html("");
	var thtml='<ul class="finance-title clearFloat"> <li class="li-01">项目名称</li><li class="li-02">年化利率</li><li class="li-03">借款期限</li><li class="li-04">借款金额</li><li class="li-05">借款进度</li><li class="li-06">&nbsp;</li></ul>';
	$("#pageList").html("");
	$("#pageList").html('<div class="tcdPageCode" id="page1"></div>');

	//组织查询、排序条件
	var loanRateType = $("#loanRateType-lend").attr("data-id");
	var loanType = $("#loanType-lend").attr("data-id");
	var durationTypes = $("#durationType-lend").attr("data-id");
	$.ajax({
		url:rootPath+"/finance/loanSpecialList?catch="+Math.random(),
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"loanType": loanType,
			"annualRate": loanRateType,
			"cycleCounts": durationTypes,
			"durationOrder": durationOrder,
			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				thtml += '<ul class="finance-list clearFloat">' ;
				var loanApplicationTitle = '';
				if(data.loanApplicationTitle.length > 24){					
					loanApplicationTitle = data.loanApplicationTitle.substr(0,25)+"...";
				}else{
					loanApplicationTitle = data.loanApplicationTitle;
				}
				if(data.oType=='1'||data.oType=='2'){
					thtml +='<li class="li-01  dx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'" >'+loanApplicationTitle+'</a></li>';
				}else if(data.oType=='3'){
					thtml +='<li class="li-01  dx-newdai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'" >'+loanApplicationTitle+'</a></li>';
				}else{
					if(data.loanType =='0')
						thtml +='<li class="li-01  x-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='1' || data.loanType =='7')
						thtml +='<li class="li-01 f-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='2')
						thtml +='<li class="li-01 c-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='3')
						thtml +='<li class="li-01 qyx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='4')
						thtml +='<li class="li-01 bl-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='5')
						thtml +='<li class="li-01 j-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='6')
						thtml +='<li class="li-01 qyb-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
				}
				
				//奖励
				var reward = data.rewardsPercent;// <li class="li-02"><em>12</em>%</li>
				if(reward!=null&&reward!='0'){
					reward='<li class="li-02"><em>'+data.annualRate+'+'+reward+'</em>%<img src="../images/borrow_05.png" /></li>';
				}else{
					reward='<li class="li-02"><em>'+data.annualRate+'</em>%</li>';
				}
				thtml += reward;
	//			thtml += '<li class="li-02"><em>12</em>%</li>';
				thtml += '<li class="li-03"><em>'+data.cycleCount+'</em>个月</li>';
				thtml += '<li class="li-04"><em>'+fmoney(data.confirmBalance,2)+'</em>元</li>';
				thtml += '<li class="li-05"><div class="pro-group"><div class="pro-schedule" style="width:'+data.ratePercent+'%"></div></div><span class="rate">'+data.ratePercent+'%</span></li>';
				if(data.ratePercent==100){
					if(data.applicationState=='6'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">还款中</a></li></ul>';
					}else if(data.applicationState=='7'||data.applicationState=='8'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已结清</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已满标</a></li></ul>';
					}
				}else{
					if(!data.begin){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">预热中</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">立即加入</a></li></ul>';
					}
				}
				
			}
			$('#loanlist-flcontext2').html(thtml);
		
//			$('.flcontext2').html(thtml);
			

			if(d_rows.length>=0){
				$("#lend_page").createPage({
					pageCount:pageCount,
					current:1,
					backFn:function(p){
						//点击分页效果
						searchHtmlSpeical_paging(parseInt(p),10);
					}
				});
			}
			bottomB();
		}
	});
}

/**
 * 出借新手列表查询
 * */
function searchHtmlSpeical_paging(page,rows){
	var thtml='<ul class="finance-title clearFloat"> <li class="li-01">项目名称</li><li class="li-02">年化利率</li><li class="li-03">借款期限</li><li class="li-04">借款金额</li><li class="li-05">借款进度</li><li class="li-06">&nbsp;</li></ul>';
	//组织查询、排序条件
	var loanRateType = $("#loanRateType-lend").attr("data-id");
	var loanType = $("#loanType-lend").attr("data-id");
	var durationTypes = $("#durationType-lend").attr("data-id");
	$.ajax({
		url:rootPath+"/finance/loanSpecialList?catch="+Math.random(),
		type:"post",
		data: {
			"pageSize": rows,
			"pageNo": page,
			"loanType": loanType,
			"annualRate": loanRateType,
			"cycleCounts": durationTypes,
			"durationOrder": durationOrder,
			"creditRankOrder": creditRankOrder,
			"annRateOrder": annRateOrder
		},
		success: function (data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			for(var i=0;i<d_rows.length;i++){
				var data = d_rows[i];
				thtml += '<ul class="finance-list clearFloat">' ;
				var loanApplicationTitle = '';
				if(data.loanApplicationTitle.length > 24){					
					loanApplicationTitle = data.loanApplicationTitle.substr(0,25)+"...";
				}else{
					loanApplicationTitle = data.loanApplicationTitle;
				}
				if(data.oType=='1'||data.oType=='2'){
					thtml +='<li class="li-01  dx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'" >'+loanApplicationTitle+'</a></li>';
				}else if(data.oType=='3'){
					thtml +='<li class="li-01  dx-newdai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'" >'+loanApplicationTitle+'</a></li>';
				}else{
					if(data.loanType =='0')
						thtml +='<li class="li-01  x-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='1' || data.loanType =='7')
						thtml +='<li class="li-01 f-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='2')
						thtml +='<li class="li-01 c-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='3')
						thtml +='<li class="li-01 qyx-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='4')
						thtml +='<li class="li-01 bl-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='5')
						thtml +='<li class="li-01 j-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
					if(data.loanType =='6')
						thtml +='<li class="li-01 qyb-dai"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" title="'+data.loanApplicationTitle+'"  >'+loanApplicationTitle+'</a></li>';
				}
				
				//奖励
				var reward = data.rewardsPercent;// <li class="li-02"><em>12</em>%</li>
				if(reward!=null&&reward!='0'){
					reward='<li class="li-02"><em>'+data.annualRate+'+'+reward+'</em>%<img src="../images/borrow_05.png" /></li>';
				}else{
					reward='<li class="li-02"><em>'+data.annualRate+'</em>%</li>';
				}
				thtml += reward;
	//			thtml += '<li class="li-02"><em>12</em>%</li>';
				thtml += '<li class="li-03"><em>'+data.cycleCount+'</em>个月</li>';
				thtml += '<li class="li-04"><em>'+fmoney(data.confirmBalance,2)+'</em>元</li>';
				thtml += '<li class="li-05"><div class="pro-group"><div class="pro-schedule" style="width:'+data.ratePercent+'%"></div></div><span class="rate">'+data.ratePercent+'%</span></li>';
				if(data.ratePercent==100){
					if(data.applicationState=='6'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">还款中</a></li></ul>';
					}else if(data.applicationState=='7'||data.applicationState=='8'){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已结清</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-deer-gray">已满标</a></li></ul>';
					}
				}else{
					if(!data.begin){
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">预热中</a></li></ul>';
					}else{
						thtml += '<li class="li-06"><a onclick="setWinName2()" href="'+rootPath+'/finance/bidding?loanApplicationNo='+data.loanApplicationId+'" class="btn btn-write">立即加入</a></li></ul>';
					}
				}
				
			}
			$('#loanlist-flcontext2').html("");
			$('#loanlist-flcontext2').html(thtml);
			bottomB();
		}
	});

}	


