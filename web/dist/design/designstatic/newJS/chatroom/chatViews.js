try{loadSource(["/design/designstatic/chatroom/js/jquery-1.11.0.min.js","/design/designstatic/newJS/common/js/loader.js?my_version="]),getValue("token")&&setCookie("token",getValue("token"))}catch(r){console.log("jquery:",r)}function loadSource(r){r.forEach(i=>{const t=`<script type="text/javascript" charset="utf-8" src="${i}"></script>`;document.write(t)})}function getFileType(r){const i=r.lastIndexOf(".");return r?r.substring(i+1):""}function setCookie(r,i){const t=r+"="+i+";path=/";document.cookie=t}function getValue(r){var i=i||window.location.href,t=i.split("?");if(t.length==1)return null;if(t.length==2){for(var n=t[1].split("&"),o={},e=0;e<n.length;e++){var s=n[e].split("=");o[s[0]]=s[1]}return o[r]?o[r]:!1}if(t.length==3){for(var n=t[1].split("&"),l=!1,o={},e=0,a=n.length;e<a;e++){var s=n[e].split("=");o[s[0]]=s[1],e+1==a&&s[0]==r&&(l=!0)}return o[r]?l?o[r]+"?"+t[2]:o[r]:!1}for(var n=t[1].split("&"),o={},e=0;e<n.length;e++){var s=n[e].split("=");o[s[0]]=s[1]}return o[r]?o[r]:!1}