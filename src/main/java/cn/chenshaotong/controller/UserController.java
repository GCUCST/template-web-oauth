package cn.chenshaotong.controller;

import cn.chenshaotong.listener.event.UserEvent;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/common")
public class UserController {

  @GetMapping("/get")
  public String getUserName(HttpServletRequest request) {
    System.out.println(request.getPathInfo());
    return "success";
  }

  @PostMapping("/date")
  public String testDate(
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
          LocalDateTime date) {
    return date.toString();
  }

  @PostMapping("/valid")
  public String valid(@NotNull String data) {
    System.out.println(data);
    return "success";
  }

  @Autowired private ApplicationContext applicationContext;

  @PostMapping("/event")
  public String send(String data) {
    applicationContext.publishEvent(new UserEvent(applicationContext, "你好"));
    return "success";
  }
}
