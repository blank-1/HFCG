//邀请好友查询
$(document).ready(function() {
	searchHtml(1, 10, true);
})
function searchHtml(page, rows, flag) {
	var thtml = "";
	$.ajax({
		url : rootPath + "/person/invite_friends_list",
		type : "post",
		data : {
			"pageSize" : rows,
			"pageNo" : page
		},
		success : function(data) {
			var d_rows = data.rows;
			var pageCount = data.totalPage;
			
			//new html
			thtml +='<div class="in_friends_bot">';
			thtml +='<div class="p-Right-top"><p class="yqhy-list-title">我邀请注册成功的好友</p></div>';
			thtml +='<ul class="user_success_title"><li>用户名</li><li>注册时间</li><li>邀请方式</li><li>平台奖励</li></ul>';
			
			thtml +='<ul class="user_success_con">';
			
			for (var j = 0; j < d_rows.length; j++) {
				if(j % 2==0){
					thtml +='<li class="friend_ul_li">';
				}else{
					thtml +='<li class="friend_ul_li">';
				}
				thtml +='<ul><li>' + d_rows[j].loginName + '</li><li class="yq_ptjl">' + dateTimeFormatter(d_rows[j].createTime) + '</li><li>链接邀请</li><li class="yq_ptjl">--</li></ul>'	
				thtml +='</li>';
			}
			thtml +='</ul>';
			
			
	
			$("#yqlist").html(thtml);
			bottomB();
			if (flag) {
				$(".tcdPageCode").createPage({
					pageCount : pageCount,
					current : 1,
					backFn : function(p) {
						// 点击分页效果
						searchHtml(parseInt(p), 10, false);
					}
				});
			}
		}
	});
}