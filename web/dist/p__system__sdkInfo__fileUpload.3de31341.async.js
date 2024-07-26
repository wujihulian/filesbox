(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[97384,42270],{74944:function(o){o.exports={sdkMainBox:"sdkMainBox___2O1X0"}},55862:function(o,s,n){"use strict";n.r(s),n.d(s,{default:function(){return j}});var t=n(57663),a=n(71577),c=n(28216),r=n(67294),l=n(74981),D=n(56395),y=n.n(D),x=n(42270),I=n.n(x),B=n(82679),U=n.n(B),b=n(74944),C=n.n(b),e=n(85893),p,F,j=(p=(0,c.$j)(g=>{var u=g.design;return{design:u}}),p(F=class extends r.PureComponent{constructor(){super(...arguments);this.state={}}renderFileCreate(){var u=this,m=this.state.imageData,d=`
    // fileName;\u6587\u4EF6\u540D; content:\u6587\u4EF6\u5185\u5BB9
    // callback(path);
    fileBoxApi.fileCreate({fileName,content,callback});`,_=`
    fileBoxApi.fileCreate({
      fileName: "test.txt",
      content: "hello",
      callback: function(result){
        var json2Html = function(json){
            var text = JSON.stringify(json,null,"    ");
            var html = hljs.highlightAuto(text).value;
            return '<pre><code class="hljs">'+html+'</code></pre>';
        }
        $('#fileCreate').html(json2Html(result))  
      }
    });`,E=()=>{fileBoxApi.fileCreate({fileName:"test.txt",content:"hello",callback:function(i){var h=function(v){var A=JSON.stringify(v,null,"    "),f=hljs.highlightAuto(A).value;return'<pre><code class="hljs">'+f+"</code></pre>"};$("#fileCreate").html(h(i))}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u6587\u4EF6\u521B\u5EFA\u5E76\u5199\u5165"}),(0,e.jsx)(l.ZP,{width:"100%",height:"90px",style:{"border-radius":"4px"},value:d,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsx)("p",{style:{marginTop:10,fontSize:16},children:"demo\u6D4B\u8BD5: \u65B0\u5EFAtest.txt \u5E76\u5199\u5165\u5185\u5BB9;"}),(0,e.jsx)(l.ZP,{width:"100%",height:"250px",style:{"border-radius":"4px"},value:_,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsxs)("div",{className:"buttonBox",children:[(0,e.jsx)(a.Z,{type:"primary",onClick:E,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"}),(0,e.jsx)("div",{id:"fileCreate",style:{marginTop:10}})]})]})}renderFileUpload(){var u=this,m=this.state.imageData,d=`
    /**
    * \u4E0A\u4F20\u6587\u4EF6\u5230\u6307\u5B9A\u8DEF\u5F84
    * file: \u4E0A\u4F20\u7684\u6587\u4EF6, file\u5BF9\u8C61;
    * sourceID: \u4E0A\u4F20\u5230\u76EE\u6807\u6587\u4EF6\u5939\u7684sourceID,\u4E0D\u5B58\u5728\u65F6\u81EA\u52A8\u4E0A\u4F20\u5230\u4E3B\u76EE\u5F55
    * callback: \u4E0A\u4F20\u6210\u529F\u540E\u56DE\u8C03;;
    */
    fileBoxApi.fileUpload(file,sourceID,callback);
   
    // \u4ECE\u672C\u5730\u9009\u62E9\u6587\u4EF6\u4E0A\u4F20
    fileBoxApi.fileUploadSelect({
        sourceID: "",       // \u4E0A\u4F20\u5230\u76EE\u6807\u6587\u4EF6\u5939\u7684sourceID,\u4E0D\u5B58\u5728\u65F6\u81EA\u52A8\u4E0A\u4F20\u5230\u4E3B\u76EE\u5F55
        allowExt:'',        // \u6587\u4EF6\u6269\u5C55\u540D\u9650\u5236,\u4E0D\u586B\u5219\u4E0D\u9650\u5236;
        single:true,        // \u9ED8\u8BA4\u5355\u4E2A\u6587\u4EF6; false\u65F6\u4E3A\u591A\u9009\u4E0A\u4F20;
        callback: function(result) {} // \u4E0A\u4F20\u6210\u529F\u540E\u56DE\u8C03; 
    }); `,_=`
    fileBoxApi.fileUploadSelect({
      // sourceID:"",
      callback: function(result){
        var json2Html = function(json){
            var text = JSON.stringify(json,null,"    ");
            var html = hljs.highlightAuto(text).value + '<img src="'+result.sourcePath+'"/>';
            return '<pre><code class="hljs">'+html+'</code></pre>';
        }
        $('#fileUpload').html(json2Html(result)) 
      }
    });`,E=()=>{fileBoxApi.fileUploadSelect({callback:function(i){var h=function(A){var f=JSON.stringify(A,null,"    "),P=hljs.highlightAuto(f).value;return'<pre><code class="hljs">'+P+"</code></pre>"},k=h(i)+'<img src="'+i.data.selectItems[0].sourcePath+'"/>';$("#fileUpload").html(k)}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u6587\u4EF6\u4E0A\u4F20"}),(0,e.jsx)("p",{style:{fontSize:16},children:"\u4E0A\u4F20\u5230\u6307\u5B9A\u6587\u4EF6\u5939, \u6216\u4E0A\u4F20\u5230\u81EA\u5B9A\u4E49\u76EE\u5F55;"}),(0,e.jsx)(l.ZP,{width:"100%",height:"305px",style:{"border-radius":"4px"},value:d,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsx)("p",{style:{marginTop:10,fontSize:16},children:"demo \u4E0A\u4F20\u6D4B\u8BD5: \u9009\u62E9\u56FE\u7247\u8FDB\u884C\u4E0A\u4F20,\u5B8C\u6210\u540E\u5C55\u793A\u6587\u4EF6\u4FE1\u606F\u53CA\u56FE\u7247"}),(0,e.jsx)(l.ZP,{width:"100%",height:"235px",style:{"border-radius":"4px"},value:_,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsxs)("div",{className:"buttonBox",children:[(0,e.jsx)(a.Z,{type:"primary",onClick:E,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"}),(0,e.jsx)("div",{id:"fileUpload",style:{marginTop:10}})]})]})}render(){var u=this,m=`
    // sourceID;\u6587\u4EF6sourceID; content:\u6587\u4EF6\u5185\u5BB9
    fileBoxApi.fileSave(sourceID, content, callback);`;return(0,e.jsx)("div",{className:"layoutBox",children:(0,e.jsxs)("div",{className:C().sdkMainBox,children:[(0,e.jsx)("h1",{className:"sdk-header",children:"\u6587\u4EF6\u8BFB\u5199\u4E0A\u4F20"}),(0,e.jsx)("h2",{className:"sdk-title",children:"\u6587\u4EF6\u4FDD\u5B58"}),(0,e.jsx)("p",{style:{fontSize:16},children:"\u5DF2\u77E5\u6587\u4EF6sourceID,\u53EF\u4EE5\u8C03\u7528\u5199\u5165\u66F4\u65B0\u6587\u4EF6\u5185\u5BB9;"}),(0,e.jsx)(l.ZP,{width:"100%",height:"70px",style:{"border-radius":"4px"},value:m,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),u.renderFileCreate(),u.renderFileUpload()]})})}})||F)},42270:function(o,s,n){o=n.nmd(o),ace.define("ace/theme/monokai.css",["require","exports","module"],function(t,a,c){c.exports=`.ace-monokai .ace_gutter {
  background: #2F3129;
  color: #8F908A
}

.ace-monokai .ace_print-margin {
  width: 1px;
  background: #555651
}

.ace-monokai {
  background-color: #272822;
  color: #F8F8F2
}

.ace-monokai .ace_cursor {
  color: #F8F8F0
}

.ace-monokai .ace_marker-layer .ace_selection {
  background: #49483E
}

.ace-monokai.ace_multiselect .ace_selection.ace_start {
  box-shadow: 0 0 3px 0px #272822;
}

.ace-monokai .ace_marker-layer .ace_step {
  background: rgb(102, 82, 0)
}

.ace-monokai .ace_marker-layer .ace_bracket {
  margin: -1px 0 0 -1px;
  border: 1px solid #49483E
}

.ace-monokai .ace_marker-layer .ace_active-line {
  background: #202020
}

.ace-monokai .ace_gutter-active-line {
  background-color: #272727
}

.ace-monokai .ace_marker-layer .ace_selected-word {
  border: 1px solid #49483E
}

.ace-monokai .ace_invisible {
  color: #52524d
}

.ace-monokai .ace_entity.ace_name.ace_tag,
.ace-monokai .ace_keyword,
.ace-monokai .ace_meta.ace_tag,
.ace-monokai .ace_storage {
  color: #F92672
}

.ace-monokai .ace_punctuation,
.ace-monokai .ace_punctuation.ace_tag {
  color: #fff
}

.ace-monokai .ace_constant.ace_character,
.ace-monokai .ace_constant.ace_language,
.ace-monokai .ace_constant.ace_numeric,
.ace-monokai .ace_constant.ace_other {
  color: #AE81FF
}

.ace-monokai .ace_invalid {
  color: #F8F8F0;
  background-color: #F92672
}

.ace-monokai .ace_invalid.ace_deprecated {
  color: #F8F8F0;
  background-color: #AE81FF
}

.ace-monokai .ace_support.ace_constant,
.ace-monokai .ace_support.ace_function {
  color: #66D9EF
}

.ace-monokai .ace_fold {
  background-color: #A6E22E;
  border-color: #F8F8F2
}

.ace-monokai .ace_storage.ace_type,
.ace-monokai .ace_support.ace_class,
.ace-monokai .ace_support.ace_type {
  font-style: italic;
  color: #66D9EF
}

.ace-monokai .ace_entity.ace_name.ace_function,
.ace-monokai .ace_entity.ace_other,
.ace-monokai .ace_entity.ace_other.ace_attribute-name,
.ace-monokai .ace_variable {
  color: #A6E22E
}

.ace-monokai .ace_variable.ace_parameter {
  font-style: italic;
  color: #FD971F
}

.ace-monokai .ace_string {
  color: #E6DB74
}

.ace-monokai .ace_comment {
  color: #75715E
}

.ace-monokai .ace_indent-guide {
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAEklEQVQImWPQ0FD0ZXBzd/wPAAjVAoxeSgNeAAAAAElFTkSuQmCC) right repeat-y
}

.ace-monokai .ace_indent-guide-active {
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAEklEQVQIW2PQ1dX9zzBz5sz/ABCcBFFentLlAAAAAElFTkSuQmCC) right repeat-y;
}
`}),ace.define("ace/theme/monokai",["require","exports","module","ace/theme/monokai.css","ace/lib/dom"],function(t,a,c){a.isDark=!0,a.cssClass="ace-monokai",a.cssText=t("./monokai.css");var r=t("../lib/dom");r.importCssString(a.cssText,a.cssClass,!1)}),function(){ace.require(["ace/theme/monokai"],function(t){o&&(o.exports=t)})}()}}]);
