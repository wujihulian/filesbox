package com.svnlan.home.utils;

import com.alibaba.edu.cube.api.CubeClient;
import com.alibaba.edu.cube.api.CubeClientFactory;
import com.alibaba.edu.cube.api.dto.cubeorg.request.OapiImDeptListByParentIdRequest;
import com.alibaba.edu.cube.api.dto.cubeorg.response.OapiImDeptListByParentIdResponse;
import com.alibaba.edu.cube.api.request.*;
import com.alibaba.edu.cube.api.response.*;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkoauth2_1_0.Client;
import com.aliyun.dingtalkoauth2_1_0.models.GetCorpAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetCorpAccessTokenResponse;
import com.aliyun.dingtalkoauth2_1_0.models.GetCorpAccessTokenResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.Common;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.svnlan.utils.LogUtil;
import com.taobao.api.ApiException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class CubeUtil {



    public static JSONObject getCubeOrgByCubeOrgIdApi(String ak, String sk) throws ApiException{
        CubeClient cubeClient =
                CubeClientFactory.cubeClient("/topapi/cube/org/getCubeOrgByCubeOrgIdApi",ak,sk);
        OapiCubeOrgGetRequest cubeOrgRequest = new OapiCubeOrgGetRequest();
        cubeOrgRequest.setCubeOrgId("1");
        OapiCubeOrgGetResponse cubeOrgResponse =
                cubeClient.executeCube(cubeOrgRequest);
        return JSONObject.parseObject(cubeOrgResponse.getBody());
    }
    public static JSONObject getCubeOrgByCorpIdApi(String ak, String sk, String dingCorpId) throws ApiException {
        LogUtil.info(String.format("根据钉钉组织id获取魔方组织信息 getCubeOrgByCorpIdApi ak: %s  sk: %s  dingCorpId: %s",ak,sk,dingCorpId));
        CubeClient cubeClient =
                CubeClientFactory.cubeClient("/topapi/cube/org/getCubeOrgByCorpIdApi",ak,sk);
        OapiCubeOrgGetRequest cubeOrgRequest = new OapiCubeOrgGetRequest();
        cubeOrgRequest.setCorpId(dingCorpId);
        OapiCubeOrgGetResponse cubeOrgResponse = cubeClient.executeCube(cubeOrgRequest);
        return JSONObject.parseObject(cubeOrgResponse.getBody());
    }

    /*
     * 查询组织班级等接⼝
     */
    public static JSONObject getDepartmentSchoolList(String ak, String sk, String cubeOrgId, String parentDeptId) throws ApiException {

        LogUtil.info(String.format("查询组织班级等接 getDepartmentSchoolList ak: %s  sk: %s  cubeOrgId: %s  parentDeptId: %s",ak,sk,cubeOrgId,parentDeptId));
        //CubeClientFactory.setEnv(EvnEnums.Daily);
        String url = "/topapi/cube/user/CubeOrgOutQueryService_queryCubeEduDeptList";
        CubeClient cubeClient = CubeClientFactory.cubeClient(url, ak, sk);
        ApiStringMapRequest request = new ApiStringMapRequest();
        request.putBizParams("cubeOrgId",cubeOrgId);
        request.putBizParams("parentDeptId", parentDeptId);
        ApiCommonResponse response = cubeClient.executeCube(request);
        return JSONObject.parseObject(response.getBody());
    }

    /*
     * 魔⽅组织查询魔⽅组织⼈员列表（分⻚）
     */
    public static JSONObject getDepartmentUserList(String cubeOrgId, String imDeptId,String ak, String sk, int index, int size) throws ApiException {

        CubeClient cubeClient =
                CubeClientFactory.cubeClient("/topapi/cube/org/getCubeUserByPageApi",ak,sk);
        OapiCubeUserGetByPageRequest cubeUserRequest = new
                OapiCubeUserGetByPageRequest();
        cubeUserRequest.setCubeOrgId(cubeOrgId);
        cubeUserRequest.setImDeptId(imDeptId);
        cubeUserRequest.setPageIndex(index);
        cubeUserRequest.setPageSize(size);
        OapiCubeUserGetByPageResponse cubeUserResponse = cubeClient.executeCube(cubeUserRequest);
        return JSONObject.parseObject(cubeUserResponse.getBody());
    }

    /*
    根据魔⽅⽤户id查询魔⽅⽤户信息
     */
    public static JSONObject getCubeUserInfo(String cubeUserId, String ak, String sk) throws ApiException {
        CubeClient cubeClient = CubeClientFactory.cubeClient("/topapi/cube/org/getCubeUserByIdApi",ak,sk );
        OapiCubeUserByIdGetRequest cubeUserRequest = new OapiCubeUserByIdGetRequest();
        cubeUserRequest.setCubeUserId(cubeUserId);
        OapiCubeUserGetResponse cubeUserResponse = cubeClient.executeCube(cubeUserRequest);
        return JSONObject.parseObject(cubeUserResponse.getBody());
    }


    /*
    魔⽅组织下钉钉部⻔列表
     */
    public static JSONObject queryImDeptList(String cubeOrgId, String ak, String sk) throws ApiException {
        LogUtil.info(String.format("魔⽅组织下钉钉部⻔列表 ak：%s  sk: %s  cubeOrgId: %s", ak,sk,cubeOrgId));
        CubeClient cubeClient =
                CubeClientFactory.cubeClient("/topapi/cube/org/queryImDeptList",ak,sk);
        OapiCubeImDeptListRequest request = new OapiCubeImDeptListRequest();
        request.setOrgId(cubeOrgId);
        OapiCubeImDeptListResponse response = cubeClient.executeCube(request);
        return JSONObject.parseObject(response.getBody());
    }

    public static JSONObject getCubeUserAllInfo(String cubeUserId, String cubeOrgId, String ak, String sk) throws ApiException {
        String url = "/topapi/cube/CubeUserOutQueryService_queryCubeUserWithDeptInfo";
        CubeClient cubeClient = CubeClientFactory.cubeClient(url, ak, sk)
                ;
        ApiStringMapRequest request = new ApiStringMapRequest();
        request.putBizParams("cubeOrgId",cubeOrgId);
        request.putBizParams("cubeUserId",cubeUserId);
        ApiCommonResponse response = cubeClient.executeCube(request);
        return JSONObject.parseObject(response.getBody());
    }

    /*
     * 根据学校组织、班级查询学⽣列表
     */
    public static JSONObject getDepartmentStudentList(String cubeOrgId, String classId,String ak, String sk) throws ApiException {

        String url = "/topapi/cube/user/getCubeUserByClassApi";
        CubeClient cubeClient = CubeClientFactory.cubeClient(url, ak, sk);
        ApiStringMapRequest request = new ApiStringMapRequest();
        request.putBizParams("cubeOrgId",cubeOrgId);
        request.putBizParams("classId",classId);
        ApiCommonResponse response = cubeClient.executeCube(request);
        return JSONObject.parseObject(response.getBody());
    }

    /*
    根据魔⽅组织id查询分⽀已激活组织信息
     */
    public static JSONObject getSubCubeOrgApi(String cubeOrgId, String ak, String sk) throws ApiException {
        CubeClient cubeClient1 =
                CubeClientFactory.cubeClient("/topapi/cube/org/getSubCubeOrgApi",ak,sk)
                ;
        OapiCubeOrgListGetRequest cubeOrgRequest1 = new
                OapiCubeOrgListGetRequest();
        cubeOrgRequest1.setCubeOrgId(cubeOrgId);
        OapiCubeOrgListGetResponse cubeOrgResponse1 =
                cubeClient1.executeCube(cubeOrgRequest1);
        return JSONObject.parseObject(cubeOrgResponse1.getBody());
    }

    public static JSONObject queryImDeptListByParentId(String cubeOrgId, String ak, String sk, String parentId) throws ApiException{
        CubeClient cubeClient = CubeClientFactory.cubeClient("/topapi/cube/org/queryImDeptListByParentId",
                ak, sk);
        OapiImDeptListByParentIdRequest request = new OapiImDeptListByParentIdRequest();
        request.setOrgId(cubeOrgId);
        request.setParentId(parentId);
        OapiImDeptListByParentIdResponse response = cubeClient.executeCube(request);
        return JSONObject.parseObject(response.getBody());
    }
    /**
     * 根据魔方用户获取钉钉im用户信息
     *
     * @return
     */
    public static JSONObject getImUserByCubeUserApi(String cubeOrgId, String cubeUserId, String ak, String sk) throws ApiException {
        final CubeClient cubeClient = CubeClientFactory.cubeClient("/topapi/cube/user/getImUserByCubeUserApi",
                ak, sk);
        OapiCubeImUserByCubeGetRequest cubeUserRequest = new OapiCubeImUserByCubeGetRequest();
        cubeUserRequest.setCubeUserId(cubeOrgId);
        cubeUserRequest.setCubeOrgId(cubeUserId);
        OapiCubeImUserByCubeGetResponse cubeUserResponse = cubeClient.executeCube(cubeUserRequest);

        return JSONObject.parseObject(cubeUserResponse.getBody());
    }
    public static JSONObject queryImUserIdList(String cubeOrgId, List<String> cubeUserIds, String ak, String sk) throws ApiException {
        String url = "/topapi/cube/user/queryImUserIdList";
        CubeClient cubeClient = CubeClientFactory.cubeClient(url,
                ak, sk);
        ApiStringMapRequest request = new ApiStringMapRequest();
        request.putBizParams("cubeOrgId", cubeOrgId);
        request.putBizParams("cubeUserIds", JSONObject.toJSONString(cubeUserIds));
        ApiCommonResponse response = cubeClient.executeCube(request);

        return JSONObject.parseObject(response.getBody());
    }

    public static JSONObject getCorpToken(String cubeOrgId, String suiteTicket, String ak, String sk) {
        DefaultDingTalkClient client= new DefaultDingTalkClient("https://oapi.dingtalk.com/service/get_corp_token");
        OapiServiceGetCorpTokenRequest req= new OapiServiceGetCorpTokenRequest();
        req.setAuthCorpid(cubeOrgId);


        String token = null;
        JSONObject jsonObj = null;
        try {
            OapiServiceGetCorpTokenResponse execute= client.execute(req,ak,sk,suiteTicket);
            jsonObj = JSONObject.parseObject(execute.getBody());
        }catch (ApiException e){
            LogUtil.error("服务商获取第三方应用授权企业的access_token suiteTicket=" + suiteTicket + "， jsonObj" + jsonObj);
        }

        JSONObject body = null;

        if (!org.springframework.util.ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)
                && Objects.equals(jsonObj.getString("errmsg"), "ok")) {
            LogUtil.info("服务商获取第三方应用授权企业的access_token " + jsonObj);
            token = jsonObj.getString("access_token");
            int expiresIn = jsonObj.getInteger("expires_in");
            body = new JSONObject();
            body.put("token", token);
            body.put("expiresIn", expiresIn);
        } else {
            LogUtil.error("服务商获取第三方应用授权企业的access_token 失败 jsonObj" + jsonObj);
        }

        return body;
    }


    public static JSONObject getCubeUserByImApi(String cubeOrgId, String cubeUserId, String ak, String sk) throws ApiException {
        String url = "/cube/org/getCubeUserByImApi";
        CubeClient cubeClient = CubeClientFactory.cubeClient(url,
                ak, sk);
        ApiStringMapRequest request = new ApiStringMapRequest();
        request.putBizParams("cubeOrgId", cubeOrgId);
        request.putBizParams("corpId", cubeOrgId);
        request.putBizParams("imUserId", cubeUserId);
        ApiCommonResponse response = cubeClient.executeCube(request);

        return JSONObject.parseObject(response.getBody());
    }

    public static Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }

    public static String getCorpToken2(String corpId, String suiteTicket, String ak, String sk) throws Exception{

        Client client = createClient();
        GetCorpAccessTokenRequest getCorpAccessTokenRequest = new GetCorpAccessTokenRequest()
                .setSuiteKey(ak)
                .setSuiteSecret(sk)
                .setAuthCorpId(corpId)
                .setSuiteTicket(suiteTicket);
        String accessToken = null;
        try {
            GetCorpAccessTokenResponse accessTokenResponse = client.getCorpAccessToken(getCorpAccessTokenRequest);

            GetCorpAccessTokenResponseBody body = accessTokenResponse.getBody();
            accessToken = body.accessToken;

        } catch (TeaException err) {
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!Common.empty(err.code) && !Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        }
        return accessToken;
    }
}
