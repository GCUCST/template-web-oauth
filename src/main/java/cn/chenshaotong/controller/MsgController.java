package cn.chenshaotong.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class MsgController {
    @PostMapping("/get")
    @PreAuthorize("hasAuthority('p1')")
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "success";
    }

    @PostMapping("/get2")
    public String getUserName2() {
        return "success2";

    }
}
