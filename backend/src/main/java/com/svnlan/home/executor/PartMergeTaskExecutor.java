package com.svnlan.home.executor;

import com.svnlan.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @ClassName: ScheduledTask
 * @Description: 多文件分片上传，分片文件合并写入执行器
 */
@Slf4j
public class PartMergeTaskExecutor implements Callable<Long> {

    private String filePath;

    private String tempPath;

    private String fileName;

    private Integer partNum;

    private List<String> tempPathList;

    public PartMergeTaskExecutor() {
        super();
    }

    public PartMergeTaskExecutor(String filePath, String tempPath, String fileName, Integer partNum) {
        this.filePath = filePath;
        this.tempPath = tempPath;
        this.fileName = fileName;
        this.partNum = partNum;
    }

    public PartMergeTaskExecutor(String filePath, List<String> tempPathList) {
        this.filePath = filePath;
        this.tempPathList = tempPathList;
    }

    @Override
    public Long call() throws Exception {
        LogUtil.info("ThreadMergeFile 当前正在合并的文件是：" + filePath);
        InputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        Long partSizeTotal = 0L;
        try {
            fileOutputStream = new FileOutputStream(filePath);
            for (String path : tempPathList) {
                //读取分片文件
                fileInputStream = new FileInputStream(path);
                byte[] buf = new byte[1024 * 8];//8MB
                int length;
                while ((length = fileInputStream.read(buf)) != -1) {//读取fis文件输入字节流里面的数据
                    fileOutputStream.write(buf, 0, length);//通过fos文件输出字节流写出去
                    partSizeTotal += length;
                }
                fileInputStream.close();
            }
            //todo 这里可以校验一下文件是否合并完成,然后在删除临时分片文件 （多线程合并完成后一并删除）
            /*
            // 删除临时文件夹里面的分片文件 如果使用流操作且没有关闭输入流，可能导致删除失败
            for (int i = 0; i < partNum; i++) {
                boolean delete = new File(tempPath + "\\" + fileName + "_" + i + ".part").delete();
                File file = new File(tempPath + "\\" + fileName + "_" + i + ".part");
//            System.out.println(i + "; 是否删除：" + delete + " ; 是否还存在：" + file.exists());
            }
            //在删除对应的临时文件夹
            File tempDir = new File(tempPath);
            if (Objects.requireNonNull(tempDir.listFiles()).length == 0) {
                boolean delete = tempDir.delete();
                //System.out.println("文件夹: " + tempPath + ";是否删除: " + delete);
            }*/
        } catch (Exception e) {
            log.error("ThreadMergeFile {}:文件分片合并失败!", filePath, e);
            throw new Exception(e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                log.error("ThreadMergeFile {} 文件分片合并完成后,关闭输入输出流错误！", filePath, e);
                e.printStackTrace();
            }
        }
        return partSizeTotal;
    }
    /*
    @Override
    public Integer call() throws Exception {
        LogUtil.info("当前正在合并的文件是：" + filePath);
        InputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        Integer partSizeTotal = 0;
        try {
            fileOutputStream = new FileOutputStream(filePath + fileName);

            for (int i = 0; i < partNum; i++) {
                //读取分片文件
                fileInputStream = new FileInputStream(tempPath + "\\" + fileName + "_" + i + ".part");
                byte[] buf = new byte[1024 * 8];//8MB
                int length;
                while ((length = fileInputStream.read(buf)) != -1) {//读取fis文件输入字节流里面的数据
                    fileOutputStream.write(buf, 0, length);//通过fos文件输出字节流写出去
                    partSizeTotal += length;
                }
                fileInputStream.close();
            }

            //todo 这里可以校验一下文件是否合并完成,然后在删除临时分片文件 （多线程合并完成后一并删除）
            /*
            // 删除临时文件夹里面的分片文件 如果使用流操作且没有关闭输入流，可能导致删除失败
            for (int i = 0; i < partNum; i++) {
                boolean delete = new File(tempPath + "\\" + fileName + "_" + i + ".part").delete();
                File file = new File(tempPath + "\\" + fileName + "_" + i + ".part");
//            System.out.println(i + "; 是否删除：" + delete + " ; 是否还存在：" + file.exists());
            }
            //在删除对应的临时文件夹
            File tempDir = new File(tempPath);
            if (Objects.requireNonNull(tempDir.listFiles()).length == 0) {
                boolean delete = tempDir.delete();
                //System.out.println("文件夹: " + tempPath + ";是否删除: " + delete);
            }* /
        } catch (Exception e) {
            log.error("{}:文件分片合并失败!", fileName, e);
            throw new Exception(e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                log.error("{} 文件分片合并完成后,关闭输入输出流错误！", fileName, e);
                e.printStackTrace();
            }
        }
        return partSizeTotal;
    }*/
}
