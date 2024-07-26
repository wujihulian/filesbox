(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[44560],{44560:function(e,t,i){e=i.nmd(e),ace.define("ace/snippets/maze.snippets",["require","exports","module"],function(n,s,p){p.exports=`snippet >
description assignment
scope maze
	-> \${1}= \${2}

snippet >
description if
scope maze
	-> IF \${2:**} THEN %\${3:L} ELSE %\${4:R}
`}),ace.define("ace/snippets/maze",["require","exports","module","ace/snippets/maze.snippets"],function(n,s,p){"use strict";s.snippetText=n("./maze.snippets"),s.scope="maze"}),function(){ace.require(["ace/snippets/maze"],function(n){e&&(e.exports=n)})}()}}]);
