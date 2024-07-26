package com.svnlan.home.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.svnlan.common.GlobalConfig;
import com.svnlan.home.domain.FileHeaderRar;
import com.svnlan.home.utils.zip.FileType;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.CollationKey;
import java.text.Collator;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author
 * create
 */
@Component
public class CompressFileReader {

    private static final Pattern pattern = Pattern.compile("^\\d+");
    private final ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final String fileDir = ConfigUtils.getHomePath() + File.separator + "file" + File.separator;

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

    public String getUtf8String(String str) {
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
    private static boolean isChinese(char c) {
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

    public String unRar(String filePath, String fileKey, String baseUrl) {
        LogUtil.info("unRar filePath=" + filePath);
        Map<String, FileNode> appender = new HashMap<>();
        //List<String> imgUrls = new ArrayList<>();
        try {
            Boolean isEncrypted = false;
            List<FileHeaderRar> items = new ArrayList<>();
            isEncrypted = getRar4Paths(filePath, items);

            LogUtil.info("unRar isEncrypted=" + isEncrypted + "，items=" + JsonUtils.beanToJson(items));
            //items = getRar4Paths2(filePath);
            if (!CollectionUtils.isEmpty(items)){
                items = items.stream().sorted(Comparator.comparing(FileHeaderRar::getFileNameW))
                        .collect(Collectors.toList());
            }
            String archiveFileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
            //List<Map<String, FileHeaderRar>> headersToBeExtract = new ArrayList<>();
            // 根目录
            for (FileHeaderRar header : items) {
                String fullName = header.getFileNameW().replaceAll(GlobalConfig.separator,"/");
                String originName = getLastFileName(fullName);
                String childName = originName;
                boolean directory = header.getDirectory();
                //FileType type = null;
                if (!directory) {
                    childName = archiveFileName + "_" + fullName.replaceAll(GlobalConfig.separator,"_").replaceAll("/", "_");
                    //headersToBeExtract.add(Collections.singletonMap(childName, header));
                   // type = FileType.typeFromUrl(childName);
                }else {
                   // type = FileType.FOLDER;
                }
                String parentName = getLast2FileName(fullName, archiveFileName);
                /*if (type.equals(FileType.PICTURE)) {
                    imgUrls.add(baseUrl + childName);
                }*/
                FileNode node =
                        new FileNode(originName, childName, parentName, new ArrayList<>(), directory, fileKey, fullName, header.getSize(), header.getEncrypted(), header.getIndex());
                addNodes(appender, parentName, node, archiveFileName, fullName, fileKey, isEncrypted, 0);
                appender.put(childName, node);
            }
            LogUtil.info("unRar appender=" + JsonUtils.beanToJson(appender));
             //executors.submit(new RarExtractorWorker(headersToBeExtract, filePath));
            return new ObjectMapper().writeValueAsString(appender.get(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean getRar4Paths(String paths, List<FileHeaderRar> itemPath) {
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;

        boolean isEncrypted = false;
        try {
            randomAccessFile = new RandomAccessFile(paths, "r");
            LogUtil.info("unRar getRar4Paths randomAccessFile" );
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
            LogUtil.info("unRar getRar4Paths openInArchive");
            String firstPath = FileUtil.getFirstStorageDevicePath(paths);
            //String folderName = paths.substring(paths.lastIndexOf(File.separator) + 1);
            String extractPath = paths.substring(0, paths.lastIndexOf(".") ) + GlobalConfig.separatorTO;
            extractPath = extractPath.replace(firstPath + "/private", firstPath + "/common/down_temp");

            LogUtil.info("unRar getRar4Paths extractPath=" + extractPath);
            File extractPathFile = new File(extractPath);
            if (!extractPathFile.exists()) {
                extractPathFile.mkdirs();
            }

            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            int i = 0;
            for (final ISimpleInArchiveItem o : simpleInArchive.getArchiveItems()) {
                try {
                    String path = getUtf8String(o.getPath());
                    if (isMessyCode(path)){
                        path = new String(o.getPath().getBytes(StandardCharsets.ISO_8859_1), "GBK");
                    }
                    LogUtil.info("unRar getRar4Paths path=" + path);
                    if (!isEncrypted && !o.isFolder()){
                        if (Boolean.TRUE.equals(inArchive.getProperty(i,PropID.ENCRYPTED))) {
                            LogUtil.info("unRar getRar4Paths isEncrypted is true");
                            isEncrypted = true;
                            //break;
                        }
                    }
                    itemPath.add(new FileHeaderRar(path, o.isFolder(), o.getSize(), o.getItemIndex(), isEncrypted));
                    i ++;
                } catch (SevenZipException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            LogUtil.error(e, "unRar getRar4Paths Error occurs: " + e);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    LogUtil.error(e, "unRar getRar4Paths Error closing archive: " + e);
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    LogUtil.error(e, "unRar getRar4Paths Error closing file: " + e);
                }
            }
        }
        return isEncrypted;
    }

    public List<FileHeaderRar> getRar4Paths2(String paths) {
        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        List<FileHeaderRar> itemPath = null;
        try {
            randomAccessFile = new RandomAccessFile(paths, "r");

            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));
            Object a = inArchive.getProperty(0,PropID.ENCRYPTED);
            LogUtil.info("是否需要密码" + a );
            String folderName = paths.substring(paths.lastIndexOf(File.separator) + 1);
            String extractPath = paths.substring(0, paths.lastIndexOf(".") ) + GlobalConfig.separatorTO;
            String firstPath = FileUtil.getFirstStorageDevicePath(paths);
            extractPath = extractPath.replace(firstPath + "/private", firstPath + "/common/down_temp");
            inArchive.extract(null, false, new ExtractCallback(inArchive, extractPath, folderName + "_"));
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            itemPath = Arrays.stream(simpleInArchive.getArchiveItems()).map(o -> {
                                        try {
                                            String path = getUtf8String(o.getPath());
                                            if (isMessyCode(path)){
                                                path = new String(o.getPath().getBytes(StandardCharsets.ISO_8859_1), "GBK");
                                            }
                                            return new FileHeaderRar(path, o.isFolder(), o.getSize());
                                        } catch (SevenZipException e) {
                                            e.printStackTrace();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    })
                            .collect(Collectors.toList())
                            .stream()
                            .sorted(Comparator.comparing(FileHeaderRar::getFileNameW))
                            .collect(Collectors.toList());
        } catch (Exception e) {
            LogUtil.error(e, "getRar4Paths Error occurs: " + e);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    LogUtil.error(e, "getRar4Paths Error closing archive: " + e);
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    LogUtil.error(e, "getRar4Paths Error closing file: " + e);
                }
            }
        }
        return itemPath;
    }

    /** 原始压缩包名称 archiveFileName */
    private void addNodes(Map<String, FileNode> appender, String parentName, FileNode node, String archiveFileName
    , String fullName, String fileKey, Boolean isEncrypted, Integer index) {
        if (appender.containsKey(parentName)) {
            appender.get(parentName).getChildList().add(node);
            appender.get(parentName).getChildList().sort(sortComparator);
        } else {
            if (!ObjectUtils.isEmpty(appender)){
                fileProberParent(appender, parentName, node, archiveFileName
                        , fullName, fileKey, isEncrypted, index);
            }else {
                // 根节点
                if (archiveFileName.equals(parentName)) {
                    FileNode nodeRoot = new FileNode(parentName, parentName, "", new ArrayList<>(), true, isEncrypted, index);
                    nodeRoot.getChildList().add(node);
                    appender.put("", nodeRoot);
                    appender.put(parentName, nodeRoot);
                }else {
                    FileNode nodeRoot = new FileNode(archiveFileName, archiveFileName, "", new ArrayList<>(), true, isEncrypted, index);
                    fileProberParentRoot(appender, parentName, node, archiveFileName
                            , fullName, fileKey, isEncrypted, index, nodeRoot);
                }
            }
        }
    }

    private static void fileProberParentRoot(Map<String, FileNode> appender, String parentName, FileNode node, String archiveFileName
            , String fullName, String fileKey, Boolean isEncrypted, Integer index, FileNode nodeRoot) {

        List<String> parentList = Arrays.asList(fullName.split(GlobalConfig.separator)).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
        if (fullName.indexOf("/") >= 0){
            parentList = Arrays.asList(fullName.split(GlobalConfig.separatorTO)).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(parentList)){
            LogUtil.info("fileProberParent archiveFileName="+archiveFileName+"  appender=" + JsonUtils.beanToJson(appender));
            LogUtil.info("fileProberParent fullName="+fullName+"  parentList=" + JsonUtils.beanToJson(parentList));

            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (String parent : parentList) {
                sb.append(parent + "/");
                if (parent.endsWith(FileType.suffixFromFileName(node.getOriginName()))) {
                    appender.get(parentName).getChildList().add(node);
                    appender.get(parentName).getChildList().sort(sortComparator);
                    continue;
                }
                if (appender.containsKey(parent)) {
                    i++;
                    continue;
                }
                FileNode node1 =
                        new FileNode(parent, parent, i == 0 ? archiveFileName : parentList.get(i - 1), new ArrayList<>(), true, fileKey, sb.toString(), 0L, isEncrypted, index);
                if (i == 0){
                    nodeRoot.getChildList().add(node1);
                    appender.put("", nodeRoot);
                    appender.put(archiveFileName, nodeRoot);
                }
                appender.put(parent, node1);
                if (i > 0) {
                    appender.get(node1.getParentFileName()).getChildList().add(node1);
                    appender.get(node1.getParentFileName()).getChildList().sort(sortComparator);
                }
                i++;
            }
        }
    }
    private static void fileProberParent(Map<String, FileNode> appender, String parentName, FileNode node, String archiveFileName
            , String fullName, String fileKey, Boolean isEncrypted, Integer index) {

        List<String> parentList = Arrays.asList(fullName.split(GlobalConfig.separator)).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
        if (fullName.indexOf("/") >= 0){
            parentList = Arrays.asList(fullName.split(GlobalConfig.separatorTO)).stream().filter(n -> !ObjectUtils.isEmpty(n)).map(String::valueOf).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(parentList)){

            LogUtil.info("fileProberParent archiveFileName="+archiveFileName+"  appender=" + JsonUtils.beanToJson(appender));
            LogUtil.info("fileProberParent fullName="+fullName+"  parentList=" + JsonUtils.beanToJson(parentList));
            if (parentList.size() == 1 && node.directory){
                appender.get("").getChildList().add(node);
                appender.get("").getChildList().sort(sortComparator);
            }else {
                int i = 0;
                StringBuilder sb = new StringBuilder();
                for (String parent : parentList) {
                    sb.append(parent + "/");
                    if (parent.endsWith(FileType.suffixFromFileName(node.getOriginName()))) {
                        appender.get(parentName).getChildList().add(node);
                        appender.get(parentName).getChildList().sort(sortComparator);
                        continue;
                    }
                    if (appender.containsKey(parent)) {
                        i++;
                        continue;
                    }
                    FileNode node1 =
                            new FileNode(parent, parent, i == 0 ? archiveFileName : parentList.get(i - 1), new ArrayList<>(), true, fileKey, sb.toString(), 0L, isEncrypted, index);
                    appender.put(parent, node1);
                    appender.get(node1.getParentFileName()).getChildList().add(node1);
                    appender.get(node1.getParentFileName()).getChildList().sort(sortComparator);
                    i++;
                }
            }
        }
    }


    private static String getLast2FileName(String fullName, String rootName) {
        String split = File.separator;

        if (fullName.indexOf(File.separator) < 0 && fullName.indexOf(GlobalConfig.separatorTO) >= 0) {
            split = GlobalConfig.separatorTO;
        }
        if (fullName.endsWith(split)) {
            fullName = fullName.substring(0, fullName.length() - 1);
        }
        // 1.获取剩余部分
        int endIndex = fullName.lastIndexOf(split);
        String leftPath = fullName.substring(0, endIndex == -1 ? 0 : endIndex);
        if (leftPath.length() > 1) {
            // 2.获取倒数第二个
            return getLastFileName(leftPath);
        } else {
            return rootName;
        }
    }

    private static String getLastFileName(String fullName) {
        String split = File.separator;

        if (fullName.indexOf(File.separator) < 0 && fullName.indexOf(GlobalConfig.separatorTO) >= 0) {
            split = GlobalConfig.separatorTO;
        }
        if (fullName.endsWith(split)) {
            fullName = fullName.substring(0, fullName.length() - 1);
        }
        String newName = fullName;
        if (fullName.contains(split)) {
            newName = fullName.substring(fullName.lastIndexOf(split) + 1);
        }
        return newName;
    }

    public static Comparator<FileNode> sortComparator = new Comparator<FileNode>() {
        final Collator cmp = Collator.getInstance(Locale.US);

        @Override
        public int compare(FileNode o1, FileNode o2) {
            // 判断两个对比对象是否是开头包含数字，如果包含数字则获取数字并按数字真正大小进行排序
            BigDecimal num1, num2;
            if (null != (num1 = isStartNumber(o1))
                    && null != (num2 = isStartNumber(o2))) {
                return num1.subtract(num2).intValue();
            }
            CollationKey c1 = cmp.getCollationKey(o1.getOriginName());
            CollationKey c2 = cmp.getCollationKey(o2.getOriginName());
            return cmp.compare(c1.getSourceString(), c2.getSourceString());
        }
    };

    private static BigDecimal isStartNumber(FileNode src) {
        Matcher matcher = pattern.matcher(src.getOriginName());
        if (matcher.find()) {
            return new BigDecimal(matcher.group());
        }
        return null;
    }

    public static class FileNode {

        private String originName;
        private String fileName;
        private String parentFileName;
        private String fullName;
        private Long size;
        private boolean directory;
        //用于图片预览时寻址
        private String fileKey;
        private List<FileNode> childList;
        private Boolean encrypted;
        private Integer index;

        public FileNode(){}

        public FileNode(String originName, String fileName, String parentFileName, List<FileNode> childList, boolean directory, Boolean encrypted, Integer index) {
            this.originName = originName;
            this.fileName = fileName;
            this.parentFileName = parentFileName;
            this.childList = childList;
            this.directory = directory;
            this.encrypted = encrypted;
            this.index = index;
        }

        public FileNode(String originName, String fileName, String parentFileName, List<FileNode> childList, boolean directory, String fileKey
                , String fullName, Long size, Boolean encrypted, Integer index) {
            this.originName = originName;
            this.fileName = fileName;
            this.parentFileName = parentFileName;
            this.childList = childList;
            this.directory = directory;
            this.fileKey = fileKey;
            this.fullName = fullName;
            this.size = size;
            this.encrypted = encrypted;
            this.index = index;
        }

        public Boolean getEncrypted() {
            return encrypted;
        }

        public void setEncrypted(Boolean encrypted) {
            this.encrypted = encrypted;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getFileKey() {
            return fileKey;
        }

        public void setFileKey(String fileKey) {
            this.fileKey = fileKey;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getParentFileName() {
            return parentFileName;
        }

        public void setParentFileName(String parentFileName) {
            this.parentFileName = parentFileName;
        }

        public List<FileNode> getChildList() {
            return childList;
        }

        public void setChildList(List<FileNode> childList) {
            this.childList = childList;
        }

        @Override
        public String toString() {
            try {
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "";
            }
        }

        public String getOriginName() {
            return originName;
        }

        public void setOriginName(String originName) {
            this.originName = originName;
        }

        public boolean isDirectory() {
            return directory;
        }

        public void setDirectory(boolean directory) {
            this.directory = directory;
        }
    }

    class RarExtractorWorker implements Runnable {
        private final List<Map<String, FileHeader>> headersToBeExtracted;

        private final List<Map<String, FileHeaderRar>> headersToBeExtract;

        private final Archive archive;
        /**
         * 用以删除源文件
         */
        private final String filePath;

        public RarExtractorWorker(
                List<Map<String, FileHeader>> headersToBeExtracted, Archive archive, String filePath) {
            this.headersToBeExtracted = headersToBeExtracted;
            this.archive = archive;
            this.filePath = filePath;
            headersToBeExtract = null;
        }

        public RarExtractorWorker(
                List<Map<String, FileHeaderRar>> headersToBeExtract, String filePath) {
            this.headersToBeExtract = headersToBeExtract;
            this.filePath = filePath;
            archive = null;
            headersToBeExtracted = null;
        }

        @Override
        public void run() {
            for (Map<String, FileHeader> entryMap : headersToBeExtracted) {
                String childName = entryMap.keySet().iterator().next();
                extractRarFile(childName, entryMap.values().iterator().next(), archive);
            }
            try {
                archive.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //KkFileUtils.deleteFileByPath(filePath);
        }

        private void extractRarFile(String childName, FileHeader header, Archive archive) {
            String outPath = fileDir + childName;
            try (OutputStream ot = new FileOutputStream(outPath)) {
                archive.extractFile(header, ot);
            } catch (IOException | RarException e) {
                e.printStackTrace();
            }
        }
    }




    private static class ExtractCallback implements IArchiveExtractCallback {
        private final IInArchive inArchive;

        private final String extractPath;
        private final String folderName;

        public ExtractCallback(IInArchive inArchive, String extractPath, String folderName) {
            this.inArchive = inArchive;
            if (!extractPath.endsWith("/") && !extractPath.endsWith("\\")) {
                extractPath += File.separator;
            }
            this.extractPath = extractPath;
            this.folderName = folderName;
        }

        @Override
        public void setTotal(long total) {

        }

        @Override
        public void setCompleted(long complete) {

        }

        @Override
        public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
            String filePath = inArchive.getStringProperty(index, PropID.PATH);
            String real = folderName + filePath.replaceAll(GlobalConfig.separator,"_").replaceAll("/", "_");

            LogUtil.info("getStream-----------------filePath=" + (filePath));
            LogUtil.info("getStream-----------------extractPath=" + (extractPath));
            LogUtil.info("getStream-----------------real=" + (real));
            File f = new File(extractPath + real);
            f.delete();
            return data -> {
                FileOutputStream fos = null;
                try {
                    File path = new File(extractPath + real);
                    if (!path.getParentFile().exists()) {
                        path.getParentFile().mkdirs();
                    }
                    if (!path.exists()) {
                        path.createNewFile();
                    }
                    fos = new FileOutputStream(path, true);
                    fos.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return data.length;
            };
        }

        @Override
        public void prepareOperation(ExtractAskMode extractAskMode) {

        }

        @Override
        public void setOperationResult(ExtractOperationResult extractOperationResult) {
        }
    }

    public static void extractRAR(String rarFilePath, String destDirectory) throws Exception {
        FileInputStream rarFile = new FileInputStream(rarFilePath);
        Archive archive = new Archive(rarFile);

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        FileHeader fileHeader = archive.nextFileHeader();
        while (fileHeader != null) {
            String fileName = fileHeader.getFileNameString().trim();
            if (fileHeader.isDirectory()) {
                File dir = new File(destDirectory + File.separator + fileName);
                dir.mkdirs();
            } else {
                String destFilePath = destDirectory + File.separator + fileName;
                File destFile = new File(destFilePath);
                destFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(destFile);
                archive.extractFile(fileHeader, fos);
                fos.close();
            }
            fileHeader = archive.nextFileHeader();
        }
        archive.close();
    }
}
