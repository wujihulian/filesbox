(function(){var we=/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent),ke=parseInt($(window).width()),X=o("isnorelated")?parseInt(o("isnorelated")):0,H=["/design/designstatic/chatroom/js/xgplayer/xgplayer.js","/design/designstatic/common/live/xgplayer-flv.js","/design/designstatic/common/live/xgplayer-hls.js","/design/designstatic/chatroom/video/playerreview.js"],ee=["\u9762\u8BAE","2\u5343\u4EE5\u4E0B","2-3\u5343","3-4.5\u5343","4.5-6\u5343","6-8\u5343","0.8-1\u4E07","1-1.5\u4E07","1.5-2\u4E07","2-3\u4E07","3-4\u4E07","5\u4E07\u4EE5\u4E0A"],ie=["\u4E0D\u9650","1-2\u5E74","2-3\u5E74","3-5\u5E74","5\u5E74\u4EE5\u4E0A"],ae=["\u4E0D\u9650","\u793E\u62DB","\u6821\u62DB"],te=["\u4E0D\u9650","\u5168\u804C","\u517C\u804C"],re=["\u4E0D\u9650","\u5C0F\u5B66","\u521D\u4E2D","\u9AD8\u4E2D","\u4E2D\u4E13","\u5927\u4E13","\u672C\u79D1","\u7855\u58EB","\u535A\u58EB","\u5176\u4ED6"],Ve=cdnPath=="//pre-static.1x.cn"?"https://pre.xx.cn":cdnPath=="//static.wxbig.cn"?location.origin:"https://test.1x.cn",ne=parseInt(o("mobile")||o("mobile100")||0),oe=o("targetServerNameForOverride")?"&targetServerNameForOverride="+o("targetServerNameForOverride"):"",$e=o("p"),Ie=o("r"),se=$e&&Ie?1:0,E=D("w")||D("kf")||se?parseInt(D("w")||D("kf")||se):0,Ye=o("kf")?parseInt(o("kf")):0,Ke={},z=0,le=1,m=60,F,de=0,O=0,Qe=ne?0:1,ce,pe,ve,fe,ue=4,Ze="",Xe=0,Te=$(".curriculum").width(),ei=(Te-(ue-1)*12)/ue,ii=[],ai=0,ti=!1;if(o("token")&&he("token",decodeURIComponent(o("token"))),o("vctoken")&&he("vctoken",decodeURIComponent(o("vctoken"))),!f("vxtoken")&&E&&(x()==1||x()==9)?(window.localStorage.setItem("isreport",1),De()):!f("vxtoken")&&E&&!(x()==1||x()==9||x()==5)||f("vxtoken")&&E||f("token"),(x()==4||x()==5||x()==1||x()==2)&&($(".head,.foot").remove(),x()==5&&($(".inforht").remove(),$(".inforpm").css({"margin-top":"14px"}))),o("associated")&&location.pathname.indexOf("pubinfo")>-1){var ze=new Base64,Se=decodeURIComponent(o("associated")),M=JSON.parse(ze.decode(Se))||{};ce=M.bindinfo||void 0,pe=M.bindcourse||void 0,ve=M.bindshop||void 0,fe=M.bindform||void 0}$(".coursemain img").each(function(i){var e=$(this),a,t;$("<img/>").attr("src",$(e).attr("src")).load(function(){a=this.width,t=this.height,a>=ke&&$(e).css("width","100%").css("height","auto"),a<1200&&$(e).css("width",a+"px").css("height",t+"px")})}),Date.prototype.format=function(i){var e={"M+":this.getMonth()+1,"d+":this.getDate(),"h+":this.getHours(),"m+":this.getMinutes(),"s+":this.getSeconds(),"q+":Math.floor((this.getMonth()+3)/3),S:this.getMilliseconds()};/(y+)/.test(i)&&(i=i.replace(RegExp.$1,(this.getFullYear()+"").substr(4-RegExp.$1.length)));for(var a in e)new RegExp("("+a+")").test(i)&&(i=i.replace(RegExp.$1,RegExp.$1.length==1?e[a]:("00"+e[a]).substr((""+e[a]).length)));return i};function Ce(){var i=document.createElement("script");i.setAttribute("type","text/javascript"),i.setAttribute("src","/information/js/qrcode.min.js"),document.getElementsByTagName("head")[0].appendChild(i)}function Oe(){$.ajax({type:"GET",headers:{token:f("token")},url:"/api/disk/getHomepageDetail",contentType:"application/json",dataType:"json",data:{infoID:q()||o("aid")||G(),infoId:ce,courseId:pe,shopId:ve,formId:fe,targetServerNameForOverride:o("targetServerNameForOverride")||void 0},async:!0,success:function(i){if(!i.success)return i.code=="user.bindSignError"?layer.open({content:"\u65E0\u6743\u9650\u67E5\u770B\uFF0C\u8BF7\u767B\u5F55",btn:["\u786E\u5B9A","\u53D6\u6D88"],yes:function(r,l){layer.close(r),setTimeout(()=>{location.href=`/#/login?redirect=${encodeURIComponent(location.pathname)}&isInfo=1`},100)}}):layer.open({content:i.message||"\u8D44\u8BAF\u4E0D\u5B58\u5728",btn:"\u786E\u5B9A"}),!1;var e=i.data,a=e.likeCount||0;window.information_sourceType=Me(e.infoType),window.information_busType=e.infoType,window.information_sourceId=q()||o("aid")||G();var t=e.isLike||!1,n=e.infoSource?JSON.parse(e.infoSource):{url:""},s=e.title,c=U(e.modifyTime||e.createTime);if($("title").text(s),$(".inforht").text(s),we)if(e.infoType=="2"){for(var d=e.employAreaList||[],p="",v,W=U(e.modifyTime||e.createTime),I=0;I<d.length;I++)v=I>0?"/":"",p+=v+d[I].areaName;$(".inforpm").html('<span style="padding:0px 12px 0px 0px;border-right: 1px solid #999;">'+ie[parseInt(e.employExp)]+'\u5DE5\u4F5C\u7ECF\u9A8C</span><span style="padding:0px 12px;border-right: 1px solid #999;">'+re[parseInt(e.employDegree)]+'</span><span style="padding: 0px 12px ;border-right: 1px solid #999;">'+te[parseInt(e.employNature)]+'</span><span style="padding: 0px 12px ;border-right: 1px solid #999;">'+ae[parseInt(e.employFrom)]+'</span><span style="padding: 0px 12px ;">'+W+"</span><div style='color:#FF5400;font-size:20px;margin-top:12px;'>"+ee[parseInt(e.employWage)]+"/\u6708</div>"),$(".inforht,.inforpm").css("textAlign","left")}else{var c=U(e.modifyTime||e.createTime);$(".inforpm").css("textAlign","left"),$(".inforpm").html('<span style="color:rgba(0,0,0,0.3);">'+c+"</span>"+(e.state=="4"||e.state=="5"?"":'<span style="float: inherit;margin-left:12px;" class="curshare">\u5206\u4EAB</span>'))}else if(e.infoType=="2"){for(var d=e.employAreaList||[],p="",v,W=U(e.modifyTime||e.createTime),I=0;I<d.length;I++)v=I>0?"/":"",p+=v+d[I].areaName;$(".inforpm").html((p?'<span style="padding-right: 12px;border-right: 1px solid #999;">'+p+"</span>":"")+'<span style="padding:0px 12px;border-right: 1px solid #999;">'+ie[parseInt(e.employExp)]+'\u5DE5\u4F5C\u7ECF\u9A8C</span><span style="padding:0px 12px;border-right: 1px solid #999;">'+re[parseInt(e.employDegree)]+'</span><span style="padding: 0px 12px ;border-right: 1px solid #999;">'+(parseInt(e.employNumber)?"\u62DB"+parseInt(e.employNumber)+"\u4EBA":"\u4E0D\u9650\u4EBA\u6570")+'</span><span style="padding: 0px 12px ;border-right: 1px solid #999;">'+te[parseInt(e.employNature)]+'</span><span style="padding: 0px 12px ;border-right: 1px solid #999;">'+ae[parseInt(e.employFrom)]+'</span><span style="padding: 0px 12px ;">'+W+"</span><span style='float:right;color:#FF5400;font-size:20px;'>"+ee[parseInt(e.employWage)]+"/\u6708</span>"),$(".inforht,.inforpm").css("textAlign","left")}else $(".inforpm").html("<span>"+c+"</span>"+(e.state=="4"||e.state=="5"?"":'<span style="float: inherit;margin-left:12px;" class="curshare">\u5206\u4EAB</span>')),$(".coursemain").html(e.detail||"");if(e.videoUrl){var P="",J=e.isVertical?e.computerPicPathVertical||[]:e.computerPicPath||[],me=e.thumb||"";J&&J.length&&(P=J[0].sourcePath),$(".coursemain").html(e.infoType=="1"||e.infoType=="15"?'<div style="margin-top:12px;margin-bottom: 12px;"><div style="margin:0 auto;" id="shortvideopreview"></div></div>'+e.introduce:(e.videoUrl&&e.videoId&&e.infoType=="6"||e.videoUrl&&e.videoId&&e.infoType=="0"?'<div style="margin-top:12px;margin-bottom: 12px;"><div style="margin:0 auto;" id="shortvideopreview"></div></div>':"")+e.detail),$("#shortvideopreview").css({"background-image":"url("+b((e.videoUrl&&e.videoId&&e.infoType=="6"||e.videoUrl&&e.videoId&&e.infoType=="0")&&me||P)+")","background-position":"center","background-size":"contain","background-repeat":"no-repeat"}),Ne(function(){var r=e.videoUrl,l={id:"shortvideopreview",url:r,autoplay:!1,poster:b((e.videoUrl&&e.videoId&&e.infoType=="6"||e.videoUrl&&e.videoId&&e.infoType=="0")&&me||P),ignores:["error"],playbackRate:""};init(l,function(){setTimeout(function(){$("#shortvideopreview").css({"background-image":"none"})},2e3)})})}else{var Ae=(e.infoType=="1"||e.infoType=="4"||e.infoType=="12"||e.infoType=="15"?e.introduce:(e.infoType=="2"?"<p><b>\u62DB\u8058\u5185\u5BB9</b></p>":"")+e.detail).replace(/<script>/gi,j("<script>"));if($(".coursemain").html(Ae),e.infoType=="3"||e.infoType=="10"||e.infoType=="8"||e.infoType=="9"){var R=e.computerPicPath&&e.computerPicPath.length?e.computerPicPath[0]:{};R&&($(".coursemain").html('<img title="'+R.title+'"  src="'+b(R.sourcePath)+'" />').css({"text-align":"center"}),$(".inforht").text(R.title))}else e.infoType=="2"}var S="<div class='infoSourcebox' >";if(n.name&&(S+="<span>\u6765\u6E90\uFF1A"+n.name+"</span>"),n.url){var Le=n.url.indexOf("http")<0?"//"+n.url:n.url;S+="<span>\u94FE\u63A5\uFF1A<a target='_blank' href='"+Le+"'>"+n.url+"</a></span>"}if(S+="</div>",(e.infoType=="0"||e.infoType=="1")&&!e.applyOriginal&&(S+="<div class='infostatementtipbtn' style='display: block;text-align:left; margin-top: 6px;'><div style='display: inline-block;cursor: pointer;background: rgba(255, 255, 255, 0.1);padding: 2px 6px;color: #999;font-size:12px;' class='infostatementtip'>\u24D8 \u514D\u8D23\u58F0\u660E</div></div><div style='font-size:12px;display:none;color: #999;' class='infotipscontent'>\u58F0\u660E\uFF1A\u672C\u5E73\u53F0\u53D1\u5E03\u7684\u5185\u5BB9\uFF08\u56FE\u7247\u3001\u89C6\u9891\u548C\u6587\u5B57\uFF09\u4EE5\u539F\u521B\u3001\u8F6C\u8F7D\u548C\u5206\u4EAB\u7F51\u7EDC\u5185\u5BB9\u4E3A\u4E3B\uFF0C\u5982\u679C\u6D89\u53CA\u4FB5\u6743\u8BF7\u5C3D\u5FEB\u544A\u77E5\uFF0C\u6211\u4EEC\u5C06\u4F1A\u5728\u7B2C\u4E00\u65F6\u95F4\u5220\u9664\u3002\u6587\u7AE0/\u89C6\u9891\u89C2\u70B9\u4E0D\u4EE3\u8868\u672C\u5E73\u53F0\u7ACB\u573A\uFF0C\u5982\u9700\u5904\u7406\u8BF7\u8054\u7CFB\u5BA2\u670D\u3002</div>"),!X){var _e='style="display:none"';S+="<div class='likesharebox'><div class='like "+(t?"likeactive":"")+(a?"":"nolikebtn")+"'>"+(a||"")+"</div><div class='share'"+_e+" id='share'>\u5206\u4EAB</div></div>"}if($(".curriculum .albumbox").hide(),$(".sharesource").html(S),$(".infostatementtipbtn").on("click",function(){$(this).hide().siblings(".infotipscontent").show()}),$(".infotipscontent").on("click",function(){$(this).hide().siblings(".infostatementtipbtn").show()}),e.fileAttList){$(".fotfer").show(),$(".naber").html(e.fileAttList.length),e.fileAttList.length==1&&$(".lones").hide();var w="";e.fileAttList.forEach(function(r,l,k){var u=Re(r.fileType),y="/information/image/fileIcon/"+u+".png",g=Pe(r.size),T=r.previewUrl.replace(/\s+/g,"%20"),h=r.sourceName.replace(/\s+/g,"&nbsp;");w+='<a class="fuctdiv" title='+h+" href="+T+" >",w+="<img src="+b(y)+" />",w+="<div class='sourcecontent'>",w+="<p class='sourceName'>"+r.sourceName+"</p>",w+="<p>"+g+"</p>",w+="</div>",w+='<i class="nosder"></i>',w+="</a>"}),w&&(w+='<div style="clear: both;"></div>'),$(".fuwent").html(w)}var A="",L="",_="",V=[],Y=[],K=[],B=0;if(!X){var Be=e.relationMap||[];if(Be.forEach(function(r,l,k){r.relationType=="3"&&(B=r.busId),r.relationType=="1"&&Y.push(r),r.relationType=="2"&&K.push(r),r.relationType=="4"&&V.push(r)}),B){$(".relation").html("<div class='relationformul'></div>");var li=window.location.href.indexOf("html?")>=0?1:0,xe=Fe("formId="),Q=location.origin+location.pathname+xe+(xe?"&":"?")+"formId="+B;history.replaceState?history.replaceState({},"",Q):location.href!=Q&&(location.href=Q);var He={init:function(r){var l=this;window.formId=r,l.renderRoot()},renderRoot:function(){$(".relationformul").html('<div id="root"></div>'),$.getScript("/portal/umi.js")}};setTimeout(function(){He.init(B)},200)}Y.length&&(L+="<div class='relationinfoul'><div class='label'>\u70ED\u95E8\u6587\u7AE0</div>",Y.forEach(function(r,l,k){var u=U(r.modifyTime||r.createTime),y=r.isUrlInfo?(r.infoUrl||"").indexOf("http")<0?"//"+r.infoUrl:r.infoUrl||"":"",g=y||(location.port=="81"?"/pages/information.html?aid="+r.busId:"/information/"+r.busId+".shtml");L+="<div class='infoli'><div class='infotitle'><a target='_blank' href='"+g+"'>"+r.title.replace(/<script>/gi,j("<script>"))+"</a></div><div class='infointroduce'>"+r.introduce.replace(/<script>/gi,j("<script>"))+"</div><div class='infotime'>"+u+"</div></div>"}),L+="</div>",$(".relation").append(L)),V.length&&(A+="<div class='relationcourseul'><div class='label'>\u7CBE\u9009\u8BFE\u7A0B</div><div class='relationcourseulflex'>",V.forEach(function(r,l,k){var u=r.cover?r.cover:r.busInnerType==2||r.busInnerType==4?"/design/designstatic/common/image/course/package.jpg":"/design/designstatic/common/image/course/defult.jpg",y=parseInt(r.price)?"\xA5"+r.price/100:"\u514D\u8D39",g=r.busInnerType==2||r.busInnerType==4?"bundle":"info";A+="<a target='_blank' href='/pages/"+g+".html?cid="+r.busId+"'><div class='courseli'><div class='coursecover'><img src='"+b(u)+"' /></div><div class='coursebottom'><div class='courseprice'>"+y+"</div><div class='coursefl'>"+r.title.replace(/<script>/gi,j("<script>"))+"</div></div></div></a>"}),A+="</div></div>",$(".relation").append(A)),K.length&&(_+="<div class='relationshopul'><div class='label'>\u597D\u7269\u63A8\u8350</div><div class='relationshopulflex'>",K.forEach(function(r,l,k){var u=r.cover||"",l=u.lastIndexOf(".done")>=0?u.lastIndexOf(".done"):u.lastIndexOf("."),y=u?u.substring(0,l)+".jpg":"";if(!y){var g=r.coverList?JSON.parse(r.coverList):[];g&&g.length&&(y=g[0].sourcePath)}var T="",h=r.price?r.price.split(","):[],C=parseFloat(h&&h.length?h[0]:0),N=parseFloat(h&&h.length?h[h.length-1]:0);T=C||N?r.paymentType=="2"?(C||N)+"\u79EF\u5206":"\uFFE5"+(C||N)/100:"",_+="<a target='_blank' href='/shop/item.html?itemid="+r.busId+"'><div class='shopli'><div class='shopcover' style='background:url("+b(y)+") no-repeat;'></div><div class='shopbottom'><div class='shopprice'>"+T+"</div><div class='shopfl'>"+r.title.replace(/<script>/gi,j("<script>"))+"</div></div></div></a>"}),_+="</div></div>",$(".relation").append(_))}var qe="",ye=o("att")?decodeURIComponent(o("att")):"",di=o("vcardId")?decodeURIComponent(o("vcardId")):"";ye&&($(".cardwxphonebtns").remove(),$.ajax({headers:{token:f("token")},type:"GET",url:"/api/common/getHpsAttInfo",data:{att:ye},dataType:"json",async:!1,success:function(r){if(r.code==200){var l=r.data||{},k=l.mobile||"",u=l.position||"",y=l.schoolName||"",g=l.wcQrImg||"",T=l.wxAccount||"",h=l.headPortrait||"",C=l.realName||l.nickname||l.loginName||"";qe=l.vcardId||"";var N='<div class="cardwxphonebtns">'+(k?'<a class="cardphone"></a>':"")+(T||g?'<a class="cardwx"></a>':"")+"</div>";$("body").append(N),$(".mobileyzbox .mobileuser .cover img").length?$(".mobileyzbox .mobileuser .cover img").attr({src:b(h)}):window.yzmmodalface=h,$(".cardwxphonebtns .cardphone").on("click",function(){var We=layer.open({type:1,title:"",skin:"layui-layer-antd nohasbtn",closeBtn:1,anim:2,area:"375px 310px",scrollbar:!1,shadeClose:!1,cancel:function(Z,Je){},success:function(Z){},content:"<div style='padding:20px 30px;width:354px;height:169px;box-sizing:border-box;'> <div style='display:flex;align-items:center;'> <div style='width:60px;height:60px;border-radius:50%;overflow:hidden;position: relative;margin-right: 16px;'><img style='width: 100%; height: 100%; position: absolute;top: 0px; left: 0px;' src='"+b(h)+"' /></div><div style='flex: 1; overflow: hidden;'><div style='font-size: 18px;color: #333333;margin-bottom: 14px;white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>"+C+"<span style='font-size:16px;color:#666666;margin-left:13px;'>"+u+"</span></div><div style='font-size:16px;color:#666666;white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>"+y+"</div></div></div><div style='font-size:16px;color:#333333;margin-top:30px;'>\u6B22\u8FCE\u54A8\u8BE2\uFF1A<span style='margin-left:10px;font-size:22px;color:#333333;'>"+k+"</span></div></div>"})}),$(".cardwxphonebtns .cardwx").on("click",function(){var We=layer.open({type:1,title:"",skin:"layui-layer-antd nohasbtn",closeBtn:1,anim:2,area:"375px 310px",scrollbar:!1,shadeClose:!1,cancel:function(Z,Je){},success:function(Z){},content:"<div style='padding:30px 40px;width:354px;height:447px;box-sizing:border-box;'> <div style='display:flex;align-items:center;'> <div style='width:60px;height:60px;border-radius:50%;overflow:hidden;position: relative;margin-right: 16px;'><img style='width: 100%; height: 100%; position: absolute;top: 0px; left: 0px;' src='"+b(h)+"' /></div><div style='flex: 1; overflow: hidden;'><div style='font-size: 18px;color: #333333;margin-bottom: 14px;white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>"+C+"<span style='font-size:16px;color:#666666;margin-left:13px;'>"+u+"</span></div><div style='font-size:16px;color:#666666;white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>"+y+"</div></div></div>"+(g?"<div style='font-size:16px;color:#333333;margin-top:30px;'> <div style='width:180px;height:180px;margin:0px auto;position: relative;'><img style='width: 100%; height: 100%; position: absolute;top: 0px; left: 0px;' src='"+b(g)+"'  /></div> </div>":"")+(T?"<div style='font-size:16px;color:#333333;margin-top:30px;    text-align: center;'>\u5FAE\u4FE1\u53F7\uFF1A<span style='margin-left:10px;font-size:22px;color:#333333;'>"+T+"</span></div>":"")+"<div style='font-size:16px;color:#333333;margin-top:30px;    text-align: center;'>\u626B\u4E00\u626B\u52A0\u6211\u7684\u5FAE\u4FE1</div></div>"})})}}}));var Ge=parseInt(window.localStorage.isreport)||0;Ge==1&&E&&window.localStorage.setItem("isreport",2),ne&&!(z?le:(window.localStorage.notokencheckCode?parseInt(window.localStorage.notokencheckCode):0)||0)&&Ue(),$(".curshare").on("click",function(){be()}),$(".likesharebox .share").on("click",function(){be()}),(x()==4||x()==5)&&$(".likesharebox .share,.curshare").hide(),$(".likesharebox .like").on("click",function(){$.ajax({type:"POST",headers:{token:f("token")},url:"/api/common/like",contentType:"application/json",dataType:"json",data:JSON.stringify({infoID:q()||o("aid")||G(),isLike:t?0:1}),async:!0,success:function(r){r.success?(t?(a-=1,t=!1):(a+=1,t=!0),$(".likesharebox .like").text(a||""),t?$(".likesharebox .like").removeClass("likeactive").addClass("likeactive"):$(".likesharebox .like").addClass("likeactive").removeClass("likeactive"),a?$(".likesharebox .like").removeClass("nolikebtn"):$(".likesharebox .like").addClass("nolikebtn")):layer.open({content:r.message||"\u70B9\u8D5E\u5931\u8D25\uFF01",btn:"\u786E\u5B9A"})}})});function be(){layer.open({type:1,title:"\u5206\u4EAB",area:["420px","auto"],content:'<div class="shareMax"><div class="shareBox"><span>\u94FE\u63A5\uFF1A</span><input id="shareUrl" readOnly value="'+location.href+'" /><button id="copy">\u590D\u5236</button></div><div class="qrcode" id="qrcode"></div><a class="download" id="qrcodeadown">\u4E0B\u8F7D\u4E8C\u7EF4\u7801</a></div>',btn:""}),$("#copy").on("click",function(){document.getElementById("shareUrl").select(),document.execCommand("copy"),layer.msg("\u590D\u5236\u6210\u529F")}),new QRCode(document.getElementById("qrcode"),{text:location.href,width:160,height:160,correctLevel:0}),$("#qrcodeadown").on("click",function(){let r=$("#qrcode").find("canvas").get(0),l=new Image;l.src=r&&r.toDataURL("image/png"),l.name=s;let k=document.getElementById("qrcodeadown");k.href=l.src,k.setAttribute("download",s+".png")})}je()}})}function je(){let i=document.getElementById("share");if(!f("token")){i.style.display="inline-block";return}$.ajax({type:"GET",headers:{token:f("token")},url:"/api/disk/options",contentType:"application/json",dataType:"json",async:!0,success:function(e){if(!e.success)return;let a=e.data;const{user:t,role:n}=a;(t.administrator==1||n["explorer.share"]==1)&&(i.style.display="inline-block")}})}$(function(){Ce(),setTimeout(Oe,10)});function Ue(i){var e='<div class="mobileyzbox"><div class="mobileuser"><div class="cover"><img src='+b(window.yzmmodalface||"/design/designstatic/aroom/img/clueface.png")+' /></div></div><div class="usertext"><div class="triangle-up"></div>\u6DF1\u5165\u4E86\u89E3\u4E2D\uFF1F\u4E5F\u8BB8\u4F1A\u9700\u8981\u6211\u7684\u7B54\u7591\u8981\u70B9\uFF0C\u8BF7\u7559\u7ED9\u6211\u60A8\u7684\u8054\u7CFB\u65B9\u5F0F\u5427</div><div class="yzform"><div class="mobilebox"><input type="text" maxLength="11" placeholder="\u60A8\u7684\u624B\u673A\u53F7\u7801" /></div><div class="erroralert">\u8BF7\u8F93\u5165\u6B63\u786E\u624B\u673A\u53F7</div><div class="yzmbox"><input type="text" maxLength="10" placeholder="\u9A8C\u8BC1\u7801" /><div class="yzmbtn"><div>\u5DF2\u53D1\u9001(60)</div><a>\u53D1\u9001\u9A8C\u8BC1\u7801</a></div></div></div><div class="subbtn"><a>\u9A6C\u4E0A\u67E5\u770B</a></div></div>';if(!de)var a=layer.open({type:1,title:"",skin:"layui-layer-antd nohasbtn mobileyzmodal",closeBtn:0,anim:2,area:"375px 310px",scrollbar:!1,shadeClose:!1,cancel:function(t,n){},success:function(t){de=1,$(".mobileyzbox .mobilebox input").on("blur",function(n){var s=n.target.value||"";s&&!/^1\d{10}$/.test(s)?$(".mobileyzbox .erroralert").show():$(".mobileyzbox .erroralert").hide()}),$(".mobileyzbox .yzmbtn a").on("click",function(n){if(!O){var s=$(".mobileyzbox .mobilebox input").val();if(!s){layer.msg("\u8BF7\u8F93\u5165\u624B\u673A\u53F7");return}if(!/^1\d{10}$/.test(s)){layer.msg("\u8BF7\u8F93\u5165\u6B63\u786E\u624B\u673A\u53F7");return}var c=z?"/api/vcard/sendVerificationCode":"/api/vcard/sendVerificationCodeG";O=1,z?$.ajax({type:"GET",headers:{token:f("token")},url:c+"?projectid=74",contentType:"application/json",dataType:"json",data:{mobilePhone:s,uvcid:z?void 0:localStorage.uvcid||"",targetServerNameForOverride:o("targetServerNameForOverride")||void 0},async:!0,success:function(d){var p=d.data||{},v=p.vCardInfo||{};d.code==200&&d.success&&(m=60,F=setInterval(()=>{m<=0?(m=60,$(".mobileyzbox .yzmbtn a").show(),$(".mobileyzbox .yzmbtn div").hide(),clearInterval(F),O=0):(m=m-1,$(".mobileyzbox .yzmbtn a").hide(),$(".mobileyzbox .yzmbtn div").text("\u5DF2\u53D1\u9001("+m+")").show())},1e3))}}):$.ajax({type:"POST",headers:{token:f("token")},url:c+"?projectid=74"+oe,contentType:"application/json",dataType:"json",data:JSON.stringify({mobilePhone:s,uvcid:z?void 0:localStorage.uvcid||""}),async:!0,success:function(d){var p=d.data||{},v=p.vCardInfo||{};d.code==200&&d.success&&(m=60,O=1,F=setInterval(()=>{m<=0?(m=60,$(".mobileyzbox .yzmbtn a").show(),$(".mobileyzbox .yzmbtn div").hide(),clearInterval(F),O=0):(m=m-1,$(".mobileyzbox .yzmbtn a").hide(),$(".mobileyzbox .yzmbtn div").text("\u5DF2\u53D1\u9001("+m+")").show())},1e3))}})}}),$(".mobileyzbox .subbtn a").on("click",function(n){var s=$(".mobileyzbox .mobilebox input").val(),c=$(".mobileyzbox .yzmbox input").val();if(!s){layer.msg("\u8BF7\u8F93\u5165\u624B\u673A\u53F7");return}if(!/^1\d{10}$/.test(s)){layer.msg("\u8BF7\u8F93\u5165\u6B63\u786E\u624B\u673A\u53F7");return}if(!c){layer.msg("\u8BF7\u8F93\u5165\u9A8C\u8BC1\u7801");return}var d=z?"/api/vcard/checkVerifyCode":"/api/vcard/checkVerifyCodeG";$.ajax({type:"POST",headers:{token:f("token")},url:d+"?projectid=74"+oe,contentType:"application/json",dataType:"json",data:JSON.stringify({code:c,mobilePhone:s,uvcid:z?void 0:localStorage.uvcid||""}),async:!0,success:function(p){parseInt(p.code)==200?(window.localStorage.setItem("notokencheckCode",1),le=1,layer.msg("\u9A8C\u8BC1\u6210\u529F"),layer.closeAll()):layer.msg(p.message)}})})},content:e})}function Pe(i){if(!i)return"";var e=1024;return i<e?i+"B":i<Math.pow(e,2)?(i/e).toFixed(2)+"KB":i<Math.pow(e,3)?(i/Math.pow(e,2)).toFixed(2)+"M":i<Math.pow(e,4)?(i/Math.pow(e,3)).toFixed(2)+"G":(i/Math.pow(e,4)).toFixed(2)+"T"}function ri(i){var e=i.replace(/([^.]*).([^.]*)$/g,"$1!medium.$2");return e}function D(i){var e=new RegExp("(^|&)"+i+"=([^&]*)(&|$)","i"),a=window.location.search.substr(1).match(e),t="";return a!=null&&(t=a[2]),e=null,a=null,t==null||t==""||t=="undefined"?"":t}function o(i){var e=new RegExp("(^|&)"+i+"=([^&]*)(&|$)"),a=window.location.search.substr(1).match(e);return a!=null?unescape(a[2]):null}function q(){for(var i=location.href.split("/"),e=0,a=i.length;e<a;e++){var t=i[e];if(t.indexOf(".shtml")>-1)return t.split(".shtml")[0]}return!1}function G(){for(var i=location.href.split("/"),e=0,a=i.length;e<a;e++){var t=i[e];if(t.indexOf(".html")>-1)return t.split(".html")[0]}return!1}function f(i){for(var e=document.cookie,a=e.split("; "),t=0;t<a.length;t++){var n=a[t].split("=");if(n[0]==i)return n[1]}return""}function he(i,e){var a=30,t=new Date;t.setTime(t.getTime()+a*24*60*60*1e3),document.cookie=i+"="+escape(e)+";path=/;expires="+t.toGMTString()}function Ne(i){var e=this;ge(i)}function ge(i){var e=this,a=H.length;a?Ee(H[0],function(){H.shift(),ge.call(e,i)}):i.call(e)}function Ee(i,e){var a=document.getElementsByTagName("head")[0],t=document.createElement("script");t.type="text/javascript",t.src=i+"?v="+new Date().valueOf(),typeof e=="function"&&(t.onload=t.onreadystatechange=function(){(!this.readyState||this.readyState==="loaded"||this.readyState==="complete")&&(e(),t.onload=t.onreadystatechange=null)}),a.appendChild(t)}function x(){var i=navigator.userAgent.toLowerCase(),e=i.indexOf("wujiapp")>-1;if(window.__wxjs_environment==="miniprogram")return 4;if(i.match(/MicroMessenger/i)=="micromessenger"){var a={win:!1,mac:!1},t=navigator.platform;return a.win=t.indexOf("Win")==0,a.mac=t.indexOf("Mac")==0,a.win||a.mac?9:1}else return window.localStorage.appAdapt==="true"||window.localStorage.appAdapt===!0||e?5:o("channel")?o("channel"):/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)?2:3}function ni(i){var e=new window.URL(window.location.href);return e.searchParams.delete(i),e.href}function oi(i,e){var a=new window.URL(window.location.href);return a.searchParams.set(i,e),a.href}function Fe(i){for(var e=location.search,a=e.split("&"),t="",n=0,s=a.length;n<s;n++){var c=a[n];c.indexOf("?"+i)>-1?n+1!==s&&(t+="?"):c.indexOf(i)==0||(t+=t&&t!="?"?"&"+c:c)}return t}function Me(i){switch(i){case"0":return"1";case"1":return"2";case"2":return"8";case"7":return"9";case"6":return"10";case"5":return"11";case"4":return"12";case"3":return"13";case"11":return"16";case"8":return"19";case"9":return"18";case"10":return"17";case"13":return"21";case"14":return"22";case"15":return"23";default:return"0"}}function De(){$(document).off("touchend"),$(document).off("click"),$(document).on("touchend",function(){var i="wxa09a4ca9fba61af1",e="wx60a208c13d8acea1",a="test.1x.cn";cdnPath=="//pre-static.1x.cn"&&(i="wxc0968245d91aaf85",e="wx60a208c13d8acea1",a="pre.xx.cn"),cdnPath=="//static.wxbig.cn"&&(i="wxc420bf93bbe26d62",e="wx1ae69c2e8734dd28",a="sys.wxdad.cn");var t=window.location.href,n=decodeURIComponent(decodeURIComponent(t)),s=n+(n.indexOf("?")>-1?"&":"?")+"noevxtoken=true",c=encodeURIComponent(s),d=window.frames.length!=parent.frames.length;$.ajax({headers:{token:f("token")},type:"GET",url:"/api/welink/wechat/qrCode/url",data:{targetServerNameForOverride:o("targetServerNameForOverride")||void 0,envTypeName:"official_account",sceneTypeName:"consulting",redirectUrl:c},dataType:"json",async:!1,success:function(p){if(p.code==200&&p.success){var v=p.data||{};v.url&&(d?window.parent.location.href=v.url:window.location.replace(v.url))}}})}),$(document).on("click",function(){var i="wxa09a4ca9fba61af1",e="wx60a208c13d8acea1",a="test.1x.cn";cdnPath=="//pre-static.1x.cn"&&(i="wxc0968245d91aaf85",e="wx60a208c13d8acea1",a="pre.xx.cn"),cdnPath=="//static.wxbig.cn"&&(i="wxc420bf93bbe26d62",e="wx1ae69c2e8734dd28",a="sys.wxdad.cn");var t=window.location.href,n=decodeURIComponent(decodeURIComponent(t)),s=n+(n.indexOf("?")>-1?"&":"?")+"noevxtoken=true",c=encodeURIComponent(s),d=window.frames.length!=parent.frames.length;$.ajax({headers:{token:f("token")},type:"GET",url:"/api/welink/wechat/qrCode/url",data:{targetServerNameForOverride:o("targetServerNameForOverride")||void 0,envTypeName:"official_account",sceneTypeName:"consulting",redirectUrl:c},dataType:"json",async:!1,success:function(p){if(p.code==200&&p.success){var v=p.data||{};v.url&&(d?window.parent.location.href=v.url:window.location.replace(v.url))}}})})}function si(i){var e,a=500,t=570,n=(window.screen.height-30-t)/2,s=(window.screen.width-10-a)/2;window.open(i,e,"height="+t+",innerHeight="+t+",width="+a+",innerWidth="+a+",top="+n+",left="+s+",toolbar=no,menubar=no,scrollbars=auto,resizable=no,location=no,status=no")}function Re(i){var e="1."+(i||"").toLowerCase(),a="unknow@2x";return/^\w+\.(docx|doc)$/.test(e)?a="-e-doc@2x":/^\w+\.(xlsx|xls)$/.test(e)?a="-e-xlsx@2x":/^\w+\.(pptx|ppt)$/.test(e)?a="-e-pptx@2x":/^\w+\.(pdf)$/.test(e)?a="-e-pdf@2x":/^\w+\.(jpg|jpeg|bmp|gif|png)$/.test(e)?a="image":/^\w+\.(avi|mov|rmvb|rm|flv|mp4|3gp)$/.test(e)?a="-e-mp4@2x":/^\w+\.(mp3)$/.test(e)?a="-e-music@2x":/^\w+\.(rar|zip|gz|7z)$/.test(e)&&(a="-e-zip@2x"),a}function j(i){var e=document.createElement("div");return e.innerText?e.innerText=i:e.textContent=i,e.innerHTML}function b(i){return(i||"").replace(/(^\s*)/g,"").indexOf("http")!=0&&(i||"").indexOf(cdnPath)<0&&(i||"").indexOf("data:image")!==0?cdnPath+i:i}function U(i,e){if((i+"").length==10&&(i=i*1e3),new Date(parseInt(i))=="Invalid Date"&&new Date(parseInt(i)*1e3)=="Invalid Date")return"--";new Date(parseInt(e))=="Invalid Date"&&new Date(parseInt(e)*1e3)=="Invalid Date"&&(e=Date.parse(new Date));var a=(e-i)/1e3;if(a<0)return"\u521A\u521A";if(a<60)return parseInt(a,10)+"\u79D2\u524D";if(a<3600)return parseInt(a/60,10)+"\u5206\u949F\u524D";if(a<24*3600)return parseInt(a/3600,10)+"\u5C0F\u65F6\u524D";if(a<7*24*3600)return parseInt(a/3600/24,10)+"\u5929\u524D";if(a<30*24*3600)return parseInt(a/3600/24/7,10)+"\u5468\u524D";if(a<365*24*3600)return parseInt(a/3600/24/30,10)+"\u4E2A\u6708\u524D";var t=new Date(parseInt(i)),n=parseInt(t.getMonth()+1)<10?"0"+(t.getMonth()+1):t.getMonth()+1,s=parseInt(t.getDate())<10?"0"+t.getDate():t.getDate(),c=parseInt(t.getHours())<10?"0"+t.getHours():t.getHours(),d=parseInt(t.getMinutes())<10?"0"+t.getMinutes():t.getMinutes();return t.getFullYear()+"-"+n+"-"+s+" "+c+":"+d}})();