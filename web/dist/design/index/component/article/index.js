(function(){var D={init:function(){var t=this;t.$article=$(".module[data-type=article]");for(var n=0,s=t.$article.length;n<s;n++){var i=t.$article.eq(n);t.getAjaxArticle(i)}},getAjaxArticle:function(t){var n=this,s=t.attr("data-article");if(!s)return!1;var i=JSON.parse(s);i.item=t,$.ajax({type:"get",url:"/api/disk/firstHomepageDetail?projectid=12",data:{InfoTypeId:i.classify[i.classify.length-1],beginNum:i.startNum,endNum:i.endNum,isAll:i.isAll},contentType:"application/json",dataType:"json",success:function(r){r.success&&n.renderArticle(i,r.data)},error:function(r){console.log("\u63A5\u53E3\u8BF7\u6C42\u5931\u8D25")}})},getTimeString:function(t,n){if(new Date(parseInt(t))=="Invalid Date"&&new Date(parseInt(t)*1e3)=="Invalid Date")return"--";var s=new Date(parseInt(t)),i=s.getFullYear(),r=s.getMonth()+1,l=s.getDate();switch(n){case"DD":return l;case"YYYY/MM":return i+"/"+r;default:return i+"-"+r+"-"+l}},renderArticle:function(t,n){var s=this,i="",r,l=0,c=n.length,a=n[0];if(!a)return!1;var g=(window.cdnPath||"")+(a.computerPicPath?JSON.parse(a.computerPicPath).sourcePath||JSON.parse(a.computerPicPath)[0].sourcePath:"/design/designstatic/common/image/information/defult.jpg"),p=function(e){return location.port=="81","/pubinfo/"+e+".shtml"},I=parseInt(t.sTitle||0)==1,T=parseInt(t.sDate||0)==1,b=parseInt(t.sTeacher||0)==1,T=parseInt(t.sDate||0)==1,h=function(e){return t.sTitle?'<p><span class="title moreTitle">'+e.title+"</span></p>":'<p><span class="title">'+e.title+'</span></p><p class="introduce" title="'+e.introduce+'">'+e.introduce+"</p>"},u=function(e){return t.sTeacher?'<p class="teacher">'+e.realName||e.nickname+"</p>":""},f=function(e){return t.sDate?'<span class="sDate">'+s.getTimeString(e.gmtCreate)+"</span>":""},d=function(e){return t.sNum?'<span class="sNum"><i class="iconfont icon-eye"></i>'+e.viewCount+"</span>":""};switch(t.styleType){case"xtwlb":r=function(e){return'<li>   <a href="'+p(e.infoID)+'">     <div class="coverBox">       <div class="maskBox">         <img src="'+e.avatar+'"/>         <div class="coverMask">'+e.introduce+'</div>       </div>     </div>     <p class="title" title="'+e.title+'">'+e.title+"</p>"+u(e)+'     <p class="info">'+d(e)+f(e)+"     </p>   </a> </li>"};break;case"twlb":r=function(e){return'<li>   <a href="'+p(e.infoID)+'">    <div class="coverBox">         <img src="'+(window.cdnPath||"")+e.computerPicPath+'"/>    </div>    <div class="rightBox">'+h(e)+u(e)+'      <p class="info">'+d(e)+f(e)+"      </p>    </div>    </a>  </li>"};break;case"btrqlb":r=function(e){return parseInt(t.sTitle)==1?'<li><a href="'+p(e.infoID)+'"><div class="rightBox">  <p><span class="title">'+e.title+"</span></p></div></a></li>":'<li><a href="'+p(e.infoID)+'"><div class="rightBox">'+h(e)+u(e)+'  <p class="info">'+d(e)+f(e)+"  </p></div></a></li>"};break;case"new3":l=1,c=c>6?6:c,i='<a href="/pubinfo/'+a.infoID+'.shtml"> <div class="topBox">        <div class="leftbox">           <img src="'+g+'">        </div>        <div class="rightbox">          <div class="headBox">            <div class="title">'+a.title+'</div>            <div class="time">'+(parseInt(t.sDate)==1?'<span class="sDate">'+s.getTimeString(n.gmtCreate)+"</span>":"")+'</div>          </div>              <div class="conBox">            <p class="info">'+a.introduce+"</p>          </div>        </div>     </div>     </a>",r=function(e){return'<li >  <a href="/pubinfo/'+e.infoID+'.shtml">   <div class="conBox">    <img src="'+e.computerPicPath+'">    <p class="title">'+e.title+'</p>    <p class="time">'+(parseInt(t.sDate)==1?'<span class="sDate">'+s.getTimeString(e.gmtCreate)+"</span>":"")+"    </p>   </div>  </a> </li>"};break;case"new4":l=1,c=c>5?5:c,i='<div class="leftbox">  <a href="/pubinfo/'+a.infoID+'.shtml">      <div class="top">        <img src="'+g+'">      </div>      <div class="conbox">        <div class="left">'+(parseInt(t.sDate)==1?'<span class="sDate"><span class="dateTop">'+s.getTimeString(a.gmtCreate,"DD")+'</span><span class="dateBottom">'+s.getTimeString(a.gmtCreate,"YYYY/MM")+"</span></span>":"")+'          </div>        <div class="right">        <div class="title">'+a.title+'</div>            <p class="info">'+a.introduce+"</p>        </div>      </div>     </a>    </div>",r=function(e){return'<li>  <a href="/pubinfo/'+e.infoID+'.shtml">    <div class="conbox">      <div class="left">'+(parseInt(t.sDate)==1?'<span class="sDate"><span class="dateTop">'+s.getTimeString(e.gmtCreate,"DD")+'</span><span class="dateBottom">'+s.getTimeString(e.gmtCreate,"YYYY/MM")+"</span></span>":"")+'      </div>      <div class="right">        <div class="title">'+e.title+'</div>        <p class="info">'+e.introduce+"</p>      </div>    </div>  </a></li>"};break;default:}for(var m=document.createElement("div"),v=l;v<c;v++){var o=n[v];o.computerPicPath=o.computerPicPath?JSON.parse(o.computerPicPath)[0].sourcePath:"/design/designstatic/common/image/information/defult.jpg",m.innerHTML=o.detail,o.detail=m.innerText,i+=r(o,v)}t.item.find("ul").html(i)}};D.init()})();