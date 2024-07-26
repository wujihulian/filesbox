(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[2031],{2031:function(n,s,i){n=i.nmd(n),ace.define("ace/snippets/graphqlschema.snippets",["require","exports","module"],function(e,t,p){p.exports=`# Type Snippet
trigger type
snippet type
	type \${1:type_name} {
		\${2:type_siblings}
	}

# Input Snippet
trigger input
snippet input
	input \${1:input_name} {
		\${2:input_siblings}
	}

# Interface Snippet
trigger interface
snippet interface
	interface \${1:interface_name} {
		\${2:interface_siblings}
	}

# Interface Snippet
trigger union
snippet union
	union \${1:union_name} = \${2:type} | \${3: type}

# Enum Snippet
trigger enum
snippet enum
	enum \${1:enum_name} {
		\${2:enum_siblings}
	}
`}),ace.define("ace/snippets/graphqlschema",["require","exports","module","ace/snippets/graphqlschema.snippets"],function(e,t,p){"use strict";t.snippetText=e("./graphqlschema.snippets"),t.scope="graphqlschema"}),function(){ace.require(["ace/snippets/graphqlschema"],function(e){n&&(n.exports=e)})}()}}]);
