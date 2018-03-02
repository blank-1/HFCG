// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"main" : ["main"]
	}
})
require(['jquery',"main"],function($,main) {
	
	var pageNo = 1, pageSize = 10, totalPage;//分页变量
	
	function searchHtml(page,rows){
        $.ajax({
            url:rootPath+"/notice/noticeList",
            type:"post",
            data: {
                "pageSize": rows,
                "pageNo": page
            },
            success: function (data) {
                var d_rows = data.rows;
                totalPage = data.totalPage;
                pageNo = data.currentPage;
                $(".l_lastTip").remove();
                for(var i=0;i<d_rows.length;i++){
                    var data = d_rows[i];
                    
                    var time=data.publishTime!=null?dateTimeFormatter(data.publishTime):"";
                    time=calcDate(time,dateTimeFormatter(new Date()));
                    
                    var html="<dl class='newsList' onclick='toDetail("+data.noticeId+");'>"+
								 "<dd>"+
									 "<div class='imgBox'>"+
									 	"<img src='../images/icon/news_01.png' alt=''>"+
									 "</div>"+
							     "</dd>"+
							     "<dt>"+
									 "<p class='title'>"+
									 	"<span>"+data.noticeTitle+"</span>"+
									 "</p>"+
									 "<div class='text-overflow'>"
									 	+data.noticeSynopsis+
									 "</div>"+
									 "<div class='Notice'>"+
										 "<i>公告</i>"+
										 "<i class='timer'>"+time+"</i>"+
									 "</div>"+
								 "</dt>"+
							 "</dl>";
                    
                    $("#noticeList").append(html);
                };
                
                if(d_rows.length == 0){
                	var noData = "<div class='messageImg'>" +
					  "<img src='../images/messageIcon.png' >" +
		              "<p class='imgTip'>运营偷懒了，暂无公告</p>" +
		              "</div>";
                	$("#noticeList").append(noData);
                }else{
                	if(pageNo==totalPage){
                    	$("#noticeList").append("<p class='l_lastTip'>已加载全部</p>");
                    }else{
                    	$("#noticeList").append("<p class='l_lastTip'>向下滑动加载更多</p>");
                    }
                }
            }
        });
    }
	
	$(function(){
		//tab切换
		$(".headNav").on("click","li",function(){
			var num = $(this).index();
			$(".headNav li").eq(num).addClass("cur").siblings().removeClass("cur");
			$(".couponList>div").eq($(".headNav>li").index(this)).show().siblings("div").hide();
		})
		
		searchHtml(pageNo,pageSize);
		
		//滚动加载
		$("#noticeList_div").on("scroll",function() {
			var scrollTop = $(this).find("div#noticeList").outerHeight(true), 
			selfH = $(this).scrollTop() + $(window).height();
			//console.log(scrollTop + '---' + selfH);
			if (scrollTop <= selfH) {
				//分页数据
				pageNo+=1;
	            if(pageNo<=totalPage){
	            	searchHtml(pageNo,pageSize);
	            }
			}
			
		});

		function getUrlParam(name) { 
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
			var r = window.location.search.substr(1).match(reg); //匹配目标参数
			if (r != null) return unescape(r[2]); return null; //返回参数值
		}
		var get_ind = getUrlParam("news_list");
		if(get_ind){
			$(".headNav li").eq(get_ind).addClass("cur").siblings().removeClass("cur");
			$(".couponList>div").eq(get_ind).show().siblings("div").hide();
			console.log(get_ind);
		}
	})
})
