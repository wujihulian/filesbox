const app = getApp();

// 获取当前存的语言选择结果，如果没有默认用中文
const languageVersion = function () {
  return wx.getStorageSync('lang') || 'zh';
}

//返回翻译数据
function translate() {
  return require('../language/_' + languageVersion() + '.js').languageMap;
}

//切换语言方法
const changeLanguage= function (langType) {
  if (langType== 1) {
    wx.setStorageSync('lang', 'en')
  } else {
    wx.setStorageSync('lang', 'zh')
  }
}

//抛出方法
module.exports = {
  languageVersion: languageVersion,
  changeLanguage: changeLanguage,
  _lang: translate,
}