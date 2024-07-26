(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[68245,42270],{74944:function(l){l.exports={sdkMainBox:"sdkMainBox___2O1X0"}},15566:function(l,_,a){"use strict";a.r(_),a.d(_,{default:function(){return F}});var s=a(57663),t=a(71577),m=a(28216),p=a(67294),r=a(74981),D=a(56395),b=a.n(D),B=a(42270),w=a.n(B),g=a(82679),C=a.n(g),v=a(74944),k=a.n(v),e=a(85893),h,E,F=(h=(0,m.$j)(x=>{var n=x.design;return{design:n}}),h(E=class extends p.PureComponent{constructor(){super(...arguments);this.state={}}renderSelectFileView(){var n=this,d=this.state.imageData,o=`
    fileBoxApi.fileSelect({
      title:"\u9009\u62E9\u6587\u6863",
      allowExt:'pdf,ofd,doc,docx,xls,xlsx,ppt,pptx',    
      callback:function(result){
          const data = result.data.selectItems[0]
          const yzViewData = JSON.parse(data.yzViewData)
          fileBoxApi.fileView(yzViewData.viewUrl,{title: data.name});
      }
  });`,c=()=>{fileBoxApi.fileSelect({title:"\u9009\u62E9\u6587\u6863",allowExt:"pdf,ofd,doc,docx,xls,xlsx,ppt,pptx",callback:function(u){var i=u.data.selectItems[0],A=i.yzViewData?JSON.parse(i.yzViewData):{};fileBoxApi.fileView(A.viewUrl,{title:i.name})}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u9009\u62E9\u4E00\u4E2A\u6587\u6863\u5E76\u6253\u5F00"}),(0,e.jsx)(r.ZP,{width:"100%",height:"200px",style:{"border-radius":"4px"},value:o,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsx)("div",{className:"buttonBox",children:(0,e.jsx)(t.Z,{type:"primary",onClick:c,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"})})]})}renderSelectFileView2(){var n=this,d=this.state.imageData,o=`
    fileBoxApi.fileSelect({
      title:"\u9009\u62E9\u6587\u6863",
      allowExt:'pdf,ofd,doc,docx,xls,xlsx,ppt,pptx',    
      callback:function(result){
          const data = result.data.selectItems[0]
          const yzViewData = JSON.parse(data.yzViewData)
          fileBoxApi.fileView(yzViewData.viewUrl, '#selectFileView');
      }
    });`,c=()=>{fileBoxApi.fileSelect({title:"\u9009\u62E9\u6587\u6863",allowExt:"pdf,ofd,doc,docx,xls,xlsx,ppt,pptx",callback:function(u){var i=u.data.selectItems[0],A=i.yzViewData?JSON.parse(i.yzViewData):{};fileBoxApi.fileView(A.viewUrl,"#selectFileView")}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u9009\u62E9\u4E00\u4E2A\u6587\u6863\u5E76\u5D4C\u5165"}),(0,e.jsx)(r.ZP,{width:"100%",height:"200px",style:{"border-radius":"4px"},value:o,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsxs)("div",{className:"buttonBox",children:[(0,e.jsx)(t.Z,{type:"primary",onClick:c,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"}),(0,e.jsx)("div",{id:"selectFileView",style:{marginTop:10}})]})]})}renderSelectFolder(){var n=this,d=this.state.imageData,o=`
    fileBoxApi.folderView(path,option);

    // 1. option \u4E3A\u5B57\u7B26\u4E32\u6216dom\u65F6, \u4EE3\u8868\u5D4C\u5165\u9875\u9762\u7684\u65B9\u5F0F\u663E\u793A\u6587\u4EF6\u9884\u89C8
    // 2. option \u4E3A\u5BF9\u8C61\u65F6; \u4EE3\u8868\u5BF9\u8BDD\u6846\u9884\u89C8\u6587\u4EF6

    fileBoxApi.folderView({    
      width: 900,
      height: 500,                   // \u5BF9\u8BDD\u6846\u5BBD\u5EA6\u53CA\u9AD8\u5EA6;
      background: '#000',            // \u9501\u5B9A\u540E\u8499\u5C42\u989C\u8272
      opacity: 0.5,                  // \u9501\u5B9A\u540E\u8499\u5C42\u900F\u660E\u5EA6
      resize: true,                  // \u662F\u5426\u53EF\u4EE5\u8C03\u8282\u5BF9\u8BDD\u6846\u9AD8\u5EA6\u548C\u5BBD\u5EA6
    })`;return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u6587\u4EF6\u5939\u9884\u89C8"}),(0,e.jsx)(r.ZP,{width:"100%",height:"250px",style:{"border-radius":"4px"},value:o,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}})]})}renderSelectFolderView(){var n=this,d=this.state.imageData,o=`
    fileBoxApi.fileSelect({
      title:"\u9009\u62E9\u6587\u6863",
      type: "folder", 
      callback:function(result){
          const data = result.data.selectItems[0]          
          fileBoxApi.folderView({ sourceID : data.sourceID }, {title: data.name});
      }
  });`,c=()=>{fileBoxApi.fileSelect({title:"\u9009\u62E9\u6587\u6863",type:"folder",callback:function(u){var i=u.data.selectItems[0];fileBoxApi.folderView({sourceID:i.sourceID},{title:i.name})}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u5BF9\u8BDD\u6846\u6253\u5F00\u6587\u4EF6\u5939"}),(0,e.jsx)(r.ZP,{width:"100%",height:"180px",style:{"border-radius":"4px"},value:o,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsx)("div",{className:"buttonBox",children:(0,e.jsx)(t.Z,{type:"primary",onClick:c,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"})})]})}renderSelectFolderView2(){var n=this,d=this.state.imageData,o=`
    fileBoxApi.fileSelect({
      title:"\u9009\u62E9\u6587\u6863",
      type: "folder", 
      callback:function(result){
          const data = result.data.selectItems[0]          
          fileBoxApi.folderView({ sourceID : data.sourceID}, '#selectFolderView');
      }
    });`,c=()=>{fileBoxApi.fileSelect({title:"\u9009\u62E9\u6587\u6863",type:"folder",callback:function(u){var i=u.data.selectItems[0];fileBoxApi.folderView({sourceID:i.sourceID},"#selectFolderView")}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u5D4C\u5165\u6587\u4EF6\u5939"}),(0,e.jsx)(r.ZP,{width:"100%",height:"190px",style:{"border-radius":"4px"},value:o,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsxs)("div",{className:"buttonBox",children:[(0,e.jsx)(t.Z,{type:"primary",onClick:c,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"}),(0,e.jsx)("div",{id:"selectFolderView",style:{marginTop:10}})]})]})}render(){var n=this,d=`
    fileBoxApi.fileView(path,option);

    // 1. option \u4E3A\u5B57\u7B26\u4E32\u6216dom\u65F6, \u4EE3\u8868\u5D4C\u5165\u9875\u9762\u7684\u65B9\u5F0F\u663E\u793A\u6587\u4EF6\u9884\u89C8
    // 2. option \u4E3A\u5BF9\u8C61\u65F6; \u4EE3\u8868\u5BF9\u8BDD\u6846\u9884\u89C8\u6587\u4EF6

    fileBoxApi.fileView({    
      width: 900,
      height: 500,                   // \u5BF9\u8BDD\u6846\u5BBD\u5EA6\u53CA\u9AD8\u5EA6;
      background: '#000',            // \u9501\u5B9A\u540E\u8499\u5C42\u989C\u8272
      opacity: 0.5,                  // \u9501\u5B9A\u540E\u8499\u5C42\u900F\u660E\u5EA6
      resize: true,                  // \u662F\u5426\u53EF\u4EE5\u8C03\u8282\u5BF9\u8BDD\u6846\u9AD8\u5EA6\u548C\u5BBD\u5EA6
    })`;return(0,e.jsx)("div",{className:"layoutBox",children:(0,e.jsxs)("div",{className:k().sdkMainBox,children:[(0,e.jsx)("h1",{className:"sdk-header",children:"\u6587\u4EF6\u9884\u89C8"}),(0,e.jsx)("h2",{className:"sdk-title",children:"\u63A5\u53E3\u8BF4\u660E"}),(0,e.jsx)(r.ZP,{width:"100%",height:"250px",style:{"border-radius":"4px"},value:d,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),n.renderSelectFileView(),n.renderSelectFileView2(),n.renderSelectFolder(),n.renderSelectFolderView(),n.renderSelectFolderView2()]})})}})||E)},42270:function(l,_,a){l=a.nmd(l),ace.define("ace/theme/monokai.css",["require","exports","module"],function(s,t,m){m.exports=`.ace-monokai .ace_gutter {
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
`}),ace.define("ace/theme/monokai",["require","exports","module","ace/theme/monokai.css","ace/lib/dom"],function(s,t,m){t.isDark=!0,t.cssClass="ace-monokai",t.cssText=s("./monokai.css");var p=s("../lib/dom");p.importCssString(t.cssText,t.cssClass,!1)}),function(){ace.require(["ace/theme/monokai"],function(s){l&&(l.exports=s)})}()}}]);
