(self.webpackChunkant_design_pro=self.webpackChunkant_design_pro||[]).push([[69190],{69190:function(a,g,r){a=r.nmd(a),ace.define("ace/ext/statusbar",["require","exports","module","ace/lib/dom","ace/lib/lang"],function(s,d,b){"use strict";var p=s("../lib/dom"),f=s("../lib/lang"),m=function(){function c(t,n){this.element=p.createElement("div"),this.element.className="ace_status-indicator",this.element.style.cssText="display: inline-block;",n.appendChild(this.element);var e=f.delayedCall(function(){this.updateStatus(t)}.bind(this)).schedule.bind(null,100);t.on("changeStatus",e),t.on("changeSelection",e),t.on("keyboardActivity",e)}return c.prototype.updateStatus=function(t){var n=[];function e(o,h){o&&n.push(o,h||"|")}e(t.keyBinding.getStatusText(t)),t.commands.recording&&e("REC");var i=t.selection,l=i.lead;if(!i.isEmpty()){var u=t.getSelectionRange();e("("+(u.end.row-u.start.row)+":"+(u.end.column-u.start.column)+")"," ")}e(l.row+":"+l.column," "),i.rangeCount&&e("["+i.rangeCount+"]"," "),n.pop(),this.element.textContent=n.join("")},c}();d.StatusBar=m}),function(){ace.require(["ace/ext/statusbar"],function(s){a&&(a.exports=s)})}()}}]);