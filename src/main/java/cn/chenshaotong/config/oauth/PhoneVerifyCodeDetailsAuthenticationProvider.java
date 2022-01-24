package cn.chenshaotong.config.oauth;

import java.util.Map;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

public class PhoneVerifyCodeDetailsAuthenticationProvider
    extends AbstractUserDetailsAuthenticationProvider {

  private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

  private PasswordEncoder passwordEncoder;

  private volatile String userNotFoundEncodedPassword;

  private UserDetailsService userDetailsService;

  private UserDetailsPasswordService userDetailsPasswordService;

  public PhoneVerifyCodeDetailsAuthenticationProvider() {
    setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
  }

  protected void additionalAuthenticationChecks(
      UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    if (authentication.getCredentials() == null) {
      logger.debug("Authentication failed: no credentials provided");

      throw new BadCredentialsException(
          messages.getMessage(
              "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }

    String presentedPassword = authentication.getCredentials().toString();

    if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
      logger.debug("Authentication failed: password does not match stored value");

      throw new BadCredentialsException(
          messages.getMessage(
              "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
  }

  protected void doAfterPropertiesSet() {
    Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
  }

  protected final UserDetails retrieveUser(
      String username, UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    prepareTimingAttackProtection();

    try {
      Map<String, String> map = (Map<String, String>) authentication.getDetails();
      String phone = map.get("phone");

      // 传入手机号，代替用户名
      UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(phone);

      if (loadedUser == null) {
        throw new InternalAuthenticationServiceException(
            "UserDetailsService returned null, which is an interface contract violation");
      }
      return loadedUser;
    } catch (UsernameNotFoundException ex) {
      mitigateAgainstTimingAttack(authentication);
      throw ex;
    } catch (InternalAuthenticationServiceException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
    }
  }

  @Override
  protected Authentication createSuccessAuthentication(
      Object principal, Authentication authentication, UserDetails user) {
    boolean upgradeEncoding =
        this.userDetailsPasswordService != null
            && this.passwordEncoder.upgradeEncoding(user.getPassword());
    if (upgradeEncoding) {
      String presentedPassword = authentication.getCredentials().toString();
      String newPassword = this.passwordEncoder.encode(presentedPassword);
      user = this.userDetailsPasswordService.updatePassword(user, newPassword);
    }

    return super.createSuccessAuthentication(principal, authentication, user);
  }

  private void prepareTimingAttackProtection() {
    if (this.userNotFoundEncodedPassword == null) {
      this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
    }
  }

  private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
    if (authentication.getCredentials() != null) {
      String presentedPassword = authentication.getCredentials().toString();
      this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
    }
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
    this.passwordEncoder = passwordEncoder;
    this.userNotFoundEncodedPassword = null;
  }

  protected PasswordEncoder getPasswordEncoder() {
    return passwordEncoder;
  }

  public void setUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  protected UserDetailsService getUserDetailsService() {
    return userDetailsService;
  }

  public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
    this.userDetailsPasswordService = userDetailsPasswordService;
  }
}
