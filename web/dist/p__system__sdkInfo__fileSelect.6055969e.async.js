(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[28835],{74944:function(d){d.exports={sdkMainBox:"sdkMainBox___2O1X0"}},73711:function(d,m,t){"use strict";t.r(m),t.d(m,{default:function(){return F}});var M=t(57663),E=t(71577),p=t(28216),v=t(67294),i=t(74981),D=t(56395),C=t.n(D),B=t(42270),b=t.n(B),A=t(82679),S=t.n(A),j=t(74944),x=t.n(j),y=t(77396),e=t(85893),_,f,F=(_=(0,p.$j)(g=>{var u=g.design;return{design:u}}),_(f=class extends v.PureComponent{constructor(){super(...arguments);this.state={imageData:""}}renderSelectImage(){var u=this,l=this.state.imageData,h=`
    fileBoxApi.fileSelect({
      title: "\u9009\u62E9\u56FE\u7247",           // \u9009\u62E9\u5BF9\u8BDD\u6846\u6807\u9898;(\u53EF\u9009, \u9ED8\u8BA4\u4E3A"\u6587\u4EF6\u9009\u62E9")
      allowExt: 'png,jpg,bmp,gif',
      callback: function(result){  // \u56DE\u8C03\u5730\u5740;
        var json2Html = function(json){
          var text = JSON.stringify(json, null, "    ");
          var html = hljs.highlightAuto(text).value;
          var currentItem = json.data.selectItems[0]
          return '<pre><code class="hljs">'+html+'</code></pre>'
                +'<img src="'+currentItem.path+'"/>';
        }
        $('#selectImage').html(json2Html(result))   
      }
    });`,n=()=>{fileBoxApi.fileSelect({title:"\u9009\u62E9\u56FE\u7247",allowExt:"png,jpg,bmp,gif",callback:function(o){var s=function(a){var O=JSON.stringify(a,null,"    "),P=hljs.highlightAuto(O).value,I=o.data.selectItems[0];return'<pre><code class="hljs">'+P+'</code></pre><img src="'+I.path+'"/>'};$("#selectImage").html(s(o))}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u9009\u62E9\u56FE\u7247"}),(0,e.jsx)(i.ZP,{width:"100%",height:"290px",style:{"border-radius":"4px"},value:h,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsxs)("div",{className:"buttonBox",children:[(0,e.jsx)(E.Z,{type:"primary",onClick:n,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"}),(0,e.jsx)("div",{id:"selectImage",style:{marginTop:10}})]})]})}renderSelectFolder(){var u=`
    fileBoxApi.fileSelect({
      title: "\u9009\u62E9\u6587\u4EF6\u5939",           // \u9009\u62E9\u5BF9\u8BDD\u6846\u6807\u9898;(\u53EF\u9009, \u9ED8\u8BA4\u4E3A"\u6587\u4EF6\u9009\u62E9")
      type: "folder",
      callback: function(result){  // \u56DE\u8C03\u5730\u5740;
        var json2Html = function(json){
            var text = JSON.stringify(json, null, "    ");
            var html = hljs.highlightAuto(text).value;
            return '<pre><code class="hljs">'+html+'</code></pre>';
        }
        $('#selectFolder').html(json2Html(result)) 
      }
    });`,l=()=>{fileBoxApi.fileSelect({title:"\u9009\u62E9\u6587\u4EF6\u5939",allowExt:"png,jpg,bmp,gif",callback:function(n){var r=function(s){var c=JSON.stringify(s,null,"    "),a=hljs.highlightAuto(c).value;return'<pre><code class="hljs">'+a+"</code></pre>"};$("#selectFolder").html(r(n))}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u9009\u62E9\u6587\u4EF6\u5939"}),(0,e.jsx)(i.ZP,{width:"100%",height:"260px",style:{"border-radius":"4px"},value:u,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsxs)("div",{className:"buttonBox",children:[(0,e.jsx)(E.Z,{type:"primary",onClick:l,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"}),(0,e.jsx)("div",{id:"selectFolder",style:{marginTop:10}})]})]})}renderMultipleImage(){var u=`
    fileBoxApi.fileSelect({
      title: "\u591A\u9009\u56FE\u7247",           // \u9009\u62E9\u5BF9\u8BDD\u6846\u6807\u9898;(\u53EF\u9009, \u9ED8\u8BA4\u4E3A"\u6587\u4EF6\u9009\u62E9")
      allowExt:'png,jpg,jpeg,gif',
      single: false,
      callback: function(result){  // \u56DE\u8C03\u5730\u5740;
        var json2Html = function(json){
            var text = JSON.stringify(json, null, "    ");
            var html = hljs.highlightAuto(text).value;
            return '<pre><code class="hljs">'+html+'</code></pre>';
        }
        $('#multipleImage').html(json2Html(result))  
      }
    });`,l=()=>{fileBoxApi.fileSelect({title:"\u591A\u9009\u56FE\u7247",allowExt:"png,jpg,jpeg,gif",single:!1,callback:function(n){var r=function(s){var c=JSON.stringify(s,null,"    "),a=hljs.highlightAuto(c).value;return'<pre><code class="hljs">'+a+"</code></pre>"};$("#multipleImage").html(r(n))}})};return(0,e.jsxs)(e.Fragment,{children:[(0,e.jsx)("h2",{className:"sdk-title",children:"\u591A\u9009\u56FE\u7247"}),(0,e.jsx)(i.ZP,{width:"100%",height:"275px",style:{"border-radius":"4px"},value:u,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),(0,e.jsxs)("div",{className:"buttonBox",children:[(0,e.jsx)(E.Z,{type:"primary",onClick:l,style:{fontSize:16},children:"\u7ACB\u5373\u6D4B\u8BD5"}),(0,e.jsx)("div",{id:"multipleImage",style:{marginTop:10}})]})]})}render(){var u=this,l=`
    fileBoxApi.fileSelect({
      // \u6587\u4EF6\u9009\u62E9\u76F8\u5173\u914D\u7F6E, \u4EE5\u4E0B\u4E3A\u9ED8\u8BA4,\u8BBE\u7F6E\u503C\u5219\u8986\u76D6\u4E0B\u5217\u503C;      
      title: "\u6587\u4EF6\u9009\u62E9",              // \u9009\u62E9\u5BF9\u8BDD\u6846\u6807\u9898;(\u53EF\u9009, \u9ED8\u8BA4\u4E3A"\u6587\u4EF6\u9009\u62E9") 
      type: 'file',                  // \u9009\u62E9\u7C7B\u578B\uFF1Bfile|folder|all (\u53EF\u9009,\u9ED8\u8BA4file)
      single: true,                  // \u662F\u5426\u5355\u4E2A(\u53EF\u9009, \u9ED8\u8BA4\u4E3Atrue)
      allowExt:'',                   // \u7C7B\u578B\u4E3A\u6587\u4EF6\u65F6\u914D\u7F6E\u751F\u6548;\u591A\u4E2A\u9017\u53F7\u9694\u5F00; png,jpg,gif
      callback: function (result) {  // \u56DE\u8C03\u5730\u5740;
          console.error(result);
      }
      width: 900,
      height: 500,                   // \u5BF9\u8BDD\u6846\u5BBD\u5EA6\u53CA\u9AD8\u5EA6;
      background: '#000',            // \u9501\u5B9A\u540E\u8499\u5C42\u989C\u8272
      opacity: 0.5,                  // \u9501\u5B9A\u540E\u8499\u5C42\u900F\u660E\u5EA6
      resize: true,                  // \u662F\u5426\u53EF\u4EE5\u8C03\u8282\u5BF9\u8BDD\u6846\u9AD8\u5EA6\u548C\u5BBD\u5EA6
    })`;return(0,e.jsx)("div",{className:"layoutBox",children:(0,e.jsxs)("div",{className:x().sdkMainBox,children:[(0,e.jsx)("h1",{className:"sdk-header",children:"\u6587\u4EF6\u9009\u62E9"}),(0,e.jsx)("h2",{className:"sdk-title",children:"\u914D\u7F6E\u8BF4\u660E"}),(0,e.jsx)(i.ZP,{width:"100%",height:"310px",style:{"border-radius":"4px"},value:l,mode:"javascript",theme:"monokai",name:"UNIQUE_ID_OF_DIV",showGutter:!1,readOnly:!0,highlightActiveLine:!1,placeholder:"Placeholder Text",fontSize:15,showPrintMargin:!1,setOptions:{enableBasicAutocompletion:!1,enableLiveAutocompletion:!0,enableSnippets:!1,showLineNumbers:!1,tabSize:2}}),u.renderSelectImage(),u.renderSelectFolder(),u.renderMultipleImage()]})})}})||f)}}]);
