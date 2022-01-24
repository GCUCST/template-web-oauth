package cn.chenshaotong.config.oauth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @Component
public class PhoneVerifyCodeUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String phoneNumber) {

    // 密码模式，需要通过一些配置

    // 獲取該用戶的密碼
    String password = searchPassword(phoneNumber);

    // 獲取該用戶的權限資料
    List<String> authorityStrList = searchAuthorities(phoneNumber);

    List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
    Optional.ofNullable(authorityStrList)
        .orElse(new ArrayList<>())
        .forEach(
            authorityStr -> {
              authorities.add(new SimpleGrantedAuthority(authorityStr));
            });

    return new User(phoneNumber, password, authorities);
  }

  public String searchPassword(String username) {

    // TODO: 2021/6/11  这里用来查询短信验证码
    return new BCryptPasswordEncoder().encode("123456");
  }

  public List<String> searchAuthorities(String username) {

    // TODO: 2021/6/11 查询权限
    List<String> authority = new ArrayList<String>();
    authority.add("authorityA");
    return new ArrayList<>();
  }
}
