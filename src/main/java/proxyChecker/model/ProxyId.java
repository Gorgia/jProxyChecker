package proxyChecker.model;

import java.io.Serializable;

/**
 * Created by andrea on 29/01/15.
 */


public class ProxyId implements Serializable {
    private String ip;
    private String port;

    public ProxyId() {
    }

    public ProxyId(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}