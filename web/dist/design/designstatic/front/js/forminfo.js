(function(){var v=parseInt($(window).width()),o=["/static/chatroom/js/xgplayer/xgplayer.js","/static/common/live/xgplayer-flv.js","/static/common/live/xgplayer-hls.js","/static/chatroom/video/playerreview.js"];$(".coursemain img").each(function(e){var t=$(this),i,r;$("<img/>").attr("src",$(t).attr("src")).load(function(){i=this.width,r=this.height,i>=v&&$(t).css("width","100%").css("height","auto"),i<1200&&$(t).css("width",i+"px").css("height",r+"px")})});function E(e){var t=new Date(e),i=t.getFullYear()+"-",r=t.getMonth()+1<10?"0"+(t.getMonth()+1):t.getMonth()+1,n=t.getDate()+" ",g=t.getHours()+":",c=t.getMinutes()+":",a=t.getSeconds();return i+r+n+g+c+a}var x;$.ajax({headers:{token:s("token")},type:"GET",url:"/api/jwt/getLoginUser?projectid=45",data:{},dataType:"json",async:!1,success:function(e){e.code==200&&(x=e.data.userId)}}),Date.prototype.format=function(e){var t={"M+":this.getMonth()+1,"d+":this.getDate(),"h+":this.getHours(),"m+":this.getMinutes(),"s+":this.getSeconds(),"q+":Math.floor((this.getMonth()+3)/3),S:this.getMilliseconds()};/(y+)/.test(e)&&(e=e.replace(RegExp.$1,(this.getFullYear()+"").substr(4-RegExp.$1.length)));for(var i in t)new RegExp("("+i+")").test(e)&&(e=e.replace(RegExp.$1,RegExp.$1.length==1?t[i]:("00"+t[i]).substr((""+t[i]).length)));return e},document.write('<link rel="stylesheet" href="/designschool/form/css/index.css">'),document.write('<link rel="stylesheet" href="/designschool/form/css/form.css">'),document.write('<script type="text/javascript" charset="utf-8" src="/designschool/form/DateTime/js/calendar.js"></script>'),document.write('<link rel="stylesheet" href="/designschool/form/DateTime/css/calendar.css">'),document.write('<script type="text/javascript" charset="utf-8" src="/designschool/form/js/md5.js"></script>');var y={init:function(e){var t=this;t.formId=e,t.getFormDate()},getFormDate:function(){var e=this;$.ajax({type:"GET",headers:{token:s("token")},url:" /api/common/findFormForStu?projectid=16",contentType:"application/json",dataType:"json",data:{formId:e.formId},async:!0,success:function(t){e.carryFormEvent(t)}})},carryFormEvent:function(e){var t=this,i=e.data||{},r=i.formInfo||{},n=i.answerInfo;window.playerItemList=r.itemList,parseInt(r.anonymousLimit)==0&&!s("token")?t.renderFormState("unlogin",e):parseInt(r.isMultiSubmit)==0&&parseInt(n.isFilled)==1?t.renderFormState("completed"):t.renderFormHtml(r)},renderFormState:function(e,t){var i=this,r="";switch(e){case"completed":r='<div style="text-align:center;font-size: 22px;height:32px;"><div class="textBox" style="height: 60px;text-align: center;color:#2f2d2c;"><p style="font-size: 12px;line-height: 30px;color:#999999;"><img src="//static.wxbig.cn/designschool/form/image/success.png" style="width: 18px;vertical-align: sub;margin-right: 7px;">\u6211\u4EEC\u5DF2\u6536\u5230\u60A8\u7684\u53CD\u9988, \u975E\u5E38\u611F\u8C22\u3002</p></div></div>';break;case"unlogin":r='<div style="text-align:center;font-size: 22px;height:500px;"><div class="textBox" style="height: 200px;text-align: center;color:#2f2d2c;"><p style="font-size: 20px;line-height: 30px;"><img src="//static.wxbig.cn/designschool/form/image/warn.png" style="width: 25px;vertical-align: sub;margin-right: 7px;">\u672A\u767B\u5F55</p><p style="font-size: 12px;line-height: 30px;color:#999999;">\u8BF7\u60A8<a class="loginFormEvent" style="color: #1890ff;cursor: pointer;">\u767B\u5F55</a>\u540E\u586B\u5199\uFF0C\u975E\u5E38\u611F\u8C22\uFF01</p></div></div>';break}$(".relationformul").html(r),$(".loginFormEvent").click(function(){user.loginLayer(function(n){n.success&&i.getFormDate()})})},renderFormHtml:function(e){var t=this;e.referenceSavePath&&$.ajax({type:"get",url:e.referenceSavePath,dataType:"html",success:function(i){if(i){var r="<div class='formtitle'>"+e.title+"</div><div class='formcontro' style='text-align: center;padding: 0 10px;'>"+e.description+"</div><div class='formcontent'>"+i+"</div>";$(".relationformul").html(r)}t.bindEvent()}})},bindEvent:function(){$.getScript("/designschool/form/js/formScript.js",function(){})}};function h(){var e,t,i,r=p()||u("formId");if(r){$(".relation").append("<div class='relationformul'></div>");var n=j(window.location.href,"formId",r);window.history.replaceState({path:n},"",n),y.init(r)}function g(){var c="/static/front/images/shareBackground.png",a=res.isVertical?res.computerPicPathVertical||[]:res.computerPicPath||[],F=a&&a.length?a[0].sourcePath:"",l={courseName:res.title,commonHomepageInfoId:p()||u("aid"),cover:F||"/static/common/image/information/defult.jpg",introduce:res.introduce};l.userInfo=user.useInfo||{},l.iscourse=res.infoType=="1"?"shortvideo":"information",user.isLogin(function(d){if(parseInt(d.code)==200){var I=window.imgEditor,M=this,b=d.data.userId,S=window.location.href;I.shareLayer(S,c,function(f){f.success||M.unlogin()},l)}else parseInt(d.code)==401?user.loginLayer(function(f){parseInt(f.code)==200&&h()}):layer.open({content:"\u8BF7\u8054\u7CFB\u7BA1\u7406\u5458\uFF01",btn:"\u786E\u5B9A"})})}}$(function(){setTimeout(h,10)});function T(e){if(!e)return"";var t=1024;return e<t?e+"B":e<Math.pow(t,2)?(e/t).toFixed(2)+"KB":e<Math.pow(t,3)?(e/Math.pow(t,2)).toFixed(2)+"M":e<Math.pow(t,4)?(e/Math.pow(t,3)).toFixed(2)+"G":(e/Math.pow(t,4)).toFixed(2)+"T"}function D(e){var t=e.replace(/([^.]*).([^.]*)$/g,"$1!medium.$2");return t}function u(e){var t=document.location.href,i=t.indexOf(e+"=");if(i==-1)return!1;var r=t.slice(e.length+i+1),n=r.indexOf("&");return n!=-1&&(r=r.slice(0,n)),r}function p(){for(var e=location.href.split("/"),t=0,i=e.length;t<i;t++){var r=e[t];if(r.indexOf(".shtml")>-1)return r.split(".shtml")[0]}return!1}function s(e){for(var t=document.cookie,i=t.split("; "),r=0;r<i.length;r++){var n=i[r].split("=");if(n[0]==e)return n[1]}return""}function P(e){var t=this;m(e)}function m(e){var t=this,i=o.length;i?w(o[0],function(){o.shift(),m.call(t,e)}):e.call(t)}function w(e,t){var i=document.getElementsByTagName("head")[0],r=document.createElement("script");r.type="text/javascript",r.src=e+"?v="+new Date().valueOf(),typeof t=="function"&&(r.onload=r.onreadystatechange=function(){(!this.readyState||this.readyState==="loaded"||this.readyState==="complete")&&(t(),r.onload=r.onreadystatechange=null)}),i.appendChild(r)}function j(e,t,i){if(!i)return e;var r=new RegExp("([?&])"+t+"=.*?(&|$)","i"),n=e.indexOf("?")!==-1?"&":"?";return e.match(r)?e.replace(r,"$1"+t+"="+i+"$2"):e+n+t+"="+i}})();