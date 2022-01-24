package cn.chenshaotong.config.oauth;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

public class PhoneVerifyCodeTokenGranter extends AbstractTokenGranter {

  private static final String GRANT_TYPE = "phone_verify_code";

  private static AuthenticationManager authenticationManager;

  public PhoneVerifyCodeTokenGranter(
      AuthenticationManager authenticationManager,
      AuthorizationServerTokenServices tokenServices,
      ClientDetailsService clientDetailsService,
      OAuth2RequestFactory requestFactory) {
    this(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    this.authenticationManager = authenticationManager;
  }

  protected PhoneVerifyCodeTokenGranter(
      AuthorizationServerTokenServices tokenServices,
      ClientDetailsService clientDetailsService,
      OAuth2RequestFactory requestFactory,
      String grantType) {
    super(tokenServices, clientDetailsService, requestFactory, grantType);
  }

  @Override
  protected OAuth2Authentication getOAuth2Authentication(
      ClientDetails client, TokenRequest tokenRequest) {

    Map<String, String> parameters =
        new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
    String phone = parameters.get("phone"); // 这里接收手机号
    String verifyCode = parameters.get("verifyCode"); // 这里接收验证码
    // Protect from downstream leaks of password
    parameters.remove("verifyCode");

    Authentication userAuth = new UsernamePasswordAuthenticationToken(phone, verifyCode);
    ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
    try {
      userAuth = authenticationManager.authenticate(userAuth);
    } catch (AccountStatusException ase) {
      // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
      throw new InvalidGrantException(ase.getMessage());
    } catch (BadCredentialsException e) {
      // If the username/password are wrong the spec says we should send 400/invalid grant
      throw new InvalidGrantException(e.getMessage());
    }
    if (userAuth == null || !userAuth.isAuthenticated()) {
      throw new InvalidGrantException("Could not authenticate user: " + phone);
    }

    OAuth2Request storedOAuth2Request =
        getRequestFactory().createOAuth2Request(client, tokenRequest);
    return new OAuth2Authentication(storedOAuth2Request, userAuth);
  }
}
