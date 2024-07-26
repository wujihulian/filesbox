(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[66593],{66593:function(e,s,p){e=p.nmd(e),ace.define("ace/snippets/velocity.snippets",["require","exports","module"],function(t,n,i){i.exports=`# macro
snippet #macro
	#macro ( \${1:macroName} \${2:\\$var1, [\\$var2, ...]} )
		\${3:## macro code}
	#end
# foreach
snippet #foreach
	#foreach ( \${1:\\$item} in \${2:\\$collection} )
		\${3:## foreach code}
	#end
# if
snippet #if
	#if ( \${1:true} )
		\${0}
	#end
# if ... else
snippet #ife
	#if ( \${1:true} )
		\${2}
	#else
		\${0}
	#end
#import
snippet #import
	#import ( "\${1:path/to/velocity/format}" )
# set
snippet #set
	#set ( $\${1:var} = \${0} )
`}),ace.define("ace/snippets/velocity",["require","exports","module","ace/snippets/velocity.snippets"],function(t,n,i){"use strict";n.snippetText=t("./velocity.snippets"),n.scope="velocity",n.includeScopes=["html","javascript","css"]}),function(){ace.require(["ace/snippets/velocity"],function(t){e&&(e.exports=t)})}()}}]);
