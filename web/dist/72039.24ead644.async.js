(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[72039],{72039:function(L,s,b){"use strict";b.r(s),b.d(s,{textile:function(){return T}});var a={addition:"inserted",attributes:"propertyName",bold:"strong",cite:"keyword",code:"monospace",definitionList:"list",deletion:"deleted",div:"punctuation",em:"emphasis",footnote:"variable",footCite:"qualifier",header:"heading",html:"comment",image:"atom",italic:"emphasis",link:"link",linkDefinition:"link",list1:"list",list2:"list.special",list3:"list",notextile:"string.special",pre:"operator",p:"content",quote:"bracket",span:"quote",specialChar:"character",strong:"strong",sub:"content.special",sup:"content.special",table:"variableName.special",tableHeading:"operator"};function g(n,e){e.mode=r.newLayout,e.tableHeading=!1,e.layoutType==="definitionList"&&e.spanningLayout&&n.match(l("definitionListEnd"),!1)&&(e.spanningLayout=!1)}function y(n,e,i){if(i==="_")return n.eat("_")?f(n,e,"italic",/__/,2):f(n,e,"em",/_/,1);if(i==="*")return n.eat("*")?f(n,e,"bold",/\*\*/,2):f(n,e,"strong",/\*/,1);if(i==="[")return n.match(/\d+\]/)&&(e.footCite=!0),o(e);if(i==="("){var u=n.match(/^(r|tm|c)\)/);if(u)return a.specialChar}if(i==="<"&&n.match(/(\w+)[^>]+>[^<]+<\/\1>/))return a.html;if(i==="?"&&n.eat("?"))return f(n,e,"cite",/\?\?/,2);if(i==="="&&n.eat("="))return f(n,e,"notextile",/==/,2);if(i==="-"&&!n.eat("-"))return f(n,e,"deletion",/-/,1);if(i==="+")return f(n,e,"addition",/\+/,1);if(i==="~")return f(n,e,"sub",/~/,1);if(i==="^")return f(n,e,"sup",/\^/,1);if(i==="%")return f(n,e,"span",/%/,1);if(i==="@")return f(n,e,"code",/@/,1);if(i==="!"){var c=f(n,e,"image",/(?:\([^\)]+\))?!/,1);return n.match(/^:\S+/),c}return o(e)}function f(n,e,i,u,c){var d=n.pos>c?n.string.charAt(n.pos-c-1):null,p=n.peek();if(e[i]){if((!p||/\W/.test(p))&&d&&/\S/.test(d)){var v=o(e);return e[i]=!1,v}}else(!d||/\W/.test(d))&&p&&/\S/.test(p)&&n.match(new RegExp("^.*\\S"+u.source+"(?:\\W|$)"),!1)&&(e[i]=!0,e.mode=r.attributes);return o(e)}function o(n){var e=h(n);if(e)return e;var i=[];return n.layoutType&&i.push(a[n.layoutType]),i=i.concat(m(n,"addition","bold","cite","code","deletion","em","footCite","image","italic","link","span","strong","sub","sup","table","tableHeading")),n.layoutType==="header"&&i.push(a.header+"-"+n.header),i.length?i.join(" "):null}function h(n){var e=n.layoutType;switch(e){case"notextile":case"code":case"pre":return a[e];default:return n.notextile?a.notextile+(e?" "+a[e]:""):null}}function m(n){for(var e=[],i=1;i<arguments.length;++i)n[arguments[i]]&&e.push(a[arguments[i]]);return e}function k(n){var e=n.spanningLayout,i=n.layoutType;for(var u in n)n.hasOwnProperty(u)&&delete n[u];n.mode=r.newLayout,e&&(n.layoutType=i,n.spanningLayout=!0)}var t={cache:{},single:{bc:"bc",bq:"bq",definitionList:/- .*?:=+/,definitionListEnd:/.*=:\s*$/,div:"div",drawTable:/\|.*\|/,foot:/fn\d+/,header:/h[1-6]/,html:/\s*<(?:\/)?(\w+)(?:[^>]+)?>(?:[^<]+<\/\1>)?/,link:/[^"]+":\S/,linkDefinition:/\[[^\s\]]+\]\S+/,list:/(?:#+|\*+)/,notextile:"notextile",para:"p",pre:"pre",table:"table",tableCellAttributes:/[\/\\]\d+/,tableHeading:/\|_\./,tableText:/[^"_\*\[\(\?\+~\^%@|-]+/,text:/[^!"_=\*\[\(<\?\+~\^%@-]+/},attributes:{align:/(?:<>|<|>|=)/,selector:/\([^\(][^\)]+\)/,lang:/\[[^\[\]]+\]/,pad:/(?:\(+|\)+){1,2}/,css:/\{[^\}]+\}/},createRe:function(n){switch(n){case"drawTable":return t.makeRe("^",t.single.drawTable,"$");case"html":return t.makeRe("^",t.single.html,"(?:",t.single.html,")*","$");case"linkDefinition":return t.makeRe("^",t.single.linkDefinition,"$");case"listLayout":return t.makeRe("^",t.single.list,l("allAttributes"),"*\\s+");case"tableCellAttributes":return t.makeRe("^",t.choiceRe(t.single.tableCellAttributes,l("allAttributes")),"+\\.");case"type":return t.makeRe("^",l("allTypes"));case"typeLayout":return t.makeRe("^",l("allTypes"),l("allAttributes"),"*\\.\\.?","(\\s+|$)");case"attributes":return t.makeRe("^",l("allAttributes"),"+");case"allTypes":return t.choiceRe(t.single.div,t.single.foot,t.single.header,t.single.bc,t.single.bq,t.single.notextile,t.single.pre,t.single.table,t.single.para);case"allAttributes":return t.choiceRe(t.attributes.selector,t.attributes.css,t.attributes.lang,t.attributes.align,t.attributes.pad);default:return t.makeRe("^",t.single[n])}},makeRe:function(){for(var n="",e=0;e<arguments.length;++e){var i=arguments[e];n+=typeof i=="string"?i:i.source}return new RegExp(n)},choiceRe:function(){for(var n=[arguments[0]],e=1;e<arguments.length;++e)n[e*2-1]="|",n[e*2]=arguments[e];return n.unshift("(?:"),n.push(")"),t.makeRe.apply(null,n)}};function l(n){return t.cache[n]||(t.cache[n]=t.createRe(n))}var r={newLayout:function(n,e){if(n.match(l("typeLayout"),!1))return e.spanningLayout=!1,(e.mode=r.blockType)(n,e);var i;return h(e)||(n.match(l("listLayout"),!1)?i=r.list:n.match(l("drawTable"),!1)?i=r.table:n.match(l("linkDefinition"),!1)?i=r.linkDefinition:n.match(l("definitionList"))?i=r.definitionList:n.match(l("html"),!1)&&(i=r.html)),(e.mode=i||r.text)(n,e)},blockType:function(n,e){var i,u;if(e.layoutType=null,i=n.match(l("type")))u=i[0];else return(e.mode=r.text)(n,e);return(i=u.match(l("header")))?(e.layoutType="header",e.header=parseInt(i[0][1])):u.match(l("bq"))?e.layoutType="quote":u.match(l("bc"))?e.layoutType="code":u.match(l("foot"))?e.layoutType="footnote":u.match(l("notextile"))?e.layoutType="notextile":u.match(l("pre"))?e.layoutType="pre":u.match(l("div"))?e.layoutType="div":u.match(l("table"))&&(e.layoutType="table"),e.mode=r.attributes,o(e)},text:function(n,e){if(n.match(l("text")))return o(e);var i=n.next();return i==='"'?(e.mode=r.link)(n,e):y(n,e,i)},attributes:function(n,e){return e.mode=r.layoutLength,n.match(l("attributes"))?a.attributes:o(e)},layoutLength:function(n,e){return n.eat(".")&&n.eat(".")&&(e.spanningLayout=!0),e.mode=r.text,o(e)},list:function(n,e){var i=n.match(l("list"));e.listDepth=i[0].length;var u=(e.listDepth-1)%3;return u?u===1?e.layoutType="list2":e.layoutType="list3":e.layoutType="list1",e.mode=r.attributes,o(e)},link:function(n,e){return e.mode=r.text,n.match(l("link"))?(n.match(/\S+/),a.link):o(e)},linkDefinition:function(n){return n.skipToEnd(),a.linkDefinition},definitionList:function(n,e){return n.match(l("definitionList")),e.layoutType="definitionList",n.match(/\s*$/)?e.spanningLayout=!0:e.mode=r.attributes,o(e)},html:function(n){return n.skipToEnd(),a.html},table:function(n,e){return e.layoutType="table",(e.mode=r.tableCell)(n,e)},tableCell:function(n,e){return n.match(l("tableHeading"))?e.tableHeading=!0:n.eat("|"),e.mode=r.tableCellAttributes,o(e)},tableCellAttributes:function(n,e){return e.mode=r.tableText,n.match(l("tableCellAttributes"))?a.attributes:o(e)},tableText:function(n,e){return n.match(l("tableText"))?o(e):n.peek()==="|"?(e.mode=r.tableCell,o(e)):y(n,e,n.next())}};const T={name:"textile",startState:function(){return{mode:r.newLayout}},token:function(n,e){return n.sol()&&g(n,e),e.mode(n,e)},blankLine:k}}}]);