(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[26260],{26260:function(p,$,L){p=L.nmd(p),ace.define("ace/ext/whitespace",["require","exports","module","ace/lib/lang"],function(T,i,_){"use strict";var w=T("../lib/lang");i.$detectIndentation=function(e,n){for(var t=[],s=[],h=0,c=0,o=Math.min(e.length,1e3),r=0;r<o;r++){var a=e[r];if(!!/^\s*[^*+\-\s]/.test(a)){if(a[0]=="	")h++,c=-Number.MAX_VALUE;else{var u=a.match(/^ */)[0].length;if(u&&a[u]!="	"){var f=u-c;f>0&&!(c%f)&&!(u%f)&&(s[f]=(s[f]||0)+1),t[u]=(t[u]||0)+1}c=u}for(;r<o&&a[a.length-1]=="\\";)a=e[r++]}}function d(S){for(var I=0,A=S;A<t.length;A+=S)I+=t[A]||0;return I}for(var b=s.reduce(function(S,I){return S+I},0),v={score:0,length:0},l=0,r=1;r<12;r++){var g=d(r);r==1?(l=g,g=t[1]?.9:.8,t.length||(g=0)):g/=l,s[r]&&(g+=s[r]/b),g>v.score&&(v={score:g,length:r})}if(v.score&&v.score>1.4)var m=v.length;if(h>l+1)return(m==1||l<h/4||v.score<1.8)&&(m=void 0),{ch:"	",length:m};if(l>h+1)return{ch:" ",length:m}},i.detectIndentation=function(e){var n=e.getLines(0,1e3),t=i.$detectIndentation(n)||{};return t.ch&&e.setUseSoftTabs(t.ch==" "),t.length&&e.setTabSize(t.length),t},i.trimTrailingSpace=function(e,n){var t=e.getDocument(),s=t.getAllLines(),h=n&&n.trimEmpty?-1:0,c=[],o=-1;n&&n.keepCursorPosition&&(e.selection.rangeCount?e.selection.rangeList.ranges.forEach(function(b,v,l){var g=l[v+1];g&&g.cursor.row==b.cursor.row||c.push(b.cursor)}):c.push(e.selection.getCursor()),o=0);for(var r=c[o]&&c[o].row,a=0,u=s.length;a<u;a++){var f=s[a],d=f.search(/\s+$/);a==r&&(d<c[o].column&&d>h&&(d=c[o].column),o++,r=c[o]?c[o].row:-1),d>h&&t.removeInLine(a,d,f.length)}},i.convertIndentation=function(e,n,t){var s=e.getTabString()[0],h=e.getTabSize();t||(t=h),n||(n=s);for(var c=n=="	"?n:w.stringRepeat(n,t),o=e.doc,r=o.getAllLines(),a={},u={},f=0,d=r.length;f<d;f++){var b=r[f],v=b.match(/^\s*/)[0];if(v){var l=e.$getStringScreenWidth(v)[0],g=Math.floor(l/h),m=l%h,S=a[g]||(a[g]=w.stringRepeat(c,g));S+=u[m]||(u[m]=w.stringRepeat(" ",m)),S!=v&&(o.removeInLine(f,0,v.length),o.insertInLine({row:f,column:0},S))}}e.setTabSize(t),e.setUseSoftTabs(n==" ")},i.$parseStringArg=function(e){var n={};/t/.test(e)?n.ch="	":/s/.test(e)&&(n.ch=" ");var t=e.match(/\d+/);return t&&(n.length=parseInt(t[0],10)),n},i.$parseArg=function(e){return e?typeof e=="string"?i.$parseStringArg(e):typeof e.text=="string"?i.$parseStringArg(e.text):e:{}},i.commands=[{name:"detectIndentation",description:"Detect indentation from content",exec:function(e){i.detectIndentation(e.session)}},{name:"trimTrailingSpace",description:"Trim trailing whitespace",exec:function(e,n){i.trimTrailingSpace(e.session,n)}},{name:"convertIndentation",description:"Convert indentation to ...",exec:function(e,n){var t=i.$parseArg(n);i.convertIndentation(e.session,t.ch,t.length)}},{name:"setIndentation",description:"Set indentation",exec:function(e,n){var t=i.$parseArg(n);t.length&&e.session.setTabSize(t.length),t.ch&&e.session.setUseSoftTabs(t.ch==" ")}}]}),function(){ace.require(["ace/ext/whitespace"],function(T){p&&(p.exports=T)})}()}}]);