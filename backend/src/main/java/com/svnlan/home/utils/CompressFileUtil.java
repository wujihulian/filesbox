package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.FileHeaderRar;
import com.svnlan.home.dto.CompressFileDto;
import com.svnlan.home.utils.zip.UnBz2Utils;
import com.svnlan.home.utils.zip.UnRarUtils;
import com.svnlan.home.utils.zip.UnTarUtils;
import com.svnlan.home.utils.zip.UnZipUtils;
import com.svnlan.home.vo.ChangeSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.RandomUtil;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/26 13:32
 */
@Component
public class CompressFileUtil {


    /** 解压整个压缩包 */
    public static void compressFileBySource(CommonSource commonSource, String finalFolderPath, List<ChangeSourceVo> fileList, LoginUser loginUser, String password){
        try {

            // 根据fileType解压
            switch (commonSource.getFileType()){
                case "zip":
                    UnZipUtils.decompress(commonSource.getPath(), finalFolderPath, fileList, loginUser.getUserID());
                    break;
                case "tar":
                    UnTarUtils.decompress(commonSource.getPath(), finalFolderPath, fileList, loginUser.getUserID());
                    break;
                case "bz2":
                    UnBz2Utils.decompress(commonSource.getPath(), finalFolderPath, fileList, loginUser.getUserID());
                    break;
                case "rar":
                    UnRarUtils.decompress(commonSource.getPath(), finalFolderPath, fileList, loginUser.getUserID());
                    break;
                case "gzip":
                case "7z":
                case "gz":
                case "iso":
                case "ar":
                case "bz":
                case "xz":
                case "arj":
                default:
                    /** 解压整个文件 */
                    CompressFileDto dto = CompressFileUtil.unzipFilePassword(commonSource.getPath(), finalFolderPath, fileList, loginUser.getUserID(), password
                    , "", false, null);
                    if (ObjectUtils.isEmpty(dto) || !dto.getSuccess()){
                        if (!ObjectUtils.isEmpty(password)){
                            throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
                        }else {
                            throw new SvnlanRuntimeException(CodeMessageEnum.unzipErrorTips.getCode());
                        }
                    }
                    break;
            }

        } catch (Exception e) {
            LogUtil.error(e, "unZip decompress 解压error.");
        }

    }

