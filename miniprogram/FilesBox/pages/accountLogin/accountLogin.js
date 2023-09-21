import {
    req
} from "../../utils/service";
import {
    URL
} from "../../utils/config";
import {
    showErrMsg,
    showToast,
    twoWayBinding,
    encrypt
} from "../../utils/util";
import {
    imageUrl
} from '../../utils/image';
const {
    globalData
} = getApp();

Page({
    data: {
        loginType: '账号登录',
        eyeShow: false,
        accountText: '',
        passwordText: '',
        readAgreement: false,
        accountFocus: true,
        passwordFocus: false,
        showPassword:false,
        showSite:false,
        imageUrl,
        lang:globalData.lang,
    },

    onLoad: function (e) {
        let title = this.data.lang['用户登录'];
        wx.setNavigationBarTitle({
            title
        });
        this.setData({
            showSite:wx.getStorageSync('DOMAIN')?false:true
        })
    },
    // 监听：切换站点
    switchSite: function () {
        wx.removeStorageSync('DOMAIN')
        this.setData({
            showSite:!this.data.showSite
        })
    },
    showPassword: function () {
        this.setData({
            showPassword: !this.data.showPassword
        })
    },
    // 信息同步
    synchronizeInfo: function (fieldName, value) {
        const {
            login
        } = this.data;
        const one = login.filter(i => i.fieldName === fieldName)[0];
        one.value = value;
        this.setData({
            login
        });
    },
    getPhone(e) {
        getPhoneNumber(e).then(data => {

            if (data.code != 200) return;

            this.setData({
                account: data.data
            })
        })
    },
    inputLoginInfo: function (e) {
        let {
            name
        } = e.currentTarget.dataset, {
            value
        } = e.detail;
        this.setData({
            [name]: value
        })
    },
    switchLoginType: function () {
        this.setData({
            isPhoneLogin: !this.data.isPhoneLogin
        })
    },
    // 监听：登录
    onLogin: function (login) {
        const body = {
            isGraphicCode: 0,
            name: login.account,
            password: encrypt(login.password)
        };
        req("POST",'/api/disk/login', {}, body).then(res => {
            // console.log(res)
            wx.setStorageSync('token', res.token)
            wx.setStorageSync('userID', res.userID)
            wx.setStorageSync('needRefresh', true)
            this.getUserInfo()
        }, err => {
            if(err.code=='admin.setting.passwordErrorLock'){
                wx.showModal({
                    title:'温馨提示',
                    content: '很抱歉，您多次密码输入错误，账号已被锁定，请10分钟之后再试。',
                    showCancel: false,
                })
                return
            }
            showErrMsg(err);
        });
    },
    getUserInfo(){
        req("GET",'/api/disk/options', {}, {}).then(res => {
            // console.log(res)
            wx.setStorageSync('config',res.config)
            wx.setStorageSync('role',res.role)
            wx.setStorageSync('markConfig', res.markConfig)
            wx.setStorageSync('options', res)
            // if(res.lang=='zh_CN'){
            //     wx.setStorageSync('lang','zh')
            // }else{
            //     wx.setStorageSync('lang','en')
            // } 
            wx.setStorageSync('targetSpace', res.targetSpace)
            wx.setStorageSync('accountLogin', res.user)
            setTimeout(function(){
                wx.switchTab({
                    url: '/pages/personal/personal'
                });
                // wx.navigateTo({
                //     url: '/pages/personal/personal'
                // });
            },1000)  
        }, err => {
            showErrMsg(err);
        });
    },
    // 表单提交
    formSubmit(e) {
        let {
            account,
            password,
            showSite
        } = this.data;
        let DOMAIN = showSite?this.data.DOMAIN:wx.getStorageSync('DOMAIN')
        const login = {
            account,
            password,
        } 
        if(DOMAIN){
            DOMAIN = /https/.test(DOMAIN) ? DOMAIN :'https://'+DOMAIN
            wx.setStorageSync('DOMAIN',DOMAIN)
        }
        if (!login.account) {
            wx.showToast({
                icon: 'none',
                title: `请输入账号`
            });
        }else if (!DOMAIN) {
            wx.showModal({
                content: '请输入服务器IP地址或者域名（如demo.filesbox.cn)',
                showCancel: false,
            })
        } else if (!login.password) {
            wx.showToast({
                icon: 'none',
                title: `请输入密码`
            });
        } else if (!this.data.readAgreement) {
            wx.showToast({
                icon: 'none',
                title: `请先同意服务条款和用户隐私协议`
            });
            wx.hideKeyboard({})
        } else {
            this.onLogin(login)
        }
    },
    // 表单重置
    formReset(e) {
        this.setData({
            passwordText: '',
            accountText: ''
        })
    },
    onEyeShow: function () {
        const {
            eyeShow
        } = this.data;
        this.setData({
            eyeShow: !eyeShow
        });
    },
    onChangeAccount: function (e) {
        this.setData({
            account: e.detail
        })
    },
    onChange: function (e) {
        this.setData({
            password: e.detail
        })
    },
    // 组件回调：密码显示
    onEyeClick: function (isHideText) {
        const {
            login
        } = this.data;
        const one = login.filter(i => i.fieldName === 'password')[0];
        one.password = isHideText;
        this.setData({
            login
        });
    },
    //更改已读状态
    switchCheckState: function (e) {
        this.setData({
            readAgreement: !this.data.readAgreement
        })
        wx.setStorageSync('readAgreement', this.data.readAgreement ? 1 : 0)
    },
    // 去服务协议
    goServiceAgreement: function (e) {
        wx.navigateTo({
            url: `/pages/link/link?url=https://test.1x.cn/pages/schoolRegister/fbxAppPolicy.html`,
        })
    },
    // 去隐私协议
    goPrivacyAgreement: function (e) {
        wx.navigateTo({
            url: `/pages/link/link?url=https://test.1x.cn/pages/schoolRegister/fbxAppProtocol.html`,
        })
    },
    twoWayBinding
})