(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[43610],{19883:function(x,L,b){x=b.nmd(x),ace.define("ace/mode/logiql_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(a,k,_){"use strict";var v=a("../lib/oop"),m=a("./text_highlight_rules").TextHighlightRules,c=function(){this.$rules={start:[{token:"comment.block",regex:"/\\*",push:[{token:"comment.block",regex:"\\*/",next:"pop"},{defaultToken:"comment.block"}]},{token:"comment.single",regex:"//.*"},{token:"constant.numeric",regex:"\\d+(?:\\.\\d+)?(?:[eE][+-]?\\d+)?[fd]?"},{token:"string",regex:'"',push:[{token:"string",regex:'"',next:"pop"},{defaultToken:"string"}]},{token:"constant.language",regex:"\\b(true|false)\\b"},{token:"entity.name.type.logicblox",regex:"`[a-zA-Z_:]+(\\d|\\a)*\\b"},{token:"keyword.start",regex:"->",comment:"Constraint"},{token:"keyword.start",regex:"-->",comment:"Level 1 Constraint"},{token:"keyword.start",regex:"<-",comment:"Rule"},{token:"keyword.start",regex:"<--",comment:"Level 1 Rule"},{token:"keyword.end",regex:"\\.",comment:"Terminator"},{token:"keyword.other",regex:"!",comment:"Negation"},{token:"keyword.other",regex:",",comment:"Conjunction"},{token:"keyword.other",regex:";",comment:"Disjunction"},{token:"keyword.operator",regex:"<=|>=|!=|<|>",comment:"Equality"},{token:"keyword.other",regex:"@",comment:"Equality"},{token:"keyword.operator",regex:"\\+|-|\\*|/",comment:"Arithmetic operations"},{token:"keyword",regex:"::",comment:"Colon colon"},{token:"support.function",regex:"\\b(agg\\s*<<)",push:[{include:"$self"},{token:"support.function",regex:">>",next:"pop"}]},{token:"storage.modifier",regex:"\\b(lang:[\\w:]*)"},{token:["storage.type","text"],regex:"(export|sealed|clauses|block|alias|alias_all)(\\s*\\()(?=`)"},{token:"entity.name",regex:"[a-zA-Z_][a-zA-Z_0-9:]*(@prev|@init|@final)?(?=(\\(|\\[))"},{token:"variable.parameter",regex:"([a-zA-Z][a-zA-Z_0-9]*|_)\\s*(?=(,|\\.|<-|->|\\)|\\]|=))"}]},this.normalizeRules()};v.inherits(c,m),k.LogiQLHighlightRules=c}),ace.define("ace/mode/folding/coffee",["require","exports","module","ace/lib/oop","ace/mode/folding/fold_mode","ace/range"],function(a,k,_){"use strict";var v=a("../../lib/oop"),m=a("./fold_mode").FoldMode,c=a("../../range").Range,u=k.FoldMode=function(){};v.inherits(u,m),function(){this.getFoldWidgetRange=function(r,p,n){var s=this.indentationBlock(r,n);if(s)return s;var o=/\S/,i=r.getLine(n),e=i.search(o);if(!(e==-1||i[e]!="#")){for(var t=i.length,g=r.getLength(),f=n,l=n;++n<g;){i=r.getLine(n);var h=i.search(o);if(h!=-1){if(i[h]!="#")break;l=n}}if(l>f){var d=r.getLine(l).length;return new c(f,t,l,d)}}},this.getFoldWidget=function(r,p,n){var s=r.getLine(n),o=s.search(/\S/),i=r.getLine(n+1),e=r.getLine(n-1),t=e.search(/\S/),g=i.search(/\S/);if(o==-1)return r.foldWidgets[n-1]=t!=-1&&t<g?"start":"","";if(t==-1){if(o==g&&s[o]=="#"&&i[o]=="#")return r.foldWidgets[n-1]="",r.foldWidgets[n+1]="","start"}else if(t==o&&s[o]=="#"&&e[o]=="#"&&r.getLine(n-2).search(/\S/)==-1)return r.foldWidgets[n-1]="start",r.foldWidgets[n+1]="","";return t!=-1&&t<o?r.foldWidgets[n-1]="start":r.foldWidgets[n-1]="",o<g?"start":""}}.call(u.prototype)}),ace.define("ace/mode/matching_brace_outdent",["require","exports","module","ace/range"],function(a,k,_){"use strict";var v=a("../range").Range,m=function(){};(function(){this.checkOutdent=function(c,u){return/^\s+$/.test(c)?/^\s*\}/.test(u):!1},this.autoOutdent=function(c,u){var r=c.getLine(u),p=r.match(/^(\s*\})/);if(!p)return 0;var n=p[1].length,s=c.findMatchingBracket({row:u,column:n});if(!s||s.row==u)return 0;var o=this.$getIndent(c.getLine(s.row));c.replace(new v(u,0,u,n-1),o)},this.$getIndent=function(c){return c.match(/^\s*/)[0]}}).call(m.prototype),k.MatchingBraceOutdent=m}),ace.define("ace/mode/logiql",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/logiql_highlight_rules","ace/mode/folding/coffee","ace/token_iterator","ace/range","ace/mode/behaviour/cstyle","ace/mode/matching_brace_outdent"],function(a,k,_){"use strict";var v=a("../lib/oop"),m=a("./text").Mode,c=a("./logiql_highlight_rules").LogiQLHighlightRules,u=a("./folding/coffee").FoldMode,r=a("../token_iterator").TokenIterator,p=a("../range").Range,n=a("./behaviour/cstyle").CstyleBehaviour,s=a("./matching_brace_outdent").MatchingBraceOutdent,o=function(){this.HighlightRules=c,this.foldingRules=new u,this.$outdent=new s,this.$behaviour=new n};v.inherits(o,m),function(){this.lineCommentStart="//",this.blockComment={start:"/*",end:"*/"},this.getNextLineIndent=function(i,e,t){var g=this.$getIndent(e),f=this.getTokenizer().getLineTokens(e,i),l=f.tokens,h=f.state;if(/comment|string/.test(h)||l.length&&l[l.length-1].type=="comment.single")return g;var d=e.match();return/(-->|<--|<-|->|{)\s*$/.test(e)&&(g+=t),g},this.checkOutdent=function(i,e,t){return this.$outdent.checkOutdent(e,t)?!0:!(t!==`
`&&t!==`\r
`||!/^\s+/.test(e))},this.autoOutdent=function(i,e,t){if(!this.$outdent.autoOutdent(e,t)){var g=e.getLine(t),f=g.match(/^\s+/),l=g.lastIndexOf(".")+1;if(!f||!t||!l)return 0;var h=e.getLine(t+1),d=this.getMatching(e,{row:t,column:l});if(!d||d.start.row==t)return 0;l=f[0].length;var y=this.$getIndent(e.getLine(d.start.row));e.replace(new p(t+1,0,t+1,l),y)}},this.getMatching=function(i,e,t){e==null&&(e=i.selection.lead),typeof e=="object"&&(t=e.column,e=e.row);var g=i.getTokenAt(e,t),f="keyword.start",l="keyword.end",h;if(!!g){if(g.type==f){var d=new r(i,e,t);d.step=d.stepForward}else if(g.type==l){var d=new r(i,e,t);d.step=d.stepBackward}else return;for(;(h=d.step())&&!(h.type==f||h.type==l););if(!(!h||h.type==g.type)){var y=d.getCurrentTokenColumn(),e=d.getCurrentTokenRow();return new p(e,y,e,y+h.value.length)}}},this.$id="ace/mode/logiql"}.call(o.prototype),k.Mode=o}),function(){ace.require(["ace/mode/logiql"],function(a){x&&(x.exports=a)})}()}}]);