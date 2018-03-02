window.onload=function(){
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
				//alert(timestamp+"==="+nonceStr+"==="+signature);
				wx.config({
				      debug: false,
				      appId: resultMap.appid,
				      timestamp: resultMap.timestamp,
				      nonceStr: resultMap.nonceStr,
				      signature: resultMap.signature,
				      jsApiList: [
				        'onMenuShareTimeline',
				        'onMenuShareAppMessage',
				        'hideAllNonBaseMenuItem',
				        'hideMenuItems',
				        'showAllNonBaseMenuItem'
				      ]
				  });
			}
		}	
	});
}

/*
 * 注意：
 * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
 * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
 * 3. 完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
 *
 * 如有问题请通过以下渠道反馈：
 * 邮箱地址：weixin-open@qq.com
 * 邮件主题：【微信JS-SDK反馈】具体问题
 * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
 */
wx.ready(function () {
  // 1 判断当前版本是否支持指定 JS 接口，支持批量判断
  
//2. 分享接口
//2.1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
 wx.onMenuShareAppMessage({
   title: '感谢你们的陪伴，成就今天的我们。',
   desc: '财富派感恩月，戳我领取感恩回馈',
   link: 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd06957f9d3daac89&redirect_uri=http%3a%2f%2fm.caifupad.com%2fgame%2ftoWechatMilepost%2f&response_type=code&scope=snsapi_base&state=1#wechat_redirect',
   imgUrl: 'http://m.caifupad.com/gameimg/anniversary/share_icon_2.png',
   trigger: function (res) {
     // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
     //alert('用户点击发送给朋友');
   },
   success: function (res) {
     //alert('已分享');
     //window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd06957f9d3daac89&redirect_uri=http%3a%2f%2fm.caifupad.com%2fgame%2ftoRoulette&response_type=code&scope=snsapi_base&state=0#wechat_redirect";
   },
   cancel: function (res) {
     //alert('已取消');
   },
   fail: function (res) {
     alert(JSON.stringify(res));
   }
 });
//2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
 wx.onMenuShareTimeline({
   title: '感谢你们的陪伴，成就今天的我们。',
   link: 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd06957f9d3daac89&redirect_uri=http%3a%2f%2fm.caifupad.com%2fgame%2ftoWechatMilepost%2f&response_type=code&scope=snsapi_base&state=1#wechat_redirect',
   imgUrl: 'http://m.caifupad.com/gameimg/anniversary/share_icon_2.png',
   trigger: function (res) {
     // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
     //alert('用户点击分享到朋友圈');
   },
   success: function (res) {
     //alert('已分享');
     //window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd06957f9d3daac89&redirect_uri=http%3a%2f%2fm.caifupad.com%2fgame%2ftoRoulette&response_type=code&scope=snsapi_base&state=0#wechat_redirect";
   },
   cancel: function (res) {
     //alert('已取消');
   },
   fail: function (res) {
     //alert(JSON.stringify(res));
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

});

