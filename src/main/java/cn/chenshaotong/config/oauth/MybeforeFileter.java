package cn.chenshaotong.config.oauth;

import java.io.IOException;
import javax.servlet.*;

public class MybeforeFileter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    System.out.println("测试。。。。。");
    chain.doFilter(request,response);
  }
}
