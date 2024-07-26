(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[79540],{79540:function(m,j,N){m=N.nmd(m),ace.define("ace/ext/static.css",["require","exports","module"],function(l,M,v){v.exports=`.ace_static_highlight {
    font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', 'Source Code Pro', 'source-code-pro', 'Droid Sans Mono', monospace;
    font-size: 12px;
    white-space: pre-wrap
}

.ace_static_highlight .ace_gutter {
    width: 2em;
    text-align: right;
    padding: 0 3px 0 0;
    margin-right: 3px;
    contain: none;
}

.ace_static_highlight.ace_show_gutter .ace_line {
    padding-left: 2.6em;
}

.ace_static_highlight .ace_line { position: relative; }

.ace_static_highlight .ace_gutter-cell {
    -moz-user-select: -moz-none;
    -khtml-user-select: none;
    -webkit-user-select: none;
    user-select: none;
    top: 0;
    bottom: 0;
    left: 0;
    position: absolute;
}


.ace_static_highlight .ace_gutter-cell:before {
    content: counter(ace_line, decimal);
    counter-increment: ace_line;
}
.ace_static_highlight {
    counter-reset: ace_line;
}
`}),ace.define("ace/ext/static_highlight",["require","exports","module","ace/edit_session","ace/layer/text","ace/ext/static.css","ace/config","ace/lib/dom","ace/lib/lang"],function(l,M,v){"use strict";var y=l("../edit_session").EditSession,T=l("../layer/text").Text,L=l("./static.css"),C=l("../config"),E=l("../lib/dom"),$=l("../lib/lang").escapeHTML,w=function(){function t(e){this.type=e,this.style={},this.textContent=""}return t.prototype.cloneNode=function(){return this},t.prototype.appendChild=function(e){this.textContent+=e.toString()},t.prototype.toString=function(){var e=[];if(this.type!="fragment"){e.push("<",this.type),this.className&&e.push(" class='",this.className,"'");var a=[];for(var r in this.style)a.push(r,":",this.style[r]);a.length&&e.push(" style='",a.join(""),"'"),e.push(">")}return this.textContent&&e.push(this.textContent),this.type!="fragment"&&e.push("</",this.type,">"),e.join("")},t}(),g={createTextNode:function(t,e){return $(t)},createElement:function(t){return new w(t)},createFragment:function(){return new w("fragment")}},b=function(){this.config={},this.dom=g};b.prototype=T.prototype;var d=function(t,e,a){var r=t.className.match(/lang-(\w+)/),u=e.mode||r&&"ace/mode/"+r[1];if(!u)return!1;var o=e.theme||"ace/theme/textmate",n="",h=[];if(t.firstElementChild)for(var c=0,s=0;s<t.childNodes.length;s++){var i=t.childNodes[s];i.nodeType==3?(c+=i.data.length,n+=i.data):h.push(c,i)}else n=t.textContent,e.trim&&(n=n.trim());d.render(n,u,o,e.firstLineNumber,!e.showGutter,function(p){E.importCssString(p.css,"ace_highlight"),t.innerHTML=p.html;for(var _=t.firstChild.firstChild,f=0;f<h.length;f+=2){var x=p.session.doc.indexToPosition(h[f]),k=h[f+1],S=_.children[x.row];S&&S.appendChild(k)}a&&a()})};d.render=function(t,e,a,r,u,o){var n=1,h=y.prototype.$modes;typeof a=="string"&&(n++,C.loadModule(["theme",a],function(i){a=i,--n||s()}));var c;e&&typeof e=="object"&&!e.getTokenizer&&(c=e,e=c.path),typeof e=="string"&&(n++,C.loadModule(["mode",e],function(i){(!h[e]||c)&&(h[e]=new i.Mode(c)),e=h[e],--n||s()}));function s(){var i=d.renderSync(t,e,a,r,u);return o?o(i):i}return--n||s()},d.renderSync=function(t,e,a,r,u){r=parseInt(r||1,10);var o=new y("");o.setUseWorker(!1),o.setMode(e);var n=new b;n.setSession(o),Object.keys(n.$tabStrings).forEach(function(f){if(typeof n.$tabStrings[f]=="string"){var x=g.createFragment();x.textContent=n.$tabStrings[f],n.$tabStrings[f]=x}}),o.setValue(t);var h=o.getLength(),c=g.createElement("div");c.className=a.cssClass;var s=g.createElement("div");s.className="ace_static_highlight"+(u?"":" ace_show_gutter"),s.style["counter-reset"]="ace_line "+(r-1);for(var i=0;i<h;i++){var p=g.createElement("div");if(p.className="ace_line",!u){var _=g.createElement("span");_.className="ace_gutter ace_gutter-cell",_.textContent="",p.appendChild(_)}n.$renderLine(p,i,!1),p.textContent+=`
`,s.appendChild(p)}return c.appendChild(s),{css:L+a.cssText,html:c.toString(),session:o}},v.exports=d,v.exports.highlight=d}),function(){ace.require(["ace/ext/static_highlight"],function(l){m&&(m.exports=l)})}()}}]);
