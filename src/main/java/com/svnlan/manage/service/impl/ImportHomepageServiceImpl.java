package com.svnlan.manage.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.net.InternetDomainName;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.domain.InfoSource;
import com.svnlan.manage.domain.LogImportArticle;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.manage.service.ImportHomepageService;
import com.svnlan.manage.vo.ImportArticleVO;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @Author:
 * @Description:
 * @Date:
 */
@Service
public class ImportHomepageServiceImpl implements ImportHomepageService {
    @Value("${cdn.domain}")
    private String cdnDomain;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private LoginUserUtil loginUserUtil;
    @Resource
    StorageService storageService;
    //文章限制20M
    private static final long maxContentLength = 1204 * 1024 * 20;
    private static Pattern scriptPattern = Pattern.compile("<script (.*?)>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        private static final String baseSavePath = "/common/dldsrc/";

    /**
     * @Description: 导入资讯
     * @params: [importHomepageDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    @Override
    public LogImportArticle importHomepage(CommonInfoDto infoDto, LoginUser loginUser) {

        //锁
        String lockKey = GlobalConfig.IMPORT_ARTICLE_LOCK + loginUser.getUserID();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 3, TimeUnit.SECONDS);
        if (!Boolean.TRUE.equals(lock)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.codeErrorFreq.getCode());
        }
        String url = infoDto.getUrl();
        ImportArticleVO importArticleVO = null;
        try {
            importArticleVO = importArticle(url);
        }catch (Exception e){
            LogUtil.error(e, "导入失败");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        return getHtml(importArticleVO, loginUser);
    }

    public String getTopDomain(String url) {
        String domain = "";
        try {
            String host = new URL(url).getHost();
            InternetDomainName domainName = InternetDomainName.from(host);
            domain = domainName.topPrivateDomain().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return domain;
    }

    private ImportArticleVO importArticle(String url) {
        if (StringUtil.isEmpty(url)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        url = url.trim();
        if (url.length() > 512) {
            url = url.substring(0, 512);
        }
        //没以 http打头的, 补上
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        //获取域名
        String domain = getTopDomain(url);

        //单独处理下公司的独立域名
        switch (domain) {
            //腾讯
            case "qq.com":
                return getHtmlByTencent(url);
            //光明网
            case "gmw.cn":
                return getHtmlBygmw(url);
            //百度
            case "baidu.com":
                return getHtmlByBaidu(url);
            //中国网
            case "china.com.cn":
                return getHtmlByChina(url);
            //人民日报app
            case "wap.peopleapp.com":
                return getHtmlByPeopleapp(url);
            //中国青年网
            case "youth.cn":
                return getHtmlByYouth(url);
            //凤凰网
            case "ifeng.com":
                return getHtmlByIfeng(url);
            //今日头条
            case "toutiao.com":
            case "toutiaocdn.com":
                return getHtmlByToutiao(url);
            //新浪网
            case "sina.com.cn":
            case "sina.cn":
                return getHtmlBySina(url);
            //知乎
            case "zhihu.com":
                return getHtmlByZhihu(url);
            //人民日报PC
            case "people.com.cn":
                return getHtmlByPeople(url);
            //新华网
            case "news.cn":
            case "xinhuanet.com":
                return getHtmlByNews(url);
            //搜狐网
            case "sohu.com":
                return getHtmlBySohu(url);
            //鲸媒体
            case "jingmeiti.com":
                return getHtmlByJingmeiti(url);
            //多知网
            case "duozhi.com":
                return getHtmlByDuozhi(url);
            //网易
            case "163.com":
                return getHtmlByWangyi(url);
            //uc
            case "uc.cn":
                return getHtmlByUC(url);
            //学习网
            case "xuexi.cn":
                return getHtmlByXuexi(url);
            //新华社APP
            case "zhongguowangshi.com":
                return getHtmlByZGWS(url);
            //一点资讯
            case "yidianzixun.com":
                return getHtmlByYidianzixun(url);
            //华为
            case "huawei.com":
                return getHtmlByHuawei(url);
            //简书
            case "jianshu.com":
                return getHtmlByJianshu(url);
            //和讯
            case "hexun.com":
                return getHtmlByHexun(url);
            //央广网
            case "cnr.cn":
                return getHtmlByCnr(url);
            //中国经济新闻网
            case "cet.com.cn":
                return getHtmlByCet(url);
            //人民日报经济新闻网
            case "ceweekly.cn":
                return getHtmlByCeweekly(url);
            //中国消费网
            case "ccn.com.cn":
                return getHtmlByCcn(url);
            //澎湃网
            case "thepaper.cn":
                return getHtmlByThepaper(url);
            //中国日报
            case "chinadaily.com.cn":
                return getHtmlByChinadaily(url);
            //中国经济网
            case "ce.cn":
                return getHtmlByCe(url);
            //金融界
            case "jrj.com.cn":
                return getHtmlByJrj(url);
            //金融界
            case "stockstar.com":
                return getHtmlByStockStar(url);
            //中国金融网
            case "financeun.com":
                return getHtmlByFinanceun(url);
            //中华网
            case "china.com":
                return getHtmlByChina1(url);
            //环球网
            case "huanqiu.com":
                return getHtmlByHuanqiu(url);
            //36氪
            case "36kr.com":
                return getHtmlBy36kr(url);
            //脉脉
            case "maimai.cn":
                return getHtmlByMaimai(url);
            //太平洋教育网
            case "ffhhvv.com":
                return getHtmlByFfhhvv(url);
            //中国教育在线
            case "eol.cn":
                return getHtmlByEol(url);
            //中国企业新闻网
            case "gdcenn.cn":
                return getHtmlByGdcenn(url);
            //国际在线
            case "cri.cn":
                return getHtmlByCri(url);
            //21世纪教育
            case "21cnjy.com":
                return getHtmlBy21cnjy(url);
            //21世纪教育网
            case "21jiaoyu.cn":
                return getHtmlBy21jiaoyu(url);
            //中国教育培训网
            case "yangchengmed.com":
                return getHtmlByYangchengmed(url);
            //教育视窗网
            case "jysc.org":
                return getHtmlByJysc(url);
            //中国国际教育网
            case "ieduchina.com":
                return getHtmlByIeduchina(url);
            //中国教育论坛
            case "chinaedubbs.com":
                return getHtmlByChinaedubbs(url);
            //中国教育网
            case "chinaedunet.com":
                return getHtmlByChinaedunet(url);
            //中华教育网
            case "edu-gov.cn":
                return getHtmlByEdugov(url);
            //太平洋电脑网
            case "pconline.com.cn":
                return getHtmlByPconline(url);
            //中关村在线
            case "zol.com.cn":
                return getHtmlByZol(url);
            //21财经
            case "21jingji.com":
                return getHtmlBy21Jingji(url);
            //天极网
            case "yesky.com":
                return getHtmlByYesky(url);
            //比特网
            case "chinabyte.com":
                return getHtmlByChinaByte(url);
            //软件资讯网
            case "cnsoftnews.com":
                return getHtmlByCnsoftnews(url);
            //求是网
            case "qstheory.cn":
                return getHtmlByQstheory(url);
            //站长之家
            case "chinaz.com":
                return getHtmlByChinaz(url);
            //泡泡
            case "pcpop.com":
                return getHtmlByPop(url);
            //cctv
            case "cctv-gy.cn":
                return getHtmlByCCtv(url);
                //tom
            case "tom.com":
                return getHtmlByTom(url);
                //东财
                case "eastmoney.com":
                return getHtmlByEastMoney(url);
            default:
                //不识别的常识匹配去抓取
                try {
                    return getHtmlByOther(url);
                } catch (Exception e) {
                    LogUtil.error(e,">>>>>>>>>>>>>>>>>>>>>>>>>>暂不支持该资讯采集：" + url);
                    throw new SvnlanRuntimeException(CodeMessageEnum.DOMAIN_CANNOT_RECOGNIZE.getCode());
                }
        }
    }

    private ImportArticleVO getHtmlByEastMoney(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("ContentBody");
        return new ImportArticleVO(contentElement, null, titleElement, null, "东方财富网", url);
    }

    private ImportArticleVO getHtmlByTom(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("news_box_text").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, null, "tom", url);
    }

    private ImportArticleVO getHtmlByCCtv(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("ad_468_2");
        return new ImportArticleVO(contentElement, null, titleElement, null, "泡泡网", url);
    }

    private ImportArticleVO getHtmlByPop(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("newsd_main_txt_p").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "cctv", url);
    }

    private ImportArticleVO getHtmlBy21jiaoyu(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("articleBody");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "21世纪教育网", url);
    }

    private ImportArticleVO getHtmlByChinaz(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[property=og:description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("article-content");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "站长之家", url);
    }

    private ImportArticleVO getHtmlByQstheory(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("content").get(0);
        Elements img = contentElement.getElementsByTag("img");
        if (!img.isEmpty()) {
            for (Element element : img) {
                String src = element.attr("src");
                if (!src.startsWith("http")) {
                    int i = url.lastIndexOf("/");
                    src = url.substring(0, i + 1) + src;
                    element.attr("src", src);
                }
            }
        }
        return new ImportArticleVO(contentElement, null, titleElement, null, "求是网", url);
    }

    private ImportArticleVO getHtmlByOther(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements contentElements = document.select("div[class~=artic]");
        Element contentElement = new Element("content");
        if (CollectionUtils.isEmpty(contentElements)) {
            contentElements = document.select("div[class~=content]");
        }
        if (CollectionUtils.isEmpty(contentElements)) {
            contentElements = document.select("div[class~=cont]");
        }
        if (!CollectionUtils.isEmpty(contentElements)) {
            String html = contentElements.html();
            contentElement = contentElements.get(0).html(html);
        }
        if (contentElement == null) {
            contentElement = document.getElementById("article");
        }
        Elements metas = document.getElementsByTag("meta");
        Element titleElement = new Element("content");
        Element descriptionElement = new Element("content");
        if (!metas.isEmpty()) {
            //如果标题为空，则从内容截取
            Elements titleElements = document.getElementsByTag("title");
            if (CollectionUtils.isEmpty(titleElements)) {
                String title = contentElement.text().substring(0, 32);
                titleElement.text(title);
            } else {
                titleElement = titleElements.get(0);
            }
            Elements descriptionElements = metas.select("meta[name=description]");
            if (CollectionUtils.isEmpty(descriptionElements)) {
                descriptionElement.text(titleElement.text());
            } else {
                descriptionElement = descriptionElements.get(0);
            }
        }
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "其他网站", url);

    }

    private ImportArticleVO getHtmlByCnsoftnews(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("content_info").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "软件资讯网", url);
    }

    private ImportArticleVO getHtmlByChinaByte(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "比特网", url);
    }

    private ImportArticleVO getHtmlByYesky(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article_infor").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "天极网", url);
    }

    private ImportArticleVO getHtmlByZol(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("article-content");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中关村在线", url);
    }

    private ImportArticleVO getHtmlBy21Jingji(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = null;
        try {
            contentElement = document.getElementsByClass("detailCont").get(0);
        } catch (Exception e) {
            contentElement = document.getElementsByClass("txtContent").get(0);
            contentElement.html(contentElement.html().replaceAll("data-original", "src"));
        }
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "21财经", url);

    }

    private ImportArticleVO getHtmlByPconline(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("JCmtImg");
        Elements img = contentElement.getElementsByTag("img");
        if (!img.isEmpty()) {
            for (Element element : img) {
                String src = element.attr("src");
                if (src.lastIndexOf("articleImageLoading.gif") > 0) {
                    element.attr("src", element.attr("#src"));
                    element.removeAttr("#src");
                }
            }
        }
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "太平洋电脑网", url);
    }

    private ImportArticleVO getHtmlByEdugov(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("entry-content").get(0);
        Elements img = contentElement.getElementsByTag("img");
        if (!img.isEmpty()) {
            for (Element element : img) {
                element.removeAttr("srcset");
            }
        }
        return new ImportArticleVO(contentElement, null, titleElement, null, "中华教育网", url);
    }

    private ImportArticleVO getHtmlByChinaedunet(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Elements contentElements = document.getElementsByClass("bjh-p");
        Element contentElement = new Element("content");
        for (Element element : contentElements) {
            contentElement.appendElement(element.html());
        }
        return new ImportArticleVO(contentElement, null, titleElement, null, "中国教育网", url);
    }

    private ImportArticleVO getHtmlByChinaedubbs(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("article_content");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国教育论坛", url);
    }

    private ImportArticleVO getHtmlByIeduchina(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("con").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国国际教育网", url);
    }

    private ImportArticleVO getHtmlByJysc(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article_content").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "教育视窗网", url);
    }

    private ImportArticleVO getHtmlByYangchengmed(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("content").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国教育培训网", url);
    }

    private ImportArticleVO getHtmlBy21cnjy(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("article_content");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "21世纪教育", url);
    }

    private ImportArticleVO getHtmlByCri(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("abody");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "国际在线", url);
    }

    private ImportArticleVO getHtmlByGdcenn(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("articlecon");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国企业新闻网", url);
    }

    private ImportArticleVO getHtmlByEol(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国教育在线", url);
    }

    private ImportArticleVO getHtmlByFfhhvv(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("con").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "太平洋教育网", url);
    }

    //TODO
    private ImportArticleVO getHtmlByMaimai(String url) {
        Document document = JsoupHtml.getDocument(url);
        String content = "";
        String title = "";
        Element script = document.body().select("script").first();
        String text = script.data();
        text = text.substring(text.indexOf("{") + 1, text.lastIndexOf("}"));
        text = StringUtil.unicodetoString(text);
        text = "{" + text + "}";
        Map<String, Object> map = JsonUtils.jsonToMap(text);
        String data = map.get("data").toString();
        if (!StringUtils.isEmpty(data)) {

            String feed = HMapper.parseData(data, "feed", String.class);
            if (!StringUtils.isEmpty(feed)) {
                Map result = HMapper.parseData(feed, "main", Map.class);
                if (!CollectionUtils.isEmpty(result)) {
                    title = result.get("title").toString();
                    content = result.get("html").toString();
                }
            }
        }
        Element contentElement = new Element("content");
        Element titleElement = new Element("content");
        contentElement.html(content);
        titleElement.text(title);
        return new ImportArticleVO(contentElement, null, titleElement, null, "脉脉", url);
    }

    private ImportArticleVO getHtmlBy36kr(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article-content").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "36氪", url);
    }

    private ImportArticleVO getHtmlByHuanqiu(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByTag("article").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "环球网", url);
    }

    private ImportArticleVO getHtmlByChina1(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("js_article_content");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中华网", url);
    }

    private ImportArticleVO getHtmlByFinanceun(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("txt").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国金融网", url);
    }

    private ImportArticleVO getHtmlByStockStar(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article_content").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "证券之星", url);
    }

    private ImportArticleVO getHtmlByJrj(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[property=og:description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("texttit_m1").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "金融界", url);
    }

    private ImportArticleVO getHtmlByCe(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=DEscription]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("articleText");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国经济网", url);
    }

    private ImportArticleVO getHtmlByChinadaily(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("Content");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国日报", url);
    }

    private ImportArticleVO getHtmlByThepaper(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=Description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("news_txt").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "澎湃网", url);
    }

    private ImportArticleVO getHtmlByCcn(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.select("div[class~=^detail_conBox]").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, null, "中国消费网", url);
    }

    private ImportArticleVO getHtmlByCeweekly(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.select("div[class~=^article-content]").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "人民日报经济网", url);
    }

    private ImportArticleVO getHtmlByCnr(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article-body").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, null, "央广网", url);
    }

    private ImportArticleVO getHtmlByCet(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("article_content").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国经济新闻网", url);
    }

    private ImportArticleVO getHtmlByJianshu(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByTag("article").get(0);
        Elements img = contentElement.getElementsByTag("img");
        if (!img.isEmpty()) {
            for (Element element : img) {
                String path = element.attr("data-original-src");
                element.attr("src", "http:" + path);
                element.removeAttr("data-original-src");
            }
        }
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "简书", url);

    }

    //TODO
    private ImportArticleVO getHtmlByHexun(String url) {
        try {
            Document document = JsoupHtml.getDocument(url);
            Elements metas = document.getElementsByTag("meta");
            Element descriptionElement = metas.select("meta[name=description]").get(0);
            Element titleElement = document.getElementsByTag("title").get(0);
            Element contentElement = document.getElementsByTag("article").get(0);
            return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "和讯", url);
        } catch (Exception e) {
            Document document = JsoupHtml.getDocument(url);
            Element titleElement = document.getElementsByTag("title").get(0);
            Element contentElement = document.getElementsByClass("art_contextBox").get(0);
            return new ImportArticleVO(contentElement, null, titleElement, null, "和讯", url);
        }
    }


    private ImportArticleVO getHtmlByHuawei(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("hwb-news__main").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "华为", url);
    }

    private ImportArticleVO getHtmlByYidianzixun(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("content-bd").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "一点资讯", url);

    }

    private ImportArticleVO getHtmlByBaidu(String url) {
        if (url.contains("baijiahao.baidu.com")) {
            return getHtmlByBjh(url);
        } else if (url.contains("mbd.baidu.com")) {
            return getHtmlBymbd(url);
        }
        throw new SvnlanRuntimeException(CodeMessageEnum.domainSupportError.getCode());
    }

    /**
     * @param url
     * @Description:腾讯文章采集
     * @Return: com.svnlan.common.vo.ImportArticleVO
     **/
    private ImportArticleVO getHtmlByTencent(String url) {
        //微信公众号
        if (url.contains("weixin.qq.com")) {
            return getHtmlByWeixin(url);
        } else if (url.contains("om.qq.com")) {
            return getHtmlByomqq(url);
        } else if (url.contains("qq.com")) {
            return getHtmlByQQ(url);
        }
        throw new SvnlanRuntimeException(CodeMessageEnum.domainSupportError.getCode());
    }

    private ImportArticleVO getHtmlByZGWS(String url) {
        int i = url.lastIndexOf("?");
        if (i > 0) {
            url = url.substring(0, i);
        }
        String id = url.substring(url.lastIndexOf("/") + 1);
        String domain = HttpUtil.getDomainFromUrl(url);
        String result = HttpUtil.get("https://" + domain + "/v600/news/" + id + ".js");
        result = result.substring(result.indexOf("{"));
        Map<String, Object> map = JsonUtils.jsonToMap(result);
        String content = map.get("content").toString();
        String topic = map.get("topic").toString();
        Element contentElement = new Element("content").html(content);
        Element titleElement = new Element("content").text(topic);
        return new ImportArticleVO(contentElement, null, titleElement, null, "新华-中国网事", url);
    }

    private ImportArticleVO getHtmlByZhihu(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("Post-RichTextContainer").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, null, "知乎", url);
    }

