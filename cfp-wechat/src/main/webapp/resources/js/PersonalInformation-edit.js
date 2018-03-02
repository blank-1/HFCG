// JavaScript Document
//require.js调用的主框架
require.config({
	paths:{
		"jquery":["lib/jquery-1.11.0.min"],
		"swal":["lib/sweetalert2"]
	}
})
require(['jquery','swal'],function($,swal) {
	$(function(){
		$("#remove-btn").on("click",function(){
			$("#name").val("");
		});
		$("#complete").on("click",function(){
			var nameVal = $("#name").val();
			if(nameVal == ""){
				swal({
				  text: '名称不能为空',
  				  timer: 2000,
  				  type:'warning'
				})
			}else if(nameVal.length > 20 || nameVal.length < 4){
				swal({
				  text: '请输入4~20位字符，支持汉字、字母、数字及"-"、"_组合',
				  type:'warning'
				});
			}else{
				swal({
				  title:'成功',
				  type:'success'
				})
			}
		})
	})
})
