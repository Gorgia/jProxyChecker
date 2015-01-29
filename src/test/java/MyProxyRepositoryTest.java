import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import proxyChecker.Application;
import proxyChecker.MyProxyRepository;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by andrea on 23/01/15.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@EnableJpaRepositories
public class MyProxyRepositoryTest {

    @Autowired
    MyProxyRepository myProxyRepository;

    @Test
    public void repositoryTest() {
        assertNotNull(myProxyRepository);
    }

}