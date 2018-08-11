package com.cx.item.common.model;

/**
 * 注1：tracker_servers 配置项不能重复属性名，多个 tracker_server 用逗号","隔开
 * 注2：除了tracker_server，其它配置项都是可选的
 * FastDFS文件系统操作工具类的配置信息
 */
public class FastDFSConf {

    // fast环境信息
    private int connect_timeout = 2;
    private int network_timeout = 30;
    private String charset = "UTF-8";
    private String http_anti_steal_token = "no";
    private String http_secret_key = "FastDFS1234567890";

    // 上传文件storage环境信息
    private Integer http_tracker_http_port = 80;
    private String tracker_servers;
    private String group_name = "group1";
    private int storage_port = 23000;
    private int store_path_index = 0;


    public FastDFSConf(String tracker_servers) {
        this.tracker_servers = tracker_servers;
    }

    public FastDFSConf(String tracker_servers, int store_path_index) {
        this.tracker_servers = tracker_servers;
        this.store_path_index = store_path_index;
    }

    public FastDFSConf(String tracker_servers, String group_name, int storage_port, int store_path_index) {
        this.tracker_servers = tracker_servers;
        this.group_name = group_name;
        this.storage_port = storage_port;
        this.store_path_index = store_path_index;
    }

    public FastDFSConf(Integer http_tracker_http_port, String tracker_servers, String group_name, int storage_port, int store_path_index) {
        this.http_tracker_http_port = http_tracker_http_port;
        this.tracker_servers = tracker_servers;
        this.group_name = group_name;
        this.storage_port = storage_port;
        this.store_path_index = store_path_index;
    }

    public FastDFSConf(int connect_timeout, int network_timeout, String charset, String http_anti_steal_token, String http_secret_key, Integer http_tracker_http_port, String tracker_servers, String group_name, int storage_port, int store_path_index) {
        this.connect_timeout = connect_timeout;
        this.network_timeout = network_timeout;
        this.charset = charset;
        this.http_anti_steal_token = http_anti_steal_token;
        this.http_secret_key = http_secret_key;
        this.http_tracker_http_port = http_tracker_http_port;
        this.tracker_servers = tracker_servers;
        this.group_name = group_name;
        this.storage_port = storage_port;
        this.store_path_index = store_path_index;
    }

    public FastDFSConf() {
    }

    public int getConnect_timeout() {
        return connect_timeout;
    }

    public void setConnect_timeout(int connect_timeout) {
        this.connect_timeout = connect_timeout;
    }

    public int getNetwork_timeout() {
        return network_timeout;
    }

    public void setNetwork_timeout(int network_timeout) {
        this.network_timeout = network_timeout;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Integer getHttp_tracker_http_port() {
        return http_tracker_http_port;
    }

    public void setHttp_tracker_http_port(Integer http_tracker_http_port) {
        this.http_tracker_http_port = http_tracker_http_port;
    }

    public String getHttp_anti_steal_token() {
        return http_anti_steal_token;
    }

    public void setHttp_anti_steal_token(String http_anti_steal_token) {
        this.http_anti_steal_token = http_anti_steal_token;
    }

    public String getHttp_secret_key() {
        return http_secret_key;
    }

    public void setHttp_secret_key(String http_secret_key) {
        this.http_secret_key = http_secret_key;
    }

    public String getTracker_servers() {
        return tracker_servers;
    }

    public void setTracker_servers(String tracker_servers) {
        this.tracker_servers = tracker_servers;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getStorage_port() {
        return storage_port;
    }

    public void setStorage_port(int storage_port) {
        this.storage_port = storage_port;
    }

    public int getStore_path_index() {
        return store_path_index;
    }

    public void setStore_path_index(int store_path_index) {
        this.store_path_index = store_path_index;
    }
}
