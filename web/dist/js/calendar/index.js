var doc=window.document,Calendar=function(t){"use strict";var e={monthNames:["\u4E00\u6708","\u4E8C\u6708","\u4E09\u6708","\u56DB\u6708","\u4E94\u6708","\u516D\u6708","\u4E03\u6708","\u516B\u6708","\u4E5D\u6708","\u5341\u6708","\u5341\u4E00\u6708","\u5341\u4E8C\u6708"],dayNames:["\u5468\u65E5","\u5468\u4E00","\u5468\u4E8C","\u5468\u4E09","\u5468\u56DB","\u5468\u4E94","\u5468\u516D"],dayLongNames:["\u661F\u671F\u65E5","\u661F\u671F\u4E00","\u661F\u671F\u4E8C","\u661F\u671F\u4E09","\u661F\u671F\u56DB","\u661F\u671F\u4E94","\u661F\u671F\u516D"],holiday:{"1-1":"\u5143\u65E6","2-2":"\u6E7F\u5730\u65E5","2-14":"\u60C5\u4EBA\u8282","3-8":"\u5987\u5973\u8282","3-12":"\u690D\u6811\u8282","3-15":"\u6D88\u8D39\u8005\u6743\u76CA\u65E5","4-1":"\u611A\u4EBA\u8282","4-22":"\u5730\u7403\u65E5","5-1":"\u52B3\u52A8\u8282","5-4":"\u9752\u5E74\u8282","5-12":"\u62A4\u58EB\u8282","5-18":"\u535A\u7269\u9986\u65E5","6-1":"\u513F\u7AE5\u8282","6-5":"\u73AF\u5883\u65E5","6-23":"\u5965\u6797\u5339\u514B\u65E5","6-24":"\u9AA8\u8D28\u758F\u677E\u65E5","7-1":"\u5EFA\u515A\u8282","8-1":"\u5EFA\u519B\u8282","9-3":"\u6297\u6218\u80DC\u5229\u65E5","9-10":"\u6559\u5E08\u8282","10-1":"\u56FD\u5E86\u8282","11-17":"\u5B66\u751F\u65E5","12-1":"\u827E\u6ECB\u75C5\u65E5","12-24":"\u5E73\u5B89\u591C","12-25":"\u5723\u8BDE\u8282"},firstDay:1,weekendDays:[0,6],dateFormat:"yyyy-mm-dd",limitDis:80,weekHandler:"dayThead",monthContainer:"dateUl",toolBar:"timeChoose",parentNode:document.getElementById("calendar"),template:'<div class="pannel" id="timePannel"></div><div class="container"><div class="aside"><header class="timeNow"></header><div class="dateContain"><div class="bigTime"></div><div class="noliDate"></div><div class="goodBad"><div class="gooList"></div><div class="badList"></div></div></div></div><div class="main"><div class="operator"><div class="goPrev"><img height="24" src="/js/calendar/img/left.png" /></div><div class="timeChoose"></div><div class="goNext"><img height="24" src="/js/calendar/img/right.png" /></div></div><div class="datePicker"><div class="dayThead"></div><div class="dateUlContainer"><div class="dateUl"><div class="dateLi"><div class="dayTbody"></div></div></div></div></div></div></div>'};t=t||{};for(var a in e)typeof t[a]=="undefined"&&(t[a]=e[a]);this.attrs=t,this.touchesStart={},this.init()};Calendar.prototype={constructor:Calendar,init:function(){this.render(),this.initEvents(),this.layout(),this.index=0,this._initEd=!0,this._interval=!0,this.offsetValue=this.monthEle.offsetWidth,this.timeNowEle=doc.querySelector(".timeNow"),this.bigTimeEle=doc.querySelector(".bigTime"),this.noliDateEle=doc.querySelector(".noliDate"),this.gooList=doc.querySelector(".gooList"),this.badList=doc.querySelector(".badList"),this.timePannel=doc.getElementById("timePannel"),this.prevMonthEle=doc.querySelector(".prev-month-html"),this.currMonthEle=doc.querySelector(".current-month-html"),this.nextMonthEle=doc.querySelector(".next-month-html"),this.setAside()},render:function(){this.attrs.parentNode.innerHTML=this.attrs.template},setAside:function(t){t=t||new Date;var e=t.getFullYear(),a=t.getMonth(),i=t.getDate(),n=Util.getLunarCalendar(e,a+1,i),r=Util.getSuitAndTaboo(e,a+1,i),s="<i>\u5B9C</i>",h="<i>\u5FCC</i>";this.timeNowEle.innerHTML="<span>"+e+"\u5E74"+(a+1)+"\u6708</span><span>"+this.attrs.dayLongNames[t.getDay()]+"</span>",this.bigTimeEle.innerHTML="<span>"+i+"</span>",this.noliDateEle.innerHTML="<p>"+n.month+n.date+"</p><p>"+Util.getSexagenaryCycle(e)+"\u3010"+Util.getZodiac(e)+"\u3011</p>";for(var o=0,l=r.suit.length;l>o;o++)s+="<span>"+r.suit[o]+"</span>";for(var o=0,l=r.taboo.length;l>o;o++)h+="<span>"+r.taboo[o]+"</span>";this.gooList.innerHTML=s,this.badList.innerHTML=h},initEvents:function(){var t=this,e=this.monthEle=doc.querySelector("."+this.attrs.monthContainer);this.timeChooseEle=doc.querySelector(".timeChoose"),this.goPrev=doc.querySelector(".goPrev"),this.goNext=doc.querySelector(".goNext"),e.addEventListener("mousedown",this._handleTouchStart.bind(this),!1),e.addEventListener("mousemove",this._handleTouchMove.bind(this),!1),e.addEventListener("mouseup",this._handleTouchEnd.bind(this),!1),e.addEventListener("touchstart",this._handleTouchStart.bind(this),!1),e.addEventListener("touchmove",this._handleTouchMove.bind(this),!1),e.addEventListener("touchend",this._handleTouchEnd.bind(this),!1),e.addEventListener("click",this._handleClick.bind(this),!1),e.addEventListener("touchstart",this._handleClick.bind(this),!1),this.goPrev.addEventListener("click",function(a){t.turnPre()},!1),this.goNext.addEventListener("click",function(a){t.turnNext()},!1),e.addEventListener("transitionend",this._transformEnd.bind(this),!1),e.addEventListener("webkitTransitionEnd",this._transformEnd.bind(this),!1),this.timeChooseEle.addEventListener("click",this._handleTimeChoose.bind(this),!1),document.addEventListener("click",this._handleDocument.bind(this),!1),window.addEventListener("resize",this._handleResize.bind(this),!1)},_handleDocument:function(t){var e=t.target,a=e.className;if(a!=="pullDown")for(var i=document.querySelectorAll(".buttonGroup"),n=0,r=i.length;r>n;n++)i[n].classList.remove("open")},_handleTimeChoose:function(t){t.preventDefault();var e=t.target,a=e.parentNode,i=e.className;if(e.nodeType===1){if(i==="pullDown"){var n=a.className.split(" ");n.indexOf("open")===-1?a.classList.add("open"):a.classList.remove("open")}if((i==="returnToday"||a.className==="returnToday")&&this.resetDate(new Date),i==="list-year"){var r=parseInt(e.getAttribute("data-year")),s=parseInt(doc.getElementById("op-month-time").textContent)-1;doc.getElementById("op-year-time").textContent=r+"\u5E74",this.resetDate(new Date(r,s))}if(i==="list-month"){var r=parseInt(doc.getElementById("op-year-time").textContent),s=parseInt(e.getAttribute("data-month"));doc.getElementById("op-month-time").textContent=s+"\u6708",this.resetDate(new Date(r,s))}}},_handleResize:function(t){var e=this;e.resizeInterval=setTimeout(function(){e.offsetValue=e.monthEle.offsetWidth},300)},_handleClick:function(t){t.preventDefault();var e=t.target,a=e.nodeName;if(a==="SPAN"||e.className==="dayTd"){var i=a==="SPAN"?e.parentNode:e,n=parseInt(i.getAttribute("data-year")),r=parseInt(i.getAttribute("data-month")),s=parseInt(i.getAttribute("data-day"));this._oldEle&&this._oldEle.classList.remove("date-selected"),i.classList.add("date-selected"),this._oldEle=i,this.setAside(new Date(n,r,s))}},_handleTouchStart:function(t){},_handleTouchMove:function(t){if(t.preventDefault(),this.isTouched){if(this.isMoved=!0,t.type==="touchmove")var e=t.targetTouches[0].pageX;else var e=t.pageX;this.touchesDiff=e-this.touchesStart.x,this.endPos=e,this.monthEle.style[Util.prefix+"transition"]="all 0ms",this.monthEle.style[Util.prefix+"transform"]="translate3d("+(this.index*this.offsetValue+this.touchesDiff)+"px, 0px, 0px)"}},_handleTouchEnd:function(t){if(t.preventDefault(),!this.isTouched||!this.isMoved)return this.isTouched=!1,void(this.isMoved=!1);var e=this.endPos-this.touchesStart.x;Math.abs(e)<this.attrs.limitDis?this._transformPage():0>e?this.turnNext():this.turnPre(),this.isTouched=!1,this.isMoved=!1},turnPre:function(){this._interval&&(this.index++,this._isTurnPage=!0,this._offset="prev",this._interval=!1,this._transformPage())},turnNext:function(){this._interval&&(this.index--,this._isTurnPage=!0,this._offset="next",this._interval=!1,this._transformPage())},_transformPage:function(){this.monthEle.style[Util.prefix+"transition"]="300ms",this.monthEle.style[Util.prefix+"transform"]="translate3d("+100*this.index+"%, 0, 0)"},_tipPannel:function(){var t=this;if(!(this.offsetValue>425)){var e=this.value.getFullYear(),a=this.value.getMonth()+1;this.timePannel.textContent=e+"\u5E74"+a+"\u6708",clearTimeout(this.tipInterval),this.timePannel.style.display="block",t.timePannel.offsetWidth,this.timePannel.style.opacity=1,this.tipInterval=setTimeout(function(){t.timePannel.style.opacity=0,t.timePannel.style.display="none"},800)}},_transformEnd:function(){var t=this._offset;if(this._isTurnPage){var e=new Date(this.value),a=e.getFullYear(),i=e.getMonth();t==="next"&&(e=i===11?new Date(a+1,0):new Date(a,i+1,1)),t==="prev"&&(e=i===0?new Date(a-1,11):new Date(a,i-1,1)),this.value=e,this._tipPannel(),this.layout();var n=-1*this.index;this.prevMonthEle=doc.querySelector(".prev-month-html"),this.currMonthEle=doc.querySelector(".current-month-html"),this.nextMonthEle=doc.querySelector(".next-month-html"),this.prevMonthEle.style[Util.prefix+"transform"]="translate3d("+100*(n-1)+"%, 0px, 0px)",this.currMonthEle.style[Util.prefix+"transform"]="translate3d("+100*n+"%, 0px, 0px)",this.nextMonthEle.style[Util.prefix+"transform"]="translate3d("+100*(n+1)+"%, 0px, 0px)",doc.getElementById("op-year-time").textContent=this.value.getFullYear()+"\u5E74",doc.getElementById("op-month-time").textContent=this.value.getMonth()+1+"\u6708",this._interval=!0,this._isTurnPage=!1}},resetDate:function(t){this.value=t,this.index=0,this.monthEle.style[Util.prefix+"transition"]="all 0ms",this.monthEle.style[Util.prefix+"transform"]="translate3d(0 ,0 ,0)",this.prevMonthEle.style[Util.prefix+"transform"]="translate3d(-100%, 0px, 0px)",this.currMonthEle.style[Util.prefix+"transform"]="translate3d(0%, 0px, 0px)",this.nextMonthEle.style[Util.prefix+"transform"]="translate3d(100%, 0px, 0px)",doc.getElementById("op-year-time").textContent=this.value.getFullYear()+"\u5E74",doc.getElementById("op-month-time").textContent=this.value.getMonth()+1+"\u6708",this.layout()},layout:function(){var t=this.value?this.value:new Date().setHours(0,0,0,0);this.value=t;var e=this.monthHTML(t,"prev"),a=this.monthHTML(t),i=this.monthHTML(t,"next"),n='<div class="dateLi prev-month-html"><div class="dayTbody">'+e+'</div></div><div class="dateLi current-month-html"><div class="dayTbody">'+a+'</div></div><div class="dateLi next-month-html"><div class="dayTbody">'+i+"</div></div>";if(!this._initEd){for(var r=[],s=0;7>s;s++){var h=s+this.attrs.firstDay>6?s-7+this.attrs.firstDay:s+this.attrs.firstDay,o=this.attrs.dayNames[h];this.attrs.weekendDays.indexOf(h)!==-1?r.push('<div class="dayTd active">'+o+"</div>"):r.push('<div class="dayTd">'+o+"</div>")}for(var l=[],v=[],s=1900;2050>=s;s++){var d="<li class='list-year' data-year='"+s+"'>"+s+"\u5E74</li>";l.push(d)}for(var s=1;12>=s;s++){var d="<li class='list-month' data-month='"+(s-1)+"'>"+s+"\u6708</li>";v.push(d)}doc.querySelector("."+this.attrs.weekHandler).innerHTML=r.join(""),doc.querySelector("."+this.attrs.toolBar).innerHTML='<div class="yearChoose"><div class="chooseContainer"><div class="buttonGroup"><span class="yearTime" id="op-year-time">'+new Date().getFullYear()+'\u5E74</span><span class="pullDown">+</span></div><div class="pullSelect"><ul>'+l.join("")+'</ul></div></div></div><div class="monthChoose"><div class="chooseContainer"><div class="buttonGroup"><span class="monthTime" id="op-month-time">'+(new Date().getMonth()+1)+'\u6708</span><span class="pullDown">+</span></div><div class="pullSelect"><ul>'+v.join("")+'</ul></div></div></div><div class="returnToday"><span>\u8FD4\u56DE\u4ECA\u5929</span></div>'}doc.querySelector("."+this.attrs.monthContainer).innerHTML=n},totalDaysInMonth:function(t){var e=new Date(t);return new Date(e.getFullYear(),e.getMonth()+1,0).getDate()},monthHTML:function(t,e){var t=new Date(t),a=t.getFullYear(),i=t.getMonth();t.getDate(),e==="next"&&(t=i===11?new Date(a+1,0):new Date(a,i+1,1)),e==="prev"&&(t=i===0?new Date(a-1,11):new Date(a,i-1,1)),(e==="next"||e==="prev")&&(i=t.getMonth(),a=t.getFullYear());var n=this.totalDaysInMonth(new Date(t.getFullYear(),t.getMonth()).getTime()-864e6),r=this.totalDaysInMonth(t),s=new Date(t.getFullYear(),t.getMonth()).getDay();s===0&&(s=7);for(var h,o=6,l=7,v=[],d=0+(this.attrs.firstDay-1),c=new Date().setHours(0,0,0,0),y=1;o>=y;y++){for(var E=[],g=1;l>=g;g++){d++;var u=d-s,m=["dayTd"];0>u?(m.push("date-prev"),u=n+u+1,h=new Date(0>i-1?a-1:a,0>i-1?11:i-1,u).getTime()):(u+=1,u>r?(m.push("date-next"),u-=r,h=new Date(i+1>11?a+1:a,i+1>11?0:i+1,u).getTime()):h=new Date(a,i,u).getTime()),h===c&&m.push("date-current"),(d%7===this.attrs.weekendDays[0]||d%7===this.attrs.weekendDays[1])&&m.push("date-reset"),h=new Date(h);var x=h.getFullYear(),f=h.getMonth(),p=Util.getLunarCalendar(x,f+1,u),D=this.attrs.holiday[f+1+"-"+u];if(D){var T=D;m.push("date-holiday")}else if(p.festival){var T=p.festival;m.push("date-holiday")}else if(p.solarTerm){var T=p.solarTerm;m.push("date-holiday")}else var T=p.date;E.push('<div class="'+m.join(" ")+'" data-year='+x+" data-month="+f+" data-day="+u+'><span class="dayNumber">'+u+'</span><span class="almanac">'+T+"</span></div>")}v.push('<div class="dayTr">'+E.join("")+"</div>")}return v.join("")},formatDate:function(t){t=new Date(t);var e=t.getFullYear(),a=t.getMonth(),i=a+1,n=t.getDate(),r=t.getDay();return this.attrs.dateFormat.replace(/yyyy/g,e).replace(/yy/g,(e+"").substring(2)).replace(/mm/g,10>i?"0"+i:i).replace(/m/g,i).replace(/MM/g,this.attrs.monthNames[a]).replace(/dd/g,10>n?"0"+n:n).replace(/d/g,n).replace(/DD/g,this.attrs.dayNames[r])}};var Util={prefix:function(){var t=document.createElement("div"),e="-webkit-transition:all .1s; -moz-transition:all .1s; -o-transition:all .1s; -ms-transition:all .1s; transition:all .1s;";t.style.cssText=e;var a=t.style;return a.transition?"":a.webkitTransition?"-webkit-":a.MozTransition?"-moz-":a.oTransition?"-o-":a.msTransition?"-ms-":void 0}(),_gregorianCalendarToAccumulateDate:function(t,e,a){var i=0;i+=365*(t-1900),i+=parseInt((t-1901)/4);for(var n=e-1;n>0;n--)i+=new Date(t,n,0).getDate();return i+=a},_getLunarDate:function(t){var e=parseInt((t-1.6)/29.5306),a=e-1;do{a++;var i=parseInt(1.6+29.5306*a+.4*Math.sin(1-.45058*a)),n=t-i+1}while(n>=30);return n===0?30:n},_getSolarTermAccumulateDate:function(t,e){var a=parseInt(365.242*t+6.2+15.22*e-1.9*Math.sin(.262*e));return a},getLunarCalendar:function(t,e,a){var i=["\u6B63\u6708","\u4E8C\u6708","\u4E09\u6708","\u56DB\u6708","\u4E94\u6708","\u516D\u6708","\u4E03\u6708","\u516B\u6708","\u4E5D\u6708","\u5341\u6708","\u5341\u4E00\u6708","\u814A\u6708"],n=["\u5341","\u4E00","\u4E8C","\u4E09","\u56DB","\u4E94","\u516D","\u4E03","\u516B","\u4E5D"],r=["\u521D","\u5341","\u5EFF","\u4E09"],s=["\u5C0F\u5BD2","\u5927\u5BD2","\u7ACB\u6625","\u96E8\u6C34","\u60CA\u86F0","\u6625\u5206","\u6E05\u660E","\u8C37\u96E8","\u7ACB\u590F","\u5C0F\u6EE1","\u8292\u79CD","\u590F\u81F3","\u5C0F\u6691","\u5927\u6691","\u7ACB\u79CB","\u5904\u6691","\u9732\u6C34","\u79CB\u5206","\u5BD2\u9732","\u971C\u964D","\u7ACB\u51AC","\u5C0F\u96EA","\u5927\u96EA","\u51AC\u81F3"],h={"0101":"\u6625\u8282","0115":"\u5143\u5BB5\u8282","0202":"\u9F99\u5934\u8282","0505":"\u7AEF\u5348","0707":"\u4E03\u5915","0715":"\u4E2D\u5143\u8282","0815":"\u4E2D\u79CB","0909":"\u91CD\u9633\u8282",1001:"\u5BD2\u8863\u8282",1015:"\u4E0B\u5143\u8282",1208:"\u814A\u516B\u8282",1223:"\u5C0F\u5E74",1230:"\u9664\u5915"},o=t-1900,l=2*e-1,v=(e-1+12)%12,d=Util._gregorianCalendarToAccumulateDate(t,e,a),c=Util._getLunarDate(d),y=Util._getLunarDate(Util._getSolarTermAccumulateDate(o,l)),E=(Util._getLunarDate(Util._getSolarTermAccumulateDate(o,l-2)),Util._getLunarDate(Util._getSolarTermAccumulateDate(o,l+2))),g=(Util._getLunarDate(Util._getSolarTermAccumulateDate(o,l-1)),Util._getLunarDate(Util._getSolarTermAccumulateDate(o,l+1)),Util._getSolarTermAccumulateDate(o,l)),u=(Util._getSolarTermAccumulateDate(o,l-2),Util._getSolarTermAccumulateDate(o,l+2)),m=Util._getSolarTermAccumulateDate(o,l-1),x=Util._getSolarTermAccumulateDate(o,l+1);if(g>d&&c+g-d!==y?v--:d>g&&c-(d-g)!==y&&c+E-d===u&&v++,g===d)var f=s[l];else if(m===d)var f=s[l-1];else if(x===d)var f=s[l+1];var p=v;if(10>p)var D="0"+p;else var D=p;D+=10>c?"0"+c:c;var T=h[D],M=i[(p+11)%12],L=parseInt((c-1)/10);if(c===20||c===30)var _=r[L+1];else var _=r[L];_+=n[c%10];var w={month:M,date:_,solarTerm:f,festival:T};return w},getSexagenaryCycle:function(t){var e=["\u7678","\u7532","\u4E59","\u4E19","\u4E01","\u620A","\u5DF1","\u5E9A","\u8F9B","\u58EC"],a=["\u4EA5","\u5B50","\u4E11","\u5BC5","\u536F","\u8FB0","\u5DF3","\u5348","\u672A","\u7533","\u9149","\u620C"],i=t-1863,n=e[i%10];return n+=a[i%12],n+="\u5E74"},getZodiac:function(t){var e=["\u732A\u5E74","\u9F20\u5E74","\u725B\u5E74","\u864E\u5E74","\u5154\u5E74","\u9F99\u5E74","\u86C7\u5E74","\u9A6C\u5E74","\u7F8A\u5E74","\u7334\u5E74","\u9E21\u5E74","\u72D7\u5E74"],a=t-1863;return e[a%12]},getSuitAndTaboo:function(t,e,a){var i=["\u5F00\u5149","\u5AC1\u5A36","\u5165\u5B85","\u4E0A\u6881","\u796D\u7940","\u51FA\u884C","\u4F5C\u7076","\u7834\u571F","\u8BA2\u76DF","\u7948\u798F"],n=["\u7EB3\u91C7","\u51A0\u7B04","\u7AD6\u67F1","\u6398\u4E95","\u4F10\u6728","\u7406\u53D1","\u4EA4\u6613","\u63A2\u75C5","\u96D5\u523B","\u658B\u91AE"],r=parseInt(t*e*a%1025).toString(2),s=r.length;if(10>s)for(;10>s;s++)r="0"+r;r=r.split("").reverse().join("");for(var h=parseInt(r,2),o=[],l=[],v=0;10>v;v++)h%2?o.push(n[v]):l.push(i[v]),h=parseInt(h/2);var d=o,c=l,y={suit:d,taboo:c};return y}};