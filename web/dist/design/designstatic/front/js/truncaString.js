(function(e){e.fn.truncaString=function(i){var h={length:100,isHide:!1,hideClue:!1,moreText:"[Expansion]",hideText:"[Shrink]",moreTitle:"",hideTitle:"",boundary:/^\s+$/},i=e.extend(h,i);return this.each(function(a){var r=e(this).html().length;if(r>h.length)for(var l=new RegExp(h.boundary),d='<a class="more_'+a+'" href="#" title="'+h.moreTitle+'">'+h.moreText+"</a> ",n='<a class="hide_'+a+'" href="#" title="'+h.hideTitle+'">'+h.hideText+"</a>",t=h.length;t<r;t++){var o=e(this).html().substr(0,t),c=e(this).html().substr(t),f=o.slice(-1);if(l.test(f)){var _=h.hideClue?'<span class="hideClue_'+a+'">...</span>':'<span class="hideClue_'+a+'"></span>',p='<span class="hiddenText_'+a+'">'+c+"</span>",v=e(this).html().substr(0,t-1);e(this).html(v).append(_+p+d+n);var s=".swiper-slide-"+h.id+" ";e(s+"a.more_"+a).bind("click",function(){return e(s+"span.hiddenText_"+a).show(),e(s+"span.hideClue_"+a).hide(),e(s+"a.more_"+a).hide(),e(s+"a.hide_"+a).show(),!1}),e(s+"a.hide_"+a).bind("click",function(){return e(s+"span.hiddenText_"+a).hide(),e(s+"span.hideClue_"+a).show(),e(s+"a.more_"+a).show(),e(s+"a.hide_"+a).hide(),!1}),h.isHide?e(s+"a.hide_"+a).click():e(s+"a.more_"+a).click(),t=r}}})}})(jQuery);