package cn.chenshaotong.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;


// 三大配置
// 1.根据oauth，客户端访问授权服务器需要有一个身份，即 客户端详情
// 2.令牌访问端点，即 token访问的地址和类型
// 3.令牌端点访问的安全约束
@EnableAuthorizationServer
@Configuration
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {


  @Autowired TokenStore tokenStore;
  @Autowired ClientDetailsService clientDetailsService;
  @Autowired AuthenticationManager authenticationManager;
  @Autowired JwtAccessTokenConverter accessTokenConverter;
  @Autowired AuthorizationCodeServices authorizationCodeServices;

  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {
    return new InMemoryAuthorizationCodeServices();
  }

  @Bean
  public AuthorizationServerTokenServices tokenServices() {
    DefaultTokenServices services = new DefaultTokenServices();
    services.setClientDetailsService(clientDetailsService); //客户端信息服务
    services.setSupportRefreshToken(false); //产生刷新令牌
    services.setTokenStore(tokenStore); //令牌存储方案
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
    services.setTokenEnhancer(tokenEnhancerChain);
    services.setAccessTokenValiditySeconds(7200);//令牌默认有效期
    services.setRefreshTokenValiditySeconds(259200);//刷新令牌有效期
    return services;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("c1")
        .secret(new BCryptPasswordEncoder().encode("secret"))
        .resourceIds("res1")
        .authorizedGrantTypes(
            "authorization_code", "password", "client_credentials", "implicit", "refresh_token")
        .scopes("all")
        .autoApprove(false) // 会跳转到授权页面
        .redirectUris("http://www.baidu.com");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .authenticationManager(authenticationManager)
        .authorizationCodeServices(authorizationCodeServices)
        .tokenServices(tokenServices())
        .allowedTokenEndpointRequestMethods(HttpMethod.POST);
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("permitAll()")
        .allowFormAuthenticationForClients();
  }








}