    //TODO
    private ImportArticleVO getHtmlByPeopleapp(String url) {
        String s = HttpUtil.get(url);
        System.out.println(s);
//        return new ImportArticleVO(contentElement, null, titleElement, null, "人民日报", url);
        return null;
    }

    private ImportArticleVO getHtmlByChina(String url) {
        try {
            return getHtmlByChinaPc(url);
        } catch (Exception e) {
            return getHtmlByChinaApp(url);
        }
    }

    private ImportArticleVO getHtmlByChinaApp(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("content");
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "中国网", url);

    }

    private ImportArticleVO getHtmlByChinaPc(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementById("articleBody");
        if (contentElement == null) {
            contentElement = document.getElementById("chan_newsDetail");
        }
        return new ImportArticleVO(contentElement, null, titleElement, null, "中国网", url);
    }

    private ImportArticleVO getHtmlByQQ(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("content-article").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "腾讯网", url);
    }

    private ImportArticleVO getHtmlByIfeng(String url) {
        try {
            return getHtmlByIfengpc(url);
        } catch (Exception e) {
            return getHtmlByIfengApp(url);
        }
    }

    private ImportArticleVO getHtmlByIfengpc(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("text-3w2e3DBc").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "凤凰网", url);
    }

    private ImportArticleVO getHtmlByIfengApp(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.select("div[class~=^detailBox]").get(0);
        Elements img = contentElement.getElementsByTag("img");
        if (!img.isEmpty()) {
            for (Element e : img) {
                String path = e.attr("data-lazyload");
                if (!StringUtils.isEmpty(path)) {
                    e.attr("src", path);
                }
            }
        }
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "凤凰网APP", url);
    }

    private ImportArticleVO getHtmlByYouth(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element authorElement = metas.select("meta[name=author]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("page_text").get(0);
        return new ImportArticleVO(contentElement, authorElement, titleElement, null, "中国年轻网", url);
    }

    private ImportArticleVO getHtmlByomqq(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element authorElement = metas.select("meta[itemprop=author]").get(0);
        Element titleElement = metas.select("meta[itemprop=name]").get(0);
        Element contentElement = document.getElementsByTag("section").get(0);
        return new ImportArticleVO(contentElement, authorElement, titleElement, null, "腾讯开放平台", url);
    }

    private ImportArticleVO getHtmlByBjh(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Elements authorElements = document.getElementsByClass("author-name");
        Element authorElement = CollectionUtils.isEmpty(authorElements) ? null : authorElements.get(0);
        Elements contentElements = document.getElementsByClass("article-content");
        Element contentElement = CollectionUtils.isEmpty(contentElements) ? null : contentElements.get(0);
        return new ImportArticleVO(contentElement, authorElement, titleElement, null, "百家号", url);
    }

    private ImportArticleVO getHtmlBymbd(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element authorElement = document.getElementsByClass("index-module_authorName_7y5nA").get(0);
        Element contentElement = document.getElementsByClass("index-module_articleWrap_2Zphx").get(0);
        return new ImportArticleVO(contentElement, authorElement, titleElement, null, "百度", url);
    }

    private ImportArticleVO getHtmlBygmw(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element titleElement = document.getElementsByTag("title").get(0);
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element authorElement = metas.select("meta[name=author]").get(0);
        Element contentElement = document.getElementById("articleBox");
        return new ImportArticleVO(contentElement, authorElement, titleElement, descriptionElement, "光明网", url);
    }

    private ImportArticleVO getHtmlByWeixin(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[property=og:description]").get(0);
        Element titleElement = metas.select("meta[property=og:title]").get(0);
        Element authorElement = metas.select("meta[property=og:article:author]").get(0);
        Element contentElement = document.getElementById("js_content");
        //不取文件(表单只取标题), 就返回了
        boolean withoutAttachment = Boolean.TRUE.equals(HttpUtil.getRequest().getAttribute("importWithoutAttachment"));
        if (withoutAttachment){
            return new ImportArticleVO(contentElement, authorElement, titleElement, descriptionElement, "微信", url);
        }

        Elements img = contentElement.getElementsByTag("img");
        if (!img.isEmpty()) {
            for (Element e : img) {
                String dataSrc = e.attr("data-src");
                e.attr("src", dataSrc);
                e.removeAttr("data-src");
            }
        }
        StringBuilder videoIdBuilder = new StringBuilder();
        videoIdBuilder.append("wx");
        String firstPath = storageService.getDefaultStorageDevicePath();
        //视频
        Elements videoElements = contentElement.getElementsByClass("video_iframe");
        String savePath = firstPath + baseSavePath + FileUtil.getDatePath() + Long.toHexString(System.currentTimeMillis()) + "_" + loginUserUtil.getLoginUser().getUserID() + "/";
        for (Element element : videoElements){
            String videoId = element.attr("data-mpvid");
            String dataCover = element.attr("data-cover");
            if (!StringUtil.isEmpty(dataCover)){
                try {
                    dataCover = URLDecoder.decode(dataCover, "UTF-8");
                    dataCover = DownloadUtil.getWxImage(dataCover, savePath, cdnDomain, maxContentLength);
                } catch (UnsupportedEncodingException e) {
                    LogUtil.error(e, "dataCover: " + dataCover);
                }
            }
            if (videoIdBuilder.length() + videoId.length() > 500){
                LogUtil.error("微信videoId拼装长度, 超过500, " + url);
                continue;
            }
            videoIdBuilder.append(videoId).append("\n");

            element.empty().tagName("video").attr("class", "wjImpVideo")
                    .attr("data-src","")
                    .attr("controls", "controls")
                    .attr("src", "/api/common/tVideo?vid=" + videoId)
                    .attr("poster", dataCover)
            ;
        }
        Elements audioElements = contentElement.getElementsByClass("audio_iframe");
        for (Element element : audioElements){
            String audioUrl = "https://res.wx.qq.com/voice/getvoice?mediaid=" + element.attr("voice_encode_fileid");
            element.attr("src", audioUrl).attr("controls", "controls")
            .tagName("audio");
        }
        return new ImportArticleVO(contentElement, authorElement, titleElement, descriptionElement, "微信", url);
    }


    private ImportArticleVO getHtmlByToutiao(String url) {
        //类短链形式的, 会302, 取302之后的地址
        boolean isRedirect = false;
        url = url.replaceAll(" ", "%20");
        if (url.contains("toutiao.com/is/")){
            url = HttpUtil.getRedirectLocation(url);
            isRedirect = true;
        }

        if (url.indexOf("?") > 0) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.endsWith("/")) {
            url = url.substring(0, url.lastIndexOf("/"));
        }
        url = url.substring(url.lastIndexOf("/") + 1);
        if (url.startsWith("a")) {
            url = url.replace("a", "i");
        } else if (!url.startsWith("i")){
            url = "i" + url;
        }

        url = "https://m.toutiao.com/" + url + "/info/";

        String data = HttpUtil.get(url);
        Map map = HMapper.parseData(data, "data", Map.class);
        LogUtil.info("getHtmlByToutiao url="+ url + "，map=" + JsonUtils.beanToJson(map));
        Object titleObj = map.get("title");
        String title = "";
        String content = "";
        if (titleObj != null){
            title = titleObj.toString();
            content = map.get("content").toString();
        } else {
            //例子 https://www.toutiao.com/w/i1716677036982286/?timestamp=1637216807&app=toutiao_web&use_new_style=1&log_from=ae624074cdc9_1637216809446
            // thread -> tread_base -> title, content, large_image_list
            Object threadObj = map.get("thread");
            if (threadObj != null){
                JSONObject threadBase = ((JSONObject) threadObj).getJSONObject("thread_base");
                title = threadBase.getString("title");
                content = threadBase.getString("content");
                JSONArray imageList = threadBase.getJSONArray("large_image_list");
                for (Object o : imageList) {
                    String imgUrl = ((JSONObject) o).getString("url");
                    content += "<img src=\" " + imgUrl + " \"/>";
                }
            }
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("js");
        try {
            content = (String) engine.eval("unescape('" + content + "')");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        System.out.println(content);
        Element titleElement = new Element("title");
        titleElement.text(title);
        Element contentElement = new Element("content");
        contentElement.html(content);
        return new ImportArticleVO(contentElement, null, titleElement, null, "今日头条", url);

    }

    private ImportArticleVO getHtmlByXuexi(String url) {
        String substring = url.substring(url.indexOf("?") + 1);
        Map<String, String> map = Splitter.on("&").withKeyValueSeparator("=").split(substring);
        String itemId = map.get("item_id");
        //解析js
        String lastUrl = "https://article.xuexi.cn/data/app/" + itemId + ".js?callback=callback";
        String s = HttpUtil.get(lastUrl);
        //返回格式"callback(***)"，括号里的json就是目标值
        String ss = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));
        Map<String, Object> data = JsonUtils.jsonToMap(ss);
        String title = data.get("title").toString();
        String content = data.get("content").toString();
        String source = data.get("show_source").toString();
        String imgStr = data.get("image").toString();
        Element authorElement = new Element("author");
        authorElement.attr("content", source);
        Element titleElement = new Element("title");
        titleElement.text(title);
        Element contentElement = new Element("content");
        List<Map> mapList = JsonUtils.jsonToList(imgStr, Map.class);
        //如果仅一张图片则是网站自己的底部图片
        if (!CollectionUtils.isEmpty(mapList) && mapList.size() > 1) {
            //最后一张网站图片不处理
            for (int i = 0, size = mapList.size(); i < size - 1; i++) {
                Map imgMap = mapList.get(i);
                String imgUrl = imgMap.get("url").toString();
                String img = "<img " + "src=" + "\"" + imgUrl + "\"";
                String old = "<!--{img:" + i + "}-->";
                content = content.replace(old, img);
            }
        }
        contentElement.html(content);
        return new ImportArticleVO(contentElement, authorElement, titleElement, null, "学习强国", url);
    }

    private ImportArticleVO getHtmlByUC(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element authorElement = document.getElementsByTag("span").select("span[class=xiss-name]").get(0);
        authorElement.attr("content", authorElement.text());
        Element contentElement = document.getElementById("contentShow");
        return new ImportArticleVO(contentElement, authorElement, titleElement, descriptionElement, "UC", url);
    }

    private ImportArticleVO getHtmlBySina(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element authorElement = metas.select("meta[property=article:author]").get(0);
        Element contentElement = document.getElementById("article");
        return new ImportArticleVO(contentElement, authorElement, titleElement, descriptionElement, "新浪网", url);
    }

    private ImportArticleVO getHtmlByPeople(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        titleElement.attr("content", titleElement.text());
        Element authorElement = metas.select("meta[name=author]").get(0);
        Element contentElement = document.getElementsByClass("layout rm_txt cf").get(0);
        return new ImportArticleVO(contentElement, authorElement, titleElement, descriptionElement, "人民网", url);

    }

    private ImportArticleVO getHtmlByNews(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.select("title").get(0);
        Elements PElements = document.getElementsByTag("p");
        Element contentElement = document.createElement("content");
        for (Element element : PElements) {
            contentElement.append(element.html());
        }
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "新华网", url);
    }

    private ImportArticleVO getHtmlBySohu(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement;
        try {
            contentElement = document.getElementsByClass("at-cnt-main").get(0);
        } catch (Exception e) {
            contentElement = document.getElementsByTag("article").get(0);
        }
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "搜狐网", url);
    }

    private ImportArticleVO getHtmlByJingmeiti(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element author = document.getElementsByClass("postauthor").get(0);
        Element contentElement = document.getElementsByClass("post-content").get(0);
        return new ImportArticleVO(contentElement, author, titleElement, descriptionElement, "鲸媒体", url);

    }

    private ImportArticleVO getHtmlByDuozhi(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByClass("subject-title").get(0);
        Element authorElement = document.getElementsByClass("subject-meta").get(0);
        String text = authorElement.text();
        int i = text.indexOf("作者");
        String author = text.substring(i + 3);
        authorElement.attr("content", author);
        Element contentElement = document.getElementsByClass("subject-content").get(0);
        return new ImportArticleVO(contentElement, authorElement, titleElement, descriptionElement, "多知网", url);

    }

    private ImportArticleVO getHtmlByWangyi(String url) {
        try {
            return getHtmlByWangyiPc(url);
        } catch (Exception e) {
            return getHtmlByWangyiApp(url);
        }
    }

    private ImportArticleVO getHtmlByWangyiPc(String url) {
        Document document = JsoupHtml.getDocument(url);
        Elements metas = document.getElementsByTag("meta");
        Element descriptionElement = metas.select("meta[name=description]").get(0);
        Element titleElement = document.getElementsByTag("title").get(0);
        Element contentElement = document.getElementsByClass("post_body").get(0);
        return new ImportArticleVO(contentElement, null, titleElement, descriptionElement, "网易", url);

    }

    private ImportArticleVO getHtmlByWangyiApp(String url) {
        Document document = JsoupHtml.getDocument(url);
        Element titleElement = document.getElementsByClass("header-title").get(0);
        Element contentElement = document.getElementsByTag("article").get(0);
        Elements figure = contentElement.getElementsByTag("figure");
        if (!figure.isEmpty()) {
            for (Element e : figure) {
                String src = e.attr("data-echo");
                if (!StringUtils.isEmpty(src)) {
                    try {
                        src = URLDecoder.decode(src, "utf-8");
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        unsupportedEncodingException.printStackTrace();
                    }
                    String paramStr = src.substring(src.indexOf("?") + 1);
                    if (!StringUtils.isEmpty(paramStr)) {
                        Map<String, String> paramMap = Splitter.on("&").withKeyValueSeparator("=").split(paramStr);
                        if (!CollectionUtils.isEmpty(paramMap)) {
                            Element element = e.child(0);
                            element.attr("src", paramMap.get("url"));
                        }
                    }
                }
            }
        }
        return new ImportArticleVO(contentElement, null, titleElement, null, "网易App", url);
    }

    /**
     * 功能描述:获取并添加html
     * [vo, url, name, loginUser]
     *
     * @return: com.svnlan.common.domain.LogImportArticle
     **/
    private LogImportArticle getHtml(ImportArticleVO importArticleVO, LoginUser loginUser) {
        Element authorElement = importArticleVO.getAuthorElement();
        Element titleElement = importArticleVO.getTitleElement();
        Element descriptionElement = importArticleVO.getDescriptionElement();
        Element contentElement = importArticleVO.getContentElement();
        if (ObjectUtils.isEmpty(contentElement)){
            throw new SvnlanRuntimeException(CodeMessageEnum.ARTICLE_CANNOT_RECOGNIZE.getCode());
        }
        String html = "";
        String name = importArticleVO.getName();
        String url = importArticleVO.getUrl();
        String title = ObjectUtils.isEmpty(titleElement) ? "" : StringUtils.isEmpty(titleElement.text()) ? titleElement.attr("content") : titleElement.text();
        String description = ObjectUtils.isEmpty(descriptionElement) ? "" : descriptionElement.attr("content");
        if (StringUtils.isEmpty(description)) {
            description = title;
        }
        String firstPath = storageService.getDefaultStorageDevicePath();
        String author = ObjectUtils.isEmpty(authorElement) ? "" : StringUtils.isEmpty(authorElement.text()) ? authorElement.attr("content") : authorElement.text();
        Elements imagesElements = contentElement.getElementsByTag("img");
        String savePath = firstPath + baseSavePath + FileUtil.getDatePath() + Long.toHexString(System.currentTimeMillis()) + "_" + loginUser.getUserID() + "/";
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        LogImportArticle logImportArticle = new LogImportArticle();
        for (Element image : imagesElements) {
            String absSrc = image.attr("src");
            if (!StringUtils.isEmpty(absSrc)) {
                absSrc = UrlDUtil.GetRealUrl(absSrc);
                 String finalSrc = DownloadUtil.getWxImage(absSrc, savePath, cdnDomain, maxContentLength);
                image.attr("src", finalSrc);
            }
        }

        Long loginUserId = loginUser.getUserID();
        //处理样式中的图片下载
        handleElementStyleImg(contentElement, "background: url", loginUserId);
        //
        handleElementStyleImg(contentElement, "background:url", loginUserId);
        //
        handleElementStyleImg(contentElement, "background-image: url", loginUserId);
        //
        handleElementStyleImg(contentElement, "background-image:url", loginUserId);

        //
        html = scriptPattern.matcher(contentElement.html()).replaceAll("");
        InfoSource infoSource = new InfoSource();
        infoSource.setUrl(url);
        infoSource.setName(name);
        infoSource.setAuthor(author);
        FileUtil.putFileContent(savePath + "index.html", html);
        logImportArticle.setSavePath(savePath + "index.html");
        logImportArticle.setUrl(url);
        logImportArticle.setUserId(loginUser.getUserID());
        logImportArticle.setInfoSource(JsonUtils.beanToJson(infoSource));
        logImportArticle.setIntroduce(StringUtil.subStringLimit(description, 1024));
        logImportArticle.setTitle(StringUtil.subStringLimit(title, 256));
        LogImportArticle logImportArticleVO = new LogImportArticle();
        logImportArticleVO.setInfoSourceVO(infoSource);
        logImportArticleVO.setTitle(logImportArticle.getTitle());
        logImportArticleVO.setIntroduce(logImportArticle.getIntroduce());
        logImportArticleVO.setContent(html);
        ObjUtil.initializefield(logImportArticle);
        return logImportArticleVO;
    }

    /**
     * @description: 处理样式中的图片下载
     * @param contentElement
     * @param styleProperty
     * @param userId
     * @return void
     * @author zmc
     */
    private void handleElementStyleImg(Element contentElement, String styleProperty, Long userId) {
        try {
            Elements sections = contentElement.select("[style*='" + styleProperty + "']");
            if(!CollectionUtils.isEmpty(sections)) {
                for (Element e : sections) {
                    String styleStr = e.attr("style");
                    e.removeAttr("style");
                    e.attr("style", handleStyleImg(styleStr,  styleProperty + "(\"", "\")", userId));
                }
            }
        } catch (Exception e) {
            LogUtil.error("处理样式中的图片失败：styleProperty为：" + styleProperty + "，内容：" + contentElement.html());
        }
    }

    private String handleStyleImg(String str, String startStr, String endStr, Long userId)
    {
        if (!str.contains(startStr) && !str.contains(endStr)) { return str; }
        String s1 = str.substring(0, str.indexOf(startStr));

        String s2 = str.substring(str.indexOf(startStr), str.length());
        String imgUrl = s2.substring(s2.indexOf(startStr) + startStr.length(), s2.indexOf(endStr));
        String s3 = s2.substring(s2.indexOf(endStr));
        String firstPath = storageService.getDefaultStorageDevicePath();
        String savePath = firstPath + baseSavePath + FileUtil.getDatePath() + Long.toHexString(System.currentTimeMillis()) + "_" + userId + "/";
        //
        String finalSrc = DownloadUtil.getWxImage(imgUrl, savePath, cdnDomain, maxContentLength);
        return s1 + startStr + finalSrc + s3;
    }
}
