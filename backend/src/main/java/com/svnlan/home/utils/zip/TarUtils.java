package com.svnlan.home.utils.zip;

import com.svnlan.home.utils.FileUtil;
import com.svnlan.utils.LogUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/4 13:44
 */
public class TarUtils {

    /**缓冲字节*/
    public static final int BUFFER = 1024;


    /** 压缩目录  KeepDirStructure 一级目录是否保留 nextKeepDirStructure 下级目录是否保留 */
    public static void toZip(String srcDir, String finalFilePath, boolean KeepDirStructure) throws RuntimeException {
        toZip(srcDir, finalFilePath, KeepDirStructure, KeepDirStructure);
    }
    public static void toZip(String srcDir, String finalFilePath, boolean KeepDirStructure, boolean nextKeepDirStructure) throws RuntimeException {
        FileOutputStream out = null;
        long start = System.currentTimeMillis();
        TarArchiveOutputStream zos = null;
        try {
            out = new FileOutputStream(finalFilePath);
            zos = new TarArchiveOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure, nextKeepDirStructure);
            long end = System.currentTimeMillis();
            LogUtil.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            LogUtil.error(e,"tar compress error");
            throw new RuntimeException("tar error from ", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    LogUtil.error(e,"zos error");
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LogUtil.error(e,"zos out error");
                }
            }
        }
    }

    private static boolean isMessyCode( String str )
    {
        for( int i = 0; i < str.length(); i++ )
        {
            char c = str.charAt( i ) ;
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            // 从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            if( (int)c == 0xfffd )
            {
                // 存在乱码
                return true ;
            }
        }
        return false ;
    }

    /** 递归压缩  KeepDirStructure 一级目录是否保留 nextKeepDirStructure 下级目录是否保留*/
    public static void compress(File sourceFile, TarArchiveOutputStream zos, String name, boolean KeepDirStructure)throws Exception{
        compress(sourceFile, zos, name, KeepDirStructure, KeepDirStructure);
    }
    public static void compress(File sourceFile, TarArchiveOutputStream taos, String name, boolean KeepDirStructure, boolean nextKeepDirStructure)
            throws Exception {
        byte[] buf = new byte[BUFFER];
        if (sourceFile.isFile()) {
            TarArchiveEntry tae = new TarArchiveEntry(sourceFile);
            //不加tae.setName，压缩文件里是全路径(filePath有几层，压缩包里有几层)
            String n = FileUtil.getUtf8String(name);
            LogUtil.info("tar getUtf8String name=" + name + "，n="+ n);
            if (isMessyCode(n)){
                n = new String(name.getBytes(StandardCharsets.ISO_8859_1), "GBK");
                LogUtil.info("tar isMessyCode name=" + name + "，n="+ n);
            }
            tae.setName(n);

            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            taos.putArchiveEntry(tae);

            // copy文件到zip输出流中
            int len;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            while ((len = bis.read(buf, 0 , 1024)) != -1) {
                taos.write(buf, 0, len);
            }
            // Complete the entry
            bis.close();
            taos.closeArchiveEntry();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    taos.putArchiveEntry(new TarArchiveEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    taos.closeArchiveEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, taos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, taos, file.getName(), nextKeepDirStructure);
                    }
                }
            }
        }
    }



    public static void main(String[] args) {
        String finalFolderPath = "E:\\zip测试打包\\999\\gz2\\";
        String targetDir = "E:\\zip测试打包\\999\\";

        String fileName = System.currentTimeMillis()
                + "_1" + ".tar";
        String finalFilePath = targetDir + fileName;
        try  {
            toZip(finalFolderPath, finalFilePath, true);
        } catch (Exception e){

        }
    }
}
