package com.cx.item.common.utils;

import cn.hutool.core.util.StrUtil;
import com.cx.item.common.model.FastDFSConf;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * FastDFS文件系统操作工具类
 */
public class FastDFSUtil {

    private static Logger log = LoggerFactory.getLogger(FastDFSUtil.class);

    /**
     * 上传文件到FastDFS
     *
     * @param fastDFSConf
     * @param bytes
     */
    public static Map<String, String> fastDFSUpload(FastDFSConf fastDFSConf, byte[] bytes, String fileName) throws IOException, MyException {

        String ext_Name = "无名文件";
        String file_Name = "blob";
        try {
            ext_Name = fileName.split("\\.")[1];
            file_Name = fileName.split("\\.")[0];
        } catch (Exception e) {
            log.error("fastDFSUpload文件名有误fileName=" + fileName, e);
        }

        Map<String, String> stringMap = uploadFile(fastDFSConf, bytes, ext_Name, file_Name);
        return stringMap;
    }

    /**
     * FastDFS实现文件下载
     *
     * @param filePath
     */
    public static byte[] fastDFSDownload(FastDFSConf fastDFSConf, String filePath) throws IOException, MyException {

        StorageClient storageClient = getStorageClient(fastDFSConf);

        byte[] b = storageClient.download_file(fastDFSConf.getGroup_name(), filePath);
        if (b == null) {
            throw new IOException("文件" + filePath + "不存在");
        }

        return b;
    }

    /**
     * FastDFS获取上传文件信息
     */
    public static FileInfo fastDFSGetFileInfo(FastDFSConf fastDFSConf, String filePath) throws IOException, MyException {

        // 初始化文件资源
        StorageClient storageClient = getStorageClient(fastDFSConf);

        FileInfo fi = storageClient.get_file_info(fastDFSConf.getGroup_name(), filePath);
        if (fi == null) {
            throw new IOException("文件" + filePath + "不存在");
        }

        log.debug(StrUtil.format("-----------文件信息={}-----------", fi.toString()));
        return fi;
    }

    /**
     * FastDFS获取文件名称
     */
    public static String fastDFSGetFileName(FastDFSConf fastDFSConf, String filePath) throws IOException, MyException {

        // 初始化文件资源
        StorageClient storageClient = getStorageClient(fastDFSConf);

        NameValuePair[] nvps = storageClient.get_metadata(fastDFSConf.getGroup_name(), filePath);
        if (nvps == null) {
            throw new IOException("文件" + filePath + "不存在");
        }

        String fileName = nvps[0].getName() + "." + nvps[0].getValue();
        log.debug(StrUtil.format("-----------文件名称={}-----------", fileName));
        return fileName;
    }

    /**
     * FastDFS实现删除文件
     */
    public static int fastDFSDelete(FastDFSConf fastDFSConf, String filePath) throws IOException, MyException {

        // 初始化文件资源
        StorageClient storageClient = getStorageClient(fastDFSConf);

        int i = storageClient.delete_file(fastDFSConf.getGroup_name(), filePath);
        log.debug(i == 0 ? "删除成功" : "删除失败:" + i);

        return i;
    }

    private static Map<String, String> uploadFile(FastDFSConf fastDFSConf, byte[] byteFile, String ext_file, String file_Name) throws IOException, MyException {

        // 初始化文件资源
        StorageClient storageClient = getStorageClient(fastDFSConf);

        // 文件名称，此处不设置
        // NameValuePair[] nvps = new NameValuePair[1];
        // nvps[0] = new NameValuePair(file_Name, ext_file);

        // 利用字节流上传文件
        String[] strings = storageClient.upload_file(byteFile, ext_file, null);

        // 拼接的文件路径
        String filePath = StrUtil.join("/", strings);
        log.debug(StrUtil.format("-----------文件上传路径={}-----------", filePath));

        int begin = filePath.lastIndexOf("/") + 1;
        int end = filePath.lastIndexOf(".");
        String fileNameUuid = StrUtil.sub(filePath, begin, end);

        Map<String, String> resultMap = new HashMap();
        resultMap.put("fileUuid", fileNameUuid);
        resultMap.put("fileType", ext_file);
        resultMap.put("fileName", file_Name + "." + ext_file);
        resultMap.put("fileSize", String.valueOf(byteFile.length / 1024));
        resultMap.put("filePath", "/" + filePath.replace("\\", "/"));
        resultMap.put("fileAbsolutePath", "");

        return resultMap;
    }


    private static StorageClient getStorageClient(FastDFSConf fastDFSConf) throws IOException, MyException {

        // 加载 Properties 对象配置：
        Properties props = new Properties();
        props.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, fastDFSConf.getConnect_timeout());
        props.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, fastDFSConf.getNetwork_timeout());
        props.put(ClientGlobal.PROP_KEY_CHARSET, fastDFSConf.getCharset());
        props.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, fastDFSConf.getHttp_anti_steal_token());
        props.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, fastDFSConf.getHttp_secret_key());
        props.put(ClientGlobal.PROP_KEY_HTTP_TRACKER_HTTP_PORT, fastDFSConf.getHttp_tracker_http_port());
        props.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, fastDFSConf.getTracker_servers());
        ClientGlobal.initByProperties(props);

        // 链接FastDFS服务器，创建tracker和Stroage
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();

        String storageServerIp = getStorageServerIp(trackerClient, trackerServer, fastDFSConf.getGroup_name());
        StorageServer storageServer = getStorageServer(storageServerIp, fastDFSConf.getStorage_port(), fastDFSConf.getStore_path_index());
        return new StorageClient(trackerServer, storageServer);
    }

    /**
     * 得到Storage服务
     *
     * @param storageIp
     * @param storage_port
     * @param store_path_index
     * @return
     */
    private static StorageServer getStorageServer(String storageIp, int storage_port, int store_path_index) throws IOException {
        StorageServer storageServer = null;
        if (storageIp != null && !("").equals(storageIp)) {
            // 参数：ip port store_path下标
            storageServer = new StorageServer(storageIp, storage_port, store_path_index);
        }
        log.debug(StrUtil.format("-----------storage server生成storage_port={}, store_path_index={}-----------", storage_port, store_path_index));
        return storageServer;
    }

    /**
     * 获得可用的storage IP
     *
     * @param trackerClient
     * @param trackerServer
     * @param group_name
     * @return
     */
    private static String getStorageServerIp(TrackerClient trackerClient, TrackerServer trackerServer, String group_name) throws IOException {
        String storageIp = null;
        if (trackerClient != null && trackerServer != null) {
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer, group_name);
            storageIp = storageServer.getSocket().getInetAddress().getHostAddress();
        }
        log.debug(StrUtil.format("-----------获取组中可用的storage IP={}, group_name={}-----------", storageIp, group_name));
        return storageIp;
    }
}
