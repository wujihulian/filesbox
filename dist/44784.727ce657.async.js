(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[44784],{44784:function(g,y,_){g=_.nmd(g),ace.define("ace/ext/menu_tools/settings_menu.css",["require","exports","module"],function(t,f,s){s.exports=`#ace_settingsmenu, #kbshortcutmenu {
    background-color: #F7F7F7;
    color: black;
    box-shadow: -5px 4px 5px rgba(126, 126, 126, 0.55);
    padding: 1em 0.5em 2em 1em;
    overflow: auto;
    position: absolute;
    margin: 0;
    bottom: 0;
    right: 0;
    top: 0;
    z-index: 9991;
    cursor: default;
}

.ace_dark #ace_settingsmenu, .ace_dark #kbshortcutmenu {
    box-shadow: -20px 10px 25px rgba(126, 126, 126, 0.25);
    background-color: rgba(255, 255, 255, 0.6);
    color: black;
}

.ace_optionsMenuEntry:hover {
    background-color: rgba(100, 100, 100, 0.1);
    transition: all 0.3s
}

.ace_closeButton {
    background: rgba(245, 146, 146, 0.5);
    border: 1px solid #F48A8A;
    border-radius: 50%;
    padding: 7px;
    position: absolute;
    right: -8px;
    top: -8px;
    z-index: 100000;
}
.ace_closeButton{
    background: rgba(245, 146, 146, 0.9);
}
.ace_optionsMenuKey {
    color: darkslateblue;
    font-weight: bold;
}
.ace_optionsMenuCommand {
    color: darkcyan;
    font-weight: normal;
}
.ace_optionsMenuEntry input, .ace_optionsMenuEntry button {
    vertical-align: middle;
}

.ace_optionsMenuEntry button[ace_selected_button=true] {
    background: #e7e7e7;
    box-shadow: 1px 0px 2px 0px #adadad inset;
    border-color: #adadad;
}
.ace_optionsMenuEntry button {
    background: white;
    border: 1px solid lightgray;
    margin: 0px;
}
.ace_optionsMenuEntry button:hover{
    background: #f0f0f0;
}`}),ace.define("ace/ext/menu_tools/overlay_page",["require","exports","module","ace/lib/dom","ace/ext/menu_tools/settings_menu.css"],function(t,f,s){"use strict";var l=t("../../lib/dom"),b=t("./settings_menu.css");l.importCssString(b,"settings_menu.css",!1),s.exports.overlayPage=function(n,r,p){var e=document.createElement("div"),i=!1;function c(u){u.keyCode===27&&a()}function a(){!e||(document.removeEventListener("keydown",c),e.parentNode.removeChild(e),n&&n.focus(),e=null,p&&p())}function o(u){i=u,u&&(e.style.pointerEvents="none",r.style.pointerEvents="auto")}return e.style.cssText="margin: 0; padding: 0; position: fixed; top:0; bottom:0; left:0; right:0;z-index: 9990; "+(n?"background-color: rgba(0, 0, 0, 0.3);":""),e.addEventListener("click",function(u){i||a()}),document.addEventListener("keydown",c),r.addEventListener("click",function(u){u.stopPropagation()}),e.appendChild(r),document.body.appendChild(e),n&&n.blur(),{close:a,setIgnoreFocusOut:o}}}),ace.define("ace/ext/menu_tools/get_editor_keyboard_shortcuts",["require","exports","module","ace/lib/keys"],function(t,f,s){"use strict";var l=t("../../lib/keys");s.exports.getEditorKeybordShortcuts=function(b){var d=l.KEY_MODS,n=[],r={};return b.keyBinding.$handlers.forEach(function(p){var e=p.commandKeyBinding;for(var i in e){var c=i.replace(/(^|-)\w/g,function(o){return o.toUpperCase()}),a=e[i];Array.isArray(a)||(a=[a]),a.forEach(function(o){typeof o!="string"&&(o=o.name),r[o]?r[o].key+="|"+c:(r[o]={key:c,command:o},n.push(r[o]))})}}),n}}),ace.define("ace/ext/keybinding_menu",["require","exports","module","ace/editor","ace/ext/menu_tools/overlay_page","ace/ext/menu_tools/get_editor_keyboard_shortcuts"],function(t,f,s){"use strict";var l=t("../editor").Editor;function b(d){if(!document.getElementById("kbshortcutmenu")){var n=t("./menu_tools/overlay_page").overlayPage,r=t("./menu_tools/get_editor_keyboard_shortcuts").getEditorKeybordShortcuts,p=r(d),e=document.createElement("div"),i=p.reduce(function(c,a){return c+'<div class="ace_optionsMenuEntry"><span class="ace_optionsMenuCommand">'+a.command+'</span> : <span class="ace_optionsMenuKey">'+a.key+"</span></div>"},"");e.id="kbshortcutmenu",e.innerHTML="<h1>Keyboard Shortcuts</h1>"+i+"</div>",n(d,e)}}s.exports.init=function(d){l.prototype.showKeyboardShortcuts=function(){b(this)},d.commands.addCommands([{name:"showKeyboardShortcuts",bindKey:{win:"Ctrl-Alt-h",mac:"Command-Alt-h"},exec:function(n,r){n.showKeyboardShortcuts()}}])}}),function(){ace.require(["ace/ext/keybinding_menu"],function(t){g&&(g.exports=t)})}()}}]);
