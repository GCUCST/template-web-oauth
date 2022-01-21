package cn.chenshaotong.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CornConfig {

  @Scheduled(cron = "0/10 * *  * * ? ")
  public synchronized void doSomething() {
    System.out.println("当前时间：" + System.currentTimeMillis());
  }
}
