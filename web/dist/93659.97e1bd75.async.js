(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[93659],{93659:function(r,m,b){r=b.nmd(r),ace.define("ace/mode/haskell_cabal_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(a,h,_){"use strict";var g=a("../lib/oop"),d=a("./text_highlight_rules").TextHighlightRules,n=function(){this.$rules={start:[{token:"comment",regex:"^\\s*--.*$"},{token:["keyword"],regex:/^(\s*\w.*?)(:(?:\s+|$))/},{token:"constant.numeric",regex:/[\d_]+(?:(?:[\.\d_]*)?)/},{token:"constant.language.boolean",regex:"(?:true|false|TRUE|FALSE|True|False|yes|no)\\b"},{token:"markup.heading",regex:/^(\w.*)$/}]}};g.inherits(n,d),h.CabalHighlightRules=n}),ace.define("ace/mode/folding/haskell_cabal",["require","exports","module","ace/lib/oop","ace/mode/folding/fold_mode","ace/range"],function(a,h,_){"use strict";var g=a("../../lib/oop"),d=a("./fold_mode").FoldMode,n=a("../../range").Range,u=h.FoldMode=function(){};g.inherits(u,d),function(){this.isHeading=function(t,o){var e="markup.heading",l=t.getTokens(o)[0];return o==0||l&&l.type.lastIndexOf(e,0)===0},this.getFoldWidget=function(t,o,e){if(this.isHeading(t,e))return"start";if(o==="markbeginend"&&!/^\s*$/.test(t.getLine(e))){for(var l=t.getLength();++e<l&&/^\s*$/.test(t.getLine(e)););if(e==l||this.isHeading(t,e))return"end"}return""},this.getFoldWidgetRange=function(t,o,e){var l=t.getLine(e),s=l.length,k=t.getLength(),c=e,i=e;if(this.isHeading(t,e)){for(;++e<k;)if(this.isHeading(t,e)){e--;break}if(i=e,i>c)for(;i>c&&/^\s*$/.test(t.getLine(i));)i--;if(i>c){var f=t.getLine(i).length;return new n(c,s,i,f)}}else if(this.getFoldWidget(t,o,e)==="end"){for(var i=e,f=t.getLine(i).length;--e>=0&&!this.isHeading(t,e););var l=t.getLine(e),s=l.length;return new n(e,s,i,f)}}}.call(u.prototype)}),ace.define("ace/mode/haskell_cabal",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/haskell_cabal_highlight_rules","ace/mode/folding/haskell_cabal"],function(a,h,_){"use strict";var g=a("../lib/oop"),d=a("./text").Mode,n=a("./haskell_cabal_highlight_rules").CabalHighlightRules,u=a("./folding/haskell_cabal").FoldMode,t=function(){this.HighlightRules=n,this.foldingRules=new u,this.$behaviour=this.$defaultBehaviour};g.inherits(t,d),function(){this.lineCommentStart="--",this.blockComment=null,this.$id="ace/mode/haskell_cabal"}.call(t.prototype),h.Mode=t}),function(){ace.require(["ace/mode/haskell_cabal"],function(a){r&&(r.exports=a)})}()}}]);