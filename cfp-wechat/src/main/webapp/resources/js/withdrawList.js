// JavaScript Document
//require.js调用的主框架
require.config({
    urlArgs: "bust=" +  (new Date()).getTime(),
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"]
	}
})
require(['jquery',"main"],function($,main) {
    var pageNo=1,pageSize=20,totalPage;

    function searchHtml(page,rows){
        var now=dateTimeFormatter(new Date());
        now=now.substr(0,16);
        $.ajax({
            url:rootPath+"/person/withDrawList",
            type:"post",
            data: {
                "pageSize": rows,
                "pageNo": page
            },
            success: function (data) {
                var d_rows = data.rows;
                totalPage = data.totalPage;
                pageNo = data.currentPage;
                if(d_rows.length<pageSize){
                    $(".l_lastTip").html("");
                }
                for(var i=0;i<d_rows.length;i++){
                    var data = d_rows[i];
                    var time=data.createTime!=null?dateTimeFormatter(data.createTime):"";
                    time=time.substr(0,16);
                    time=calcDate(time,now);
                    var card=(data.cardNo==null?"":data.cardNo)+"";
                    card=card.substr(-4);
                    var bankName=data.shortBank;
                    if(bankName!=null&&bankName!=undefined&&bankName!=""){
                        if(bankName.length>4){
                            if(bankName=="中国邮政储蓄银行"){
                                bankName="邮政储蓄";
                            }else{
                                bankName=bankName.substr(0,4);
                            }
                        }
                    }else{
                        bankName="";
                    }
                    var status = "";
                    var color="style='color:#47a9db;'";
                    if(data.verifyStatus=='0'&&data.transStatus=='0'){
                        status='审核中';
                    }
                    if(data.verifyStatus=='2'){
                        status='审核驳回';
                        color="style='color:#f23655;'";
                    }
                    if(data.verifyStatus=='1'&&(data.transStatus=='0'||data.transStatus=='1'||data.transStatus=='2')){
                        status='审核通过待打款';
                    }
                    if(data.verifyStatus=='1'&&data.transStatus=='3'){
                        status='提现成功';
                    }
                    if(data.verifyStatus=='1'&&data.transStatus=='4'){
                        status='提现失败';
                        color="style='color:#f23655;'";
                    }
                    var logo="withdraw";
                    console.log(data.bankCode);
                    if(data.bankCode!=null){
                        logo=data.bankCode;
                    }
                    var html="<div class='recordListBox'>"+
                                "<p>"+
                                "<span class='recordLe'><img src='../images/banklogo/bankS/"+logo+".png' /><i><font>"+bankName+"</font>"+card+"</i></span>"+
                                    "<span class='recordRi'>"+time+"</span>"+
                                "</p>"+
                                "<p>"+
                                "<span class='recordsLe'>"+data.withdrawAmount.toFixed(2)+"<i>元</i></span>"+
                                "<span class='recordsRi' "+color+">"+status+"</span>"+
                                "</p>"+
                            "</div>";
                    $(".recordList").append(html);
                };
                var trs=$(".recordList").find("div[class='recordListBox']");
                if(trs.length==0){
                    $(".l_lastTip").next().show();
                }
            }
        });
    }

    $(function(){
        searchHtml(1,20);
        //滚动加载
		$(document).on("scroll",function() {
			if(main.scrollLoading("l_scrBox","l_NewScroll")) {
                pageNo+=1;
                if(pageNo<=totalPage){
                    searchHtml(pageNo,20);
                }else{
                    var records=$(".recordList").find("div[class='recordListBox']");
                    if(records.length>1){
                        $(".l_lastTip").html("已加载全部");
                    }
                }
			}
			
		})
	})
})
