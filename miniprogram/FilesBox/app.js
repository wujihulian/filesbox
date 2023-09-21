// app.js
import config from './utils/config';
import languageUtils from './utils/languageUtils';
App({
  onLaunch() {
    this.initLanguage();
    this.setEnv();
    config.STATIC_DOMAIN = 'https://test-static.1x.cn';
    config.WEBSOCKET_DOMAIN = 'wss://webchattest.1x.cn';
    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
    // wx.getSystemInfo({
    //   success: function (res) {
    //     wx.setStorageSync('windowHeight', res.windowHeight)
    //   }
    // })
  },
  globalData: {
    systemInfo: wx.getSystemInfoSync(),
  },
  initLanguage() {
    this.globalData.lang = languageUtils._lang()
  },
  // 环境对接
  setEnv: function () {
    const accountInfo = wx.getAccountInfoSync();
    // miniProgram.envVersion的合法值：develop开发版、trial体验版、release正式版
    this.globalData.appId = accountInfo.miniProgram.appId;
    // 环境判断 true 开发环境 false 体验版、正式版
    let version = accountInfo.miniProgram.envVersion;
    let extConfig = wx.getExtConfigSync ? wx.getExtConfigSync() : {};
    this.globalData.ext = JSON.stringify(extConfig)
    if (extConfig.hasOwnProperty('environment')) {
        if (extConfig.environment == 'pro') {
            version = 'release';
        } else {
            version = 'develop';
        }
    }
    // // version='release'
    // this.globalData.isDevelop = version != 'release';
    // // 正式版本
    // if (version === 'release') {
    //     config.STATIC_DOMAIN = 'https://static.wxbig.cn';
    //     config.DOMAIN = 'https://dev.filesbox.cn';
    //     config.WEBSOCKET_DOMAIN = 'wss://webchat.xx.cn';
    //     this.setOptions();
    // }
    // //体验版
    // // else if( version === 'trial'){
    // //   config.STATIC_DOMAIN = 'https://pre-static.1x.cn';
    // //   config.DOMAIN = 'https://pre.xx.cn';
    // //   config.WEBSOCKET_DOMAIN = 'wss://webchatpre.1x.cn';
    // // }
    // // 开发版本
    // else {
    //     config.STATIC_DOMAIN = 'https://test-static.1x.cn';
    //     config.DOMAIN = 'https://dev.filesbox.cn';
    //     config.WEBSOCKET_DOMAIN = 'wss://webchattest.1x.cn';
    // }
  },
})
