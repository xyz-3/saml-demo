package mujina.idp;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import mujina.api.IdpConfiguration;
import mujina.dao.UserDao;
import mujina.dto.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static mujina.api.AuthenticationMethod.ALL;

public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final UserDao userDao;

    private final IdpConfiguration idpConfiguration;

    private final GoogleAuthenticator gAuth;

    public AuthenticationProvider(UserDao userDao, IdpConfiguration idpConfiguration, GoogleAuthenticator gAuth) {
        this.userDao = userDao;
        this.idpConfiguration = idpConfiguration;
        this.gAuth = gAuth;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (StringUtils.isEmpty(authentication.getPrincipal())) {
            throw new InvalidAuthenticationException("Principal may not be empty");
        }
        if (idpConfiguration.getAuthenticationMethod().equals(ALL)) {
            return new FederatedUserAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER")));
        } else {
            UserDto user = userDao.getUser(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
            // 1FA 用户名密码认证
            if (user == null) {
                throw new InvalidAuthenticationException("User not found or bad credentials");
            }
            // Check if this user has access to this Application
            Boolean check_res = userDao.checkUserAppAccess(user.getId(), idpConfiguration.getIssuer());
            if (!check_res) {
                throw new InvalidAuthenticationException("User does not have access to this Application");
            }
            if (user.getMfaEnabled()){
                // 2FA Google Authenticator Code认证
                Object details = authentication.getDetails();
                if (details instanceof HashMap) {
                    HashMap<String, Object> detailsMap = (HashMap<String, Object>) details;
                    Object code = detailsMap.get("totpcode");
                    boolean res = false;
                    if(code instanceof String[]){
                        String[] code_after = (String[]) code;
                        String totpcode = code_after[0];
                        res = gAuth.authorizeUser(user.getName(), Integer.parseInt(totpcode));
                    }
                    if (!res) {
                        throw new InvalidAuthenticationException("Google Authenticator Code is invalid");
                    }
                }
            }
            List<GrantedAuthority> authorities = user.getAuthorities()
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new FederatedUserAuthenticationToken(
                    authentication.getPrincipal(),
                    authentication.getCredentials(),
                    authorities);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
