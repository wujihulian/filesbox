(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[38298],{38298:function(e,r,p){e=p.nmd(e),ace.define("ace/snippets/drools.snippets",["require","exports","module"],function(n,t,s){s.exports=`
snippet rule
	rule "\${1?:rule_name}"
	when
		\${2:// when...} 
	then
		\${3:// then...}
	end

snippet query
	query \${1?:query_name}
		\${2:// find} 
	end
	
snippet declare
	declare \${1?:type_name}
		\${2:// attributes} 
	end

`}),ace.define("ace/snippets/drools",["require","exports","module","ace/snippets/drools.snippets"],function(n,t,s){"use strict";t.snippetText=n("./drools.snippets"),t.scope="drools"}),function(){ace.require(["ace/snippets/drools"],function(n){e&&(e.exports=n)})}()}}]);
