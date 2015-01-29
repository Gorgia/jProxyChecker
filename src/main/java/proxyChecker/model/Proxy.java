package proxyChecker.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.net.UnknownHostException;
import java.sql.Date;


@Entity
@IdClass(ProxyId.class)
public class Proxy {


    @Id
    private String ip;
    @Id
    private String port;
    private Long delay;
    private String anonymity;
    private String country;
    private String city;
    private String hostName;
    private Date lastCheck;

    public Proxy() {
    }


    public Proxy(String ip, String port) throws UnknownHostException {
        this.ip = ip;
        this.port = port;
    }

    public Proxy(String proxyString) {
        if (proxyString != null) {
            String[] splittedProxyString = proxyString.split(":");
            if (splittedProxyString.length == 2) {
                this.ip = splittedProxyString[0];
                this.port = splittedProxyString[1];
            }
        }
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

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public String getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(String anonymity) {
        this.anonymity = anonymity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Date getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
    }

    public boolean isActive() {
        return delay > 0 && anonymity != null;
    }

    @JsonIgnore
    public ProxyId getProxyId() {
        return new ProxyId(this.getIp(), this.getPort());
    }

}