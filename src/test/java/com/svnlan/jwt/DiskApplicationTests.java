package com.svnlan.jwt;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.CompressFileDto;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.service.ExplorerFileService;
import com.svnlan.home.service.ExplorerOperationsService;
import com.svnlan.home.service.UploadService;
import com.svnlan.home.service.UploadZipService;
import com.svnlan.home.utils.CompressFileReader;
import com.svnlan.home.utils.CompressFileUtil;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.office.LibreOfficeDUtil;
import com.svnlan.home.utils.video.VideoGetUtil;
import com.svnlan.home.utils.zip.ZipUtils;
import com.svnlan.home.vo.ChangeSourceVo;
import com.svnlan.home.vo.YzEditParamsDto;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.service.JWTService;
import com.svnlan.manage.dto.CommonInfoTypeDto;
import com.svnlan.manage.dto.GetDesignListDTO;
import com.svnlan.manage.service.CommonDesignService;
import com.svnlan.manage.service.CommonInfoTypeService;
import com.svnlan.utils.*;
import net.lingala.zip4j.ZipFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.xmind.core.Core;
import org.xmind.core.ISheet;
import org.xmind.core.IWorkbook;
import org.xmind.core.IWorkbookBuilder;
import org.xmind.core.marker.IMarkerSheet;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiskApplicationTests {

    @Test
    public void jwtTest() {

        //String winUserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
        String winUserAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36";
        //String winUserAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
        System.out.println("浏览器组：" + UserAgentUtils.getBorderGroup(winUserAgent));
        String borderName = UserAgentUtils.getOsName(winUserAgent);
        System.out.println(borderName + "a");
        System.out.println("浏览器名字：" + UserAgentUtils.getBorderName(winUserAgent));
        System.out.println("浏览器类型" + UserAgentUtils.getBorderType(winUserAgent));
        System.out.println("浏览器生产商：" + UserAgentUtils.getBrowserManufacturer(winUserAgent));
        System.out.println("浏览器版本：" + UserAgentUtils.getBrowserVersion(winUserAgent));
        System.out.println("设备生产厂商:" + UserAgentUtils.getDeviceManufacturer(winUserAgent));
        System.out.println("设备类型:" + UserAgentUtils.getDevicetype(winUserAgent));
        System.out.println("设备操作系统：" + UserAgentUtils.getOs(winUserAgent));
        System.out.println("操作系统的名字：" + UserAgentUtils.getOsName(winUserAgent));
        System.out.println("操作系统浏览器的渲染引擎:" + UserAgentUtils.getBorderRenderingEngine(winUserAgent));
//		String androidUserAgent = "Mozilla/5.0 (Linux; Android 8.0; LON-AL00 Build/HUAWEILON-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044204 Mobile Safari/537.36 V1_AND_SQ_7.7.8_908_YYB_D QQ/7.7.8.3705 NetType/WIFI WebP/0.3.0 Pixel/1440";
//		String iosUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16A366 QQ/7.7.8.421 V1_IPH_SQ_7.7.8_1_APP_A Pixel/750 Core/UIWebView Device/Apple(iPhone 6s) NetType/WIFI QBWebViewType/1";
    }

    @Test
    public void aa() {
        String address = "220.195.66.3";
        IPAddressUtils ipAddressUtils = new IPAddressUtils();
        if (IPAddressUtils.file == null) {
            //ipAddressUtils.init();
            String city = ipAddressUtils.getIPLocation(address).getCountry();
            System.out.println(city);
        } else {
            try {
                // ipAddressUtils.init();
                String city = ipAddressUtils.getIPLocation(address).getCountry();
                System.out.println(city);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
    @Autowired
    JWTService jwtService;
    @Autowired
    LoginUserUtil loginUserUtil;
    @Autowired
    UploadService uploadService;
    @Test
    public void loginTest(){
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = servletRequestAttributes.getRequest();
//        String serverName = request.getServerName();
//        UserDTO dto = new UserDTO();
//        String username="xiaoman2";
//        String password="Y2PGVLEjTvQMsajbxA4tZw==";
//        dto.setUsername(username);
//        dto.setPassword(password);
//        System.out.println(jwtService.getAccessToken(dto));
        LoginUser loginUser = loginUserUtil.getAccessToken("+uZCAfeuoQOJ0W+gcYvPLVAlb7duE4cuQ1VAilNTh9WEkTGMqUvdcstz8CDAqP5bRaAxG81U3AhNuPI/P9yLUzuCmhg9SQqCWZvQ+Z2qTlY=");
        System.out.println(JsonUtils.beanToJson(loginUser));
    }



    /**
     * @Description: properties 中午转义 uncode
     * @params:  []
     * @Return:  void
     * @Author:  sulijuan
     * @Date:  2023/2/7 16:20
     * @Modified:
     */
    @Test
    public void te(){
        long startTime=System.currentTimeMillis(); //获取开始时间

        System.out.println(StringUtil.encode("该文件不存在"));
        System.out.println(StringUtil.encode("該文件不存在"));
        try {

            FileInputStream filestream=new FileInputStream("src/messages.txt");

            byte[] b = new byte[3];

            filestream.read(b);

            String ecode="utf-8";

            if(b[0] == -17 && b[1] == -69 && b[2] == -65){

                ecode="utf-8";

            }

            InputStreamReader readStream=new InputStreamReader(filestream,ecode);

            BufferedReader reader=new BufferedReader(readStream);

            String temp=null;

            int line=0;//行号

            while((temp=reader.readLine())!=null){

                line++;

                String[] sl = temp.split("=");
                System.out.println(sl[0].trim()+"="+ StringUtil.encode(sl[1].trim()));

            }

            long endTime=System.currentTimeMillis(); //获取结束时间

            if(readStream!=null){

                readStream.close();

            }

            if(reader!=null){

                reader.close();

            }

            System.out.println("程序运行时间： "+(endTime-startTime)/1000+"s");
        }catch (Exception e){
            LogUtil.error(e);
        }




    }

    @Test
    public void getTxt(){
        //String txt = "D://467046.txt";
        String txt = "cs025.md";
        System.out.println(FileUtil.getFileContent(txt));
    }

    @Test
    public void getTxt2(){
        CheckFileDTO checkFileDTO = new CheckFileDTO();
        checkFileDTO.setBusType("cloud");
        checkFileDTO.setSourceID(60914L);

        String text = FileUtil.getFileContent("D://467046.txt", StandardCharsets.UTF_8);
        LogUtil.info("**********************re=" + text);
    }
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    UploadZipService uploadZipService;
    @Test
    public void addIOSource(){
        List<IOSource> copyList = ioSourceDao.copySourceList(Arrays.asList(30718L));
        String name = "无极直播摄像机宣传图（";
        IOSource source = copyList.get(0);
        List<IOSource> list = new ArrayList<>();
        for (int i = 0; i <= 10000; i++) {
            source.setTargetID(1L);
            source.setCreateUser(1L);
            source.setModifyUser(1L);
            source.setName(name + (i+1) + "）");
            list.add(source);
        }

        for (List<IOSource> subList : Partition.ofSize(list, 2000)) {
            try {
                //  ioSourceDao.batchInsert(subList);
            } catch (Exception e) {
                LogUtil.error(e, "复制出错");
            }
        }


    }


    @Resource
    StringRedisTemplate stringRedisTemplate;
    //@Test
    public void toZip(){

        String targetPath = "E://zip测试打包/";
        String targetFile = "1599794872690_2232915.gif";
        String targetDir = "target/";

        //基础路径+日期路径
        String finalFolderPath = targetPath + targetDir + FileUtil.getDatePath();
        String fileName = RandomUtil.getuuid() + System.currentTimeMillis()
                + "_1" + ".zip";
        String finalFilePath = finalFolderPath + fileName;
        // 创建一个目录
        java.nio.file.Path dirPath = ZipUtils.createDirectory("人物.gif", finalFolderPath);
        ZipUtils.copyFile(targetPath + targetFile, dirPath);
        try  {
            ZipUtils.toZip(finalFolderPath + "人物.gif", finalFilePath, true, stringRedisTemplate, "555", 856252);
        } catch (Exception e){

        }
    }

    // @Test
    public void toListZip(){


        /*List<String> levelList = new ArrayList<>();
        levelList.add("目录一/");
        levelList.add("目录二/");
        System.out.println(levelList.stream().collect(Collectors.joining("", "", "")));*/


        String dir = "E:/zip测试打包/target/";

        String targetPath = "E://zip测试打包/";
        String targetDir = "target/";


        String finalFolderPath = targetPath + targetDir + FileUtil.getDatePath();
        String fileName = "005.zip";
        String finalFilePath = finalFolderPath + fileName;
        try {
            ZipUtils.toZip(dir, finalFilePath, false, true, stringRedisTemplate,"111", 1545);
        }catch (Exception e){
            LogUtil.error(e);
        }
    }

    @Resource
    ExplorerOperationsService operationsService;

    //@Test
    public void initPathLevel(){
        operationsService.initSourcePathLevel(null);
    }


    //@Test
    public void getAudioPic() {


        String picPath = "F:/audio/梦然少年.jpg";
        RandomAccessFile file = null;
        try {

            Mp3File mp3file = new Mp3File("F:/audio/梦然少年.mp3");
            if (mp3file.hasId3v2Tag()) {
                ID3v2 id3v2Tag = mp3file.getId3v2Tag();

                System.out.println("唱片歌曲数量: " + id3v2Tag.getTrack());
                System.out.println("艺术家: " + id3v2Tag.getArtist());
                System.out.println("歌曲名: " + id3v2Tag.getTitle());
                System.out.println("唱片名: " + id3v2Tag.getAlbum());
                System.out.println("歌曲长度:"+mp3file.getLengthInSeconds()+"秒");
                System.out.println("码率: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
                System.out.println("专辑插画 : "+id3v2Tag.getAlbumImage());
                System.out.println("专辑插画类型"+id3v2Tag.getAlbumImageMimeType());
                System.out.println("发行时间: " + id3v2Tag.getYear());
                System.out.println("流派: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
                System.out.println("注释: " + id3v2Tag.getComment());
                System.out.println("歌词: " + id3v2Tag.getLyrics());
                System.out.println("作曲家: " + id3v2Tag.getComposer());
                System.out.println("发行公司: " + id3v2Tag.getPublisher());
                System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
                System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
                System.out.println("版权: " + id3v2Tag.getCopyright());
                System.out.println("URL: " + id3v2Tag.getUrl());
                System.out.println("编码格式: " + id3v2Tag.getEncoder());
                byte[] albumImageData = id3v2Tag.getAlbumImage();
                if (albumImageData != null) {
                    System.out.println("专辑插图长度: " + albumImageData.length + " bytes");
                    System.out.println("专辑插图类型: " + id3v2Tag.getAlbumImageMimeType());
                    //String mimeType = id3v2Tag.getAlbumImageMimeType();
                    // Write image to file - can determine appropriate file extension from the mime type
                    file = new RandomAccessFile(picPath, "rw");
                    file.write(albumImageData);
                    file.close();
                }
            }
        }catch (Exception e){
            LogUtil.error(e);
        }finally {

        }

    }

    @Resource
    DocUtil docUtil;

    @Test
    public void yzView(){

        String filePath = "https://dev.filesbox.cn/uploads/private/cloud/2023_3/2/b3339a08621e422887487b494dedbc121677742502180_30028.xlsx";
        try {
            String postUrl = "https://demo.filesbox.cn/fcscloud/composite/httpfile?convertType=61&fileUrl=" + URLEncoder.encode(filePath,"UTF-8");


            String res1 = docUtil.postYZFile(postUrl);
            LogUtil.info("yongZhong h5 api return retry : " + res1 + "filePath : " + filePath );
            Map<String, Object> resMap = JsonUtils.jsonToMap(res1);
            if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorcode") && "0".equals(resMap.get("errorcode").toString())){
                String data = resMap.get("data").toString();
                System.out.println(data);
            }
            LogUtil.info("yongZhong resMap=" + JsonUtils.beanToJson(resMap));
            System.out.println("yongZhong resMap=" + JsonUtils.beanToJson(resMap));
        } catch (Exception e2){
            LogUtil.error(e2, "转h5提交重试也失败 , o:" + filePath);
        }
    }


    @Test
    public void yzEdit(){

        //

        String filePath = "https://demo.filesbox.cn/api/disk/attachment/［定制］定制评估模板 (1).xlsx?busId=60146&busType=cloud&key=yORCT716y2coywUAaUWWMKyi5IUtJuIRhNz%2FJEqyPPFHsf8gQ%2Bz%2BKhq58Z5nfy%2FMZQR4c0BaDN2upMLesW8PoR0xBIe9ax7%2FTBmxcAcL6wly143TZQ1uMZlgblG4ZM30PxE4LeXIodpsiv8O996S%2BVhSf6mKqKjZO8nC9KE1%2BKa4SqddE%2FsgakZ2q4y8kAzjSRYjs%2FcPijXIFIJ%2FJRDRV%2FIuE6rrJBlGQby%2Bn253hKbpn%2FbyysCsgqQf7jJMJLlSnajpQ0oD7pyxRbf9OMCLHdHNPXEpmD9PE7EhJj%2FlbxLzMP9JM8upXCqXarxJdHrZ";
        try {
            String postUrl = "https://demo.filesbox.cn/plugin/yzwo/api.do" ;

            Map<String, Object> param = new HashMap<>();
            String callbackUrl = "https://dev.filesbox.cn/api/disk/yzwo/office";
            param.put("method", 1);
            param.put("params", new YzEditParamsDto("1", "60146", "［定制］定制评估模板 (1).xlsx", filePath, callbackUrl));
            String res1 = docUtil.postYZFile(postUrl + "?jsonParams=" + URLEncoder.encode(JsonUtils.beanToJson(param),"UTF-8"));
            LogUtil.info("yongZhong h5 api return retry : " + res1 + "filePath : " + filePath );
            Map<String, Object> resMap = JsonUtils.jsonToMap(res1);
            if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorCode") && "0".equals(resMap.get("errorCode").toString())){
                String data = resMap.get("result").toString();
                Map<String, Object> urlMap = JsonUtils.jsonToMap(data);
                if (!ObjectUtils.isEmpty(urlMap) && urlMap.containsKey("urls")){
                    String urls = urlMap.get("urls").toString();
                }
                System.out.println(data);
            }else if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorcode") && "0".equals(resMap.get("errorcode").toString())){

            }
            LogUtil.info("yongZhong resMap=" + JsonUtils.beanToJson(resMap));
            System.out.println("yongZhong resMap=" + JsonUtils.beanToJson(resMap));
        } catch (Exception e2){
            LogUtil.error(e2, "转h5提交重试也失败 , o:" + filePath);
        }
    }

    @Test
    public void yzClose(){

        //

        String filePath = "https://demo.filesbox.cn/api/disk/attachment/［定制］定制评估模板 (1).xlsx?busId=60146&busType=cloud&key=yORCT716y2coywUAaUWWMKyi5IUtJuIRhNz%2FJEqyPPFHsf8gQ%2Bz%2BKhq58Z5nfy%2FMZQR4c0BaDN2upMLesW8PoR0xBIe9ax7%2FTBmxcAcL6wly143TZQ1uMZlgblG4ZM30PxE4LeXIodpsiv8O996S%2BVhSf6mKqKjZO8nC9KE1%2BKa4SqddE%2FsgakZ2q4y8kAzjSRYjs%2FcPijXIFIJ%2FJRDRV%2FIuE6rrJBlGQby%2Bn253hKbpn%2FbyysCsgqQf7jJMJLlSnajpQ0oD7pyxRbf9OMCLHdHNPXEpmD9PE7EhJj%2FlbxLzMP9JM8upXCqXarxJdHrZ";
        try {
            String postUrl = "https://demo.filesbox.cn/plugin/yzwo/api.do" ;

            Map<String, Object> param = new HashMap<>();
            String callbackUrl = "https://dev.filesbox.cn/api/disk/yzwo/office";
            param.put("method", 3);
            param.put("params", new YzEditParamsDto(null, "3186", null, null, callbackUrl));
            String paramStr = URLEncoder.encode(JsonUtils.beanToJson(param),"UTF-8");
            String res1 = docUtil.postYZFile(postUrl + "?jsonParams=" + paramStr);
            LogUtil.info("yongZhong h5 api return retry : " + res1 + "filePath : " + filePath );
            Map<String, Object> resMap = JsonUtils.jsonToMap(res1);
            if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorCode") && "0".equals(resMap.get("errorCode").toString())){

            }else if (!ObjectUtils.isEmpty(resMap) && resMap.containsKey("errorcode") && "0".equals(resMap.get("errorcode").toString())){

            }
            LogUtil.info("yongZhong resMap=" + JsonUtils.beanToJson(resMap));
            System.out.println("yongZhong resMap=" + JsonUtils.beanToJson(resMap));
        } catch (Exception e2){
            LogUtil.error(e2, "转h5提交重试也失败 , o:" + filePath);
        }
    }

    @Test
    public void tes2(){

        List<String> soureNamelist = new ArrayList<>();
        /*soureNamelist.add("新");
        soureNamelist.add("新(1)");
        soureNamelist.add("新(2)");
        soureNamelist.add("新(3)");
        soureNamelist.add("新(6)");

        String a = checkRepeatName("新","新", soureNamelist, 1);*/

        soureNamelist.add("新.doc");
        soureNamelist.add("新(1).doc");
        soureNamelist.add("新(2).doc");
        soureNamelist.add("新(3).doc");
        soureNamelist.add("新(6).doc");

        String a = checkRepeatName("新.doc","新.doc", "doc", soureNamelist, 1);
        System.out.println(a);

    }

    private String checkRepeatName(String name, String newName, List<String> soureNamelist, int i){
        if (soureNamelist.contains(newName)){
            return checkRepeatName(name , name + "(" + i +")", soureNamelist, i+1);
        }
        return newName;
    }

    private String checkRepeatName(String name, String newName, String fileType, List<String> sourceNameList, int i){
        if (!CollectionUtils.isEmpty(sourceNameList)){
            if (sourceNameList.contains(newName)){
                return checkRepeatName(name , name.replaceAll("." + fileType, "") + "(" + i +")." + fileType, fileType, sourceNameList, i+1);
            }
        }
        return newName;
    }

    @Test
    public void cretaePPt(){
        FileUtil.writePptFile(new File("E:\\zip测试打包\\999\\newPPT.pptx"), "new ppt");
    }


    @Resource
    CompressFileReader compressFileReader;
    @Resource
    ExplorerFileService explorerFileService;
    @Test
    public void unZipList(){

        //String filePath = "E:\\zip测试打包\\999.zip";
        //String filePath = "E:\\zip测试打包\\解压rar\\测试密码压缩.zip";
        String filePath = "E:\\zip测试打包\\个人空间 (1).zip";
        String fileName = "个人空间 (1).zip";

        try {
            String a = compressFileReader.unRar(filePath, fileName, "");
            System.out.println(a);
        }catch (Exception e){
            LogUtil.error(e, "aaa");
        }


        /*
        {"originName":"999.zip","fileName":"999.zip","parentFileName":"","fullName":null,"size":null,"directory":true,"fileKey":null,"childList":[{"originName":"打包555","fileName":"打包555","parentFileName":"999.zip","fullName":"打包555","size":0,"directory":true,"fileKey":"999.zip","childList":[]},{"originName":"打包666","fileName":"打包666","parentFileName":"999.zip","fullName":"打包666/","size":0,"directory":true,"fileKey":"999.zip","childList":[{"originName":"b38f16408466431aa421a13643ba412f1678167794239_30028.mp3","fileName":"999.zip_b38f16408466431aa421a13643ba412f1678167794239_30028.mp3","parentFileName":"打包666","fullName":"打包666\\b38f16408466431aa421a13643ba412f1678167794239_30028.mp3","size":4543462,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"上传文件夹","fileName":"上传文件夹","parentFileName":"打包666","fullName":"打包666/上传文件夹/","size":0,"directory":true,"fileKey":"999.zip","childList":[{"originName":"1599794872690_2232915.gif","fileName":"999.zip_1599794872690_2232915.gif","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\1599794872690_2232915.gif","size":914096,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"txt打开.txt","fileName":"999.zip_txt打开.txt","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\txt打开.txt","size":34,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"枫叶.JPEG","fileName":"999.zip_枫叶.JPEG","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\枫叶.JPEG","size":47509,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"画01.mp4","fileName":"999.zip_画01.mp4","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\画01.mp4","size":4744602,"directory":false,"fileKey":"999.zip","childList":[]}]},{"originName":"新文件夹","fileName":"新文件夹","parentFileName":"打包666","fullName":"打包666\\新文件夹","size":0,"directory":true,"fileKey":"999.zip","childList":[]}]}]}
         */
    }

    @Test
    public void unZipTest(){

        String filePath = "E:\\zip测试打包\\999.zip";
        //String filePath = "E:\\zip测试打包\\解压rar\\测试密码压缩.zip";
        String fileName = "测试密码压缩.zip";
        String password = "";
        String finalFolderPath = "E:\\zip测试打包\\解压rar\\测试密码压缩\\";
        String finalFolderPath2 = "E:\\zip测试打包\\解压rar\\测试密码压缩\\8da657289ebc43fd8023de49cdc748761683272563095_1.zip_打包666_上传文件夹_1599794872690_2232915.gif";
        List<ChangeSourceVo> fileList = new ArrayList<>();

        CompressFileDto dto =  CompressFileUtil.unzipOneFilePassword(filePath, finalFolderPath2, password, 1);
        System.out.println(password);
        try {
            // CompressFileUtil.unzipFilePassword(filePath, finalFolderPath, fileList, 1L, password);
            System.out.println(JsonUtils.beanToJson(fileList));
        }catch (Exception e){
            LogUtil.error(e, "aaa");
        }


        /*
        {"originName":"999.zip","fileName":"999.zip","parentFileName":"","fullName":null,"size":null,"directory":true,"fileKey":null,"childList":[{"originName":"打包555","fileName":"打包555","parentFileName":"999.zip","fullName":"打包555","size":0,"directory":true,"fileKey":"999.zip","childList":[]},{"originName":"打包666","fileName":"打包666","parentFileName":"999.zip","fullName":"打包666/","size":0,"directory":true,"fileKey":"999.zip","childList":[{"originName":"b38f16408466431aa421a13643ba412f1678167794239_30028.mp3","fileName":"999.zip_b38f16408466431aa421a13643ba412f1678167794239_30028.mp3","parentFileName":"打包666","fullName":"打包666\\b38f16408466431aa421a13643ba412f1678167794239_30028.mp3","size":4543462,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"上传文件夹","fileName":"上传文件夹","parentFileName":"打包666","fullName":"打包666/上传文件夹/","size":0,"directory":true,"fileKey":"999.zip","childList":[{"originName":"1599794872690_2232915.gif","fileName":"999.zip_1599794872690_2232915.gif","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\1599794872690_2232915.gif","size":914096,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"txt打开.txt","fileName":"999.zip_txt打开.txt","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\txt打开.txt","size":34,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"枫叶.JPEG","fileName":"999.zip_枫叶.JPEG","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\枫叶.JPEG","size":47509,"directory":false,"fileKey":"999.zip","childList":[]},{"originName":"画01.mp4","fileName":"999.zip_画01.mp4","parentFileName":"上传文件夹","fullName":"打包666\\上传文件夹\\画01.mp4","size":4744602,"directory":false,"fileKey":"999.zip","childList":[]}]},{"originName":"新文件夹","fileName":"新文件夹","parentFileName":"打包666","fullName":"打包666\\新文件夹","size":0,"directory":true,"fileKey":"999.zip","childList":[]}]}]}
         */
    }
    @Test
    public void compressByOne(){
        String nodeString = "{\"originName\":\"999.zip\",\"fileName\":\"999.zip\",\"parentFileName\":\"\",\"fullName\":null,\"size\":null,\"directory\":true,\"fileKey\":null,\"childList\":[{\"originName\":\"打包555\",\"fileName\":\"打包555\",\"parentFileName\":\"999.zip\",\"fullName\":\"打包555\",\"size\":0,\"directory\":true,\"fileKey\":\"999.zip\",\"childList\":[]},{\"originName\":\"打包666\",\"fileName\":\"打包666\",\"parentFileName\":\"999.zip\",\"fullName\":\"打包666/\",\"size\":0,\"directory\":true,\"fileKey\":\"999.zip\",\"childList\":[{\"originName\":\"b38f16408466431aa421a13643ba412f1678167794239_30028.mp3\",\"fileName\":\"999.zip_b38f16408466431aa421a13643ba412f1678167794239_30028.mp3\",\"parentFileName\":\"打包666\",\"fullName\":\"打包666\\\\b38f16408466431aa421a13643ba412f1678167794239_30028.mp3\",\"size\":4543462,\"directory\":false,\"fileKey\":\"999.zip\",\"childList\":[]},{\"originName\":\"上传文件夹\",\"fileName\":\"上传文件夹\",\"parentFileName\":\"打包666\",\"fullName\":\"打包666/上传文件夹/\",\"size\":0,\"directory\":true,\"fileKey\":\"999.zip\",\"childList\":[{\"originName\":\"1599794872690_2232915.gif\",\"fileName\":\"999.zip_1599794872690_2232915.gif\",\"parentFileName\":\"上传文件夹\",\"fullName\":\"打包666\\\\上传文件夹\\\\1599794872690_2232915.gif\",\"size\":914096,\"directory\":false,\"fileKey\":\"999.zip\",\"childList\":[]},{\"originName\":\"txt打开.txt\",\"fileName\":\"999.zip_txt打开.txt\",\"parentFileName\":\"上传文件夹\",\"fullName\":\"打包666\\\\上传文件夹\\\\txt打开.txt\",\"size\":34,\"directory\":false,\"fileKey\":\"999.zip\",\"childList\":[]},{\"originName\":\"枫叶.JPEG\",\"fileName\":\"999.zip_枫叶.JPEG\",\"parentFileName\":\"上传文件夹\",\"fullName\":\"打包666\\\\上传文件夹\\\\枫叶.JPEG\",\"size\":47509,\"directory\":false,\"fileKey\":\"999.zip\",\"childList\":[]},{\"originName\":\"画01.mp4\",\"fileName\":\"999.zip_画01.mp4\",\"parentFileName\":\"上传文件夹\",\"fullName\":\"打包666\\\\上传文件夹\\\\画01.mp4\",\"size\":4744602,\"directory\":false,\"fileKey\":\"999.zip\",\"childList\":[]}]},{\"originName\":\"新文件夹\",\"fileName\":\"新文件夹\",\"parentFileName\":\"打包666\",\"fullName\":\"打包666\\\\新文件夹\",\"size\":0,\"directory\":true,\"fileKey\":\"999.zip\",\"childList\":[]}]}]}";
        CompressFileReader.FileNode fileNode = JsonUtils.jsonToBean(nodeString, CompressFileReader.FileNode.class);

        String filePath = "E:\\zip测试打包\\999.zip";
        String fileName = "999.zip";
        LoginUser loginUser = new LoginUser();
        loginUser.setUserID(1L);
        String extractPath = "E:\\zip测试打包\\999\\";
        String finalFolderPath = "E:\\zip测试打包\\target\\";
        List fileList = new ArrayList();
        CheckFileDTO checkFileDTO = new CheckFileDTO();
        checkFileDTO.setFullName("打包666/上传文件夹/");
        checkFileDTO.setDirectory(true);
        int i = 0;
        this.compressFileCopy(fileNode.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO, extractPath, i);

        LogUtil.info("fileList=" + JsonUtils.beanToJson(fileList));
    }

    private void compressFileCopyChangeList(List<CompressFileReader.FileNode> fileNodeList, String finalFolderPath, List<ChangeSourceVo> fileList, LoginUser loginUser, CheckFileDTO checkFileDTO
            , String extractPath, int i){
        fileNodeList = fileNodeList.stream().sorted(Comparator.comparing(CompressFileReader.FileNode::isDirectory)).collect(Collectors.toList());

        for (CompressFileReader.FileNode node : fileNodeList) {
            if (node.isDirectory()) {
                fileList.add(new ChangeSourceVo(node.getOriginName().endsWith("/") ? node.getOriginName().substring(0, node.getOriginName().length() - 1) : node.getOriginName(),
                        1, node.getFullName()));
                if (!CollectionUtils.isEmpty(node.getChildList())) {
                    compressFileCopyChangeList(node.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO
                            , extractPath, i);
                }
            } else {
                String suffix = FileUtil.getFileExtension(node.getOriginName());
                String finalFilePath = finalFolderPath + RandomUtil.getuuid() + System.currentTimeMillis()
                        + "_" + i + "_" + loginUser.getUserID() + "." + suffix;

                File linkFile = new File(finalFilePath);
                try {//创建硬链
                    if (!linkFile.exists()) {
                        Files.createLink(Paths.get(finalFilePath), Paths.get(extractPath + node.getFileName()));
                        fileList.add(new ChangeSourceVo(node.getOriginName(), 0, suffix
                                , finalFilePath, node.getFullName().replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO)
                                , node.getSize(), ""));
                    }
                } catch (FileAlreadyExistsException e) {//文件已存在, 忽略
                    LogUtil.error(e, "compressFileCopy 创建硬链接失败 finalFilePath=" + finalFilePath + ", orgPath=" + extractPath + node.getFileName());
                } catch (Exception e) {
                    LogUtil.error(e, "compressFileCopy error  创建硬链接失败 finalFilePath" + finalFilePath + ", orgPath=" + extractPath + node.getFileName());

                }
            }
        }
    }

    private void compressFileCopy(List<CompressFileReader.FileNode> fileNodeList, String finalFolderPath, List<ChangeSourceVo> fileList, LoginUser loginUser, CheckFileDTO checkFileDTO
            , String extractPath, int i){
        if (!CollectionUtils.isEmpty(fileList)){
            return;
        }
        fileNodeList = fileNodeList.stream().sorted(Comparator.comparing(CompressFileReader.FileNode::isDirectory)).collect(Collectors.toList());

        for (CompressFileReader.FileNode node : fileNodeList){
            i ++;
            LogUtil.info(checkFileDTO.getFullName() + "-----compressFileCopy-------" + node.getFullName());
            if (checkFileDTO.getFullName().equals(node.getFullName())){
                if (node.isDirectory()){
                    fileList.add(new ChangeSourceVo(node.getOriginName().endsWith("/") ? node.getOriginName().substring(0,node.getOriginName().length() -1) : node.getOriginName(),
                            1, node.getFullName()));
                    this.compressFileCopyChangeList(node.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO, extractPath, i);
                    return;
                }else {
                    String suffix = FileUtil.getFileExtension(node.getOriginName());
                    String finalFilePath = finalFolderPath + RandomUtil.getuuid() +  System.currentTimeMillis()
                            + "_" + i +  "_" + loginUser.getUserID()+ "." + suffix;

                    File linkFile = new File(finalFilePath);
                    try {//创建硬链
                        if (!linkFile.exists()){
                            Files.createLink(Paths.get(finalFilePath), Paths.get(extractPath + node.getFileName()));
                            fileList.add(new ChangeSourceVo(node.getOriginName(), 0, suffix
                                    , finalFilePath , node.getFullName().replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO)
                                    , node.getSize(), ""));
                        }
                    } catch (FileAlreadyExistsException e){//文件已存在, 忽略
                        LogUtil.error(e, "compressFileCopy 创建硬链接失败 finalFilePath=" + finalFilePath + ", orgPath=" + extractPath + node.getFileName());
                    } catch (Exception e){
                        LogUtil.error(e, "compressFileCopy error  创建硬链接失败 finalFilePath"+ finalFilePath + ", orgPath=" + extractPath + node.getFileName());
                    }
                    return;
                }
            }else if (!CollectionUtils.isEmpty(node.getChildList())){
                compressFileCopy(node.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO
                        , extractPath, i);
            }
        }
    }

    @Test
    public void d(){

        HomeExplorerDTO homeExp = new HomeExplorerDTO();
        homeExp.setSourceID(88608L);
        LoginUser loginUser = new LoginUser();
        loginUser.setUserID(1L);
        loginUser.setUserType(1);

        try {
            Object re = explorerFileService.unzipList(homeExp, loginUser);
            LogUtil.info(JsonUtils.beanToJson(re));
        }catch (Exception e){
            LogUtil.error(e, "error");
        }
    }

    @Resource
    CommonInfoTypeService commonInfoTypeService;
    @Test
    public void a(){

        String path = "/uploads/private/cloud/2023_3/2/03d57b92f0e2446c98ac78e63aefc71e1677750335699_30028.swf";
        String a = path.substring(path.lastIndexOf("/") + 1);
        String b = path.substring(0,path.lastIndexOf("/") + 1);
        String c = path.substring(0,path.lastIndexOf(".") ) + "/";

        String tempIP = c + a.replace(".", "_")+"_cut_%2d.jpg";

        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(tempIP);

        CommonInfoTypeDto infoTypeDto = new CommonInfoTypeDto();
        LoginUser loginUser = new LoginUser();
        List list = commonInfoTypeService.getInfoTypeList(infoTypeDto, loginUser);
        System.out.println(JsonUtils.beanToJson(list));
    }


    @Test
    public void getVideoCutImg(){

        String tempVideoPath = "s01.mp4";
        Double begin = 5.1;
        Double end = 9.0;


        String nameDir = tempVideoPath.substring(tempVideoPath.lastIndexOf("/") + 1);
        String tempPath = tempVideoPath.substring(0,tempVideoPath.lastIndexOf(".") ) + "/";
        // 从视频中截取多张图片
        String tempImgPath = tempPath + nameDir.replace(".", "_")+"_cut_%02d.jpg";

        System.out.println(begin * 24);
        System.out.println(end * 24);
        Integer beginN = (int)Math.ceil(begin * 24);
        Integer endN = (int)Math.ceil(end * 24);

        int num = 1;
        if (endN > beginN) {
            System.out.println((endN - beginN) / 24);
            double n = (endN - beginN);
            num = (int) Math.ceil(n / 24);
        }
        System.out.println("************** num=" + num);
        for (int i = 1; i <= num  ; i ++){
            System.out.println(String.format(tempImgPath, i));
        }

        boolean a = VideoGetUtil.covPicBatch(tempVideoPath, tempImgPath, beginN, endN, 24);
        System.out.println(a);
    }

    @Test
    public void zipNew(){
        String filePath = "E:\\zip测试打包\\压缩zip\\999_new.zip";

        String fileName1 = "E:\\zip测试打包\\压缩zip\\txt打开.txt";
        String fileName2 = "E:\\zip测试打包\\压缩zip\\枫叶.JPEG";

        try {
            ZipFile zipFile = new ZipFile(filePath);

            zipFile.addFolder(new File("E:\\zip测试打包\\压缩zip\\aaa"));
            zipFile.addFolder(new File("E:\\zip测试打包\\压缩zip\\bbb"));
            zipFile.renameFile("bbb/", "aaa/bbb");

            File file = new File(fileName1);
            zipFile.addFile(file);

            zipFile.renameFile("txt打开.txt", "aaa/bbb/txt打开000.txt");

        }catch (Exception e){
            LogUtil.error(e, "");
        }



    }

    @Test
    public void zipNew2(){
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = new LoginUser();
        loginUser.setUserType(1);
        loginUser.setUserID(1L);
        CheckFileDTO updateFileDTO = new CheckFileDTO();
        updateFileDTO.setName("压缩new3.zip");
        updateFileDTO.setSourceID(82110L);
        updateFileDTO.setSourceLevel(",0,");
        updateFileDTO.setTaskID(",0,23c26d29ba124832bd06dccde0a93f98");

        List<SourceOpDto> dataArr = new ArrayList<>();
        SourceOpDto sourceOpDto1 = new SourceOpDto();

        sourceOpDto1.setName("压缩new2");
        sourceOpDto1.setSourceID(58786L);
        sourceOpDto1.setParentID(82110L);
        sourceOpDto1.setType("type");
        sourceOpDto1.setParentLevel(",0,82110,");

        dataArr.add(sourceOpDto1);
        updateFileDTO.setDataArr(dataArr);


        try {
            uploadZipService.zipFile(updateFileDTO, resultMap, loginUser);
        }catch (Exception e){
            LogUtil.error(e, "");
        }

    }

    @Test
    public void te666(){
       /* int[] heightAndWidth;
        String path = "E:\\zip测试打包\\压缩zip\\977b8cddf20646e582e18d4167f928b21684720528068_1.mp4";
        try {
            heightAndWidth = VideoUtil.getHeightAndWidthAndduration(path);
        } catch (Exception e){
            LogUtil.error("获取视频宽高失败, path: " + path + ", e: " + e.getMessage());
            heightAndWidth = new int[]{0, 0, 0};
        }
        System.out.println(heightAndWidth);
        */
        int w = 1920;
        int h = 1080;

        int width = 220;
        int height = 480;

        BigDecimal widthRatio = new BigDecimal(width).divide(new BigDecimal(h),1,BigDecimal.ROUND_UP);
        BigDecimal heightReal = new BigDecimal(height).divide(widthRatio,BigDecimal.ROUND_UP);
        BigDecimal value = new BigDecimal(w).subtract(heightReal).divide(new BigDecimal(2),BigDecimal.ROUND_UP);

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add("/uploads/private/cloud/2023_5/22/977b8cddf20646e582e18d4167f928b21684720528068_1.mp4");
        command.add("-vf");
        //command.add("scale=" + h +":"+heightReal.intValue()+",pad=" + h+":" + w +":0:"+value.intValue()+":black");
        command.add("scale=" + h +":"+heightReal.intValue());
        command.add("-r");
        command.add( "30");
        command.add("-vcodec");
        command.add("h264");
        command.add("-b:v");
        command.add("0");

        command.add("/uploads/private/cloud/2023_5/24/f14e95f76e7042adb2511893ebb79c061684897313404_102.mp4");//文件输出源

        // ffmpeg -i /uploads/private/cloud/2023_5/22/977b8cddf20646e582e18d4167f928b21684720528068_1.mp4 -s 1920x1080 -r 30 /uploads/private/cloud/2023_5/24/f14e95f76e7042adb2511893ebb79c061684897313404_1.mp4
        StringBuilder sb = new StringBuilder();
        for (String a : command){
            sb.append(a);
            sb.append(" ");
        }
        LogUtil.info("convertVideoMerge  cmd:" + sb.toString());

    }

    @Resource
    CommonDesignService commonDesignService;
    @Test
    public void aaa() {

        GetDesignListDTO dto = new GetDesignListDTO();
        dto.setClientType("1");
        dto.setDesignType("2");
        dto.setCommonDesignId(30011L);
        Map<String, Object> map = commonDesignService.getDesignList(dto);
        System.out.println(JsonUtils.beanToJson(map));
    }

    @Test
    public void bbb() {


        String filePath = "E:\\smm\\三维动画设计.xmind";
        String imgPath = "E:\\smm\\思维导图2.png";
        String finalFilePath = "E:\\smm\\思维导图.pdf";
        String text = "{\"root\":{\"data\":{\"text\":\"<p><strong style=\\\"font-size: 24px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; color: rgb(255, 255, 255);\\\">中心主题333</strong></p>\",\"richText\":true,\"expand\":true,\"isActive\":false,\"uid\":\"d9bbcace-3277-4f71-82b4-ba2ac26680a8\"},\"children\":[{\"data\":{\"text\":\"<p><span style=\\\"font-size: 18px; font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; color: rgb(26, 26, 26);\\\">分支主题 133333</span></p>\",\"richText\":true,\"expand\":true,\"isActive\":false,\"uid\":\"6fe19717-9478-4cd0-9fdd-ea669ed6282c\"},\"children\":[]},{\"data\":{\"text\":\"<p><span style=\\\"\\n      color: #1a1a1a;\\n      font-family: 微软雅黑, Microsoft YaHei;\\n      font-size: 18px;\\n      font-weight: noraml;\\n      font-style: normal;\\n      text-decoration: none\\n    \\\">分支主题 2</span></p>\",\"richText\":true,\"expand\":true,\"isActive\":false,\"uid\":\"6244b71b-731a-4797-9192-faa46c635fdd\"},\"children\":[]},{\"data\":{\"text\":\"<p><span style=\\\"\\n      color: #1a1a1a;\\n      font-family: 微软雅黑, Microsoft YaHei;\\n      font-size: 18px;\\n      font-weight: noraml;\\n      font-style: normal;\\n      text-decoration: none\\n    \\\">分支主题 3</span></p>\",\"richText\":true,\"expand\":true,\"isActive\":false,\"uid\":\"536174cd-d639-499c-ad97-229ede809cc6\"},\"children\":[]},{\"data\":{\"text\":\"<p><span style=\\\"\\n      color: #1a1a1a;\\n      font-family: 微软雅黑, Microsoft YaHei;\\n      font-size: 18px;\\n      font-weight: noraml;\\n      font-style: normal;\\n      text-decoration: none\\n    \\\">分支主题 4</span></p>\",\"richText\":true,\"expand\":true,\"isActive\":false,\"uid\":\"15c1925a-9d9b-4743-aef3-14f06d8272fc\"},\"children\":[]}]},\"theme\":{\"template\":\"classic4\",\"config\":{}},\"layout\":\"logicalStructure\",\"config\":{},\"view\":{\"transform\":{\"scaleX\":0.8999999999999999,\"scaleY\":0.8999999999999999,\"shear\":0,\"rotate\":0,\"translateX\":-291.8000000000003,\"translateY\":-48.500000000000426,\"originX\":0,\"originY\":0,\"a\":0.8999999999999999,\"b\":0,\"c\":0,\"d\":0.8999999999999999,\"e\":-291.8000000000003,\"f\":-48.500000000000426},\"state\":{\"scale\":0.8999999999999999,\"x\":-291.8000000000003,\"y\":-48.500000000000426,\"sx\":-291.8000000000003,\"sy\":-48.500000000000426}}}";



        try {
            IWorkbookBuilder workbookBuilder = Core.getWorkbookBuilder();

            FileInputStream stream = new FileInputStream(new File(filePath));
            IWorkbook iWorkbook = workbookBuilder.loadFromStream(stream,".");
            IMarkerSheet sheet = iWorkbook.getMarkerSheet();
            ISheet primarySheet = iWorkbook.getPrimarySheet();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            sheet.save(bos);
            //sheet.getWorkbook().write(bos);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            BufferedImage image = ImageIO.read(bis);
            File file = new File(imgPath);
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            LogUtil.error(e, "读取失败！");
        }
    }

    @Test
    public void bd(){
        try {
            aaaaaaa();
        }catch (SvnlanRuntimeException e){
            Result result = new Result(false, e.getErrorCode(), e.getMessage(), "");
            System.out.println(JsonUtils.beanToJson(result));
        }
    }

    private void aaaaaaa(){
        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{"个人空间"});
    }
}
