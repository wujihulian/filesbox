(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[27052],{27052:function(u,v,k){u=k.nmd(u),ace.define("ace/mode/fsl_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"],function(o,d,p){"use strict";var m=o("../lib/oop"),h=o("./text_highlight_rules").TextHighlightRules,c=function(){this.$rules={start:[{token:"punctuation.definition.comment.mn",regex:/\/\*/,push:[{token:"punctuation.definition.comment.mn",regex:/\*\//,next:"pop"},{defaultToken:"comment.block.fsl"}]},{token:"comment.line.fsl",regex:/\/\//,push:[{token:"comment.line.fsl",regex:/$/,next:"pop"},{defaultToken:"comment.line.fsl"}]},{token:"entity.name.function",regex:/\${/,push:[{token:"entity.name.function",regex:/}/,next:"pop"},{defaultToken:"keyword.other"}],comment:"js outcalls"},{token:"constant.numeric",regex:/[0-9]*\.[0-9]*\.[0-9]*/,comment:"semver"},{token:"constant.language.fslLanguage",regex:"(?:graph_layout|machine_name|machine_author|machine_license|machine_comment|machine_language|machine_version|machine_reference|npm_name|graph_layout|on_init|on_halt|on_end|on_terminate|on_finalize|on_transition|on_action|on_stochastic_action|on_legal|on_main|on_forced|on_validation|on_validation_failure|on_transition_refused|on_forced_transition_refused|on_action_refused|on_enter|on_exit|start_states|end_states|terminal_states|final_states|fsl_version)\\s*:"},{token:"keyword.control.transition.fslArrow",regex:/<->|<-|->|<=>|=>|<=|<~>|~>|<~|<-=>|<=->|<-~>|<~->|<=~>|<~=>/},{token:"constant.numeric.fslProbability",regex:/[0-9]+%/,comment:"edge probability annotation"},{token:"constant.character.fslAction",regex:/\'[^']*\'/,comment:"action annotation"},{token:"string.quoted.double.fslLabel.doublequoted",regex:/\"[^"]*\"/,comment:"fsl label annotation"},{token:"entity.name.tag.fslLabel.atom",regex:/[a-zA-Z0-9_.+&()#@!?,]/,comment:"fsl label annotation"}]},this.normalizeRules()};c.metaData={fileTypes:["fsl","fsl_state"],name:"FSL",scopeName:"source.fsl"},m.inherits(c,h),d.FSLHighlightRules=c}),ace.define("ace/mode/folding/cstyle",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"],function(o,d,p){"use strict";var m=o("../../lib/oop"),h=o("../../range").Range,c=o("./fold_mode").FoldMode,_=d.FoldMode=function(e){e&&(this.foldingStartMarker=new RegExp(this.foldingStartMarker.source.replace(/\|[^|]*?$/,"|"+e.start)),this.foldingStopMarker=new RegExp(this.foldingStopMarker.source.replace(/\|[^|]*?$/,"|"+e.end)))};m.inherits(_,c),function(){this.foldingStartMarker=/([\{\[\(])[^\}\]\)]*$|^\s*(\/\*)/,this.foldingStopMarker=/^[^\[\{\(]*([\}\]\)])|^[\s\*]*(\*\/)/,this.singleLineBlockCommentRe=/^\s*(\/\*).*\*\/\s*$/,this.tripleStarBlockCommentRe=/^\s*(\/\*\*\*).*\*\/\s*$/,this.startRegionRe=/^\s*(\/\*|\/\/)#?region\b/,this._getFoldWidgetBase=this.getFoldWidget,this.getFoldWidget=function(e,t,n){var l=e.getLine(n);if(this.singleLineBlockCommentRe.test(l)&&!this.startRegionRe.test(l)&&!this.tripleStarBlockCommentRe.test(l))return"";var a=this._getFoldWidgetBase(e,t,n);return!a&&this.startRegionRe.test(l)?"start":a},this.getFoldWidgetRange=function(e,t,n,l){var a=e.getLine(n);if(this.startRegionRe.test(a))return this.getCommentRegionBlock(e,a,n);var i=a.match(this.foldingStartMarker);if(i){var r=i.index;if(i[1])return this.openingBracketBlock(e,i[1],n,r);var s=e.getCommentFoldRange(n,r+i[0].length,1);return s&&!s.isMultiLine()&&(l?s=this.getSectionRange(e,n):t!="all"&&(s=null)),s}if(t!=="markbegin"){var i=a.match(this.foldingStopMarker);if(i){var r=i.index+i[0].length;return i[1]?this.closingBracketBlock(e,i[1],n,r):e.getCommentFoldRange(n,r,-1)}}},this.getSectionRange=function(e,t){var n=e.getLine(t),l=n.search(/\S/),a=t,i=n.length;t=t+1;for(var r=t,s=e.getLength();++t<s;){n=e.getLine(t);var f=n.search(/\S/);if(f!==-1){if(l>f)break;var g=this.getFoldWidgetRange(e,"all",t);if(g){if(g.start.row<=a)break;if(g.isMultiLine())t=g.end.row;else if(l==f)break}r=t}}return new h(a,i,r,e.getLine(r).length)},this.getCommentRegionBlock=function(e,t,n){for(var l=t.search(/\s*$/),a=e.getLength(),i=n,r=/^\s*(?:\/\*|\/\/|--)#?(end)?region\b/,s=1;++n<a;){t=e.getLine(n);var f=r.exec(t);if(!!f&&(f[1]?s--:s++,!s))break}var g=n;if(g>i)return new h(i,l,g,t.length)}}.call(_.prototype)}),ace.define("ace/mode/fsl",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/fsl_highlight_rules","ace/mode/folding/cstyle"],function(o,d,p){"use strict";var m=o("../lib/oop"),h=o("./text").Mode,c=o("./fsl_highlight_rules").FSLHighlightRules,_=o("./folding/cstyle").FoldMode,e=function(){this.HighlightRules=c,this.foldingRules=new _};m.inherits(e,h),function(){this.lineCommentStart="//",this.blockComment={start:"/*",end:"*/"},this.$id="ace/mode/fsl",this.snippetFileId="ace/snippets/fsl"}.call(e.prototype),d.Mode=e}),function(){ace.require(["ace/mode/fsl"],function(o){u&&(u.exports=o)})}()}}]);