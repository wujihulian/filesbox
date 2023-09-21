// packageB/components/music-play/music-play.js
let innerAudioContext = wx.createInnerAudioContext();
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        fileName:{
            type:String,
            value:''
        },
        src:{
            type:String,
            value:'',
            observer:function(o,n){
                if(o != n) this.initInnerAudio()
            }
        }
    },
    lifetimes:{
        attached(){
            this.innerAudioContext = wx.createInnerAudioContext() 
        },
        detached(){
            innerAudioContext.destroy();
        }
    },
    /**
     * 组件的初始数据
     */
    data: {
        isEnded:false,
        isPlay:false,
        currentTime:0,
        totalTime:0,
        audioCanPlay:false
    },

    /**
     * 组件的方法列表
     */
    methods: {
        // 初始化innerAudioContext对象
        initInnerAudio:function(){
            let { src } = this.properties
            innerAudioContext = wx.createInnerAudioContext(this);      
            let syt = wx.getSystemInfoSync().system.replace(/[^a-z]+/ig,'')
            if(syt=='iOS'){
              innerAudioContext.src = encodeURI(src);
            }else{
                wx.downloadFile({
                  url:src,
                  success:function(res){
                    innerAudioContext.src = res.tempFilePath
                  }
                })
            }
            innerAudioContext.autoplay = true; 
            wx.setInnerAudioOption({
                obeyMuteSwitch: false
            })  
            console.log(src)
            innerAudioContext.onCanplay(() => {
                console.log('可以播放了')
                this.setData({audioCanPlay:true})
                innerAudioContext.duration;
                let intervalID = setInterval(()=> {
                  if (innerAudioContext.duration !== 0) {
                    this.setData({
                        totalTime:innerAudioContext.duration == 'Infinity' ? 0 : parseInt(innerAudioContext.duration)
                    })
                    clearInterval(intervalID);
                  }
                }, 500);
            })
            innerAudioContext.onPlay(() => {
                this.setData({
                    isPlay:true
                })
            })
            innerAudioContext.onPause(()=>{
                this.setData({
                    isPlay:false
                })
            })

            innerAudioContext.onEnded(()=>{
                this.setData({
                    currentTime:this.data.totalTime,
                    isPlay:false,
                    isEnded:true
                })
            })

            innerAudioContext.onError((err)=>{
                console.error(err)
            })

            innerAudioContext.onTimeUpdate((res)=>{
                let systemInfo = wx.getSystemInfoSync(), pixelRatio = systemInfo.screenWidth/750,percent = innerAudioContext.currentTime/this.data.totalTime;
                let movableViewX = percent*480*pixelRatio;
                this.setData({
                    currentTime:innerAudioContext.currentTime,
                    percent,
                    movableViewX,
                })
            })
        },
        // 音乐播放器暂停/播放
        onAudioPause:function(){
            if(!this.data.audioCanPlay) return;
            let { isPlay, isEnded } = this.data;
            if(isPlay) innerAudioContext.pause();
            else innerAudioContext.play();
            if(isEnded){
                this.setData({
                    isEnded:false,
                    currentTime:0,
                    movableViewX:0
                })
            }
            this.setData({
                isPlay:!isPlay
            })
        },
        // 拖动进度条
        onMovableViewChange:function(e){
            if(!this.x) this.x = e.detail.x;
            if(Math.abs(e.detail.x-this.x)<=1){
                this.x = e.detail.x;
                return;
            }
            this.x = e.detail.x;
            let systemInfo = wx.getSystemInfoSync(), pixelRatio = systemInfo.screenWidth/750;
            let x_rpx = e.detail.x/pixelRatio;
            let percent = x_rpx/480;
            if(Math.abs(e.detail.x-this.x) != 15) innerAudioContext.seek(percent*this.data.totalTime);
        },
        // 快进15秒，快退15秒
        seekAudio:function(e){
            let { type } = e.currentTarget.dataset, { currentTime } = this.data;
            currentTime += type == 0 ? -15 : 15;
            innerAudioContext.seek(currentTime);
            this.setData({currentTime})
        }
    }
})
