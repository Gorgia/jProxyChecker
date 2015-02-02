package proxyChecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import proxyChecker.model.Proxy;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@ComponentScan
@EnableAutoConfiguration
@EnableJpaRepositories
@EnableWebMvc
@EnableAsync
@EnableScheduling
public class Application extends WebMvcConfigurerAdapter {

    static ConfigurableApplicationContext context;
    static MyProxyRepository repository;

    public static void main(String[] args) throws IOException {
        context = SpringApplication.run(Application.class, args);
        repository = context.getBean(MyProxyRepository.class);
        ProxyChecker proxyChecker = context.getBean(ProxyChecker.class);
        List<Proxy> proxies = repository.findAll();
        if (proxies.size() < 300) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            URL url = classloader.getResource("proxies.txt");
            if (url != null) {
                testProxiesFromFile(url.getPath());//testProxies(Arrays.asList(proxyList));
            }
        }
    }



    public static void testProxiesFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> proxyStrings = Files.readAllLines(path, StandardCharsets.UTF_8);
        ProxyChecker proxyChecker = context.getBean(ProxyChecker.class);
        for (String proxyString : proxyStrings) {
            Proxy proxy = new Proxy(proxyString);
            proxyChecker.checkProxy(proxy);
            repository.save(proxy);
        }
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Bean
    public ViewResolver resolver() {
        UrlBasedViewResolver url = new UrlBasedViewResolver();
        url.setPrefix("/");
        url.setViewClass(JstlView.class);
        url.setSuffix(".html");
        return url;
    }
}
