package cn.chenshaotong.controller;


import cn.chenshaotong.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cache")
public class CacheController {


    @PostMapping("/cst")
    @Cacheable(
            cacheNames = "chenst",
            key = "#id + '-' + #name",
            unless = "#result == null")
    public User getUserName(  @RequestParam String id,@RequestParam String name) {
        User cst = User.builder().name(name).password(id).belong(id).build();
        System.out.println("保存用户："+name);
        return cst;
    }

}
