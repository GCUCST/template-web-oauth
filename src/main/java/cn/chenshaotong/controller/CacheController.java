package cn.chenshaotong.controller;

import cn.chenshaotong.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

  // 51秒钟后失效
  @PostMapping("/cst")
  @Cacheable(cacheNames = "chenst#51", key = "#id + '-' + #name", unless = "#result == null")
  public User getUserName(@RequestParam String id, @RequestParam String name) {
    User cst = User.builder().name(name).password(id).belong(id).build();
    System.out.println("保存用户：" + name);
    return cst;
  }
}
