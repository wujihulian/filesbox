(function(n){const s=window.localStorage||window.sessionStorage,c={getStore(e){const t=s.getItem(e)||"",r=t.substr(0,1),o=t.substr(-1);return r==="{"&&o==="}"||r==="["&&o==="]"?JSON.parse(t):t},setStore(e,t=""){switch(typeof t){case"object":{s.setItem(e,JSON.stringify(t));break}case"string":{s.setItem(e,t);break}default:break}},remove(e){s.removeItem(e)},clear(){s.clear()}};n.myStore=c})(window);