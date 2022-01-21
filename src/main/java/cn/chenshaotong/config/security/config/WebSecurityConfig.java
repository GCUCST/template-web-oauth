package cn.chenshaotong.config.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
// @EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  //  @Autowired public UserDetailsService springDataUserDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  //
  //  @Override
  //  public void configure(WebSecurity web) throws Exception {
  //    super.configure(web);
  //  }

  // 认证用户的来源
  //  @Override
  //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //    //        auth.inMemoryAuthentication()
  //    //                .withUser("cst")
  //    //                .password("{noop}123")//{noop}代表原文
  //    //                .roles("ADMIN");
  //    auth.userDetailsService(springDataUserDetailsService).passwordEncoder(passwordEncoder());
  //  }

  // 相关信息，拦截规则
  //    @Override
  //    protected void configure(HttpSecurity http) throws Exception {
  //        http.rememberMe();
  //        http.csrf().disable();
  ////        http.formLogin().loginPage("/my-login-page").loginProcessingUrl(); //定制登录页面
  //        http.formLogin();
  //        http.logout().logoutSuccessUrl("http://www.baidu.com");
  //        http
  //                .authorizeRequests()
  //                .antMatchers("/get/guest").permitAll() // 匹配到就放行
  //                .antMatchers("/get/admin").hasAnyRole("admin");
  //    }

  //  认证管理器
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
