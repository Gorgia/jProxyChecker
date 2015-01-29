package proxyChecker;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import proxyChecker.logger.InjectLogger;
import proxyChecker.model.Proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.util.List;

/**
 * Created by andrea on 23/01/15.
 */
@Service
public class ProxyChecker {

    String anonCheckerUrl = "http://jnerd.altervista.org/jproxycheck/check.php";
    private String geodata = getClass().getResource("/GeoIP.dat").getFile();
    private String geocity = getClass().getResource("/GeoLiteCity.dat").getFile();
    private int timeout = 3000; //ms

    @InjectLogger
    private Logger log;

    @Autowired
    private MyProxyRepository proxyRepo;

    @Scheduled(fixedDelay=3600000)
    public void checkAll(){
        List<Proxy> proxies = proxyRepo.findAll();
        for (Proxy proxy : proxies){
            checkProxy(proxy);
            proxyRepo.save(proxy);
        }
    }


    public Proxy checkProxy(Proxy proxy) {
        try {
            InetSocketAddress addressInTesting = new InetSocketAddress(proxy.getIp(), Integer.parseInt(proxy.getPort()));
            proxy.setDelay(determineDelay(addressInTesting));
            proxy.setLastCheck(new Date(new java.util.Date().getTime()));
            if (proxy.getDelay() > 0l) {
                proxy.setHostName(getHostName(addressInTesting));
                proxy.setCountry(getCountry(addressInTesting));
                proxy.setCity(getCity(addressInTesting));
                proxy.setAnonymity(getAnonLevel(addressInTesting));
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return proxy;
    }

    public Long determineDelay(InetSocketAddress addressInTesting) throws IOException {
        long t0 = System.currentTimeMillis();
        if (addressInTesting.getAddress().isReachable(timeout)) {
            long t1 = System.currentTimeMillis();
            long delay = t1 - t0;
            return delay;
        }
        return 0l;

    }


    public String getHostName(InetSocketAddress addressInTesting) {
        return addressInTesting.getHostName();

    }


    public String getCountry(InetSocketAddress addressInTesting) throws IOException {
        LookupService cl = new LookupService(geodata);
        String countrycode = cl.getCountry(addressInTesting.getAddress()).getCode();

        cl.close();

        return countrycode;
    }

    public String getCity(InetSocketAddress addressInTesting) throws IOException {
        LookupService cl = new LookupService(geocity, LookupService.GEOIP_MEMORY_CACHE);
        Location l1 = cl.getLocation(addressInTesting.getAddress());
        String city = null;
        if (l1 != null) {
            city = l1.city;
        }
        cl.close();
        return city;

    }


    public String getAnonLevel(InetSocketAddress addressInTesting) throws IOException {
        URL oracle = new URL(anonCheckerUrl);
        java.net.Proxy httpProxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, addressInTesting);
        String response = null;
        try {
            URLConnection urlconn = null;
            urlconn = oracle.openConnection(httpProxy);
            urlconn.connect();
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            response = buffreader.readLine();
        } catch (IOException e) {
            log.debug(addressInTesting.getHostName() + ":" + addressInTesting.getPort() + " is reachable but cannot proxy");
        }
        return response;
    }

}
