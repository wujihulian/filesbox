(function(o){class r{constructor(){this.state={courseData:{}}}setState(t,e){for(let s in t)this.state[s]=t[s];e&&e()}getStore(){return this.state}clone(t,e){return Object.assign(t,e)}setCourse(t,e){const{courseData:s={}}=this.state,i=this.clone(s,t);this.setState({courseData:i},e)}}const n=new r;o.allStore=n})(window);