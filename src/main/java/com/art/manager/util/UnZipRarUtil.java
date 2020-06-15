package com.art.manager.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 解压缩
 *
 * @author yuan
 */
public class UnZipRarUtil {
    private static final Logger logger = LoggerFactory.getLogger(UnZipRarUtil.class);

    public static String unzip(String zipFilePath, String saveFileDir) throws Exception {
        if (!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/")) {
            saveFileDir += File.separator;
        }
        File dir = new File(new String(saveFileDir.getBytes(StandardCharsets.UTF_8)));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String entryFileName = "";
        File file = new File(new String(zipFilePath.getBytes(StandardCharsets.UTF_8)));
        if (file.exists()) {
            InputStream is = null;
            ZipArchiveInputStream zais = null;
            try {
                is = new FileInputStream(file);
                zais = new ZipArchiveInputStream(is,"gbk",true);
                ArchiveEntry archiveEntry = null;
                while ((archiveEntry = zais.getNextEntry()) != null) {
                    // 获取文件名
                    entryFileName = archiveEntry.getName();
                    entryFileName = new String(entryFileName.getBytes(StandardCharsets.UTF_8));
                    // 构造解压出来的文件存放路径
                    String entryFilePath = saveFileDir + entryFileName;
                    OutputStream os = null;
                    try {
                        // 把解压出来的文件写到指定路径
                        File entryFile = new File(entryFilePath);
                        if (entryFileName.endsWith("/")) {
                            entryFile.mkdirs();
                        } else {
                            os = new BufferedOutputStream(new FileOutputStream(
                                    entryFile));
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = zais.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        throw new IOException(e);
                    } finally {
                        if (os != null) {
                            os.flush();
                            os.close();
                        }
                    }

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (zais != null) {
                        zais.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return entryFileName.substring(0, entryFileName.indexOf("/"));

    }

    /**
     * @param sourcePath 需要压缩的文件夹的路径
     * @param saveDir    新生产的压缩包所在的路径
     * @throws Exception
     */
    public static void zip(@NotNull String sourcePath, @NotNull File saveDir) throws Exception {
        if (!saveDir.exists()) {
            saveDir.getParentFile().mkdirs();
        }
        ZipArchiveOutputStream stream = new ZipArchiveOutputStream(saveDir);
        stream.setEncoding("utf-8");
        compress(sourcePath, stream);
        stream.finish();
        stream.close();
    }

    private static void compress(String sourcePath, ZipArchiveOutputStream stream) throws Exception {
        File source = new File(sourcePath);
        File[] files = new File(sourcePath).listFiles();
        if (files == null) {
            System.out.println(sourcePath);
            throw new Exception("需要压缩的文件夹不能为空");
        }
        for (File file : files) {
            String type = file.getPath().substring(file.getPath().lastIndexOf(".") + 1);
            if (file.isFile() && !type.equals("zip")) {
                InputStream in = new FileInputStream(file);
                ZipArchiveEntry entry = new ZipArchiveEntry(file, source.getName() + File.separator + file.getName());
                // 添加一个条目
                stream.putArchiveEntry(entry);
                IOUtils.copy(in, stream);
                // 结束
                stream.closeArchiveEntry();
                in.close();
            } else {
                compress(sourcePath + File.separator + file.getName(), stream);
            }
        }
    }


}
