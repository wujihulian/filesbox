import {
    CryptoJS
} from "./Crypto";
import { STATIC_DOMAIN} from './config';
import { emoji } from './emoji';
const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return `${[year, month, day].map(formatNumber).join('/')} ${[hour, minute, second].map(formatNumber).join(':')}`
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : `0${n}`
}
const encodeReqParams = params => {
  const keys = Object.keys(params);
  let result = '';
  keys.map((item, index) => {
      index ? result += `&${item}=${params[item]}` : result += `?${item}=${params[item]}`;
  });
  return result;
};
const showToast = title => {
  if (title) {
      if (title.indexOf('安全校验失败') > -1) {
          wx.showToast({
              icon: 'none',
              title: title.split('安全校验失败')[0] + '安全校验失败，请稍后再试'
          });
      } else {
          wx.showToast({
              icon: 'none',
              title
          });
      }
  }
};
const showErrMsg = err => {
  console.log(err)
  if (err) {
      if (!['CARD_NOT_CREATED', '签名验证不存在'].includes(err.message)) {
          wx.showToast({
              icon: 'none',
              title: err.message
          });
      } else if (err.message.indexOf('安全校验失败') > -1) {
          const title = err.message.split('安全校验失败')[0] + '安全校验失败，请稍后再试';
          wx.showToast({
              icon: 'none',
              title
          });
      }
  }
};
const timeFormat = function (fmt = 'yyyy-MM-dd hh:mm:ss', date = new Date()) {
  if (typeof (date) === 'number') date = new Date(date);
  var o = {
      "M+": date.getMonth() + 1, //月份 
      "d+": date.getDate(), //日 
      "h+": date.getHours(), //小时 
      "m+": date.getMinutes(), //分 
      "s+": date.getSeconds(), //秒 
      "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
      "S": date.getMilliseconds() //毫秒 
  };
  if (/(y+)/.test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (var k in o) {
      if (new RegExp("(" + k + ")").test(fmt)) {
          fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      }
  }
  return fmt;
}
// 输入框根据label字段，双向绑定当前页面字段
const twoWayBinding = function (e) {
  let {
      label
  } = e.currentTarget.dataset, value = typeof (e.detail) == 'object' ? e.detail.value : e.detail;
  if (e.currentTarget.dataset.hasOwnProperty('debug')) {
      console.log(e)
  }
  this.setData({
      [label]: value
  })
}

// AES加密
const encrypt = str => {
    const key = CryptoJS.enc.Utf8.parse("njsdearr8h239ay3");
    const srcs = CryptoJS.enc.Utf8.parse(str);
    const encrypted = CryptoJS.AES.encrypt(srcs, key, {
        mode: CryptoJS.mode.ECB,
        padding: CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}
//获取随机数
const randomNumber = function generateMixed(n){
    const charts = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z']
    var res = '';
    for(var i = 0; i <n; i++){
      var id = Math.ceil(Math.random()*61);
      res += charts[id];
    }
    return res;
}
const getDateStr = function (today, addDayCount) {
    let date;
    if (today) {
      date = new Date(today);
    } else {
      date = new Date();
    }
    date.setDate(date.getDate() + addDayCount); //获取AddDayCount天后的日期 
    let y = date.getFullYear();
    let m = date.getMonth() + 1; //获取当前月份的日期 
    let d = date.getDate();
    if (m < 10) {
      m = '0' + m;
    };
    if (d < 10) {
      d = '0' + d;
    };
    // console.log(y + "-" + m + "-" + d)
    return y + "-" + m + "-" + d;
}
//获取时间戳
const getTimetamp = function(num) {
  const timestamp = Date.parse(new Date())/1000;  
  return timestamp+24*60*60*num
}
const symbols = ['B', 'KB', 'MB', 'GB'];
//转化size大小
const renderSize = function(fileSize) {
  let bytes = fileSize;
  let exp = Math.floor(Math.log(fileSize) / Math.log(2));
  if (exp < 1) {
    exp = 0;
  }
  const i = Math.floor(exp / 10);
  bytes /= Math.pow(2, 10 * i);
  if (bytes.toString().length > bytes.toFixed(2).toString().length) {
    bytes = bytes.toFixed(2);
  }
  return bytes + symbols[i];
}
// 通过时间戳获取正常的时间显示
const timeFormatter = (timeStamp, format, style = '/') => {
  const week = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  const _data = String(timeStamp).length === 13 ? timeStamp : timeStamp * 1000;

  const time = new Date(_data);
  const Y = time.getFullYear();
  const Mon = add_zero(time.getMonth() + 1);
  const Day = add_zero(time.getDate());
  const H = add_zero(time.getHours());

  let Min = time.getMinutes();
  if (Min < 10) Min = '0' + Min;
  let S = time.getSeconds();
  if (S < 10) S = '0' + S;
  const W = week[time.getDay()];

  if (format === "Y") return `${Y}${style}${Mon}${style}${Day}`;
  else if (format === "H") return `${H}:${Min}:${S}`;
  else if (format === "M/D") return `${Mon}${style}${Day}`;
  else if (format === "M/D/H") return `${Mon}${style}${Day} ${H}:${Min}`;
  else if (format === "Y W") return `${Y}${style}${Mon}${style}${Day} ${W}`;
  else if (format === 'YmdHM') return `${Y}${style}${Mon}${style}${Day} ${H}:${Min}`;
  else return `${Y}${style}${Mon}${style}${Day} ${H}:${Min}:${S}`;
};
const add_zero = function (val) {
  return val >= 10 ? val : '0' + val
}
//获取距离今天时间
const timeFromNow = timeStamp => {
  const ONE_SENCOND = 1000;
  const ONE_MINUTE = 60 * ONE_SENCOND;
  const ONE_HOUR = 60 * ONE_MINUTE;
  const ONE_DAY = 24 * ONE_HOUR;
  const ONE_WEEK = 7 * ONE_DAY;
  const ONE_MONTH = 30 * ONE_DAY;
  const ONE_YEAR = 365 * ONE_DAY;
  const curr = new Date().getTime();
  const diff = curr - timeStamp;
  if (diff < ONE_MINUTE) return Math.floor(diff / ONE_SENCOND)>0?Math.floor(diff / ONE_SENCOND)+ '秒钟前':1 + '秒钟前';
  else if (diff < ONE_HOUR) return Math.floor(diff / ONE_MINUTE) + '分钟前';
  else if (diff < ONE_DAY) return Math.floor(diff / ONE_HOUR) + '小时前';
  else if (diff < ONE_WEEK) return Math.floor(diff / ONE_DAY) + '天前';
  else if (diff < ONE_MONTH) return Math.floor(diff / ONE_WEEK) + '星期前';
  else if (diff < ONE_YEAR) return Math.floor(diff / ONE_MONTH) + '个月前';
  else return timeFormatter(timeStamp, "Y");
};
//获取用户随机id
const genUuid = () => {
  const hexDigits = "0123456789abcdef";
  const s = [];
  for (let i = 0; i < 36; i++) {
      s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4";
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
  // s[8] = s[13] = s[18] = s[23] = "-";
  s[8] = s[13] = s[18] = s[23] = "";
  return s.join("");
};
// 判断一个对象是否为空
const isEmptyObject = function (object) {
  for (const i in object) {
      return false;
  }
  return true;
}
//如果用户拒绝了保存到相册的权限，以后再点击任何保存图片到本地的按钮将没有反应，应在保存失败后回调设置
const openSaveImageSetting = () => {
  wx.getSetting({
      success: res => {
          let authSetting = res.authSetting
          if (!authSetting['scope.writePhotosAlbum']) {
              wx.showModal({
                  title: "提示",
                  content: '您之前拒绝了【保存到相册】的权限，开启后可保存照片到相册,是否去开启?',
                  success: result => {
                      if (result.confirm) {
                          wx.openSetting({
                              withSubscriptions: true,
                          })
                      }
                  }
              })
          }
      }
  })
}
//裁剪图片
const clipImage = (src, imgW, imgH, cb) => {  
  // ‘shareCanvas’为前面创建的canvas标签的canvas-id属性值
  let ctx = wx.createCanvasContext('shareCanvas');  
  let canvasW = 640, canvasH = imgH;
  if (imgW / imgH > 5 / 4) { // 长宽比大于5:4
    canvasW = imgH * 5 / 4;
  }
  // // 将图片绘制到画布
  // ctx.drawImage(src, (imgW - canvasW) / 2, 0, canvasW, canvasH, 0, 0, canvasW, canvasH) 
  ctx.setFillStyle('#ffffff')
  ctx.fillRect(0,0,640,512);
  ctx.drawImage(src,0,64,640,384);
  // draw()必须要用到，并且需要在绘制成功后导出图片
  ctx.draw(false, () => {
    setTimeout(() => {
      //  导出图片
      wx.canvasToTempFilePath({
        // width: canvasW,
        // height: canvasH,
        // destWidth: canvasW,
        // destHeight: canvasH,
        canvasId: 'shareCanvas',
        fileType: 'jpg',
        success: (res) => {
          // res.tempFilePath为导出的图片路径
          typeof cb == 'function' && cb(res.tempFilePath);
        }
      })
    }, 1000);
  })
}
// emoji转换图片
const parseEmoji = function(msg){
  let newMsg = msg.replace(/\[.*?\]/gi,(match, capture) => {
    if(msg === match){
      match = `<img style="width:60px;height:60px;vertical-align:middle;display: inline;" src="${STATIC_DOMAIN}/appstatic/images/emoji/new_${emoji.indexOf(match)}.png" />`
    }else{
      match = `<img style="width:18px;height:18px;vertical-align:middle;display: inline;margin-bottom:2px;" src="${STATIC_DOMAIN}/appstatic/images/emoji/new_${emoji.indexOf(match)}.png" />`
    }
    return match;
  })
  return `<div style="vertical-align:middle">${newMsg}</div>`;
}
// 判断消息是否只有表情
const booleanIsOnlyEmoji = function(msg) {
  let newMsg = msg.replace(/\[.*?\]/gi,(match, capture) => {
    if(match === msg) return ''
  })
  return newMsg === '';
}
module.exports = {
  formatTime,encodeReqParams,showToast,showErrMsg,timeFormat,twoWayBinding,encrypt,getDateStr,renderSize,timeFromNow,genUuid,randomNumber,isEmptyObject,getTimetamp,openSaveImageSetting,clipImage,booleanIsOnlyEmoji,parseEmoji
}
