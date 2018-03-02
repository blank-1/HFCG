//分页插件
/**
2014-08-05 ch
**/
(function($){
	var ms2 = {
		init2:function(obj2,args2){
			return (function(){
				ms2.unbindEvent2(obj2, args2);
				ms2.fillHtml2(obj2,args2);
				ms2.bindEvent2(obj2,args2);
			})();
		},
		//填充html
		fillHtml2:function(obj2,args2){
			return (function(){
				obj2.empty();
				//上一页
				if(args2.current2 > 1){
					obj2.append('<a href="javascript:;" class="prevPage2 firstpage2"></a>');
				}else{
					obj2.remove('.prevPage2');
					obj2.append('<span class="disabled2 firstpage2" ></span>');
				}
				//中间页码
				if(args2.current2 != 1 && args2.current2 >= 4 && args2.pageCount2 != 4){
					obj2.append('<a href="javascript:;" class="tcdNumber2">'+1+'</a>');
				}
				if(args2.current2-2 > 2 && args2.current2 <= args2.pageCount2 && args2.pageCount2 > 5){
					obj2.append('<span>...</span>');
				}
				var start = args2.current2 -2,end = args2.current2+2;
				if((start > 1 && args2.current2 < 4)||args2.current2 == 1){
					end++;
				}
				if(args2.current2 > args2.pageCount2-4 && args2.current2 >= args2.pageCount2){
					start--;
				}
				for (;start <= end; start++) {
					if(start <= args2.pageCount2 && start >= 1){
						if(start != args2.current2){
							obj2.append('<a href="javascript:;" class="tcdNumber2">'+ start +'</a>');
						}else{
							obj2.append('<span class="current2">'+ start +'</span>');
						}
					}
				}
				if(args2.current2 + 2 < args2.pageCount2 - 1 && args2.current2 >= 1 && args2.pageCount2 > 5){
					obj2.append('<span>...</span>');
				}
				if(args2.current2 != args2.pageCount2 && args2.current2 < args2.pageCount2 -2  && args2.pageCount2 != 4){
					obj2.append('<a href="javascript:;" class="tcdNumber2">'+args2.pageCount2+'</a>');
				}
				//下一页
				if(args2.current2 < args2.pageCount2){
					obj2.append('<a href="javascript:;" class="nextPage2 endPage2"></a>');
				}else{
					obj2.remove('.nextPage2');
					obj2.append('<span class="disabled2 endPage2"></span>');
				}
			})();
		},
		//解绑事件
		unbindEvent2:function(obj2,args2){
			return (function(){
				obj2.off("click","a.tcdNumber2");
				//上一页
				obj2.off("click","a.prevPage2");
				//下一页
				obj2.off("click","a.nextPage2");
			})();
		},
		//绑定事件
		bindEvent2:function(obj2,args2){
			return (function(){
				obj2.on("click","a.tcdNumber2",function(){
					var current2 = parseInt($(this).text());
					ms2.fillHtml2(obj2,{"current2":current2,"pageCount2":args2.pageCount2});
					if(typeof(args2.backFn2)=="function"){
						args2.backFn2(current2);
					}
				});
				//上一页
				obj2.on("click","a.prevPage2",function(){
					var current2 = parseInt(obj2.children("span.current2").text());
					ms2.fillHtml2(obj2,{"current2":current2-1,"pageCount2":args2.pageCount2});
					if(typeof(args2.backFn2)=="function"){
						args2.backFn2(current2-1);
					}
				});
				//下一页
				obj2.on("click","a.nextPage2",function(){
					var current2 = parseInt(obj2.children("span.current2").text());
					ms2.fillHtml2(obj2,{"current2":current2+1,"pageCount2":args2.pageCount2});
					if(typeof(args2.backFn2)=="function"){
						args2.backFn2(current2+1);
					}
				});
			})();
		}
	}
	$.fn.createPage2 = function(options2){
		var args2 = $.extend({
			pageCount2 : 10,
			current2 : 1,
			backFn2 : function(){}
		},options2);
		ms2.init2(this,args2);
	}
})(jQuery);

//代码整理：懒人之家 www.lanrenzhijia.com