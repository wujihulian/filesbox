layui.define(function(i){"use strict";var r={open:"{{",close:"}}"},p={exp:function(e){return new RegExp(e,"g")},query:function(e,n,t){var a=["#([\\s\\S])+?","([^{#}])*?"][e||0];return o((n||"")+r.open+a+r.close+(t||""))},escape:function(e){return String(e||"").replace(/&(?!#?[a-zA-Z0-9]+;)/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/'/g,"&#39;").replace(/"/g,"&quot;")},error:function(e,n){var t="Laytpl Error\uFF1A";return typeof console=="object"&&console.error(t+e+`
`+(n||"")),t+e}},o=p.exp,l=function(e){this.tpl=e};l.pt=l.prototype,window.errors=0,l.pt.parse=function(e,n){var t=this,a=e,f=o("^"+r.open+"#",""),g=o(r.close+"$","");e=e.replace(/\s+|\r|\t|\n/g," ").replace(o(r.open+"#"),r.open+"# ").replace(o(r.close+"}"),"} "+r.close).replace(/\\/g,"\\\\").replace(o(r.open+"!(.+?)!"+r.close),function(c){return c=c.replace(o("^"+r.open+"!"),"").replace(o("!"+r.close),"").replace(o(r.open+"|"+r.close),function(u){return u.replace(/(.)/g,"\\$1")})}).replace(/(?="|')/g,"\\").replace(p.query(),function(c){return c=c.replace(f,"").replace(g,""),'";'+c.replace(/\\/g,"")+';view+="'}).replace(p.query(1),function(c){var u='"+(';return c.replace(/\s/g,"")===r.open+r.close?"":(c=c.replace(o(r.open+"|"+r.close),""),/^=/.test(c)&&(c=c.replace(/^=/,""),u='"+_escape_('),u+c.replace(/\\/g,"")+')+"')}),e='"use strict";var view = "'+e+'";return view;';try{return t.cache=e=new Function("d, _escape_",e),e(n,p.escape)}catch(c){return delete t.cache,p.error(c,a)}},l.pt.render=function(e,n){var t,a=this;return e?(t=a.cache?a.cache(e,p.escape):a.parse(a.tpl,e),n?void n(t):t):p.error("no data")};var s=function(e){return typeof e!="string"?p.error("Template not found"):new l(e)};s.config=function(e){e=e||{};for(var n in e)r[n]=e[n]},s.v="1.2.0",i("laytpl",s)});