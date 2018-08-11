package com.cx.item.common.utils;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件工具类
 * Created by hwm on 2018/6/13.
 */
public class FileUtil {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 上传文件
     * 自动生成日期文件夹，文件和图片类型分开存放
     * 判断文件类型，图片类型自动创建image文件夹，文件类型自动创建file文件夹
     *
     * @param uploadFileName 文件名称
     * @param uploadFilePath 上传路径
     * @param inputStream    上传文件流
     * @return
     */
    public static Map<String, String> uploadFile(String uploadFileName, String uploadFilePath, InputStream inputStream) {

        return up(uploadFileName, uploadFilePath, inputStream);
    }

    private static Map<String, String> up(String uploadFileName, String uploadFilePath, InputStream inputStream) {

        // 文件名
        String fileName = uploadFileName;
        // .blob结尾文件改成.png结尾文件
        if (StrUtil.isEmpty(fileName) || fileName.endsWith("blob")) {
            fileName = "裁剪文件.png";
        }

        StringBuilder uploadPath = new StringBuilder(uploadFilePath);
        // 如果是图片类型
        boolean isImage = isImage(fileName);
        if (isImage) {
            uploadPath.append(File.separator).append("image");
        } else {
            uploadPath.append(File.separator).append("file");
        }

        // 文件存放带日期的路径
        String fileDate = DateUtil.getDateFormat(new Date(), DateUtil.YYYYMMDD);
        uploadPath.append(File.separator).append(fileDate);
        File filepath = new File(uploadPath.toString());
        if (!filepath.exists()) {
            filepath.mkdirs();
        }

        // 保存的文件名uuid
        String fileNameUuid = getFileNamePrefix();
        // 文件格式
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 文件路径(前端使用)
        String saveFilePath = File.separator + fileDate + File.separator + fileNameUuid + "." + fileType;

        // 写入文件
        File targetFile = new File(filepath.getPath() + File.separator + fileNameUuid + "." + fileType);

            // 异步保存和压缩文件
            Runnable runnable = () -> {
                log.debug("===========异步保存和压缩文件=======");

                try {
                    FileUtils.copyInputStreamToFile(inputStream, targetFile);

                    if (ProfileUtil.isLinuxSystem() && isImage) {
                        zipImage(targetFile.getAbsolutePath());
                    }
                } catch (IOException e) {
                    log.error("error=", e);
                }
            };
            runnable.run();



        Map<String, String> resultMap = new HashMap();
        resultMap.put("fileUuid", fileNameUuid);
        resultMap.put("fileType", fileType);
        resultMap.put("fileName", fileName);
        resultMap.put("fileSize", String.valueOf((targetFile.length() / 1024)));
        resultMap.put("filePath", saveFilePath.replace("\\", "/"));
        resultMap.put("fileAbsolutePath", targetFile.getAbsolutePath().replace("\\", "/"));

        return resultMap;
    }

    private static String getFileNamePrefix(){

        // 格式，仿照uuid HHmmssSSS-YYYY-MMdd-xxxx-0c87b06c0d6b 使得文件有序
        StringBuilder filename = new StringBuilder();
        Date date = new Date();

        filename.append(DateUtil.getDateFormat(date, "HHmmssSSS")).append("-");
        filename.append(DateUtil.getDateFormat(date, DateUtil.YYYY)).append("-");
        filename.append(DateUtil.getDateFormat(date, "MMdd")).append("-");
        filename.append(StringUtil.getStringRandom(4)).append("-");
        filename.append(StringUtil.getStringRandom(12));

        return filename.toString();
    }

    /**
     * 压缩图片
     *
     * @param zipFilePath 需要压缩文件的绝对路径
     */
    public static void zipImage(String zipFilePath) {

        /**
         * 注：linux环境使用图片压缩需要安装 ImageMagick
         * 使用方式参考博客：https://www.cnblogs.com/tinywan/p/7060802.html
         * 安装命令：yum install ImageMagick ImageMagick-devel
         * 第一次安装失败可以重新运行命令重装
         * 检查系统是否安装：convert -version
         */
        try {
            // linux环境压缩图片
            log.debug(StrUtil.format("===>linux环境压缩图片"));
            StringBuilder exec = new StringBuilder();
            exec.append("convert -quality 70"); // 70表图片质量，越大质量越高,默认70
            exec.append(" ").append(zipFilePath);
            exec.append(" ").append(zipFilePath);
            RuntimeUtil.exec(exec.toString());
        }catch (Exception e){
            log.debug("======图片压缩失败：服务器没有安装压缩软件ImageMagick,运行命令：yum install ImageMagick ImageMagick-devel 安装（不安装也是没有关系的，只是图片不压缩，o(*￣︶￣*)o）=====");
        }
    }

    /**
     * 通过inputStream 判断图片是否图片类型
     *
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName) {

        String imgType = "BMP.JPG.JPEG.PNG.GIF";

        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (imgType.contains(fileType.toUpperCase())) {
            return true;
        }
        return false;
    }
}
