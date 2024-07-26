(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[74283],{74283:function($,R,b){$=b.nmd($),ace.define("ace/ext/code_lens",["require","exports","module","ace/line_widgets","ace/lib/event","ace/lib/lang","ace/lib/dom","ace/editor","ace/config"],function(r,p,O){"use strict";var y=r("../line_widgets").LineWidgets,k=r("../lib/event"),W=r("../lib/lang"),w=r("../lib/dom");function H(e){var t=e.$textLayer,n=t.$lenses;n&&n.forEach(function(i){i.remove()}),t.$lenses=null}function x(e,t){var n=e&t.CHANGE_LINES||e&t.CHANGE_FULL||e&t.CHANGE_SCROLL||e&t.CHANGE_TEXT;if(!!n){var i=t.session,o=t.session.lineWidgets,u=t.$textLayer,a=u.$lenses;if(!o){a&&H(t);return}var v=t.$textLayer.$lines.cells,c=t.layerConfig,L=t.$padding;a||(a=u.$lenses=[]);for(var l=0,h=0;h<v.length;h++){var d=v[h].row,g=o[d],m=g&&g.lenses;if(!(!m||!m.length)){var s=a[l];s||(s=a[l]=w.buildDom(["div",{class:"ace_codeLens"}],t.container)),s.style.height=c.lineHeight+"px",l++;for(var f=0;f<m.length;f++){var _=s.childNodes[2*f];_||(f!=0&&s.appendChild(w.createTextNode("\xA0|\xA0")),_=w.buildDom(["a"],s)),_.textContent=m[f].title,_.lensCommand=m[f]}for(;s.childNodes.length>2*f-1;)s.lastChild.remove();var A=t.$cursorLayer.getPixelPosition({row:d,column:0},!0).top-c.lineHeight*g.rowsAbove-c.offset;s.style.top=A+"px";var E=t.gutterWidth,C=i.getLine(d).search(/\S|$/);C==-1&&(C=0),E+=C*c.characterWidth,s.style.paddingLeft=L+E+"px"}}for(;l<a.length;)a.pop().remove()}}function T(e){if(!!e.lineWidgets){var t=e.widgetManager;e.lineWidgets.forEach(function(n){n&&n.lenses&&t.removeLineWidget(n)})}}p.setLenses=function(e,t){var n=Number.MAX_VALUE;return T(e),t&&t.forEach(function(i){var o=i.start.row,u=i.start.column,a=e.lineWidgets&&e.lineWidgets[o];(!a||!a.lenses)&&(a=e.widgetManager.$registerLineWidget({rowCount:1,rowsAbove:1,row:o,column:u,lenses:[]})),a.lenses.push(i.command),o<n&&(n=o)}),e._emit("changeFold",{data:{start:{row:n}}}),n};function N(e){e.codeLensProviders=[],e.renderer.on("afterRender",x),e.$codeLensClickHandler||(e.$codeLensClickHandler=function(n){var i=n.target.lensCommand;!i||(e.execCommand(i.id,i.arguments),e._emit("codeLensClick",n))},k.addListener(e.container,"click",e.$codeLensClickHandler,e)),e.$updateLenses=function(){var n=e.session;if(!n)return;n.widgetManager||(n.widgetManager=new y(n),n.widgetManager.attach(e));var i=e.codeLensProviders.length,o=[];e.codeLensProviders.forEach(function(a){a.provideCodeLenses(n,function(v,c){v||(c.forEach(function(L){o.push(L)}),i--,i==0&&u())})});function u(){var a=n.selection.cursor,v=n.documentToScreenRow(a),c=n.getScrollTop(),L=p.setLenses(n,o),l=n.$undoManager&&n.$undoManager.$lastDelta;if(!(l&&l.action=="remove"&&l.lines.length>1)){var h=n.documentToScreenRow(a),d=e.renderer.layerConfig.lineHeight,g=n.getScrollTop()+(h-v)*d;L==0&&c<d/4&&c>-d/4&&(g=-d),n.setScrollTop(g)}}};var t=W.delayedCall(e.$updateLenses);e.$updateLensesOnInput=function(){t.delay(250)},e.on("input",e.$updateLensesOnInput)}function M(e){e.off("input",e.$updateLensesOnInput),e.renderer.off("afterRender",x),e.$codeLensClickHandler&&e.container.removeEventListener("click",e.$codeLensClickHandler)}p.registerCodeLensProvider=function(e,t){e.setOption("enableCodeLens",!0),e.codeLensProviders.push(t),e.$updateLensesOnInput()},p.clear=function(e){p.setLenses(e,null)};var S=r("../editor").Editor;r("../config").defineOptions(S.prototype,"editor",{enableCodeLens:{set:function(e){e?N(this):M(this)}}}),w.importCssString(`
.ace_codeLens {
    position: absolute;
    color: #aaa;
    font-size: 88%;
    background: inherit;
    width: 100%;
    display: flex;
    align-items: flex-end;
    pointer-events: none;
}
.ace_codeLens > a {
    cursor: pointer;
    pointer-events: auto;
}
.ace_codeLens > a:hover {
    color: #0000ff;
    text-decoration: underline;
}
.ace_dark > .ace_codeLens > a:hover {
    color: #4e94ce;
}
`,"codelense.css",!1)}),function(){ace.require(["ace/ext/code_lens"],function(r){$&&($.exports=r)})}()}}]);
