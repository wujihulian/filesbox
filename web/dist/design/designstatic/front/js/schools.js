function zesol(){var a=getPar("appid"),t=getPar("code"),o=getPar("state"),r=getPar("showMode"),s=getPar("appUserName");$.ajax({type:"POST",headers:{token:getCookie("token")},url:"/api/welink/userEntry/getSchoolList?projectid=50",contentType:"application/json",dataType:"json",data:JSON.stringify({appid:a,code:t,state:o,showMode:r}),async:!0,success:function(p){if(p.code==200){var i=p.data;if(i.length==1){var c=i[0].domain,l=i[0].token;if(i[0].isHttps==1)var d="https://";else var d="http://";i[0].userType==1?window.location.href=d+c+"/pages/Independent/wxjump.html?type=others&rurl="+o+"&token="+l+"&userType=1":window.location.href=d+c+"/pages/Independent/wxjump.html?type=others&rurl="+o+"&token="+l+"&userType=2"}else if(i.length==0)window.location.href="/api/welink/bindPage?id="+s;else{$(document).attr("title","\u9009\u62E9\u7F51\u6821"),$("body").css("background","url(/static/front/images/backpic.png) #f0f2f5");for(var e="",g=0,u=i.length;g<u;g++){var n=i[g],f=n.domain,v=getMyDate(n.gmtExpire),l=n.token,y=n.userType;/Android|webOS|iPhone|iPod|iPad|BlackBerry/i.test(navigator.userAgent)?e+='<li class="mobile">':e+="<li>",e+='<a class="home" href="'+(n.isHttps==1?"https":"http")+"://"+f+"/pages/Independent/wxjump.html?type=others&rurl="+o+"&token="+l+"&userType="+y+'">',e+='<span class="logimg"><img src="'+n.logo+'"></span>',n.cover?e+='<img src="'+n.cover+'">':e+='<img src="/static/front/images/node.png">',e+='<span class="title">'+n.schoolName+"</span>",e+='<span class="introduce">'+n.introduce+"</span>",e+='<span class="tise" style="line-height:22px;">'+v+"",n.userType==1?e+='<label style="color:red;float:right">(\u5B66\u751F)</label>':e+='<label style="color:red;float:right">(\u8001\u5E08)</label>',e+="</span>",e+="</a>",e+="</li>"}$(".xtwlb").html(e),$(".introduce img").hide()}}}})}zesol();function getMyDate(a){var t=new Date(a),o=t.getFullYear(),r=t.getMonth()+1,s=t.getDate(),p=t.getHours(),i=t.getMinutes(),c=t.getSeconds(),l=o+"-"+getzf(r)+"-"+getzf(s);return l}function getzf(a){return parseInt(a)<10&&(a="0"+a),a}function getCookie(a){for(var t=document.cookie,o=t.split("; "),r=0;r<o.length;r++){var s=o[r].split("=");if(s[0]==a)return s[1]}return""}function getPar(a){var t=document.location.href,o=t.indexOf(a+"=");if(o==-1)return!1;var r=t.slice(a.length+o+1),s=r.indexOf("&");return s!=-1&&(r=r.slice(0,s)),r}