(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[31723],{31723:function(l,p,d){l=d.nmd(l),ace.define("ace/mode/tex_highlight_rules",["require","exports","module","ace/lib/oop","ace/lib/lang","ace/mode/text_highlight_rules"],function(e,i,h){"use strict";var a=e("../lib/oop"),c=e("../lib/lang"),t=e("./text_highlight_rules").TextHighlightRules,n=function(r){r||(r="text"),this.$rules={start:[{token:"comment",regex:"%.*$"},{token:r,regex:"\\\\[$&%#\\{\\}]"},{token:"keyword",regex:"\\\\(?:documentclass|usepackage|newcounter|setcounter|addtocounter|value|arabic|stepcounter|newenvironment|renewenvironment|ref|vref|eqref|pageref|label|cite[a-zA-Z]*|tag|begin|end|bibitem)\\b",next:"nospell"},{token:"keyword",regex:"\\\\(?:[a-zA-Z0-9]+|[^a-zA-Z0-9])"},{token:"paren.keyword.operator",regex:"[[({]"},{token:"paren.keyword.operator",regex:"[\\])}]"},{token:r,regex:"\\s+"}],nospell:[{token:"comment",regex:"%.*$",next:"start"},{token:"nospell."+r,regex:"\\\\[$&%#\\{\\}]"},{token:"keyword",regex:"\\\\(?:documentclass|usepackage|newcounter|setcounter|addtocounter|value|arabic|stepcounter|newenvironment|renewenvironment|ref|vref|eqref|pageref|label|cite[a-zA-Z]*|tag|begin|end|bibitem)\\b"},{token:"keyword",regex:"\\\\(?:[a-zA-Z0-9]+|[^a-zA-Z0-9])",next:"start"},{token:"paren.keyword.operator",regex:"[[({]"},{token:"paren.keyword.operator",regex:"[\\])]"},{token:"paren.keyword.operator",regex:"}",next:"start"},{token:"nospell."+r,regex:"\\s+"},{token:"nospell."+r,regex:"\\w+"}]}};a.inherits(n,t),i.TexHighlightRules=n}),ace.define("ace/mode/matching_brace_outdent",["require","exports","module","ace/range"],function(e,i,h){"use strict";var a=e("../range").Range,c=function(){};(function(){this.checkOutdent=function(t,n){return/^\s+$/.test(t)?/^\s*\}/.test(n):!1},this.autoOutdent=function(t,n){var r=t.getLine(n),o=r.match(/^(\s*\})/);if(!o)return 0;var g=o[1].length,u=t.findMatchingBracket({row:n,column:g});if(!u||u.row==n)return 0;var s=this.$getIndent(t.getLine(u.row));t.replace(new a(n,0,n,g-1),s)},this.$getIndent=function(t){return t.match(/^\s*/)[0]}}).call(c.prototype),i.MatchingBraceOutdent=c}),ace.define("ace/mode/tex",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/text_highlight_rules","ace/mode/tex_highlight_rules","ace/mode/matching_brace_outdent"],function(e,i,h){"use strict";var a=e("../lib/oop"),c=e("./text").Mode,t=e("./text_highlight_rules").TextHighlightRules,n=e("./tex_highlight_rules").TexHighlightRules,r=e("./matching_brace_outdent").MatchingBraceOutdent,o=function(g){g?this.HighlightRules=t:this.HighlightRules=n,this.$outdent=new r,this.$behaviour=this.$defaultBehaviour};a.inherits(o,c),function(){this.lineCommentStart="%",this.getNextLineIndent=function(g,u,s){return this.$getIndent(u)},this.allowAutoInsert=function(){return!1},this.$id="ace/mode/tex",this.snippetFileId="ace/snippets/tex"}.call(o.prototype),i.Mode=o}),function(){ace.require(["ace/mode/tex"],function(e){l&&(l.exports=e)})}()}}]);