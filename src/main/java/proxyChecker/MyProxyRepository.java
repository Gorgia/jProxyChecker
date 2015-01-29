package proxyChecker;




import org.springframework.data.repository.CrudRepository;
import proxyChecker.model.Proxy;
import proxyChecker.model.ProxyId;

import java.util.List;

/**
 * Created by andrea on 23/01/15.
 */

public interface MyProxyRepository extends CrudRepository<Proxy, ProxyId> {
    Proxy findOne(ProxyId proxyId);
    List<Proxy> findAll();
}