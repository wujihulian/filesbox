(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[68108],{68108:function(n,i,p){n=p.nmd(n),ace.define("ace/snippets/rst.snippets",["require","exports","module"],function(e,t,s){s.exports=`# rst

snippet :
	:\${1:field name}: \${2:field body}
snippet *
	*\${1:Emphasis}*
snippet **
	**\${1:Strong emphasis}**
snippet _
	\\\`\${1:hyperlink-name}\\\`_
	.. _\\\`$1\\\`: \${2:link-block}
snippet =
	\${1:Title}
	=====\${2:=}
	\${3}
snippet -
	\${1:Title}
	-----\${2:-}
	\${3}
snippet cont:
	.. contents::
	
`}),ace.define("ace/snippets/rst",["require","exports","module","ace/snippets/rst.snippets"],function(e,t,s){"use strict";t.snippetText=e("./rst.snippets"),t.scope="rst"}),function(){ace.require(["ace/snippets/rst"],function(e){n&&(n.exports=e)})}()}}]);
