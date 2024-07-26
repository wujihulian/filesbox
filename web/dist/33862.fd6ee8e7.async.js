(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[33862],{33862:function(l,c,g){l=g.nmd(l),ace.define("ace/mode/rst_highlight_rules",["require","exports","module","ace/lib/oop","ace/lib/lang","ace/mode/text_highlight_rules"],function(t,r,k){"use strict";var a=t("../lib/oop"),s=t("../lib/lang"),x=t("./text_highlight_rules").TextHighlightRules,n=function(){var e={title:"markup.heading",list:"markup.heading",table:"constant",directive:"keyword.operator",entity:"string",link:"markup.underline.list",bold:"markup.bold",italic:"markup.italic",literal:"support.function",comment:"comment"},i=`(^|\\s|["'(<\\[{\\-/:])`,o=`(?:$|(?=\\s|[\\\\.,;!?\\-/:"')>\\]}]))`;this.$rules={start:[{token:e.title,regex:"(^)([\\=\\-`:\\.'\"~\\^_\\*\\+#])(\\2{2,}\\s*$)"},{token:["text",e.directive,e.literal],regex:"(^\\s*\\.\\. )([^: ]+::)(.*$)",next:"codeblock"},{token:e.directive,regex:"::$",next:"codeblock"},{token:[e.entity,e.link],regex:"(^\\.\\. _[^:]+:)(.*$)"},{token:[e.entity,e.link],regex:"(^__ )(https?://.*$)"},{token:e.entity,regex:"^\\.\\. \\[[^\\]]+\\] "},{token:e.comment,regex:"^\\.\\. .*$",next:"comment"},{token:e.list,regex:"^\\s*[\\*\\+-] "},{token:e.list,regex:"^\\s*(?:[A-Za-z]|[0-9]+|[ivxlcdmIVXLCDM]+)\\. "},{token:e.list,regex:"^\\s*\\(?(?:[A-Za-z]|[0-9]+|[ivxlcdmIVXLCDM]+)\\) "},{token:e.table,regex:"^={2,}(?: +={2,})+$"},{token:e.table,regex:"^\\+-{2,}(?:\\+-{2,})+\\+$"},{token:e.table,regex:"^\\+={2,}(?:\\+={2,})+\\+$"},{token:["text",e.literal],regex:i+"(``)(?=\\S)",next:"code"},{token:["text",e.bold],regex:i+"(\\*\\*)(?=\\S)",next:"bold"},{token:["text",e.italic],regex:i+"(\\*)(?=\\S)",next:"italic"},{token:e.entity,regex:"\\|[\\w\\-]+?\\|"},{token:e.entity,regex:":[\\w-:]+:`\\S",next:"entity"},{token:["text",e.entity],regex:i+"(_`)(?=\\S)",next:"entity"},{token:e.entity,regex:"_[A-Za-z0-9\\-]+?"},{token:["text",e.link],regex:i+"(`)(?=\\S)",next:"link"},{token:e.link,regex:"[A-Za-z0-9\\-]+?__?"},{token:e.link,regex:"\\[[^\\]]+?\\]_"},{token:e.link,regex:"https?://\\S+"},{token:e.table,regex:"\\|"}],codeblock:[{token:e.literal,regex:"^ +.+$",next:"codeblock"},{token:e.literal,regex:"^$",next:"codeblock"},{token:"empty",regex:"",next:"start"}],code:[{token:e.literal,regex:"\\S``"+o,next:"start"},{defaultToken:e.literal}],bold:[{token:e.bold,regex:"\\S\\*\\*"+o,next:"start"},{defaultToken:e.bold}],italic:[{token:e.italic,regex:"\\S\\*"+o,next:"start"},{defaultToken:e.italic}],entity:[{token:e.entity,regex:"\\S`"+o,next:"start"},{defaultToken:e.entity}],link:[{token:e.link,regex:"\\S`__?"+o,next:"start"},{defaultToken:e.link}],comment:[{token:e.comment,regex:"^ +.+$",next:"comment"},{token:e.comment,regex:"^$",next:"comment"},{token:"empty",regex:"",next:"start"}]}};a.inherits(n,x),r.RSTHighlightRules=n}),ace.define("ace/mode/rst",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/rst_highlight_rules"],function(t,r,k){"use strict";var a=t("../lib/oop"),s=t("./text").Mode,x=t("./rst_highlight_rules").RSTHighlightRules,n=function(){this.HighlightRules=x};a.inherits(n,s),function(){this.type="text",this.$id="ace/mode/rst",this.snippetFileId="ace/snippets/rst"}.call(n.prototype),r.Mode=n}),function(){ace.require(["ace/mode/rst"],function(t){l&&(l.exports=t)})}()}}]);