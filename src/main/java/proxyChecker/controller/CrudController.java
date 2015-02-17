package proxyChecker.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import proxyChecker.MyProxyRepository;
import proxyChecker.ProxyChecker;
import proxyChecker.logger.InjectLogger;
import proxyChecker.model.Proxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 27/01/15.
 */

@Controller
@RequestMapping("/repo")
public class CrudController {

    @InjectLogger
    private Logger log;

    @Autowired
    private MyProxyRepository repository;

    @Autowired
    private ProxyChecker proxyChecker;


    @RequestMapping(value = "/getall", method = RequestMethod.GET, produces = {"application/json"})
    public
    @ResponseBody
    List<Proxy> getAll() {
        List<Proxy> proxiesInDb;
        proxiesInDb = repository.findAll();
        if (proxiesInDb.size() == 0) {
            log.warn("There are no proxies in DB. please fill it (through webservice if you want).");
        }
        return proxiesInDb;
    }


    @RequestMapping(value = "/getactive", method = RequestMethod.GET, produces = {"application/json"})
    public
    @ResponseBody
    List<Proxy> getAllActive() {
        List<Proxy> proxiesInDb = getAll();
        List<Proxy> activeProxiesInDb = new ArrayList<Proxy>();
        for (Proxy proxy : proxiesInDb) {
            if (proxy.isActive()) {
                activeProxiesInDb.add(proxy);
            }
        }
        return activeProxiesInDb;
    }


    @RequestMapping(value = "/saveproxy", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadInfo() {
        return "You can upload a proxy by posting to this same URL.";
    }

    @RequestMapping(value = "/saveproxy", method = RequestMethod.POST)
    public
    @ResponseBody
    String saveProxy(@RequestParam("proxy") Proxy proxy) {
        if (proxyIsAlreadyInDb(proxy))
            return "Proxy " + proxy.getIp() + ":" + proxy.getPort() + " is already in DB. Thanks a lot anyway";
        if (proxyIsProxy(proxy)) {
            try {
                proxyChecker.checkProxy(proxy);
                repository.save(proxy);
                return "success";
            } catch (Exception e) {
                log.error("Something gone wrong saving proxy", e);
            }
        }
        return "fail to insert proxy: " + proxy.getProxyId().toString() ;
    }

    private boolean proxyIsProxy(Proxy proxy) {
        String proxyString = proxy.getIp();
        String onlyNumbers = proxyString.replace(".", "");
        if (onlyNumbers.contains("[a-zA-Z]+") == false && onlyNumbers.length() > 3) {
            return true;
        }
        return false;
    }

    private boolean proxyIsAlreadyInDb(Proxy proxy) {
        return repository.findOne(proxy.getProxyId()) != null;
    }


}
