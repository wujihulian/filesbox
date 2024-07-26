package com.svnlan.utils;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能描述:
 *
 * @author:
 * @param:
 * @return:
 */
public class HttpUtil {

    private static Pattern domainPattern = Pattern.compile("http[s]?://([^#?/]*)[/?]+\\w+");
    private static final Header[] defaultHeaders = new Header[]{new BasicHeader("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")};
    /**
     * 封装HTTP POST方法
     *
     * @param
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String post(String url, Map<String, Object> paramMap) {
        HttpResponse response = null;
        HttpPost httpPost = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);
            List<NameValuePair> formparams = setHttpParams(paramMap);
            UrlEncodedFormEntity param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(param);
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP POST方法
     *
     * @param
     * @param （如JSON串）
     * @return
     */
    public static String post(String url, String data) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "text/json; charset=utf-8");
        HttpResponse response = null;
        try {
            httpPost.setEntity(new StringEntity(URLEncoder.encode(data, "UTF-8")));
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String httpEntityContent = getHttpEntityContent(response);
        httpPost.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP GET方法
     *
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String get(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();
        httpGet.setURI(URI.create(url));
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            LogUtil.info("url 请求失败" + url);
        }
        String httpEntityContent = getHttpEntityContent(response);
        httpGet.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP GET方法
     *
     * @param
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String get(String url, Map<String, Object> paramMap) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();
        List<NameValuePair> formparams = setHttpParams(paramMap);
        String param = URLEncodedUtils.format(formparams, "UTF-8");
        httpGet.setURI(URI.create(url + "?" + param));
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String httpEntityContent = getHttpEntityContent(response);
        httpGet.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP PUT方法
     *
     * @param
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String put(String url, Map<String, Object> paramMap) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        List<NameValuePair> formparams = setHttpParams(paramMap);
        UrlEncodedFormEntity param = null;
        HttpResponse response = null;
        try {
            param = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPut.setEntity(param);
            response = httpClient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String httpEntityContent = getHttpEntityContent(response);
        httpPut.abort();
        return httpEntityContent;
    }

    /**
     * 封装HTTP DELETE方法
     *
     * @param
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String delete(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete();
        httpDelete.setURI(URI.create(url));
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String httpEntityContent = getHttpEntityContent(response);
        httpDelete.abort();
        return httpEntityContent;
    }

    /**
     * 设置请求参数
     *
     * @param
     * @return
     */
    private static List<NameValuePair> setHttpParams(Map<String, Object> paramMap) {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        Set<Map.Entry<String, Object>> set = paramMap.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            formparams.add(new BasicNameValuePair(entry.getKey(), (String) entry.getValue()));
        }
        return formparams;
    }

    /**
     * 获得响应HTTP实体内容
     *
     * @param response
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private static String getHttpEntityContent(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            try {
                InputStream is = entity.getContent();
                BufferedReader br = null;
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = br.readLine();
                StringBuilder sb = new StringBuilder();
                while (line != null) {
                    sb.append(line + "\n");
                    line = br.readLine();
                }
                return sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    public static String postJson(String url, String data){
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
        HttpResponse response = null;
        try {
            httpPost.setEntity(new StringEntity(data, "UTF-8"));
            response = httpClient.execute(httpPost);
            String httpEntityContent = EntityUtils.toString(response.getEntity());
            httpPost.abort();
            return httpEntityContent;
        } catch (Exception e){
            LogUtil.error(e, "请求失败 url:" + url + ", data:" + data);
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }

    public static HttpResponse head(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpHead httpHead = new HttpHead();
        httpHead.setURI(URI.create(url));
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpHead);
        } catch (IOException e) {
            LogUtil.error(e, "HttpHead 失败");
        }
        httpHead.abort();
        return response;
    }

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getSchemeAndDomain(HttpServletRequest request) {
        return request.getScheme() + "://" + getServerName(request)
                + (request.getServerPort() == 443 || request.getServerPort() == 80
                ? "" : ":" + request.getServerPort());
    }

    public static String getServerName(HttpServletRequest request) {
        //先从attribute取
        if (request.getAttribute("targetServerNameForOverride") != null && request.getAttribute("targetServerNameForOverride") != "null" ){
            return request.getAttribute("targetServerNameForOverride").toString();
        }
        //再从param取
        String targetServerName = request.getParameter("targetServerNameForOverride");
        //param没有才用host的值
        String serverName = StringUtil.isEmpty(targetServerName) ? request.getServerName() : targetServerName;
        //写到attribute
        request.setAttribute("targetServerNameForOverride", serverName);
        return serverName;
    }
    public static String getUA() {
        HttpServletRequest request = getRequest();
        return getUA(request);
    }
    public static String getUA(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public static String getHeadersForLog(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();

        StringBuilder sb = new StringBuilder();
        while (headerNames.hasMoreElements()){
            String next = headerNames.nextElement();
            String header = request.getHeader(next);
            sb.append(next).append(": ").append(header).append(";\n");
        }
        return sb.toString();
    }

    /**
     * @description: 获取请求链接的根地址url
     * @param request
     * @return java.lang.String
     */
    public static String getRequestRootUrl(HttpServletRequest request) {
        if (null == request){
            request = getRequest();
        }

        String protocol = request.getScheme();
        int port = request.getServerPort();
        String portStr = "";
        if("https".equals(protocol)) {
            if (443 != port && 80 != port) {
                portStr = ":" + port;
            } else {
                portStr = "";
            }
        }else if("http".equals(protocol)) {
            if (80 != port) {
                portStr = ":" + port;
            } else {
                portStr = "";
            }
        }

            //http为443端口的，一般是https协议，nginx或Tomcat等配置不是很好导致取得的是http
        if(443 == port && "http".equals(protocol)) {
            protocol = "https";
        }

        //去掉https的443端口，预发、正式上有问题
        if(443 == port && "https".equals(protocol)) {
            portStr = "";
        }

        String requestRootUrl = protocol + "://" + getServerName(request) + portStr;
        return requestRootUrl;
    }
    /**
     * @description: 获取请求链接的根地址url
     * @param request
     * @return java.lang.String
     */
    public static String getReqRootUrl(HttpServletRequest request, List<String> list) {
        if (null == request){
            request = getRequest();
        }

        String serverName = getServerName(request);

        if (!CollectionUtils.isEmpty(list)){
            String u = null;
            for (String h : list){
                if (h.indexOf(serverName) >= 0){
                    u = h.substring(h.indexOf("//"), h.length());
                }
            }
            if (!ObjectUtils.isEmpty(u)) {
                return u;
            }
        }

        String protocol = request.getScheme();
        int port = request.getServerPort();
        String portStr = "";
        if("https".equals(protocol)) {
            if (443 != port && 80 != port) {
                portStr = ":" + port;
            } else {
                portStr = "";
            }
        }else if("http".equals(protocol)) {
            if (80 != port) {
                portStr = ":" + port;
            } else {
                portStr = "";
            }
        }

        //http为443端口的，一般是https协议，nginx或Tomcat等配置不是很好导致取得的是http
        if(443 == port && "http".equals(protocol)) {
            protocol = "https";
        }

        //去掉https的443端口，预发、正式上有问题
        if(443 == port && "https".equals(protocol)) {
            portStr = "";
        }

        String requestRootUrl =  "//" + serverName + portStr;
        return requestRootUrl;
    }

    /** 获取域名*/
    public static String getDomainFromUrl(String url) {
        Matcher matcher = domainPattern.matcher(url);
        if (matcher.find()){
            System.out.println(matcher.group());
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            return matcher.group(1);
        }
        return null;
    }

    public static String getRedirectLocation(String url) {
        HttpResponse httpResponse = headWithHeader(url, defaultHeaders, true);
        if (httpResponse == null){
            return "";
        }
        if (httpResponse.getStatusLine().getStatusCode() == 302 || httpResponse.getStatusLine().getStatusCode() == 301){
            Header location = httpResponse.getFirstHeader("location");
            return location.getValue();
        }
        return "";
    }
    public static HttpResponse headWithHeader(String url, Header[] headers, boolean noRedirect){
        HttpClient httpClient = noRedirect /*不跟随跳转*/ ? HttpClientBuilder.create().disableRedirectHandling().build() : HttpClients.createDefault();
        HttpHead httpHead = new HttpHead();
        httpHead.setHeaders(headers);
        httpHead.setURI(URI.create(url));
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpHead);
        } catch (IOException e) {
            LogUtil.error(e, "HttpHead 失败");
        }
        httpHead.abort();
        return response;
    }

    /**
     * @description: 获取Url链接的根地址url
     * @param urlString
     * @return java.lang.String
     */
    public static String geUrlRootUrl(String urlString) {
        String requestRootUrl = "";
        if (ObjectUtils.isEmpty(urlString)){
            return requestRootUrl;
        }
        URL request  = null;
        try {

            request = new  URL(urlString);
            String protocol = request.getProtocol();
            int port = request.getPort();
            String portStr = "";
            if("https".equals(protocol)) {
                if (443 != port && 80 != port && -1 != port) {
                    portStr = ":" + port;
                } else {
                    portStr = "";
                }
            }else if("http".equals(protocol)) {
                if (80 != port && -1 != port) {
                    portStr = ":" + port;
                } else {
                    portStr = "";
                }
            }

            //http为443端口的，一般是https协议，nginx或Tomcat等配置不是很好导致取得的是http
            if(443 == port && "http".equals(protocol)) {
                protocol = "https";
            }

            //去掉https的443端口，预发、正式上有问题
            if(443 == port && "https".equals(protocol)) {
                portStr = "";
            }

            requestRootUrl = protocol + "://" + request.getHost() + portStr;
        }catch (Exception e){
            LogUtil.error(e,"geUrlRootUrl error");
        }
        return requestRootUrl;
    }
}