    /**
       * @Description:
       * @params:  [sourceZipFile 压缩包文件, extractPath 解压路径, password 密码, index ]
       * @Return:  com.svnlan.home.dto.CompressFileDto
       * @Author:  sulijuan
       * @Modified:
       */
    public static CompressFileDto unzipOneFilePassword(String sourceZipFile, String filePath, final String password, int index) {
        CompressFileDto dto = null;
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            randomAccessFile = new RandomAccessFile(sourceZipFile, "r");
            if (!ObjectUtils.isEmpty(password) && password.length() > 0) {
                inArchive = SevenZip.openInArchive(null,
                        new RandomAccessFileInStream(randomAccessFile), password);
            }else {
                inArchive = SevenZip.openInArchive(null,
                        new RandomAccessFileInStream(randomAccessFile));
            }
            ISimpleInArchive  simpleInArchive = inArchive.getSimpleInterface();
            dto = unzipOneFilePassword(simpleInArchive, filePath, password, index);
        } catch (Exception e) {
            dto = new CompressFileDto();
            dto.setSuccess(false);
            dto.setStatus(0);
            e.printStackTrace();
            LogUtil.error(e, " getSimpleInArchiveByPath Exception : " + e);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    LogUtil.error(e, "Error closing archive: " + e);
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    LogUtil.error(e, "Error closing file " );
                    e.printStackTrace();
                }
            }
        }
        return dto;
    }
    public static CompressFileDto unzipOneFilePassword(ISimpleInArchive simpleInArchive, String filePath, final String password, int index) {

        CompressFileDto dto = new CompressFileDto();
        dto.setSuccess(true);
        dto.setStatus(1);
        if (ObjectUtils.isEmpty(simpleInArchive)){
            dto.setSuccess(false);
            dto.setStatus(0);
            return dto;
        }
        try {
            // 存在则返回
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            ISimpleInArchiveItem item = simpleInArchive.getArchiveItem(index);
            String path = getUtf8String(item.getPath());
            if (isMessyCode(path)){
                path = new String(item.getPath().getBytes(StandardCharsets.ISO_8859_1), "GBK");
            }
            final int[] hash = new int[]{0};
            if (!item.isFolder()) {
                ExtractOperationResult result;
                final long[] sizeArray = new long[1];

                //被解压的文件
                OutputStream out = new FileOutputStream(filePath);
                if (!ObjectUtils.isEmpty(password) && password.length() > 0) {
                    //如果文件很大用这个方法可以解压完整，当然文件小用这个也没毛病
                    result = item.extractSlow(new ISequentialOutStream() {
                        @Override
                        public int write(final byte[] data) throws SevenZipException {
                            try {
                                out.write(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            hash[0] |= Arrays.hashCode(data);
                            sizeArray[0] += data.length;
                            return data.length;
                        }
                    }, password);
                }else {

                    //如果文件很大用这个方法可以解压完整，当然文件小用这个也没毛病
                    result = item.extractSlow(new ISequentialOutStream() {
                        @Override
                        public int write(final byte[] data) throws SevenZipException {
                            try {
                                out.write(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            hash[0] |= Arrays.hashCode(data);
                            sizeArray[0] += data.length;
                            return data.length;
                        }
                    });
                }
                out.close();

                if (result == ExtractOperationResult.OK) {
                    LogUtil.info("unzipOneFilePassword success " + String.format("%9X | %10s | %s", //
                            hash[0], sizeArray[0], path));
                } else {
                    dto.setSuccess(false);
                    dto.setStatus(0);
                    dto.setResult(result);
                    LogUtil.info("unzipOneFilePassword Error extracting item: " + result);
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, " unzipOneFilePassword Exception : " + e);
            e.printStackTrace();
            dto.setSuccess(false);
            dto.setStatus(0);
        }
        return dto;
    }

    /**
     * @Description:
     * @params:  [sourceZipFile 压缩包文件, extractPath 解压路径, password 密码 ]
     * @Return:  com.svnlan.home.dto.CompressFileDto
     * @Author:  sulijuan
     * @Modified:
     */
    public static CompressFileDto unzipFilePassword(String sourceZipFile, String unzipPath, List<ChangeSourceVo> fileList, Long userID
            , final String password, String taskID, Boolean isProgress,
                                                    StringRedisTemplate stringRedisTemplate) {

        Map<String, Integer> reMap = new HashMap<>(1);
        CompressFileDto dto = new CompressFileDto();
        dto.setSuccess(true);
        dto.setStatus(1);

        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            //如果解压到的目标文件夹不存在，则新建
            File destinationFolder = new File(unzipPath);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }
            randomAccessFile = new RandomAccessFile(sourceZipFile, "r");
            if (!ObjectUtils.isEmpty(password) && password.length() > 0) {
                inArchive = SevenZip.openInArchive(null,
                        new RandomAccessFileInStream(randomAccessFile), password);
            }else {
                inArchive = SevenZip.openInArchive(null,
                        new RandomAccessFileInStream(randomAccessFile));
            }
            long totalSize = randomAccessFile.length();
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            int i = 0;
            final long[] sizeArray = new long[1];
            final int[] hash = new int[] { 0 };
            for (final ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {

                if (!item.isFolder()) {
                    ExtractOperationResult result;


                    String path = getUtf8String(item.getPath());
                    if (isMessyCode(path)){
                        path = new String(item.getPath().getBytes(StandardCharsets.ISO_8859_1), "GBK");
                    }
                    String suffix = FileUtil.getFileExtension(path);

                    String filePath = path.replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO);
                    String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());

                    //递归检查文件路径，路径上没有文件夹则创建，保证整条路径在本地存在
                    fileProberParent(filePath.replace(fileName, ""), destinationFolder.getPath() + File.separator, suffix, fileList, reMap);


                    //最终文件路径  最终文件目录路径+毫秒+.后缀
                    String finalFilePath = unzipPath + (unzipPath.endsWith("/") ? "" : File.separator)
                            + RandomUtil.getuuid() + System.currentTimeMillis()
                            + "_" + i +  "_" + userID + "." + suffix;
                    byte[] b = new byte[2048];
                    File fileExisting = new File(finalFilePath);
                    fileList.add(new ChangeSourceVo(fileName, 0, suffix, finalFilePath , filePath
                            , item.getSize(), ""));
                    //被解压的文件
                    OutputStream out = new FileOutputStream(fileExisting);
                    if (!ObjectUtils.isEmpty(password) && password.length() > 0) {
                        //如果文件很大用这个方法可以解压完整，当然文件小用这个也没毛病
                        result = item.extractSlow(new ISequentialOutStream() {
                            @Override
                            public int write(final byte[] data) throws SevenZipException {
                                int l = b.length;
                                int length = data.length;
                                int totalPage = (length + l - 1) / l;
                                try {
                                    for (int i = 0; i < totalPage; i ++){
                                        if (i == totalPage -1){
                                            sizeArray[0] += length - (i*l);
                                            out.write(data, i * l, length - (i*l));
                                                // out.write(data);
                                        }else {
                                            sizeArray[0] += l;
                                            out.write(data, i * l, l);
                                        }
                                        // 计算进度
                                        if (isProgress) {
                                            unZipProgressCalculate(taskID, stringRedisTemplate, sizeArray[0], totalSize);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                hash[0] |= Arrays.hashCode(data);

                                return data.length;
                            }
                        }, password);
                    }else {
                        result = item.extractSlow(new ISequentialOutStream() {
                            @Override
                            public int write(final byte[] data) throws SevenZipException {
                                int l = b.length;
                                int length = data.length;
                                int totalPage = (length + l - 1) / l;
                                try {
                                    for (int i = 0; i < totalPage; i ++){
                                        if (i == totalPage -1){
                                            sizeArray[0] += length - (i*l);
                                            out.write(data, i * l, length - (i*l));
                                            // out.write(data);
                                        }else {
                                            sizeArray[0] += l;
                                            out.write(data, i * l, l);
                                        }
                                        // 计算进度
                                        if (isProgress) {
                                            unZipProgressCalculate(taskID, stringRedisTemplate, sizeArray[0], totalSize);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                hash[0] |= Arrays.hashCode(data);
                                return data.length;
                            }
                        });
                    }
                    i ++;
                    out.close();


                    if (result == ExtractOperationResult.OK) {
                        LogUtil.info("unzipOneFilePassword success " + String.format("%9X | %10s | %s", //
                                hash[0], sizeArray[0], path));
                    } else {
                        dto.setSuccess(false);
                        dto.setStatus(0);
                        dto.setResult(result);
                        LogUtil.info("unzipOneFilePassword Error extracting item: " + result);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, " unzipOneFilePassword Exception : " + e);
            e.printStackTrace();
            dto.setSuccess(false);
            dto.setStatus(0);
        }
        if (isProgress){
            stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_unzip_file + taskID, "1", 1, TimeUnit.HOURS);
        }
        return dto;
    }

    public static void unZipProgressCalculate(String taskID, StringRedisTemplate stringRedisTemplate, long count, long total){
        String p = calculateProgress( count, total,1);
        LogUtil.info("unZipProgressCalculate taskID=" + taskID + "，count=" + count + "，total=" + total + "， progress=" + p);
        stringRedisTemplate.opsForValue().set(GlobalConfig.progress_key_unzip_file + taskID, p, 1, TimeUnit.HOURS);

    }

    public static String calculateProgress(long num, long total, int dis){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(dis); //保留几位小数填写几
        //注意需要将long转换为float进行计算
        String percent = numberFormat.format((float) num / (float) total * 100);
        return percent;
    }


    public static void fileProberParent(String parentPath, String destPath, String suffix, List<ChangeSourceVo> fileList, Map<String, Integer> reMap) {
        if (!ObjectUtils.isEmpty(reMap) && reMap.containsKey(parentPath)){
            return;
        }
        List<String> parentList = Arrays.asList(parentPath.split("/")).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(parentList)){
            StringBuilder sb = new StringBuilder();
            for (String parent : parentList){
                if (parent.endsWith(suffix)){
                    continue;
                }
                sb.append(parent + "/");
                if (ObjectUtils.isEmpty(reMap) || !reMap.containsKey(sb.toString())) {
                    fileList.add(new ChangeSourceVo(parent, 1, destPath + sb.toString()));
                }
                reMap.put(sb.toString(), 1);
            }
        }
    }

    public static Boolean checkZipIsEncrypted(String paths) {
        LogUtil.info("checkZipIsEncrypted paths=" + paths);
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;

        boolean isEncrypted = false;
        try {
            randomAccessFile = new RandomAccessFile(paths, "r");

            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));

            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            int i = 0;
            for (final ISimpleInArchiveItem o : simpleInArchive.getArchiveItems()) {
                if (!isEncrypted && !o.isFolder()){
                    if (Boolean.TRUE.equals(inArchive.getProperty(i,PropID.ENCRYPTED))) {
                        isEncrypted = true;
                        break;
                    }
                    break;
                }
                i ++;
            }
        } catch (Exception e) {
            LogUtil.error(e, "checkZipIsEncrypted Error occurs: " + e);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    LogUtil.error(e, "checkZipIsEncrypted Error closing archive: " + e);
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    LogUtil.error(e, "checkZipIsEncrypted Error closing file: " + e);
                }
            }
        }
        return isEncrypted;
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    public static String getUtf8String(String str) {
        if (str != null && str.length() > 0) {
            String needEncodeCode = "ISO-8859-1";
            String neeEncodeCode = "ISO-8859-2";
            String gbkEncodeCode = "GBK";
            try {
                if (Charset.forName(needEncodeCode).newEncoder().canEncode(str)) {
                    str = new String(str.getBytes(needEncodeCode), StandardCharsets.UTF_8);
                }
                if (Charset.forName(neeEncodeCode).newEncoder().canEncode(str)) {
                    str = new String(str.getBytes(neeEncodeCode), StandardCharsets.UTF_8);
                }
                if (Charset.forName(gbkEncodeCode).newEncoder().canEncode(str)) {
                    str = new String(getUTF8BytesFromGBKString(str), StandardCharsets.UTF_8);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return str;
    }
    /**
     * 判断是否是中日韩文字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    public static boolean judge(char c){
        if((c >='0' && c<='9')||(c >='a' && c<='z' ||  c >='A' && c<='Z')){
            return true;
        }
        return false;
    }
    public static boolean isMessyCode(String strName) {
        //去除字符串中的空格 制表符 换行 回车
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("").replaceAll("\\+", "").replaceAll("#", "").replaceAll("&", "");
        //去除字符串中的标点符号
        String temp = after.replaceAll("\\p{P}", "");
        //处理之后转换成字符数组
        char[] ch = temp.trim().toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            //判断是否是数字或者英文字符
            if (!judge(c)) {
                //判断是否是中日韩文
                if (!isChinese(c)) {
                    //如果不是数字或者英文字符也不是中日韩文则表示是乱码返回true
                    return true;
                }
            }
        }
        //表示不是乱码 返回false
        return false;
    }


}
