package com.svnlan.home.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkcontact_1_0.models.GetUserResponseBody;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponseBody;
import com.aliyun.dingtalkstorage_1_0.models.*;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.domain.Dept;
import com.svnlan.jwt.third.dingding.DingTalkResult;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.taobao.api.ApiException;
import io.jsonwebtoken.lang.Assert;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/20 14:00
 */
@Component
public class DingUtil {


    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    public static com.aliyun.dingtalkstorage_1_0.Client createClient1() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkstorage_1_0.Client(config);
    }
    public static com.aliyun.dingtalkconv_file_1_0.Client createClient2() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkconv_file_1_0.Client(config);
    }

    public static com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkcontact_1_0.Client(config);
    }
    public static com.aliyun.dingtalkstorage_1_0.Client createClient3() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkstorage_1_0.Client(config);
    }

    /** 刷新钉钉企业内部应用的accessToken*/
    public static GetAccessTokenResponseBody refreshDingToken(String appKey, String appSecret) {

        try {
            com.aliyun.dingtalkoauth2_1_0.Client client = DingUtil.createClient();
            com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                    .setAppKey(appKey)
                    .setAppSecret(appSecret);
            GetAccessTokenResponse getAccessTokenResponse = client.getAccessToken(getAccessTokenRequest);
            GetAccessTokenResponseBody body = getAccessTokenResponse.getBody();

            return body;
        } catch (TeaException err) {
            LogUtil.error(err, "refreshDingToken2 TeaException error");
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            LogUtil.error(_err, "refreshDingToken2 Exception error");
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return null;
    }

    /** 刷新钉钉企业内部应用的accessToken*/
    public static String refreshDingToken(StringRedisTemplate stringRedisTemplate, String appKey, String appSecret) {
        String dingKey = GlobalConfig.ding_accessToken_key + appKey;

        String accessToken = stringRedisTemplate.opsForValue().get(dingKey);
        if (!ObjectUtils.isEmpty(accessToken)) {
            return accessToken;
        }
        long expires = 7200;
        GetAccessTokenResponseBody body = DingUtil.refreshDingToken(appKey,appSecret);
        if (ObjectUtils.isEmpty(body)) {
            int i = 3;
            while (i > 0){
                LogUtil.info("refreshDingToken 尝试重新获取 i=" + i);
                body = DingUtil.refreshDingToken(appKey,appSecret);
                if (!ObjectUtils.isEmpty(body)){
                    break;
                }
                i --;
            }
        }
        if (!ObjectUtils.isEmpty(body)) {
            expires = body.expireIn;
            accessToken = body.getAccessToken();

            stringRedisTemplate.opsForValue().set(dingKey, accessToken, expires, TimeUnit.SECONDS);
        }
        return accessToken;
    }

    /**
     * 获取文件夹、文件信息
     * @param stringRedisTemplate
     * @param appKey
     * @param appSecret
     * @param unionId
     * @param spaceId
     * @param dentryId
     * @https://open.dingtalk.com/document/orgapp/obtain-file-or-folder-information 文档
     */
    public static GetDentryResponseBody getDingSourceInfo(StringRedisTemplate stringRedisTemplate, String appKey, String appSecret, String unionId
            , String spaceId, String dentryId) {

        try {
            com.aliyun.dingtalkstorage_1_0.Client client = DingUtil.createClient1();
            com.aliyun.dingtalkstorage_1_0.models.GetDentryHeaders getDentryHeaders = new com.aliyun.dingtalkstorage_1_0.models.GetDentryHeaders();
            getDentryHeaders.xAcsDingtalkAccessToken = refreshDingToken(stringRedisTemplate,appKey, appSecret);
            com.aliyun.dingtalkstorage_1_0.models.GetDentryRequest.GetDentryRequestOption option = new com.aliyun.dingtalkstorage_1_0.models.GetDentryRequest.GetDentryRequestOption()
                    .setAppIdsForAppProperties(java.util.Arrays.asList(
                            unionId
                    ))
                    .setWithThumbnail(false);
            com.aliyun.dingtalkstorage_1_0.models.GetDentryRequest getDentryRequest = new com.aliyun.dingtalkstorage_1_0.models.GetDentryRequest()
                    .setUnionId(unionId)
                    .setOption(option);
            GetDentryResponse getDentryResponse = client.getDentryWithOptions(spaceId, dentryId, getDentryRequest, getDentryHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            return getDentryResponse.getBody();
        } catch (TeaException err) {
            LogUtil.error(err, "getDingSourceInfo TeaException error");
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            LogUtil.error(_err, "getDingSourceInfo Exception error");
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return null;
    }
    /**
       * @Description: 获取空间信息
       * @params:  [stringRedisTemplate, appKey, appSecret, unionId, spaceId, dentryId]
       * @Return:  com.aliyun.dingtalkstorage_1_0.models.GetDentryResponseBody
       * @Author:  sulijuan
       * @Date:  2023/10/20 16:56
       * @https://open.dingtalk.com/document/orgapp/get-space-information
       */
    public static GetSpaceResponseBody getDingSpaceInfo(StringRedisTemplate stringRedisTemplate, String appKey, String appSecret, String unionId, String spaceId) {

            try {
                com.aliyun.dingtalkstorage_1_0.Client client = DingUtil.createClient1();
                com.aliyun.dingtalkstorage_1_0.models.GetSpaceHeaders getSpaceHeaders = new com.aliyun.dingtalkstorage_1_0.models.GetSpaceHeaders();
                getSpaceHeaders.xAcsDingtalkAccessToken = refreshDingToken(stringRedisTemplate,appKey, appSecret);
                com.aliyun.dingtalkstorage_1_0.models.GetSpaceRequest getSpaceRequest = new com.aliyun.dingtalkstorage_1_0.models.GetSpaceRequest()
                        .setUnionId(unionId);
                GetSpaceResponse getSpaceResponse =client.getSpaceWithOptions(spaceId, getSpaceRequest, getSpaceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
                return getSpaceResponse.getBody();
            } catch (TeaException err) {
                LogUtil.error(err, "getDingSpaceInfo TeaException error");
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                }
            } catch (Exception _err) {
                LogUtil.error(_err, "getDingSpaceInfo Exception error");
                TeaException err = new TeaException(_err.getMessage(), _err);
                if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                    // err 中含有 code 和 message 属性，可帮助开发定位问题
                }

            }
            return null;
    }
    /**
       * @Description: 获取群存储空间信息
       * @params:  [stringRedisTemplate, appKey, appSecret, unionId, spaceId, dentryId]
       * @Return:  com.aliyun.dingtalkstorage_1_0.models.GetDentryResponseBody
       * @Author:  sulijuan
       * @Date:  2023/10/20 16:50
       * @https://open.dingtalk.com/document/orgapp/obtain-group-storage-space-information 文档
       */
    public static GetDentryResponseBody getDingGroupStorageInfo(StringRedisTemplate stringRedisTemplate, String appKey, String appSecret, String unionId, String openConversationId) {

        try {
            com.aliyun.dingtalkconv_file_1_0.Client client = DingUtil.createClient2();
            com.aliyun.dingtalkconv_file_1_0.models.GetSpaceHeaders getSpaceHeaders = new com.aliyun.dingtalkconv_file_1_0.models.GetSpaceHeaders();
            getSpaceHeaders.xAcsDingtalkAccessToken = refreshDingToken(stringRedisTemplate,appKey, appSecret);
            com.aliyun.dingtalkconv_file_1_0.models.GetSpaceRequest getSpaceRequest = new com.aliyun.dingtalkconv_file_1_0.models.GetSpaceRequest()
                    .setUnionId(unionId)
                    .setOpenConversationId(openConversationId);
            client.getSpaceWithOptions(getSpaceRequest, getSpaceHeaders, new com.aliyun.teautil.models.RuntimeOptions());
        } catch (TeaException err) {
            LogUtil.error(err, "getDingGroupStorageInfo TeaException error");
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            LogUtil.error(_err, "getDingGroupStorageInfo Exception error");
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        }
        return null;
    }

    /**
       * @Description: 获取文件下载信息
       * @params:  [stringRedisTemplate, appKey, appSecret, unionId, spaceId, dentryId]
       * @Return:  com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoResponseBody
       * @Author:  sulijuan
       * @Date:  2023/10/20 16:49 
       * @https://open.dingtalk.com/document/orgapp/obtains-the-download-information-about-a-file 文档
       */
    public static GetFileDownloadInfoResponseBody getDingDownloadInfos(StringRedisTemplate stringRedisTemplate, String appKey, String appSecret, String unionId, String spaceId
            , String dentryId) {

        try {
            com.aliyun.dingtalkstorage_1_0.Client client = DingUtil.createClient1();
            com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoHeaders getFileDownloadInfoHeaders = new com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoHeaders();
            getFileDownloadInfoHeaders.xAcsDingtalkAccessToken = refreshDingToken(stringRedisTemplate,appKey, appSecret);
            com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoRequest.GetFileDownloadInfoRequestOption option = new com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoRequest.GetFileDownloadInfoRequestOption()
                    .setVersion(1L)
                    .setPreferIntranet(false);
            com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoRequest getFileDownloadInfoRequest = new com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoRequest()
                    .setUnionId(unionId)
                    .setOption(option);
            GetFileDownloadInfoResponse getFileDownloadInfoResponse =  client.getFileDownloadInfoWithOptions(spaceId, dentryId, getFileDownloadInfoRequest, getFileDownloadInfoHeaders, new com.aliyun.teautil.models.RuntimeOptions());
            return getFileDownloadInfoResponse.getBody();
        } catch (TeaException err) {
            LogUtil.error(err, "getDingDownloadInfos TeaException error");
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            LogUtil.error(_err, "getDingDownloadInfos Exception error");
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return null;
    }

    /**
       * @Description: 下载文件
       * @params:  [getFileDownloadInfoResponseBody, path  文件要下载的目标路径]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/10/20 17:15
       * @https://open.dingtalk.com/document/orgapp/obtains-the-download-information-about-a-file
       */
    public static void dingDownloadFile(GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody, String path) {
        dingDownloadFile(getFileDownloadInfoResponseBody, path, null);
    }

    /** https://open.dingtalk.com/document/orgapp/use-range-download-a-part-of-content */
    public static void dingDownloadFile(GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody, String path, String range) {

        //用okhttp下载
        GetFileDownloadInfoResponseBody.GetFileDownloadInfoResponseBodyHeaderSignatureInfo info = getFileDownloadInfoResponseBody.getHeaderSignatureInfo();
        List<String> urls = info.getResourceUrls();
        String url = urls.get(0); // 调用获取下载信息接口获取的internalResourceUrls。

        // 调用获取下载信息接口得到的headers信息。
        Map<String, String> headers = info.getHeaders();
        if (!ObjectUtils.isEmpty(range)){
            // headers.put("Range","bytes=0-499");
            headers.put("Range",range);
        }

        //创建客户端对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建请求对象
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build();
        Sink sink = null;
        BufferedSink bufferedSink = null;
        try {
            //创建回调对象
            Response response = okHttpClient.newCall(request).execute();
            long length = response.body().contentLength();
            LogUtil.info("storageDentryUpdate dingDownloadFile result path=" + path + "长度=" + length);
            File dest = new File(path);
            sink = Okio.sink(dest);
            bufferedSink = Okio.buffer(sink);
            bufferedSink.writeAll(response.body().source());
        } catch (Exception e) {
            LogUtil.error(e, "storageDentryUpdate dingDownloadFile 下载失败， path=" + path);
            e.printStackTrace();
        } finally {
            if (bufferedSink != null) {
                try {
                    bufferedSink.close();
                }catch (Exception e){
                    LogUtil.error(e, "storageDentryUpdate dingDownloadFile bufferedSink 关闭流失败");
                }
            }
            if (sink != null) {
                try {
                    sink.close();
                }catch (Exception e){
                    LogUtil.error(e, "storageDentryUpdate dingDownloadFile sink 关闭流失败");
                }
            }
        }

        /* 异步请求
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.info("storageDentryUpdate result 结果：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                long lenth = response.body().contentLength();
                LogUtil.info("storageDentryUpdate result 长度=" + lenth);
                Sink sink = null;
                BufferedSink bufferedSink = null;
                try {
                    File dest = new File(path);
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());
                } catch (Exception e) {
                    LogUtil.error(e, "storageDentryUpdate 下载失败");
                    e.printStackTrace();
                } finally {
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }
                    if (sink != null) {
                        sink.close();
                    }
                }
            }
        });*/

    }
    public static final String REQUEST_HOST = "https://oapi.dingtalk.com/topapi";

    /**
       * @Description: 根据unionid获得钉钉userId
       * @params:  [UnionId, accessToken]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/10/24 11:00
       * @Modified:
       */
    public static String getUserIdByUnionid(String UnionId, String accessToken) {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(REQUEST_HOST + "/user/getbyunionid");
            OapiUserGetbyunionidRequest req = new OapiUserGetbyunionidRequest();
            req.setUnionid(UnionId);
            OapiUserGetbyunionidResponse rsp = client.execute(req, accessToken);
            if (Objects.equals(rsp.getErrcode(), 0L) && Objects.equals(rsp.getErrmsg(), "ok")) {
                return rsp.getResult().getUserid();
            }
            LogUtil.info("getUserIdByUnionid " + JSONObject.toJSONString(rsp));
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
       * @Description: 根据unionid获得用户信息
       * @params:  [unionId, accessToken]
       * @Return:  com.svnlan.user.dto.UserDTO
       * @Author:  sulijuan
       * @Date:  2023/10/24 11:01
       * @https://open.dingtalk.com/document/isvapp/dingtalk-retrieve-user-information
       */
    public static UserDTO getDingUserInfoByUnionid(String unionId, String accessToken){
        try {
            // 三方登录
            com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
            GetUserHeaders getUserHeaders = new GetUserHeaders();
            getUserHeaders.xAcsDingtalkAccessToken = accessToken;
            //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
            GetUserResponseBody dingUserInfo = client.getUserWithOptions(unionId, getUserHeaders, new RuntimeOptions()).getBody();

            LogUtil.info("getDingUserInfoByUnionid dingUserInfo= " + JsonUtils.beanToJson(dingUserInfo));
            UserDTO userDTO = new UserDTO();
            userDTO.setUnionId(unionId);
            userDTO.setNickname(dingUserInfo.nick);
            userDTO.setAvatar(dingUserInfo.avatarUrl);
            userDTO.setOpenId(dingUserInfo.openId);
            userDTO.setName(dingUserInfo.mobile);
            userDTO.setPhone(dingUserInfo.mobile);
            userDTO.setAvatar(dingUserInfo.avatarUrl);
            return userDTO;
        }catch (Exception e){
            LogUtil.error(e, "getDingUserInfoByUnionid 获取用户信息失败");
        }
        return null;
    }

    public static UserDTO getDingUserInfoByuserid(String userId, String accessToken) {
        UserDTO userDTO = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);
            OapiV2UserGetResponse rsp = client.execute(req, accessToken);
            OapiV2UserGetResponse.UserGetResponse dingUserInfo = rsp.getResult();
            if (!ObjectUtils.isEmpty(dingUserInfo)){
                LogUtil.info("getDingUserInfoByUnionid dingUserInfo= " + JsonUtils.beanToJson(dingUserInfo));
                userDTO = new UserDTO();
                userDTO.setUnionId(dingUserInfo.getUnionid());
                userDTO.setNickname(dingUserInfo.getName());
                userDTO.setAvatar(dingUserInfo.getAvatar());
                userDTO.setOpenId(null);
                userDTO.setName(dingUserInfo.getMobile());
                userDTO.setPhone(dingUserInfo.getMobile());
                userDTO.setAvatar(dingUserInfo.getAvatar());
                userDTO.setDingUserId(userId);
                userDTO.setDeptIdList(dingUserInfo.getDeptIdList());
                return userDTO;
            }
        } catch (ApiException e) {
            LogUtil.error(e, "根据unionId获取钉钉用户信息失败");
        }
        return null;
    }

    public static boolean dingtalkstorageSubscribeEvent(String accessToken,String unionId,String scopeId) throws Exception {
        com.aliyun.dingtalkstorage_1_0.Client client = createClient3();
        com.aliyun.dingtalkstorage_1_0.models.SubscribeEventHeaders subscribeEventHeaders = new com.aliyun.dingtalkstorage_1_0.models.SubscribeEventHeaders();
        subscribeEventHeaders.xAcsDingtalkAccessToken = accessToken;
        com.aliyun.dingtalkstorage_1_0.models.SubscribeEventRequest subscribeEventRequest = new com.aliyun.dingtalkstorage_1_0.models.SubscribeEventRequest()
                .setUnionId(unionId)
                .setScopeId(scopeId)
                .setScope("ORG");
        boolean result = false;
        try {
            SubscribeEventResponse subscribeEventResponse = client.subscribeEventWithOptions(subscribeEventRequest, subscribeEventHeaders, new RuntimeOptions());
            SubscribeEventResponseBody b = subscribeEventResponse.getBody();
            if (!ObjectUtils.isEmpty(b)){
                LogUtil.info("dingtalkstorageSubscribeEvent body=" + JsonUtils.beanToJson(b));
            }
            result = subscribeEventResponse.getBody().getSuccess();
        } catch (TeaException err) {
            LogUtil.error(err, "dingtalkstorageSubscribeEvent 订阅失败！");
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            LogUtil.error(_err, "dingtalkstorageSubscribeEvent 订阅失败！");
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return result;
    }

    /**
     * 获取部门列表
     * @param deptId
     * @throws ApiException
     * 本接口只支持获取当前部门的下一级部门基础信息，不支持获取当前部门下所有层级子部门
     */
    public static JSONObject getDepartmentList(String accessToken,Long deptId) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(DingTalkRequestApi.DEPT_LIST);
        OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
        if (!ObjectUtils.isEmpty(deptId) && deptId > 0){
            req.setDeptId(deptId);
        }
        req.setLanguage("zh_CN");
        OapiV2DepartmentListsubResponse rsp = client.execute(req, accessToken);
        LogUtil.info("getDepartmentList=" + rsp.getBody());
        return JSONObject.parseObject(rsp.getBody());
    }

    public static JSONObject getDepartmentUserList(String accessToken,Long deptId, long cursor, long size) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(DingTalkRequestApi.UNION_USER_LIST);
        OapiV2UserListRequest req = new OapiV2UserListRequest();
        req.setDeptId(deptId);
        req.setCursor(cursor);
        req.setSize(size);
        req.setOrderField("modify_desc");
        req.setContainAccessLimit(false);
        req.setLanguage("zh_CN");
        OapiV2UserListResponse rsp = client.execute(req, accessToken);
        String body = rsp.getBody();
        LogUtil.info("getDepartmentUserList=" + body);
        return ObjectUtils.isEmpty(body) ? null : JSONObject.parseObject(rsp.getBody());
    }

    /**
     * 获取家校通部门列表
     * @param deptId
     * @throws ApiException
     * 本接口只支持获取当前部门的下一级部门基础信息，不支持获取当前部门下所有层级子部门
     */
    public static JSONObject getSchoolDepartmentList(String accessToken,Long deptId) throws ApiException {
        return getSchoolDepartmentList(accessToken, deptId, 100L, 1L);
    }
    public static JSONObject getSchoolDepartmentList(String accessToken,Long deptId, Long pageSize, Long pageNo) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(DingTalkRequestApi.SCHOOL_DEPT_LIST);
        OapiEduDeptListRequest req = new OapiEduDeptListRequest();
        req.setPageSize(pageSize);
        req.setPageNo(pageNo);
        if (!ObjectUtils.isEmpty(deptId) && deptId > 0){
            req.setSuperId(deptId);
        }
        OapiEduDeptListResponse rsp = client.execute(req, accessToken);
        LogUtil.info("getSchoolDepartmentList=" + rsp.getBody());
        return JSONObject.parseObject(rsp.getBody());
    }

    /**
     *
     * @param accessToken
     * @param deptId
     * @param keyword
     * @param pageSize
     * @param pageNo
     * @return
     * 文档地址 https://open.dingtalk.com/document/orgapp/obtains-a-list-of-home-school-user-identities
     */
    public static JSONObject getDepartmentSchoolUserList(String accessToken,Long deptId, String keyword, Long pageSize, Long pageNo) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(DingTalkRequestApi.SCHOOL_USER_LIST);
        OapiEduUserListRequest req = new OapiEduUserListRequest();
        req.setPageSize(pageSize);
        req.setPageNo(pageNo);
        req.setRole(keyword);
        req.setClassId(deptId);
        OapiEduUserListResponse rsp = client.execute(req, accessToken);
        LogUtil.info("getDepartmentSchoolUserList=" + rsp.getBody());
        return JSONObject.parseObject(rsp.getBody());
    }

    public static Dept getDeptDetail(Long deptId, String accessToken) {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(DingTalkRequestApi.DEPT_DETAIL);
            OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
            if (Objects.nonNull(deptId)) {
                req.setDeptId(deptId);
            }
            OapiV2DepartmentGetResponse resp = client.execute(req, accessToken);
            System.out.println(resp.getBody());
            DingTalkResult<Dept> dingTalkResult = JSON.parseObject(resp.getBody(), new TypeReference<DingTalkResult<Dept>>() {
            });
            Assert.isTrue(dingTalkResult.getErrCode() == 0, "获取部门列表失败 " + dingTalkResult.getErrMsg());
            return dingTalkResult.getResult();
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static OapiEduUserGetResponse getEduUserInfo(Long deptId, String userId , String access_token) throws ApiException{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/edu/user/get");
        OapiEduUserGetRequest req = new OapiEduUserGetRequest();
        req.setClassId(deptId);
        req.setRole("teacher");
        req.setUserid(userId);
        OapiEduUserGetResponse rsp = client.execute(req, access_token);

        LogUtil.info(userId + " 家校通类型钉转存 getEduUserInfo=" + rsp.getBody());
        // {"errcode":0,"result":{"details":[]},"success":true,"request_id":"15r8esyh0o1v7"}
        return rsp;
    }

    /**
     * 获取部门详情
     * @param deptId
     * @param access_token
     * @return
     *  https://open.dingtalk.com/document/isvapp/query-department-details0-v2
     */
    public static OapiV2DepartmentGetResponse getDepartmentInfo(Long deptId, String access_token) throws ApiException{
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/department/get");
        OapiV2DepartmentGetRequest req = new OapiV2DepartmentGetRequest();
        req.setDeptId(deptId);
        req.setLanguage("zh_CN");
        OapiV2DepartmentGetResponse rsp = client.execute(req, access_token);
        LogUtil.info(deptId + " 部门详情 getDepartmentInfo=" + rsp.getBody());
        return rsp;
    }

    public static boolean checkDingUserIsTeacher(List<Long> deptList, String access_token) throws ApiException{
        boolean isTeacher = false;
        for (Long deptId : deptList){
            try {
                isTeacher = DingUtil.checkDingUserIsTeacher(deptId, access_token);
                if (isTeacher){
                    break;
                }
            }catch (Exception e){
                LogUtil.error(e, "获取家校通是否是老师失败");
            }
        }
        return isTeacher;
    }
    public static boolean checkDingUserIsTeacher(Long deptId, String access_token) throws ApiException{
        boolean check = false;
        // 学生、家长通过/topapi/v2/user/get接口获得的部门id列表是部门名称为学生、家长，此部门上一级为classId
        // {"errcode": 0,"errmsg": "ok","result": {"auto_add_user": false,"auto_approve_apply": false,"create_dept_group": false,"dept_id": 704737013,"dept_permits": [],"emp_apply_join_dept": false,"extention": "{\"faceCount\":\"1\"}","group_contain_sub_dept": false,"hide_dept": false,"name": "家长","order": 704737013,"outer_dept": true,"outer_permit_depts": [704737015],"outer_permit_users": [],"parent_id": 704495823,"user_permits": []},"request_id": "16kkdi35wx79a"}
        OapiV2DepartmentGetResponse rsp = getDepartmentInfo(deptId, access_token);
        if (Objects.equals(rsp.getErrcode(), 0L) && Objects.equals(rsp.getErrmsg(), "ok")) {
            OapiV2DepartmentGetResponse.DeptGetResponse result = rsp.getResult();
            if (!ObjectUtils.isEmpty(result)){
                if (Arrays.asList(GlobalConfig.NOT_TEACHER).contains(result.getName())){
                    check = false;
                }else {
                    check = true;
                }
            }
        }
        return check;
    }
}
