// JavaScript Document
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"publicJS":["main"],
		"swal":["lib/sweetalert2"],
	}
})
require(['jquery','publicJS','swal'],function($,publicJS,swalr) {
	$(function() {
			var overscroll = function(el) {
			el.addEventListener('touchstart', function() {
				var top = el.scrollTop
					, totalScroll = el.scrollHeight
					, currentScroll = top + el.offsetHeight
				if(top === 0) {
					el.scrollTop = 1
				} else if(currentScroll === totalScroll) {
					el.scrollTop = top - 1
				}
			})
			el.addEventListener('touchmove', function(evt) {
				//if the content is actually scrollable, i.e. the content is long enough
				//that scrolling can occur
				if(el.offsetHeight < el.scrollHeight)
					evt._isScroller = true
			})
		}
		overscroll(document.querySelector('.l_NewScroll'));
		document.body.addEventListener('touchmove', function(evt) {
			//In this case, the default behavior is scrolling the body, which
			//would result in an overflow.  Since we don't want that, we preventDefault.
			if(!evt._isScroller) {
				evt.preventDefault()
			}
			})
		$("header p").on("click",function(){
			$(this).addClass("l_focus").siblings().removeClass("l_focus");
			if ($(this).index()>0) {
				$(".l_shengList").show().siblings("section").hide();
			}else {
				$(".l_shengTip").show().siblings("section").hide();
			}
		})
			var pageNo=1,rows=10,pageCount=0;
		$(document).on("scroll",function(){
			console.log(1)
			if(!$(".l_shengList").is(":hidden")){
					if(publicJS.scrollLoading("l_shengList","l_NewScroll")){
						pageNo=pageNo+1;
						if(pageNo<=pageCount)
						searchHtml(pageNo,rows);
					}
			}
		})
		
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
		//格式化时间
		function dateTimeFormatter(val) {

		    if (val == undefined || val == "")
		        return "";
		    var date;
		    if (val instanceof Date) {
		        date = val;
		    } else {
		        date = new Date(val);
		    }
		    var y = date.getFullYear();
		    var m = date.getMonth() + 1;
		    var d = date.getDate();

		    var h = date.getHours();
		    var mm = date.getMinutes();
		    var s = date.getSeconds();

		    var dateStr = y + '-' + (m < 10 ? ('0' + m) : m) + '-'
		        + (d < 10 ? ('0' + d) : d);
		    var TimeStr = (h < 10 ? ("0" + h) : h) + ':' + (mm < 10 ? ('0' + mm) : mm) + ':'
		        + (s < 10 ? ('0' + s) : s);
		    return dateStr + ' ' + TimeStr;
		}

	
			//省心投资列表首次查询
			function searchHtml(page,rows){
			    var thtml="";
			    $.ajax({
			      url:rootPath+"/finance/getSXJHLender",
			      type:"post",
			      data: {
			        "pageSize": rows,
			        "pageNo": pageNo,
			        "lendProductPublishId": $("#lendProductPublishId").val()
			      },
			      success: function (data) {
			         var d_rows = data.rows;
			         pageCount = data.totalPage;
			         var pageDesc="";
			         
			         if (pageCount == pageNo) {
			         	if(1!=pageNo){
                            pageDesc="<p class='l_lastTip'>数据已加载完成</p>";
						}

			        	 if(data.rows=="")
				        	 pageDesc="<p class='l_lastTip'>暂无数据</p>";
						} else {
						 pageDesc="<p class='l_lastTip'>向下滑动加载更多</p>";
						} 
			         
			         
			         $(".l_lastTip").remove();
			         for(var i=0;i<d_rows.length;i++){
			        	 thtml +="<li> <p>"+d_rows[i].lenderName+"</p> <p>"+dateTimeFormatter(d_rows[i].lendTime)+"</p> <p>"+fmoney(d_rows[i].lendAmount,2)+"</p> </li>";
			         }
			        $("#shenglist").append(thtml+pageDesc);
			      }
			    });
		  }
			searchHtml(pageNo,rows);
	})

//js结尾
})
