package cn.chenshaotong.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {
  @PostMapping("/upload")
  public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

    return "success";
  }
}
