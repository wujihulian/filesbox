(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[69674],{69674:function(t,s,i){t=i.nmd(t),ace.define("ace/snippets/haml.snippets",["require","exports","module"],function(e,n,p){p.exports=`snippet t
	%table
		%tr
			%th
				\${1:headers}
		%tr
			%td
				\${2:headers}
snippet ul
	%ul
		%li
			\${1:item}
		%li
snippet =rp
	= render :partial => '\${1:partial}'
snippet =rpl
	= render :partial => '\${1:partial}', :locals => {}
snippet =rpc
	= render :partial => '\${1:partial}', :collection => @$1

`}),ace.define("ace/snippets/haml",["require","exports","module","ace/snippets/haml.snippets"],function(e,n,p){"use strict";n.snippetText=e("./haml.snippets"),n.scope="haml"}),function(){ace.require(["ace/snippets/haml"],function(e){t&&(t.exports=e)})}()}}]);
