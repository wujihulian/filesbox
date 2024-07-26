package com.svnlan.home.utils;


import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DingTalkSignatureUtil;
import com.dingtalk.api.response.OapiEduUserGetResponse;
import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.Dept;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.utils.AESUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class AsyncDingFileUtilTest {
    //@Resource
    //AsyncDingFileUtil asyncDingFileUtil;
    @Test
    public void refreshSubscribe() throws Exception {
       // asyncDingFileUtil.refreshSubscribe();
    }

    @Test
    public void getCubeSin(){
        // 获取魔方签名
        System.out.println("aaaaaaaaaaaa");
        Long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String canonicalString = DingTalkSignatureUtil.getCanonicalStringForIsv(timestamp, (String)null);
        String signature = DingTalkSignatureUtil.computeSignature(sk, canonicalString);
        System.out.println(signature);
    }

    @Test
    public void sss(){
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String dingCorpId = "dinga48ee59c9f9567c0f2c783f7214b6d69";
        try {
            jsonObj = CubeUtil.getCubeOrgByCorpIdApi(ak, sk, dingCorpId);
        }catch (Exception e){
            LogUtil.error(e, "获取当前钉钉组织信息绑定的魔⽅组织 ak=" + ak);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }
    @Test
    public void sss2(){
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String corpId = "61d3b20aa88122245888f205";
        String imDeptId = "class_64d9cd719824ae272660015c";
        try {
            jsonObj = CubeUtil.getDepartmentSchoolList(ak, sk, corpId, imDeptId);
        }catch (Exception e){
            LogUtil.error(e, "查询组织班级等接 ak=" + ak);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }

    @Test
    public void sss3(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";
        String imDeptId =  "1";
        int index = 2;
        int size = 100;
        try {
            jsonObj = CubeUtil.getDepartmentUserList(cubeOrgId, imDeptId,ak, sk, index, size);
            //jsonObj = CubeUtil.getDepartmentStudentList(cubeOrgId, imDeptId,ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "魔⽅组织查询魔⽅组织⼈员列表（分⻚） imDeptId=" + imDeptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }

    @Test
    public void sss4(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeUserId = "64425abdf70a66a94a6cf9b3";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.getCubeUserInfo(cubeUserId, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "根据魔⽅⽤户id查询魔⽅⽤户信息 imDeptId=" + imDeptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }

    @Test
    public void sss5(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";

        String cubeUserId = "6194aae9f70a6660ae920967";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.getCubeUserAllInfo(cubeUserId, cubeOrgId, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "根据魔⽅⽤户获取部⻔信息 imDeptId=" + imDeptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }


    @Test
    public void sss6(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";

        String cubeUserId = "64d9cd7e73a66301aa0ed385";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.queryImDeptList( cubeOrgId, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "魔⽅组织下钉钉部⻔列表 " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }
    @Test
    public void sss601(){
        JSONObject jsonObj = null;

        String accessToken = "52378e29b36934a5ad34411d278e9e35";
        Long deptId = 669263063L;
        long pageNo = 1;
        long pageSize = 100;
        try {
            jsonObj = DingUtil.getDepartmentSchoolUserList(accessToken, deptId, "teacher", pageSize, pageNo);
            System.out.println(JsonUtils.beanToJson(jsonObj));
        }catch (Exception e){
            LogUtil.error(e, "魔⽅组织下钉钉部⻔列表 " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

    }
    @Test
    public void sss7(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";

        String cubeUserId = "64d9cd7e73a66301aa0ed385";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.getSubCubeOrgApi( cubeOrgId, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "根据魔⽅组织id查询分⽀已激活组织信息 " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }
    @Test
    public void sss8(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";

        String cubeUserId = "64d9cd7e73a66301aa0ed385";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.queryImDeptListByParentId( cubeOrgId, ak, sk, null);
        }catch (Exception e){
            LogUtil.error(e, "魔方内部的部门列表 " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }
    @Test
    public void sss9(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";

        String cubeUserId = "64425abdf70a66a94a6cf9b3";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.queryImUserIdList( cubeOrgId, Arrays.asList(cubeUserId), ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "据用户ID查询绑定的钉钉id " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }

    @Test
    public void sss10(){
        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";

        String cubeUserId = "6144b588a88122f77900c6e2";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        String suiteTicket = "JqLOAL9WbgsLyLdQLqvQGuX5ulU0YLhYGOiglCc4YM7LP5i20LeP17yJidD4IB6pCfeuR50gECwPkpEsyMNQqp";// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.getCorpToken( cubeOrgId, suiteTicket, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "据用户ID查询绑定的钉钉id " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }
    @Test
    public void sss010(){
        JSONObject jsonObj = null;
        String token = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "dinge270ef3100e747cc";

        String suiteTicket = "JqLOAL9WbgsLyLdQLqvQGuX5ulU0YLhYGOiglCc4YM7LP5i20LeP17yJidD4IB6pCfeuR50gECwPkpEsyMNQqp";

        try {
            jsonObj = CubeUtil.getCorpToken( cubeOrgId, suiteTicket, ak, sk);
            token = CubeUtil.getCorpToken2( cubeOrgId, suiteTicket, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "据用户ID查询绑定的钉钉id " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
        System.out.println(token);
    }
    @Test
    public void sss11() {

        List<JSONObject> list = null;
        JSONObject jsonObj = null;
        JSONObject dataJsonObject = null;
        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";
        String ak = "suiten05tpmwiqvjgj4s2";
        String cubeOrgId = "61d3b20aa88122245888f205";

        String cubeUserId = "6144b588a88122f77900c6e2";
        String imDeptId = null;// "class_64d9cd719824ae272660015c";
        //String imUserId = "3000000000545945097";
        String imUserId = "016107581205451";
        String suiteTicket = "135891";// "class_64d9cd719824ae272660015c";
        int index = 0;
        int size = 100;
        try {
            jsonObj = CubeUtil.getCubeUserByImApi( "dingcgtebn7qeow6oo1i", imUserId, ak, sk);
        }catch (Exception e){
            LogUtil.error(e, "根据钉钉信息查询用户信息 " );
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        System.out.println(jsonObj);
    }
    @Test
    public void aaas(){
        String password = "0+cCf59vvlTpfCvbLEd3Yg==";
         password = AESUtil.decrypt(password, GlobalConfig.LOGIN_PASSWORD_AES_KEY, true);
        System.out.println(password);

        String token = "a81786f51d9b33168f7b3abad889edad";
        String dingUserId = "080529161832824977";//"016107581205451";

        //UserDTO userDTO = DingUtil.getDingUserInfoByuserid(dingUserId, token);

          token = "9e24699718433596abb63962e10062e4";
        //UserDTO userDTO = DingUtil.getDingUserInfoByuserid(dingUserId, token);

        Long deptId = 98013138L;//669263063L;
        OapiEduUserGetResponse jsonObj = null;
        try {
            jsonObj = DingUtil.getEduUserInfo(deptId, dingUserId, token);
        }catch (Exception e){
            LogUtil.error(e);
        }

        System.out.println(jsonObj);
    }


    @Test
    public void getCubeOrgByCubeOrgIdApi(){
        String ak = "suiten05tpmwiqvjgj4s2";

        String sk = "LfXHNuH_UQ_CVKeIPAVS1PU-APJxE3uHossAxOsawazUoMMSBSzoeGf1TYNyHPjR";

        JSONObject jsonObj = null;
        try {
            jsonObj = CubeUtil.getCubeOrgByCubeOrgIdApi(ak, sk);
        }catch (Exception e){
            LogUtil.error(e);
        }
        System.out.println(jsonObj);
    }

}