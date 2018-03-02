var inviteURL="";
window.onload=function(){
	inviteURL = "http://m.caifupad.com/person/distribution?invite_code="+$("#incode").val();
	$.ajax({
		url:rootPath+"/user/getConfig",
		type:"post",
		success: function (data) {
			var _data = eval('('+data+')');
			//alert(_data.isSuccess);
			if(_data.isSuccess){
				//alert(timestamp+"==="+nonceStr+"==="+signature);
				var resultMap = _data.resultMap
				//alert(resultMap.timestamp);
				timestamp = resultMap.timestamp;
				nonceStr = resultMap.nonceStr;
				signature = resultMap.signature;
				inviteURL = "http://m.caifupad.com/person/distribution?invite_code="+$("#incode").val();
				/*if(resultMap.codeIsNull == '1'){
					inviteURL=inviteURL+'?invite_code=' + $("#shareCode").val();
				}*/
				wx.config({
				      debug: false,
				      appId: resultMap.appid,
				      timestamp: resultMap.timestamp,
				      nonceStr: resultMap.nonceStr,
				      signature: resultMap.signature,
				      jsApiList: [
				        'onMenuShareTimeline',
				        'onMenuShareAppMessage',
				        'hideMenuItems'
				      ]
				  });
			}
		}	
	});
}

wx.ready(function () {

	wx.onMenuShareAppMessage({
	   title: '“卖”友求钱，友谊不翻船！',
	   desc: '一起赚钱，一起吃蒜。好友投一笔，你就赚一笔 ',
	   link: inviteURL,
	   imgUrl: 'http://m.caifupad.com/images/distribution.jpg',
	   trigger: function (res) {
		   // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
		   //alert('用户点击发送给朋友');
	   },
	   success: function (res) {
	     //alert('已分享');
	   },
	   cancel: function (res) {
	     //alert('已取消');
	   },
	   fail: function (res) {
	     alert("分享失败");
	   }
	});
	//2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
	 wx.onMenuShareTimeline({
	   title: '“卖”友求钱，友谊不翻船！',
	   link: inviteURL,
	   imgUrl: 'http://m.caifupad.com/images/distribution.jpg',
	   trigger: function (res) {
	     // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
	     //alert('用户点击分享到朋友圈');
	   },
	   success: function (res) {
	     //alert('已分享');
	   },
	   cancel: function (res) {
	     //alert('已取消');
	   },
	   fail: function (res) {
	     alert("分享失败");
	   }
	 });
 
	 wx.hideMenuItems({
	     menuList: [
	       'menuItem:readMode', // 阅读模式
	       'menuItem:copyUrl', // 复制链接
	       'menuItem:share:QZone',//分享到QQ空间	
	       'menuItem:share:weiboApp',//分享到微博
	       'menuItem:openWithQQBrowser',//用QQ浏览器打开
	       'menuItem:openWithSafari',//用safari打开
	       'menuItem:share:facebook', //分享到FB
	       'menuItem:share:qq' //分享到qq
	     ],
	     success: function (res) {
	       //alert('已隐藏“阅读模式”，“分享到朋友圈”，“复制链接”等按钮');
	     },
	     fail: function (res) {
	       //alert(JSON.stringify(res));
	     }
	   });
});


wx.error(function (res) {
  //alert(res.errMsg);
 //window.location.reload();
});

