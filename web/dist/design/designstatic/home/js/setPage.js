(function(e,f){for(var o=window.localStorage,a=e("head").find("style"),n=0,d=a.length;n<d;n++){var t=a.eq(n);t.text().indexOf(".module")>0&&t.text().indexOf(".head")>0&&t.text().indexOf(".foot")>0&&(o.comStyle=t.text())}o.comHead=e(".content > .head").html(),o.comFoot=e(".content > .foot").html()})(jQuery,window);