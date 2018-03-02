//wangyadong 页面加载
//声明出借债权明细列表
 
var pageNo=1,pageSize=10,totalPage; 

var jhlist=new Array();
//出借列表详细
var ylist=new Array();
$(function(){
	var fisrtList = "";
	var SecondList = "";
	inItByListDate(pageNo,pageSize);
});
function init(){//初始化函数
	changePage()
}

//tab切换
function changePage(){
	var list=$("#list"),
		pages=$("section ul");
	if($("#flag1").val()==1){
		$("#curById1").addClass("cur").siblings().removeClass("cur");
		pages.eq($("#curById1").index()).show().siblings().hide()
//		$(this).addClass("cur").siblings().removeClass("cur");
//		pages.eq($(this).index()).show().siblings().hide()
	}
	list.on("click","li",function(){
		$(this).addClass("cur").siblings().removeClass("cur");
		pages.eq($(this).index()).show().siblings().hide()
	})
}
 
 
init()
//
function inItByListDate(){
//	inItByListDate(pageNo,pageSize);
	financeHtml(pageNo,pageSize,jhlist,ylist);
}
	//查询出借债权明细
	function financeHtml(page,rows,flist,ylist){
		flist = new Array();
		ylist=new Array();
		//组织参数
		var creditorRightsStatus = "0-1-2-3";
		$.ajax({
			url:rootPath+"/finance/getCreditRightList",
			type:"post",
			data: {
				"pageSize": rows,
				"pageNo": page,
				"creditorRightsStatus": creditorRightsStatus,
			},
			success: function (data) {
				var d_rows = data.rows;
				 totalPage = data.totalPage,
		            pageNo = data.currentPage;
				for(var i=0;i<d_rows.length;i++){
					var data = d_rows[i];
					var list = data.rightsRepaymentDetailList;
					flist[i]=[data.creditorRightsName,data.loanLoginName,fmoney(data.buyPrice,2),
					          fmoney(data.waitTotalpayMent,2),fmoney(data.factBalance,2),
					          getDateStr(new Date(data.currentPayDate)),showCreditoRightStatus(data.rightsState),
					          showTurnStatus(data.turnState), getDateStr(new Date(data.buyDate)),"<img src='../images/up.jpg' />","index.do?financeId=1",
					          data.loanApplicationListVO,fmoney(data.expectProfit,2),data.creditorRightsCode];
					ylist[i] = new Array();
					for(var j=0;j<list.length;j++){
						var detail = list[j];
						var capital = fmoney(detail.shouldCapital2,2);
						var interest = fmoney(detail.shouldInterest2,2);
						var dInterest = fmoney(detail.defaultInterest,2);
						var fee = fmoney(detail.shouldFee,2);
						var allBackMoney =  fmoney(detail.shouldCapital2+detail.shouldInterest2+detail.defaultInterest-detail.shouldFee,2);
						var factMoney = fmoney(detail.factBalance+detail.depalFine,2);
						ylist[i][j] = [detail.sectionCode,getDateStr(new Date(detail.repaymentDayPlanned)),capital,interest,dInterest,fee,allBackMoney,factMoney,repaymentDetailStatus(detail.rightsDetailState),"index.do?financeId=1"];
					}
				}
				//页面渲染
				render(flist,ylist);
			}
		});
	}
	
	//渲染页面
	function render(flist,ylist){
		//先判断状态为为1（假设1为已投中）渲染第一个列表  ，其余的都是全部记录这个列表，在分别判断不同的状态
		var thtml="";
		var html="";
		var cssStyle = '';
		SecondList =ylist;
		fisrtList=flist;
		var myDate = new Date();
		var currentMonths = parseInt(Math.abs(myDate.getMonth()+1))
		s1  =  myDate.getFullYear()+"-"+currentMonths+"-"+myDate.getDate();
		//如果到期时间大于等于 当前时间
		for(var i=0;i<flist.length;i++){
			if(flist[i][6] === "已生效" ){
				flist[i][6]="回款中";
				cssStyle = 'bid';
			}else if(flist[i][6] === '还款中'){
				flist[i][6]="回款中";
				cssStyle = 'bid';
			}else if(flist[i][6] === '已结清'){
				cssStyle = 'bid1';
			}else if(flist[i][6] === '提前结清'){
				cssStyle = 'bid2';
			} 
			var listlenght = SecondList[i].length-1 ;
			if(SecondList[i][listlenght]==undefined){
				continue;
			}
			s2  =  SecondList[i][listlenght][1] ;
		    var  j =i;
		    var ckeckArr = [flist[i][6],flist[i][0],flist[i][11].cycleCount,flist[i][2],flist[i][12]];
			if(flist[i][6] == '回款中'){
				thtml += getFinancingRecord(i,ckeckArr,cssStyle);
			}
				html  += getFinancingRecord(i,ckeckArr,cssStyle);
		   }
		 
		$("#listByFinanceByIng").append(thtml);
		$("#listByFinanceBy").append(html);
		
		if($("#listByFinanceByIng").has("li").length == 0){
			$("#listByFinanceByIng").html('<div class="l_noData"><img src="'+rootPath+'/images/icon_noData.png"><p>还没有记录</p><a href="'+rootPath+'/finance/list">去理财</a></div>');
		}
		if($("#listByFinanceBy").has("li").length == 0){
			$("#listByFinanceBy").html('<div class="l_noData"><img src="'+rootPath+'/images/icon_noData.png"><p>还没有记录</p><a href="'+rootPath+'/finance/list">去理财</a></div>');
		}
	}
	//拼装理财列表页面  王亚东  当前时间--> 到期时间
	 function getFinancingRecord(i,FinancingRecord,cssStyle){
		 var htmls="";
		 if(FinancingRecord[0] === '已结清'){
			var day= DateDiff(s1,s2);
//			if(day=='78'){
			 htmls = '<li><dl onclick="getDetailByflist('+i+');"><dt class='+cssStyle+'> <h1 style="line-height: 5rem;">'+FinancingRecord[0]+'</h1><p id="howLong'+i+'">'+
				'<dd> <div class="topContent">'+FinancingRecord[1]+' <p> <span> 期限'+FinancingRecord[2]+'月</span></div><div class="bottomContent">'+
				'<div class="leftContent"><p>在投金额</br><span>'+FinancingRecord[3]+'元</span></p></div><div class="rightContent">'+
				'<p>预期收益</br><span>'+FinancingRecord[4]+'元</span></p></div></div> </dd></dl>'
				+'</li>';
			 return htmls;
//		 }
		 }
		 var dayStr="";
		 if(DateDiff(s1,s2)==0){
			 dayStr="<p id='howLong'"+i+">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p><p><span>今天到期</span></p>";
		 }else{
			 dayStr="<p id='howLong'"+i+">距到期</p><p><span>"+DateDiff(s1,s2)+"</span>天</p>";
		 }
		 htmls = '<li><dl onclick="getDetailByflist('+i+');"><dt class='+cssStyle+'> <h1>'+FinancingRecord[0]+'</h1>'+
		 			dayStr+'</dt>'+ 
			'<dd> <div class="topContent">'+FinancingRecord[1]+' <p> <span> 期限'+FinancingRecord[2]+'月</span></div><div class="bottomContent">'+
			'<div class="leftContent"><p>在投金额</br><span>'+FinancingRecord[3]+'元</span></p></div><div class="rightContent">'+
			'<p>预期收益</br><span>'+FinancingRecord[4]+'元</span></p></div></div> </dd></dl>'
			+'</li>';
		 return htmls;
	 }
	 
		//格式化金额
		function fmoney(s, n) {  
			n = n > 0 && n <= 20 ? n : 2;  
			s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
			var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];  
			t = "";  
			for (i = 0; i < l.length; i++) {  
				t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
			}  
			return t.split("").reverse().join("") + "." + r;  
		}

	
	function getDetailByflist(_index){
		var imagePath ='';
		var beginTime = '';
		console.log(fisrtList[_index]);
		if(fisrtList[_index][6] === "已生效" ){
			imagePath = '<img id="moneyicon" src='+rootPath+'/images/iconTou.png'+' />';
		}else if(fisrtList[_index][6] === '还款中'){
			imagePath = '<img id="moneyicon" src='+rootPath+'/images/iconTou.png'+' />';
		}else if(fisrtList[_index][6] === '已结清'){
			imagePath = '<img id="moneyicon" src='+rootPath+'/images/iconQing.png'+' />';
		}else if(fisrtList[_index][6] === '提前结清'){
			imagePath = '<img id="moneyicon" src='+rootPath+'/images/icon_tiqian.png'+' />';
		} 
		var myDate = new Date();
		var currentMonths = parseInt(Math.abs(myDate.getMonth()+1))
		s1  =  myDate.getFullYear()+"-"+currentMonths+"-"+myDate.getDate();
		
		
		var num =parseFloat("0.00");
		var  yishou = '';
		var count ='';
		for(var j =0;j<SecondList[_index].length;j++){
			if(SecondList[_index][j][8] === '已还清'){
				var temp = parseFloat(SecondList[_index][j][3].replace(/,/g,"")) * '100';
				num +=temp;
				count = num / '100';
//				num +=SecondList[_index][j][3];
//				alert("已还清"+num);
//				console.log("第"+j+"期的收益="+SecondList[_index][j][3]);
//				console.log("已还清"+count);
//				console.log('74' * '3'/'100');
//				count +=parseInt(SecondList[_index][j][3]);
//				console.log("INT类型的第"+j+"期的收益="+SecondList[_index][j][3]);
//				console.log("INT类型的已还清"+count);
			}
//			if("yes"==DateDiffBigOrSmall(s1,SecondList[_index][j][1])){//当前时间大于等于 计算收益
//				num +=SecondList[_index][j][3];
//			}
			}
			if(''==count){ //等于空则等于首月收益
				count =parseFloat(fisrtList[_index][12].replace(/,/g,""));
				yishou ='预期收益:';
			}else{
				yishou ='已获收益:';
			}
		var html='<div class="topmoneyinfo">'
				+'<ul class="leftlist">'
				+ imagePath
				+'<span style="display:block; line-height:1.8rem; font-size:1.4rem; clear:both;" class="yellow">'
			    +fisrtList[_index][0]+'</br></span>'
				+'<li>年化收益：<span>'+fisrtList[_index][11].annualRate+'%</span></li>'
				+'<li>借款时长：<span>'+fisrtList[_index][11].cycleCount+'</span></li>'
				+'<li>还款方式：<span>'+fisrtList[_index][11].repayMentMethod+'</span></li>'
				+'</ul>'
				+' <ul class="rightlist">'
				+'<li>投资金额：</li><li style="color:#f4536e;font-size:2rem;">'+fisrtList[_index][2]+'元</li><br>' 
		
				+' <li>'+yishou+'</li><li style="color:#f4536e;font-size:2rem;">'+fmoney(count,2)+'元</li>'
				+'</ul>'
				+'</div>'
				//第一段CSS
				+'  <div id="dataAll" class="dataAll">'
				+' <ul class="dataLeft"> <li>回款期</li><li>回款日期</li>'
			    +'<li>应回本金（元）</li><li>应回利息（元）</li><li>罚息（元）</li>'
			    +'<li>应缴费用（元）</li><li>应回款总额（元）</li><li>已回款总额（元）</li><li>状态</li></ul>'
			    +'<div class="dataRight">'
				for(var j =0;j<SecondList[_index].length;j++){
					 
				html =html +'<ul><li>'+SecondList[_index][j][0]+'</li>'
					       +'<li>'+SecondList[_index][j][1]+'</li>'
					       +'<li>'+SecondList[_index][j][2]+'</li>'
					       +'<li>'+SecondList[_index][j][3]+'</li>'
					       +'<li>'+SecondList[_index][j][4]+'</li>'
					       +'<li>'+SecondList[_index][j][5]+'</li>'
					       +'<li>'+SecondList[_index][j][6]+'</li>'
					       +'<li>'+SecondList[_index][j][7]+'</li>'
					       +'<li>'+SecondList[_index][j][8]+'</li></ul>'
					     
				}
				html =html+'</div></div>';
		
		$.ajax({
			url:rootPath+"/finance/SendCreditRightList",
			type:"post",
			data: {
				"bangDingDetails": html,
				"beginTime":beginTime
			},
			success: function (){
                window.location.href=rootPath+"/finance/SendCreditRightListByShowPage" ;  
			}
			,error: function(){
				window.location.href=rootPath+"/finance/SendCreditRightListByShowPage" ;  
			}
		});
	}

	   //计算天数差的函数，通用    王亚东
	   function  DateDiff(DateOne,  DateTwo, _indexOf){   
		    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));  
		    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);  
		    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));  
		  
		    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));  
		    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);  
		    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));  
		   //如果当前时间大于到期时间  则不显示
		    if((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)> Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))){
//		    	$("#howLong"+_indexOf).hide();
		    	return "已过期";
		    }
		    var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);   
		    return Math.abs(cha);
	   }
	   
	   //计算当前时间和还款时间的大小    王亚东
	   function  DateDiffBigOrSmall(DateOne,  DateTwo){   
		    var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));  
		    var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);  
		    var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));  
		  
		    var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));  
		    var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);  
		    var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));  
		   //如果当前时间大于到期时间  则不显示
		    if((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)> Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))){
		    	return "yes";
		    }
		   
		    return  "no";
	   } 
	function showTurnStatus(v) {
		switch (v) {
			case '0':
				return "未申请";
				break
			case '1':
				return "申请转出";
				break
			case '2':
				return "已转让";
				break
		}
	}
	
	function repaymentDetailStatus(v){
		switch (v) {
			case '0':
				return "未还款";
				break
			case '1':
				return "部分还款";
				break
			case '2':
				return "已还清";
				break
			case '3':
				return "逾期";
				break
			case '4':
				return "提前还款";
				break
			case '5':
				return "已转出";
				break
			case '6':
				return "平台垫付利息";
				break
		}

	}

	function showCreditoRightStatus(v) {
		switch (v) {
			case '0':
				return "已生效";
				break
			case '1':
				return "还款中";vb
				break
			case '2':
				return "已转出";
				break
			case '3':
				return "已结清";
				break
			case '4':
				return "已删除";
				break
			case '5':
				return "申请转出";
				break
			case '6':
				return "已转出(平台垫付)";
				break
			case '7':
				return "提前结清";
				break
		}
	}
	$('#straining').scroll(function(){
		var parentH=$("#straining ul").height(),
			lastTop=$("#straining ul li:last").offset().top,
			winH=$(window).height();
			if(lastTop<=winH){
				pageNo+=1;
			    if(pageNo<=totalPage){
//			    	pageNo+=1;
			    	financeHtml(pageNo,10,jhlist,ylist);
			    	return false;
			    }
			}
	})
