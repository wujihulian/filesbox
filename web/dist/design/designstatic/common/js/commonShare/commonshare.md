#图片分享方法
window.imgEditor.shareLayer(product,backgroundUrl,callback,res, sourceId)；
依赖layer，没有的话需要手动引入

## 参数
成员 | 说明 | 类型 | 默认值
:---:|:---:|:---: | :---:
product | 要分享的链接地址 | string | 默认'/design/designstatic/front/images/shareBackground.png',可传''
backgroundUrl | 默认的图片背景地址 | string | 默认window.location.href,可传''
callback | 回调函数 | function | 无默认值，可传''
res | 需要分享的内容 | object | 无默认值，必传
sourceId | 背景图片的sourceId | string |

### res参数构成说明
成员 | 说明 | 类型 | 默认值
:---:|:---:|:---: | :---:
preCloseCoverModalListener | 关闭分享弹窗前钩子函数 | function |
closeCoverModalListener | 关闭分享弹窗后钩子函数 | function |
courseId | 课程id | string |
courseWareId | 课件id | string |
shopId | 商品id | string |
vipId | VIP id | string |
schoolName | 网校名称 | string |
userInfo | 用户信息 | object |
cover | 课程封面地址 | string | 各模版默认的图片地址
courseName | 课程名 | string |
introduce | 课程介绍 | string |
iscourse | 是否课程 | Boolean |
config   | 社交分享配置 | object | 可选，没有配置降使用默认配置，目前只支持PC端

#### userInfo参数构成说明
成员 | 说明 | 类型 | 默认值
:---:|:---:|:---: | :---:
headPortrait | 课程介绍 | string |
realName | 用户名字 | string |
nickname | 用户昵称 | string |
loginName | 用户登录名 | string |

PC端分享，支持社交
var config = {
    url                 : product, // 网址，默认使用 window.location.href
    source              : res.source || W.location.origin || '', // 来源（QQ空间会用到）, 默认读取head标签：<meta name="site" content="https://www.xx.cn" />
    title               : res.title || res.courseName || '', // 标题，默认读取 document.title 或者 <meta name="title" content="https://www.xx.cn" />
    origin              : res.origin || W.location.origin || '', // 分享 @ 相关 twitter 账号
    description         : res.description || res.introduce || '', // 描述, 默认读取head标签：<meta name="description" content="https://www.xx.cn" />
    image               : res.image || W.location.origin + res.cover || '', // 图片, 默认取网页中第一个img标签
    sites               : ['weibo', 'qq','moments', 'dingding','wechat', 'douban', 'qzone', 'linkedin', 'facebook', 'twitter', 'google', 'picture', 'copylink'], // 启用的站点
    disabled            : ['google', 'facebook', 'twitter'], // 禁用的站点
};

sites配置支持
'weibo'| 'qq'|'moments'| 'dingding'|'wechat'| 'douban'| 'qzone'| 'linkedin'| 'facebook'| 'twitter'| 'google'| 'picture'| 'copylink'
微博   | QQ  |  朋友圈  |    钉钉    |  微信  |  豆瓣   |  QQ空间 |   领英    |  facebook |  twitter  |  google |  图片    |  复制链接
