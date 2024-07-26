layui.define("jquery",function(V){"use strict";var s=layui.jquery,T={config:{},index:layui.colorpicker?layui.colorpicker.index+1e4:0,set:function(e){var i=this;return i.config=s.extend({},i.config,e),i},on:function(e,i){return layui.onevent.call(this,"colorpicker",e,i)}},Z=function(){var e=this,i=e.config;return{config:i}},_="colorpicker",$="layui-show",S="layui-colorpicker",w=".layui-colorpicker-main",j="layui-icon-down",F="layui-icon-close",D="layui-colorpicker-trigger-span",X="layui-colorpicker-trigger-i",ee="layui-colorpicker-side",q="layui-colorpicker-side-slider",R="layui-colorpicker-basis",O="layui-colorpicker-alpha-bgcolor",L="layui-colorpicker-alpha-slider",G="layui-colorpicker-basis-cursor",E="layui-colorpicker-main-input",H=function(e){var i={h:0,s:0,b:0},r=Math.min(e.r,e.g,e.b),n=Math.max(e.r,e.g,e.b),o=n-r;return i.b=n,i.s=n!=0?255*o/n:0,i.s!=0?e.r==n?i.h=(e.g-e.b)/o:e.g==n?i.h=2+(e.b-e.r)/o:i.h=4+(e.r-e.g)/o:i.h=-1,n==r&&(i.h=0),i.h*=60,i.h<0&&(i.h+=360),i.s*=100/255,i.b*=100/255,i},ie=function(e){var e=e.indexOf("#")>-1?e.substring(1):e;if(e.length==3){var i=e.split("");e=i[0]+i[0]+i[1]+i[1]+i[2]+i[2]}e=parseInt(e,16);var r={r:e>>16,g:(65280&e)>>8,b:255&e};return H(r)},J=function(e){var i={},r=e.h,n=255*e.s/100,o=255*e.b/100;if(n==0)i.r=i.g=i.b=o;else{var a=o,l=(255-n)*o/255,t=(a-l)*(r%60)/60;r==360&&(r=0),r<60?(i.r=a,i.b=l,i.g=l+t):r<120?(i.g=a,i.b=l,i.r=a-t):r<180?(i.g=a,i.r=l,i.b=l+t):r<240?(i.b=a,i.r=l,i.g=a-t):r<300?(i.b=a,i.g=l,i.r=l+t):r<360?(i.r=a,i.g=l,i.b=a-t):(i.r=0,i.g=0,i.b=0)}return{r:Math.round(i.r),g:Math.round(i.g),b:Math.round(i.b)}},M=function(e){var i=J(e),r=[i.r.toString(16),i.g.toString(16),i.b.toString(16)];return s.each(r,function(n,o){o.length==1&&(r[n]="0"+o)}),r.join("")},W=function(e){var i=/[0-9]{1,3}/g,r=e.match(i)||[];return{r:r[0],g:r[1],b:r[2]}},z=s(window),oe=s(document),m=function(e){var i=this;i.index=++T.index,i.config=s.extend({},i.config,T.config,e),i.render()};m.prototype.config={color:"",size:null,alpha:!1,format:"hex",predefine:!1,colors:["#009688","#5FB878","#1E9FFF","#FF5722","#FFB800","#01AAED","#999","#c00","#ff8c00","#ffd700","#90ee90","#00ced1","#1e90ff","#c71585","rgb(0, 186, 189)","rgb(255, 120, 0)","rgb(250, 212, 0)","#393D49","rgba(0,0,0,.5)","rgba(255, 69, 0, 0.68)","rgba(144, 240, 144, 0.5)","rgba(31, 147, 255, 0.73)"]},m.prototype.render=function(){var e=this,i=e.config,r=s(['<div class="layui-unselect layui-colorpicker">',"<span "+(i.format=="rgb"&&i.alpha?'class="layui-colorpicker-trigger-bgcolor"':"")+">",'<span class="layui-colorpicker-trigger-span" ','lay-type="'+(i.format=="rgb"?i.alpha?"rgba":"torgb":"")+'" ','style="'+function(){var o="";return i.color?(o=i.color,(i.color.match(/[0-9]{1,3}/g)||[]).length>3&&(i.alpha&&i.format=="rgb"||(o="#"+M(H(W(i.color))))),"background: "+o):o}()+'">','<i class="layui-icon layui-colorpicker-trigger-i '+(i.color?j:F)+'"></i>',"</span>","</span>","</div>"].join("")),n=s(i.elem);i.size&&r.addClass("layui-colorpicker-"+i.size),n.addClass("layui-inline").html(e.elemColorBox=r),e.color=e.elemColorBox.find("."+D)[0].style.background,e.events()},m.prototype.renderPicker=function(){var e=this,i=e.config,r=e.elemColorBox[0],n=e.elemPicker=s(['<div id="layui-colorpicker'+e.index+'" data-index="'+e.index+'" class="layui-anim layui-anim-upbit layui-colorpicker-main">','<div class="layui-colorpicker-main-wrapper">','<div class="layui-colorpicker-basis">','<div class="layui-colorpicker-basis-white"></div>','<div class="layui-colorpicker-basis-black"></div>','<div class="layui-colorpicker-basis-cursor"></div>',"</div>",'<div class="layui-colorpicker-side">','<div class="layui-colorpicker-side-slider"></div>',"</div>","</div>",'<div class="layui-colorpicker-main-alpha '+(i.alpha?$:"")+'">','<div class="layui-colorpicker-alpha-bgcolor">','<div class="layui-colorpicker-alpha-slider"></div>',"</div>","</div>",function(){if(i.predefine){var o=['<div class="layui-colorpicker-main-pre">'];return layui.each(i.colors,function(a,l){o.push(['<div class="layui-colorpicker-pre'+((l.match(/[0-9]{1,3}/g)||[]).length>3?" layui-colorpicker-pre-isalpha":"")+'">','<div style="background:'+l+'"></div>',"</div>"].join(""))}),o.push("</div>"),o.join("")}return""}(),'<div class="layui-colorpicker-main-input">','<div class="layui-inline">','<input type="text" class="layui-input">',"</div>",'<div class="layui-btn-container">','<button class="layui-btn layui-btn-primary layui-btn-sm" colorpicker-events="clear">\u6E05\u7A7A</button>','<button class="layui-btn layui-btn-sm" colorpicker-events="confirm">\u786E\u5B9A</button>',"</div","</div>","</div>"].join(""));e.elemColorBox.find("."+D)[0],s(w)[0]&&s(w).data("index")==e.index?e.removePicker(m.thisElemInd):(e.removePicker(m.thisElemInd),s("body").append(n)),m.thisElemInd=e.index,m.thisColor=r.style.background,e.position(),e.pickerEvents()},m.prototype.removePicker=function(e){var i=this;return i.config,s("#layui-colorpicker"+(e||i.index)).remove(),i},m.prototype.position=function(){var e=this,i=e.config,r=e.bindElem||e.elemColorBox[0],n=e.elemPicker[0],o=r.getBoundingClientRect(),a=n.offsetWidth,l=n.offsetHeight,t=function(k){return k=k?"scrollLeft":"scrollTop",document.body[k]|document.documentElement[k]},g=function(k){return document.documentElement[k?"clientWidth":"clientHeight"]},p=5,h=o.left,y=o.bottom;h-=(a-r.offsetWidth)/2,y+=p,h+a+p>g("width")?h=g("width")-a-p:h<p&&(h=p),y+l+p>g()&&(y=o.top>l?o.top-l:g()-l,y-=2*p),i.position&&(n.style.position=i.position),n.style.left=h+(i.position==="fixed"?0:t(1))+"px",n.style.top=y+(i.position==="fixed"?0:t())+"px"},m.prototype.val=function(){var e=this,i=(e.config,e.elemColorBox.find("."+D)),r=e.elemPicker.find("."+E),n=i[0],o=n.style.backgroundColor;if(o){var a=H(W(o)),l=i.attr("lay-type");if(e.select(a.h,a.s,a.b),l==="torgb"&&r.find("input").val(o),l==="rgba"){var t=W(o);if((o.match(/[0-9]{1,3}/g)||[]).length==3)r.find("input").val("rgba("+t.r+", "+t.g+", "+t.b+", 1)"),e.elemPicker.find("."+L).css("left",280);else{r.find("input").val(o);var g=280*o.slice(o.lastIndexOf(",")+1,o.length-1);e.elemPicker.find("."+L).css("left",g)}e.elemPicker.find("."+O)[0].style.background="linear-gradient(to right, rgba("+t.r+", "+t.g+", "+t.b+", 0), rgb("+t.r+", "+t.g+", "+t.b+"))"}}else e.select(0,100,100),r.find("input").val(""),e.elemPicker.find("."+O)[0].style.background="",e.elemPicker.find("."+L).css("left",280)},m.prototype.side=function(){var e=this,i=e.config,r=e.elemColorBox.find("."+D),n=r.attr("lay-type"),o=e.elemPicker.find("."+ee),a=e.elemPicker.find("."+q),l=e.elemPicker.find("."+R),t=e.elemPicker.find("."+G),g=e.elemPicker.find("."+O),p=e.elemPicker.find("."+L),h=a[0].offsetTop/180*360,y=100-(t[0].offsetTop+3)/180*100,k=(t[0].offsetLeft+3)/260*100,P=Math.round(p[0].offsetLeft/280*100)/100,re=e.elemColorBox.find("."+X),te=e.elemPicker.find(".layui-colorpicker-pre").children("div"),B=function(u,c,d,v){e.select(u,c,d);var f=J({h:u,s:c,b:d});if(re.addClass(j).removeClass(F),r[0].style.background="rgb("+f.r+", "+f.g+", "+f.b+")",n==="torgb"&&e.elemPicker.find("."+E).find("input").val("rgb("+f.r+", "+f.g+", "+f.b+")"),n==="rgba"){var b=0;b=280*v,p.css("left",b),e.elemPicker.find("."+E).find("input").val("rgba("+f.r+", "+f.g+", "+f.b+", "+v+")"),r[0].style.background="rgba("+f.r+", "+f.g+", "+f.b+", "+v+")",g[0].style.background="linear-gradient(to right, rgba("+f.r+", "+f.g+", "+f.b+", 0), rgb("+f.r+", "+f.g+", "+f.b+"))"}i.change&&i.change(e.elemPicker.find("."+E).find("input").val())},Y=s(['<div class="layui-auxiliar-moving" id="LAY-colorpicker-moving"></div'].join("")),A=function(u){s("#LAY-colorpicker-moving")[0]||s("body").append(Y),Y.on("mousemove",u),Y.on("mouseup",function(){Y.remove()}).on("mouseleave",function(){Y.remove()})};a.on("mousedown",function(u){var c=this.offsetTop,d=u.clientY,v=function(f){var b=c+(f.clientY-d),C=o[0].offsetHeight;b<0&&(b=0),b>C&&(b=C);var x=b/180*360;h=x,B(x,k,y,P),f.preventDefault()};A(v),u.preventDefault()}),o.on("click",function(u){var c=u.clientY-s(this).offset().top;c<0&&(c=0),c>this.offsetHeight&&(c=this.offsetHeight);var d=c/180*360;h=d,B(d,k,y,P),u.preventDefault()}),t.on("mousedown",function(u){var c=this.offsetTop,d=this.offsetLeft,v=u.clientY,f=u.clientX,b=function(C){var x=c+(C.clientY-v),I=d+(C.clientX-f),K=l[0].offsetHeight-3,N=l[0].offsetWidth-3;x<-3&&(x=-3),x>K&&(x=K),I<-3&&(I=-3),I>N&&(I=N);var Q=(I+3)/260*100,U=100-(x+3)/180*100;y=U,k=Q,B(h,Q,U,P),C.preventDefault()};A(b),u.preventDefault()}),l.on("click",function(u){var c=u.clientY-s(this).offset().top-3+z.scrollTop(),d=u.clientX-s(this).offset().left-3+z.scrollLeft();c<-3&&(c=-3),c>this.offsetHeight-3&&(c=this.offsetHeight-3),d<-3&&(d=-3),d>this.offsetWidth-3&&(d=this.offsetWidth-3);var v=(d+3)/260*100,f=100-(c+3)/180*100;y=f,k=v,B(h,v,f,P),u.preventDefault()}),p.on("mousedown",function(u){var c=this.offsetLeft,d=u.clientX,v=function(f){var b=c+(f.clientX-d),C=g[0].offsetWidth;b<0&&(b=0),b>C&&(b=C);var x=Math.round(b/280*100)/100;P=x,B(h,k,y,x),f.preventDefault()};A(v),u.preventDefault()}),g.on("click",function(u){var c=u.clientX-s(this).offset().left;c<0&&(c=0),c>this.offsetWidth&&(c=this.offsetWidth);var d=Math.round(c/280*100)/100;P=d,B(h,k,y,d),u.preventDefault()}),te.each(function(){s(this).on("click",function(){s(this).parent(".layui-colorpicker-pre").addClass("selected").siblings().removeClass("selected");var u,c=this.style.backgroundColor,d=H(W(c)),v=c.slice(c.lastIndexOf(",")+1,c.length-1);h=d.h,k=d.s,y=d.b,(c.match(/[0-9]{1,3}/g)||[]).length==3&&(v=1),P=v,u=280*v,B(d.h,d.s,d.b,v)})})},m.prototype.select=function(e,i,r,n){var o=this,a=(o.config,M({h:e,s:100,b:100})),l=M({h:e,s:i,b:r}),t=e/360*180,g=180-r/100*180-3,p=i/100*260-3;o.elemPicker.find("."+q).css("top",t),o.elemPicker.find("."+R)[0].style.background="#"+a,o.elemPicker.find("."+G).css({top:g,left:p}),n!=="change"&&o.elemPicker.find("."+E).find("input").val("#"+l)},m.prototype.pickerEvents=function(){var e=this,i=e.config,r=e.elemColorBox.find("."+D),n=e.elemPicker.find("."+E+" input"),o={clear:function(a){r[0].style.background="",e.elemColorBox.find("."+X).removeClass(j).addClass(F),e.color="",i.done&&i.done(""),e.removePicker()},confirm:function(a,l){var t=n.val(),g=t,p={};if(t.indexOf(",")>-1){if(p=H(W(t)),e.select(p.h,p.s,p.b),r[0].style.background=g="#"+M(p),(t.match(/[0-9]{1,3}/g)||[]).length>3&&r.attr("lay-type")==="rgba"){var h=280*t.slice(t.lastIndexOf(",")+1,t.length-1);e.elemPicker.find("."+L).css("left",h),r[0].style.background=t,g=t}}else p=ie(t),r[0].style.background=g="#"+M(p),e.elemColorBox.find("."+X).removeClass(F).addClass(j);return l==="change"?(e.select(p.h,p.s,p.b,l),void(i.change&&i.change(g))):(e.color=t,i.done&&i.done(t),void e.removePicker())}};e.elemPicker.on("click","*[colorpicker-events]",function(){var a=s(this),l=a.attr("colorpicker-events");o[l]&&o[l].call(this,a)}),n.on("keyup",function(a){var l=s(this);o.confirm.call(this,l,a.keyCode===13?null:"change")})},m.prototype.events=function(){var e=this,i=e.config,r=e.elemColorBox.find("."+D);e.elemColorBox.on("click",function(){e.renderPicker(),s(w)[0]&&(e.val(),e.side())}),i.elem[0]&&!e.elemColorBox[0].eventHandler&&(oe.on("click",function(n){if(!s(n.target).hasClass(S)&&!s(n.target).parents("."+S)[0]&&!s(n.target).hasClass(w.replace(/\./g,""))&&!s(n.target).parents(w)[0]&&e.elemPicker){if(e.color){var o=H(W(e.color));e.select(o.h,o.s,o.b)}else e.elemColorBox.find("."+X).removeClass(j).addClass(F);r[0].style.background=e.color||"",e.removePicker()}}),z.on("resize",function(){return!(!e.elemPicker||!s(w)[0])&&void e.position()}),e.elemColorBox[0].eventHandler=!0)},T.render=function(e){var i=new m(e);return Z.call(i)},V(_,T)});