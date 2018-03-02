//³ö½èÏêÇé

window.onload=function(){
	var oBox=document.getElementById('my-glider');
	var aDiv=oBox.getElementsByTagName('div');	
	var oLeft=document.getElementById('prevLink');
	var oRight=document.getElementById('nextLink');
	var timer=null;
	var index=0;
	oLeft.onclick=function(){
		index++;
		for(var i=0;i<aDiv.length;i++){
			aDiv[i].style.display='none';
			}
		if(index==aDiv.length){
			index=0;
			}
		aDiv[index].style.display='block';
		};
	oRight.onclick=function(){
		index--;
		for(var i=0;i<aDiv.length;i++){
			aDiv[i].style.display='none';
			}
		if(index==-1){
			index=aDiv.length-1;
			}
		aDiv[index].style.display='block';
		};
		
		
	var oBox1=document.getElementById('my-glider2');
	var aDiv1=oBox1.getElementsByTagName('div');	
	var oLeft1=document.getElementById('prevLink2');
	var oRight1=document.getElementById('nextLink2');
	var timer=null;
	var index1=0;
	oLeft1.onclick=function(){
		index1++;
		for(var i=0;i<aDiv1.length;i++){
			aDiv1[i].style.display='none';
			}
		if(index1==aDiv1.length){
			index1=0;
			}
		aDiv1[index1].style.display='block';
		};
	oRight1.onclick=function(){
		index1--;
		for(var i=0;i<aDiv1.length;i++){
			aDiv1[i].style.display='none';
			}
		if(index1==-1){
			index1=aDiv1.length-1;
			}
		aDiv1[index1].style.display='block';
		};
	};