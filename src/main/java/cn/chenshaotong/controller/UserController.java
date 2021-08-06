package cn.chenshaotong.controller;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/common")
public class UserController {

    @PostMapping("/get")
    public String getUserName(HttpServletRequest request) {
        System.out.println(request.getPathInfo());
        return "success";
    }

    @PostMapping("/date")
    public String testDate(@RequestParam(required = false)
                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime date) {
        return date.toString();
    }
}
