(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[61549],{61549:function(v,w,k){v=k.nmd(v),ace.define("ace/mode/css_highlight_rules",["require","exports","module","ace/lib/oop","ace/lib/lang","ace/mode/text_highlight_rules"],function(d,p,f){"use strict";var m=d("../lib/oop"),h=d("../lib/lang"),c=d("./text_highlight_rules").TextHighlightRules,l=p.supportType="align-content|align-items|align-self|all|animation|animation-delay|animation-direction|animation-duration|animation-fill-mode|animation-iteration-count|animation-name|animation-play-state|animation-timing-function|backface-visibility|background|background-attachment|background-blend-mode|background-clip|background-color|background-image|background-origin|background-position|background-repeat|background-size|border|border-bottom|border-bottom-color|border-bottom-left-radius|border-bottom-right-radius|border-bottom-style|border-bottom-width|border-collapse|border-color|border-image|border-image-outset|border-image-repeat|border-image-slice|border-image-source|border-image-width|border-left|border-left-color|border-left-style|border-left-width|border-radius|border-right|border-right-color|border-right-style|border-right-width|border-spacing|border-style|border-top|border-top-color|border-top-left-radius|border-top-right-radius|border-top-style|border-top-width|border-width|bottom|box-shadow|box-sizing|caption-side|clear|clip|color|column-count|column-fill|column-gap|column-rule|column-rule-color|column-rule-style|column-rule-width|column-span|column-width|columns|content|counter-increment|counter-reset|cursor|direction|display|empty-cells|filter|flex|flex-basis|flex-direction|flex-flow|flex-grow|flex-shrink|flex-wrap|float|font|font-family|font-size|font-size-adjust|font-stretch|font-style|font-variant|font-weight|hanging-punctuation|height|justify-content|left|letter-spacing|line-height|list-style|list-style-image|list-style-position|list-style-type|margin|margin-bottom|margin-left|margin-right|margin-top|max-height|max-width|max-zoom|min-height|min-width|min-zoom|nav-down|nav-index|nav-left|nav-right|nav-up|opacity|order|outline|outline-color|outline-offset|outline-style|outline-width|overflow|overflow-x|overflow-y|padding|padding-bottom|padding-left|padding-right|padding-top|page-break-after|page-break-before|page-break-inside|perspective|perspective-origin|position|quotes|resize|right|tab-size|table-layout|text-align|text-align-last|text-decoration|text-decoration-color|text-decoration-line|text-decoration-style|text-indent|text-justify|text-overflow|text-shadow|text-transform|top|transform|transform-origin|transform-style|transition|transition-delay|transition-duration|transition-property|transition-timing-function|unicode-bidi|user-select|user-zoom|vertical-align|visibility|white-space|width|word-break|word-spacing|word-wrap|z-index",t=p.supportFunction="rgb|rgba|url|attr|counter|counters",a=p.supportConstant="absolute|after-edge|after|all-scroll|all|alphabetic|always|antialiased|armenian|auto|avoid-column|avoid-page|avoid|balance|baseline|before-edge|before|below|bidi-override|block-line-height|block|bold|bolder|border-box|both|bottom|box|break-all|break-word|capitalize|caps-height|caption|center|central|char|circle|cjk-ideographic|clone|close-quote|col-resize|collapse|column|consider-shifts|contain|content-box|cover|crosshair|cubic-bezier|dashed|decimal-leading-zero|decimal|default|disabled|disc|disregard-shifts|distribute-all-lines|distribute-letter|distribute-space|distribute|dotted|double|e-resize|ease-in|ease-in-out|ease-out|ease|ellipsis|end|exclude-ruby|flex-end|flex-start|fill|fixed|georgian|glyphs|grid-height|groove|hand|hanging|hebrew|help|hidden|hiragana-iroha|hiragana|horizontal|icon|ideograph-alpha|ideograph-numeric|ideograph-parenthesis|ideograph-space|ideographic|inactive|include-ruby|inherit|initial|inline-block|inline-box|inline-line-height|inline-table|inline|inset|inside|inter-ideograph|inter-word|invert|italic|justify|katakana-iroha|katakana|keep-all|last|left|lighter|line-edge|line-through|line|linear|list-item|local|loose|lower-alpha|lower-greek|lower-latin|lower-roman|lowercase|lr-tb|ltr|mathematical|max-height|max-size|medium|menu|message-box|middle|move|n-resize|ne-resize|newspaper|no-change|no-close-quote|no-drop|no-open-quote|no-repeat|none|normal|not-allowed|nowrap|nw-resize|oblique|open-quote|outset|outside|overline|padding-box|page|pointer|pre-line|pre-wrap|pre|preserve-3d|progress|relative|repeat-x|repeat-y|repeat|replaced|reset-size|ridge|right|round|row-resize|rtl|s-resize|scroll|se-resize|separate|slice|small-caps|small-caption|solid|space|square|start|static|status-bar|step-end|step-start|steps|stretch|strict|sub|super|sw-resize|table-caption|table-cell|table-column-group|table-column|table-footer-group|table-header-group|table-row-group|table-row|table|tb-rl|text-after-edge|text-before-edge|text-bottom|text-size|text-top|text|thick|thin|transparent|underline|upper-alpha|upper-latin|upper-roman|uppercase|use-script|vertical-ideographic|vertical-text|visible|w-resize|wait|whitespace|z-index|zero|zoom",r=p.supportConstantColor="aliceblue|antiquewhite|aqua|aquamarine|azure|beige|bisque|black|blanchedalmond|blue|blueviolet|brown|burlywood|cadetblue|chartreuse|chocolate|coral|cornflowerblue|cornsilk|crimson|cyan|darkblue|darkcyan|darkgoldenrod|darkgray|darkgreen|darkgrey|darkkhaki|darkmagenta|darkolivegreen|darkorange|darkorchid|darkred|darksalmon|darkseagreen|darkslateblue|darkslategray|darkslategrey|darkturquoise|darkviolet|deeppink|deepskyblue|dimgray|dimgrey|dodgerblue|firebrick|floralwhite|forestgreen|fuchsia|gainsboro|ghostwhite|gold|goldenrod|gray|green|greenyellow|grey|honeydew|hotpink|indianred|indigo|ivory|khaki|lavender|lavenderblush|lawngreen|lemonchiffon|lightblue|lightcoral|lightcyan|lightgoldenrodyellow|lightgray|lightgreen|lightgrey|lightpink|lightsalmon|lightseagreen|lightskyblue|lightslategray|lightslategrey|lightsteelblue|lightyellow|lime|limegreen|linen|magenta|maroon|mediumaquamarine|mediumblue|mediumorchid|mediumpurple|mediumseagreen|mediumslateblue|mediumspringgreen|mediumturquoise|mediumvioletred|midnightblue|mintcream|mistyrose|moccasin|navajowhite|navy|oldlace|olive|olivedrab|orange|orangered|orchid|palegoldenrod|palegreen|paleturquoise|palevioletred|papayawhip|peachpuff|peru|pink|plum|powderblue|purple|rebeccapurple|red|rosybrown|royalblue|saddlebrown|salmon|sandybrown|seagreen|seashell|sienna|silver|skyblue|slateblue|slategray|slategrey|snow|springgreen|steelblue|tan|teal|thistle|tomato|turquoise|violet|wheat|white|whitesmoke|yellow|yellowgreen",i=p.supportConstantFonts="arial|century|comic|courier|cursive|fantasy|garamond|georgia|helvetica|impact|lucida|symbol|system|tahoma|times|trebuchet|utopia|verdana|webdings|sans-serif|serif|monospace",n=p.numRe="\\-?(?:(?:[0-9]+(?:\\.[0-9]+)?)|(?:\\.[0-9]+))",e=p.pseudoElements="(\\:+)\\b(after|before|first-letter|first-line|moz-selection|selection)\\b",o=p.pseudoClasses="(:)\\b(active|checked|disabled|empty|enabled|first-child|first-of-type|focus|hover|indeterminate|invalid|last-child|last-of-type|link|not|nth-child|nth-last-child|nth-last-of-type|nth-of-type|only-child|only-of-type|required|root|target|valid|visited)\\b",s=function(){var u=this.createKeywordMapper({"support.function":t,"support.constant":a,"support.type":l,"support.constant.color":r,"support.constant.fonts":i},"text",!0);this.$rules={start:[{include:["strings","url","comments"]},{token:"paren.lparen",regex:"\\{",next:"ruleset"},{token:"paren.rparen",regex:"\\}"},{token:"string",regex:"@(?!viewport)",next:"media"},{token:"keyword",regex:"#[a-z0-9-_]+"},{token:"keyword",regex:"%"},{token:"variable",regex:"\\.[a-z0-9-_]+"},{token:"string",regex:":[a-z0-9-_]+"},{token:"constant.numeric",regex:n},{token:"constant",regex:"[a-z0-9-_]+"},{caseInsensitive:!0}],media:[{include:["strings","url","comments"]},{token:"paren.lparen",regex:"\\{",next:"start"},{token:"paren.rparen",regex:"\\}",next:"start"},{token:"string",regex:";",next:"start"},{token:"keyword",regex:"(?:media|supports|document|charset|import|namespace|media|supports|document|page|font|keyframes|viewport|counter-style|font-feature-values|swash|ornaments|annotation|stylistic|styleset|character-variant)"}],comments:[{token:"comment",regex:"\\/\\*",push:[{token:"comment",regex:"\\*\\/",next:"pop"},{defaultToken:"comment"}]}],ruleset:[{regex:"-(webkit|ms|moz|o)-",token:"text"},{token:"punctuation.operator",regex:"[:;]"},{token:"paren.rparen",regex:"\\}",next:"start"},{include:["strings","url","comments"]},{token:["constant.numeric","keyword"],regex:"("+n+")(ch|cm|deg|em|ex|fr|gd|grad|Hz|in|kHz|mm|ms|pc|pt|px|rad|rem|s|turn|vh|vmax|vmin|vm|vw|%)"},{token:"constant.numeric",regex:n},{token:"constant.numeric",regex:"#[a-f0-9]{6}"},{token:"constant.numeric",regex:"#[a-f0-9]{3}"},{token:["punctuation","entity.other.attribute-name.pseudo-element.css"],regex:e},{token:["punctuation","entity.other.attribute-name.pseudo-class.css"],regex:o},{include:"url"},{token:u,regex:"\\-?[a-zA-Z_][a-zA-Z0-9_\\-]*"},{caseInsensitive:!0}],url:[{token:"support.function",regex:"(?:url(:?-prefix)?|domain|regexp)\\(",push:[{token:"support.function",regex:"\\)",next:"pop"},{defaultToken:"string"}]}],strings:[{token:"string.start",regex:"'",push:[{token:"string.end",regex:"'|$",next:"pop"},{include:"escapes"},{token:"constant.language.escape",regex:/\\$/,consumeLineEnd:!0},{defaultToken:"string"}]},{token:"string.start",regex:'"',push:[{token:"string.end",regex:'"|$',next:"pop"},{include:"escapes"},{token:"constant.language.escape",regex:/\\$/,consumeLineEnd:!0},{defaultToken:"string"}]}],escapes:[{token:"constant.language.escape",regex:/\\([a-fA-F\d]{1,6}|[^a-fA-F\d])/}]},this.normalizeRules()};m.inherits(s,c),p.CssHighlightRules=s}),ace.define("ace/mode/matching_brace_outdent",["require","exports","module","ace/range"],function(d,p,f){"use strict";var m=d("../range").Range,h=function(){};(function(){this.checkOutdent=function(c,l){return/^\s+$/.test(c)?/^\s*\}/.test(l):!1},this.autoOutdent=function(c,l){var t=c.getLine(l),a=t.match(/^(\s*\})/);if(!a)return 0;var r=a[1].length,i=c.findMatchingBracket({row:l,column:r});if(!i||i.row==l)return 0;var n=this.$getIndent(c.getLine(i.row));c.replace(new m(l,0,l,r-1),n)},this.$getIndent=function(c){return c.match(/^\s*/)[0]}}).call(h.prototype),p.MatchingBraceOutdent=h}),ace.define("ace/mode/css_completions",["require","exports","module"],function(d,p,f){"use strict";var m={background:{"#$0":1},"background-color":{"#$0":1,transparent:1,fixed:1},"background-image":{"url('/$0')":1},"background-repeat":{repeat:1,"repeat-x":1,"repeat-y":1,"no-repeat":1,inherit:1},"background-position":{bottom:2,center:2,left:2,right:2,top:2,inherit:2},"background-attachment":{scroll:1,fixed:1},"background-size":{cover:1,contain:1},"background-clip":{"border-box":1,"padding-box":1,"content-box":1},"background-origin":{"border-box":1,"padding-box":1,"content-box":1},border:{"solid $0":1,"dashed $0":1,"dotted $0":1,"#$0":1},"border-color":{"#$0":1},"border-style":{solid:2,dashed:2,dotted:2,double:2,groove:2,hidden:2,inherit:2,inset:2,none:2,outset:2,ridged:2},"border-collapse":{collapse:1,separate:1},bottom:{px:1,em:1,"%":1},clear:{left:1,right:1,both:1,none:1},color:{"#$0":1,"rgb(#$00,0,0)":1},cursor:{default:1,pointer:1,move:1,text:1,wait:1,help:1,progress:1,"n-resize":1,"ne-resize":1,"e-resize":1,"se-resize":1,"s-resize":1,"sw-resize":1,"w-resize":1,"nw-resize":1},display:{none:1,block:1,inline:1,"inline-block":1,"table-cell":1},"empty-cells":{show:1,hide:1},float:{left:1,right:1,none:1},"font-family":{Arial:2,"Comic Sans MS":2,Consolas:2,"Courier New":2,Courier:2,Georgia:2,Monospace:2,"Sans-Serif":2,"Segoe UI":2,Tahoma:2,"Times New Roman":2,"Trebuchet MS":2,Verdana:1},"font-size":{px:1,em:1,"%":1},"font-weight":{bold:1,normal:1},"font-style":{italic:1,normal:1},"font-variant":{normal:1,"small-caps":1},height:{px:1,em:1,"%":1},left:{px:1,em:1,"%":1},"letter-spacing":{normal:1},"line-height":{normal:1},"list-style-type":{none:1,disc:1,circle:1,square:1,decimal:1,"decimal-leading-zero":1,"lower-roman":1,"upper-roman":1,"lower-greek":1,"lower-latin":1,"upper-latin":1,georgian:1,"lower-alpha":1,"upper-alpha":1},margin:{px:1,em:1,"%":1},"margin-right":{px:1,em:1,"%":1},"margin-left":{px:1,em:1,"%":1},"margin-top":{px:1,em:1,"%":1},"margin-bottom":{px:1,em:1,"%":1},"max-height":{px:1,em:1,"%":1},"max-width":{px:1,em:1,"%":1},"min-height":{px:1,em:1,"%":1},"min-width":{px:1,em:1,"%":1},overflow:{hidden:1,visible:1,auto:1,scroll:1},"overflow-x":{hidden:1,visible:1,auto:1,scroll:1},"overflow-y":{hidden:1,visible:1,auto:1,scroll:1},padding:{px:1,em:1,"%":1},"padding-top":{px:1,em:1,"%":1},"padding-right":{px:1,em:1,"%":1},"padding-bottom":{px:1,em:1,"%":1},"padding-left":{px:1,em:1,"%":1},"page-break-after":{auto:1,always:1,avoid:1,left:1,right:1},"page-break-before":{auto:1,always:1,avoid:1,left:1,right:1},position:{absolute:1,relative:1,fixed:1,static:1},right:{px:1,em:1,"%":1},"table-layout":{fixed:1,auto:1},"text-decoration":{none:1,underline:1,"line-through":1,blink:1},"text-align":{left:1,right:1,center:1,justify:1},"text-transform":{capitalize:1,uppercase:1,lowercase:1,none:1},top:{px:1,em:1,"%":1},"vertical-align":{top:1,bottom:1},visibility:{hidden:1,visible:1},"white-space":{nowrap:1,normal:1,pre:1,"pre-line":1,"pre-wrap":1},width:{px:1,em:1,"%":1},"word-spacing":{normal:1},filter:{"alpha(opacity=$0100)":1},"text-shadow":{"$02px 2px 2px #777":1},"text-overflow":{"ellipsis-word":1,clip:1,ellipsis:1},"-moz-border-radius":1,"-moz-border-radius-topright":1,"-moz-border-radius-bottomright":1,"-moz-border-radius-topleft":1,"-moz-border-radius-bottomleft":1,"-webkit-border-radius":1,"-webkit-border-top-right-radius":1,"-webkit-border-top-left-radius":1,"-webkit-border-bottom-right-radius":1,"-webkit-border-bottom-left-radius":1,"-moz-box-shadow":1,"-webkit-box-shadow":1,transform:{"rotate($00deg)":1,"skew($00deg)":1},"-moz-transform":{"rotate($00deg)":1,"skew($00deg)":1},"-webkit-transform":{"rotate($00deg)":1,"skew($00deg)":1}},h=function(){};(function(){this.completionsDefined=!1,this.defineCompletions=function(){if(document){var c=document.createElement("c").style;for(var l in c)if(typeof c[l]=="string"){var t=l.replace(/[A-Z]/g,function(a){return"-"+a.toLowerCase()});m.hasOwnProperty(t)||(m[t]=1)}}this.completionsDefined=!0},this.getCompletions=function(c,l,t,a){if(this.completionsDefined||this.defineCompletions(),c==="ruleset"||l.$mode.$id=="ace/mode/scss"){var r=l.getLine(t.row).substr(0,t.column),i=/\([^)]*$/.test(r);return i&&(r=r.substr(r.lastIndexOf("(")+1)),/:[^;]+$/.test(r)?(/([\w\-]+):[^:]*$/.test(r),this.getPropertyValueCompletions(c,l,t,a)):this.getPropertyCompletions(c,l,t,a,i)}return[]},this.getPropertyCompletions=function(c,l,t,a,r){r=r||!1;var i=Object.keys(m);return i.map(function(n){return{caption:n,snippet:n+": $0"+(r?"":";"),meta:"property",score:1e6}})},this.getPropertyValueCompletions=function(c,l,t,a){var r=l.getLine(t.row).substr(0,t.column),i=(/([\w\-]+):[^:]*$/.exec(r)||{})[1];if(!i)return[];var n=[];return i in m&&typeof m[i]=="object"&&(n=Object.keys(m[i])),n.map(function(e){return{caption:e,snippet:e,meta:"property value",score:1e6}})}}).call(h.prototype),p.CssCompletions=h}),ace.define("ace/mode/behaviour/css",["require","exports","module","ace/lib/oop","ace/mode/behaviour","ace/mode/behaviour/cstyle","ace/token_iterator"],function(d,p,f){"use strict";var m=d("../../lib/oop"),h=d("../behaviour").Behaviour,c=d("./cstyle").CstyleBehaviour,l=d("../../token_iterator").TokenIterator,t=function(){this.inherit(c),this.add("colon","insertion",function(a,r,i,n,e){if(e===":"&&i.selection.isEmpty()){var o=i.getCursorPosition(),s=new l(n,o.row,o.column),u=s.getCurrentToken();if(u&&u.value.match(/\s+/)&&(u=s.stepBackward()),u&&u.type==="support.type"){var g=n.doc.getLine(o.row),b=g.substring(o.column,o.column+1);if(b===":")return{text:"",selection:[1,1]};if(/^(\s+[^;]|\s*$)/.test(g.substring(o.column)))return{text:":;",selection:[1,1]}}}}),this.add("colon","deletion",function(a,r,i,n,e){var o=n.doc.getTextRange(e);if(!e.isMultiLine()&&o===":"){var s=i.getCursorPosition(),u=new l(n,s.row,s.column),g=u.getCurrentToken();if(g&&g.value.match(/\s+/)&&(g=u.stepBackward()),g&&g.type==="support.type"){var b=n.doc.getLine(e.start.row),x=b.substring(e.end.column,e.end.column+1);if(x===";")return e.end.column++,e}}}),this.add("semicolon","insertion",function(a,r,i,n,e){if(e===";"&&i.selection.isEmpty()){var o=i.getCursorPosition(),s=n.doc.getLine(o.row),u=s.substring(o.column,o.column+1);if(u===";")return{text:"",selection:[1,1]}}}),this.add("!important","insertion",function(a,r,i,n,e){if(e==="!"&&i.selection.isEmpty()){var o=i.getCursorPosition(),s=n.doc.getLine(o.row);if(/^\s*(;|}|$)/.test(s.substring(o.column)))return{text:"!important",selection:[10,10]}}})};m.inherits(t,c),p.CssBehaviour=t}),ace.define("ace/mode/folding/cstyle",["require","exports","module","ace/lib/oop","ace/range","ace/mode/folding/fold_mode"],function(d,p,f){"use strict";var m=d("../../lib/oop"),h=d("../../range").Range,c=d("./fold_mode").FoldMode,l=p.FoldMode=function(t){t&&(this.foldingStartMarker=new RegExp(this.foldingStartMarker.source.replace(/\|[^|]*?$/,"|"+t.start)),this.foldingStopMarker=new RegExp(this.foldingStopMarker.source.replace(/\|[^|]*?$/,"|"+t.end)))};m.inherits(l,c),function(){this.foldingStartMarker=/([\{\[\(])[^\}\]\)]*$|^\s*(\/\*)/,this.foldingStopMarker=/^[^\[\{\(]*([\}\]\)])|^[\s\*]*(\*\/)/,this.singleLineBlockCommentRe=/^\s*(\/\*).*\*\/\s*$/,this.tripleStarBlockCommentRe=/^\s*(\/\*\*\*).*\*\/\s*$/,this.startRegionRe=/^\s*(\/\*|\/\/)#?region\b/,this._getFoldWidgetBase=this.getFoldWidget,this.getFoldWidget=function(t,a,r){var i=t.getLine(r);if(this.singleLineBlockCommentRe.test(i)&&!this.startRegionRe.test(i)&&!this.tripleStarBlockCommentRe.test(i))return"";var n=this._getFoldWidgetBase(t,a,r);return!n&&this.startRegionRe.test(i)?"start":n},this.getFoldWidgetRange=function(t,a,r,i){var n=t.getLine(r);if(this.startRegionRe.test(n))return this.getCommentRegionBlock(t,n,r);var e=n.match(this.foldingStartMarker);if(e){var o=e.index;if(e[1])return this.openingBracketBlock(t,e[1],r,o);var s=t.getCommentFoldRange(r,o+e[0].length,1);return s&&!s.isMultiLine()&&(i?s=this.getSectionRange(t,r):a!="all"&&(s=null)),s}if(a!=="markbegin"){var e=n.match(this.foldingStopMarker);if(e){var o=e.index+e[0].length;return e[1]?this.closingBracketBlock(t,e[1],r,o):t.getCommentFoldRange(r,o,-1)}}},this.getSectionRange=function(t,a){var r=t.getLine(a),i=r.search(/\S/),n=a,e=r.length;a=a+1;for(var o=a,s=t.getLength();++a<s;){r=t.getLine(a);var u=r.search(/\S/);if(u!==-1){if(i>u)break;var g=this.getFoldWidgetRange(t,"all",a);if(g){if(g.start.row<=n)break;if(g.isMultiLine())a=g.end.row;else if(i==u)break}o=a}}return new h(n,e,o,t.getLine(o).length)},this.getCommentRegionBlock=function(t,a,r){for(var i=a.search(/\s*$/),n=t.getLength(),e=r,o=/^\s*(?:\/\*|\/\/|--)#?(end)?region\b/,s=1;++r<n;){a=t.getLine(r);var u=o.exec(a);if(!!u&&(u[1]?s--:s++,!s))break}var g=r;if(g>e)return new h(e,i,g,a.length)}}.call(l.prototype)}),ace.define("ace/mode/css",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/css_highlight_rules","ace/mode/matching_brace_outdent","ace/worker/worker_client","ace/mode/css_completions","ace/mode/behaviour/css","ace/mode/folding/cstyle"],function(d,p,f){"use strict";var m=d("../lib/oop"),h=d("./text").Mode,c=d("./css_highlight_rules").CssHighlightRules,l=d("./matching_brace_outdent").MatchingBraceOutdent,t=d("../worker/worker_client").WorkerClient,a=d("./css_completions").CssCompletions,r=d("./behaviour/css").CssBehaviour,i=d("./folding/cstyle").FoldMode,n=function(){this.HighlightRules=c,this.$outdent=new l,this.$behaviour=new r,this.$completer=new a,this.foldingRules=new i};m.inherits(n,h),function(){this.foldingRules="cStyle",this.blockComment={start:"/*",end:"*/"},this.getNextLineIndent=function(e,o,s){var u=this.$getIndent(o),g=this.getTokenizer().getLineTokens(o,e).tokens;if(g.length&&g[g.length-1].type=="comment")return u;var b=o.match(/^.*\{\s*$/);return b&&(u+=s),u},this.checkOutdent=function(e,o,s){return this.$outdent.checkOutdent(o,s)},this.autoOutdent=function(e,o,s){this.$outdent.autoOutdent(o,s)},this.getCompletions=function(e,o,s,u){return this.$completer.getCompletions(e,o,s,u)},this.createWorker=function(e){var o=new t(["ace"],"ace/mode/css_worker","Worker");return o.attachToDocument(e.getDocument()),o.on("annotate",function(s){e.setAnnotations(s.data)}),o.on("terminate",function(){e.clearAnnotations()}),o},this.$id="ace/mode/css",this.snippetFileId="ace/snippets/css"}.call(n.prototype),p.Mode=n}),function(){ace.require(["ace/mode/css"],function(d){v&&(v.exports=d)})}()}}]);