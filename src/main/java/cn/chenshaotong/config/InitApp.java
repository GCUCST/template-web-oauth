package cn.chenshaotong.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

@Configuration
public class InitApp {

    @Value("${custom.value}")
    private String value;

//    @Autowired
//    private RestTemplate restTemplate;

    @PostConstruct
    public void init(){
        System.err.println("初始化app....."+value);
    }

    //相当于在外面包了一层try catch
    @SneakyThrows
    public  void a(){
        b();
    }

    public  void b() throws IOException {

    }

}
