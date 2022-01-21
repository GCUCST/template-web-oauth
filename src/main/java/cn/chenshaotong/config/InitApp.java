package cn.chenshaotong.config;

import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitApp {

  @Value("${custom.value}")
  private String value;

  //    @Autowired
  //    private RestTemplate restTemplate;

  @PostConstruct
  public void init() {
    System.err.println("初始化app....." + value);
  }

  // 相当于在外面包了一层try catch
  @SneakyThrows
  public void a() {
    b();
  }

  public void b() throws IOException {}
}
