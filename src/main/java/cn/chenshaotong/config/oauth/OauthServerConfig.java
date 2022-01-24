package cn.chenshaotong.config.oauth;

import cn.chenshaotong.service.SpringDataUserDetailsService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

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
  @Autowired SpringDataUserDetailsService springDataUserDetailsService;

  @Bean
  public AuthorizationCodeServices authorizationCodeServices() {
    return new InMemoryAuthorizationCodeServices();
  }

  @Bean
  public AuthorizationServerTokenServices tokenServices() {
    DefaultTokenServices services = new DefaultTokenServices();
    services.setClientDetailsService(clientDetailsService); // 客户端信息服务
    services.setSupportRefreshToken(false); // 产生刷新令牌
    services.setTokenStore(tokenStore); // 令牌存储方案
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
    services.setTokenEnhancer(tokenEnhancerChain);
    services.setAccessTokenValiditySeconds(7200); // 令牌默认有效期
    services.setRefreshTokenValiditySeconds(259200); // 刷新令牌有效期
    return services;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("c1")
        .secret(new BCryptPasswordEncoder().encode("123456"))
        .resourceIds("res1")
        .authorizedGrantTypes(
            "authorization_code",
            "password",
            "client_credentials",
            "implicit",
            "refresh_token",
            "phone_verify_code")
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
        .tokenGranter(tokenGranter(endpoints))
        .userDetailsService(springDataUserDetailsService)
        //            .tokenStore(tokenStore)

        .allowedTokenEndpointRequestMethods(HttpMethod.POST);
  }

  private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
    List<TokenGranter> list = new ArrayList<>();
    ResourceOwnerPasswordTokenGranter resourceOwnerPasswordTokenGranter =
        new ResourceOwnerPasswordTokenGranter(
            authenticationManager,
            tokenServices(),
            clientDetailsService,
            endpoints.getOAuth2RequestFactory());

    list.add(resourceOwnerPasswordTokenGranter);

    list.add(
        new RefreshTokenGranter(
            endpoints.getTokenServices(),
            clientDetailsService,
            endpoints.getOAuth2RequestFactory()));

    list.add(
        new AuthorizationCodeTokenGranter(
            endpoints.getTokenServices(),
            endpoints.getAuthorizationCodeServices(),
            clientDetailsService,
            endpoints.getOAuth2RequestFactory()));

    list.add(
        new ImplicitTokenGranter(
            endpoints.getTokenServices(),
            clientDetailsService,
            endpoints.getOAuth2RequestFactory()));

    // 添加自定义的手机验证码模式
    list.add(getPhoneVerifyCodeTokenGranter(endpoints));

    list.add(
        new ClientCredentialsTokenGranter(
            endpoints.getTokenServices(),
            clientDetailsService,
            endpoints.getOAuth2RequestFactory()));

    return new CompositeTokenGranter(list);
  }

  //  @Autowired
  //  PhoneVerifyCodeUserDetailsService phoneVerifyCodeUserDetailsService;

  private PhoneVerifyCodeTokenGranter getPhoneVerifyCodeTokenGranter(
      AuthorizationServerEndpointsConfigurer endpoints) {

    PhoneVerifyCodeDetailsAuthenticationProvider phoneVerifyCodeDetailsAuthenticationProvider =
        new PhoneVerifyCodeDetailsAuthenticationProvider();
    phoneVerifyCodeDetailsAuthenticationProvider.setUserDetailsService(
        new PhoneVerifyCodeUserDetailsService());
    phoneVerifyCodeDetailsAuthenticationProvider.setHideUserNotFoundExceptions(false);
    phoneVerifyCodeDetailsAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

    return new PhoneVerifyCodeTokenGranter(
        (Authentication authentication) ->
            phoneVerifyCodeDetailsAuthenticationProvider.authenticate(authentication),
        endpoints.getTokenServices(),
        clientDetailsService,
        endpoints.getOAuth2RequestFactory());
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("permitAll()")
        .allowFormAuthenticationForClients()
        .addTokenEndpointAuthenticationFilter(new MybeforeFileter());
  }
}
